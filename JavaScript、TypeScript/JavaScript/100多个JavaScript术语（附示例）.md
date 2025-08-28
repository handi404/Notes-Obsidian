## 100多个JavaScript术语（附示例）

#### 保持敏锐，与时俱进，保持领先

JavaScript 早已不再仅仅是浏览器的脚本语言——它已成为现代 Web 开发、后端服务、原生应用、物联网（IoT）甚至人工智能（AI）工具的跳动心脏。每一年，新的特性、概念和最佳实践都在不断涌现。

随着 2025 年的到来，开发者必须牢牢掌握驱动现代 JavaScript 的核心术语。无论你是在求职、扩展代码库，还是在指导初级开发者，这 100 多个 JavaScript 术语都将磨练你的技能，巩固你的基础。

### 📚 目录

- 语法与关键字
- 核心概念
- 函数与作用域
- ES6+ 新特性
- Promise 与异步
- JavaScript 中的面向对象编程 (OOP)
- DOM 与 BOM
- 模块与导入
- 错误处理
- 事件
- 浏览器 API
- 性能与优化
- 测试与调试
- 2025年新增/高级概念
- 总结与后续步骤

### 1\. ⚙️ 语法与关键字 (15个术语)

- **`var`, `let`, `const`** 声明变量； `let` 和 `const` 提供了块级作用域。
	```
	let name = "Alex";
	const age = 30;
	```
- **`typeof`** 返回变量的类型。
	```
	typeof123; // "number"
	```
- **`==` vs `===`** `==` 允许类型转换（非严格相等）， `===` 不允许（严格相等）。
	```
	2 == "2"; // true
	2 === "2"; // false
	```
- **`null` vs `undefined`** `null` ：人为赋值的“空值”。 `undefined` ：已声明但未赋值。
- **`if`, `else`, `switch`, `case`** 用于实现逻辑判断的控制结构。
- **`return`** 退出函数并返回一个值。
- **`break`, `continue`** 控制循环的行为。
- **`for`, `while`, `do...while`** 循环结构。
- **`this`** 指向当前对象的上下文。
- **`in` / `instanceof`** 检查属性是否存在于对象中，或对象是否为某个构造函数的实例。
- **`delete`** 删除对象的属性。
	```
	delete obj.name;
	```
- **`new`** 通过构造函数实例化对象。

### 2\. 🔍 核心概念 (12个术语)

- **Hoisting (提升)** 声明（而非初始化）会被提升到其作用域的顶部。
- **Execution Context (执行上下文)** 代码运行所处的环境。
- **Call Stack (调用栈)** 跟踪函数调用的数据结构。
- **Lexical Scope (词法作用域)** 在代码编写时就已确定的作用域，而非在运行时确定。
- **Block Scope (块级作用域)** `let` 和 `const` 变量在 `{}` 代码块内的作用域。
- **Global Scope (全局作用域)** 在文件中的任何地方都可以访问的作用域。
- **Temporal Dead Zone (暂时性死区)** `let` 和 `const` 变量从声明到初始化之间的时期。
- **Type Coercion (类型转换)** 值的隐式转换。
- **Truthy & Falsy (真值与假值)** 在条件判断中被分别解释为 `true` 或 `false` 的值。
- **Garbage Collection (垃圾回收)** JavaScript 引擎自动清理不再使用的内存。
- **Event Loop (事件循环)** 处理异步代码执行的机制。
- **Callback Queue (回调队列)** 回调函数在当前调用栈清空后等待被执行的地方。

### 3\. 🧠 函数与作用域 (10个术语)

- **First-Class Functions (一等公民函数)** 函数可以像变量一样被对待（赋值、传递、返回）。
- **Higher-Order Functions (高阶函数)** 接收其他函数作为参数或将函数作为返回值的函数。
- **Arrow Functions (箭头函数)** 一种简洁的函数语法。
	```
	const greet = (name) =>\`Hello ${name}\`;
	```
