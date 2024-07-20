#### //ToString() = 将对象转换为其字符串表示形式，以便适合显示

---

```c#
using System;

namespace MyFirstProgram
{
    class Program 
    {
        static void Main(string[] args)
        {
            Car car = new Car("Chevy", "Corvette", 2022, "blue");

            Console.WriteLine(car.ToString());

            Console.ReadKey();
        }
    }
    class Car
    {
        String make;
        String model;
        int year;
        String color;

        public Car(String make, String model, int year, String color)
        {
            this.make = make;
            this.model = model;
            this.year = year;
            this.color = color;
        }
        public override string ToString()
        {       
            return "This is a " + make + " " + model;
        }
    }
}
```