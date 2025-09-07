`map`, `filter`, 和 `reduce` 是现代 JavaScript (尤其是函数式编程风格) 的三大基石。它们都是数组的实例方法，核心思想都是**对数组进行遍历和处理，并返回一个新值，同时保持原数组不变（Immutability）**。

---

### 核心比喻：工厂流水线

想象一个加工产品的流水线：

*   **`filter()` (筛选工序):** 这是**质检员**。他站在传送带旁，检查每个送来的产品。符合标准（`return true`）的留下，不符合的（`return false`）直接丢掉。**产品的数量可能会减少，但留下来的产品本身没有变化。**
*   **`map()` (加工工序):** 这是**加工员**。他把传送带上送来的每个产品都拿起来，进行一次加工（例如，喷漆、贴标签）。他**不会丢弃任何产品**，只是把它们变成新的样子再放回传送带。**产品的数量不变，但每个产品都可能变成了新模样。**
*   **`reduce()` (打包工序):** 这是**打包员**。他站在传送带的尽头，把所有送来的产品（无论是原始的还是加工过的）**全部收集起来，放进一个大箱子里**，最终只产出一个最终成品（这个成品可以是一个总价、一个分类好的包裹、任何你想要的东西）。

---

### 1. `Array.prototype.map()` - 转换/映射

#### 核心概念

`map` 方法用于**转换**数组中的每个元素，并返回一个包含所有转换后结果的**新数组**。它像一个映射关系，将原数组的每个值映射成一个新值。

#### 语法与参数

```javascript
const newArray = array.map(callback(currentValue, index, array));
```

*   **`callback`**: 为数组中每个元素执行的函数。
    *   **`currentValue`**: (必需) 当前正在处理的元素。
    *   **`index`**: (可选) 当前元素的索引。
    *   **`array`**: (可选) 调用 `map` 的数组本身。
*   **返回值**: 一个**新的数组**，其长度与原数组完全相同。

#### 工作流程

1.  `map` 创建一个空的临时数组。
2.  它遍历原数组中的每一个元素。
3.  对于每个元素，它调用你提供的 `callback` 函数。
4.  它将 `callback` 函数的**返回值**推入临时数组。
5.  遍历结束后，返回这个填充完毕的临时数组。

#### 实战应用

**场景：从一个用户对象数组中，提取所有用户的姓名。**

```javascript
const users = [
  { id: 1, name: "Alice", age: 30 },
  { id: 2, name: "Bob", age: 25 },
  { id: 3, name: "Charlie", age: 35 },
];

// 使用 map 提取姓名
const userNames = users.map(user => user.name);

console.log(userNames); // ["Alice", "Bob", "Charlie"]
console.log(users);     // 原数组 users 保持不变
```

---

### 2. `Array.prototype.filter()` - 筛选

#### 核心概念

`filter` 方法用于**筛选**数组中的元素，并返回一个包含所有通过测试的元素的**新数组**。

#### 语法与参数

```javascript
const newArray = array.filter(callback(currentValue, index, array));
```

*   **`callback`**: 为数组中每个元素执行的测试函数。
    *   **必须返回一个布尔值** (`true` 或 `false`)。
*   **返回值**: 一个**新的数组**，只包含那些让 `callback` 返回 `true` 的元素。其长度小于或等于原数组。

#### 工作流程

1.  `filter` 创建一个空的临时数组。
2.  它遍历原数组中的每一个元素。
3.  对于每个元素，它调用你提供的 `callback` 测试函数。
4.  如果 `callback` 返回 `true`，它就将**原元素**推入临时数组。
5.  如果返回 `false`，则忽略该元素。
6.  遍历结束后，返回这个填充完毕的临时数组。

#### 实战应用

**场景：从用户对象数组中，筛选出所有年龄大于等于 30 岁的用户。**

```javascript
const users = [
  { id: 1, name: "Alice", age: 30 },
  { id: 2, name: "Bob", age: 25 },
  { id: 3, name: "Charlie", age: 35 },
];

// 使用 filter 筛选用户
const matureUsers = users.filter(user => user.age >= 30);

console.log(matureUsers);
// [
//   { id: 1, name: "Alice", age: 30 },
//   { id: 3, name: "Charlie", age: 35 }
// ]
console.log(users); // 原数组 users 保持不变
```

---

### 3. `Array.prototype.reduce()` - 聚合/归约

#### 核心概念

`reduce` 是这三者中最强大的一个。它接收一个函数作为累加器，将数组中的每个值（从左到右）合并，最终计算为一个**单一的值**。

#### 语法与参数

```javascript
const result = array.reduce(callback(accumulator, currentValue, index, array), initialValue);
```

