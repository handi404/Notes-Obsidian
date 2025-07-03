在现代 Web 应用开发中，对用户输入或传入的数据进行校验是至关重要的，以确保数据的正确性、完整性和安全性。Spring Boot 通过集成 **Bean Validation API** 和其参考实现 **Hibernate Validator**，提供了一套强大且易于使用的声明式校验机制。

---

### 1. 核心依赖：Bean Validation API 与 Hibernate Validator

Spring Boot 的校验功能主要依赖以下两个核心组件：

*   **Bean Validation API (JSR 380 / Jakarta Bean Validation 3.0)**:
    *   这是一个 Java 规范 (由 Jakarta EE 定义，之前是 JSR 标准)，它定义了一套用于 JavaBean 组件数据校验的元数据模型和 API。
    *   它允许你通过注解的方式在你的模型类 (POJOs/Beans) 的字段、方法或类级别上声明校验规则。
    *   **关键 API 包**:
        *   **`jakarta.validation.constraints.*`**: 包含所有标准的校验注解，如 `@NotNull`, `@Size`, `@Min`, `@Max`, `@Pattern`, `@Email` 等。
        *   `jakarta.validation.Validator`: 执行校验的核心接口。
        *   `jakarta.validation.ConstraintViolation`: 代表一个校验失败的信息。
    *   **注意**: 在 Spring Boot 3.x (基于 Jakarta EE 9+) 中，包名是 `jakarta.validation.*`。在 Spring Boot 2.x (基于 Java EE 8) 中，包名是 `javax.validation.*`。

*   **Hibernate Validator**:
    *   这是 Bean Validation API 的一个**参考实现**，也是目前最流行和功能最完善的实现。
    *   Spring Boot 默认会使用 Hibernate Validator (如果它在类路径下)。
    *   它实现了 Bean Validation API 定义的所有标准注解，并提供了一些额外的实用注解和特性。

---

### 2. 如何在 Spring Boot 项目中引入 Validation 依赖

通常情况下，如果你使用了某些 starter，Validation 相关的依赖可能已经被间接引入了。最常见的是 `spring-boot-starter-web`。

*   **对于 Web 应用 (最常见)**:
    *   `spring-boot-starter-web` 包含了 `spring-boot-starter-validation`。
    *   `spring-boot-starter-validation` 会自动引入 `hibernate-validator`。
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```
    或者，如果你只需要校验功能而不构建 Web 应用 (例如，在服务层或独立应用中进行校验)：
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ```
    这个 starter 会确保 `jakarta.validation-api` 和 `hibernate-validator` (作为实现) 被添加到你的项目中。

*   **Spring Boot 版本与包名**:
    *   **Spring Boot 3.x**: 使用 `jakarta.validation-api` (Jakarta Bean Validation 3.0)。
    *   **Spring Boot 2.x**: 使用 `javax.validation-api` (Bean Validation 2.0)。

Spring Boot 的依赖管理会确保你获得兼容版本的 API 和实现。

---

### 3. 如何使用 Validation 注解

一旦依赖引入，你就可以在你的数据模型类 (通常是 DTOs - Data Transfer Objects, 或 Entities) 上使用校验注解了。

**示例：一个用户注册的 DTO**

```java
import jakarta.validation.constraints.*; // Spring Boot 3.x
// import javax.validation.constraints.*; // Spring Boot 2.x

public class UserRegistrationDto {

    @NotEmpty(message = "Username cannot be empty") // 不能为空字符串，且不能为 null
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password must be strong") // 示例：强密码校验
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than or equal to 120")
    private Integer age;

    @AssertTrue(message = "You must agree to the terms and conditions")
    private boolean termsAccepted;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    // ... other getters and setters
}
```

**常用的标准校验注解 (来自 `jakarta.validation.constraints.*`)**:

*   `@Null`: 被注释的元素必须为 `null`。
*   `@NotNull`: 被注释的元素必须不为 `null`。
*   `@NotEmpty`: 被注释的元素（如字符串、集合、Map 或数组）必须不为 `null` 且不能为空。
*   `@NotBlank`: 被注释的字符串必须非 `null` 且必须包含至少一个非空白字符。
*   `@Size(min=, max=)`: 被注释的元素的大小必须在指定的范围内 (适用于字符串、集合、Map、数组)。
*   `@Min(value)`: 被注释的元素必须是一个数字，其值必须大于等于指定的最小值。
*   `@Max(value)`: 被注释的元素必须是一个数字，其值必须小于等于指定的最大值。
*   `@DecimalMin(value, inclusive=)`: 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 (可以指定是否包含边界)。
*   `@DecimalMax(value, inclusive=)`: 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 (可以指定是否包含边界)。
*   `@Positive`: 被注释的元素必须是一个正数。
*   `@PositiveOrZero`: 被注释的元素必须是一个正数或零。
*   `@Negative`: 被注释的元素必须是一个负数。
*   `@NegativeOrZero`: 被注释的元素必须是一个负数或零。
*   `@Email`: 被注释的元素必须是格式正确的电子邮件地址。
*   `@Pattern(regexp=, flags=)`: 被注释的元素必须符合指定的正则表达式。
*   `@Digits(integer=, fraction=)`: 被注释的元素必须是一个数字，其值必须在可接受的范围内 (整数位数、小数位数)。
*   `@Future`: 被注释的日期必须是将来的日期。
*   `@Past`: 被注释的日期必须是过去的日期。
*   `@FutureOrPresent`: 被注释的日期必须是将来的日期或当前日期。
*   `@PastOrPresent`: 被注释的日期必须是过去的日期或当前日期。
*   `@AssertTrue`: 被注释的元素必须为 `true`。
*   `@AssertFalse`: 被注释的元素必须为 `false`。

