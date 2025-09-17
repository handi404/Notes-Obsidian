
## 注解配置

按照注解的使用位置分为三大类：
1.  **全局与安全配置层** (通常在 `@Configuration` 类或主启动类上)
2.  **Controller 接口层** (在 Controller 类或方法上)
3.  **数据模型 (DTO) 层** (在 POJO, Record, DTO 类或其字段上)

---

### 第一部分：全局与安全配置层注解

这些注解定义了 API 文档的整体元数据、服务器信息和安全方案。

#### 1. `@OpenAPIDefinition`
这是定义 API 文档“封面”信息的总入口。

*   **作用**: 提供 API 的全局信息，如标题、版本、描述、联系人等。
*   **常用位置**: 主启动类或专门的 `@Configuration` 类。

**属性详解**:
*   `info` (类型: `Info`): 最核心的属性，用于定义 API 的基本信息。
    *   `title`: **(必需)** API 的标题。效果：显示在 Swagger UI 的最顶端。
    *   `version`: **(必需)** API 的版本号。效果：显示在标题下方。
    *   `description`: API 的详细描述，支持 Markdown。效果：在标题下方显示大段描述文字。
    *   `termsOfService`: 服务条款 URL。
    *   `contact` (类型: `Contact`): API 联系人信息。
        *   `name`: 联系人名称。
        *   `url`: 联系人主页 URL。
        *   `email`: 联系人邮箱。
    *   `license` (类型: `License`): API 的许可证信息。
        *   `name`: 许可证名称 (如 `Apache 2.0`)。
        *   `url`: 许可证详情 URL。
*   `servers` (类型: `Server[]`): 定义 API 的服务器地址。当你的 API 部署在多个环境（开发、测试、生产）时非常有用。
    *   `url`: 服务器的 URL (如 `https://api.example.com/v1`)。
    *   `description`: 对该服务器的描述 (如 `生产环境`)。
*   `security` (类型: `SecurityRequirement[]`): **全局**应用安全方案。在这里定义的方案会应用到所有未特殊指定的接口上。我们稍后在 `@SecurityRequirement` 中详述。
*   `tags` (类型: `Tag[]`): 预定义全局标签，可以统一管理标签的顺序和描述。
*   `externalDocs` (类型: `ExternalDocumentation`): 链接到外部的详细文档。

**代码示例**:
```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "企业级用户中心 API",
        version = "2.1.0",
        description = "提供完整的用户管理、认证授权功能。本文档遵循 OpenAPI 3.0 规范。",
        contact = @Contact(name = "技术支持", email = "dev-support@example.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "本地开发环境"),
        @Server(url = "https://api.uat.example.com", description = "UAT 测试环境")
    }
)
public class OpenApiConfig {
}
```
**效果**: 打开 Swagger UI，你会看到标题、版本、描述、联系人等信息都已设置好，并且右上角会有一个服务器下拉列表让你切换环境。

---

#### 2. `@SecurityScheme`
这是定义安全认证方案的“蓝图”。定义了之后才能被引用。

*   **作用**: 声明一种或多种 API 使用的安全机制，比如 JWT Bearer Token, API Key, OAuth 2 等。
*   **常用位置**: 通常和 `@OpenAPIDefinition` 放在同一个配置类中。

**属性详解 (以 JWT 为例)**:
*   `name`: **(必需)** 安全方案的唯一标识符。在 `@SecurityRequirement` 中通过此名称引用。
*   `type` (类型: `SecuritySchemeType`): **(必需)** 安全方案类型。常用值：
    *   `HTTP`: 用于 Basic Auth 和 Bearer tokens (JWT)。
    *   `APIKEY`: 用于 API 密钥。
    *   `OAUTH2`: 用于 OAuth 2 流程。
*   `scheme`: 当 `type` 为 `HTTP` 时使用，指定具体的方案。对于 JWT，固定为 `bearer`。
*   `bearerFormat`: 对 `bearer` token 格式的提示，通常为 `JWT`。
*   `in` (类型: `SecuritySchemeIn`): 当 `type` 为 `APIKEY` 时使用，指定 API Key 放在哪里（`HEADER`, `QUERY`, `COOKIE`）。
*   `paramName`: 当 `type` 为 `APIKEY` 时使用，指定存放 Key 的参数名（比如 `X-API-KEY`）。
*   `description`: 对该安全方案的描述。

**代码示例**:
```java
// 在 OpenApiConfig.java 中
@SecurityScheme(
    name = "bearerAuth", // 唯一的 key
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "请输入有效的 JWT Token"
)
public class OpenApiConfig { ... }
```
**效果**: 定义后，Swagger UI 右上角会出现一个 "Authorize" 按钮。点击后会弹出一个输入框，让你输入 Token。但此时接口还不会自动应用它，这只是一个定义。

---

### 第二部分：Controller 接口层注解

这些注解用于描述 Controller 中的每一个接口、参数和响应。

