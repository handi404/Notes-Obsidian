Java 21 中正式发布的、将 `switch` 语句和表达式的威力推向新高度的特性——**`switch` 的模式匹配 (Pattern Matching for `switch`)**。

---

### `switch` 的模式匹配

#### 1. 核心概念 (Core Concept)

`switch` 的模式匹配是 Java 14 中 `switch` 表达式和 Java 16 中 `instanceof` 模式匹配的**自然演进和融合**。它允许你在 `switch` 块中，不仅可以基于确切的值（`int`, `String`, `enum`），还可以基于**类型 (Type Pattern)** 和**类型守卫 (Guarded Pattern)** 进行匹配，并直接解构匹配到的对象。

**核心能力：**
1.  **类型模式 (Type Patterns):** `case TypePattern variable -> ...`
    *   直接在 `case` 标签中使用类型。如果 `switch` 的输入值是该类型或其子类型，则匹配成功。
    *   匹配成功后，自动将输入值转换为该类型，并赋值给模式变量。
    *   彻底消除了 `if (obj instanceof Type)` 和手动强制转换的冗余代码。
2.  **类型守卫 (Guarded Patterns):** `case TypePattern variable when condition -> ...`
    *   在类型模式的基础上，增加一个 `when` 子句来定义额外的布尔条件。只有类型匹配且条件为真时，才执行该 `case` 分支。
3.  **模式变量作用域：** 模式变量 (`variable`) 只在匹配成功的 `case` 分支内可见和可用。
4.  **穷尽性检查 (Exhaustiveness Checking):** 编译器能够更智能地检查 `switch` 表达式是否覆盖了所有可能的输入，从而减少 `default` 分支的需求（尤其对于密封类）。

**通俗比喻：**
*   **传统 `switch`：** 就像一个**智能分拣机**。你给它一个**箱子上的标签（确切的值）**，它会准确地把箱子扔到对应的通道里。
*   **`switch` 的模式匹配：** 就像一个**超级智能分拣机**。
    *   你给它一个**包裹 (Object)**。
    *   它可以根据包裹的**种类 (Type Pattern)**——“这是一个易碎品（`case GlassWare g -> ...`）”——来分拣。
    *   它还可以根据包裹的**种类和附加条件 (Guarded Pattern)**——“这是一个易碎品，并且它的尺寸大于 1 米（`case GlassWare g when g.size() > 1 -> ...`）”——来更精确地分拣。
    *   分拣完成后，包裹里的东西（模式变量 `g`）可以直接拿出来使用。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 21 之前 (The Pain Point):**
    在处理多态类型时，传统的 `switch` 毫无用武之地。你不得不使用冗长、嵌套的 `if-else if` 链，充斥着 `instanceof` 检查和强制类型转换：

    ```java
    // 旧写法：处理一个 Shape 对象
    public double calculatePerimeter(Object shape) {
        if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return 2 * Math.PI * c.radius();
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return 2 * (r.width() + r.height());
        } else if (shape instanceof Square) {
            Square s = (Square) shape;
            return 4 * s.side();
        } else {
            throw new IllegalArgumentException("Unknown shape: " + shape.getClass().getName());
        }
    }
    ```
    这段代码冗长、重复且容易出错。

*   **预览阶段 (Java 17 & 18 & 19 & 20):**
    `switch` 的模式匹配经历了四个预览版本，是 OpenJDK 历史上预览时间最长的特性之一，这体现了其复杂性和重要性，以及社区对其设计细节的深思熟虑。

*   **Java 21 (The Solution - 正式版):**
    `switch` 模式匹配的正式发布，极大地改变了处理多态数据的代码风格，使其变得简洁、安全、富有表现力，并且是 Java 实现**代数数据类型 (Algebraic Data Types, ADT)** 的关键一步。

    ```java
    // 新写法：使用 switch 模式匹配
    public double calculatePerimeter(Object shape) {
        return switch (shape) { // switch 表达式
            case Circle c -> 2 * Math.PI * c.radius(); // 类型模式
            case Rectangle r -> 2 * (r.width() + r.height());
            case Square s -> 4 * s.side();
            case null -> throw new NullPointerException("Shape cannot be null"); // null 模式
            default -> throw new IllegalArgumentException("Unknown shape: " + shape.getClass().getName());
        };
    }
    ```

#### 3. 代码示例 (Code Example)

首先，定义一些用于演示的 Record 和类（最好是密封的）：

```java
// 使用密封接口和 Record 来演示效果最佳
public sealed interface Shape permits Circle, Rectangle, Square {}
public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Square(double side) implements Shape {} // 假设 Square 也是一个独立的类

// 另一个非密封的类
public class Message {
    private String content;
    public Message(String content) { this.content = content; }
    public String getContent() { return content; }
}
```

现在，来看 `switch` 的模式匹配：

