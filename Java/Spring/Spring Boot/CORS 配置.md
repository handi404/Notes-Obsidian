CORS（跨域资源共享，Cross-Origin Resource Sharing）是一种浏览器安全机制，用于解决浏览器“同源策略”带来的跨域访问限制。在 Spring Boot 项目中，配置 CORS 主要是为了解决前后端分离架构中，由于前端和后端部署在不同域名或端口上而产生的跨域请求问题。

---

## 为什么需要配置 CORS？

### **同源策略的限制**

浏览器的同源策略要求：

- 协议、域名、端口必须完全一致，否则就被视为跨域请求。
- 跨域请求在默认情况下会被浏览器拦截，从而保护用户数据和资源的安全。

### **CORS 的作用**

CORS 通过服务器在响应头中添加特定字段，告诉浏览器允许来自特定来源的跨域请求。其主要作用包括：

- **允许跨域请求**：解决前端（如 Angular、React、Vue）和后端（Spring Boot）分离部署时跨域调用 REST API 的问题。
- **细粒度控制**：可以设置允许哪些域、哪些 HTTP 方法、哪些请求头以及是否允许携带凭证（如 Cookies）。

---

## 如何在 Spring Boot 中配置 CORS？

有多种方式可以配置 CORS，下面介绍几种常用方法。

### **1. 全局配置（Global Configuration）**

通过实现 `WebMvcConfigurer` 接口，可以对所有请求进行全局的 CORS 配置。例如：

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有 URL 生效
                .allowedOrigins("http://localhost:3000", "http://example.com") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true) // 是否允许发送 Cookie 等凭证信息
                .maxAge(3600);  // 预检请求的缓存时间（秒）
    }
}
```

**解析：**

- `addMapping("/**")` 表示对所有请求路径都生效。
- `allowedOrigins(...)` 指定允许跨域的来源，若要允许所有来源可使用 `"*"`（注意：当 `allowCredentials(true)` 时不能使用 `"*"`）。
- `allowedMethods(...)` 配置允许跨域请求的方法。
- `allowedHeaders("*")` 表示允许所有请求头。
- `allowCredentials(true)` 表示是否允许发送 Cookie 和其他凭证信息。

### **2. 使用 `@CrossOrigin` 注解**

可以在控制器或具体的接口方法上使用 `@CrossOrigin` 注解进行局部配置。例如：

```java
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class MyController {

    @GetMapping("/api/data")
    public String getData() {
        return "Hello, CORS!";
    }
}
```

**解析：**

- `@CrossOrigin` 注解可以直接定义允许跨域的源、方法、头信息等。
- 使用该方式适用于某些接口需要特殊的跨域策略，而不希望全局生效的情况。

### **3. 使用过滤器（CorsFilter）**

也可以通过注册自定义过滤器来实现 CORS 配置，适用于对 CORS 请求做更细致的处理。

```java
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        // 添加 CORS 响应头
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Max-Age", "3600");

        // 对预检请求直接响应
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
```

**解析：**

- 这种方式通过自定义过滤器添加响应头，适合需要对 CORS 做全局拦截或额外处理逻辑的场景。

---

## 总结

- **为何配置 CORS？**  
    浏览器出于安全考虑，默认阻止跨域请求，而前后端分离项目中常常需要跨域访问 API。配置 CORS 可以明确告知浏览器允许跨域请求，确保前后端正常通信。
    
- **作用是什么？**  
    CORS 配置可定义允许访问的域、HTTP 方法、请求头及是否支持凭证信息，提供灵活的跨域请求管理。
    
- **如何配置？**  
    可以通过全局配置（实现 `WebMvcConfigurer`）、使用 `@CrossOrigin` 注解或自定义过滤器（`CorsFilter`）来配置 CORS，开发者可根据项目需求选择合适的方式。
    

通过正确配置 CORS，可以确保你的 Spring Boot 项目在前后端分离架构下实现安全、灵活的跨域数据交互。