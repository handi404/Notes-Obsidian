Java 11 中正式“转正”的重量级特性——**标准化的 HTTP Client API**。它标志着 Java 在原生网络编程能力上，终于追赶上了现代化的步伐。

---

### 标准化 HTTP Client API (`java.net.http`)

#### 1. 核心概念 (Core Concept)

Java 11 内置了一个全新的、现代化的 HTTP 客户端 API，用于取代老旧、笨重且 API 设计不友好的 `HttpURLConnection`。这个新 API 支持 HTTP/1.1、HTTP/2 以及 WebSocket，并提供了同步和异步两种编程模型。

**它的设计围绕三个核心对象展开：**

1.  **`HttpClient`**: 它是请求的发送器。可以看作是一个配置好（如超时时间、代理、线程池等）的浏览器或客户端实例。它管理着连接和资源，**应该被创建一次并复用**。
2.  **`HttpRequest`**: 代表一个要发送的 HTTP 请求。它是一个**不可变 (Immutable)** 对象，通过 Builder 模式构建，包含了 URI、请求方法 (GET, POST 等)、请求头 (Headers) 和请求体 (Body)。
3.  **`HttpResponse<T>`**: 代表一个接收到的 HTTP 响应。它也是一个**不可变**对象，包含了状态码、响应头和经过处理的响应体（类型由 `T` 决定）。

**通俗比喻：**
*   **`HttpClient`** 就像你的**快递应用**（如顺丰、FedEx 的 App）。你只需要安装和配置一次。
*   **`HttpRequest`** 就像你填写的一个**电子运单**。你写清楚了要寄到哪里 (URI)、用什么方式寄 (GET/POST)、包裹里是什么 (Body)。这个运单一旦生成就不能修改。
*   **发送请求** 就相当于点击 “下单” 按钮。
*   **`HttpResponse`** 就像快递公司返回给你的**签收回执**。上面有签收状态（状态码）、签收时间等信息，以及对方的回信（响应体）。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 11 之前 (The Pain Point):**
    *   **`HttpURLConnection`**: Java 原生的唯一选择。它的 API 是阻塞式的、非常低级、使用繁琐，且难以配置（例如设置超时）。不支持 HTTP/2。
    *   **第三方库的统治**: 正因为 `HttpURLConnection` 的难用，几乎所有生产项目都会引入第三方库，如 **Apache HttpClient**, **OkHttp**, 或 **Retrofit**。这些库虽然功能强大，但增加了项目的依赖和复杂性。

*   **孵化与诞生 (The Solution):**
    这个新的 HTTP Client 并不是凭空出现的。它在 Java 9 和 10 中作为“孵化模块” (`jdk.incubator.http`) 存在，经过了社区的充分测试和反馈。在 Java 11 中，它被正式标准化，移入 `java.net.http` 包，成为 JDK 的一部分。

*   **现代化特性：**
    *   **天生异步 (`sendAsync`)**: API 的设计核心之一就是非阻塞 I/O。它与 `CompletableFuture` 完美集成，让你能以优雅的函数式链式调用来处理异步响应，非常适合高并发场景。
    *   **支持 HTTP/2**: 无需任何额外配置，客户端会**自动协商**使用 HTTP/2（如果服务器支持）。这能带来头部压缩、多路复用等性能提升。
    *   **Fluent API (流式 API)**: 使用 Builder 模式 (`HttpRequest.newBuilder()`)，代码可读性极高，配置请求的过程像是在写文章。
    *   **响应体处理**: 通过 `HttpResponse.BodyHandlers`，可以非常方便地将响应体处理成字符串、字节数组、文件，甚至是响应式流。

#### 3. 代码示例 (Code Example)

```java
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpClientExample {

    // 1. 创建一个可复用的 HttpClient 实例
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
        // --- 2. 同步 (Synchronous) GET 请求 ---
        System.out.println("--- Synchronous GET ---");
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/users/openjdk"))
                .header("Accept", "application/vnd.github.v3+json")
                .GET() // 默认就是 GET，可以省略
                .build();
        
        // 使用 send() 发送同步请求，会阻塞直到响应返回
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body (truncated): " + response.body().substring(0, 100) + "...");

        System.out.println("\n----------------------------\n");

        // --- 3. 异步 (Asynchronous) POST 请求 ---
        System.out.println("--- Asynchronous POST ---");
        String jsonBody = "{\"title\": \"Hello Java 11\", \"body\": \"This is a test.\"}";
        
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // 使用 sendAsync() 发送异步请求，立即返回一个 CompletableFuture
        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        // 对 CompletableFuture 进行链式处理，整个过程非阻塞
        futureResponse
            .thenApply(HttpResponse::body)
            .thenAccept(body -> System.out.println("Async POST Response Body: " + body))
            .exceptionally(ex -> {
                System.err.println("An error occurred: " + ex.getMessage());
                return null;
            });
        
        System.out.println("Async request sent, main thread is not blocked and can do other work.");
        
        // 在 main 方法中，我们需要等待异步任务完成才能退出程序
        futureResponse.join(); 
    }
}
```

#### 4. 扩展与应用 (Extension & Application)

*   **微服务通信：** 在服务间进行 RESTful API 调用，新的 HTTP Client 是一个轻量级、零依赖的绝佳选择。
*   **调用第三方 API：** 无需引入重型框架，即可方便地与外部服务（如天气、支付、社交媒体 API）集成。
*   **Web 爬虫或数据抓取：** 异步 API 非常适合同时发起大量请求，提高抓取效率。
*   **Health Checks & Monitoring:** 实现对其他服务的健康检查。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **复用 `HttpClient` 实例：** 这是**最重要的性能实践**。`HttpClient` 内部管理着连接池和线程池，每次都 `HttpClient.newHttpClient()` 会严重浪费资源。应该将其创建为单例或静态常量。
2.  **不可变性：** `HttpRequest` 和 `HttpResponse` 都是不可变的，这意味着它们是线程安全的，可以放心地在多线程间共享。
3.  **同步 vs. 异步：**
    *   **同步 (`send`)**: 适用于简单的脚本、命令行工具或逻辑上必须等待结果的场景。
    *   **异步 (`sendAsync`)**: 适用于高并发应用，如 Web 服务器、GUI 应用，可以避免线程阻塞，最大化吞吐量。**现代服务端开发应首选异步**。
4.  **`BodyHandlers` 和 `BodyPublishers`：**
    *   `BodyPublishers` 用于**创建请求体**，可以从字符串 (`ofString`)、文件 (`ofFile`)、字节数组 (`ofByteArray`) 等创建。
    *   `BodyHandlers` 用于**处理响应体**，同样可以将响应体转换为字符串、文件、字节数组，甚至可以忽略 (`discarding`)。
5.  **超时配置：** 可以在 `HttpClient.newBuilder()` 中设置连接超时 (`connectTimeout`)，也可以在 `HttpRequest.newBuilder()` 中为单个请求设置超时 (`timeout`)。
6.  **异常处理：** 在同步调用中，网络问题会抛出 `IOException` 或 `InterruptedException`。在异步调用中，异常会在 `CompletableFuture` 链的 `exceptionally()` 或 `handle()` 中被捕获。

总而言之，Java 11 的 HTTP Client 是一个设计精良、功能强大且易于使用的现代化工具。对于绝大多数 HTTP 通信场景，它已经完全可以替代笨重的第三方库，成为 Java 开发者的首选。