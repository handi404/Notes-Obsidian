
#### // NodeList = HTML 元素的静态集合 (id, class, element)
#### // 可以使用 querySelectorAll () 创建
#### // 与数组类似，但没有（map、filter、reduce） 
#### // NodeList 不会更新以自动反映更改 
##### //需重新 querySelectorAll () 以获得更新的 NodeList

---

```js
// ---------- 创建节点列表 ----------
let buttons = document.querySelectorAll(".myButtons");

// ---------- 添加 HTML/CSS 属性 ----------
buttons.forEach(button => {
    button.style.backgroundColor = "green";
    button.textContent += "😊";
});

// ---------- CLICK 事件监听器 ----------
buttons.forEach(button => {
    button.addEventListener("click", event => {
        event.target.style.backgroundColor = "tomato";
    });
});

// ---------- MOUSEOVER + MOUSEOUT 事件监听器 ----------
buttons.forEach(button => {
    button.addEventListener("mouseover", event => {
        event.target.style.backgroundColor = "hsl(205, 100%, 40%)";
    });
});
buttons.forEach(button => {
    button.addEventListener("mouseout", event => {
        event.target.style.backgroundColor = "hsl(205, 100%, 60%)";
    });
});

// ---------- 添加元素 ----------
const newButton = document.createElement("button"); //STEP 1 
newButton.textContent = "Button 5"; //STEP 2
newButton.classList = "myButtons";
document.body.appendChild(newButton); //STEP 3

buttons = document.querySelectorAll(".myButtons");

// ---------- 删除元素 ----------
buttons.forEach(button => {
    button.addEventListener("click", event => {
        event.target.remove();
        buttons = document.querySelectorAll(".myButtons");
    });
});
```
```html
<body>
    <button class="myButtons">Button 1</button>
    <button class="myButtons">Button 2</button>
    <button class="myButtons">Button 3</button>
    <button class="myButtons">Button 4</button>
  </body>
```
```css
.myButtons {
  font-size: 4rem;
  margin: 10px;
  border: none;
  border-radius: 5px;
  padding: 10px 15px;
  background-color: hsl(205, 100%, 60%);
}
```