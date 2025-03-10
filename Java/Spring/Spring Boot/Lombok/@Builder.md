Lombok 中的 **@Builder** 注解（注意：并非 @Build）用于简化对象的构造过程，通过自动生成 Builder 模式代码，使得创建对象变得更直观和可读，尤其适用于构造参数较多或者部分参数为可选的场景。

---

### 为什么使用 @Builder？

- **提高可读性**  
    当类中包含多个参数时，直接使用构造方法可能会让代码难以理解，容易发生参数顺序混乱的问题。使用 Builder 模式，可以通过链式调用明确指定每个参数的含义。
    
- **减少代码冗余**  
    Lombok 自动生成 Builder 类和相关方法，无需手动编写大量模板代码，减少了出错的可能性，并使代码更简洁。
    
- **便于扩展和维护**  
    如果未来需要增加或者修改属性，只需更新类定义，Builder 模式能更容易地适应这些变化，而不需要重构所有使用构造方法的代码。
    

---

### @Builder 的作用

- **自动生成 Builder 类**  
    当你在一个类或者构造方法上使用 @Builder 注解时，Lombok 会生成一个嵌套的静态 Builder 类。这个 Builder 类包含与目标类属性对应的设置方法，并最终提供一个 `build()` 方法来构造目标对象。
    
- **提供链式调用 API**  
    生成的 Builder 类允许你通过链式调用逐个设置属性值，从而使得代码更加直观、清晰，避免了传统构造方法中参数顺序容易出错的问题。
    
- **适用于不可变对象**  
    对于需要创建不可变对象的类，通过 @Builder 注解，可以在构造对象时只通过 Builder 完成所有属性的赋值，然后生成一个不可变对象。
    

---

### 如何使用 @Builder

1. **在类上使用 @Builder**  
    直接在类上添加 @Builder 注解，Lombok 会自动为该类生成 Builder 代码。
    
    ```java
    import lombok.Builder;
    
    @Builder
    public class User {
        private String name;
        private int age;
        private String email;
    }
    ```
    
2. **调用生成的 Builder 方法**  
    Lombok 会为 User 类生成一个静态的 `builder()` 方法，以及一个内部的 `UserBuilder` 类。使用时可以这样调用：
    
    ```java
    public class Main {
        public static void main(String[] args) {
            User user = User.builder()
                            .name("张三")
                            .age(25)
                            .email("zhangsan@example.com")
                            .build();
            System.out.println(user);
        }
    }
    ```
    
3. **在构造方法或静态方法上使用 @Builder**  
    如果你只想为某个构造方法或静态方法生成 Builder，也可以直接在这些方法上使用 @Builder 注解。例如：
    
    ```java
    public class Product {
        private String id;
        private String name;
        private double price;
    
        @Builder
        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }
    ```
    
4. **高级特性：@Singular**  
    如果类中包含集合属性，可以结合 **@Singular** 注解使用，这样 Builder 会提供添加单个元素的方法，同时也能设置整个集合。
    
    ```java
    import lombok.Builder;
    import lombok.Singular;
    import java.util.List;
    
    @Builder
    public class Order {
        private String orderId;
        @Singular
        private List<String> items;
    }
    ```
    
    使用示例：
    
    ```java
    Order order = Order.builder()
                       .orderId("ORD123")
                       .item("Item1")
                       .item("Item2")
                       .build();
    ```
    

---

### 总结

- **目的**：@Builder 主要用于实现 Builder 模式，解决构造函数参数较多时的可读性和维护性问题。
- **作用**：自动生成一个内部 Builder 类和链式调用方法，通过 `build()` 方法构造目标对象。
- **使用场景**：适用于需要灵活、清晰地创建对象，特别是当对象有许多属性（其中部分可能为可选项）时。
- **使用方法**：在类或构造方法上添加 @Builder 注解，然后通过生成的 `builder()` 方法来构造对象。

使用 Lombok 的 @Builder，不仅能让代码更优雅，还能大幅度减少手动编写重复代码的工作量，从而提高开发效率。

# 具体使用

