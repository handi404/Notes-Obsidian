在现代前端开发中，Promise 和 `async/await` 已经成为处理异步操作的基石。然而，一个常见的棘手问题是： **如何取消一个已经开始的 Promise？**

比如，用户发起一个数据请求，但在请求完成前又导航到了其他页面；或者用户在一个搜索框中快速输入，我们需要取消前一次的搜索请求，只保留最后一次。在这些场景下，取消一个进行中的 Promise 就显得至关重要。

### 核心问题：为什么 Promise 本身不可取消？

首先，我们需要理解 Promise 的核心设计理念。一个 Promise 代表一个异步操作的 **最终结果** 。它的状态一旦从 `pending` （进行中）变为 `fulfilled` （已成功）或 `rejected` （已失败），就永远不会再改变。

**Promise 本身不提供取消机制，原因如下：**

1. **状态不可逆** ：这是 Promise 的核心规范。一旦状态改变，就形成了一个确定的、不可变的结果。
2. **单一责任** ：Promise 的职责是传递价值和状态，而不是控制异步操作本身的执行流程。发起异步操作的函数（如 `fetch` ）才是执行者。

打个比方：你寄出了一封信（发起了一个 Promise），你不能在信件投递过程中把它神奇地从邮政系统里撤回来。你能做的，是在信件送达时（Promise 完成时），选择 **忽略它** 。

我们的目标，就是实现这种“忽略”机制，并尽可能地通知底层的异步操作停止工作，以节省资源。

### AbortController

`AbortController` 是目前实现 Promise 取消的 **最佳实践和标准方案** 。它最初是为取消 `fetch` 请求而设计的，但其通用性使其可以与任何异步操作集成。

**`AbortController` 的工作方式** ：

1. 创建一个 `AbortController` 实例。
2. `controller.signal` ：这是一个 `AbortSignal` 对象，可以传递给需要支持取消的异步函数（如 `fetch` ）。
3. `controller.abort()` ：调用此方法来发出“中止”信号。
4. 当 `abort()` 被调用时， `signal` 会通知所有监听它的异步操作。对于 `fetch` 来说，它会自动中止网络请求并让 Promise reject 一个名为 `AbortError` 的错误。

#### 1\. 与 fetch 配合使用

这是 `AbortController` 最常见的用法。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAMdwee30vUSm8nnb7HbwRCjD9ia2HTmhFTb9WGicNHXUjGzJ48A2IpvcshdK8lzNJHTOibFg1xia0jTQg/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

```js

```

在 `async/await` 语法中同样清晰：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAMdwee30vUSm8nnb7HbwRCjPkovY6xsxV77lVFZaQ9iciazxQicHheLDYAWJfF5IyGJ3wFue7Wbrr3pQ/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

#### 2\. 在自定义 Promise 中使用 AbortController

你也可以让你自己的异步函数支持 `AbortSignal` 。

**原理** ：

- 你的函数需要接收 `signal` 作为参数。
- 在异步操作的关键节点，检查 `signal.aborted` 属性。如果为 `true` ，则提前退出。
- 使用 `signal.addEventListener('abort', ...)` 来注册清理逻辑（如清除定时器）。
![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAMdwee30vUSm8nnb7HbwRCjJJXEdc7x5jYhsres3s90S5iclWNictOXmLibquibnq3kicLLvTWicpXr8GPw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

**优点** ：

- **官方标准** ：是 W3C 和 WHATWG 定义的标准 API。
- **真正中止底层操作** ： `fetch` 会中止网络连接，自定义函数也可以通过它来清理资源（如清除定时器），避免了不必要的浪费。
- **语义清晰** ：通过专门的 `AbortError` 来区分“取消”和“其他错误”，代码更健壮。
- **组合性强** ：一个 `AbortSignal` 可以传递给多个 Promise，实现批量取消。

虽然 Promise 本身的核心设计使其不可变，但通过 `AbortController` 这一强大的模式，我们已经可以非常有效地控制和终止异步流程，编写出更健壮、更高效的应用程序。
