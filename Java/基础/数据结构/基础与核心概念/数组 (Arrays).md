虽然数组在 Java 中是一种基础的数据结构，但对其特性、内存分配以及 `java.util.Arrays` 工具类的熟练运用，是理解更复杂数据结构（如 `ArrayList`）和高效编程的基础。

---

### 数组 (Arrays)

#### **1. 什么是数组？ (What & Why)**

*   **定义：** 数组是一种用于存储**固定大小**的、**相同类型**元素的**连续内存空间**的数据结构。
    *   **固定大小 (Fixed-size):** 一旦数组被创建，其长度就不能改变。如果需要动态调整大小，通常会考虑使用 `ArrayList` 等集合类。
    *   **相同类型 (Homogeneous):** 数组中的所有元素必须是同一种数据类型，可以是基本数据类型（如 `int`, `double`, `char`）或引用数据类型（如 `String`, 自定义对象）。
    *   **连续内存空间 (Contiguous memory):** 数组元素在内存中是紧密排列的，这使得通过索引访问元素非常高效。
*   **为什么使用数组？**
    *   **高效的随机访问：** 由于元素在内存中是连续存储的，可以通过索引直接计算出元素的内存地址，因此**访问任何元素的时间复杂度都是 O(1)**。
    *   **简单性：** 数组的概念直观，易于理解和使用。
    *   **多维数组：** Java 支持多维数组（实质上是数组的数组），可以用来表示矩阵、表格等结构。
    *   **与底层硬件交互：** 数组的内存布局与计算机硬件的内存管理方式较为接近，有时在性能敏感的底层编程中会用到。

#### **2. 数组的声明、创建与初始化**

*   **声明数组引用：**
    ```java
    dataType[] arrayRefVar;  // 推荐方式 (符合 Java 习惯)
    // 或者
    dataType arrayRefVar[];  // C/C++ 风格，在 Java 中也合法但不推荐
    ```
    例如：
    ```java
    int[] numbers;
    String[] names;
    MyClass[] objects;
    ```
    **注意：** 此时只是声明了一个数组引用变量，它还没有指向任何实际的数组对象，其值为 `null`。

*   **创建数组对象 (分配内存)：**
    使用 `new` 关键字来创建数组并指定其长度。
    ```java
    arrayRefVar = new dataType[arraySize];
    ```
    例如：
    ```java
    numbers = new int[10];      // 创建一个包含 10 个 int 元素的数组
    names = new String[5];    // 创建一个包含 5 个 String 引用的数组
    objects = new MyClass[3]; // 创建一个包含 3 个 MyClass 引用的数组
    ```
    *   **内存分配：**
        *   `numbers` 引用本身存储在栈上（如果是局部变量）或堆上（如果是实例变量）。
        *   `new int[10]` 在堆内存中分配了一块足够容纳 10 个 `int` 值的连续空间。
        *   对于基本类型数组 (如 `int[]`)，数组元素直接存储值。
        *   对于引用类型数组 (如 `String[]`, `MyClass[]`)，数组元素存储的是对象的引用 (内存地址)，实际的对象本身仍在堆上的其他位置。
    *   **默认初始化值：** 当数组被创建时，其元素会被自动初始化为对应类型的默认值：
        *   数值类型 (byte, short, int, long, float, double): `0` 或 `0.0`
        *   `char`: `\u0000` (空字符)
        *   `boolean`: `false`
        *   引用类型 (String, 自定义对象等): `null`

*   **初始化数组元素：**
    1.  **逐个赋值：**
        ```java
        numbers[0] = 10;
        numbers[1] = 20;
        // ...
        names[0] = "Alice";
        names[1] = new String("Bob"); // 或者 "Bob"
        ```
    2.  **使用数组初始化器 (Array Initializer) - 在声明时或创建时直接赋值：**
        这种方式会同时声明、创建并初始化数组。长度由初始化值的数量决定。
        ```java
        int[] scores = {90, 85, 92, 78}; // 长度为 4
        String[] days = {"Monday", "Tuesday", "Wednesday"}; // 长度为 3

        // 也可以与 new 关键字结合 (不常用，但合法)
        // int[] data = new int[]{1, 2, 3, 4, 5};
        ```
        **注意：** 如果是分开声明和初始化，则不能直接用 `{...}` 形式，必须用 `new dataType[]{...}`。
        ```java
        int[] moreScores;
        // moreScores = {1, 2, 3}; // 编译错误！
        moreScores = new int[]{1, 2, 3}; // 正确
        ```

#### **3. 数组的基本操作**

