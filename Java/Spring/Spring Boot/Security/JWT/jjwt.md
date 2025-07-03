`jjwt` 库。这绝对是 Java 生态中处理 JWT 的事实标准。

我将按照以下路径为你讲解，确保覆盖从基础到高级应用的每一个环节：

1.  **环境准备**：引入依赖。
2.  **核心基石：密钥（Key）**：一切的开始。
3.  **生成令牌（Creating JWTs）**：构建你的数字身份证。
4.  **解析与验证令牌（Parsing & Verifying）**：检查身份证的真伪和有效性。
5.  **提取信息（Extracting Claims）**：读取身份证上的内容。
6.  **异常处理（Exception Handling）**：专业地处理各种伪造、过期情况。
7.  **高级主题与最佳实践**：密钥管理、非对称加密、刷新令牌。
8.  **实战整合：构建 `JwtUtil` for Spring Security**：将所有知识融合成一个可复用的工具类。

---

### 1. 环境准备 (Setup)

首先，在你的 `pom.xml` (Maven) 或 `build.gradle` (Gradle) 中添加 `jjwt` 的依赖。请注意，`jjwt` 从 0.10.0 版本开始将库拆分成了 API、实现和 JSON 解析器三个部分。

**Maven 示例:**

```xml
<dependencies>
    <!-- JJWT API -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.5</version> <!-- 请使用最新版本 -->
    </dependency>
    <!-- JJWT Implementation -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.5</version>
        <scope>runtime</scope>
    </dependency>
    <!-- JJWT Jackson JSON Processor -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.5</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 2. 核心基石：密钥 (The Key)

JWT 的安全完全依赖于密钥。`jjwt` 提供了非常现代和安全的方式来生成密钥。

#### 对称密钥 (Symmetric Keys) - HS 256, HS 384, HS 512

双方共享同一个密钥，用于签名和验证。这在单个应用或后端微服务之间（密钥可以安全共享）很常见。

**正确生成方式：**

```java
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

// 使用 Keys 工厂类为指定的 HMAC-SHA 算法生成一个安全的密钥。
// 这比自己创建 byte[] 然后 new SecretKeySpec(bytes, "HmacSHA256") 更安全、更推荐。
SecretKey key = Jwts.SIG.HS256.key().build();
```

#### 非对称密钥 (Asymmetric Keys) - RS 256, ES 256 等

使用公钥/私钥对。私钥用于签名，公钥用于验证。这非常适合“认证服务器”和“资源服务器”分离的场景。认证服务用私钥签发令牌，所有资源服务都可以用公钥验证令牌，而无需知道私钥。

**正确生成方式：**

```java
import io.jsonwebtoken.Jwts;
import java.security.KeyPair;

// 为指定的算法生成一个密钥对
KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
// PrivateKey privateKey = keyPair.getPrivate();
// PublicKey publicKey = keyPair.getPublic();
```

### 3. 生成令牌 (Creating JWTs)

我们使用 `Jwts.builder()` 这个流畅的 API 来构建令牌。

```java
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;

public class JwtGenerator {

    public String generateToken(String username, List<String> roles, SecretKey key) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 设置过期时间为1小时后
        long expMillis = nowMillis + 3600_000;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                // 1. Header: 算法会自动根据 key 的类型设置 (e.g., "alg":"HS256")
                // 2. Payload
                .subject(username) // "sub" (Subject): 通常是用户的唯一标识
                .claim("roles", roles) // 添加自定义的私有声明 (Private Claim)
                .issuedAt(now) // "iat" (Issued At): 签发时间
                .expiration(exp) // "exp" (Expiration Time): 过期时间
                // 3. Signature
                .signWith(key) // 使用密钥进行签名
                .compact(); // 构建并序列化为紧凑的 URL 安全字符串
    }
}
```

**代码解读：**
*   `.subject()`: 设置标准声明 `sub`。
*   `.claim("key", value)`: 添加自定义声明。你可以链式调用多次来添加多个声明。
*   `.expiration()`: **极其重要！** 务必设置过期时间，这是无状态认证安全的关键一环。
*   `.signWith(key)`: 使用第2步中生成的 `SecretKey` 或 `PrivateKey` 进行签名。`jjwt` 会自动推断出正确的 `alg` 算法。
*   `.compact()`: 完成构建，返回 `String` 类型的 JWT。

### 4. 解析与验证令牌 (Parsing & Verifying)

这是接收方（API 服务器）需要做的事情。使用 `Jwts.parser()` API。

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

public class JwtParser {

    public Jws<Claims> parseAndVerify(String jwtString, SecretKey key) {
        return Jwts.parser() // 获取解析器实例
                .verifyWith(key) // **必须**设置用于验证签名的密钥
                .build()
                .parseSignedClaims(jwtString); // 解析并验证令牌
    }
}
```

