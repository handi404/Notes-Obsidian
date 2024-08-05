	views文件夹 中有 404.html  about.html  index.html

```js
const http = require('http');
const fs = require('fs');

const server = http.createServer((req, res) => {
  // console.log(req);
  console.log(req.url);

  // 设置标题内容类型 set header content type
  res.setHeader('Content-Type', 'text/html');

  // res.write('<p>hello, ninjas</p>');
  // res.write('<p>hello again, ninjas</p>');
  // res.end();

  // 发送 html 文件
  // fs.readFile('./views/index.html', (err, data) => {
  //   if (err) {
  //     console.log(err);
  //     res.end();
  //   }
  //   //res.write(data);
  //   res.end(data);
  // });

  //  路由routing
  let path = './views/';
  switch(req.url) {
    case '/':
      path += 'index.html';
      res.statusCode = 200;
      break;
    case '/about':
      path += 'about.html';
      res.statusCode = 200;
      break;
    case '/about-us':
      res.statusCode = 301;
      res.setHeader('Location', '/about');  //重定向redirect
      res.end();
      break;
    default:
      path += '404.html';
      res.statusCode = 404;
  }

  // 发送 html send html
  fs.readFile(path, (err, data) => {
    if (err) {
      console.error(err);
      res.end();
    }
    //res.write(data);
    res.end(data);
  });


});

// localhost 是第二个参数的默认值localhost
server.listen(3000, 'localhost', () => {
  console.log('listening for requests on port 3000');
});
```

## 代码解释

### 整体功能

这段代码创建了一个简单的 Node.js HTTP 服务器，能够根据客户端请求的 URL，返回不同的 HTML 文件。它实现了基本的路由功能，并处理了常见的 HTTP 状态码。
### 总结

这段代码实现了一个简单的 Web 服务器，能够根据请求的 URL 返回不同的 HTML 页面。它使用了 Node. js 的 `http` 和 `fs` 模块来处理 HTTP 请求和文件读写。
### 代码逐行解释

```JavaScript
const http = require('http');
const fs = require('fs');
```

- 引入 Node.js 内置的 `http` 和 `fs` 模块，分别用于创建 HTTP 服务器和文件操作。


```JavaScript
const server = http.createServer((req, res) => {
  // ...
});
```

- 创建一个 HTTP 服务器，并定义了一个回调函数来处理传入的请求 (req) 和响应 (res) 对象。


```JavaScript
console.log(req.url);
```

- 打印出客户端请求的 URL 到控制台，用于调试。


```JavaScript
res.setHeader('Content-Type', 'text/html');
```

- 设置响应头中的 `Content-Type` 为 `text/html`，表示响应内容是 HTML 格式。


```JavaScript
// ... commented out code ...
```

- 注释掉的代码是用于测试和演示的，可以忽略。


```JavaScript
let path = './views/';
switch(req.url) {
  // ... routing logic ...
}
```

- 定义了一个变量 `path` 来存储要读取的文件路径。
- 使用 `switch` 语句根据请求的 URL 进行路由：
    - `/`: 返回 `index.html` 文件，设置状态码为 200 (成功)。
    - `/about`: 返回 `about.html` 文件，设置状态码为 200 (成功)。
    - `/about-us`: 重定向到 `/about`，设置状态码为 301 (永久重定向)。
    - 其他情况：返回 `404.html` 文件，设置状态码为 404 (未找到)。


```JavaScript
fs.readFile(path, (err, data) => {
  // ...
});
```

- 使用 `fs.readFile` 读取指定路径的文件内容。
- 如果读取成功，将文件内容作为响应发送给客户端。
- 如果读取失败，打印错误信息并结束响应。


```JavaScript
server.listen(3000, 'localhost', () => {
  console.log('listening for requests on port 3000');
});
```

- 启动服务器，监听 `localhost` 的 3000 端口，并在成功启动时打印一条消息。