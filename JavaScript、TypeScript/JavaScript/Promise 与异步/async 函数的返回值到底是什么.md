`async/await` 已经成为处理异步操作的标配，它让我们能够用看似同步的方式书写异步代码，极大地提高了代码的可读性和可维护性。我们每天都在写 `await fetch(...)` 。

但：一个 `async` 函数，它的返回值究竟是什么？

#### 场景一：返回一个非 Promise 值

这是最常见也最容易产生困惑的情况。我们来看一个最简单的 `async` 函数：

```js
async function getNumber() {
  return 42; // 返回一个普通的数字
}

const result = getNumber();
console.log(result);
```

如果你运行这段代码，控制台输出的并不会是 `42` ，而是：

```js
Promise { <pending> }
```

很快，这个 Promise 的状态会变为 `fulfilled` ，并且其值为 `42` 。

**背后发生了什么？**

当 `async` 函数的 `return` 语句返回一个非 Promise 值（如数字、字符串、对象等）时，JavaScript 引擎会自动将其包装在一个 resolved状态的 Promise 中。换句话说，上面的代码在底层等价于：

```js
function getNumber() {
  return Promise.resolve(42);
}
```

这就是为什么直接调用 `getNumber()` 会得到一个 Promise。为了获取到内部的值 `42` ，我们必须使用 `await` 或者 `.then()` 来“解包”：

```js
// 使用 await (必须在另一个 async 函数内)
async function main() {
  const num = await getNumberj();
  console.log(num); // 输出 42
}
main();

// 或者使用 .then()
getNumber().then(num => {
  console.log(num); // 输出 42
});
```
#### 场景二：返回一个 Promise

如果 `async` 函数本身就返回一个 Promise，情况会怎样？JavaScript 引擎会再把它包一层，变成 `Promise<Promise<T>>` 吗？

答案是： **不会。**

`async` 函数足够智能，如果它检测到返回值已经是一个 Promise，它会直接返回这个 Promise，而不会进行额外的包装。

```js
async function fetchUser() {
 // 返回一个显式的 Promise
 return new Promise(resolve => {
    setTimeout(() => {
      resolve({ name: 'Alice' });
    }, 1000);
  });
}

const promise = fetchUser();
console.log(promise); // Promise { <pending> }

promise.then(user => {
 console.log(user); // 1秒后输出: { name: 'Alice' }
});
```

这个行为至关重要，它保证了 `async` 函数的返回值总是一个行为一致的、可 `await` 的对象，避免了不必要的 Promise 嵌套。

#### 场景三：函数内部抛出错误

如果在 `async` 函数内部 `throw` 一个错误，会发生什么？程序会崩溃吗？

不一定。 `async` 函数会将抛出的错误捕获，并将其作为 **一个 rejected 状态的 Promise** 返回。

```js
async function willFail() {
  throw new Error('Something went wrong!');
}

const result = willFail();
console.log(result); // Promise { <pending> } -> 很快变为 -> Promise { <rejected> }
```

这个 rejected Promise 的 reason 就是我们抛出的那个 `Error` 对象。因此，我们可以用标准的 Promise 错误处理方式来捕获它：

```js
// 使用 try...catch 配合 await
async function handleFailure() {
 try {
    await willFail();
  } catch (error) {
    console.error(error.message); // 输出: Something went wrong!
  }
}
handleFailure();

// 或者使用 .catch()
willFail().catch(error => {
 console.error(error.message); // 输出: Something went wrong!
});
```

这种机制将同步代码中的 `try...catch` 错误处理模型，无缝地融入到了异步流程控制中。

#### 场景四：没有 return 语句

如果一个 `async` 函数执行完毕但没有 `return` 语句，它的返回值是什么？

和普通函数一样，没有 `return` 语句的函数会隐式地返回 `undefined` 。根据场景一的规则，这个 `undefined` 会被 `async` 关键字包装成一个 resolved 状态的 Promise。

```js
async function doNothing() {
  const a = 1 + 1;
  // 没有 return
}

doNothing().then(value => {
  console.log(value); // 输出: undefined
});
```

所以，即使函数什么都不返回，它依然遵循“永远返回一个 Promise”的黄金法则，只不过这个 Promise 的 resolved 值是 `undefined` 。

`async/await` 本质上是 Promise 的语法糖。它的设计初衷就是为了让开发者能够以更直观的方式处理异步逻辑。

`async` 的“包装”行为和 `await` 的“解包”行为，两者相辅相成，构成了这套优雅语法糖的核心。