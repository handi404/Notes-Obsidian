全局安装软件包 （nodemon）
npm install nodemon

package.json文件和本地安装包
**npm init**
npm install lodash       [[Lodash]]
## npm init 的作用

**npm init** 是 Node.js 包管理器 npm 的一个重要命令，它的主要作用是**初始化一个新的 Node.js 项目**，并在当前目录下生成一个 `package.json` 文件。

### `package.json` 文件的作用

- **记录项目信息:** 包括项目名称、版本号、描述、作者、许可证等基本信息。
- **管理项目依赖:** 列出项目所依赖的第三方模块以及它们的版本号。
- **作为项目的配置文件:** 可以配置一些脚本命令（如启动、测试、构建等），方便项目的开发和部署。

### npm init 的过程

1. **执行命令:** 在终端中进入项目目录，执行 `npm init` 命令。
2. **交互式提问:** npm 会提示你输入一些关于项目的信息，如项目名称、版本号等。你可以直接输入，也可以按回车使用默认值。
3. **生成 package.json:** npm 会根据你输入的信息，在当前目录下创建一个 `package.json` 文件。

### npm init 的好处

- **标准化项目结构:** `package.json` 文件为 Node.js 项目提供了一个标准化的结构，方便项目管理和协作。
- **方便依赖管理:** ==可以通过 `npm install` 命令根据 `package.json` 文件中的配置，自动安装项目所需要的依赖模块。==
- **提高项目可维护性:** `package.json` 文件记录了项目的详细信息，方便后期维护和升级。

### 示例

```Bash
# 在终端中进入项目目录
cd my-project

# 执行 npm init
npm init

# 按照提示输入项目信息

# 生成的 package.json 文件示例
{
  "name": "my-project",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
  "license": "   ISC",
  "dependencies": {
    "express   ": "^4.17.1"
  }
}
```

### 总结

`npm init` 是创建 Node.js 项目的必备步骤，它生成的 `package.json` 文件是项目的核心配置文件。通过这个文件，我们可以方便地管理项目的依赖、配置脚本命令，从而提高开发效率。


```js
const http = require('http');
const fs = require('fs');
const _ = require('lodash');

const server = http.createServer((req, res) => {

  // lodash
  const num = _.random(0, 20);
  console.log(num);

  const greet = _.once(() => {
    console.log('hello');
  });
  greet();
  greet();

  // set header content type
  res.setHeader('Content-Type', 'text/html');

  // routing
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
      res.setHeader('Location', '/about');
      res.end();
      break;
    default:
      path += '404.html';
      res.statusCode = 404;
  }

  // send html
  fs.readFile(path, (err, data) => {
    if (err) {
      console.log(err);
      res.end();
    }
    //res.write(data);
    res.end(data);
  });


});

// localhost is the default value for 2nd argument
server.listen(3000, 'localhost', () => {
  console.log('listening for requests on port 3000');
});
```

