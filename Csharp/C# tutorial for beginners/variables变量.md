

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            int x; // declaration 声明
            x = 123; // initialization 初始化

            int y = 321; // declaration + initialization

            int z = x + y;

            int age = 21; // whole integer 整数
            double height = 300.5; // decimal number
            bool alive = false; // true or false
            char symbol = '@'; // single character 
            String name = "Bro"; // a series of characters

            Console.WriteLine("Hello " + name);
            Console.WriteLine("Your age is " + age);
            Console.WriteLine("Your height is " + height + "cm");
            Console.WriteLine("Are you alive? " + alive);
            Console.WriteLine("Your symbol is: " + symbol);

            String userName = symbol + name;

            Console.WriteLine("Your username is: " + userName);

            Console.ReadKey();
        }
    }
}
```