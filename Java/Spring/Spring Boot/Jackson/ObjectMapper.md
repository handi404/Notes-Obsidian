Jackson 的核心引擎：`ObjectMapper`。它是你在 Java 中进行 JSON 序列化（Java 对象转 JSON）和反序列化（JSON 转 Java 对象）时交互最多的类。

---

### 1. `ObjectMapper` 是什么？

`ObjectMapper` (全称 `com.fasterxml.jackson.databind.ObjectMapper`) 是 Jackson 库中最主要、最核心的类。你可以把它想象成一个**高度可配置的 JSON 处理工厂**或**转换器**。

它的主要职责包括：

*   **读取 JSON**: 将 JSON 字符串、流或文件解析成 Java 对象 (POJOs)、`Map`、`List` 或 `JsonNode` (树模型)。
*   **写入 JSON**: 将 Java 对象 (POJOs)、`Map`、`List` 转换成 JSON 字符串或写入流/文件。

`ObjectMapper` 实例包含了所有用于执行这些转换的配置信息，例如使用哪些特性、模块、命名策略、注解解释器等。

---

### 2. 为什么 `ObjectMapper` 如此重要？

1.  **功能中心**：几乎所有 Jackson 的功能（如注解处理、特性开关、模块注册、自定义序列化/反序列化器）都是通过配置和使用 `ObjectMapper` 来实现的。
2.  **易用性**：它提供了简单直观的 API (如 `writeValueAsString()`, `readValue()`) 来执行常见的 JSON 操作。
3.  **可配置性**：提供了丰富的配置选项，允许你精细控制 JSON 的序列化和反序列化行为，以适应各种复杂需求。
4.  **可扩展性**：通过模块化设计 (Modules)，可以轻松扩展其功能以支持新的数据类型 (如 Java 8 时间 API, Guava 类型) 或格式 (如 XML, CSV, YAML - 虽然这里我们主要关注 JSON)。
5.  **线程安全**：一旦 `ObjectMapper` 被配置完成，其 `readValue()` 和 `writeValue()` 等核心方法是线程安全的。这意味着你可以创建一个 `ObjectMapper` 实例并在多个线程中安全地共享和重用它（这通常是推荐的做法，因为创建 `ObjectMapper` 有一定开销）。

---

### 3. `ObjectMapper` 如何工作（简要原理）？

虽然内部机制复杂，但我们可以概括一下：

*   **序列化 (Java -> JSON)**:
    1.  当你调用如 `writeValueAsString(myObject)` 时，`ObjectMapper` 会获取 `myObject` 的类信息。
    2.  它会查找并应用该类及其属性上的 Jackson 注解 (如 `@JsonProperty`, `@JsonIgnore` 等)。
    3.  根据配置（如 `SerializationFeature`、命名策略），它会决定哪些属性被包含以及如何命名。
    4.  对于每个属性，它会找到合适的 `JsonSerializer` (可以是默认的，也可以是自定义的或模块提供的)。
    5.  `JsonSerializer` 将属性值转换为 JSON 表示形式。
    6.  最终，所有部分组合成一个完整的 JSON 字符串或流。

*   **反序列化 (JSON -> Java)**:
    1.  当你调用如 `readValue(jsonString, MyClass.class)` 时，`ObjectMapper` 会解析 `jsonString`。
    2.  它会尝试根据 `MyClass.class` 的结构（构造函数、setter、字段，以及 `@JsonCreator`, `@JsonProperty` 等注解）来创建 `MyClass` 的实例。
    3.  它会将 JSON 字段的值映射到 Java 对象的相应属性上。
    4.  对于每个属性，它会找到合适的 `JsonDeserializer` 来将 JSON 值转换为 Java 类型。
    5.  如果 JSON 中包含未在 Java 类中定义的字段，其行为取决于 `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` 的设置（Spring Boot 默认设为 `false`，即忽略未知字段）。

---

### 4. `ObjectMapper` 的核心用法

#### a. 创建 `ObjectMapper` 实例

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // For Java 8 Date/Time

// 1. 基本创建
ObjectMapper objectMapper = new ObjectMapper();

// 2. 推荐：注册常用模块 (Spring Boot 会自动做这些)
objectMapper.registerModule(new JavaTimeModule());
// objectMapper.registerModule(new Jdk8Module()); // for Optional, etc.

