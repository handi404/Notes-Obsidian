深入剖析 Lombok 中最受欢迎的注解之一：`@Builder`。它完美地解决了传统对象构建方式的痛点，让代码既安全又易读。

---

### 一、为什么需要 `@Builder`？—— 告别冗长的构造函数和 Setter

在没有 `@Builder` 的世界里，创建一个拥有多个字段的对象，通常有两种痛苦的方式：

1.  **重叠构造函数 (Telescoping Constructor)**：为了应对不同的字段组合，你需要编写多个构造函数，非常笨拙且容易出错。
    ```java
    // 噩梦般的构造函数
    public class Pizza {
        private String size;
        private boolean cheese;
        private boolean pepperoni;
        private boolean bacon;
        // Pizza(String size) { ... }
        // Pizza(String size, boolean cheese) { ... }
        // Pizza(String size, boolean cheese, boolean pepperoni) { ... }
        // ... 无尽的组合
    }
    // 调用时像在猜谜：
    Pizza pizza = new Pizza("large", true, false, true); // 第三个参数是啥来着？
    ```

2.  **JavaBean 模式 (Setter)**：对象在完全构建好之前，处于一个不完整的状态，这在并发环境中是危险的。
    ```java
    Pizza pizza = new Pizza();
    pizza.setSize("large");
    pizza.setCheese(true);
    // ... 如果此时另一个线程拿到了这个 pizza 对象，它就是一个半成品
    pizza.setBacon(true);
    ```

**`@Builder` 的核心价值**：它提供了第三种选择——**建造者模式**，集安全与优雅于一身。

---

### 二、`@Builder` 的基础与核心用法

`@Builder` 注解会为你的类生成一套流畅的 (fluent) API，用于创建对象实例。

#### 1. 基本用法

你只需要在类上加上 `@Builder` 注解，Lombok 就会在编译期为你完成所有繁重工作。

```java
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class User {
    private final Long id;
    private final String name;
    private final String email;
    private int age; // 也可以是非 final 字段
}

// ✨ 优雅的调用方式 ✨
User user = User.builder()
                .id(1001L)
                .name("Lombok Master")
                .email("master@lombok.dev")
                .age(30)
                .build();

System.out.println(user);
// 输出: User(id=1001, name=Lombok Master, email=master@lombok.dev, age=30)
```

#### 2. 它是如何工作的？⚙️

当你写下 `@Builder` 时，Lombok 在背后生成了：
*   一个名为 `UserBuilder` 的**内部静态类**。
*   这个 `UserBuilder` 类包含了与 `User` 类对应的字段。
*   `UserBuilder` 类中，每个字段都有一个同名的、返回 `UserBuilder` 自身的方法（例如 `name(String name)`），这构成了**链式调用**。
*   一个 `build()` 方法，它会调用 `User` 的构造函数来创建最终对象。
*   在 `User` 类中，一个名为 `builder()` 的**静态方法**，用于获取 `UserBuilder` 的实例。

> **洞察**：`@Builder` 默认需要一个**全参构造函数**来工作。如果你的类中没有，Lombok 会自动生成一个 `private` 的全参构造函数供 `build()` 方法使用。如果你自己定义了构造函数，Lombok 会尝试使用它，这有时会导致问题（后面会讲如何处理）。

---

### 三、高级特性与定制化

`@Builder` 不仅仅是傻瓜式生成，它提供了丰富的定制选项。

#### 1. `@Builder.Default`：为字段设置默认值

一个常见的误区是直接给字段赋初始值，但这在 `@Builder` 中是**无效**的。

```java
@Builder
public class Server {
    private String host = "localhost"; // ⚠️ 这个默认值不会生效！
    private int port = 8080;         // ⚠️ 这个也不会！
}
// Server.builder().build() 会得到 host=null, port=0
```

**正确姿势**：使用 `@Builder.Default`。

```java
import lombok.Builder;

@Builder
public class Server {
    @Builder.Default private String host = "localhost"; // ✅ 正确
    @Builder.Default private int port = 8080;         // ✅ 正确
}

// 现在可以这样用
Server server = Server.builder().build(); // -> Server(host=localhost, port=8080)
```

#### 2. `@Singular`：优雅地处理集合

这是 `@Builder` 的一个杀手级特性，用于简化集合字段的构建。

```java
import lombok.Builder;
import lombok.Singular;
import java.util.List;
import java.util.Map;

@Builder
public class Team {
    private String teamName;
    @Singular // 注意这里
    private List<String> members;
    @Singular("role") // 可以给方法起别名
    private Map<String, String> memberRoles;
}

// ✨ 极其方便的调用 ✨
Team team = Team.builder()
                .teamName("Lombok Avengers")
                .member("Iron Man")       // 1. 单个添加
                .member("Captain America") // 1. 单个添加
                .members(List.of("Thor", "Hulk")) // 2. 批量添加 (会覆盖之前的)
                .role("Iron Man", "Leader") // 3. 为 Map 添加键值对
                .role("Hulk", "Muscle")
                .clearMembers()             // 4. 清空集合
                .build();
```

