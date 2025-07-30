`java.util.stream.Stream` 接口中所有的静态方法。

这些静态方法是所有 Stream 流水线操作的**起点**，它们扮演着“工厂”的角色，负责从各种数据源创建出 Stream 实例。

---

### 核心概念 (Core Concept)

`Stream` 的静态方法，本质上就是**Stream 的创建方法**。它们不依赖于任何已存在的 Stream 实例，而是直接通过 `Stream.methodName()` 的形式调用，用于从无到有地生成一个流。

想象一下，你要处理一批货物。这些静态方法就是帮你把货物（数据）放上传送带（Stream）的第一步。货物可以是一个一个给的，可以是一整箱给的，也可以是按照一个规则持续不断生成的。

为了便于理解，我将它们分为三大类：

1.  **从已知元素创建**：当你手头已经有明确的数据时使用。
2.  **从函数生成 (无限流)**：当你需要根据一个规则动态生成数据序列时使用。
3.  **特殊用途创建**：用于一些特殊场景，如合并、构建空流等。

---

### 1. 从已知元素创建

这类方法适用于数据已经存在且确定的情况。

#### `Stream.of(...)`

*   **核心概念：** 最常用、最直接的创建方式。像把几个零散的物品直接放上传送带。
*   **方法签名：**
    *   `static <T> Stream<T> of(T... values)`: 接受一个可变参数数组。
    *   `static <T> Stream<T> of(T t)`: 接受单个元素。

*   **代码示例：**

    ```java
    // 1. 从多个元素创建
    Stream<String> streamFromElements = Stream.of("Java", "Python", "Go");
    streamFromElements.map(String::toUpperCase).forEach(System.out::println); // JAVA PYTHON GO

    // 2. 从单个元素创建
    Stream<String> streamFromSingle = Stream.of("Rust");
    streamFromSingle.forEach(System.out::println); // Rust

    // 3. 传入一个数组 (注意：是把整个数组作为单一元素，还是数组内容作为流元素)
    String[] languages = {"C++", "C#"};
    Stream<String> streamFromArray = Stream.of(languages); // 正确用法，将数组内容作为流元素
    streamFromArray.forEach(System.out::println); // C++ C#
    ```

#### `Stream.ofNullable(T t)` (Java 9 新增)

*   **核心概念：** `of()` 方法的“安全”版本。如果传入的元素是 `null`，它会创建一个空流，而不是像 `of(null)` 那样创建一个包含单个 `null` 元素的流。这能有效避免 `NullPointerException`。
*   **比喻：** 一个智能的传送带入口，你给它一个空盒子，它会生成一个空的传送带，而不是让这个空盒子在传送带上跑。

*   **代码示例：**

    ```java
    public class User {
        private String name;
        // getter & setter
    }

    User user = null; // 假设从数据库或API获取的用户可能为null

    // 旧方法，需要手动检查
    Stream<String> oldWay = (user != null) ? Stream.of(user.getName()) : Stream.empty();

    // 现代、简洁的方法 (since Java 9)
    Stream<String> modernWay = Stream.ofNullable(user).map(User::getName);
    long count = modernWay.count(); // count为0，不会抛出NPE

    System.out.println("Stream中的元素数量: " + count);

    // 对比 of(null)
    // Stream<User> streamWithNull = Stream.of(null);
    // streamWithNull.map(User::getName).forEach(System.out::println); // 这会抛出 NullPointerException
    ```

---

### 2. 从函数生成 (无限流)

这类方法会创建一个“无限”流，数据在被访问时才动态生成。**必须配合 `limit()` 等短路操作使用，否则会无限运行导致程序挂起或内存溢出。**

#### `Stream.iterate(...)`

*   **核心概念：** 一个“数列生成器”。给定一个初始值（种子）和一个函数，它会不断地将函数应用于前一个结果，以生成下一个元素。`seed, f(seed), f(f(seed)), ...`
*   **方法签名：**
    *   `static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)`: (经典无限流)
    *   `static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)`: (Java 9 新增的有限流版本，更安全)

*   **代码示例：**

    ```java
    // 1. 经典用法：生成一个偶数序列 (必须 limit)
    System.out.println("经典iterate生成偶数序列:");
    Stream.iterate(0, n -> n + 2)
          .limit(5) // 如果没有limit()，这里会无限执行
          .forEach(System.out::println); // 0 2 4 6 8

    // 2. 现代用法 (Java 9+)，自带终止条件，类似 for 循环
    System.out.println("\nJava 9+ iterate生成序列:");
    Stream.iterate(0, n -> n < 10, n -> n + 2) // (初始值; 终止条件; 迭代步进)
          .forEach(System.out::println); // 0 2 4 6 8
    ```

#### `Stream.generate(Supplier<T> s)`

*   **核心概念：** 一个“重复供应器”。它接受一个不接受参数但返回一个值的 `Supplier` 函数，并不断调用它来生成流的每一个元素。每个元素之间没有关联。
*   **比喻：** 一个不停生产同一种商品（或随机商品）的机器。

*   **代码示例：**

    ```java
    // 1. 生成5个随机数 (必须 limit)
    System.out.println("生成随机数:");
    Stream.generate(Math::random)
          .limit(5)
          .forEach(System.out::println);

    // 2. 生成固定的字符串
    System.out.println("\n生成常量:");
    Stream.generate(() -> "Hello")
          .limit(3)
          .forEach(System.out::println); // Hello Hello Hello
    ```

---

### 3. 特殊用途创建

#### `Stream.empty()`

