
```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            // foreach 循环 = 迭代数组的更简单方法，但灵活性较差

            String[] cars = {"BMW", "Mustang", "Corvette"};

            foreach (String car in cars)
            {
                Console.WriteLine(car);
            }

            Console.ReadKey();
        }
    }
}
```




在 C# 中，`foreach` 循环用于遍历集合或数组中的每个元素。与 `for` 循环相比，`foreach` 循环不需要手动管理循环计数器，并且更加简洁和易于阅读，特别是在处理集合或数组时。

# 
- [[#`foreach` 循环的语法|`foreach` 循环的语法]]
- [[#使用示例|使用示例]]
	- [[#使用示例#遍历数组|遍历数组]]
	- [[#使用示例#遍历列表|遍历列表]]
	- [[#使用示例#遍历字典|遍历字典]]
	- [[#使用示例#遍历多维数组|遍历多维数组]]
- [[#`foreach` 循环注意事项|`foreach` 循环注意事项]]
- [[#示例代码|示例代码]]

### `foreach` 循环的语法

```csharp
foreach (元素类型 元素名 in 集合)
{
    // 对元素进行操作
}
```

### 使用示例

#### 遍历数组

```csharp
using System;

class Program
{
    static void Main()
    {
        int[] numbers = { 1, 2, 3, 4, 5 };

        // 使用 foreach 循环遍历数组
        foreach (int number in numbers)
        {
            Console.WriteLine(number);
        }
    }
}
```

#### 遍历列表

```csharp
using System;
using System.Collections.Generic;

class Program
{
    static void Main()
    {
        List<string> names = new List<string> { "Alice", "Bob", "Charlie" };

        // 使用 foreach 循环遍历列表
        foreach (string name in names)
        {
            Console.WriteLine(name);
        }
    }
}
```

#### 遍历字典

```csharp
using System;
using System.Collections.Generic;

class Program
{
    static void Main()
    {
        Dictionary<string, int> ages = new Dictionary<string, int>
        {
            { "Alice", 25 },
            { "Bob", 30 },
            { "Charlie", 35 }
        };

        // 使用 foreach 循环遍历字典
        foreach (KeyValuePair<string, int> entry in ages)
        {
            Console.WriteLine($"{entry.Key}: {entry.Value}");
        }
    }
}
```

#### 遍历多维数组

对于多维数组，可以使用嵌套的 `foreach` 循环进行遍历。

```csharp
using System;

class Program
{
    static void Main()
    {
        int[,] matrix = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 }
        };

        // 使用 foreach 循环遍历多维数组
        foreach (int value in matrix)
        {
            Console.WriteLine(value);
        }
    }
}
```

### `foreach` 循环注意事项

1. **不可修改集合**：在 `foreach` 循环中，不允许修改集合的结构（如添加或删除元素），否则会引发运行时异常。
   
   ```csharp
   List<int> numbers = new List<int> { 1, 2, 3, 4, 5 };
   foreach (int number in numbers)
   {
       if (number == 3)
       {
           // numbers.Remove(number); // 这会导致运行时异常
       }
   }
   ```

2. **只读上下文**：循环变量在 `foreach` 块中是只读的，不能直接修改。
   
   ```csharp
   int[] numbers = { 1, 2, 3, 4, 5 };
   foreach (int number in numbers)
   {
       // number = number * 2; // 这会导致编译错误
   }
   ```

   如果需要修改数组中的值，可以使用 `for` 循环。

### 示例代码

以下是一个完整的示例代码，演示了如何使用 `foreach` 循环遍历不同类型的集合：

```csharp
using System;
using System.Collections.Generic;

class Program
{
    static void Main()
    {
        // 遍历数组
        int[] numbers = { 1, 2, 3, 4, 5 };
        Console.WriteLine("遍历数组:");
        foreach (int number in numbers)
        {
            Console.WriteLine(number);
        }

        // 遍历列表
        List<string> names = new List<string> { "Alice", "Bob", "Charlie" };
        Console.WriteLine("遍历列表:");
        foreach (string name in names)
        {
            Console.WriteLine(name);
        }

        // 遍历字典
        Dictionary<string, int> ages = new Dictionary<string, int>
        {
            { "Alice", 25 },
            { "Bob", 30 },
            { "Charlie", 35 }
        };
        Console.WriteLine("遍历字典:");
        foreach (KeyValuePair<string, int> entry in ages)
        {
            Console.WriteLine($"{entry.Key}: {entry.Value}");
        }

        // 遍历多维数组
        int[,] matrix = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 }
        };
        Console.WriteLine("遍历多维数组:");
        foreach (int value in matrix)
        {
            Console.WriteLine(value);
        }
    }
}
```

通过上述示例，可以看到 `foreach` 循环在遍历集合时的简洁和方便。