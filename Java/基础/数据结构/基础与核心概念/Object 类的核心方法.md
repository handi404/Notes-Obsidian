**`Object` 类的核心方法—— `equals(Object obj)` 和 `hashCode()`**。

这两个方法对于 Java 中对象的比较、以及 `Set` 和 `Map` 等集合的正确运作至关重要。

---

### `Object` 类的 `equals(Object obj)` 和 `hashCode()`

`java.lang.Object` 类是 Java 中所有类的根父类。它定义了一些所有对象都应该具备的基本方法，其中 `equals()` 和 `hashCode()` 对我们理解数据结构尤为重要。

#### **1. `equals(Object obj)` 方法**

*   **目的 (What & Why)：**
    *   `equals()` 方法用于比较两个对象是否“相等”。
    *   **默认实现 (在 `Object` 类中)：** `Object` 类中的 `equals()` 方法默认是比较两个对象的**内存地址**是否相同，等价于使用 `==` 操作符比较两个引用。
        ```java
        // Object.java 源码 (简化示意)
        public boolean equals(Object obj) {
            return (this == obj);
        }
        ```
    *   **为什么需要重写：** 对于大多数类，我们通常关心的是对象的**内容**或**逻辑状态**是否相等，而不是它们是否指向内存中的同一个实例。例如，我们希望两个内容相同的 `String` 对象是相等的，即使它们是不同的实例。因此，很多类（如 `String`, `Integer`, `Date` 以及集合类）都重写了 `equals()` 方法来实现基于内容的比较。

*   **重写 `equals()` 的五个基本约定 (JLS - Java Language Specification)：**
    当你在自定义类中重写 `equals()` 方法时，必须遵守以下五个约定，以保证其行为符合预期，特别是与集合框架一起使用时：

    1.  **自反性 (Reflexive):** 对于任何非 `null` 的引用值 `x`，`x.equals(x)` 必须返回 `true`。
        *   简单来说，对象必须等于它自己。
    2.  **对称性 (Symmetric):** 对于任何非 `null` 的引用值 `x` 和 `y`，当且仅当 `y.equals(x)` 返回 `true` 时，`x.equals(y)` 必须返回 `true`。
        *   如果 `x` 等于 `y`，那么 `y` 也必须等于 `x`。
        *   **常见陷阱：** 在继承关系中，如果子类 `equals()` 方法试图与父类对象比较，而父类 `equals()` 方法不知道子类的特定字段，可能会破坏对称性。例如，如果 `Point.equals(Object)` 和 `ColorPoint.equals(Object)` (其中 `ColorPoint extends Point`) 的实现不当。
            ```java
            // 错误的对称性示例
            class Point {
                int x, y;
                // ... constructor, getters ...
                @Override public boolean equals(Object o) {
                    if (!(o instanceof Point)) return false;
                    Point p = (Point)o;
                    return p.x == x && p.y == y;
                }
            }
            class ColorPoint extends Point {
                Color color;
                // ... constructor, getters ...
                @Override public boolean equals(Object o) {
                    if (!(o instanceof ColorPoint)) return false; // 问题点1: 如果 o 是 Point，直接 false
                    return super.equals(o) && ((ColorPoint)o).color == color;
                }
            }
            // Point p = new Point(1, 2);
            // ColorPoint cp = new ColorPoint(1, 2, Color.RED);
            // p.equals(cp) -> true (ColorPoint is a Point, ColorPoint.equals 会被跳过，执行 Point.equals)
            // cp.equals(p) -> false (p 不是 ColorPoint 的实例)
            // 对称性被破坏!
            ```
            **正确做法：** 通常建议使用 `getClass()` 进行精确类型匹配，或者在组合优于继承的场景下避免这个问题。对于继承场景下的 `equals`，推荐使用 Joshua Bloch 在《Effective Java》中提出的方法（通常是 final `equals` 或委托给一个规范表示）。
    3.  **传递性 (Transitive):** 对于任何非 `null` 的引用值 `x`、`y` 和 `z`，如果 `x.equals(y)` 返回 `true` 且 `y.equals(z)` 返回 `true`，那么 `x.equals(z)` 必须返回 `true`。
        *   如果 `x` 等于 `y`，`y` 等于 `z`，那么 `x` 必须等于 `z`。
        *   **常见陷阱：** 同样在继承关系中，如果不同子类添加了不同的“重要”字段，并试图进行混合比较，容易破坏传递性。
    4.  **一致性 (Consistent):** 对于任何非 `null` 的引用值 `x` 和 `y`，只要 `equals` 比较操作中涉及的对象信息没有被修改，多次调用 `x.equals(y)` 应该始终返回 `true` 或始终返回 `false`。
        *   即，`equals()` 的结果不应随机变化，除非对象的比较字段发生了改变。
    5.  **非空性 (Non-nullity):** 对于任何非 `null` 的引用值 `x`，`x.equals(null)` 必须返回 `false`。
        *   对象不能等于 `null`。

