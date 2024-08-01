## 详细讲解 Fetch 调用 API

- [[#什么是 Fetch API？|什么是 Fetch API？]]
- [[#Fetch API 的基本用法|Fetch API 的基本用法]]
	- [[#Fetch API 的基本用法#GET请求|GET请求]]
- [[#Fetch API 的常用选项|Fetch API 的常用选项]]
- [[#处理响应|处理响应]]
- [[#错误处理|错误处理]]
- [[#异步/等待|异步/等待]]
	- [[#异步/等待#GET 请求示例|GET 请求示例]]
	- [[#异步/等待#POST请求示例|POST请求示例]]
- [[#处理其他响应类型|处理其他响应类型]]
	- [[#处理其他响应类型#处理文本响应|处理文本响应]]
	- [[#处理其他响应类型#处理二进制响应|处理二进制响应]]
- [[#传递查询参数|传递查询参数]]
- [[#优势|优势]]
- [[#注意事项|注意事项]]
### 什么是 Fetch API？

Fetch API 是一个现代化的 JavaScript 接口，用于获取资源（包括跨域）。它提供了一种简单、灵活且基于 Promise 的方式来发出网络请求。相较于传统的 XMLHttpRequest，Fetch API 更易于使用和理解。

### Fetch API 的基本用法

#### GET请求
GET请求通常用于获取数据，不需要发送请求体。
```JavaScript
// 发起 GET 请求
fetch('https://api.example.com/data')
  .then(response => {
    // 处理响应
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json(); // 将响应转换为 JSON
  })
  .then(data => {
    // 处理数据
    console.log(data);
  })
  .catch(error => {
    // 处理错误
    console.error('There has been a problem with your fetch operation:', error);
  });
```

**解释：**

1. **fetch() 方法：**
    
    - 接受一个 URL 作为参数，发起一个请求。
    - 返回一个 Promise 对象。
2. **.then() 方法：**
    
    - 接收一个回调函数，当 Promise 成功解析时执行。
    - `response` 对象包含了服务器的响应信息。
    - `response.json()` 方法将响应体解析为 JSON 格式。
3. **.catch() 方法：**
    
    - 接收一个回调函数，当 Promise 拒绝时执行。
    - 处理网络错误或其他异常。

### Fetch API 的常用选项

```JavaScript
fetch('https://api.example.com/data', {
  method: 'POST', // 请求方法 (GET, POST, PUT, DELETE 等)
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    // 请求体
  })
})
```

- **method:** 指定请求方法。
- **headers:** 设置请求头。
- **body:** 设置请求体。

### 处理响应

- **response.ok:** 判断响应是否成功。
- **response.status:** 获取响应状态码。
- **response.statusText:** 获取响应状态文本。
- **response.json():** 将响应体解析为 JSON。
- **response.text():** 将响应体解析为文本。
- **response.blob():** 将响应体解析为 Blob 对象。

### 错误处理

- **网络错误：** 当网络连接失败时，Promise 会被拒绝。
- **服务器错误：** 当服务器返回错误状态码时，Promise 会被拒绝。
- **解析错误：** 当解析响应数据时发生错误，Promise 会被拒绝。

### 异步/等待
`async/await` 可以使代码更简洁，更易于理解。
#### GET 请求示例

```JavaScript
async function fetchData() {
  try {
    const response = await fetch('https://api.example.com/data');
    if (!response.ok) {
      // 处理HTTP错误
      throw new Error('Network response was not ok ' + response.statusText);
    }
    const data = await response.json(); // 将响应解析为JSON
    console.log('GET请求的响应数据:', data);
  } catch (error) {
    // 处理错误
    console.error('请求失败:', error);
  }
}
fetchData();
```

#### POST请求示例

```js
async function postData() {
  const requestData = { key: 'value' };

  try {
    const response = await fetch('https://api.example.com/data', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });
    if (!response.ok) {
      // 处理HTTP错误
      throw new Error('Network response was not ok ' + response.statusText);
    }
    const data = await response.json(); // 将响应解析为JSON
    console.log('POST请求的响应数据:', data);
  } catch (error) {
    // 处理错误
    console.error('请求失败:', error);
  }
}
postData();
```

### 处理其他响应类型

除了JSON，`fetch`还可以处理其他类型的响应，如文本、二进制数据等。
#### 处理文本响应

```js
fetch('https://api.example.com/data')
  .then(response => response.text()) // 将响应解析为文本
  .then(data => {
    console.log('响应的文本数据:', data);
  })
  .catch(error => {
    console.error('请求失败:', error);
  });
```
#### 处理二进制响应

```js
fetch('https://api.example.com/data')
  .then(response => response.blob()) // 将响应解析为二进制大对象（Blob）
  .then(blob => {
    console.log('响应的二进制数据:', blob);
    // 可以将Blob数据处理成文件或其他用途
  })
  .catch(error => {
    console.error('请求失败:', error);
  });
```

### 传递查询参数

可以通过URLSearchParams对象或手动构建查询字符串来传递查询参数。

```js
const params = new URLSearchParams({ key1: 'value1', key2: 'value2' });
fetch(`https://api.example.com/data?${params.toString()}`)
  .then(response => response.json())
  .then(data => {
    console.log('带查询参数的GET请求响应数据:', data);
  })
  .catch(error => {
    console.error('请求失败:', error);
  });
```

### 优势

- **简洁易用：** 语法简洁，易于理解。
- **Promise 支持：** 天然支持 Promise，方便异步操作。
- **灵活配置：** 可以自定义请求头、请求体等。

### 注意事项

- **浏览器兼容性：** Fetch API 并非所有浏览器都支持，需要考虑兼容性问题。
- **CORS：** 跨域资源共享，需要服务器端设置 CORS 才能跨域请求。
- **错误处理：** 完善的错误处理机制是必不可少的。