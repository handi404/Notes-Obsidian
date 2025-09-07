剖析 JavaScript 中两个最基础也是最重要的数据集合：**数组 (Array)** 和 **集合 (Set)**。

---

## 一、数组 (Array)

### 1. 核心概念 (Core Concept)

把数组想象成一个**带编号的储物柜**。

*   它是一个**有序**的集合，每个物品（元素）都有自己固定的柜子编号（索引 `index`），从 0 开始。
*   你可以存放**任何类型**的物品（数字、字符串、对象、甚至其他数组）。
*   它是 JavaScript 中使用最频繁、功能最丰富的数据结构。

### 2. 创建方式 (Creation Methods)

1.  **数组字面量 (Array Literal) - (🔥 最佳实践)**
    *   最简洁、最常用、性能最好的方式。
    ```javascript
    const fruits = ["Apple", "Banana", "Cherry"];
    const emptyArray = [];
    const mixedArray = [1, "hello", { id: 1 }, true];
    ```

2.  **`Array` 构造函数 (Array Constructor)**
    *   通常不推荐，因为其行为根据参数数量不同而有歧义。
    ```javascript
    // 传入单个数字：创建一个指定长度的“稀疏数组”（空插槽）
    const arr1 = new Array(3); // [ <3 empty items> ], length is 3

    // 传入多个参数：创建包含这些元素的数组
    const arr2 = new Array("Apple", "Banana"); // ["Apple", "Banana"]
    ```

3.  **静态方法 `Array.from()` (非常强大)**
    *   用于将**类数组对象 (Array-like Objects)** 或 **可迭代对象 (Iterables)** 转换为一个真正的数组。
    ```javascript
    // 从字符串创建
    Array.from("hello"); // ["h", "e", "l", "l", "o"]

    // 从 Set 创建
    const mySet = new Set([1, 2, 3]);
    Array.from(mySet); // [1, 2, 3]

    // 从 NodeList (DOM 操作) 创建
    // const elements = document.querySelectorAll('div');
    // const divArray = Array.from(elements);

    // 使用映射函数 (map function) - 简洁高效
    // 创建一个从 1 到 5 的数组
    Array.from({ length: 5 }, (value, index) => index + 1); // [1, 2, 3, 4, 5]
    ```

4.  **静态方法 `Array.of()`**
    *   解决了 `new Array()` 的歧义，无论参数是什么，都创建一个包含这些参数的数组。
    ```javascript
    Array.of(3); // [3] (对比 new Array(3))
    Array.of(1, 2, 3); // [1, 2, 3]
    ```

### 3. 实例属性 (Instance Properties)

*   **`length`**: 获取或**设置**数组中元素的数量。这是数组唯一的、最重要的属性。
    ```javascript
    const arr = [1, 2, 3];
    console.log(arr.length); // 3

    // 设置 length 可以截断或扩展数组
    arr.length = 2;
    console.log(arr); // [1, 2]
    arr.length = 5;
    console.log(arr); // [1, 2, <3 empty items>]
    ```

### 4. 实例方法 (Instance Methods)

方法众多，我们按功能和**是否改变原数组 (mutability)** 分类，这在现代开发中至关重要。

#### 🚨 A. 改变原数组 (Mutator Methods)

这些方法会直接修改调用它们的数组。在函数式编程或 React/Vue 等状态管理中要**谨慎使用**。

*   `push(item1, ...)`: 在数组**末尾**添加一个或多个元素，返回新长度。
*   `pop()`: **移除**数组末尾的最后一个元素，返回被移除的元素。
*   `unshift(item1, ...)`: 在数组**开头**添加一个或多个元素，返回新长度。
*   `shift()`: **移除**数组开头的第一个元素，返回被移除的元素。
*   `splice(start, deleteCount, item1, ...)`: **万能方法**。从 `start` 索引开始，删除 `deleteCount` 个元素，并可以插入新元素。返回被删除元素的数组。
*   `sort(compareFunction)`: 对数组元素进行**排序**。默认按字符串的 Unicode 码位排序，通常需要提供比较函数。
*   `reverse()`: **反转**数组中元素的顺序。
*   `fill(value, start, end)`: 用一个固定值 `value` 填充数组中从 `start` 到 `end` 的部分。
*   `copyWithin(target, start, end)`: 浅拷贝数组的一部分到同一数组的另一个位置。

#### ✅ B. 不改变原数组 (Accessor/Iteration Methods) - (🔥 最佳实践)

这些方法返回新值或新数组，原数组保持不变。

**1. 返回新数组的方法:**