*   **`equals()` 实现步骤与最佳实践：**

    1.  **使用 `==` 检查参数是否为 `this` 的引用：** 如果是，直接返回 `true`。这是一种性能优化。
        ```java
        if (this == o) return true;
        ```
    2.  **使用 `instanceof` 检查参数类型是否正确 (或者 `getClass()`):**
        *   **`instanceof` 方式：** `if (!(o instanceof MyClass))`。允许子类对象与父类对象比较（如果子类没有添加新的影响相等的字段）。
        *   **`getClass()` 方式：** `if (o == null || getClass() != o.getClass())`。要求比较的两个对象必须是完全相同的类。这通常更容易满足对称性和传递性，尤其是在有继承且子类添加了影响相等性的状态时。
        *   **选择：** 《Effective Java》建议，如果你编写的类是 `final` 的，或者你确信其 `equals` 语义不会在子类中改变，可以使用 `instanceof`。否则，使用 `getClass()` 通常更安全，以避免破坏对称性。
        ```java
        // instanceof 方式
        if (!(o instanceof Point)) return false;
        Point other = (Point) o;

        // getClass() 方式
        // if (o == null || getClass() != o.getClass()) return false;
        // Point other = (Point) o; // 如果前面 getClass 检查通过，这里强转是安全的
        ```
    3.  **将参数转换为正确的类型：** 在 `instanceof` 或 `getClass()` 检查之后，将 `Object` 类型的参数强制转换为你的类类型。
    4.  **比较关键字段 (Significant Fields)：** 对类中所有影响对象逻辑相等的“关键”字段进行比较。
        *   **基本类型字段：** 使用 `==` 比较 (如 `int`, `boolean`, `char`)。
        *   **浮点类型字段 (`float`, `double`):** 使用 `Float.compare(f1, f2)` 或 `Double.compare(d1, d2)`，因为 `float` 和 `double` 有 `NaN` 和正负零等特殊值，直接用 `==` 比较 `NaN` 会是 `false`。
        *   **对象引用字段：** 递归调用这些字段的 `equals()` 方法。如果字段可能为 `null`，需要进行 `null` 检查。Java 7+ 提供了 `java.util.Objects.equals(a, b)` 辅助方法，它可以优雅地处理 `null`。
        *   **数组字段：** 不能直接用数组的 `equals()` 方法 (它继承自 `Object`，比较引用)。应使用 `java.util.Arrays.equals()` (针对一维数组) 或 `java.util.Arrays.deepEquals()` (针对多维数组)。
        *   **顺序无关的字段：** 如果字段的顺序不影响相等性 (例如，一个 `Set` 类型的字段)，需要更复杂的比较逻辑，确保内容相同即可。
        *   **派生字段：** 通常不需要比较可以通过其他关键字段计算出来的派生字段，除非这个计算非常耗时，而派生字段被缓存了。
    5.  **编写完成后，问自己：它是否满足对称性、传递性、一致性？**

