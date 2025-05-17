这是 Java 近年来引入的一个非常实用的特性，旨在减少创建简单数据载体类（Data Carrier Class）时的样板代码。

**`record` 类 (自 Java 14 预览，Java 16 正式发布)**

1.  **是什么？为什么需要它？**
    *   在 `record` 出现之前，如果你想创建一个简单的类来封装数据，比如一个 `Point` 类包含 `x` 和 `y` 坐标，你需要手动编写：
        *   私有 final 字段 (`private final int x; private final int y;`)
        *   构造函数 (`public Point(int x, int y)`)
        *   getter 方法 (`public int getX()`, `public int getY()`)
        *   `equals()` 方法
        *   `hashCode()` 方法
        *   `toString()` 方法
        这些代码非常冗长且容易出错。
    *   `record` 就是为了解决这个问题而生的。它是一种特殊的类，主要目的是**透明地持有不可变数据**。编译器会自动为你生成上述所有样板代码。

2.  **如何定义？**
    非常简洁！你只需要声明 `record` 的名称和它的组件（components），也就是它所持有的数据字段。

    ```java
    // 定义一个简单的 Point record
    public record Point(int x, int y) {
        // 就这么简单！
    }
    ```

3.  **编译器为你做了什么？**
    对于上面的 `Point` record，编译器会自动生成：
    *   **私有 final 字段**：`private final int x;` 和 `private final int y;`。这意味着 `record` 实例默认是（浅）不可变的。
    *   **全参构造函数 (Canonical Constructor)**：`public Point(int x, int y)`，用于初始化所有字段。
    *   **公共访问器方法 (Accessor Methods)**：名称与字段名相同，例如 `public int x()` 和 `public int y()`。(注意：不是传统的 `getX()` `getY()` 格式，虽然你也可以自定义添加这些)。
    *   **`equals()` 方法**：比较两个 `Point` 实例是否所有组件都相等。
    *   **`hashCode()` 方法**：基于所有组件的值计算哈希码。
    *   **`toString()` 方法**：返回一个包含 `record` 名称和所有组件及其值的字符串，如 `Point[x=1, y=2]`。

4.  **使用示例：**

    ```java
    public class RecordDemo {
        public static void main(String[] args) {
            Point p1 = new Point(10, 20);
            Point p2 = new Point(10, 20);
            Point p3 = new Point(30, 40);

            // 访问器
            System.out.println("p1.x: " + p1.x()); // 输出: p1.x: 10
            System.out.println("p1.y: " + p1.y()); // 输出: p1.y: 20

            // toString()
            System.out.println("p1: " + p1);     // 输出: p1: Point[x=10, y=20]

            // equals()
            System.out.println("p1.equals(p2): " + p1.equals(p2)); // 输出: true
            System.out.println("p1.equals(p3): " + p1.equals(p3)); // 输出: false

            // hashCode()
            System.out.println("p1.hashCode(): " + p1.hashCode());
            System.out.println("p2.hashCode(): " + p2.hashCode());
            System.out.println("p3.hashCode(): " + p3.hashCode());
        }
    }
    ```

5.  **`record` 的一些重要特性和限制：**
    *   **隐式 `final`**：`record` 类本身是隐式 `final` 的，不能被继承。
    *   **不能继承其他类**：`record` 隐式继承自 `java.lang.Record`（类似于 `enum` 继承自 `java.lang.Enum`），因此不能再继承其他类。
    *   **可以实现接口**：`record` 可以实现一个或多个接口。
    *   **不能声明实例字段**：除了与 `record` 组件对应的私有 `final` 字段外，不能声明其他实例字段。
    *   **可以声明静态字段和静态方法**。
    *   **可以声明实例方法**。
    *   **紧凑构造函数 (Compact Constructor)**：可以声明一个没有参数列表的构造函数，用于在标准构造函数执行前进行参数校验或规范化。
        ```java
        public record Range(int start, int end) {
            // 紧凑构造函数
            public Range { // 注意：没有 (int start, int end) 参数列表
                if (start > end) {
                    throw new IllegalArgumentException("Start cannot be greater than end");
                }
                // 这里的 start 和 end 指的是构造函数的参数，在紧凑构造函数体执行完毕后，
                // 这些值（可能已被修改）会被赋给 record 的同名字段。
            }
        }
        ```
    *   **自定义规范构造函数 (Canonical Constructor)**：虽然编译器会自动生成，但你也可以显式提供一个与 `record` 组件签名完全匹配的构造函数，这时编译器就不再生成默认的了。通常只有在需要非常特殊的初始化逻辑时才这样做。
    *   **（浅）不可变性**：`record` 的字段是 `final` 的，这使得 `record` 实例本身是不可变的。但如果 `record` 的组件是可变对象（例如 `List`），那么 `record` 只是浅不可变的（即 `List` 引用本身不变，但 `List` 内部的内容可以变）。

