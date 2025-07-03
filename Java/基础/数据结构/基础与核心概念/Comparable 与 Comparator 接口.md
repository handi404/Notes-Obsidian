**`Comparable<T>` 与 `Comparator<T>` 接口**。

这两个接口是 Java 中定义对象排序规则的核心机制，广泛应用于数组排序 (`Arrays.sort()`)、集合排序 (`Collections.sort()`) 以及有序集合（如 `TreeSet`, `TreeMap`）和优先队列 (`PriorityQueue`)。

---

### `Comparable<T>` 与 `Comparator<T>` 接口

#### **1. `Comparable<T>` 接口 - 自然排序 (Natural Ordering)**

*   **目的 (What & Why)：**
    *   `Comparable<T>` 接口用于定义类的**自然排序**规则。当一个类实现了 `Comparable` 接口，意味着它的实例自身就具备了相互比较的能力。
    *   例如，`String` 类、`Integer` 类、`Date` 类等 JDK 内置的许多类都实现了 `Comparable` 接口，它们有自己默认的排序方式（字符串按字典序，数字按大小，日期按先后）。
*   **接口定义：**
    ```java
    package java.lang;

    public interface Comparable<T> {
        /**
         * 将此对象与指定对象进行比较以确定顺序。返回一个负整数、零或正整数，表示此对象分别小于、等于或大于指定对象。
		 * @param o 要比较的对象。
		 * @return 一个负整数、零或正整数，表示该对象
		 * 小于、等于或大于指定对象。
		 * @throws NullPointerException 如果指定对象为空
		 * @throws ClassCastException 如果指定对象的类型不允许
		 * 与该对象进行比较。
         */
        public int compareTo(T o);
    }
    ```
*   **`compareTo(T o)` 方法的约定：**
    *   **`this` < `o`**: 返回一个**负整数**。
    *   **`this` == `o`**: 返回 **零**。
    *   **`this` > `o`**: 返回一个**正整数**。
    *   **sgn(x.compareTo(y)) == -sgn(y.compareTo(x))**: 必须确保比较的对称性。如果 `x.compareTo(y)` 抛异常，则 `y.compareTo(x)` 也应该抛异常（或者表现出类似行为）。
    *   **传递性**: `(x.compareTo(y) > 0 && y.compareTo(z) > 0)` 蕴含 `x.compareTo(z) > 0`。
    *   **与 `equals` 的一致性 (推荐，但非强制)：** `(x.compareTo(y) == 0)` 应该与 `x.equals(y)` 具有相同的布尔值。如果一个类实现了 `Comparable` 但其 `compareTo` 方法与 `equals` 不一致（例如，`BigDecimal` 的 `compareTo` 忽略标度，而 `equals` 考虑标度），则在有序集合（如 `TreeSet`）中使用时要特别小心，因为这些集合使用 `compareTo` 来判断元素“相等性”。如果 `compareTo` 返回 0，有序集合会认为元素重复，即使 `equals` 返回 `false`。
    *   **`NullPointerException`**: 通常约定，如果 `o` 为 `null`，则抛出 `NullPointerException` (尽管这不是 `Object.equals` 的行为)。
    *   **`ClassCastException`**: 如果 `o` 的类型与 `this` 对象不兼容，无法比较，则抛出 `ClassCastException`。

