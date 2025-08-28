 `get` 和 `set`，它们是 JavaScript 中实现封装和创建“智能”属性的强大工具。

---

### Getters 和 Setters (访问器属性)

#### 1. 核心概念
在 JavaScript 对象中，属性可以分为两种：

1.  **数据属性 (Data Properties):** 我们最常见的属性，直接存储一个值。例如：`{ name: 'Alice' }`。
2.  **访问器属性 (Accessor Properties):** 不直接存储值，而是通过一对函数（getter 和 setter）来控制对属性的读取和写入。

*   **`get` (Getter):** 一个函数，在**读取**属性值时被调用。它不接收参数，但必须返回一个值。它让你可以运行一段代码来计算或派生出一个值，而不是简单地返回一个已存储的值。
*   **`set` (Setter):** 一个函数，在**设置**属性值时被调用。它接收你试图赋给属性的那个值作为唯一参数。它让你可以执行验证、触发更新或其他副作用。

**核心思想：** 让属性的访问和赋值看起来像普通属性一样简单 (`obj.prop`)，但在底层执行复杂的逻辑。这是一种完美的封装实践。

#### 2. 语法与示例

`get` 和 `set` 可以在**类 (Class)** 和**对象字面量 (Object Literal)** 中使用。

**示例 1: 在 Class 中使用**

这是一个非常经典的例子：一个 `User` 类，它有 `firstName` 和 `lastName`，我们想提供一个方便的 `fullName` 属性。

```javascript
class User {
  #firstName;
  #lastName;

  constructor(firstName, lastName) {
    this.#firstName = firstName;
    this.#lastName = lastName;
  }

  // Getter: 当我们访问 user.fullName 时，这个函数会被调用
  get fullName() {
    console.log('Getter for fullName is running...');
    // 它派生出一个值，而不是存储它
    return `${this.#firstName} ${this.#lastName}`;
  }

  // Setter: 当我们给 user.fullName 赋值时 (user.fullName = ...)，这个函数会被调用
  set fullName(value) {
    console.log('Setter for fullName is running...');
    if (typeof value !== 'string' || !value.includes(' ')) {
      console.error('Invalid full name format. Please provide "firstName lastName".');
      return;
    }
    // 它执行逻辑来更新内部的私有状态
    const [firstName, lastName] = value.split(' ');
    this.#firstName = firstName;
    this.#lastName = lastName;
  }

  greet() {
      console.log(`Hello, I'm ${this.#firstName}.`);
  }
}

const user = new User('John', 'Doe');

// 1. 访问 getter
// 看起来像访问一个普通属性，但实际上在执行 get fullName() 函数
console.log(user.fullName); 
// 输出:
// Getter for fullName is running...
// John Doe

// 2. 调用 setter
// 看起来像给一个普通属性赋值，但实际上在执行 set fullName(...) 函数
user.fullName = 'Jane Smith'; 
// 输出:
// Setter for fullName is running...

// 3. 再次访问 getter，验证 setter 是否生效
console.log(user.fullName); 
// 输出:
// Getter for fullName is running...
// Jane Smith

user.greet(); // 输出: Hello, I'm Jane.
```

**示例 2: 在对象字面量中使用**

```javascript
const circle = {
  radius: 5,
  
  // Getter for area
  get area() {
    return Math.PI * this.radius * this.radius;
  },

  // Setter for radius (with validation)
  set diameter(value) {
    if (value <= 0) {
      throw new Error('Diameter must be a positive number.');
    }
    this.radius = value / 2;
  }
};

console.log(circle.radius); // 5
console.log(circle.area.toFixed(2)); // 78.54 (area 是根据 radius 动态计算的)

