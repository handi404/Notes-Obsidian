
如果使用该函数调用 this，this将针对一个对象，否则 this将始终引用全局对象
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
