### **HTTP 响应格式详解**  

HTTP 响应由 **状态行、响应头、空行、响应体** 四部分组成，格式如下：  

```http
<状态行>
<响应头>
空行（CRLF）
[可选的响应体]
```

---

## **1. 状态行（Status Line）**
状态行包含 **HTTP 版本、状态码、状态描述**，格式如下：  
```
HTTP/<版本> <状态码> <状态描述>
```
示例：  
```
HTTP/1.1 200 OK
```

### **HTTP 状态码分类**
状态码由 **3 位数字** 组成，第一位表示类别：

| 状态码范围 | 类别 | 说明 |
|------------|------|------|
| **1 xx** | **信息性响应** | 请求已接收，继续处理 |
| **2 xx** | **成功响应** | 请求已成功处理 |
| **3 xx** | **重定向响应** | 需要进一步操作（如跳转） |
| **4 xx** | **客户端错误** | 请求有误（如路径错误、权限不足） |
| **5 xx** | **服务器错误** | 服务器处理请求失败 |

### **常见状态码详解**
#### **1 xx（信息性响应）**
- **100 Continue**  
  客户端应继续发送请求（用于大文件上传前的确认）。
- **101 Switching Protocols**  
  服务器同意切换协议（如从 HTTP 切换到 WebSocket）。

#### **2 xx（成功响应）**
- **200 OK**  
  请求成功，响应中包含请求的数据（如 HTML、JSON）。
- **201 Created**  
  资源已成功创建（常用于 POST 请求）。
- **204 No Content**  
  请求成功，但无返回内容（如 DELETE 请求）。

#### **3 xx（重定向）**
- **301 Moved Permanently**  
  资源已永久移动到新 URL（浏览器会缓存新地址）。
- **302 Found（临时重定向）**  
  资源临时移动，客户端应继续使用原 URL 访问。
- **304 Not Modified**  
  资源未修改，客户端可使用缓存（用于缓存优化）。

#### **4 xx（客户端错误）**
- **400 Bad Request**  
  请求语法错误（如 JSON 格式错误）。
- **401 Unauthorized**  
  未授权（需登录或 Token 无效）。
- **403 Forbidden**  
  服务器拒绝访问（权限不足）。
- **404 Not Found**  
  请求的资源不存在。
- **405 Method Not Allowed**  
  请求方法不支持（如 GET 接口用 POST 访问）。

#### **5 xx（服务器错误）**
- **500 Internal Server Error**  
  服务器内部错误（如代码异常）。
- **502 Bad Gateway**  
  网关/代理服务器收到无效响应（如后端服务崩溃）。
- **503 Service Unavailable**  
  服务器暂时不可用（如过载维护）。
- **504 Gateway Timeout**  
  网关超时（后端服务未及时响应）。

---

## **2. 响应头（Response Headers）**
服务器返回的元信息，常见响应头包括：  

| 响应头 | 说明 | 示例 |
|--------|------|------|
| `Content-Type` | 响应体的数据类型 | `text/html`, `application/json` |
| `Content-Length` | 响应体的字节数 | `Content-Length: 1024` |
| `Server` | 服务器软件信息 | `Server: nginx/1.18.0` |
| `Cache-Control` | 缓存策略 | `Cache-Control: no-cache` |
| `Set-Cookie` | 设置 Cookie | `Set-Cookie: session_id=abc123` |
| `Location` | 重定向目标（用于 3xx） | `Location: /new-page` |

示例：
```
Content-Type: application/json
Content-Length: 89
Server: Apache/2.4.1
Cache-Control: no-cache
```

---

## **3. 空行（CRLF）**
单独一行的 `\r\n`（回车换行），用于分隔 **响应头** 和 **响应体**。

---

## **4. 响应体（Response Body）**
服务器返回的实际数据，如：
- **HTML 页面**（`Content-Type: text/html`）
- **JSON 数据**（`Content-Type: application/json`）
- **文件下载**（`Content-Type: application/octet-stream`）

示例（JSON 响应）：
```json
{
  "status": "success",
  "data": {
    "id": 123,
    "name": "John Doe"
  }
}
```

---

## **完整 HTTP 响应示例**
### **成功响应（200 OK）**
```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 89
Server: nginx/1.18.0
Date: Mon, 05 May 2025 12:00:00 GMT

{
  "status": "success",
  "data": {
    "id": 123,
    "name": "John Doe"
  }
}
```

### **资源未找到（404 Not Found）**
```http
HTTP/1.1 404 Not Found
Content-Type: text/html
Content-Length: 127
Server: Apache/2.4.1

<!DOCTYPE html>
<html>
<body>
  <h1>404 Not Found</h1>
  <p>The requested resource was not found.</p>
</body>
</html>
```

### **服务器错误（500 Internal Server Error）**
```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/json
Content-Length: 56
Server: nginx/1.18.0

{
  "error": "Database connection failed"
}
```

---

## **总结**
| 组件 | 说明 |
|------|------|
| **状态行** | `HTTP/1.1 200 OK`（版本 + 状态码 + 描述） |
| **响应头** | 服务器元信息（`Content-Type`, `Server` 等） |
| **空行** | `\r\n`（分隔头部和响应体） |
| **响应体** | 返回的数据（HTML/JSON/文件等） |
