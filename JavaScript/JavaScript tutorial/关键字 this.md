
如果使用该函数调用 this，this将针对一个对象，否则 this将始终引用全局对象

---

```js
const user = {

  name: "Edwin",

  age: 24,

  married: false,

  purchases: ["phone", "car", "laptop"],

  sayName: function () {

    console.log(this);

    console.log(this.name);
  },
};
console.log(user);

function apples() {

  console.log("apple");

}

window.apples();

console.log(this);

user.sayName();

function myAge() {

  console.log(`my age is ${this}`);

}
myAge();
```


```js
// this = 对使用 THIS 的对象的引用 
//（对象取决于直接上下文）
const person1 = { 
	name: "Spongebob", 
	favFood: "hamburgers", 
	sayHello: function(){console.log(`Hi! I am ${this.favFood}`)}, 
	eat: function(){console.log(`${this.name} is eating ${this.favFood}`)} } 
const person2 = { 
	name: "Patrick", 
	favFood: "pizza", 
	sayHello: function(){console.log(`Hi! I am ${this.favFood}`)}, 
	eat: function(){console.log(`${this.name} is eating ${this.favFood}`)} } 
person1.eat(); 
person2.eat();
```

**this 不适用于箭头**