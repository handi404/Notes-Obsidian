下面分几个步骤，演示如何基于 Spring Framework 6.1+ 的 `RestClient` 和 HTTP 接口（`@HttpExchange`）来声明式地调用外部 HTTP 服务。

---

## 1. 添加依赖

在 Spring Boot 项目中，通常只需引入以下 starter，就能同时获得 `RestClient`、HTTP 接口支持等所需组件：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

> 该 starter 会拉入 `spring-web`（含 `RestClient`、`HttpServiceProxyFactory` 等类）以及对 WebFlux 的依赖，从而支持 HTTP 接口代理。

---

## 2. 定义 HTTP 接口

新建一个 Java 接口，用 `@HttpExchange`（可写在类上作公共前缀），并在方法上用 `@GetExchange`、`@PostExchange` 等指定 HTTP 方法和路径：

```java
package com.example.client;

import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PathVariable;
import com.example.dto.UserDTO;

@HttpExchange(url = "/users")      // 全部方法的基础路径
public interface UserClient {

    /** GET https://api.example.com/users/{id} */
    @GetExchange("/{id}")
    UserDTO getById(@PathVariable("id") Long id);

    // 还可以定义 post/put/delete 等方法……
}
```

> 这样就只写“**声明**”，无需自己拼 `HttpRequest`、解析 `Response`，Spring 会为你生成实现。
https://docs.spring.io/spring-framework/reference/integration/rest-clients.html
---

## 3. 配置 `RestClient` 与代理工厂

在一个 `@Configuration` 类中，先创建一个带 baseUrl 的 `RestClient`，再将其适配到 `HttpServiceProxyFactory`，生成接口的代理 Bean：

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import com.example.client.UserClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                         .baseUrl("https://api.example.com")  // 外部服务根地址
                         .build();
    }

    @Bean
    public UserClient userClient(RestClient restClient) {
        // 1) 用 RestClient 构建适配器  
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        // 2) 创建 Proxy 工厂  
        HttpServiceProxyFactory factory =
             HttpServiceProxyFactory.builderFor(adapter)
                                    .build();
        // 3) 生成接口代理  
        return factory.createClient(UserClient.class);
    }
}
```

- `RestClientAdapter.create(restClient)`：将 `RestClient` 转为 HTTP 接口可用的适配器
    
- `HttpServiceProxyFactory.builderFor(...)`：用适配器构建工厂
    
- `factory.createClient(...)`：生成接口实现，并注册为 Spring Bean 
    
https://docs.spring.io/spring-framework/reference/integration/rest-clients.html
---

## 4. 在业务中注入并调用

在任意 `@Service`、`@Component` 中直接注入 `UserClient`，调用就像调用本地方法一样：

```java
package com.example.service;