// 3. 在 Spring Boot 中，通常直接注入 ObjectMapper 实例
// @Autowired
// private ObjectMapper springManagedObjectMapper;
```

#### b. 序列化：Java 对象 -> JSON

```java
public class User {
    public String name;
    public int age;
    // 构造函数, getters, setters...
}

User user = new User("Alice", 30);

// 1. 序列化为 JSON 字符串 (最常用)
String jsonString = objectMapper.writeValueAsString(user);
// jsonString: {"name":"Alice","age":30}

// 2. 序列化为格式化的 (pretty-printed) JSON 字符串
objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 或 configure(SerializationFeature.INDENT_OUTPUT, true);
String prettyJsonString = objectMapper.writeValueAsString(user);
/* prettyJsonString:
{
  "name" : "Alice",
  "age" : 30
}
*/
objectMapper.disable(SerializationFeature.INDENT_OUTPUT); // 如果之后不需要，可以关闭

// 3. 序列化为字节数组
byte[] jsonBytes = objectMapper.writeValueAsBytes(user);

// 4. 序列化到文件
// objectMapper.writeValue(new File("user.json"), user);

// 5. 序列化到 OutputStream
// OutputStream outputStream = ...;
// objectMapper.writeValue(outputStream, user);
```

#### c. 反序列化：JSON -> Java 对象

```java
String jsonInput = "{\"name\":\"Bob\",\"age\":25}";

// 1. 从 JSON 字符串反序列化 (最常用)
User bob = objectMapper.readValue(jsonInput, User.class);
// bob.getName() -> "Bob", bob.getAge() -> 25

// 2. 从文件反序列化
// User userFromFile = objectMapper.readValue(new File("user.json"), User.class);

// 3. 从 InputStream 反序列化
// InputStream inputStream = ...;
// User userFromStream = objectMapper.readValue(inputStream, User.class);

// 4. 反序列化为泛型集合 (如 List<User>) - 需要 TypeReference
String jsonListInput = "[{\"name\":\"Carol\",\"age\":35},{\"name\":\"Dave\",\"age\":40}]";
List<User> users = objectMapper.readValue(jsonListInput, new TypeReference<List<User>>() {});
// users.get(0).getName() -> "Carol"

// 如果没有 TypeReference，直接用 List.class 会得到 List<LinkedHashMap<String, Object>>
// List wrongUsers = objectMapper.readValue(jsonListInput, List.class);
```

#### d. 处理 JSON 树模型 (`JsonNode`)

当你需要处理结构不确定或只想读取部分 JSON 数据时，`JsonNode` 非常有用。

```java
String complexJson = "{\"name\":\"Eve\",\"details\":{\"city\":\"Wonderland\",\"active\":true},\"tags\":[\"friendly\",\"smart\"]}";

// 1. 读取 JSON 为 JsonNode
JsonNode rootNode = objectMapper.readTree(complexJson);

// 2. 访问节点数据
String name = rootNode.path("name").asText(); // "Eve"
String city = rootNode.path("details").path("city").asText(); // "Wonderland"
boolean isActive = rootNode.path("details").path("active").asBoolean(); // true
JsonNode tagsNode = rootNode.path("tags");
if (tagsNode.isArray()) {
    for (JsonNode tagNode : tagsNode) {
        System.out.println(tagNode.asText()); // "friendly", "smart"
    }
}

// 3. 将 JsonNode 转换为 POJO
User eve = objectMapper.treeToValue(rootNode, User.class); // 如果结构匹配

// 4. 将 POJO 转换为 JsonNode
User anotherUser = new User("Frank", 50);
JsonNode userNode = objectMapper.valueToTree(anotherUser);
// userNode.path("name").asText() -> "Frank"
```

#### e. 对象转换

`ObjectMapper` 也可以在不同的 Java 类型之间进行转换（只要它们在结构上兼容 Jackson 的序列化/反序列化规则）。

```java
Map<String, Object> map = new HashMap<>();
map.put("name", "Grace");
map.put("age", 28);

// Map -> POJO
User grace = objectMapper.convertValue(map, User.class);