*   **示例 (`Point` 类)：**

    ```java
    import java.util.Objects;

    public final class Point { // 标记为 final，使用 instanceof 更安全些
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            // 1. 自引用检查
            if (this == o) return true;

            // 2. null 检查和类型检查 (使用 instanceof，因为 Point 是 final)
            // 如果 Point 不是 final，且子类可能添加影响相等的字段，则 getClass() 更佳
            if (!(o instanceof Point)) return false;
            // if (o == null || getClass() != o.getClass()) return false; // 或者用 getClass()

            // 3. 类型转换
            Point point = (Point) o;

            // 4. 关键字段比较
            return x == point.x && y == point.y;
            // 或者使用 Objects.equals() 处理可能为 null 的对象字段，但这里 x,y 是基本类型
        }

        // hashCode() 方法稍后讨论，但必须与 equals() 一起重写
        @Override
        public int hashCode() {
            return Objects.hash(x, y); // Java 7+ 简单方便的方式
        }

        public static void main(String[] args) {
            Point p1 = new Point(1, 2);
            Point p2 = new Point(1, 2);
            Point p3 = new Point(2, 3);
            Point p4 = p1;

            System.out.println("p1.equals(p2): " + p1.equals(p2)); // true
            System.out.println("p1.equals(p3): " + p1.equals(p3)); // false
            System.out.println("p1.equals(p4): " + p1.equals(p4)); // true
            System.out.println("p1.equals(null): " + p1.equals(null)); // false
            System.out.println("p1.equals(\"text\"): " + p1.equals("text")); // false
        }
    }
    ```

#### **2. `hashCode()` 方法**

*   **目的 (What & Why)：**
    *   `hashCode()` 方法返回该对象的哈希码值 (一个 `int` 类型的值)。
    *   这个哈希码主要用于**基于哈希的集合**，如 `HashSet`, `HashMap`, `LinkedHashSet`, `LinkedHashMap`, `ConcurrentHashMap` 等。
    *   这些集合使用哈希码来确定对象在内部数据结构（通常是哈希表）中的存储位置（桶/bucket），从而实现快速的查找、插入和删除 (平均 O(1) 复杂度)。

*   **`hashCode()` 与 `equals()` 的约定 (非常重要！)：**
    这是 `Object` 类规范中最重要的约定之一：
    1.  **一致性 (during execution):** 在 Java 应用程序执行期间，如果一个对象用于 `equals` 比较的信息没有被修改，那么对该对象多次调用 `hashCode()` 方法必须始终返回相同的整数。这个整数在程序的两次不同执行之间不必保持一致。
    2.  **`equals` 相等则 `hashCode` 必须相等：** 如果两个对象根据 `equals(Object)` 方法比较是相等的，那么对这两个对象中的每个对象调用 `hashCode()` 方法都必须产生相同的整数结果。
        *   **这是核心！如果违反这条，基于哈希的集合将无法正常工作。** 例如，如果你把一个对象 `obj1` 放入 `HashSet`，然后试图用一个与 `obj1` `equals` 但 `hashCode` 不同的 `obj2` 去查找或删除，`HashSet` 可能找不到 `obj2`，因为它会先根据 `obj2` 的 `hashCode` 去错误的桶里查找。
    3.  **`equals` 不相等则 `hashCode` 不必不相等 (但推荐不相等)：** 如果两个对象根据 `equals(Object)` 方法比较是不相等的，那么对这两个对象中的每个对象调用 `hashCode()` 方法，不要求必须产生不同的整数结果。但是，程序员应该意识到，为不相等的对象产生不同的整数结果，有可能提高哈希表的性能（减少哈希冲突）。

*   **默认实现 (在 `Object` 类中)：**
    *   `Object` 类的 `hashCode()` 方法通常是根据对象的内存地址（或与之相关的值）计算的。因此，对于不同的对象实例，即使它们的内容可能相同（按自定义 `equals()`），默认的 `hashCode()` 也会返回不同的值。
    *   **所以，如果你重写了 `equals()` 方法来比较对象内容，你几乎总是需要同时重写 `hashCode()` 方法，以确保满足上述第二条约定。**

