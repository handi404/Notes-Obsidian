**建造者模式（Builder Pattern）**。这是一种在 Spring Boot 和整个 Java 生态中极其常用且重要的设计模式。

---

### 1. 核心概念

建造者模式是一种**对象创建型模式**。它的核心思想是：**将一个复杂对象的构建过程与其最终的表示（Representation）分离，使得同样的构建过程可以创建出不同的表示。**

**通俗讲解：**
想象一下你去赛百味点三明治。你不会直接跟店员说“我要一个包含烤鸡肉、全麦面包、番茄、生菜、不要洋葱、加西南酱的三明治”。这个指令太长、太复杂、容易出错。

你会这样做：
1.  **选择面包类型**（构建过程第一步）
2.  **选择主料**（第二步）
3.  **选择蔬菜**（第三步）
4.  **选择酱料**（第四步）
5.  最后店员对你说：“好了！”，然后递给你最终的三明治（获取最终表示）。

这个点餐过程就是建造者模式。店员（`Builder`）通过一系列独立、清晰的步骤，最终为你构建出复杂的三明治（`Product`）。你可以用同样的过程（同样的店员），通过改变每一步的选择，构建出完全不同的三明治。

**它主要解决的问题：**
*   当一个类的构造函数参数过多时（通常超过 4 个），代码可读性急剧下降。
*   当某些参数是可选的，你可能需要编写多个重载的构造函数，这很繁琐。
*   希望能创建一个**不可变（Immutable）** 的对象，一旦创建，其状态就不能被修改。

---

### 2. 实现原理与基础应用

建造者模式通常包含两个核心角色：
*   **Product（产品）**：最终要构建的复杂对象。
*   **Builder（建造者）**：负责一步一步构建 Product，并包含一个最终返回 Product 的方法。

通常，`Builder` 会作为 `Product` 的一个**静态内部类**。

**代码示例（经典实现）：**

假设我们要创建一个 `User` 对象，它有必选的 `id` 和 `username`，以及可选的 `email` 和 `nickname`。

```java
// 1. Product - 最终要构建的 User 对象
public final class User { // 使用 final 确保不可变
    private final Long id;          // 必选
    private final String username;  // 必选
    private final String email;     // 可选
    private final String nickname;  // 可选

    // 2. 私有构造函数，只能被 Builder 调用
    private User(UserBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.nickname = builder.nickname;
    }

    // 3. 提供静态方法来获取 Builder 实例
    public static UserBuilder builder(Long id, String username) {
        return new UserBuilder(id, username);
    }
    
    // 省略 getters...

    // 4. Builder - 作为 User 的静态内部类
    public static class UserBuilder {
        private final Long id;
        private final String username;
        private String email;
        private String nickname;

        // Builder 的构造函数只接收必选参数
        public UserBuilder(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        // 5. 为可选参数提供 "链式调用" 方法，返回 this
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        // 6. 最终的 build() 方法，创建并返回 Product 实例
        public User build() {
            return new User(this);
        }
    }
}
```

**如何使用：**

```java
// 创建一个只有必选参数的 User
User user1 = User.builder(1L, "admin").build();

// 创建一个包含所有参数的 User，注意看链式调用的可读性
User user2 = User.builder(2L, "zhangsan")
                 .email("zhangsan@example.com")
                 .nickname("San")
                 .build();
```

这种写法清晰、易读，且由于 `User` 类没有提供 `setter` 方法且字段是 `final` 的，所以创建出的 `User` 对象是**不可变的**，这在多线程环境下尤其重要。

---

### 3. 进阶探讨：在 Spring Boot 中的应用

手动编写 Builder 模式的代码有些繁琐。在现代 Spring Boot 开发中，我们通常会借助 **Lombok** 来自动生成这些模板代码。

**使用 Lombok `@Builder` 注解：**

上面的 `User` 类可以简化为：

```java
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder // 核心注解
@ToString
public final class User {
    private final Long id;
    private final String username;
    private final String email;
    private final String nickname;
}
```

Lombok 会在编译时自动为你生成 `UserBuilder` 静态内部类、私有构造函数以及所有链式调用方法。用法完全一样，但代码量大大减少。

**Spring 框架自身对建造者模式的应用：**