*   **示例：**
    假设我们有一个 `Book` 类，我们希望它能按书名 (title) 进行自然排序。

    ```java
    public class Book implements Comparable<Book> {
        private String title;
        private String author;
        private int publicationYear;

        public Book(String title, String author, int publicationYear) {
            this.title = title;
            this.author = author;
            this.publicationYear = publicationYear;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public int getPublicationYear() { return publicationYear; }

        @Override
        public String toString() {
            return "Book{" +
                   "title='" + title + '\'' +
                   ", author='" + author + '\'' +
                   ", publicationYear=" + publicationYear +
                   '}';
        }

        // 实现 compareTo 方法，按书名升序排序
        @Override
        public int compareTo(Book other) {
            if (other == null) {
                throw new NullPointerException("Cannot compare Book to null");
            }
            // String 类本身实现了 Comparable
            return this.title.compareTo(other.title);
        }

        public static void main(String[] args) {
            Book book1 = new Book("Effective Java", "Joshua Bloch", 2017);
            Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 2006);
            Book book3 = new Book("Clean Code", "Robert C. Martin", 2008);

            System.out.println("book1.compareTo(book2): " + book1.compareTo(book2)); // "Effective Java" vs "Java..."
            System.out.println("book3.compareTo(book1): " + book3.compareTo(book1)); // "Clean Code" vs "Effective Java"

            List<Book> books = new ArrayList<>(List.of(book1, book2, book3));
            System.out.println("Before sorting: " + books);
            Collections.sort(books); // 使用 Book 类的自然排序
            System.out.println("After sorting by title (natural): " + books);
        }
    }
    ```

#### **2. `Comparator<T>` 接口 - 定制排序 (Custom/External Ordering)**

*   **目的 (What & Why)：**
    *   `Comparator<T>` 接口用于定义**外部的、定制化的排序规则**。当你无法修改类的源代码（比如它是第三方库的类），或者你需要多种不同的排序方式时，`Comparator` 就派上用场了。
    *   它将比较逻辑从被比较的类中分离出来，形成一个独立的比较器类。这是一种**策略模式 (Strategy Pattern)** 的体现。
*   **接口定义：**
    ```java
    package java.util;

    @FunctionalInterface // 表明它是一个函数式接口，可以用 lambda 表达式创建实例
    public interface Comparator<T> {
        /**
         * 比较其两个参数的大小顺序。返回一个负整数、零或正整数，具体取决于第一个参数是否小于、等于或大于第二个参数。
		 *
		 * @param o1 要比较的第一个对象。
		 * @param o2 要比较的第二个对象。
		 * @return 返回一个负整数、零或正整数，具体取决于
		 * 第一个参数是否小于、等于或大于第二个参数。
		 *
		 * @throws NullPointerException 如果参数为空且此比较器不允许空参数
		 *
		 * @throws ClassCastException 如果参数类型不允许此比较器进行比较
         */
        int compare(T o1, T o2);

        // Java 8+ 增加了很多 default 和 static 方法，如:
        // default Comparator<T> reversed() { ... }
        // default Comparator<T> thenComparing(Comparator<? super T> other) { ... }
        // static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) { ... }
        // ...等等
    }
    ```
*   **`compare(T o1, T o2)` 方法的约定：**
    *   与 `Comparable.compareTo()` 类似：
        *   `o1` < `o2`: 返回负整数。
        *   `o1` == `o2`: 返回零。
        *   `o1` > `o2`: 返回正整数。
    *   同样需要满足对称性、传递性。
    *   对于 `null` 的处理，取决于比较器的实现。有些比较器可能允许 `null`，有些则会抛 `NullPointerException`。Java 8+ 的 `Comparator.nullsFirst()` 和 `Comparator.nullsLast()` 可以帮助优雅地处理 `null` 值。
    *   与 `equals` 的一致性对于 `Comparator` 来说，如果它将在有序集合（如 `TreeSet`，构造时传入此 `Comparator`）中使用，则其 `compare(o1, o2) == 0` 的行为应与 `o1.equals(o2)` 一致，以避免混淆。

