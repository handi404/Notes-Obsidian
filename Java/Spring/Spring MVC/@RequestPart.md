探讨 `@RequestPart` 注解。这是一个在文件上传场景中非常强大且容易被误解的注解。

---

### @RequestPart：专为“混合体”请求设计的快递分拣员

#### 1. 是什么：一个能处理复杂包裹的专家

想象一个 HTTP 请求就是一个快递包裹。

*   有时包裹里只有一张纸条（`application/x-www-form-urlencoded`），`@RequestParam` 就能轻松读取。
*   有时包裹里是一件用特定模具塑封好的商品（`application/json`），`@RequestBody` 能完美地把它取出来并还原成对象。

但如果这个包裹很复杂，里面既有一本书（**文件**），又有一张详细的、结构化的贺卡（**JSON 数据**），怎么办？

这时候就需要 `@RequestPart` 这个专家了。它专门处理 `multipart/form-data` 这种“混合内容”的请求。`multipart/form-data` 请求将整个请求体分成了多个“部分”（Part），每个部分都可以有自己的 `Content-Type`。

**`@RequestPart` 的核心作用就是：精准地从 `multipart/form-data` 请求中取出某一个“部分”，并将其内容绑定到方法的参数上。它最强大的地方在于，如果这个部分的内容是 JSON 或 XML，它能自动调用消息转换器（MessageConverter）将其反序列化为 Java 对象。**

#### 2. 为什么需要 @RequestPart：填补 `@RequestParam` 和 `@RequestBody` 的“盲区”

很多开发者会困惑：“我用 `@RequestParam` 也能上传文件，为什么还需要 `@RequestPart`？”

我们来看一个典型的场景：**上传用户头像，并同时提交用户的详细信息（如昵称、邮箱等）。**

*   **如果用 `@RequestParam`**：
    ```java
    // 方案一：所有信息都用字符串接收
    @PostMapping("/upload")
    public void upload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("userId") String userId,
        @RequestParam("nickname") String nickname
    ) {
        // ...
    }
    ```
    这个方案在用户信息很简单时可行。但如果用户信息是一个复杂的 JSON 对象，比如包含嵌套结构，你只能接收一个 JSON 字符串，然后手动用 `ObjectMapper` 去解析，非常笨拙。

*   **如果用 `@RequestBody`**：
    你**不能**同时使用 `@RequestBody` 和 `@RequestParam` 来接收文件，因为 `@RequestBody` 默认会读取整个请求体，而 `multipart/form-data` 的格式和 `application/json` 完全不同。一个方法中也只能有一个 `@RequestBody`。

*   **`@RequestPart` 的完美解决方案**：
    `@RequestPart` 允许你将请求中的一个部分当作一个 DTO (Data Transfer Object) 来接收，同时将另一部分当作文件来接收。

    ```java
    // 完美方案：文件和结构化数据分离接收，自动绑定
    @PostMapping("/upload-profile")
    public ResponseEntity<String> uploadProfile(
        @RequestPart("profile-data") UserProfileDto profile, // Spring会自动将这个part的JSON字符串反序列化为UserProfileDto对象
        @RequestPart("avatar-file") MultipartFile avatarFile
    ) {
        // profile 对象已经被自动填充好了
        System.out.println("User Nickname: " + profile.getNickname());
        // avatarFile 也是一个可用的文件对象
        System.out.println("File Name: " + avatarFile.getOriginalFilename());
        // ... 处理业务逻辑
        return ResponseEntity.ok("Profile updated successfully!");
    }
    ```
    这就是 `@RequestPart` 的核心价值所在：**在处理 `multipart/form-data` 请求时，能够像 `@RequestBody` 一样对其中某个部分进行自动的类型转换和对象绑定。**

#### 3. 怎么用：核心用法与示例

下面是一个完整、可运行的例子。

**第一步：定义一个 DTO 用于接收 JSON 数据**
使用 Jakarta Validation API (通过 `spring-boot-starter-validation`) 进行数据校验。

```java
// UserProfileDto.java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserProfileDto {
    @NotBlank(message = "Nickname cannot be blank")
    private String nickname;

    @Email(message = "Invalid email format")
    private String email;

    // Getters and Setters
}
```

**第二步：创建 Controller**
注意 `consumes` 属性，明确指出这个端点只接收 `multipart/form-data` 类型的请求。

```java
// FileUploadController.java
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated // 使方法级别的参数校验生效
public class FileUploadController {

    /**
     * 经典场景：上传一个文件 + 一个复杂的JSON对象
     */
    @PostMapping(value = "/users/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserProfile(
            @Valid @RequestPart("profile") UserProfileDto profile,
            @RequestPart("avatar") MultipartFile avatar) {

        System.out.println("Received Profile: " + profile.getNickname() + ", " + profile.getEmail());
        System.out.println("Received Avatar: " + avatar.getOriginalFilename() + ", Size: " + avatar.getSize());
        // ... 业务逻辑 ...
        return ResponseEntity.ok("User profile and avatar updated.");
    }

    /**
     * 上传多个文件
     */
    @PostMapping(value = "/galleries/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhotos(
            @RequestPart("files") List<MultipartFile> files) {
        
        System.out.println("Received " + files.size() + " files.");
        files.forEach(file -> System.out.println(" - " + file.getOriginalFilename()));
        // ... 业务逻辑 ...
        return ResponseEntity.ok("Uploaded " + files.size() + " files.");
    }
}
```

