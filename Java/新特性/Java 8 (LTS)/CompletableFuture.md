`CompletableFuture` 是 Java 8 并发库的皇冠明珠，是现代 Java 异步编程的基石。掌握它，是区分普通 Java 程序员和资深工程师的一个重要标志。

---

### CompletableFuture

#### 1. 核心概念 (Core Concept)

在 Java 5 中，我们有了 `Future`，它像一张“未来的凭证”。你可以把一个耗时的任务交给一个线程池去执行，然后立刻拿到一个 `Future` 对象。但这个凭证很“笨”，你只能做两件事：
1.  不断问：“任务完成了吗？” (`isDone()`)
2.  坐着干等，直到任务完成，然后拿结果 (`get()`)。这个等待过程是**阻塞**的。

**`CompletableFuture` 是一个“聪明的凭证”**。它不仅代表一个未来的结果，更重要的是，它提供了一套强大的机制，让你能像搭积木一样，**编排**任务的执行流程，而无需阻塞等待。

**通俗比喻：**
*   **`Future` 就像在餐厅点了一份牛排。** 你拿到一个订单号，然后只能在座位上干等，直到服务员把牛排端上来。
*   **`CompletableFuture` 就像你在一家高级餐厅点餐。** 你告诉服务员：“牛排做好后 (`thenApply`)，撒上黑胡椒；同时 (`thenCombine`)，去酒窖取一瓶红酒；两样都准备好后，一起送到我的餐桌上 (`whenComplete`)；如果牛排卖完了 (`exceptionally`)，就换成烤鸡。”

整个过程，你不需要亲自去厨房盯着，你可以继续做自己的事。这就是**非阻塞、回调式**的异步编程。

**核心能力：**
1.  **异步执行任务：** `supplyAsync()` (有返回值), `runAsync()` (无返回值)。
2.  **结果转换与消费：** `thenApply()`, `thenAccept()`, `thenRun()`。
3.  **任务编排 (串行、并行、聚合)：**
    *   **串行：** `thenCompose()` (任务 B 依赖任务 A 的结果)。
    *   **AND 聚合：** `thenCombine()` (A 和 B 都完成后，执行 C), `allOf()` (等待所有任务完成)。
    *   **OR 聚合：** `acceptEither()` (A 或 B 任意一个完成后，执行 C), `anyOf()` (等待任意一个任务完成)。
4.  **强大的异常处理：** `exceptionally()`, `handle()`, `whenComplete()`。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 8 之前 (The Pain Point):**
    *   **回调地狱 (Callback Hell):** 通过匿内部类实现回调，代码层层嵌套，难以阅读和维护。
    *   **`Future.get()` 阻塞：** 异步的优势被阻塞调用所抵消，本质上还是同步等待。
    *   **缺乏组合能力：** 无法轻松地将两个 `Future` 的结果合并，或者在一个 `Future` 完成后启动另一个。

*   **Java 8 (The Solution):**
    `CompletableFuture` 结合了 `Future` 的优点和函数式编程（Lambda 表达式），提供了一套**流式 (Fluent) API** 来构建异步计算管道。它彻底改变了 Java 的并发编程范式，使其向 Node.js、C# 等语言的现代异步模型看齐。

*   **现代化 (Java 9+):**
    *   Java 9 为 `CompletableFuture` 增加了超时控制 (`orTimeout()`) 和延迟执行等辅助功能，使其更加完善。
    *   在现代微服务架构中，`CompletableFuture` 是执行**并行、非阻塞 I/O 调用**（如调用多个下游服务的 REST API）的首选工具。
    *   它是响应式编程框架（如 Spring WebFlux/Project Reactor）的底层思想基础之一。`Mono` 和 `Flux` 可以看作是功能更强大的 `CompletableFuture`。

#### 3. 代码示例 (Code Example)

