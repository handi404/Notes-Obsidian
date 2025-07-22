在现代 API 开发中，一份清晰、可交互的文档是团队协作和对外开放的基石。OpenAPI (前身是 Swagger) 是目前的事实标准。

在 Spring Boot 生态中，我们当前的首选是 `springdoc-openapi` 库。它无缝集成了 Spring Boot 3.x，并且遵循“约定优于配置”的理念，上手极其简单。

我将分三步来为你讲解：

1.  **基础配置与常用注解**：让你快速上手，为 API 生成文档。
2.  **安全集成：Bearer Token (JWT)**：最常见的场景，让你的 Swagger UI 支持需要认证的接口。
3.  **安全集成：OAuth 2.0**：更复杂的认证流程，例如对接外部认证服务器。

---

### 第一步：基础配置与常用注解

#### 1. 引入依赖

首先，在你的 `pom.xml` 中加入 `springdoc-openapi` 的起步依赖。它会搞定一切。
使用与你的 Spring Boot [适配的版本](https://springdoc.org/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot)
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version> <!-- 请使用最新版本 -->
</dependency>
```

**就这么简单！** 启动你的 Spring Boot 应用，现在访问两个 URL：

*   `http://localhost:8080/v3/api-docs`：你会看到一个 JSON 文件，这是 OpenAPI 3.0 规范的机器可读定义。
*   `http://localhost:8080/swagger-ui.html`：你会看到一个漂亮、可交互的 UI 界面，它就是解析上面的 JSON 后生成的。

#### 2. 自定义全局信息

默认的文档信息很简陋。我们通常会创建一个专门的配置类来定义全局信息，比如 API 标题、版本、描述等。这里就轮到 **`@OpenAPIDefinition`** 注解登场了。

`@OpenAPIDefinition` 用于提供整个 API 的元数据。它通常放在一个 `@Configuration` 类或者主应用类上。

**代码示例：**

```java
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    // 1. API 的基本信息
    info = @Info(
        title = "用户中心 API 文档",
        version = "1.0.0",
        description = "这是一个用于管理用户和角色的微服务 API。",
        // 联系人信息
        contact = @Contact(name = "DevTeam", email = "dev@example.com", url = "https://example.com"),
        // 许可证信息
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    ),
    // 2. API 服务器的信息，可以定义多个，比如开发、测试、生产环境
    servers = {
        @Server(url = "http://localhost:8080", description = "本地开发环境"),
        @Server(url = "https://dev.example.com", description = "开发服务器")
    }
)
public class OpenApiConfig {
    // 这个类可以是空的，主要起注解容器的作用
}
```

**`@OpenAPIDefinition` 注解属性详解：**
*   `info`: `(@Info)` 定义 API 的核心元数据。
    *   `title`: **(必需)** API 的标题。
    *   `version`: **(必需)** API 的版本号。
    *   `description`: API 的详细描述，支持 Markdown。
    *   `termsOfService`: 服务条款 URL。
    *   `contact`: `(@Contact)` 联系人信息。
        *   `name`: 联系人姓名。
        *   `url`: 联系人主页 URL。
        *   `email`: 联系人邮箱。
    *   `license`: `(@License)` 许可证信息。
        *   `name`: **(必需)** 许可证名称。
        *   `url`: 许可证详情 URL。
*   `servers`: `(@Server[])` 定义 API 的服务器地址。
    *   `url`: 服务器的 URL。
    *   `description`: 对该服务器的描述。
*   `security`: `(@SecurityRequirement[])` 全局安全需求，我们将在第二步详细讲解。
*   `tags`: `(@Tag[])` 预定义 API 的标签（分组），方便管理。
*   `externalDocs`: `(@ExternalDocumentation)` 指向外部的补充文档。

#### 3. 常用注解（Controller & DTO）

现在我们来精细化地描述每一个 Controller 和接口。

*   **`@Tag`**: 为 Controller 进行分组。
*   **`@Operation`**: 描述一个具体的接口操作。
*   **`@Parameter`**: 描述一个接口的参数。
*   **`@ApiResponse` / `@ApiResponses`**: 描述接口的响应情况。
*   **`@Schema`**: 描述一个数据模型（DTO）。

**代码示例：**

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "提供用户的增删改查接口") // 1. 为整个 Controller 打上标签
public class UserController {

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取详细信息") // 2. 描述接口
    @ApiResponses({ // 3. 描述多种可能的响应
        @ApiResponse(responseCode = "200", description = "成功获取", 
                     content = @Content(mediaType = "application/json", 
                                        schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "用户未找到")
    })
    public ResponseEntity<UserDTO> getUserById(
        @Parameter(description = "用户的唯一标识ID", required = true, example = "123") // 4. 描述路径参数
        @PathVariable Long id
    ) {
        // ... 业务逻辑
        return ResponseEntity.ok(new UserDTO());
    }
}

// 在 DTO 中使用 @Schema
@Schema(description = "用户信息数据传输对象")
class UserDTO {
    @Schema(description = "用户ID", example = "1")
    private Long id;
    
