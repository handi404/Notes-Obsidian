全面整理 Java 中的建造者模式（Builder Pattern），包括其最新实现方式、适用场景、优缺点，并通过现代代码示例（如支持 Java 17+ 的写法）通俗讲解其工作原理。  

# Java 中的建造者模式（Builder Pattern）详解

## 定义与设计目的

建造者模式是一种**创建型设计模式**，其核心思想是将复杂对象的**构建过程与表示方式分离**。通过建造者模式，客户端可以一步步地创建复杂对象，而无需关心内部的创建细节。这种模式允许同样的构建过程可以创建不同表示的对象，从而极大提高了构建过程的灵活性。例如，在《Effective Java》中推荐的Builder模式，即通过静态内部类封装构造过程，最终调用 `build()` 方法得到不可变对象。

## 它解决的问题

- **构造函数参数过多**：当一个类的构造函数参数过多时（尤其是可选参数很多），使用传统构造函数或静态工厂会导致代码臃肿且难以维护。例如采用“_伸缩构造函数_”模式（Telescoping Constructor）时，需要编写多个构造函数来应对不同组合的参数。这种方式一旦参数较多就非常繁琐。
    
- **可选参数处理**：许多参数可能是可选的，但使用工厂或构造函数时，不得不给每个可选参数传入 `null` 或默认值。Builder 模式允许以链式调用的形式设置可选参数，只有需要的参数才调用对应方法，从而避免了传入大量 `null` 的不便。
    
- **对象状态不一致**：如果使用普通setter，可能会在构造过程中出现中间状态，导致对象不一致。Builder 模式通过在最终调用 `build()` 时才创建对象，可以在内部校验参数和保证对象的一致性。
    
