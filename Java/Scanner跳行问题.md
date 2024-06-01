
由于 `Scanner` 类中的一个常见问题，通常被称为“Scanner跳行问题”。这个问题是由于 `nextInt()` 方法只会读取一个整数，但不会读取行结束符，而 `nextLine()` 方法会读取行结束符，并返回剩余的空行。所以，当你在使用 `nextInt()` 之后紧接着使用 `nextLine()` 时，`nextLine()` 会读取到 `nextInt()` 方法后的换行符，然后返回一个空字符串，导致感觉上好像被跳过了。

解决这个问题的方法之一是在 `nextLine()` 之前先调用一次 `next()` 方法，将上次 `nextInt()` 后的换行符读取掉。例如：

```java
Scanner scanner = new Scanner(System.in);

System.out.print("请输入一个整数: ");
int number = scanner.nextInt();

// 清除缓冲区中的换行符
scanner.nextLine();

System.out.print("请输入一行文字: ");
String text = scanner.nextLine();

```

在这个例子中，调用了 `nextInt()` 读取了一个整数，然后使用 `nextLine()` 之前调用了一个 `next()` 方法来读取掉剩余的换行符，这样就可以避免 `nextLine()` 返回空字符串的情况。

另一种解决方法是将所有的输入都使用 `nextLine()` 方法读取，然后根据需要进行类型转换。