如何从 HTML 中获取元素。 
如何在 javascript 中添加类。 
使用普通 JavaScript 创建一个待办事项列表类型的应用程序。

```html
<!DOCTYPE html>

<html lang="en">

  <head>

    <meta charset="UTF-8" />

    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="style.css" />

    <title>Document</title>

  </head>

  <body>

    <h1 class="title">hello</h1>

    <button class="changeColor">chick me</button>

    <ul class="nameList">

      <li>ed</li>

      <li>joy</li>

      <li>boy</li>

      <li>girl</li>

      <li>change</li>

      <li>Harry</li>

    </ul>

    <input type="text" class="input" />

  </body>

  <script src="app.js"></script>

</html>
```

```js
const title = document.querySelector(".title");

const changeColor = document.querySelector(".changeColor");

const nameList = document.querySelectorAll(".nameList li"); //获取ul中的所有li

const nameList2 = document.querySelector(".nameList");

const input = document.querySelector(".input");

  

title.classList.add("change"); //classList包含所有的类

title.classList.remove("change");

  

// changeColor.addEventListener("click", function () {

//   title.classList.toggle("change");

// }); //事件监听器

  

changeColor.addEventListener("click", function () {

  // 实现输入添加功能

  const newLi = document.createElement("LI"); //创建li标签

  //   console.log(input.value); //value 动态获取输入的值

  const liContent = document.createTextNode(input.value);

  newLi.appendChild(liContent);

  nameList2.appendChild(newLi);

});

  

for (user of nameList) {

  user.addEventListener("click", function () {

    console.log(this);

  });

}
```

```css
/* 创建类 */

.change {

  color: lightblue;

  font-size: 85px;

  border: 10px solid black;

}
```