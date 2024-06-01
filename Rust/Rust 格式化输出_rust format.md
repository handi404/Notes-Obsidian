> 提示：文章写完后，目录可以自动生成，如何生成可参考右边的[帮助文档](https://so.csdn.net/so/search?q=%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3&spm=1001.2101.3001.7020)

#### 文章目录

-   [前言](https://blog.csdn.net/Bruce__taotao/article/details/136083485#_7)
-   [一、format! 宏](https://blog.csdn.net/Bruce__taotao/article/details/136083485#format__11)
-   [二、fmt::Debug](https://blog.csdn.net/Bruce__taotao/article/details/136083485#fmtDebug_39)
-   [三、fmt::Display](https://blog.csdn.net/Bruce__taotao/article/details/136083485#fmtDisplay_72)
-   [四、? 操作符 循环打印](https://blog.csdn.net/Bruce__taotao/article/details/136083485#___103)

___

## 前言

[Rust](https://so.csdn.net/so/search?q=Rust&spm=1001.2101.3001.7020)学习系列-本文根据教程学习Rust的格式化输出，包括**fmt::Debug**，**fmt::Display**等。

___

## 一、format! 宏

在 Rust 中，可以使用 `format!` 宏来进行[格式化输出](https://so.csdn.net/so/search?q=%E6%A0%BC%E5%BC%8F%E5%8C%96%E8%BE%93%E5%87%BA&spm=1001.2101.3001.7020)。这个宏类似于其他编程语言中的 `printf` 或者 `sprintf` 函数。

以下是一个简单的例子：

```rust
fn main() { let name = "Alice"; let age = 25; let height = 165.5; let formatted = format!("Name: {}, Age: {}, Height: {:.2}", name, age, height); println!("{}", formatted); }
```

这个例子中，我们使用了 `format!` 宏来创建一个格式化的字符串。在字符串中通过 `{}` 占位符来指定需要替换的值，并且可以使用 `:` 来指定格式化选项，比如使用 `:.2` 来保留小数点后两位。

然后我们使用 `println!` 宏来输出格式化后的字符串。

输出结果为：

```
Name: Alice, Age: 25, Height: 165.50
```

除了使用 `format!` 宏，还可以使用其他的格式化宏，比如 `println!` 和 `eprintln!` 用于标准输出和标准错误输出。

## 二、fmt::Debug

`rust fmt::Debug` 是 Rust 标准库中的一个 trait，用于控制如何格式化数据类型的输出。它可以被任何实现了 `std::fmt::Debug` trait 的类型使用。

`std::fmt::Debug` trait 提供了一个 `fmt` 方法，该方法接受一个实现了 `std::fmt::Debug` trait 的数据类型，并将其格式化为一个字符串。可以使用 `{}` 占位符来表示要格式化的数据，并使用 `:?` 格式化符号来表示数据类型的 Debug 格式输出。

使用 `std::fmt::Debug` trait 的主要目的是为了调试目的，因为它提供了一个格式化的输出，可以使开发人员更容易地了解数据类型的内部结构和值。

下面是一个示例代码，展示了如何使用 `std::fmt::Debug` trait：

```rust
#[derive(Debug)] struct Person { name: String, age: u32, } fn main() { let person = Person { name: String::from("Alice"), age: 30, }; println!("{:?}", person); // 美化输出 println!("{:#?}",person); }
```

在上面的示例中，`Person` 结构体实现了 `std::fmt::Debug` trait，并使用 `#[derive(Debug)]` 注解来自动实现它。在 `main` 函数中，我们创建了一个 `Person` 对象并使用 `println!` 宏来打印它。`{:?}` 是一个格式化占位符，它会调用 `std::fmt::Debug` trait 的 `fmt` 方法来打印 `person` 对象的 Debug 格式输出。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/ce08f996a134450cb4644c42bced0d9a.png)

## 三、fmt::Display

在Rust中，fmt::Display是一个trait（特征），用于定义如何格式化类型的输出。该trait包含一个名为fmt的方法，它接受一个fmt::Formatter对象，并返回一个fmt::Result对象。

通过实现fmt::Display trait，你可以指定自定义类型的输出格式，以便在使用println!和format!宏时能够以预期的方式打印该类型的实例。例如：

```rust
use std::fmt; struct Point { x: i32, y: i32, } impl fmt::Display for Point { fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result { write!(f, "({}, {})", self.x, self.y) } } fn main() { let p = Point { x: 1, y: 2 }; println!("The point is {}", p); // 输出：The point is (1, 2) }
```

在上面的例子中，我们为自定义的Point类型实现了fmt::Display trait。在fmt方法中，我们使用write!宏向Formatter对象f写入格式化的内容。

通过这种方式，你可以根据自定义类型的属性和需求，定义自己的输出格式。

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1494cbfa301b4528b74e686b40d24d2b.png)

## 四、? 操作符 循环打印

对一个结构体实现 fmt::Display，其中的元素需要一个接一个地处理到，这可能会很麻烦。问题在于每个 write! 都要生成一个 fmt::Result。正确的实现需要处理所有的 Result。Rust 专门为解决这个问题提供了 ? 操作符。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/3e29b2853a48479983ccdbc1cda2b904.png)

___