// POJO -> Map
Map<String, Object> graceMap = objectMapper.convertValue(grace, new TypeReference<Map<String, Object>>() {});
```

---

### 5. `ObjectMapper` 的关键配置

`ObjectMapper` 有大量配置选项，通过 `configure()`, `enable()`, `disable()` 方法或直接设置特性。

*   **`SerializationFeature`**: 控制序列化行为。
    *   `INDENT_OUTPUT`: 是否美化输出 JSON。
    *   `WRITE_DATES_AS_TIMESTAMPS`: 日期序列化为时间戳 (long) 还是 ISO-8601 字符串 (需要 `JavaTimeModule` 且此特性为 `false`)。Spring Boot 默认将日期序列化为 ISO-8601 字符串。
    *   `FAIL_ON_EMPTY_BEANS`: 当序列化一个没有可识别属性的 Bean 时是否失败。

*   **`DeserializationFeature`**: 控制反序列化行为。
    *   `FAIL_ON_UNKNOWN_PROPERTIES`: JSON 中有未知属性时是否失败。Spring Boot 默认设为 `false` (忽略未知属性)。
    *   `ACCEPT_EMPTY_STRING_AS_NULL_OBJECT`: 是否接受空字符串作为 `null` 对象。
    *   `ACCEPT_SINGLE_VALUE_AS_ARRAY`: 当期望一个数组但 JSON 中只提供单个值时，是否自动包装成单元素数组。

*   **`MapperFeature`**: 通用映射器特性。
    *   `DEFAULT_VIEW_INCLUSION`: 与 `@JsonView` 相关。
    *   `SORT_PROPERTIES_ALPHABETICALLY`: 是否按字母顺序序列化属性。
    *   `ACCEPT_CASE_INSENSITIVE_PROPERTIES`: 反序列化时是否忽略属性名的大小写。

*   **`JsonParser.Feature`**: 底层 JSON 解析器特性。
*   **`JsonGenerator.Feature`**: 底层 JSON 生成器特性。

**配置示例：**
```java
ObjectMapper customMapper = new ObjectMapper();
customMapper.registerModule(new JavaTimeModule());

// 序列化配置
customMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
customMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 使用 ISO-8601

// 反序列化配置
customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
customMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

// 命名策略 (PropertyNamingStrategy 在 Jackson 2.12+ 中变为 PropertyNamingStrategies)
// import com.fasterxml.jackson.databind.PropertyNamingStrategies;
customMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // e.g., userName -> user_name

// 设置默认的日期格式 (主要用于 java.util.Date，对于 Java 8 Time API，@JsonFormat 更常用)
// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// customMapper.setDateFormat(sdf);

