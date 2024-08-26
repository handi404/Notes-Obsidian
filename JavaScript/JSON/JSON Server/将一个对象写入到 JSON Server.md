要将一个对象写入到 JSON Server 中，可以使用 HTTP 请求（通常是 `POST` 请求）将数据发送到 JSON Server 提供的 REST API 端点。以下是详细的步骤：

### 假设环境

假设我们已经有一个 JSON Server 在运行，且它使用的数据库文件 `db.json` 中包含如下内容：

```json
{
  "posts": [
    { "id": 1, "title": "Hello World", "author": "John Doe" }
  ]
}
```

### 通过 `POST` 请求写入对象

如果你想向 `posts` 资源添加一个新的对象，可以通过发送 `POST` 请求来实现。

#### 1. 使用 `curl` 命令行工具

在终端中，可以使用 `curl` 命令发送 `POST` 请求。例如，要添加一个新的帖子对象：

```bash
curl -X POST -H "Content-Type: application/json" -d '{"title": "New Post", "author": "Jane Doe"}' http://localhost:3000/posts
```

- `-X POST` 指定了请求方法为 `POST`。
- `-H "Content-Type: application/json"` 设置请求头，表明请求体的内容类型为 JSON。
- `-d '{"title": "New Post", "author": "Jane Doe"}'` 是发送的数据（即要写入的对象）。
- `http://localhost:3000/posts` 是 API 的 URL，`posts` 是我们要操作的资源。

执行这个命令后，JSON Server 会将新的对象添加到 `posts` 资源中，并**自动生成一个 `id`**（假设现在 `id` 为 2）：

```json
{
  "id": 2,
  "title": "New Post",
  "author": "Jane Doe"
}
```

#### 2. 使用 JavaScript (Fetch API)

在前端 JavaScript 中，可以使用 `fetch` API 发送 `POST` 请求来写入对象：

```javascript
const createPost = async () => {
  const url = 'http://localhost:3000/posts';
  const newPost = {
    title: 'Another Post with Async/Await and Arrow Function',
    author: 'John Smith'
  };

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newPost)
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Successfully created post:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用 createPost 函数
createPost();
```

- `method: 'POST'` 指定请求方法为 `POST`。
- `headers` 包含 `Content-Type` 头，表示请求体的内容类型为 JSON。
- `body` 包含请求体，即要写入的对象，使用 `JSON.stringify` 将 JavaScript 对象转换为 JSON 字符串。

JSON Server 会将对象写入 `posts` 资源，并返回写入后的对象（包括生成的 `id`）。

#### 3. 使用 Postman 工具

如果你更喜欢图形化界面，可以使用 Postman 工具发送 `POST` 请求：

1. 打开 Postman，选择 `POST` 方法。
2. 输入 URL，如 `http://localhost:3000/posts`。
3. 在 `Body` 选项卡中，选择 `raw`，并将数据格式设置为 `JSON`。
4. 输入要发送的 JSON 对象，例如：

   ```json
   {
     "title": "Learning JSON Server",
     "author": "Alice"
   }
   ```

5. 点击 `Send` 按钮。

JSON Server 将返回带有 `id` 的新对象，表示成功写入。

### 结果

无论你使用哪种方法，最终都会将新对象添加到 `db.json` 中的 `posts` 资源中。假设我们添加了两个新的对象，`db.json` 文件现在看起来可能像这样：

```json
{
  "posts": [
    { "id": 1, "title": "Hello World", "author": "John Doe" },
    { "id": 2, "title": "New Post", "author": "Jane Doe" },
    { "id": 3, "title": "Another Post", "author": "John Smith" },
    { "id": 4, "title": "Learning JSON Server", "author": "Alice" }
  ]
}
```

每次添加新的对象，JSON Server 会自动为它们生成唯一的 `id`，并将数据写入到相应的资源中。