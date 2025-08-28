JavaScript 中的所有关键字 (Keywords) 和保留字 (Reserved Words)。理解它们是编写合法、无歧义代码的基础。

---

### 什么是关键字？
关键字是 JavaScript 语言内部预定义的、有特殊含义的标识符。你**不能**将它们用作变量名、函数名或任何其他标识符。例如，`let let = 5;` 会直接导致语法错误。

---

### 分类讲解

#### 1. 变量声明 (Variable Declaration)

*   `let` (ES 6+): 声明一个**块级作用域**的局部变量。这是现代 JS 中最常用的变量声明方式。
    ```javascript
    if (true) {
      let x = 10;
      console.log(x); // 10
    }
    // console.log(x); // ReferenceError: x is not defined
    ```
*   `const` (ES 6+): 声明一个**块级作用域**的**只读常量**。一旦赋值后，其值（内存地址）不能再改变。对于对象和数组，意味着不能重新赋值，但可以修改其内部内容。
    ```javascript
    const PI = 3.14;
    // PI = 3.1415; // TypeError: Assignment to constant variable.

    const user = { name: 'Alice' };
    user.name = 'Bob'; // 这是允许的
    // user = { name: 'Charlie' }; // 这是不允许的
    ```
*   `var` (传统): 声明一个**函数作用域**或**全局作用域**的变量。存在变量提升 (hoisting) 问题，在现代 JS 中**应避免使用**。
    ```javascript
    if (true) {
      var y = 20;
    }
    console.log(y); // 20 (y 泄露到了块外部)
    ```

#### 2. 控制流 (Control Flow)

*   `if` / `else`: 条件语句，根据表达式的真假执行不同代码块。
*   `switch` / `case` / `break` / `default`: 多分支选择结构。`break` 用于跳出 `switch`，`default` 用于处理所有 `case` 都不匹配的情况。
*   `try` / `catch` / `finally` / `throw`: 错误处理机制。`try` 包含可能出错的代码，`catch` 捕获并处理错误，`finally` 包含无论是否出错都将执行的代码，`throw` 手动抛出一个错误。

#### 3. 循环 (Looping)

*   `for`: 最通用的循环结构，包含初始化、条件判断和迭代表达式。
*   `for...in`: 遍历一个**对象**的**可枚举属性**（包括原型链上的）。通常不推荐用于遍历数组。
    ```javascript
    const obj = { a: 1, b: 2 };
    for (const key in obj) {
      console.log(key); // 'a', 'b'
    }
    ```
*   `for...of` (ES 6+): 遍历**可迭代对象**（如 Array, String, Map, Set）的值。这是遍历数组最推荐的方式。
    ```javascript
    const arr = ['a', 'b'];
    for (const value of arr) {
      console.log(value); // 'a', 'b'
    }
    ```
*   `while`: 当指定条件为真时，循环执行代码块。
*   `do...while`: 先执行一次代码块，然后当指定条件为真时，继续循环。
*   `break`: 立即**终止**整个循环。
*   `continue`: 跳过当前迭代，进入下一次循环。

#### 4. 函数与作用域 (Functions & Scope)

*   `function`: 定义一个函数。
*   `return`: 从函数中返回值，并终止函数执行。
*   `this`: 一个特殊的关键字，它的值在函数被调用时确定，通常指向函数的调用者。箭头函数除外，它会捕获其定义时所在上下文的 `this`。
*   `new`: 用于创建一个用户定义的对象类型的实例或内置对象的实例。
*   `void`: 确保表达式不返回值 (即返回 `undefined`)。

#### 5. 类 (Classes - ES 6+)

*   `class`: 声明一个类，是创建对象的模板。
*   `constructor`: 类中用于创建和初始化对象的特殊方法。
*   `extends`: 用于创建一个类的子类，实现继承。
*   `super`: 用于调用父对象的构造函数或方法。
*   `static`: 定义类本身的静态方法或属性，而不是在实例上。
*   `get`: 定义一个 getter，用于获取特定属性的值。
*   `set`: 定义一个 setter，用于设置特定属性的值。

