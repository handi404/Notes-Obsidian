如何在 Spring Boot 中创建和使用**自定义校验 (Custom Validation)**。

当你发现 Bean Validation API 提供的标准注解 (如 `@NotNull`, `@Size`, `@Email` 等) 无法满足你特定的业务校验逻辑时，就需要自定义校验规则。

自定义校验通常涉及两个主要部分：

1.  **自定义校验注解 (Custom Constraint Annotation)**：定义一个新的注解，用于标记需要进行特定校验的字段、方法或类。
2.  **自定义校验器 (Custom Constraint Validator)**：实现一个类，该类包含实际的校验逻辑，并与自定义注解关联。

---

### 创建自定义校验的步骤

#### 1. 定义自定义校验注解

这是一个 Java 注解，它需要满足 Bean Validation 规范的一些要求：

*   使用 `@Constraint(validatedBy = ...)` 注解来关联一个或多个实现了 `ConstraintValidator` 接口的校验器类。
*   通常需要包含以下三个标准属性 (尽管可以有默认值)：
    *   `message()`: 校验失败时的错误消息模板。
    *   `groups()`: 定义该校验属于哪个校验组 (默认为空，即属于 `Default` 组)。
    *   `payload()`: 用于传递校验相关的元数据 (通常不常用)。
*   使用 `@Target` 指定该注解可以应用于哪些元素 (字段、方法、类等)。
*   使用 `@Retention(RetentionPolicy.RUNTIME)` 确保注解在运行时可见。
*   可选地，使用 `@Documented` 使其包含在 Javadoc 中。

**示例：创建一个校验字符串是否为有效手机号码的注解 `@ValidPhoneNumber`**

```java
package com.example.validation.custom;

import jakarta.validation.Constraint; // Spring Boot 3.x
import jakarta.validation.Payload;
// import javax.validation.Constraint; // Spring Boot 2.x
// import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class) // 关联校验器
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    // 默认错误消息
    String message() default "Invalid phone number format";

    // 校验分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

    // 可以添加自定义属性，例如，如果手机号格式因国家/地区而异
    // String countryCode() default "CN"; // 示例
}
```

#### 2. 实现自定义校验器

校验器类需要实现 `jakarta.validation.ConstraintValidator` (或 `javax.validation.ConstraintValidator`) 接口。这个接口是泛型的，包含两个类型参数：

*   第一个类型参数：你创建的自定义注解类型 (例如 `ValidPhoneNumber`)。
*   第二个类型参数：被校验的元素的数据类型 (例如 `String`，因为手机号通常是字符串)。

校验器类需要实现两个方法：

*   `initialize(A constraintAnnotation)`:
    *   在校验器实例化后、执行校验前调用。
    *   允许你从注解实例中获取自定义属性的值，并进行初始化。
    *   参数 `constraintAnnotation` 就是你的自定义注解实例。
*   `isValid(T value, ConstraintValidatorContext context)`:
    *   执行实际的校验逻辑。
    *   参数 `value` 是被校验的字段/参数的值。
    *   参数 `context` 提供了额外的上下文信息，例如用于构建更复杂的错误消息或禁用默认的错误消息。
    *   返回 `true` 表示校验通过，`false` 表示校验失败。

**示例：`PhoneNumberValidator` 实现**

```java
package com.example.validation.custom;

import jakarta.validation.ConstraintValidator; // Spring Boot 3.x
import jakarta.validation.ConstraintValidatorContext;
// import javax.validation.ConstraintValidator; // Spring Boot 2.x
// import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // 如果注解中有自定义属性，可以在这里声明并由 initialize 方法初始化
    // private String countryCode;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // 如果注解中有自定义属性，可以在这里获取
        // this.countryCode = constraintAnnotation.countryCode();
        // System.out.println("PhoneNumberValidator initialized. Country code: " + this.countryCode);
    }

    @Override
    public boolean isValid(String phoneNumberField, ConstraintValidatorContext context) {
        if (phoneNumberField == null || phoneNumberField.trim().isEmpty()) {
            // 通常，@NotNull, @NotEmpty, @NotBlank 会处理空值检查。
            // 如果自定义校验允许 null 或空值，则返回 true。
            // 如果自定义校验要求非空，这里可以返回 false，或者依赖其他注解。
            // 对于 `@ValidPhoneNumber`，我们可能认为 null 或空字符串是无效的，
            // 或者我们期望它与 `@NotEmpty` 一起使用。
            // 这里假设如果为空，则认为是有效的 (由其他注解处理非空约束)
            // 如果需要非空，可以直接返回 false。
            return true; // 或 false，取决于业务需求
        }

        // 简单的手机号格式校验 (中国大陆示例，实际应用中可能需要更复杂的正则或库)
        // 例如，11位数字，以1开头
        String regex = "^1[3-9]\\d{9}$";

        // 如果有 countryCode 属性，可以根据国家代码选择不同的正则
        // if ("US".equals(this.countryCode)) {
        //     regex = "..."; // 美国手机号正则
        // }

        boolean isValid = phoneNumberField.matches(regex);

        if (!isValid) {
            // 如果需要自定义更复杂的错误消息或禁用默认消息，可以使用 context
            // context.disableDefaultConstraintViolation(); // 禁用默认消息
            // context.buildConstraintViolationWithTemplate("Phone number must be 11 digits and start with '1'.")
            //        .addPropertyNode("phoneNumber") // 指向哪个属性
            //        .addConstraintViolation();
        }
        return isValid;
    }
}
```

