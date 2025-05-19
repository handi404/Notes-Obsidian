**函数签名 (Function Signatures)**。

简单来说，**函数签名** 就像是函数的 **“类型说明书”** 或 **“接口定义”**。它精确地描述了一个函数**应该接受什么样的参数**（类型和数量）以及它**将返回什么类型的值**，但**不包含函数内部的具体实现逻辑**（也就是 `{}` 里的代码）。

想象一下，你要使用一个别人写好的工具（函数），函数签名就是贴在工具箱外面的标签，告诉你：

*   需要放进去什么材料（参数类型）？
*   能得到什么成品（返回值类型）？

你不需要知道工具内部的复杂构造（函数体），只需要看懂标签（签名）就能正确使用它。

**为什么需要函数签名？**

1.  **类型安全 (Type Safety):** 确保你调用函数时传入了正确类型的参数，也确保你能正确地使用函数的返回值。编译器会帮你检查，减少运行时错误。
2.  **代码清晰与可读性 (Clarity & Readability):** 一眼就能看出函数的用途和用法，就像文档一样。
3.  **可重用性 (Reusability):** 可以定义一种函数类型，然后在多个地方强制函数必须符合这个“规格”。特别适用于回调函数或策略模式等场景。
4.  **强制契约 (Enforcing Contracts):** 在团队协作或库设计中，确保函数的实现者遵循预定的接口。

**如何定义函数签名？**

在 TypeScript 中，主要有以下几种方式来定义函数签名：

**1. 使用类型别名 (`type`) - 最常用**

这是定义独立、可重用的函数类型的首选方式。

```typescript
// 定义一个函数签名：接受一个 string 和一个可选的 number，返回 string
type GreetFunction = (name: string, age?: number) => string;

// 实现这个签名的函数
const greet: GreetFunction = (name, age) => {
  if (age !== undefined) {
    return `Hello, ${name}! You are ${age} years old.`;
  } else {
    return `Hello, ${name}!`;
  }
};

// 使用
console.log(greet("Alice")); // 输出: Hello, Alice!
console.log(greet("Bob", 30)); // 输出: Hello, Bob! You are 30 years old.

// --- 另一个例子：处理数字的回调函数 ---
type NumberCallback = (n: number) => void; // 返回 void 表示不关心返回值

function processNumbers(numbers: number[], callback: NumberCallback) {
  numbers.forEach(callback);
}

processNumbers([1, 2, 3], (num) => {
  console.log(`Processing number: ${num * 2}`);
});
```

**关键点：** `(parameters) => ReturnType` 这种箭头函数形式的语法清晰地定义了输入和输出。

**2. 使用接口 (`interface`)**

接口也可以用来定义函数签名，尤其是当这个函数类型可能还需要附加属性时（虽然不太常见，但技术上可行）。

```typescript
interface Calculate {
  (x: number, y: number, operation: 'add' | 'subtract'): number;
  // 可以为这个函数类型添加属性 (较少见)
  // version?: string;
}

const performCalculation: Calculate = (a, b, op) => {
  if (op === 'add') {
    return a + b;
  } else {
    return a - b;
  }
};
// performCalculation.version = '1.0'; // 如果接口定义了属性

console.log(performCalculation(5, 3, 'add'));      // 输出: 8
console.log(performCalculation(10, 4, 'subtract')); // 输出: 6
```

**关键点：** 接口使用 `(parameters): ReturnType` 的语法，注意是冒号 `:` 而不是箭头 `=>`。

**3. 内联签名 (Inline Signature)**

直接在需要函数类型的地方书写签名，不创建单独的类型别名或接口。这在函数参数或对象属性中很常见。

```typescript
function fetchData(url: string, callback: (data: any, error: Error | null) => void) {
  // 模拟异步请求
  setTimeout(() => {
    try {
      // 假设成功获取数据
      const responseData = { message: "Success!" };
      callback(responseData, null);
    } catch (err) {
      // 假设出错
      callback(null, new Error("Failed to fetch"));
    }
  }, 100);
}

// 使用内联签名定义的回调
fetchData("/api/users", (data, error) => {
  if (error) {
    console.error("Error:", error.message);
  } else {
    console.log("Data received:", data);
  }
});

// 对象属性也可以是函数签名
interface ButtonConfig {
    text: string;
    onClick: (event: MouseEvent) => void; // 内联函数签名
}
```

**函数签名的要素：**

*   **参数列表 (Parameter List):**
    *   参数名 (如 `name`, `age`)：虽然签名里的参数名不必与实现中的完全一致，但使用有意义的名称能极大提高可读性。
    *   参数类型 (如 `: string`, `: number`)：这是类型检查的核心。
    *   可选参数 (`?`: 如 `age?: number`)：表示该参数可以不传递。
    *   默认参数 (`= value`: 如 `level: string = 'info'`)：在类型签名中通常不直接体现默认值，但在函数实现中会影响类型推断（变成非可选）。签名主要关注类型。
    *   剩余参数 (`...`: 如 `...args: any[]`)：表示可以接受任意数量的额外参数。
*   **返回值类型 (Return Type):**
    *   紧跟在参数列表后面，使用 `: ReturnType` (接口) 或 `=> ReturnType` (类型别名/箭头函数)。
    *   `void`: 表示函数没有显式返回值，或者返回值不应该被使用。
    *   `never`: 表示函数永远不会正常返回（例如总是抛出异常或无限循环）。
    *   具体类型（如 `: string`, `: Promise<User>` 等）。

**总结:**

函数签名是 TypeScript 强类型系统的基石之一。它让你能够精确地定义函数的“契约”，专注于函数的输入和输出类型，从而：

*   **提升代码的健壮性和可靠性。**
*   **改善开发体验（编辑器智能提示、编译时错误检查）。**
*   **促进代码的模块化和可重用性。**