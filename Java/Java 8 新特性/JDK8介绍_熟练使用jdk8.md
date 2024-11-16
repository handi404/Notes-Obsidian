 

> [Java 8](https://so.csdn.net/so/search?q=Java%208&spm=1001.2101.3001.7020) 引入了许多令人兴奋的新特性，其中包括 Lambda 表达式、Stream API、函数式接口、方法引用、默认方法等。下面我将为您详细介绍这些新特性，并提供相应的代码案例。

**目录**

[一、Lambda 表达式](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t0)

[示例代码](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t1)

[二、Stream API](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t2)

[示例代码](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t3)

[三、函数式接口](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t4)

[示例代码](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t5)

[四、方法引用](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t6)

[示例代码](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t7)

[五、默认方法](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t8)

[示例代码](https://blog.csdn.net/qq_23126581/article/details/137818974?spm=1001.2014.3001.5506#t9)

___

## 一、Lambda 表达式

Lambda 表达式是 Java 8 中引入的一种新的语法特性，它可以使代码更加简洁、清晰，提高了代码的可读性和可维护性。

### 示例代码

```java
// 使用匿名内部类实现 Runnable 接口
new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello from anonymous class!");
    }
}).start();
 
// 使用 Lambda 表达式实现 Runnable 接口
new Thread(() -> System.out.println("Hello from Lambda expression!")).start();
```

## 二、Stream API

Stream API 提供了一种更加便捷和高效的处理集合数据的方式，它支持流式操作，包括过滤、映射、归约等。

### 示例代码

```java
import java.util.Arrays;
import java.util.List;
 
public class StreamExample {
    public static void main(String[] args) {
        List<String> languages = Arrays.asList("Java", "Python", "C++", "JavaScript", "Ruby");
 
        // 使用 Stream 进行过滤和打印
        languages.stream()
                 .filter(s -> s.startsWith("J"))
                 .forEach(System.out::println);
 
        // 使用 Stream 进行映射和打印
        languages.stream()
                 .map(String::toUpperCase)
                 .forEach(System.out::println);
 
        // 使用 Stream 进行归约操作
        int sum = languages.stream()
                          .mapToInt(String::length)
                          .sum();
        System.out.println("Total characters length: " + sum);
    }
}
```

## 三、函数式接口

函数式接口是指只包含一个抽象方法的接口，它们可以被 Lambda 表达式所代替。Java 8 提供了 `@FunctionalInterface` 注解来标记函数式接口。

### 示例代码

```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}
 
public class LambdaExample {
    public static void main(String[] args) {
        MathOperation addition = (a, b) -> a + b;
        System.out.println("10 + 5 = " + addition.operate(10, 5));
 
        MathOperation subtraction = (a, b) -> a - b;
        System.out.println("10 - 5 = " + subtraction.operate(10, 5));
    }
}
```

## 四、方法引用

方法引用是一种更简洁的 Lambda 表达式，它可以直接引用现有方法。

### 示例代码

```java
import java.util.Arrays;
import java.util.List;
 
public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("apple", "banana", "cherry");
 
        // 使用 Lambda 表达式打印每个元素
        fruits.forEach(fruit -> System.out.println(fruit));
 
        // 使用方法引用打印每个元素
        fruits.forEach(System.out::println);
    }
}
```

## 五、默认方法

在接口中可以定义默认方法，这样在接口的实现类中就不需要重写这些方法了。

### 示例代码

```java
interface Vehicle {

default void drive() {

System.out.println("Driving the vehicle");

}

}

class Car implements Vehicle {

}

public class DefaultMethodExample {

public static void main(String[] args) {

Car car = new Car();

car.drive();

}

}
```

以上是 Java 8 及以上版本的一些新特性，包括 Lambda 表达式、Stream API、函数式接口、方法引用、默认方法等。这些特性使得 Java 编程更加灵活和便捷，提高了代码的可读性和可维护性，同时也带来了更好的性能和效率。