**关于 `isValid` 方法中的 `null` 值处理：**
通常，Bean Validation 的最佳实践是让自定义校验器对 `null` 值返回 `true` (即认为 `null` 是有效的)，并将非空校验的责任交给 `@NotNull`、`@NotEmpty` 或 `@NotBlank` 注解。这样可以保持职责分离，并允许你组合使用注解。如果你的自定义校验逻辑本身就隐含了非空要求，那么当值为 `null` 时返回 `false` 也是可以的。

#### 3. 使用自定义校验注解

现在你可以在你的 Bean (DTO, Entity 等) 中使用新创建的 `@ValidPhoneNumber` 注解了：

```java
package com.example.dto;

import com.example.validation.custom.ValidPhoneNumber;
import jakarta.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotEmpty;

public class ContactInfo {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @ValidPhoneNumber(message = "Please provide a valid Chinese mobile phone number.")
    // @ValidPhoneNumber(countryCode = "US", message = "Please provide a valid US phone number.") // 如果注解支持 countryCode
    private String mobileNumber;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
```

当 Spring Boot (通过 Hibernate Validator) 校验 `ContactInfo` 对象时，如果 `mobileNumber` 字段的值不符合 `PhoneNumberValidator` 中定义的规则，就会产生一个校验错误，错误消息会使用 `@ValidPhoneNumber` 注解中 `message` 属性定义的值，或者你在 `PhoneNumberValidator` 中通过 `ConstraintValidatorContext` 构建的自定义消息。

---

### 进阶主题

#### 1. 组合约束 (Composed Constraints)

你可以创建一个自定义注解，它本身由多个其他的标准或自定义校验注解组成。

**示例：创建一个 `@StrongPassword` 注解，它要求密码非空，长度在 8-20 之间，并且包含特定字符。**

