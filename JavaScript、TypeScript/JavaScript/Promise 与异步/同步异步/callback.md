###### // 回调 = 作为参数传递的函数
###### // 到另一个函数。

###### // 用于处理异步操作：
###### // 1. 读取文件
###### // 2. 网络请求
###### // 3. 与数据库交互

---

```js
hello(goodbye);

function hello(callback){
    console.log("Hello!");
    callback();
}

function goodbye(){
    console.log("Goodbye!");
}
```