- **`arguments` object (arguments 对象)** 在函数内部可用的、包含所有传入参数的类数组对象。
- **Closures (闭包)** 一个函数可以“记住”并访问其外部词法作用域中的变量，即使该外部函数已经执行完毕。
	```
	functionouter() {
	let count = 0;
	returnfunctioninner() {
	    count++;
	return count;
	  };
	}
	```
- **IIFE (立即调用函数表达式)** 定义后立即执行的函数。
	```
	(function () {
	console.log("run");
	})();
	```
- **Pure Functions (纯函数)** 没有副作用，其返回值仅由其输入决定。
- **Currying (柯里化)** 一种将接受多个参数的函数转变为接受单一参数的函数序列的技术。
	```
	const add = (x) =>(y) => x + y;
	```
- **Recursion (递归)** 函数调用自身的行为。
- **Scope Chain (作用域链)** JavaScript 解析变量时查找变量的路径。

### 4\. 🚀 ES6+ 新特性 (10个术语)

- **Destructuring (解构赋值)**
	```
	const { name, age } = person;
	```
- **Spread/Rest Operator (展开/剩余操作符)**
	```
	const clone = { ...obj };
	functionsum(...args) {}
	```
- **Template Literals (模板字符串)**
	```
	\`Hello, ${name}\`;
	```
- **Default Parameters (默认参数)**
	```
	functiongreet(name = "User") {}
	```
- **Object Property Shorthand (对象属性简写)**
	```
	let name = "Sam";
	let obj = { name };
	```
- **For…of loop (`for...of` 循环)** 用于遍历可迭代对象（如数组）。
- **Symbol** 一种唯一的、不可变的数据类型。
- **Optional Chaining (可选链操作符 `?.`)**
	```
	user?.profile?.email;
	```
- **Nullish Coalescing (空值合并操作符 `??`)**
	```
	let username = input ?? "Guest";
	```
- **Logical Assignment (逻辑赋值操作符)**
	```
	x ||= 10; // 等价于 x = x || 10;
	```

### 5\. ⏱ Promise 与异步 (8个术语)

- **Promise** 一个表示异步操作最终完成或失败的对象。
	```
	newPromise((resolve, reject) => {});
	```
- **`.then()` / `.catch()` / `.finally()`** 用于处理 Promise 状态变化的方法。
- **`async / await`** 以同步方式编写异步代码的语法糖。
	```
	asyncfunctionload() {
	const data = await fetch(...);
	}
	```
- **Microtasks (微任务)** Promise 的回调函数所在的队列，优先级高于宏任务（如 `setTimeout` ）。
- **Callback Hell (回调地狱)** 深度嵌套的回调函数，导致代码难以阅读和维护。
- **Fetch API** 现代的、基于 Promise 的网络请求 API。
	```
	fetch(url).then(...)
	```
- **AbortController** 用于中止 `fetch` 请求或其他异步任务的控制器。

### 6\. 🧱 JavaScript 中的面向对象编程 (OOP) (7个术语)

- **Class (类)** 创建对象的蓝图或模板。
	```
	classCar{}
	```
- **Constructor (构造函数)** 类中用于初始化对象的特殊方法。
- **`super()`** 调用父类的构造函数。
- **Encapsulation (封装)** 将数据和操作数据的方法捆绑在一起。
- **Inheritance (继承)** 一个类扩展另一个类的特性。
- **Prototypes (原型)** JavaScript 中实现继承的机制，通过原型链实现。
- **Polymorphism (多态)** 一个方法，多种形态（例如通过方法重写实现）。

### 7\. 🌐 DOM 与 BOM (6个术语)

- **DOM (文档对象模型)** HTML 的树状结构表示。
- **`document.querySelector()`** 使用 CSS 选择器来获取 DOM 元素。
- **`innerHTML`, `textContent`** 获取或设置元素的 HTML 内容或纯文本内容。
- **`createElement()`** 动态创建 DOM 节点。
- **`localStorage`, `sessionStorage`** 浏览器提供的本地存储方案。

