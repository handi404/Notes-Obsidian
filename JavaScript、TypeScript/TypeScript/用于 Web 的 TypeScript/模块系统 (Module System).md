TypeScript 中的 **模块系统 (Module System)**。

在 JavaScript (以及 TypeScript) 中，**模块** 是组织代码的基本单元。随着应用程序变得越来越复杂，将所有代码都放在一个巨大的文件中是不可行且难以维护的。模块系统允许你：

1.  **组织代码:** 将相关的代码（函数、类、接口、变量等）划分到不同的文件中。
2.  **封装:** 默认情况下，模块内的代码作用域是局部的，不会污染全局命名空间。只有显式 **导出 (export)** 的成员才能被其他模块访问。
3.  **重用:** 可以轻松地在不同地方 **导入 (import)** 和使用其他模块导出的功能。
4.  **依赖管理:** 清晰地声明一个模块依赖哪些其他模块。

**TypeScript 采用标准的 ECMAScript 模块 (ESM) 语法**

这是最重要的一点。TypeScript 完全拥抱并推荐使用 JavaScript 的标准模块语法（从 ES6/ES2015 开始引入）。如果你熟悉现代 JavaScript 的 `import` 和 `export`，那么你在 TypeScript 中就已经掌握了核心概念。

**核心语法:**

**1. 导出 (`export`)**

在一个模块（`.ts` 文件）中，你可以使用 `export` 关键字将变量、函数、类或接口等公开给其他模块使用。

*   **命名导出 (Named Exports):** 可以导出多个成员，导入时需要使用确切的名称。

    ```typescript
    // --- utils.ts ---
    export const PI = 3.14159;

    export function calculateCircumference(radius: number): number {
      return 2 * PI * radius;
    }

    export interface Circle {
      radius: number;
      color?: string;
    }

    const internalSecret = "shhh"; // 没有 export，外部无法访问
    ```

*   **默认导出 (Default Export):** 每个模块**最多**可以有一个默认导出。导入时可以为其指定任意名称。通常用于导出模块的主要功能，如一个类或一个主函数。

    ```typescript
    // --- Greeter.ts ---
    export default class Greeter {
      greeting: string;
      constructor(message: string) {
        this.greeting = message;
      }
      greet() {
        return "Hello, " + this.greeting;
      }
    }

    // 也可以导出非 class 的内容
    // export default function greetUser(name: string) { ... }
    // export default { configValue: 42 };
    ```

**2. 导入 (`import`)**

在一个模块中，使用 `import` 关键字来引入其他模块导出的成员。

*   **导入命名导出:** 使用花括号 `{}` 包裹要导入的成员名称，名称必须与导出的名称匹配。可以导入多个，也可以使用 `as` 重命名。

    ```typescript
    // --- main.ts ---
    import { PI, calculateCircumference, Circle as CircleShape } from './utils';
    //       ^^^^^^   ^^^^^^^^^^^^^^^^^^^^^   ^^^^^^^^^^^^^^^^^^ 使用 as 重命名

    console.log(PI);
    const radius = 5;
    const circumference = calculateCircumference(radius);
    console.log(`Circumference: ${circumference}`);

    const myCircle: CircleShape = { radius: 10, color: "red" };
    console.log(myCircle);
    ```

*   **导入默认导出:** 直接写一个名称（无需花括号），这个名称可以是你自己定义的。

    ```typescript
    // --- main.ts ---
    // 假设 Greeter.ts 和 utils.ts 在同一目录下
    import MyGreeter from './Greeter'; // MyGreeter 是自定义的名称，对应 Greeter.ts 的默认导出

    const greeterInstance = new MyGreeter("world");
    console.log(greeterInstance.greet()); // 输出: Hello, world
    ```

*   **混合导入:** 同时导入默认导出和命名导出。

    ```typescript
    // 假设 module.ts 同时有默认导出和命名导出
    import DefaultMember, { namedMember1, namedMember2 as renamed } from './module';
    ```

*   **命名空间导入 (Namespace Import):** 将模块所有**命名导出**的成员导入到一个对象（命名空间）下。

    ```typescript
    // --- main.ts ---
    import * as MathUtils from './utils'; // 将 utils.ts 的所有命名导出放入 MathUtils 对象

    console.log(MathUtils.PI);
    const circ = MathUtils.calculateCircumference(3);
    const myCircle2: MathUtils.Circle = { radius: 3 };
    ```

