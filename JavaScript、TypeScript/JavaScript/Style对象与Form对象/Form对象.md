以下是将 `Form` 对象的属性和方法结合起来的讲解方式。

---

### 二、Form 对象

#### 1. 属性和方法概述

`Form` 对象表示网页中的表单，通过它可以访问和操作表单的各种属性和行为。可以通过以下方式获取表单对象：

```javascript
let form = document.getElementById("myForm");
// 或者
let form = document.forms[0]; // 获取第一个表单
```

#### 2. 常用属性和方法

**① `action`**

- **定义**：指定表单提交的目标 URL。
- **用法**：

```javascript
form.action = "submit.php";
console.log(form.action); // 获取当前的 action 值
```

---

**② `enctype`**

- **定义**：指定表单数据的编码类型，默认是 `application/x-www-form-urlencoded`。
- **常用值**：
  - `application/x-www-form-urlencoded`：默认编码。
  - `multipart/form-data`：文件上传时使用。
  - `text/plain`：纯文本格式。
- **用法**：

```javascript
form.enctype = "multipart/form-data";
console.log(form.enctype); // 查看当前的编码方式
```

---

**③ `method`**

- **定义**：指定表单的请求方式，通常是 `GET` 或 `POST`。
- **用法**：

```javascript
form.method = "POST";
console.log(form.method); // 获取当前的请求方法
```

---

**④ `target`**

- **定义**：指定提交表单的目标窗口。
- **常用值**：
  - `_self`：当前窗口（默认）。
  - `_blank`：新窗口。
  - `_parent`：父窗口。
  - `_top`：顶级窗口。
- **用法**：

```javascript
form.target = "_blank";
console.log(form.target); // 查看当前目标窗口
```

---

**⑤ `submit()` 方法**

- **定义**：以编程方式提交表单，模拟用户点击提交按钮的行为。
- **用法**：

```javascript
form.submit(); // 提交表单
```

---

**⑥ `reset()` 方法**

- **定义**：重置表单所有字段的值为默认值。
- **用法**：

```javascript
form.reset(); // 重置表单
```

---

**⑦ 表单字段的动态操作**

可以通过 `elements` 属性访问表单中的所有字段，并动态操作这些字段：

```javascript
// 遍历所有表单字段
for (let i = 0; i < form.elements.length; i++) {
  console.log(form.elements[i].name + ": " + form.elements[i].value);
}

// 修改某字段值
form.elements["username"].value = "newUser";
```

#### 3. 应用实例

**动态修改表单属性并提交**

```javascript
let form = document.getElementById("myForm");

// 修改属性
form.action = "process.php";
form.method = "POST";
form.enctype = "multipart/form-data";

// 提交表单
form.submit();
```

**表单验证**

在提交表单之前验证字段值是否符合要求：

```javascript
form.onsubmit = function(event) {
  if (form.elements["email"].value === "") {
    alert("邮箱不能为空！");
    event.preventDefault(); // 阻止提交
  }
};
```

通过将属性和方法合并讲解，既可以清晰理解每个功能的作用，也更方便在实际开发中快速应用。