`Promise` 对象的所有静态方法。这些方法提供了强大的工具来处理多个 Promise，是编写复杂异步逻辑的关键。

---

### 什么是 Promise 的静态方法？
静态方法是直接在 `Promise` 构造函数上调用的，而不是在 Promise 实例上。例如，`Promise.all()` 而不是 `myPromise.all()`。它们通常用于组合或管理多个 Promise。

---

### 1. `Promise.all(iterable)`

#### 核心概念
`Promise.all` 接收一个可迭代对象（通常是一个 Promise 数组），并返回一个**新的 Promise**。这个新的 Promise 会在以下情况之一发生时改变状态：

1.  **成功 (Fulfilled):** 当 `iterable` 中**所有**的 Promise 都成功时，新的 Promise 才会成功。它的成功值是一个数组，包含了 `iterable` 中每个 Promise 的成功值，且**顺序与原始数组保持一致**。
2.  **失败 (Rejected):** 只要 `iterable` 中有**任何一个** Promise 失败，新的 Promise 就会**立即**失败，并且其失败原因就是那个第一个失败的 Promise 的原因。

**简单记：** “一荣俱荣，一损俱损”。

#### 语法与示例
```javascript
const p1 = Promise.resolve(3);
const p2 = 42; // 非 Promise 值会被当作已成功的 Promise 处理
const p3 = new Promise((resolve, reject) => {
  setTimeout(resolve, 100, 'foo');
});

// 场景 1: 全部成功
Promise.all([p1, p2, p3]).then((values) => {
  console.log(values); // 输出: [3, 42, "foo"]
}).catch(err => {
  console.error(err);
});


const p4 = Promise.reject('Error occurred');

// 场景 2: 有一个失败
Promise.all([p1, p3, p4]).then((values) => {
  // 这部分代码不会执行
  console.log(values);
}).catch(err => {
  console.error(err); // 输出: Error occurred
});
```

#### 应用
*   **并行执行多个独立任务：** 当你需要同时发起多个网络请求，并且只有在所有请求都成功后才进行下一步操作时，`Promise.all` 是完美的选择。例如，加载页面所需的多个资源（用户信息、文章列表、配置信息）。

---

### 2. `Promise.race(iterable)`

#### 核心概念
`Promise.race` 也接收一个 Promise 数组，并返回一个**新的 Promise**。这个新 Promise 的状态会跟随 `iterable` 中**第一个** settled（即成功或失败）的 Promise 的状态。

**简单记：** “谁快，就要谁的结果，不论成败”。

#### 语法与示例
```javascript
const p1 = new Promise((resolve) => setTimeout(resolve, 500, 'one'));
const p2 = new Promise((resolve, reject) => setTimeout(reject, 100, new Error('two')));

// p2 会在 100ms 后失败，比 p1 的 500ms 快
Promise.race([p1, p2]).then((value) => {
  // 不会执行
  console.log(value);
}).catch((err) => {
  console.error(err.message); // 输出: two
});
```

#### 应用
*   **请求超时处理：** 一个常见的应用场景是为一个网络请求设置超时。你可以让请求 Promise 和一个定时器 Promise (e.g., `setTimeout` that rejects) 进行 `race`。
    ```javascript
    function fetchWithTimeout(url, timeout) {
      const fetchPromise = fetch(url);
      const timeoutPromise = new Promise((_, reject) =>
        setTimeout(() => reject(new Error('Request timed out')), timeout)
      );
      return Promise.race([fetchPromise, timeoutPromise]);
    }

    fetchWithTimeout('https://slow-api.com', 2000)
      .then(res => res.json())
      .catch(err => console.error(err.message)); // 如果 2 秒内没返回，就会输出 'Request timed out'
    ```

---

### 3. `Promise.allSettled(iterable)` (ES 2020)

#### 核心概念
`Promise.allSettled` 是对 `Promise.all` 的一个重要补充。它也接收一个 Promise 数组，但它会**等待所有**的 Promise 都 settled（无论是成功还是失败），然后返回一个**永远成功 (fulfilled)** 的新 Promise。

这个新 Promise 的成功值是一个对象数组，每个对象都描述了原始 Promise 的最终状态。每个结果对象都有以下两种形态之一：

*   `{ status: 'fulfilled', value: <成功值> }`
*   `{ status: 'rejected', reason: <失败原因> }`

**简单记：** “不论成败，我都要等所有人，并给我一份详细的结果报告”。

#### 语法与示例
```javascript
const p1 = Promise.resolve(3);
const p2 = new Promise((resolve, reject) => setTimeout(reject, 100, 'error'));

Promise.allSettled([p1, p2]).then((results) => {
  console.log(results);
  /*
  输出:
  [
    { status: 'fulfilled', value: 3 },
    { status: 'rejected', reason: 'error' }
  ]
  */
  
  // 可以方便地处理成功和失败的 cases
  const successfulPromises = results.filter(r => r.status === 'fulfilled');
  const failedPromises = results.filter(r => r.status === 'rejected');
  
  console.log('Successful values:', successfulPromises.map(r => r.value));
});
```

