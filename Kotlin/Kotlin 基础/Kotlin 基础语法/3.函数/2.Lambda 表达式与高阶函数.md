### **Kotlin 第7天：Lambda 表达式与高阶函数详解**

---

Kotlin 支持强大的函数式编程风格，其核心概念包括 **Lambda 表达式** 和 **高阶函数**。它们可以简化代码、提升可读性，并使程序更灵活。

---

## **1. 什么是 Lambda 表达式**

Lambda 表达式是一种匿名函数，常用于作为参数传递给高阶函数。Lambda 表达式的语法简洁且灵活。

### **1.1 Lambda 表达式的基本语法**

```kotlin
val lambdaName: (参数类型) -> 返回类型 = { 参数名: 参数类型 -> 函数体 }
```

#### 示例：

```kotlin
val square: (Int) -> Int = { number: Int -> number * number }

fun main() {
    println(square(5))  // 输出: 25
}
```

- **`(Int) -> Int`**：表示 Lambda 接收一个 `Int` 类型参数，并返回一个 `Int`。
- **`{ number: Int -> number * number }`**：Lambda 的实现部分。

---

### **1.2 简化写法**

1. 如果 Kotlin 能推断参数类型，可以省略参数类型。
2. 如果 Lambda 中只有一个参数，可以用默认的 `it` 表示。

#### 示例：

```kotlin
val square = { number: Int -> number * number }  // 自动推断类型
val double = { it * 2 }  // 使用默认参数 it

fun main() {
    println(square(5))  // 输出: 25
    println(double(6))  // 输出: 12
}
```

---

## **2. 高阶函数**

### **2.1 什么是高阶函数**

高阶函数是指以函数为参数或返回值的函数。

#### 示例：以函数为参数

```kotlin
fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

fun main() {
    val sum = calculate(3, 5) { a, b -> a + b }
    println(sum)  // 输出: 8
}
```

- **`operation: (Int, Int) -> Int`**：表示一个接受两个 `Int` 参数并返回 `Int` 的函数类型。
- **`{ a, b -> a + b }`**：以 Lambda 表达式作为参数传递。

---

### **2.2 示例：高阶函数返回函数**

```kotlin
fun createMultiplier(factor: Int): (Int) -> Int {
    return { number -> number * factor }
}

fun main() {
    val multiplier = createMultiplier(3)  // 创建一个乘以3的函数
    println(multiplier(4))  // 输出: 12
}
```

---

## **3. Kotlin 标准库中的高阶函数**

Kotlin 提供了大量内置高阶函数，例如 `filter`、`map`、`reduce` 等，这些函数常用于集合操作。

---

### **3.1 `filter` 函数**

用于过滤集合中满足条件的元素。

#### 示例：

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

fun main() {
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println(evenNumbers)  // 输出: [2, 4]
}
```

---

### **3.2 `map` 函数**

用于将集合中的每个元素映射为另一个值。

#### 示例：

```kotlin
val numbers = listOf(1, 2, 3, 4)

fun main() {
    val doubledNumbers = numbers.map { it * 2 }
    println(doubledNumbers)  // 输出: [2, 4, 6, 8]
}
```

---

### **3.3 `reduce` 函数**

将集合中的元素进行累积操作。

#### 示例：

```kotlin
val numbers = listOf(1, 2, 3, 4)

fun main() {
    val sum = numbers.reduce { acc, number -> acc + number }
    println(sum)  // 输出: 10
}
```

- **`acc`** 是累积结果。
- **`number`** 是当前元素。

---

### **3.4 `forEach` 函数**

遍历集合的每个元素。

#### 示例：

```kotlin
val numbers = listOf(1, 2, 3, 4)

fun main() {
    numbers.forEach { println(it) }
    // 输出:
    // 1
    // 2
    // 3
    // 4
}
```

---

### **3.5 组合操作**

高阶函数可以组合使用，例如 `filter` 和 `map`。

#### 示例：

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

fun main() {
    val result = numbers.filter { it % 2 != 0 }.map { it * 2 }
    println(result)  // 输出: [2, 6, 10]
}
```

---

## **4. 带接收者的 Lambda**

Kotlin 支持 **带接收者的 Lambda**，类似于扩展函数的语法。

### **4.1 语法**

```kotlin
val lambda: String.() -> Int = { this.length }
```

- **`String.() -> Int`**：Lambda 的接收者是 `String` 类型。
- **`this.length`**：表示当前接收者对象的属性或方法。

#### 示例：

```kotlin
val getLength: String.() -> Int = { this.length }

fun main() {
    val length = "Hello".getLength()
    println(length)  // 输出: 5
}
```

---

## **5. 内联函数（`inline`）**

在高阶函数中，如果函数作为参数频繁调用，可能会引入性能开销。为了解决这一问题，Kotlin 提供了 `inline` 关键字，将高阶函数展开为内联代码。

#### 示例：

```kotlin
inline fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

fun main() {
    val result = calculate(4, 5) { a, b -> a * b }
    println(result)  // 输出: 20
}
```

---

## **6. Lambda 表达式中的返回（`return`）**

在 Lambda 表达式中，可以使用 `return` 返回值，但需要注意上下文。

### **6.1 从 Lambda 返回**

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5)

    numbers.forEach {
        if (it == 3) return  // 退出整个函数
        println(it)
    }
    // 输出:
    // 1
    // 2
}
```

### **6.2 使用 `label` 返回**

为避免返回整个函数，可以使用 `label` 限制返回范围。

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5)

    numbers.forEach label@{
        if (it == 3) return@label  // 仅退出当前 Lambda
        println(it)
    }
    // 输出:
    // 1
    // 2
    // 4
    // 5
}
```

---

## **7. 练习题**

1. 定义一个高阶函数，接收一个整型列表和一个 Lambda，返回符合 Lambda 条件的元素。
2. 使用 `filter` 和 `map`，将一个整数列表中所有偶数的平方存储到一个新列表中。
3. 编写一个高阶函数，该函数返回另一个函数，用于计算两个数的和。
4. 使用 `reduce` 函数计算一个列表的乘积。
5. 定义一个带接收者的 Lambda，接收一个字符串，返回反转后的字符串。

完成后，可以随时和我讨论答案或遇到的疑问！ 😊