1.  **`RestTemplateBuilder`**: Spring 5 以后，推荐使用 `RestTemplateBuilder` 来创建和配置 `RestTemplate` 实例，而不是直接 `new RestTemplate()`。
    ```java
    @Configuration
    public class AppConfig {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder
                    .setConnectTimeout(Duration.ofSeconds(5))
                    .setReadTimeout(Duration.ofSeconds(5))
                    .basicAuthentication("user", "password")
                    .build();
        }
    }
    ```

2.  **`MockMvcBuilders`**: 在编写 Controller 单元测试时，我们使用 `MockMvcBuilders` 来构建一个模拟的 MVC 环境。
    ```java
    // 示例
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MyController())
                                    .setMessageConverters(new MappingJackson2HttpMessageConverter())
                                    .build();
    ```

---

### 4. 要点 / 注意事项

1.  **不变性（Immutability）**：建造者模式是实现对象不可变性的绝佳方式。将 Product 类的字段设为 `final`，不提供 `setter`，并将构造函数设为 `private`，是实现这一点的关键。
2.  **参数校验（Validation）**：最佳的校验时机是在 `Builder` 的 `build()` 方法中，或者在 `Product` 的私有构造函数中。这样可以确保一旦对象被创建，它一定是处于一个合法的状态。
    ```java
    // 在 build 方法中校验
    public User build() {
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Username cannot be empty");
        }
        return new User(this);
    }
    ```
3.  **线程安全（Thread Safety）**：
    *   **Builder 对象本身不是线程安全的**。它是一个状态可变的对象，不应该在多个线程间共享。每个线程都应该创建自己的 `Builder` 实例。
    *   **最终创建的 Product 对象，如果是不可变的，则是线程安全的**。
4.  **何时不适用**：如果一个对象只有两三个参数，且没有复杂的构建逻辑，直接使用构造函数或静态工厂方法会更简洁，不必过度设计。

---

### 5. 发展与扩展

#### 建造者模式与 Java Records (JDK 16+)

Java 16 引入的 `Record` 类型提供了一种创建不可变数据载体的简洁语法。

```java
// 使用 Record 定义 User
public record User(Long id, String username, String email, String nickname) {}
```
这行代码自动创建了 `final` 字段、全参构造函数、`getters`、`equals()`、`hashCode()` 和 `toString()`。

**Record 是否替代了建造者模式？**
**不完全是。** 它们解决的问题有重叠，但侧重点不同。

*   **Record**：适用于所有字段都是**必需**的、简单的不可变数据对象。它的构造函数需要你提供所有参数。
*   **Builder**：当对象有很多**可选**参数时，或者构建过程比较复杂时，建造者模式的优势依然巨大。

**最佳实践：Record 与 Builder 结合**
在现代 Java 中，可以将两者结合，享受 Record 的简洁和 Builder 的灵活性。Lombok 完美支持这一点。

```java
import lombok.Builder;

@Builder
public record User(Long id, String username, String email, String nickname) {}
```
这样，你既拥有了 Record 的所有优点（简洁、不可变），又可以通过 Lombok 生成的 Builder 来方便地创建实例，尤其是处理可选字段时。

**Wither 方法**
对于不可变对象（无论是 Record 还是 Builder 模式创建的），如果你想“修改”某个字段，标准做法是创建一个新的、包含修改后字段值的副本。这种方法被称为 "wither" 方法。

Lombok 的 `@Builder(toBuilder = true)` 可以自动生成一个 `toBuilder()` 方法，轻松实现 "wither" 功能。

```java
@Builder(toBuilder = true) // 开启 toBuilder
public record User(Long id, String username, String email, String nickname) {}

// 使用
User originalUser = User.builder().id(1L).username("admin").build();

// "修改" username，实际上是创建了一个新对象
User updatedUser = originalUser.toBuilder()
                               .username("new_admin")
                               .build(); 
// updatedUser 是一个新实例，originalUser 保持不变
```
这种方式在函数式编程和状态管理中非常流行，因为它保证了数据的不可变性。

## 特性
### 分离配置（预配置）
```Java
@Bean  
public RestClient.Builder restClientBuilder() {  
    return RestClient.builder();  
}
```
这段代码 `public RestClient.Builder restClientBuilder()` 完美地展示了建造者模式在现代 Spring Boot 应用中的一种高级且实用的整合方式。

---

#### 这段代码属于建造者模式的什么特性？

