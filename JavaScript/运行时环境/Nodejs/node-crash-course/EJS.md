## EJS 模板引擎详细总结

### 什么是 EJS？

EJS (Embedded JavaScript templates) 是一款简单、灵活的模板引擎，主要用于在 Node.js 环境中生成 HTML 页面。它允许你在 HTML 中嵌入 JavaScript 代码，从而实现动态的数据渲染。

### EJS 的主要特点：

- **简单易用：** EJS 的语法简洁明了，学习成本低。
- **灵活强大：** 可以轻松地将数据嵌入到 HTML 中，实现动态页面生成。
- **与 Express 集成良好：** EJS 是 Express 框架默认支持的模板引擎之一，两者可以无缝集成。
- **性能优异：** EJS 的编译和渲染速度较快。
- **自定义标签：** 可以自定义模板标签，满足个性化需求。

### EJS 的基本语法：

- **<% %>：** 用于执行 JavaScript 代码。
- **<%= %>：** 用于输出变量的值。

### EJS 的常用场景：

- **动态生成 HTML 页面：** 根据不同的数据生成不同的页面内容。
- **构建 Web 应用程序：** EJS 可以与 Express 框架结合，构建复杂的 Web 应用程序。
- **创建静态站点：** EJS 可以用于生成静态 HTML 页面，例如博客、文档等。

### EJS 的使用示例：

```JavaScript
// index.js
const express = require('express');
const ejs = require('ejs');

const app = express();

app.set('view engine', 'ejs');

app.get('/', (req, res) => {
  const data    = {
    name: 'World',
    message: 'Hello'
  };
  res.render('index', data);
});

app.listen(3000, () => {
  console.log('Server listening on port 3000');
});
```


```HTML
<!DOCTYPE html>
<html>
<head>
  <title>EJS Example</title>
</head>
<body>
  <h1><%= message %></h1>
  <p>Hello, <%= name %>!</p>
</body>
</html>
```

### EJS 的优势：

- **提高开发效率：** 通过将逻辑和视图分离，提高代码的可维护性。
- **增强代码可读性：** EJS 的语法简洁，易于理解。
- **灵活定制：** 可以根据项目需求自定义模板标签和过滤器。

### 总结

EJS 是一款非常优秀的模板引擎，它简单易用、灵活强大，是构建 Node.js Web 应用程序的理想选择。如果你需要在 Node.js 项目中生成动态 HTML 页面，EJS 是一个非常不错的选择。

### 常见问题

- **EJS 和 Pug（Jade）有什么区别？**
    - EJS 和 Pug 都是常用的 Node.js 模板引擎，但它们在语法和设计理念上有一些差异。EJS 的语法更接近 JavaScript，而 Pug 的语法更简洁，更像 HTML。
- **如何自定义 EJS 的标签？**
    - EJS 允许你自定义模板标签，可以通过 `ejs.open` 和 `ejs.close` 来设置自定义的开始和结束标签。
- **EJS 支持哪些数据类型？**
    - EJS 支持所有的 JavaScript 数据类型，包括字符串、数字、数组、对象等。