*   **核心概念：** 创建一个不包含任何元素的空流。
*   **应用：** 当一个方法的返回值类型是 `Stream`，但在某些逻辑下需要返回一个“无结果”的流时，返回 `Stream.empty()` 是最佳实践，而不是返回 `null`。这符合函数式编程的风格，避免了调用方进行烦人的 `null` 检查。

*   **代码示例：**

    ```java
    public Stream<String> getValidTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Stream.empty(); // 返回空流，而不是null
        }
        return tags.stream().filter(tag -> tag.startsWith("#"));
    }

    // 调用方可以安全地直接操作，无需null检查
    getValidTags(null).forEach(System.out::println); // 不会报错，什么也不输出
    ```

#### `Stream.concat(Stream<? extends T> a, Stream<? extends T> b)`

*   **核心概念：** 将两个流按顺序拼接成一个新流。
*   **比喻：** 连接两个传送带的首尾，形成一个更长的传送带。

*   **代码示例：**

    ```java
    Stream<String> streamA = Stream.of("A", "B");
    Stream<String> streamB = Stream.of("C", "D");

    Stream.concat(streamA, streamB)
          .forEach(System.out::println); // A B C D
    ```

#### `Stream.builder()`

*   **核心概念：** 提供一个 `Builder` 对象，允许你以编程方式、分步地向流中添加元素，最后调用 `build()` 方法一次性创建出流。
*   **应用：** 当流的元素需要根据复杂的业务逻辑动态、分步添加时，`Builder` 模式非常有用。

*   **代码示例：**

    ```java
    Stream.Builder<String> builder = Stream.builder();

    builder.add("Java");

    // 根据条件动态添加
    boolean includeAdvanced = true;
    if (includeAdvanced) {
        builder.accept("Kotlin"); // accept是add的别名
        builder.accept("Scala");
    }

    // 最后构建流
    Stream<String> finalStream = builder.build();
    finalStream.forEach(System.out::println); // Java Kotlin Scala
    ```

---

### 深入解析与演进 (In-depth Analysis & Evolution)

*   **为什么是接口里的静态方法？** Java 8 允许在接口中定义静态方法。这使得像 `Stream` 这样的核心 API 可以在不破坏现有实现类的情况下，提供方便的工厂方法。这些方法不依赖于任何特定实现（如 `ArrayList.stream()`），而是直接属于 `Stream` 这个抽象本身。
*   **惰性求值 (Lazy Evaluation):** 所有这些静态方法（以及后续的中间操作如 `map`, `filter`）都遵循惰性求值。也就是说，调用 `Stream.iterate(...)` 并不会立即生成所有数字，只有当终端操作（如 `forEach`, `collect`）被调用时，数据才会按需生成。这是 Stream API 高性能的关键。
*   **演进：** Java 9 的 `ofNullable` 和新版 `iterate` 是对 Java 8 的重要补充，它们让代码更安全、更易读，体现了 Java 在函数式编程领域的持续改进。

---

### 扩展与应用 (Extension & Application)

*   **与集合创建对比：** 最常见的流创建方式是 `collection.stream()`。你应该优先使用这种方式，因为它最直观。当你的数据源不是一个现成的集合（例如，零散的几个变量、一个数组、或一个生成规则）时，这些静态方法就派上用场了。
*   **真实项目场景（Spring Boot）:**
    ```java
    @Service
    public class NotificationService {
        // 假设配置中有一些默认的通知渠道
        @Value("${notification.default-channels:EMAIL,SMS}")
        private String[] defaultChannels;

        public Stream<String> getChannels(User user) {
            // 从配置中获取默认渠道
            Stream<String> defaultStream = Stream.of(defaultChannels);
            // 从用户偏好中获取额外渠道 (可能为null)
            Stream<String> userPrefStream = Stream.ofNullable(user.getPreferredChannel());

            // 合并去重后返回
            return Stream.concat(defaultStream, userPrefStream).distinct();
        }
    }
    ```
*   **原始类型流 (Primitive Streams):** 对于 `int`, `long`, `double`，有专门的 `IntStream`, `LongStream`, `DoubleStream`，它们也提供了更高效的静态方法，如 `range` 和 `rangeClosed`，用于生成数值范围，避免了自动装箱/拆箱的性能开销。
    ```java
    // 生成 1 到 4 的整数流
    IntStream.range(1, 5).forEach(System.out::println); // 1, 2, 3, 4
    // 生成 1 到 5 的整数流
    IntStream.rangeClosed(1, 5).forEach(System.out::println); // 1, 2, 3, 4, 5
    ```

---

### 要点与注意事项 (Key Points & Best Practices)

1.  **流只能消费一次：** 任何 `Stream` 实例（无论如何创建）在经过终端操作后就会关闭，不能重用。如果你需要再次使用，必须重新创建。
2.  **无限流必须短路：** 使用 `iterate` 或 `generate` 创建的流，如果没有 `limit()`, `findFirst()` 等短路操作，将导致无限循环。这是新手最常犯的错误。
3.  **优先使用 `ofNullable`:** 在处理可能为 `null` 的单个对象时，用 `Stream.ofNullable()` 替代 `if-else` 判断，代码更优雅。
4.  **`Stream.empty()` vs `null`:** 方法返回值是流时，用 `Stream.empty()` 表示空集合，这是更健壮的 API 设计。
5.  **I/O 资源管理：** 如果从 I/O 操作（如 `Files.lines()`，它也创建流）创建流，务必使用 `try-with-resources` 语句来确保流和底层资源被正确关闭。
6.  **性能考量：** 对于原始数字类型，优先使用 `IntStream`, `LongStream`, `DoubleStream` 及其静态方法（如 `range`），以获得更好的性能。