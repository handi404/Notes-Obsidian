探讨一下 Java 中令人兴奋的特性——**模式匹配 (Pattern Matching)**。这是一个逐步增强的特性，旨在使处理复杂数据结构和条件逻辑的代码更加简洁、可读和安全。

目前，模式匹配主要体现在以下几个方面：

1.  **`instanceof` 的模式匹配 (Pattern Matching for `instanceof`)** (Java 16 正式特性)
2.  **`switch` 表达式和语句的模式匹配 (Pattern Matching for `switch`)** (Java 21 正式特性，预览版始于 Java 17)
3.  **记录模式 (Record Patterns)** (Java 21 正式特性，预览版始于 Java 19) (与解构相关)
4.  **数组模式 (Array Patterns)** (Java 21 预览特性，JEP 443，预计未来会成为正式特性)

我们逐个来看：

**一、`instanceof` 的模式匹配 (Pattern Matching for `instanceof`)**

**1. 老旧的方式 (Java 16 之前):**

在检查一个对象的类型并将其转换为该类型以便访问其成员时，我们通常需要这样做：

```java
Object obj = "Hello Java"; // 或者任何其他类型的对象

if (obj instanceof String) {
    String s = (String) obj; // 1. 类型检查
                            // 2. 显式类型转换
    System.out.println("Length: " + s.length()); // 3. 使用转换后的变量
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    System.out.println("Value: " + i.intValue());
}
```
这种写法有几个小问题：
*   **冗余**: `instanceof` 检查和类型转换是重复的。
*   **容易出错**: 如果忘记转换或转换到错误类型（虽然 `instanceof` 避免了 `ClassCastException`），代码会显得笨拙。
*   **作用域问题**: `s` 或 `i` 的作用域仅限于 `if` 块内部，这是好的，但声明和转换是分开的。

**2. 使用模式匹配 (Java 16+):**

`instanceof` 的模式匹配允许你在类型检查的同时声明一个绑定变量，如果检查成功，该变量会自动转换为目标类型，并且可以直接在该作用域内使用。

```java
Object obj = "Hello Java";

if (obj instanceof String s) { // 模式：String s
    // 如果 obj 是 String 类型，则 s 自动被赋值为 (String)obj
    // 并且 s 可以在这个 if 块（true 分支）内直接使用
    System.out.println("Length: " + s.length());
} else if (obj instanceof Integer i) {
    System.out.println("Value: " + i.intValue());
} else {
    System.out.println("Unknown type");
}
```

**关键点：**

*   **简洁性**: 一步完成类型检查、转换和变量声明。
*   **安全性**: 编译器确保类型正确，避免了手动转换的潜在错误。
*   **作用域 (Flow Scoping)**: 模式变量 (`s`, `i`) 的作用域被精确控制。
    *   在 `if (obj instanceof String s)` 中，`s` 仅在 `if` 的 true 分支中可用。
    *   在 `if (obj instanceof String s && s.length() > 5)` 中，`s` 在 `&&` 右侧的表达式以及 `if` 的 true 分支中可用。
    *   在 `if (!(obj instanceof String s))` 中，`s` 在 `if` 的 true 分支中不可用（因为条件是 `obj` 不是 `String`），但在 `else` 分支中可用（如果 `else` 分支存在并且逻辑上 `s` 能被明确赋值）。更常见的是 `if (obj instanceof String s) { ... } else { /* s 在这里不可用 */ }`。

**二、`switch` 表达式和语句的模式匹配 (Pattern Matching for `switch`)**

这是模式匹配一个更强大的应用，极大地增强了 `switch` 的能力。

**1. 老旧的 `switch` (只能用于特定原始类型、其包装类、`String` 和枚举):**

```java
// 假设 Shape 是一个接口，Circle, Rectangle 是实现类
// 老的 switch 无法直接作用于 Shape 类型
static String getTypeNameOld(Object obj) {
    if (obj instanceof String) {
        return "String";
    } else if (obj instanceof Integer) {
        return "Integer";
    } else {
        return "Unknown";
    }
}
```

**2. 使用模式匹配的 `switch` (Java 21+):**

现在 `switch` 可以直接对任意类型进行模式匹配，并在 `case` 标签中使用类型模式。

```java
sealed interface Shape permits Circle, Rectangle, Square {} // 密封类效果更好
final record Circle(double radius) implements Shape {}
final record Rectangle(double length, double width) implements Shape {}
final record Square(double side) implements Shape {}

static String getShapeDescription(Shape shape) {
    return switch (shape) { // switch 可以作用于 Shape 类型
        case Circle c    -> "A circle with radius " + c.radius(); // c 自动转换为 Circle
        case Rectangle r -> "A rectangle with length " + r.length() + " and width " + r.width();
        case Square s    -> "A square with side " + s.side();
        // 如果 Shape 不是密封的，或者没有覆盖所有 permits 的子类型，编译器会要求 default
        // default         -> "Unknown shape";
    };
}

public static void main(String[] args) {
    System.out.println(getShapeDescription(new Circle(5.0)));
    System.out.println(getShapeDescription(new Rectangle(3.0, 4.0)));
}
```

**关键特性：**

*   **类型模式 (`case Type variable`)**: `case Circle c` 会检查 `shape` 是否是 `Circle`，如果是，则将其转换为 `Circle` 并赋值给 `c`。
*   **`null` 处理**:
    *   默认情况下，如果 `switch` 的选择器表达式求值为 `null`，会抛出 `NullPointerException`。
    *   你可以显式添加 `case null` 来处理 `null` 值：
        ```java
        static String handleNulls(Object o) {
            return switch (o) {
                case String s  -> "String: " + s;
                case null      -> "It's null!"; // 处理 null
                default        -> "Something else";
            };
        }
        ```