*   **访问元素：** 通过索引 (从 `0` 开始) 访问。
    ```java
    int firstNumber = numbers[0];
    String secondName = names[1];
    ```
    如果索引越界 (小于 `0` 或大于等于 `array.length`)，会抛出 `ArrayIndexOutOfBoundsException`。

*   **获取数组长度：** 使用数组的 `length` **属性** (注意，不是方法 `length()`)。
    ```java
    int size = numbers.length; // size 为 10
    ```

*   **遍历数组：**
    1.  **传统 `for` 循环：**
        ```java
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Element at index " + i + ": " + numbers[i]);
        }
        ```
    2.  **增强 `for` 循环 (For-Each Loop) - Java 5+：**
        更简洁，用于只读访问或简单修改（对于引用类型，修改的是引用的副本，但可以通过副本修改对象状态）。
        ```java
        for (String name : names) {
            System.out.println("Name: " + name);
        }
        ```
        **注意：** 增强 `for` 循环不能用于在遍历过程中修改基本类型数组的元素值（因为 `name` 是一个值的拷贝），也不能获取当前元素的索引。

    3.  **Java 8+ Stream API：**
        ```java
        Arrays.stream(numbers).forEach(System.out::println);
        // 或者
        IntStream.of(numbers).forEach(num -> System.out.println("Number: " + num));
        ```

#### **4. 数组的特性与注意事项**

*   **数组是对象：** 在 Java 中，数组本身是对象。它们继承自 `java.lang.Object`，因此可以使用 `Object` 类的方法，如 `toString()` (默认输出不友好，类似 `[I@hashcode` for `int[]`)、`equals()` (默认比较引用)、`hashCode()` (默认基于引用)、`clone()` (浅拷贝)。
*   **`clone()` 方法：**
    *   数组的 `clone()` 方法执行的是**浅拷贝 (shallow copy)**。
    *   对于基本类型数组，浅拷贝会复制所有元素的值，得到一个新的数组，两个数组互不影响。
    *   对于引用类型数组，浅拷贝会复制所有元素的引用，得到一个新的数组，但新旧数组的元素指向的是**同一批对象**。修改新数组元素所指向对象的状态，会影响旧数组。
    ```java
    int[] originalInts = {1, 2, 3};
    int[] clonedInts = originalInts.clone();
    clonedInts[0] = 100; // originalInts[0] 仍然是 1

    StringBuilder[] originalBuilders = {new StringBuilder("A"), new StringBuilder("B")};
    StringBuilder[] clonedBuilders = originalBuilders.clone();
    clonedBuilders[0].append("X"); // originalBuilders[0] 的内容也变成了 "AX"
    clonedBuilders[1] = new StringBuilder("C"); // originalBuilders[1] 仍然指向 "B"
    ```
*   **多维数组 (Multidimensional Arrays)：**
    *   实质上是“数组的数组”。例如，二维数组 `int[][] matrix;` 是一个 `int[]` 类型的数组，它的每个元素又是一个 `int[]`。
    *   每一维的长度可以不同 (不规则数组或锯齿数组 jagged arrays)。
    ```java
    int[][] matrix = new int[3][4]; // 3 行 4 列
    matrix[0][0] = 1;

    int[][] jaggedArray = new int[3][];
    jaggedArray[0] = new int[2]; // 第 0 行有 2 个元素
    jaggedArray[1] = new int[4]; // 第 1 行有 4 个元素
    jaggedArray[2] = new int[3]; // 第 2 行有 3 个元素
    ```
*   **数组与泛型：**
    *   **不能创建泛型类型的数组** (如 `new T[10]`)，因为类型擦除。
    *   可以声明泛型数组引用 (如 `List<String>[] arrayOfLists;`)，但通常不推荐这样做，因为它可能导致类型安全问题和 `ArrayStoreException`。
        ```java
        // List<String>[] stringLists = new List<String>[1]; // 编译错误：generic array creation
        List<?>[] listArray = new List<?>[1]; // 合法，但使用时要小心
        Object[] objectArray = new String[1]; // 协变，但可能导致 ArrayStoreException
        // objectArray[0] = Integer.valueOf(1); // 运行时 ArrayStoreException
        ```
        更安全的做法是使用 `List<List<String>>` 等集合的集合。

#### **5. `java.util.Arrays` 工具类**

`java.util.Arrays` 类提供了大量静态方法，用于方便地操作数组。这些方法经过了优化，通常比手动实现更高效、更可靠。

