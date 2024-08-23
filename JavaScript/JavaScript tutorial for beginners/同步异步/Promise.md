###### // Promise = 管理异步操作的对象。
###### // 将 Promise 对象包装在 {异步代码} 周围
###### //“我保证返回一个值”
###### // 待处理 -> 已解决或已拒绝
###### // new Promise ((resolve, reject) => {异步代码})

---

```js
// 按顺序做这些杂务

// 1. 遛狗
// 2. 清洁厨房
// 3. 倒掉垃圾
function walkDog(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {

            const dogWalked = false;

            if(dogWalked){
                resolve("You walk the dog 🐶");
            }
            else{
                reject("You DIDN'T walk the dog");
            }
        }, 1500);
    });
}

function cleanKitchen(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            
            const kitchenCleaned = true;

            if(kitchenCleaned){
                resolve("You clean the kitchen 🧹");
            }
            else{
                reject("You DIDN'T clean the kitchen");
            }
        }, 2500);
    });
}

function takeOutTrash(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {

            const trashTakenOut = true;

            if(trashTakenOut){
                resolve("You take out the trash 🚮");
            }
            else{
                reject("You DIDN'T take out the trash");
            }

        }, 500);
    });
}

walkDog().then(value => {console.log(value); return cleanKitchen()})
         .then(value => {console.log(value); return takeOutTrash()})
         .then(value => {console.log(value); console.log("You finished all the chores!")})
         .catch(error => console.error(error));
```

## Promise：JavaScript 中的异步编程利器

### 什么是 Promise？

Promise 是 JavaScript 中一种用于处理异步操作的机制。它代表了一个异步操作的最终结果（可能是 fulfilled 或 rejected），并且允许你关联回调函数来处理这些结果。

### 为什么使用 Promise？

- **更易于理解：** 比传统的回调函数方式更清晰地表达异步操作的流程。
- **链式调用：** 可以通过 then() 方法链式调用多个异步操作。
- **错误处理：** 可以使用 catch() 方法捕获错误。
- **更好的可读性：** 代码结构更清晰，更容易维护。

### Promise 的基本用法

```JavaScript
// 创建一个 Promise
const promise = new Promise((resolve, reject) => {
  // 异步操作
  setTimeout(() => {
    // 成功
    resolve('Success!');
  }, 1000);
  // 失败
  // reject(new Error('Something went wrong'));
});

// 处理结果
promise
  .then(result => {
    console.log(result); // 输出：Success!
  })
  .catch(error => {
    console.error(error);
  });
```

### Promise 的状态

- **Pending：** 初始状态，异步操作尚未完成。
- **Fulfilled：** 异步操作成功完成。
- **Rejected：** 异步操作失败。

### Promise 的方法

- **then(onFulfilled, onRejected)：** 指定成功和失败的回调函数。
- **catch(onRejected)：** 指定失败的回调函数，是 then(null, onRejected) 的简写。
- **finally(onFinally)：** 无论 Promise 是 fulfilled 还是 rejected，都会执行的回调函数。

### Promise 的链式调用

```JavaScript
fetch('https://api.example.com/data')
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error(error));
```

### Promise 的常见用法

- **异步数据获取：** 获取网络数据、读取文件等。
- **定时器：** 模拟异步操作。
- **错误处理：** 捕获异步操作中的错误。

### async/await 与 Promise

- **async/await：** 是基于 Promise 的语法糖，使异步代码看起来更像同步代码。
- **示例：**

```JavaScript
async function fetchData() {
  try {
    const response = await fetch('https://api.example.com/data');
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.erro   1. codedamn.com codedamn.comr(error);
  }
}
```

### 小结

Promise 是 JavaScript 中处理异步操作的重要工具，它提供了一种更优雅、更易于理解的方式来管理异步代码。通过掌握 Promise 的基本概念和用法，你可以编写出更加健壮和可维护的 JavaScript 应用程序。

### 拓展知识

- **Promise.all()：** 并发执行多个 Promise，所有 Promise 都 fulfilled 时才 resolve。
- **Promise.race()：** 并发执行多个 Promise，第一个 fulfilled 或 rejected 的 Promise 决定最终状态。
- **Promise.resolve()：** 创建一个已经 fulfilled 的 Promise。
- **Promise.reject()：** 创建一个已经 rejected 的 Promise。