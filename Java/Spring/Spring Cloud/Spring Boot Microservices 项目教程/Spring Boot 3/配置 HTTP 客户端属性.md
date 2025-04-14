从 Spring Boot 3.4.0 起，原先基于  `ClientHttpRequestFactorySettings` 的配置方式已被弃用，推荐使用其他方式来配置 HTTP 客户端属性。一种常见方案有两种：

---

### 1. 使用 `RestTemplateBuilder` 配置 RestTemplate

Spring Boot 提供了 `RestTemplateBuilder` 来简化 `RestTemplate` 的配置。通过它可以直接设置连接超时和读取超时，例如：

```java
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 设置连接超时和读取超时
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }
}
```

这种方式利用 Spring Boot 自动配置，代码更加简洁且易于维护。

---

### 2. 直接创建具体的 `ClientHttpRequestFactory` 实例

如果不借助 `RestTemplateBuilder`，也可以直接创建一个 `ClientHttpRequestFactory` 的实例，并设置超时参数。以最简单的实现——使用 `SimpleClientHttpRequestFactory` 为例：

```java
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class MyHttpClient {

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 单位：毫秒
        factory.setReadTimeout(3000);    // 单位：毫秒
        return factory;
    }
}
```

如果你需要更高级的特性（例如连接池管理、SSL 配置等），也可以考虑使用 Apache HttpComponents 提供的 `HttpComponentsClientHttpRequestFactory`，配置方式与上述类似。

---

### 总结

由于 `ClientHttpRequestFactorySettings` 在 Spring Boot 3.4.0 后已弃用，建议使用更加灵活且易于定制的方案，比如通过 `RestTemplateBuilder` 注入超时配置。如果你的应用中不直接使用 `RestTemplate`，而是需要自定义 HTTP 请求工厂，那么直接实例化并配置具体的实现类（如 `SimpleClientHttpRequestFactory` 或 `HttpComponentsClientHttpRequestFactory`）也是一个简单且直接的替代方案。

---

## 完整指南
---
以下是为 Spring Boot 应用配置 HTTP 客户端的完整指南，涵盖主流配置项和最佳实践：

---

### 一、基础配置（SimpleClientHttpRequestFactory）

```java
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.time.Duration;

private ClientHttpRequestFactory createBasicFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    
    // 核心配置参数
    factory.setConnectTimeout((int) Duration.ofSeconds(3).toMillis());  // 连接建立超时
    factory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());     // 数据读取超时
    factory.setBufferRequestBody(true);  // 是否缓冲请求体（默认true）
    
    // 高级参数（需强制类型转换）
    ((SimpleClientHttpRequestFactory) factory).setOutputStreaming(false); // 禁用流式上传
    
    return factory;
}
```

**配置项说明表**：

| 参数                      | 类型 | 默认值 | 说明                                                                 |
|---------------------------|------|--------|--------------------------------------------------------------------|
| `connectTimeout`          | int  | -1     | 连接建立超时（毫秒），-1 表示无限                                   |
| `readTimeout`             | int  | -1     | 数据读取超时（毫秒）                                               |
| `bufferRequestBody`       | bool | true   | 是否缓存请求体，设为 false 时支持大文件流式上传                    |
| `outputStreaming`         | bool | true   | 是否使用流式输出，设为 false 时强制缓冲整个请求体                   |

---

### 二、高级配置（Apache HttpClient）

需要先添加依赖：
```xml
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
</dependency>
```

```java
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

private ClientHttpRequestFactory createAdvancedFactory() {
    // 连接池配置
    PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
    pool.setMaxTotal(200);           // 最大连接数
    pool.setDefaultMaxPerRoute(50);  // 单路由最大连接数
    
    // 请求配置
    RequestConfig config = RequestConfig.custom()
        .setConnectTimeout(Duration.ofSeconds(3))   // 连接超时
        .setResponseTimeout(Duration.ofSeconds(5))   // 响应超时
        .setConnectionRequestTimeout(Duration.ofSeconds(2)) // 获取连接超时
        .build();

    // 构建客户端
    CloseableHttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(pool)
        .setDefaultRequestConfig(config)
        .disableCookieManagement()  // 禁用 Cookie
        .disableRedirectHandling() // 禁用自动重定向
        .setProxy(new HttpHost("proxy.example.com", 8080)) // 代理配置
        .build();

    return new HttpComponentsClientHttpRequestFactory(httpClient);
}
```

**高级配置项说明**：

