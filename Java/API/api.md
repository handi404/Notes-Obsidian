在 Java 中编写 API 通常使用 Spring Boot，它是一个开源的 Java 框架，用于简化 Spring 应用程序的创建和开发过程。Spring Boot 提供了一种快速构建生产级 Spring 应用程序的方式，特别适合创建 RESTful API 服务。

以下是一个使用 Spring Boot 编写简单 RESTful API 的示例。

### 步骤 1: 设置开发环境

确保你已经安装了以下工具：
- JDK 8 或更高版本
- Maven 或 Gradle
- 一个 IDE（如 IntelliJ IDEA 或 Eclipse）

### 步骤 2: 创建 Spring Boot 项目

你可以使用 Spring Initializr 来生成一个 Spring Boot 项目。访问 [Spring Initializr](https://start.spring.io/) 并选择以下选项：
- Project: Maven Project
- Language: Java
- Spring Boot: 2.7. X 或 3. X
- Project Metadata: 填写 `Group`, `Artifact`, 和 `Name`
- Dependencies: 添加 `Spring Web`

点击“Generate”按钮下载项目并解压到你的工作目录中。

### 步骤 3: 编写 API 代码

打开解压后的项目，找到并打开 `src/main/java/com/example/demo/DemoApplication.java`（假设你将 `Artifact` 命名为 `demo`）。

#### 创建一个控制器类

在 `src/main/java/com/example/demo` 目录下创建一个新的 Java 类，命名为 `HelloWorldController.java`，并添加以下代码：

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, World!";
    }
}
```

#### 更新主类

确保你的主类 `DemoApplication.java` 看起来像这样：

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 步骤 4: 运行应用程序

在终端中导航到项目目录，然后运行以下命令启动应用程序：

```bash
./mvnw spring-boot:run
```

如果你使用的是 Gradle，则运行：

```bash
./gradlew bootRun
```

### 步骤 5: 测试 API 服务

打开浏览器或使用 Postman 等工具访问 `http://localhost:8080/api/hello`，你应该会看到以下响应：

```text
Hello, World!
```

### 完整的项目结构

你的项目结构应该类似于以下内容：

```
demo
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── DemoApplication.java
│   │   │               └── HelloWorldController.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── demo
│                       └── DemoApplicationTests.java
└── pom.xml 或 build.gradle
```

### 总结

这是一个使用 Spring Boot 创建简单 RESTful API 的基本示例。Spring Boot 大大简化了 Spring 应用程序的开发，提供了丰富的功能和灵活性。你可以扩展这个示例，添加更多的控制器、服务和存储层，以构建复杂的 API 应用程序。