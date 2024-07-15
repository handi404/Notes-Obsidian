#### // 元素选择器 = 用于定位和操作 HTML 元素的方法 
#### // 它们允许您选择一个或多个 HTML 元素
#### // 来自 DOM（文档对象模型）

#### // 1. Document.GetElementById ()                // 返回元素或 NULL
#### // 2. Document.GetElementsClassName ()   // HTML 集合
#### // 3. Document.GetElementsByTagName () // HTML 集合
#### // 4. Document.QuerySelector ()                 // 第一个元素或 NULL
#### // 5. Document.QuerySelectorAll ()             // NODELIST

---

```js
// ---------- getElementById() ----------

const myHeading = document.getElementById("my-heading");
myHeading.style.backgroundColor = "yellow";
myHeading.style.textAlign = "center";

// ---------- getElementsByClassName() ----------

const fruits = document.getElementsByClassName("fruits");

Array.from(fruits).forEach(fruit => {	
    fruit.style.backgroundColor = "yellow";
});		//把html集合类型转换为数组

// ---------- getElementsByTagName() ----------

const h4Elements = document.getElementsByTagName("h4");
const liElements = document.getElementsByTagName("li");

Array.from(h4Elements).forEach(h4Element => {
    h4Element.style.backgroundColor = "yellow";
});

Array.from(liElements).forEach(liElement => {
    liElement.style.backgroundColor = "lightgreen";
});

// ---------- querySelector() ----------

const element = document.querySelector("li");

element.style.backgroundColor = "yellow";

// ---------- querySelectorAll() ----------

const foods = document.querySelectorAll("li");

foods.forEach(food => {
    food.style.backgroundColor = "yellow"
});

```

![[屏幕截图 2024-07-13 162527.png]]