*   **示例 (使用 `Comparator` 为 `Book` 类提供多种排序方式)：**

    ```java
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;

    // Book 类定义同上，但可以不实现 Comparable 接口
    // public class Book { ... }

    // 比较器1：按出版年份升序
    class BookYearAscComparator implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) {
            // Integer.compare 静态方法是比较 int 的好方式
            return Integer.compare(b1.getPublicationYear(), b2.getPublicationYear());
        }
    }

    // 比较器2：按作者名字典序升序
    class BookAuthorAscComparator implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getAuthor().compareTo(b2.getAuthor());
        }
    }

    public class BookSorters {
        public static void main(String[] args) {
            Book book1 = new Book("Effective Java", "Joshua Bloch", 2017);
            Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 2006);
            Book book3 = new Book("Clean Code", "Robert C. Martin", 2008);
            Book book4 = new Book("Domain-Driven Design", "Eric Evans", 2003); // 新增一本，年份更早

            List<Book> books = new ArrayList<>(List.of(book1, book2, book3, book4));
            System.out.println("Original list: " + books);

            // 1. 使用按年份排序的比较器
            books.sort(new BookYearAscComparator()); // List.sort() 方法 (Java 8+)
            // Collections.sort(books, new BookYearAscComparator()); // 传统方式
            System.out.println("\nSorted by publication year (asc): " + books);

            // 2. 使用按作者排序的比较器 (lambda 表达式方式 - Java 8+)
            books.sort((b1, b2) -> b1.getAuthor().compareTo(b2.getAuthor()));
            System.out.println("\nSorted by author (asc) using lambda: " + books);

            // 3. 使用 Java 8 Comparator 静态工厂方法 (更简洁和强大)
            // 按出版年份降序
            books.sort(Comparator.comparingInt(Book::getPublicationYear).reversed());
            System.out.println("\nSorted by publication year (desc) using Comparator.comparingInt.reversed: " + books);

            // 多级排序：先按作者，再按出版年份
            books.sort(Comparator.comparing(Book::getAuthor)
                                 .thenComparingInt(Book::getPublicationYear));
            System.out.println("\nSorted by author, then by publication year (asc): " + books);

            // 处理 null 值 (假设 author 可能为 null)
            List<Book> booksWithNullAuthor = new ArrayList<>(books);
            booksWithNullAuthor.add(new Book("Unknown Title", null, 2020));

            // null 作者排在最前面
            booksWithNullAuthor.sort(Comparator.comparing(Book::getAuthor, Comparator.nullsFirst(String::compareTo)));
            System.out.println("\nSorted by author (nulls first): " + booksWithNullAuthor);
        }
    }
    ```

**3. Java 8+ 对 `Comparator` 的增强**

Java 8 极大地增强了 `Comparator` 接口，通过引入 `default` 方法和 `static` 工厂方法，使得创建和组合比较器变得非常方便和富有表现力：

*   **函数式接口 (`@FunctionalInterface`):** `Comparator` 只有一个抽象方法 `compare(T o1, T o2)`，因此可以用 lambda 表达式或方法引用来快速创建实例。
    ```java
    Comparator<Book> byTitleLambda = (b1, b2) -> b1.getTitle().compareTo(b2.getTitle());
    Comparator<Book> byYearLambda = (b1, b2) -> Integer.compare(b1.getPublicationYear(), b2.getPublicationYear());
    ```

*   **静态工厂方法 `Comparator.comparing(...)`:**
    *   `Comparator.comparing(Function<? super T, ? extends U> keyExtractor)`:
        *   `keyExtractor` 是一个函数，它从对象 `T` 中提取一个可比较的键 `U` (这个键 `U` 必须实现了 `Comparable` 接口)。
        *   例如：`Comparator.comparing(Book::getTitle)` (按书名排序，`Book::getTitle` 返回 `String`，`String` 是 `Comparable`)。
    *   `Comparator.comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)`:
        *   提取键后，使用指定的 `keyComparator` 来比较这些键。
        *   例如：`Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER)` (按作者忽略大小写排序)。
    *   `Comparator.comparingInt(ToIntFunction<? super T> keyExtractor)`
    *   `Comparator.comparingLong(ToLongFunction<? super T> keyExtractor)`
    *   `Comparator.comparingDouble(ToDoubleFunction<? super T> keyExtractor)`
        *   这些是针对基本类型键的优化版本，避免了自动装箱。例如：`Comparator.comparingInt(Book::getPublicationYear)`。

