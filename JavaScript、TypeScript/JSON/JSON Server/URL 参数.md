URL 参数是通过 URL 向服务器传递数据的一种方式，通常用于 GET 请求中。它们通常位于 URL 的查询部分，以键值对的形式出现。

### URL 参数的结构

URL 参数位于 URL 中的问号 `?` 之后，并且多个参数之间用 `&` 分隔。每个参数由键和值组成，键和值之间用等号 `=` 分隔。参数的结构如下：

```
http://example.com/page?key1=value1&key2=value2
```

- `http://example.com/page` 是基础 URL（base URL）。
- `?` 表示 URL 参数的开始。
- `key1=value1` 是第一个参数的键和值。
- `&` 用于分隔多个参数。
- `key2=value2` 是第二个参数的键和值。

### 示例

假设有一个用于搜索的 URL，用户可以输入关键词和选择结果数量：

```
http://example.com/search?q=apple&limit=10
```

- `q=apple`：`q` 是参数名，`apple` 是用户搜索的关键词。
- `limit=10`：`limit` 是参数名，`10` 是返回的搜索结果数量。

### URL 参数的用途

1. **数据传递**：
   - URL 参数经常用于将用户输入的数据传递给服务器，如搜索关键词、筛选条件等。

2. **分页**：
   - 在返回大量数据时，可以通过 `page` 和 `limit` 参数控制分页，比如：
     ```
     http://example.com/articles?page=2&limit=20
     ```
   - 这表示请求第 2 页的内容，每页显示 20 条数据。

3. **筛选和排序**：
   - 可以通过参数实现数据的筛选和排序：
     ```
     http://example.com/products?category=electronics&sort=price_desc
     ```
   - 这表示请求 `electronics` 类别的产品，并按价格从高到低排序。

4. **状态保持**：
   - URL 参数可以用于保持某些状态，如选定的标签、语言设置等。
     ```
     http://example.com/profile?tab=posts&lang=en
     ```
   - 这表示显示用户的 `posts` 标签页，并使用 `en`（英语）界面。

### 编码和解码

URL 参数中的特殊字符（如空格、`&`、`=` 等）需要进行 URL 编码，以避免与实际的 URL 语法混淆。常见的编码规则包括：

- 空格被编码为 `%20` 或 `+`。
- `&` 被编码为 `%26`。
- `=` 被编码为 `%3D`。

**示例**：
```
http://example.com/search?q=hello world
```
在 URL 中，空格需要编码，所以实际 URL 是：
```
http://example.com/search?q=hello%20world
```

### URL 参数的读取

在服务器端或客户端（如 JavaScript 中），可以解析 URL 参数并获取其值。例如，JavaScript 中可以通过以下方式获取 URL 参数：

```javascript
const urlParams = new URLSearchParams(window.location.search);
const query = urlParams.get('q'); // 获取参数 q 的值
```

### URL 参数的安全性

由于 URL 参数是公开的，并且出现在浏览器地址栏中，敏感信息不应通过 URL 参数传递。对于需要保密的信息，推荐使用 POST 请求的请求体来传递数据，或者使用加密的方法保护参数。

总结：URL 参数是一种灵活且常用的方式，用于在客户端和服务器之间传递数据，特别适合查询、筛选、分页等场景。开发者需要注意其编码、解码，以及避免传递敏感信息。