`Object` 是 JavaScript 中最核心、最基础的构造函数，也是所有对象的根源。

---

### 什么是 `Object`？

在 JavaScript 中，`Object` 是一个特殊的存在，它扮演着三重角色：

1.  **一个构造函数 (Constructor):** 你可以使用 `new Object()` 来创建一个新的空对象，但这在实践中很少使用，因为对象字面量 `{}` 更简洁、更高效。
2.  **一个全局对象:** 它本身是一个对象，包含了大量用于处理对象的静态方法（如 `Object.keys()`, `Object.assign()` 等）。
3.  **原型链的顶端:** 几乎所有的 JavaScript 对象（除了特意创建的 `null` 原型对象）最终都继承自 `Object.prototype`。这意味着所有对象都能访问 `Object.prototype` 上的方法，比如 `toString()` 和 `hasOwnProperty()`。

**核心理念:** 在 JavaScript 中，除了原始类型（string, number, boolean, null, undefined, symbol, bigint），**一切皆为对象（或表现得像对象）**。

---

### `Object` 的静态方法 (Static Methods)

静态方法是直接在 `Object` 构造函数上调用的方法，而不是在对象实例上调用。我将它们按功能分组，以便于理解和记忆。

#### 组一：创建与合并对象

##### 1. `Object.create(proto, [propertiesObject])`
*   **核心概念:** 创建一个新对象，并**指定其原型**。这是实现原型式继承最直接的方式。
*   **语法与示例:**
    ```javascript
    const animal = {
      speak() {
        console.log("Makes a sound.");
      }
    };
    
    // 创建一个以 `animal` 为原型的新对象 `dog`
    const dog = Object.create(animal);
    dog.breed = 'Labrador';

    dog.speak(); // 输出: "Makes a sound." (继承自 animal)
    console.log(Object.getPrototypeOf(dog) === animal); // true

    // 创建一个没有原型的对象 (一个“纯净”的字典)
    const pureDict = Object.create(null);
    console.log(pureDict.toString); // undefined
    ```
*   **应用:** 当你需要精确控制对象的继承关系时，或者创建不继承任何默认方法（如 `toString`）的“纯净”哈希表时，这个方法非常有用。

##### 2. `Object.assign(target, ...sources)`
*   **核心概念:** 将一个或多个源对象 (`sources`) 的所有**可枚举自有属性**复制到目标对象 (`target`)。它会修改并返回 `target` 对象。
*   **语法与示例:**
    ```javascript
    const target = { a: 1, b: 2 };
    const source1 = { b: 3, c: 4 };
    const source2 = { d: 5 };

    const result = Object.assign(target, source1, source2);

    console.log(target);   // { a: 1, b: 3, c: 4, d: 5 }
    console.log(result === target); // true (target 对象被改变了)
    ```
*   **应用:**
    *   **对象合并/混入 (Mixin):** 将多个对象的属性合并到一个对象中。
    *   **浅拷贝 (Shallow Clone):** `const copy = Object.assign({}, original);`
*   **注意:** 这是**浅拷贝**！如果源对象的属性值是另一个对象，那么复制的是该对象的引用，而不是对象本身。现代开发中，更推荐使用**扩展语法 (Spread Syntax)** 来合并或浅拷贝，因为它更简洁：`const newObj = { ...target, ...source1 };`

#### 组二：检查与迭代属性

##### 3. `Object.keys(obj)`
*   **核心概念:** 返回一个由对象**自身的、可枚举的**属性名组成的**字符串数组**。
*   **示例:**
    ```javascript
    const user = { name: 'Alice', age: 30 };
    console.log(Object.keys(user)); // ['name', 'age']
    ```

##### 4. `Object.values(obj)`
*   **核心概念:** 返回一个由对象**自身的、可枚举的**属性值组成的**数组**。
*   **示例:**
    ```javascript
    const user = { name: 'Alice', age: 30 };
    console.log(Object.values(user)); // ['Alice', 30]
    ```

##### 5. `Object.entries(obj)`
*   **核心概念:** 返回一个由对象**自身的、可枚举的**属性的 `[key, value]` 键值对组成的**数组**。
*   **示例:**
    ```javascript
    const user = { name: 'Alice', age: 30 };
    console.log(Object.entries(user)); // [['name', 'Alice'], ['age', 30]]
    ```
