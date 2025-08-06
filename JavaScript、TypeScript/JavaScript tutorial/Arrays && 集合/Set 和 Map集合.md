在 js 中 List 集合为数组
### Set集合

> JavaScript 的 Set（集合）是一组唯一值的集合。
> 每个值只能在 Set 中出现一次。
> Set 可以容纳任何数据类型的值。

#### ① 创建Set集合

```js
//方式1：使用数组创建
const letters = new Set(["a","b","c"]);

//方式2：创建空Set，然后添加数据
const letters = new Set();
letters.add("a");
letters.add("b");
letters.add("c");
```

#### ② 常见方法

| 方法        | 描述             |
| --------- | -------------- |
| add()     | 向 Set 中添加新元素。  |
| delete()  | 从 Set 中移除元素。   |
| has()     | 如果值存在则返回 true。 |
| clear()   | 从 Set 中移除所有元素。 |
| forEach() | 为每个元素调用回调函数。   |
#### ③ 常见属性

| 属性   | 描述             |
| ---- | -------------- |
| size | 返回 Set 中元素的数量。 |

#### ④ Set遍历

```js
//1.创建Set集合
let s = new Set();
	
//2 添加
s.add(1);
s.add(2);
s.add(3);
s.add(4);
s.add(5);

//2.5 遍历forEach(每遍历一个元素就执行一次方法)
s.forEach(function(val){
  console.log("集合中的元素：" + val);
});
```

#### ⑤ 练习题

1. 创建一个 `Set` 对象 `mySet`，并使用 `add()` 方法向其中添加数字 `1`、`2`、`3`、`4`、`5`。然后使用 `has()` 方法检查数字 `3` 是否在集合中，并将结果打印到控制台。
2. 创建一个新的 `Set` 对象 `newSet`，向其中添加字符串 `"apple"`、`"banana"`、`"orange"`。使用 `delete()` 方法删除 `"banana"`，然后使用 `forEach()` 方法遍历集合，并将每个元素打印到控制台。
3. 创建一个 `Set` 对象 `numSet`，添加一些随机整数。使用 `size` 属性获取集合的元素个数，并将其打印到控制台。然后，使用 `clear()` 方法清空集合，再次打印集合的 `size` 属性值。


### Map集合

> Map 保存键值对，其中键可以是任何数据类型。
> Map 会记住键的原始插入顺序。
> Map 提供表示映射大小的属性。

#### ① 创建Map集合

```js
//方式1：直接创建
const fruits = new Map([
  ["apples", 500],
  ["bananas", 300],
  ["oranges", 200]
]);

//方式2：创建空Map，然后添加数据
const fruits = new Map();
fruits.set("apples", 500);
fruits.set("bananas", 300);
fruits.set("oranges", 200);
```

#### ② 常见方法

| 方法        | 描述                     |
| --------- | ---------------------- |
| set()     | 为 Map 中的键设置值。          |
| get()     | 获取 Map 对象中键的值。         |
| clear()   | 从 Map 中移除所有元素。         |
| delete()  | 删除由某个键指定的 Map 元素。      |
| has()     | 如果键存在于 Map 中，则返回 true。 |
| forEach() | 为 Map 中的每个键/值对调用回调函数。  |

#### ③ 常见属性

| 属性   | 描述           |
| ---- | ------------ |
| size | 返回Map中元素的数量。 |
#### ④ 练习题

1. 创建一个 `Map` 对象 `myMap`，使用 `set()` 方法添加以下键值对：`{"name": "John", "age": 25, "city": "New York"}`。然后使用 `get()` 方法获取 `name` 对应的值，并将其打印到控制台。
2. 创建一个 `Map` 对象 `numberMap`，使用 `set()` 方法添加一些键值对，键为整数，值为对应的平方。然后使用 `has()` 方法检查键 `5` 是否存在于 `Map` 中，如果存在，使用 `delete()` 方法删除它，并打印 `Map` 的 `size` 属性值。
3. 创建一个 `Map` 对象 `fruitMap`，添加几种水果及其颜色的键值对。使用 `forEach()` 方法遍历 `Map`，并将每个水果及其颜色打印到控制台，格式为 `"水果: 颜色"`。