// 设置全局的属性包含规则 (类似类级别的 @JsonInclude)
// import com.fasterxml.jackson.annotation.JsonInclude;
// customMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
```

---

### 6. 与 Spring Boot 的集成

Spring Boot 极大地简化了 `ObjectMapper` 的使用：

1.  **自动配置**: Spring Boot 会自动创建一个 `ObjectMapper` Bean 并将其配置为合理的默认值。
    *   `spring-boot-starter-json` (通常由 `spring-boot-starter-web` 引入) 负责此功能。
    *   自动注册 `JavaTimeModule`, `Jdk8Module`, `ParameterNamesModule` 等。
    *   默认 `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` 为 `false`。
    *   默认 `SerializationFeature.WRITE_DATES_AS_TIMESTAMPS` 为 `false` (使用 ISO-8601 字符串)。

2.  **通过 `application.properties` / `application.yml` 配置**:
    ```yaml
    # application.yml
    spring:
      jackson:
        serialization:
          indent-output: true # 美化输出
          write-dates-as-timestamps: false
        deserialization:
          fail-on-unknown-properties: false
        property-naming-strategy: SNAKE_CASE # e.g., com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
        default-property-inclusion: non_null # 类似 @JsonInclude(JsonInclude.Include.NON_NULL)
        date-format: "yyyy-MM-dd HH:mm:ss" # 对 java.util.Date 生效
        time-zone: "GMT+8"
    ```

3.  **自定义 `ObjectMapper`**:
    *   **`Jackson2ObjectMapperBuilderCustomizer`**: 这是推荐的、细粒度的自定义方式。你可以定义一个或多个此类型的 Bean，它们会按顺序应用于 Spring Boot 自动配置的 `Jackson2ObjectMapperBuilder`。
        ```java
        import com.fasterxml.jackson.databind.PropertyNamingStrategies;
        import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        @Configuration
        public class JacksonConfig {
            @Bean
            public Jackson2ObjectMapperBuilderCustomizer customJackson() {
                return builder -> {
                    builder.indentOutput(true);
                    builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
                    // builder.modulesToInstall(new MyCustomModule());
                };
            }
        }
        ```
    *   **提供一个 `@Primary ObjectMapper` Bean**: 如果你需要完全控制 `ObjectMapper` 的创建，可以定义自己的 `ObjectMapper` Bean 并标记为 `@Primary`。这将覆盖 Spring Boot 的自动配置。
        ```java
        // @Bean
        // @Primary
        // public ObjectMapper primaryObjectMapper() {
        //     ObjectMapper objectMapper = new ObjectMapper();
        //     // ...你的完整配置...
        //     return objectMapper;
        // }
        ```
    *   **使用 `Jackson2ObjectMapperBuilder`**: Spring Boot 也提供了一个 `Jackson2ObjectMapperBuilder` Bean，你可以注入它来构建具有 Spring Boot 默认设置基础的 `ObjectMapper` 实例。
        ```java
        // import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
        // @Autowired
        // private Jackson2ObjectMapperBuilder builder;
        // ObjectMapper customMapper = builder.build(); // 基于Spring Boot配置，可以再定制
        // ObjectMapper snakeCaseMapper = builder.createXmlMapper(false) // 这是一个例子，说明builder可以创建不同mapper
        //                                      .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        //                                      .build();
        ```

---

### 7. 最新进展与高级主题 (Spring Boot 3.x 视角)

*   **Java Records 良好支持**: Jackson 对 Java 14+ 的 Records 类型有出色的原生支持。`ObjectMapper` 可以无缝序列化和反序列化 Records，通常不需要额外注解（除非要自定义名称或行为）。
*   **GraalVM Native Image**: Spring Boot 3.x 重点支持 GraalVM。Jackson 通常能很好地与 Native Image 配合。Spring AOT (Ahead-Of-Time) 编译会为 `ObjectMapper` 和你代码中用到的 DTO 生成必要的反射和序列化 hints。对于非常动态或复杂的自定义序列化器/反序列化器，可能需要手动添加 `@RegisterReflectionForBinding` 或编程注册 hints。
*   **性能考量 - `ObjectReader` 和 `ObjectWriter`**:
    *   对于性能敏感且需要重复序列化/反序列化相同类型和配置的场景，`ObjectMapper` 提供了 `readerFor(Class<T>)` 和 `writerFor(Class<T>)` (以及其他重载版本如 `writerWithView()`)。
    *   这些方法返回 `ObjectReader` 和 `ObjectWriter` 实例，它们是预配置且不可变的，可以安全重用，并且比直接使用 `ObjectMapper.readValue/writeValue` 略快，因为它们缓存了类型信息和部分配置。
    ```java
    ObjectReader userReader = objectMapper.readerFor(User.class);
    ObjectWriter userWriter = objectMapper.writerFor(User.class);

    User user = userReader.readValue(jsonInput);
    String jsonOutput = userWriter.writeValueAsString(user);
    ```
*   **模块化生态**: Jackson 拥有丰富的模块生态系统，如：
    *   `jackson-module-afterburner`: 通过字节码生成加速序列化/反序列化（对现代 JVM 和 Records 带来的性能提升可能边际递减）。
    *   `jackson-dataformat-xml`: 支持 XML。
    *   `jackson-module-kotlin`: 更好地支持 Kotlin 特性。
    Spring Boot 会根据 classpath 上的依赖自动注册一些模块。

---

### 8. 最佳实践与提示

1.  **重用 `ObjectMapper`**: `ObjectMapper` 实例是线程安全的（一旦配置完成）。创建它有一定开销，所以应该尽可能重用（Spring Boot 自动管理的 Bean 就是单例的）。
2.  **使用 `TypeReference` 处理泛型**: 反序列化泛型集合（如 `List<MyClass>`）时，务必使用 `new TypeReference<List<MyClass>>() {}` 来保留泛型类型信息。
3.  **理解配置优先级**: 注解 (`@JsonProperty` 等) > `ObjectMapper` 实例配置 > 全局默认配置。Spring Boot 的 `application.properties` 配置会影响自动配置的 `ObjectMapper` 实例。
4.  **模块化你的配置**: 在 Spring Boot 中，使用 `Jackson2ObjectMapperBuilderCustomizer` 是推荐的模块化配置方式。
5.  **谨慎对待 `FAIL_ON_UNKNOWN_PROPERTIES`**: Spring Boot 默认将其设为 `false` (忽略未知属性)，这通常更具弹性，能更好地适应 API 的演进。但在某些需要严格校验的场景下，你可能希望将其设为 `true`。

---

总结来说，`ObjectMapper` 是 Jackson 库的心脏。理解它的核心功能、配置选项以及如何与 Spring Boot 集成，对于任何使用 Spring Boot 进行 JSON 处理的开发者来说都至关重要。它提供了强大的灵活性和控制力，以满足各种 JSON 处理需求。


## objectMapper.writeValue(response.getOutputStream(), dataObject)
在 `void` 返回类型的方法中，通过 `HttpServletResponse` 对象，调用 `objectMapper.writeValue(response.getOutputStream(), dataObject)` 这种方式来发送 JSON 响应。

这确实是一种直接且有效的方法，在传统的 Servlet API 环境下 (如 Spring MVC) 可以工作。让我们详细分析一下这种具体用法：

---

### 使用 `objectMapper.writeValue(response.getOutputStream(), object)`

这种方法的核心思想是：

1.  获取 `HttpServletResponse` 对象。
2.  设置响应的 `Content-Type` 为 `application/json` (以及可选的字符编码)。
3.  从 `response` 对象获取底层的 `ServletOutputStream`。
4.  使用 `ObjectMapper` 的 `writeValue()` 方法，将你的 Java 对象直接序列化为 JSON 字节流，并写入到该 `ServletOutputStream` 中。

**示例代码 (与之前类似，但聚焦于 `getOutputStream()`):**

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletResponse; // 使用 jakarta.servlet
import java.io.IOException;

@Controller
public class MyMvcController {

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot 自动配置的 ObjectMapper

    @GetMapping("/send-data-via-outputstream")
    public void sendDataViaOutputStream(HttpServletResponse response) {
        MyDataObject data = new MyDataObject("example via OutputStream", 456);

        try {
            // 1. 设置响应内容类型和编码 (非常重要！)
            // 必须在获取 OutputStream 之前设置，或者至少在响应提交之前。
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"
            response.setCharacterEncoding("UTF-8"); // 明确指定UTF-8总是好的

            // 2. 将对象直接序列化到响应的输出流
            // ObjectMapper 会处理将对象转换为 JSON 字节并写入流
            objectMapper.writeValue(response.getOutputStream(), data);

            // response.getOutputStream().flush(); // 一般 objectMapper.writeValue 会处理 flush，但显式调用有时有益
                                              // 或者让 Servlet 容器在请求结束时处理。

        } catch (IOException e) {
            // 异常处理：记录日志，也许尝试发送一个错误响应 (如果可能)
            e.printStackTrace();
            // 如果头部还未提交，可以尝试设置错误状态码
            if (!response.isCommitted()) {
                try {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error writing JSON response");
                } catch (IOException ex) {
                    ex.printStackTrace(); // 如果连错误都发送不了，就只能记录了
                }
            }
        }
        // 方法返回 void，Spring MVC 知道响应已经由你手动处理
    }
}

// 假设 MyDataObject 类已定义 (如之前的例子)
class MyDataObject {
    public String name;
    public int value;

    public MyDataObject(String name, int value) {
        this.name = name;
        this.value = value;
    }
    // getters/setters or public fields for Jackson
}
```