```java
public class PatternMatchingSwitchExample {

    public static String describeObject(Object obj) {
        // 作为 switch 表达式，必须覆盖所有可能情况
        return switch (obj) {
            case null -> "It's null"; // null 模式
            case String s && s.length() > 5 -> "Long String: " + s; // 带守卫的模式
            case String s -> "Short String: " + s; // 类型模式
            case Integer i -> "Integer: " + i;
            case Circle c -> "Circle with radius: " + c.radius();
            case Rectangle r -> "Rectangle with dimensions: " + r.width() + "x" + r.height();
            // 注意：Square 在 Rectangle 之后，如果 obj 是 Square，也会匹配到 Rectangle。
            // 顺序很重要！更精确的模式应该放在前面。
            case Square s -> "Square with side: " + s.side(); // 这个分支永远不会被匹配到 (在当前顺序下)
            case Message msg when msg.getContent().contains("error") -> "Error Message: " + msg.getContent(); // 模式守卫
            case Message msg -> "Generic Message: " + msg.getContent();
            default -> "Unknown type: " + obj.getClass().getName();
        };
    }

    // 更好的顺序：更具体的模式在前
    public static String describeShapeBetter(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle with radius: " + c.radius();
            case Square s -> "Square with side: " + s.side(); // 放在 Rectangle 前面
            case Rectangle r -> "Rectangle with dimensions: " + r.width() + "x" + r.height();
            // 由于 Shape 是密封接口，并且我们已经覆盖了所有 permits 的子类
            // 编译器会进行穷尽性检查，这里不需要 default 分支 (如果 switch 的输入是 Shape 类型)
            // 但是如果输入是 Object，或者其他非密封类型，还是需要 default 或 null
            // case null -> "Shape cannot be null"; // 如果输入是 Shape，则不会有 null
        };
    }

    public static void main(String[] args) {
        System.out.println(describeObject("Hello World")); // Long String
        System.out.println(describeObject("Hi"));         // Short String
        System.out.println(describeObject(123));          // Integer
        System.out.println(describeObject(new Circle(5))); // Circle
        System.out.println(describeObject(new Square(4))); // Rectangle (因为 Square 继承 Rectangle)
        System.out.println(describeObject(new Message("This is an error."))); // Error Message
        System.out.println(describeObject(new Message("Just a message."))); // Generic Message
        System.out.println(describeObject(null));         // It's null

        System.out.println("\n--- Better Shape Description ---");
        System.out.println(describeShapeBetter(new Circle(3)));
        System.out.println(describeShapeBetter(new Square(5))); // 现在能正确匹配 Square
        System.out.println(describeShapeBetter(new Rectangle(2, 3)));
    }
}
```

#### 4. 关键特性与注意事项

1.  **`null` 模式：** `case null -> ...` 是一个特殊的模式，用于处理 `switch` 表达式或语句中传入 `null` 的情况。这解决了之前 `switch` 无法直接处理 `null` 而会抛出 `NullPointerException` 的问题。
2.  **模式顺序很重要：** `case` 分支是从上到下按顺序匹配的。更具体的模式（如 `Square`）应该放在更通用的模式（如 `Rectangle` 或 `Object`）之前，否则可能永远无法匹配到。编译器在某些情况下会给出警告甚至错误。
3.  **穷尽性检查：**
    *   对于**密封类 (Sealed Classes)** 或 **枚举 (Enum)** 作为 `switch` 的输入，并且你覆盖了所有可能的子类型/枚举常量，编译器可以保证 `switch` 是穷尽的，从而**不再需要 `default` 分支**，除非你显式地处理了 `null` 值。
    *   对于非密封类或 `Object` 作为输入，`default` 分支或覆盖所有可能类型仍然是必需的。
4.  **模式变量的作用域：** 模式变量 (`c`, `r`, `s` 等) 只在其对应的 `case` 块中可见。
5.  **`when` 子句 (模式守卫)：** 允许你在类型匹配后添加额外的布尔条件。

#### 5. 扩展与应用 (Extension & Application)

*   **消除 `if-else if` 链：** 这是最直接、最普遍的用例，代码变得极其干净和易读。
*   **代数数据类型 (ADT)：** 结合密封类和 `record`，`switch` 的模式匹配是实现 ADT 的强大工具，让你可以用类型安全的方式处理数据变体。
*   **命令处理/事件分发：** 根据传入的命令或事件的类型，分发到不同的处理逻辑。
*   **解析器和解释器：** 在构建语法树或处理不同类型的节点时，模式匹配提供了优雅的解决方案。

#### 6. 要点与注意事项 (Key Points & Cautions)

1.  **可读性优先：** 虽然强大，但不要滥用。如果 `switch` 分支过多，代码仍可能变得复杂。考虑拆分方法或使用多态。
2.  **避免 `default` 隐藏错误：** 对于密封类，尽量利用穷尽性检查，不要随意添加 `default`，以免未来新增子类时，编译器无法警告你。
3.  **与 `instanceof` 模式匹配的区别：**
    *   `instanceof` 模式匹配：用于**单个**类型检查和转换。
    *   `switch` 模式匹配：用于处理**多个**不同类型，并在一个地方集中分派逻辑。

`switch` 的模式匹配是 Java 语言走向更现代、更函数式编程范式的关键里程碑。它不仅解决了长期以来的样板代码问题，更通过编译时的安全性保障，帮助开发者编写出更健壮、更易维护的多态处理代码。这是 Java 21 最值得学习和掌握的特性之一。