假设我们需要完成一个任务：1. 查询商品基本信息；2. 并行查询商品价格和库存；3. 组合所有信息并展示。

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {

    // 建议使用自定义线程池，避免耗尽公共的 ForkJoinPool
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        // 1. 开始一个异步任务链：查询商品信息
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> getProductInfo(123), executor)
            // 2. 当上一步完成后，并行执行两个新任务
            .thenCompose(productInfo -> {
                CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync(() -> getPrice(productInfo), executor);
                CompletableFuture<Integer> stockFuture = CompletableFuture.supplyAsync(() -> getStock(productInfo), executor);

                // 3. AND 组合：当价格和库存都返回时，进行合并
                return priceFuture.thenCombine(stockFuture, (price, stock) ->
                    String.format("商品详情: %s, 价格: %.2f, 库存: %d", productInfo, price, stock)
                );
            })
            // 4. 异常处理：如果链条中任何地方发生异常
            .exceptionally(ex -> "查询失败: " + ex.getMessage())
            // 5. 最终处理：无论成功或失败都会执行（类似 finally）
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("最终结果: " + result);
                }
            });

        // Main 线程可以继续做其他事...

        // 阻塞等待最终结果，仅用于演示。在真实应用中（如Web服务器），整个链条都是非阻塞的。
        future.join();

        long endTime = System.currentTimeMillis();
        System.out.println("总耗时: " + (endTime - startTime) + " ms");

        executor.shutdown();
    }

    // --- 模拟的耗时API调用 ---
    private static String getProductInfo(int productId) {
        sleep(1);
        System.out.println("查询商品信息...");
        return "商品-" + productId;
    }

    private static double getPrice(String productInfo) {
        sleep(2);
        // if (true) throw new RuntimeException("价格服务超时"); // <- 解除注释测试异常
        System.out.println("查询价格...");
        return 99.99;
    }

    private static int getStock(String productInfo) {
        sleep(1);
        System.out.println("查询库存...");
        return 500;
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**代码解读：**
*   `supplyAsync`: 启动一个有返回值的异步任务。
*   `thenCompose`: 用于串联两个有依赖关系的异步任务。它接收上一步的结果，并返回一个新的 `CompletableFuture`。
*   `thenCombine`: 用于并行组合两个独立的异步任务。当两个都完成后，将它们的结果合并。
*   `exceptionally`: 异步世界的 `try-catch`，提供一个兜底方案。
*   `join()`: 阻塞等待结果，与 `get()` 类似但抛出非受检异常，在 `main` 方法演示时更简洁。

#### 4. 扩展与应用 (Extension & Application)

*   **微服务 API 网关：** 网关需要调用多个后端的微服务来聚合数据，使用 `CompletableFuture.allOf()` 或 `thenCombine` 可以并行发起所有请求，显著降低响应延迟。
*   **高并发 I/O 操作：** 同时向数据库写入多条数据、并行读取多个文件、请求第三方 API 等。
*   **提升 GUI 应用响应速度：** 将耗时操作（如网络请求、文件处理）放入 `CompletableFuture`，避免 UI 线程冻结。
*   **与 Spring 框架集成：** Spring 的 `@Async` 注解可以返回 `CompletableFuture`，与 Spring 的异步 MVC（`DeferredResult`）无缝集成。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **务必使用自定义线程池：** `CompletableFuture` 默认使用 `ForkJoinPool.commonPool()`。这是一个 JVM 级别的共享池，如果你的任务是 I/O 密集型或可能会阻塞（如访问数据库、调用外部API），会耗尽公共池的线程，导致整个应用性能下降甚至死锁。**最佳实践是为不同类型的异步任务创建专用的 `ExecutorService`。**

2.  **理解 `...` 和 `...Async` 方法的区别：**
    *   `thenApply()`: 可能会在**上一步任务的线程**或**提交任务的线程**中执行。
    *   `thenApplyAsync()`: 会将任务**重新提交到线程池**中执行，保证了执行的异步性。
    *   **选择：** 如果后续操作非常轻量级且快速，使用非 `Async` 版本可以减少线程切换开销；否则，特别是涉及 I/O 或耗时计算时，应使用 `Async` 版本。

3.  **异常会沿着链传播：** 如果一个 `CompletableFuture` 异常完成，所有后续依赖它的 `thenApply/thenAccept` 等阶段都会被跳过，直到遇到第一个 `exceptionally` 或 `handle`。

4.  **避免在 `CompletableFuture` 链中调用阻塞代码：** `CompletableFuture` 的设计初衷就是为了避免阻塞。在 `thenApply` 等的回调函数中调用 `get()` 或其他阻塞方法，会违背其设计理念，让异步编程退化为同步。

5.  **`allOf` vs. `thenCombine`:**
    *   `thenCombine`: 组合两个 Future。
    *   `allOf`: 组合任意多个 Future，但它的返回类型是 `CompletableFuture<Void>`，你需要在其 `then...` 阶段手动从原始 Future 列表中获取结果。

`CompletableFuture` 是一个功能极其丰富的工具，初学时可能会觉得复杂。但只要理解了其**非阻塞、事件驱动、可编排**的核心思想，并从最常用的方法（`supplyAsync`, `thenApply`, `thenCombine`, `exceptionally`）入手，就能逐步掌握它，并写出优雅、高效的并发代码。