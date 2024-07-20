

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {

            // for 循环 = 重复某些代码有限次

            // Count up to 10
            for (int i = 1; i <= 10; i++)
            {
                Console.WriteLine(i);
            }
               
            // Count down from 10
            for (int i = 10; i > 0; i--)
            {
                Console.WriteLine(i);
            }
            Console.WriteLine("HAPPY NEW YEAR!");

            Console.ReadKey();
        }
    }
}
```

## C# 中的 for 循环

C# 中的 for 循环是一种控制流语句，用于重复执行一段代码块。它是一种常用的循环结构，用于遍历集合、计数等场景。

### for 循环语法

C#

```
for (init; condition; increment)
{
    // 循环体
}
```

其中：

- `init`：初始化表达式，仅在进入循环之前执行一次，通常用于声明和初始化循环控制变量。
- `condition`：条件表达式，每次迭代之前都会检查该表达式，如果为真则继续执行循环体，否则跳出循环。
- `increment`：迭代表达式，每次迭代结束后都会执行该表达式，通常用于更新循环控制变量。
- `循环体`：要重复执行的代码块。

### for 循环示例

**示例 1：打印数字 1 到 10**

C#

```
for (int i = 1; i <= 10; i++)
{
    Console.WriteLine(i);
}
```

该代码将输出以下内容：

```
1
2
3
4
5
6
7
8
9
10
```

**示例 2：计算 1 到 100 的和**

C#

```
int sum = 0;
for (int i = 1; i <= 100; i++)
{
    sum += i;
}
Console.WriteLine(sum);
```

该代码将输出以下内容：

```
5050
```

### for 循环的应用

- 遍历集合：可以使用 for 循环来遍历数组、列表、字典等集合类型的每个元素。
- 计数：可以使用 for 循环来执行固定次数的重复操作。
- 模拟迭代：可以使用 for 循环来模拟一些现实世界的迭代过程。

### for 循环的注意事项

- 条件表达式的值必须是布尔类型。
- 迭代表达式的值可以省略，但通常建议使用迭代表达式来更新循环控制变量。
- 可以在循环体中使用 `break` 语句跳出循环。
- 可以在循环体中使用 `continue` 语句跳过当前迭代。

### 总结

for 循环是 C# 中一种常用的循环结构，用于重复执行一段代码块。它具有语法简单、易于理解等特点，可以在许多场景中使用。

希望以上内容能够帮助您理解 C# 中的 for 循环。如果您还有其他问题，请随时提问。