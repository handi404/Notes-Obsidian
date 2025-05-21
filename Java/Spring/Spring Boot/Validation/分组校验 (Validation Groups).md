Spring Boot 中的**分组校验 (Validation Groups)**。

当同一个 JavaBean (或 DTO) 在不同的业务场景下需要应用不同的校验规则时，分组校验就派上用场了。例如：

*   **创建用户 (Create User)**: 用户名、密码、邮箱都必须提供且符合格式。
*   **更新用户信息 (Update User Info)**: 用户名可能不允许修改，邮箱可以修改但必须符合格式，密码字段可能完全不出现 (表示不修改密码)。
*   **用户登录 (User Login)**: 只需要用户名和密码。

如果不对校验规则进行分组，你可能需要为每个场景创建不同的 DTO 类，或者在代码中用 `if-else` 判断来手动选择校验哪些字段，这会使代码变得复杂和难以维护。分组校验提供了一种更优雅、更声明式的方式来解决这个问题。

---

### 如何实现分组校验

实现分组校验主要涉及以下步骤：

1.  **定义分组接口 (Marker Interfaces)**：
    创建一些空的接口，用作校验分组的标记。这些接口本身没有任何方法或字段，它们仅仅作为一种类型标识。

2.  **在校验注解中指定分组 (Apply Groups to Constraints)**：
    在你的 JavaBean 的校验注解 (如 `@NotNull`, `@Size`, `@Email`，以及自定义注解) 上，使用 `groups` 属性来指定该校验规则属于哪个或哪些分组。

3.  **在触发校验时指定要激活的分组 (Specify Active Groups During Validation)**：
    当执行校验操作时 (例如在 Controller 中或服务层方法中)，通过 Spring 的 `@Validated` 注解来指定当前场景下应该激活哪些校验分组。

---

### 1. 定义分组接口

这些接口通常定义在一个集中的地方，例如 `com.example.validation.groups` 包下。

```java
package com.example.validation.groups;

/**
 * 标记接口，用于创建操作的分组校验
 */
public interface OnCreate {}

/**
 * 标记接口，用于更新操作的分组校验
 */
public interface OnUpdate {}

/**
 * 标记接口，用于登录操作的分组校验
 */
public interface OnLogin {}

// 你还可以定义一个默认分组，虽然 Default 分组是隐式的
// import jakarta.validation.groups.Default; // Spring Boot 3.x
// import javax.validation.groups.Default; // Spring Boot 2.x
// public interface Default extends jakarta.validation.groups.Default {}
// (通常不需要显式声明 Default 接口，除非你想在继承或组合时更明确)
```

**`jakarta.validation.groups.Default` (或 `javax.validation.groups.Default`)**：
这是一个特殊的内置分组。如果校验注解没有显式指定 `groups` 属性，那么它默认属于 `Default` 分组。

---

### 2. 在校验注解中指定分组

在你的 JavaBean (DTO 或 Entity) 的字段上应用校验注解时，通过 `groups` 属性指定它们适用的分组。

**示例：`UserDto.java`**

```java
package com.example.dto;

import com.example.validation.groups.OnCreate;
import com.example.validation.groups.OnLogin;
import com.example.validation.groups.OnUpdate;
import jakarta.validation.constraints.Email; // Spring Boot 3.x
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
// import javax.validation.constraints.*; // Spring Boot 2.x
// import javax.validation.groups.Default; // Spring Boot 2.x

public class UserDto {

    @Null(groups = OnCreate.class, message = "ID must be null during creation")
    @NotEmpty(groups = OnUpdate.class, message = "ID cannot be empty during update")
    private String id; // 例如，UUID 字符串

    @NotEmpty(groups = {OnCreate.class, OnLogin.class}, message = "Username cannot be empty")
    @Size(min = 3, max = 20, groups = {OnCreate.class, OnLogin.class}, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(groups = {OnCreate.class, OnLogin.class}, message = "Password cannot be empty")
    @Size(min = 8, groups = OnCreate.class, message = "Password must be at least 8 characters for new user")
    // 更新时，密码可以为空 (表示不修改)，如果提供了，则需要校验长度
    @Size(min = 8, groups = OnUpdate.class, message = "New password must be at least 8 characters")
    private String password;

    @NotEmpty(groups = OnCreate.class, message = "Email cannot be empty for new user")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Email should be valid")
    // 更新时，邮箱可以不提供 (表示不修改)，如果提供了，则需要是有效邮箱
    private String email;

    @NotEmpty(message = "Nickname cannot be empty") // 没有指定 groups，属于 Default 分组
    private String nickname;


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
```

**解释**：

*   `id`:
    *   在 `OnCreate` 组下，必须为 `null` (因为 ID 通常由后端生成)。
    *   在 `OnUpdate` 组下，必须非空 (用于标识要更新的实体)。
*   `username`:
    *   在 `OnCreate` 和 `OnLogin` 组下，都不能为空且长度在 3-20 之间。
    *   在 `OnUpdate` 组下，没有校验规则 (假设用户名不允许更新，或者由其他逻辑处理)。
