## Lambda 表达式：简化 Java 代码的利器

### 什么是 Lambda 表达式？

Lambda 表达式是 Java 8 引入的一个新特性，它提供了一种简洁、灵活的方式来表示接口的实例。本质上，Lambda 表达式就是一个匿名函数，可以理解为一段可以传递的代码块。

### 为什么使用 Lambda 表达式？

- **代码更简洁：** Lambda 表达式可以减少很多样板代码，让代码更易读。
- **提高代码可读性：** Lambda 表达式可以将函数式编程的思想引入到 Java 中，使代码更具表达力。
- **支持函数式编程风格：** Lambda 表达式是函数式编程的重要概念，可以帮助我们写出更优雅的代码。

### Lambda 表达式的语法

```Java
(参数列表) -> {
    // 表达式体
}
```

- **参数列表：** 可以是零个或多个参数，参数类型可以显式声明或由编译器推断。
- **箭头：** 分隔参数列表和表达式体。
- **表达式体：** 可以是一个表达式，也可以是一个代码块。如果表达式体只有一行，可以省略花括号和 return 语句。

### Lambda 表达式的示例

```Java
// 传统写法
List<String> names = Arrays.asList("apple", "banana", "orange");
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// 使用 Lambda 表达式
List<String> names = Arrays.asList("apple", "banana", "orange");
Collections.sort(names, (a, b) -> a.compareTo(b));
```

### Lambda 表达式与函数式接口

- **函数式接口：** 只有一个抽象方法的接口。
- **Lambda 表达式与函数式接口的关系：** Lambda 表达式可以用来创建函数式接口的实例。

```Java
@FunctionalInterface
interface MyInterface {
    int operation(int a, int b);
}

// 使用 Lambda 表达式创建 MyInterface 的实例
MyInterface myInterface = (a, b) -> a + b;
```

### Lambda 表达式的常见应用场景

- **集合操作：** `filter`、`map`、`reduce` 等操作
- **线程：** `Runnable`、`Callable` 接口
- **事件处理：** ActionListener、MouseListener 等
- **流式编程：** Java 8 的 Stream API

### 注意事项

- **Lambda 表达式只能用于实现函数式接口。**
- **Lambda 表达式可以访问外部变量，但不能修改外部变量的值（除非是 final 或 effectively final）。**

### 总结

Lambda 表达式是 Java 8 中一个非常强大的特性，它可以极大地简化我们的代码，提高代码的可读性。通过合理地使用 Lambda 表达式，我们可以写出更加简洁、优雅的 Java 代码。

### 拓展学习

- **Java 8 Stream API：** 与 Lambda 表达式结合，可以对集合进行高效的处理。
- **方法引用：** 方法引用是 Lambda 表达式的一种简写形式。
- **默认方法：** 默认方法可以为接口添加新的方法，而不会破坏已有的实现。