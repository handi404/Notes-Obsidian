### OOP in JavaScript: 核心思想

首先，你需要知道一个关键事实：**JavaScript 是一门基于原型的语言**。传统的面向对象语言（如 Java, C++）是基于类的。虽然 ES6 引入了 `class` 关键字，但这只是一个“语法糖”（Syntactic Sugar），它的底层实现依然是基于原型。理解这一点至关重要。

OOP 的核心思想是将现实世界的事物抽象成程序中的**对象**，每个对象都有自己的**属性**（数据）和**方法**（行为）。OOP 主要有三大特性：封装、继承、多态。

---

### 1. Class (类) 与 Constructor (构造函数)

#### 核心概念
*   **Class (类):** 一个用于创建对象的**蓝图**或**模板**。它定义了一类对象应该拥有的属性和方法。
*   **Constructor (构造函数):** `class` 内部一个特殊的方法，名为 `constructor`。当你使用 `new` 关键字创建一个类的实例（新对象）时，`constructor` 会被自动调用，其主要任务是初始化这个新对象的属性。

#### 语法与示例
```javascript
// 定义一个 “汽车” 类
class Car {
  // 构造函数：在 `new Car(...)` 时自动执行
  constructor(brand, speed) {
    // `this` 指向新创建的实例对象
    // 初始化实例的属性
    this.brand = brand;
    this.speed = speed;
  }

  // 定义一个方法（行为）
  drive() {
    console.log(`${this.brand} is driving at ${this.speed} km/h.`);
  }

  // 定义一个静态方法 (属于类本身，而不是实例)
  static getCarInfo() {
    console.log("A car is a four-wheeled road vehicle.");
  }
}

// 使用 `new` 关键字创建 Car 类的实例（对象）
const myCar = new Car('Tesla', 120);
const anotherCar = new Car('BMW', 100);

// 调用实例的方法
myCar.drive(); // 输出: Tesla is driving at 120 km/h.
anotherCar.drive(); // 输出: BMW is driving at 100 km/h.

// 调用类的静态方法
Car.getCarInfo(); // 输出: A car is a four-wheeled road vehicle.

// 错误调用：实例无法调用静态方法
// myCar.getCarInfo(); // TypeError: myCar.getCarInfo is not a function
```

#### 扩展与应用
*   **React 中的组件:** 在 React 的早期版本中，类组件是主流，`class MyComponent extends React.Component` 就是一个典型的应用。
*   **Node.js:** Node.js 的核心模块（如 `EventEmitter`）以及许多库都广泛使用类来构建可复用的模块。
*   **自定义错误类型:** 通过继承 `Error` 类可以创建更具描述性的自定义错误，便于调试和错误处理。

#### 要点与注意事项
1.  **Hoisting (提升):** 与函数声明不同，`class` 声明**不会被提升**。你必须先声明类，然后才能使用它。
2.  **严格模式:** 类声明和表达式的主体都默认在**严格模式**下执行。
3.  **`constructor` 是可选的:** 如果你没有提供 `constructor`，JavaScript 会为你添加一个空的 `constructor() {}`。
4.  **`this` 指向:** 类方法中的 `this` 默认指向调用该方法的实例。但如果方法被用作回调函数，`this` 可能会丢失。解决方法是使用箭头函数或在 `constructor` 中 `bind`。

---

### 2. Inheritance (继承) 与 `super()`

#### 核心概念
*   **Inheritance (继承):** 允许一个类（子类）获取另一个类（父类）的属性和方法。这实现了代码的复用，并建立了类之间的层级关系。
*   **`super()`:** 一个关键字，用于在子类中调用父类的方法。
    *   在 `constructor` 中，`super()` 用来调用父类的构造函数。
    *   在普通方法中，`super.methodName()` 用来调用父类的同名方法。

#### 语法与示例
```javascript
// 父类 (Superclass)
class Vehicle {
  constructor(name) {
    this.name = name;
  }

  honk() {
    console.log(`${this.name} makes a sound.`);
  }
}

// 子类 (Subclass) 使用 `extends` 关键字实现继承
class Motorcycle extends Vehicle {
  constructor(name, engineType) {
    // 1. 在子类 constructor 中，必须先调用 super()
    super(name); // 调用父类 Vehicle 的 constructor(name)
    this.engineType = engineType;
  }

  // 2. 覆写 (Override) 父类的方法
  honk() {
    // 3. 使用 super 调用父类的同名方法
    super.honk(); 
    console.log('It sounds like "vroom vroom!"');
  }
}

const myMotorcycle = new Motorcycle('Harley', 'V-Twin');
myMotorcycle.honk();
// 输出:
// Harley makes a sound.
// It sounds like "vroom vroom!"

console.log(myMotorcycle.name); // 输出: Harley (继承自 Vehicle)
```