**`record` 嵌套 `record`**

`record` 可以像普通类一样嵌套在其他类或接口中，当然也包括嵌套在另一个 `record` 中。

1.  **如何定义？**
    直接在外部类或 `record` 内部定义即可。

    ```java
    // 外部 Record
    public record Order(String orderId, CustomerInfo customer, ProductInfo product) {

        // 嵌套 Record (隐式静态)
        public record CustomerInfo(String customerId, String name, Address address) {
            // CustomerInfo 也可以有自己的方法或紧凑构造函数
        }

        // 另一个嵌套 Record (隐式静态)
        public record ProductInfo(String productId, String productName, double price) {
            // ...
        }

        // 嵌套 Record 也可以嵌套更深层的 Record (隐式静态)
        public record Address(String street, String city, String zipCode) {
            // ...
        }

        // 外部 Record 也可以有自己的方法
        public double calculateTotalPrice() {
            return product.price(); // 假设没有数量和折扣
        }
    }
    ```

2.  **关键点：**
    *   **隐式静态**：当一个 `record` (或普通类/接口) 嵌套在另一个顶层类或 `record` 中时，如果它不是内部类（即没有 `inner` 关键字，`record` 不能是 `inner` 的），它默认是**隐式静态**的。这意味着你不需要外部 `record` 的实例来创建嵌套 `record` 的实例。
    *   **命名空间**：嵌套有助于组织代码和提供命名空间，使得相关的纯数据结构聚合在一起。
    *   **访问性**：遵循标准的 Java 访问修饰符规则（`public`, `protected`, `private`, 包私有）。

3.  **使用示例：**

    ```java
    public class NestedRecordDemo {
        public static void main(String[] args) {
            // 创建嵌套 record 实例
            Order.Address shippingAddress = new Order.Address("123 Main St", "Anytown", "12345");
            Order.CustomerInfo customer = new Order.CustomerInfo("CUST001", "Alice Smith", shippingAddress);
            Order.ProductInfo product = new Order.ProductInfo("PROD007", "Awesome Widget", 29.99);

            // 创建外部 record 实例
            Order myOrder = new Order("ORDER_XYZ", customer, product);

            System.out.println(myOrder);
            // 输出类似:
            // Order[orderId=ORDER_XYZ, customer=CustomerInfo[customerId=CUST001, name=Alice Smith, address=Address[street=123 Main St, city=Anytown, zipCode=12345]], product=ProductInfo[productId=PROD007, productName=Awesome Widget, price=29.99]]

            System.out.println("Customer Name: " + myOrder.customer().name()); // Customer Name: Alice Smith
            System.out.println("Shipping City: " + myOrder.customer().address().city()); // Shipping City: Anytown
            System.out.println("Total Price: " + myOrder.calculateTotalPrice()); // Total Price: 29.99
        }
    }
    ```

**总结**

*   `record` 是 Java 中创建不可变数据载体类的现代化、简洁方式，极大地减少了样板代码。
*   它自动提供构造函数、访问器、`equals()`、`hashCode()` 和 `toString()`。
*   `record` 可以嵌套定义，嵌套的 `record` (在顶层类或另一 `record` 中) 是隐式静态的。
*   这使得构建复杂但清晰的数据模型变得更加容易，特别适用于 API 响应、DTO（数据传输对象）、配置对象等场景。