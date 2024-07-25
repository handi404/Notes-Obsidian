###### // enums = 用户定义的数据类型，包含 
###### // 成对的命名整数常量。 
###### // 如果你有一组潜在的选择那就太好了

```cpp
#include <iostream>

enum Day {sunday = 0, monday = 1, tuesday = 2, wednesday = 3,
                    thursday = 4, friday = 5, saturday = 6};

int main () {

    Day today = friday;
	//switch 不能匹配字符串
    switch(today){
        case sunday:    std::cout << "It is Sunday!\n";
                        break;
        case monday:    std::cout << "It is Monday!\n";
                        break;
        case tuesday:   std::cout << "It is Tuesday!\n";
                        break;
        case wednesday: std::cout << "It is Wednesday!\n";
                        break;
        case thursday:  std::cout << "It is Thursday!\n";
                        break;
        case friday:    std::cout << "It is Friday!\n";
                        break;
        case saturday:  std::cout << "It is Saturday!\n";
                        break;
    }

    return 0;
}
```

## C++ 枚举类型详解

### 什么是枚举类型？

C++ 枚举类型（enum）是一种用户自定义的数据类型，它将一组命名常量集合在一起。这些常量通常代表一组相关的、有限的值。枚举类型使得代码更具可读性，并且可以提高类型安全。

### 为什么使用枚举类型？

- **提高代码可读性:** 使用有意义的名称代替数字，使代码更易于理解。
- **增强类型安全:** 限制变量只能取枚举类型中定义的值，避免错误。
- **方便调试:** 调试时，更容易识别和追踪错误。

### 枚举类型的定义

```C++
enum Color {
    Red,
    Green,
    Blue
};
```

- `enum` 关键字：表示定义一个枚举类型。
- `Color`：枚举类型的名称。
- `Red`, `Green`, `Blue`：枚举常量，代表枚举类型可能的值。

### 枚举常量的值

- **默认值:** 如果不显式指定，枚举常量的值从 0 开始，依次递增 1。
- **自定义值:** 可以显式指定枚举常量的值。

```C++
enum Weekday {
    Monday = 1,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday
};
```

### 使用枚举类型

```C++
Color myColor = Red;
if (myColor == Green) {
    // ...
}
```

### 枚举类型的特点

- **枚举常量是常量:** 不能被修改。
- **枚举类型是强类型:** 不同枚举类型的变量不能相互赋值。
- **枚举类型可以隐式转换为整数:** 但不建议这样做，容易导致类型不安全。
- **枚举类型可以作为switch语句的表达式:** 方便进行条件判断。

### C++11 强类型枚举

C++11 引入了强类型枚举，以解决传统枚举的一些问题。

```C++
enum class Season : char {
    Spring,
    Summer,
    Autumn,
    Winter
};
```

- **作用域:** 强类型枚举的值具有作用域，避免了命名冲突。
- **类型安全:** 强类型枚举不能隐式转换为整数，提高了类型安全。

### 枚举类型的应用场景

- **表示一组有限的状态:** 例如，交通灯的状态、星期几、月份等。
- **定义常量:** 使用枚举常量代替魔数，提高代码可读性。
- **作为函数参数和返回值:** 传递枚举类型的值，可以增强函数的类型安全。

### 总结

C++ 枚举类型是一种非常有用的工具，可以提高代码的可读性、类型安全性和可维护性。通过合理使用枚举类型，可以编写出更加清晰、健壮的代码。