`@Transactional` 注解虽然强大方便，但在某些情况下确实会失效，导致事务没有按预期工作（比如该回滚的没回滚）。理解这些场景并知道如何解决，对于写出健壮的应用至关重要。

以下是 `@Transactional` 失效的常见场景及其解决方法：

---

### 1. 方法的访问权限问题

*   **场景描述**：`@Transactional` 注解应用在了 `private`、`protected` 或包级私有（default）方法上。
*   **原因**：Spring 的事务管理是基于 AOP 代理实现的。
    *   如果使用 **JDK 动态代理**（默认当目标类实现接口时），它只能代理接口中定义的方法。如果你在实现类的非接口方法或非 `public` 方法上加 `@Transactional`，代理无法拦截。
    *   如果使用 **CGLIB 代理**（默认当目标类没有实现接口时，或者配置了 `proxy-target-class="true"`），它通过继承目标类来创建代理。理论上 CGLIB 可以代理 `protected` 和包级私有方法，但 Spring AOP 的约定和最佳实践通常是针对 `public` 方法的。Spring 框架的设计者通常建议将事务边界放在 `public` 方法上，因为这些方法代表了服务的公开契约。
*   **解决方法**：
    *   **将该方法改为 `public`。** 这是最直接且推荐的做法。

---

### 2. 方法被 `final` 或 `static` 修饰

*   **场景描述**：`@Transactional` 注解应用在了 `final` 或 `static` 方法上。
*   **原因**：
    *   **`final` 方法**：CGLIB 代理是通过子类化目标类并重写其方法来实现的。`final` 方法不能被子类重写，因此 CGLIB 无法代理 `final` 方法。
    *   **`static` 方法**：静态方法属于类而不是对象实例，AOP 代理是基于对象实例的，因此无法代理静态方法。
*   **解决方法**：
    *   **移除 `final` 修饰符**。
    *   **将 `static` 方法改为实例方法**，并通过 Spring bean 的方式调用。

---

### 3. 同一个类中的方法调用（自调用问题 / this 调用）

*   **场景描述**：一个没有 `@Transactional` 注解的方法 A，调用了同一个类中被 `@Transactional` 注解修饰的方法 B（例如 `this.methodB()`）。
    ```java
    @Service
    public class MyService {
        public void methodA() {
            this.methodB(); // 事务可能不会生效
        }

        @Transactional
        public void methodB() {
            // ... 数据库操作 ...
        }
    }
    ```
*   **原因**：Spring AOP 代理的调用拦截是发生在**外部调用**时。当 `methodA` 调用 `this.methodB()` 时，它直接调用的是原始对象（`this`）的 `methodB`，而不是代理对象的 `methodB`。因此，AOP 增强（包括事务）不会被应用。
*   **解决方法**：
    1.  **注入自身代理对象**：
        ```java
        @Service
        public class MyService {
            @Autowired // 注入自身的代理对象
            private MyService self; // 或者使用 ApplicationContext.getBean(MyService.class)

            public void methodA() {
                self.methodB(); // 通过代理对象调用，事务生效
            }

            @Transactional
            public void methodB() {
                // ... 数据库操作 ...
            }
        }
        ```
        注意：默认情况下，Spring 不允许循环依赖（A 依赖 B，B 依赖 A）。对于构造器注入的循环依赖，Spring Boot 2.6+ 默认禁止。对于字段注入或 setter 注入的自身代理，通常可以工作，但需谨慎。可以通过 `@Lazy` 注解辅助解决某些循环依赖问题。

    2.  **使用 `AopContext.currentProxy()`**：
        需要添加 `spring-boot-starter-aop` 依赖，并在启动类或配置类上添加 `@EnableAspectJAutoProxy(exposeProxy = true)`。
        ```java
        import org.springframework.aop.framework.AopContext;
        // ...
        @Service
        public class MyService {
            public void methodA() {
                ((MyService) AopContext.currentProxy()).methodB(); // 事务生效
            }
            @Transactional
            public void methodB() {
                // ... 数据库操作 ...
            }
        }
        ```
        这种方式耦合性较高，一般不推荐首选。

    3.  **将事务方法移到另一个 Bean 中**：
        这是最清晰、最推荐的解耦方式。
        ```java
        @Service
        public class ServiceA {
            @Autowired
            private ServiceB serviceB;

            public void methodA() {
                serviceB.methodB(); // 调用另一个 Bean 的方法，事务生效
            }
        }

        @Service
        public class ServiceB {
            @Transactional
            public void methodB() {
                // ... 数据库操作 ...
            }
        }
        ```
    4.  **将 `@Transactional` 注解也加到调用方法 `methodA` 上**：如果业务逻辑允许，可以将事务边界扩大到 `methodA`。

---

### 4. 异常被 `try-catch` 捕获且没有正确处理

*   **场景描述**：在 `@Transactional` 方法内部，抛出的异常（尤其是 `RuntimeException`）被 `try-catch` 块捕获了，并且在 `catch` 块中没有重新抛出该异常或手动设置事务回滚。
    ```java
    @Transactional
    public void problematicTransaction() {
        try {
            // ... 数据库操作1 ...
            if (true) { // 假设这里条件成立，需要回滚
                throw new RuntimeException("Something went wrong!");
            }
            // ... 数据库操作2 ...
        } catch (RuntimeException e) {
            // 异常被捕获了，但没有重新抛出，也没有手动设置回滚
            System.err.println("Caught exception: " + e.getMessage());
            // 事务会默认提交，因为 Spring 认为异常已经被处理了
        }
    }
    ```
