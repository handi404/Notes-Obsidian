### **CORS（跨域资源共享）概述**

**CORS（Cross-Origin Resource Sharing）** 是一种机制，它允许浏览器进行跨域请求，解决了同源策略（Same-Origin Policy）的限制。在前后端分离的架构中，前端和后端通常部署在不同的域名或端口下。由于浏览器的同源策略，跨域请求会被浏览器阻止。CORS 通过在 HTTP 响应头中添加特定的字段，来告诉浏览器是否允许跨域请求。

### **同源策略与跨域请求**

- **同源策略**：浏览器在发起跨域请求时，会遵循同源策略。同源策略要求请求的目标 URL 和发起请求的页面必须在同一个协议、域名、端口下，否则会被认为是跨域请求。
    
- **跨域请求**：当前端应用和后端服务部署在不同的域、端口或协议下时，浏览器会阻止跨域请求，CORS 允许开发者通过特定的配置让浏览器接受跨域请求。
    

#### **跨域请求示例**：

- **同源请求**：`https://example.com/api` 和 `https://example.com/login` 是同源请求。
- **跨域请求**：`https://example.com/api` 和 `http://api.example.com/login` 是跨域请求，因为它们的协议或端口不同。

### **CORS 的作用**

CORS 允许开发者明确控制哪些来源的请求可以访问资源。通过配置跨域策略，开发者可以：

1. **控制哪些来源可以访问资源**：只允许指定的域名访问 API，增强安全性。
2. **指定允许的 HTTP 方法**：例如只允许 `GET` 和 `POST` 方法，限制其他不必要的方法。
3. **控制允许的请求头**：限制哪些请求头是允许的，如 `Content-Type`、`Authorization` 等。
4. **设置凭证支持**：允许浏览器携带凭证（如 Cookies、Authorization 头等）。

### **CORS 的工作原理**

1. **预检请求（Preflight Request）**：
    
    - 当一个跨域请求使用了某些特殊的 HTTP 方法（如 `PUT`、`DELETE`）或请求头时，浏览器会发送一个 **预检请求**（OPTIONS 请求）来询问目标服务器是否允许跨域请求。
    - 服务器返回响应头 `Access-Control-Allow-Methods` 和 `Access-Control-Allow-Headers`，告知浏览器是否允许实际的跨域请求。
2. **实际请求**：
    
    - 如果预检请求成功，浏览器才会发起实际的请求（如 `GET`、`POST` 等）。

### **CORS 响应头**

CORS 通过 HTTP 响应头来控制跨域请求。常见的 CORS 响应头包括：

1. **`Access-Control-Allow-Origin`**：指定允许访问资源的域。如果值是 `*`，表示允许任何域访问。
2. **`Access-Control-Allow-Methods`**：指定允许的 HTTP 方法（如 `GET`、`POST`、`PUT` 等）。
3. **`Access-Control-Allow-Headers`**：指定允许的请求头（如 `Content-Type`、`Authorization` 等）。
4. **`Access-Control-Allow-Credentials`**：是否允许浏览器发送凭证（如 Cookies）。
5. **`Access-Control-Max-Age`**：指定预检请求的缓存时间，单位为秒。

### **如何配置 CORS**

在 Spring Boot 中，可以通过多种方法配置 CORS，以下是常见的几种方式：

---

### **1. 配置全局 CORS（使用 `WebMvcConfigurer`）**

`WebMvcConfigurer` 接口允许我们在应用启动时配置全局 CORS 策略。通常这种方式适用于全局配置跨域。

#### **代码示例：**

```kotlin
@Configuration
class CorsConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")  // 对所有路径开启 CORS
            .allowedOrigins("http://example.com")  // 允许来自 http://example.com 的请求
            .allowedMethods("GET", "POST")  // 只允许 GET 和 POST 方法
            .allowedHeaders("Content-Type", "Authorization")  // 只允许 Content-Type 和 Authorization 请求头
            .allowCredentials(true)  // 允许携带凭证（如 Cookies）
    }
}
```

#### **解析：**

- `addMapping("/**")`：为所有路径开启 CORS。
- `allowedOrigins("http://example.com")`：只允许 `http://example.com` 域名的请求。如果需要允许多个域，可以传递多个域名。
- `allowedMethods("GET", "POST")`：指定允许的 HTTP 方法。
- `allowedHeaders("Content-Type", "Authorization")`：指定允许的请求头。
- `allowCredentials(true)`：允许携带凭证（例如 Cookies）。

---

### **2. 通过 `@CrossOrigin` 注解配置 CORS**

如果你只希望为某个控制器或某个方法配置 CORS，可以使用 `@CrossOrigin` 注解。这种方式适合于细粒度控制。

#### **代码示例：**