*   `map(callback)`: **转换**。遍历数组，对每个元素执行 `callback` 函数，将结果收集到一个**新数组**中返回。
    ```javascript
    [1, 2, 3].map(n => n * 2); // [2, 4, 6]
    ```
*   `filter(callback)`: **筛选**。返回一个**新数组**，包含所有通过 `callback` 函数测试（返回 `true`）的元素。
    ```javascript
    [1, 2, 3, 4].filter(n => n % 2 === 0); // [2, 4]
    ```
*   `slice(start, end)`: **切片**。返回一个从 `start` 到 `end`（不包含 `end`）的新数组。
*   `concat(arr1, ...)`: **合并**。连接两个或多个数组，返回一个新数组。
*   `flat(depth)`: **扁平化**。按指定深度 `depth`（默认为 1）展开嵌套数组，返回一个新数组。
*   `flatMap(callback)`: 等同于先 `map()` 再 `flat(1)`，但效率更高。

**2. 迭代与查找方法:**

*   `forEach(callback)`: 遍历数组，对每个元素执行 `callback`，**没有返回值** (`undefined`)。
*   `reduce(callback, initialValue)`: **聚合**。对数组所有元素执行一个 “reducer” 函数，将其减少为单个值（例如求和、求积）。
    ```javascript
    [1, 2, 3, 4].reduce((accumulator, currentValue) => accumulator + currentValue, 0); // 10
    ```
*   `find(callback)`: 返回数组中第一个满足 `callback` 函数的**元素**，否则返回 `undefined`。
*   `findIndex(callback)`: 返回数组中第一个满足 `callback` 函数的**元素的索引**，否则返回 `-1`。
*   `includes(value, fromIndex)`: 判断数组是否包含某个值，返回 `true` 或 `false`。比 `indexOf` 更直观。
*   `some(callback)`: 测试数组中**是否有至少一个**元素通过 `callback` 测试。返回 `true` 或 `false`。
*   `every(callback)`: 测试数组中**是否所有**元素都通过 `callback` 测试。返回 `true` 或 `false`。
*   `at(index)`: (ES 2022) 获取指定索引的元素，支持**负索引**（从后往前数）。
    ```javascript
    const arr = [10, 20, 30];
    arr.at(0);  // 10
    arr.at(-1); // 30 (非常方便！)
    ```
*   `join(separator)`: 将数组所有元素连接成一个字符串。

---

## 二、集合 (Set)

### 1. 核心概念 (Core Concept)

把 Set 想象成一个**无重复的贵宾名单**。

*   它是一个**无序**（严格来说是按插入顺序迭代）的集合。
*   最重要的特性：**所有成员的值都是唯一的**。
*   主要用于**去重**和**快速判断成员是否存在**。

### 2. 创建方式 (Creation Methods)

*   **`Set` 构造函数 (Set Constructor)**
    ```javascript
    // 创建一个空 Set
    const mySet = new Set();

    // 从可迭代对象创建 (最常用)
    const numbers = [1, 2, 2, 3, 4, 4];
    const uniqueNumbers = new Set(numbers); // Set(4) { 1, 2, 3, 4 }
    ```
    **🔥 经典应用：数组去重**
    ```javascript
    const uniqueArray = [...new Set(numbers)]; // [1, 2, 3, 4]
    // or
    const uniqueArray2 = Array.from(new Set(numbers)); // [1, 2, 3, 4]
    ```

### 3. 实例属性 (Instance Properties)

*   **`size`**: 返回 Set 实例的成员总数（类似于数组的 `length`）。

### 4. 实例方法 (Instance Methods)

Set 的方法更专注于成员管理，而非顺序操作。

*   `add(value)`: 向 Set 中添加一个新元素。如果元素已存在，则什么都不做。返回该 Set 对象本身，**支持链式调用**。
    ```javascript
    mySet.add(1).add(5).add("some text");
    ```
*   `has(value)`: 判断 Set 中是否存在某个值，返回 `true` 或 `false`。**性能极高 (O(1) 复杂度)**。
*   `delete(value)`: 从 Set 中删除指定元素。如果删除成功，返回 `true`；否则返回 `false`。
*   `clear()`: 移除 Set 中所有元素。

**迭代方法 (Set 是可迭代的):**

*   `forEach(callback)`: 遍历 Set。`callback` 接收三个参数 `(value, key, set)`，其中 `value` 和 `key` 的值相同，这是为了与 `Map` 的 API 保持一致。
*   `values()`: 返回一个新的迭代器对象，包含 Set 中所有**值**。
*   `keys()`: `keys()` 是 `values()` 的别名，行为完全相同。
*   `entries()`: 返回一个新的迭代器对象，包含 `[value, value]` 形式的数组，同样是为了与 `Map` 保持兼容。

