	views 文件夹中有 404. html  about. html  index. html

[[Express.js 中定义路由处理函数的方法]]

```js
const express = require('express');

// express app
const app = express();

// 倾听请求listen for requests
app.listen(3000, () => {
  console.log("Server listening on port 3000");
});

app.get('/', (req, res) => {
  // res.send('<p>home page</p>');
  res.sendFile('./views/index.html', { root: __dirname });
});

app.get('/about', (req, res) => {
  // res.send('<p>about page</p>');
  res.sendFile('./views/about.html', { root: __dirname });
});

// redirects
app.get('/about-us', (req, res) => {
  res.redirect('/about');
});

// 404 page
app.use((req, res) => {
  res.status(404).sendFile('./views/404.html', { root: __dirname });
});
```

## 代码解析：一个简单的 Node.js Express 应用
### 总结

这段代码创建了一个基本的 web 服务器，为不同的 URL 路径提供 HTML 页面。它还处理重定向和 404 错误。

### 概述

这段代码使用 Node.js 和 Express 框架搭建了一个基本的网页应用。它为不同的 URL 路径定义了路由，并相应地提供 HTML 文件。
### 详细解释

1. **引入 Express**
    
    ```JavaScript
    const express = require('express');
    ```
    
    这行代码引入 Express 模块，它是 Node.js 中一个流行的用于构建 web 应用的框架。
    
2. **创建 Express 应用**
    
    ```JavaScript
    const app = express();
    ```
    
    这行代码创建一个 Express 应用实例。这个实例将用于定义路由并处理传入的请求。
    
3. **启动服务器**
    
    ```JavaScript
    app.listen(3000);
    ```
    
    这行代码启动 Express 应用，并在 3000 端口上监听传入的连接。
    
4. **定义路由**
    
    - **主页路由:**
        
        ```JavaScript
        app.get('/', (req, res) => {
          res.sendFile('./views/index.html', { root: __dirname });
        });
        ```
        
        - 这个路由处理对根路径 (`/`) 的 GET 请求。
        - 它发送位于 `views` 目录下的 `index.html` 文件，该目录相对于当前文件所在的目录 (`__dirname`)。
    - **关于页路由:**
        
        ```JavaScript
        app.get('/about', (req, res) => {
          res.sendFile('./views/about.html', { root: __dirname });
        });
        ```
        
        - 这个路由处理对 `/about` 路径的 GET 请求。
        - 它发送位于 `views` 目录下的 `about.html` 文件。
    - **重定向路由:**
        
        ```JavaScript
        app.get('/about-us', (req, res) => {
          res.redirect('/about');
        });
        ```
        
        - 这个路由处理对 `/about-us` 路径的 GET 请求。
        - 它将用户重定向到 `/about` 路径。
    - **404 错误路由:**
        
        ```JavaScript
        app.use((req, res) => {
          res.status(404).sendFile('./views/404.html', { root: __dirname });
        });
        ```
        
        - 这个路由是一个中间件函数，用于处理所有与前面路由不匹配的请求。
        - 它将响应状态设置为 404（未找到），并发送 `404.html` 文件。

### 关键点

- `req` 对象表示传入的请求，`res` 对象表示传出的响应。
- `sendFile` 方法用于发送文件作为响应。
- `redirect` 方法用于将用户重定向到不同的 URL。
- 中间件函数可以在路由处理程序之前处理请求。

## __dirname 的含义

在 Node.js 中，`__dirname` 是一个全局变量，它代表当前正在执行的 JavaScript 文件所在的目录的 **绝对路径**。

### 简单来说：

- **__dirname** 就相当于一个“指路牌”，它总是指向当前文件所在的文件夹。
- 无论你把这个 JavaScript 文件放在哪里，`__dirname` 都会自动调整，始终指向正确的路径。

### 用途

- **动态获取文件路径：** 当你需要动态地获取某个文件或文件夹的路径时，可以使用 `__dirname` 来拼接路径。例如，如果你想读取一个名为 `data.json` 的文件，可以这样写：

```JavaScript
const fs = require('fs');
const data = fs.readFileSync(__dirname + '/data.json');
```

- **构建相对路径：** 在模块化开发中，经常需要引用其他文件。使用 `__dirname` 可以构建相对路径，提高代码的可维护性。

### 示例

```JavaScript
// 假设当前文件路径为：/home/user/projects/myapp/app.js

console.log(__dirname); // 输出：/home/user/projects/myapp
```

### 注意

- `__dirname` 是一个字符串，可以直接与其他字符串拼接。
- `__dirname` 只在 Node.js 环境中有效，在浏览器环境中是无法使用的。
- 除了 `__dirname`，还有一个类似的变量 `__filename`，它表示当前文件的 **绝对路径**。

### 为什么使用 __dirname？

- **提高代码可移植性：** 无论你将文件移动到哪个目录，`__dirname` 都会自动更新，保证代码的正确性。
- **避免硬编码路径：** 硬编码路径容易出错，而且难以维护。使用 `__dirname` 可以避免这个问题。

### 小结

`__dirname` 是 Node.js 开发中非常常用的一个变量。它帮助我们动态地获取文件路径，提高代码的可维护性。理解 `__dirname` 的含义和用法，对于编写高质量的 Node.js 应用非常重要。