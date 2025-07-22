系统地梳理一下 Lombok 的注解全家桶。按照功能将它们分门别类，并从最常用、最重要的开始，逐步深入到更专门的工具。

---

### Lombok 注解分类导航

我将所有注解分为五大类，这基本上覆盖了日常开发的所有场景：

1.  **核心“瑞士军刀”类**：用一个注解干多个注解的活，快速搞定一个类。
2.  **POJO基础组件类**：精细化控制 `getter`, `setter`, `toString`, 构造函数等。
3.  **构建器模式类**：优雅地创建复杂对象。
4.  **日志与异常处理类**：简化日志和异常代码。
5.  **实用工具与其他**：一些“黑科技”和代码风格优化工具。

---

### 1. 核心“瑞士军刀”类 (The "Swiss Army Knife" Annotations)

这类注解是复合注解，一个顶好几个。适合快速开发，但有时需要注意其“副作用”。

| 注解 | 一句话概括 |
| :--- | :--- |
| **`@Data`** | **最强王者，但慎用。** 集合了 `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, `@RequiredArgsConstructor`。 |
| **`@Value`** | **不可变版的 `@Data`。** 集合了 `@Getter`, `@ToString`, `@EqualsAndHashCode`, `@AllArgsConstructor`，并将所有字段设为 `private final`。 |
| **`@Cleanup`** | **自动的资源关闭器。** 类似于 `try-with-resources` 语句。 |

#### 详解与用法

*   **`@Data`**:
    *   **用途**：用于普通的 JavaBean 或 POJO，快速生成所有模板代码。
    *   **代码示例**：
        ```java
        @Data
        public class User {
            private Long id;
            private String username;
            private final String userType; // 会被 @RequiredArgsConstructor 包含
        }
        ```
    *   **最佳实践/注意事项**：
        1.  **JPA 实体类中绝对不要用**：`@EqualsAndHashCode` 可能会因为懒加载和代理对象导致严重问题。
        2.  **有继承关系时不要用**：`@EqualsAndHashCode` 无法正确处理父类字段。应手动添加所需注解并使用 `@EqualsAndHashCode(callSuper=true)`。
        3.  **有双向关联时慎用**：`@ToString` 可能会导致 `StackOverflowError`。需要手动使用 `@ToString(exclude="...")` 排除另一方。
        4.  **它是可变的**：因为它包含了 `@Setter`，不适合做 DTO 或需要保证状态不变的场景。

*   **`@Value`**:
    *   **用途**：创建**不可变**对象（Immutable Object），非常适合用作 DTOs 或配置类。
    *   **代码示例**：
        ```java
        @Value
        public class UserProfile {
            String name; // 自动变为 private final
            int age;     // 自动变为 private final
        }
        ```
    *   **最佳实践/注意事项**：因为所有字段都是 `final` 且没有 `setter`，对象一旦创建就无法修改，线程安全。这是 `@Data` 的一个绝佳替代品。

*   **`@Cleanup`**:
    *   **用途**：自动调用资源的 `close()` 方法。
    *   **代码示例**：
        ```java
        public void copyFile(String in, String out) throws IOException {
            @Cleanup InputStream input = new FileInputStream(in);
            @Cleanup OutputStream output = new FileOutputStream(out);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        }
        ```
    *   **最佳实践/注意事项**：在 Java 7 引入 `try-with-resources` 后，其使用场景减少。但它对于那些没有实现 `AutoCloseable` 接口但有 `close()` 方法的旧版库依然有用。

---

### 2. POJO 基础组件类 (Basic POJO Components)

这些是 `@Data` 等注解的“组成零件”，可以按需取用，实现更精细的控制。

| 注解 | 一句话概括 |
| :--- | :--- |
| **`@Getter` / `@Setter`** | 生成 getter 和 setter 方法。 |
| **`@ToString`** | 生成 `toString()` 方法。 |
| **`@EqualsAndHashCode`** | 生成 `equals()` 和 `hashCode()` 方法。 |
| **`@NoArgsConstructor`** | 生成无参构造函数。 |
| **`@RequiredArgsConstructor`** | 生成包含 `final` 和 `@NonNull` 字段的构造函数。 |
| **`@AllArgsConstructor`** | 生成包含所有字段的构造函数。 |

#### 详解与用法

*   **`@Getter` / `@Setter`**:
    *   **高级用法**：可以指定访问级别 `AccessLevel.PROTECTED`。可以放在类上对所有字段生效，也可以放在单个字段上。

*   **`@ToString`**:
    *   **高级用法**：`@ToString(exclude = {"password"})` 排除敏感字段。`@ToString(of = {"id", "name"})` 只包含指定字段。`@ToString(callSuper = true)` 在 `toString` 中包含父类信息。

*   **`@EqualsAndHashCode`**:
    *   **高级用法**：与 `@ToString` 类似，有 `exclude` 和 `of` 参数。`callSuper = true` 也同样适用。
    *   **关键实践**：在 JPA 实体中，如果你非要重写，请使用一个稳定的业务主键，例如 `@EqualsAndHashCode(of = "uuid")`，而不是数据库自增的 ID。

*   **构造函数三兄弟**:
    *   **`@NoArgsConstructor`**: 很多框架（如 JPA, Jackson）需要无参构造函数来进行实例化。
    *   **`@RequiredArgsConstructor`**: 非常适合用于依赖注入（`final` 字段），实现构造器注入。
    *   **`@AllArgsConstructor`**: 当你需要一个能设置所有属性的“全能”构造函数时使用。

---

### 3. 构建器模式类 (Builder Pattern)

| 注解 | 一句话概括 |
| :--- | :--- |
| **`@Builder`** | 为类实现经典的**构建器模式**。 |
| **`@SuperBuilder`** | **支持继承**的构建器模式。 |

#### 详解与用法

*   **`@Builder`**:
    *   **用途**：当一个类有多个字段，特别是可选字段很多时，避免使用冗长的构造函数。
    *   **高级用法**：
        *   `@Builder(toBuilder = true)`: 生成一个 `toBuilder()` 方法，可以基于现有对象创建新的 Builder，用于“克隆并修改”。
        *   `@Builder.Default`: 为 Builder 中的某个字段设置默认值。

*   **`@SuperBuilder`**:
    *   **用途**：当你的类有继承关系时，必须用它替代 `@Builder`。
    *   **使用方法**：**父类和所有子类都必须标注** `@SuperBuilder`。
    *   **最佳实践**：使用 `@SuperBuilder` 时，通常手动添加 `@Getter`, `@ToString` 等注解，而不是用 `@Data`，以避免冲突。

---

### 4. 日志与异常处理类 (Logging & Exception Handling)

| 注解 | 一句话概括 |
| :--- | :--- |
| **`@Slf4j`** (及同类) | 自动注入一个**日志记录器**实例。 |
| **`@SneakyThrows`** | “欺骗”编译器，让你**不用显式 `try-catch` 受检异常**。 |

#### 详解与用法

*   **`@Slf4j`**:
    *   **用途**：省去手写 `private static final Logger log = LoggerFactory.getLogger(MyClass.class);` 这行样板代码。
    *   **代码示例**：
        ```java
        @Slf4j
        public class OrderService {
            public void placeOrder(Order order) {
                log.info("Placing a new order: {}", order.getId());
                // ...
            }
        }
        ```
    *   **同类注解**：`@Log`, `@Log4j`, `@Log4j2`, `@JBossLog` 等，根据你项目使用的日志框架选择。`@Slf4j` 是最通用的。

*   **`@SneakyThrows`**:
    *   **用途**：当你确定某个受检异常（Checked Exception）不可能发生，或者你想把它抛给上层统一处理时使用。
    *   **代码示例**：
        ```java
        @SneakyThrows(UnsupportedEncodingException.class)
        public String utf8ToString(byte[] bytes) {
            return new String(bytes, "UTF-8"); // "UTF-8" 是标准编码，永远不会抛异常
        }
        ```
    *   **最佳实践/注意事项**：**这是一个“危险”的注解，请务必谨慎使用！** 滥用它会破坏 Java 的异常检查机制。只在非常有把握的情况下使用。

---

### 5. 实用工具与其他 (Utilities & Others)

| 注解 | 一句话概括 |
| :--- | :--- |
| **`@NonNull`** | 自动**检查参数或字段是否为 null**，如果是则抛 `NullPointerException`。 |
| **`@With`** | 为 `final` 字段生成一个 `withXxx()` 方法，返回一个**带新值的克隆对象**。 |
| **`@Accessors`** | **改变 getter/setter 的行为**，支持链式调用。 |
| **`@Getter(lazy=true)`** | **懒加载**。当第一次调用 getter 时才计算并缓存结果。 |

#### 详解与用法

*   **`@NonNull`**:
    *   可以放在方法参数、构造函数参数或字段上。放在字段上时，会在 `@RequiredArgsConstructor` 和 `@Setter` 中添加 null 检查。

*   **`@With`**:
    *   **用途**：不可变对象的“部分修改”利器。
    *   **代码示例**：
        ```java
        @With @AllArgsConstructor
        public class User {
            private final String name;
            private final int age;
        }
        User user1 = new User("Tom", 20);
        User user2 = user1.withAge(21); // user1 没变，user2 是一个新对象
        ```

*   **`@Accessors`**:
    *   **`chain = true`**: setter 返回 `this`，可以链式调用：`user.setName("a").setAge(10);`。
    *   **`fluent = true`**: getter 和 setter 的名字都变成字段名，即 `user.name()` 和 `user.name("a")`。
    *   **`@Data` + `@Accessors(chain = true)` 是一个非常流行的组合。**

*   **`@Getter(lazy=true)`**:
    *   **用途**：用于那些计算成本很高，但又不是每次都需要的字段。
    *   **代码示例**：
        ```java
        public class HeavyResource {
            @Getter(lazy = true)
            private final double[] cachedData = calculateExpensiveData();

            private double[] calculateExpensiveData() {
                // ... 模拟耗时计算
                System.out.println("Calculating expensive data...");
                return new double[1_000_000];
            }
        }
        ```
    *   `cachedData` 字段只会在第一次调用 `getCachedData()` 时被计算和赋值。

---

### 如何选择？—— 实践中的决策树

1.  **这个类需要继承别的类，或者将来可能被继承吗？**
    *   **是** -> 使用 **`@SuperBuilder`**，并手动添加 `@Getter`, `@ToString` 等。**绝对不要用 `@Data`**。
    *   **否** -> 进入下一步。

2.  **这个类需要是不可变的吗（比如 DTO）？**
    *   **是** -> 使用 **`@Value`**。如果需要“修改”功能，可以考虑结合 **`@With`** 或 **`@Builder(toBuilder=true)`**。
    *   **否** -> 进入下一步。

3.  **这个类是 JPA/Hibernate 实体吗？**
    *   **是** -> **不要用 `@Data`**。手动组合 **`@Getter`**, **`@Setter`**, **`@ToString`**（注意 `exclude` 关联字段），**`@NoArgsConstructor`**, **`@AllArgsConstructor`**。**对 `@EqualsAndHashCode` 保持极度警惕**。
    *   **否** -> 进入下一步。

4.  **这是一个简单的、临时的、没有复杂关联的 JavaBean 吗？**
    *   **是** -> 可以安全地使用 **`@Data`**。如果想用链式调用，可以加上 **`@Accessors(chain = true)`**。
    *   **否** -> 老老实实地手动组合 **`@Getter`, `@Setter`, `@ToString`, 构造函数三兄弟** 等基础组件，实现最精准的控制。

最后，记得在项目中加入 `lombok.config` 文件来统一团队的编码风格，例如 `lombok.addConstructorProperties=true`，确保所有 IDE 和构建工具生成的代码都是一致的。