#### 1. `@Tag`
*   **作用**: 将接口分组。同一个 Tag 的接口会显示在 Swagger UI 的同一个可折叠区域内。
*   **常用位置**: Controller 类上（作用于所有方法）或单个方法上。

**属性详解**:
*   `name`: **(必需)** 标签名称，比如 "用户管理"。
*   `description`: 对该标签组的详细描述，支持 Markdown。

**代码示例**:
```java
@RestController
@Tag(name = "用户管理", description = "包含用户的增、删、改、查等核心操作")
public class UserController { ... }
```
**效果**: Swagger UI 页面上会出现一个名为 "用户管理" 的可折叠面板，所有 `UserController` 的接口都在这个面板下。

---

#### 2. `@Operation`
*   **作用**: 这是最核心的注解，用于详细描述一个具体的接口（操作）。
*   **常用位置**: Controller 的方法上。

**属性详解**:
*   `summary`: **(强烈推荐)** 对接口的简短总结。效果：在 Swagger UI 的折叠面板中，作为接口的标题显示。
*   `description`: 对接口的详细描述，支持 Markdown。效果：展开接口详情后可见。
*   `operationId`: 操作的唯一标识符。如果不指定，会自动生成。对于代码生成客户端 SDK 的场景非常重要，建议手动指定，保持唯一且有意义（如 `getUserById`）。
*   `tags`: 字符串数组，用于将此操作关联到一个或多个 `@Tag` 的 `name`。如果 Controller 类上已有 `@Tag`，通常无需在此处重复。
*   `parameters` (类型: `Parameter[]`): 显式地定义参数，当 JAX-RS/Spring 注解不足以描述时使用。通常我们更倾向于直接在方法参数上使用 `@Parameter`。
*   `requestBody` (类型: `RequestBody`): 描述请求体。通常 `springdoc` 会自动根据 `@RequestBody` 注解推断，但你可以用此注解提供更丰富的描述。
*   `responses` (类型: `ApiResponse[]`): **(极其重要)** 定义所有可能的响应。这是接口文档完整性的关键。
*   `security` (类型: `SecurityRequirement[]`): **局部应用安全方案**。

**代码示例**:
```java
@Operation(
    summary = "获取指定ID的用户信息",
    description = "通过路径参数传入用户ID，返回该用户的详细数据。如果用户不存在，则返回404。",
    operationId = "getUserById",
    responses = {
        @ApiResponse(responseCode = "200", description = "成功获取用户信息"),
        @ApiResponse(responseCode = "404", description = "用户未找到"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    }
)
@GetMapping("/{id}")
public User getUserById(@PathVariable Long id) { ... }
```
**效果**:
*   接口标题显示为 "获取指定 ID 的用户信息"。
*   展开后能看到详细描述。
*   "Responses" 部分会清晰地列出 200, 404, 500 三种情况及其描述。

---

#### 3. `@Parameter` / `@Parameters`
*   **作用**: 详细描述一个请求参数（路径、查询、请求头、Cookie）。
*   **常用位置**: Controller 方法的参数上。`@Parameters` 是 `@Parameter` 的容器。

**属性详解**:
*   `name`: 参数名。`springdoc` 通常能自动推断，但你可以覆盖它。
*   `in` (类型: `ParameterIn`): 参数位置。`springdoc` 会根据 `@PathVariable`, `@RequestParam`, `@RequestHeader` 等自动推断，一般无需手动设置。
*   `description`: 参数的详细描述。
*   `required`: 是否必需。`springdoc` 会根据 `@RequestParam(required=true)` 或 `@PathVariable` 自动设为 `true`。
*   `example`: 参数的示例值。**非常有用！**
*   `schema` (类型: `Schema`): 引用或定义参数的数据结构。对于简单类型，可以定义格式、模式等。

**代码示例**:
```java
@GetMapping("/search")
public List<User> searchUsers(
    @Parameter(description = "搜索关键字，如姓名或邮箱", example = "john")
    @RequestParam String keyword,

    @Parameter(description = "分页页码，从1开始", example = "1")
    @RequestParam(defaultValue = "1") int page
) { ... }
```
**效果**: 在 Swagger UI 中，`keyword` 和 `page` 参数旁边会显示它们的描述和示例值，极大地方便了调用者。

---

#### 4. `@ApiResponse` / `@ApiResponses`
*   **作用**: 定义一个或多个可能的 HTTP 响应。
*   **常用位置**: Controller 方法上，通常在 `@Operation` 的 `responses` 属性内部。`@ApiResponses` 是 `@ApiResponse` 的容器。

**属性详解**:
*   `responseCode`: **(必需)** HTTP 状态码，如 "200", "404"。
*   `description`: 对该响应的描述，如 "操作成功"。
*   `content` (类型: `Content[]`): 描述响应体的内容。
    *   `mediaType`: 媒体类型，如 `application/json`。
    *   `schema` (类型: `Schema`): **(核心)** 描述响应体的数据模型。通过 `implementation` 属性指向 DTO 类。
    *   `examples` (类型: `ExampleObject[]`): 提供具体的响应体示例 JSON。

