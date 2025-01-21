## **Rest API 和 Restful API 的关系与区别**

### **1. Rest API 和 Restful API 的关系**

- **Rest API**（**Representational State Transfer API**）是一种基于 REST（表述性状态转移）架构风格的 API 设计方式。REST 是一种软件架构风格，而 REST API 是遵循该架构的具体实现。
- **Restful API** 是符合 REST 设计原则的 API，强调 **资源（Resource）** 的使用和 REST 约定的最佳实践。

**简单来说，Restful API 是 Rest API 的一个子集，Rest API 可能符合或不完全符合 REST 设计原则，而 Restful API 是完全符合 REST 规范的 API。**

---

### **2. Rest API 和 Restful API 的区别**

|维度|Rest API|Restful API|
|---|---|---|
|**定义**|任何基于 REST 设计风格的 API|严格遵循 REST 设计原则的 API|
|**是否符合 REST**|不一定完全符合 REST 规范|必须符合 REST 设计原则|
|**资源（Resource）**|可能使用 **动词** 表达操作，如 `/getUser`|使用 **名词** 作为资源，如 `/users/{id}`|
|**HTTP 方法**|可能不遵循标准的 HTTP 方法，如所有请求都用 `POST`|严格使用 `GET`、`POST`、`PUT`、`DELETE` 等方法|
|**URL 结构**|可能使用复杂或非标准的路径，如 `/api?action=getUser&id=1`|遵循 REST 资源风格，如 `/users/1`|
|**状态管理**|可能依赖服务器存储状态|必须是无状态（Stateless），每个请求都应包含所有必要信息|
|**返回格式**|可能返回 HTML、XML、JSON 等|主要返回 JSON 或 XML（推荐 JSON）|

---

### **3. Restful API 的核心设计原则**

1. **使用资源（Resource）而非动作（Action）**
    
    - ❌ `/getUser?id=1`
    - ✅ `/users/1`
2. **使用 HTTP 方法表述操作**
    
    - `GET /users` → 获取所有用户
    - `GET /users/{id}` → 获取某个用户
    - `POST /users` → 创建用户
    - `PUT /users/{id}` → 更新用户
    - `DELETE /users/{id}` → 删除用户
3. **无状态（Stateless）**
    
    - 服务器不会保存客户端的会话状态，每个请求都必须携带完整的信息（如 `Authorization` 令牌）。
4. **使用合适的 HTTP 状态码**
    
    - `200 OK` → 请求成功
    - `201 Created` → 资源创建成功
    - `400 Bad Request` → 请求参数错误
    - `401 Unauthorized` → 认证失败
    - `404 Not Found` → 资源不存在
    - `500 Internal Server Error` → 服务器错误

---

### **4. 总结**

- **Rest API** 是一个更广泛的概念，只要使用 REST 作为架构风格的 API 都可以称为 Rest API。
- **Restful API** 是严格遵循 REST 设计规范的 API，符合资源风格、无状态、正确使用 HTTP 方法等原则。
- Restful API 是高质量的 Rest API，但并非所有 Rest API 都是 Restful API。

如果你在开发 API，建议尽量遵循 **Restful API 规范**，以提高可维护性、可扩展性和一致性。