*   **`callback`**: 为数组中每个元素执行的 "reducer" 函数。
    *   **`accumulator` (累加器)**: (必需) 累计回调的返回值。它是上一次回调返回的累积值，或 `initialValue`。
    *   **`currentValue` (当前值)**: (必需) 数组中正在处理的元素。
*   **`initialValue` (初始值)**: (可选) **强烈推荐提供！** 作为第一次调用 `callback` 时 `accumulator` 的值。如果没有提供，`accumulator` 将使用数组的第一个元素，`currentValue` 将从第二个元素开始。

#### 工作流程

1.  **检查 `initialValue`：**
    *   **如果提供了 `initialValue`：** `accumulator` 初始化为 `initialValue`，并从数组的第一个元素 (`index = 0`) 开始遍历。
    *   **如果未提供 `initialValue`：** `accumulator` 初始化为数组的第一个元素 (`array[0]`)，并从第二个元素 (`index = 1`) 开始遍历。
2.  对于数组中的每个元素，执行 `callback` 函数。
3.  `callback` 的返回值会成为**下一次**迭代的 `accumulator`。
4.  遍历结束后，返回最终的 `accumulator` 值。

#### 实战应用

**场景 1：计算数组中所有数字的总和。**

```javascript
const numbers = [1, 2, 3, 4, 5];

const sum = numbers.reduce((accumulator, currentValue) => {
  // 在每次迭代中，将当前值加到累加器上
  return accumulator + currentValue;
}, 0); // 0 是 initialValue

console.log(sum); // 15
```

**场景 2 (进阶)：将商品数组按类别分组。**

```javascript
const products = [
  { name: "Laptop", category: "Electronics" },
  { name: "T-shirt", category: "Apparel" },
  { name: "Phone", category: "Electronics" },
  { name: "Jeans", category: "Apparel" },
];

const groupedProducts = products.reduce((acc, product) => {
  const category = product.category;
  // 如果累加器对象中还没有这个类别，就创建一个空数组
  if (!acc[category]) {
    acc[category] = [];
  }
  // 将当前商品推入对应类别的数组
  acc[category].push(product);
  return acc; // 必须返回更新后的累加器
}, {}); // 初始值是一个空对象

console.log(groupedProducts);
/*
{
  Electronics: [
    { name: 'Laptop', category: 'Electronics' },
    { name: 'Phone', category: 'Electronics' }
  ],
  Apparel: [
    { name: 'T-shirt', category: 'Apparel' },
    { name: 'Jeans', category: 'Apparel' }
  ]
}
*/
```

---

### 四、三者结合：优雅的链式调用

`map`, `filter`, `reduce` 的真正威力在于它们可以被链接在一起，形成一条清晰的数据处理管道。

**需求：计算出所有在售的（`inStock: true`）、价格高于 20 的商品的总价。**

```javascript
const items = [
  { name: "Book", price: 15, inStock: true },
  { name: "Pen", price: 5, inStock: false },
  { name: "Notebook", price: 25, inStock: true },
  { name: "Mouse", price: 50, inStock: true },
];

const totalPrice = items
  .filter(item => item.inStock && item.price > 20) // 1. 筛选：先找出符合条件的商品
  // 此时流转到下一步的数据是: [{ name: "Notebook", ... }, { name: "Mouse", ... }]
  
  .map(item => item.price) // 2. 映射：再从商品中提取出价格
  // 此时流转到下一步的数据是: [25, 50]

  .reduce((total, price) => total + price, 0); // 3. 聚合：最后将价格相加

console.log(totalPrice); // 75
```
这个链式调用清晰地表达了业务逻辑的每一步，可读性极高。

### 五、要点 & 最佳实践

1.  **不可变性 (Immutability):** 永远记住 `map`, `filter`, `reduce` 都不会改变原数组。这是它们最核心的优点之一，可以避免很多意外的副作用 (side effects)。
2.  **纯函数 (Pure Functions):** 传递给这些方法的回调函数最好是纯函数（相同的输入总有相同的输出，且没有副作用），这会让你的代码更可预测、更易于测试。
3.  **`reduce` 的 `initialValue`:** **永远为 `reduce` 提供初始值！** 如果不提供，当处理一个空数组时，代码会抛出 `TypeError`。提供初始值能让你的代码更健壮。
4.  **选择正确的工具:**
    *   只想遍历数组执行操作，不关心返回值？用 `forEach`。
    *   需要转换数组，得到一个长度相同的新数组？用 `map`。
    *   需要从数组中挑选一部分元素？用 `filter`。
    *   需要将整个数组“浓缩”成一个值（数字、对象、数组等）？用 `reduce`。