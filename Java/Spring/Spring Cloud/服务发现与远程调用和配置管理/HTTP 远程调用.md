首先，注册了服务实例（如使用 Eureka、Nacos 等）。

服务名称：每个服务在服务注册中心的标识，相当于Java中的类名。
服务实例：网络中提供服务的实例，具有IP和端口，相当于Java中的对象，一个实例即为运行在服务器
上的一个进程。

执行流程：
1、服务提供方将自己注册到服务注册中心
2、服务消费方从注册中心获取服务地址
3、进行远程调用

# OpenFeign

# WebClient
# RestClient

这是 Spring Framework 6.1 / Spring Boot 3.2 之后官方**主推**的用于同步阻塞式 HTTP 调用的现代化客户端。

### 一、它是什么？用来做什么？是什么类型的远程服务调用？

#### 1. 是什么？
`RestClient` 是一个现代化的、流畅（Fluent API）的 **同步、阻塞式** HTTP 请求客户端。你可以把它看作是经典 `RestTemplate` 的一个全新、更优雅的替代品。

#### 2. 用来做什么？
它的核心任务就是让你的 Spring 应用能够以一种非常直观、可读性强的方式，去调用外部的 RESTful API。比如：
*   调用另一个微服务的接口。
*   请求第三方服务（如天气查询、支付网关等）。
*   任何需要你的后端应用作为客户端发起 HTTP 请求的场景。

#### 3. 是什么类型的远程服务调用？
**同步阻塞式 (Synchronous Blocking)**。

这一点至关重要，也是它与 `WebClient` 的核心区别：
*   **同步阻塞 (`RestClient`)**: 当你调用 `restClient.get()...retrieve()` 时，执行该代码的线程会**暂停**（阻塞），直到收到 HTTP 响应或超时。这非常符合传统的、线性的编程思维，代码写起来简单直接。
*   **异步非阻塞 (`WebClient`)**: 当你调用 `webClient.get()...retrieve()` 时，它会立即返回一个代表未来结果的对象（`Mono` 或 `Flux`），当前线程不会阻塞，可以继续做其他事。当响应返回时，你预先定义好的回调逻辑才会在另一个线程上执行。这是响应式编程范式，适合高并发、吞吐量要求极高的场景。

**一句话总结**：`RestClient` 是 Spring 官方为我们提供的、用于替代 `RestTemplate` 的、写法更现代、功能更强大的**同步阻塞**调用工具。

---

### 二、各种配置使用（注解与编程方式）

`RestClient` 的核心魅力在于其编程体验，它没有像 OpenFeign 那样复杂的注解体系，而是通过**链式调用 (Method Chaining)** 来构建请求。

#### 1. 基础配置与创建

首先，确保你的项目依赖了 `spring-boot-starter-web`，它已经包含了 `spring-web` 模块。

最推荐的配置方式是在配置类中注入一个 `RestClient.Builder`，然后用它来创建 `RestClient` 实例。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // 1. 将 RestClient.Builder 注入到 Spring 容器
    // Spring Boot 会自动配置好这个 Builder，包含消息转换器等
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    // 2. 使用 Builder 创建一个预配置好的 RestClient 实例
    // 比如，为调用用户服务创建一个特定的 Client
    @Bean("userRestClient")
    public RestClient userRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://user-service/api/v1") // 设置基础URL
                .defaultHeader("X-Source", "OrderService") // 设置默认请求头
                .build();
    }
}
```

#### 2. Fluent API 详解（核心用法）

假设我们已经注入了上面配置的 `userRestClient`。

```java
@Service
public class UserServiceCaller {

    private final RestClient restClient;

    // 使用 @Qualifier 指定要注入的 Bean
    public UserServiceCaller(@Qualifier("userRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // 示例1：GET 请求，获取用户详情，返回一个对象
    public UserDTO getUserById(Long id) {
        return restClient.get()
                .uri("/users/{id}", id) // URI，支持路径变量
                .accept(MediaType.APPLICATION_JSON) //期望响应类型
                .retrieve() // 发送请求并获取响应体
                .body(UserDTO.class); // 将响应体转为 UserDTO 对象
    }

    // 示例2：POST 请求，创建用户，发送一个对象
    public void createUser(UserCreationRequest request) {
        restClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON) // 请求体类型
                .body(request) // 设置请求体
                .retrieve() // 发送请求
                .toBodilessEntity(); // 我们不关心返回体，但关心响应状态码
    }
    
    // 示例3：GET 请求，处理更复杂的返回类型，比如 List<UserDTO>
    public List<UserDTO> getAllUsers() {
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserDTO>>() {}); // 使用 ParameterizedTypeReference 处理泛型
    }

