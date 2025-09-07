Java 15 中正式发布的、极大改善多行字符串编写体验的特性——**文本块 (Text Blocks)**。

---

### 文本块 (Text Blocks)

#### 1. 核心概念 (Core Concept)

文本块是一个**多行的字符串字面量**，它允许你在代码中以“所见即所得”的方式编写包含换行和缩进的字符串，而无需使用大量的转义字符（如 `\n`）和字符串拼接符（`+`）。

**核心语法：**
*   以**三个双引号 `"""`** 开始，后面必须跟一个**换行符**。
*   中间是字符串的内容，可以包含多行。
*   以**三个双引号 `"""`** 结束。

**通俗比喻：**
*   **传统字符串：** 就像用**一根很长的面条**来写一首诗。每写完一行，你都得手动把面条折一下（`\n`），如果诗里有引号，你还得特殊处理一下（`\"`）。整首诗写完，面条已经扭曲不堪。
*   **文本块：** 就像给你一张**稿纸**。你可以直接在上面写诗，换行、缩进都自然保留，诗写完是什么样，稿纸上就是什么样。清爽、直观。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 15 之前 (The Pain Point):**
    在 Java 中编写多行字符串是一件非常痛苦的事情，尤其是在嵌入代码片段（如 SQL, JSON, HTML, XML）时。代码会变得难以阅读和维护。

    **示例：编写一段 JSON**
    ```java
    // 旧写法：丑陋且易错
    String json = "{\n" +
                  "  \"name\": \"Java\",\n" +
                  "  \"version\": 15,\n" +
                  "  \"features\": [\n" +
                  "    \"Text Blocks\",\n" +
                  "    \"Records\"\n" +
                  "  ]\n" +
                  "}";
    ```
    这段代码充满了 `\n`、`+` 和手动的缩进，如果从别处复制粘贴一段 JSON 过来，需要花费大量时间进行格式化。

*   **预览阶段 (Java 13 & 14):**
    文本块经过了两个预览版本，根据社区的反馈进行了多次微调，主要集中在如何处理**缩进**和**尾部空格**上。

*   **Java 15 (The Solution - 正式版):**
    文本块正式成为 Java 语言的一部分，提供了优雅的解决方案。

    **示例：用文本块编写 JSON**
    ```java
    // 新写法：所见即所得
    String json = """
                  {
                    "name": "Java",
                    "version": 15,
                    "features": [
                      "Text Blocks",
                      "Records"
                    ]
                  }
                  """;
    ```
    这段代码不仅清晰易读，而且可以直接从 JSON 文件或 API 文档中复制粘贴，无需任何修改。

#### 3. 核心规则：缩进处理

文本块最精妙的地方在于它对**缩进**的智能处理。编译器会遵循以下两个步骤：

1.  **处理行终止符：** 将不同操作系统下的行终止符（`\r\n`, `\n`, `\r`）统一规范化为 `\n`。
2.  **去除附带的缩进 (Incidental White Space)：**
    *   找到所有**非空内容行**中**最左边**的那条线（即最小缩进）。
    *   将每一行的这条公共缩进都**剥离**掉。
    *   结束的 `"""` 标记的位置决定了这条线在哪里。如果把它向左移动，就会保留更多的缩进。

**代码示例：理解缩进**

```java
public class TextBlockExample {
    public static void main(String[] args) {
        // 示例 1: 基本用法
        String html = """
                      <html>
                          <body>
                              <p>Hello, World!</p>
                          </body>
                      </html>
                      """;
        // 结束的 """ 在第 22 列。
        // 内容行的最小缩进在第 22 列 (<html>)。
        // 所以所有行都向左移动 22 个空格。
        // 结果:
        // <html>
        //     <body>
        //         <p>Hello, World!</p>
        //     </body>
        // </html>
        System.out.println(html);

        // 示例 2: 通过移动结束符 """ 来控制整体缩进
        String indentedHtml = """
                              <html>
                                  <body>
                                      <p>Hello, World!</p>
                                  </body>
                              </html>
                          """; // 结束的 """ 在第 26 列
        // 内容行的最小缩进还是第 22 列。
        // 但由于结束符在第 26 列，所以整体向左移动 22 个空格，
        // 使得 <html> 没有任何前导空格。
        // 效果同上。

        String moreIndentedHtml = """
                                  <html>
                                      <body>
                                          <p>Hello, World!</p>
                                      </body>
                                  </html>
                                """; // 结束的 """ 向左移动到第 16 列
        // 最小缩进是第 16 列 (结束符的位置)。
        // 所有内容行都向左移动 16 个空格。
        // 结果会保留 4 个空格的额外缩进。
        //    <html>
        //        <body>
        //            <p>Hello, World!</p>
        //        </body>
        //    </html>
        System.out.println("--- More Indented ---");
        System.out.println(moreIndentedHtml);
    }
}
```

#### 4. 新的转义序列

文本块引入了两个新的转义序列：

*   **`\` (反斜杠在行尾):** **取消换行**。这在你有一行非常长的文本，但为了代码可读性想把它分成多行书写时非常有用。
    ```java
    String longText = """
                      This is a very long line of text that I want to \
                      write on multiple lines in my source code, but \
                      it should be treated as a single line in the final string.
                      """;
    // 结果: "This is a very long line of text that I want to write on multiple lines in my source code, but it should be treated as a single line in the final string."
    System.out.println(longText);
    ```

*   **`\s`:** 表示一个**单个的普通空格** (`U+0020`)。这主要用来**保留行尾的空格**，因为文本块默认会去除行尾的附带空格。
    ```java
    String colors = """
                    red  \s
                    green\s
                    blue \s
                    """;
    // 结果: "red  \ngreen\nblue " (保留了行尾的空格)
    // 如果没有 \s，结果会是 "red\ngreen\nblue"
    ```

#### 5. 扩展与应用 (Extension & Application)

文本块的用武之地非常广泛：

*   **SQL 查询语句：** 在代码中嵌入格式化的 SQL 语句，可读性大大提升。
*   **JSON / XML / HTML 片段：** 单元测试中的 Mock 数据，或者在代码中构建这些格式的字符串。
*   **Shell 脚本或任何配置文本：** 在 Java 程序中生成或包含其他语言的脚本代码。
*   **代码生成器：** 在生成代码的模板中使用文本块。

#### 6. 要点与注意事项 (Key Points & Cautions)

1.  **起始的 `"""` 后面必须是换行符：** `String s = """Hello""";` 是非法的。必须是 `String s = """\nHello""";`。
2.  **结束的 `"""` 可以和内容在同一行：** `String s = """Hello"""` 是合法的，但通常为了可读性，会把它放在新的一行。
3.  **智能的缩进管理是关键：** 理解“公共缩进剥离”的规则是掌握文本块的核心。通常，将结束的 `"""` 与你希望的最外层内容的缩进对齐，就能得到你想要的结果。
4.  **变量插值：** 文本块本身**不直接支持**变量插值（像 Groovy 或 Kotlin 那样）。你仍然需要使用 `String.format()` 或新的 `String::formatted` 方法。
    ```java
    String name = "Java";
    int version = 15;
    String formattedJson = """
                         {
                           "name": "%s",
                           "version": %d
                         }
                         """.formatted(name, version);
    ```
5.  **Windows 用户注意：** 你无需担心 `\r\n` 的问题，文本块会自动将其处理为 `\n`。

总结来说，文本块是 Java 语言在提升开发者“生活质量”方面迈出的坚实一步。它彻底解决了多行字符串的编写难题，让代码更干净、更直观、更易于维护。对于任何需要处理嵌入式文本的场景，它都是不二之选。