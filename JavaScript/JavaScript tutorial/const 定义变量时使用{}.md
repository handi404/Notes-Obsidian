1 [ES6解构赋值]

含义：ES6允许按照一定的模式，从数组和对象中提取值，对变量进行赋值，这种操作称为解构（destructuring）

```javascript
const {a}=b;

//等价于

a=b.a;
```

2 有这样几个解构的例子

```javascript
const a ={a:1};

console.log(a);//1
```

```js
const e={a:1,b:2};

const a=e.a;

console.log(a);//1
```

3 所以可以这样理解，当使用{ }来括住变量时，这个变量是右侧对象的某一属性或数组中的某一元素。