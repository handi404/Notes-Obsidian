Jackson 库中的 `@JsonProperty` 注解。这是在 Spring Boot (乃至任何使用 Jackson 进行 JSON 处理的 Java 应用) 中非常核心且常用的一个注解。

---

### 1. `@JsonProperty` 是什么？

`@JsonProperty` 是 Jackson 库提供的一个注解，它的核心作用是**自定义 Java 对象属性与 JSON 字段之间的映射关系**。

简单来说，当你需要：

*   Java 对象的属性名和 JSON 中的字段名不一致时；
*   控制某个属性是否参与序列化（Java 对象转 JSON）或反序列化（JSON 转 Java 对象）时；
*   指定属性在 JSON 中的顺序（虽然不常用，但可以做到）；
*   标记一个属性在反序列化时是否为必需的；
*   为缺失的属性提供默认值时。

`@JsonProperty` 就能派上用场。它可以标记在**字段**、**getter 方法**或**setter 方法**上。

---

### 2. 为什么需要 `@JsonProperty`？

1.  **命名规范差异**：
    *   Java 通常使用驼峰命名法（camelCase），例如 `firstName`。
    *   JSON API 可能使用下划线命名法（snake_case），例如 `first_name`，或者其他任意自定义名称。
    *   `@JsonProperty("first_name")` 就能轻松桥接 `firstName` 和 `first_name`。

2.  **避免歧义与控制可见性**：
    *   有时 getter/setter 的名称可能与期望的 JSON 字段名有出入。
    *   你可能只想让某个字段参与序列化（对外暴露）而不参与反序列化（不接受外部传入），反之亦然。

3.  **处理特殊字符或 Java 关键字**：
    *   如果 JSON 字段名是 Java 的关键字 (如 `class`) 或包含非法字符 (如 `user-id`)，你不能直接用作 Java 属性名。`@JsonProperty("user-id")` 可以解决这个问题。

4.  **增强代码可读性和明确性**：
    *   即使 Java 属性名和 JSON 字段名一致，显式使用 `@JsonProperty` 也能让映射关系更加清晰。

---

### 3. `@JsonProperty` 如何工作？

Jackson 的核心组件 `ObjectMapper` 在进行序列化和反序列化时，会扫描 Java 对象中的注解。

*   **序列化（Java -> JSON）**：当 `ObjectMapper` 序列化一个对象时，如果遇到一个被 `@JsonProperty` 注解的字段或 getter 方法，它会使用注解中指定的名字作为 JSON 输出的键名。
*   **反序列化（JSON -> Java）**：当 `ObjectMapper` 反序列化 JSON 字符串时，它会查找 JSON 中的键名，并尝试匹配 Java 对象中被 `@JsonProperty` 注解（且注解值与 JSON 键名相同）的字段或 setter 方法，然后将对应的值赋给该属性。

**作用位置优先级**：

*   如果字段、getter 和 setter 都被注解，通常以更具体的方法注解（getter/setter）为准，但具体行为可能受 Jackson 版本和配置影响。一般建议只在一个地方注解以保持清晰。
*   **最佳实践**：通常注解在字段上是最简洁的。如果需要更细致的控制（比如只读或只写），可以注解在 getter 或 setter 上。

---

### 4. 核心属性与常见用法

`@JsonProperty` 注解本身可以接受一些参数来进一步定制行为：

*   **`value` (默认属性)**: 指定 JSON 中的字段名。
    ```java
    public class User {
        @JsonProperty("user_name") // JSON 字段将是 "user_name"
        private String userName;

        @JsonProperty("age_in_years")
        private int age;

        // 构造函数、getter 和 setter
    }
    ```

*   **`access`**: 控制属性的访问权限，有以下几种模式：
    *   `JsonProperty.Access.AUTO` (默认): Jackson 自动判断。
    *   `JsonProperty.Access.READ_ONLY`: 属性仅在序列化时包含（Java -> JSON）。
        ```java
        public class Product {
            @JsonProperty(access = JsonProperty.Access.READ_ONLY)
            private String internalId; // 比如内部ID，只向外展示，不接受外部传入
        }
        ```
    *   `JsonProperty.Access.WRITE_ONLY`: 属性仅在反序列化时接受（JSON -> Java）。
        ```java
        public class Credentials {
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
            private String password; // 比如密码，只接受传入，序列化时不暴露
        }
        ```
    *   `JsonProperty.Access.READ_WRITE`: 属性在序列化和反序列化时都包含。

*   **`required` (boolean, 默认为 `false`)**:
    *   当设置为 `true` 时，表示在**反序列化**过程中，如果 JSON 数据中**缺少**这个字段，Jackson 会抛出异常（通常是 `MismatchedInputException` 或类似的）。
    *   **注意**：此属性主要在与构造函数参数（配合 `@JsonCreator`）一起使用时效果最明显。对于普通字段，其行为可能受到 Jackson 全局配置 `DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES` 的影响。对于非构造函数注入的字段，通常建议使用 Bean Validation API (如 `@NotNull`) 来进行更通用的校验。
    ```java
    import com.fasterxml.jackson.annotation.JsonCreator;
    import com.fasterxml.jackson.annotation.JsonProperty;

    public class Order {
        private final String orderId;
        private final String itemName;

        @JsonCreator // 标记构造函数用于反序列化
        public Order(
                @JsonProperty(value = "order_id", required = true) String orderId,
                @JsonProperty(value = "item_name", required = true) String itemName) {
            this.orderId = orderId;
            this.itemName = itemName;
        }
        // getters
    }
    ```

