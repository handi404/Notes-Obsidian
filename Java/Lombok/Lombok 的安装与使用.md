#### 文章目录

___

## 一、什么是 Lombok

### 1.1 Lombok 的概念

Lombok（“Project Lombok”）是一款针对 Java 开发的实用工具库。它通过使用`注解`自动生成 [Java 类](https://so.csdn.net/so/search?q=Java%20%E7%B1%BB&spm=1001.2101.3001.7020)的通用代码，从而简化了 Java 代码的编写过程。Lombok 的**目标是减少样板代码（boilerplate code），提高代码的简洁性和可读性**。

### 1.2 为什么使用 Lombok

在传统的 Java 开发中，常常需要为 Java 类编写大量的 `getter` 和 `setter` 方法、`equals` 和 `hashCode`方法、`toString`方法等通用的代码，这些代码在大多数情况下都是重复且无趣的。而使用 Lombok，可以通过简单的注解在Java类上，自动生成这些通用代码，从而避免了重复劳动。

### 1.3 Lombok 的相关注解

**下面是常用的 Lombok 注解：**

| 注解                                                 | 说明                                                          |
| -------------------------------------------------- | ----------------------------------------------------------- |
| `@Data`                                            | 自动生成 `getter`、`setter`、`equals`、`hashCode` 和 `toString` 方法。 |
| `@Getter`                                          | 自动生成 `getter` 方法。                                           |
| `@Setter`                                          | 自动生成 `setter` 方法。                                           |
| `@ToString`                                        | 自动生成 `toString` 方法。                                         |
| `@EqualsAndHashCode`                               | 自动生成 `equals` 和 `hashCode` 方法。                              |
| `@NoArgsConstructor`                               | 自动生成无参构造函数。                                                 |
| `@RequiredArgsConstructor`                         | 自动生成带有 `@NonNull` 注解的成员变量的构造函数。                             |
| `@AllArgsConstructor`                              | 自动生成包含所有成员变量的构造函数。                                          |
| `@Builder`                                         | 自动生成建造者模式的构建方法。                                             |
| `@Slf4j`                                           | 自动生成 `log` 日志对象。                                            |
| `@Value`                                           | 类似于 `@Data`，但生成的类是不可变的（immutable）。                          |
| `@Cleanup`                                         | 自动生成资源（如流）的关闭代码。                                            |
| `@SneakyThrows`                                    | 自动生成异常抛出代码，用于把受检异常转换为非受检异常。                                 |
| `@NonNull`                                         | 为成员变量标记非空约束，自动生成空值检查代码。                                     |
| `@Getter(AccessLevel.NONE)`                        | 取消生成 `getter` 方法。                                           |
| `@Setter(AccessLevel.NONE)`                        | 取消生成 `setter` 方法。                                           |
| `@NoArgsConstructor(access = AccessLevel.PRIVATE)` | 生成私有的无参构造函数。                                                |

以上是常用的一些 Lombok 注解及其说明，通过使用这些注解，可以简化 Java 类的编写，减少样板代码，提高代码的简洁性和可读性。需要根据实际情况选择合适的注解，以便在项目中获得更好的开发体验。

## 二、Lombok 的安装

### 2.1 引入依赖

可以通过 Lombok的官网：[https://projectlombok.org/](https://projectlombok.org/) 获取依赖：

![](https://i-blog.csdnimg.cn/blog_migrate/0ce8e090962e9bea018add4f1d07bd5d.png)

如果是 `Maven` 项目，选择`Install`中的`Build tools`，点击`maven` 进行复制，然后粘贴到 `pom.xml` 中的 `<dependencies>` 里面即可；当然，在创建 Spring Boot 项目时，也可以直接在创建的时候选择 [Lombok 依赖](https://so.csdn.net/so/search?q=Lombok%20%E4%BE%9D%E8%B5%96&spm=1001.2101.3001.7020)。

### 2.2 安装插件

在 IDEA 中，如果想要在使用时提示相应的注解，还需要安装 `Lombok Builder Helper` 插件：

![](https://i-blog.csdnimg.cn/blog_migrate/7c5665e54f35884f76922c4cf923f8ae.png)

## 三、Lombok 的使用案例

**创建了一个使用 Lombok 注解的User类：**

```java
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Integer age;
}
```

**通过 `TestController`类获取 `User` 对象，并对其执行操作：**

```java
@Controller
@ResponseBody
public class TestController {
    @Autowired
    private User user;

    @RequestMapping("/test")
    public void test(){
        System.out.println(user);
        System.out.println("===================");
        user.setId(123);
        user.setName("张三");
        user.setAge(18);
        System.out.println("===================");
        System.out.println(user);
    }
}
```

执行结果：  
![](https://i-blog.csdnimg.cn/blog_migrate/a4064f574f93a3ae95a3acf18d6bf5b4.png)

## 四、Lombok 的原理

Lombok 的原理主要依赖于 Java 的注解处理器（Annotation Processor）。在**编译 Java 源代码的过程中，注解处理器会读取源代码中的Lombok 注解，并根据注解的定义自动生成相应的 Java 代码，然后将这些生成的代码插入到编译结果中，最终生成编译后的 class 文件**。

**具体来说，Lombok的原理如下：**

1. **注解的识别**：在编译 Java 源代码的过程中，注解处理器会扫描源代码中的 Lombok 注解，如`@Getter`、`@Setter`、`@NoArgsConstructor`等。

2. **解析注解**：一旦发现 Lombok 注解，注解处理器会解析该注解的定义，并确定生成代码的内容，例如生成 `getter` 和 `setter` 方法，构造函数等。

3. **生成代码**：根据注解的定义，注解处理器会生成对应的 Java 代码片段，比如生成 `getter` 方法的代码，`setter` 方法的代码，构造函数的代码等。

4. **插入生成的代码**：注解处理器将生成的代码插入到源代码中相应的位置。

5. **编译**：在生成了包含 Lombok 生成的代码的新的 Java 源代码之后，编译器会继续将新的源代码编译成字节码文件（.class文件）。

6. **最终结果**：最终生成的class文件包含了Lombok生成的代码，这样在运行时，就能够使用Lombok自动生成的方法和构造函数等功能。

**例如，在编译 `User` 类前的代码如下：**

![](https://i-blog.csdnimg.cn/blog_migrate/99f8fa3dbd14596e3c7f4bed45e0ab55.png)  
**编译后查看 `target` 目录下 `User` 对应的 `.class` 文件：**

![](https://i-blog.csdnimg.cn/blog_migrate/f283e7719f56af41555cdfa6435bf2c1.png)  
此时可以发现自动生成了各种 `setter` 、`getter` 和构造方法等。并且与 Lombok 相关的注解也消失了。