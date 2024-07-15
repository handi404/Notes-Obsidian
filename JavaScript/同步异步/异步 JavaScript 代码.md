###### // 同步 = 以顺序方式连续执行一行一行 
###### // 等待操作完成的代码。

###### // 异步 = 允许多个操作同时执行而无需等待
###### // 不阻塞执行流程并允许程序继续执行
###### //（I/O 操作、网络请求、获取数据）
###### // 处理方式：Callbacks, Promises, Async/Await

---

```js
// ASYNCHRONOUS异步
function func1(callback){
    setTimeout(() => {console.log("Task 1");
                                    callback()}, 3000);
}

// SYNCHRONOUS同步
function func2(){
    console.log("Task 2");
    console.log("Task 3");
    console.log("Task 4");
}

func1(func2);

```