*   **`when` 子句 (Guarded Patterns)**: 可以在 `case` 标签后添加一个布尔表达式（`when` 子句）来进一步细化匹配条件。只有当类型匹配且 `when` 子句为 `true` 时，该 `case` 才匹配。

    ```java
    static String processString(String s) {
        return switch (s) {
            case String str when str.length() > 10 -> "Long string: " + str;
            case String str when str.isEmpty()     -> "Empty string";
            case String str                        -> "Short string: " + str; // 默认String情况
            case null                              -> "Null string"; // 如果允许null
            // default -> ... // 如果s不是String (在此例中不可能，但通常需要)
        };
    }
    ```
*   **穷尽性检查 (Exhaustiveness)**:
    *   **对于 `switch` 表达式 (返回值)**，编译器会检查所有可能的值是否都被 `case` 覆盖。如果不是（例如，对于非密封类型，或者密封类型未覆盖所有子类），则必须提供 `default` 分支。
    *   对于 `switch` 语句，如果不是穷尽的，则不需要 `default`，但执行可能会“穿透”过去（如果没有匹配的 `case`）。
    *   与**密封类 (Sealed Classes)** 结合使用时尤其强大。如果 `switch` 的选择器是密封类型，并且你为所有 `permits` 的直接子类型都提供了 `case`，编译器就能确定 `switch` 是穷尽的，从而可能不需要 `default` 分支，这使得代码更安全（如果未来添加新的子类型，编译器会报错，提示你更新 `switch`）。

**三、记录模式 (Record Patterns)**

记录模式允许你在模式匹配中**解构 (destructure)** 记录实例，直接访问其组件。

**1. 不使用记录模式 (但使用 `instanceof` 模式匹配):**

```java
record Point(int x, int y) {}

void printPointOld(Object obj) {
    if (obj instanceof Point p) { // instanceof 模式匹配
        int x = p.x(); // 手动获取组件
        int y = p.y();
        System.out.println("Point: x = " + x + ", y = " + y);
    }
}
```

**2. 使用记录模式 (Java 21+):**

```java
record Point(int x, int y) {}

void printPointNew(Object obj) {
    if (obj instanceof Point(int x, int y)) { // 记录模式 Point(int x, int y)
        // x 和 y 自动从 Point 实例中解构出来并赋值
        System.out.println("Point: x = " + x + ", y = " + y);
    }
}

// 在 switch 中使用记录模式
static void processRecord(Object obj) {
    switch (obj) {
        case Point(int x, int y) -> System.out.println("Point: x = " + x + ", y = " + y);
        // 可以嵌套模式，例如一个记录包含另一个记录或特定类型
        // case Line(Point(int x1, int y1), Point(int x2, int y2)) -> ...
        default -> System.out.println("Not a Point");
    }
}
```

**关键点：**

*   **解构**: `Point(int x, int y)` 不仅检查类型，还将 `Point` 对象的 `x` 和 `y` 组件提取到局部变量 `x` 和 `y` 中。
*   **嵌套模式**: 记录模式可以嵌套。如果一个记录的组件是另一个记录，你可以递归地解构它们。
    ```java
    record ColoredPoint(Point p, String color) {}
    // ...
    if (obj instanceof ColoredPoint(Point(int x, int y), String c)) {
        System.out.println("Colored point at (" + x + "," + y + ") with color " + c);
    }
    ```
*   **类型推断 (`var`)**: 在记录模式中，组件的类型可以省略，使用 `var` 进行类型推断（但通常写出类型更清晰）。
    `case Point(var x, var y)`

**四、数组模式 (Array Patterns) (预览特性)**

JEP 443 (Java 21 预览) 引入了数组模式，允许解构数组。

```java
// 假设这是预览特性启用后的代码
static void processArray(Object obj) {
    switch (obj) {
        // 匹配一个包含两个元素的int数组，并将元素绑定到x和y
        case int[] {int x, int y} -> System.out.printf("Two ints: %d, %d%n", x, y);
        // 匹配一个String数组，第一个元素是 "start"，后续元素不关心
        case String[] {String s, ...} when s.equals("start") -> System.out.println("Starts with 'start'");
        // 匹配任意长度的String数组
        case String[] arr -> System.out.println("A string array of length " + arr.length);
        default -> System.out.println("Not a recognized array pattern");
    }
}
```
**关键点 (预览)：**
* 解构数组元素。
* 可以使用 `...` (rest pattern) 来匹配数组的剩余部分。
* 同样可以与 `when` 子句结合。

**总结模式匹配的好处：**

1.  **代码更简洁、可读性更高**: 减少了样板代码（如显式类型转换和 getter 调用）。
2.  **更安全**: 编译器进行更多检查（如穷尽性检查），减少了运行时错误（如 `ClassCastException`）。
3.  **更具表达力**: 能够以更声明式的方式表达复杂的条件逻辑和数据解构。
4.  **函数式风格**: 与记录类、密封类等特性结合，使得编写代数数据类型 (ADT) 及其处理逻辑更加自然，更接近函数式编程风格。

模式匹配是 Java 语言演进的一个重要方向，它使得 Java 在保持其面向对象特性的同时，吸收了函数式编程和其他现代语言的一些优秀思想，让开发者能编写出更优雅、更健壮的代码。随着后续 Java 版本的发布，我们可以期待模式匹配能力的进一步增强。