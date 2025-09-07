好的，我们来详细讲解 Java 11 为 `String` 类带来的几个极其实用的小工具。这些方法虽然简单，但它们解决了日常字符串处理中一些非常普遍的痛点，让代码变得更简洁、更健壮。

---

### String API 增强 (Java 11)

Java 11 主要为 `String` 类增加了四个实例方法：`isBlank()`, `lines()`, `strip()` 系列, 和 `repeat()`。

#### 1. 核心概念 (Core Concept) & 代码示例

##### **`isBlank()`**

*   **核心概念：** 判断一个字符串是否为**空白**。空白的定义是：字符串为 `null`、长度为 `0`，或者**只包含空白字符**（如空格、制表符 `\t`、换行符 `\n`）。
*   **解决了什么痛点：** 在 Java 11 之前，`isEmpty()` 只能判断长度是否为 `0`，对于 `"   "` 这样的字符串，`isEmpty()` 返回 `false`，不符合业务直觉。我们通常需要自己写 `str == null || str.trim().isEmpty()` 来判断。`isBlank()` 一步到位。

```java
// --- isBlank() Example ---
System.out.println("--- isBlank() ---");
String emptyStr = "";
String blankStr = "   \t\n  ";
String textStr = " Java ";

System.out.println("'' is empty? " + emptyStr.isEmpty());       // true
System.out.println("'' is blank? " + emptyStr.isBlank());       // true

System.out.println("'   ' is empty? " + blankStr.isEmpty());     // false
System.out.println("'   ' is blank? " + blankStr.isBlank());     // true

System.out.println("' Java ' is blank? " + textStr.isBlank());   // false
```

---

##### **`lines()`**

*   **核心概念：** 将一个多行字符串按照**行终止符**（如 `\n`, `\r`, `\r\n`）分割成一个 `Stream<String>`。
*   **解决了什么痛点：** 以前分割多行字符串需要用 `str.split("\\r?\\n")`，这个正则表达式有点复杂，而且返回的是一个数组。`lines()` 返回一个 Stream，可以直接进行后续的流式操作（`filter`, `map` 等），代码更具现代感和函数式风格。

```java
// --- lines() Example ---
System.out.println("\n--- lines() ---");
String multiLineStr = "First line\nSecond line\r\nThird line";

multiLineStr.lines()
    .filter(line -> !line.isBlank()) // 过滤掉空行
    .map(line -> "Line: " + line)
    .forEach(System.out::println);
/* Output:
   Line: First line
   Line: Second line
   Line: Third line
*/
```

---

##### **`strip()`, `stripLeading()`, `stripTrailing()`**

*   **核心概念：** 去除字符串首尾的**空白字符**。
    *   `strip()`: 去除首尾空白。
    *   `stripLeading()`: 只去除首部空白。
    *   `stripTrailing()`: 只去除尾部空白。
*   **`strip()` vs. `trim()` 的关键区别：**
    *   `trim()` 只能去除半角的 ASCII 空格 (`<= U+0020`)。
    *   `strip()` 能够去除所有 Unicode 空白字符，例如全角空格 (`\u3000`)。它更加“与时俱进”。

```
// --- strip() vs trim() Example ---
System.out.println("\n--- strip() vs trim() ---");
String strWithUnicodeSpace = "\u3000  Hello Java  \u3000"; // \u3000 is a full-width space
String strWithAsciiSpace = "  Hello Java  ";

System.out.println("Original: '" + strWithUnicodeSpace + "'");
System.out.println("Using trim(): '" + strWithUnicodeSpace.trim() + "'");   // Fails to remove Unicode space
System.out.println("Using strip(): '" + strWithUnicodeSpace.strip() + "'");   // Correctly removes it

// --- stripLeading() and stripTrailing() ---
System.out.println("Using stripLeading(): '" + strWithAsciiSpace.stripLeading() + "'");
System.out.println("Using stripTrailing(): '" + strWithAsciiSpace.stripTrailing() + "'");
```