    @Schema(description = "用户名", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
}
```

---

### 第二步：安全集成 - Bearer Token (JWT)

假设你的 API 使用了 Spring Security，并且通过 `Authorization: Bearer <token>` 的方式进行 JWT 认证。我们需要在 Swagger UI 上添加一个“Authorize”按钮，让用户可以输入 Token。

这需要两步：**1. 定义安全方案 (Define)，2. 应用安全方案 (Apply)**。

#### 1. 定义安全方案 (`@SecurityScheme`)

我们在之前的 `OpenApiConfig` 类中，使用 `@SecurityScheme` 注解来定义一个名为 "Bearer Authentication" 的安全方案。

**`@SecurityScheme`**：用于定义一个可重用的安全方案。

```java
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

// ... 其他注解 ...
@Configuration
@OpenAPIDefinition(...)
// 定义一个名为 "bearerAuth" 的 Bearer Token 安全方案
@SecurityScheme(
    name = "bearerAuth", // 方案名称，在 @SecurityRequirement 中引用
    type = SecuritySchemeType.HTTP, // 方案类型：HTTP
    scheme = "bearer", // HTTP 认证方案：Bearer
    bearerFormat = "JWT", // Bearer 的格式，这里是 JWT
    in = SecuritySchemeIn.HEADER, // Token 所在位置：请求头
    description = "请输入 JWT Token，格式为 Bearer {token}"
)
public class OpenApiConfig {
}
```

**`@SecurityScheme` 属性详解：**
*   `name`: **(关键)** 安全方案的唯一标识符。后面会通过这个名字来引用它。
*   `type`: `(SecuritySchemeType)` 安全方案的类型。
    *   `HTTP`: 用于 HTTP 认证，如 Basic, Bearer。
    *   `APIKEY`: 用于 API Key 认证。
    *   `OAUTH2`: 用于 OAuth 2.0 流程。
    *   `OPENIDCONNECT`: 用于 OpenID Connect Discovery。
*   `description`: 方案的描述。
*   `scheme`: (当 `type` 为 `HTTP` 时使用) 指定具体的 HTTP 方案，如 `basic`, `bearer`。
*   `bearerFormat`: (当 `scheme` 为 `bearer` 时使用) 对 Bearer Token 格式的提示，如 `JWT`。
*   `in`: `(SecuritySchemeIn)` (当 `type` 为 `APIKEY` 时使用) 指定 API Key 的位置，如 `HEADER`, `QUERY`, `COOKIE`。
*   `paramName`: (当 `type` 为 `APIKEY` 时使用) 指定 API Key 的参数名。
*   `flows`: `(@OAuthFlows)` (当 `type` 为 `OAUTH2` 时使用) 描述 OAuth 2.0 的流程，下节详述。

#### 2. 应用安全方案 (`@SecurityRequirement`)

定义好了方案，还需要告诉 OpenAPI 哪些接口需要这个方案。你可以全局应用，也可以只在特定接口上应用。

*   **全局应用**：在 `@OpenAPIDefinition` 中添加 `security` 属性。

    ```java
    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
    
    @OpenAPIDefinition(
        info = @Info(...),
        servers = @Server(...),
        // 全局应用名为 "bearerAuth" 的安全方案
        security = @SecurityRequirement(name = "bearerAuth") 
    )
    @SecurityScheme(...)
    public class OpenApiConfig {}
    ```

*   **局部应用**：在 Controller 类或方法上使用 `@SecurityRequirement`。这会覆盖全局设置。适用于部分接口公共、部分接口需要认证的场景。

    ```java
    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
    
    @RestController
    @RequestMapping("/api/admin")
    @Tag(name = "管理接口")
    @SecurityRequirement(name = "bearerAuth") // 应用于此 Controller 下的所有接口
    public class AdminController {
        
