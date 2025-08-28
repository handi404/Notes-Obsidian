无论是同时请求多个API、并行处理多个文件，还是执行一系列独立的动画，我们都需要一个可靠的机制来管理这些任务。

长久以来， `Promise.all` 一直是我们的首选方案，但它有一个致命的弱点。

### Promise.all

让我们来看一个例子，假设我们需要从三个不同的API获取数据来渲染一个页面仪表盘：

```js
const api1 = fetch('/api/user-info');
const api2 = fetch('/api/dashboard-widgets');
const api3 = fetch('/api/notifications');

Promise.all([api1, api2, api3])
  .then(([userInfo, widgets, notifications]) => {
    // 当三个请求都成功时，我们在这里处理数据
    console.log('所有数据加载成功!');
    renderDashboard(userInfo, widgets, notifications);
  })
  .catch(error => {
    // 只要有一个请求失败，就会立即进入这里
    console.error('加载失败:', error);
    showErrorUI();
  });
```

想象一下，获取用户信息的 `api1` 和获取小组件的 `api2` 都成功了，但获取通知的 `api3` 因为服务器打了个盹而失败了。

整个操作因为一个非核心的通知功能失败而全盘崩溃，这显然不是我们想要的用户体验。

这就是 `Promise.all` 的核心风险：它不关心那些已经成功的 Promise，一旦有失败，就会丢失所有结果。

### Promise.allSettled

为了解决上述问题，ECMAScript 引入了 `Promise.allSettled` ，它的设计哲学与 `Promise.all` 完全不同，它是一个更加宽容和稳健的并发处理器。

`Promise.allSettled` 同样接收一个 Promise 数组，但它的行为是：

1. 其返回的 Promise 永远不会被拒绝，无论是成功还是失败
2. 其返回的 Promise 其解析值是一个对象数组，每个对象都描述了对应 Promise 的最终状态

每个结果对象都有以下两种形态之一：

- `{ status: 'fulfilled', value: <解析值> }`
- `{ status: 'rejected', reason: <拒绝原因> }`

让我们用 `Promise.allSettled` 重写上面的例子：

```js
const api1 = fetch('/api/user-info');
const api2 = fetch('/api/dashboard-widgets');
const api3 = fetch('/api/notifications'); // 假设这个会失败

Promise.allSettled([api1, api2, api3])
  .then(results => {
    // 我们可以安全地处理每一个结果，假设 api3 失败
    if (notificationsResult[2].status === 'rejected') {
      console.warn('通知加载失败:', notificationsResult[2].reason);
      // 即使通知失败，其他部分依然可以正常渲染
      showNotificationFallback();
    }
  });
  // 注意：这里几乎不需要 .catch()，因为它永远是 fulfilled 状态
```

通过 `Promise.allSettled` ，即使 `api3` 失败了，我们依然能够拿到 `api1` 和 `api2` 的成功结果，并分别进行处理，应用的健壮性大大提高。

`Promise.allSettled` 通过提供一种永不失败的承诺聚合方式，让我们能够以一种更精细、更安全的方式来处理并发任务。