- **代码可读性差**：大量参数的构造函数调用容易出错，阅读时很难分辨各参数含义。Builder 以**流畅接口**（fluent API）的形式设置各个属性，使代码更具可读性 [geeksforgeeks.org](https://www.geeksforgeeks.org/difference-between-builder-design-pattern-and-factory-design-pattern/#:~:text=%2A%20Step,more%20readable%20and%20maintainable%20by)。例如：
    
    ```java
    NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
        .calories(100)
        .fat(0)
        .build();
    ```
    
    这段代码相比于 `new NutritionFacts(240, 8, 100, 0, 35, 27)` 等冗长构造函数更易理解。
    

## 标准实现方式（Java 17+）

在Java中，建造者模式的经典实现通常采用**静态内部类** + **链式调用** 的形式：

- 定义一个外部类，其字段大多为 `private final`，并有一个私有构造函数，该构造函数接收一个Builder实例，将Builder中收集的属性值赋给外部类的字段。
    
- 定义一个静态内部类 `Builder`，其成员变量与外部类相同。Builder 对必需参数有一个带参构造函数，所有可选参数则通过链式方法（setter样式方法）赋值，方法返回 `this` 以支持链式调用。
    
- Builder 内部提供一个 `build()` 方法，该方法调用外部类的私有构造函数并返回构建好的对象。
    

例如，经典的 `NutritionFacts` 示例：

```java
public class NutritionFacts {
    private final int servingSize;  // 必需
    private final int servings;     // 必需
    private final int calories;     // 可选
    private final int fat;          // 可选
    private final int sodium;       // 可选

    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings    = builder.servings;
        this.calories    = builder.calories;
        this.fat         = builder.fat;
        this.sodium      = builder.sodium;
    }

    public static class Builder {
        private final int servingSize;
        private final int servings;
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int val) { this.calories = val; return this; }
        public Builder fat(int val)      { this.fat = val;      return this; }
        public Builder sodium(int val)   { this.sodium = val;   return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}
```

上述实现方式可在 Java 17+ 环境中正常使用。最新 Java 语言特性（如 **记录类（record）**、**链式调用**、**var局部变量类型推断** 等）并不会改变这一模式的本质，但可以简化类的定义。

### 现代写法示例

- **Lombok 注解**：使用 [Project Lombok](https://projectlombok.org/features/Builder) 的 `@Builder` 注解可以自动生成上述 Builder 模式的模板代码。例如：
    
    ```java
    @lombok.Builder
    public class Person {
        private final String name;
        private final int age;
    }
    // 使用示例：
    Person p = Person.builder()
                     .name("Alice")
                     .age(30)
                     .build();
    ```
    
    Lombok 会自动生成一个内部静态 `PersonBuilder` 类以及链式方法，因此使用起来非常简洁。
    
- **Record 类**（Java 16+）：`record` 关键字简化了不可变数据类的定义，但原生不支持 Builder 模式。可以手动为 `record` 编写 Builder，或使用注解工具（如 [RecordBuilder](https://github.com/arnaudroger/record-builder)）自动生成。例如：
    
    ```java
    @recordbuilder
    public record NameAndAge(String name, int age) {}
    // Lombok或RecordBuilder生成NameAndAgeBuilder，实现链式调用
    NameAndAge na = NameAndAgeBuilder.builder()
                                     .name("Bob")
                                     .age(25)
                                     .build();
    ```
    
    如极道Jdon博文所述，引入 `@RecordBuilder` 注解可以为 `record` 自动生成对应的 Builder 类。
    
- **Java 21新特性**：目前 Java 21 的主要新特性（如记录模式匹配、虚拟线程等）并未直接影响建造者模式本身。可以说，Java 21 引入的**记录模式（Record Pattern）**等使得处理不可变数据更方便，但构建模式依然需要手动实现或借助注解工具。如上所述，**record**（Java 14+）已用于简化数据载体，但仍需额外库（RecordBuilder）来支持 Builder。
    

## 示例代码

- **传统写法**：见上文 `NutritionFacts` 示例。典型用法是使用嵌套静态 `Builder` 类，利用链式调用设置属性，最后通过 `build()` 生成实例。
    
- **Lombok 写法**：使用 `@Builder` 注解极大简化代码。示例：
    
    ```java
    @lombok.Builder
    public class Computer {
        private final String HDD;
        private final String RAM;
        private boolean graphicsCardEnabled;
        private boolean bluetoothEnabled;
    }
    // 使用示例：
    Computer comp = Computer.builder()
                            .HDD("1TB")
                            .RAM("16GB")
                            .graphicsCardEnabled(true)
                            .build();
    ```
    
    Lombok 会生成类似于上述传统写法的 Builder 代码。
    
- **Record + Builder**：假设我们有一个 `record`：
    
    ```java
    public record Point(int x, int y) {
        public static class Builder {
            private int x, y;
            public Builder x(int x) { this.x = x; return this; }
            public Builder y(int y) { this.y = y; return this; }
            public Point build() { return new Point(x, y); }
        }
        public static Builder builder() { return new Builder(); }
    }
    // 使用示例：
    Point p = Point.builder().x(5).y(10).build();
    ```
    
    如上所示，我们手动为 `record` 添加了一个静态内部 `Builder` 类和 `builder()` 方法，实现与普通类相同的 Builder 模式。
    

## 与工厂模式的区别

- **目的不同**：工厂模式（Factory Pattern）关注“一步创建对象”，通常通过工厂方法或静态工厂返回完整对象，而建造者模式关注“逐步构建复杂对象”的过程。
    
- **灵活性**：Builder 允许在构建过程中灵活选择调用哪些步骤，适用于复杂对象的多变组合；工厂模式一般只提供不同类型产品的一次性创建。正如菜鸟教程所述，“建造者模式更加关注于零件装配的顺序”。
    
- **示例**：如果对象仅是简单实例化，使用工厂（Factory 或简单构造函数）即可；若对象具有很多可选配置，则推荐使用 Builder 模式。
    

## 使用场景

- **复杂对象**：对象内部结构复杂、属性众多时，如生成 HTML 文档、构造复杂配置对象等，可以使用建造者模式来组织构建步骤。
    
- **可选参数多**：当一个对象有多个可选参数时，Builder 提供清晰的链式接口，避免过长的构造函数和大量 `null` 传参。
    
- **不可变对象**：需要创建不可变类并在创建前进行多个参数验证时，Builder 模式可以先在 Builder 中收集值，最后通过一次构造产生不可变实例。
    
- **逐步构建**：如需要在不同场景下按顺序调用不同构造步骤，可使用 Builder 模式将这些步骤封装。
    
- **Spring 等框架**：许多主流框架中也使用了 Builder 模式。例如 Spring Boot 的 `RestTemplateBuilder` 用于配置并创建 `RestTemplate` [geeksforgeeks.org](https://www.geeksforgeeks.org/spring-boot-resttemplatebuilder-with-example/#:~:text=RestTemplateBuilder%20is%20a%20Builder%20that,Let%E2%80%99s%20understand%20the)，Spring WebFlux 的 `WebClient.builder()` 用于链式设置参数构建 `WebClient`，URI 构建器 `UriComponentsBuilder` 也是典型的 Builder 用例。
    

## 不适用场景

- **简单对象**：如果待构建的对象只包含少量属性或逻辑简单，使用建造者模式会导致类和对象数量增加，反而显得冗余。正如菜鸟教程指出的，“如果产品的属性较少，建造者模式可能会导致代码冗余”。
    
- **性能敏感**：Builder 会产生额外的对象（Builder 实例），在极端性能敏感的场合可能略有开销。
    
- **无需多步构造**：如果对象只需一个构造方法即可初始化完成，则不需要使用 Builder。使用 Builder 对于简单职责的类来说是一种过度设计，会增加维护成本（有评论指出，若类仅仅是简单的功能，Builder 模式可能“过度设计”）。[stackoverflow.com](https://stackoverflow.com/questions/2829106/disadvantages-of-builder-design-pattern#:~:text=It%20depends,screen%20then%20its%20probably%20overkill)
    

## 优缺点总结

- **优点**：
    
    - _可读性高_：链式调用清晰地展示了各参数含义，构建过程一目了然。
        
    - _灵活性强_：允许分步构建对象，调用者只需关心所需步骤，不需要按固定顺序调用所有构造函数。
        
    - _与表示分离_：可以在不同的 `Builder` 子类中以不同方式构建同一产品，使构建过程可复用。
        
    - _易于扩展_：当需要增加新的可选参数时，只需在 Builder 中添加新方法，而不必修改现有构造函数签名，兼容性好。
        
    - _支持不可变对象_：Builder 可以在内部保持可变状态，最终构建出包含所有值的不可变对象。
        
- **缺点**：
    
    - _代码量增加_：需要额外定义 Builder 类、链式方法和构造函数，类数量和文件大小增加。
        
    - _初期工作量_：实现 Builder 模式需要编写更多模板代码（尽管可用 Lombok 等工具减轻）。
        
    - _过度设计风险_：对于简单对象使用 Builder，会显得臃肿复杂，降低开发效率 [stackoverflow.com](https://stackoverflow.com/questions/2829106/disadvantages-of-builder-design-pattern#:~:text=It%20depends,screen%20then%20its%20probably%20overkill)。
        
    - _学习成本_：理解和正确使用 Builder 模式需要一定的设计模式知识，对于初学者来说可能略显复杂。
        

## 案例：主流框架中的应用

- **Spring 框架**：Spring Boot 提供了多种 Builder。例如 `RestTemplateBuilder` 可以链式配置后 `.build()` 得到 `RestTemplate` [geeksforgeeks.org](https://www.geeksforgeeks.org/spring-boot-resttemplatebuilder-with-example/#:~:text=RestTemplateBuilder%20is%20a%20Builder%20that,Let%E2%80%99s%20understand%20the) 实例；Spring WebFlux 的 `WebClient` 也通过 `WebClient.builder()` 链式设置参数并 `.build()` 生成客户端 [docs.spring.io](https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html#:~:text=The%20simplest%20way%20to%20create,of%20the%20static%20factory%20methods)；`UriComponentsBuilder` 则用于灵活地构造 URI。
    
- **其他库**：许多第三方库也采用 Builder 模式，如 Apache HttpClient 的 `HttpRequest.Builder`、OkHttp 的 `Request.Builder` 等，都通过链式方法累积配置并最终 `.build()` 得到请求对象。
    

通过以上分析和示例，可以看出建造者模式在 Java 中能够有效地解决构造函数参数过多、可读性差等问题，提高代码可维护性。正确使用时，它能使复杂对象的创建过程清晰、可控；但在简单场景下也要避免无谓的复杂性 [geeksforgeeks.org](https://www.geeksforgeeks.org/difference-between-builder-design-pattern-and-factory-design-pattern/#:~:text=%2A%20Step,more%20readable%20and%20maintainable%20by)。

**参考资料：** 相关设计模式资料和示例 [geeksforgeeks.org](https://www.geeksforgeeks.org/difference-between-builder-design-pattern-and-factory-design-pattern/#:~:text=Design%20patterns%20provide%20proven%20solutions,In%20this)[geeksforgeeks.org](https://www.geeksforgeeks.org/spring-boot-resttemplatebuilder-with-example/#:~:text=RestTemplateBuilder%20is%20a%20Builder%20that,Let%E2%80%99s%20understand%20the)[docs.spring.io](https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html#:~:text=The%20simplest%20way%20to%20create,of%20the%20static%20factory%20methods) 等。