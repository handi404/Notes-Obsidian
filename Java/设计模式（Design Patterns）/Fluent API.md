**Fluent API**（流畅 API）。

想象一下你在和你的代码“对话”，你希望这个对话过程自然、流畅，就像说一句话一样，而不是磕磕绊绊地发出一个个独立的指令。Fluent API 就是为了实现这种“对话式”编程体验而设计的一种 API 风格。

**核心思想与实现：**

Fluent API 的核心在于**方法链式调用 (Method Chaining)**。简单来说，就是一个对象的方法执行完毕后，返回对象自身（`this`）或者另一个可以继续进行操作的对象，从而允许你像链条一样把多个方法调用串联起来。

**通俗比喻：**

*   **点菜：** 你不会说：“给我米饭。给我宫保鸡丁。给我可乐。” 你会说：“我想要一份米饭、一份宫保鸡丁和一杯可乐。” Fluent API 就像后者，把多个操作连贯地表达出来。
*   **乐高积木：** 每一块积木（方法调用）都能无缝地拼接到上一块积木上，最终搭建出一个完整的模型（配置好的对象或执行完的逻辑）。

**一个简单的例子：**

假设我们要构建一个 `Email` 对象，传统方式可能是这样：

```java
// 传统方式
Email email = new Email();
email.setFrom("sender@example.com");
email.setTo("receiver@example.com");
email.setSubject("Hello");
email.setBody("This is a fluent API example!");
email.send();
```

使用 Fluent API 可以写成这样：

```java
// Email 类的 Fluent API 实现（简化版）
class Email {
    private String from;
    private String to;
    private String subject;
    private String body;

    public Email from(String from) {
        this.from = from;
        return this; // 返回自身，实现链式调用
    }

    public Email to(String to) {
        this.to = to;
        return this;
    }

    public Email subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Email body(String body) {
        this.body = body;
        return this;
    }

    public void send() {
        // 实际发送邮件的逻辑
        System.out.println("Sending email from: " + from + " to: " + to + " with subject: '" + subject + "'");
    }

    // 通常会有一个静态工厂方法作为入口
    public static Email builder() {
        return new Email();
    }
}

// Fluent API 调用方式
Email.builder()
     .from("sender@example.com")
     .to("receiver@example.com")
     .subject("Hello")
     .body("This is a fluent API example!")
     .send();
```

**优点：**

1.  **极佳的可读性 (Readability)：** 代码读起来更像自然语言，逻辑更清晰。
2.  **易于编写和理解 (Ease of Writing & Understanding)：** 链式调用使得代码的意图一目了然。
3.  **减少冗余代码 (Reduced Verbosity)：** 相对于多次重复对象名调用 setter，代码更简洁。
4.  **引导性 (Guidance) / 更好的发现性 (Discoverability)：** IDE 的自动补全功能在 Fluent API 下非常好用，你可以通过 `.` 点出来当前对象可用的下一步操作。
5.  **创建领域特定语言 (DSL - Domain Specific Language) 的基础：** 可以设计出针对特定业务场景的、表达力很强的 API。
6.  **通常与构建者模式 (Builder Pattern) 结合使用：** 特别适合构建复杂对象，可以确保对象在构建完成前处于一致的状态，并且可以方便地创建不可变对象。

**设计 Fluent API 的关键点：**

1.  **返回 `this`：** 大部分中间方法返回当前对象的引用 (`this`)。
2.  **上下文对象：** 有时，一个方法可能会返回一个新的、不同类型的对象，但这个新对象仍然支持链式调用，用于处理不同阶段的逻辑。
3.  **终止方法 (Terminal Method)：** 通常会有一个或多个“终止方法”，它们不再返回 `this` 或下一个可链式调用的对象，而是返回最终结果（如构建好的对象、一个值）或执行一个动作（如 `send()`、`build()`、`execute()`）。
4.  **清晰的方法命名：** 方法名应该清晰地表达其作用，使得链式调用读起来通顺。
5.  **不变性 (Immutability) 的考量：** Fluent API 常用于构建不可变对象。Builder 本身是可变的，但最终通过 `build()` 方法返回一个不可变的对象。

**最新 Java 中的应用与扩展：**

1.  **Java Stream API (自 Java 8 起)：** 这是 Java 标准库中最典型的 Fluent API 例子。
    ```java
    List<String> names = List.of("Alice", "Bob", "Charlie", "David");
    List<String> longNamesInUpperCase = names.stream()
                                           .filter(name -> name.length() > 4)
                                           .map(String::toUpperCase)
                                           .sorted()
                                           .collect(Collectors.toList());
    ```
    这里的 `stream()`, `filter()`, `map()`, `sorted()` 都是中间操作，返回一个新的 Stream，而 `collect()` 是终止操作。

2.  **`Optional` 类 (自 Java 8 起)：** 也体现了 Fluent API 的思想。[[Java/Spring/Spring Boot/Optional]]
    ```java
    Optional.ofNullable(getUserById(1))
            .map(User::getName)
            .filter(name -> name.startsWith("A"))
            .ifPresent(System.out::println);
    ```

3.  **Lombok 的 `@Builder`：** Lombok 是一个非常流行的 Java 库，它的 `@Builder` 注解可以自动为一个类生成 Fluent API 风格的构建器。[[@Builder]]
    ```java
    // 使用 Lombok
    @lombok.Builder
    @lombok.Value // 创建不可变类
    class User {
        String name;
        int age;
    }

    // 调用
    User user = User.builder()
                    .name("Alice")
                    .age(30)
                    .build();
    ```
    Lombok 大大简化了创建 Builder 模式和 Fluent API 的代码。

4.  **第三方库：** 许多现代 Java 库都广泛采用 Fluent API：
    *   **Mockito (测试框架):** `when(mockedObject.someMethod()).thenReturn(value);`
    *   **jOOQ (数据库查询):** 用于构建类型安全的 SQL 查询。
    *   **Spring Framework (尤其是 Spring Security, Spring Data):** 大量配置类使用 Fluent API 风格。例如，Spring Security 的 `HttpSecurity` 配置。
    *   **OkHttp/Retrofit (网络请求):** 构建 HTTP 请求。

5.  **与记录类型 (Records - Java 14+) 的结合：**
    虽然记录类型主要用于数据载体，并且有其规范的构造函数，但它们可以与 Fluent Builder 结合使用。Lombok 的 `@Builder` 注解也能很好地与记录类型配合，为记录类型生成 Fluent Builder。

**注意事项：**

*   **不要滥用：** 不是所有 API 都适合设计成 Fluent 风格。对于非常简单的对象或操作，Fluent API 反而可能增加不必要的复杂性。
*   **调试：** 过长的链式调用可能在调试时稍微麻烦一点，因为单步调试会逐个进入链中的方法。但现代 IDE 通常能很好地处理。
*   **方法数量：** Fluent API 可能会导致接口中方法数量增多，但通常每个方法都很小且专注。

**总结：**

Fluent API 是一种强大的 API 设计模式，它通过方法链式调用来提高代码的可读性和易用性，使得代码更接近自然语言的表达方式。它在现代 Java 开发中非常流行，尤其是在对象构建、配置、数据处理（如 Stream API）和 DSL 构建等场景中。结合 Lombok 等工具，实现 Fluent API 也变得非常便捷。当你希望提供一种用户友好、表达力强的编程接口时，Fluent API 是一个非常值得考虑的选择。