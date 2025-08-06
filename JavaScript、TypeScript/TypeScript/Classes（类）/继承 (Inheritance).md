TypeScript 中的 **继承 (Inheritance)**。

**继承** 是面向对象编程（OOP）的三大基本特征（封装、继承、多态）之一。它允许你创建一个 **新类（子类/派生类 - Subclass/Derived Class）**，这个新类可以**自动获得**另一个 **已存在类（父类/基类 - Superclass/Base Class）** 的 **非私有 (non-private)** 属性和方法。

可以把它想象成现实世界中的“遗传”：

*   一个 **“汽车 (Car)”** 类可能有 `颜色 (color)`、`品牌 (brand)` 属性和 `启动 (start())`、`停止 (stop())` 方法。
*   一个 **“跑车 (SportsCar)”** 类 *继承* 自 “汽车 (Car)” 类。它自动就拥有了 `颜色`、`品牌`、`启动()`、`停止()` 这些特性。
*   同时，“跑车” 还可以添加自己独有的特性，比如 `涡轮增压 (hasTurbo)` 属性和 `加速 (accelerate())` 方法。

这种关系通常被称为 **"is-a"** 关系（一个跑车 **是一个** 汽车）。

**为什么使用继承？**

1.  **代码复用 (Code Reusability):** 避免在多个类中重复编写相同的代码。将通用属性和方法放在父类中，子类自动获得。
2.  **创建层次结构 (Hierarchy):** 可以建立清晰的类层级关系，更好地模拟现实世界或系统的结构。
3.  **可维护性 (Maintainability):** 修改父类的通用逻辑，所有子类都能受益。
4.  **多态 (Polymorphism) 的基础:** 允许将子类对象当作父类对象来处理，实现更灵活的设计（这个话题可以单独深入）。

**如何在 TypeScript 中实现继承？**

使用 `extends` 关键字。

```typescript
// 1. 定义父类 (Base Class / Superclass)
class Animal {
  public name: string; // 公共属性
  protected age: number; // 受保护属性，子类可以访问

  constructor(name: string, age: number) {
    this.name = name;
    this.age = age;
    console.log(`${this.name} the Animal is born.`);
  }

  public move(distanceInMeters: number = 0): void { // 公共方法
    console.log(`${this.name} moved ${distanceInMeters}m.`);
  }

  protected getAgeInYears(): number { // 受保护方法，子类可以调用
      return this.age;
  }
}

// 2. 定义子类 (Derived Class / Subclass) 使用 'extends'
class Dog extends Animal {
  // Dog 自动继承了 name, age, move(), getAgeInYears()

  public breed: string; // Dog 特有的属性
  private secretTrick: string = "Roll over"; // Dog 特有的私有属性

  // 3. 子类的构造函数
  constructor(name: string, age: number, breed: string) {
    // 4. 调用父类的构造函数 (super()) - 必须是第一行！
    super(name, age); // 将 name 和 age 传递给 Animal 的构造函数
    this.breed = breed;
    console.log(`It's a ${this.breed} dog!`);
    // 在子类构造函数中可以访问继承的 protected 成员
    console.log(`Initial age check: ${this.age} years old.`);
  }

  // 5. 子类特有的方法
  public bark(): void {
    console.log("Woof! Woof!");
    // console.log(this.secretTrick); // OK: 在类内部可以访问 private 成员
  }

  // 6. 方法重写 (Method Overriding)
  // 子类可以提供与父类同名方法的不同实现
  public move(distanceInMeters: number = 5): void {
    console.log("Dog is running...");
    // 7. 使用 super 关键字调用父类的方法
    super.move(distanceInMeters);
    // 子类可以访问父类的 protected 成员
    console.log(`Age check during move: ${this.getAgeInYears()}`);
  }

  public showAge(): void {
      // 子类可以访问父类的 protected 成员
      console.log(`${this.name} is ${this.age} years old.`);
  }
}

// --- 使用类 ---
const genericAnimal = new Animal("Creature", 3);
genericAnimal.move(10); // 输出: Creature moved 10m.
// console.log(genericAnimal.age); // Error: Property 'age' is protected...

const dog = new Dog("Buddy", 2, "Golden Retriever");
dog.move(20);    // 输出: Dog is running... Buddy moved 20m. Age check during move: 2
dog.bark();      // 输出: Woof! Woof!
console.log(dog.name); // 输出: Buddy (继承自 Animal)
console.log(dog.breed); // 输出: Golden Retriever (Dog 自己的属性)
// console.log(dog.age); // Error: Property 'age' is protected... (不能从外部访问)
// console.log(dog.secretTrick); // Error: Property 'secretTrick' is private...
// dog.getAgeInYears(); // Error: Property 'getAgeInYears' is protected...
dog.showAge();   // 输出: Buddy is 2 years old. (通过 Dog 内部方法访问 age)

// 多态性体现：可以将子类实例赋值给父类类型的变量
let animalRef: Animal = dog;
animalRef.move(15); // 实际执行的是 Dog 类的 move 方法! 输出: Dog is running... Buddy moved 15m.
// animalRef.bark(); // Error: Property 'bark' does not exist on type 'Animal'.
```

**关键概念回顾:**

1.  **`extends`:** 表明一个类继承自另一个类。
2.  **`super()` (构造函数调用):** 在子类的构造函数中，**必须**首先调用 `super()` 来执行父类的构造函数，完成父类部分的初始化。需要传递父类构造函数所需的参数。如果你在子类中定义了 `constructor`，就必须显式调用 `super()`。如果子类没有 `constructor`，则会自动调用父类的 `constructor`（如果父类有无参构造函数的话）。
3.  **`super.` (成员访问):** 在子类的方法中，可以使用 `super.methodName()` 或 `super.propertyName` 来调用父类**被重写 (overridden)** 的方法或访问父类的成员（通常是 `public` 或 `protected`）。
4.  **方法重写 (Method Overriding):** 子类可以定义一个与父类中**非私有**方法同名、同参数列表（或兼容参数列表）的方法，来提供自己的特定实现。当通过子类实例调用该方法时，执行的是子类的版本。
5.  **访问修饰符与继承:**
    *   `public` 成员：被子类继承，并且在子类内部、外部实例、孙子类等任何地方都可访问。
    *   `protected` 成员：被子类继承，可以在子类**内部**访问，但不能通过子类的实例从**外部**直接访问。这是为了让子类能够访问和修改基类的某些内部状态或行为，同时不将其暴露给外部。
    *   `private` 成员：**不被**子类继承或访问。它们完全属于定义它们的那个类。

**总结:**

继承是 TypeScript (以及 OOP) 中实现代码复用和构建类层次结构的核心机制。通过 `extends` 关键字，子类可以获得父类的属性和方法。`super()` 用于调用父类构造函数，`super.` 用于访问父类成员，而方法重写允许子类定制继承来的行为。访问修饰符（特别是 `protected`）在控制继承成员的可见性方面扮演着重要角色。