---

### 三、横向对比：Array vs. Set (何时使用？)

| 特性 | 数组 (Array) | 集合 (Set) |
| :--- | :--- | :--- |
| **唯一性** | 允许重复元素 | **元素唯一** |
| **顺序** | **有序**，按索引 `0, 1, 2...` | **有序** (按插入顺序)，但无索引 |
| **访问** | 通过索引快速访问 `arr[i]` (O(1)) | 只能通过迭代或 `has()` 检查 |
| **查找性能** | `includes()` / `indexOf()` 性能为 O(n) | `has()` 性能极高，为 O(1) 平均 |
| **主要用途** | 存储有序列表，需要按位置操作数据 | 保证元素唯一性，快速查找成员，数组去重 |

**一句话决策：**

*   当你需要一个**有序列表**，并且关心元素的位置（索引）时，使用 **Array**。
*   当你需要存储**不重复的值**，并且频繁进行**存在性检查**时，使用 **Set**。它在性能上远超数组的 `includes`。

---

## 三、映射 (Map)

### 1. 核心概念 (Core Concept)

如果说数组是“带编号的储物柜”，那么 Map 就是一个 **“万能钥匙储物柜”**。

*   它是一个**键值对 (key-value pairs) 的集合**，类似于我们常用的 `Object`。
*   **核心优势：** 它的**键 (key) 可以是任意类型**，包括对象、函数、原始类型等，而不仅仅是字符串或 Symbol。
*   它**保持键的插入顺序**，这意味着当你迭代 Map 时，元素的顺序与你插入它们的顺序是一致的。

### 2. 创建方式 (Creation Methods)

*   **`Map` 构造函数 (Map Constructor)**
    ```javascript
    // 创建一个空 Map
    const myMap = new Map();

    // 从一个可迭代的键值对数组创建 (最常用)
    const initialData = [
      ['name', 'Alice'],
      [30, 'age'], // key 是数字
      [true, 'isStudent'], // key 是布尔值
      [{ id: 1 }, 'userObject'] // key 是对象！
    ];
    const userMap = new Map(initialData);
    
    console.log(userMap.get({ id: 1 })); // undefined (后面解释为什么)
    ```

    **注意：** 上述代码中 `userMap.get({ id: 1 })` 返回 `undefined` 是因为 `{ id: 1 } !== { id: 1 }`。它们是两个不同的对象引用。你需要用同一个对象引用来操作：

    ```javascript
    const userKey = { id: 1 };
    const userMap = new Map([
      [userKey, 'userObject']
    ]);
    console.log(userMap.get(userKey)); // "userObject" (正确！)
    ```

### 3. 实例属性 (Instance Properties)

*   **`size`**: 返回 Map 实例中键值对的数量。

### 4. 实例方法 (Instance Methods)

Map 的方法清晰地围绕着“增删改查”和“迭代”展开。

*   `set(key, value)`: 向 Map 中**添加或更新**一个键值对。返回该 Map 对象本身，**支持链式调用**。
    ```javascript
    myMap.set('name', 'Bob').set('city', 'New York');
    ```
*   `get(key)`: 获取指定 `key` 对应的 `value`。如果 `key` 不存在，返回 `undefined`。
*   `has(key)`: 判断 Map 中是否存在指定的 `key`，返回 `true` 或 `false`。**性能极高 (O(1) 复杂度)**。
*   `delete(key)`: 从 Map 中删除指定的键值对。如果删除成功，返回 `true`；否则返回 `false`。
*   `clear()`: 移除 Map 中所有的键值对。

**迭代方法 (Map 是可迭代的):**

*   `forEach(callback)`: 按插入顺序遍历 Map。`callback` 接收三个参数 `(value, key, map)`。注意 `value` 在前，`key` 在后。
*   `keys()`: 返回一个新的**迭代器对象**，包含 Map 中所有的**键 (keys)**。
*   `values()`: 返回一个新的**迭代器对象**，包含 Map 中所有的**值 (values)**。
*   `entries()`: 返回一个新的**迭代器对象**，包含 Map 中所有的 `[key, value]` 数组。`for...of` 循环默认使用的就是这个迭代器。

```javascript
const userMap = new Map([['name', 'Alice'], ['age', 30]]);

// 遍历 Map (最佳实践)
for (const [key, value] of userMap) {
  console.log(`${key}: ${value}`);
}
// 输出:
// name: Alice
// age: 30

// 获取所有键
console.log([...userMap.keys()]); // ['name', 'age']

// 获取所有值
console.log([...userMap.values()]); // ['Alice', 30]
```

