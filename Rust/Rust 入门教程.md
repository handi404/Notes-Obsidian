# 文章目录
- [[#前言|前言]]
- [[#1\. 使用cargo创建项目|1\. 使用cargo创建项目]]
- [[#2\. 编程语言类型|2\. 编程语言类型]]
	- [[#2\. 编程语言类型#1\. 整数|1\. 整数]]
	- [[#2\. 编程语言类型#2\. 浮点数|2\. 浮点数]]
	- [[#2\. 编程语言类型#3\. 字符|3\. 字符]]
- [[#3\. 常量与不可变变量的区别|3\. 常量与不可变变量的区别]]
- [[#4\. 重影（Shadowing）|4\. 重影（Shadowing）]]
- [[#5\. 两个重要的泛型类型|5\. 两个重要的泛型类型]]
- [[#6\. 常见的内存管理方式|6\. 常见的内存管理方式]]
- [[#7\. 如何理解生命周期？|7\. 如何理解生命周期？]]
- [[#8\. 条件语句|8\. 条件语句]]
	- [[#8\. 条件语句#1\. if实例|1\. if实例]]
	- [[#8\. 条件语句#2\. while循环实例|2\. while循环实例]]
	- [[#8\. 条件语句#3\. for循环实例|3\. for循环实例]]
	- [[#8\. 条件语句#4\. loop循环实例|4\. loop循环实例]]
- [[#9\. 变量与数据交互的方式|9\. 变量与数据交互的方式]]
- [[#10\. 引用的一些规制|10\. 引用的一些规制]]
	- [[#10\. 引用的一些规制#1\. 引用实例（实质上"引用"是变量的间接访问方式）|1\. 引用实例（实质上"引用"是变量的间接访问方式）]]
	- [[#10\. 引用的一些规制#2\. "垂悬引用"实例|2\. "垂悬引用"实例]]
- [[#11\. Slice（切片）类型实例|11\. Slice（切片）类型实例]]
- [[#12\. 非字符串切片实例|12\. 非字符串切片实例]]
- [[#13\. 结构体实例|13\. 结构体实例]]
- [[#14\. 元组结构体实例|14\. 元组结构体实例]]
- [[#15\. 输出结构体实例|15\. 输出结构体实例]]
- [[#16\. 结构体方法实例|16\. 结构体方法实例]]
- [[#17\. 结构体关联函数实例|17\. 结构体关联函数实例]]
- [[#18\. 单元结构体|18\. 单元结构体]]
- [[#19\. 枚举类|19\. 枚举类]]
	- [[#19\. 枚举类#1\. match语法实例|1\. match语法实例]]
	- [[#19\. 枚举类#2\. Option 枚举类|2\. Option 枚举类]]
- [[#20\. if let 语法实例|20\. if let 语法实例]]
- [[#21\. Rust 组织管理|21\. Rust 组织管理]]
	- [[#21\. Rust 组织管理#1\. 箱（Crate）|1\. 箱（Crate）]]
	- [[#21\. Rust 组织管理#2\. 包（Package）|2\. 包（Package）]]
	- [[#21\. Rust 组织管理#3\. 模块（Module）|3\. 模块（Module）]]
- [[#22\. 访问权限|22\. 访问权限]]
	- [[#22\. 访问权限#use 关键字|use 关键字]]
	- [[#22\. 访问权限#2\. 引用标准库|2\. 引用标准库]]
- [[#23\. 格式化输出|23\. 格式化输出]]
- [[#24\. 特性|24\. 特性]]
	- [[#24\. 特性#1\. 默认特性|1\. 默认特性]]
	- [[#24\. 特性#2\. 特性做参数|2\. 特性做参数]]
	- [[#24\. 特性#3\. 特性做返回值|3\. 特性做返回值]]
	- [[#24\. 特性#4\. 有条件实现方法|4\. 有条件实现方法]]
- [[#25\. 优雅地错误处理|25\. 优雅地错误处理]]
- [[#26\. 丑陋的 Option/Result 处理|26\. 丑陋的 Option/Result 处理]]
- [[#27\. Vectors|27\. Vectors]]
- [[#28\. 解引用|28\. 解引用]]
- [[#29\. 生命周期|29\. 生命周期]]
	- [[#29\. 生命周期#1\. 显式生命周期|1\. 显式生命周期]]
	- [[#29\. 生命周期#2\. 多个生命周期|2\. 多个生命周期]]
	- [[#29\. 生命周期#3\. 静态生命周期|3\. 静态生命周期]]
- [[#30\. 数据类型中的生命周期|30\. 数据类型中的生命周期]]
- [[#31\. 原始字符串常量|31\. 原始字符串常量]]
- [[#32\. 文件中的字符串常量|32\. 文件中的字符串常量]]
- [[#33\. 重温字符串片段（String Slice）|33\. 重温字符串片段（String Slice）]]
- [[#34\. Char|34\. Char]]
- [[#35\. 字符串（String）|35\. 字符串（String）]]
- [[#36\. 将文本作为函数的参数|36\. 将文本作为函数的参数]]
- [[#37 . 字符串构建]]
- [[#38\. 重温字符串格式化|38\. 重温字符串格式化]]
	- [[#38\. 重温字符串格式化#1\. 宏 format!|1\. 宏 format!]]
	- [[#38\. 重温字符串格式化#2\. 填充/对齐|2\. 填充/对齐]]
	- [[#38\. 重温字符串格式化#3\. 星号：.\*|3\. 星号：.\*]]
- [[#39\. 字符串转换|39\. 字符串转换]]
- [[#40\. 动态调度和静态调度|40\. 动态调度和静态调度]]
- [[#41\. 泛型|41\. 泛型]]
- [[#42\. Box|42\. Box]]
- [[#43\. 重温泛型结构体|43\. 重温泛型结构体]]
- [[#44\. 指针|44\. 指针]]
- [[#45\. 解引用|45\. 解引用]]
	- [[#45\. 解引用#1\. 运算符 \*|1\. 运算符 \*]]
	- [[#45\. 解引用#2\. 运算符 .|2\. 运算符 .]]
- [[#46\. 智能指针|46\. 智能指针]]
- [[#47\. 智能不安全代码|47\. 智能不安全代码]]
- [[#48\. 重温error的使用|48\. 重温error的使用]]
- [[#49\. 引用计数|49\. 引用计数]]
- [[#50\. 共享访问|50\. 共享访问]]
- [[#51\. 线程间共享|51\. 线程间共享]]
- [[#52\. 组合智能指针|52\. 组合智能指针]]
- [[#53\. 项目的结构和管理|53\. 项目的结构和管理]]
	- [[#53\. 项目的结构和管理#1\. 模块|1\. 模块]]
	- [[#53\. 项目的结构和管理#2\. 编写程序|2\. 编写程序]]
	- [[#53\. 项目的结构和管理#3\. 编写库|3\. 编写库]]
	- [[#53\. 项目的结构和管理#4\. 引用其他模块和 crate|4\. 引用其他模块和 crate]]
	- [[#53\. 项目的结构和管理#5\. 引用多个项目|5\. 引用多个项目]]
	- [[#53\. 项目的结构和管理#6\. 创建模块|6\. 创建模块]]
	- [[#53\. 项目的结构和管理#7\. 模块层次结构|7\. 模块层次结构]]
	- [[#53\. 项目的结构和管理#8\. 内联模块|8\. 内联模块]]
	- [[#53\. 项目的结构和管理#9\. 模块内部引用|9\. 模块内部引用]]
	- [[#53\. 项目的结构和管理#10\. 导出|10\. 导出]]
	- [[#53\. 项目的结构和管理#11\. 结构体可见性|11\. 结构体可见性]]
- [[#54\. Prelude|54\. Prelude]]
- [[#55\. 闭包|55\. 闭包]]
- [[#56\. Rust中的所有权|56\. Rust中的所有权]]
- [[#57\. 引用和借用|57\. 引用和借用]]
- [[#总结|总结]]


___

## 前言

欢迎来到 Rust 语言之旅。本教程旨在循序渐进地介绍 Rust 编程语言的特性，大家通常认为 Rust 是一门学习曲线陡峭的语言。本文适用于有一定编程基础的同学学习，以代码实例来演示 Rust 编程。如果你不会 Rust 或者想要加深 Rust 的印象的话，那么就跟着我一起来学习吧！

___

## 1\. 使用cargo创建项目

cargo 是Rust 的包管理器和构建系统

创建项目的命令如下：

```shell
cargo new 项目名
```

编译：

```shell
cargo build
```

运行：

```shell
cargo run
```

## 2\. 编程语言类型

静态类型？动态类型？强类型？弱类型？

-   JavaScript是什么类型的语言？ 动态，弱类型
-   C是什么类型的语言？ 静态，弱类型
-   Rust是什么类型的语言？ 静态，强类型

### 1\. 整数

-   无符号整数：u8，u16，u32（推荐），u64，usize
-   有符号整数：i8，i16，i32（推荐），i64，isize

### 2\. 浮点数

-   32位浮点数：f32
-   64位浮点数：f64（推荐）

### 3\. 字符

`Char` 是Unicode码，并且总是4bytes大小

## 3\. 常量与不可变变量的区别

既然不可变变量是不可变的，那不就是常量吗？为什么叫变量？

变量和常量还是有区别的。在Rust中，以下程序是合法的：

```rust
let a = 123;   // 可以编译，但可能有警告，因为该变量没有被使用
let a = 456;
```

## 4\. 重影（Shadowing）

重影的概念与其他面向对象语言里的"重写"（Override）或"重载"（Overload）是不一样的。重影就是刚才讲述的所谓"重新绑定"，之所以加引号就是为了在没有介绍这个概念的时候代替一下概念。

重影就是指变量的名称可以被重新使用的机制：

实例

```rust
fn main() {
    let x = 5;
    let x = x + 1;
    let x = x * 2;
    println!("The value of x is: {}", x);
}

```

这段程序的运行结果：  
The value of x is: 12

**重影与可变变量的赋值不是一个概念，重影是指用同一个名字重新代表另一个变量实体，其类型、可变属性和值都可以变化。但可变变量赋值仅能发生值的变化。**

```rust
let mut s = "123"; 
s = s.len();
```

这段程序会出错：不能给字符串变量赋整型值。

## 5\. 两个重要的泛型类型

-   Option，代表有或无
-   Result<T,E>，代表成功或失败

实例

```rust
// 两个重要的泛型类型
enum Option<T> {
    Some(T),
    None,
}

enum Result<T, E> {
    Ok(T),
    Err(E),
}

fn main() {
    // 查看当前用户目录—>返回Option
    match std::env::home_dir() {
        Some(data) => println!("option i some, data = {:?}", data),
        // 如果为None执行
        None => println!("option is none"),
    }
    // 查看当前系统的环境变量—>返回Result
    match std::env::var("MYSQL_HOME") {
        Ok(data) => println!("ok! {:?}", data),
        Err(err) => println!("err {}", err),
    }
}

```

运行结果：  
option i some, data = “C:\\Users\\1”  
ok! “D:\\Program Files\\MySQL\\MySQL Server 8.0”

## 6\. 常见的内存管理方式

-   C语言的malloc和free（手动管理，Bug制造机）
-   GC：Golang，Java等语言（自动管理）
-   基于生命周期的半自动管理：Rust

## 7\. 如何理解生命周期？

在C语言中需要直接手动调用free去释放内存

Rust在编译间计算变量的使用范围

当变量不再被使用时编译自动在源码中插入free代码

## 8\. 条件语句

### 1\. if实例

```rust
fn main() {
    let a = 3;
    let number = if a > 0 { 1 } else { -1 };
    println!("number 为 {}", number);
}

```

### 2\. while循环实例

常用来循环外部条件，条件不成立结束循环

```rust
let mut i = 0;
while i < 10 {
    // 循环体
    i += 1;
}

```

### 3\. for循环实例

常用来遍历一个线性数据据结构（比如数组）

```rust
fn main() {
    let a = [10, 20, 30, 40, 50];
    for i in a.iter() {
        println!("值为 : {}", i);
    }
}

```

### 4\. loop循环实例

常用来循环内部条件，使用 `break` 结束循环

```rust
fn main() {
    let s = ['R', 'U', 'N', 'O', 'O', 'B'];
    let mut i = 0;
    loop {
        let ch = s[i];
        if ch == 'O' {
            break;
        }
        println!("\'{}\'", ch);
        i += 1;
    }
}

```

福利时刻，玩个游戏放松一下吧！————猜谜游戏

```rust
extern crate rand;
use rand::Rng;
use std::cmp::Ordering;
use std::io;

fn main() {
    println!("Guess the number!");
    let secret_number = rand::thread_rng().gen_range(1..101);
    loop {
        println!("Please input your guess.");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("Failed to read line");
        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        println!("You guessed: {}", guess);
        match guess.cmp(&secret_number) {
            Ordering::Less => println!("Too small!"),
            Ordering::Greater => println!("Too big!"),
            Ordering::Equal => {
                println!("You win!");
                break;
            }
        }
    }
}

```

运行结果：  
Guess the number!  
Please input your guess.  
60  
You guessed: 60  
Too big!  
Please input your guess.  
30  
You guessed: 30  
Too small!  
Please input your guess.  
45  
You guessed: 45  
Too small!  
Please input your guess.  
55  
You guessed: 55  
Too small!  
Please input your guess.  
58  
You guessed: 58  
Too small!  
Please input your guess.  
59  
You guessed: 59  
You win!

## 9\. 变量与数据交互的方式

变量与数据交互方式主要有移动（Move）和克隆（Clone）两种

## 10\. 引用的一些规制

不会获取所有权（所有权稍后会详细介绍），默认情况下是不可变的，同一时间最多只能存在一个可变引用

### 1\. 引用实例（实质上"引用"是变量的间接访问方式）

```rust
fn main() {
    let s1 = String::from("hello");
    let s2 = &s1;
    println!("s1 is {}, s2 is {}", s1, s2);
}

```

**可变引用与不可变引用相比除了权限不同以外，可变引用不允许多重引用，但不可变引用可以**

### 2\. "垂悬引用"实例

在 Rust 语言里不允许出现，如果有，编译器会发现它

```rust
fn main() {
    let reference_to_nothing = dangle();
}
fn dangle() -> &String {
    let s = String::from("hello");
    &s
}

```

**很显然，伴随着 dangle 函数的结束，其局部变量的值本身没有被当作返回值，被释放了。但它的引用却被返回，这个引用所指向的值已经不能确定的存在，故不允许其出现。**

## 11\. Slice（切片）类型实例

```rust
fn main() {
    let s = String::from("broadcast");
    let part1 = &s[0..5];
    let part2 = &s[5..9];
    println!("{}={}+{}", s, part1, part2);
}
```

运行结果：  
broadcast=broad+cast

-   …y 等价于 0…y
-   x… 等价于位置 x 到数据结束
-   … 等价于位置 0 到结束

注意：到目前为止，尽量不要在字符串中使用非英文字符，因为编码的问题。

实际上，到目前为止你一定疑惑为什么每一次使用字符串都要这样写String::from(“runoob”) ，直接写 “runoob” 不行吗？

事已至此我们必须分辨这两者概念的区别了。在 Rust 中有两种常用的字符串类型：str 和 String。str 是 Rust 核心语言类型，字符串切片（String Slice），常常以引用的形式出现（&str）。

凡是用双引号包括的字符串常量整体的类型性质都是 &str：

```rust
let s = "hello";
```

这里的 s 就是一个 &str 类型的变量。

String 类型是 Rust 标准公共库提供的一种数据类型，它的功能更完善——它支持字符串的追加、清空等实用的操作。String 和 str 除了同样拥有一个字符开始位置属性和一个字符串长度属性以外还有一个容量（capacity）属性。

String 和 str 都支持切片，切片的结果是 &str 类型的数据。

注意：切片结果必须是引用类型，但开发者必须自己明示这一点:

```rust
let slice = &s[0..3];
```

有一个快速的办法可以将 String 转换成 &str：

```rust
let s1 = String::from("hello"); 
let s2 = &s1[..];
```

## 12\. 非字符串切片实例

除了字符串以外，其他一些线性数据结构也支持切片操作，例如数组

```rust
fn main() {
    let arr = [1, 3, 5, 7, 9];
    let part = &arr[0..3];
    for i in part.iter() {
        println!("{}", i);
    }
}
```

运行结果：  
1  
3  
5

## 13\. 结构体实例

```rust
let domain = String::from("www.runoob.com");
let name = String::from("RUNOOB");
let runoob = Site {
    domain,  // 等同于 domain : domain,
    name,    // 等同于 name : name,
    nation: String::from("China"),
    traffic: 2013
};
```

有这样一种情况：你想要新建一个结构体的实例，其中大部分属性需要被设置成与现存的一个结构体属性一样，仅需更改其中的一两个字段的值，可以使用结构体更新语法：

```rust
let site = Site {
    domain: String::from("www.runoob.com"),
    name: String::from("RUNOOB"),
    ..runoob
};
```

**注意：…runoob 后面不可以有逗号。这种语法不允许一成不变的复制另一个结构体实例，意思就是说至少重新设定一个字段的值才能引用其他实例的值。**

## 14\. 元组结构体实例

"颜色"和"点坐标"是常用的两种数据类型，但如果实例化时写个大括号再写上两个名字就为了可读性牺牲了便捷性，Rust 不会遗留这个问题。元组结构体对象的使用方式和元组一样，通过 `.` 和下标来进行访问：

```rust
fn main() {
    struct Color(u8, u8, u8);
    struct Point(f64, f64);

    let black = Color(0, 0, 0);
    let origin = Point(0.0, 0.0);

    println!("black = ({}, {}, {})", black.0, black.1, black.2);
    println!("origin = ({}, {})", origin.0, origin.1);
}
```

运行结果：  
black = (0, 0, 0)  
origin = (0, 0)

## 15\. 输出结构体实例

调试中，完整地显示出一个结构体实例是非常有用的。但如果我们手动的书写一个格式会非常的不方便。所以 Rust 提供了一个方便地输出一整个结构体的方法：

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}
fn main() {
    let rect1 = Rectangle { width: 30, height: 50 };
    println!("rect1 is {:?}", rect1);
}
```

如第一行所示：一定要导入调试库 `#[derive(Debug)]` ，之后在 `println` 和 `print` 宏中就可以用 `{:?}` 占位符输出一整个结构体：

```rust
rect1 is Rectangle { width: 30, height: 50 }
```

如果属性较多的话可以使用另一个占位符 `{:#?}` 。

输出结果：  
rect1 is Rectangle {  
width: 30,  
height: 50  
}

## 16\. 结构体方法实例

结构体方法的第一个参数必须是 `&self` ，不需声明类型，因为 self 不是一种风格而是关键字。

```rust
struct Rectangle {
    width: u32,
    height: u32,
}
impl Rectangle {
    fn area(&self) -> u32 {
        self.width * self.height
    }

    fn wider(&self, rect: &Rectangle) -> bool {
        self.width > rect.width
    }
}
fn main() {
    let rect1 = Rectangle { width: 30, height: 50 };
    let rect2 = Rectangle { width: 40, height: 20 };

    println!("{}", rect1.wider(&rect2));
}
```

运行结果：  
false

## 17\. 结构体关联函数实例

之所以"结构体方法"不叫"结构体函数"是因为"函数"这个名字留给了这种函数：它在 `impl` 块中却没有 `&self` 参数。

这种函数不依赖实例，但是使用它需要声明是在哪个 impl 块中的。

一直使用的 `String::from` 函数就是一个"关联函数"。

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}
impl Rectangle {
    fn create(width: u32, height: u32) -> Rectangle {
        Rectangle { width, height }
    }
}
fn main() {
    let rect = Rectangle::create(30, 50);
    println!("{:?}", rect);
}
```

运行结果：  
Rectangle { width: 30, height: 50 }

贴士：结构体 impl 块可以写几次，效果相当于它们内容的拼接！

## 18\. 单元结构体

结构体可以只作为一种象征而无需任何成员：

> struct UnitStruct;  
> 我们称这种没有身体的结构体为单元结构体（Unit Struct）。

## 19\. 枚举类

### 1\. match语法实例

```rust
fn main() {
    enum Book {
        Papery {index: u32},
        Electronic {url: String},
    }
    let book = Book::Papery{index: 1001};
    let ebook = Book::Electronic{url: String::from("url...")};
    match book {
        Book::Papery { index } => {
            println!("Papery book {}", index);
        },
        Book::Electronic { url } => {
            println!("E-book {}", url);
        }
    }
}
```

运行结果:  
Papery book 1001

`match` 块也可以当作函数表达式来对待，它也是可以有返回值的：

```rust
match 枚举类实例 {
    分类1 => 返回值表达式,
    分类2 => 返回值表达式,
    ...
}
```

`match` 除了能够对枚举类进行分支选择以外，还可以对整数、浮点数、字符和字符串切片引用（&str）类型的数据进行分支选择。其中，浮点数类型被分支选择虽然合法，但不推荐这样使用，因为精度问题可能会导致分支错误。

对非枚举类进行分支选择时必须注意处理例外情况，即使在例外情况下没有任何要做的事，例外情况用下划线 `_` 表示：

实例

```rust
fn main() {
    let t = "abc";
    match t {
        "abc" => println!("Yes"),
        _ => {},
    }
}
```

### 2\. Option 枚举类

`Option` 是 Rust 标准库中的枚举类，这个类用于填补 Rust 不支持 null 引用的空白。

Rust 在语言层面彻底不允许空值 null 的存在，但无奈null 可以高效地解决少量的问题，所以 Rust 引入了 Option 枚举类：

```rust
enum Option<T> {
    Some(T),
    None,
}
```

如果你想定义一个可以为空值的类，你可以这样：

```rust
let opt = Option::Some("Hello");
```

如果你想针对 opt 执行某些操作，你必须先判断它是否是 Option::None

实例

```rust
fn main() {
    let opt = Option::Some("Hello");
    match opt {
        Option::Some(something) => {
            println!("{}", something);
        },
        Option::None => {
            println!("opt is nothing");
        }
    }
}
```

运行结果：  
Hello

如果你的变量刚开始是空值，你体谅一下编译器，它怎么知道值不为空的时候变量是什么类型的呢？

所以初始值为空的 Option 必须明确类型：

实例

```rust
fn main() {
    let opt: Option<&str> = Option::None;
    match opt {
        Option::Some(something) => {
            println!("{}", something);
        },
        Option::None => {
            println!("opt is nothing");
        }
    }
}
```

运行结果：  
opt is nothing

这种设计会让空值编程变得不容易，但这正是构建一个稳定高效的系统所需要的。由于 Option 是 Rust 编译器默认引入的，在使用时可以省略 `Option::` 直接写 `None` 或者 `Some()` 。

Option 是一种特殊的枚举类，它可以含值分支选择：

实例

```rust
fn main() {
    let t = Some(64);
    match t {
        Some(64) => println!("Yes"),
        _ => println!("No"),
    }
}
```

## 20\. if let 语法实例

```rust
let i = 0;
match i {
    0 => println!("zero"),
    _ => {},
}
```

放入主函数运行结果：  
zero

这段程序的目的是判断 i 是否是数字 0，如果是就打印 zero。

现在用 `if let` 语法缩短这段代码：

```rust
let i = 0;
if let 0 = i {
    println!("zero");
}
```

`if let` 语法格式如下：

```rust
if let 匹配值 = 源变量 {
    语句块
}
```

可以在之后添加一个 else 块来处理例外情况。

if let 语法可以认为是只区分两种情况的 match 语句的"语法糖"（语法糖指的是某种语法的原理相同的便捷替代品）。

对于枚举类依然适用：

实例

```rust
fn main() {
    enum Book {
        Papery(u32),
        Electronic(String)
    }
    let book = Book::Electronic(String::from("url"));
    if let Book::Papery(index) = book {
        println!("Papery {}", index);
    } else {
        println!("Not papery book");
    }
}
```

## 21\. Rust 组织管理

Rust 中有三个重要的组织概念：`箱` 、`包` 、`模块` 。

### 1\. 箱（Crate）

"箱"是二进制程序文件或者库文件，存在于"包"中。

"箱"是树状结构的，它的树根是编译器开始运行时编译的源文件所编译的程序。

注意：“二进制程序文件"不一定是"二进制可执行文件”，只能确定是是包含目标机器语言的文件，文件格式随编译环境的不同而不同。

### 2\. 包（Package）

当我们使用 Cargo 执行 new 命令创建 Rust 工程时，工程目录下会建立一个 Cargo.toml 文件。工程的实质就是一个包，包必须由一个 Cargo.toml 文件来管理，该文件描述了包的基本信息以及依赖项。

一个包最多包含一个库"箱"，可以包含任意数量的二进制"箱"，但是至少包含一个"箱"（不管是库还是二进制"箱"）。

当使用 cargo new 命令创建完包之后，src 目录下会生成一个 main.rs 源文件，Cargo 默认这个文件为二进制箱的根，编译之后的二进制箱将与包名相同。

### 3\. 模块（Module）

对于一个软件工程来说，我们往往按照所使用的编程语言的组织规范来进行组织，组织模块的主要结构往往是树。Java 组织功能模块的主要单位是类，而 JavaScript 组织模块的主要方式是 function。

这些先进的语言的组织单位可以层层包含，就像文件系统的目录结构一样。Rust 中的组织单位是模块（Module）。

## 22\. 访问权限

Rust 中有两种简单的访问权：公共（public）和私有（private）。

默认情况下，如果不加修饰符，模块中的成员访问权将是私有的。

如果想使用公共权限，需要使用 pub 关键字。

对于私有的模块，只有在与其平级的位置或下级的位置才能访问，不能从其外部访问。

### use 关键字

`use` 关键字能够将模块标识符引入当前作用域：

实例

```rust
mod nation {
    pub mod government {
        pub fn govern() {}
    }
}

use crate::nation::government::govern;
fn main() {
    govern();
}
```

这段程序能够通过编译。

因为 use 关键字把 govern 标识符导入到了当前的模块下，可以直接使用。

这样就解决了局部模块路径过长的问题。

当然，有些情况下存在两个相同的名称，且同样需要导入，我们可以使用 `as` 关键字为标识符添加别名：

实例

```rust
mod nation {
    pub mod government {
        pub fn govern() {}
    }
    pub fn govern() {}
} 

use crate::nation::government::govern;
use crate::nation::govern as nation_govern;
fn main() {
    nation_govern();
    govern();
}
```

这里有两个 govern 函数，一个是 nation 下的，一个是 government 下的，我们用 as 将 nation 下的取别名 nation\_govern。两个名称可以同时使用。

use 关键字可以与 `pub` 关键字配合使用：

实例

```rust
mod nation {
    pub mod government {
        pub fn govern() {}
    }
    pub use government::govern;
}

fn main() {
    nation::govern();
}
```

### 2\. 引用标准库

Rust 官方标准库字典：[https://doc.rust-lang.org/stable/std/all.html](https://doc.rust-lang.org/stable/std/all.html)

在学习了组织管理概念之后，我们可以轻松的导入系统库来方便的开发程序了：

实例

```rust
use std::f64::consts::PI;
fn main() {
    println!("{}", (PI / 2.0).sin());
}
```

运行结果：  
1

所有的系统库模块都是被默认导入的，所以在使用的时候只需要使用 use 关键字简化路径就可以方便的使用了。

## 23\. 格式化输出

打印操作由 `std::fmt` 里面所定义的一系列宏来处理，包括：

-   format!：将格式化文本写到字符串。
-   print!：与 format! 类似，但将文本输出到控制台（io::stdout）。
-   println!: 与 print! 类似，但输出结果追加一个换行符。
-   eprint!：与 print! 类似，但将文本输出到标准错误（io::stderr）。
-   eprintln!：与 eprint! 类似，但输出结果追加一个换行符。

## 24\. 特性

特性（trait）概念接近于 Java 中的接口（Interface），但两者不完全相同。特性与接口相同的地方在于它们都是一种行为规范，可以用于标识哪些类有哪些方法。

特性在 Rust 中用 trait 表示：

```rust
trait Descriptive {
    fn describe(&self) -> String;
}
```
Descriptive 规定了实现者必需有 describe (&self) -> String 方法。

我们用它实现一个结构体：

实例
```rust
struct Person {
    name: String,
    age: u8
}
impl Descriptive for Person {
    fn describe(&self) -> String {
        format!("{} {}", self.name, self.age)
    }
}

```
格式是：

> impl <特性名> for <所实现的类型名>

Rust 同一个类可以实现多个特性，每个 impl 块只能实现一个。

### 1. 默认特性

这是特性与接口的不同点：接口只能规范方法而不能定义方法，但特性可以定义方法作为默认方法，因为是"默认"，所以对象既可以重新定义方法，也可以不重新定义方法使用默认的方法：  
实例

```rust
trait Descriptive {
    fn describe(&self) -> String {
        String::from("[Object]")
    }
}
struct Person {
    name: String,
    age: u8
}
impl Descriptive for Person {
    fn describe(&self) -> String {
        format!("{} {}", self.name, self.age)
    }
}
fn main() {
    let cali = Person {
        name: String::from("Cali"),
        age: 24
    };
    println!("{}", cali.describe());
}

```

运行结果：  
Cali 24

如果我们将 impl Descriptive for Person 块中的内容去掉，那么运行结果就是：  
[Object]

### 2. 特性做参数

很多情况下我们需要传递一个函数做参数，例如回调函数、设置按钮事件等。在 Java 中函数必须以接口实现的类实例来传递，在 Rust 中可以通过传递特性参数来实现：
```rust
fn output(object: impl Descriptive) {
    println!("{}", object.describe());
}

```

任何实现了 Descriptive 特性的对象都可以作为这个函数的参数，这个函数没必要了解传入对象有没有其他属性或方法，只需要了解它一定有 Descriptive 特性规范的方法就可以了。当然，此函数内也无法使用其他的属性与方法。

特性参数还可以用这种等效语法实现：
```rust
fn output<T: Descriptive>(object: T) {
    println!("{}", object.describe());
}

```

这是一种风格类似泛型的语法糖，这种语法糖在有多个参数类型均是特性的情况下十分实用：
```rust
fn output_two<T: Descriptive>(arg1: T, arg2: T) {
    println!("{}", arg1.describe());
    println!("{}", arg2.describe());
}

```

特性作类型表示时如果涉及多个特性，可以用 + 符号表示，例如：
```rust
fn notify(item: impl Summary + Display) 
fn notify<T: Summary + Display>(item: T)
```

**注意：仅用于表示类型的时候，并不意味着可以在 impl 块中使用。**

复杂的实现关系可以使用 `where` \`关键字简化，例如：

```rust
fn some_function<T: Display + Clone, U: Clone + Debug>(t: T, u: U)
```

可以简化成：

```rust
fn some_function<T, U>(t: T, u: U) -> i32
    where T: Display + Clone,
          U: Clone + Debug

```

在了解这个语法之后，泛型章节中的"取最大值"案例就可以真正实现了：  
实例

```rust
trait Comparable {
    fn compare(&self, object: &Self) -> i8;
}
fn max<T: Comparable>(array: &[T]) -> &T {
    let mut max_index = 0;
    let mut i = 1;
    while i < array.len() {
        if array[i].compare(&array[max_index]) > 0 {
            max_index = i;
        }
        i += 1;
    }
    &array[max_index]
}
impl Comparable for f64 {
    fn compare(&self, object: &f64) -> i8 {
        if &self > &object { 1 }
        else if &self == &object { 0 }
        else { -1 }
    }
}
fn main() {
    let arr = [1.0, 3.0, 5.0, 4.0, 2.0];
    println!("maximum of arr is {}", max(&arr));
}

```

运行结果：  
maximum of arr is 5

Tip: 由于需要声明 compare 函数的第二参数必须与实现该特性的类型相同，所以 Self （注意大小写）关键字就代表了当前类型（不是实例）本身。

### 3\. 特性做返回值

特性做返回值格式如下：

实例

```rust
fn person() -> impl Descriptive {
    Person {
        name: String::from("Cali"),
        age: 24
    }
}

```

但是有一点，特性做返回值只接受实现了该特性的对象做返回值且在同一个函数中所有可能的返回值类型必须完全一样。比如结构体 A 与结构体 B 都实现了特性 Trait，下面这个函数就是错误的：

实例

```rust
fn some_function(bool bl) -> impl Descriptive {
    if bl {
        return A {};
    } else {
        return B {};
    }
}

```

### 4\. 有条件实现方法

`impl` 功能十分强大，我们可以用它实现类的方法。但对于泛型类来说，有时我们需要区分一下它所属的泛型已经实现的方法来决定它接下来该实现的方法：

```rust
struct A<T> {}
impl<T: B + C> A<T> {
    fn d(&self) {}
}

```

这段代码声明了 A 类型必须在 T 已经实现 B 和 C 特性的前提下才能有效实现此 impl 块。

## 25\. 优雅地错误处理

Result 如此常见以至于 Rust 有个强大的操作符 `?` 来与之配合。 以下两个表达式是等价的：

```rust
do_something_that_might_fail()?

match do_something_that_might_fail() {
    Ok(v) => v,
    Err(e) => return Err(e),
}

```

## 26\. 丑陋的 Option/Result 处理

当你只是试图快速地写一些代码时，Option/Result 对付起来可能比较无聊。 Option 和 Result 都有一个名为 `unwrap` 的函数：这个函数可以简单粗暴地获取其中的值。

unwrap 会：获取 Option/Result 内部的值

如果枚举的类型是 `None/Err`， 则会 `panic!`

这两段代码是等价的：

```rust
my_option.unwrap()

match my_option {
    Some(v) => v,
    None => panic!("some error message generated by Rust!"),
}

```

类似的：

```rust
my_result.unwrap()

match my_result {
    Ok(v) => v,
    Err(e) => panic!("some error message generated by Rust!"),
}

```

**不过啊，做个好 Rustacean，正确地使用 match！**

## 27\. Vectors

一些经常使用的泛型是集合类型。一个 vector 是可变长度的元素集合，以 Vec 结构表示。

比起手动构建，宏 `vec!` 让我们可以轻松地创建 vector。

Vec 有一个形如 `iter()` 的方法可以为一个 vector 创建迭代器，这允许我们可以轻松地将 vector 用到 for 循环中去。

内存细节：  
Vec 是一个结构体，但是内部其实保存了在堆上固定长度数据的引用。

一个 vector 开始有默认大小容量，当更多的元素被添加进来后，它会重新在堆上分配一个新的并具有更大容量的定长列表。（类似 C++ 的 vector）

实例

```rust
fn main() {
    // 我们可以显式确定类型
    let mut i32_vec = Vec::<i32>::new(); // turbofish <3
    i32_vec.push(1);
    i32_vec.push(2);
    i32_vec.push(3);

    // 但是看看 Rust 是多么聪明的自动检测类型啊
    let mut float_vec = Vec::new();
    float_vec.push(1.3);
    float_vec.push(2.3);
    float_vec.push(3.4);

    // 这是个漂亮的宏！
    let string_vec = vec![String::from("Hello"), String::from("World")];

    for word in string_vec.iter() {
        println!("{}", word);
    }
}

```

## 28\. 解引用

使用 `&mut` 引用时, 你可以通过 `*` 操作符来修改其指向的值。 你也可以使用 `*` 操作符来对所拥有的值进行拷贝（前提是该值可以被拷贝）。  
实例

```rust
fn main() {
    let mut foo = 42;
    let f = &mut foo;
    let bar = *f; // 取得所有者值的拷贝
    *f = 13;      // 设置引用所有者的值
    println!("{}", bar);
    println!("{}", foo);
}

```

运行结果：  
42  
13

## 29\. 生命周期

### 1\. 显式生命周期

尽管 Rust 不总是在代码中将它展示出来，但编译器会理解每一个变量的生命周期并进行验证以确保一个引用不会有长于其所有者的存在时间。

同时，函数可以通过使用一些符号来参数化函数签名，以帮助界定哪些参数和返回值共享同一生命周期。 生命周期注解总是以 ’ 开头，例如 'a，'b 以及 'c。

### 2\. 多个生命周期

生命周期注解可以通过区分函数签名中不同部分的生命周期，来允许我们显式地明确某些编译器靠自己无法解决的场景。

### 3\. 静态生命周期

一个静态变量是一个在编译期间即被创建并存在于整个程序始末的内存资源。他们必须被明确指定类型。

一个静态生命周期是指一段内存资源无限期地延续到程序结束。需要注意的一点是，在此定义之下，一些静态生命周期的资源也可以在运行时被创建。

拥有静态生命周期的资源会拥有一个特殊的生命周期注解 'static。 'static 资源永远也不会被 drop 释放。

如果静态生命周期资源包含了引用，那么这些引用的生命周期也一定是 'static 的。（任何缺少了此注解的引用都不会达到同样长的存活时间）

内存细节：  
因为静态变量可以全局性地被任何人访问读取而潜在地引入数据争用，所以修改它具有内在的危险性。

Rust 允许使用 `unsafe { ... }` 代码块来进行一些无法被编译器担保的内存操作。The R̸͉̟͈͔̄͛̾̇͜U̶͓͖͋̅Ṡ̴͉͇̃̉̀T̵̻̻͔̟͉́͆Ơ̷̥̟̳̓͝N̶̨̼̹̲͛Ö̵̝͉̖̏̾̔M̶̡̠̺̠̐͜Î̷̛͓̣̃̐̏C̸̥̤̭̏͛̎͜O̶̧͚͖͔̊͗̇͠N̸͇̰̏̏̽̃（常见的中文翻译为：Rust 死灵书）在讨论时应该被严肃地看待。

实例

```rust
static PI: f64 = 3.1415;

fn main() {
    // 静态变量的范围也可以被限制在一个函数内
    static mut SECRET: &'static str = "swordfish";

    // 字符串字面值拥有 'static 生命周期
    let msg: &'static str = "Hello World!";
    let p: &'static f64 = &PI;
    println!("{} {}", msg, p);

    // 你可以打破一些规则，但是必须是显式地
    unsafe {
        // 我们可以修改 SECRET 到一个字符串字面值因为其同样是 'static 的
        SECRET = "abracadabra";
        println!("{}", SECRET);
    }
}

```

运行结果：  
Hello World! 3.1415  
abracadabra

## 30\. 数据类型中的生命周期

和函数相同，数据类型也可以用生命周期注解来参数化其成员。 Rust 会验证引用所包含的数据结构永远也不会比引用指向的所有者存活周期更长。 我们不能在运行中拥有一个包括指向虚无的引用结构存在！

实例

```rust
struct Foo<'a> {
    i:&'a i32
}

fn main() {
    let x = 42;
    let foo = Foo {
        i: &x
    };
    println!("{}",foo.i);
}

```

运行结果：  
42

## 31\. 原始字符串常量

原始字符串支持写入原始的文本而无需为特殊字符转义，因而不会导致可读性下降（如双引号与反斜杠无需写为 " 和 \\），只需以 r#" 开头，以 "# 结尾。  
实例

```rust
fn main() {
    let a: &'static str = r#"
        <div class="advice">
            原始字符串在一些情景下非常有用。
        </div>
        "#;
    println!("{}", a);
}

```

## 32\. 文件中的字符串常量

如果你需要使用大量文本，可以尝试用宏 `include_str!` 来从本地文件中导入文本到程序中：

```rust
let hello_html = include_str!("hello.html");
```

## 33\. 重温字符串片段（String Slice）

字符串片段是对内存中字节序列的引用，而且这段字节序列必须是合法的 utf-8 字节序列。

str 片段的字符串片段（子片段），也必须是合法的 utf-8 字节序列。

&str 的常用方法：

-   len 获取字符串常量的字节长度（不是字符长度）。
-   starts\_with/ends\_with 用于基础测试。
-   is\_empty 长度为 0 时返回 true。
-   find 返回 Option，其中的 usize 为匹配到的第一个对应文本的索引值。  
    实例

```rust
fn main() {
    let a = "你好 🦀";
    println!("{}", a.len());
    let first_word = &a[0..6];
    let second_word = &a[7..11];
    // let half_crab = &a[7..9]; // 报错，Rust 不接受无效 unicode 字符构成的片段
    println!("{} {}", first_word, second_word);
}

```

运行结果：  
11  
你好 🦀

## 34\. Char

为了解决使用 Unicode 带来的麻烦，Rust 提供了将 utf-8 字节序列转化为类型 char 的 vector 的方法。

每个 char 长度都为 4 字节（可提高字符查找的效率）。

实例

```rust
fn main() {
    // 收集字符并转换为类型为 char 的 vector
    let chars = "你好 🦀".chars().collect::<Vec<char>>();
    println!("{}", chars.len()); // 结果应为 4
    // 由于 char 为 4 字节长，我们可以将其转化为 u32
    println!("{}", chars[3] as u32);
}

```

运行结果：  
4  
129408

## 35\. 字符串（String）

字符串String 是一个结构体，其持有以堆（heap）的形式在内存中存储的 utf-8 字节序列。

由于它以堆的形式来存储，字符串可以延长、修改等等。这些都是字符串常量（string literals）无法执行的操作。

常用方法：

-   push\_str 用于在字符串的结尾添加字符串常量（&str）。
-   replace 用于将一段字符串替换为其它的。
-   to\_lowercase/to\_uppercase 用于大小写转换。
-   trim 用于去除字符串前后的空格。

如果字符串String 被释放（drop）了，其对应的堆内存片段也将被释放。

字符串String 可以使用 + 运算符来在其结尾处连接一个 &str 并将其自身返回。但这个方法可能并不像你想象中的那么人性化。

实例

```rust
fn main() {
    let mut helloworld = String::from("你好");
    helloworld.push_str(" 世界");
    helloworld = helloworld + "!";
    println!("{}", helloworld);
}

```

运行结果：  
你好 世界!

## 36\. 将文本作为函数的参数

字符串常量（String literals）和字符串（String）一般以字符串片段（string slice）的形式传递给函数。这给许多场景提供了充足的灵活性，因为所有权并未被传递。  
实例

```rust
fn say_it_loud(msg:&str){
    println!("{}！！！",msg.to_string().to_uppercase());
}

fn main() {
    // say_it_loud can borrow &'static str as a &str
    say_it_loud("你好");
    // say_it_loud can also borrow String as a &str
    say_it_loud(&String::from("再见"));
}

```

运行结果：  
你好！！！  
再见！！！

## 37\. 字符串构建

`concat` 和 `join` 可以以简洁而有效的方式构建字符串。

实例

```rust
fn main() {
    let helloworld = ["你好", " ", "世界", "！"].concat();
    let abc = ["a", "b", "c"].join(",");
    println!("{}", helloworld);
    println!("{}",abc);
}

```

运行结果：  
你好 世界！  
a,b,c

## 38\. 重温字符串格式化

### 1\. 宏 format!

可用于创建一个使用占位符的参数化字符串。（例：{}）

`format!` 和 `println!` 生成的参数化字符串相同，只是 format! 将其返回而 println! 将其打印出来。

```rust
format!("Hello");                 // => "Hello"
format!("Hello, {}!", "world");   // => "Hello, world!"
format!("The number is {}", 1);   // => "The number is 1"
format!("{:?}", (3, 4));          // => "(3, 4)"
format!("{value}", value=4);      // => "4"
let people = "Rustaceans";
format!("Hello {people}!");       // => "Hello Rustaceans!"
format!("{} {}", 1, 2);           // => "1 2"
format!("{:04}", 42);             // => "0042" with leading zeros
format!("{:#?}", (100, 200));     // => "(
                                  //       100,
                                  //       200,
                                  //     )"
format!("{1} {} {0} {}", 1, 2);   // => "2 1 1 2"
format!("{argument}", argument = "test");   // => "test"
format!("{name} {}", 1, name = 2);          // => "2 1"
format!("{a} {c} {b}", a="a", b='b', c=3);  // => "a 3 b"

// All of these print "Hello x    !"
println!("Hello {:5}!", "x");
println!("Hello {:1$}!", "x", 5);
println!("Hello {1:0$}!", 5, "x");
println!("Hello {:width$}!", "x", width = 5);
let width = 5;
println!("Hello {:width$}!", "x");

```

### 2\. 填充/对齐

```rust
assert_eq!(format!("Hello {:<5}!", "x"),  "Hello x    !");
assert_eq!(format!("Hello {:-<5}!", "x"), "Hello x----!");
assert_eq!(format!("Hello {:^5}!", "x"),  "Hello   x  !");
assert_eq!(format!("Hello {:>5}!", "x"),  "Hello     x!");

println!("Hello {:^15}!", format!("{:?}", Some("hi"))); // => "Hello   Some("hi")   !"

```

### 3\. 星号：.\*

`.*` 这意味着这与两个格式输入而不是一个格式输入相关联：

第一个输入保持精度，第二个输入保存要打印的值。

请注意，在这种情况下，如果使用 格式字符串，则该部分引用要打印的值，并且必须出现在前面的输入中。

> {…}usize{:.\*}precision

例如，以下调用都打印相同的内容：Hello x is 0.01000

```rust
// Hello {arg 0 ("x")} is {arg 1 (0.01) with precision specified inline (5)}
println!("Hello {0} is {1:.5}", "x", 0.01);

// Hello {arg 1 ("x")} is {arg 2 (0.01) with precision specified in arg 0 (5)}
println!("Hello {1} is {2:.0$}", 5, "x", 0.01);

// Hello {arg 0 ("x")} is {arg 2 (0.01) with precision specified in arg 1 (5)}
println!("Hello {0} is {2:.1$}", "x", 5, 0.01);

// Hello {next arg ("x")} is {second of next two args (0.01) with precision specified in first of next two args (5)}
println!("Hello {} is {:.*}",    "x", 5, 0.01);

// Hello {next arg ("x")} is {arg 2 (0.01) with precision specified in its predecessor (5)}
println!("Hello {} is {2:.*}",   "x", 5, 0.01);

// Hello {next arg ("x")} is {arg "number" (0.01) with precision specified in arg "prec" (5)}
println!("Hello {} is {number:.prec$}", "x", prec = 5, number = 0.01);


```

name是数字或者字符，打印的结果不同

```rust
println!("{}, `{name:.*}` has 3 fractional digits", "Hello", 3, name=1234.56);
println!("{}, `{name:.*}` has 3 characters", "Hello", 3, name="1234.56");
println!("{}, `{name:>8.*}` has 3 right-aligned characters", "Hello", 3, name="1234.56");

```

运行结果：  
Hello, `1234.560` has 3 fractional digits  
Hello, `123` has 3 characters  
Hello, `123` has 3 right-aligned characters

## 39\. 字符串转换

许多类型都可以通过 `to_string` 转换为字符串。

而泛型函数 `parse` 则可将字符串或是字符串常量转换为其它类型，该函数会返回 Result 因为转换有可能失败。

实例

```rust
fn main() -> Result<(), std::num::ParseIntError> {
    let a = 42;
    let a_string = a.to_string();
    let b = a_string.parse::<i32>()?;
    println!("{} {}", a, b);
    Ok(())
}

```

运行结果：  
42 42

## 40\. 动态调度和静态调度

方法的执行有两种方式：

-   静态调度——当实例类型已知时，我们直接知道要调用什么函数。
-   动态调度——当实例类型未知时，我们必须想方法来调用正确的函数。

Trait 类型 &dyn MyTrait 给我们提供了使用动态调度间接处理对象实例的能力。

当使用动态调度时，Rust 会鼓励你在你的 trait 类型前加上dyn，以便其他人知道你在做什么。

内存细节：  
动态调度的速度稍慢，因为要追寻指针以找到真正的函数调用。

实例

```rust
struct SeaCreature {
    pub name: String,
    noise: String,
}

impl SeaCreature {
    pub fn get_sound(&self) -> &str {
        &self.noise
    }
}

trait NoiseMaker {
    fn make_noise(&self);
}

impl NoiseMaker for SeaCreature {
    fn make_noise(&self) {
        println!("{}", &self.get_sound());
    }
}

fn static_make_noise(creature: &SeaCreature) {
    // 我们知道真实类型
    creature.make_noise();
}

fn dynamic_make_noise(noise_maker: &dyn NoiseMaker) {
    // 我们不知道真实类型
    noise_maker.make_noise();
}

fn main() {
    let creature = SeaCreature {
        name: String::from("Ferris"),
        noise: String::from("咕噜"),
    };
    static_make_noise(&creature);
    dynamic_make_noise(&creature);
}

```

运行结果：  
咕噜  
咕噜

Trait 对象  
当我们将一个对象的实例传递给类型为 &dyn MyTrait 的参数时，我们传递的是所谓的 trait 对象。

Trait 对象允许我们间接调用一个实例的正确方法。一个 trait 对象对应一个结构。 它保存着我们实例的指针，并保有一个指向我们实例方法的函数指针列表。

内存细节：  
这个函数列表在 C++ 中被称为 vtable。

## 41\. 泛型

实例

```rust
fn max<T: std::cmp::PartialOrd>(nn: &Vec<T>) -> &T {
    let mut f = &nn[0];
    for i in nn {
        if i > f {
            f = i;
        }
    }
    f
}
fn main() {
    let v = vec![11, 23, 13, 1, 2, 4, 678, 3, 215, 61];
    let k = vec![11.2, 23.3, 13.12, 61.98];
    let m = vec!["lilei", "zhangs", "wangw", "liulu"];
    let ret = max(&v);
    let rrt = max(&k);
    let rwt = max(&m);
    println!("max int is: {} ", ret);
    println!("max float is: {} ", rrt);
    println!("max &str is: {}", rwt);
}

```

运行结果：  
max int is: 678  
max float is: 61.98  
max &str is: zhangs

泛型函数简写  
Rust 为由 Trait 限制的泛型函数提供了简写形式：

```rust
fn my_function(foo: impl Foo) {
    ...
}
```

这段代码等价于：

```rust
fn my_function<T>(foo: T)
where
    T:Foo
{
    ...
}

```

## 42\. Box

`Box` 是一个允许我们将数据从栈上移到堆上的数据结构。

Box 是一个被称为智能指针的结构，它持有指向我们在堆上的数据的指针。

由于 Box 是一个已知大小的结构体（因为它只是持有一个指针）， 因此它经常被用在一个必须知道其字段大小的结构体中存储对某个目标的引用。

Box 非常常见，它几乎可以被用在任何地方：

```rust
Box::new(Foo { ... })
```

实例

```rust
struct SeaCreature {
    pub name: String,
    noise: String,
}

impl SeaCreature {
    pub fn get_sound(&self) -> &str {
        &self.noise
    }
}

trait NoiseMaker {
    fn make_noise(&self);
}

impl NoiseMaker for SeaCreature {
    fn make_noise(&self) {
        println!("{}", &self.get_sound());
    }
}

struct Ocean {
    animals: Vec<Box<dyn NoiseMaker>>,
}

fn main() {
    let ferris = SeaCreature {
        name: String::from("Ferris"),
        noise: String::from("咕噜"),
    };
    let sarah = SeaCreature {
        name: String::from("Sarah"),
        noise: String::from("哧溜"),
    };
    let ocean = Ocean {
        animals: vec![Box::new(ferris), Box::new(sarah)],
    };
    for a in ocean.animals.iter() {
        a.make_noise();
    }
}

```

运行结果：  
咕噜  
哧溜

## 43\. 重温泛型结构体

泛型结构体也可以通过 Trait 来约束其参数化类型：

```rust
struct MyStruct<T>
where
    T: MyTrait
{
    foo: T
    ...
}

```

泛型结构体在它的实现块中有其参数化的类型：

```rust
impl<T> MyStruct<T> { 
	... 
}
```

## 44\. 指针

引用可以转换成一个更原始的类型，指针(raw pointer)。像数字一样，它可以不受限制地复制和传递，但是 Rust 不保证它指向的内存位置的有效性。有两种指针类型：

-   \*const T - 指向永远不会改变的 T 类型数据的指针。
-   \*mut T - 指向可以更改的 T 类型数据的指针。

指针可以与数字相互转换（例如usize）。  
指针可以使用 unsafe 代码访问数据（稍后会详细介绍）。

内存细节：  
Rust中的引用在用法上与 C 中的指针非常相似，但在如何存储和传递给其他函数上有更多的编译时间限制。

Rust中的指针类似于 C 中的指针，它表示一个可以复制或传递的数字，甚至可以转换为数字类型，可以将其修改为数字以进行指针数学运算。

实例

```rust
fn main() {
    let a = 42;
    let memory_location = &a as *const i32 as usize;
    println!("Data is here {}", memory_location);
}

```

运行结果：  
Data is here 448330200684

## 45\. 解引用

访问或操作 由引用（例如&i32）指向的数据的过程称为解除引用。

有两种方式通过引用来访问或操作数据：

-   在变量赋值期间访问引用的数据。
-   访问引用数据的字段或方法。

Rust 有一些强大的运算符可以让我们做到这一点。

### 1\. 运算符 \*

`*` 运算符是一种很明确的解引用的方法。

```rust
let a: i32 = 42;
let ref_ref_ref_a: &&&i32 = &&&a;
let ref_a: &i32 = **ref_ref_ref_a;
let b: i32 = *ref_a;

```

内存细节:  
因为 i32 是实现了 `Copy` 特性的原始类型，堆栈上变量 a 的字节被复制到变量 b 的字节中。

### 2\. 运算符 .

`.` 运算符用于访问引用的字段和方法，它的工作原理更加巧妙。

```rust
let f = Foo { value: 42 };
let ref_ref_ref_f = &&&f;
println!("{}", ref_ref_ref_f.value);

```

哇，为什么我们不需要在 `ref_ref_ref_f` 之前添加 `***` ？这是因为 `.` 运算符会做一些列自动解引用操作。  
最后一行由编译器自动转换为以下内容。

```rust
println!("{}", (***ref_ref_ref_f).value);
```

## 46\. 智能指针

除了能够使用 `&` 运算符创建对现有类型数据的引用之外, Rust 给我们提供了能够创建称为智能指针的类引用结构。

我们可以在高层次上将引用视为一种类型，它使我们能够访问另一种类型。

智能指针的行为与普通引用不同，因为它们基于程序员编写的内部逻辑进行操作，作为程序员的你就是智能的一部分。

通常，智能指针实现了 `Deref`、`DerefMut` 和 `Drop` 特征，以指定当使用 `*` 和 `.` 运算符时解引用应该触发的逻辑。

## 47\. 智能不安全代码

智能指针倾向于经常使用不安全的代码。如前所述，它们是与 Rust 中最低级别的内存进行交互的常用工具。

什么是不安全代码? 不安全代码的行为与普通 Rust 完全一样，除了一些 Rust 编译器无法保证的功能。

不安全代码的主要功能是解引用指针。这意味着将原始指针指向内存中的某个位置并声明“此处存在数据结构！”并将其转换为您可以使用的数据表示（例如将\*const u8 转换为u8）。

Rust 无法跟踪写入内存的每个字节的含义。 因为 Rust 不能保证在用作 指针 的任意数字上存在什么，所以它将解引用放在一个 `unsafe { ... }` 块中。

智能指针广泛地被用来解引用指针，它们的作用得到了很好的证明。

实例

```rust
fn main() {
    let a: [u8; 4] = [86, 14, 73, 64];
    // this is a raw pointer. Getting the memory address
    // of something as a number is totally safe
    let pointer_a = &a as *const u8 as usize;
    println!("Data memory location: {}", pointer_a);
    // Turning our number into a raw pointer to a f32 is
    // also safe to do.
    let pointer_b = pointer_a as *const f32;
    println!("{:?}",pointer_b);
    let b = unsafe {
        // This is unsafe because we are telling the compiler
        // to assume our pointer is a valid f32 and
        // dereference it's value into the variable b.
        // Rust has no way to verify this assumption is true.
        *pointer_b
    };
    println!("I swear this is a pie! {}", b);
}


```

运行结果：  
Data memory location: 845782644476  
0xc4ec92f6fc  
I swear this is a pie! 3.1415

## 48\. 重温error的使用

Rust可能有过多的错误表示方法，但标准库有一个通用特性 std::error::Error 来描述错误。

使用智能指针 `Box` ，我们可以使用类型 `Box<dyn std::error::Error>` 作为常见的返回错误类型，因为它允许我们在堆上、高级别的传播错误，而不必知道特定的类型。

在 Rust 之旅的早期，我们了解到 main 可以返回一个错误。我们现在可以返回一个类型，该类型能够描述我们程序中可能发生的几乎任何类型的错误，只要错误的数据结构实现了 Rust 的通用Error特征。

> fn main() -> Result<(), Box>

实例

```rust
use core::fmt::Display;
use std::error::Error;

struct Pie;

#[derive(Debug)]
struct NotFreshError;

impl Display for NotFreshError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "This pie is not fresh!")
    }
}

impl Error for NotFreshError {}

impl Pie {
    fn eat(&self) -> Result<(), Box<dyn Error>> {
        Err(Box::new(NotFreshError))
    }
}

fn main() -> Result<(), Box<dyn Error>> {
    let heap_pie = Box::new(Pie);
    heap_pie.eat()?;
    Ok(())
}

```

运行结果：  
Error: NotFreshError

## 49\. 引用计数

`Rc` 是一个能将数据从栈移动到智能指针。它允许我们克隆其他Rc智能指针，这些指针都具有不可改变地借用放在堆上的数据的能力。  
只有当最后一个智能指针被删除时，堆上的数据才会被释放。

实例

```rust
use std::rc::Rc;

struct Pie;

impl Pie {
    fn eat(&self) {
        println!("tastes better on the heap!")
    }
}

fn main() {
    let heap_pie = Rc::new(Pie);
    let heap_pie2 = heap_pie.clone();
    let heap_pie3 = heap_pie2.clone();

    heap_pie3.eat();
    heap_pie2.eat();
    heap_pie.eat();

    // all reference count smart pointers are dropped now
    // the heap data Pie finally deallocates
}

```

运行结果：  
tastes better on the heap!  
tastes better on the heap!  
tastes better on the heap!

## 50\. 共享访问

`RefCell` 是一个容器数据结构，通常由智能指针拥有，它接收数据并让我们借用可变或不可变引用来访问内部内容。

当您要求借用数据时，它通过在运行时强制执行 Rust 的内存安全规则来防止借用被滥用。

只有一个可变引用或多个不可变引用，但不能同时有！

如果你违反了这些规则，RefCell 将会panic。

实例

```rust
use std::cell::RefCell;

struct Pie {
    slices: u8
}

impl Pie {
    fn eat(&mut self) {
        println!("tastes better on the heap!");
        self.slices -= 1;
    }
}

fn main() {
    // RefCell validates memory safety at runtime
    // notice: pie_cell is not mut!
    let pie_cell = RefCell::new(Pie{slices:8});
    
    {
        // but we can borrow mutable references!
        let mut mut_ref_pie = pie_cell.borrow_mut();
        mut_ref_pie.eat();
        mut_ref_pie.eat();
        
        // mut_ref_pie is dropped at end of scope
    }
    
    // now we can borrow immutably once our mutable reference drops
     let ref_pie = pie_cell.borrow();
     println!("{} slices left",ref_pie.slices);
}

```

运行结果：  
tastes better on the heap!  
tastes better on the heap!  
6 slices left

## 51\. 线程间共享

`Mutex` 是一种容器数据结构，通常由智能指针持有，它接收数据并让我们借用对其中数据的可变和不可变引用。这可以防止借用被滥用，因为操作系统一次只限制一个 CPU 线程访问数据，阻塞其他线程，直到原线程完成其锁定的借用。

多线程超出了 Rust 之旅的范围，但 Mutex 是协调多个 CPU 线程访问相同数据的基本部分。

有一个特殊的智能指针 `Arc` ，它与 `Rc` 相同，除了使用线程安全的引用计数递增。它通常用于对同一个 `Mutex` 进行多次引用。

实例

```rust
use std::sync::Mutex;

struct Pie;

impl Pie {
    fn eat(&self) {
        println!("only I eat the pie right now!");
    }
}

fn main() {
    let mutex_pie = Mutex::new(Pie);
    // let's borrow a locked immutable reference of pie
    // we have to unwrap the result of a lock
    // because it might fail
    let ref_pie = mutex_pie.lock().unwrap();
    ref_pie.eat();
    // locked reference drops here, and mutex protected value can be used by someone else
}

```

运行结果：  
only I eat the pie right now!

## 52\. 组合智能指针

智能指针看起来可能会存在一些限制，但是我们可以做一些非常有用的结合。

-   `Rc<Vec<Foo>>` - 允许克隆多个可以借用堆上不可变数据结构的相同向量的智能指针。
-   `Rc<RefCell<Foo>>` - 允许多个智能指针可变/不可变地借用相同的结构Foo
-   `Arc<Mutex<Foo>>` - 允许多个智能指针以 CPU 线程独占方式锁定临时可变/不可变借用的能力。

内存细节：  
您会注意到一个包含许多这些组合的主题 使用不可变数据类型（可能由多个智能指针拥有）来修改内部数据。这在 Rust 中被称为“内部可变性”模式。这种模式让我们可以在运行时以与 Rust 的编译时检查相同的安全级别来改变内存使用规则。

先看一个简单结构体的实例

```rust
use std::cell::RefCell;
use std::rc::Rc;

#[derive(Debug)]
struct S{
    age: i8,
}

fn main() {
    let a: Rc<RefCell<S>> = Rc::new(RefCell::new(S { age: 15 }));
    let mut b = a.borrow_mut();
    b.age = 10;
    println!("{:?}", b);

    drop(b);
    println!("{:?}", a.borrow());
}

```

运行结果：  
S { age: 10 }  
S { age: 10 }

先看一个简单HashMap的实例

```rust
use std::collections::HashMap;
use std::cell::RefCell;
use std::rc::Rc;

fn main() {
    let shared_map: Rc<RefCell<_>> = Rc::new(RefCell::new(HashMap::new()));
    shared_map.borrow_mut().insert("africa", 92388);
    shared_map.borrow_mut().insert("kyoto", 11837);
    shared_map.borrow_mut().insert("piccadilly", 11826);
    shared_map.borrow_mut().insert("marbles", 38);
    
    for (k, v) in shared_map.borrow().iter() {
        println!("{:?}:{:?}", k, v);
    }
}

```

运行结果：  
“marbles”:38  
“kyoto”:11837  
“africa”:92388  
“piccadilly”:11826

Tip: HashMap迭代输出，每次输出的排序结果可能不一样

实例

```rust
use std::cell::RefCell;
use std::rc::Rc;

struct Pie {
    slices: u8,
}

impl Pie {
    fn eat_slice(&mut self, name: &str) {
        println!("{} took a slice!", name);
        self.slices -= 1;
    }
}

struct SeaCreature {
    name: String,
    pie: Rc<RefCell<Pie>>,
}

impl SeaCreature {
    fn eat(&self) {
        // use smart pointer to pie for a mutable borrow
        let mut p = self.pie.borrow_mut();
        // take a bite!
        p.eat_slice(&self.name);
    }
}

fn main() {
    let pie = Rc::new(RefCell::new(Pie { slices: 8 }));
    // ferris and sarah are given clones of smart pointer to pie
    let ferris = SeaCreature {
        name: String::from("ferris"),
        pie: pie.clone(),
    };
    let sarah = SeaCreature {
        name: String::from("sarah"),
        pie: pie.clone(),
    };
    ferris.eat();
    sarah.eat();

    let p = pie.borrow();
    println!("{} slices left", p.slices);
}

```

运行结果：  
ferris took a slice!  
sarah took a slice!  
6 slices left

## 53\. 项目的结构和管理

### 1\. 模块

-   每个 Rust 程序或者库都叫 crate。
-   每个 crate 都是由模块的层次结构组成。
-   每个 crate 都有一个根模块。

模块里面可以有全局变量、全局函数、全局结构体、全局 `Trait` 甚至是全局模块！  
在 Rust 中，文件与模块树的层次结构并不是一对一的映射关系。我们必须在我们的代码中手动构建模块树。

### 2\. 编写程序

应用程序的根模块需要在一个叫 main.rs 的文件里面。

### 3\. 编写库

库的根模块需要在一个叫 lib.rs 的文件里面。

### 4\. 引用其他模块和 crate

你可以使用完整的模块路径路径引用模块中的项目： `std::f64::consts::PI` 。  
更简单的方法是使用 `use` 关键字。此关键字可以让我们在代码中使用模块中的项目而无需指定完整路径。例如 `use std::f64::consts::PI` 这样我在 main 函数中只需要写 `PI` 就可以了。  
`std` 是 Rust 的标准库。这个库中包含了大量有用的数据结构和与操作系统交互的函数。  
由社区创建的 `crate` 的搜索索引可以在这里找到： https://crates.io.

### 5\. 引用多个项目

在同一个模块路径中可以引用多个项目，比如：

```rust
use std::f64::consts::{PI,TAU}
```

Ferris 不吃桃（TAU），它只吃派（PI）。

### 6\. 创建模块

当我们想到项目时，我们通常会想象一个以目录组织的文件层次结构。Rust 允许您创建与您的文件结构密切相关的模块。  
在 Rust 中，有两种方式来声明一个模块。例如，模块 foo 可以表示为：  
一个名为 foo.rs 的文件。  
在名为 foo 的目录，里面有一个叫 mod.rs 文件。

### 7\. 模块层次结构

模块可以互相依赖。要建立一个模块和其子模块之间的关系，你需要在父模块中这样写：

```rust
mod foo;
```

上面的声明将使编译器寻找一个名为 foo.rs 或 foo/mod.rs 的文件，并将其内容插入这个作用域内名为 foo 的模块中。

### 8\. 内联模块

一个子模块可以直接内联在一个模块的代码中。  
内联模块最常见的用途是创建单元测试。 下面我们创建一个只有在使用 Rust 进行测试时才会存在的内联模块！

```rust
// 当 Rust 不在测试模式时，这个宏会删除这个内联模块。
#[cfg(test)]
mod tests {
    // 请注意，我们并不能立即获得对父模块的访问。我们必须显式地导入它们。
    use super::*;

    ... 单元测试写在这里 ...
}

```

### 9\. 模块内部引用

你可以在你的 `use` 路径中使用如下 `Rust 关键字` 来获得你想要的模块：

-   crate - 你的 crate 的根模块
-   super - 当前模块的父模块
-   self - 当前模块

### 10\. 导出

默认情况下，模块的成员不能从模块外部访问（甚至它的子模块也不行！）。我们可以使用 `pub 关键字` 使一个模块的成员可以从外部访问。  
默认情况下，`crate` 中的成员无法从当前 crate 之外访问。我们可以通过在根模块中 (lib.rs 或 main.rs)， 将成员标记为 `pub` 使它们可以访问。

### 11\. 结构体可见性

就像函数一样，`结构体` 可以使用 `pub` 声明它们想要在模块外暴露的东西。

实例

```rust
// SeaCreature 结构体在我们的模块外面也能使用了
pub struct SeaCreature {
    pub animal_type: String,
    pub name: String,
    pub arms: i32,
    pub legs: i32,
    // 我们把武器信息保密起来好了
    weapon: String,
}
```

## 54\. Prelude

你可能很好奇，为什么我们在没用 use 导入 Vec 或 Box 的情况下却可以到处使用它们。 这是因为标准库中有一个叫 `prelude` 的模块。  
要知道，在 Rust 标准库中，以 `std::prelude::*` 导出的任何东西都会自动提供给 Rust 的各个部分。 Vec 和 Box 便是如此，并且其他东西（Option、Copy 等）也是如此

你自己的 Prelude  
你看，既然标准库里面有 prelude，那么你自己的库里面最好也要有一个 prelude 模块。 这个模块可以作为其他使用你的库的用户的起点：他们可以借此导入你的库里面所有常用的数据结构 (例如 `use my_library::prelude::*`)。当然，这个模块就不会在用了你的库的程序或别的库里面自动启用了。不过使用这个惯例的话，大家会很轻松地知道从何开始的。

Ferris 说：“当个好 rustacean，帮助蟹友奏好序曲（prelude）！”

## 55\. 闭包

闭包捕获变量有三种方式，是能过三个特性实现的：

-   Fn：如果闭包只是对捕获变量的非修改操作，闭包捕获的是&T类型，闭包按照Fn trait方式执行，闭包可以重复多次执行
-   FnMut：如果闭包对捕获变量有修改操作，闭包捕获的是&mut T类型，闭包按照FnMut trait方式执行，闭包可以重复多次执行
-   FnOnce：如果闭包会消耗掉捕获的变量，变量被move进闭包，闭包按照FnOnce trait方式执行，闭包只能执行一次

这三种方式是编译器根据闭包中的行为自动选择的，我们并不能指定它。此外，还可以在闭包前添加move关键字，告诉编译器复制一份变量到闭包中，这样，闭包就不再借用外部变量了：

```rust
fn main() {
    // 第一个闭包执行完后被销毁，num=10可以被执行，再使用第二个闭包（虽然两个一模一样），
    // 第二个闭包再捕获的变量就是改变以后的了，可以到达想像中的效果。
    let mut num = 5;
    let closure = |a| println!("a={}, num={}", a, num);
    closure(10);
    num = 10;
    let closure = |a| println!("a={}, num={}", a, num);
    closure(10);
} 

```

运行结果：  
a=10, num=5  
a=10, num=10

```rust
fn main() {
    fn main() {
        // 在闭包前添加move关键字，告诉编译器复制一份变量到闭包中，这样，闭包就不再借用外部变量了
        let mut num = 5;
        let closure = move |a| println!("a={}, num={}", a, num);
        closure(10);
        num = 10;
        closure(10);
    }
} 

```

运行结果：  
a=10, num=5  
a=10, num=10

## 56\. Rust中的所有权

实例

```rust
fn main() {
    // 变量的作用域
    let string1 = String::from("string1");
    println!("{}", string1);
    {
        let string2 = String::from("string2");
        println!("{}", string2);
    }
    // 会报错
    // println!("{}", string2);

    // 报错，string1的值已经发生了转移（所有权的转移）
    // let string1 = String::from("data");
    // let string2 = string1;
    // println!("{}", string1);

    // 报错，之所以发生移动，是因为'string1'具有'String'类型，而该类型不实现'Copy'特性
    // let string1 = String::from("data");
    // {
    //     let string2 = string1; // 所有权转移
    // }
    // println!("{}", string1);
}

```

运行结果：  
string1  
string2

## 57\. 引用和借用

克隆字符串和反转实例

```rust
// 方式一
fn reverse_string(s: String) -> (String, String) {
    (s.clone(), s.chars().rev().collect())
}

fn main() {
    let s1 = String::from("any string");
    let (s1_ret, s2) = reverse_string(s1);
    println!("{} {}", s1_ret, s2);
}

```

```rust
// 方式二
fn reverse_string(s: &String) -> String {
    s.chars().rev().collect()
}

fn main() {
    let s1 = String::from("any string");
    let s2 = reverse_string(&s1);
    println!("{} {}", s1, s2);
}

```

运行结果：  
any string gnirts yna

___

## 总结

以上就是Rust语言的内容，本文介绍了Rust大部分常用的语法。路漫漫其修远兮，吾将上下而求索.