*   **`sort(array)` / `sort(array, fromIndex, toIndex)`:**
    *   对数组进行排序（升序）。
    *   对于基本类型数组，使用优化的快速排序或归并排序变种 (JDK 7+ 对基本类型使用双轴快速排序，对象数组使用 TimSort - 一种混合稳定排序算法)。
    *   对于对象数组，对象需要实现 `Comparable` 接口，或者提供一个 `Comparator`。
    ```java
    int[] nums = {5, 1, 9, 3, 7};
    Arrays.sort(nums); // nums 变为 {1, 3, 5, 7, 9}

    String[] words = {"banana", "apple", "cherry"};
    Arrays.sort(words); // words 变为 {"apple", "banana", "cherry"}
    Arrays.sort(words, Comparator.reverseOrder()); // words 变为 {"cherry", "banana", "apple"}
    ```

*   **`binarySearch(array, key)` / `binarySearch(array, fromIndex, toIndex, key)`:**
    *   在**已排序**的数组中进行二分查找。如果数组未排序，结果是未定义的。
    *   返回：
        *   如果找到 `key`，返回其索引。
        *   如果未找到 `key`，返回 `(-(insertion point) - 1)`。插入点是指 `key` 如果存在于数组中，应该被插入的位置。这个负值可以用来确定插入位置。
    ```java
    int[] sortedNums = {10, 20, 30, 40, 50};
    int index = Arrays.binarySearch(sortedNums, 30); // index = 2
    int notFoundIndex = Arrays.binarySearch(sortedNums, 35); // notFoundIndex = -4 (-(3)-1)
                                                            // 意味着 35 应该插入到索引 3 的位置
    ```

*   **`equals(array1, array2)`:**
    *   比较两个数组的内容是否相等 (元素逐个比较)。
    *   如果数组元素是对象，则调用对象的 `equals()` 方法。
    *   对于多维数组，它只比较顶层数组的引用，要比较内容需用 `deepEquals()`。
    ```java
    int[] a1 = {1, 2, 3};
    int[] a2 = {1, 2, 3};
    int[] a3 = {1, 3, 2};
    System.out.println(Arrays.equals(a1, a2)); // true
    System.out.println(Arrays.equals(a1, a3)); // false
    ```

*   **`deepEquals(Object[] a1, Object[] a2)`:**
    *   深度比较两个数组的内容是否相等，适用于任意嵌套深度的数组。
    ```java
    String[][] s1 = {{"a", "b"}, {"c"}};
    String[][] s2 = {{"a", "b"}, {"c"}};
    System.out.println(Arrays.deepEquals(s1, s2)); // true
    ```

*   **`fill(array, value)` / `fill(array, fromIndex, toIndex, value)`:**
    *   用指定的值填充数组的所有或部分元素。
    ```java
    int[] arr = new int[5];
    Arrays.fill(arr, 100); // arr 变为 {100, 100, 100, 100, 100}
    ```

*   **`copyOf(original, newLength)`:**
    *   复制指定的数组，截取或用默认值填充以达到指定的长度。返回一个新数组。
    *   **浅拷贝**行为（对于引用类型）。
    ```java
    int[] source = {1, 2, 3, 4, 5};
    int[] dest1 = Arrays.copyOf(source, 3);    // dest1 = {1, 2, 3}
    int[] dest2 = Arrays.copyOf(source, 7);    // dest2 = {1, 2, 3, 4, 5, 0, 0}
    ```

*   **`copyOfRange(original, from, to)`:**
    *   复制指定数组的指定范围到新数组。`from` 包含，`to` 不包含。
    *   **浅拷贝**行为。
    ```java
    int[] rangeCopy = Arrays.copyOfRange(source, 1, 4); // rangeCopy = {2, 3, 4} (元素索引 1, 2, 3)
    ```

*   **`toString(array)`:**
    *   返回数组内容的字符串表示形式，比数组对象本身的 `toString()` 方法友好得多。
    *   例如：`[1, 2, 3]`。
    *   对于多维数组，只处理顶层，要深度打印用 `deepToString()`。
    ```java
    System.out.println(Arrays.toString(nums)); // 输出类似 "[1, 3, 5, 7, 9]"
    ```

*   **`deepToString(Object[] a)`:**
    *   返回多维数组内容的字符串表示形式。
    ```java
    String[][] matrixStr = {{"x", "y"}, {"z"}};
    System.out.println(Arrays.deepToString(matrixStr)); // 输出类似 "[[x, y], [z]]"
    ```