*   **应用:** 非常适合与 `for...of` 循环或 `Map` 构造函数一起使用。
    ```javascript
    for (const [key, value] of Object.entries(user)) {
      console.log(`${key}: ${value}`);
    }
    const map = new Map(Object.entries(user));
    ```

##### 6. `Object.fromEntries(iterable)`
*   **核心概念:** `Object.entries()` 的逆操作。将一个 `[key, value]` 键值对的可迭代对象（如数组、Map）转换回一个对象。
*   **示例:**
    ```javascript
    const entries = [['name', 'Bob'], ['age', 42]];
    const person = Object.fromEntries(entries);
    console.log(person); // { name: 'Bob', age: 42 }
    ```
*   **应用:** 在对对象的键值对进行 `map`, `filter` 等数组操作后，重新组合成对象时非常有用。
    ```javascript
    const data = { a: 1, b: 2, c: 3 };
    const filteredData = Object.fromEntries(
      Object.entries(data).filter(([key, value]) => value > 1)
    );
    console.log(filteredData); // { b: 2, c: 3 }
    ```

#### 组三：控制属性特性 (高级)

每个对象属性都有一个“属性描述符”，它控制着该属性的行为。

##### 7. `Object.getOwnPropertyDescriptor(obj, prop)` & `Object.getOwnPropertyDescriptors(obj)`
*   **核心概念:** 获取一个对象自有属性的**属性描述符**。描述符包含 `value`, `writable` (可写), `enumerable` (可枚举), `configurable` (可配置)。`getOwnPropertyDescriptors` 则是获取所有自有属性的描述符。
*   **示例:**
    ```javascript
    const obj = { a: 1 };
    const descriptor = Object.getOwnPropertyDescriptor(obj, 'a');
    console.log(descriptor);
    /*
    {
      value: 1,
      writable: true,
      enumerable: true,
      configurable: true
    }
    */
    ```

##### 8. `Object.defineProperty(obj, prop, descriptor)` & `Object.defineProperties(obj, props)`
*   **核心概念:** 在一个对象上定义一个新属性，或者修改一个现有属性的特性。这是实现底层数据绑定的核心，也是 Vue 2.x 响应式系统的基石。
*   **示例:** 创建一个不可修改的属性。
    ```javascript
    const user = {};
    Object.defineProperty(user, 'id', {
      value: '123',
      writable: false, // 不可写
      enumerable: true,
      configurable: false // 不可删除，不可再次配置
    });
    console.log(user.id); // '123'
    user.id = '456'; // 在严格模式下会报错，非严格模式下静默失败
    console.log(user.id); // '123'
    ```

#### 组四：管理原型

##### 9. `Object.getPrototypeOf(obj)`
*   **核心概念:** 返回指定对象的原型。**这是获取对象原型的标准、现代方式。**
*   **示例:**
    ```javascript
    const arr = [];
    console.log(Object.getPrototypeOf(arr) === Array.prototype); // true
    ```
*   **注意:** 不要使用已废弃的 `__proto__` 属性来获取原型。

##### 10. `Object.setPrototypeOf(obj, prototype)`
*   **核心概念:** 设置一个指定对象的原型。这是一个非常消耗性能的操作，应谨慎使用。
*   **示例:**
    ```javascript
    const obj1 = {};
    const obj2 = { greet: () => 'hello' };
    Object.setPrototypeOf(obj1, obj2);
    console.log(obj1.greet()); // 'hello'
    ```

#### 组五：控制对象状态 (不变性)

##### 11. `Object.preventExtensions(obj)` & `Object.isExtensible(obj)`
*   **核心概念:** 让一个对象**不可扩展**，即不能再向其添加新属性。
*   `isExtensible()` 用于检查对象是否可扩展。

##### 12. `Object.seal(obj)` & `Object.isSealed(obj)`
*   **核心概念:** **密封**一个对象。密封的对象不可扩展，且所有现有属性都不可配置 (`configurable: false`)。但属性值是可写的 (`writable: true`)。
*   `isSealed()` 用于检查对象是否被密封。