```kotlin
@RestController
@RequestMapping("/api")
class MyController {

    @CrossOrigin(origins = ["http://example.com"], allowedHeaders = ["Content-Type"])
    @GetMapping("/data")
    fun getData(): String {
        return "Data from server"
    }
}
```

#### **解析：**

- `@CrossOrigin` 注解可以放在控制器或方法上，允许指定跨域配置。
- `origins` 指定允许跨域的来源。
- `allowedHeaders` 指定允许的请求头。

---

### **3. 使用 Filter 配置 CORS**

如果你想更灵活地控制 CORS 配置，可以通过 `Filter` 来进行配置。

#### **代码示例：**

```kotlin
@Component
class CorsFilter : Filter {
    
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpResponse = response as HttpServletResponse
        httpResponse.setHeader("Access-Control-Allow-Origin", "*")
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true")
        
        chain.doFilter(request, response)
    }
    
    override fun init(filterConfig: FilterConfig?) {}

    override fun destroy() {}
}
```

#### **解析：**

- 这种方式使用 `Filter` 直接在 HTTP 请求处理链中添加 CORS 头信息。
- 通过 `setHeader` 方法手动设置 CORS 响应头。

---

### **4. Spring Boot 2.x 通过 `CorsMapping` 配置**

Spring Boot 2.x 引入了更加简化的 CORS 配置方式，可以通过 `CorsMapping` 来配置全局 CORS 策略。

#### **代码示例：**

```kotlin
@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000") // 允许 http://localhost:3000 跨域请求
            .allowedMethods("GET", "POST", "PUT")
            .allowedHeaders("Content-Type")
            .allowCredentials(true)
    }
}
```

这种方式与 `WebMvcConfigurer` 配置的第一种方式类似，但这是针对 Spring Boot 2.x 版本的简化方式。

---

### **5. CORS 处理的常见问题**

6. **预检请求（Preflight）**：如果浏览器发送的是带有自定义头信息或使用 `PUT`、`DELETE` 等 HTTP 方法的请求，浏览器会首先发送一个预检请求（OPTIONS），服务器需要响应相应的 CORS 头信息。
    
7. **Access-Control-Allow-Origin**：如果你返回 `*`（允许任何来源），浏览器不允许携带凭证（如 Cookies）。因此，`allowCredentials` 设为 `true` 时，`allowedOrigins` 不能使用 `*`，必须指定具体域名。
    
8. **安全性**：尽量避免在生产环境中使用 `*`，允许所有来源。为了安全起见，应该限制访问的域名。
    

---

### **总结**

- **CORS** 是解决跨域请求问题的标准机制，允许开发者控制哪些来源可以访问资源。
- 在 **Spring Boot** 中，CORS 可以通过多种方式配置：全局配置、控制器级配置、Filter 配置等。
- 配置 CORS 时，可以精确控制请求方法、请求头、响应头以及是否允许携带凭证等。
- **安全性**：在生产环境中，要确保配置只允许信任的域进行跨域请求，避免潜在的安全风险。

---

## 示例
在 cors package 中的 CorsConfig
```kotlin
@Configuration
class CorsConfig {

    @Bean
    fun getCorsConfiguration(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("*")
                    .allowedHeaders("*")
            }
        }
    }
}
```

这段代码定义了一个 Spring Boot 配置类 `CorsConfig`，用于配置 **CORS（跨域资源共享）**，以允许来自不同来源的客户端访问服务器资源。下面我们逐行详细分析代码。

### **1. `@Configuration` 注解**

```kotlin
@Configuration
class CorsConfig {
```

- **作用**：`@Configuration` 注解表示该类是一个 **配置类**，Spring 会自动将其作为应用程序的配置加载。
- 在 Spring Boot 中，配置类通常用于定义 Bean、设置应用程序级别的配置等。

在这个配置类中，我们定义了 CORS 的设置，它会影响所有的 HTTP 请求，允许跨域请求。

### **2. 定义 `getCorsConfiguration` Bean**

```kotlin
@Bean
fun getCorsConfiguration(): WebMvcConfigurer {
```

- **`@Bean` 注解**：这个注解表示该方法返回一个 Bean。Spring 会自动将返回的对象（此处为 `WebMvcConfigurer`）注册为 Spring 应用上下文中的 Bean。
- **返回类型**：`WebMvcConfigurer` 是 Spring 中用来配置 Web MVC 设置的接口。通过实现这个接口，用户可以自定义 HTTP 请求处理过程，如 CORS、拦截器、消息转换器等。

方法 `getCorsConfiguration` 返回一个 `WebMvcConfigurer` 对象，用于配置 CORS 策略。

### **3. 创建匿名 `WebMvcConfigurer` 实现**