*   **`asList(T... a)`:**
    *   返回一个由指定数组支持的**固定大小**的 `List`。
    *   **重要：** 这个返回的 `List` **不是** `java.util.ArrayList`，而是 `java.util.Arrays` 类中的一个私有静态内部类 `ArrayList`。
    *   **特性：**
        *   对返回的 `List` 的修改会反映到原始数组中，反之亦然。
        *   **不支持 `add()` 和 `remove()` 操作** (会抛 `UnsupportedOperationException`)，因为底层数组大小是固定的。可以 `set()` 元素。
        *   如果原始数组是基本类型数组 (如 `int[]`)，直接调用 `Arrays.asList(intArray)` 会遇到问题，它会把整个 `int[]` 数组当作 `List` 的**一个元素**。需要使用包装类型数组或 Stream API 转换。
    ```java
    String[] fruits = {"apple", "banana", "cherry"};
    List<String> fruitList = Arrays.asList(fruits);
    System.out.println(fruitList); // [apple, banana, cherry]

    fruitList.set(0, "apricot");
    System.out.println(Arrays.toString(fruits)); // [apricot, banana, cherry]

    // fruitList.add("date"); // 会抛 UnsupportedOperationException

    int[] primitiveInts = {1, 2, 3};
    // List<int[]> listContainingOneArray = Arrays.asList(primitiveInts); // List 的元素是 int[]
    // System.out.println(listContainingOneArray.size()); // 1
    // System.out.println(listContainingOneArray.get(0)[0]); // 1

    // 正确转换基本类型数组为 List<Integer>:
    List<Integer> intWrapperList = Arrays.stream(primitiveInts).boxed().collect(Collectors.toList());
    // 或者手动创建
    // List<Integer> intWrapperListManual = new ArrayList<>();
    // for (int i : primitiveInts) { intWrapperListManual.add(i); }
    ```

*   **`stream(array)` / `stream(array, startInclusive, endExclusive)` (Java 8+):**
    *   返回一个由数组元素组成的顺序流 (`Stream`, `IntStream`, `LongStream`, `DoubleStream`)。
    *   这是进行函数式编程操作数组的入口。
    ```java
    int[] scoresArray = {10, 20, 30, 40, 50};
    int sum = Arrays.stream(scoresArray).sum();
    double average = Arrays.stream(scoresArray).average().orElse(0.0);
    List<Integer> evenScores = Arrays.stream(scoresArray)
                                     .filter(s -> s % 20 == 0)
                                     .boxed() // IntStream -> Stream<Integer>
                                     .collect(Collectors.toList());
    ```

*   **`setAll(array, generator)` / `parallelSetAll(array, generator)` (Java 8+):**
    *   使用一个生成器函数 (接受索引作为参数) 来计算数组的每个元素并赋值。`parallelSetAll` 会并行执行。
    ```java
    double[] values = new double[5];
    Arrays.setAll(values, i -> i * 1.5); // values = {0.0, 1.5, 3.0, 4.5, 6.0}

    long[] factorials = new long[10];
    Arrays.parallelSetAll(factorials, i -> {
        if (i == 0) return 1;
        long fact = 1;
        for (int j = 1; j <= i; j++) fact *= j;
        return fact;
    });
    ```

*   **`parallelSort(array)` (Java 8+):**
    *   并行排序数组，对于大型数组可能比串行 `sort` 更快。内部使用 fork/join 框架。

*   **`mismatch(array1, array2)` (Java 9+):**
    *   查找并返回两个数组之间第一个不匹配元素的索引。如果两个数组内容相同直到其中一个结束，则返回较短数组的长度。如果完全相同且长度一样，返回 -1。
    ```java
    int[] arrA = {1, 2, 3, 4};
    int[] arrB = {1, 2, 5, 4};
    int[] arrC = {1, 2, 3};
    int mismatchIndexAB = Arrays.mismatch(arrA, arrB); // 2 (3 vs 5)
    int mismatchIndexAC = Arrays.mismatch(arrA, arrC); // 3 (arrC 结束了)
    int mismatchIndexAA = Arrays.mismatch(arrA, arrA); // -1 (完全匹配)
    ```

**总结与重要性：**

*   数组是 Java 中最基本的数据结构，提供固定大小、同类型元素的存储。
*   核心优势是 O(1) 的随机访问速度。
*   主要劣势是固定大小，插入和删除操作效率低 (O(n))。
*   理解基本类型数组和引用类型数组在内存中的区别，以及 `clone()` 的浅拷贝行为。
*   `java.util.Arrays` 工具类提供了大量实用方法，务必熟练掌握，它们能极大提高开发效率和代码质量。
*   `Arrays.asList()` 返回的特殊 `List` 需要特别注意其固定大小和与原数组联动的特性。
*   Java 8+ 的 Stream API 和并行操作为数组处理带来了更现代和高效的方式。

---

这些知识（泛型、`equals` / `hashCode`、排序接口、数组）是你深入学习 Java 集合框架 (JCF) 的坚实地基。