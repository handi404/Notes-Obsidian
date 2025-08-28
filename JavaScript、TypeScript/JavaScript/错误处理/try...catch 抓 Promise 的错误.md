在 JavaScript 中， `try...catch` 是我们处理错误的得力助手。我们很自然地认为，只要把可能出错的代码放进 `try` 块， `catch` 就一定能捕获到异常。但当你开始和 Promise 打交道时，可能会遇到一个让你困惑的场景：

```js
try {
 // 假设这是一个会失败的 API 请求
 fetch('https://non-existent-url.com/api'); 
 console.log('请求已发送');
} catch (error) {
 // 这里的 catch 会执行吗？
 console.log('抓到错误了！', error);
}

// 控制台输出:
// 请求已发送
// Uncaught (in promise) TypeError: Failed to fetch
```

咦？ `catch` 块根本没有执行！错误信息直接在控制台炸开了，带着一个扎眼的 `Uncaught (in promise)` 。

这究竟是为什么？难道 `try...catch` 对 Promise 无效吗？

别急，这并非 `try...catch` 的 bug，而是我们对 **同步** 与 **异步** 的理解出了偏差。

### 核心原因：**try...catch 是同步的，而 Promise 是异步的**

让我们用一个更简单的比喻来理解：

你点了一份外卖（ **发起一个 Promise 请求** ）。 `try...catch` 就像你家门口的保安。

1. **你下单的动作是瞬间完成的** 。你按下“支付”按钮，App 立刻告诉你“下单成功，骑手正在路上”。这个“下单成功”的反馈是 **同步** 的。
2. **保安 `try...catch` 只在你下单的那个瞬间盯着你** 。他看到你成功下了单，没出任何问题（比如网络断了、余额不足等），于是他就下班了。
3. **半小时后，骑手送餐路上翻车了** （ **Promise 状态变为 rejected** ）。这个错误发生在未来，发生在保安下班之后。保安自然是抓不到这个“错误”的。

回到代码中：

- `try { ... }` 块里的代码是 **同步执行** 的。
- `fetch(...)` 这个函数被调用时，它 **立即返回** 一个 Promise 对象。在 `try` 块看来，这个返回动作是成功的，没有任何错误被“抛出”（ `throw` ）。
- 所以， `try` 块顺利执行完毕， `catch` 自然不会被触发。
- 真正的网络错误发生在稍后的某个时间点，当这个错误发生时，它改变了那个已经返回的 Promise 对象的状态，将其置为 `rejected` 。这个错误属于 **异步世界** ，而同步的 `try...catch` 早已执行完毕，鞭长莫及。

### 正确的姿势：使用 async/await

那么，如何让保安（ `try...catch` ）等到外卖送到（或出事）再下班呢？答案就是使用 `async/await` 。

`await` 关键字有一个神奇的魔力： **它会“暂停”当前 `async` 函数的执行，直到它等待的 Promise 有了结果（无论是成功 `resolved` 还是失败 `rejected` ）** 。

如果 Promise 失败了， `await` 会像一个“信使”，把这个异步的错误“解包”并 **重新在当前同步上下文中抛出** 。这样一来， `try...catch` 就能稳稳地接住它了。

让我们来改造一下代码：

```js
// 必须在一个 async 函数中使用 await
async function fetchData() {
  try {
    // 使用 await 等待 Promise 的结果
    console.log("准备请求...");
    const response = await fetch('https://example.com/api');
    
    // 如果请求失败，下面这行代码不会执行
    const data = await response.json();
    console.log("请求成功: ", data);
  } catch (error) {
    // 这次，错误被抓住了！
    console.log("在 catch 中抓到错误了！", error);
  }
}

fetchData();

// 控制台输出:
// 准备请求...
// 在 catch 中抓到错误了！ TypeError: Failed to fetch
```

看，这次 `catch` 完美地捕获了错误！

**`async/await` 的工作流程：**

1. 函数用 `async` 标记，表示这是一个异步函数。
2. `await` 守在 `fetch(...)` 前面，函数执行到这里就“暂停”了，但不会阻塞整个程序。
3. 它耐心等待 `fetch` 返回的 Promise 结果。
4. 当 Promise 因为网络问题而 `rejected` 时， `await` 将这个 `rejection` 的原因（也就是那个 `error` 对象）作为一个同步错误 `throw` 出来。
5. 这个被 `throw` 出来的错误，正好在 `try` 块的作用域内，于是被 `catch` 成功捕获。

### 别忘了还有.catch() 方法

当然，处理 Promise 错误并非只有 `async/await` 这一条路。在 `async/await` 出现之前，我们一直使用 Promise 自带的 `.catch()` 方法链式调用来处理错误，这同样非常有效。

```js
fetch('https://non-existent-url.com/api')
  .then(response => {
    if (!response.ok) {
      // 手动抛出一个错误，让下面的 .catch() 捕获
      throw new Error('网络响应不佳');
    }
    return response.json();
  })
  .then(data => {
    console.log('请求成功:', data);
  })
  .catch(error => {
    // 任何在 .then() 链中发生的错误都会在这里被捕获
    console.log('在 .catch() 方法中抓到错误了！', error);
  });
```

这种方式的优点是代码结构清晰，形成了一条“成功路径” (`.then`) 和一条“失败路径” (`.catch`)。
