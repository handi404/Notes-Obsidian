要在 JSON Server 中进行搜索，可以使用带有查询参数的 `GET` 请求。这允许你根据某些字段的值来筛选资源。JSON Server 提供了多种查询方式，下面是常见的几种搜索方式。

### 1. **根据字段值精确匹配**

你可以通过在 URL 中添加查询参数来查找与特定字段值匹配的对象。

**示例：查找作者为 "John Doe" 的所有帖子**

```bash
GET http://localhost:3000/posts?author=John Doe
```

这将返回所有 `author` 字段值为 `John Doe` 的帖子。

**JavaScript 示例**：

```javascript
const searchPostsByAuthor = async (author) => {
  const url = `http://localhost:3000/posts?author=${encodeURIComponent(author)}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Search results:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数查找作者为 "John Doe" 的帖子
searchPostsByAuthor('John Doe');
```

- `encodeURIComponent(author)`：确保作者名中的特殊字符（如空格）被正确编码。

### 2. **根据多个字段值匹配**

你可以在查询中指定多个字段进行组合搜索。

**示例：查找作者为 "John Doe" 且标题为 "Hello World" 的帖子**

```bash
GET http://localhost:3000/posts?author=John Doe&title=Hello World
```

这将返回同时满足 `author` 和 `title` 条件的帖子。

**JavaScript 示例**：

```javascript
const searchPosts = async (author, title) => {
  const url = `http://localhost:3000/posts?author=${encodeURIComponent(author)}&title=${encodeURIComponent(title)}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Search results:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数查找符合条件的帖子
searchPosts('John Doe', 'Hello World');
```

### 3. **使用 `_like` 进行模糊搜索**

你可以使用 `_like` 关键字来进行模糊匹配，这类似于 SQL 中的 `LIKE` 语句。

**示例：查找标题中包含 "Hello" 的所有帖子**

```bash
GET http://localhost:3000/posts?title_like=Hello
```

这将返回所有 `title` 字段中包含 "Hello" 的帖子。

**JavaScript 示例**：

```javascript
const searchPostsByTitle = async (title) => {
  const url = `http://localhost:3000/posts?title_like=${encodeURIComponent(title)}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Search results:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数查找标题中包含 "Hello" 的帖子
searchPostsByTitle('Hello');
```

### 4. **使用 `_sort` 和 `_order` 进行排序**

你可以使用 `_sort` 和 `_order` 参数来对结果进行排序。

**示例：按 `id` 降序排列所有帖子**

```bash
GET http://localhost:3000/posts?_sort=id&_order=desc
```

**JavaScript 示例**：

```javascript
const getSortedPosts = async () => {
  const url = `http://localhost:3000/posts?_sort=id&_order=desc`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('Sorted posts:', data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数获取排序后的帖子
getSortedPosts();
```

### 5. **分页查询**

你可以通过 `_page` 和 `_limit` 参数实现分页。

**示例：获取第 2 页，每页 5 条的帖子**

```bash
GET http://localhost:3000/posts?_page=2&_limit=5
```

**JavaScript 示例**：

```javascript
const getPagedPosts = async (page, limit) => {
  const url = `http://localhost:3000/posts?_page=${page}&_limit=${limit}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log(`Page ${page} results:`, data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数获取第 2 页，每页 5 条的帖子
getPagedPosts(2, 5);
```

### 6. **组合查询**

你可以将上述各种查询方式组合起来使用，例如模糊搜索、排序、分页等。

**示例：获取标题中包含 "Post" 的所有帖子，按 `id` 降序排序，显示第 1 页，每页 3 条**

```bash
GET http://localhost:3000/posts?title_like=Post&_sort=id&_order=desc&_page=1&_limit=3
```

**JavaScript 示例**：

```javascript
const searchAndSortPosts = async (title, page, limit) => {
  const url = `http://localhost:3000/posts?title_like=${encodeURIComponent(title)}&_sort=id&_order=desc&_page=${page}&_limit=${limit}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log(`Search and sort results:`, data);
  } catch (error) {
    console.error('Error:', error);
  }
};

// 调用函数查找并排序
searchAndSortPosts('Post', 1, 3);
```

### 总结

JSON Server 提供了多种查询参数，允许你根据不同的需求来进行数据搜索、排序和分页。通过 `GET` 请求和查询参数的灵活组合，你可以非常方便地从 JSON Server 中获取所需的数据。