这段代码展示的特性可以被称为：**建造者作为可配置的工厂原型 (Builder as a Configurable Factory Prototype)**。

更具体地说，它利用了 Spring 的**依赖注入 (DI)** 机制来管理和分发一个**预配置好**的 `Builder` 实例。

#### 为何可以这样，与什么有关？

**1. 与 Spring 的 IoC/DI 核心有关：**
*   `@Bean` 注解告诉 Spring 的 IoC (Inversion of Control) 容器：“请调用这个 `restClientBuilder()` 方法，获取它返回的对象（一个 `RestClient.Builder` 实例），然后由你来管理这个对象。”
*   默认情况下，这个被管理的 `Builder` 是一个**单例 (Singleton)** Bean。这意味着在整个应用程序的生命周期中，容器中只有这一个 `restClientBuilder` 实例。
*   其他任何需要创建 `RestClient` 的组件（比如 Service、Controller）都可以通过 `@Autowired` 或构造函数注入的方式，直接获取到这个**已经存在的、统一的** `Builder` 实例。

**2. 核心思想：分离“基础配置”与“个性化配置”**
这种做法的精妙之处在于，它把 `RestClient` 的创建过程分成了两步：

*   **第一步：基础配置（在 `@Bean` 方法中完成）**。
    你可以在这里为整个应用设置一个统一的、基础的 `RestClient` 行为。比如，设置通用的超时时间、添加所有请求都需要的认证头、配置统一的日志拦截器等。这个 `@Bean` 就是你定义全应用“网络请求规范”的地方。

    ```java
    @Configuration
    public class MyRestClientConfig {
        @Bean
        public RestClient.Builder restClientBuilder() {
            // 在这里进行全局的、基础的配置
            return RestClient.builder()
                    .baseUrl("https://api.example.com") // 统一的基础URL
                    .defaultHeader("X-App-Version", "1.2.0") // 统一的请求头
                    .requestInterceptor((request, body, execution) -> {
                        // 统一添加日志
                        log.info("Sending request to {}", request.getURI());
                        return execution.execute(request, body);
                    });
        }
    }
    ```

*   **第二步：个性化配置与构建（在具体使用的地方完成）**。
    当某个 Service 需要使用 `RestClient` 时，它注入这个预配置好的 `Builder`，然后可以在这个基础上添加自己特有的配置（比如某个请求需要特殊的 Header），最后调用 `.build()` 方法得到一个最终的 `RestClient` 实例。

    ```java
    @Service
    public class ProductService {
        private final RestClient restClient;

        // 注入预配置好的 Builder，而不是最终的 RestClient
        public ProductService(RestClient.Builder restClientBuilder) {
            // 在全局配置的基础上，添加本 Service 特有的配置，然后构建
            this.restClient = restClientBuilder
                                .defaultHeader("X-Service-Name", "ProductService") // 添加个性化Header
                                .build();
        }

        public Product getProduct(String id) {
            return restClient.get()
                    .uri("/products/{id}", id)
                    .retrieve()
                    .body(Product.class);
        }
    }
    ```

**总结：** 这样做的好处是**巨大的**。
*   **配置集中化**：所有网络请求的基础配置都在一个地方，易于维护和修改。
*   **代码复用**：避免了在每个 Service 中都重复编写相同的配置代码。
*   **灵活性**：每个 Service 仍然可以自由地进行个性化定制。
*   **线程安全**：尽管 `Builder` Bean 是单例的，但 `restClientBuilder.build()` 每次都会返回一个**新的、独立的** `RestClient` 实例。各个 Service 之间使用的 `RestClient` 实例是隔离的，互不影响，保证了线程安全。

---

### 建造者模式的其他所有核心特性详解

下面我们系统地梳理一下建造者模式的所有核心特性，并用一个 `Notification`（通知消息）的例子来贯穿讲解。

#### 1. 链式调用与流式 API (Chaining and Fluent API)

这是建造者模式最直观的特性。每个设置参数的方法都返回 `Builder` 自身（`return this;`），使得调用可以像链条一样串起来，代码一气呵成，可读性极高。

*   **讲解**：一系列 `setter` 方法连接在一起，形成一个流畅的、描述性的代码块。
*   **示例**：
    ```java
    Notification notification = Notification.builder("user-123", "New Message") // 必选参数
        .content("Your package has been shipped.")   // 链式调用
        .type(Notification.Type.INFO)                // 链式调用
        .urgent(true)                                // 链式调用
        .build();                                    // 最终构建
    ```

