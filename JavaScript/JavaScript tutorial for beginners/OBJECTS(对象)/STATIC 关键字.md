###### // static = 定义所属属性或方法的关键字 
###### // 类本身而不是创建的对象 
###### // class拥有任何 static内容，而不是对象
###### //static 修饰的可以用 class 直接调用

---

```js
// ------------ EXAMPLE 1 ------------ 
class MathUtil{ 
	static PI = 3.14159; 
	static getDiameter(radius){ 
		return radius * 2; 
	} 
	static getCircumference(radius){ 
		return 2 * this.PI * radius; 
	} 
	static getArea(radius){ 
		return this.PI * radius * radius; 
	} 
} 
console.log(MathUtil.PI); 
console.log(MathUtil.getDiameter(10)); console.log(MathUtil.getCircumference(10)); console.log(MathUtil.getArea(10)); 

// ------------ EXAMPLE 2 ------------ 
class User{ 
	static userCount = 0; 
	constructor(username){ 
		this.username = username; 
		User.userCount++; //跟踪user数量
	} 
	static getUserCount(){ 
		console.log(`There are ${User.userCount} users online`); 
	} 
	sayHello(){ 
		console.log(`Hello, my username is ${this.username}`); 
	} 
} 
const user1 = new User("Spongebob"); 
const user2 = new User("Patrick"); 
const user3 = new User("Sandy"); 
user1.sayHello(); 
user2.sayHello(); 
user3.sayHello(); 
User.getUserCount();

```