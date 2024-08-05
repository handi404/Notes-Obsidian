## Express 中的 GET、POST 和 DELETE 请求详解

### Express 简介

Express.js 是一个基于 Node.js 平台的极简、灵活的 web 应用程序框架。它提供了一系列强大的特性，帮助开发者快速构建高效、可靠的 web 应用。其中，对 HTTP 请求方法的处理是 Express 的核心功能之一。

### GET 请求

GET 请求用于从服务器获取数据。通常用于获取资源、查询数据等。

**示例：**

```JavaScript
const express = require('express');
const app = express();

app.get('/users', (req, res) => {
  // 处理获取用户列表的逻辑
  res.json([{ id: 1, name: 'Alice' }, { id: 2, name: 'Bob' }]);
});
```

- **`app.get()`**: 用于定义一个处理 GET 请求的路由。
- **`/users`**: 请求的路径。
- **回调函数**: 当接收到该路径的 GET 请求时，会执行回调函数。
- **`res.json()`**: 发送 JSON 格式的数据作为响应。

### POST 请求

POST 请求用于向服务器提交数据。通常用于创建新的资源。

**示例：**

```JavaScript
app.post('/users', (req, res) => {
  // 处理创建用户的逻辑
  const newUser = req.body; // 获取请求体中的数据
  // ... 将newUser保存到数据库
  res.status(201).json(newUser);
});
```

- **`app.post()`**: 用于定义一个处理 POST 请求的路由。
- **`req.body`**: 获取请求体中的数据，通常用于接收表单提交的数据。
- **`res.status(201)`**: 设置响应状态码为 201 (Created)。

### DELETE 请求

DELETE 请求用于从服务器删除资源。

**示例：**

```JavaScript
app.delete('/users/:id', (req, res) => {
  const userId = req.params.id;
  // 处理删除用户的逻辑
  // ... 从数据库删除用户
  res.sendStatus(204);
});
```

- **`app.delete()`**: 用于定义一个处理 DELETE 请求的路由。
- **`req.params.id`**: 获取 URL 中的动态参数，用于标识要删除的资源。
- **`res.sendStatus(204)`**: 发送 204 No Content 状态码，表示删除成功且没有内容返回。

### 其他 HTTP 方法

除了 GET、POST 和 DELETE，Express 还支持 PUT、PATCH 等 HTTP 方法。

- **PUT**: 用于更新整个资源。
- **PATCH**: 用于更新资源的部分属性。

### 路由参数

- **动态参数**: 在路由路径中使用 `:` 来定义动态参数，例如 `/:id`。
- **查询参数**: 通过 `req.query` 获取 URL 中的查询参数，例如 `?name=Alice&age=30`。

### 中间件

Express 中间件可以用于对请求和响应进行拦截和处理，例如：

- **日志记录**: 记录请求信息。
- **身份验证**: 验证用户身份。
- **错误处理**: 处理错误。

```JavaScript
app.use((req, res, next) => {
  console.log('Time:', Date.now());
  next();
});
```

### 总结

Express 提供了灵活的方式来定义和处理各种 HTTP 请求。通过合理地使用 GET、POST、DELETE 等方法，以及路由参数和中间件，可以构建出功能强大、易于维护的 Web 应用程序。

**关键点：**

- **路由定义**: 使用 `app.get()`, `app.post()`, `app.delete()` 等方法定义路由。
- **请求处理**: 在回调函数中处理请求，并生成响应。
- **路由参数**: 使用动态参数和查询参数获取请求数据。
- **中间件**: 使用中间件对请求和响应进行处理。

**常见场景：**

- **RESTful API**: 构建 RESTful API，实现数据的增删改查。
- **单页面应用程序 (SPA)**: 提供数据接口给前端。
- **构建 Web 服务**: 提供各种 Web 服务。