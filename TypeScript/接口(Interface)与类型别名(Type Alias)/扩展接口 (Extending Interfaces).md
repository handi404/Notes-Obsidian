**扩展接口 (Extending Interfaces)**。

想象一下，你已经有了一个“基础款”的蓝图（接口），现在想在这个基础上创建一个“升级款”或“特定款”的蓝图，加入一些额外的规格。**扩展接口** 就是用来干这个的！它允许一个接口**继承**另一个（或多个）接口的成员（属性和方法）。

**核心思想：** 复用现有接口定义，并添加新的约束，构建更具体的类型结构。这体现了 **“is-a”** （是一个）的关系（例如，一个 `Admin` *是一个* `User`）。

**如何实现？**

使用 `extends` 关键字。

**1. 单一接口继承**

这是最常见的情况：一个接口扩展自另一个接口。

```typescript
// 基础接口：定义所有“人”共有的属性
interface Person {
  name: string;
  age: number;
  greet(): void; // 一个方法签名
}

// 扩展接口：Employee "is a" Person，并有额外的属性
interface Employee extends Person {
  employeeId: string;
  department: string;
  // greet 方法是从 Person 继承来的，无需重复定义
  // 可以选择性地 "重新定义" greet，但签名必须兼容
  // greet(message?: string): void; // 例如，可以添加一个可选参数
}

// 实现 Employee 接口的对象
const john: Employee = {
  name: "John Doe",
  age: 35,
  employeeId: "E12345",
  department: "Engineering",
  greet() {
    console.log(`Hello, my name is ${this.name} and I work in ${this.department}.`);
  },
};

john.greet(); // 调用继承并实现的方法

// 一个只接受 Person 的函数，也可以接受 Employee
function introduce(person: Person): void {
  console.log(`Introducing ${person.name}, age ${person.age}.`);
  person.greet(); // 可以调用 Person 上定义的方法
}

introduce(john); // OK! 因为 Employee 兼容 Person (类型兼容性)
```

**关键点：**

*   `Employee` 自动获得了 `Person` 的所有成员 (`name`, `age`, `greet`)。
*   `Employee` 添加了自己的特定成员 (`employeeId`, `department`)。
*   类型兼容性：任何需要 `Person` 类型的地方，都可以传入 `Employee` 类型的对象。

**2. 多接口继承**

一个接口可以同时扩展多个接口，它将拥有所有父接口的成员。这对于组合不同的“能力”或“特性”非常有用。

```typescript
interface Clickable {
  onClick(event: MouseEvent): void;
}

interface Focusable {
  onFocus(): void;
  onBlur(): void;
}

// Button 接口需要同时具备 Clickable 和 Focusable 的能力
interface Button extends Clickable, Focusable {
  label: string;
  disabled?: boolean;
}

// 实现 Button 接口
const myButton: Button = {
  label: "Submit",
  onClick: (e) => console.log("Button clicked!", e.clientX),
  onFocus: () => console.log("Button focused!"),
  onBlur: () => console.log("Button blurred!"),
  // disabled: false // 可选属性
};

function simulateClick(item: Clickable) {
    // 伪造一个 MouseEvent
    const fakeEvent = new MouseEvent('click');
    item.onClick(fakeEvent);
}

function simulateFocus(item: Focusable) {
    item.onFocus();
}

simulateClick(myButton); // OK, myButton is Clickable
simulateFocus(myButton); // OK, myButton is Focusable
```

**关键点：**

*   `Button` 继承了 `Clickable` 的 `onClick` 和 `Focusable` 的 `onFocus`、`onBlur`。
*   **名称冲突处理：** 如果多个父接口定义了同名但类型不兼容的属性或方法，TypeScript 会报错。如果同名且类型兼容（例如，一个是另一个的子类型），则可以合并。

**为什么要扩展接口？**

*   **代码复用 (DRY):** 避免在多个接口中重复定义相同的属性或方法。
*   **建立类型层次结构:** 清晰地表达类型之间的关系（如 `Admin` 是 `User` 的一种）。
*   **提高可维护性:** 修改基础接口，所有扩展它的接口都会自动更新。
*   **组合能力:** 通过多重继承，可以像搭积木一样组合不同的功能特性。
*   **遵循面向对象原则:** 模拟类继承的概念，但应用于类型系统。

**与类型别名 + 交叉类型 (`&`) 的比较：**

你也可以使用类型别名和交叉类型 (`&`) 来达到类似组合效果：

```typescript
type ClickableAndFocusable = Clickable & Focusable;

type ButtonType = ClickableAndFocusable & {
  label: string;
  disabled?: boolean;
};
```

虽然结果相似，但 `extends` 通常被认为：

*   在表达**类型层次**（"is-a" 关系）时更自然、更清晰。
*   错误信息可能更友好一些（接口扩展失败 vs. 交叉类型计算结果为 `never`）。
*   更符合面向对象背景的开发者的习惯。

而交叉类型 (`&`) 更通用，可以组合任何类型，不仅仅是对象类型。

**总结:**

扩展接口 (`extends`) 是 TypeScript 中组织和复用接口定义的强大机制。它通过**继承**让你可以基于现有接口创建更专门化的接口，支持**单一继承**和**多重继承**，极大地提高了代码的**结构性、可维护性和复用性**。这是构建复杂类型系统时的常用且重要的工具。