**代码解读：**
*   `Jwts.parser()`: 获取一个解析器构建器。
*   `.verifyWith(key)`: **这是最关键的一步**。你必须提供生成令牌时所用的 `SecretKey` (对称加密) 或 `PublicKey` (非对称加密) 来让 `jjwt` 验证签名。
*   `.build().parseSignedClaims(jwtString)`: 解析传入的 JWT 字符串。如果**签名无效、格式错误或令牌结构不被支持**，这一步会直接抛出异常。
*   **返回值 `Jws<Claims>`**:
    *   这是一个泛型类型，代表一个已验证签名的 JWT。
    *   `Jws` 表示 a "Signed JWT"。
    *   `Claims` 表示 Payload 部分。

### 5. 提取信息 (Extracting Claims)

一旦令牌验证通过，你就可以安全地从中提取信息。

```java
public void processToken(String jwtString, SecretKey key) {
    try {
        Jws<Claims> jws = parseAndVerify(jwtString, key);
        
        // 从解析结果中获取 Payload
        Claims claims = jws.getPayload();

        // 提取标准声明
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        
        // 提取自定义声明
        // 注意：需要指定期望的类型，以获得类型安全
        List<String> roles = claims.get("roles", List.class);
        
        System.out.println("Username: " + username);
        System.out.println("Roles: " + roles);
        System.out.println("Expires at: " + expiration);

    } catch (Exception e) {
        // 处理验证失败的情况
        System.err.println("Token validation failed: " + e.getMessage());
    }
}
```

### 6. 异常处理 (Exception Handling)

专业的代码必须能精确地处理各种错误情况，并给前端返回有意义的错误信息。`jjwt` 会在验证失败时抛出特定的 `JwtException` 子类。

```java
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public void robustParse(String jwtString, SecretKey key) {
    try {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtString);
        System.out.println("Token is valid!");

    } catch (ExpiredJwtException e) {
        // HTTP 401 Unauthorized
        System.err.println("Token has expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
        // HTTP 400 Bad Request
        System.err.println("Token is not in a supported format: " + e.getMessage());
    } catch (MalformedJwtException e) {
        // HTTP 400 Bad Request
        System.err.println("Token is malformed: " + e.getMessage());
    } catch (SignatureException e) {
        // HTTP 401 Unauthorized
        System.err.println("Signature validation failed: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        // HTTP 400 Bad Request
        System.err.println("Token string is empty or null: " + e.getMessage());
    }
}
```
在你的 Spring Boot 应用中，你可以创建一个 `@RestControllerAdvice` 来全局捕获这些异常，并统一返回标准的 JSON 错误响应。

### 7. 高级主题与最佳实践

*   **密钥管理**:
    *   **绝对不要**在代码中硬编码密钥！
    *   至少要将其存放在 `application.properties` 或 `application.yml` 中。**注意：** 密钥通常是二进制数据，直接放在配置文件中可能会有字符编码问题，**最佳实践是将其 Base 64 编码后再存入配置文件**。
    *   生产环境中，应使用更安全的方案，如 HashiCorp Vault、AWS Secrets Manager 或环境变量。

*   **非对称加密应用**:
    *   认证服务持有 `RS256.keyPair().build()` 生成的 `KeyPair`。
    *   签发 JWT 时使用 `signWith(keyPair.getPrivate())`。
    *   将 `keyPair.getPublic()` (公钥) 安全地分发给所有需要验证 JWT 的资源服务。资源服务使用 `verifyWith(publicKey)` 进行验证。

*   **刷新令牌 (Refresh Token)**:
    *   **问题**: Access Token (JWT) 的有效期通常很短（如 15 分钟-1 小时），过期后用户需要重新登录，体验不佳。
    *   **解决方案**: 引入 Refresh Token。
        1.  用户登录时，同时签发一个短有效期的 Access Token 和一个长有效期（如 7 天）的 Refresh Token。
        2.  Access Token 用于访问受保护资源。Refresh Token **仅用于**获取新的 Access Token。
        3.  当 Access Token 过期时，客户端携带 Refresh Token 请求一个特定的“/refresh”端点。
        4.  服务器验证 Refresh Token 的有效性（通常会将其存储在数据库中并与请求的进行比对，以实现吊销），然后签发一个新的 Access Token。
        5.  这样既保证了安全性（频繁访问的 Token 很快过期），又提升了用户体验。

