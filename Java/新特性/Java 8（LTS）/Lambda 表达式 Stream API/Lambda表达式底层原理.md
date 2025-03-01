
__ _ _ _ _

Lambda 表达式是 Java 8 引入的一项重要特性，它允许开发者以更简洁的方式编写代码，特别是在处理函数式接口时。

### 1\. 匿名内部类的使用

在 Java 8 之前，实现类似功能通常需要使用匿名内部类。举个栗子，创建一个线程时，可以这样写：

```java
Thread t1 = new Thread(new Runnable() {  
    @Override  
    public void run() {  
        System.out.println("t1");  
    }  
});  
t1.start();
```

这里， ` Runnable ` 是一个接口，通过匿名内部类实现了 ` run ` 方法。

### 2\. 函数式接口和 Lambda 表达式

Java 8 引入了 ` @FunctionalInterface ` 注解，用于标记函数式接口，即只包含一个抽象方法的接口。Lambda表达式允许直接实现这些接口的抽象方法，而无需显式地创建类和方法。

使用 Lambda 表达式，上述代码可以简化为：

```java
Thread t1 = new Thread(() -> System.out.println("t1"));  
t1.start();
```

这种写法更加简洁，减少了代码的冗余。

### 3\. Lambda 表达式的语法

Lambda 表达式的基本语法是：

* • ` (parameters) -> expression `

* • ` (parameters) -> { statements; } `

其中，参数列表可以为空，Lambda 体可以是一个表达式或一个代码块。

Lambda 表达式主要用于**实现函数式接口**。例如，使用 ` filter ` 方法过滤列表时：

  ```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);  
List<Integer> evenNumbers = numbers.stream()  
                                     .filter(n -> n % 2 == 0)  
                                     .collect(Collectors.toList());  
System.out.println(evenNumbers); // 输出 [2, 4, 6, 8]
  ```

这里， ` n -> n % 2 == 0 ` 是一个 Lambda 表达式，用于判断数字是否为偶数。

### 4\. Lambda 表达式的底层原理

Lambda 表达式在底层是通过匿名内部类实现的。当使用 Lambda 表达式时，Java 编译器会：

**1\. 自动生成一个匿名内部类，实现相应的函数式接口。**

**2\. 将 Lambda 表达式的代码转换为这个匿名内部类中的一个方法。**

Lambda
表达式其实是匿名内部类的语法糖，这个语法糖在程序执行时会进行兑现，也就是生成匿名内部类并进行任务执行。这个过程对开发者是透明的，使得代码更加简洁和易于理解。

* * *

这个代码示例展示了如何使用 Java Stream API 来对一个列表进行筛选，过滤出偶数并将结果收集到一个新的列表中。下面我将逐行详细解释代码的含义：

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
```
- **`Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)`**：创建了一个包含整数 1 到 9 的不可变的 `List`。
- **`List<Integer> numbers`**：定义了一个名为 `numbers` 的 `List`，其元素类型为 `Integer`。
- 这行代码的作用是初始化一个整数列表，`numbers` 存储了 `[1, 2, 3, 4, 5, 6, 7, 8, 9]`。

```java
List<Integer> evenNumbers = numbers.stream()
                                    .filter(n -> n % 2 == 0)
                                    .collect(Collectors.toList());
```
这是核心部分，它使用了 Java 8 的 Stream API 来处理列表中的元素。

- **`numbers.stream()`**：将 `numbers` 列表转换为一个 Stream。Stream 是一个可以进行各种操作的数据流，它允许对数据进行过滤、映射、排序、收集等操作。转换为 Stream 后，列表中的每个元素都会被处理。
  
- **`.filter(n -> n % 2 == 0)`**：`filter` 是一个中间操作，用来对 Stream 中的元素进行过滤。它接受一个**谓词**（返回 `boolean` 的函数），将不满足条件的元素从 Stream 中移除。
  - 在这个例子中，`n -> n % 2 == 0` 是一个 **lambda 表达式**，其中 `n` 是列表中的每个整数元素，`n % 2 == 0` 的意思是判断 `n` 是否是偶数。如果 `n` 是偶数，那么 `filter` 会保留该元素，否则会将其过滤掉。
  
- **`.collect(Collectors.toList())`**：`collect` 是一个终端操作，它将 Stream 中的元素收集成一个 `List`。`Collectors.toList()` 是一个收集器，它告诉 Stream 将处理过的结果转换成一个新的 `List`。

最终，`evenNumbers` 列表中将包含过滤后的偶数 `[2, 4, 6, 8]`。

```java
System.out.println(evenNumbers); // 输出 [2, 4, 6, 8]
```
- 这一行代码使用 `System.out.println` 打印 `evenNumbers` 列表。因为之前的过滤操作将列表中的偶数筛选了出来，所以输出的是 `[2, 4, 6, 8]`。

### 完整流程概述：
1. **转换为 Stream**：`numbers` 列表被转换成一个 Stream。
2. **过滤偶数**：`filter` 方法对 Stream 中的每个元素进行检查，只保留偶数元素。
3. **收集结果**：`collect` 方法将过滤后的 Stream 转换回一个新的 `List`。
4. **输出结果**：通过 `System.out.println` 输出偶数列表 `[2, 4, 6, 8]`。

### Stream API 的优点：
- **简洁明了**：Stream API 使得代码更加简洁。传统上需要使用循环和条件语句来处理的逻辑，在 Stream API 中可以通过链式调用简洁地表达。
- **可读性高**：`filter` 和 `collect` 等方法的命名和功能直观明了，代码更容易理解。
- **支持并行处理**：Stream API 允许通过 `parallelStream()` 轻松实现并行处理以提高性能（对于大型数据集非常有用）。

### 总结
这段代码展示了如何通过 Java 8 的 Stream API 以一种函数式编程风格来处理列表中的数据。通过 `filter` 对列表进行筛选，最终将偶数收集到一个新的列表中并打印。