在分层架构中，经常需要在领域对象（例如数据库实体）和数据传输对象（DTO）之间转换，以确保数据在不同层之间传递时既安全又符合业务需求。利用 Lombok 的 @Builder 注解，可以简化这种转换过程，使代码更清晰、易读和易维护。下面详细说明这种转换的目的、优势以及如何使用 @Builder 实现转换。

---

## 1. 转换的目的与意义

- **解耦数据结构**  
    领域对象（Entity）通常反映数据库结构，可能包含敏感或内部信息；而 DTO 只保留需要暴露给客户端或跨层传输的部分数据。转换有助于分离关注点，防止数据库细节泄露到外部。
    
- **提高安全性**  
    通过转换，可以过滤掉不必要甚至敏感的字段，例如密码、内部标识符等，保证数据传输的安全。
    
- **便于数据格式转换**  
    DTO 中的字段可能与实体的字段类型或命名不完全一致。转换过程中可以进行必要的格式处理或数据校验，使得数据更符合业务或接口要求。
    

---

## 2. Lombok @Builder 在转换中的优势

- **代码简洁**  
    Lombok 的 @Builder 自动生成 Builder 类，利用链式调用创建对象，无需手写冗长的构造函数或 setter 调用。
    
- **清晰的字段映射**  
    在转换方法中，通过 Builder 模式可以逐一对应各个字段，代码直观且易于维护。例如：
    
    ```java
    public static UserDTO fromEntity(UserEntity entity) {
        return UserDTO.builder()
                      .id(entity.getId())
                      .username(entity.getUsername())
                      .email(entity.getEmail())
                      .build();
    }
    ```
    
- **支持不可变对象**  
    利用 Builder 模式，可以构造出不可变对象（通过省略 setter），保证数据在传输过程中不被意外修改。
    

---

## 3. 实战示例

假设我们有一个用户实体类 `UserEntity` 和一个对应的 DTO 类 `UserDTO`，可以如下实现对象之间的转换：

### 实体类定义

```java
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    private String password; // 数据库中可能存在敏感信息
}
```

### DTO 类定义

```java
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    
    // 转换方法：将 UserEntity 转换为 UserDTO
    public static UserDTO fromEntity(UserEntity entity) {
        return UserDTO.builder()
                      .id(entity.getId())
                      .username(entity.getUsername())
                      .email(entity.getEmail())
                      .build();
    }
    
    // 如有需要，也可以增加转换为实体的方法，但注意处理敏感字段
    public UserEntity toEntity() {
        return UserEntity.builder()
                         .id(this.id)
                         .username(this.username)
                         .email(this.email)
                         // password 需要单独处理或由业务逻辑赋值
                         .build();
    }
}
```

### 使用示例

在业务逻辑层，调用转换方法即可实现对象间的数据传递：

```java
public class UserService {
    public UserDTO getUserDTOById(Long id) {
        // 假设通过某种方式获得了 UserEntity 对象
        UserEntity entity = userRepository.findById(id);
        
        // 利用转换方法，将实体转换为 DTO
        return UserDTO.fromEntity(entity);
    }
}
```

---

## 4. 其他考虑因素

- **字段映射逻辑**  
    如果 DTO 与实体字段不完全一致（如命名、类型、格式），在转换方法中可以加入转换逻辑，例如日期格式转换、枚举映射等。
    
- **自动化映射工具**  
    除了手写转换方法，项目中也可以考虑使用 MapStruct 这类自动映射工具，它可以根据配置自动生成转换代码。但对于简单转换场景，利用 Lombok 的 @Builder 足以满足需求。
    
- **扩展性**  
    当实体或 DTO 结构发生变化时，只需更新转换方法中的映射逻辑，借助 Builder 模式，代码修改局部且易于理解，降低维护成本。
    

---

## 5. 总结

利用 Lombok 的 @Builder 注解进行对象与 DTO 之间的转换，可以带来以下好处：

- **代码简洁且可读性高**：利用链式调用明确映射关系，避免参数混淆。
- **易于维护与扩展**：转换逻辑集中在静态方法中，当数据结构发生变化时，可迅速做出调整。
- **增强数据安全性**：在转换过程中可以过滤敏感信息，确保只传输必要数据。

通过这种方式，不仅提升了代码质量，同时也增强了应用程序在数据传输层面的灵活性和安全性。