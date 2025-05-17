Java 中的**文本块（Text Blocks）**，这确实是一个非常实用的现代 Java 特性。

**什么是文本块 (Text Blocks)？**

简单来说，文本块是 Java 中一种**新的字符串字面量形式**，它允许你更轻松、更自然地定义**包含多行文本的字符串**。在文本块出现之前，如果你想在 Java 代码中定义一个包含多行文本的字符串（比如一段 SQL 查询、JSON 数据、HTML 代码或者诗歌），通常会非常麻烦。

**为什么需要文本块？（解决了什么痛点）**

想象一下没有文本块的时代，我们要写一段 JSON：

```java
// Java 14 及更早版本
String jsonOld = "{\n" +
                 "  \"name\": \"Java\",\n" +
                 "  \"version\": \"17\",\n" +
                 "  \"features\": [\n" +
                 "    \"Lambda Expressions\",\n" +
                 "    \"Streams API\",\n" +
                 "    \"Text Blocks\"\n" +
                 "  ]\n" +
                 "}";
```

或者使用 `StringBuilder`：

```java
// Java 14 及更早版本
String jsonOldBuilder = new StringBuilder()
    .append("{\n")
    .append("  \"name\": \"Java\",\n")
    .append("  \"version\": \"17\",\n")
    .append("  \"features\": [\n")
    .append("    \"Lambda Expressions\",\n")
    .append("    \"Streams API\",\n")
    .append("    \"Text Blocks\"\n")
    .append("  ]\n")
    .append("}")
    .toString();
```

痛点很明显：
1.  **大量的 `+` 拼接符**：使代码显得冗长。
2.  **显式的换行符 `\n`**：容易遗漏或写错，影响可读性。
3.  **转义字符 `\"`**：如果字符串本身包含双引号，就需要转义，进一步降低可读性。
4.  **缩进混乱**：代码中的缩进和字符串实际内容的缩进可能不一致，难以维护。

**文本块如何解决这些问题？**

文本块通过引入新的语法 `"""` 来界定字符串，使得多行字符串的定义变得非常直观和简洁。

**语法和核心规则：**

1.  **定界符**：文本块以三个双引号 `"""` 开始，并以三个双引号 `"""` 结束。
2.  **开始定界符后的换行**：开始的 `"""` 之后**必须**紧跟着一个换行符。你的文本内容从下一行开始。
3.  **结束定界符**：结束的 `"""` 标记字符串的结束。它可以与最后一行内容在同一行，或者单独放在新的一行。

**示例：**

```java
// 使用文本块 (Java 15+)
String jsonNew = """
                 {
                   "name": "Java",
                   "version": "21",
                   "features": [
                     "Lambda Expressions",
                     "Streams API",
                     "Text Blocks"
                   ]
                 }
                 """; // 注意：这里结束的 """ 前面没有内容，所以这一行是空的。

// 或者，结束的 """ 可以和内容在同一行（但不推荐，除非内容很少）
String greeting = """
                  Hello,
                  World!""";

System.out.println(jsonNew);
// 输出：
// {
//   "name": "Java",
//   "version": "21",
//   "features": [
//     "Lambda Expressions",
//     "Streams API",
//     "Text Blocks"
//   ]
// }

System.out.println(greeting);
// 输出：
// Hello,
// World!
```

**关键特性与细节：**

1.  **智能处理缩进 (Incidental White Space Stripping)**：
    *   这是文本块最强大的特性之一。编译器会自动移除“附带的”或“偶然的”前导空格。
    *   **如何确定？**
        *   编译器会分析文本块内容中的所有非空行以及闭合 `"""` 所在行（如果它前面有内容）。
        *   它会找到这些行中最左边的非空白字符的位置，或者闭合 `"""` 的位置（如果闭合 `"""` 比所有内容行都靠左）。这条“垂直线”左边的所有空格都被认为是“附带的”，会被移除。
        *   **简单理解**：文本块内容的整体缩进，是以内容中最靠左的字符或闭合 `"""` 为基准的。你在 IDE 里看到的相对缩进会被保留。

    ```java
    public class IndentationExample {
        public static void main(String[] args) {
            String html = """
                          <html>
                              <body>
                                  <p>Hello, Java!</p>
                              </body>
                          </html>
                          """; // 闭合的"""决定了基准线
            System.out.println(html);
        }
    }
    // 输出:
    // <html>
    //     <body>
    //         <p>Hello, Java!</p>
    //     </body>
    // </html>
    // (注意：输出结果的每一行前面没有额外的空格，因为基准线就是</html>这一行的开始)

    // 如果想让输出也带缩进：
    String indentedHtml = """
                              <html>
                                  <body>
                                      <p>Hello, Java!</p>
                                  </body>
                              </html>
                          """.indent(4); // 使用 String.indent() 方法增加整体缩进
    System.out.println(indentedHtml);
    // 输出 (每行前面有4个空格):
    //     <html>
    //         <body>
    //             <p>Hello, Java!</p>
    //         </body>
    //     </html>
    ```

