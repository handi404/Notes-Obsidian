#### Go语言Interface使用详解

-   [初识interface](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#interface_1)
-   -   [基本语法](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#_5)
    -   [其他注意事项](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#_27)
-   [interface底层实现](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#interface_78)
-   -   [iface](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#iface_81)
    -   [eface](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#eface_176)
-   [侵入式与非侵入式的理解](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#_192)
-   [interface的应用场景](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#interface_273)
-   -   [类型转换](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#_274)
    -   [实现多态功能](https://blog.csdn.net/yuqiang870/article/details/124746693?spm=1001.2014.3001.5506#_334)

## 初识interface

Go语言的面向对象的知识点时，发现它的面向对象能力全靠 interface 撑着，而且它的 interface 还与我们以前知道的 interface 完全不同。故而整个过程不断的思考为什么要如此设计？这样设计给我们带来了什么影响？

interface(接口)是golang最重要的特性之一，实现多态。Interface类型可以定义一组方法，但是这些不需要实现。并且interface不能包含任何变量。

### 基本语法

定义一个接口

```go
type Person interface {
    // 声明方法
    method1(参数列表)返回值列表
    method2(参数列表)返回值列表
}

```

实现一个接口

```go
func (t 自定义类型）method1(参数列表）返回值列表 {
    //方法实现
}
func (t 自定义类型）method2(参数列表）返回值列表 {
    //方法实现
}

```

小结：  
（1）接口里的所有方法都没有方法体，即接口的方法都是没有实现的方法。接口体现了程序设计的多态和高内聚低耦合的思想。  
（2）Go中的接口，不需要显示的实现。只要一个变量，含有[接口类型](https://so.csdn.net/so/search?q=%E6%8E%A5%E5%8F%A3%E7%B1%BB%E5%9E%8B&spm=1001.2101.3001.7020)中的所有方法，那么这个变量就实现这个接口。因此，Go中没有implement关键字样。  
（3）Go实现接口与方法有关，与接口本身叫什么名字没有特别大的关系。变量需要实现接口所有的方法。

### 其他注意事项

(1)接口本身不能创建实例，但是可以指向一个实现了该接口的自定义类型的变量（实例）。

```go
package main

import "fmt"

// Person 定义接口
type Person interface {
	GetName() string
	GetAge() uint32
}

// Student 定义类型
type Student struct {
	Name string
	Age uint32
}

func (s Student) GetName()  string{
	return s.Name
}
func (s Student) GetAge()  uint32{
	return s.Age
}

func main() {

var student Student
	student.Age = 12
	student.Name = "小明"

var person Person
	person = student  //接口执行向student
	fmt.Printf("name:%s,age: %d\n", person.GetName(), person.GetAge())
}

```

（2）接口中所有的方法都没有方法体，即都是没有实现的方法。  
（3）在Go中，一个自定义类型需要将某个接口的所有方法都实现，我们说这个自定义类型实现了该接口。

（4）一个自定义类型只有实现了某个接口，才能将该自定义类型的实例（变量）赋给接口类型。

（5）只要是自定义数据类型就可以实现接口，不仅仅是结构体类型。  
（6）一个自定义类型可以实现多个接口。  
（7）Go接口不能有任何变量。  
（8）一个接口可以继承多个别的接口，这时如果要实现这个接口必须实现它继承的所有接口的方法。在低版本的Go编辑器中，一个接口继承其他多个接口时，不允许继承的接口有相同的方法名。比如A接口继承B、C接口，B、C接口的方法名不能一样。高版本的Go编辑器没有相关问题。  
（9）interface类型默认是一个指针（引用类型），如果没有对interface初始化就使用，那么会输出nil。  
（10）空接口interface{}没有任何方法，所以所有类型都实现了空接口，即我们可以把任何一个变量赋给空接口类型。

## interface底层实现

Go的interface源码在Golang源码的runtime目录中。  
Go的interface是由两种类型来实现的：iface和eface。

### iface

iface是包含方法的interface，如：

```go
type Person interface { 
	Print() 
}
```

iface的源代码是：

```go
type iface struct { 
	tab *itab 
	data unsafe.Pointer 
}
```

iface具体结构是：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/ba9146a847e14d4a990ce25ff8083cd6.jpeg#pic_center)

itab是iface不同于eface的关键数据结构。其包含两部分：一部分是唯一确定包含该interface的具体结构类型，一部分是指向具体方法集的指针。其具体结构为：  
![itab结构](https://img-blog.csdnimg.cn/1ae34d2d47414b00886b8a1198066d7c.jpeg#pic_center)

属性 itab的源代码是：

```go
type itab struct {
	inter *interfacetype //此属性用于定位到具体interface
	_type *_type         //此属性用于定位到具体interface
	hash  uint32         // copy of _type.hash. Used for type switches.
	_     [4]byte
	fun   [1]uintptr     // variable sized. fun[0]==0 means _type does not implement inter.
}

```

属性interfacetype类似于\_type，其作用就是interface的公共描述，类似的还有maptype、arraytype、chantype…其都是各个结构的公共描述，可以理解为一种外在的表现信息。interfacetype源码如下：

```go
type interfacetype struct {
	typ     _type
	pkgpath name
	mhdr    []imethod
}
type imethod struct {
	name nameOff
	ityp typeOff
}
```

iface的整体结构为：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/f1d7a2cf59734a24ab2584c9b6ed65a1.jpeg#pic_center)  
我们来看一个例子，对于含有方法的interface赋值后的内部结构是怎样的呢？

```go
package main

import "fmt"

// Person 定义接口
type Person interface {
	GetName() string
	GetAge() uint32
}

// Student 定义类型
type Student struct {
	Name string
	Age uint32
}

func (s Student) GetName()  string{
	return s.Name
}
func (s Student) GetAge()  uint32{
	return s.Age
}

func main() {

var student Student
	student.Age = 12
	student.Name = "小明"

var person Person
	person = student
	fmt.Printf("name:%s,age: %d\n", person.GetName(), person.GetAge())
}

```

运行结果:

```go
name:小明,age: 12 

Process finished with the exit code 0
```

内存分布示意图：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/2dff1c73c3c347f58752c56db7b87292.jpeg#pic_center)

### eface

eface是不包含方法的interface，即空interface，如：

```go
type Person interface { }
```

或者

```go
var person interface{} = xxxx实体
```

## 侵入式与非侵入式的理解

侵入式  
你的代码里已经嵌入了别的代码，这些代码可能是你引入过的框架，也可能是你通过接口继承得来的，比如：java中的继承，必须显示的表明我要继承那个接口，这样你就可以拥有侵入代码的一些功能。所以我们就称这段代码是侵入式代码。  
优点：通过侵入代码与你的代码结合可以更好的利用侵入代码提供给的功能。  
缺点：框架外代码就不能使用了，不利于代码复用。依赖太多重构代码太痛苦了。

非侵入式  
正好与侵入式相反，你的代码没有引入别的包或框架，完完全全是自主开发。比如go中的接口，不需要显示的继承接口，只需要实现接口的所有方法就叫实现了该接口，即便该接口删掉了，也不会影响我，所有go语言的接口数非侵入式接口；再如Python所崇尚的鸭子类型。

优点：代码可复用，方便移植。非侵入式也体现了代码的设计原则：高内聚，低耦合。  
缺点：无法复用框架提供的代码和功能。

接下来看看java与go语言编程实现接口来理解侵入式与非侵入式的区别。

java语言实现  
定义接口

```java
public interface IPersonService {
    String getName();
    Integer getAge();
}
```

实现接口的类

```java
public class PersonService implements IPersonService{
    @Override
    public String getName() {
        return "小明";
    }
    @Override
    public Integer getAge() {
        return 12;
    }
}

```

go语言实现

```go
package main
import "fmt"

// Person 定义接口
type Person interface {
	GetName() string
	GetAge() uint32
}
// Student 定义类型
type Student struct {
	Name string
	Age uint32
}

func (s Student) GetName()  string{
	return s.Name
}
func (s Student) GetAge()  uint32{
	return s.Age
}

func main() {

var student Student
	student.Age = 12
	student.Name = "小明"

var person Person
	person = student
	fmt.Printf("name:%s,age: %d\n", person.GetName(), person.GetAge())
}

```

通过上面的例子我们总结了以下问题：

1.  侵入式通过 implements 把实现类与具体接口绑定起来了，因此有了强耦合;
2.  假如修改了接口方法，则实现类方法必须改动；
3.  假如类想再实现一个接口，实现类也必须进行改动；
4.  后续实现此接口的类，必须了解相关的接口；  
    Go语言非侵入式的方式很好地解决了这几个问题，只要实现了实现了与接口相同的方法，就实现了这个接口。随着代码量的增加，根本不需要的关心实现了哪些接口，不需要刻意去先定义接口再实现接口的固定模式，在原有类新增实现接口时，不需要更改类，做到低侵入式、低耦合开发的好处。

## interface的应用场景

### 类型转换

类型推断可将接口变量还原为原始类型，或用来判断是否实现了某个更具体的接口类型。

```go
type data int
  
func(d data)String()string{ 
   return fmt.Sprintf("data:%d",d) 
} 
  
func main() { 
   var d data=15
   var x interface{} =d
  
   if n,ok:=x.(fmt.Stringer);ok{  // 转换为更具体的接口类型 
       fmt.Println(n) 
    } 
  
   if d2,ok:=x.(data);ok{        // 转换回原始类型 
       fmt.Println(d2) 
    } 
  
   e:=x.(error)           // 错误:main.data is not error
   fmt.Println(e) 
}

```

输出为：

```go
data:15 
data:15 
panic:interface conversion:main.data is not error:missing method Error
```

但是此处会触发[panic](https://so.csdn.net/so/search?q=panic&spm=1001.2101.3001.7020)，使用ok-idiom模式，即便转换失败也不会引发panic。还可用switch语句在多种类型间做出推断匹配，这样空接口就有更多发挥空间。

```go
func main() {
var x interface{} =func(x int)string{ 
       return fmt.Sprintf("d:%d",x) 
    } 
  
   switch v:=x.(type) {            // 局部变量v是类型转换后的结果 
   case nil: 
       println("nil") 
   case*int: 
       println(*v) 
   case func(int)string: 
       println(v(100)) 
   case fmt.Stringer: 
       fmt.Println(v) 
   default: 
       println("unknown") 
    } 
}

```

输出为：

```go
d:100
```

### 实现多态功能

多态功能是interface实现的重要功能，也是Golang中的一大行为特色，其多态功能一般要结合Go method实现，作为函数参数可以容易的实现多台功能。

```go
package main

import "fmt"

// notifier是一个定义了通知类行为的接口
type notifier interface {
　 notify()
}

// 定义user及user.notify方法
type user struct {
　 name　string
　 email string
}

func (u *user) notify() {
　 fmt.Printf("Sending user email to %s<%s>\n",
　　　 u.name,
　　　 u.email)
}

// 定义admin及admin.notify方法
type admin struct {
　 name　string
　 email string
}

func (a *admin) notify() {
　 fmt.Printf("Sending admin email to %s<%s>\n",
　　　 a.name,
　　　 a.email)
}

func main() {
　 // 创建一个user值并传给sendNotification
　 bill := user{"Bill", "bill@email.com"}
　 sendNotification(&bill)

　 // 创建一个admin值并传给sendNotification
　 lisa := admin{"Lisa", "lisa@email.com"}
　 sendNotification(&lisa)
}

// sendNotification接受一个实现了notifier接口的值
// 并发送通知
func sendNotification(n notifier) {
　 n.notify()
}
```

上述代码中实现了一个多态的例子，函数sendNotification接受一个实现了notifier接口的值作为参数。既然任意一个实体类型都能实现该接口，那么这个函数可以针对任意实体类型的值来执行notify方法，调用notify时，会根据对象的实际定义来实现不同的行为，从而实现多态行为。