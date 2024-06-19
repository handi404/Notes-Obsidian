#### 文章目录

-   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_1)
-   [reflect包](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#reflect_9)
-   -   [reflect.Type](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#reflectType_10)
    -   [reflect.Value](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#reflectValue_96)
    -   [reflect.Kind](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#reflectKind_143)
    -   [具体类型、空接口与reflect.Value的相互转换](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#reflectValue_191)
-   [反射应用场景](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_247)
-   -   [修改变量的值](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_248)
    -   [访问结构体的字段信息](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_291)
    -   [调用变量所绑定的方法](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_351)
    -   [实现函数适配器](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_438)
    -   [创建任意类型的变量](https://blog.csdn.net/chenlong_cxy/article/details/137794414?spm=1001.2014.3001.5506#_503)

## 基本介绍

> 基本介绍

-   在Go中，反射（reflection）是一种机制，其允许程序在运行时检查并操作变量、类型和结构的信息，而不需要提前知道它们的具体定义，使得代码更加灵活和通用。
-   反射通常用于动态获取获取类型信息、动态创建对象、动态调用函数、动态修改对象等，在实现反射时需要用到reflect包。
-   需要注意的是，虽然反射的功能强大，但由于其使用了运行时的类型检查和动态调用，在性能上可能会有一定的开销，因此在性能敏感的场景中，应该尽量避免过度依赖反射来实现常规的编程任务。

## reflect包

### reflect.Type

> reflect.Type

-   reflect.Type是reflect包中的一个接口类型，用于表示任意变量的类型信息。
-   通过reflect包中的TypeOf函数，可以获取指定变量的类型信息。

reflect.TypeOf函数的函数原型如下：

```go
func TypeOf(i interface{}) Type
```

reflect.Type接口中常用的方法如下：

| 方法名 | 功能 |
| --- | --- |
| Kind | 获取该类型对应的Kind |
| Size | 获取该类型的大小 |
| Elem | 获取该类型的元素的Type |
| NumField | 获取结构体类型的字段数 |
| NumMethod | 获取该类型所绑定的方法数 |
| Field | 获取结构体类型的第i个字段的信息 |
| Method | 获取该类型所绑定的第i个方法的信息 |
| FieldByName | 获取结构体类型的字段中，指定字段名的字段信息 |
| MethodByName | 获取该类型所绑定的方法中，指定方法名的方法信息 |
| NumIn | 获取函数/方法类型的参数个数 |
| In | 获取函数/方法类型的第i个参数的Type |
| NumOut | 获取函数/方法类型的返回值个数 |
| Out | 获取函数/方法类型的第i个返回值的Type |

**说明一下：**

-   reflect.Type接口中的方法不需要用户手动实现，这些方法由反射系统在运行时为每个类型自动生成。
-   reflect.Type接口中的方法不是对所有类型都能使用，每个方法都有其特定的适用范围和前提条件，如果在不满足调用条件的情况下调用了某个方法，则会触发panic异常。比如Elem方法只适用于数组、channel、map、指针和切片类型，NumField和Field方法只适用于结构体类型，NumIn、In、NumOut和Out方法只适用于函数或方法类型。

> 字段信息

通过reflect.Type接口的Field或FieldByName方法，能够获取[结构体](https://so.csdn.net/so/search?q=%E7%BB%93%E6%9E%84%E4%BD%93&spm=1001.2101.3001.7020)中某个字段的字段信息，获取到的字段信息通过StructField结构体进行描述。StructField结构体的定义如下：

```go
type StructField struct {
	Name string    // field name
	PkgPath string // package path

	Type      Type      // field type
	Tag       StructTag // field tag string
	Offset    uintptr   // offset within struct, in bytes
	Index     []int     // index sequence for Type.FieldByIndex
	Anonymous bool      // is an embedded field
}

```

**字段说明：**

-   Name：表示该字段的名称。
-   PkgPath：表示该字段所在的包路径，对于可导出的字段，PkgPath为空字符串。
-   Type：表示该字段对应的reflect.Type。
-   Tag：表示该字段的Tag标签信息。
-   Offset：表示该字段在结构体中的偏移量。
-   Index：表示该字段的索引序列。
-   Anonymous：表示该字段是否为匿名字段。

> 方法信息

通过reflect.Type接口的Method或MethodByName方法，能够获取对应类型所绑定的某个方法的方法信息，获取到的方法信息通过Method结构体进行描述。Method结构体的定义如下：

```go
type Method struct {
	Name string    // method name
	PkgPath string // package path

	Type  Type  // method type
	Func  Value // func with receiver as first argument
	Index int   // index for Type.Method
}
```

**字段说明：**

-   Name：表示该方法的名称。
-   PkgPath：表示该方法所在的包路径，对于可导出的方法，PkgPath为空字符串。
-   Type：表示该方法对应的reflect.Type。
-   Func：表示该方法对应的reflect.Value。
-   Index：表示该方法在对应类型的方法集中的索引。

### reflect.Value

> reflect.Value

-   reflect.Value是reflect包中的一个类型，用于表示任意变量的值。
-   通过reflect包中的ValueOf函数，可以获取持有指定变量的Value。
-   通过reflect包中的New函数，可以创建指定类型的变量，并获取持有指向该变量的指针的Value。

reflect.ValueOf函数的函数原型如下：

```go
func ValueOf(i interface{}) Value 
func New(typ Type) Value
```

reflect.Value类型常用的方法如下：

| 方法名 | 功能 |
| --- | --- |
| Kind | 获取所持有的值对应的Kind |
| Type | 获取所持有的值对应的Type |
| Elem | 获取所持有的接口保管的值的Value封装，或获取所持有的指针指向的值的Value封装 |
| Index | 获取所持有的值的第i个元素的Value封装 |
| NumField | 获取所持有的结构体类型值的字段数 |
| NumMethod | 获取所持有的值所绑定的方法数 |
| Field | 获取所持有的结构体类型值的第i个字段的Value封装 |
| Method | 获取所持有的值所绑定的第i个方法的函数形式的Value封装 |
| FieldByName | 获取所持有的结构体类型值的字段中，指定字段名的字段的Value封装 |
| MethodByName | 获取所持有的值所绑定的方法中，指定方法名的方法的函数形式的Value封装 |
| Call | 指定参数调用所持有的函数，返回函数返回值的Value封装 |
| Interface | 返回所持有的值的interface{}类型值 |
| Int、Float、Bool、String、Pointer | 返回所持有的值的对应类型值，如果所持有的值不是对应的类型，则会触发panic异常 |
| SetInt、SetFloat、SetBool、SetString、SetPointer | 设置所持有的值，如果所持有的值不是对应的类型，则会触发panic异常 |
| Set | 将所持有的值设置为指定Value所持有的值，指定Value所持有值的类型必须与当前所持有值的类型相同，否则会触发panic异常 |

**说明一下：**

-   reflect.Value类型的方法不是对所有类型都能使用，每个方法都有其特定的适用范围和前提条件，如果在不满足调用条件的情况下调用了某个方法，则会触发panic异常。比如Elem方法只适用于接口和指针类型，NumField和Field方法只适用于结构体类型，Index方法只适用于数组、channel、切片和字符串类型，Call方法只适用于函数或方法类型。
-   通过Method或MethodByName方法，获取v所持有的值所绑定的某个方法的函数形式的Value封装时，返回值持有的函数总是使用v所持有的值作为receiver（即第一个参数），因此返回值在调用Call方法时不用手动传入receiver参数。

> reflect.Value与reflect.Type

reflect.Value是一个具体的类型，而reflect.Type被设计成了一个[接口类型](https://so.csdn.net/so/search?q=%E6%8E%A5%E5%8F%A3%E7%B1%BB%E5%9E%8B&spm=1001.2101.3001.7020)。其原因如下：

-   类型的信息需要反射系统在运行时为每个类型自动生成，以适应各种未知的类型，将reflect.Type定义成接口类型的目的就是，指明运行时需要为每个类型生成哪些方法。
-   reflect.Value提供了各种访问和操作所持有值的方法，在使用reflect.Value时，通常已经知道所持有值的具体类型，这时通过reflect.Type即可获取到所持有值的类型信息，运行时不需要为其动态的生成任何方法，因此最终将reflect.Value定义成具体类型。

### reflect.Kind

> reflect.Kind

-   reflect.Kind是reflect包中的一个类型，用于表示类型的类别。
-   reflect.Type和reflect.Value都提供了对应的Kind方法，用于获取类型的Kind。

reflect.Kind本质是一个常量[枚举类型](https://so.csdn.net/so/search?q=%E6%9E%9A%E4%B8%BE%E7%B1%BB%E5%9E%8B&spm=1001.2101.3001.7020)。其定义如下：

```go
type Kind uint

const (
	Invalid Kind = iota
	Bool
	Int
	Int8
	Int16
	Int32
	Int64
	Uint
	Uint8
	Uint16
	Uint32
	Uint64
	Uintptr
	Float32
	Float64
	Complex64
	Complex128
	Array
	Chan
	Func
	Interface
	Map
	Pointer
	Slice
	String
	Struct
	UnsafePointer
)

```

**说明一下：**

-   类型和类别可能是一对一的，比如int类型对应的Kind是Int，float32类型对应的Kind是Float32。类型和类别也可能是一对多的，比如所有结构体类型对应的Kind都是Struct，所有指针类型对应的Kind都是Pointer。
-   在获取变量的reflect.Value时，ValueOf函数或提取出接口值中对应的动态类型和动态值，并返回具体类型的Value。如果需要创建一个Kind为Interface的Value，可以先通过ValueOf函数获取一个指向接口的指针的Value，然后通过Value的Elem方法获取Value持有的指针指向的值的Value封装，这时获取到的Value的Kind就是Interface。

### 具体类型、空接口与reflect.Value的相互转换

> 具体类型、空接口与reflect.Value的相互转换

在反射过程中，变量的类型经常需要在具体类型、空接口类型和reflect.Value类型之间进行转换。其转换的方式如下：

-   将变量由具体类型转换为空接口类型时，直接通过变量赋值的方式即可。
-   将变量由空接口类型转换为reflect.Value类型时，通过调用reflect.ValueOf函数即可。
-   将变量由reflect.Value类型转换为空接口类型时，通过调用reflect.Value的Interface方法即可。
-   将变量由空接口类型转为具体类型时，需要借助类型断言。

转换示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/edd683618c4d4f9091cd3e918612c432.png)

转换案例如下：

```go
package main

import (
	"fmt"
	"reflect"
)

type Student struct {
	Name string
	Age  int
}

func Reflect(iVal interface{}) { // 具体类型->interface{}

	rVal := reflect.ValueOf(iVal) // interface{}->reflect.Value

	iVal2 := rVal.Interface() // reflect.Value->interface{}

	switch val := iVal2.(type) { // interface{}->具体类型
	case Student:
		fmt.Printf("type = %T, value = %v\n", val, val)
	case int:
		fmt.Printf("type = %T, value = %v\n", val, val)
	case float64:
		fmt.Printf("type = %T, value = %v\n", val, val)
	default:
		fmt.Printf("unknown type: %T\n", val)
	}
}

func main() {
	var stu = Student{"Alice", 14}
	Reflect(stu) // type = main.Student, value = {Alice 14}
	Reflect(1)   // type = int, value = 1
	Reflect(1.2) // type = float64, value = 1.2
}

```

## 反射应用场景

### 修改变量的值

> 修改变量的值

通过反射可以修改变量的值，具体步骤如下：

1.  通过reflect.ValueOf函数，获取指向该值的指针的Value封装v1。
2.  通过Value的Elem方法，获取v1所持有的指针指向的值的Value封装v2。
3.  通过Value的Set系列方法，设置v2所持有的值，完成对变量的修改。

案例如下：

```go
package main

import (
	"fmt"
	"reflect"
)

func Reflect(iVal interface{}) {
	rVal := reflect.ValueOf(iVal) // 获取指向该值的指针的Value封装

	switch val := iVal.(type) {
	case *int:
		rVal.Elem().SetInt(20) // 获取所持有的指针指向的值的Value封装，并设置所持有的值
	default:
		fmt.Printf("unknown type: %T\n", val)
	}
}

func main() {
	var a = 10
	Reflect(&a) // 传入的是指向变量的指针
	fmt.Printf("a = %d\n", a) // a = 20
}

```

**说明一下：**

-   通过反射修改变量的值时，需要通过指向对应变量的指针来修改，这样反射内部才能找到需要被修改的变量并对其进行修改。在修改变量的值时，需要先通过Value的Elem方法获取所持有的指针指向的值的Value封装（可以理解成对指针解引用），然后再调用Set系列方法修改变量的值。
-   除了通过Set系列方法修改变量的值外，也可以使用Set方法将当前Value所持有的值设置为另一个Value所持有的值。

### 访问结构体的字段信息

> 访问结构体的字段信息

通过反射可以访问结构体的字段信息，具体步骤如下：

1.  通过reflect.ValueOf和reflect.TypeOf函数，分别获取结构体变量的Value和Type。
2.  通过Value或Type的NumField方法，获取结构体的字段数。
3.  通过Value的Field方法，获取指定索引字段的Value。
4.  通过Type的Field方法，获取指定索引字段的各种信息。

案例如下：

```go
package main

import (
	"fmt"
	"reflect"
)

type Student struct {
	Name string `json:"name"`
	Age  int    `json:"age"`
}

func Reflect(iVal interface{}) {
	rVal := reflect.ValueOf(iVal)
	rType := reflect.TypeOf(iVal)
	rKind := rType.Kind()

	if rKind != reflect.Struct { // 确保传入的变量是结构体类型
		return
	}
	// 访问结构体的字段信息
	num := rType.NumField() // 获取结构体的字段数
	for i := 0; i < num; i++ {
		fieldInfo := rType.Field(i) // 获取结构体第i个字段的信息
		filedValue := rVal.Field(i) // 获取结构体第i个字段的Value封装

		fmt.Printf("field[%d] name = %s\ttype = %v\ttag = %s\tvalue = %v\n",
			i, fieldInfo.Name, fieldInfo.Type, fieldInfo.Tag, filedValue)
	}
}

func main() {
	var stu = Student{"Alice", 14}
	Reflect(stu)
}

```

程序的运行结果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/948964687511451d88a7471093c6af40.png)

**说明一下：**

-   上述代码中通过对变量的Kind进行判断，以确保传入的变量是结构体类型。
-   json.Marshal函数在对结构体变量进行JSON序列化时，在函数内部就是通过反射来获取结构体字段的Tag标签的。

### 调用变量所绑定的方法

> 调用变量所绑定的方法

通过反射可以调用变量所绑定的方法，具体步骤如下：

1.  通过reflect.ValueOf和reflect.TypeOf函数，分别获取变量的Value和Type。
2.  通过Value或Type的NumMethod方法，获取变量对应的类型所绑定的方法数。
3.  通过Value的Method方法，获取指定索引方法的Value。
4.  通过Type的Method方法，获取指定索引方法的各种信息。
5.  通过Value的Call方法，调用所持有的方法。

案例如下：

```go
package main

import (
	"fmt"
	"reflect"
)

type Student struct {
	Name string `json:"name"`
	Age  int    `json:"age"`
}

func (stu Student) Study() {
	fmt.Printf("Study: student %s is studying...\n", stu.Name)
}

func (stu *Student) UpdateAge(age int) {
	stu.Age = age
	fmt.Printf("UpdateAge: update %s age = %d...\n", stu.Name, stu.Age)
}

func (stu Student) StuInfo() {
	fmt.Printf("StuInfo: name = %s, age = %d...\n", stu.Name, stu.Age)
}

func Reflect(iVal interface{}) {
	rVal := reflect.ValueOf(iVal)
	rType := reflect.TypeOf(iVal)

	switch val := iVal.(type) {
	case *Student, Student:
		fmt.Printf("------type = %v------\n", rType)
		// 调用变量对应的类型所绑定的方法
		num := rType.NumMethod() // 获取该类型所绑定的方法数
		fmt.Printf("method num = %d\n", num)
		for i := 0; i < num; i++ {
			methodVal := rVal.Method(i)         // 获取该类型所绑定的第i个方法的Value封装
			methodInfo := rType.Method(i)       // 获取该类型所绑定的第i个方法的信息
			if methodInfo.Name == "UpdateAge" { // 调用时需要传参
				var args []reflect.Value
				args = append(args, reflect.ValueOf(18))
				methodVal.Call(args) // 调用方法
			} else {
				methodVal.Call(nil) // 调用方法
			}
		}
	default:
		fmt.Printf("unknown type: %T\n", val)
	}
}

func main() {
	var stu1 = Student{"Alice", 14}
	Reflect(&stu1)
	fmt.Printf("stu1 = %v\n", stu1) // stu1 = {Alice 18}

	var stu2 = Student{"Bob", 14}
	Reflect(stu2)
	fmt.Printf("stu2 = %v\n", stu2) // stu2 = {Bob 14}
}

```

程序的运行结果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/e31a1ff93c9948348c390f0c26da43fa.png)

**说明一下：**

-   通过反射获取变量对应的类型所绑定的方法数，以及获取指定索引方法的Value封装或方法信息时，如果变量的类型是`type`，则只能访问到receiver为`type`的方法，如果变量的类型是`*type`，则能同时访问到receiver为`type`和`*type`的方法。
-   因为receiver为`*type`的方法中可能会对变量的值进行修改，为了让反射内部能够找到需要被修改的变量并对其进行修改，这就要求变量的类型必须是`*type`，因此如果变量的类型是`type`，那就无法访问到receiver为`*type`的方法。
-   Value的Call方法接收一个类型为`[]Value`的参数，表示在调用Value所持有的函数或方法时，需要传入的各个参数的Value封装，如果被调用的函数或方法无需传入任何参数，则调用Call方法时传入nil即可。同时Call方法会返回一个`[]Value`类型的返回值，表示调用Value所持有的函数或方法得到的各个返回值的Value封装。

### 实现函数适配器

> 实现函数适配器

通过反射可以实现函数适配器，具体步骤如下：

1.  通过reflect.ValueOf函数，获取函数的Value。
2.  对用户传入的用于调用函数的参数进行Value封装，并放到Value切片中。
3.  通过Value的Call方法，指定参数调用所持有的函数，并返回函数调用的返回值。

案例如下：

```go
package main

import (
	"errors"
	"fmt"
	"reflect"
)

func AddTwo(num1 int, num2 int) int {
	return num1 + num2
}

func AddThree(num1 int, num2 int, num3 int) int {
	return num1 + num2 + num3
}

func Bridge(f interface{}, args ...interface{}) (ret int, err error) {
	rVal := reflect.ValueOf(f)
	rKind := rVal.Kind()
	if rKind != reflect.Func {
		err = errors.New("the first arg is not a function")
		return
	}

	// 对传入的参数进行Value封装，并放到Value切片中
	num := len(args)
	argVals := make([]reflect.Value, num)
	for i := 0; i < num; i++ {
		argVals[i] = reflect.ValueOf(args[i])
	}
	retVals := rVal.Call(argVals) // 调用函数
	ret = int(retVals[0].Int())
	return
}

func main() {
	ret, err := Bridge(AddTwo, 10, 20)
	if err != nil {
		fmt.Printf("err = %v\n", err)
	} else {
		fmt.Printf("ret = %d\n", ret) // ret = 30
	}

	ret, err = Bridge(AddThree, 10, 20, 30)
	if err != nil {
		fmt.Printf("err = %v\n", err)
	} else {
		fmt.Printf("ret = %d\n", ret) // ret = 60
	}
}

```

### 创建任意类型的变量

> 创建任意类型变量

通过反射可以创建任意类型的变量，具体步骤如下：

1.  通过reflect.ValueOf和reflect.TypeOf函数，分别获取二级指针的Value和Type，并继续通过Value和Type的Elem方法，分别获取二级指针指向的一级指针的Value和Type。
2.  再次通过Type的Elem方法，继续获取一级指针指向的元素的Type，即需要创建的变量的类型。
3.  通过reflect.New函数，创建指定Type的变量，并获取持有指向该变量的指针的Value封装elemVal。
4.  通过Value的Set方法，将一级指针所持有的值设置为elemVal所持有的值，让一级指针指向创建的变量。

案例如下：

```go
package main

import (
	"fmt"
	"reflect"
)

type Student struct {
	Name string
	Age  int
}

func CreateObj(iVal interface{}) {
	rType := reflect.TypeOf(iVal).Elem() // 获取二级指针指向的一级指针的Type
	rVal := reflect.ValueOf(iVal).Elem() // 获取二级指针指向的一级指针的Value
	rKind := rType.Kind()
	if rKind != reflect.Ptr { // 确保传入的是二级指针（该类型指向的是一个指针类型）
		return
	}
	elemType := rType.Elem()         // 获取一级指针指向的元素的Type
	elemVal := reflect.New(elemType) // 创建elemType类型的变量，并获取持有指向该变量的指针的Value
	rVal.Set(elemVal)                // 将一级指针所持有的值设置为elemVal所持有的值
}

func main() {
	var p1 *Student
	fmt.Printf("p1 = %v\n", p1) // p1 = <nil>
	CreateObj(&p1)
	fmt.Printf("p1 = %v\n", p1) // p1 = &{ 0}

	var p2 *int
	fmt.Printf("p2 = %v\n", p2) // p2 = <nil>
	CreateObj(&p2)
	fmt.Printf("p2 = %v\n", p2) // p2 = 0xc00000e0f8
}

```

**说明一下：**

-   在创建变量时需要提供一个`*type`类型的指针，然后根据指针的类型创建一个`type`类型的变量，并让该指针指向这个变量，完成变量的创建。由于最终需要修改所给指针变量的指向，因此在调用CreateObj函数时需要传入该指针的地址（二级指针）。