#### 应用
*   当你需要执行多个**互不依赖**的异步任务，并且你关心**每个任务的最终结果**，而不是只要有一个失败就中断整个操作时，`allSettled` 是最佳选择。例如，同时上传多个文件，你想知道哪些成功了，哪些失败了。

---

### 4. `Promise.any(iterable)` (ES 2021)

#### 核心概念
`Promise.any` 是 `Promise.all` 的镜像。它接收一个 Promise 数组，并返回一个**新的 Promise**。

1.  **成功 (Fulfilled):** 只要 `iterable` 中有**任何一个** Promise 成功，新的 Promise 就会**立即**成功，并且其成功值就是那个第一个成功的 Promise 的值。
2.  **失败 (Rejected):** 只有当 `iterable` 中**所有**的 Promise 都失败时，新的 Promise 才会失败。它的失败原因是一个 `AggregateError` 对象，这个对象有一个 `errors` 属性，包含了所有 Promise 的失败原因。

**简单记：** “只要有一个成功就行，全都失败了我才认输”。

#### 语法与示例
```javascript
const p1 = Promise.reject('Error A');
const p2 = new Promise((resolve) => setTimeout(resolve, 100, 'Success B'));
const p3 = new Promise((resolve) => setTimeout(resolve, 200, 'Success C'));

// 场景 1: 有成功的 Promise
Promise.any([p1, p2, p3]).then((value) => {
  console.log(value); // 输出: Success B (因为 p2 是第一个成功的)
}).catch(err => {
  // 不会执行
  console.error(err);
});

const p4 = Promise.reject('Error D');

// 场景 2: 全部失败
Promise.any([p1, p4]).then(value => {
  // 不会执行
}).catch(err => {
  console.log(err instanceof AggregateError); // true
  console.log(err.message); // "All promises were rejected"
  console.log(err.errors);  // ['Error A', 'Error D']
});
```

#### 应用
*   当你需要从多个来源获取同一个资源（例如，多个 CDN 服务器上的同一个文件），并且只要有一个来源成功返回，你就可以继续执行时，`Promise.any` 非常有用。

---

### 5. `Promise.resolve(value)` & `Promise.reject(reason)`

#### 核心概念
这两个是辅助方法，用于快速创建一个已确定状态的 Promise。

*   **`Promise.resolve(value)`:** 创建一个立即进入 `fulfilled` 状态的 Promise，并将 `value` 作为其成功值。
    *   **特殊行为：** 如果传递给 `Promise.resolve()` 的 `value` 本身就是一个 Promise，那么该方法会直接返回这个 Promise，而不是创建一个新的。
*   **`Promise.reject(reason)`:** 创建一个立即进入 `rejected` 状态的 Promise，并将 `reason`（通常是一个 `Error` 对象）作为其失败原因。

#### 语法与示例
```javascript
// Resolve
Promise.resolve('Success!').then(val => console.log(val));

// Reject
Promise.reject(new Error('Failure!')).catch(err => console.error(err.message));

// `resolve` 的特殊行为
const originalPromise = new Promise(res => setTimeout(res, 100));
const newPromise = Promise.resolve(originalPromise);
console.log(newPromise === originalPromise); // true
```

#### 应用
*   在 `async` 函数中，`return value` 实际上就等同于 `return Promise.resolve(value)`。
*   在编写测试或需要将同步值/函数包装成符合 Promise API 的场景中非常有用。

---

### 总结对比

| 方法                         | 触发条件        | 成功值                             | 失败原因                      | 核心用途                       |
| :------------------------- | :---------- | :------------------------------ | :------------------------ | :------------------------- |
| **`Promise.all()`**        | 所有都成功       | `[val1, val2, ...]`             | 第一个失败的原因                  | 所有任务都必须成功 (All or nothing) |
| **`Promise.race()`**       | 第一个 settled | 第一个成功的值                         | 第一个失败的原因                  | 竞速，谁快用谁 (Race)             |
| **`Promise.allSettled()`** | 所有都 settled | `[{status, value/reason}, ...]` | (永远不会失败)                  | 获取所有任务的最终状态 (Inspection)   |
| **`Promise.any()`**        | 任何一个成功      | 第一个成功的值                         | `AggregateError` (所有都失败时) | 只要有一个成功即可 (First success)  |
| **`Promise.resolve()`**    | -           | 传入的值                            | -                         | 创建一个已成功的 Promise           |
| **`Promise.reject()`**     | -           | -                               | 传入的原因                     | 创建一个已失败的 Promise           |