*   **原因**：Spring 默认只在捕获到未处理的 `RuntimeException` 或 `Error` 时才会回滚事务。如果异常被捕获并且没有再次抛出，Spring 会认为异常已经被业务逻辑处理了，从而继续提交事务。
*   **解决方法**：
    1.  **在 `catch` 块中重新抛出 `RuntimeException`（或其子类）**：
        ```java
        catch (RuntimeException e) {
            System.err.println("Caught exception: " + e.getMessage());
            throw e; // 或 throw new MyBusinessRuntimeException("封装后的异常", e);
        }
        ```
    2.  **在 `catch` 块中手动设置事务回滚**：
        ```java
        import org.springframework.transaction.interceptor.TransactionAspectSupport;
        // ...
        catch (RuntimeException e) {
            System.err.println("Caught exception: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        ```

---

### 5. 抛出的异常类型不符合默认回滚规则

*   **场景描述**：`@Transactional` 方法抛出的是一个 `Checked Exception` (受检异常，即非 `RuntimeException` 或 `Error` 的子类)，并且没有通过 `rollbackFor` 属性指定该异常类型。
    ```java
    @Transactional
    public void transactionWithCheckedException() throws IOException {
        // ... 数据库操作 ...
        if (true) {
            throw new IOException("A checked exception occurred!"); // 默认不会回滚
        }
    }
    ```
*   **原因**：Spring 的默认事务回滚策略是：遇到 `RuntimeException` 或 `Error` 时回滚，遇到 `Checked Exception` 时不回滚。
*   **解决方法**：
    *   使用 `@Transactional(rollbackFor = YourCheckedException.class)` 或 `@Transactional(rollbackForClassName = "your.package.YourCheckedException")` 来指定需要回滚的受检异常类型。
        ```java
        @Transactional(rollbackFor = IOException.class)
        public void transactionWithCheckedException() throws IOException { ... }
        ```
    *   将受检异常包装成运行时异常再抛出。

---

### 6. 数据库引擎不支持事务

*   **场景描述**：使用的数据库表引擎不支持事务（例如 MySQL 的 MyISAM 引擎）。
*   **原因**：`@Transactional` 最终依赖数据库层面实现事务的 ACID 特性。如果数据库引擎本身不支持事务，Spring 的事务管理也无能为力。
*   **解决方法**：
    *   **更换支持事务的数据库引擎** (例如 MySQL 的 InnoDB 引擎)。
    *   对于 MySQL，可以通过 `ALTER TABLE your_table ENGINE = InnoDB;` 修改表引擎。

---

### 7. `@Transactional` 注解所在的类没有被 Spring 容器管理

*   **场景描述**：包含 `@Transactional` 方法的类是通过 `new MyService()` 方式创建的，而不是通过 Spring IoC 容器注入的。
*   **原因**：只有 Spring管理的 Bean，Spring 才会为其创建代理对象，从而使 `@Transactional` 生效。
*   **解决方法**：
    *   **确保该类被 Spring 扫描并注册为 Bean** (例如使用 `@Service`, `@Component` 等注解)。
    *   **通过 `@Autowired` 或构造函数注入来使用该 Bean**。

---

### 8. 事务传播行为（Propagation）设置不当

*   **场景描述**：对事务传播行为的理解和使用不当，导致事务未按预期开启、加入或挂起。
    例如，你期望一个方法总是开启新事务，但它被一个已有事务的方法调用，且传播行为是默认的 `REQUIRED`，那么它会加入现有事务，而不是开启新事务。
    或者，方法 A 调用方法 B，方法 B 的传播行为是 `NOT_SUPPORTED`，那么即使方法 A 有事务，方法 B 的操作也不会在事务中执行。
*   **原因**：事务传播行为决定了事务方法在被另一个事务方法调用时的行为方式。
*   **解决方法**：
    *   **仔细选择合适的事务传播行为**。`REQUIRED` 是最常用的。如果需要独立事务，使用 `REQUIRES_NEW`。
    *   理解每个传播行为的含义和影响。

---

### 9. `@EnableTransactionManagement` 未配置 (Spring Boot 中通常自动配置)

*   **场景描述**：如果完全不使用 Spring Boot 的自动配置，而是手动配置 Spring 环境，可能会忘记添加 `@EnableTransactionManagement` 注解。
*   **原因**：`@EnableTransactionManagement` 负责启用 Spring 的注解驱动事务管理功能，它会扫描 `@Transactional` 注解并应用事务 AOP。
*   **解决方法**：
    *   在 Spring Boot 应用中，只要引入了 `spring-boot-starter-data-jpa` 或 `spring-boot-starter-jdbc` 等依赖，`TransactionAutoConfiguration` 通常会自动启用事务管理，所以这个问题在 Spring Boot 中较少见。
    *   如果是纯 Spring 项目或自定义配置，确保在配置类上添加 `@EnableTransactionManagement`。

---

### 调试事务不生效的通用建议：

1.  **检查日志**：将 Spring 事务相关的日志级别（如 `org.springframework.transaction` 和 `org.springframework.orm.jpa` 或 `org.springframework.jdbc.datasource`）设置为 `DEBUG` 或 `TRACE`，观察事务的创建、提交、回滚日志。
    ```
    logging.level.org.springframework.transaction=TRACE
    logging.level.org.springframework.orm.jpa=DEBUG // (for JPA)
    logging.level.org.springframework.jdbc.datasource.DataSourceTransactionManager=DEBUG // (for JDBC)
    ```
2.  **断点调试**：在 `@Transactional` 方法的入口和出口打断点，查看当前线程绑定的事务状态，以及调用的对象是否是代理对象。
3.  **简化问题**：创建一个最小的可复现问题的示例代码，排除其他复杂因素的干扰。