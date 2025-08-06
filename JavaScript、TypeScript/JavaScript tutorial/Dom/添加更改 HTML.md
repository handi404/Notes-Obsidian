
---

// ---------- 示例 1 h1 元素 ----------
```js
// 第 1 步创建元素
const newH1 = document.createElement("h1");

// 第 2 步添加属性/属性
newH1.textContent = "I like pizza!";
newH1.id = "myH1";
newH1.style.color = "tomato";
newH1.style.textAlign = "center";

// 步骤 3 将元素附加到 DOM
document.body.append(newH1); //添加到最后
// document.body.prepend(newH1); //添加到最前
// document.getElementById("box1").append(newH1);
// document.getElementById("box1").prepend(newH1);

// const box4 = document.getElementById("box4");
// document.body.insertBefore(newH1, box4); //添加到 box4 前

// const box = document.querySelectorAll(".box");
// document.body.insertBefore(newH1,boxes[0]);

// 删除 HTML 元素
// document.body.removeChild(newH1);
// document.getElementById("box1").removeChild(newH1);
```
```html
<body>
    <div id="box1" class="box">
      <p>Box1</p>
    </div>

    <div id="box2" class="box">
      <p>Box2</p>
    </div>

    <div id="box3" class="box">
      <p>Box3</p>
    </div>

    <div id="box4" class="box">
      <p>Box4</p>
    </div>

    <script src="index.js"></script>
  </body>
```
```css
.box{
	border: 3px solid;
	width: 100%;
	height: 125px;
}
```


// ---------- 示例 2 li 元素 ----------
```js
// 第 1 步创建元素
const newListItem = document.createElement("li");

// 第 2 步添加属性/属性
newListItem.textContent = "coconut";
newListItem.id = "coconut";
newListItem.style.fontWeight = "bold";
newListItem.style.backgroundColor = "lightgreen";

// 步骤 3 将元素附加到 DOM
document.body.append(newListItem);
// document.body.prepend(newListItem);
// document.getElementById("fruits").append(newListItem);
// document.getElementById("fruits").prepend(newListItem);

// const Banana = document.getElementById("banana");
// document.getElementById("fruits").insertBefore(newListItem, banana);

// const listItems = document.querySelectorAll("#fruits li");
// document.getElementById("fruits").insertBefore(newListItem, listItems[1]);

// 删除 HTML 元素
// document.body.removeChild(newLink);
// document.getElementById("fruits").removeChild(newListItem);
```
```html
<body>
    <ol id="fruits">
      <li id="apple">apple</li>
      <li id="orange">orange</li>
      <li id="banana">banana</li>
    </ol>
  </body>
```
```css
#fruits{
	border: 3px solid;
	font-size: 2rem;
}
```