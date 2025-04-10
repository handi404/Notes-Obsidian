**泛型 (Generics)** 是 TypeScript 中一个极其强大的特性，也是其类型系统灵活性的核心来源之一。掌握它能让你编写出**高度可重用且类型安全**的代码。

**核心思想：带 “类型参数” 的组件**

想象一下，你想写一个函数，这个函数的功能是接收一个参数，然后直接返回这个参数。这个逻辑很简单，但你想让这个函数能处理**任何类型**的数据，同时**保留**传入数据的原始类型信息。

**没有泛型，你可能会这样做：**

1.  **使用 `any` (不推荐):**
    ```typescript
    function identityAny(arg: any): any {
      return arg;
    }
    let outputAny = identityAny("myString"); // outputAny 的类型是 any，丢失了类型信息！
    // outputAny.toFixed(); // 运行时会报错，但编译时不会提示！
    ```
    问题：丢失了类型信息，放弃了类型安全。

2.  **为每种类型写一个函数 (繁琐):**
    ```typescript
    function identityString(arg: string): string { return arg; }
    function identityNumber(arg: number): number { return arg; }
    // ... 为 boolean, object, array 等等都写一个？太麻烦了！
    ```
    问题：代码大量重复，难以维护。

**泛型的解决方案：定义时使用 “类型变量”**

泛型允许你定义函数、接口或类时，不预先指定具体的类型，而是使用一个**类型变量 (Type Variable)**，通常用 `T` 来表示（但这只是一个约定俗成的名字，你可以用任何有效的标识符，如 `T`, `U`, `K`, `V`, `TypeParam` 等）。这个类型变量就像一个**占位符**，表示“某种类型”。

#### **1. 泛型函数 (Generic Functions)**

```typescript
// 1. 定义泛型函数
//    <T> 在函数名后声明了一个类型变量 T
//    arg: T 表示参数 arg 的类型是 T
//    : T 表示函数返回值类型是 T
function identity<T>(arg: T): T {
  // 函数体内部，arg 被当作 T 类型处理
  return arg;
}

// 2. 使用泛型函数
// 方式一：显式指定类型参数
let outputString = identity<string>("myString"); // T 被指定为 string
let outputNumber = identity<number>(123);       // T 被指定为 number

// 方式二：利用类型推断 (Type Inference) - 更常见
let outputBoolean = identity(true); // TypeScript 自动推断出 T 是 boolean
let outputObject = identity({ name: "Alice" }); // 推断出 T 是 { name: string }

// 关键点：类型安全！
console.log(outputString.toUpperCase()); // OK, outputString 是 string
console.log(outputNumber.toFixed(2));    // OK, outputNumber 是 number
// console.log(outputBoolean.toFixed()); // Error: Property 'toFixed' does not exist on type 'boolean'. (编译时报错！)
```

**泛型解决了之前的问题：**

*   **代码可重用:** 只写了一个 `identity` 函数。
*   **类型安全:** 保留了传入参数的类型，并在后续使用时提供正确的类型检查和智能提示。

#### **2. 泛型接口 (Generic Interfaces)**

接口也可以使用泛型，来定义具有可变类型部分的结构。

```typescript
// 定义一个泛型接口，表示一个包含某种类型数据的盒子
interface Box<T> {
  content: T; // content 的类型由使用 Box 时传入的 T 决定
}

// 使用泛型接口
let stringBox: Box<string> = { content: "Hello Generics" };
let numberBox: Box<number> = { content: 42 };
let userBox: Box<{ id: number, name: string }> = { content: { id: 1, name: "Bob" } };

console.log(stringBox.content.toUpperCase()); // OK
console.log(numberBox.content.toFixed());    // OK
console.log(userBox.content.name);          // OK
```

**常见场景：API 响应**

```typescript
interface ApiResponse<DataType> {
  success: boolean;
  data: DataType; // 响应数据的类型是可变的
  message?: string;
}

interface User { id: number; username: string; }
interface Product { sku: string; price: number; }

function handleUserResponse(response: ApiResponse<User>) {
  if (response.success) {
    console.log("User data:", response.data.username); // 类型安全地访问 username
  }
}

function handleProductResponse(response: ApiResponse<Product[]>) {
    if(response.success) {
        console.log("First product price:", response.data[0]?.price); // 类型安全地访问 price
    }
}
```

#### **3. 泛型类 (Generic Classes)**

类也可以是泛型的，允许类的属性或方法处理不同的类型。

```typescript
class DataStore<T> {
  private data: T[] = [];

  addItem(item: T): void {
    this.data.push(item);
  }

  getAll(): T[] {
    return [...this.data]; // 返回副本
  }

  getItem(index: number): T | undefined {
      return this.data[index];
  }
}

// 创建一个只能存储字符串的 DataStore
const stringStore = new DataStore<string>();
stringStore.addItem("Apple");
stringStore.addItem("Banana");
// stringStore.addItem(123); // Error: Argument of type 'number' is not assignable to parameter of type 'string'.
console.log(stringStore.getAll().join(", ")); // 输出: Apple, Banana

// 创建一个只能存储数字的 DataStore (类型推断也可以)
const numberStore = new DataStore<number>();
numberStore.addItem(10);
numberStore.addItem(20);
console.log(numberStore.getAll().reduce((a, b) => a + b, 0)); // 输出: 30
```

#### **4. 泛型约束 (Generic Constraints)**

有时，你希望对泛型类型 `T` 做一些**限制**，确保它**至少**具有某些属性或方法。例如，你想写一个函数，它接收一个参数并打印它的 `.length` 属性。你不能保证任意类型 `T` 都有 `.length`。这时就需要**约束**。

使用 `extends` 关键字来添加约束。

```typescript
// 1. 定义一个接口，描述具有 length 属性的类型
interface Lengthwise {
  length: number;
}

// 2. 定义泛型函数，并约束 T 必须符合 Lengthwise 接口
//    <T extends Lengthwise> 表示 T 必须是 Lengthwise 或其子类型
function logLength<T extends Lengthwise>(arg: T): T {
  console.log(`Length: ${arg.length}`); // OK! 因为 T 被约束了，保证有 .length
  return arg;
}

// 3. 使用
logLength("hello"); // OK, string 有 length 属性
logLength([1, 2, 3]); // OK, array 有 length 属性
logLength({ length: 10, value: "test" }); // OK, 对象字面量只要有 length 属性就满足约束
// logLength(123); // Error: Argument of type 'number' is not assignable to parameter of type 'Lengthwise'. (number 没有 length)
// logLength({ name: "Alice" }); // Error: Property 'length' is missing...
```

**总结:**

泛型是 TypeScript 的精髓之一，它允许你：

*   编写**可重用**的函数、接口和类，这些组件可以处理多种数据类型。
*   在重用的同时保持**类型安全**，避免使用 `any` 带来的风险。
*   通过**泛型约束**，对可接受的类型施加限制，确保能安全地访问某些属性或方法。

掌握泛型能显著提高你的代码质量、减少重复代码、增强代码的灵活性和健壮性。它是编写高级、抽象和可维护 TypeScript 代码的必备工具。