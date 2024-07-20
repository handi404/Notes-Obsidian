#### //方法重写=提供从父类继承的方法的新版本 
#### // 继承的方法必须是：abstract, virtual, or already overriden (抽象、虚拟或已重写)
#### // 与ToString()一起使用，多态性

---

```c#
using System;

namespace MyFirstProgram
{
    class Program
    {
        static void Main(string[] args) 
        {
            Dog dog = new Dog();
            Cat cat = new Cat();

            dog.Speak();
            cat.Speak();

            Console.ReadKey();
        }
    }
    class Animal 
    {
        public virtual void Speak()
        {
            Console.WriteLine("The animal goes *brrr*");
        }
    }
    class Dog : Animal
    {
        public override void Speak()
        {
            Console.WriteLine("The dog goes *woof*");
        }
    }
    class Cat : Animal
    {

    }
}
```

方法重写（Method Overriding）是面向对象编程中一个重要的概念。在C#中 ，方法重写允许子类提供基类方法的特定实现。基类的方法必须用 `virtual` 修饰符声明，而子类中的重写方法则使用 `override` 修饰符。

### 基本示例

以下是方法重写的基本示例，包括如何声明、重写和调用这些方法。

#### 1. 基类和虚方法

首先，我们定义一个基类 `Animal`，其中包含一个可以被重写的虚方法 `MakeSound`：

```csharp
public class Animal
{
    public string Name { get; set; }

    public Animal(string name)
    {
        Name = name;
    }

    public virtual void MakeSound()
    {
        Console.WriteLine($"{Name} makes a sound.");
    }
}
```

#### 2. 派生类和重写方法

然后，我们定义一个派生类 `Dog`，重写 `Animal` 类中的 `MakeSound` 方法：

```csharp
public class Dog : Animal
{
    public Dog(string name) : base(name)
    {
    }

    public override void MakeSound()
    {
        Console.WriteLine($"{Name} barks.");
    }
}
```

同样，我们可以定义另一个派生类 `Cat`，也重写 `MakeSound` 方法：

```csharp
public class Cat : Animal
{
    public Cat(string name) : base(name)
    {
    }

    public override void MakeSound()
    {
        Console.WriteLine($"{Name} meows.");
    }
}
```

#### 3. 使用方法重写

在主程序中，我们可以创建 `Dog` 和 `Cat` 的实例，并调用它们的 `MakeSound` 方法：

```csharp
class Program
{
    static void Main(string[] args)
    {
        Animal myDog = new Dog("Buddy");
        Animal myCat = new Cat("Whiskers");

        myDog.MakeSound(); // 输出 "Buddy barks."
        myCat.MakeSound(); // 输出 "Whiskers meows."
    }
}
```

### 多态性

方法重写的一个主要用途是实现多态性（Polymorphism）。这允许你在运行时决定调用哪个方法实现，具体取决于对象的实际类型。

```csharp
class Program
{
    static void Main(string[] args)
    {
        Animal[] animals = new Animal[]
        {
            new Dog("Buddy"),
            new Cat("Whiskers")
        };

        foreach (Animal animal in animals)
        {
            animal.MakeSound();
        }
    }
}
```

在这个示例中，尽管我们在编译时不知道具体的动物类型，但在运行时会调用适当的 `MakeSound` 方法。

### 基类调用

有时，重写方法还需要调用基类的方法。你可以使用 `base` 关键字来实现这一点：

```csharp
public class Dog : Animal
{
    public Dog(string name) : base(name)
    {
    }

    public override void MakeSound()
    {
        base.MakeSound(); // 调用基类的方法
        Console.WriteLine($"{Name} barks loudly.");
    }
}
```

```csharp
class Program
{
    static void Main(string[] args)
    {
        Animal myDog = new Dog("Buddy");
        myDog.MakeSound(); // 输出 "Buddy makes a sound." 和 "Buddy barks loudly."
    }
}
```

### 方法重写的完整示例

以下是一个完整示例，包括基类、派生类、重写方法和主程序：

```csharp
using System;

public class Animal
{
    public string Name { get; set; }

    public Animal(string name)
    {
        Name = name;
    }

    public virtual void MakeSound()
    {
        Console.WriteLine($"{Name} makes a sound.");
    }
}

public class Dog : Animal
{
    public Dog(string name) : base(name)
    {
    }

    public override void MakeSound()
    {
        base.MakeSound(); // 调用基类的方法
        Console.WriteLine($"{Name} barks.");
    }
}

public class Cat : Animal
{
    public Cat(string name) : base(name)
    {
    }

    public override void MakeSound()
    {
        Console.WriteLine($"{Name} meows.");
    }
}

class Program
{
    static void Main(string[] args)
    {
        Animal myDog = new Dog("Buddy");
        Animal myCat = new Cat("Whiskers");

        myDog.MakeSound(); // 输出 "Buddy makes a sound." 和 "Buddy barks."
        myCat.MakeSound(); // 输出 "Whiskers meows."

        Animal[] animals = new Animal[]
        {
            new Dog("Rex"),
            new Cat("Kitty")
        };

        foreach (Animal animal in animals)
        {
            animal.MakeSound();
        }
    }
}
```

### 总结

- `virtual`：用于基类方法，表示该方法可以被重写。
- `override`：用于子类方法，表示该方法重写基类的虚方法。
- `base`：用于在重写的方法中调用基类的方法。

通过方法重写和多态性，你可以创建更灵活和可扩展的代码结构，从而更好地实现代码复用和维护。