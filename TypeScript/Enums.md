TypeScript 中的 **枚举 (Enums)**。

**枚举** 是一种让你为一组**数值 (Numeric)** 或 **字符串值 (String)** 定义**友好名称 (Friendly Names)** 的方式。你可以把它看作是创建了一个**命名的常量集合**，特别适合用来表示一组固定的状态、类型、选项或类别。

**核心思想：** 用有意义的名字代替“魔法数字”或“魔法字符串”，提高代码的**可读性**和**可维护性**。

**为什么使用枚举？**

1.  **可读性 (Readability):** `OrderStatus.Shipped` 比 `2` 或 `"SHIPPED"` 更清晰易懂。
2.  **可维护性 (Maintainability):** 如果某个状态对应的具体值需要改变（比如数据库代码变了），你只需要修改枚举定义的地方，而不需要查找和替换代码中所有用到该值的地方。
3.  **类型安全 (Type Safety):** TypeScript 会将枚举视为一个独特的类型。你不能随便把一个普通的 `number` 或 `string` 赋值给一个期望枚举类型的变量（除非显式转换或类型断言），这有助于减少错误。
4.  **意图明确 (Intent):** 表明这些常量是属于同一个固定集合的。

**枚举的类型：**

#### **1. 数字枚举 (Numeric Enums)**

*   **默认行为:** 如果不给成员赋值，它们会从 `0` 开始自动递增。
*   **显式赋值:** 你可以为部分或全部成员指定数值。未赋值的成员会从上一个已赋值成员的值加 1 开始递增。
*   **反向映射 (Reverse Mapping):** 数字枚举的一个**重要特性**是它会创建**反向映射**，即你不仅可以通过名称获取值 (`Enum.Member`)，还可以通过值获取名称 (`Enum[value]`)。

```typescript
enum Direction {
  Up,    // 默认是 0
  Down,  // 默认是 1
  Left,  // 默认是 2
  Right, // 默认是 3
}

enum OrderStatus {
  Pending = 1, // 显式赋值为 1
  Processing,  // 自动递增为 2
  Shipped,     // 自动递增为 3
  Delivered,   // 自动递增为 4
  Cancelled = 99, // 显式赋值
}

// --- 使用 ---
let currentDirection: Direction = Direction.Up;
console.log(currentDirection); // 输出: 0

let myOrderStatus: OrderStatus = OrderStatus.Shipped;
console.log(myOrderStatus); // 输出: 3

// 反向映射 (仅数字枚举)
console.log(Direction[0]); // 输出: "Up"
console.log(OrderStatus[3]); // 输出: "Shipped"

if (myOrderStatus === OrderStatus.Shipped) {
  console.log("Order has been shipped!");
}
```

#### **2. 字符串枚举 (String Enums)**

*   每个成员**必须**用字符串字面量或另一个字符串枚举成员进行显式初始化。
*   **没有**自动递增行为。
*   **没有**反向映射。
*   **优点:**
    *   调试时更直观，日志或控制台输出会显示有意义的字符串（如 `"PENDING"` 而不是 `1`）。
    *   运行时行为更可预测（因为没有反向映射）。

```
enum LogLevel {
  Debug = "DEBUG",
  Info = "INFO",
  Warning = "WARN", // 值可以不同于名称
  Error = "ERROR",
}

// --- 使用 ---
let level: LogLevel = LogLevel.Info;
console.log(level); // 输出: "INFO"

function logMessage(message: string, level: LogLevel) {
  console.log(`[${level}] ${message}`);
}

logMessage("User logged in", LogLevel.Info); // 输出: [INFO] User logged in
// console.log(LogLevel["INFO"]); // 错误! 字符串枚举没有反向映射
```

#### **3. 异构枚举 (Heterogeneous Enums)** - **不推荐使用**

*   可以混合使用字符串和数字成员。
*   虽然技术上可行，但通常不推荐，因为它可能导致混淆，并且很少有明确的用例。

```typescript
enum MixedBag {
  Yes = 1,
  No = "NO",
  // Maybe, // Error! 需要初始化，因为上一个是字符串
}
```

#### **4. `const` 枚举 (`const enum`)** - **优化手段**

*   在 `enum` 前加上 `const` 关键字。
*   **关键区别:** `const` 枚举在编译后**完全消失**，所有用到枚举成员的地方会被直接**内联 (inline)** 替换为对应的**常量值**。
*   **优点:**
    *   生成的 JavaScript 代码更少，更简洁，运行时性能可能略有提升（没有额外的查找对象）。
*   **缺点/限制:**
    *   无法访问其运行时对象（因为它不存在），因此不能进行反向映射（即使是数字类型），也不能在运行时迭代枚举成员。
    *   在某些构建设置下（如启用了 `isolatedModules`），跨文件使用 `const enum` 可能会有问题，因为编译器需要原始枚举信息来进行内联，而 `isolatedModules` 假设每个文件是独立编译的。通常在应用程序内部使用较安全。

```
const enum FileAccess {
  Read = 1,
  Write = 2,
  ReadWrite = Read | Write, // 可以基于其他成员计算
}

// --- 使用 ---
let access: FileAccess = FileAccess.Write;
console.log(access); // 编译后的 JS 可能直接是 console.log(2);

// const enum 不能反向映射
// console.log(FileAccess[2]); // Error! A const enum member can only be accessed using a string literal.

// 在函数参数中使用
function checkAccess(level: FileAccess) {
  if (level === FileAccess.ReadWrite) { // 编译后 JS 可能直接是 if (level === 3)
    console.log("Full access granted.");
  }
}
checkAccess(FileAccess.ReadWrite);
```

**总结:**

枚举是 TypeScript 提供的一种非常有用的工具，用于创建具名的常量集合，从而提高代码的**可读性、可维护性和类型安全性**。

*   **数字枚举** 提供自动递增和反向映射，但反向映射有时可能引入不必要的运行时对象。
*   **字符串枚举** 需要显式初始化，没有反向映射，运行时行为更简单直接，调试输出更友好。**现代 TypeScript 开发中更常用**。
*   **`const` 枚举** 是编译时的优化，会内联枚举值，减少运行时代码，但失去了运行时的枚举对象。

根据你的具体需求（是否需要反向映射、是否关心运行时代码大小、调试体验等）来选择合适的枚举类型。