2.  **转义序列 (Escape Sequences)**：
    *   传统的转义序列如 `\n` (换行), `\t` (制表符), `\\` (反斜杠), `\"` (双引号) 在文本块中**仍然有效**。但由于文本块本身就能很好地处理多行和引号，所以 `\n` 和 `\"` 的使用场景大大减少。
    *   **`\s`**：这个新的转义序列代表一个**空格 (space)**。当你希望在行尾保留一个或多个空格时，它非常有用，因为行尾的普通空格默认会被去除。
    *   **`\` (反斜杠在行尾)**：如果一行内容以 `\` 结束，那么这一行末尾的换行符将**不会**被包含在字符串中。这允许你将一个很长的逻辑行在源代码中分割成多行，而不影响最终字符串的单行形式。

    ```java
    String stringWithEscapes = """
                               Line 1\nLine 2 with a tab\t.
                               A line ending with two spaces\s\s
                               This is a very, very, very, very, very, very, \
                               very, very, very, very, very, very, very long line.
                               "Quotes" can be used directly.
                               """;
    System.out.println(stringWithEscapes);
    // 输出:
    // Line 1
    // Line 2 with a tab	.
    // A line ending with two spaces
    // This is a very, very, very, very, very, very, very, very, very, very, very, very, very long line.
    // "Quotes" can be used directly.
    ```

3.  **不支持字符串插值 (No String Interpolation)**：
    *   非常重要的一点：Java的文本块**本身不支持**像某些其他语言（如Kotlin的 `${variable}` 或 Python 的 f-string）那样的字符串内联变量或表达式求值（即字符串插值）。
    *   你仍然需要使用 `String.format()` 方法或者Java 15引入的实例方法 `String::formatted` 来将变量值嵌入到文本块中。

    ```java
    String name = "Duke";
    int version = 21;

    // 使用 String.format()
    String messageFormat = String.format("""
                                         Hello %s,
                                         Welcome to Java %d!
                                         """, name, version);
    System.out.println(messageFormat);

    // 使用 String.formatted() (Java 15+)
    String messageFormatted = """
                              Hello %s,
                              Welcome to Java %d!
                              """.formatted(name, version);
    System.out.println(messageFormatted);
    // 两者输出均为:
    // Hello Duke,
    // Welcome to Java 21!
    ```

**版本信息：**

*   **Java 13**: 作为预览特性首次引入 (JEP 355)。需要使用 `--enable-preview` 编译和运行。
*   **Java 14**: 第二次预览 (JEP 368)，进行了一些改进。同样需要 `--enable-preview`。
*   **Java 15**: **正式标准化** (JEP 378)。从 Java 15 开始，你可以直接使用文本块，无需任何特殊标志。

**好处总结：**

1.  **可读性大大增强**：代码更接近最终字符串的实际样子。
2.  **易于维护**：修改多行字符串内容（如 SQL、JSON）时，无需关心拼接和转义。
3.  **减少错误**：避免了因忘记 `\n` 或错误转义 `"` 导致的 bug。
4.  **非常适合嵌入其他语言的代码片段**：如 SQL 查询、JSON 对象、XML/HTML 结构、Shell 脚本等。

**使用建议：**

*   将起始的 `"""` 单独放在一行，内容从下一行开始，这样最清晰。
*   结尾的 `"""` 通常也建议单独一行，或者与最后一行内容左对齐，以明确表示文本块的结束和缩进基准。
*   利用文本块的智能缩进，使嵌入的代码片段在 Java 源码中也能保持良好的格式和可读性。

文本块是 Java 语言在提升开发者体验和代码可读性方面迈出的重要一步。一旦习惯了它，你可能就再也回不去手动拼接多行字符串的时代了！