[[req 和 res 对象]]
[[引入和导出]]

```js
const http = require('http');

const server = http.createServer((req, res) => {
  console.log('提出请求request made');
});

// localhost 是第二个参数的默认值localhost is the default value for 2nd argument
server.listen(3000, 'localhost', () => {
  console.log('侦听端口 3000 上的请求listening for requests on port 3000');
});
```

https://github.com/iamshaunjp/node-crash-course