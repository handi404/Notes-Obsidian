#### // sizeof() = 确定 a 的大小（以字节为单位）： 
#### // 变量、数据类型、类、对象等

---

```c++
#include <iostream>

int main()
{
    std::string name = "Bro Code";
    double gpa = 2.5;
    char grade = 'F';
    bool student = true;
    char grades[] = {'A', 'B', 'C', 'D', 'F'};
    std::string students[] = {"Spongebob", "Patrick", "Squidward", "Sandy"};

    std::cout << sizeof(name) << " bytes\n";
    std::cout << sizeof(students)/sizeof(std::string) << " elements\n";
    
    return 0;
}
```

## C++ sizeof() 运算符详解

**sizeof** 运算符是 C++ 中的一个关键字，用于获取一个对象（数据类型或数据对象）的字节大小。其返回值是 `size_t` 类型的常量表达式。

**语法：**

```C++
sizeof(operand)
```

其中，`operand` 可以是以下几种类型：

- **基本数据类型**：例如 `int`、`char`、`float`、`double` 等。
- **用户定义类型**：例如结构体、联合体、类等。
- **数组**：sizeof 运算符将返回数组元素的总大小，而不是数组本身的大小。
- **指针**：sizeof 运算符将返回指针所指向的数据类型的字节大小。
- **引用**：sizeof 运算符将返回引用所指对象的字节大小。

**返回值：**

sizeof 运算符的返回值是 `size_t` 类型的常量表达式。`size_t` 是一个无符号整数类型，用于表示对象的字节大小。

**示例：**

```C++
int i = 10;
char ch = 'a';
float f = 3.14f;
double d = 1.7e10;

std::cout << sizeof(i) << std::endl; // 输出：4
std::cout << sizeof(ch) << std::endl; // 输出：1
std::cout << sizeof(f) << std::endl; // 输出：4
std::cout << sizeof(d) << std::endl; // 输出：8

int numbers[10];
std::cout << sizeof(numbers) << std::endl; // 输出：40

int* p = &i;
std::cout << sizeof(p) << std::endl; // 输出：4

int& r = i;
std::cout << sizeof(r) << std::endl; // 输出：4
```

**注意事项：**

- sizeof 运算符是一个编译时运算符，这意味着它会在程序编译时计算对象的字节大小。在运行时，sizeof 运算符不会返回任何值。
- sizeof 运算符不能用于计算表达式或函数的值。例如，以下代码将导致编译错误：

```C++
int i = 10;
std::cout << sizeof(i + 1) << std::endl; // 错误：表达式不能用于 sizeof 运算符
```

- sizeof 运算符不能用于获取对象的地址。例如，以下代码将导致编译错误：

```C++
int i = 10;
int* p = &i;
std::cout << sizeof(p) << std::endl; // 错误：不能获取对象的地址
```

**总结：**

sizeof 运算符是 C++ 中一个非常有用的运算符，可以用于获取各种对象的字节大小。在编写 C++ 程序时，可以根据需要使用 sizeof 运算符来获取相关信息。