*   **重写 `hashCode()` 的指导原则与技巧：**
    目标是为不相等的对象生成尽可能分散的哈希码，以减少哈希冲突。

    1.  **选择一个非零的初始值常量：** 例如，`17` 或 `31` (这两个是常见的素数)。
        ```java
        int result = 17;
        ```
    2.  **对于每个影响 `equals()` 比较的关键字段 `f`，计算一个哈希码 `c`：**
        *   **`boolean`:** `(f ? 1 : 0)`
        *   **`byte`, `char`, `short`, `int`:** `(int) f`
        *   **`long`:** `(int) (f ^ (f >>> 32))` (将高32位和低32位异或，让高位也参与运算)
        *   **`float`:** `Float.floatToIntBits(f)`
        *   **`double`:** `Double.doubleToLongBits(d)`，然后按 `long` 处理 `(int) (l ^ (l >>> 32))`。
        *   **对象引用字段:**
            *   如果字段为 `null`，则使用 `0`。
            *   否则，递归调用该字段的 `hashCode()` 方法。
            *   **注意：** 如果对象的 `hashCode()` 计算依赖于一个可变的对象字段，而这个字段在对象放入哈希集合后发生了改变，那么对象的 `hashCode()` 也会改变，这将导致在集合中找不到该对象。因此，**用于计算 `hashCode` 的字段应该是不可变的，或者至少在对象位于哈希集合中时不能改变。** 这也是为什么通常推荐使用不可变对象作为 `Map` 的键或 `Set` 的元素。
        *   **数组字段:**
            *   对数组中的每个元素计算哈希码，然后组合起来。
            *   使用 `java.util.Arrays.hashCode(array)` (针对基本类型数组和对象数组，但对象数组是浅哈希) 或 `java.util.Arrays.deepHashCode(Object[])` (针对多维数组或元素需要深度哈希的对象数组)。

    3.  **将每个字段的哈希码 `c` 合并到 `result` 中：**
        ```java
        result = prime * result + c;
        ```
        其中 `prime` 通常是一个奇素数，如 `31`。使用素数有助于更好地分散哈希值。`31` 是一个特别好的选择，因为 `31 * i` 可以被优化为 `(i << 5) - i`，位移运算比乘法快。

    4.  **返回 `result`。**

*   **Java 7+ 的 `Objects.hash(...)` 辅助方法：**
    `java.util.Objects` 类提供了一个静态的 `hash(Object... values)` 方法，它可以方便地为给定的字段序列计算哈希码。它内部处理了 `null` 值，并使用了与上述原则类似的计算方式。
    ```java
    // 在 Point 类中
    @Override
    public int hashCode() {
        // x 和 y 是 int，可以直接传入
        return Objects.hash(x, y);
    }
    ```
    对于更复杂的字段，你可能仍然需要先计算它们的哈希码再传入 `Objects.hash()`，或者直接手动实现。

*   **示例 (`Point` 类的 `hashCode`)：**
    ```java
    // 之前已展示，使用 Objects.hash()
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // 手动实现版本 (类似 Objects.hash 的逻辑)
    // @Override
    // public int hashCode() {
    //     int result = 17; // 或 1
    //     result = 31 * result + x; // x 是 int
    //     result = 31 * result + y; // y 是 int
    //     return result;
    // }
    ```

**总结与重要性：**

*   **`equals()` 定义了对象的逻辑相等性。**
*   **`hashCode()` 为基于哈希的集合服务。**
*   **核心规则：`equals()` 相等，`hashCode()` 必须相等。**
*   **重写 `equals()` 时，几乎总是要重写 `hashCode()`。**
*   不正确的 `equals()` 和 `hashCode()` 实现会导致 `HashSet`, `HashMap` 等集合出现难以预料的行为（如元素丢失、重复添加等）。
*   IDE (如 IntelliJ IDEA, Eclipse) 通常可以自动生成符合规范的 `equals()` 和 `hashCode()` 方法，但理解其原理仍然重要，以便在需要时进行调整或审查。
*   对于作为 `Map` 键或 `Set` 元素的对象，**强烈推荐使用不可变对象 (Immutable Objects)**，或者至少确保用于 `equals` 和 `hashCode` 计算的字段在对象存入集合后不再改变。

---

与泛型相同，`Object` 类的 `equals()` / `hashCode()` 也是理解 Java 集合框架运作方式的基石。