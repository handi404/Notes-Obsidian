### 二、JavaScript 中的对象

JavaScript 的对象是包含键值对的集合，键（属性名）可以是字符串或符号，值可以是任何类型（包括另一个对象）。对象用于存储和操作结构化的数据。

#### 2.1 创建对象
JavaScript 提供了多种方式来创建对象。

##### ① 语法：
1. **对象字面量**：
   ```javascript
   let obj = {
       key1: value1,
       key2: value2
   };
   ```

2. **使用 `new Object()`**：
   ```javascript
   let obj = new Object();
   obj.key1 = value1;
   obj.key2 = value2;
   ```

3. **构造函数**：
   可以使用自定义的构造函数来创建对象。
   ```javascript
   function Person(name, age) {
       this.name = name;
       this.age = age;
   }
   let person1 = new Person("Alice", 25);
   ```

4. **`Object.create()`**：
   创建一个具有指定原型和属性的对象。
   ```javascript
   let proto = {greet: function() { console.log("Hello"); }};
   let obj = Object.create(proto);
   ```

##### ② 案例：
```javascript
let car = {
    brand: "Toyota",
    model: "Camry",
    year: 2021
};
console.log(car);
```

#### 2.2 对象属性

##### ① 定义属性：
对象的属性是以键值对的形式存储的。

- **直接定义**：
  ```javascript
  let obj = { key: value };
  ```

- **动态添加**：
  可以在对象创建之后动态添加新属性。
  ```javascript
  obj.newKey = newValue;
  ```

##### ② 操作属性：
- **访问属性**：
  可以使用点运算符（`.`）或方括号（`[]`）来访问属性。
  ```javascript
  console.log(obj.key);        // 使用点运算符
  console.log(obj['key']);     // 使用方括号
  ```

- **修改属性**：
  ```javascript
  obj.key = newValue;
  ```

- **删除属性**：
  可以使用 `delete` 关键字删除属性。
  ```javascript
  delete obj.key;
  ```

#### 2.3 对象方法

##### ① 定义方法：
对象中的方法就是赋值为函数的属性。

```javascript
let person = {
    name: "Alice",
    greet: function() {
        console.log("Hello, " + this.name);
    }
};
```

##### ② 调用方法：
```javascript
person.greet();  // 输出: "Hello, Alice"
```

#### 2.4 注意事项
- **对象是引用类型**：当你将一个对象赋值给另一个变量时，它们都指向同一个对象。
- **使用 `this`**：在对象方法中，`this` 指的是调用该方法的对象。

---

### 三、对象的补充

#### 3.1 动态成员
JavaScript 允许在对象创建后动态地向对象添加属性或方法。

```javascript
let obj = {};
obj.newProperty = "New Value";
obj.newMethod = function() { console.log("New Method"); };
```

#### 3.2 删除成员
使用 `delete` 操作符删除对象中的某个属性或方法。

```javascript
delete obj.newProperty;
```

#### 3.3 访问属性
通过点运算符或方括号运算符访问对象的属性。

```javascript
console.log(obj.property);   // 通过点运算符
console.log(obj['property']); // 通过方括号
```

#### 3.4 对象的遍历

##### ① for...in 循环：
`for...in` 循环可以遍历对象的所有可枚举属性。

```javascript
for (let key in obj) {
    console.log(key + ": " + obj[key]);
}
```

##### ② 其他遍历：
- **`Object.keys()`**：返回对象的所有可枚举属性名。
  ```javascript
  Object.keys(obj).forEach(key => {
      console.log(key + ": " + obj[key]);
  });
  ```

- **`Object.values()`**：返回对象的所有属性值。
  ```javascript
  Object.values(obj).forEach(value => {
      console.log(value);
  });
  ```

- **`Object.entries()`**：返回对象的键值对数组。
  ```javascript
  Object.entries(obj).forEach(([key, value]) => {
      console.log(key + ": " + value);
  });
  ```

---

### 四、创建对象方式

#### 4.1 原始模式
最简单的创建对象方式是通过对象字面量。

```javascript
let obj = {
    name: "Alice",
    age: 25
};
```

#### 4.2 工厂模式
通过工厂函数创建对象。工厂模式是通过定义一个函数，每次调用这个函数时返回一个新对象。

```javascript
function createPerson(name, age) {
    return {
        name: name,
        age: age,
        greet: function() {
            console.log("Hello, " + this.name);
        }
    };
}

let person1 = createPerson("Alice", 25);
let person2 = createPerson("Bob", 30);
```

#### 4.3 构造函数模式
构造函数是一种通过 `new` 关键字实例化对象的模式。在构造函数内部，`this` 代表新创建的对象。

```javascript
function Person(name, age) {
    this.name = name;
    this.age = age;
    this.greet = function() {
        console.log("Hello, " + this.name);
    };
}

let person1 = new Person("Alice", 25);
let person2 = new Person("Bob", 30);
```

构造函数模式允许通过 `new` 关键字创建多个相同类型的对象，且各个对象可以拥有不同的属性值。