        @GetMapping("/dashboard")
        @Operation(summary = "获取管理后台数据")
        public String getDashboard() {
            return "Sensitive admin data";
        }
    }
    ```

完成以上配置后，刷新 Swagger UI 页面，你会看到右上角出现一个 **Authorize** 按钮。点击它，输入你的 JWT Token (注意，只需输入 token 本身，`Bearer ` 前缀会自动添加)，之后所有需要认证的接口请求都会自动带上 `Authorization` 头。

---

### 第三步：安全集成 - OAuth 2.0

对于 OAuth 2.0，配置稍微复杂一些，因为它涉及多个 URL 和流程（Flows）。我们以常见的 **Authorization Code Flow** 为例。

同样是使用 `@SecurityScheme`，但 `type` 和 `flows` 属性是关键。

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "OAuth2 Protected API"),
    security = @SecurityRequirement(name = "oauth2_auth_code")
)
@SecurityScheme(
    name = "oauth2_auth_code", // 方案名称
    type = SecuritySchemeType.OAUTH2,
    description = "使用 OAuth 2.0 Authorization Code 流程进行认证",
    // 核心：定义 OAuth 2.0 的流程
    flows = @OAuthFlows(
        // 定义 Authorization Code 流程
        authorizationCode = @OAuthFlow(
            // 1. 获取授权码的 URL
            authorizationUrl = "http://my-auth-server.com/oauth/authorize",
            // 2. 用授权码换取 Token 的 URL
            tokenUrl = "http://my-auth-server.com/oauth/token",
            // 3. 定义此流程可用的范围 (Scopes)
            scopes = {
                @OAuthScope(name = "read:profile", description = "读取用户资料"),
                @OAuthScope(name = "write:data", description = "修改用户数据")
            }
        )
        // 你也可以在这里定义其他流程，如 implicit, password, clientCredentials
    )
)
public class OpenApiOAuth2Config {
}
```

**`@OAuthFlows` 和 `@OAuthFlow` 属性详解：**
*   `@OAuthFlows`: 包含一个或多个 OAuth 流程。
    *   `authorizationCode`: `(@OAuthFlow)` 授权码流程。
    *   `implicit`: `(@OAuthFlow)` 隐式授权流程。
    *   `password`: `(@OAuthFlow)` 密码模式流程。
    *   `clientCredentials`: `(@OAuthFlow)` 客户端凭证流程。
*   `@OAuthFlow`: 定义单个流程的细节。
    *   `authorizationUrl`: **(授权码/隐式流程必需)** 授权服务器的授权端点 URL。
    *   `tokenUrl`: **(授权码/密码/客户端凭证流程必需)** 授权服务器的令牌端点 URL。
    *   `refreshUrl`: 可选，用于刷新令牌的 URL。
    *   `scopes`: `(@OAuthScope[])` **(必需)** 定义此流程支持的权限范围。
        *   `@OAuthScope`:
            *   `name`: 范围的名称，如 `read`。
            *   `description`: 对该范围的描述。

配置完成后，Swagger UI 的“Authorize”按钮会弹出一个更复杂的对话框，引导用户完成 OAuth 2.0 的授权流程，包括选择 Scopes、重定向到授权服务器、然后获取 Token。

### 总结

1.  **入门**：引入 `springdoc-openapi-starter-webmvc-ui` 依赖即可获得基础功能。
2.  **美化**：使用 `@OpenAPIDefinition` 配置全局信息，使用 `@Tag`, `@Operation`, `@Parameter` 等注解丰富接口细节。
3.  **JWT/Bearer 认证**：
    *   **定义**：使用 `@SecurityScheme` 定义一个 `type=HTTP`, `scheme=bearer` 的安全方案。
    *   **应用**：使用 `@SecurityRequirement(name="...")` 在全局或局部应用该方案。
4.  **OAuth 2.0 认证**：
    *   **定义**：使用 `@SecurityScheme` 定义一个 `type=OAUTH2` 的方案，并通过 `@OAuthFlows` 和 `@OAuthFlow` 详细描述你的授权流程（URL、Scopes 等）。
    *   **应用**：同样使用 `@SecurityRequirement` 应用该方案。

这套流程覆盖了从零到一，再到高级安全集成的全过程，完全符合现代 Spring Boot 项目的最佳实践。