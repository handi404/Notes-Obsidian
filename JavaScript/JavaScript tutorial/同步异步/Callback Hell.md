###### //Callback 地狱 = JavaScript 中回调的情况 
###### // 嵌套在其他回调中
###### // 代码难以阅读的程度。
###### // 处理异步函数的旧模式。
#### // 使用 Promises + async/await 避免回调地狱

---

```js
function task1(callback){
    setTimeout(() => {
        console.log("Task 1 complete");
        callback();
    }, 2000);
}

function task2(callback){
    setTimeout(() => {
        console.log("Task 2 complete");
        callback();
    }, 1000);
}

function task3(callback){
    setTimeout(() => {
        console.log("Task 3 complete");
        callback();
    }, 3000);
}

function task4(callback){
    setTimeout(() => {
        console.log("Task 4 complete");
        callback();
    }, 1500);
}

task1(() => {
    task2(() => {
        task3(() => {
            task4(() => console.log("All tasks completed"));
        })
    });
})
```