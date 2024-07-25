#### // enums = 包含一组命名整数常量的特殊“类”。 
#### // 当你知道你的值不会改变时使用枚举， 
#### // 要从项目中获取整数值，必须显式转换为 int 
#### // name = integer

---

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {

        static void Main(string[] args)
        {
            String name = PlanetRadius.Earth.ToString();
            int radius = (int)PlanetRadius.Earth;
            double volume = Volume(PlanetRadius.Earth);

            Console.WriteLine("planet: " + name); //Earth
            Console.WriteLine("radius: " + radius +"km"); //radius: 6371km
            Console.WriteLine("volume: " + volume +"km^3");

            Console.ReadKey();
        }
        public static double Volume(PlanetRadius radius)
        {
            double volume = (4.0 / 3.0) * Math.PI * Math.Pow((int)radius, 3);
            return volume;
        }
    }
    enum Planets
    {
        Mercury = 1, 
        Venus = 2, 
        Earth = 3, 
        Mars = 4, 
        Jupiter = 5, 
        Saturn = 6, 
        Uranus = 7, 
        Neptune = 8, 
        Pluto = 9
    }

    enum PlanetRadius
    {
        Mercury = 2439,
        Venus = 6051,
        Earth = 6371,
        Mars = 3389,
        Jupiter = 69911,
        Saturn = 58232,
        Uranus = 25362,
        Neptune = 24622,
        Pluto = 1188
    }
}
```