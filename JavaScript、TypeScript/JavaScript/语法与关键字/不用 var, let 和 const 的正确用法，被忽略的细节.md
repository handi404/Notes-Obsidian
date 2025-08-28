目前， `let`  和  `const`  关键字已经取代了传统的  `var` ，带来了更合理的作用域规则和更严格的使用限制。然而，即使是有经验的开发者，也会忽略一些微妙的细节。

## var 的问题：为什么不要用它

在深入了解 `let`  和  `const`  之前，有必要先理解为什么我们不要用  `var` ：

1. 函数作用域而非块级作用域
	```
	if (true) {
	  var x = 10;
	}
	console.log(x); // 10，变量 x 泄露到外部作用域
	```
2. 变量提升（Hoisting）带来的困惑
	```
	console.log(x); // undefined，而非报错
	var x = 5;
	```
3. 允许重复声明
	```
	var user = "张三";
	var user = "李四"; // 不报错，静默覆盖
	```
4. 全局声明成为全局对象的属性
	```
	var global = "我是全局变量";
	console.log(window.global); // "我是全局变量"（浏览器环境）
	```

这些特性导致了许多难以追踪的 bug，尤其在大型应用程序中。

## let 的核心特性：被忽略的细节

### 1\. 暂时性死区（Temporal Dead Zone）

这可能是 `let` 最容易被忽略的特性：

```
console.log(x); // ReferenceError: x is not defined
let x = 5;
```

与 `var`  不同， `let` 声明的变量存在"暂时性死区"（TDZ）。从块作用域开始到变量声明之前，该变量都是不可访问的。这并非简单的"不提升"，而是一种更精细的机制。

```
let x = 10;
function example() {
  // 从这里开始，x 进入 TDZ
  console.log(x); // ReferenceError
  
  let x = 20; // 这里 x 离开 TDZ
}
```

被忽略的细节 ：即使外部作用域已有同名变量，内部作用域的暂时性死区仍然会阻止访问。

### 2\. 真正的块级作用域

`let` 声明的变量严格遵循块级作用域规则，这点经常被低估：

```
for (let i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：0, 1, 2

for (var i = 0; i < 3; i++) {
  setTimeout(() => console.log(i), 100);
}
// 输出：3, 3, 3
```

被忽略的细节 ：每次循环迭代， `let` 声明都会创建一个新的变量实例，这在处理闭包时尤为重要。

### 3\. 不污染全局对象

虽然知道 `let` 不会成为全局对象的属性，但有一个细节常被忽略：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcZd48PRa9YcZ45w2Vw1djFk8Holfx0LJ04EbaRiaia7sLJVWiaMicqia6s1Q/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

被忽略的细节 ：全局 `let` 变量存储在称为"脚本作用域"的特殊环境中，而非全局对象上。

## const 的核心特性：被误解的不变性

### 1\. 对象和数组的可变性

许多开发者误以为 `const` 声明的对象或数组是完全不可变的：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcT3pg2wNwIzicJoHHCWSm5ozLCAvZDMfWePcgBYmQJFkgjC5b0votKWw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

被忽略的细节 ： `const`  只保证引用不变，而非内容不变。要创建不可变对象，需要使用  `Object.freeze()` ：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcC6RzBDGouwOnVmwRloDybb0ic8YjJkTI0t54zJw0YaoxDxKmSRyX7AQ/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

但要注意， `Object.freeze()` 只是浅冻结：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcxLiaT8zNlO25nkoEdsOCzCGe8Y63BBxsfNq3TlRHNOj7yPrxQHMicueg/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

深度冻结 需要递归应用 `Object.freeze()` 。

### 2\. 声明时必须初始化

这似乎是显而易见的，但容易被忽略的是初始化的时机：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qchcUOxlcFLVw0FdZ982qet6yib2mZBDrLCd0Wcu0l4l54uGPCatEOujA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

被忽略的细节 ： `const` 声明的变量不能被赋予新值，但这并不意味着它的内容不可变。

### 3\. 性能考虑

一个常被忽略的事实是，在某些 JavaScript 引擎中， `const` 声明可能会有轻微的性能优势：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcibibtdoXQURic1H46a8bvv6AXAOx1bk4eDzJogaY01UgGrEiaiaibJnLiae0g/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

引擎可以确保这些值永远不会改变，从而可能进行常量折叠等优化。

## 实用的使用模式和最佳实践

### 1\. 默认使用 const，必要时退回到 let

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcqMhNUSEVLIQ18vaLGOicuzq14Btfk7CJp9uF0HicL8NcbtsaLBLGA8GA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

这种方式可以最大限度减少代码中的变量重新赋值，提高可读性和可维护性。

### 2\. 解构赋值中的 let 和 const

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcicAGcd1Qk9u1GEASjHFVvsj9HYhbvEmRa25ZCsr3VpZK1Xlicia6tEdnA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

被忽略的细节 ：函数参数解构本质上是 `const` 声明，不能重新赋值。

### 3\. 循环中的 let vs const

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAP3ic0hOIz5s5qcDcX1ibT2qcX2o75yuEm7RHRjhwf1HtZ5nbsbDYEhDDEbiaK7jbO7alpwkJw7R9hxg/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

被忽略的细节 ： `for-of`  和  `for-in`  循环中使用  `const` 是合法的，因为每次迭代都会创建新的绑定。

## 深入理解：let、const 的内部工作机制

理解 JavaScript 引擎如何处理这些声明，有助于避免常见陷阱：

```
// 简化的内部处理流程
function example() {
  // 1. 创建词法环境
  // 2. 对 let/const 声明进行"未初始化"标记（TDZ 开始）
  
  // console.log(x); // 如果取消注释，会报错：x 在 TDZ 中
  
  let x = 10; // x 从 TDZ 中解除，并赋值为 10
  
  if (true) {
    // 创建新的块级词法环境
    const y = 20;
    x = 30; // 可以访问外部 x
    // y 仅在此块中可用
  }
  
  // console.log(y); // 如果取消注释，会报错：y 不在此作用域
}
```

词法环境（Lexical Environment）和变量环境（Variable Environment）的区别是 JavaScript 引擎如何区分处理不同类型的声明。