**Hibernate Validator 提供的额外注解 (部分)**:

*   `@URL`: 校验是否为合法的 URL。
*   `@CreditCardNumber`: 校验是否为合法的信用卡号。
*   `@Length(min=, max=)`: 类似于 `@Size`，但专门用于字符串长度。
*   `@Range(min=, max=)`: 校验数字是否在指定范围内。
*   `@SafeHtml`: 校验字符串是否不包含恶意的 HTML 代码 (需要额外的依赖 `jsoup`)。

---

### 4. 在 Spring MVC Controller 中触发校验

在 Spring MVC 中，校验通常在 Controller 层处理 HTTP 请求时触发。

*   在 Controller 方法的参数上使用 `@Valid` (来自 `jakarta.validation` 包) 或 `@Validated` (来自 `org.springframework.validation` 包) 注解。
*   紧跟在 `@Valid` 或 `@Validated` 注解的参数后面，可以添加一个 `BindingResult` (或 `Errors`) 参数，用于捕获校验结果。

```java
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Spring Boot 3.x
// import javax.validation.Valid; // Spring Boot 2.x
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userDto,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 处理校验错误
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // 如果校验通过，继续处理用户注册逻辑
        // userService.register(userDto);
        return ResponseEntity.ok("User registered successfully!");
    }
}
```

**`@Valid` vs `@Validated`**:

*   `@Valid`: 这是 JSR 380/Jakarta Bean Validation 标准定义的注解。它主要用于级联校验 (如果 `UserRegistrationDto` 内部有其他需要校验的对象，并且该对象字段也用了 `@Valid`)。
*   `@Validated`: 这是 Spring 框架提供的注解。它支持**校验分组 (Validation Groups)** 功能，允许你对同一个 Bean 在不同场景下应用不同的校验规则。`@Validated` 也可以用于方法级别的参数校验 (配合 `@Constraint` 注解的方法参数)。

在简单的 Controller 参数校验场景下，两者通常可以互换使用。但如果需要分组校验，就必须使用 `@Validated`。

---

### 5. 校验分组 (Validation Groups)

有时，你可能希望同一个 Bean 在不同的操作中有不同的校验规则。例如，创建用户时所有字段必填，但更新用户时某些字段可选。

1.  **定义分组接口**:
    ```java
    public interface OnCreate {}
    public interface OnUpdate {}
    ```

2.
    **在 Bean 的注解上指定分组**:
```java
public class UserDto {
	@NotNull(groups = OnCreate.class, message = "ID cannot be null for update")
	@Null(groups = OnUpdate.class, message = "ID must be null for creation") // 假设创建时不应提供ID
	private Long id;

	@NotEmpty(groups = {OnCreate.class, OnUpdate.class}, message = "Username cannot be empty")
	@Size(min = 3, max = 20, groups = {OnCreate.class, OnUpdate.class})
	private String username;

	@NotEmpty(groups = OnCreate.class, message = "Password cannot be empty for creation")
	// 更新时密码可以为空 (不修改密码)
	@Size(min = 8, groups = OnCreate.class, message = "Password must be at least 8 characters")
	private String password;

	// ...
}
```

3.  **在 Controller 中使用 `@Validated` 指定分组**:
    ```java
    import org.springframework.validation.annotation.Validated;

    @RestController
    @RequestMapping("/api/users")
    public class UserController {

        @PostMapping
        public ResponseEntity<?> createUser(@Validated(OnCreate.class) @RequestBody UserDto userDto,
                                           BindingResult bindingResult) {
            // ...
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(@PathVariable Long id,
                                            @Validated(OnUpdate.class) @RequestBody UserDto userDto,
                                            BindingResult bindingResult) {
            // ... userDto.setId(id);
        }
    }
    ```

---

### 6. 在服务层 (Service Layer) 进行校验

你也可以在服务层的方法上进行校验，特别是当方法参数不是直接来自 HTTP 请求时，或者你想在业务逻辑执行前再次确认数据有效性。