```kotlin
return object : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*")
    }
}
```

- 这里使用了 Kotlin 的 **匿名类**（匿名实现），通过 `object : WebMvcConfigurer` 创建一个 `WebMvcConfigurer` 的匿名实现，来覆盖 `addCorsMappings` 方法。
- `addCorsMappings` 方法用于配置 CORS 策略，`CorsRegistry` 提供了设置跨域请求的多种选项。

#### **CORS 配置项解释：**

- **`registry.addMapping("/**")`**：
    
    - `addMapping` 指定了允许跨域的请求路径。在这个例子中，`/**` 表示所有的路径都允许进行跨域请求，即允许任何 API 端点进行跨域访问。
- **`allowedOrigins("*")`**：
    
    - `allowedOrigins` 配置允许跨域请求的源（Origin）。`*` 表示允许来自任何域名的请求。
    - 注意，`allowedOrigins` 可以设置为多个域名，或者使用 `*` 来表示所有域都可以访问。
- **`allowedMethods("*")`**：
    
    - `allowedMethods` 配置允许跨域请求的 HTTP 方法。`*` 表示允许所有常见的 HTTP 方法（如 GET、POST、PUT、DELETE 等）。
    - 如果只想允许某些方法，例如仅允许 `GET` 和 `POST`，可以这样写：`.allowedMethods("GET", "POST")`。
- **`allowedHeaders("*")`**：
    
    - `allowedHeaders` 配置允许跨域请求的 HTTP 请求头。`*` 表示允许所有请求头。
    - 你可以指定特定的请求头，例如 `.allowedHeaders("Authorization", "Content-Type")` 来限制只允许这两个头部。

### **4. 整体代码作用**

这段代码的作用是为 Spring Boot 应用程序配置一个全局的 CORS 策略，允许所有来源的客户端对所有路径、所有 HTTP 方法和所有请求头进行跨域访问。

### **5. CORS 简介**

**CORS（跨域资源共享）** 是一种机制，它允许来自不同来源的请求访问服务器资源。通常在前后端分离的开发中，前端与后端部署在不同的域名或端口下，浏览器会进行同源策略限制，阻止前端发起跨域请求。

CORS 允许服务端通过设置 HTTP 头来告诉浏览器，哪些域名可以访问该服务，哪些方法和请求头是允许的。例如：

- `Access-Control-Allow-Origin`：指定允许哪些域进行访问，通常是一个域名或者 `*`。
- `Access-Control-Allow-Methods`：指定允许的 HTTP 方法，如 GET、POST 等。
- `Access-Control-Allow-Headers`：指定允许的请求头，如 `Content-Type`、`Authorization` 等。

### **6. 为什么要使用 `WebMvcConfigurer` 配置 CORS？**

在 Spring Boot 中，`WebMvcConfigurer` 接口提供了自定义 MVC 配置的能力。`addCorsMappings` 方法允许开发者通过编程方式配置跨域请求的行为。

- **灵活性**：如果你需要自定义不同路径的 CORS 配置，`WebMvcConfigurer` 提供了灵活的方式。
- **全局配置**：如果希望在全局范围内设置 CORS（即所有 API 路径都可以被跨域访问），可以在 `@Configuration` 类中配置 CORS。

### **7. 其他常见 CORS 配置示例**

#### **只允许指定来源**

```kotlin
registry.addMapping("/**")
    .allowedOrigins("https://example.com")  // 只允许来自 https://example.com 的跨域请求
    .allowedMethods("GET", "POST")  // 只允许 GET 和 POST 方法
    .allowedHeaders("Content-Type")  // 只允许 Content-Type 请求头
```

#### **允许带上身份验证信息**

```kotlin
registry.addMapping("/**")
    .allowedOrigins("https://example.com")
    .allowedMethods("*")
    .allowedHeaders("*")
    .allowCredentials(true)  // 允许发送凭证（如 Cookies 和 HTTP 认证信息）
```

#### **允许特定请求头**

```kotlin
registry.addMapping("/**")
    .allowedOrigins("https://example.com")
    .allowedMethods("GET")
    .allowedHeaders("Authorization", "Content-Type")
```

---

### **8. 总结**

- **CORS 配置**：通过 `@Configuration` 类中的 `WebMvcConfigurer` 实现 CORS 配置，允许跨域请求访问。
- **配置项**：可以使用 `allowedOrigins`、`allowedMethods` 和 `allowedHeaders` 等方法来设置跨域请求的来源、方法和头信息。
- **常见应用场景**：在前后端分离的应用中，CORS 是必要的配置，它确保了不同域的前端应用能够访问后端 API。

通过这段配置代码，我们可以灵活地控制跨域请求的行为，确保前端和后端的通信安全、可控。