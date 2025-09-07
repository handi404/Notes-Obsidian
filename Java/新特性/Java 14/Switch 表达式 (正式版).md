Java 14 中正式发布的、极大提升代码表达力和安全性的重要特性——**Switch 表达式 (Switch Expressions)**。

---

### Switch 表达式 (Switch Expressions)

#### 1. 核心概念 (Core Concept)

在 Java 14 之前，`switch` 只是一个**语句 (Statement)**，它控制程序的执行流程，但不直接产生一个值。

Java 14 引入了 `switch` **表达式 (Expression)**。表达式和语句最大的区别是：**表达式会计算并返回一个值**。

`switch` 表达式带来了两大核心改进：

1.  **返回值能力：** `switch` 可以像三元运算符 `(a ? b : c)` 一样，根据条件计算并返回一个结果，你可以直接将其赋值给一个变量。
2.  **新的 `case` 语法：** 引入了 `case L -> ...` 的箭头语法，它有以下特点：
    *   **无需 `break`：** 箭头右边的代码执行完毕后，`switch` 会自动结束，彻底解决了传统 `switch` 语句中因忘记写 `break` 而导致的“穿透 (fall-through)”问题。
    *   **作用域更清晰：** 箭头右边只能是一个表达式、一个代码块或一个 `throw` 语句。

**通俗比喻：**
*   **传统 `switch` 语句：** 就像一个**老式电话接线员**。接到一个请求 (`case`)，他手动把线插到一个插孔里，然后**必须手动拔出来 (`break`)**，再去接下一个。如果忘了拔，电话就会串线（**穿透**）。他只负责接线（控制流程），不产出任何东西。
*   **`switch` 表达式：** 就像一个**自动问答机器人**。你问它一个问题（`switch` 的输入值），它根据内部知识库（`case` 分支），直接**给出一个答案（返回值）**。每个答案都是独立的，不会混淆。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 14 之前 (The Pain Point):**
    传统 `switch` 语句存在几个臭名昭著的问题：
    *   **易错的 `break`：** 忘记 `break` 是一个非常常见且难以发现的 bug 来源。
    *   **代码冗余：** 为了给同一个变量在不同 `case` 中赋值，你需要写很多重复的 `variable = ...; break;`。
    *   **作用域混乱：** 在 `case` 块中定义的变量，其作用域会延伸到整个 `switch` 块，容易引发冲突。
    *   **啰嗦：** 整体结构显得非常笨重和模板化。

    ```java
    // 旧写法：计算一个月份有多少天
    int month = 2;
    int days;
    switch (month) {
        case 1:
        case 3:
        case 5:
        // ...
            days = 31;
            break;
        case 4:
        case 6:
        // ...
            days = 30;
            break;
        case 2:
            days = 28; // 不考虑闰年
            break;
        default:
            throw new IllegalArgumentException("Invalid month");
    }
    // `days` 在这里使用
    ```

*   **预览阶段 (Java 12 & 13):**
    `switch` 表达式经过了两个版本的预览，社区反馈非常好。在预览期间，用于从 `case` 块返回值的是 `break value;` 语法。

*   **Java 14 (The Solution - 正式版):**
    Java 14 正式确定了最终的语法，并引入了 `yield` 关键字来替代预览版中的 `break value;`，以避免歧义。

    ```java
    // 新写法：使用 switch 表达式
    int month = 2;
    int days = switch (month) {
        case 1, 3, 5, 7, 8, 10, 12 -> 31; // 多 case 合并
        case 4, 6, 9, 11 -> 30;
        case 2 -> 28;
        default -> throw new IllegalArgumentException("Invalid month");
    };
    // `days` 在这里直接被赋值
    ```
    代码量减少了，逻辑更清晰，并且 `default` 分支保证了 `switch` 表达式的**穷尽性**，即必须覆盖所有可能的情况。

#### 3. 代码示例 (Code Example)

`switch` 表达式有两种主要的语法形式：