---

### 优点和考虑因素：

1.  **效率 (相对于先转 String 再写入 `PrintWriter`)**:
    *   `objectMapper.writeValue(OutputStream, Object)` 通常比 `objectMapper.writeValueAsString(Object)` 然后 `printWriter.print(String)` 更高效。
    *   因为它直接将序列化后的字节写入输出流，避免了创建一个完整的中间 JSON 字符串对象的开销，特别是对于大的 JSON 对象，这可以减少内存分配和拷贝。

2.  **直接控制**:
    *   你对响应的生成有完全的控制。

3.  **适用于流式大数据**:
    *   虽然这个例子中的 `MyDataObject` 很小，但如果 `data` 是一个可以被 Jackson 流式序列化的大型对象或集合（例如，通过 `JsonGenerator` 手动控制），这种方式是合适的。

4.  **与 `void` 返回类型兼容**:
    *   完美契合 `void` 方法，因为你显式地处理了响应的写入。

---

### 注意事项和最佳实践：

1.  **`Content-Type` 和 `CharacterEncoding`**:
    *   **至关重要**: 必须在调用 `response.getOutputStream()` 并开始写入之前，或者至少在响应被提交（flushed）之前，通过 `response.setContentType()` 和 `response.setCharacterEncoding()` 设置正确的响应头。
    *   对于 JSON，`Content-Type` 通常是 `application/json`。
    *   `CharacterEncoding` 推荐设置为 `UTF-8`。

