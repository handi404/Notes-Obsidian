在 CSS 中，`initial`、`unset` 和 `revert` 是用于设置属性值的特殊关键字，它们可以帮助我们控制样式属性的初始值、继承行为以及浏览器默认样式。以下是对它们的详细讲解：

---

## **1. `initial`**

### **功能：**

- 将 CSS 属性设置为其默认值（CSS 规范定义的默认值）。
- **不受父元素的样式影响**，即使该属性是可继承的。

### **使用场景：**

当需要重置某个属性的值为其 CSS 规范定义的默认值时使用。

### **适用属性：**

适用于所有 CSS 属性（继承属性和非继承属性）。

### **示例：**

```css
/* 示例 HTML */
<p style="color: red; font-size: 20px;">
  This is a paragraph with styles.
  <span style="color: blue;">This is a span.</span>
</p>

/* CSS */
span {
  color: initial;
}
```

**效果：**

- `span` 元素的 `color` 被重置为 `initial`，即 **黑色（默认文本颜色）**，因为 CSS 规范定义 `color` 的默认值为黑色。

---

## **2. `unset`**

### **功能：**

- 对继承属性（如 `color`、`font-size` 等）：表现为 `inherit`，即继承父元素的值。
- 对非继承属性（如 `margin`、`border` 等）：表现为 `initial`，即使用属性的默认值。

### **使用场景：**

当不确定属性是继承属性还是非继承属性，但希望在不同上下文中进行动态处理时使用。

### **适用属性：**

适用于所有 CSS 属性。

### **示例：**

```css
/* 示例 HTML */
<p style="color: red; font-size: 20px;">
  This is a paragraph with styles.
  <span style="color: blue; margin: 10px;">This is a span.</span>
</p>

/* CSS */
span {
  color: unset;  /* 对 color 继承父元素的 red */
  margin: unset; /* 对 margin 使用 CSS 默认值（0） */
}
```

**效果：**

- `span` 的 `color` 会继承 `p` 的值（`red`）。
- `span` 的 `margin` 被重置为 `initial`，即默认值 `0`。

---

## **3. `revert`**

### **功能：**

- 将属性值重置为 **浏览器默认样式**（即用户代理样式表，通常是浏览器的初始样式）。
- 如果用户样式表（如用户在浏览器中定义的自定义样式）覆盖了默认样式，则会还原为用户样式表定义的值。

### **使用场景：**

当需要撤销开发者定义的样式，恢复到浏览器的默认值或用户样式时使用。

### **适用属性：**

适用于所有 CSS 属性。

### **示例：**

```css
/* 示例 HTML */
<p style="color: red; font-size: 20px;">
  This is a paragraph with styles.
  <span style="color: blue;">This is a span.</span>
</p>

/* CSS */
span {
  color: revert;
}
```

**效果：**

- `span` 的 `color` 会被还原为浏览器默认值（通常是黑色），无论 `p` 的 `color` 是什么。

---

## **关键字对比**

|**关键字**|**继承属性的行为**|**非继承属性的行为**|**作用范围**|
|---|---|---|---|
|**initial**|重置为规范定义的默认值|重置为规范定义的默认值|忽略父元素的样式|
|**unset**|继承父元素的值（表现为 inherit）|使用规范默认值（表现为 initial）|动态适配继承属性和非继承属性|
|**revert**|恢复到浏览器或用户代理样式表|恢复到浏览器或用户代理样式表|受用户代理样式和用户样式表影响|

---

## **示例代码：三者效果对比**

```css
/* 示例 HTML */
<div style="color: red; margin: 10px;">
  <p style="color: blue; margin: 20px;">
    This is a paragraph.
    <span>This is a span.</span>
  </p>
</div>

/* CSS */
span {
  color: initial; /* 规范默认值：黑色 */
  margin: initial; /* 规范默认值：0 */
  
  font-size: unset; /* 继承自父元素 <p> 的 font-size */
  padding: unset; /* 非继承属性：默认值 0 */
  
  border: revert; /* 恢复到浏览器默认样式（无边框） */
}
```

---

## **浏览器支持**

|属性|支持情况|
|---|---|
|`initial`|**全面支持**（主流浏览器均支持）|
|`unset`|**全面支持**（主流浏览器均支持）|
|`revert`|**部分支持**（Chrome 84+、Firefox 97+、Safari 13.1+）|

对于较旧的浏览器，可能需要提供备用解决方案或回退样式。

---

### 总结：

- **`initial`**：将属性值重置为 CSS 规范定义的默认值。
- **`unset`**：动态处理继承和非继承属性。
- **`revert`**：恢复到浏览器或用户代理样式表的默认值。 合理选择这三个关键字，可以更灵活地控制 CSS 样式行为，减少代码冗余，提升样式的可控性和可维护性。