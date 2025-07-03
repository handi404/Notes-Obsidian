**JSON Web Token (JWT)**。这在现代 Web 开发，尤其是前后端分离和微服务架构中，是认证授权的基石。

---

### JSON Web Token (JWT)

#### 是什么？(What is it?)

一句话概括：**JWT 是一个盖了章的、自包含的数字身份证**。

*   **自包含 (Self-contained)**: 这张“身份证”本身就包含了持有者的信息（比如用户 ID、角色）和有效期等，服务器无需再去数据库查询这些基础信息。
*   **盖了章 (Signed)**: 这张“身份证”带有一个数字签名，可以防止信息被伪造或篡改。服务器收到后，只需用事先约定好的“印章”（密钥）一验，就能知道真伪。
*   **JSON 格式**: “身份证”上的信息是以 JSON 格式存储的，方便程序读写。

它是一个开放标准 (RFC 7519)，定义了一种紧凑且 URL 安全的方式，用于在各方之间安全地传输信息。

#### 何时使用？(When to use it?)

JWT 的核心优势在于其**无状态 (Stateless)** 特性，因此它最适合以下场景：

1.  **前后端分离的 Web 应用 (SPA - Single Page Application)**
    *   前端（如 Vue, React, Angular）与后端 API 服务器是独立部署的。用户登录后，后端 API 只需颁发一个 JWT，前端将其存储起来。之后每次请求 API，前端都在请求头中带上这个 JWT，API 服务器验证即可，完全不需要在服务器端维护 Session。

2.  **微服务架构 (Microservices)**
    *   当一个请求需要流经多个微服务时，JWT 是完美的身份凭证。用户在“认证服务”登录后获得 JWT。当请求到达“订单服务”或“商品服务”时，这些服务只需用公共的密钥验证 JWT 的签名，就能安全地识别用户身份和权限，无需频繁请求“认证服务”。

3.  **移动应用 (Mobile Apps)**
    *   与 SPA 类似，原生 App 调用后端 API 时，使用 JWT 进行认证既简单又高效。

**总结：任何需要“无状态认证”的场景，都是 JWT 的主场。**

#### 结构是什么？(What is its structure?)

一个 JWT 看起来是一长串无意义的字符，但它实际上由三部分组成，用点 `.` 分隔：

`xxxxx.yyyyy.zzzzz`  ->  **Header.Payload.Signature**

让我们拆开来看：

**1. Header (头部)**
*   **内容**: 描述 JWT 元数据，通常包含两部分：
    *   `typ`: 类型，固定为 "JWT"。
    *   `alg`: 使用的签名算法，如 `HS256` (对称加密) 或 `RS256` (非对称加密)。
*   **示例**: `{"alg": "HS256", "typ": "JWT"}`
*   **生成**: 将此 JSON 对象进行 **Base64Url** 编码，得到第一部分 `xxxxx`。

