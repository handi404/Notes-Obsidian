Maven 是一个用于 Java 项目管理和构建的工具。它可以自动处理项目的依赖项、编译代码、打包应用程序、运行测试等。以下是 Maven 的基本使用方法：

### 1. **安装 Maven**

#### **在 Windows 上安装 Maven**
1. **下载 Maven**：
   - 访问 [Maven官网](https://maven.apache.org/download.cgi)，下载最新版本的 Maven 压缩包（通常是 `.zip` 文件）。

2. **解压 Maven**：
   - 解压下载的 Maven 压缩包到你选择的目录，比如 `C:\Program Files\Apache\maven`。

3. **配置环境变量**：
   - 打开 `控制面板` -> `系统和安全` -> `系统` -> `高级系统设置` -> `环境变量`。
   - 在 `系统变量` 中找到 `Path`，点击 `编辑`，然后在末尾添加 Maven 的 `bin` 目录路径，比如 `C:\Program Files\Apache\maven\bin`。
   - 新建一个 `MAVEN_HOME` 环境变量，值为 Maven 的解压目录（不包括 `bin`），比如 `C:\Program Files\Apache\maven`。

4. **验证安装**：
   - 打开命令提示符（`cmd`），输入 `mvn -v`。如果显示 Maven 版本信息，说明安装成功。

#### **在 macOS/Linux 上安装 Maven**
1. **使用 Homebrew（macOS）**：
   - 运行以下命令：
     ```bash
     brew install maven
     ```

2. **手动下载和安装**：
   - 下载 Maven 的 tar. gz 文件，并将其解压到你选择的目录。
   - 将 Maven 的 `bin` 目录路径添加到 `.bash_profile` 或 `.zshrc` 文件中的 `PATH` 变量：
     ```bash
     export PATH=/path/to/maven/bin:$PATH
     ```
   - 运行 `source ~/.bash_profile` 或 `source ~/.zshrc` 以应用更改。

3. **验证安装**：
   - 运行 `mvn -v`，如果显示 Maven 版本信息，说明安装成功。

### 2. **创建 Maven 项目**

#### **使用命令行创建 Maven 项目**
1. 打开命令行或终端，导航到你想要创建项目的目录。
2. 运行以下命令来创建一个新的 Maven 项目：
   ```bash
   mvn archetype:generate -DgroupId=com.example -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
   ```
   - `groupId` 是项目的组织或公司名称，通常是域名的反转形式。
   - `artifactId` 是项目的名称。
   - `maven-archetype-quickstart` 是一个简单的 Java 项目模板。

3. Maven 将会生成一个包含基本项目结构的目录。

### 3. **项目结构**

Maven 生成的项目通常具有以下结构：

```
my-app/
├── pom.xml               # Maven项目的配置文件
├── src/
│   ├── main/
│   │   └── java/         # 源代码目录
│   └── test/
│       └── java/         # 测试代码目录
└── target/               # 编译后的文件将会放在这里
```

### 4. **添加依赖项**

Maven 使用 `pom.xml` 文件来管理项目的依赖项、插件和其他配置。

#### **在 `pom.xml` 中添加依赖项**
1. 打开 `pom.xml` 文件。
2. 在 `<dependencies>` 标签内添加所需的依赖项。例如，要添加 MongoDB Java 驱动，你可以添加以下内容：

```xml
<dependencies>
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongodb-driver-sync</artifactId>
        <version>4.10.2</version>
    </dependency>
    <!-- 其他依赖项 -->
</dependencies>
```

3. 保存 `pom.xml`，Maven 会自动下载并管理这些依赖项。

### 5. **编译和运行项目**

- **编译项目**：
  ```bash
  mvn compile
  ```
  Maven 将会编译 `src/main/java` 中的代码，并将输出放在 `target/classes` 目录中。

- **运行项目**（假设你有一个主类）：
  ```bash
  mvn exec:java -Dexec.mainClass="com.example.App"
  ```
  这将运行指定的主类。

- **打包项目**：
  ```bash
  mvn package
  ```
  这将编译代码并将其打包成一个 `jar` 文件，放在 `target/` 目录中。

- **运行测试**：
  ```bash
  mvn test
  ```
  Maven 将会运行 `src/test/java` 中的所有测试。

### 6. **使用插件和构建生命周期**

Maven 有许多插件，用于执行不同的任务，如编译、测试、打包、部署等。你可以通过修改 `pom.xml` 文件中的 `<build>` 和 `<plugins>` 标签来自定义这些操作。

例如：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 7. **常用 Maven 命令**

- **清理项目**：删除 `target` 目录中的所有生成文件。
  ```bash
  mvn clean
  ```
- **安装依赖项**：下载 `pom.xml` 中列出的所有依赖项。
  ```bash
  mvn install
  ```
- **查看项目依赖项树**：
  ```bash
  mvn dependency:tree
  ```

通过掌握这些基本操作，你可以高效地使用 Maven 来管理和构建 Java 项目。

Maven 项目的目录结构是标准化的，遵循了一定的惯例，以便开发人员和构建工具可以轻松理解和管理项目。以下是 Maven 项目的标准目录结构：

```
my-app
|-- pom.xml
|-- src
    |-- main
    |   |-- java
    |   |   `-- com
    |   |       `-- example
    |   |           `-- App.java
    |   |-- resources
    |   |   `-- application.properties
    |   |-- filters
    |   |-- webapp
    |       `-- WEB-INF
    |           `-- web.xml
    |-- test
        |-- java
        |   `-- com
        |       `-- example
        |           `-- AppTest.java
        |-- resources
        |   `-- test-configuration.properties
        |-- filters
```

### 目录结构说明

- **`my-app`**: 项目的根目录，通常与项目的 Artifact ID 一致。

- **`pom.xml`**: Maven 的配置文件，包含项目的基本信息、依赖、构建配置、插件等。Maven 根据这个文件来构建项目。

- **`src`**: 源代码的根目录，包含所有的源代码文件和资源文件。

  - **`main`**: 主代码和资源所在的目录。
    - **`java`**: 存放项目的主代码，按包名分层（如 `com.example`）。
    - **`resources`**: 存放项目的资源文件，如配置文件、静态资源等，这些文件会被打包到最终的构建产物中。
    - **`filters`**: 存放资源过滤文件，用于替换资源中的占位符。
    - **`webapp`**: 如果是 Web 应用，这里存放 Web 应用的相关资源，如 HTML、JSP 文件，及其 WEB-INF 目录中的配置文件（如 `web.xml`）。

  - **`test`**: 测试代码和资源所在的目录。
    - **`java`**: 存放测试代码，按包名分层（如 `com.example`）。
    - **`resources`**: 存放测试资源文件，类似于 `main/resources`。
    - **`filters`**: 存放测试资源过滤文件。

### 其他常见目录

- **`target`**: 编译和打包的输出目录。Maven 会将生成的字节码、JAR 文件、WAR 文件等放在这里。这个目录通常不放入版本控制系统中（如 Git），因为它是 Maven 构建时自动生成的。

### 目录结构的优势

Maven 的目录结构遵循了“约定优于配置”（Convention over Configuration）的理念，减少了开发人员的配置工作量。通过这种标准化的目录结构，Maven 可以自动识别源代码、资源、测试等，从而简化了构建过程。

### 自定义目录结构

尽管 Maven 有标准的目录结构，但你可以在 `pom.xml` 文件中进行配置，以支持自定义的目录结构。例如，你可以指定不同的源码路径或资源路径。不过，在可能的情况下，最好遵循 Maven 的标准结构，以提高项目的可读性和可维护性。