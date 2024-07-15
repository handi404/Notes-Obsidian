# 文章目录

- [[#TypeScrip|TypeScrip]]
	- [[#TypeScrip#基础语法|基础语法]]
		- [[#基础语法#变量的声明|变量的声明]]
			- [[#变量的声明#复合类型|复合类型]]
		- [[#基础语法#条件控制|条件控制]]
			- [[#条件控制#if-else|if-else]]
			- [[#条件控制#switch|switch]]
		- [[#基础语法#for&while 循环|for&while 循环]]
			- [[#for&while 循环#常规|常规]]
			- [[#for&while 循环#遍历数组|遍历数组]]
		- [[#基础语法#函数|函数]]
			- [[#函数#基础样式|基础样式]]
			- [[#函数#箭头函数|箭头函数]]
			- [[#函数#可选参数|可选参数]]
	- [[#TypeScrip#面向对象|面向对象]]
		- [[#面向对象#枚举、接口|枚举、接口]]
		- [[#面向对象#继承|继承]]
	- [[#TypeScrip#模块开发|模块开发]]
		- [[#模块开发#导出|导出]]
		- [[#模块开发#导入|导入]]
## TypeScrip

TypeScript 是微软开发的开源编程语言，在 JavaScript 的基础上拓展了一些语法，是 JavaScript 的一个超集。

> TypeScript 官网：https://www.typescriptlang.org/
>
> 在线测试 TypeScript 代码：https://www.typescriptlang.org/play
>
> 菜鸟教程：https://www.runoob.com/typescript/ts-tutorial.html

### 基础语法

#### 变量的声明

TypeScript 在 JavaScript 基础上加入了静态类型检查功能，因此每一个变量都有固定的数据类型。

例如：`let msg: string = 'hello world`

- let: 声明变量的关键字，类似的 const 代表常量
- string：变量类型（拓展 JavaScript 的部分），其他常见类型如下：

    - number：整数、数值、浮点数、二进制等
    - boolean：布尔类型
    - any：不确定，可是任意类型（相当于跳过类型检查）
    - union 类型：例如`let u: string|number|boolean = 'hello'`可以是多个指定类型中的一种
    - object：对象

```ts
let p = {name:'jack', age: 21}
console.log(p.name)
console.log(p['name'])
```


##### 复合类型

- 数组

```typescript
let names: Array<string> = ['a','b']
let age: number[] = [1,2] 
console.log(names[1])

```

#### 条件控制

##### if-else

```typescript
let num:number = 21
if(num%2===0){ // 推荐使用三个等于号判断
    console.log("ou")
} else{
    console.log("ji")
}

```

TypeScript 中，空字符串、0、 null 、undefined 等被解析为 false ，它值则为 true

##### switch

#### for&while 循环

##### 常规

```typescript
for(let i=0;i<10;i++){
    console.log(i)
}
let num=0
while(num<10){
        console.log(num)
}

```

##### 遍历数组

```typescript
let names: Array<string> = ['a','b']
for(let i in names){
    console.log(i+':' names[i]) // 这种方取出来的是下标
}
for(let n of names){
    console.log(n) // 这种方取出来的是一个个的元素
}

```

#### 函数

##### 基础样式

```typescript
function sum(x: number, y: number): number {
    return x+y
}
let result = sum(1,2)
console.log('1+2=' + result)

```

##### 箭头函数

```typescript
let sayHi = (name: string) =>{
	console.log('hi' + name)
}
sayHi('Jack')
```

##### 可选参数

```typescript
// 参数后加问号？，表示可选
function sayHi (name？: string) {
    name = name ? name: '无名氏' //判断，没有传入参数就赋值
	console.log('hi' + name)
}
sayHi('Jack')
sayHi()

// 上述可赋默认值 类似python语法
function sayHi (name: string = 'Jack')  //若不传参 默认为Jack

```

### 面向对象

TypeScript 具备面向对象编程的基本语法，例如 interface 、 class 、 enum 等。也具备封装、继承、多态等面向对象基本特征。

#### 枚举、接口

```typescript
// 定义枚举 不需要写let和参数类型等
enum Msg{
    HI = 'hi',
    HELLO = 'hello'
}
// 定义接口 
interface A{
    say(msg: Msg):void
}
// 实现接口
class B implements A{
    say(msg:Msg):void{
        console.log(msg + "ni hao")
    }
}
// 初始化对象 并调用方法 
let a:A = new B()
a.say(Msg.HI)

```

#### 继承

```typescript
// 定义矩形类
class Rectangle{
    // 定义成员变量 无需let
    private width: number
    private length: number
    //构造函数 无需function
    constructor(width:number,length:number){
        this.length=length
        this.width=width
    }
    public area():number{
        return this.width*this.length
    }
}
// 定义正方形类，继承于矩形类
class square extends Rectangle{
    constructor(side: number){
        super(side,side) //调用父类构造函数
    }
}

let s = new square(10)
console.log('正方形面积：'+ s.area())

```

### 模块开发

应用复杂时我们可以把通用功能抽取到单独的 ts 文件，每个文件都是一个模块 (module)模块可以相互加载，提高代码复用性。

#### 导出

```typescript
// 定义类，并通过export导出
export class Rectangle{
    xxx
}
//  定义工具方法
export function area(rec:Rectangle) number{
    xxx
}

```

#### 导入

```typescript
// 通过import语法导入 from后写地址
import {Rectangle, area} from '../rectangle'
// 使用导入的类创建对象
let r = new Rectangle(10,20)
// 调用导入的方法
console.log(area(r))

```