### 8. 实战整合：构建 `JwtUtil` for Spring Security

现在，我们将所有知识点整合到一个可以在 Spring Security 中直接使用的工具类里。

**application.yml:**
```yaml
app:
  jwt:
    # 使用 ./gradlew generateSecretKey 这样的任务来生成一个安全的Base64编码的密钥
    # 不要使用 "my-super-secret-key-that-is-not-secret" 这样的弱密钥
    secret: "3Z5NpxwL5gAY4GDAc8aL1+1e8cQoBwFmI0o3Z4b8H9E=" # 这是一个示例 HS256 密钥的 Base64 编码
    expiration-ms: 3600000 # 1 hour
```

**JwtUtil.java:**
```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secretString;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key;

    // 在构造函数或 @PostConstruct 中从 Base64 字符串解码密钥
    @jakarta.annotation.PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretString);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 从令牌中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从令牌中提取单个 Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // 生成令牌
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 你可以在这里添加更多自定义 claims，例如用户的角色
        // claims.put("roles", userDetails.getAuthorities().stream()...);
        return createToken(claims, userDetails.getUsername());
    }

    // 验证令牌是否有效
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        // 注意：这里的 parse 方法会隐式验证签名和过期时间
        // 如果验证失败，会抛出相应的异常
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

这个 `JwtUtil` 类封装了所有 `jjwt` 的复杂性，为你的 Spring Security 过滤器提供了干净、可测试的接口，完美地将理论知识应用到了实践中。


---

## 个人代码
```java
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;


    // 仅使用用户详细信息生成令牌
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    
    // 使用额外声明和用户详细信息生成令牌
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /*
    * 构建令牌
    * */
    public String buildToken(
            Map<String, Object> extraClaims, // 额外声明
            UserDetails userDetails,
            long expiration // 到期日期
    ){
        // 用户权限列表
        var authorities = userDetails.getAuthorities()
                .stream()
                // getAuthority: 返回授予权限的表示（如果授予的权限无法以足够精确的字符串形式表示，则返回空值）。
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // subject(主题)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 发布日期，这些信息将帮助我们计算到期日期或检查令牌是否仍然有效
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 到期日期
                .claim("authorities", authorities) // 追加声明：(name, value)
                .signWith(getSignInKey()) // 用于签署此令牌的密钥进行签名
                .compact(); // 生成并返回令牌
    }
    
    /*
    * 验证令牌是否有效（令牌是否属于用户 && 令牌是否过期）
    * */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // token是否过期，过期返回true
    public boolean isTokenExpired(String token) {
        /* public boolean before(Date when)
         * true：如果当前 Date 对象表示的时间严格早于参数 when 表示的时间。
         * false：如果当前 Date 对象表示的时间等于或晚于参数 when 表示的时间。
         * */
        // 若过期时间比当前时间晚，说明还未过期，返回false
        return extractExpiration(token).before(new Date());
    }

    // 获取token到期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 提取令牌主题
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Function<T, R> 是一个函数式接口。简单来说，它代表了一个“接收一个参数并产生一个结果”的函数。
     * T：代表输入参数的类型 (Type of input)。
     * R：代表返回结果的类型 (Type of result)。
     * */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        // apply：触发实例化的Function的方法
        return claimsResolver.apply(claims);
    }
    
    /*
    * 获取令牌中拥有的所有声明
    * Claims 来自 io.jsonwebtoken，添加的jjwt依赖
    * */
    public Claims extractAllClaims(String token) {
        // Jwts 来自 io.jsonwebtoken
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // 签名密钥 - 创建、生成或解码令牌时，需要使用签名密钥
                .build() // 它是一个构建器，一旦对象被构建，就可以调用方法 如 parseClaimsJwt()
                .parseClaimsJws(token) // 解析令牌，一旦令牌被解析， 就可以调用方法获取主体
                .getBody();
    }

    private Key getSignInKey() {
        // 解码密钥
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // HMAC（Hash Message Authentication Code(哈希消息验证码)）
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```


`setClaims` 和 `claim` 的区别。
**这两个方法设置的内容最终都会被放置在 JWT 的第二部分：Payload (有效载荷) 中**。
它们的主要区别在于**操作方式和使用场景**。

---

### 两者核心区别

可以把 JWT 的 Payload 想象成一个正在构建中的 JSON 对象。

1. **`setClaims(Map<String, Object> claims)`：批量设置/替换**
    
    - **作用**：此方法接收一个 `Map` 对象，并将该 `Map` 中的所有键值对设置为 Payload 的内容。
        
    - **关键行为**：这是一个**覆盖性**操作。如果你在调用 `setClaims` 之前已经设置了任何声明（比如通过 `setSubject`），`setClaims` 会**清空**之前所有的声明，然后用传入的 `Map` 内容来填充。
        
2. **`claim(String name, Object value)`：单个添加/设置**
    
    - **作用**：此方法用于添加或设置一个**单一的**自定义声明。
        
    - **关键行为**：这是一个**添加性**或**更新性**操作。它不会影响任何已经存在的其他声明。如果名为 `name` 的声明已存在，它的值会被更新；如果不存在，则会被添加进去。
        

---

### 举例说明

让我们用一个简单的比喻来理解：假设你在点一份自定义披萨（Payload）。

- `setClaims(pre-set-pizza)` 就好比你对店员说：“别管我之前说的，就完全按照这张‘豪华套餐’的配料单来做。” 这张配料单会替换掉你之前所有的选择。
    
- `claim("topping", "cheese")` 就好比你对店员说：“在我现在的披萨上，加一份芝士。” 这只是在现有基础上增加一样东西。
    

---

### 在您的代码中的含义和最佳实践

现在我们来分析您提供的代码片段，这是一个非常经典且推荐的写法：

Java

```java
Jwts
    .builder()
    // 1. 首先用一个 Map 设置一批非标准的、基础的声明
    .setClaims(extraClaims) 
    
    // 2. 接着设置标准的、必须的声明 (会覆盖 extraClaims 中可能存在的同名项)
    .setSubject(userDetails.getUsername()) 
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis() + expiration))
    
    // 3. 最后，添加一个特定的、额外的声明
    .claim("authorities", authorities) 
    
    .signWith(getSignInKey())
    .compact();
