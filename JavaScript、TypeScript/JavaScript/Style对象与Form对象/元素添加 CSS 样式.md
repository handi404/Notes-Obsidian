要向元素的 `style` 属性添加已经写好的 CSS 样式，可以使用以下几种方法：

### 方法一：添加已有 CSS 类名
[[classList]]
这是最常用的方法。可以将预先写好的 CSS 样式放在 `.css` 文件中，然后在 JavaScript 中通过 `classList.add()` 方法将对应的类名添加到元素上：

```css
/* style.css */
.highlight {
  background-color: yellow;
  font-size: 20px;
  color: red;
}
```

```html
<!-- HTML -->
<div id="myElement">Hello World</div>
```

```javascript
// JavaScript
let element = document.getElementById("myElement");
element.classList.add("highlight"); // 添加样式类
```

这样就可以将 `.highlight` 样式应用到元素上，而不需要直接操作 `style` 属性。

---

### 方法二：使用 `className` 替换类名

这种方法用于替换元素的 `className`。但是需要注意的是，使用这种方法会替换掉所有其他的类名，因此只适用于不需要保留其他类名的情况：

```javascript
element.className = "highlight"; // 替换原有的类名
```

如果要保留其他类名，可以先获取原有的类名，再添加新的类：

```javascript
element.className += " highlight"; // 在已有类名基础上添加
```

---

### 方法三：动态添加 `style` 标签

可以使用 JavaScript 创建一个 `style` 标签，将预定义的样式插入到页面中，并给元素应用样式类：

```javascript
// 创建 <style> 标签
let style = document.createElement("style");
style.innerHTML = `
  .highlight {
    background-color: yellow;
    font-size: 20px;
    color: red;
  }
`;

// 将 <style> 标签插入 <head>
document.head.appendChild(style);

// 应用样式类
element.classList.add("highlight");
```

---

### 方法四：直接设置 `style.cssText`（仅适用于少量样式）

可以直接将 CSS 样式作为字符串赋给 `style.cssText` 属性：

```javascript
element.style.cssText = "background-color: yellow; font-size: 20px; color: red;";
```

---

### 方法五：使用 `setAttribute` 方法

可以使用 `setAttribute` 方法给 `style` 属性赋值：

```javascript
element.setAttribute("style", "background-color: yellow; font-size: 20px; color: red;");
```

---

### 方法总结

- **推荐使用类名**（方法一），这样可以清晰地将样式分离，并且易于维护。
- **直接修改 `style` 属性**（方法四和五）适用于需要动态设置样式且样式较少的情况。
- **动态添加 `style` 标签**（方法三）适用于需要动态定义多样式的情况。

选择方法时根据需求和样式量的多少进行判断。