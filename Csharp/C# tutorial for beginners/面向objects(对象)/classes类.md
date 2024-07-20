// class = 相关代码包。
// 可以用作创建对象的蓝图（OOP）

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            Messages.Hello();
            Messages.Waiting();
            Messages.Bye();

            Console.ReadKey();
        }
    }
    static class Messages
    {
        public static void Hello()
        {
            Console.WriteLine("Hello! Welcome to the program");
        }
        public static void Waiting()
        {
            Console.WriteLine("I am waiting for something");
        }
        public static void Bye()
        {
            Console.WriteLine("Bye! Thanks for visiting");
        }
    }
}
```