import org.springframework.stereotype.Service;
import com.example.client.UserClient;
import com.example.dto.UserDTO;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDTO fetchUser(Long id) {
        // 实际发起 HTTP GET https://api.example.com/users/{id}
        return userClient.getById(id);
    }
}
```

---

## 5. （可选）定制拦截器、超时、错误处理

- **拦截器**  
    `RestClient.builder().requestInterceptor((request, next) -> {…}).build()`
    
- **错误处理**  
    `builder().defaultStatusHandler(HttpStatusCode::isError, (req, res) -> {…})`
    
- **超时**  
    通过底层 HTTP 客户端（如 `HttpComponentsClientHttpRequestFactory`）配置 connect/read 超时
    https://docs.spring.io/spring-framework/reference/integration/rest-clients.html

---

### 小结

1. **定义**：用 `@HttpExchange` 在接口上声明外部服务契约
    
2. **适配**：用 `RestClientAdapter` + `HttpServiceProxyFactory` 将接口生成代理
    
3. **调用**：直接注入接口，像调用本地方法一样使用
    

如此即可用最少的样板代码，获得类型安全、可注入、可测试的声明式 HTTP 客户端。

---

## Deepseek
以下是使用 Spring 声明式 HTTP 客户端（基于 `RestClient`）创建调用外部服务接口的详细步骤：

---

### 1. **环境准备：添加依赖**
确保项目使用 **Spring Boot 3.x** 或更高版本（需要 Spring Framework 6+），并在 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

`RestClient` 是 Spring 6 引入的新 HTTP 客户端，集成在 `spring-web` 模块中，无需额外依赖。

---

### 2. **定义声明式 HTTP 客户端接口**
通过接口和注解描述 HTTP 请求，无需实现类。

#### 示例接口：
```java
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {

    // GET 请求，查询参数
    @GetExchange("/api/inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    // POST 请求，JSON 请求体
    @PostExchange("/api/inventory/update")
    void updateStock(@RequestParam String skuCode, @RequestBody StockRequest request);
}
```

#### 注解说明：
- **`@GetExchange` / `@PostExchange`**：定义 HTTP 方法及路径。
- **`@RequestParam`**：将参数作为查询字符串（如 `?skuCode=ABC`）。
- **`@RequestBody`**：将参数序列化为请求体（如 JSON）。

---

### 3. **配置 `RestClient` 和代理工厂**
创建配置类，生成接口的代理实例。

#### 配置类示例：
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient inventoryClient() {
        // 1. 构建 RestClient
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .defaultHeader("Accept", "application/json")
                .build();

        // 2. 适配 RestClient 为 RestClientAdapter
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        // 3. 创建代理工厂
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        // 4. 生成接口代理
        return factory.createClient(InventoryClient.class);
    }
}
```

#### 配置详解：
- **`baseUrl`**：服务的基础 URL（如 `http://localhost:8080`）。
- **`defaultHeader`**：设置默认请求头。
- **`HttpServiceProxyFactory`**：将接口方法转换为 HTTP 请求的动态代理工厂。

---

### 4. **在业务类中注入并使用客户端**
通过依赖注入调用接口方法。

#### 使用示例：
```java
@Service
public class OrderService {

    private final InventoryClient inventoryClient;

    public OrderService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public void processOrder(String skuCode, int quantity) {
        if (inventoryClient.isInStock(skuCode, quantity)) {
            // 库存充足，执行业务逻辑
        } else {
            throw new InsufficientStockException("库存不足");
        }
    }
}
```

---

### 5. **处理复杂场景**

#### 路径参数
使用 `{变量}` 和 `@PathVariable`：
```java
@GetExchange("/api/products/{id}")
Product getProduct(@PathVariable String id);
```

#### 自定义请求头
通过 `@RequestHeader` 添加动态头：
```java
@GetExchange("/api/data")
String fetchData(@RequestHeader("Authorization") String token);
```

#### 文件上传
使用 `MultiPartFile` 和 `@HttpExchange`：
```java
@PostExchange(contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
void uploadFile(@RequestPart("file") MultiPartFile file);
```

---

### 6. **错误处理**
自定义异常或响应状态码处理：

#### 全局异常处理：
```java
@Bean
public RestClient restClient() {
    return RestClient.builder()
        .baseUrl(inventoryServiceUrl)
        .defaultStatusHandler(HttpStatusCode::isError, (req, res) -> {
            throw new ServiceException("服务调用失败: " + res.getStatusCode());
        })
        .build();
}
```

---

### 7. **高级配置**

#### 超时设置
```java
RestClient.builder()
    .baseUrl(inventoryServiceUrl)
    .requestFactory(new HttpComponentsClientHttpRequestFactory())
    .build();
```

在 `HttpComponentsClientHttpRequestFactory` 中设置连接和读取超时。

#### 拦截器
添加请求拦截器：
```java
RestClient.builder()
    .baseUrl(inventoryServiceUrl)
    .requestInterceptor((request, body, execution) -> {
        request.getHeaders().set("X-Custom-Header", "value");
        return execution.execute(request, body);
    })
    .build();
```

---

### 8. **测试**
使用 `MockRestServiceServer` 模拟服务端响应：

```java
@SpringBootTest
public class InventoryClientTest {

    @Autowired
    private InventoryClient inventoryClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.bindTo(inventoryClient).build();
    }

    @Test
    void testIsInStock() {
        mockServer.expect(requestTo("/api/inventory?skuCode=123&quantity=5"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

        boolean result = inventoryClient.isInStock("123", 5);
        assertTrue(result);
        mockServer.verify();
    }
}
```

---

### 总结
通过 **声明式接口 + RestClient 配置**，Spring 简化了 HTTP 客户端的编写：
1. **定义接口**：用注解描述请求方法、路径和参数。
2. **配置代理工厂**：通过 `RestClient` 和 `HttpServiceProxyFactory` 生成实现类。
3. **注入使用**：像调用本地方法一样发起 HTTP 请求。

优势包括 **代码简洁**、**类型安全** 和 **易维护性**，适用于微服务间的 REST API 调用。