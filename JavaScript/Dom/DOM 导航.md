###### // DOM 导航 = 结构导航的过程 
###### // 使用 JavaScript 的 HTML 文档。

#### // .firstElementChild
#### // .lastElementChild
#### // .nextElementSibling
#### // .previousElementSibling
#### // .parentElement
#### // .children

---

```js
// ---------- .firstElementChild ----------

const ulElements = document.querySelectorAll("ul");

ulElements.forEach(ulElement => {
    const firstChild = ulElement.firstElementChild;
    firstChild.style.backgroundColor = "yellow";
});

// ---------- .lastElementChild ----------

const ulElements = document.querySelectorAll("ul");

ulElements.forEach(ulElement => {
    const lastChild = ulElement.lastElementChild;
    lastChild.style.backgroundColor = "yellow";
});

// ---------- .nextElementSibling ----------

const element = document.getElementById("vegetables");
const nextSibling = element.nextElementSibling;
nextSibling.style.backgroundColor = "yellow";

// ---------- .previousElementSibling ----------

const element = document.getElementById("desserts");
const prevSibling = element.previousElementSibling;
prevSibling.style.backgroundColor = "yellow";

// ---------- .parentElement ----------

const element = document.getElementById("ice cream");
const parent = element.parentElement;
parent.style.backgroundColor = "yellow";

// ---------- .children ----------

const element = document.getElementById("fruits");
const children = element.children;

Array.from(children).forEach(child => {
    child.style.backgroundColor = "yellow";
});

```

```html
<body>
    <ul id="fruits">
      <li id="apple">apple</li>
      <li id="orange">orange</li>
      <li id="banana">banana</li>
    </ul>

    <ul id="vegetables">
      <li id="carrots">carrots</li>
      <li id="onions">onions</li>
      <li id="potatoes">potatoes</li>
    </ul>
    
    <ul id="desserts">
      <li id="cake">cake</li>
      <li id="pie">pie</li>
      <li id="ice cream">ice cream</li>
    </ul>

    <script src="index.js"></script>
  </body>
```