// 使用 setter 来间接修改 radius
circle.diameter = 20;
console.log(circle.radius); // 10
console.log(circle.area.toFixed(2)); // 314.16 (area 自动更新了)
```

#### 3. 扩展与应用

1.  **数据验证 (Validation):** 这是 setter 最常见的用途。在设置一个值之前，检查它是否符合规则（如年龄必须是正数，email 格式必须正确等）。

    ```javascript
    class Product {
      #price;
      set price(value) {
        if (value < 0) {
          console.error("Price cannot be negative.");
          return;
        }
        this.#price = value;
      }
      get price() {
        return this.#price;
      }
    }
    ```

2.  **计算/派生属性 (Computed Properties):** getter 的经典用途。属性的值依赖于其他属性，当其他属性变化时，它也随之变化，如上面的 `fullName` 和 `area`。

3.  **触发副作用 (Triggering Side Effects):** 当一个属性被修改时，可能需要执行一些额外的操作，比如更新 UI、记录日志或通知其他部分。

    ```javascript
    // 在一个简单的UI组件中
    let component = {
      _text: '',
      set text(value) {
        this._text = value;
        this.render(); // 当 text 改变时，自动重新渲染
      },
      render() {
        console.log(`Rendering UI with text: ${this._text}`);
      }
    };
    component.text = 'Hello World'; // 会自动调用 render()
    ```

4.  **创建只读属性 (Read-only Properties):** 只提供 `get` 而不提供 `set`，就可以创建一个外部无法修改的属性。

    ```javascript
    class Circle {
        constructor(radius) {
            this.radius = radius;
        }
        get area() {
            return Math.PI * this.radius ** 2;
        }
    }
    const c = new Circle(10);
    console.log(c.area); // 314.159...
    c.area = 500; // 在严格模式下会报错，非严格模式下静默失败。赋值无效。
    console.log(c.area); // 仍然是 314.159...
    ```

#### 4. 要点与注意事项

1.  **无限递归陷阱 (Infinite Recursion):** 这是最常见的错误！**Getter/Setter 的名字不能与它们操作的实际数据属性同名。**

    ```javascript
    // 错误示例 ❌
    class Person {
      set name(value) {
        // 当你给 this.name 赋值时，它会再次调用 set name()
        // 导致无限循环，最终栈溢出 (stack overflow)
        this.name = value; 
      }
    }
    ```

    **正确做法：** 使用一个内部的“后备”属性来存储实际数据。通常约定使用下划线 `_` 前缀，或者在现代 JavaScript 中，最好使用**私有字段 `#`**。

    ```javascript
    // 正确示例 ✅
    class Person {
      #name; // 使用私有字段
      set name(value) {
        this.#name = value.trim();
      }
      get name() {
        return this.#name;
      }
    }
    ```

2.  **性能考量:** Getter 应该快速返回结果。因为它们看起来像属性访问，调用者不期望这里有复杂的计算、网络请求或 I/O 操作。如果需要执行耗时操作，请使用普通方法，例如 `calculateArea()` 而不是 `get area`。

3.  **签名规则:** Getter 函数不能有参数，Setter 函数必须有且只有一个参数。

4.  **不能使用箭头函数:** Getter 和 Setter 不能是箭头函数，因为它们需要自己的 `this` 绑定来指向实例对象。箭头函数会捕获其词法上下文的 `this`，这在这里是错误的。

#### 5. 新旧对比 / 相关概念

*   **`Object.defineProperty()`:** `get` / `set` 语法是 `Object.defineProperty()` 的语法糖。这个底层方法功能更强大，可以更精细地控制属性的行为（如 `enumerable`, `configurable`）。

    ```javascript
    // 使用 Object.defineProperty() 实现与上面 circle.area 类似的效果
    const obj = { radius: 5 };

    Object.defineProperty(obj, 'area', {
      get: function() {
        return Math.PI * this.radius * this.radius;
      },
      enumerable: true, // 可以在 for...in 循环中被看到
      configurable: true // 可以被删除或再次定义
    });

    console.log(obj.area); // 78.53...
    ```
    对于大多数日常开发，`get` / `set` 语法更简洁易读。

*   **Getter vs. 方法 (Method):**
    *   **语义:** Getter 应该用于**访问数据**，即使是计算得来的。方法应该用于**执行操作**。`user.fullName`（获取数据）感觉比 `user.getFullName()` 更自然。而 `user.save()`（执行动作）则应该是一个方法。
    *   **调用方式:** Getter 不需要括号 `()`，而方法需要。
    *   **参数:** Getter 不能接收参数，而方法可以。如果你需要根据参数来计算，那就必须用方法。

总结来说，`get` 和 `set` 是实现封装、保持 API 简洁和添加属性访问/赋值逻辑的绝佳工具，是现代 JavaScript 开发中非常实用的一部分。