#### 扩展与应用
*   **UI 框架:** 创建基础组件 `BaseButton`，然后继承它创建 `PrimaryButton`, `DangerButton` 等，它们共享基础样式和行为，但各有特点。
*   **游戏开发:** `Enemy` 基类，然后派生出 `Goblin`, `Dragon` 等不同类型的敌人，它们都继承了共同的 `attack()` 或 `move()` 方法。

#### 要点与注意事项
1.  **`super()` 必须在 `this` 之前调用:** 在子类的 `constructor` 中，你必须在使用 `this` 关键字之前调用 `super()`，否则会抛出 `ReferenceError`。这是因为子类的 `this` 是由父类构造函数初始化的。
2.  **单一继承:** JavaScript 的 `class` 只支持单一继承，即一个子类只能有一个直接父类。但可以通过组合或 Mixin 模式实现更复杂的行为复用。

---

### 3. Encapsulation (封装)

#### 核心概念
*   **Encapsulation (封装):** 将数据（属性）和操作数据的代码（方法）捆绑在一起（即在类中），并对外部隐藏对象的内部状态。外部只能通过对象暴露的公共接口（方法）来访问或修改数据，而不能直接操作。这提高了代码的安全性和可维护性。

#### 语法与示例 (现代方式)
在现代 JavaScript (ES2022) 中，我们使用 `#` 前缀来创建**私有字段**和**私有方法**，实现真正的封装。

```javascript
class BankAccount {
  // #balance 是一个私有字段，外部无法访问
  #balance = 0;

  constructor(initialDeposit) {
    this.#balance = initialDeposit;
  }

  // 公共方法，作为外部访问私有数据的接口
  deposit(amount) {
    if (amount > 0) {
      this.#balance += amount;
      console.log(`Deposited: ${amount}. New balance: ${this.#getBalance()}`);
    }
  }

  withdraw(amount) {
    if (amount > 0 && amount <= this.#balance) {
      this.#balance -= amount;
      console.log(`Withdrew: ${amount}. New balance: ${this.#getBalance()}`);
    } else {
      console.log('Withdrawal failed: insufficient funds.');
    }
  }

  // #getBalance() 是一个私有方法
  #getBalance() {
    return this.#balance;
  }
  
  // 提供一个公共的 getter 来安全地获取余额
  get currentBalance() {
    return this.#getBalance();
  }
}

const myAccount = new BankAccount(100);
myAccount.deposit(50); // 输出: Deposited: 50. New balance: 150

// 尝试直接访问私有字段，会抛出语法错误
// console.log(myAccount.#balance); // SyntaxError: Private field '#balance' must be declared in an enclosing class.

// 通过公共接口安全地获取数据
console.log(`Current balance is: ${myAccount.currentBalance}`); // 输出: Current balance is: 150
```

#### 扩展与应用
*   **库和框架开发:** 库的作者可以隐藏内部实现细节（如内部状态、缓存等），只暴露稳定的公共 API。这样即使未来内部实现重构，也不会影响用户代码。
*   **状态管理:** 确保状态只能通过预定义的方法（如 Redux 中的 action/reducer）来修改，防止意外的直接篡改。

#### 要点与注意事项
1.  **真私有:** `#` 提供了硬性的私有性，与过去使用下划线 `_` 前缀（如 `_balance`）作为“私有”约定的方式完全不同。下划线只是一种君子协定，外部代码依然可以直接访问。
2.  **兼容性:** 私有字段是较新的特性，需要注意目标环境（浏览器、Node.js 版本）的兼容性。

---

### 4. Prototypes (原型) - The Truth Behind Classes

#### 核心概念
这是 JavaScript OOP 的根基。
*   每个 JavaScript 对象都有一个内部的 `[[Prototype]]` 属性，它指向另一个对象，我们称之为该对象的**原型**。
*   当你试图访问一个对象的属性时，如果该对象本身没有这个属性，JavaScript 引擎就会沿着原型链（`[[Prototype]]` 链）向上查找，直到找到该属性或到达链的末端 (`null`)。
*   `class` 语法实际上是创建构造函数和设置其 `prototype` 属性的便捷方式。类的实例方法实际上是定义在类的 `prototype` 对象上的。

#### 示例
让我们揭开 `class Car` 的“语法糖”面纱：