*   `password`:
    *   在 `OnCreate` 和 `OnLogin` 组下，都不能为空。
    *   在 `OnCreate` 组下，长度至少为 8。
    *   在 `OnUpdate` 组下，如果提供了新密码，长度至少为 8 (如果为空，则不校验，表示不修改密码)。
*   `email`:
    *   在 `OnCreate` 组下，不能为空且必须是有效邮箱。
    *   在 `OnUpdate` 组下，如果提供了，必须是有效邮箱 (可以为空，表示不修改)。
*   `nickname`:
    *   没有指定 `groups` 属性，所以它属于 `Default` 分组。这意味着，如果校验时没有指定任何分组，或者指定了 `Default.class` 作为分组，这个字段的 `@NotEmpty` 规则就会生效。

---

### 3. 在触发校验时指定要激活的分组

这通常在 Controller 的方法参数上，或者在服务层的方法上完成。你需要使用 Spring 框架提供的 `@Validated` 注解 (来自 `org.springframework.validation.annotation` 包)，而不是 JSR/Jakarta 标准的 `@Valid` 注解，因为 `@Valid` 不支持分组。

**示例：`UserController.java`**

```java
package com.example.controller;

import com.example.dto.UserDto;
import com.example.validation.groups.OnCreate;
import com.example.validation.groups.OnLogin;
import com.example.validation.groups.OnUpdate;
import jakarta.validation.groups.Default; // Spring Boot 3.x, 用于演示
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated; // Spring's annotation
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // 创建用户
    @PostMapping
    public ResponseEntity<?> createUser(@Validated(OnCreate.class) @RequestBody UserDto userDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }
        // 只有 OnCreate 组的校验规则会生效
        // userDto.setId(UUID.randomUUID().toString()); // 模拟ID生成
        // userService.create(userDto);
        return ResponseEntity.ok("User created successfully with ID: " + userDto.getId());
    }

    // 更新用户信息
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,
                                        @Validated(OnUpdate.class) @RequestBody UserDto userDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }
        // 只有 OnUpdate 组的校验规则会生效
        userDto.setId(id); // 通常从路径参数设置ID
        // userService.update(userDto);
        return ResponseEntity.ok("User updated successfully: " + userDto.getUsername());
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(OnLogin.class) @RequestBody UserDto userDto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }
        // 只有 OnLogin 组的校验规则会生效
        // authService.login(userDto.getUsername(), userDto.getPassword());
        return ResponseEntity.ok("User logged in: " + userDto.getUsername());
    }

    // 演示 Default 分组
    // 如果不指定分组，或者显式指定 Default.class，则只有属于 Default 组的校验生效
    @PostMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@Validated @RequestBody UserDto userDto, // 等同于 @Validated(Default.class)
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }
        // 此时只有 nickname 字段的 @NotEmpty 会生效，因为它是唯一属于 Default 组的
        // userService.updateProfile(userDto);
        return ResponseEntity.ok("User profile updated for nickname: " + userDto.getNickname());
    }

    // 辅助方法，构建错误响应
    private ResponseEntity<?> buildErrorResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
```

**行为解释**：

*   **`createUser`**: 使用 `@Validated(OnCreate.class)`，所以只有 `UserDto` 中 `groups` 包含 `OnCreate.class` 的校验注解会生效。例如，`id` 必须为 `null`，`username`, `password`, `email` 不能为空并满足各自的 `OnCreate` 规则。`nickname` (属于 `Default` 组) 的校验不会执行。
*   **`updateUser`**: 使用 `@Validated(OnUpdate.class)`，只有 `groups` 包含 `OnUpdate.class` 的校验注解会生效。例如，`id` 不能为空，`email` 如果提供则必须有效，`password` 如果提供则长度至少为 8。
*   **`loginUser`**: 使用 `@Validated(OnLogin.class)`，只有 `groups` 包含 `OnLogin.class` 的校验注解会生效。主要是 `username` 和 `password` 的非空校验。
*   **`updateUserProfile`**: 使用 `@Validated` (没有指定分组，等同于 `@Validated(Default.class)` 或 `@Validated({Default.class})`)，只有 `nickname` 字段的 `@NotEmpty` 校验会生效，因为其他字段的校验都指定了特定的分组，不属于 `Default` 组。

---

### 校验顺序和分组序列 (Validation Order and Group Sequences)

#### 校验顺序

默认情况下，**不同分组之间的校验是无序的**。如果一个属性同时属于多个被激活的分组，那么它的所有相关约束都会被评估。

#### 分组序列 (`@GroupSequence`)

有时你希望按特定顺序执行校验分组，并且如果前一个分组校验失败，则后续分组的校验不再执行。这时可以使用 `@GroupSequence`。

`@GroupSequence` 允许你定义一个有序的分组列表。校验会按照列表中的顺序进行，一旦某个分组出现校验错误，后续分组的校验就会被跳过。

