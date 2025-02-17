以下是如何使用 Gradle 创建新项目的详细步骤及解释：

---

### 一、安装 Gradle (如果未安装)
1. **下载 SDKMAN (推荐)**
   ```bash
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   ```
   - **作用**：SDKMAN 是管理多个 SDK 的工具，方便安装和切换 Gradle 版本。

2. **安装 Gradle**
   ```bash
   sdk install gradle 8.5
   ```
   - **参数解释**：`8.5` 是版本号，可替换为最新版本。
   - **验证安装**：运行 `gradle -v` 查看版本信息。

---

### 二、创建新项目
1. **初始化项目**
   ```bash
   mkdir my-project && cd my-project
   gradle init
   ```
   - **命令触发交互式配置**，需按提示选择选项。

2. **交互式选项详解**
   - **1. Select type of project to generate**
     ```
     1: basic                 - 基础空项目
     2: application           - 应用程序（含 main 方法）
     3: library               - 库项目（供其他项目依赖）
     4: Gradle plugin         - Gradle 插件项目
     ```
     - **推荐选择 `2: application`**（适合大多数 Java/Kotlin 应用）。

   - **2. Select implementation language**
     ```
     1: Java
     2: Kotlin
     3: Groovy
     4: Scala
     5: C++
     6: Swift
     ```
     - **选择语言**：例如 Java 或 Kotlin，Gradle 会生成对应语言的模板。

   - **3. Split functionality across multiple subprojects?**
     ```
     1: no  - 单模块项目
     2: yes - 多模块项目
     ```
     - **初学者选 `1: no`**，复杂项目可选多模块。

   - **4. Select build script DSL**
     ```
     1: Groovy - 传统 DSL，语法灵活
     2: Kotlin - 类型安全，IDE 支持更好
     ```
     - **推荐 Kotlin**（长期趋势），但 Groovy 更成熟。

   - **5. Select test framework**
     ```
     1: JUnit 4
     2: TestNG
     3: Spock
     4: JUnit Jupiter (JUnit 5)
     ```
     - **推荐 JUnit 5**（现代测试框架）。

   - **6. Project name (默认: my-project)**
     - 直接回车使用默认名称。

   - **7. Source package (默认: org.example)**
     - 输入你的包名，例如 `com.yourcompany`.

---

### 三、生成的项目结构解析
```
my-project/
├── gradle/           # Gradle Wrapper 配置
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/     # Java 源码
│   │   └── resources # 资源文件
│   └── test/
│       ├── java/     # 测试代码
│       └── resources
├── build.gradle.kts  # 构建脚本（Kotlin DSL）
├── settings.gradle.kts # 项目设置（模块定义）
└── gradlew           # Gradle Wrapper 脚本（Linux/Mac）
```

---

### 四、关键文件详解
1. **`build.gradle.kts` (Kotlin DSL 版本)**
   ```kotlin
   plugins {
       id("application")          // 应用类型插件
       id("java")                 // Java 支持
   }
   
   application {
       mainClass = "com.yourcompany.App" // 主类入口
   }
   
   repositories {
       mavenCentral() // 使用 Maven 中央仓库
   }
   
   dependencies {
       testImplementation("org.junit.jupiter:junit-jupiter:5.9.2") // 测试依赖
   }
   
   tasks.test {
       useJUnitPlatform() // 使用 JUnit 5
   }
   ```

2. **`settings.gradle.kts`**
   ```kotlin
   rootProject.name = "my-project" // 根项目名称
   // 包含子模块示例：include("app", "lib")
   ```

3. **Gradle Wrapper (`gradlew`, `gradlew.bat`)**
   - **作用**：无需全局安装 Gradle，通过 Wrapper 使用指定版本。
   - **生成命令**：`gradle wrapper --gradle-version 8.5`

---

### 五、构建与运行
4. **编译并运行**
   ```bash
   ./gradlew run   # 执行 main 方法
   ```

5. **构建 JAR 包**
   ```bash
   ./gradlew build
   ```
   - 生成的 JAR 位于 `build/libs/`。

6. **运行测试**
   ```bash
   ./gradlew test
   ```

---

### 六、添加依赖示例
7. **修改 `build.gradle.kts`**
   ```kotlin
   dependencies {
       implementation("com.google.guava:guava:32.1.3-jre") // 添加 Guava 库
       testImplementation("org.mockito:mockito-core:5.8.0") // 添加 Mockito
   }
   ```
8. **重新加载项目**
   - IDE 中点击 "Sync" 或运行 `./gradlew --refresh-dependencies`.

---

### 七、扩展配置
9. **指定 Java 版本**
   ```kotlin
   java {
       toolchain {
           languageVersion = JavaLanguageVersion.of(17)
       }
   }
   ```

10. **多模块项目**
   - 在 `settings.gradle.kts` 中添加 `include("module1", "module2")`.
   - 每个子模块需有自己的 `build.gradle.kts`.

---

### 八、常用命令速查
| 命令                   | 作用                      |
|------------------------|-------------------------|
| `./gradlew tasks`      | 查看所有可用任务            |
| `./gradlew clean`      | 清理构建目录               |
| `./gradlew dependencies` | 查看依赖树                |
| `./gradlew assemble`   | 编译代码并打包（不运行测试） |

---

通过以上步骤，你已创建一个完整的 Gradle 项目，并可根据需求扩展配置。