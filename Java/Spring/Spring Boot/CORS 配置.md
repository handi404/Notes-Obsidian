探讨 **CORS (Cross-Origin Resource Sharing, 跨域资源共享)** 在 Spring Boot 中的配置。这是一个在前后端分离架构中几乎必然会遇到的问题。

---

### 1. 【是什么】秒懂 CORS

想象一下，你的前端应用（比如用 Vue/React 写的）运行在 `http://localhost:8080`，它就像你家。你的 Spring Boot 后端 API 运行在 `http://localhost:9090`，它就像一家披萨店。

出于安全考虑，浏览器有一个内置的“小区保安”——**同源策略 (Same-Origin Policy)**。这个策略规定，你家（`localhost:8080`）的脚本，默认只能访问自己小区（同源）的资源，不能直接去访问隔壁小区披萨店（`localhost:9090`，不同源）的服务。这里的“源”由 **协议、域名、端口** 三者共同决定，任何一个不同，就是“跨域”。

当你的前端应用想调用后端 API 时，“小区保安”（浏览器）会拦住它，说：“等等，你要访问的不是你们小区的，有风险！除非那家披萨店（后端）明确告诉我，它允许你来访问，否则我不能放行。”

**CORS 就是披萨店（后端）给浏览器的一套“通行许可”规则**。后端通过在 HTTP 响应头中添加一些特定的字段（如 `Access-Control-Allow-Origin`），来告诉浏览器：“嘿，保安，`http://localhost:8080` 这个地址是我的朋友，请允许他访问我的资源。” 浏览器看到这个许可后，就会放行，前端就能成功拿到数据。

> **核心一句话：** CORS 是一个 W 3 C 标准，它允许服务器端声明哪些源站有权限访问其资源，从而克服浏览器的同源策略限制。

---

### 2. 【怎么配】Spring Boot 中的 CORS 配置方案

Spring Boot 提供了非常优雅的 CORS 配置方式。主要有三种，我会重点推荐第一种。

#### 方案一：全局配置 (推荐 👍👍👍)

这是最常用、最推荐的方式，因为它将 CORS 配置集中管理，清晰且易于维护。你只需要实现 `WebMvcConfigurer` 接口即可。

**适用场景：** 应用中大部分或所有接口都需要支持跨域。

**步骤：**
创建一个配置类，例如 `GlobalCorsConfig.java`。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 1. 添加映射路径
                registry.addMapping("/**") // 对所有请求路径都进行CORS配置
                        // 2. 放行哪些原始域
                        .allowedOriginPatterns("*") // 支持所有域的访问，更安全的选择是明确指定域
                        // 3. 放行哪些请求方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 4. 是否允许携带 Cookie
                        .allowCredentials(true)
                        // 5. 放行哪些请求头
                        .allowedHeaders("*")
                        // 6. 暴露哪些头部信息（因为跨域访问时，JS只能访问到几个简单的头，所以需要服务端明确暴露出其他头）
                        .exposedHeaders("Authorization", "Content-Type")
                        // 7. 预检请求的有效期，单位为秒。在有效时间内，浏览器无需为同一请求再次发送预检请求。
                        .maxAge(3600);
            }
        };
    }
}
```

**优点：**
*   **集中管理**：所有 CORS 规则都在一个地方。
*   **全局生效**：一次配置，整个应用受益。
*   **代码清晰**：与业务逻辑完全解耦。

---

#### 方案二：基于注解 `@CrossOrigin` (灵活，但易分散)

如果你只需要对个别 Controller 或方法开启跨域，可以使用 `@CrossOrigin` 注解。

**适用场景：** 只有少数接口需要跨域，或者不同接口需要不同的 CORS 策略。

**示例：**
可以作用于类级别（该 Controller 下所有方法生效）或方法级别（仅该方法生效）。

```java
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 对整个 Controller 的所有接口生效
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600) 
public class MyController {

    @GetMapping("/api/data")
    public String getData() {
        return "Some data";
    }