1.  **定义一个分组序列接口**:
    ```java
    package com.example.validation.groups;

    import jakarta.validation.GroupSequence; // Spring Boot 3.x
    import jakarta.validation.groups.Default;
    // import javax.validation.GroupSequence; // Spring Boot 2.x
    // import javax.validation.groups.Default;

    /**
     * 定义一个有序的校验分组序列
     * 首先校验 Default 组，如果通过，再校验 OnCreate 组
     */
    @GroupSequence({Default.class, OnCreate.class}) // 顺序很重要
    public interface CreateUserSequence {}
    ```
    在这个例子中，`CreateUserSequence` 首先会触发所有属于 `Default` 分组的校验。如果 `Default` 组的所有校验都通过了，才会继续触发属于 `OnCreate` 分组的校验。

2.  **在 `@Validated` 中使用分组序列**:
    ```java
    @PostMapping("/create-ordered")
    public ResponseEntity<?> createUserWithOrderedValidation(
            @Validated(CreateUserSequence.class) @RequestBody UserDto userDto,
            BindingResult bindingResult) {
        // ...
    }
    ```
    现在，`userDto` 的校验会先执行 `Default` 组的规则 (例如 `nickname` 的 `@NotEmpty`)。如果 `nickname` 为空，校验失败，`OnCreate` 组的规则 (如 `username`, `password`, `email` 的校验) 就不会被执行。

**`@GroupSequence` 用在 Bean 类上**:
你也可以在 Bean 类本身上使用 `@GroupSequence` 来重新定义该 Bean 的默认校验顺序。
```java
@GroupSequence({BasicInfo.class, AdvancedInfo.class, UserDto.class}) // UserDto.class 代表 Default 分组
public class UserDto {
    // ...
}
// public interface BasicInfo {}
// public interface AdvancedInfo {}
```
当对这个 `UserDto` 进行默认校验 (即 `@Validated` 或 `@Valid` 不带参数) 时，会先校验 `BasicInfo` 组，然后是 `AdvancedInfo` 组，最后是 `Default` 组 (通过 `UserDto.class` 代表)。

---

### 级联校验与分组 (Cascaded Validation and Groups)

如果你的 DTO 包含其他需要校验的对象 (嵌套对象)，并且你在嵌套对象的字段上使用了 `@Valid` 或 `@Validated`，分组校验会如何传递呢？

*   当你在父对象上使用 `@Validated(SomeGroup.class)` 时，这个 `SomeGroup` 会传递给通过 `@Valid` (或 `@Validated`) 标记的嵌套对象进行校验。
*   这意味着嵌套对象中，只有那些也属于 `SomeGroup` (或者属于 `Default` 组，如果 `SomeGroup` 继承了 `Default`) 的校验规则才会生效。

**示例**:
```java
public class OrderDto {
    @NotEmpty(groups = OnCreate.class)
    private String orderNumber;

    @Valid // 没有指定分组，会传递父级的分组
    @NotNull(groups = OnCreate.class)
    private AddressDto shippingAddress;
    // ...
}

public class AddressDto {
    @NotEmpty(groups = OnCreate.class, message = "Street cannot be empty for new orders")
    @NotEmpty(groups = OnUpdate.class, message = "Street cannot be empty for address updates")
    private String street;

    @NotEmpty(message = "City cannot be empty") // Default group
    private String city;
    // ...
}

// Controller
@PostMapping("/orders")
public ResponseEntity<?> createOrder(@Validated(OnCreate.class) @RequestBody OrderDto orderDto, BindingResult br) {
    // ...
}
```
在 `createOrder` 方法中，由于使用了 `@Validated(OnCreate.class)`：
1.  `OrderDto` 的 `orderNumber` 会根据 `OnCreate` 组进行校验。
2.  `shippingAddress` 字段的 `@NotNull(groups = OnCreate.class)` 会生效。
3.  由于 `shippingAddress` 上有 `@Valid`，校验会级联到 `AddressDto` 对象。
4.  `OnCreate.class` 分组会传递给 `AddressDto` 的校验过程。
5.  因此，`AddressDto` 中的 `street` 字段的 `@NotEmpty(groups = OnCreate.class)` 规则会生效。
6.  `AddressDto` 中的 `city` 字段的 `@NotEmpty` (属于 `Default` 组) **不会** 生效，因为当前激活的分组是 `OnCreate`，而不是 `Default`。

如果你希望在级联校验时，嵌套对象使用其自身的 `Default` 分组，而父对象使用特定分组，这会比较复杂，可能需要自定义 `Validator` 行为或重新设计 DTO 结构。通常，分组会向下传播。

---

### 总结

*   **分组校验** 使用标记接口定义分组，通过注解的 `groups` 属性将校验规则分配给分组。
*   使用 Spring 的 `@Validated` 注解在 Controller 或 Service 层指定当前操作要激活的校验分组。
*   `Default` 分组是隐式的，适用于未指定 `groups` 的校验注解。
*   `@GroupSequence` 允许你定义有序的校验分组，实现短路校验。
*   分组会影响级联校验 (`@Valid`) 的行为，激活的分组会传递给嵌套对象。

分组校验是处理复杂业务场景下不同校验需求的强大工具，它使得校验逻辑更加清晰、声明化，并易于维护。记住，只有 `@Validated` 支持分组，标准的 `@Valid` 不支持。