**Output:**
```java
Original: '　  Hello Java  　'
Using trim(): '　  Hello Java  　'
Using strip(): 'Hello Java'
Using stripLeading(): 'Hello Java  '
Using stripTrailing(): '  Hello Java'
```

---

##### **`repeat(int count)`**

*   **核心概念：** 将字符串重复指定的次数，并拼接成一个新的字符串。
*   **解决了什么痛点：** 以前要重复字符串，需要自己写循环，或者使用第三方库（如 Apache Commons Lang 的 `StringUtils.repeat()`）。现在 JDK 原生支持，非常方便。

```java
// --- repeat() Example ---
System.out.println("\n--- repeat() ---");
String separator = "-".repeat(20);
System.out.println(separator); // Output: --------------------

String greeting = "Hi! ".repeat(3);
System.out.println(greeting);    // Output: Hi! Hi! Hi! 

// Edge cases
System.out.println("Repeat 0 times: '" + "abc".repeat(0) + "'"); // Output: '' (empty string)
// System.out.println("Repeat -1 times: " + "abc".repeat(-1)); // Throws IllegalArgumentException
```

#### 2. 演进与现代化 (Evolution & Modernization)

这些 API 的出现，是 Java 平台持续关注开发者体验、不断吸收社区优秀实践（很多功能在第三方库中早已存在）的结果。它们让 Java 在处理文本这种基础而常见的任务时，代码更加现代化、简洁、并且能更好地处理国际化（Unicode）场景。

*   **代码风格的现代化：** `lines()` 的引入，进一步鼓励了开发者使用 Stream API 来处理集合和序列数据，使得数据处理流水线更加统一和流畅。
*   **国际化的支持：** `strip()` 对 Unicode 的支持，是 Java 平台全球化能力的一个小而美的体现。在处理多语言用户输入时，使用 `strip()` 而不是 `trim()` 是一个更健壮的选择。

#### 3. 扩展与应用 (Extension & Application)

*   **用户输入校验：** `isBlank()` 是校验表单输入（如用户名、评论）是否为空的完美工具。
*   **文件/文本处理：** `lines()` 极大地简化了按行读取和处理文本文件内容的代码。
    ```java
    // Java 11 Files.readString() + String.lines()
    // Path path = Paths.get("myFile.txt");
    // String content = Files.readString(path);
    // long nonEmptyLines = content.lines().filter(l -> !l.isBlank()).count();
    ```
*   **数据清洗：** `strip()` 系列方法是数据预处理阶段清洗脏数据（如去除用户输入中不小心多打的空格）的标准操作。
*   **格式化输出：** `repeat()` 在生成报告、日志分隔符、或者构建固定格式的文本时非常有用。

#### 4. 要点与注意事项 (Key Points & Cautions)

1.  **`isBlank()` vs. `isEmpty()`:** 务必分清两者的区别。`isEmpty()` 只关心 `length == 0`，而 `isBlank()` 关心的是“内容是否可见”。
2.  **`strip()` 优于 `trim()`:** 在所有新代码中，除非有特殊理由需要 `trim()` 的旧行为，否则都应该优先使用 `strip()` 系列方法。这是一个好习惯。
3.  **`lines()` 返回的是 Stream:** 这意味着你可以链式调用各种 Stream 操作，但也意味着它是一个一次性的流。如果你需要多次遍历这些行，需要先将其收集到一个 `List` 中 (`.collect(Collectors.toList())`)。
4.  **`repeat()` 的参数:**
    *   `count` 必须是**非负数** (`>= 0`)。如果传入负数，会抛出 `IllegalArgumentException`。
    *   `count` 为 `0` 时，返回空字符串。
    *   `count` 为 `1` 时，返回原字符串。

这些 `String` API 的增强，是 Java 11 中“小而美”的典范，它们虽然不像模块化或 ZGC 那样宏大，却实实在在地提升了每一位 Java 程序员的日常开发效率和代码质量。