```

**代码执行顺序和逻辑分析：**

1. **`.setClaims(extraClaims)`**:
    
    - 程序首先将 `extraClaims` 这个 `Map` 里的所有内容设置为 Payload 的基础。这通常用来传递一些非标准但又通用的信息。
        
2. **`.setSubject(...)`, `.setIssuedAt(...)`, `.setExpiration(...)`**:
    
    - 接下来，代码调用了一系列 `set` 开头的标准声明方法。这些方法实际上是 `claim(String, Object)` 的便捷封装。例如, `.setSubject(username)` 本质上等同于 `.claim("sub", username)`。
        
    - **重点**：这里的写法非常健壮。即使 `extraClaims` 中不小心包含了 `"sub"` 或 `"exp"` 这样的键，后续的 `.setSubject()` 和 `.setExpiration()` 调用也会**确保**这些标准声明被设置为正确、预期的值，从而覆盖掉 `extraClaims` 中可能存在的同名项。
        
3. **`.claim("authorities", authorities)`**:
    
    - 最后，代码添加了一个名为 `"authorities"` 的自定义声明。这通常用来存储用户的角色或权限列表。这是一个非常清晰的用法，因为它明确地为 JWT 增加了一项业务相关的特定数据。
        

### 总结

|方法|操作方式|行为|适用场景|
|---|---|---|---|
|**`setClaims(map)`**|批量设置|**覆盖性**。清空已有声明，用 `map` 的内容填充。|在构建开始时，从一个已有的 `Map` 初始化所有声明。|
|**`claim(key, value)`**|单个添加|**添加性/更新性**。不影响其他声明，只添加或更新指定的 `key`。|添加单个自定义声明（如角色、权限），或在 `setClaims` 之后微调 Payload。|
|**`setSubject(sub)`**|便捷设置|本质是 `claim("sub", sub)`|用于设置 JWT [注册声明](https://www.google.com/search?q=https://datatracker.ietf.org/doc/html/rfc7519%23section-4.1)（如 `sub`, `iss`, `exp`, `iat` 等），让代码更具可读性。|

总而言之，您代码中的写法结合了两者的优点：先用 `setClaims` 做批量初始化，再用标准的 `set` 方法确保核心声明的正确性，最后用 `claim` 添加特定的业务数据。这是一种清晰、安全且可维护的 JWT 构建方式。