##### 13. `Object.freeze(obj)` & `Object.isFrozen(obj)`
*   **核心概念:** **冻结**一个对象。这是最严格的级别。冻结的对象不可扩展、属性不可配置、属性值也不可写 (`writable: false`)。
*   `isFrozen()` 用于检查对象是否被冻结。
*   **示例 (Freeze):**
    ```javascript
    const config = { host: 'localhost', port: 8080 };
    Object.freeze(config);
    config.port = 3000; // 静默失败 (严格模式下报错)
    console.log(config.port); // 8080
    ```
*   **注意:** 这三个方法都是**浅操作**，只影响对象的第一层属性。如果属性值是另一个对象，那个嵌套的对象不受影响。

#### 组六：比较与检查

##### 14. `Object.is(value1, value2)`
*   **核心概念:** 判断两个值是否为**相同的值**。它与 `===` (严格相等) 行为基本一致，但修复了两个特殊情况：
    1.  `Object.is(NaN, NaN)` 返回 `true` (而 `NaN === NaN` 是 `false`)。
    2.  `Object.is(-0, +0)` 返回 `false` (而 `-0 === +0` 是 `true`)。
*   **示例:**
    ```javascript
    console.log(Object.is(5, 5));         // true
    console.log(Object.is(NaN, NaN));     // true
    console.log(NaN === NaN);             // false
    ```
*   **应用:** 当你需要绝对精确地比较两个值，特别是在处理 `NaN` 时，`Object.is` 是更好的选择。

##### 15. `Object.hasOwn(obj, prop)`
*   **核心概念:** **（ES2022 最新推荐）** 判断一个对象是否拥有指定的**自有属性**。这是对实例方法 `obj.hasOwnProperty()` 的更健壮、更推荐的替代方案。
*   **为什么更好？**
    1.  它能正确处理原型为 `null` 的对象 (`Object.create(null)`)。
    2.  它不会被对象上可能存在的同名 `hasOwnProperty` 属性所覆盖。
*   **示例:**
    ```javascript
    const user = { name: 'Alice' };
    console.log(Object.hasOwn(user, 'name'));     // true
    console.log(Object.hasOwn(user, 'toString')); // false (toString 是继承的)

    // 老方法的痛点
    const objWithNullProto = Object.create(null);
    objWithNullProto.foo = 'bar';
    // objWithNullProto.hasOwnProperty('foo'); // TypeError: is not a function
    console.log(Object.hasOwn(objWithNullProto, 'foo')); // true (新方法完美工作)
    ```

---

### `Object` 的实例方法 (Instance Methods)

这些方法定义在 `Object.prototype` 上，因此几乎所有对象都可以调用它们。

*   **`obj.hasOwnProperty(prop)`:** 检查属性是否为对象的自有属性。**请优先使用 `Object.hasOwn()`**。
*   **`obj.toString()`:** 返回对象的字符串表示。默认返回 `"[object Type]"`，如 `[object Object]`, `[object Array]`。许多内置对象（如 `Array`, `Date`）都重写了此方法以提供更有用的信息。
*   **`obj.isPrototypeOf(object)`:** 检查一个对象是否存在于另一个对象的原型链上。

---

### 要点与注意事项总结

1.  **首选字面量:** 使用 `{}` 创建对象，而不是 `new Object()`。
2.  **拥抱现代语法:**
    *   用**扩展语法 `...`** 进行对象合并和浅拷贝，它比 `Object.assign()` 更简洁。
    *   用 **`Object.hasOwn()`** 替代 `obj.hasOwnProperty()`。
3.  **注意浅操作:** `Object.assign()`, `...` spread, `Object.freeze()` 等都是**浅操作**。处理深度嵌套的对象时要特别小心，可能需要使用第三方库（如 lodash 的 `cloneDeep`）或自己实现深拷贝。
4.  **迭代方式的区别:**
    *   `for...in`：遍历对象及其原型链上所有**可枚举**的属性。
    *   `Object.keys/values/entries`：只遍历对象**自身**的**可枚举**属性。
    *   `Object.getOwnPropertyNames`：遍历对象**自身**的所有属性，**包括不可枚举**的（但不包括 Symbol 属性）。
    *   `Object.getOwnPropertySymbols`：只遍历对象**自身**的 Symbol 属性。
5.  **`Object.create(null)` 的威力:** 当你需要一个纯粹的、不受原型链污染的键值对存储（如用作缓存或字典）时，这是一个绝佳选择。