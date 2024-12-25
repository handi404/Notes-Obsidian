### **Kotlin 协程基础：`launch`, `async`, 和 `await`**

---

Kotlin 协程是一种轻量级的线程，它让异步编程变得简单直观。通过协程，程序可以以同步的方式书写异步代码，从而提高代码的可读性，同时避免回调地狱。

在这一节中，我们将详细讲解 Kotlin 协程的基础，尤其是如何使用 `launch`、`async` 和 `await` 来管理协程的执行。

---

## **1. 什么是协程？**

协程是一种可挂起的函数，它是轻量级的并发工具。协程由 **Kotlin 协程库** 提供，运行在 **协程上下文** 和 **协程作用域** 中。它的特点是：

1. **轻量级**：相比线程，协程消耗的资源更少，可以同时运行数千个协程。
2. **挂起与恢复**：协程可以挂起当前任务并将资源释放，稍后在需要时恢复执行。
3. **结构化并发**：协程更容易管理父子协程的生命周期。

---

## **2. 使用协程的前置条件**

在使用协程之前，需要在项目中引入 Kotlin 的协程依赖：

```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.x.x")  // 核心库
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.x.x")  // Android 环境（可选）
```

---

## **3. 启动协程：`launch` 和 `async`**

### **3.1 `launch` 启动协程**

- `launch` 是最常见的协程构建器，启动一个协程并立即返回。
- 它没有返回值，适合用于执行不需要结果的任务。

#### 示例：使用 `launch` 启动协程

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(1000L)
        println("Hello from Coroutine!")
    }
    println("Hello from Main!")
}
// 输出：
// Hello from Main!
// （延迟1秒）
// Hello from Coroutine!
```

#### 关键点：

1. **`launch` 是异步的**：它不会阻塞主线程，而是在后台运行协程。
2. **`delay` 挂起函数**：延迟指定时间，但不会阻塞线程。

---

### **3.2 `async` 启动协程**

- `async` 也会启动一个新的协程，但它会返回一个 **`Deferred`** 对象，用于表示协程的结果。
- `async` 适合用于需要返回值的任务。

#### 示例：使用 `async` 启动协程

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferred = async {
        delay(1000L)
        "Result from Coroutine"
    }
    println("Waiting for result...")
    println("Result: ${deferred.await()}")
}
// 输出：
// Waiting for result...
// （延迟1秒）
// Result: Result from Coroutine
```

#### 关键点：

1. **`Deferred` 对象**：类似于 `Future`，表示协程的延迟结果。
2. **`await`**：挂起当前协程，直到 `Deferred` 结果可用。

---

## **4. `launch` 和 `async` 的区别**

|特性|`launch`|`async`|
|---|---|---|
|返回值|无返回值 (`Job`)|返回 `Deferred`（带结果）|
|适用场景|不需要结果的任务|需要返回值的任务|
|调用方式|直接调用即可|通常需要用 `await` 获取结果|

#### 示例：同时使用 `launch` 和 `async`

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        println("Launch: Start")
        delay(500L)
        println("Launch: End")
    }

    val result = async {
        println("Async: Start")
        delay(1000L)
        "Async Result"
    }

    println("Waiting for async result...")
    println("Result: ${result.await()}")
}
// 输出：
// Launch: Start
// Async: Start
// Launch: End
// Waiting for async result...
// Result: Async Result
```

---

## **5. 协程的挂起函数**

### **5.1 什么是挂起函数？**

挂起函数是使用 **`suspend`** 关键字定义的函数，可以在协程中调用，并可以被挂起和恢复。

#### 示例：自定义挂起函数

```kotlin
suspend fun mySuspendFunction() {
    delay(1000L)
    println("Hello from suspend function!")
}

fun main() = runBlocking {
    launch {
        mySuspendFunction()
    }
}
```

---

### **5.2 挂起函数与线程的区别**

- **挂起函数**：不会阻塞线程，只是暂停协程的执行，释放线程资源。
- **阻塞函数**：直接阻塞线程，直到任务完成。

---

## **6. 结构化并发**

Kotlin 协程提供了结构化并发来管理协程的生命周期，确保协程启动后会有明确的父作用域，并且所有子协程都会在父协程结束前完成。

#### 示例：结构化并发的作用

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(1000L)
        println("Task from launch")
    }

    coroutineScope { // 创建一个协程作用域
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutineScope")
    }

    println("Coroutine scope is over")
}
// 输出：
// Task from coroutineScope
// Task from nested launch
// Task from launch
// Coroutine scope is over
```

- **`coroutineScope`**：在作用域内启动的所有子协程都执行完后才会结束。
- **`runBlocking`**：阻塞当前线程，通常只用于测试或入口函数。

---

## **7. 使用示例：并发计算**

以下示例展示如何通过 `async` 并发执行多个任务，并获取它们的结果。

#### 示例：计算两个任务的结果

```kotlin
import kotlinx.coroutines.*

suspend fun calculateTask1(): Int {
    delay(1000L)
    return 10
}

suspend fun calculateTask2(): Int {
    delay(2000L)
    return 20
}

fun main() = runBlocking {
    val result1 = async { calculateTask1() }
    val result2 = async { calculateTask2() }

    println("Waiting for results...")
    println("Total: ${result1.await() + result2.await()}")
}
// 输出：
// Waiting for results...
// （总共延迟 2 秒，因为两个任务是并发的）
// Total: 30
```

---

## **8. 常见问题与注意事项**

1. **协程上下文与调度器**：
    
    - 默认情况下，协程运行在调用者线程上，可以通过 `Dispatchers` 改变协程的执行线程（例如 `IO`、`Main` 或 `Default`）。
2. **异常处理**：
    
    - 协程中的异常会传播到父作用域中，建议使用 `try-catch` 或 `supervisorScope` 来捕获异常。
3. **使用 `launch` 和 `async` 的选择**：
    
    - 如果你只需要执行任务而不需要返回值，用 `launch`。
    - 如果需要返回值，用 `async`。

---

## **9. 练习题**

1. 使用 `launch` 启动两个协程，分别延迟 1 秒和 2 秒后打印消息。
2. 创建一个挂起函数 `fetchData`，模拟网络请求，返回一个字符串，并在 `main` 函数中调用它。
3. 使用 `async` 并发执行三个任务，并返回它们结果的总和。
4. 结合 `launch` 和 `coroutineScope`，实现一个父协程和子协程的结构化并发模型。
5. 使用 `try-catch` 捕获协程中的异常。

完成练习后，可以随时与我讨论答案或问题！ 😊