*   **`defaultValue` (String)**:
    *   为反序列化时**缺失**的属性提供一个**字符串形式的默认值**。Jackson 会尝试将这个字符串转换为属性的实际类型。
    *   这对于基本类型和它们的包装类很有用，但对于复杂对象类型，通常不适用。
    ```java
    public class Config {
        @JsonProperty(defaultValue = "10") // 如果JSON中没有 "timeout_seconds"，则默认为10
        private int timeoutSeconds;

        @JsonProperty(defaultValue = "true")
        private boolean enabled;
    }
    ```
    *   **注意**：`defaultValue` 的行为和 `null` 值的处理有关。如果 JSON 中显式地将字段设为 `null`，`defaultValue` 可能不会生效，这取决于具体的 Jackson 配置（如 `MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES`）。

*   **`index` (int, 默认为 `-1`)**:
    *   用于指定属性在序列化输出 JSON 时的顺序。具有较小 `index` 值的属性会先输出。
    *   如果多个属性有相同的 `index`，或未指定 `index`，则它们的相对顺序可能按字母顺序或声明顺序（取决于 Jackson 配置 `MapperFeature.SORT_PROPERTIES_ALPHABETICALLY`）。
    ```java
    public class Report {
        @JsonProperty(index = 1)
        private String title;

        @JsonProperty(index = 3)
        private String content;

        @JsonProperty(index = 2)
        private String author;
    }
    // 输出 JSON 顺序可能为: title, author, content
    ```

---

### 5. 最新进展与 Spring Boot 3.x

*   **Jackson 依然是 Spring Boot 的默认 JSON 库**：Spring Boot 3.x 默认集成了 Jackson 2.14+ (具体版本随 Spring Boot 版本更新)。`@JsonProperty` 的核心功能和用法保持稳定和向后兼容。
*   **对 Java Records 的良好支持**：Java 14+ 引入的 Records 非常适合用作不可变的 DTOs。Jackson 对 Records 有很好的原生支持。当使用 Records 时，构造函数参数通常会自动映射到 JSON 字段。如果需要自定义名称或行为，`@JsonProperty` 同样可以注解在 Record 组件（本质上是字段和构造函数参数）上。
    ```java
    public record UserRecord(
        @JsonProperty("user_id") Long id,
        String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String password
    ) {}
    ```
*   **GraalVM Native Image 兼容性**：Spring Boot 3.x 强调对 GraalVM Native Image 的支持，以实现更快的启动速度和更低的内存占用。Jackson 及其注解（包括 `@JsonProperty`）通常能与 Native Image 良好协作。Spring Boot AOT (Ahead-Of-Time) 处理会生成必要的反射配置，确保 Jackson 在 Native Image 环境中正确工作。对于非常复杂的自定义序列化/反序列化逻辑，可能需要额外的 `@RegisterReflectionForBinding` 或编程方式注册 hints。
*   **模块化与可扩展性**：Jackson 本身是高度模块化的（例如 `jackson-datatype-jsr310` 处理 Java 8 时间 API）。Spring Boot 会自动检测并配置这些模块。`@JsonProperty` 是核心 `jackson-annotations` 模块的一部分，始终可用。

---

### 6. 扩展与应用

*   **DTO (Data Transfer Object) 设计**：`@JsonProperty` 在设计 API 的请求体和响应体 DTO 时至关重要，它确保了前后端数据格式的正确转换。
*   **与 `@JsonIgnore`, `@JsonInclude`, `@JsonFormat`, `@JsonView` 等注解协同工作**：
    *   `@JsonIgnore`：完全忽略某个属性。
    *   `@JsonInclude(JsonInclude.Include.NON_NULL)`：只在属性值不为 `null` 时序列化。
    *   `@JsonFormat`：格式化日期、数字等。
    *   `@JsonView`：定义属性在不同视图下的可见性。
*   **自定义 `ObjectMapper` 配置**：在 Spring Boot 中，你可以通过 `application.properties` / `application.yml` 或自定义 `Jackson2ObjectMapperBuilderCustomizer` Bean 来全局配置 `ObjectMapper` 的行为（例如，命名策略 `PropertyNamingStrategies.SNAKE_CASE` 可以全局将驼峰转下划线，从而减少对 `@JsonProperty` 的依赖，但 `@JsonProperty` 仍可用于覆盖全局策略或处理特殊情况）。
    ```yaml
    # application.yml
    spring:
      jackson:
        property-naming-strategy: SNAKE_CASE # 全局使用下划线命名
        # serialization:
        #   indent-output: true # 格式化输出JSON
    ```
    即使设置了全局策略，`@JsonProperty("custom_name")` 依然会覆盖这个全局策略。

---

总结来说，`@JsonProperty` 是 Jackson 中一个强大且灵活的注解，它为你提供了对 Java 对象与 JSON 之间映射关系的精细控制。在构建 RESTful API 或任何需要 JSON 数据交换的 Spring Boot 应用中，熟练使用 `@JsonProperty` 及其相关注解是必不可少的技能。