    // 示例4：高级用法 - 错误处理
    public UserDTO getUserWithCustomErrorHandling(Long id) {
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                // 当遇到4xx或5xx错误时，不再抛出默认异常，而是自定义处理
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    // response.getStatusCode(), response.getHeaders(), response.getBody()
                    throw new CustomUserServiceException("API call failed with status: " + response.getStatusCode());
                })
                .body(UserDTO.class);
    }
    
    // 示例5：更高级的用法 - exchange 方法，完全控制响应
    public String getUsername(Long id) {
        // exchange 方法给你原始的 ClientHttpResponse，需要你手动处理所有东西
        return restClient.get()
                .uri("/users/{id}", id)
                .exchange((request, response) -> { // 使用 exchange 方法
                    if (response.getStatusCode().is2xxSuccessful()) {
                        // 假设我们只需要从复杂的JSON中提取 username 字段
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response.getBody());
                        return root.get("username").asText();
                    }
                    // 注意：使用 exchange 必须消费或关闭响应体，否则可能导致连接泄露
                    // try-with-resources 是最佳实践
                    throw new RuntimeException("Failed to get user");
                });
    }
}
```

**方法链详解**：

*   **HTTP 方法**: `.get()`, `.post()`, `.put()`, `.delete()`, `.patch()`, `.head()`, `.options()`
*   **URI**: `.uri(String, Object...)` 或 `.uri(URI)`。支持 URI 模板和变量。
*   **请求头**: `.header(String, String...)`, `.accept(MediaType)`, `.contentType(MediaType)`
*   **请求体**: `.body(Object)`
*   **执行与响应**:
    *   `.retrieve()`: **最常用**。一个简化的 API，用于处理响应体。如果状态码是 4xx 或 5xx，它会默认抛出 `HttpClientErrorException` 或 `HttpServerErrorException`。
    *   `.exchange(Function)`: **更底层**。提供完整的 `ClientHttpResponse` 对象，你需要手动检查状态码、读取响应体、处理错误，并且**必须负责关闭响应流**。适合需要精细控制的场景。
*   **响应体转换**:
    *   `.body(Class<T>)`: 转换为指定的类。
    *   `.body(ParameterizedTypeReference<T>)`: 用于转换泛型类型，如 `List<String>`。
    *   `.toEntity(Class<T>)`: 转换为 `ResponseEntity<T>`，包含响应头、状态码和响应体。
    *   `.toBodilessEntity()`: 转换为 `ResponseEntity<Void>`，只关心响应头和状态码。

#### 3. 关于注解 `@HttpExchange` (声明式客户端)
严格来说 `RestClient` 本身不依赖注解，但 Spring 6 引入了声明式 HTTP 接口机制，它**可以使用 `RestClient` 作为底层实现**。这让你可以像使用 Feign 一样，通过接口和注解来定义 HTTP 调用。

```java
// 1. 定义接口
@HttpExchange(url = "/api/v1/users", accept = "application/json", contentType = "application/json")
public interface UserClient {

    @GetExchange("/{id}")
    UserDTO getUserById(@PathVariable Long id);

    @PostExchange
    void createUser(@RequestBody UserCreationRequest request);
}

// 2. 创建代理 Bean
@Configuration
public class HttpInterfaceConfig {

    @Bean
    public UserClient userClient(@Qualifier("userRestClient") RestClient restClient) {
        // 使用 RestClientAdapter 将 RestClient 适配为声明式客户端所需
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserClient.class);
    }
}

// 3. 在 Service 中直接注入和使用
@Service
public class AnotherService {
    private final UserClient userClient;

    public AnotherService(UserClient userClient) {
        this.userClient = userClient;
    }
    
    public void doSomething() {
        UserDTO user = userClient.getUserById(123L);
        // ...
    }
}
```
**这种方式是 `RestClient` 和注解结合的最佳实践，兼具了 Feign 的简洁和 `RestClient` 的现代化底层。**

---

### 三、场景：调用多个不同基础 URL 的服务

这是一个非常常见的需求，有以下几种优雅的解决方案：

#### 方案一：为每个服务创建独立的 `RestClient` Bean (推荐)

就像我们上面例子中做的，这是最清晰、最符合 Spring DI 思想的方式。

```java
@Configuration
public class MultipleRestClientConfig {

    // 注入通用的 Builder
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean("userRestClient")
    public RestClient userRestClient(RestClient.Builder builder) {
        return builder.baseUrl("http://user-service/api").build();
    }

