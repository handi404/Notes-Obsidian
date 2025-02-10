从使用 Gradle wrapper(包装器)，将 Gradle 与 IDE 一起使用，到 Gradle 基础知识如 build.gradle 和 settings.gradle，以及运行命令（清理构建）以及理解多项目 Gradle 构建——到那时，关于 Gradle 的问题将所剩无几。
### Intro
[[Kotlin/Gradle 构建工具/详解|详解]]
### 克隆示例项目
https://github.com/marcobehlerjetbrains/gradle-tutorial.git
### Gradlew Build - First Try
在不知道 gradle 任何其他内容的情况下，尝试输入 `gradle build`，可以看到一些关于编译 java 源代码的测试，然后构建。但问题是，**为什么构建成功，发生了什么**，首先找出那个命令 gradlew 是什么，正如项目中看到的，有**两个文件 gradlew gradlew.bat**。
### Gradle Wrapper
gradlew 代表 gradle wrapper，它是 gradle 的嵌入版本，这意味着你不必在你的机器上安装很多东西，而实际上任何项目，任何分级项目都带有自己的 gradle 版本，称为gradle wrapper。
gradlew 是你在 windows 上执行的命令，不带 .bat 的 gradlew 是在 linux 和 macos 上执行的命令。
然后有一个 gradle 目录，进入 gradle 目录，发现还有另一个子目录，即 wrapper。进入 wrapper 可以看到 gradle-wrapper.jar 文件。现在可以忽略该 gradle 目录。
### settings.gradle
然后有一个 settings.gradle 文件，实际上，每个 gradle 项目的根目录中只有一个 settings.gradle 文件，这是一个很好的指标。在终端输入 `type settings.gradle` 进行查看，若是 LinuxorMacOS 使用 `cat settigns.gradle`，看到该文件中只有三行：
```
rootProject.name = 'my-multi-module-app'

include 'my-backend'
include 'my-webapp'
```
首行，指定根项目名称，没什么意义。但可以看到两行的 `include`，它是什么？首先知道简单的 gradle 示例项目，但忘记吧，让我们来创建一个真实的 **multi-module project(多模块项目)**
### Gradle Multi-Projects

### Gradle 项目结构解析
### Gradlew Clean 
### Gradlew Test 
### Gradle 的智能功能
### Build.Gradle 解释 
### Dependency Scopes 依赖范围
### Adding Dependencies 添加依赖项
### Package Search 软件包搜索
### Implementation Scope 实施范围
### API Scope API 范围
### Plugins 
### Repositories
### Modifying Gradle Tasks 修改 Gradle 任务
### Common Gradle Issues 常见 Gradle 问题
### Creating New Gradle Projects 创建新的 Gradle 项目
### Gradle Init