#### 6. 异步编程 (Asynchronous Programming)

*   `async` (ES2017+): 用于声明一个异步函数，该函数隐式返回一个 `Promise`。
*   `await` (ES2017+): 用于**暂停** `async` 函数的执行，等待一个 `Promise` 被解决 (resolved)，然后恢复执行并获取其结果。`await` 只能在 `async` 函数内部使用。
*   `yield`: 在生成器函数 (`function*`) 中暂停和恢复执行。

#### 7. 模块 (Modules - ES 6+)

*   `import`: 用于从另一个模块导入由 `export` 导出的绑定。
*   `export`: 用于从当前模块中导出函数、对象或原始值，以便其他模块可以通过 `import` 使用它们。
*   `from`: 与 `import` 和 `export` 配合使用，指定模块的来源。
*   `as`: 与 `import` 和 `export` 配合使用，为导入或导出的成员创建别名。

#### 8. 其他

*   `typeof`: 一个一元运算符，返回一个表示操作数类型的字符串。
*   `instanceof`: 运算符，用于测试构造函数的 `prototype` 属性是否存在于对象的原型链上。
*   `in`: 运算符，如果指定的属性在指定的对象或其原型链中，则返回 `true`。
*   `delete`: 运算符，用于删除对象的属性。
*   `debugger`: 调用任何可用的调试功能，如设置断点。
*   `with`: **（已废弃，严禁使用）** 将一个对象的属性扩展到当前作用域链。它会造成严重的性能问题和代码可读性问题，在严格模式下被禁用。

---

### 字面量与特殊值 (Literals & Special Values)
虽然它们不是严格意义上的关键字，但它们是语言的保留部分，不能用作标识符。

*   `null`: 表示一个空值或“无”的对象。
*   `true` / `false`: 布尔类型的两个值。
*   `undefined`: 表示一个变量未被赋值时的值。（注意：`undefined` 并非关键字，它是一个全局属性，但在现代浏览器中是只读的）。

---

### 保留字 (Reserved Words)
这些是为未来版本的 ECMAScript 规范预留的关键字。你现在可能可以用它们作标识符，但为了代码的健壮性和向后兼容性，**强烈建议不要使用**。

#### 始终保留
*   `enum`

#### 在严格模式 (`"use strict";`) 下保留
*   `implements`
*   `interface`
*   `package`
*   `private`
*   `protected`
*   `public`

---

### 总结与最佳实践

| 分类 | 现代推荐/核心关键字 | 传统/需注意 |
| :--- | :--- | :--- |
| **声明** | `let`, `const`, `class` | `var` (避免使用) |
| **循环** | `for...of` (用于可迭代对象) | `for...in` (用于对象属性) |
| **异步** | `async`, `await` | (基于回调或 `.then()` 的 Promise) |
| **模块** | `import`, `export` | (CommonJS `require` / `module.exports`) |
| **函数** | `function`, `return`, `this` | `arguments` (用 `...rest` 替代) |
| **OOP** | `extends`, `super`, `static`, `new` | (基于原型的继承) |
| **调试** | `debugger` | `console.log()` |
| **禁止** | - | `with` |

**核心要点：**
1.  **拥抱 ES6+:** 在新项目中，始终使用 `let` 和 `const` 进行变量声明，它们提供了更可预测的块级作用域。
2.  **模块化:** 使用 `import` / `export` 进行模块化开发，这是现代 JavaScript 应用的基石。
3.  **异步处理:** `async/await` 是处理异步操作（如网络请求）的首选方式，它让异步代码看起来像同步代码一样清晰。
4.  **避免保留字:** 不要使用保留字作为变量名或函数名，以免未来 JavaScript 更新导致你的代码出错。