`@Singular` 会为你生成：
*   一个添加单个元素的方法（如 `member(String member)`）。
*   一个添加一个集合所有元素的方法（如 `members(Collection<? extends String> members)`）。
*   一个清空集合的方法（如 `clearMembers()`）。
*   对于 Map，会生成一个添加键值对的方法（如 `role(String key, String value)`）。

#### 3. `toBuilder = true`：创建对象的副本并修改

当你有一个不可变对象，但又想基于它创建一个只有细微差别的新对象时，`toBuilder` 就派上用场了。

```java
import lombok.Builder;
import lombok.Value;

@Value // 创建不可变类
@Builder(toBuilder = true) // 开启 toBuilder 功能
public class HttpRequest {
    String url;
    String method;
    @Builder.Default long timeout = 3000L;
}

// 使用
HttpRequest request1 = HttpRequest.builder()
                                  .url("/api/v1/users")
                                  .method("GET")
                                  .build();

// 现在，我想发送一个 POST 请求到同一个 URL
HttpRequest request2 = request1.toBuilder() // 复制 request1 的所有属性
                               .method("POST") // 只修改 method
                               .build();

System.out.println(request2); // HttpRequest(url=/api/v1/users, method=POST, timeout=3000)
```

#### 4. 定制化 Builder

*   `builderMethodName = "of"`: 将静态的 `builder()` 方法名改为 `of()`。
*   `buildMethodName = "create"`: 将 `build()` 方法名改为 `create()`。

```java
@Builder(builderMethodName = "of", buildMethodName = "create")
public class Product { ... }

// 调用方式改变
Product p = Product.of().name("...").create();
```

---

### 四、特殊类的使用场景（完美无遗漏）

#### 1. 继承关系中的 `@Builder`：使用 `@SuperBuilder`

这是一个经典的“坑”。如果子类使用 `@Builder`，它将无法设置父类的字段。

```java
// 错误示范 ⚠️
public class Vehicle {
    private int wheels;
}

@Builder // 这个 Builder 只知道 color 字段
public class Car extends Vehicle {
    private String color;
}
// Car.builder().wheels(4)... // 编译错误！没有 .wheels() 方法
```

**正确方案**：从 Lombok v1.18.2 开始，使用 `@SuperBuilder`。

```java
import lombok.experimental.SuperBuilder;

@SuperBuilder // 父类使用 @SuperBuilder
public class Vehicle {
    protected int wheels;
}

@SuperBuilder // 子类也使用 @SuperBuilder
public class Car extends Vehicle {
    private String color;
}

// ✅ 完美调用
Car car = Car.builder()
             .wheels(4)   // 父类的字段
             .color("Red")  // 子类的字段
             .build();
```

#### 2. 泛型类 (Generic Class) 中的 `@Builder`

Lombok 对泛型有很好的支持。`@Builder` 会自动生成一个正确类型的泛型 Builder。

```java
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Box<T> {
    private T content;
    private String label;
}

// 调用时，类型会自动推断
Box<String> stringBox = Box.<String>builder() // 通常 <String> 都可以省略
                           .content("Hello Generics!")
                           .label("A String Box")
                           .build();

Box<Integer> intBox = Box.builder()
                         .content(123)
                         .label("An Integer Box")
                         .build();

System.out.println(stringBox); // Box(content=Hello Generics!, label=A String Box)
System.out.println(intBox);    // Box(content=123, label=An Integer Box)
```
Lombok 生成的 Builder 类会是 `BoxBuilder<T>`，确保了类型安全。

#### 3. 在构造函数或静态工厂方法上使用 `@Builder`

有时你不想让所有字段都加入 Builder，或者想在构建过程中执行一些额外的逻辑。你可以把 `@Builder` 注解放在**构造函数**或**静态工厂方法**上。

```java
import lombok.Builder;
import lombok.NonNull;

public class UserProfile {
    private final String username;
    private final String displayName;
    private final long createdAt;

    @Builder // 将 @Builder 放在构造函数上
    private UserProfile(@NonNull String username, String displayName) {
        this.username = username;
        this.displayName = (displayName == null) ? username : displayName; // 自定义逻辑
        this.createdAt = System.currentTimeMillis(); // 自动生成的字段
    }
}

// 此时 Builder 只会暴露 username 和 displayName 字段
UserProfile profile = UserProfile.builder()
                                   .username("admin")
                                   // .displayName("Administrator") // 可选
                                   // .createdAt(123L) // 编译错误，Builder 不知道这个字段
                                   .build();
```
这种方式给予了你最大程度的控制力。

---

### 总结与最佳实践

*   **默认选择**：对于需要构建的复杂对象，`@Builder` 是首选。
*   **不可变性**：`@Builder` 和 `final` 字段是创建**不可变对象**的黄金搭档。配合 `@Value` 或 `@AllArgsConstructor` 使用效果更佳。
*   **继承**：记住，只要涉及继承，就必须在父子类上都使用 `@SuperBuilder`。
*   **泛型**：放心使用，Lombok 处理得很好。
*   **定制化**：当默认行为不满足需求时，善用 `@Builder.Default`, `@Singular`, `toBuilder=true` 以及将 `@Builder` 放在方法上等高级技巧。

掌握了 `@Builder` 的这些用法，你就能在项目中写出非常干净、可读性极高且安全的对象构建代码。