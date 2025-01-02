在 Kotlin 中，协程并 **不是只能调用挂起函数**。协程可以调用普通函数和挂起函数。但挂起函数的作用是利用协程的能力以非阻塞的方式执行耗时任务，而普通函数则没有这样的能力。

---

### **普通函数 vs 挂起函数**

#### 1. **普通函数**

普通函数可以在协程中直接调用，就像在普通代码中一样。

#### 2. **挂起函数**

挂起函数是一种特殊类型的函数，它可以暂停执行，释放线程资源，允许其他协程继续运行，然后在需要时恢复执行。这是挂起函数的主要作用，它是协程实现异步操作的核心。

---

### **协程中调用普通函数**

你可以在协程中调用任何普通函数，普通函数的执行是同步和阻塞的。例如：

```kotlin
fun calculateSum(a: Int, b: Int): Int {
    return a + b
}

fun main() = kotlinx.coroutines.runBlocking {
    println("Starting...")
    val result = calculateSum(10, 20)  // 调用普通函数
    println("Result: $result")
}
```

输出：

```
Starting...
Result: 30
```

> **说明**: `calculateSum` 是普通函数，它会立即计算结果并返回，协程并不会暂停或释放资源。

---

### **协程中调用挂起函数**

挂起函数只能在协程或另一个挂起函数中调用。如果试图在非协程上下文中调用挂起函数，会出现编译错误。

#### 示例：

```kotlin
suspend fun fetchData(): String {
    kotlinx.coroutines.delay(1000)  // 挂起1秒
    return "Data fetched"
}

fun main() = kotlinx.coroutines.runBlocking {
    println("Starting...")
    val result = fetchData()  // 调用挂起函数
    println(result)
}
```

输出：

```
Starting...
Data fetched
```

> **说明**: `fetchData` 是挂起函数，协程会在 `delay` 时释放线程资源，允许其他协程运行。

---

### **普通函数和挂起函数的混合调用**

协程中可以混合调用普通函数和挂起函数。例如：

```kotlin
fun logMessage(message: String) {
    println("Log: $message")  // 普通函数
}

suspend fun fetchData(): String {
    kotlinx.coroutines.delay(1000)  // 模拟耗时操作
    return "Data fetched"
}

fun main() = kotlinx.coroutines.runBlocking {
    logMessage("Fetching data...") // 调用普通函数
    val data = fetchData()         // 调用挂起函数
    logMessage("Received: $data")
}
```

输出：

```
Log: Fetching data...
Log: Received: Data fetched
```

---

### **什么时候需要挂起函数？**

挂起函数通常用于以下场景：

1. **执行耗时操作**：如网络请求、数据库查询、文件读取等。
2. **非阻塞任务**：挂起函数通过 `delay` 或其他方式可以让协程暂停，而不会阻塞线程。
3. **异步编程**：使用挂起函数可以更自然地写出异步代码。

普通函数则适用于不需要暂停或异步执行的任务。

---

### **总结**

- 协程中可以调用普通函数和挂起函数。
- **普通函数**是同步执行的，不会暂停或释放线程资源。
- **挂起函数**是协程中的异步利器，它可以暂停协程并释放资源，等到需要时恢复。
- 挂起函数只能在协程或另一个挂起函数中调用，而普通函数没有这样的限制。

所以，在实际开发中，你可以根据场景需要选择普通函数或挂起函数，两者可以混合使用。