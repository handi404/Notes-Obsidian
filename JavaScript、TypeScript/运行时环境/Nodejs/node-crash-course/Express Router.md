## Express Router 深入讲解

### 什么是 Express Router？

Express Router 是 Node. js Web 框架 Express. js 中的一个重要概念，它提供了一种模块化、可组织的方式来管理应用程序的路由。简单来说，Router 就是一个请求处理函数的集合，每个函数对应一个特定的 HTTP 请求路径（URL）。

### 为什么使用 Router？

- **模块化:** 将不同的 URL 路径和处理逻辑分隔到不同的 Router 中，使得代码结构更加清晰，便于维护。
- **可复用:** 创建的 Router 可以被多个应用程序或不同的模块共享。
- **中间件:** 可以为 Router 配置中间件，实现诸如身份验证、日志记录、错误处理等功能。
- **嵌套路由:** 可以创建嵌套的 Router，实现更复杂的路由结构。

### 如何创建和使用 Router？

```JavaScript
const express = require('express');
const router = express.Router();

// 定义路由处理函数
router.get('/', (req, res) => {
  res.send('Hello from root route');
});

router.post('/users', (req, res) => {
  // 处理 POST 请求，创建用户
});

// 使用 Router
const app = express();
app.use('/api', router);

app.listen(3000, () => {
  console.log('Server listening on port 3000');
});
```

- **创建 Router:** 使用 `express.Router()` 创建一个新的 Router 实例。
- **定义路由处理函数:** 使用 `router.get()`, `router.post()`, `router.put()`, `router.delete()` 等方法定义不同的 HTTP 请求处理函数。
- **使用 Router:** 使用 `app.use()` 将 Router 挂载到 Express 应用程序上。

### Router 的嵌套

```JavaScript
const express = require('express');
const app = express();

// 创建子路由
const usersRouter = express.Router();
usersRouter.get('/', (req, res) => {
  res.send('All users');
});
usersRouter.get('/:id', (req, res) => {
  res.send(`User with ID ${req.params.id}`);
});

// 创建主路由
const productsRouter = express.Router();
productsRouter.use('/users', usersRouter);

// 使用主路由
app.use('/api', productsRouter);
```

### Router 中间件

```JavaScript
router.use((req, res, next) => {
  console.log('Time:', Date.now());
  next();
});

router.get('/', (req, res) => {
  // ...
});
```

- **应用级中间件:** 在 `app.use()` 中定义，作用于所有请求。
- **路由级中间件:** 在 Router 上定义，作用于特定的路由。
- **路由处理函数级别中间件:** 在路由处理函数内部定义，作用于单个路由处理函数。

### 总结

Express Router 是一个非常灵活和强大的工具，它可以帮助我们更好地组织和管理应用程序的路由。通过合理地使用 Router，我们可以构建出更加模块化、可维护的 Web 应用程序。

### 常见问题

- **Router 和 Middleware 的区别是什么？**
    - Router 是一个请求处理函数的集合，用于定义应用程序的路由。
    - Middleware 是一个函数，可以在请求处理之前或之后执行一些操作，例如身份验证、日志记录等。
- **如何传递参数给路由处理函数？**
    - 可以通过请求参数（req. query）、请求体（req. body）、请求路径参数（req. params）等方式传递参数。
- **如何处理错误？**
    - 可以使用错误处理中间件来捕获并处理路由处理函数中的错误。