##### **1. 箭头语法 (`case ... -> ...`) - 最常用**

```java
public enum Day { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }

public class SwitchExpressionExample {
    public static void main(String[] args) {
        Day day = Day.MONDAY;

        // 示例 1: 直接返回一个值
        int numLetters = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case TUESDAY -> 7;
            case THURSDAY, SATURDAY -> 8;
            case WEDNESDAY -> 9;
            // 对于 enum，如果覆盖了所有情况，可以省略 default
        };
        System.out.println("Number of letters: " + numLetters);

        // 示例 2: 箭头右边是一个代码块，使用 `yield` 返回值
        boolean isWeekend = switch (day) {
            case SATURDAY, SUNDAY -> true;
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> {
                System.out.println("It's a weekday.");
                // yield 用于从块中返回一个值
                yield false;
            }
        };
        System.out.println("Is it weekend? " + isWeekend);
    }
}
```

##### **2. 传统冒号语法 (`case ...: yield ...`)**

你依然可以在 `switch` 表达式中使用传统的冒号语法，但这时你需要使用 `yield` 语句来返回值。**这种写法不常用**，主要用于需要“穿透”逻辑的罕见场景。

```java
// 不太推荐的写法，仅作演示
String text = switch (day) {
    case MONDAY:
        System.out.println("Start of the week.");
        yield "Work day";
    case SATURDAY:
    case SUNDAY:
        yield "Weekend";
    default:
        yield "Midweek day";
};
```

#### 4. 扩展与应用 (Extension & Application)

*   **取代复杂的 `if-else if-else` 链：** 当你需要根据一个变量的多个可能值来赋给另一个变量时，`switch` 表达式是 `if-else` 链的完美替代品。
*   **函数式编程：** `switch` 表达式本身就是一个值，可以很自然地用在函数式编程的流式调用中，或者作为方法的返回值。
    ```java
    public String getDayType(Day day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> "Weekend";
            default -> "Weekday";
        };
    }
    ```
*   **Java 17+ 模式匹配：** `switch` 表达式是未来 `switch` 模式匹配（Pattern Matching）的基础。在 Java 21 中，`switch` 表达式已经可以根据对象的类型和属性进行匹配，其能力得到了极大的增强。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **穷尽性 (Exhaustiveness):** 这是 `switch` 表达式的一个**编译时**安全保障。编译器会检查你是否覆盖了输入值的所有可能性。
    *   对于 `enum` 类型，你必须覆盖所有枚举常量，或者提供一个 `default` 分支。
    *   对于 `String`, `int` 等类型，你**必须**提供 `default` 分支。
    *   这消除了“忘记处理某个情况”的运行时 bug。

2.  **`yield` 关键字：** `yield` **不是**一个新的通用关键字，它是一个**上下文关键字**，只在 `switch` 表达式的 `case` 块中有特殊含义。它用于从一个代码块中“产出”一个值作为 `switch` 表达式的结果。

3.  **不能混合使用两种语法：** 在同一个 `switch` 块中，你不能混合使用 `->` 和 `:`。
    ```java
    // switch (day) {
    //     case MONDAY -> 6;
    //     case TUESDAY: yield 7; // COMPILE ERROR!
    // }
    ```

4.  **`case` 分支的返回值类型必须一致：** `switch` 表达式的所有可能 `case` 分支（包括 `throw` 语句）必须能产生兼容的类型，否则编译器会报错。

5.  **`switch` 语句依然存在：** `switch` 表达式并没有取代传统的 `switch` 语句。当你不需要返回值，只是想根据条件执行不同的代码块（并且可能需要利用“穿透”特性）时，传统 `switch` 语句仍然是合法的选择。

总结而言，`switch` 表达式是 Java 语言现代化进程中的一大步。它让 `switch` 结构从一个笨拙、易错的控制流语句，蜕变成了一个简洁、安全、表达力强的函数式工具。在任何需要根据条件返回值的新代码中，都应优先使用 `switch` 表达式。