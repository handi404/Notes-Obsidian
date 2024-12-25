可是在idea中用getServletContext().getRealPath()无法获得程序的根路径，他会指向target里的根路径

您提到在 IDEA 中使用 `getServletContext().getRealPath("")` 方法时，它指向了 `target` 目录下的路径。这是因为 `getRealPath` 方法返回的是 Web 应用程序在服务器上的实际物理路径，在开发环境中，IDEA 通常会将项目编译并部署到 `target` 或类似的构建输出目录中。

对于这种情况，有几种解决方案：

### 1. 使用相对路径或绝对路径

可以考虑直接指定一个相对于当前工作目录的路径，或者是系统中的一个绝对路径来存放上传文件。不过，这种方法不太灵活，尤其是在不同环境中部署时可能会遇到问题。

```java
// 使用相对路径，假设 uploads 文件夹位于项目的根目录下
String uploadDir = "uploads";

// 或者使用绝对路径
String absoluteUploadPath = "/var/www/uploads";
```

### 2. 配置外部资源路径

更推荐的方式是通过配置文件（如 `application.properties` 或 `application.yml`）来定义上传路径，并确保这个路径是一个绝对路径或者相对于某个固定点的位置，比如用户的主目录、临时文件夹等。

**application.properties:**
```properties
upload.path=/path/to/your/upload/directory
```

然后在代码中读取这个配置项：

```java
@Autowired
private Environment env;

public String getUploadPath() {
    return env.getProperty("upload.path");
}
```

### 3. 使用环境变量或系统属性

你可以设置环境变量或系统属性来指定上传路径，这样可以在不同的环境中轻松更改路径而不必修改代码。

```java
String uploadPath = System.getenv("UPLOAD_PATH"); // 环境变量
// 或者
String uploadPath = System.getProperty("upload.path"); // 系统属性
```

### 4. 动态计算路径

如果希望保留使用 `getRealPath` 的方式，但又不想依赖于 `target` 目录，可以尝试动态计算路径。例如，可以通过获取当前应用程序的根路径，并结合项目结构来确定上传文件夹的位置。

```java
import java.nio.file.Path;
import java.nio.file.Paths;

// 获取当前应用的根路径
Path currentRelativePath = Paths.get("");
String s = currentRelativePath.toAbsolutePath().toString();
System.out.println("Current relative path is: " + s);

// 计算上传文件夹的路径
String uploadFilePath = s + File.separator + "uploads";
```

### 5. 使用自定义配置类

如果你正在使用 Spring 框架，还可以创建一个自定义配置类来管理上传文件夹的位置。

```java
@Configuration
public class UploadConfig {

    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public Path uploadLocation() {
        return Paths.get(uploadPath).toAbsolutePath().normalize();
    }
}
```

然后在你的控制器或其他组件中注入这个 Bean 来获取上传位置。

```java
@Autowired
private Path uploadLocation;
```

### 推荐方案

鉴于 `getRealPath` 在开发环境中指向 `target` 目录的问题，我最推荐使用 **配置文件** 或 **环境变量** 的方法。这两种方法提供了最大的灵活性和可移植性，使得你可以在不同的环境中轻松调整上传路径，而无需更改代码。同时，它们也避免了直接依赖于特定的文件系统结构，这在多服务器部署或云环境中尤为重要。

此外，确保上传路径的安全性和适当的访问控制，以防止潜在的安全风险。