---

### 四、横向对比：Map vs. Object (终极对决)

在 ES6 之前，我们一直用 `Object` 来模拟 `Map` 的功能。但 `Map` 的出现是专门为了解决 `Object` 作为键值存储的诸多不足。

| 特性                   | Map                         | Object                                                  |
| :------------------- | :-------------------------- | :------------------------------------------------------ |
| **键的类型 (Key Type)**  | **任意类型** (对象, 函数, etc.)     | 只能是 `string` 或 `Symbol` (其他类型会被强制转为字符串)                 |
| **键的顺序 (Key Order)** | **保持插入顺序**                  | ES 2015+ 后部分有序，但规则复杂且不完全可靠                              |
| **大小 (Size)**        | 通过 `.size` 属性直接获取 (O(1))    | 需通过 `Object.keys().length` 计算 (O(n))                    |
| **迭代 (Iteration)**   | **直接可迭代** (`for...of`)      | 默认不可迭代，需用 `Object.keys/values/entries` 辅助               |
| **性能 (Performance)** | 在**频繁增删**键值对的场景下，性能通常**更优** | 在少量、固定的属性访问时性能极佳                                        |
| **原型继承 (Prototype)** | 纯粹的数据集合，没有原型链上的意外属性         | 继承自 `Object.prototype`，可能存在原型链污染风险 (`hasOwnProperty` 等) |

**代码对比揭示差异：**

```javascript
const obj = {};
const map = new Map();

const keyObject = { id: 1 };
const keyFunc = () => {};

// Object 的键被强制转为字符串
obj[keyObject] = 'value for object key'; // obj['[object Object]'] = '...'
obj[keyFunc] = 'value for function key';   // obj['() => {}'] = '...'
console.log(Object.keys(obj)); // ["[object Object]", "() => {}"] (键已失真)

// Map 保持键的原始类型
map.set(keyObject, 'value for object key');
map.set(keyFunc, 'value for function key');

console.log(map.get(keyObject)); // 'value for object key' (完美)
```

**一句话决策：**

*   当你需要一个**纯粹的、键值类型灵活的数据字典**时，尤其是在键的类型不确定，或者需要频繁增删改查的场景下，**优先使用 `Map`**。
*   当你需要创建一个**结构固定、属性名确定**的普通对象，或者需要用到 `JSON.stringify()` 进行序列化时（`JSON` 不支持 `Map`），使用 **`Object`**。

---

### 补充：`WeakMap` & `WeakSet`

这两个是 `Map` 和 `Set` 的“弱引用”版本，是更高级的特性，主要用于解决特定的内存管理问题。

*   **核心概念：**
    *   `WeakMap` 的**键必须是对象**，`WeakSet` 的成员也必须是对象。
    *   它们对键/成员的引用是**弱引用 (Weak Reference)**。这意味着，如果一个对象只被 `WeakMap` 或 `WeakSet` 引用，而没有被其他任何地方强引用，垃圾回收机制 (GC) 就可以**自动回收这个对象**，`WeakMap` / `WeakSet` 中对应的条目也会自动消失。
*   **特性：**
    *   由于垃圾回收时机不确定，它们是**不可迭代的**（没有 `forEach`, `keys`, `values`, `entries`, `size` 等方法）。你无法获知里面到底有多少成员。
*   **实战应用场景：**
    *   **缓存私有数据：** 在一个对象上附加一些额外信息，而又不希望这些信息阻止该对象被垃圾回收。
        ```javascript
        // 场景：为一个 DOM 元素附加一些元数据，当该 DOM 元素被移除时，
        // 我们不希望元数据还残留在内存中。
        const elementMetadata = new WeakMap();
        
        const myButton = document.querySelector('#my-button');
        
        elementMetadata.set(myButton, { clickCount: 0 });
        
        // 当 myButton 从 DOM 中被移除，且没有其他地方引用它时，
        // 垃圾回收器会回收 myButton，elementMetadata 中对应的条目也会被自动清除。
        ```
    *   **框架和库中用于存储与对象相关的状态**，而又不影响宿主应用的内存生命周期。Vue 的响应式系统内部就巧妙地运用了 `WeakMap`。

**总结**

`Array`, `Set`, `Map` 构成了现代 JavaScript 中处理数据集合的“三驾马车”。理解它们的特性、差异和适用场景，是编写高效、健壮、可读性强的代码的基础。而 `WeakMap` 和 `WeakSet` 则是解决特定内存泄漏问题的“银弹”。