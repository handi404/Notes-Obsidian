# 目录

- [[#一、函数介绍|一、函数介绍]]
- [[#二、函数的声明|二、函数的声明]]
- [[#三、函数内部属性|三、函数内部属性]]
	- [[#三、函数内部属性#arguments|arguments]]
			- [[#callee 属性|callee 属性]]
			- [[#length 属性|length 属性]]
- [[#四、关于this指向|四、关于this指向]]
	- [[#四、关于this指向#1、方法中的 this|1、方法中的 this]]
	- [[#四、关于this指向#2、单独使用this|2、单独使用this]]
	- [[#四、关于this指向#3、函数中使用this|3、函数中使用this]]
	- [[#四、关于this指向#4、事件中的this|4、事件中的this]]
	- [[#四、关于this指向#5、显示函数绑定|5、显示函数绑定]]
- [[#五、立即执行函数 IIFE|五、立即执行函数 IIFE]]
- [[#六、作用域|六、作用域]]
			- [[#作用域链|作用域链]]
- [[#七、函数调用|七、函数调用]]
			- [[#1、call(执行环境对象,实参列表);|1、call(执行环境对象,实参列表);]]
			- [[#2、apply(执行环境对象,实参列表数组);|2、apply(执行环境对象,实参列表数组);]]
			- [[#3、bind(执行环境对象)(实参列表);|3、bind(执行环境对象)(实参列表);]]
- [[#八、回调函数|八、回调函数]]
- [[#九、闭包|九、闭包]]

## 一、函数介绍

函数就是将实现特定功能的代码封装起来，当我们需要实现特定功能时，直接调用函数实现即可，不需要每次都写一堆代码，实现代码的复用。

函数的作用：  
1、实现功能的封装，提高代码复用率  
2、用于构建对象的模板（构造函数）

函数实际上是对象，每个函数都是Function类型的实例，并且都与其他引用类型一样具有属性和方法，由于函数是对象，因此函数名实际上也是一个指向函数对象的指针，不会与某个函数绑定。

## 二、函数的声明

**语法：**

```javascript
function 函数名(形参列表){
    //函数体
}
```

我们也可以写成函数表达式的形式：

```javascript
var 函数名 = function(形参列表){
    //函数体
}
```

```javaScript
var/let/const 函数名 = (形参列表) => {
	//函数体
}
```

**例子：**

```javascript
// 函数声明
function sum(a,b){
  return a + b
}
//函数声明之后，需要调用才能执行
// 函数调用：函数名(实参)
console.log(sum(1,2))  //3

```

写成函数表达式的形式：

```javascript
// 函数声明
var sum = function(a,b){
  return a + b
}
// 函数调用：函数名(实参)
console.log(sum(1,2)) //3

```

**函数声明提升**  
函数声明与var声明的变量类似，也存在声明提升。

```javascript
sum(1,2) //在此处调用函数不会报错，因为存在函数声明提升
function sum(a,b){
  return a + b
}

```

注意：使用函数表达式声明函数时，不可以把函数调用写在函数声明前面，因为变量赋值不存在提升。

## 三、函数内部属性

函数内部属性只能在函数内部才能访问

### arguments

arguments是一个类数组对象，包含着传入函数中的所有参数。arguments主要用途是保存函数参数。

```javascript
function foo(){
  console.log(arguments) // [Arguments] { '0': 1, '1': 2, '2': 3, '3': 4 }
  console.log(arguments[1]) // 2
}
// 当传递的实参个数超过形参的个数的时候不会报错,所有的实参都会保存在arguments里
foo(1,2,3,4) 

```

注意：arguments 中存的是实参，而不会存形参

```javascript
function foo(a,b = 2,c = 3){
  console.log(arguments) // [Arguments] { '0': 1 }
  console.log(b) //2
  console.log(c) //3
}
//只传了一个实参，那么arguments中就只有一个值
foo(1) 
```

##### callee 属性

arguments 对象有一个名为callee的属性，该属性是一个指针，指向拥有这个arguments对象的函数。

`arguments.callee` 实际上就是函数名。

```javascript
// 递归求n的阶乘
function factorial(n) {
  if (n == 1) {
    return 1
  }
  return arguments.callee(n - 1) * n //arguments.callee 相当于函数名factorial
}
console.log(factorial(10));

```

##### length 属性

arguments 对象的 length 属性返回实参个数。

```javascript
function foo(){
  console.log(arguments.length) //5
}
foo(1,2,3,4,5) 
```

## 四、关于this指向

this：执行环境上下文对象，它指向谁取决于什么时候调用、被谁调用。

-   在方法中，this 表示该方法所属的对象。
-   如果单独使用，this 表示全局对象。
-   在函数中，this 表示全局对象。
-   在事件中，this 表示接收事件的元素。
-   在显式函数绑定时，我们可以自己决定this的指向

### 1、方法中的 this

在对象方法中， this 指向调用它所在方法的对象，也就是说谁调用这个方法，this就指向谁。

```javascript
var person = {
  name:'叶子yes',
  sayName: function () {
    console.log(this.name); 
  }
}
//person对象调用了该方法，因此this指向person
person.sayName() // '叶子yes'

```

```javascript
var name = 'hello'
    var person = {
      name: '叶子yes',
      sayName: function () {
        console.log(this.name);
      }
    }
var a = person.sayName
//这里是全局对象调用了sayName方法，因此this指向全局对象window，结果为'hello'
//注意：在node环境下执行时，结果为undefined
a()

```

### 2、单独使用this

单独使用 this，则它指向全局对象。

-   在浏览器中，window 就是该全局对象
-   在node中，指向的是一个{}

浏览器环境下：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/204a9af926644411af24e0b410a9cd2e.png)  
node环境下：

```javascript
console.log(this) // {}
```

### 3、函数中使用this

在函数中，函数的所属者默认绑定到 this 上。

-   在浏览器中，指向的是window
-   在node中，指向的就是global对象

浏览器环境下：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/9eb10d99400b410391302ef7acc5ed72.png)  
node环境下：

```javascript
function foo(){
  return this
}
console.log(foo())

```

输出结果：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/c86d90f1c01f4b12bbf20211437a16a9.png)

### 4、事件中的this

在 HTML 事件句柄中，this 指向了接收事件的 HTML 元素：

```javascript
<button onclick="this.style.display='none'"> 点我后我就消失了 </button> //this指向button
```

### 5、显示函数绑定

在 JavaScript 中函数也是对象，对象则有方法，apply 和 call 就是函数对象的方法。这两个方法异常强大，他们允许切换函数执行的上下文环境（context），即 this 绑定的对象。（后面还会讲到这两个方法）

在下面例子中，当我们使用 person2 作为参数来调用 person1.sayName 方法时, **this** 将指向 person2, 即便它是 person1 的方法：

```javascript
var person1 = {
  name: 'zhangsan',
  sayName: function () {
    console.log(this.name);
  }
}
var person2 = {
  name:'叶子yes'
}
//this指向person2
person1.sayName.call(person2) //叶子yes

```

## 五、立即执行函数 IIFE

IIFE: Immediately Invoked Function Expression，意为立即调用的函数表达式，也就是说，声明函数的同时立即调用这个函数。

**作用**  
1、页面加载完成后只执行一次的设置函数。  
2、将设置函数中的变量包裹在局部作用域中，不会泄露成全局变量。

**写法：**

```javascript
(function(形参){
	函数体内容
})(实参);

```

或者：

```javascript
(function(形参){
	函数体内容
}(实参));

```

**例子：**

```javascript
(function (a) {
  console.log(123 + a); //124
})(1)
```

```javascript
(function (a) {
  console.log(123 + a); //124
}(1))

```

如果我们写了普通函数，又写了立即执行函数，要注意加分号隔开，否则 js 编译器会把它们解析成一条语句，就会报错：

```javascript
// 普通函数
function foo() {
  console.log(123);
}
foo();
// 如果后面跟的是立即执行函数，前面的结束语句必须要加分号
// 立即执行函数
(function (a) {
  console.log(123 + a);
})(123)

```

**使用：**  
1、就像其它任何函数一样，一个立即执行函数也能返回值并且可以赋值给其它变量：

```javascript
var sum = (function (a,b) {
  return a + b;
}(1,2))
console.log(sum); //3

```

2、可以在立即执行函数前面添加！、+ 等对返回值进行类型转换：

```javascript
//1、返回布尔值
!function(形参){
	return true
}(实参)

//2、返回数字类型的值
+function(形参){
	return 123
}(实参)

//3、对于数字返回的是相反数，非数字返回NaN
-function(形参){
	return 123
}(实参)

//4、对于数字返回的是相反数减1，非数字返回-1
~function(形参){
	return 123
}(实参)

//5、返回undefined
void function(形参){
	函数体内容
}(实参)

```

3、面试题

```javascript
//下面代码输出结果是什么
for (var i = 0; i < 6; i++) {
  function output() {
    console.log(i); 
  }
}
output() //输出结果是6，因为函数执行时，for循环已经结束，函数体中最终存的i值是6

```

如果我们想要输出0,1,2,3,4,5 应该怎么做？这时候我们就可以使用立即执行函数：

```javascript
for (var i = 0; i < 6; i++) {
  (function (j) {
    console.log(j); //0,1,2,3,4,5
  })(i)
}
/*
因为 JS 中调用函数传递参数都是值传递 ，所以当立即执行函数执行时，
首先会把参数 i 的值复制一份，然后再创建函数作用域来执行函数，
循环6次就会创建6个作用域，所以每个输出访问的都是不同作用域的 i 的值 。
*/

```

## 六、作用域

ES5中：（ES5中没有块级作用域）

**函数作用域**： 在 JavaScript函数中声明的变量，会成为函数的局部变量。  
 函数内部声明的变量，在函数外部不能访问。

**全局作用域**：函数之外声明的变量，会成为全局变量。  
 函数外部声明的变量，在函数内部可以访问。  
 当函数嵌套，在这个时候，内部函数与外部函数的这个变量就组成了闭包。

```javascript
// 全局作用域定义的变量,函数作用域里是可以获取到的
var v1 = 10
v3 = 30
function foo() {
  // 函数局部作用域里面定义的变量,外界是获取不到的
  var v2 = 20
  console.log(v1, v2, v3); //10 20 30
}
foo()
console.log(v2); //ReferenceError: v2 is not defined

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/cb3ccb5678394075b2d2f05bfb343973.png)  
作用域最大的用处就是隔离变量，不同作用域下同名变量不会有冲突。

##### 作用域链

首先我们先认识一下**自由变量**：  
如下代码中，`console.log(a)`要得到a变量，但是在当前的作用域中没有定义a（可对比一下b）。**当前作用域没有定义的变量，这成为 自由变量** 。自由变量的值如何得到 —— 要到创建这个函数的那个父级作用域寻找，如果没有就一直向上级祖先元素寻找（这就是所谓的"静态作用域"，静态作用域是指函数的作用域在函数定义时就已经确定了）

```javascript
var a = 100
function fn() {
    var b = 200
    console.log(a) // 这里的a 就是一个自由变量  // 100
    console.log(b)
}
fn()

```

**作用域链：**

自由变量会顺着作用域向父作用域找，如果父级也没有找到，再一层一层向上寻找，直到找到全局作用域还是没找到，就宣布放弃。这种一层一层的关系，就是作用域链 。

```javascript
var a = 100
function F1() {
  var b = 200
  function F2() {
    var c = 300
    console.log(a) // 自由变量，顺作用域链向父作用域找 //100
    console.log(b) // 自由变量，顺作用域链向父作用域找 //200
    console.log(c) // 本作用域的变量  //300
  }
  F2()
}
F1()

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/147095e5a93c40108893d9ef30f8f143.png)

## 七、函数调用

函数声明好之后并不会直接运行，需要进行调用才能运行。  
调用函数的方式不仅限于 () 执行。

**调用函数的方式：**

-   函数名(实参列表);
-   函数名.call(执行环境对象,实参列表);
-   函数名.apply(执行环境对象,实参列表数组);
-   函数名.bind(执行环境对象)(实参列表);

下面介绍一下call、apply、bind方法：

##### 1、call(执行环境对象,实参列表);

call 方法用来改变 this 的指向，第一个参数表示 this 的指向，后面的参数表示传递的实参，可以零个、一个或多个。

```javascript
var obj = {
  name: '叶子yes',
  sayName: function (a,b) {
    console.log(this.name); // 叶子yes
    console.log(a,b); // 1,2
  }
}
var b = obj.sayName;
b.call(obj,1,2); // this 指向 obj

```

##### 2、apply(执行环境对象,实参列表数组);

apply 方法和 call 方法基本相同，也是用来改变 this 的指向，不同点在于，apply 传递实参时采用数组的形式。

```javascript
var obj = {
  name: '叶子yes',
  sayName: function (a,b) {
    console.log(this.name); // 叶子yes
    console.log(a,b); // 1,2
  }
}
var b = obj.sayName;
b.apply(obj,[1,2]); // this 指向 obj，实参列表要写成数组的形式

```

注意：如果call和apply的第一个参数是null，那么this在node环境下指向的是global对象，在HTML中指向的是window对象

```javascript
var obj = {
  name: '叶子yes',
  sayName: function (a,b) {
    console.log(this); // window 或者 global
  }
}
var b = obj.sayName;
b.apply(null);

```

##### 3、bind(执行环境对象)(实参列表);

bind 也是用来改变 this 的指向的，但是 `bind(执行环境对象)` 返回的是函数，还没有被执行，需要再使用（）执行函数。

```javascript
var obj = {
  name: '叶子yes',
  sayName: function (a,b) {
    console.log(this.name); // 叶子yes
    console.log(a,b); // 1,2
  }
}
var b = obj.sayName;
var c = b.bind(obj) //返回函数，此时还没有执行，需要再使用()来执行
console.log(c) //[Function: bound sayName]
c(1,2) //执行函数

```

**总结：call和apply都是改变上下文中的this并立即执行这个函数，bind方法可以让对应的函数想什么时候调就什么时候调用，并且可以将参数在执行的时候添加，这是它们的区别。**

## 八、回调函数

回调，就是回头调用的意思。主函数的事先做完，回头再调用传进来的那个函数。

回调函数的作用：回调函数一般都用在耗时操作上面：因为主函数不用等待回调函数执行完，可以接着执行自己的代码。比如ajax请求，比如处理文件等。

下面简单模拟一下回调函数：

```javascript
//定义主函数，回调函数作为参数
function A(callback) {
  callback();
  console.log('我是主函数');
}
//定义回调函数
function B() {
  // 模仿延时操作
  setTimeout(() => {
    console.log('我是回调函数');
  }, 3000);
}
//调用主函数，将函数B传进去
A(B);

```

```
输出结果为：
我是主函数
我是回调函数
```

先输出“我是主函数”，3秒后再输出“我是回调函数”

## 九、闭包

**1、什么是闭包**  
简单讲，闭包就是指有权访问另一个函数作用域中的变量的函数。

**闭包是一种特殊的对象**。它由两部分构成：函数以及创建该函数的环境。环境由闭包创建时在作用域中的任何局部变量组成。

**2、闭包形成的条件**

闭包的生成有三个必要条件：  
1、函数嵌套函数  
2、内部函数引用了外部函数中的数据（属性、函数）  
3、参数和变量不会被回收

这样就形成了一个不会销毁的函数空间。

下面例子中的 closure 就是一个闭包：

```javascript
function func() {
  var a = 1, b = 2;

  function closure() {
    return a + b;
  }
  return closure;
}
console.log(func()()); // 3

```

闭包的作用域链包含着它自己的作用域，以及包含它的函数的作用域和全局作用域。

在Javascript语言中，只有函数内部的子函数才能读取局部变量，因此可以把闭包简单理解成 " 定义在一个函数内部的函数 " 。

所以，在本质上，闭包就是将函数内部和函数外部连接起来的一座桥梁。

**3、闭包的作用**

闭包可以用在许多地方。它的最大用处有两个，一个是可以读取函数内部的变量，另一个就是让这些变量的值始终保持在内存中。

```javascript
function f1() {
  var n = 999;
  nAdd = function () { 
  	n += 1 
  }
  function f2() {
    console.log(n);
  }
  return f2;
}
var result = f1();
result(); // 999
nAdd();
result(); // 1000

```

在这段代码中，result 实际上就是闭包 f2函数。它一共运行了两次，第一次的值是999，第二次的值是1000。这证明了，函数f1中的局部变量n一直保存在内存中，并没有在f1调用后被自动清除。

为什么会这样呢？原因就在于f1是f2的父函数，而f2被赋给了一个全局变量，这导致f2始终在内存中，而f2的存在依赖于f1，因此f1也始终在内存中，不会在调用结束后，被垃圾回收机制（garbage collection）回收。

这段代码中另一个值得注意的地方，就是"nAdd=function(){n+=1}"这一行，首先在nAdd前面没有使用var关键字，因此nAdd是一个全局变量，而不是局部变量。其次，nAdd的值是一个匿名函数（anonymous function），而这个匿名函数本身也是一个闭包，所以nAdd相当于是一个setter，可以在函数外部对函数内部的局部变量进行操作。

**4、使用闭包的注意点**

（1）由于闭包会使得函数中的变量都被保存在内存中，内存消耗很大，所以不能滥用闭包，否则会造成网页的性能问题，在IE中可能导致内存泄露，这是IE的BUG。解决方法是，在退出函数之前，将不使用的局部变量全部删除。

（2）闭包会在父函数外部改变父函数内部变量的值。所以，如果你把父函数当作对象（object）使用，把闭包当作它的公用方法（Public Method），把内部变量当作它的私有属性（private value），这时一定要小心，不要随便改变父函数内部变量的值。多个子函数的scope都是同时指向父级，是完全共享的。因此当父级的变量对象被修改时，所有子函数都受到影响。