**第三步：如何发送请求？（以 cURL 为例）**

这是关键！客户端必须正确地构造 `multipart/form-data` 请求，并为 JSON 部分指定 `Content-Type`。

```bash
curl -X POST http://localhost:8080/api/v1/users/profile \
-H "Content-Type: multipart/form-data" \
-F "profile={ \"nickname\": \"coderZoe\", \"email\": \"zoe@example.com\" };type=application/json" \
-F "avatar=@/path/to/your/avatar.jpg"
```
*   `-F` 表示一个表单部分。
*   `profile=...;type=application/json`：这是最精髓的地方。我们告诉服务器，名为 `profile` 的这个部分，它的内容是一个 JSON 字符串，并且它的 `Content-Type` 是 `application/json`。Spring MVC 看到这个 `Content-Type`，就会调用 `MappingJackson2HttpMessageConverter` 来反序列化它。
*   `avatar=@/path/to/your/avatar.jpg`：这是标准的 cURL 文件上传语法。

#### 4. 进阶应用与最佳实践

*   **校验**：如示例所示，在 `@RequestPart` 标注的 DTO 参数前加上 `@Valid` 注解，可以触发 JSR 303/380 校验。如果校验失败，会抛出 `MethodArgumentNotValidException`，可以被全局异常处理器捕获。

*   **文件大小限制**：在 `application.yml` 中配置上传限制，这是生产环境必备。
    ```yaml
    spring:
      servlet:
        multipart:
          max-file-size: 10MB   # 单个文件最大值
          max-request-size: 100MB # 整个请求最大值
    ```

*   **在 Reactive 栈 (WebFlux) 中的使用**：
    在 WebFlux 中，`@RequestPart` 的工作方式类似，但参数类型是响应式的。文件会被处理成 `FilePart`。
    ```java
    import org.springframework.http.codec.multipart.FilePart;
    
    @PostMapping("/upload-reactive")
    public Mono<Void> uploadReactive(
        @RequestPart("profile") Mono<UserProfileDto> profileMono, // DTO被包装在Mono中
        @RequestPart("file") Mono<FilePart> filePartMono // 文件是FilePart
    ) {
        return Mono.zip(profileMono, filePartMono)
            .flatMap(tuple -> {
                UserProfileDto profile = tuple.getT1();
                FilePart filePart = tuple.getT2();
                // ... 处理逻辑，注意 filePart.transferTo(path) 返回 Mono<Void>
                return Mono.empty();
            });
    }
    ```

*   **测试**：使用 Spring 的 `MockMvc` 测试 `@RequestPart` 控制器非常方便。
    ```java
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUpdateUserProfile() throws Exception {
        UserProfileDto profile = new UserProfileDto();
        profile.setNickname("test-user");
        profile.setEmail("test@test.com");
        
        // 创建JSON部分的MockMultipartFile
        MockMultipartFile jsonPart = new MockMultipartFile(
                "profile", 
                "", 
                "application/json", 
                objectMapper.writeValueAsBytes(profile)
        );

        // 创建文件部分的MockMultipartFile
        MockMultipartFile filePart = new MockMultipartFile(
                "avatar", 
                "avatar.png", 
                MediaType.IMAGE_PNG_VALUE, 
                "some-image-bytes".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/users/profile")
                        .file(jsonPart)
                        .file(filePart))
                .andExpect(status().isOk())
                .andExpect(content().string("User profile and avatar updated."));
    }
    ```

### 总结：选择合适的工具

| 注解                  | 请求 `Content-Type`                                            | 主要用途                                                               | 示例                                        |
| :------------------ | :----------------------------------------------------------- | :----------------------------------------------------------------- | :---------------------------------------- |
| **`@RequestParam`** | `application/x-www-form-urlencoded`<br>`multipart/form-data` | 获取简单的键值对参数（字符串、数字等），或单个文件。                                         | `?name=Tom`<br>表单中的 `<input type="text">` |
| **`@RequestBody`**  | `application/json`<br>`application/xml`                      | 将整个请求体反序列化为一个完整的 Java 对象。                                          | `{"name": "Tom", "age": 25}`              |
| **`@RequestPart`**  | `multipart/form-data`                                        | **处理混合请求**。精准获取其中一个部分，并能像 `@RequestBody` 一样将 JSON/XML 部分自动反序列化为对象。 | 上传文件 + 相关的元数据(JSON)                       |
