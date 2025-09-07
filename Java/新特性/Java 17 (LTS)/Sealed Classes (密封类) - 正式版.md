Java 17 中正式发布的、用于增强面向对象建模能力的强大特性——**Sealed Classes (密封类)**。

---

### Sealed Classes (密封类)

#### 1. 核心概念 (Core Concept)

**密封类（或接口）允许你精确地控制一个类可以被哪些其他的类（或接口）继承或实现。**

在 Java 17 之前，对于一个类（非 `final`），任何在同一个包或不同包中的类都可以继承它。你只有两个极端的选择：
1.  **`public class MyClass {...}`:** 允许**任何类**继承它（过于开放）。
2.  **`public final class MyClass {...}`:** **禁止任何类**继承它（过于封闭）。

`sealed` 关键字提供了一个介于两者之间的“中间地带”。它让你能像列清单一样，明确声明：“我的这个类，**只允许**清单上的这几个指定的类来继承。”

**核心语法：**
*   **父类（密封类）：** 使用 `sealed` 关键字，并通过 `permits` 关键字列出所有允许的直接子类。
*   **子类：** 每个被 `permits` 列出的子类，必须满足以下三个条件之一：
    1.  声明为 **`final`**：表示这个继承分支到此为止，不能再被继承。
    2.  声明为 **`sealed`**：表示它将继承体系的限制继续向下传递，它自己也必须有一个 `permits` 列表。
    3.  声明为 **`non-sealed`**：表示这个继承分支“开封”，回归到传统的开放继承模式，任何类都可以继承它。

**通俗比喻：**
*   **传统继承：** 就像一个**公共俱乐部**。任何人都可以申请成为会员（继承）。
*   **`final` 类：** 就像一个**私人住宅**。谢绝访客，谁也不能进来。
*   **`sealed` 类：** 就像一个**有邀请名单的派对**。你在派对门口（`sealed class`）贴了一张**邀请函名单 (`permits list`)**。只有名单上的人（指定的子类）才能进入。进来之后，这个人可以选择：
    *   自己玩，不再邀请别人（`final`）。
    *   自己也办一个更小范围的派对，并列出他自己的邀请名单（`sealed`）。
    *   把门打开，让他的所有朋友都能进来（`non-sealed`）。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 17 之前 (The Pain Point):**
    *   **无法对继承体系建模：** 当你想表达“一个图形要么是圆形，要么是矩形，要么是三角形，再无其他可能”这样的业务约束时，Java 语言层面无法提供支持。任何人都可能创建出一个 `Pentagon` (五边形) 类来继承 `Shape`，破坏了你设计的封闭世界。
    *   **对 `switch` 不友好：** 在 `switch` 中处理一个父类型时，你必须写一个 `default` 分支，因为编译器不知道未来是否会出现新的子类。

*   **预览阶段 (Java 15 & 16):**
    密封类经过了两个版本的预览，社区对其在领域驱动设计 (DDD) 和代数数据类型 (ADT) 建模方面的潜力给予了高度评价。

*   **Java 17 (The Solution - 正式版):**
    密封类正式成为 Java 的一部分，为 Java 的类型系统带来了更强的表达力和安全性。

*   **现代化应用：**
    *   **精确的领域建模：** 非常适合用于定义一组固定的、已知的子类型。例如，支付方式（信用卡、支付宝、微信）、API 响应（成功、失败、加载中）等。
    *   **模式匹配的完美搭档：** 密封类与 `switch` 模式匹配（Java 21 中正式发布）结合使用时，威力巨大。因为编译器知道所有可能的子类型，所以**当你在 `switch` 中覆盖了所有 `permits` 的子类时，就不再需要 `default` 分支**。这提供了一种编译时的完备性检查，是密封类最强大的应用场景。

#### 3. 代码示例 (Code Example)

让我们用经典的“图形”例子来演示。

```java
// 1. 定义一个密封接口 Shape
// 它只允许 Circle, Rectangle, 和 Square 实现
public sealed interface Shape permits Circle, Rectangle, Square {
    double area();
}

// 2. 定义子类，每个子类必须是 final, sealed 或 non-sealed
//    Circle 是 final 的，继承分支结束
public final class Circle implements Shape {
    private final double radius;

    public Circle(double radius) { this.radius = radius; }

    @Override
    public double area() { return Math.PI * radius * radius; }

    public double radius() { return radius; } // 使用 record 风格的访问器
}

// Rectangle 是 non-sealed 的，回归开放继承
public non-sealed class Rectangle implements Shape {
    private final double width, height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() { return width * height; }
}

// Square 是 final 的，继承分支也结束
// 注意：Square 是一个 Record，Record 默认是 final 的，所以满足条件
public record Square(double side) implements Shape {
    @Override
    public double area() { return side * side; }
}

// (可选) 任何类都可以继承 non-sealed 的 Rectangle
class BorderedRectangle extends Rectangle {
    // ...
    public BorderedRectangle(double w, double h) { super(w, h); }
}

// (编译错误) Triangle 不在 Shape 的 permits 列表中，不能实现它
// public final class Triangle implements Shape { ... } // COMPILE ERROR!
```

**与 `switch` 模式匹配结合 (Java 21 语法):**

```java
public class SealedClassExample {
    public static double calculateArea(Shape shape) {
        // 因为 Shape 是密封的，并且我们覆盖了所有允许的子类型，
        // 所以编译器知道没有其他可能性，不需要 default 分支。
        return switch (shape) {
            case Circle c -> c.area();
            case Rectangle r -> r.area();
            case Square s -> s.area();
            // NO default NEEDED!
        };
    }
    
    public static void main(String[] args) {
        Shape circle = new Circle(10);
        System.out.println("Area: " + calculateArea(circle));
    }
}
```

#### 4. 语法要点

*   **文件位置：** 如果 `permits` 列出的子类文件很小，你可以把它们定义在同一个源文件中。在这种情况下，父类可以**省略 `permits` 子句**，编译器会自动推断。
*   **包关系：** 密封类和它的直接子类必须在**同一个包**内。如果子类在不同的 `.java` 文件中，它们必须在同一个模块的同一个包里。
*   **接口也可以密封：** 如示例所示，`sealed` 关键字同样适用于 `interface`。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **目的不是访问控制：** `sealed` 与 `public`, `protected`, `private` 无关。它控制的是**可继承性 (inheritability)**，而不是**可访问性 (accessibility)**。
2.  **`final`, `sealed`, `non-sealed` 是强制性的：** 每一个直接子类都必须明确选择这三个关键字之一。
3.  **Record 是隐式 `final` 的：** `record` 类天然满足密封类的子类要求，因为它们不能被继承。
4.  **`non-sealed` 的权衡：** 使用 `non-sealed` 会失去密封性带来的好处，比如在 `switch` 中免除 `default`。只有当你确实希望一个继承分支是开放的时，才应该使用它。
5.  **密封类是现代 Java 设计模式的基石：** 它们是实现**代数数据类型 (ADT)** 的一种方式，这在函数式编程中是一个核心概念。通过将 `sealed` 类/接口与 `record` 结合，你可以用非常简洁和安全的方式来建模复杂的数据结构。

总而言之，密封类是一个面向架构师和库设计者的强大工具。它通过在语言层面提供对继承体系的精确控制，使得代码模型更贴近业务领域，更健壮，也为强大的模式匹配功能提供了坚实的基础。