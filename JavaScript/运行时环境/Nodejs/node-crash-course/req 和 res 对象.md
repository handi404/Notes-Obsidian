## req 和 res 对象的详细解释

在 Node.js 的 Express 框架中，`req` 和 `res` 对象是处理 HTTP 请求和响应的核心。它们分别代表了客户端的请求和服务器的响应。

### req 对象 (Request 对象)

- **表示客户端的请求**：它包含了客户端向服务器发送的所有信息，例如：
    - **URL:** 客户端请求的网址。
    - **HTTP 方法:** 请求的方法，如 GET、POST、PUT、DELETE 等。
    - **Headers:** 请求头，包含一些额外的信息，如 User-Agent、Cookie 等。
    - **Query parameters:** URL 中的查询参数，例如 `http://example.com?name=John&age=30` 中的 `name` 和 `age`。
    - **Body:** 请求体，通常包含 POST 请求提交的数据。
- **常用属性和方法:**
    - `req.url`: 获取请求的 URL。
    - `req.method`: 获取请求的方法。
    - `req.headers`: 获取请求头。
    - `req.query`: 获取查询参数。
    - `req.body`: 获取请求体（通常需要使用中间件如 body-parser）。

### res 对象 (Response 对象)

- **表示服务器的响应**：它包含了服务器返回给客户端的所有信息，例如：
    - **状态码:** HTTP 状态码，如 200 (成功)、404 (未找到)、500 (服务器错误) 等。
    - **Headers:** 响应头，包含一些额外的信息，如 Content-Type、Set-Cookie 等。
    - **Body:** 响应体，即发送给客户端的数据。
- **常用方法:**
    - `res.send()`: 发送字符串作为响应。
    - `res.json()`: 发送 JSON 格式的数据。
    - `res.sendFile()`: 发送文件作为响应。
    - `res.status()`: 设置响应状态码。
    - `res.end()`: 结束响应。

### 示例

```JavaScript
app.get('/users', (req, res) => {
  const name = req.query.name; // 获取查询参数中的 name
  res.send(`Hello, ${name}!`); // 发送响应
});
```

在这个例子中：

- `req.query.name` 获取了 URL 中 `name` 参数的值。
- `res.send()` 方法发送了一条包含问候语的字符串作为响应。

**总结**

- `req` 对象承载了客户端向服务器发送的所有信息，是服务器了解客户端请求的窗口。
- `res` 对象则负责将服务器处理后的结果返回给客户端。
- 通过灵活运用 `req` 和 `res` 对象，我们可以构建各种各样的 Web 应用。