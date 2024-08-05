	npm install mongoose

[[Mongoose]]

```js
const express = require('express');
const morgan = require('morgan');
const mongoose = require('mongoose');
const Blog = require('./models/blog');

// express app
const app = express();

// connect to mongodb & listen for requests
const dbURI = "mongodb+srv://netninja:test1234@net-ninja-tuts-del96.mongodb.net/node-tuts";

mongoose.connect(dbURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(result => app.listen(3000))
  .catch(err => console.log(err));

// register view engine
app.set('view engine', 'ejs');

// middleware & static files
app.use(express.static('public'));
app.use(morgan('dev'));
app.use((req, res, next) => {
  res.locals.path = req.path;
  next();
});

// mongoose & mongo tests
app.get('/add-blog', (req, res) => {
  const blog = new Blog({
    title: 'new blog',
    snippet: 'about my new blog',
    body: 'more about my new blog'
  })

  blog.save()
    .then(result => {
      res.send(result);
    })
    .catch(err => {
      console.log(err);
    });
});

app.get('/all-blogs', (req, res) => {
  Blog.find()
    .then(result => {
      res.send(result);
    })
    .catch(err => {
      console.log(err);
    });
});

app.get('/single-blog', (req, res) => {
  Blog.findById('5ea99b49b8531f40c0fde689')
    .then(result => {
      res.send(result);
    })
    .catch(err => {
      console.log(err);
    });
});

app.get('/', (req, res) => {
  res.redirect('/blogs');
});

app.get('/about', (req, res) => {
  res.render('about', { title: 'About' });
});

// blog routes
app.get('/blogs/create', (req, res) => {
  res.render('create', { title: 'Create a new blog' });
});

app.get('/blogs', (req, res) => {
  Blog.find().sort({ createdAt: -1 })
    .then(result => {
      res.render('index', { blogs: result, title: 'All blogs' });
    })
    .catch(err => {
      console.log(err);
    });
});

// 404 page
app.use((req, res) => {
  res.status(404).render('404', { title: '404' });
});
```
	models/blog.js
```js
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const blogSchema = new Schema({
  title: {
    type: String,
    required: true,
  },
  snippet: {
    type: String,
    required: true,
  },
  body: {
    type: String,
    required: true
  },
}, { timestamps: true });

const Blog = mongoose.model('Blog', blogSchema);
module.exports = Blog;
```

详细讲解一下这段代码中连接 MongoDB 的部分：

```JavaScript
const mongoose = require('mongoose');
const dbURI = "mongodb+srv://netninja:test1234@net-ninja-tuts-del96.mongodb.net/node-tuts";

mongoose.connect(dbURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(result => app.listen(3000))
  .catch(err => console.l   og(err));
```

### 代码解析：

1. **引入 Mongoose**:
    
    - `const mongoose = require('mongoose');`： 这一行代码引入了 Mongoose 这个 Node.js 的 ODM（对象文档映射器），它提供了一套方便的 API 来操作 MongoDB 数据库。
2. **定义数据库连接字符串**:
    
    - `const dbURI = "mongodb+srv://netninja:test1234@net-ninja-tuts-del96.mongodb.net/node-tuts";`：
        - `dbURI` 变量存储了连接到 MongoDB 数据库的 URL。这个 URL 中包含了以下信息：
            - `mongodb+srv://`： 指明这是连接到 MongoDB Atlas 服务的 URI 格式。
            - `netninja:test1234`： 连接数据库的用户名和密码。
            - `net-ninja-tuts-del96.mongodb.net`： MongoDB 集群的域名。
            - `node-tuts`： 要连接的数据库名称。
3. **连接数据库**:
    
    - `mongoose.connect(dbURI, { useNewUrlParser: true, useUnifiedTopology: true })`:
        - `mongoose.connect()` 方法用于连接到指定的 MongoDB 数据库。
        - `dbURI`： 即上面定义的数据库连接字符串。
        - `{ useNewUrlParser: true, useUnifiedTopology: true }`： 这是连接选项，用于指定使用新的 URL 解析器和统一拓扑。
        - `.then(result => app.listen(3000))`: 如果连接成功，则启动 Express 服务器，监听 3000 端口。
        - `.catch(err => console.log(err))`: 如果连接失败，则输出错误信息。

### 连接过程详解：

1. **建立连接**: 当 Node.js 程序运行到这一行代码时，Mongoose 会尝试根据提供的 URL 连接到指定的 MongoDB 数据库。
2. **验证身份**: Mongoose 会使用提供的用户名和密码进行身份验证。
3. **选择数据库**: 连接成功后，Mongoose 会选择指定的数据库（`node-tuts`）。
4. **返回连接对象**: 如果连接成功，`mongoose.connect()` 方法会返回一个连接对象。
5. **启动服务器**: 如果连接成功，则调用 `app.listen(3000)` 启动 Express 服务器。

### 为什么使用 Mongoose？

- **简化操作**: Mongoose 提供了面向对象的接口，可以方便地操作 MongoDB 数据库，而不需要直接编写 MongoDB 查询语句。
- **类型安全**: Mongoose 可以定义数据模型的结构，确保数据的类型和格式正确。
- **验证**: Mongoose 可以对数据进行验证，防止无效数据的插入。
- **查询构建器**: Mongoose 提供了强大的查询构建器，可以方便地进行各种复杂的查询。


## 本地 MongoDB 连接方式

**是的，连接本地 MongoDB 数据库的方式和连接远程数据库的方式非常相似。** 不同之处主要在于连接字符串的格式。

### 远程 MongoDB 连接字符串

前面我们已经看到了连接远程 MongoDB 数据库的连接字符串：

```JavaScript
const dbURI = "mongodb+srv://netninja:test1234@net-ninja-tuts-del96.mongodb.net/node-tuts";
```

这个字符串包含了：

- **协议**：mongodb+srv 表示连接到 MongoDB Atlas 服务。
- **认证信息**：netninja:test1234 是用户名和密码。
- **主机名**：net-ninja-tuts-del96.mongodb.net 是 MongoDB 集群的域名。
- **数据库名**：node-tuts 是要连接的数据库。

### 本地 MongoDB 连接字符串

对于本地 MongoDB，连接字符串会更简单，一般格式为：

```JavaScript
const dbURI = 'mongodb://localhost:27017/yourDatabaseName';
```

- **mongodb://**： 表示使用 MongoDB 协议。
- **localhost**： 表示连接到本地主机。
- **27017**： MongoDB 默认的端口号。
- **yourDatabaseName**： 你要连接的数据库名称。

**示例：**

```JavaScript
const mongoose = require('mongoose');

const dbURI = 'mongodb://localhost:27017/myBlog';

mongoose.connect(dbURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(result => console.log('Connected to DB'))
  .catch(err => console.log   (err));
```

**注意：**

- **没有用户名和密码**： 如果你没有为本地 MongoDB 设置认证，则不需要提供用户名和密码。
- **端口号**： 如果你的 MongoDB 服务监听的是其他端口，请将 `27017` 替换为对应的端口号。

### 总结

连接本地 MongoDB 的步骤和连接远程 MongoDB 的步骤基本一致，主要区别在于连接字符串的格式。

**本地连接更简单，因为不需要提供复杂的认证信息和主机名。**

### 其他注意事项

- **环境变量**: 在生产环境中，通常会将数据库连接字符串存储在环境变量中，以避免将敏感信息暴露在代码中。
- **连接池**: Mongoose 会维护一个连接池，以提高连接效率。
- **断开连接**: 在程序退出时，记得关闭与数据库的连接。