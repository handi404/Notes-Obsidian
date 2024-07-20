##### // static = 修饰符声明一个静态成员，该成员属于类本身 
##### // 而不是任何特定的对象

---

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            Car car1 = new Car("Mustang");
            Car car2 = new Car("Corvette");
            Car car3 = new Car("Lambo");

            Console.WriteLine(Car.numberOfCars);
            Car.StartRace();

            Console.ReadKey();
        }
    }
    class Car
    {
        String model;
        public static int numberOfCars;	//**属于Car** 而不是特定的对象

        public Car(String model)
        {
            this.model = model;
            numberOfCars++;
        }

        public static void StartRace()	//同上
        {
            Console.WriteLine("The race has begun!");
        }
    }
}
```