    @Bean("productRestClient")
    public RestClient productRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://api.product-platform.com/v2").build();
    }
    
    @Bean("paymentRestClient")
    public RestClient paymentRestClient(RestClient.Builder builder) {
        // 甚至可以为特定 client 添加拦截器等
        return builder.baseUrl("http://payment-gateway")
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().set("Authorization", "Bearer some_token");
                    return execution.execute(request, body);
                })
                .build();
    }
}
```
**使用时**：通过 `@Qualifier("productRestClient")` 来精确注入你需要的实例。

**优点**：
*   职责单一，配置隔离。
*   每个 Client 可以有自己独立的默认头、拦截器、超时等配置。
*   代码可读性极高。

#### 方案二：创建一个通用的 `RestClient`，每次调用时指定完整 URL

如果你不希望在容器中创建太多 Bean，或者调用的服务非常多且不固定。

```java
@Configuration
public class GenericRestClientConfig {
    @Bean
    public RestClient genericRestClient() {
        // 创建一个没有任何 baseUrl 的通用客户端
        return RestClient.create(); 
    }
}

@Service
public class DynamicCallerService {
    private final RestClient restClient;

    public DynamicCallerService(RestClient genericRestClient) {
        this.restClient = genericRestClient;
    }

    public UserDTO getUser() {
        // 每次调用都提供完整的 URL
        return restClient.get()
                .uri("http://user-service/api/users/1")
                .retrieve()
                .body(UserDTO.class);
    }

    public ProductDTO getProduct() {
        return restClient.get()
                .uri("https://api.product-platform.com/v2/products/abc")
                .retrieve()
                .body(ProductDTO.class);
    }
}
```
**优点**：灵活。 **缺点**：URL 硬编码在业务代码中，不易维护和管理。

---

### 四、场景：外部服务返回类型在本模块不存在

这个问题本质上是模块间解耦和依赖管理的问题。`RestClient` 本身只负责 HTTP 通信和反序列化，它要求目标类型的 `.class` 文件必须在当前模块的 Classpath 中。

#### 方案一：创建共享 DTO 模块 (最佳实践)

这是微服务架构中最标准、最推荐的做法。
1.  **创建一个独立的 Maven/Gradle 模块**，例如 `common-api` 或 `user-api-client`。
2.  **在这个模块中定义 DTOs** (Data Transfer Objects)，比如 `UserDTO.java`。
3.  **服务提供方 (user-service)** 和 **服务消费方 (order-service)** 都**依赖**这个共享模块。

```
// project-root
//  ├── user-service (提供接口)
//  │   └── pom.xml (依赖 common-api)
//  ├── order-service (调用接口)
//  │   └── pom.xml (依赖 common-api)
//  └── common-api
//      └── src/main/java/.../dto/UserDTO.java
```

**优点**：
*   **类型安全**：编译时就能确保类型一致。
*   **代码复用**：避免在多个模块中重复定义同样的类。
*   **单一事实来源**：DTO 的定义只有一个地方，维护方便。

#### 方案二：反序列化为通用类型 (灵活但损失类型信息)

如果因为某些原因无法或不想引入共享模块（比如对方 API 不稳定，或者你只关心其中一两个字段），可以把响应体反序列化为 `Map` 或 `JsonNode`。

```java
public String getUsernameFromRemote(Long id) {
    // 使用 Map
    Map<String, Object> userAsMap = restClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    
    return (String) userAsMap.get("username");

    // 或者使用 Jackson 的 JsonNode，功能更强大
    JsonNode userAsNode = restClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .body(JsonNode.class);

    return userAsNode.get("profile").get("name").asText();
}
```

**优点**：
*   无需为 DTO 创建具体的类，解耦性强。
*   适合处理动态或不固定的 JSON 结构。

**缺点**：
*   **丢失类型安全**：你需要手动从 `Map` 或 `JsonNode` 中取值并进行类型转换，容易出错 (`ClassCastException`)。
*   代码可读性和可维护性下降。

#### 方案三：在消费方模块中复制 DTO 类 (不推荐)
直接在你的模块里创建一个一模一样的 `UserDTO.java`。

**优点**：简单粗暴，快速解决问题。
**缺点**：
*   **高耦合、高维护成本**：如果源服务的 DTO 发生变化（比如增加或修改字段），你必须手动同步修改你的副本，否则会在运行时出现反序列化错误。
*   违反了 DRY (Don't Repeat Yourself) 原则。
*   **只适用于非常稳定、极少变更，且你无法获取其共享 DTO 库的第三方 API。**

### 总结与要点

1.  **定位**: `RestClient` 是 Spring Boot 3.2+ 中 `RestTemplate` 的官方继任者，用于**同步阻塞**调用。
2.  **优势**: 流畅的链式 API、直观的错误处理 (`.onStatus`)、与 Spring 生态无缝集成。
3.  **现代用法**: 结合 `@HttpExchange` 接口，可以实现类似 OpenFeign 的声明式调用，是目前非常推荐的模式。
4.  **多服务调用**: 最佳实践是为每个目标服务配置一个带 `baseUrl` 的、独立的 `RestClient` Bean。
5.  **类型依赖**: 处理模块间类型依赖的最佳实践是建立一个共享的 `DTO/API` 模块。