1.  在配置类上添加 `@EnableMethodValidation` (Spring Boot 3.2+)。对于更早的版本或者更细粒度的控制，你可能需要手动配置 `MethodValidationPostProcessor`。
2.  在服务类上使用 `@Validated` 注解。
3.  在服务方法的参数上使用校验注解。

```java
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Service
@Validated // 使该类中的方法参数校验生效
public class UserService {

    public void processUser(@Valid UserRegistrationDto user) {
        // 如果 userDto 无效，这里会抛出 ConstraintViolationException
        System.out.println("Processing user: " + user.getUsername());
    }

    public String getUserDetail(@NotBlank(message = "User ID cannot be blank") String userId,
                                @Min(value = 1, message = "Page number must be at least 1") int page) {
        // 如果 userId 为空或 page < 1，会抛出 ConstraintViolationException
        return "Details for user " + userId + " on page " + page;
    }
}

// 需要在配置类中启用方法校验
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
// import org.springframework.context.annotation.Bean;
//
// @Configuration
// public class ValidationConfig {
// @Bean // 对于 Spring Boot < 3.2，或者需要自定义时
// public MethodValidationPostProcessor methodValidationPostProcessor() {
// return new MethodValidationPostProcessor();
//     }
// }

// Spring Boot 3.2+ 可以直接在主配置类或任何 @Configuration 类上使用 @EnableMethodValidation
// @SpringBootApplication
// @EnableMethodValidation
// public class MyApplication { //... }
```
当方法参数校验失败时，默认会抛出 `ConstraintViolationException`。你可以通过 `@ControllerAdvice` 来统一处理这个异常。

---

### 7. 自定义校验注解

如果标准的校验注解不满足你的需求，你可以创建自定义的校验注解。

1.  **创建注解**:
    ```java
    import jakarta.validation.Constraint;
    import jakarta.validation.Payload;
    import java.lang.annotation.*;

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Constraint(validatedBy = начинаетсяСValidator.class) // 关联校验器
    public @interface начинаетсяС { // 例如: @StartsWith("PREFIX_")
        String message() default "String must start with the specified prefix";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
        String value(); // 注解的参数，例如 "PREFIX_"
    }
    ```

2.  **创建校验器 (Validator)**:
    ```java
    import jakarta.validation.ConstraintValidator;
    import jakarta.validation.ConstraintValidatorContext;

    public class начинаетсяСValidator implements ConstraintValidator<начинаетсяС, String> {
        private String prefix;

        @Override
        public void initialize(начинаетсяС constraintAnnotation) {
            this.prefix = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // 或者根据需求返回 false，取决于 null 是否被认为有效
            }
            return value.startsWith(this.prefix);
        }
    }
    ```

3.  **使用自定义注解**:
    ```java
    public class ProductDto {
        @начинаетсяС("PROD_")
        private String productCode;
        // ...
    }
    ```

---

### 8. 国际化 (i 18 n) 错误消息

校验消息 (如 `message = "Username cannot be empty"`) 可以被国际化。

*   默认情况下，Hibernate Validator 会尝试从类路径下的 `ValidationMessages.properties` (以及特定语言环境的变体，如 `ValidationMessages_zh_CN.properties`) 文件中加载消息。
*   消息的 key 可以是注解中定义的 `message` 属性的值，或者通过 `{...}` 占位符引用。
    例如，在 `UserRegistrationDto` 中：
    ```java
    @NotEmpty // 默认消息 key 是 {jakarta.validation.constraints.NotEmpty.message} 或 {javax.validation.constraints.NotEmpty.message}
    private String username;

    @Size(min = 3, max = 20, message = "{user.username.size}") // 自定义消息 key
    private String usernameWithCustomMessage;
    ```
    然后在 `ValidationMessages.properties` 中定义：
    ```
    jakarta.validation.constraints.NotEmpty.message=This field cannot be empty.
    user.username.size=Username must be between {min} and {max} characters.
    ```
    Spring Boot 会自动配置一个 `MessageInterpolator` 来处理这些消息。

---

### 总结

*   Spring Boot 通过 `spring-boot-starter-validation` (通常由 `spring-boot-starter-web` 引入) 自动集成了 **Jakarta Bean Validation API** 和 **Hibernate Validator**。
*   使用 `jakarta.validation.constraints.*` (Spring Boot 3.x) 或 `javax.validation.constraints.*` (Spring Boot 2.x) 中的注解在你的 Bean 上声明校验规则。
*   在 Controller 方法参数上使用 `@Valid` 或 `@Validated` (配合 `BindingResult`) 来触发校验。
*   `@Validated` 支持校验分组。
*   可以通过 `@EnableMethodValidation` (Spring Boot 3.2+) 或配置 `MethodValidationPostProcessor` 在服务层方法上启用参数校验。
*   可以创建自定义校验注解和校验器。
*   校验错误消息支持国际化。

Validation 是构建健壮应用程序不可或缺的一部分，Spring Boot 使得实现它变得非常方便和声明化。