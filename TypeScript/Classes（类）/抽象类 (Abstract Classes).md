**抽象类 (Abstract Classes)**。

**抽象类** 是一种特殊的**基类 (Base Class)**，它为其他类提供了一个共同的**模板或蓝图**，但**不能被直接实例化 (instantiated)**。你可以把它想象成一个“半成品”的类，它定义了一些结构和行为，但可能故意留下了一些“待完成”的部分，强制要求**继承**它的子类去具体实现这些部分。

**核心概念:**

1.  **不能直接创建实例:** 你不能使用 `new` 关键字直接创建一个抽象类的对象。例如 `new MyAbstractClass()` 会导致编译错误。
2.  **作为基类使用:** 抽象类的主要目的是被其他类**继承 (`extends`)**。
3.  **可以包含抽象成员:**
    *   **抽象方法 (Abstract Method):** 只有方法签名（名称、参数类型、返回类型），没有具体的实现（方法体 `{}`）。子类**必须**提供这些方法的具体实现。
    *   **抽象属性 (Abstract Property):** 只有属性名和类型，没有初始值（除非在构造函数中赋值，但这通常由子类完成）。子类**必须**实现或初始化这些属性。
4.  **可以包含具体成员:** 抽象类**也可以**包含具有完整实现的普通方法和属性（就像常规类一样）。子类会直接继承这些具体成员，也可以选择重写 (override) 它们。
5.  **`abstract` 关键字:** 用于标记类本身以及类中的抽象成员。

**为什么使用抽象类？**

1.  **共享通用代码:** 将所有子类共有的属性和方法（具体实现）放在抽象基类中，避免代码重复。
2.  **强制子类实现特定行为:** 通过定义抽象方法/属性，确保所有继承该抽象类的子类都具备某些必要的功能，但允许每个子类以自己的方式实现这些功能。这定义了一个**共同的结构和契约**。
3.  **提供模板方法 (Template Method Pattern):** 抽象类可以定义一个算法的骨架（一个具体方法），其中某些步骤调用抽象方法。子类可以通过实现这些抽象方法来定制算法的特定步骤，而整体流程保持不变。

**如何在 TypeScript 中实现？**

```typescript
// 1. 使用 'abstract' 关键字定义抽象类
abstract class Shape {
  // 可以有具体属性
  color: string;

  // 可以有具体方法
  constructor(color: string) {
    this.color = color;
    console.log(`Creating a ${this.color} shape.`);
  }

  // 2. 定义抽象方法 (没有实现体，用分号结束)
  //    强制子类必须实现如何计算面积
  abstract calculateArea(): number;

  // 3. 定义抽象属性 (较少见，通常通过抽象方法或构造函数处理)
  // abstract name: string;

  // 可以有具体方法，甚至可以调用抽象方法
  displayArea(): void {
    // 调用 calculateArea()，具体实现由子类提供
    const area = this.calculateArea();
    console.log(`The area of the ${this.color} shape is ${area}.`);
    // console.log(`Shape name: ${this.name}`); // 如果 name 是抽象属性
  }
}

// --- 不能直接实例化抽象类 ---
// const genericShape = new Shape("blue"); // Error: Cannot create an instance of an abstract class.

// 4. 创建具体子类，继承抽象类
class Circle extends Shape {
  radius: number;
  // name: string = "Circle"; // 实现抽象属性

  constructor(color: string, radius: number) {
    super(color); // 调用父类 (Shape) 的构造函数
    this.radius = radius;
  }

  // 5. 必须实现父类中所有的抽象方法
  calculateArea(): number {
    return Math.PI * this.radius * this.radius;
  }
  // 如果父类有抽象属性，也必须在这里实现或在构造函数中赋值
}

class Rectangle extends Shape {
  width: number;
  height: number;
  // name: string = "Rectangle"; // 实现抽象属性

  constructor(color: string, width: number, height: number) {
    super(color);
    this.width = width;
    this.height = height;
  }

  // 5. 必须实现父类中所有的抽象方法
  calculateArea(): number {
    return this.width * this.height;
  }
}

// 6. 使用具体子类
const circle = new Circle("red", 5);
const rectangle = new Rectangle("blue", 4, 6);

circle.displayArea(); // 调用继承的具体方法，该方法内部调用了 Circle 实现的 calculateArea
rectangle.displayArea(); // 调用继承的具体方法，该方法内部调用了 Rectangle 实现的 calculateArea

// 多态性：可以将子类实例赋值给抽象类类型的变量
let shapeRef: Shape = circle;
shapeRef.displayArea(); // 仍然正确调用 Circle 的实现

shapeRef = rectangle;
shapeRef.displayArea(); // 正确调用 Rectangle 的实现
```

**抽象类 vs. 接口 (Abstract Classes vs. Interfaces):**

这是一个常见的比较点，它们都用于定义契约，但有关键区别：

| 特性        | 抽象类 (`abstract class`)                 | 接口 (`interface`)            |
| :-------- | :------------------------------------- | :-------------------------- |
| **实例**    | ❌ 不能直接实例化                              | ❌ 不能直接实例化                   |
| **实现**    | ✅ **可以包含**具体实现（方法体、属性初始值）              | ❌ **不能包含**具体实现 (只有类型签名)     |
| **抽象成员**  | ✅ 可以包含抽象方法/属性 (无实现)                    | ✅ 所有成员本质上都是“抽象”的 (只有签名)     |
| **继承/实现** | 子类使用 `extends` (只能继承一个)                | 类使用 `implements` (可以实现多个)   |
| **目的**    | 定义**共同基类**，共享代码，强制子类结构 ("is-a")        | 定义**契约/形状**，强制能力 ("can-do") |
| **构造函数**  | ✅ 可以有构造函数 (子类通过 `super()` 调用)          | ❌ 不能有构造函数                   |
| **访问修饰符** | ✅ 可以有 `public`, `private`, `protected` | ✅ 成员默认为 `public` (不能指定其他)   |
| **静态成员**  | ✅ 可以有静态成员 (包括抽象静态成员)                   | ✅ 可以定义静态部分的类型 (但不常用)        |

**何时选择？**

*   **选择抽象类 (Abstract Class):**
    *   当你想要为一组相关的类提供一个**共同的基类实现**（共享代码）。
    *   当你希望定义的组件不仅有共同的接口，还有共同的**内部状态或实现细节**。
    *   当你需要利用 `protected` 成员或构造函数来控制子类的实现时。
*   **选择接口 (Interface):**
    *   当你只想定义一个**契约或形状**，而不关心具体的实现。
    *   当你希望一个类能够扮演**多种角色**（实现多个接口）。
    *   当你需要描述现有 JavaScript 对象的形状或函数签名时。
    *   当你追求**最大程度的解耦**时（依赖接口而非具体类或抽象类）。

**总结:**

抽象类是 TypeScript 中实现继承和多态的重要工具。它们作为不能直接实例化的基类，通过混合**具体实现**和**抽象成员**，既能**共享代码**，又能**强制子类提供特定的实现**。理解抽象类与接口的区别和适用场景，有助于你设计出结构更清晰、更易于维护和扩展的面向对象系统。