## **Go语言中的函数**

Go语言中，函数主要有三种类型：

-   普通函数
    
-   [匿名函数](https://so.csdn.net/so/search?q=%E5%8C%BF%E5%90%8D%E5%87%BD%E6%95%B0&spm=1001.2101.3001.7020)（闭包）
    
-   [类方法](https://so.csdn.net/so/search?q=%E7%B1%BB%E6%96%B9%E6%B3%95&spm=1001.2101.3001.7020)
    

### **1 函数定义**

Go语言函数的基本组成包括：关键字func、函数名、参数列表、返回值、函数体和返回语句。Go语言是强类型语言，无论是参数还是返回值，在定义函数时，都需要声明其类型。

![](https://img-blog.csdnimg.cn/direct/258ef7e367af4b5eb660ae09ca4f4d6e.png)

如下是Go语言中函数的一个简单示例：

```md
// 参数类型 int

// 返回类型 int

func add(a, b int) int {

return a + b

}
```

### **2 函数调用**

#### **2.1 调用同一个包定义的函数**

如果函数在同一个包中，只需要直接调用即可：

```md
func add(a, b int) int {

return a + b

}

func main() {

fmt.Println(add(1, 2)) // 3

}
```

#### **2.2 调用其他包定义的函数**

如果函数是在不同的包中，需要先导入该函数所在的包，然后才能调用该函数，例如 Add 函数在 calculator 包中。

```md
package calculator

func Add(a, b int) int {

return a + b

}
```

在 main 包中调用Add函数。

```md
package main

import (

"fmt"

"calculator"

)

func main() {

fmt.Println(calculator.Add(1, 2)) // 3

}
```

**注意：在调用其他包定义的函数时，只有这个函数名首字母大写的才可以被调用，例如函数名为add就会出现如下情况：**

![](https://img-blog.csdnimg.cn/direct/f69f27172d104d81ad5ad1d0398474b0.png)

#### **2.3 系统内置函数**

Go语言中内置了常用的函数，如下所示

| 名称 | 说明 |
| --- | --- |
| `close` | 用于在管道通信中关闭一个管道 |
| `len`、`cap` | `len` 用于返回某个类型的长度（字符串、数组、切片、字典和管道），`cap` 则是容量的意思，用于返回某个类型的最大容量（只能用于数组、切片和管道） |
| `new`、`make` | `new` 和 `make` 均用于分配内存，`new` 用于值类型和用户自定义的类型（类），`make` 用于内置引用类型（切片、字典和管道）。它们在使用时将类型作为参数：`new(type)`、`make(type)`。`new(T)` 分配类型 T 的零值并返回其地址，也就是指向类型 T 的指针，可以用于基本类型：`v := new(int)`。`make(T)` 返回类型 T 的初始化之后的值，所以 `make` 不仅分配内存地址还会初始化对应类型。 |
| `copy`、`append` | 分别用于切片的复制和动态添加元素 |
| `panic`、`recover` | 两者均用于错误处理机制 |
| `print`、`println` | 打印函数，在实际开发中建议使用 [fmt](https://golang.org/pkg/fmt/ "fmt") 包 |
| `complex`、`real`、`imag` | 用于复数类型的创建和操作 |

### **3 参数传递**

#### **3.1 按值传参**

Go语言默认使用按值传参来传递参数，也就是传递参数值的一个副本，函数收到传递进来的参数后，会将参数值拷贝给声明该参数的变量（也叫做形式参数，简称形参），如果在函数体中有对参数值做修改，实际上修改的是形参值，这不会影响到实际传递进来的参数值（也叫实际参数，简称实参）。

示例代码如下：

```md
// a,b 是形式参数

func add(a, b int) int {

a *= 2

b *= 3

return a + b

}

func main() {

x, y := 1, 2

// x,y 是实际参数

z := add(x, y)

// z的值是x*2+y*3=8，但x，y的值并未改变

fmt.Printf("add(%d, %d) = %d\n", x, y, z)

}
```

#### **3.2 引用传参**

如果需要实现在函数中修改形参值的同时改变实参，需要引用传参来实现，此时传递给函数的参数是一个指针，而指针代表的是实参的内存地址，修改指针引用的值即修改变量内存地址中存储的值，因此实参的值也会被修改。

示例代码如下：

```md
// a,b 是形式参数

func add(a, b *int) int {

*a *= 2

*b *= 3

return *a + *b

}

func main() {

x, y := 1, 2

// x,y 是实际参数

z := add(&x, &y)

// z的值是x*2+y*3=8，由于我们直接修改了内存超出地址的值，因此x变为2，y变为6

fmt.Printf("add(%d, %d) = %d\n", x, y, z)

}
```

在函数调用时，像切片（slice）、字典（map）、接口（interface）、通道（channel）这样的引用类型默认使用引用传参。

### **4 变长参数**

变长参数指的是函数参数的数量不确定，可以按照需要传递任意数量的参数到函数。

#### **4.1 基本定义和传值**

只需要在参数类型前加上 `...` 前缀，就可以将该参数声明为变长参数。

```md
// 函数Myfunc()接受任意数量的参数，这些参数的类型全部是int

func Myfunc(numbers ...int){

for _,number := range numbers {

fmt.Println(number)

}

}

// 函数可以按照如下方式调用：

Myfunc(1,2,3,4,5,6)

Myfunc(1,2,3)
```

函数调用测试：

![](https://img-blog.csdnimg.cn/direct/8232e98edc694d3e8d8377b5afcab4dd.png)

变长参数还支持传递一个 \[\]int 类型的切片，传递切片时需要在末尾加上 ... 作为标识，标识对应的参数类型是变长参数：

```md
slice := []int{1,2,3,4,5,6}

Myfunc(slice...)

Myfunc(slice[1:3]...)
```

![](https://img-blog.csdnimg.cn/direct/1be32a2d01b7400b829251c750872e9b.png)

**注：...type 只能作为函数的参数类型存在，并且必须是函数的最后一个参数。**

#### **4.2 任意类型的变长参数（泛型）**

Go语言中，可以通过指定变长参数类型为 interface{} 来实现参数的任意类型传递。

代码实现如下：

```md
// 变长参数，可以传递任意个数的参数，类型无要求

func Myfunc2(params ...interface{}) {

for _, param := range params {

fmt.Println(param)

}

}
```

![](https://img-blog.csdnimg.cn/direct/e4e255a60f9c49c59027c110ac63f0e3.png)

### **5 多返回值**

#### **5.1 多返回值**

Go语言中，函数能够支持多返回值，经常用在程序出错的时候。

代码示例如下：

```md
// 函数定义

func AddFunc(a, b *int) (int, error) {

if *a < 0 || *b < 0 {

err := errors.New("仅支持非负整数的相加")

return 0, err

}

return *a + *b, nil

}

// 函数调用

x, y := -1, 2

z, err := function.AddFunc(&x, &y)

if err != nil {

fmt.Println(err.Error())

return

}

fmt.Printf("%d + %d = %d", x, y, z)
```

![](https://img-blog.csdnimg.cn/direct/a3704aca115f42d29e1498eea7358dd1.png)

#### **5.2命名返回值**

函数设置多返回值时，还可以对返回值进行变量命名，这样就可以直接在函数中对返回值变量进行赋值，而不需要按照指定的返回值格式返回多个变量。

代码示例:

```md
// 函数定义

func AddFunc2(a, b *int) (c int, err error) {

if *a < 0 || *b < 0 {

err = errors.New("仅支持非负整数的相加")

return

}

c = *a + *b

return

}

// 函数调用

x, y := -1, 2

z, err := function.AddFunc2(&x, &y)

if err != nil {

fmt.Println(err.Error())

return

}

fmt.Printf("%d + %d = %d", x, y, z)
```

![](https://img-blog.csdnimg.cn/direct/d68a69824e764398855d2f96243876f6.png)

### **6 匿名函数与闭包**

#### **6.1 匿名函数的定义和使用**

匿名函数是一种没有指定函数名的函数声明方式。

代码示例如下:

```md
func(a,b int) int {

return a+b

}
```

Go语言中,匿名函数也可以赋值给一个变量或者直接执行:

```md
// 将匿名函数赋值给变量

sum := func(a,b int) int {

return a+b

}

// 调用匿名函数 add

fmt.Print(sum(1,2))

// 也可以在定义的时候,直接调用匿名函数

func(a,b int){

fmt.Println(a+b)

} (1,2)
```

![](https://img-blog.csdnimg.cn/direct/c6e4cb590ae449159f0ad74b2b10e4ab.png)

#### **6.2 匿名函数与闭包**

**闭包:**指引用了外部函数作用域中的变量的函数。也即，闭包是一个函数及其相关引用环境的组合。

**匿名函数和闭包的关系:**匿名函数可以用来创建闭包,当一个匿名函数引用了外部函数作用域中的变量时,该匿名函数就成了一个闭包。

#### **6.3 匿名函数的使用场景**

##### **6.3.1 保证局部变量的安全性**

匿名函数内部声明的局部变量无法从外部修改，从而确保了安全性（类似类的私有属性）。

代码示例如下:

```md
var j int =1

f := func(){

var i int = 1

fmt.Println(i,j)

}

f()

j += 2

f()
```

![](https://img-blog.csdnimg.cn/direct/6f476f26a422453599360ac9325c0461.png)

如上代码运行所示，匿名函数引用了外部变量j，所以同时也是个闭包，变量f指向的闭包引用了局部变量i和j，i在闭包内定义，其值被隔离，不能从外部修改变量，j在闭包外定义，所以可以从外部修改，闭包只是引用了变量j的值。

##### **6.3.2 将匿名函数作为函数参数**

匿名函数除了可以赋值给普通变量外，还可以作为函数参数传递到函数中进行调用，就像普通数据类型一样。

代码示例:

```md
add := func(a, b int) int {

return a + b

}

func(call func(int, int) int) {

fmt.Println(call(1, 2))

}(add)
```

![](https://img-blog.csdnimg.cn/direct/e935a3001106432fa3be88c5800041f6.png)

##### **6.3.3 将匿名函数作为函数返回值**

Go语言中，匿名函数也能够作为返回值使用。

代码示例:

```md
func defaultAdd(a, b int) func() int {

return func() int {

return a + b

}

}

// 此时返回的是匿名函数

addFunc := defaultAdd(1, 2)

// 这里才会真正的执行加法操作

fmt.Println(addFunc())
```

![](https://img-blog.csdnimg.cn/direct/a20110a034e84ed8bda631ac57faca8f.png)