**代码示例**:
```java
@Operation(summary = "创建新用户", responses = {
    @ApiResponse(responseCode = "201", description = "用户创建成功",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "400", description = "请求参数无效",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
})
@PostMapping
public User createUser(@RequestBody CreateUserRequest request) { ... }
```
**效果**: 在 "Responses" 区域，201 状态码旁边会展示 `User` 模型的结构和示例，400 旁边会展示 `ErrorResponse` 的结构。

---

#### 5. `@SecurityRequirement`
*   **作用**: 将 `@SecurityScheme` 定义的安全方案应用到具体的操作或整个 Controller。
*   **常用位置**: Controller 类或方法上。

**属性详解**:
*   `name`: **(必需)** 要引用的 `@SecurityScheme` 的 `name`。
*   `scopes`: (主要用于 OAuth 2) 指定所需的作用域。

**代码示例**:
```java
@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth") // 应用于此 Controller 下的所有接口
public class AdminController {

    @Operation(summary = "一个不需要认证的公开端点", security = {}) // 局部覆盖，取消安全认证
    @GetMapping("/public")
    public String publicEndpoint() { return "OK"; }

    @GetMapping("/data")
    public String secureData() { return "Secret Data"; }
}
```
**效果**:
*   `AdminController` 下的所有接口（除了 `/public`）在 Swagger UI 上都会出现一个“锁”图标，表示需要认证。
*   当你通过 "Authorize" 按钮设置了 Token 后，从 UI 调用这些带锁接口时，会自动在请求头中加入 `Authorization: Bearer <token>`。
*   `/public` 接口因为 `security = {}` 的设置，将不受全局安全配置影响。

---

### 第三部分：数据模型 (DTO) 层注解

#### 1. `@Schema`
*   **作用**: 描述一个数据模型（DTO）或其字段。
*   **常用位置**: DTO 类上，或其字段/getter 方法上。

**属性详解**:
*   **用在类上**:
    *   `description`: 对整个数据模型的描述。
    *   `name`: 模型在 OpenAPI 规范中的名称，默认是类名。
*   **用在字段上**:
    *   `description`: 对字段的描述。
    *   `example`: 字段的示例值。**非常有用！**
    *   `requiredMode` (类型: `RequiredMode`): 指示字段是否必需。
        *   `REQUIRED`: 必需。
        *   `NOT_REQUIRED`: 非必需。
        *   `AUTO` (默认): `springdoc` 会根据 `@NotNull`, `@NotBlank` 或 Java 的 `Optional` 类型自动推断。
    *   `defaultValue`: 字段的默认值。
    *   `format`: 字段的格式，如 `date-time`, `email`, `uuid`。
    *   `accessMode` (类型: `AccessMode`):
        *   `READ_ONLY`: 字段只在响应中出现。
        *   `WRITE_ONLY`: 字段只在请求中出现（如密码）。
        *   `READ_WRITE` (默认)。
    *   `minLength`, `maxLength`, `minimum`, `maximum`, `pattern` (正则表达式): 用于描述字段的约束。

#### 与 Jakarta Bean Validation 的配合使用 (黄金搭档)

`springdoc-openapi` 的一大亮点是它能**自动识别并转换 Bean Validation 注解**。

**原则**:
*   **使用 Validation 注解来定义约束**: 如 `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Pattern`, `@Email`。`springdoc` 会自动将它们转换为 OpenAPI 规范中的 `required`, `minLength`, `maxLength`, `minimum`, `maximum`, `pattern`, `format` 等属性。
*   **使用 `@Schema` 注解来提供纯文档信息**: 如 `description`, `example`, `accessMode`。

**完美结合的示例**:
```java
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "用户创建请求的数据模型")
public record CreateUserRequest(
    @Schema(description = "用户名，必须是唯一的", example = "john.doe")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20)
    String username,

    @Schema(description = "用户密码", example = "s3cr3tP@ssw0rd", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank
    @Size(min = 8, message = "密码长度至少为8位")
    String password,

    @Schema(description = "用户邮箱地址", example = "john.doe@example.com", format = "email")
    @NotBlank
    @Email(message = "邮箱格式不正确")
    String email,

    @Schema(description = "用户年龄", example = "25")
    @Min(value = 18, message = "必须年满18岁")
    @Max(value = 100)
    Integer age
) {}
```
**效果**:
*   在 Swagger UI 中，`username` 字段会被标记为 `required`，并且显示 `minLength: 3`, `maxLength: 20`。
*   `password` 字段只会在请求示例中出现，不会在响应示例中出现（因为 `WRITE_ONLY`）。
*   `email` 字段会被识别为 `string (email)` 格式。
*   `age` 字段会显示 `minimum: 18`, `maximum: 100`。
*   所有字段都会带有清晰的中文描述和示例值。