*   **`default` 方法用于组合和修改比较器：**
    *   `reversed()`: 返回一个与原比较器排序相反的比较器。
        ```java
        Comparator<Book> byYearDesc = Comparator.comparingInt(Book::getPublicationYear).reversed();
        ```
    *   `thenComparing(Comparator<? super T> other)`:
        *   多级排序。如果当前比较器认为两个元素相等 (返回0)，则使用 `other` 比较器进行二次比较。
        ```java
        Comparator<Book> byAuthorThenByYear = Comparator.comparing(Book::getAuthor)
                                                       .thenComparingInt(Book::getPublicationYear);
        ```
    *   `thenComparing(Function<? super T, ? extends U> keyExtractor)`: (类似 `comparing`，用于次级比较)
    *   `thenComparingInt`, `thenComparingLong`, `thenComparingDouble`
    *   `nullsFirst(Comparator<? super T> comparator)` (static method): 返回一个将 `null` 值视为小于非 `null` 值的比较器。如果两个值都非 `null`，则使用给定的 `comparator`。
    *   `nullsLast(Comparator<? super T> comparator)` (static method): 返回一个将 `null` 值视为大于非 `null` 值的比较器。
        ```java
        // 按作者排序，null 作者排在最后
        Comparator<Book> byAuthorNullsLast = Comparator.comparing(Book::getAuthor, Comparator.nullsLast(String::compareTo));
        // 如果 Book::getAuthor 可能返回 null，且 String::compareTo 不处理 null
        // 也可以这样写，让外层处理 null
        Comparator<Book> byAuthorNullsLastBetter = Comparator.nullsLast(Comparator.comparing(Book::getAuthor, Comparator.naturalOrder()));
        // 或者更简单：
        // Comparator.comparing(b -> b.getAuthor() == null ? null : b.getAuthor(), Comparator.nullsLast(Comparator.naturalOrder()))
        // 但通常推荐直接对keyExtractor返回的类型使用 nullsFirst/Last 包装其比较器。
        ```
    *   `naturalOrder()` (static method): 返回一个使用对象的自然顺序进行比较的比较器 (要求对象实现 `Comparable`)。
    *   `reverseOrder()` (static method): 返回一个使用对象的自然顺序的逆序进行比较的比较器。

**4. 何时使用 `Comparable` vs `Comparator`？**

*   **`Comparable`:**
    *   当类有一个主要的、**内在的**、**“自然”的**比较方式时，实现 `Comparable`。
    *   每个类只能有一个 `compareTo` 方法，所以只能定义一种自然排序。
    *   使类自身就“可排序”。
*   **`Comparator`:**
    *   当需要**多种不同的排序方式**时。
    *   当要排序的类**无法修改** (如第三方库的类) 或不希望它实现 `Comparable` 时。
    *   当希望将排序逻辑与业务对象**解耦**时。
    *   使用 Lambda 表达式或方法引用可以非常简洁地定义临时或特定的排序规则。

**总结与重要性：**

*   `Comparable` 和 `Comparator` 是 Java 中对象排序的基石。
*   `Comparable` 定义了对象的**自然排序**，类自身实现。
*   `Comparator` 定义了**定制排序**，作为外部策略，更加灵活。
*   Java 8+ 对 `Comparator` 的增强使得排序逻辑的编写极为便捷和强大。
*   理解它们对于使用有序集合 (如 `TreeSet`, `TreeMap`)、优先队列 (`PriorityQueue`) 以及对列表和数组进行排序至关重要。
*   在 `compareTo` 或 `compare` 方法中，务必处理好相等性 (返回 0) 的情况，因为它会影响有序集合中元素的“唯一性”判断。