```java
package com.example.validation.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotEmpty(message = "Password cannot be empty.")
@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
         message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character.")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {}) // 注意：这里 validatedBy 为空，因为校验逻辑由组合的注解提供
public @interface StrongPassword {
    String message() default "Invalid password format."; // 这个消息通常不会被直接使用，除非组合的注解都没有提供消息
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
**注意**:
*   `@Constraint(validatedBy = {})`: 对于组合约束，如果它本身不提供额外的校验逻辑，`validatedBy` 属性可以为空数组。
*   消息处理：当组合约束中的某个约束失败时，会使用该具体约束的 `message`。`@StrongPassword` 自身的 `message` 通常作为后备。

使用：
```java
public class UserCredentials {
    @StrongPassword
    private String password;
    // ...
}
```

#### 2. 类级别校验 (Class-Level Constraints)

有时，校验逻辑需要访问 Bean 的多个属性。例如，校验“确认密码”字段是否与“密码”字段匹配。这时，你需要创建一个类级别的自定义注解。

1.  **定义注解**: `@Target` 应包含 `ElementType.TYPE`。
    ```java
    @Documented
    @Constraint(validatedBy = PasswordMatchesValidator.class)
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE }) // 应用于类
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordMatches {
        String message() default "Passwords do not match";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

        String passwordFieldName() default "password"; // 新增属性，用于指定密码字段的名称
        String confirmPasswordFieldName() default "confirmPassword"; // 新增属性，用于指定确认密码字段的名称
    }
    ```

2.  **实现校验器**: 校验器将接收整个 Bean 对象作为 `value`。
    ```java
    import org.springframework.beans.BeanWrapperImpl; // Spring 工具类，方便访问属性

    public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
        private String passwordFieldName;
        private String confirmPasswordFieldName;

        @Override
        public void initialize(PasswordMatches constraintAnnotation) {
            this.passwordFieldName = constraintAnnotation.passwordFieldName();
            this.confirmPasswordFieldName = constraintAnnotation.confirmPasswordFieldName();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // 或者根据需求处理
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
            Object passwordValue = beanWrapper.getPropertyValue(passwordFieldName);
            Object confirmPasswordValue = beanWrapper.getPropertyValue(confirmPasswordFieldName);

            boolean passwordsMatch = (passwordValue == null && confirmPasswordValue == null) ||
                                     (passwordValue != null && passwordValue.equals(confirmPasswordValue));

            if (!passwordsMatch) {
                // 可以将错误消息关联到特定的字段，例如确认密码字段
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                       .addPropertyNode(confirmPasswordFieldName) // 关联到确认密码字段
                       .addConstraintViolation();
            }
            return passwordsMatch;
        }
    }
    ```
    **注意**: 使用 `BeanWrapperImpl` (或其他反射机制) 来获取字段值。确保字段名正确，并处理 `null` 值。

3.  **使用类级别注解**:
    ```java
    @PasswordMatches
    // @PasswordMatches(passwordFieldName = "newPassword", confirmPasswordFieldName = "confirmNewPassword") // 如果字段名不同
    public class PasswordChangeDto {
        private String password;
        private String confirmPassword;
        // Getters and setters
    }
    ```

#### 3. 在校验器中注入 Spring Bean

默认情况下，`ConstraintValidator` 实例是由 Hibernate Validator 创建的，它不知道 Spring 的上下文，因此不能直接 `@Autowired` Spring Bean。

如果你需要在自定义校验器中使用 Spring 管理的 Bean (例如，查询数据库来校验某个值是否唯一)，你需要进行一些额外配置：

1.  **配置 `ConstraintValidatorFactory`**:
    告诉 Hibernate Validator 使用 Spring 来创建 `ConstraintValidator` 实例。
    ```java
    import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
    import jakarta.validation.Validation; // Spring Boot 3.x
    import jakarta.validation.Validator;
    import jakarta.validation.ValidatorFactory;
    // import javax.validation.Validation; // Spring Boot 2.x
    // import javax.validation.Validator;
    // import javax.validation.ValidatorFactory;


    @Configuration
    public class ValidationConfig {

        // 这个 Bean 通常由 Spring Boot 自动配置，但如果需要自定义 ConstraintValidatorFactory，
        // 则需要像下面这样手动配置 Validator。
        // Spring Boot 3.0+ 通常不再需要显式配置这个，
        // 因为 Spring Boot 的自动配置会处理好 ConstraintValidatorFactory 的集成。
        // 但如果遇到问题，或者在更老的版本中，可以尝试。
        @Bean
        public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
            ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                    .configure()
                    .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                    .buildValidatorFactory();
            return validatorFactory.getValidator();
        }
    }
    ```
    **在 Spring Boot 2.x 和 3.x 中，通常 Spring Boot 的自动配置已经处理了 `ConstraintValidatorFactory` 的集成，使得 Spring Bean 可以直接注入到 `ConstraintValidator` 中。** 你可以先尝试直接注入，如果不行再考虑手动配置。

2.  **在校验器中注入 Bean**:
    ```java
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component; // 可选，但有助于IDE识别

    // @Component // 如果希望Spring扫描到它，但通常由ValidatorFactory创建
    public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

        @Autowired
        private UserRepository userRepository; // 假设有一个UserRepository

        @Override
        public void initialize(UniqueUsername constraintAnnotation) {
            // No specific initialization needed from annotation
        }

        @Override
        public boolean isValid(String username, ConstraintValidatorContext context) {
            if (username == null || username.isEmpty()) {
                return true; // Let @NotEmpty handle this
            }
            // 确保 userRepository 已被注入
            if (userRepository == null) {
                // Log a warning or throw an exception, as this indicates a configuration issue
                // System.err.println("UserRepository not injected in UniqueUsernameValidator!");
                // For safety, consider it valid or invalid based on your business rule for this edge case
                return false; // Or true, depending on how critical this check is
            }
            return !userRepository.existsByUsername(username);
        }
    }

    // 对应的注解 @UniqueUsername
    // @Constraint(validatedBy = UniqueUsernameValidator.class)
    // ...
    ```
    **重要**: 确认你的 Spring Boot 版本。在较新的 Spring Boot 版本中 (尤其是2.x后期和3.x)，`SpringConstraintValidatorFactory` 通常是默认配置好的，你可以直接在 `ConstraintValidator` 中 `@Autowired` Spring管理的Bean，**无需显式配置 `ValidationConfig`**。如果遇到注入为 `null` 的情况，再检查配置。

---

### 总结

自定义校验是 Bean Validation 的一个强大扩展，它允许你：

*   封装复杂的、可复用的业务校验逻辑。
*   创建具有清晰语义的校验注解。
*   实现跨字段的类级别校验。
*   利用 Spring 的依赖注入在校验器中使用其他服务。

通过定义注解 (`@interface`) 和实现校验器 (`ConstraintValidator`)，你可以构建出高度定制化且与 Spring Boot 无缝集成的校验方案。记住测试你的自定义校验器，确保它们按预期工作，并能正确处理边界情况和 `null` 值。