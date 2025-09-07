Java 21 中最具革命性的特性，也是 Java 并发编程未来的基石——**虚拟线程 (Virtual Threads)**。

---

### 虚拟线程 (Virtual Threads)

#### 1. 核心概念 (Core Concept)

**虚拟线程是由 Java 虚拟机 (JVM) 而不是操作系统 (OS) 管理的、极其轻量级的线程。** 它的目标是让你能够用传统的、简单明了的**同步阻塞式代码风格（Thread-per-Request）**，编写出拥有**异步非阻塞式编程**极高性能和吞吐量的应用程序。

为了理解虚拟线程，我们必须先理解传统的**平台线程 (Platform Threads)**：
*   **平台线程：** 就是我们一直以来使用的 `java.lang.Thread`。它与操作系统内核线程是 **1:1** 的映射关系。创建一个平台线程，操作系统就要为其分配内核资源。因此，平台线程是**昂贵的、数量有限的**（通常几千个就是上限）。当平台线程执行 I/O 操作（如读数据库、调 API）而被阻塞时，它会一直霸占着宝贵的操作系统线程，导致系统无法处理更多请求。

**虚拟线程的革命性之处：**
*   **M:N 映射：** 虚拟线程与平台线程是 **M:N** 的映射关系。你可以创建**数百万个**虚拟线程，它们都运行在少数几个平台线程（称为**载体线程, Carrier Thread**）之上。
*   **不阻塞载体线程：** 当一个虚拟线程执行阻塞 I/O 操作时，它会**自动“卸下 (unmount)”**，让出底层的平台线程（载体线程），JVM 会把这个平台线程交给其他可运行的虚拟线程去执行。当 I/O 操作完成后，JVM 会再找一个可用的平台线程，让原来的虚拟线程**继续“挂上 (mount)”** 执行。

**通俗比喻：**
*   **平台线程：** 就像**一条泳道对应一个游泳者**。即使某个游泳者在岸边休息（阻塞 I/O），他也占着那条泳道，别人没法用。泳池里的泳道数量有限，能同时游泳的人数也就有限。
*   **虚拟线程：** 就像**少数几条“共享泳道”和一大群“高效的游泳者”**。一个游泳者游到头需要休息时（阻塞 I/O），他会**立刻出水**，把泳道让给后面排队的人。等他休息好了，再找一条空闲的泳道继续游。这样，泳道的使用率极高，整个泳池能容纳的游泳者数量（吞吐量）大大增加。

**核心项目：** 虚拟线程是 **Project Loom** 的核心成果。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 21 之前 (The Pain Point - “并发的窘境”):**
    长期以来，Java 开发者面临一个艰难的选择：
    1.  **Thread-per-Request 模型 (同步阻塞):** 代码简单直观，易于编写、调试和维护（每个请求一个线程）。但由于平台线程的限制，可伸缩性极差，无法应对高并发。
    2.  **异步非阻塞模型 (Reactive Programming):** 使用 `CompletableFuture`、回调或响应式框架（如 Netty, Vert.x, Spring WebFlux）。性能和吞吐量极高，但代码逻辑被分解成一系列回调函数，形成了所谓的“回调地狱”，代码难以理解、调试和追踪（异常堆栈变得毫无意义）。

*   **预览阶段 (Java 19 & 20):**
    虚拟线程经过了两个版本的预览，在社区中引起了巨大的反响和广泛的测试，证明了其模型的稳定性和性能优势。

*   **Java 21 (The Solution - 两全其美):**
    虚拟线程正式发布，它承诺**鱼与熊掌可以兼得**。开发者现在可以：
    *   **像写同步代码一样简单：**
        ```java
        // 传统的阻塞式代码
        String result1 = callServiceA();
        String result2 = callServiceB(result1);
        process(result2);
        ```
    *   **获得异步编程的性能：**
        将这段代码运行在虚拟线程上，当 `callServiceA()` 和 `callServiceB()` 阻塞时，底层的平台线程会被释放去处理其他请求。

#### 3. 代码示例 (How to Use)

