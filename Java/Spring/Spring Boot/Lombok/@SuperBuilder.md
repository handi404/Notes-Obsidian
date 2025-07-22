`@SuperBuilder` 是一个非常重要且能体现你对 Lombok 理解深度的注解。它解决了标准 `@Builder` 注解的一个核心痛点：**继承**。

---

### 一、为什么需要 `@SuperBuilder`？（解决了什么问题）

我们先来看一个没有 `@SuperBuilder` 的反面教材。

假设你有一个 `Person` 基类和一个 `Student` 子类：

```java
// 父类
@Getter
@Builder // 普通的 @Builder
@AllArgsConstructor
class Person {
    private String name;
    private int age;
}

// 子类
@Getter
@Builder // 普通的 @Builder
@AllArgsConstructor
class Student extends Person {
    private String school;
}
```

现在，你想创建一个 `Student` 对象。如果你使用 `Student.builder()`，你会发现一个严重的问题：

```java
Student.builder()
       .school("Hogwarts")
       // .name("Harry")  <-- 编译错误！没有 .name() 方法
       // .age(11)      <-- 编译错误！没有 .age() 方法
       .build();
```

**问题根源**：`Student` 上的 `@Builder` 注解只会为 `Student` 类自身的字段（`school`）生成构建方法。它完全不知道，也不关心父类 `Person` 中的字段（`name`, `age`）。`Person` 的 Builder 和 `Student` 的 Builder 是两个完全独立、互不相干的东西。

这就是标准 `@Builder` 在继承场景下的“天花板”。

### 二、`@SuperBuilder`：完美的解决方案

`@SuperBuilder` 就是为了优雅地解决上述问题而生的。

**核心思想**：它创建的 Builder 类本身也支持继承，从而让子类的 Builder 能够“看到”并设置父类的字段。

**正确用法**：你需要在**父类和所有相关的子类上都**标注 `@SuperBuilder`。

```java
// 父类
@Getter
@SuperBuilder // <-- 注意这里！
@AllArgsConstructor
@NoArgsConstructor // 建议加上，尤其是在JPA等场景
class Person {
    private String name;
    private int age;
}

// 子类
@Getter
@SuperBuilder // <-- 子类也必须加上！
@AllArgsConstructor
@NoArgsConstructor
class Student extends Person {
    private String school;
}
```

现在，我们再来构建 `Student` 对象：

```java
Student student = Student.builder()
                         .name("Harry")  // <-- 成功！可以设置父类字段
                         .age(11)      // <-- 成功！可以设置父类字段
                         .school("Hogwarts") // <-- 成功！可以设置子类字段
                         .build();

System.out.println(student.getName()); // 输出: Harry
System.out.println(student.getSchool()); // 输出: Hogwarts
```

问题完美解决！子类的 Builder 现在是一个“超级 Builder”，它包含了从顶层父类到底层子类所有链条上的字段。

### 三、`@SuperBuilder` 的高级应用与最佳实践

#### 1. `toBuilder = true`：对象的“克隆与修改”

这是 `@SuperBuilder` (以及 `@Builder`) 最强大的功能之一。它会额外生成一个 `toBuilder()` 方法，允许你基于一个已存在的对象实例，创建一个新的 Builder 来进行修改，实现“不可变对象”的优雅修改。

```java
// 在类上开启 toBuilder
@Getter
@SuperBuilder(toBuilder = true)
class Student extends Person { /* ... */ }

// 使用
Student harry = Student.builder().name("Harry").age(11).school("Hogwarts").build();

// 场景：Harry 升了一年级，其他信息不变
Student harryYear2 = harry.toBuilder() // 从现有对象创建 Builder
                          .age(12)      // 只修改需要改变的字段
                          .build();

System.out.println(harryYear2.getName()); // Harry
System.out.println(harryYear2.getAge());  // 12 (已更新)
```
这在处理 DTO (Data Transfer Object) 或不可变实体时极其有用。

#### 2. 与 `@Builder.Default` 配合使用

当你希望某个字段在 Builder 中有默认值时，需要使用 `@Builder.Default`。这对于 `@SuperBuilder` 同样适用。

```java
@Getter
@SuperBuilder
class Task {
    private String description;

    @Builder.Default // <-- 关键注解
    private boolean done = false; // 默认任务是未完成的

    @Builder.Default
    private final Instant creationTs = Instant.now();
}

// 使用
Task task = Task.builder().description("Buy milk").build();
System.out.println(task.isDone()); // 输出: false (使用了默认值)
```
**注意**：如果不加 `@Builder.Default`，直接 `private boolean done = false;`，那么通过 Builder 创建的对象 `done` 字段会是 `false` (基本类型的默认值)，而不是你指定的默认值。因为 Builder 会为所有字段生成设置方法，如果你不调用 `done()`，它就会被赋予默认的 `false`。`@Builder.Default` 确保了即使你不显式调用 `done()` 方法，你设置的初始值也会被使用。

#### 3. 抽象类与 `@SuperBuilder`

`@SuperBuilder` 非常适合用在抽象类上，这是它最典型的应用场景之一。

```java
@Getter
@SuperBuilder
public abstract class Vehicle {
    private int wheels;
    private int speed;
}

@Getter
@SuperBuilder
public class Car extends Vehicle {
    private int doors;
}

// 使用
Car car = Car.builder()
             .wheels(4)  // from Vehicle
             .speed(120) // from Vehicle
             .doors(4)   // from Car
             .build();
```

#### 4. 构造函数的注意事项

*   `@SuperBuilder` 为了实现其继承机制，会生成复杂的构造函数。如果你同时使用了 `@Data`，可能会导致冲突，因为 `@Data` 会尝试生成它自己的 `@RequiredArgsConstructor`。
*   **最佳实践**：当使用 `@SuperBuilder` 时，**不要使用 `@Data` 或 `@Value`**。而是手动添加你需要的注解，如 `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`。同时，显式地添加 `@NoArgsConstructor` 和 `@AllArgsConstructor` 通常是个好习惯，可以避免一些潜在的序列化或框架集成问题。

### 四、总结与对比

| 特性 | `@Builder` | `@SuperBuilder` |
| :--- | :--- | :--- |
| **核心功能** | 为单个类提供链式构建器 | **为继承体系提供链式构建器** |
| **使用场景** | 简单的、没有继承关系的类（如 DTOs, Entities） | **任何涉及继承的类**（抽象基类、多层实现等） |
| **使用方法** | 在单个类上标注 `@Builder` | 在**父类和所有子类**上都标注 `@SuperBuilder` |
| **继承支持** | ❌ **不支持**。子类 Builder 无法设置父类字段 | ✅ **完美支持**。子类 Builder 可设置完整继承链上的所有字段 |
| **`toBuilder=true`** | 支持 | 支持 |
| **`@Builder.Default`** | 支持 | 支持 |

**一句话总结**：如果你正在编写一个独立的、不会被继承的类，并且想用构建器模式，用 `@Builder`。一旦你的类涉及**任何形式的继承**（无论是作为父类还是子类），并且想在整个继承链上使用构建器，**请毫不犹豫地使用 `@SuperBuilder`**，并确保继承链上的所有相关类都加上了它。