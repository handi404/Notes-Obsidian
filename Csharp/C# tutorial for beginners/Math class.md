
C#中的`Math`类是一个静态类，包含了一系列用于数学运算的方法。以下是`Math`类的一些常用方法和属性：

### 常用属性

- `Math.PI`: 返回圆周率常数 π (约为 3.14159)。
- `Math.E`: 返回自然对数的底数 e (约为 2.71828)。

### 常用方法

#### 三角函数

- `Math.Sin(double a)`: 计算角度 `a` 的正弦值。
- `Math.Cos(double a)`: 计算角度 `a` 的余弦值。
- `Math.Tan(double a)`: 计算角度 `a` 的正切值。
- `Math.Asin(double a)`: 计算值 `a` 的反正弦值。
- `Math.Acos(double a)`: 计算值 `a` 的反余弦值。
- `Math.Atan(double a)`: 计算值 `a` 的反正切值。

#### 幂和对数函数

- `Math.Pow(double x, double y)`: 计算 `x` 的 `y` 次幂。
- `Math.Sqrt(double d)`: 计算 `d` 的平方根。
- `Math.Exp(double d)`: 计算 `e` 的 `d` 次幂。
- `Math.Log(double d)`: 计算 `d` 的自然对数 (以 e 为底)。
- `Math.Log10(double d)`: 计算 `d` 的以 10 为底的对数。

#### 舍入和其他数值函数

- `Math.Abs(double d)`: 计算 `d` 的绝对值。
- `Math.Ceiling(double a)`: 返回大于或等于指定数值的最小整数。
- `Math.Floor(double d)`: 返回小于或等于指定数值的最大整数。
- `Math.Round(double d)`: 将 `d` 四舍五入到最近的整数。
- `Math.Truncate(double d)`: 返回 `d` 的整数部分。

#### 最大值和最小值

- `Math.Max(double a, double b)`: 返回 `a` 和 `b` 中的较大值。
- `Math.Min(double a, double b)`: 返回 `a` 和 `b` 中的较小值。

### 示例代码

下面是一些使用 `Math` 类的示例代码：

```c#
using System;

class Program
{
    static void Main()
    {
        // 三角函数
        double angle = 45.0;
        double radians = angle * (Math.PI / 180); // 将角度转换为弧度
        Console.WriteLine("Sin: " + Math.Sin(radians));
        Console.WriteLine("Cos: " + Math.Cos(radians));
        Console.WriteLine("Tan: " + Math.Tan(radians));

        // 幂和对数函数
        Console.WriteLine("Pow: " + Math.Pow(2, 3)); // 2^3
        Console.WriteLine("Sqrt: " + Math.Sqrt(16)); // 平方根
        Console.WriteLine("Exp: " + Math.Exp(1)); // e^1
        Console.WriteLine("Log: " + Math.Log(Math.E)); // 自然对数
        Console.WriteLine("Log10: " + Math.Log10(100)); // 10为底的对数

        // 舍入和其他数值函数
        Console.WriteLine("Abs: " + Math.Abs(-5.5));
        Console.WriteLine("Ceiling: " + Math.Ceiling(4.3));
        Console.WriteLine("Floor: " + Math.Floor(4.7));
        Console.WriteLine("Round: " + Math.Round(4.5));
        Console.WriteLine("Truncate: " + Math.Truncate(4.7));

        // 最大值和最小值
        Console.WriteLine("Max: " + Math.Max(3, 7));
        Console.WriteLine("Min: " + Math.Min(3, 7));
    }
}
```