### 8\. 📦 模块与导入 (4个术语)

- **`import` / `export`** ES6 模块系统中用于导入和导出功能的关键字。
	```
	exportdefaultfunction () {}
	import func from"./file.js";
	```
- **CommonJS (`require`, `module.exports`)** Node.js 使用的模块化规范。
- **Dynamic Imports (动态导入)** 按需加载模块，返回一个 Promise。
	```
	import('module.js').then(...)
	```
- **Tree Shaking** 在打包过程中移除未使用的代码，以减小最终文件体积。

### 9\. 🚨 错误处理 (5个术语)

- **`try`, `catch`, `finally`** 捕获和处理代码块中可能发生的错误。
	```
	try {
	} catch (e) {
	} finally {
	}
	```
- **`throw`** 抛出一个自定义错误。
- **Error Object (错误对象)** 表示错误的内置对象。
	```
	newError("Something went wrong");
	```
- **Custom Errors (自定义错误)** 通过继承 `Error` 类来创建特定的错误类型。
	```
	classAuthErrorextendsError{}
	```
- **Stack Trace (堆栈跟踪)** 错误发生时函数调用的路径记录，用于调试。

### 10\. 🎯 事件 (5个术语)

- **`addEventListener()`** 将事件处理程序附加到 DOM 元素上。
- **Event Bubbling & Capturing (事件冒泡与捕获)** 事件在 DOM 树中传播的两种模式。
- **Event Delegation (事件委托)** 利用事件冒泡，将事件处理器添加到父元素上以管理子元素的事件。
- **`preventDefault()` / `stopPropagation()`** 阻止事件的默认行为 / 停止事件的传播。

### 11\. 📡 浏览器 API (6个术语)

- **Geolocation API (地理位置 API)**
- **Notification API (通知 API)**
- **Web Storage API (Web 存储 API)**
- **Web Workers**
- **WebSockets**
- **MediaDevices (摄像头/麦克风)**

### 12\. ⚡ 性能与优化 (5个术语)

- **Debounce & Throttle (防抖与节流)**
- **Lazy Loading (懒加载)**
- **Virtual DOM (虚拟 DOM)**
- **Code Splitting (代码分割)**
- **Memoization (记忆化)**

### 13\. 🧪 测试与调试 (5个术语)

- **`console.log()`**
- **`debugger`**
- **Unit Testing (单元测试, 如 Jest)**
- **Integration Testing (集成测试)**
- **Test Coverage (测试覆盖率)**

### 14\. 🔬 2025年新增/高级概念 (10个术语)

- **Temporal API** 用于更好地处理日期和时间的 API。
- **Pipeline Operator (`|>` 管道操作符)** 一种链式调用函数的语法提案。
	```
	value |> fn1 |> fn2;
	```
- **Records & Tuples (记录与元组)** 不可变的结构化数据类型（提案阶段）。
- **Top-Level `await` (顶层 `await`)** 在模块的顶层作用域中直接使用 `await` ，无需 `async` 函数包裹。
- **Observable (TC39 提案)** 用于处理异步事件流的响应式编程模式。
- **Decorators (装饰器)** 用于注解和修改类及其属性的语法。
- **Module Attributes (模块属性)** 为 ES 模块提供元数据的提案。
- **`import.meta`** 一个包含模块元数据的对象。
- **WeakRefs & FinalizationRegistry** 弱引用与终结器注册表，用于更精细的内存管理。
- **`Intl.DisplayNames`, `Intl.Segmenter`** 国际化 API 的新成员，分别用于显示名称和文本分段。

### 15\. ✅ 总结与后续步骤

在今天，精通 JavaScript 不仅仅是编写代码——更是要能熟练地运用这门语言。这 100 多个术语能帮助你更快地调试、编写更清晰的代码、在团队中更有效地沟通，并顺利通过技术面试。