1. **连接池管理**：
   ```java
   pool.setValidateAfterInactivity(Duration.ofSeconds(30)); // 空闲连接校验间隔
   pool.setDefaultSocketConfig(SocketConfig.custom()
       .setSoTimeout(Duration.ofSeconds(10)) // Socket 读写超时
       .build());
   ```

2. **SSL 安全配置**：
   ```java
   SSLContext sslContext = SSLContexts.custom()
       .loadTrustMaterial(trustStore, "password".toCharArray()) // 信任库
       .build();
   
   httpClient = HttpClients.custom()
       .setSSLContext(sslContext)
       .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // 跳过主机验证
       .build();
   ```

3. **重试策略**：
   ```java
   .setRetryStrategy(new DefaultHttpRequestRetryStrategy(
       3,                          // 最大重试次数
       Duration.ofSeconds(1))      // 重试间隔
   )
   ```

---

### 三、完整配置示例（含拦截器）

```java
public class CustomHttpConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        // 连接池
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(100);
        pool.setDefaultMaxPerRoute(20);

        // 请求配置
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(Duration.ofSeconds(3))
            .setResponseTimeout(Duration.ofSeconds(10))
            .build();

        // 拦截器链
        HttpRequestInterceptor loggingInterceptor = (request, entity, context) -> {
            System.out.println("Request URI: " + request.getRequestUri());
        };

        // 构建客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setConnectionManager(pool)
            .setDefaultRequestConfig(config)
            .addInterceptorFirst(loggingInterceptor)
            .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
            .evictExpiredConnections()
            .evictIdleConnections(Duration.ofSeconds(30))
            .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
```

---

### 四、配置项速查表

| 分类            | 配置项                     | 推荐值              | 说明                                                                 |
|-----------------|---------------------------|---------------------|--------------------------------------------------------------------|
| **超时控制**    | Connect Timeout           | 3-5 秒              | 建立 TCP 连接的最长等待时间                                         |
|                 | Read Timeout              | 10-30 秒            | 等待服务器响应的最长时间                                           |
| **连接池**      | Max Total                 | 根据 QPS 调整        | 总连接数 = QPS × 平均响应时间(秒) × 冗余系数(1.2-2)                |
|                 | Max Per Route             | 总连接数的 1/3       | 单个服务地址的最大连接数                                           |
|                 | Idle Timeout              | 30-60 秒            | 空闲连接保留时间                                                   |
| **重试策略**    | Max Retries               | 2-3 次              | 对可重试请求的最大重试次数                                         |
|                 | Retry Interval            | 1 秒                | 指数退避算法的基准间隔                                             |
| **高级**        | Keep-Alive                | 启用                | 复用 TCP 连接                                                      |
|                 | GZIP 压缩                | 根据响应大小启用     | 对大于 1 KB 的响应启用压缩                                          |
|                 | DNS 刷新间隔             | 5 分钟              | 防止 DNS 缓存问题                                                  |

---

### 五、最佳实践建议

1. **超时设置**：
   - 连接超时应小于服务的熔断超时
   - 读取超时 = 服务最大预期响应时间 + 网络抖动余量（推荐 20-30%）

2. **连接池调优公式**：
   ```
   最大连接数 = (QPS × P99响应时间(秒)) / 实例数 × 安全系数(1.5)
   ```

3. **监控指标**：
   ```java
   // 获取连接池状态
   PoolStats stats = pool.getTotalStats();
   System.out.println("可用连接: " + stats.getAvailable());
   System.out.println("租用连接: " + stats.getLeased());
   ```

4. **防御性配置**：
   ```java
   // 防止 DNS 欺骗
   .setDnsResolver(new SystemDefaultDnsResolver() {
       @Override
       public InetAddress[] resolve(String host) throws UnknownHostException {
           return super.resolve(host); // 可在此处添加自定义解析逻辑
       }
   })
   ```

---

### 六、根据需求选择工厂

| 特性                      | SimpleClientHttpRequestFactory | HttpComponentsClientHttpRequestFactory |
|---------------------------|---------------------------------|----------------------------------------|
| 连接池支持                | ❌                              | ✔️                                     |
| 高级超时控制              | 基本                          | 精细（连接请求/响应/空闲超时）        |
| 拦截器机制                | ❌                              | ✔️                                     |
| HTTP/2 支持               | ❌                              | ✔️ (需额外配置)                        |
| 内存消耗                  | 低                             | 中高（连接池开销）                    |
| 适用场景                  | 简单低频请求                   | 生产环境高频请求                      |

根据实际需求选择合适的实现，建议生产环境优先使用基于 Apache HttpClient 的配置方案。