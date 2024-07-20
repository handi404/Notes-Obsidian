###### // return = 将数据返回到调用方法的地方

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            

            double x;
            double y;
            double result;

            Console.WriteLine("Enter in number 1: ");
            x = Convert.ToDouble(Console.ReadLine());

            Console.WriteLine("Enter in number 2: ");
            y = Convert.ToDouble(Console.ReadLine());

            result = Multiply(x, y);

            Console.WriteLine(result);

            Console.ReadKey();
        }
        static double Multiply(double x, double y)
        {
            return x * y;
        }
    }
}
```

