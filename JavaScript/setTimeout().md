###### // setTimeout () = JavaScript 中的函数，用于安排
###### // 在一定时间后执行函数 
###### // 时间是近似值
###### // setTimeout (callback, delay)

---

```js
// ---------- EXAMPLE 1 ----------
function hello() {
    window.alert("Hello");
}
 
setTimeout(hello, 3000);

// ---------- EXAMPLE 2 ----------
// clearTimeout() = 可以在触发之前取消超时

const timeoutId = setTimeout(() => window.alert("Hello"), 3000);

clearTimeout(timeoutId);

```


```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <button onclick="startTimer()">START</button>
    <button onclick="clearTimer()">CLEAR</button>
    <script src="index.js"></script>
</body>
</html>
```
```js
// ---------- EXAMPLE 3 ----------

let timeoutId;

function showAlert() {
    window.alert("Hello");
}

function startTimer() {
    timeoutId = setTimeout(showAlert, 3000);
    console.log("STARTED");
}

function clearTime() {
    clearTimeout(timeoutId);
    console.log("CLEARED");
}
```