```javascript
class Car {
  constructor(brand) {
    this.brand = brand;
  }
  drive() { /* ... */ }
}

const myCar = new Car('Tesla');

// 揭示真相
// 1. 类本质上是一个函数
console.log(typeof Car); // "function"

// 2. 实例的 [[Prototype]] 指向类的 prototype 对象
console.log(Object.getPrototypeOf(myCar) === Car.prototype); // true

// 3. 类的方法实际定义在 prototype 对象上
console.log(Car.prototype.hasOwnProperty('drive')); // true

// 4. 实例自身没有 drive 方法，它是从原型链上找到的
console.log(myCar.hasOwnProperty('drive')); // false
```
当我们调用 `myCar.drive()` 时，引擎的查找过程是：
1.  在 `myCar` 对象上找 `drive` 方法 -> 找不到。
2.  沿着原型链找到 `myCar` 的原型，即 `Car.prototype` -> 找到了 `drive` 方法，执行它。

#### 扩展与应用
*   **性能优化:** 将方法定义在 `prototype` 上（`class` 默认就是这么做的）可以节省内存。所有实例共享同一个方法引用，而不是每个实例都复制一份。
*   **理解内置对象:** 像 `Array`, `String` 等内置对象的方法（如 `map`, `slice`, `substring`）都定义在它们的 `prototype` 上，这就是为什么任何数组或字符串实例都能调用这些方法。
*   **调试:** 当遇到 "method is not a function" 的错误时，理解原型链可以帮助你追溯问题根源。

#### 要点与注意事项
1.  **`__proto__` vs `Object.getPrototypeOf()`:** `__proto__` 是一个非标准的、已不推荐使用的访问器，应使用 `Object.getPrototypeOf()` (读取) 和 `Object.setPrototypeOf()` (设置) 来操作原型。
2.  **不要修改内置对象的原型:** 修改 `Array.prototype` 或 `Object.prototype` 是一个非常危险的操作（称为 "monkey patching"），它会污染全局环境，可能导致第三方库或未来 JavaScript 特性出现不可预知的冲突。

---

### 5. Polymorphism (多态)

#### 核心概念
*   **Polymorphism (多态):** 意为“多种形态”。在 OOP 中，它指不同的对象可以对同一个消息（方法调用）作出不同的响应。在 JavaScript 中，多态通常通过继承和方法覆写来实现。由于 JS 是动态类型语言，多态的表现非常自然。

#### 语法与示例
```javascript
class Animal {
  speak() {
    console.log("An animal makes a sound.");
  }
}

class Dog extends Animal {
  // 覆写父类的 speak 方法
  speak() {
    console.log("Dog barks: Woof! Woof!");
  }
}

class Cat extends Animal {
  // 覆写父类的 speak 方法
  speak() {
    console.log("Cat meows: Meow!");
  }
}

function makeAnimalSpeak(animal) {
  animal.speak(); // 同一个调用，但根据传入对象的不同，行为也不同
}

const genericAnimal = new Animal();
const dog = new Dog();
const cat = new Cat();

makeAnimalSpeak(genericAnimal); // 输出: An animal makes a sound.
makeAnimalSpeak(dog);           // 输出: Dog barks: Woof! Woof!
makeAnimalSpeak(cat);           // 输出: Cat meows: Meow!
```
在这个例子中，`makeAnimalSpeak` 函数不关心传入的是什么具体的动物类型，它只知道这个对象有一个 `speak` 方法。这就是多态：同一个 `animal.speak()` 调用，根据 `animal` 的实际类型（`Dog` 或 `Cat`）产生了不同的行为。

#### 扩展与应用
*   **插件系统:** 一个主程序可以调用所有插件的 `execute()` 方法，每个插件根据自身功能执行不同的任务。
*   **UI 渲染:** 一个渲染引擎调用不同 UI 元素（按钮、输入框、图片）的 `render()` 方法，每个元素都会以自己的方式绘制到屏幕上。
*   **事件处理:** 不同的事件监听器对同一种事件（如 `click`）做出不同的响应。

#### 要点与注意事项
*   **Duck Typing:** JavaScript 的多态性与“鸭子类型”思想密切相关：“如果它走起来像鸭子，叫起来也像鸭子，那么它就是一只鸭子。” 只要对象实现了所需的方法（如 `speak()`），就可以在需要该方法的上下文中使用它，而无需关心它的类或继承关系。

---

### 总结

| 概念 | 核心作用 | 现代 JS 关键字/语法 |
| :--- | :--- | :--- |
| **Class & Constructor** | 定义对象的蓝图和初始化过程 | `class`, `constructor`, `new` |
| **Inheritance** | 复用代码，建立类层级 | `extends`, `super()` |
| **Encapsulation** | 隐藏内部数据，保护对象状态 | `#` (私有字段/方法) |
| **Prototypes** | JS 实现继承的底层机制 | `Object.getPrototypeOf()`, `.prototype` |
| **Polymorphism** | 不同对象对同一消息的不同响应 | 方法覆写 (Method Overriding) |

通过 `class` 语法，JavaScript 提供了更清晰、更友好的面向对象编程体验，但其核心仍然是灵活而强大的原型链。理解这两层，你就能真正掌握 JavaScript 的 OOP。