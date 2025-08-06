## **REST API 学习步骤和关键点**

学习 REST API 主要涉及 **理论理解** 和 **实际开发** 两个方面。可以分成以下 **5 个阶段** 进行学习：

---

## **📌 阶段 1：理解 REST API 基础**

✅ **学习目标**：掌握 REST 架构风格、核心概念和 HTTP 相关知识。

### **🔹 1.1 什么是 REST？**

- **REST（Representational State Transfer）** 是一种 **软件架构风格**，它基于 **资源（Resource）** 进行 API 设计。
- 主要特点：
    - **无状态性（Stateless）**：服务器不存储客户端状态，每个请求都要包含完整的信息。
    - **统一接口（Uniform Interface）**：使用标准 HTTP 方法（`GET`、`POST`、`PUT`、`DELETE`）。
    - **基于资源（Resource-Oriented）**：URL 代表资源，而不是操作。
    - **客户端-服务器架构（Client-Server）**：前后端分离，服务器只负责提供数据。
    - **可缓存（Cacheable）**：支持 HTTP 缓存机制，提高性能。

### **🔹 1.2 HTTP 协议基础**

熟悉以下 **HTTP 方法**：

| HTTP 方法  | 作用         |
| -------- | ---------- |
| `GET`    | 获取资源       |
| `POST`   | 创建资源       |
| `PUT`    | 更新资源（整体替换） |
| `PATCH`  | 更新资源（部分更新） |
| `DELETE` | 删除资源       |

🔹 **状态码（HTTP Status Code）**

| 状态码                         | 含义     |
| --------------------------- | ------ |
| `200 OK`                    | 请求成功   |
| `201 Created`               | 资源创建成功 |
| `400 Bad Request`           | 请求参数错误 |
| `401 Unauthorized`          | 认证失败   |
| `403 Forbidden`             | 无权限访问  |
| `404 Not Found`             | 资源不存在  |
| `500 Internal Server Error` | 服务器错误  |

---

## **📌 阶段 2：REST API 设计原则**

✅ **学习目标**：掌握 RESTful API 的最佳实践，设计规范化的 API。

### **🔹 2.1 RESTful URL 设计**

| ❌ **错误设计**                | ✅ **正确设计**            |
| ------------------------- | --------------------- |
| `/getUser?id=1`           | `/users/1`            |
| `/updateOrderStatus?id=5` | `/orders/5/status`    |
| `/deleteProduct?id=10`    | `DELETE /products/10` |

**设计规则：**

- **使用名词，不使用动词**（URL 代表资源，而不是行为）。
- **层级结构清晰**（如 `/users/1/orders`）。
- **避免暴露实现细节**（如 `.php`、`.jsp`）。
- **使用复数形式**（`/users` 而不是 `/user`）。
- **使用 HTTP 方法表达操作，而不是 URL 参数**。

---

## **📌 阶段 3：实践开发 REST API**

✅ **学习目标**：使用后端框架开发 REST API，并测试 API。

### **🔹 3.1 选择技术栈**

- Java：Spring Boot（推荐）
- Kotlin：Spring Boot + Ktor
- JavaScript / TypeScript：Node.js（Express / NestJS）
- Python：Django REST Framework / Flask
- Go：Gin / Fiber

### **🔹 3.2 使用 Spring Boot 创建 REST API（示例）**

创建一个简单的 `User` API：

#### **1️⃣ 定义实体类**

```java
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
```

#### **2️⃣ 创建 Controller**

```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

### **🔹 3.3 测试 API**

使用 **Postman** 或 **cURL** 进行测试：

```sh
# 获取用户信息
curl -X GET http://localhost:8080/users/1
```

---

## **📌 阶段 4：进阶优化（安全 & 认证 & 文档）**

✅ **学习目标**：提升 API 质量，包括安全性、认证和文档。

### **🔹 4.1 添加身份验证（JWT 认证）**

- 使用 **JWT（JSON Web Token）** 实现用户身份验证。
- Spring Security / Auth0 JWT / OAuth2 认证。

### **🔹 4.2 添加 API 速率限制**

- **防止滥用 API**，可以使用 **Rate Limiting** 机制（如 Spring Boot 配合 `Bucket4j`）。

### **🔹 4.3 记录 API 日志**

- 记录每次 API 请求，方便排查问题（如 `Spring AOP` 记录日志）。

### **🔹 4.4 生成 API 文档**

- 使用 **Swagger（SpringDoc OpenAPI）** 自动生成 API 文档：

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

然后访问 `http://localhost:8080/swagger-ui.html` 查看 API 文档。

---

## **📌 阶段 5：学习 REST API 相关进阶技术**

✅ **学习目标**：掌握不同 API 技术，适应不同场景需求。

|进阶技术|适用场景|
|---|---|
|**GraphQL**|需要前端灵活获取数据|
|**gRPC**|高性能微服务、二进制传输|
|**WebSocket**|实时通信（如聊天、直播）|
|**API 网关**|处理多个微服务的 API 统一管理（如 `Spring Cloud Gateway`）|

---

## **📌 总结**

**✔ 推荐学习路线**

1. **掌握 REST API 基础**（REST 架构 + HTTP 方法 + 状态码）。
2. **学习 RESTful 设计规范**（URL 设计、最佳实践）。
3. **实战开发 REST API**（使用 Spring Boot / Node.js / Python）。
4. **优化 API**（认证、安全、速率限制、API 文档）。
5. **学习 GraphQL & gRPC**，拓展 API 技术栈。