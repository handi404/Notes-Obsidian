
##### 通过向按钮添加事件侦听器来在 HTML 图像的显示和/或可见性之间进行切换

---

```js

Contains => 包含

(method) DOMTokenList.contains(token: string): boolean

如果 token 存在则返回 true，否则返回 false。

const myButton = document.getElementById("myButton");
const myImage = document.getElementById("myImage");

// myButton.addEventListener("click", (event) => {
//   myImage.classList.toggle("change");
//   if (myImage.classList.contains("change")) {
//     myButton.innerHTML = "show";
//   } else {
//     myButton.innerHTML = "hide";
//   }
// });
myButton.addEventListener("click", () => {
  if (myImage.style.visibility === "hidden") {
    myImage.style.visibility = "visible";
    myButton.textContent = "hide";
  } else {
    myImage.style.visibility = "hidden";
    myButton.textContent = "show";
  }
});
```
```html
<body>
    <img id="myImage" src="github.png" alt="" width="300px" /><br />
    <button id="myButton">hide</button>
  </body>
  <script src="index.js"></script>
```
```css
#myButton {
  font-size: 2rem;
}

.change {
  visibility: hidden;
}
```