2.  **异常处理**:
    *   `objectMapper.writeValue()` 和 `response.getOutputStream()` 都可能抛出 `IOException`。必须妥善处理这些异常。
    *   一旦开始向 `OutputStream` 写入数据，响应头通常就已经提交了。这意味着如果后续发生 `IOException`，你可能无法再通过 `response.setStatus()` 或 `response.sendError()` 来改变 HTTP 状态码或发送标准的错误页面（因为头部可能已经发送给客户端了）。日志记录是主要的补救措施。
    *   你可以尝试在发生错误时，如果响应还未提交 (`!response.isCommitted()`)，则发送错误状态码。

3.  **关闭流 (Servlet Container 管理)**:
    *   你**不应该**手动关闭从 `HttpServletResponse.getOutputStream()` 获取的流 (`servletOutputStream.close()`)。Servlet 容器会负责在请求处理完毕后关闭它。手动关闭可能会导致问题。

4.  **`flush()`**:
    *   `objectMapper.writeValue()` 在完成时通常会刷新底层流，确保数据被发送。显式调用 `response.getOutputStream().flush()` 一般不是必需的，但有时为了确保数据立即发送（例如在长连接或流式传输中），可能会使用。对于一次性写入完整 JSON 响应的场景，通常让 Servlet 容器在请求结束时处理最终的 flush 即可。

5.  **Spring MVC 的替代方案**:
    *   再次强调，虽然这种方法可行，但在典型的 Spring MVC 应用中，返回对象并让 `@ResponseBody` (或 `@RestController`) 和 Spring 的 `HttpMessageConverter` (它内部使用 `ObjectMapper`) 来处理响应是更常见、更简洁、更易于测试和维护的方式。
    *   只有当你确实需要这种底层的、直接的流操作时，或者在一些非标准集成场景下，才应该选择这种手动写入的方式。

6.  **线程安全**:
    *   `ObjectMapper` 实例（如果正确配置且不修改其配置）的 `writeValue` 方法是线程安全的。
    *   `HttpServletResponse` 对象本身是特定于单个请求的，因此在单个请求处理线程中使用是安全的。

---

### 总结

`objectMapper.writeValue(response.getOutputStream(), dataObject)` 是一个在 `void` 返回类型的 Spring MVC 方法中直接将 Java 对象序列化为 JSON 并发送到客户端的有效技术。它比先转换为字符串再写入 `PrintWriter` 更高效，因为它避免了中间字符串的创建。

然而，你需要负责正确设置响应头 (`Content-Type`, `CharacterEncoding`) 并处理可能发生的 `IOException`。

**何时使用这种方法？**

*   当你需要对响应写入过程进行非常精细的控制时。
*   当性能是极致的考量，并且避免中间字符串转换能带来可观的收益时（通常针对非常大的对象或高吞吐量场景）。
*   当与某些期望直接操作输出流的第三方库或旧代码集成时。
*   在 `void` 方法中，这是发送二进制（如 JSON 字节）响应的直接方式。

如果这些不是你的主要驱动因素，那么利用 Spring MVC 提供的更高级别的抽象（如返回对象 + `@ResponseBody`）通常是更好、更简洁的选择。