*   **仅导入副作用 (Side Effect Import):** 有时你只想执行一个模块的代码（比如它会修改全局状态或注册一些东西），而不需要导入任何具体成员。

    ```typescript
    import './setup-global-config'; // 执行该模块的代码，但不引入任何变量
    ```

**3. 文件即模块**

在 TypeScript (和 ES Modules) 中，任何包含**顶级 `import` 或 `export` 语句**的 `.ts` 文件都被视为一个**模块**。它的顶层作用域是模块作用域，而不是全局作用域。

如果一个 `.ts` 文件没有任何顶级的 `import` 或 `export`，那么它的内容会被视为在**全局作用域**中（这在现代开发中应尽量避免）。

**4. 编译和模块解析**

*   **编译:** TypeScript 编译器 (`tsc`) 会读取你的 `.ts` 文件，理解 `import` 和 `export` 语句，然后根据 `tsconfig.json` 中的 `module` 设置，将它们编译成目标 JavaScript 环境能够理解的模块格式（如 `ESNext`, `ES2020`, `CommonJS`, `UMD`, `AMD` 等）。**对于现代 Web 开发和 Node.js，通常设置为 `ESNext` 或 `ES2020` (输出 ESM) 或 `NodeNext` (Node.js 环境，智能处理 ESM 和 CommonJS)**。
*   **模块解析:** 当你写 `import ... from './utils'` 或 `import ... from 'lodash'` 时，TypeScript 需要知道去哪里找到这个模块对应的文件（`.ts`, `.d.ts`, `.js` 等）。这个过程由 `tsconfig.json` 中的 `moduleResolution` 选项控制。
    *   **`NodeJs` / `Node` (或更新的 `NodeNext`, `Bundler`):** 模拟 Node.js 的解析策略，查找相对路径和 `node_modules` 目录。**这是最常用且推荐的策略**。`NodeNext` 和 `Bundler` 是更现代的选项，能更好地处理 ESM 与 CommonJS 的互操作。
    *   `Classic` (旧，不推荐): 一种较早的、简单的解析策略，不常用。

**5. 类型定义文件 (`.d.ts`) 和模块**

当你使用一个纯 JavaScript 库时，为了获得类型检查，你需要对应的**类型定义文件 (`.d.ts`)**。这些 `.d.ts` 文件也使用 `export` 来声明库导出的类型和值。`@types/` 组织（例如 `@types/lodash`）就是用来存放这些第三方库类型定义的地方。

**6. 动态导入 (`import()`)**

ESM 还支持动态导入，它返回一个 Promise，允许你按需、异步地加载模块。这对于**代码分割 (Code Splitting)** 非常重要，可以优化 Web 应用的初始加载时间。

```typescript
async function loadHeavyComponent() {
  try {
    const { HeavyComponent } = await import('./components/HeavyComponent');
    //                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 动态导入
    const instance = new HeavyComponent();
    instance.render();
  } catch (error) {
    console.error("Failed to load component:", error);
  }
}

// 在需要时调用
button.addEventListener('click', loadHeavyComponent);
```

**7. Type-Only Imports/Exports (TypeScript 特有)**

有时你只需要导入或导出**类型**，而不希望在编译后的 JavaScript 中产生任何代码（避免运行时依赖）。可以使用 `import type` 和 `export type`。

```typescript
// --- types.ts ---
export interface User { id: number; name: string; }
export type Status = 'active' | 'inactive';

// --- main.ts ---
import type { User, Status } from './types'; // 只导入类型

// 这个函数只在类型检查时需要 User 和 Status，运行时不需要
function processUserData(user: User, status: Status): void {
  console.log(`Processing ${user.name} with status ${status}`);
}

// 也可以在常规 import 中指定
// import { type User, type Status, someValue } from './module';
```

**总结:**

TypeScript 的模块系统基于标准的 ECMAScript Modules (ESM)，使用 `import` 和 `export` 语法。这是现代 JavaScript 和 TypeScript 开发的基石，用于组织代码、实现封装和重用、管理依赖。通过 `tsconfig.json` 中的 `module` 和 `moduleResolution` 配置，你可以控制编译输出的模块格式以及 TypeScript 如何查找模块。动态导入 (`import()`) 和类型专用导入 (`import type`) 提供了更高级的功能，分别用于代码分割和优化类型依赖。掌握模块系统对于编写可维护、可扩展的 TypeScript 应用至关重要。