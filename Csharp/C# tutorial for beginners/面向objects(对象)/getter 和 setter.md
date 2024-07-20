#### //getters & setters = 通过封装为字段添加安全性 
#### // 它们是在属性中找到的访问器 

#### // 属性 = 结合字段和方法的各个方面（与字段共享名称） 
#### // get访问器 = 用于返回属性值 
#### // set accessor = 用于分配新值 
#### // value 关键字 = 定义由集合（参数）分配的值

---
简化  -->  [[自动实现的属性]]
```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            Car car = new Car(400);

            car.Speed = 1000000000;

            Console.WriteLine(car.Speed);

            Console.ReadKey();
        }   
    }
    class Car
    {
        private int speed;

        public Car(int speed)
        {
            Speed = speed;
        }

        public int Speed
        {
            get { return speed; }
            set                   
            {
                if (value > 500)
                {
                    speed = 500;
                }
                else
                {
                    speed = value;
                }
            }
        }

    }
}
```