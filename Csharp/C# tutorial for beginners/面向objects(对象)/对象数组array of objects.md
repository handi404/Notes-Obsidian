

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args) {

            Car[] garage = { new Car("Mustang"), new Car("Corvette"), new Car("Lambo") };

            foreach (Car car in garage)
            {
                Console.WriteLine(car.model);
            }

            Console.ReadKey();
        }   
    }
    class Car 
    {
        public String model;

        public Car(String model)
        {
            this.model = model;
        }
    }
}
```

在C#中 ，可以创建包含对象的数组。这种数组可以存储同一类型的多个对象，并且可以使用数组的索引来访问这些对象。

### 创建和初始化对象数组

以下是一些基本操作示例：

1. **声明对象数组**
2. **实例化数组并初始化对象**
3. **访问数组中的对象**

### 示例代码

#### 1. 创建一个简单的类

首先，我们创建一个简单的类 `Person`，它包含一些属性和一个方法。

```csharp
public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }

    public Person(string name, int age)
    {
        Name = name;
        Age = age;
    }

    public void Introduce()
    {
        Console.WriteLine($"Hello, my name is {Name} and I am {Age} years old.");
    }
}
```

#### 2. 声明和实例化对象数组

接下来，我们声明一个 `Person` 对象的数组，并实例化该数组中的每个对象。

```csharp
class Program
{
    static void Main(string[] args)
    {
        // 声明一个Person对象的数组，大小为3
        Person[] people = new Person[3];

        // 初始化数组中的每个Person对象
        people[0] = new Person("Alice", 30);
        people[1] = new Person("Bob", 25);
        people[2] = new Person("Charlie", 35);

        // 访问和使用数组中的对象
        foreach (Person person in people)
        {
            person.Introduce();
        }
    }
}
```

在上面的代码中，我们：
1. 声明了一个 `Person` 对象的数组 `people`，它可以容纳3个 `Person` 对象。
2. 初始化了数组中的每个 `Person` 对象。
3. 通过 `foreach` 循环遍历数组，并调用每个对象的 `Introduce` 方法。

#### 3. 动态初始化对象数组

你也可以在声明数组时直接初始化对象数组：

```csharp
class Program
{
    static void Main(string[] args)
    {
        // 动态初始化数组并赋值
        Person[] people = new Person[]
        {
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        };

        // 访问和使用数组中的对象
        foreach (Person person in people)
        {
            person.Introduce();
        }
    }
}
```

#### 4. 使用数组的其他方法

你可以使用数组的其他常用方法，比如 `Length` 属性来获取数组的长度，或者 `Array` 类提供的静态方法对数组进行排序、查找等操作。

```csharp
class Program
{
    static void Main(string[] args)
    {
        // 动态初始化数组并赋值
        Person[] people = new Person[]
        {
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        };

        // 获取数组的长度
        Console.WriteLine($"The array contains {people.Length} people.");

        // 访问和使用数组中的对象
        foreach (Person person in people)
        {
            person.Introduce();
        }

        // 查找特定对象
        Person foundPerson = Array.Find(people, p => p.Name == "Bob");
        if (foundPerson != null)
        {
            Console.WriteLine("Found Bob!");
            foundPerson.Introduce();
        }
    }
}
```

### 总结

对象数组在C#中是非常常用的数据结构，可以方便地管理同类型的多个对象。你可以声明、初始化和遍历对象数组，并使用数组提供的各种方法来操作这些对象。通过使用数组，你可以更加高效地处理和组织数据。