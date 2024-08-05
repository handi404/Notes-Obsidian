![[Pasted image 20240804093642.png]]
		
中间件示例 
记录器中间件，记录每个请求的详细信息 
受保护路由的身份验证检查中间件 
从请求中解析 JSON 数据的中间件 
返回 404 页面
	
	
	app.use  app.get 都为中间件


	npm install morgan
有旧版本的NPM，那么需要双重保存标志 npm install morgan --save
[[Morgan]]

```js
const express = require("express");
const morgan = require("morgan");

const app = express();

app.set("view engine", "ejs");

app.listen(3000, () => {
  console.log("Server listening on port 3000");
});

//可以看到当在这里运行这个中间件时浏览器是如何挂起的
//它不会自动知道如何继续前进，我们必须通过使用一个名为 next 的函数来做到这一点
//明确地说，我们已经完成了这个中间件的工作，现在继续进行下一个部分
// app.use((req, res, next) => {
//   console.log("new request made");
//   console.log("host: ", req.hostname);
//   console.log("path: ", req.path);
//   console.log("method: ", req.method);
//   next();
// });
// app.use((req, res, next) => {
//   console.log("in the next");
//   next();
// });

//中间件和静态文件
//想要向浏览器公开的文件夹 		可让partials中的head.ejs链接public中的style.css
app.us(express.static("public"));

app.use(morgan("dev")); //利用morgan代替代替上面已注释的

app.get("/", (req, res) => {
  //   res.send("<p>Hello</p>");
  //   res.sendFile("./views/index.html", { root: __dirname });
  const blogs = [
    {
      title: "Yoshi finds eggs",
      snippet: "Lorem ipsum dolor sit amet consectetur",
    },
    {
      title: "Mario finds stars",
      snippet: "Lorem ipsum dolor sit amet consectetur",
    },
    {
      title: "How to defeat bowser",
      snippet: "Lorem ipsum dolor sit amet consectetur",
    },
  ];
  res.render("index", { title: "Home", blogs });
});

app.get("/about", (req, res) => {
  //   res.send("<p>about</p>");
  //   res.sendFile("./views/about.html", { root: __dirname });
  res.render("about", { title: "About" });
});

app.get("/blogs/create", (req, res) => {
  res.render("create", { title: "create a new blog" });
});

app.use((req, res) => {
  res.status(404).render("404", { title: "404" });
});
```