**2. Payload (载荷)**
*   **内容**: 存放有效信息的地方，也称为“声明 (Claims)”。这些信息是这张“身份证”的核心内容。分为三类：
    *   **Registered Claims (注册声明)**: 官方预定义的一些字段，建议使用但非强制。
        *   `iss` (Issuer): 签发者
        *   `sub` (Subject): 主题，通常是用户的唯一标识（如用户 ID）。
        *   `aud` (Audience): 接收方
        *   `exp` (Expiration Time): **极其重要！** 过期时间戳。一旦过期，令牌即失效。
        *   `iat` (Issued At): 签发时间
    *   **Public Claims (公共声明)**: 由使用者自行定义，但为了避免冲突，应在 [IANA JSON Web Token Registry](https://www.iana.org/assignments/json-web-token/json-web-token.xhtml) 中注册。
    *   **Private Claims (私有声明)**: **这是我们最常用的部分**。用于在签发方和接收方之间共享信息，例如：
        *   `userId`: 用户 ID
        *   `username`: 用户名
        *   `roles`: 用户角色列表，如 `["ADMIN", "USER"]`
*   **示例**: `{"sub": "12345", "username": "dave", "roles": ["ADMIN"], "exp": 1678886400}`
*   **生成**: 将此 JSON 对象进行 **Base64Url** 编码，得到第二部分 `yyyyy`。
*   **⚠️ 安全警告**: Payload 仅仅是 Base 64 Url 编码，**不是加密**！任何人都可以解码它看到内容。**绝对不要在 Payload 中存放敏感信息，如密码、手机号等！**

**3. Signature (签名)**
*   **内容**: 对前两部分的签名，防止数据篡改。
*   **生成**:
    1.  准备好 `secret` (密钥)。这个密钥**绝对不能泄露**，它存放在服务器端。
    2.  使用 Header 中指定的签名算法 (`alg`)，对以下内容进行计算：
        `HMACSHA256(Base64Url(Header) + "." + Base64Url(Payload), secret)`
    3.  将计算结果进行 **Base64Url** 编码，得到第三部分 `zzzzz`。

**签名是 JWT 安全的核心**。如果有人篡改了 Header 或 Payload，重新计算出的签名将与原始签名不匹配，验证就会失败。

#### 如何工作？(How does it work?)

这是一个典型的 JWT 认证流程：

1.  **用户登录**: 用户提交用户名和密码到认证服务器。
2.  **服务器验证**: 服务器验证凭证。
3.  **签发 JWT**: 验证成功后，服务器根据用户信息（如 user ID, roles）和预设的过期时间，创建一个 JWT，并用**私有密钥**对其进行签名。
4.  **返回 JWT**: 服务器将生成的 JWT 返回给客户端。
5.  **客户端存储**: 客户端（浏览器）将 JWT 存储起来。最常见的位置是 `localStorage`、`sessionStorage` 或 `HttpOnly` Cookie。
6.  **携带 JWT 请求**: 之后，客户端向受保护的 API 发起请求时，会将 JWT 放在 HTTP 的 `Authorization` 请求头中，格式为 `Bearer <token>`。
    ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsIn...
    ```
7.  **服务器验证 JWT**:
    *   API 服务器获取 `Authorization` 头中的 JWT。
    *   使用**相同的密钥**和算法来验证签名。如果签名无效，说明令牌是伪造的或被篡改，立即拒绝请求。
    *   如果签名有效，再检查 Payload 中的 `exp` 声明，判断令牌是否已过期。
    *   所有检查通过后，服务器就认为该请求是合法的，并从 Payload 中获取用户信息（如用户 ID、角色）进行后续的授权和业务处理。

#### 为什么要用？(Why use it?)

相比传统的基于 Session 的认证，JWT 的优势非常明显：

1.  **无状态与可扩展性 (Stateless & Scalable)**: 这是最大的优点。服务器端不需存储任何 Session 信息。每台服务器都可以用同一个密钥独立验证 JWT。这使得系统可以轻松地水平扩展，添加更多服务器节点，而无需处理复杂的 Session 同步问题。
2.  **解耦与跨域 (Decoupled & CORS-friendly)**: Token 机制天然支持跨域。只要你的 API 服务器配置了正确的 CORS 策略，任何来源的前端应用都可以通过发送 Token 来访问，非常适合微服务和前后端分离。
3.  **多平台适用 (Platform Agnostic)**: JWT 可以在 Web、移动端、桌面应用等多种客户端上通用，一套认证逻辑服务所有平台。
4.  **避免 CSRF 攻击**: 如果你不使用 Cookie 存储 JWT，而是使用 `localStorage` 并通过 `Authorization` 头发送，那么你就从根本上免疫了 CSRF（跨站请求伪造）攻击，因为它依赖浏览器自动发送 Cookie 的特性。

---

### 知识扩展与 Spring Security 应用

在 Spring Security 中集成 JWT 认证，通常需要以下步骤：

1.  **添加依赖**: 引入 JWT 库，如 `io.jsonwebtoken:jjwt-api`, `jjwt-impl`, `jjwt-jackson`。
2.  **禁用默认配置**:
    *   禁用 Session 管理: `.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`
    *   禁用 CSRF: `.csrf(csrf -> csrf.disable())` (因为我们不依赖 Cookie)
    *   禁用 `formLogin` 和 `httpBasic`。
3.  **创建 `JwtUtil` 工具类**: 封装 JWT 的生成和解析（验证）逻辑。
4.  **创建自定义过滤器 (`JwtAuthenticationFilter`)**:
    *   这个过滤器需要继承 `OncePerRequestFilter`。
    *   它的核心逻辑是：在每个请求到达时，从 `Authorization` 头中提取 JWT，使用 `JwtUtil` 进行验证。
    *   如果验证通过，就从 JWT 的 Payload 中解析出用户信息（用户名、权限），然后构建一个 `UsernamePasswordAuthenticationToken`，并将其设置到 `SecurityContextHolder` 中。
    *   `SecurityContextHolder.getContext().setAuthentication(...)` 这一步至关重要，它告诉 Spring Security 当前请求的认证主体是谁，后续的授权逻辑（如 `@PreAuthorize`）才能正常工作。
5.  **将自定义过滤器添加到过滤器链中**: 使用 `.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)` 将你的 JWT 过滤器放在合适的认证过滤器之前。

这个流程就构成了在 Spring Security 中实现无状态 JWT 认证的完整闭环。