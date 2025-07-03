好聊聊 Java 中的**密封类 (Sealed Classes)**。这是一个在 Java 17 正式成为标准特性（预览版始于 Java 15）的强大功能，它允许你更精确地控制类的继承。

**一、什么是密封类 (Sealed Classes)？**

简单来说，**密封类或密封接口**允许你显式地声明哪些类或接口可以直接继承或实现它。这就像给你的类/接口家族上了一把锁，只有拿到“钥匙”（在 `permits` 子句中列出）的特定子类/实现类才能成为其直系后代。

**核心思想**：限制继承，形成一个已知的、有限的类层次结构。

**二、为什么需要密封类？**

在密封类出现之前，如果你想创建一个只有特定子类能继承的父类，通常会采取一些变通方法：

1.  **包级私有构造函数 (Package-private constructor) 和 `final` 子类**：父类构造函数设为包级私有，所有子类和父类放在同一个包下，并且子类声明为 `final`。这能限制继承，但不够直观，且有局限性。
2.  **枚举 (Enums)**：对于一组固定的、行为相似的常量实例很有效，但如果每个“实例”需要有更复杂的独立状态和行为，枚举就不太灵活。

密封类提供了更清晰、更强大的语言级支持来解决这个问题，主要带来了以下好处：

1.  **更精确的领域建模**：当你设计的模型中，某个概念只有有限的几种具体表现形式时，密封类能完美地表达这种约束。例如，一个图形系统中的 `Shape` 可能只有 `Circle`, `Rectangle`, `Triangle` 这几种。
2.  **增强的编译器检查**：这是密封类最强大的优势之一，尤其与**模式匹配 (Pattern Matching)** 结合使用时。编译器可以知道一个密封类型的所有可能的直接子类型。
3.  **提升代码可读性和可维护性**：开发者可以清晰地看到一个类的继承范围，减少了意外扩展带来的风险。

**三、如何使用密封类？**

1.  **声明密封类/接口**：
    使用 `sealed` 修饰符声明父类或父接口。
    使用 `permits` 关键字列出所有允许的直接子类或实现类。

    ```java
    // 密封接口 Shape，只允许 Circle, Rectangle, Square 实现它
    public sealed interface Shape permits Circle, Rectangle, Square {
        double area(); // 示例方法
    }
    ```

2.  **声明允许的子类/实现类**：
    所有在 `permits` 子句中列出的子类/实现类必须满足以下条件之一：
    *   **`final`**：该子类不能再被继承。这是最常见的情况，代表层级结构的叶子节点。
    *   **`sealed`**：该子类也必须是密封的，它需要声明自己允许的子类（继续受控继承）。
    *   **`non-sealed`**：该子类打破了密封性，可以被任何其他类自由继承。这表示这个分支的继承是开放的。

    并且，这些子类必须与密封的父类在**同一个模块**中；如果父类在未命名的模块中（即传统的 classpath 项目），则子类必须在**同一个包**中。

    ```java
    // 1. final 子类
    public final class Circle implements Shape {
        private final double radius;
        public Circle(double radius) { this.radius = radius; }
        @Override public double area() { return Math.PI * radius * radius; }
    }

    // 2. non-sealed 子类 (可以被自由继承)
    public non-sealed class Rectangle implements Shape {
        private final double length, width;
        public Rectangle(double length, double width) {
            this.length = length;
            this.width = width;
        }
        @Override public double area() { return length * width; }
    }

    // 例如，一个 FancyRectangle 可以继承 Rectangle
    public class FancyRectangle extends Rectangle {
        public FancyRectangle(double length, double width) { super(length, width); }
        // ... 其他特性
    }

    // 3. sealed 子类 (继续受控继承)
    public sealed class Square implements Shape permits ColoredSquare {
        private final double side;
        public Square(double side) { this.side = side; }
        @Override public double area() { return side * side; }
    }

    // Square 的 final 子类
    public final class ColoredSquare extends Square {
        private final String color;
        public ColoredSquare(double side, String color) {
            super(side);
            this.color = color;
        }
        public String getColor() { return color; }
    }
    ```

**四、密封类与模式匹配 (Pattern Matching)**

密封类的最大威力体现在与Java 17+ 中的模式匹配（尤其是 `switch` 表达式和语句的模式匹配，JEP 441 in Java 21 使其成为正式特性）结合使用时。

