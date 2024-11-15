###### // classList = JavaScript 中用于交互的 Element 属性
###### // 带有元素的类列表（CSS 类）
###### // 允许您为许多元素创建可重用的类
###### // 在您的网页上。

#### // add ()     添加（）
#### // remove ()    消除（）
#### // toggle (Remove if present, Add if not) 切换（存在则删除，不存在则添加）
#### // replace (oldClass, newClass)    替换（旧类，新类）
#### // contains ()    包含()

[[js的classList]]

---

```js
// ---------- button ----------

const myButton = document.getElementById("myButton");

myButton.classList.add("enabled");

myButton.addEventListener('mouseover', event => {
    event.target.classList.toggle('hover');
});

myButton.addEventListener('mouseout', event => {
    event.target.classList.toggle('hover');
});

myButton.addEventListener('click', event => {

    if(event.target.classList.contains("disabled")){
        event.target.textContent += "🤬";
    }
    else{
        event.target.classList.replace("enabled", "disabled");
    }
});

// ---------- h1 ----------

const myH1 = document.getElementById("myH1");

myH1.classList.add("enabled");

myH1.addEventListener("mouseover", event => {
    event.target.classList.toggle('hover');
});

myH1.addEventListener('mouseout', event => {
    event.target.classList.toggle('hover');
});

myH1.addEventListener('click', event => {

    if(event.target.classList.contains("disabled")){
        event.target.textContent += "🤬";
    }
    else{
        event.target.classList.replace("enabled", "disabled");
    }
});

// ---------- NodeList ----------
let buttons = document.querySelectorAll(".myButtons");

buttons.forEach(button => {
    button.classList.add("enabled");
});

buttons.forEach(button => {
    button.addEventListener("mouseover", event => {
        event.target.classList.toggle("hover");
    });
});

buttons.forEach(button => {
    button.addEventListener("mouseout", event => {
        event.target.classList.toggle("hover");
    });
});

buttons.forEach(button => {
    button.addEventListener("click", event => {

        if(event.target.classList.contains("disabled")){
            event.target.textContent += "🤬";
        }
        else{
            event.target.classList.replace("enabled", "disabled");
        }
    });
});
```
```html
<body>

    <h1 id="myH1">Hello</h1>
    <button id="myButton">My button</button>

    <button class="myButtons">Button 1</button>
    <button class="myButtons">Button 2</button>
    <button class="myButtons">Button 3</button>
    <button class="myButtons">Button 4</button>

    <script src="index.js"></script>
</body>
```
```css
#myH1{
    font-size: 5rem;
}
#myButton, .myButtons{
    font-size: 4rem;
    margin: 10px;
    border: none;
    border-radius: 5px;
    padding: 10px 15px;
}
.enabled{
    background-color: hsl(204, 100%, 50%);
    color: white;
}
.hover{
    box-shadow: 0 0 10px hsla(0, 0%, 0%, 0.2);
    font-weight: bold;
}
.disabled{
    background-color: hsl(0, 0%, 60%);
    color: hsl(0, 0%, 80%);
}
```