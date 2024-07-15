#### // DOM = 文档对象模型 (document)
#### // Object{} 代表您在网络浏览器中看到的页面 
#### // 并为您提供与之交互的 API。
#### // Web 浏览器在加载 HTML 文档时构造 DOM，
#### // 并以树状表示形式构造所有元素。
#### // JavaScript 可以动态访问 DOM 
#### // 更改网页的内容、结构和样式。

---

```js
console.log(document);
console.dir(document);

document.title = "My website";
document.body.style.backgroundColor = "hsl(0, 0%, 15%)";

const username = "";
const welcomeMsg = document.getElementById("welcome-msg");
welcomeMsg.textContent += username === "" ? `Guest` : username;

//---html---
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <h1 id="welcome-msg">Welcome </h1>
    <script src="index.js"></script>
</body>
</html>
```



![[屏幕截图 2024-07-13 155458.png]]