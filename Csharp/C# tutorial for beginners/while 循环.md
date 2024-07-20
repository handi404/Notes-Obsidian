

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {

            // while循环=在某些条件保持为真时重复某些代码

            String name = "";

            while (name == "")
            {
                Console.Write("Enter your name: ");
                name = Console.ReadLine();
            }

            Console.WriteLine("Hello " + name);

            Console.ReadKey();
        }
    }
}
```