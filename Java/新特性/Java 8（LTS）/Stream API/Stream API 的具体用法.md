## Stream API 的具体用法

**Stream API** 是 Java 8 引入的，用于简化集合操作。它允许你以声明性风格处理数据，例如过滤、映射、排序等，而不是编写传统的迭代和条件判断代码。Stream API 基于 Lambda 表达式，使得操作更加简洁和可读。

### 什么是 Stream？

- **不是数据结构：** Stream 不是数据存储，它只是对数据源的一种视图。
- **不是对原数据的修改：** Stream 的操作不会修改原数据，而是产生一个新的 Stream。
- **惰性求值：** Stream 的操作是延迟执行的，只有当终端操作被调用时，才会真正执行。

### Stream 的基本操作

- **创建 Stream:**
    
    - `Collection.stream()`: 从集合创建 Stream
    - `Arrays.stream(T[])`: 从数组创建 Stream
    - `Stream.of(T...)`: 从多个元素创建 Stream
    - `Stream.generate()`: 生成无限 Stream
    - `Stream.iterate()`: 从一个种子值开始，不断迭代生成 Stream
- **中间操作:**
    
    - **过滤:** `filter()`
    - **映射:** `map()`, `flatMap()`
    - **排序:** `sorted()`
    - **去重:** `distinct()`
    - **限制:** `limit()`, `skip()`
    - ...
- **终端操作:**
    
    - **匹配:** `anyMatch()`, `allMatch()`, `noneMatch()`
    - **查找:** `findAny()`, `findFirst()`
    - **规约:** `reduce()`
    - **收集:** `collect()`
    - ...

### 示例

```Java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

// 过滤出偶数，并求和
int sum = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)  // 将每个元素平方
                .reduce(0, Integer::sum);

// 将字符串列表转换为大写字母
List<String> words = Arrays.asList("apple", "banana", "orange");
List<String> upperCaseWords = words.stream()
                                   .map(String::toUpperCase)
                                   .collect(Collectors.toList());
```

### Stream API 的优势

- **函数式编程风格:** 使代码更加简洁、可读。
- **并行处理:** 可以利用多核处理器进行并行计算。
- **延迟执行:** 提高性能，避免不必要的计算。

### 常见应用场景

- **集合操作:** 过滤、映射、排序、查找等。
- **数据处理:** 从数据源中提取、转换、过滤数据。
- **文件处理:** 读取文件内容、处理文本数据。

### 注意事项

- **Stream 是惰性求值的:** 只有当终端操作被调用时，才会真正执行。
- **Stream 操作不会修改原数据:** Stream 的操作会产生一个新的 Stream。
- **并行流:** 使用 `parallelStream()` 可以将 Stream 转为并行流，但需要注意并行处理可能带来的线程安全问题。

### 深入学习

- **Java 8 官方文档:** 深入了解 Stream API 的各种操作和用法。
- **函数式编程:** 学习函数式编程的思想，可以更好地理解 Stream API。
- **并行编程:** 了解并行编程的原理，可以充分利用并行流的优势。