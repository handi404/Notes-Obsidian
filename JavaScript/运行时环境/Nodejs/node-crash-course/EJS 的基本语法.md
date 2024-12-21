## EJS 的基本语法

- [[#1. 输出变量|1. 输出变量]]
- [[#2. 执行 JavaScript 代码|2. 执行 JavaScript 代码]]
- [[#3. 输出未转义的 HTML|3. 输出未转义的 HTML]]
- [[#4. 注释|4. 注释]]
- [[#5. 包含其他模板|5. 包含其他模板]]
- [[#6. 控制语句|6. 控制语句]]
- [[#7. 自定义标签|7. 自定义标签]]
- [[#示例|示例]]
- [[#总结|总结]]
- [[#更多高级用法|更多高级用法]]

EJS (Embedded JavaScript templates) 是一款简单灵活的模板引擎，允许你在 HTML 中嵌入 JavaScript 代码，从而实现动态的数据渲染。下面我们来详细介绍 EJS 的基本语法：

### 1. 输出变量

- **<%= expression %>:** 将 expression 的值输出到模板中，并进行 HTML 转义。这可以防止 XSS 攻击。
    
    ```HTML
    <p>Hello, <%= name %>!</p>
    ```
    

### 2. 执行 JavaScript 代码

- **<% code %>:** 在模板中执行 JavaScript 代码，但不输出任何内容。常用于控制流程、定义变量等。
    
    ```HTML
    <% if (user.isAdmin) { %>
      <p>You are an admin.</p>
    <% } %>
    ```
    

### 3. 输出未转义的 HTML

- **<%- expression %>:** 将 expression 的值输出到模板中，但不进行 HTML 转义。这对于输出原始 HTML 内容很有用。
    
    ```HTML
    <div><%- rawHtml %></div>
    ```
    

### 4. 注释

- **<%# comment %>:** 在模板中添加注释，不会输出到最终的 HTML 中。
    
    ```HTML
    <%# This is a comment %>
    ```
    

### 5. 包含其他模板

- **<%- include('path/to/template') %>:** 包含另一个 EJS 模板，可以实现模板的复用。
    
    ```HTML
    <%- include('header') %>
    <h1>Welcome</h1>
    <%- include('footer') %>
    ```
    

### 6. 控制语句

EJS 支持 JavaScript 中的所有控制语句，如 if、else、for、while 等。

```HTML
<% for (let i = 0; i < items.length; i++) { %>
  <li><%= items[i] %></li>
<% } %>
```

### 7. 自定义标签

EJS 允许你自定义标签，以满足特定的需求。

```js
ejs.open = '{%';
ejs.close = '%}';
```

### 示例

```js
const express = require('express');
const ejs = require('ejs');

const app = express();

app.set('view engine', 'ejs');

app.get('/', (req, res) => {
  const data = {
    title   : 'My EJS App',
    items: ['apple', 'banana', 'orange']
  };
  res.render('index', data);
});
```


```HTML
<!DOCTYPE html>
<html>
<head>
  <title><%= title %></title>
</head>
<body>
  <ul>
    <% for (let item of items) { %>
      <li><%= item %></li>
    <% } %>
  </ul>
</body>
</html>
```

### 总结

EJS 提供了一种简单而灵活的方式来生成动态 HTML 页面。通过掌握这些基本语法，你可以轻松地使用 EJS 来构建你的 Node.js 应用程序。

### 更多高级用法

- **过滤器：** EJS 支持自定义过滤器，对数据进行格式化。
- **Helpers：** 可以定义自定义的 helper 函数，用于处理复杂逻辑。
- **Layouts：** EJS 支持布局，可以将公共的部分提取出来，减少代码重复。