使用虚拟线程非常简单，JDK 提供了多种创建方式。

```java
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class VirtualThreadsExample {

    public static void main(String[] args) throws InterruptedException {
        
        // --- 方式 1: Thread.startVirtualThread() ---
        // 最简单的方式，适合快速启动一次性任务
        Thread.startVirtualThread(() -> {
            System.out.println("Hello from a virtual thread! Running on: " + Thread.currentThread());
        });

        // --- 方式 2: Thread.ofVirtual().factory() ---
        // 创建虚拟线程的工厂，用于更灵活的创建
        Thread.Builder builder = Thread.ofVirtual().name("my-virtual-thread-", 0);
        Thread t1 = builder.start(() -> System.out.println("Thread factory created: " + Thread.currentThread()));
        t1.join(); // 等待 t1 完成

        // --- 方式 3: Executors.newVirtualThreadPerTaskExecutor() ---
        // **推荐的最佳实践！** 创建一个为每个任务都启动新虚拟线程的 ExecutorService。
        // 它不池化虚拟线程（因为它们很廉价），但会池化底层的平台线程。
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    // 模拟一个耗时 1 秒的 I/O 操作
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i % 1000 == 0) {
                        System.out.println("Task " + i + " completed on: " + Thread.currentThread());
                    }
                });
            });
        } // try-with-resources 会自动关闭 executor
        // 观察输出，你会发现虽然有 10000 个任务在“并行”，但底层的平台线程 (ForkJoinPool-worker-X) 数量很少。
        System.out.println("All tasks submitted.");
    }
}
```

#### 4. 扩展与应用 (Extension & Application)

*   **高并发 Web 服务器：** 这是虚拟线程最典型的应用场景。像 Tomcat, Jetty, Spring Boot 等主流框架都已经或正在集成虚拟线程。Spring Boot 3.2+ 已经可以一键开启虚拟线程支持。
*   **微服务应用：** 微服务之间存在大量的网络 I/O 调用，虚拟线程可以极大地提高服务处理并发请求的能力。
*   **任何 I/O 密集型应用：** 数据库访问、消息队列、文件读写等。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **不要池化虚拟线程 (Don't Pool Virtual Threads):** 虚拟线程被设计为“用完即弃”的。它们创建成本极低，池化它们毫无意义，反而会带来问题。应该使用 `Executors.newVirtualThreadPerTaskExecutor()`。
2.  **`ThreadLocal` 的潜在风险：** `ThreadLocal` 在虚拟线程中仍然可用，但要小心使用。如果你在一个请求中创建了大量虚拟线程，它们可能会共享同一个 `ThreadLocal` 变量（如果它们是由同一个平台线程启动的），或者 `ThreadLocal` 的生命周期会变得不可预测。Java 21 引入了**作用域值 (Scoped Values)** 作为 `ThreadLocal` 在虚拟线程时代的更优替代品（目前为预览特性）。
3.  **小心 `synchronized` 关键字：** 当虚拟线程进入一个 `synchronized` 块或方法时，它会**“钉住 (pin)”** 底层的载体线程。这意味着即使它在 `synchronized` 块内部发生阻塞，也**无法让出**平台线程。这会严重影响性能。
    *   **替代方案：** 使用 `java.util.concurrent.locks.ReentrantLock`。它已经被重构以适应虚拟线程，不会导致“钉住”。
4.  **不适用于 CPU 密集型任务：** 虚拟线程主要优化的是 **I/O 阻塞**，而不是 CPU 计算。对于 CPU 密集型任务，传统的基于平台线程的线程池仍然是最佳选择，因为你需要的线程数应该与 CPU 核心数相当。
5.  **并非银弹：** 虚拟线程不能解决所有并发问题。它解决了“吞吐量”问题，但你仍然需要处理好**数据竞争、死锁**等并发编程的基本问题。

总而言之，虚拟线程是 Java 并发编程范式的一次**根本性转变**。它以前所未有的方式，将**编写并发代码的简单性**与**处理海量并发请求的强大能力**结合在一起，将极大地改变未来 Java 应用的架构方式。