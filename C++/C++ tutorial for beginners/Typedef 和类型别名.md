

```c++
#include <iostream>

//typedef std::string text_t;
//typedef int number_t;
using text_t = std::string;
using number_t = int;

int main(){

    text_t firstName = "Bro";
    number_t age = 21;

    std::cout << firstName << '\n';
    std::cout << age << '\n';

    return 0;
}
```

## C++ Typedef 和类型别名

### 什么是 Typedef 和类型别名？

**Typedef** 和 **类型别名** 都是 C++ 中用于给现有类型取一个别名的机制，本质上是为类型起了个新名字。这个新名字可以更具描述性，或者简化复杂的类型声明。

### Typedef

- **传统方式：** C++ 早期使用 `typedef` 关键字来定义类型别名。
- **语法：**
    
    
    ```C++
    typedef 原类型 新类型名;
    ```
    
- **示例：**
    
    
    ```C++
    typedef int Integer;
    typedef char* String;
    typedef std::vector<int> IntVector;
    ```
    

### 类型别名 (C++11 及以后)

- **新标准引入：** C++11 引入了 `using` 关键字来定义类型别名，更加直观和现代。
- **语法：**
    
    
    ```C++
    using 新类型名 = 原类型;
    ```
    
- **示例：**
    
    
    ```C++
    using Integer = int;
    using String = char*;
    using IntVector = std::vector<int>;
    ```
    

### 两者区别

- **语法：** `typedef` 的语法相对较老，而 `using` 的语法更简洁直观。
- **本质：** 两者都是为类型起别名，没有本质区别。
- **推荐使用：** C++11 及以后，推荐使用 `using`，因为它更符合现代 C++ 的风格。

### 使用场景

- **简化复杂类型声明：**
    
    
    ```C++
    using ComplexType = std::map<std::string, std::vector<int>>;
    ```
    
- **增强代码可读性：**
    
    
    ```C++
    using Byte = unsigned char;
    ```
    
- **定义平台无关类型：**
    
    
    ```C++
    #ifdef _WIN32
    typedef int intptr_t;
    #else
    typedef long intptr_t;
    #endif
    ```
    
- **为函数指针定义别名：**
    
    
    ```C++
    typedef int (*CompareFunc)(const void*, const void*);
    ```
    

### 注意事项

- **别名不是新类型：** 类型别名只是给现有类型起了个新名字，本质上还是原来的类型。
- **作用域：** 类型别名的作用域与其他标识符的作用域相同。
- **模板类型参数：** 类型别名可以用于模板类型参数。

### 总结

- **Typedef** 和 **类型别名** 的功能相同，都是为类型起别名。
- **C++11 及以后推荐使用 `using`**，因为它更简洁直观。
- **选择合适的场景使用**，可以提高代码的可读性和可维护性。

### 常见问题

- **Typedef 和 define 有什么区别？**
    - `typedef` 用于定义类型别名，而 `define` 是预处理器指令，用于文本替换。
    - `typedef` 在编译时处理，而 `define` 在预处理时处理。
    - `typedef` 更安全，因为它参与类型检查。
- **类型别名和模板别名有什么区别？**
    - 类型别名是为具体类型起别名，而模板别名是为模板类型参数起别名。