    @GetMapping("/api/special-data")
    // 方法级别的注解会覆盖类级别的配置
    @CrossOrigin(origins = "http://example.com") 
    public String getSpecialData() {
        return "Some special data";
    }
}
```

**缺点：**
*   **配置分散**：CORS 规则散落在各个 Controller 中，难以统一管理和排查问题。
*   **代码侵入**：与业务代码耦合。

---

#### 方案三：结合 Spring Security (高级)

如果你的项目使用了 Spring Security，**那么必须使用 Spring Security 的方式来配置 CORS**，因为它内置的 `CorsFilter` 会在 Spring Security 的其他过滤器之前执行。如果同时使用了方案一和 Spring Security，可能会导致配置不生效。

**适用场景：** 使用了 Spring Security 的项目。

**步骤 (以最新的 Spring Security 6.x Lambda DSL 为例):**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF，因为我们通常在无状态的 REST API 中使用 Token
            .csrf(csrf -> csrf.disable())
            // 重点：将我们定义的 CorsConfigurationSource 应用到 Spring Security 的- filter chain 中
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // ... 其他安全配置，比如授权规则
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的源，这里使用 List.of() 更简洁
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "https://app.yourdomain.com"));
        // 允许的方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的头
        configuration.setAllowedHeaders(List.of("*"));
        // 是否允许凭证
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有 URL 应用这个配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

### 3. 【参数详解】

| 参数 | `WebMvcConfigurer` 方法 | `@CrossOrigin` 属性 | 解释 |
| :--- | :--- | :--- | :--- |
| **允许的源** | `.allowedOrigins(...)` / `.allowedOriginPatterns(...)` | `origins` / `originPatterns` | 允许访问的客户端源。`*` 表示所有。`allowedOriginPatterns` 支持通配符，更灵活安全。 |
| **允许的方法** | `.allowedMethods(...)` | `methods` | 允许的 HTTP 方法，如 GET, POST, PUT 等。`*` 表示所有。 |
| **允许的头部** | `.allowedHeaders(...)` | `allowedHeaders` | 允许的请求头。前端若发送了自定义 Header（如 `Authorization`），这里必须包含。`*` 表示所有。 |
| **暴露的头部** | `.exposedHeaders(...)` | `exposedHeaders` | 允许前端 JS 访问的响应头。默认情况下，JS 只能访问少数几个，如 `Cache-Control`。若后端在响应头中返回自定义 Header（如 `X-Total-Count`），需在此处暴露。 |
| **允许凭证** | `.allowCredentials(true)` | `allowCredentials="true"` | 是否允许浏览器发送 Cookie 等凭证信息。**注意：`allowCredentials(true)` 不能与 `allowedOrigins("*")` 同时使用**，这是安全策略。 |
| **预检有效期** | `.maxAge(...)` | `maxAge` | 浏览器对预检请求（`OPTIONS` 请求）的缓存时间（秒）。在此时间内，对同一资源的跨域请求不再发送预检。 |

---

### 4. 【扩展与应用】最佳实践与常见“坑”

1.  **【坑】 `allowedOrigins("*")` 与 `allowCredentials(true)` 的冲突**
    *   **现象**：当 `allowCredentials` 设置为 `true` 时，`allowedOrigins` 不能使用通配符 `*`。浏览器会报错。
    *   **原因**：这是出于安全考虑。如果允许任何域都携带凭证访问，会存在巨大的安全风险。
    *   **解决方案**：
        *   **方案 A (推荐)**：使用 `.allowedOriginPatterns("*")` 代替 `.allowedOrigins("*")`。`allowedOriginPatterns` 是 Spring 5.3 引入的，它在服务器端进行模式匹配，可以安全地与 `allowCredentials(true)` 一起使用。
        *   **方案 B**：明确列出所有允许的源，例如 `.allowedOrigins("http://localhost:8080", "https://app.yourdomain.com")`。

2.  **【最佳实践】生产环境不要用 `*`**
    *   在开发环境中，使用 `*` 或 `allowedOriginPatterns("*")` 很方便。
    *   但在**生产环境**中，为了安全，应该**明确指定允许的前端应用的域名**。
    *   **优雅实现**：将允许的域名配置在 `application.yml` 中，通过 `@Value` 注解注入，实现环境隔离。

    **`application.yml`:**
    ```yaml
    cors:
      allowed-origins: http://prod-app.com,https://admin.prod-app.com
    ```
    **配置类:**
    ```java
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    // ... in addCorsMappings
    registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            // ... other settings
    ```

3.  **【坑】自定义请求头 `Authorization`**
    *   如果你的前端请求中包含了自定义的 Header，比如用于 JWT 认证的 `Authorization`，你必须在后端的 CORS 配置中通过 `.allowedHeaders("Authorization")` 或 `.allowedHeaders("*")` 来允许它。否则，预检请求就会失败。

4.  **【排查技巧】如何调试 CORS 问题？**
    *   打开浏览器开发者工具 (F 12)，切换到 **Network (网络)** 面板。
    *   找到失败的请求，通常它会显示为红色，状态为 `(failed)`，类型为 `cors`。
    *   切换到 **Console (控制台)** 面板，这里通常会有非常明确的 CORS 错误信息，例如：“Access to XMLHttpRequest at '...' from origin '...' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.”
    *   根据控制台的错误提示，检查你的后端 CORS 配置是否正确（源、方法、头部是否都已允许）。

### 总结

| 场景 | 推荐方案 | 关键点 |
| :--- | :--- | :--- |
| **通用项目** | 全局配置 `WebMvcConfigurer` | 集中管理，代码解耦。 |
| **使用了 Spring Security** | Spring Security 的 CORS 配置 | 必须在 Security 过滤器链中配置，否则不生效。 |
| **个别接口特殊处理** | `@CrossOrigin` 注解 | 仅用于特例，避免滥用。 |
| **生产环境** | `application.yml` + `@Value` | 外部化配置，安全、灵活。 |

以上就是关于 Spring Boot CORS 配置的讲解。