#### 2. 分离构建与表示 (Separation of Construction and Representation)

这是模式的定义核心。`Builder` 类（构建过程）负责处理所有复杂的创建逻辑，而 `Product` 类（最终表示）则是一个干净、纯粹的数据对象。

*   **讲解**：`Notification` 类只关心自己的属性是什么，而 `NotificationBuilder` 类则关心如何一步步地设置这些属性，以及在 `build()` 时进行何种校验或处理。
*   **示例**：
    ```java
    // Product: 最终表示，一个不可变对象
    public final class Notification {
        private final String userId;
        // ... 其他字段
        
        // 构造函数是私有的，只能被 Builder 调用
        private Notification(NotificationBuilder builder) {
            this.userId = builder.userId;
            // ...
        }
        // 只有 getters
    }

    // Builder: 构建过程
    public static class NotificationBuilder {
        // ... 字段
        public NotificationBuilder content(String content) { /* ... */ return this; }
        public Notification build() {
            // 可以在这里添加复杂的构建逻辑
            return new Notification(this);
        }
    }
    ```

#### 3. 优雅地处理可选参数 (Handling Optional Parameters)

这是建造者模式解决的一个关键痛点。相比于写一长串 `null` 的构造函数，或者编写多个重载构造函数（Telescoping Constructor），Builder 让你只设置你关心的参数。

*   **讲解**：一个通知可能只有接收者和标题是必选的，内容、类型、是否紧急等都是可选的。使用 Builder 可以非常清晰地表达这一点。
*   **示例**：
    ```java
    // 对比：丑陋的构造函数调用
    // new Notification("user-123", "New Message", "Package shipped.", "INFO", true, null); // 最后一个null是什么？

    // 使用 Builder，只设置需要的可选参数
    Notification simpleNotification = Notification.builder("user-123", "New Message")
        .content("Package shipped.")
        .build(); // 未设置的 type 和 urgent 会使用默认值或为 null

    Notification urgentNotification = Notification.builder("user-456", "Alert!")
        .urgent(true)
        .build(); // 未设置 content 和 type
    ```

#### 4. 创建不可变对象 (Creating Immutable Objects)

建造者模式是实现不可变性的最佳实践之一。通过将 Product 的构造函数设为私有，并使其字段为 `final`，可以保证对象一旦创建，其状态就永远不会改变。这对于并发编程和保证程序的稳定性至关重要。

*   **讲解**：`Notification` 对象一旦通过 `.build()` 创建出来，就不能再修改其任何属性，比如不能把一条普通通知“变成”紧急通知。
*   **示例**：
    ```java
    public final class Notification { // final class
        private final String userId;  // final field
        private final String title;   // final field
        // ...

        private Notification(NotificationBuilder builder) { /* ... */ }

        // 没有 setUserId(), setTitle() 等方法
    }
    
    Notification n = Notification.builder("...","...").build();
    // n.setUrgent(true); // 编译错误！无法修改
    ```

#### 5. 隐藏复杂的构建逻辑 (Hiding Complex Construction Logic)

`build()` 方法是封装复杂构建逻辑的理想场所。比如，基于某些参数计算出另外一些参数，或者在构建前进行严格的参数校验。

*   **讲解**：例如，如果 `urgent` 标志为 `true`，我们可能需要自动在标题前加上 `[URGENT]` 前缀。这个逻辑应该放在 `Builder` 中，而不是让调用者操心。
*   **示例**：
    ```java
    public static class NotificationBuilder {
        private String title;
        private boolean urgent = false;
        // ...

        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder urgent(boolean urgent) { this.urgent = urgent; return this; }
        
        public Notification build() {
            if (this.userId == null) {
                throw new IllegalStateException("User ID cannot be null.");
            }
            
            // 复杂的衍生逻辑
            if (this.urgent && !this.title.startsWith("[URGENT]")) {
                this.title = "[URGENT] " + this.title;
            }
            
            return new Notification(this);
        }
    }
    ```

通过你提出的 `RestClient.Builder` 的例子，我们看到了建造者模式如何从一个单纯的对象创建工具，升华成为 Spring 生态中实现**配置、复用与灵活扩展**的强大基石。