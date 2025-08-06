在 JavaScript 中，`Style` 对象和 `Form` 对象是 DOM 操作中常用的两个对象。下面详细讲解它们的使用。

---

### 一、Style 对象

#### 1.1 对象概述

**① 基本语法**

在 JavaScript 中，可以通过 `element.style` 来访问元素的 `style` 属性，从而修改该元素的内联样式。例如：

```javascript
let element = document.getElementById("myElement");
element.style.color = "red"; // 修改字体颜色
```

**② 样式属性**

`Style` 对象包含多个样式属性，可以直接设置样式，比如 `color`、`backgroundColor`、`width`、`height`、`fontSize` 等。需要注意的是，`JavaScript` 中样式属性的命名是 `camelCase`（驼峰命名法），例如 `background-color` 对应的是 `backgroundColor`。

**③ 注意事项**

- 使用 `style` 只能修改内联样式，不会影响其他样式表（如 `CSS` 文件）中的样式。
- 有些样式需要特殊处理，例如边框样式的简写 `border`，需分别设置 `borderWidth`、`borderStyle` 和 `borderColor`。
> Style属性和CSS一一对应，但从写法上来说有少许区别。对于简单属性(只有一个单词)而言，CSS和JS的语法一致。但对于复合属性(多个单词)，则CSS和JS的语法不太一致。
> CSS复合属性：支持的是`连字符命名法`，例如：font-size
> JS中复合属性： 支持的是`小驼峰命名法`，例如：fontSize

|                  |                     |
| ---------------- | ------------------- |
| CSS语法:(不区分大小写)   | JavaScript语法(区分大小写) |
| border-bottom    | borderBottom        |
| background-color | backgroundColor     |
| list-style       | listStyle           |
| font-size        | fontSize            |
  
#### 1.2 案例-简单样式

**① 修改字体相关样式**

可以通过 `fontSize`、`fontFamily` 和 `color` 等属性设置字体样式：

```javascript
element.style.fontSize = "16px";
element.style.fontFamily = "Arial, sans-serif";
element.style.color = "#333";
```

**② 修改背景相关样式**

使用 `backgroundColor` 和 `backgroundImage` 设置背景样式：

```javascript
element.style.backgroundColor = "#f0f0f0";
element.style.backgroundImage = "url('image.png')";
element.style.backgroundSize = "cover";
```

**③ 修改节点大小**

通过 `width` 和 `height` 设置元素大小：

```javascript
element.style.width = "200px";
element.style.height = "100px";
```

**④ 图片大小的修改**

图片节点的大小可以用 `width` 和 `height` 设置：

```javascript
let img = document.getElementById("myImage");
img.style.width = "150px";
img.style.height = "auto"; // 保持宽高比
```

**⑤ 修改节点位置**

可以使用 `position`、`top`、`left`、`right`、`bottom` 和 `margin` 属性控制元素的位置：

```javascript
element.style.position = "absolute";
element.style.top = "50px";
element.style.left = "100px";
```

---

### 二、Form 对象

`Form` 对象用于处理表单数据，在 JavaScript 中可以通过 `document.forms` 或 `document.getElementById` 获取表单对象。

#### 2.1 Form 对象属性

**① `action`**

`action` 属性定义了表单提交的目标 URL：

```javascript
let form = document.getElementById("myForm");
form.action = "submit.php";
```

**② `enctype`**

`enctype` 属性定义了表单数据的编码类型，常用的是 `application/x-www-form-urlencoded` 和 `multipart/form-data`（用于文件上传）：

```javascript
form.enctype = "multipart/form-data";
```

**③ `method`**

`method` 属性定义表单的请求方式，通常为 `GET` 或 `POST`：

```javascript
form.method = "POST";
```

#### 2.2 Form 对象方法

`Form` 对象包含一些常用方法，比如：

- `submit()`：提交表单；
- `reset()`：重置表单。

例如：

```javascript
form.submit();
form.reset();
```

#### 2.3 Form 对象的应用

可以通过 JavaScript 实现动态表单交互和数据验证，例如，在提交前检查字段是否为空：

```javascript
form.onsubmit = function(event) {
  if (form.elements["username"].value === "") {
    alert("用户名不能为空");
    event.preventDefault(); // 阻止表单提交
  }
};
```

通过 `Form` 对象和 `Style` 对象，可以灵活地控制前端样式和表单操作，实现动态和交互性的网页效果。