当 `switch` 用于一个密封类型时，编译器可以检查 `case` 标签是否**穷尽了所有允许的子类型**。

```java
public class ShapeProcessor {
    public static void describeShape(Shape shape) {
        // Java 21+ 的 switch 模式匹配
        String description = switch (shape) {
            case Circle c    -> "A circle with radius " + c.radius; // c.radius 可直接访问
            case Rectangle r -> "A rectangle with length " + r.length + " and width " + r.width;
            case Square s    -> "A square with side " + s.side;
            // 注意：因为 Shape 是密封的，并且我们处理了所有 permits 的类型，
            // 所以编译器知道这是穷尽的，不需要 default 分支！
            // 如果 Shape 是 non-sealed，或者我们没有处理所有 permits 类型，
            // 编译器会要求一个 default 分支或抛出 MatchException。
        };
        System.out.println(description);
    }

    public static void main(String[] args) {
        describeShape(new Circle(5.0));
        describeShape(new Rectangle(4.0, 6.0));
        describeShape(new ColoredSquare(3.0, "Red")); // ColoredSquare 也是 Square
    }
}
```

**优势**：
*   **编译时安全**：如果未来给 `Shape` 添加了一个新的允许子类型（例如 `Triangle`），并且忘记在 `switch` 中处理它，编译器会报错，强制你处理新情况。
*   **无需 `default` 分支**：当所有密封子类型都被覆盖时，`default` 分支通常是不必要的，这使得代码更简洁，意图更明确。

**五、知识扩展与应用**

1.  **与记录类 (Records) 的结合**：
    密封类/接口常与记录类（Java 16 正式特性）结合使用，尤其是在定义代数数据类型 (ADT) 或对数据进行建模时。记录类非常适合作为密封层次结构中的 `final` 叶子节点。

    ```java
    public sealed interface JsonValue permits JsonObject, JsonArray, JsonString, JsonNumber, JsonBoolean, JsonNull {
    }

    public record JsonObject(java.util.Map<String, JsonValue> members) implements JsonValue {}
    public record JsonArray(java.util.List<JsonValue> elements) implements JsonValue {}
    public record JsonString(String value) implements JsonValue {}
    public record JsonNumber(java.math.BigDecimal value) implements JsonValue {}
    public record JsonBoolean(boolean value) implements JsonValue {}
    public record JsonNull() implements JsonValue {} // 单例可以用常规类实现
    ```

2.  **API 设计**：
    当你的库/框架提供一个基类或接口，并且只希望用户从预定义的一组类中进行选择或扩展时，密封类非常有用。它清晰地传达了 API 的设计意图。

3.  **状态机**：
    可以用密封接口表示状态，用实现该接口的类表示具体状态，这样状态转换和处理就更类型安全。

4.  **Implicit `permits` Clause (隐式 `permits` 子句)**：
    如果一个密封类的所有允许的直接子类都定义在与密封类**同一个源文件**中，那么 `permits` 子句可以省略，编译器会自动推断。但为了清晰，显式声明通常更好，尤其是在不同文件中时。

5.  **与 `abstract` 结合**：
    密封类本身可以是 `abstract` 的。`sealed abstract class Vehicle permits Car, Truck;`

**六、注意事项和最佳实践**

*   **选择合适的子类修饰符**：
    *   大多数情况下，叶子节点用 `final`。
    *   当你希望某个分支的继承继续受控时，用 `sealed`。
    *   当你确实希望某个分支对外部开放，允许未知扩展时，用 `non-sealed`。要谨慎使用 `non-sealed`，因为它会削弱密封性带来的穷尽性检查等好处。
*   **简洁性**：密封类旨在简化对类层次结构的推理，不要过度使用或创建不必要的复杂密封层次。
*   **模块化**：注意 `permits` 子句中类与密封父类的模块/包关系。

**总结一下：**

密封类是Java语言一个非常受欢迎的增强，它通过限制继承来提高代码的健壮性、可读性和领域建模的精确性。其真正的威力在与模式匹配（尤其是 `switch`）结合使用时得以充分展现，使得处理固定类型的变体集合变得更加安全和优雅。它是现代 Java 开发中一个值得掌握的重要特性。