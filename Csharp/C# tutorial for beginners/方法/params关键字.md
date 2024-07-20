
//params 关键字 = 采用可变数量参数的方法参数。
// 参数类型必须是一维数组

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            double total = CheckOut(3.99, 5.75, 15, 1.00, 10.25);

            Console.WriteLine(total);
            Console.ReadKey();
        }

        static double CheckOut(params double[] prices)
        {
            double total = 0;

            foreach (double price in prices)
            {
                total += price;
            }
            return total;
        }
    }
}
```

## C#中的params关键字

在C#中，`params`关键字用于声明可变参数的方法。这意味着该方法可以接受数量不定的参数。

**params关键字的语法如下：**


```c#
返回值类型 方法名(参数类型 参数名, ... , params 参数类型 参数名)
```

其中，`params`关键字必须放在所有普通参数的后面。

**params关键字的用法如下：**

- 可以向包含params参数的方法传递逗号分隔的参数列表。
- 可以传递一个与params参数类型相同的数组。
- 可以不传递任何参数。

**以下是一些使用params关键字的示例：**


```c#
// 将两个整数相加
int Sum(int x, int y)
{
  return x + y;
}

// 将任意数量的整数相加
int Sum(params int[] numbers)
{
  int sum = 0;
  foreach (int number in numbers)
  {
    sum += number;
  }
  return sum;
}

// 调用Sum方法
int result1 = Sum(10, 20); // result1为30
int[] numbers = { 30, 40, 50 };
int result2 = Sum(numbers); // result2为120
int result3 = Sum(); // result3为0
```

**params关键字的注意事项：**

- params参数必须是一维数组。
- 不允许将params修饰符与ref和out修饰符结合使用。
- 与params参数对应的实参可以是同一类型的数组名，也可以是任意多个与该数组的元素属于同一类型的变量。
- 若实参是数组则按引用传递，若实参是变量或表达式则按值传递。

**params关键字的应用场景：**

- 当需要处理不确定数量的输入参数时，可以使用params关键字来简化方法的调用。
- 当需要将一组数据作为参数传递给方法时，可以使用params关键字来避免创建临时数组。

