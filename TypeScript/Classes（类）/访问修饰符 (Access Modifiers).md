访问修饰符是 TypeScript（以及其他一些面向对象语言如 Java, C#）提供的关键字，用于**控制类成员（属性和方法）的可访问性级别**。简单来说，它们决定了你可以在**哪里**访问类的某个属性或调用某个方法。

想象一下一所房子：

*   **`public` (公共的):** 就像客厅，任何人（类内部、类的实例、子类）都可以进入和使用。
*   **`private` (私有的):** 就像主人的私人卧室，只有主人自己（定义该成员的类内部）才能进入。连孩子（子类）和访客（实例）都不能直接进去。
*   **`protected` (受保护的):** 就像家庭活动室，主人（定义该成员的类）和家庭成员（子类）可以进入，但访客（实例，从外部访问时）不能直接进入。

**为什么需要访问修饰符？**

主要目的是实现 **封装 (Encapsulation)**，这是面向对象编程的核心原则之一。通过限制访问：

1.  **隐藏实现细节:** 你可以隐藏类内部复杂或易变的逻辑，只暴露稳定、必要的接口（公共成员）。
2.  **保护内部状态:** 防止外部代码随意修改对象内部的关键数据，导致对象状态不一致或错误。
3.  **提高可维护性:** 当你修改私有或受保护的成员时，你知道影响范围是有限的（只在类内部或子类），降低了破坏其他代码的风险。
4.  **清晰的 API 设计:** 明确告诉类的使用者哪些成员是供外部使用的（public），哪些是内部实现（private/protected）。

**TypeScript 中的三个访问修饰符：**

1.  **`public` (默认)**
    *   **行为:** 成员可以在任何地方被访问：
        *   在定义它的类的内部。
        *   通过类的实例从外部访问。
        *   在子类的内部。
    *   **注意:** 如果你不写任何访问修饰符，成员 **默认就是 `public`**。
    *   **示例:**
        ```typescript
        class MyClass {
          public name: string; // 显式 public
          age: number; // 隐式 public (默认)

          constructor(name: string, age: number) {
            this.name = name;
            this.age = age;
          }

          public greet() { // 显式 public
            console.log(`Hello, I am ${this.name}`);
          }
        }

        const instance = new MyClass("Alice", 30);
        console.log(instance.name); // OK, 外部访问 public 属性
        instance.greet();       // OK, 外部调用 public 方法
        ```

2.  **`private`**
    *   **行为:** 成员**只能**在**声明它的那个类**的内部被访问。
        *   实例无法从外部访问。
        *   子类也**不能**访问。
    *   **用途:** 用于隐藏那些完全属于类内部实现细节的属性和方法。
    *   **示例:**
        ```typescript
        class BankAccount {
          public readonly owner: string;
          private _balance: number; // 使用 _ 前缀是常见约定，但非强制

          constructor(owner: string, initialBalance: number) {
            this.owner = owner;
            this._balance = initialBalance;
          }

          public deposit(amount: number): void {
            if (amount > 0) {
              this.adjustBalance(amount); // 在类内部可以访问 private 方法
            }
          }

          public getBalance(): number {
            return this._balance; // 在类内部可以访问 private 属性
          }

          private adjustBalance(amount: number): void { // 私有方法
            this._balance += amount;
            console.log(`Balance updated to: ${this._balance}`);
          }
        }

        const account = new BankAccount("Bob", 100);
        account.deposit(50); // OK (public method)
        console.log(account.owner); // OK (public property)
        // console.log(account._balance); // Error: Property '_balance' is private...
        // account.adjustBalance(100); // Error: Property 'adjustBalance' is private...

        class SavingsAccount extends BankAccount {
            public report() {
                // console.log(this._balance); // Error: Property '_balance' is private...
                // 子类也不能访问父类的 private 成员
                console.log(`Owner: ${this.owner}, Current Balance: ${this.getBalance()}`); // 可以通过父类的 public 方法间接访问
            }
        }
        ```
    *   **注意:** TypeScript 的 `private` 主要是 **编译时** 的检查。在生成的 JavaScript 代码中，并没有真正的强制私有性（除非你使用 ECMAScript 的 `#` 私有字段，TypeScript 也支持）。但遵循这个规则对于维护 TypeScript 代码库至关重要。

3.  **`protected`**
    *   **行为:** 成员可以在**声明它的类**以及**该类的所有子类**内部被访问。
        *   实例**不能**从外部直接访问 `protected` 成员。
    *   **用途:** 当你希望某个成员对外部隐藏，但允许子类访问或重写它时使用。
    *   **示例:**
        ```typescript
        class Animal {
          public name: string;
          protected speed: number; // 移动速度，子类可以访问和修改

          constructor(name: string) {
            this.name = name;
            this.speed = 0;
          }

          protected move(distance: number): void { // 移动方法，子类可以调用或重写
            console.log(`${this.name} moved ${distance}m at speed ${this.speed}.`);
          }
        }

        class Dog extends Animal {
          constructor(name: string) {
            super(name); // 调用父类构造函数
            this.speed = 10; // OK: 子类可以访问和修改父类的 protected 属性
          }

          bark(): void {
            console.log("Woof! Woof!");
          }

          chase(): void {
            console.log(`${this.name} starts chasing...`);
            this.move(50); // OK: 子类可以调用父类的 protected 方法
          }

          // 可以重写 protected 方法 (如果父类没标记为 final 等)
          // protected move(distance: number): void {
          //     console.log(`Dog ${this.name} runs ${distance}m excitedly!`);
          // }
        }

        const dog = new Dog("Buddy");
        dog.bark(); // OK (public method of Dog)
        dog.chase(); // OK (public method of Dog, which calls protected move internally)
        console.log(dog.name); // OK (public property)
        // console.log(dog.speed); // Error: Property 'speed' is protected...
        // dog.move(10); // Error: Property 'move' is protected... (不能从外部实例访问)
        ```

**总结表格:**

| 修饰符         | 在定义类内部访问 | 在子类内部访问 | 通过实例从外部访问 |
| :---------- | :------- | :------ | :-------- |
| `public`    | ✅ Yes    | ✅ Yes   | ✅ Yes     |
| `protected` | ✅ Yes    | ✅ Yes   | ❌ No      |
| `private`   | ✅ Yes    | ❌ No    | ❌ No      |

**应用场景:**

*   **属性:** 控制数据的读写权限。
*   **方法:** 控制哪些操作可以被外部调用。
*   **构造函数参数 (参数属性):** `constructor(private name: string)` 这种简写会自动创建并初始化同名的私有属性 `name`。同样适用于 `public`, `protected`, `readonly`。

掌握访问修饰符是编写健壮、可维护的 TypeScript 类的关键。它们帮助你实践封装原则，构建清晰的类接口。