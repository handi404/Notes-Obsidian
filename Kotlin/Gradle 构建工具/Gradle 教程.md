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
我的项目产品是一个 web 应用程序，所以有一个 my-webapp 应用程序文件夹，其中可能有一些 javascript 和一些 html 模板。还有一个不同的模块，my-backend，其中可能有一些与 sql 数据库相关的东西，无论怎样我们将项目分成两个模块，我们可以有更多的模块，可以只有一个大模块。无论如何在这个中使用两个模块，那么**在 settigns.gradle 文件中，你需要告诉 gradle 每个模块所引用的文件夹。**
然后当你输入像gradle w build 这样的命令时，实际发生的是 gradle 查看了 settigns.gradle 文件，检查出我有几个模块，需要弄清楚必须先构建哪一个或者是否可以并行构建它们，然后它就会沿着每个模块构建进行编译，一些技术会编译源代码运行一些测试。
### Gradle 项目结构解析
了解一般的 gradle 项目结构。
进入 my-webapp 文件夹，可以看到有一个 `build` 子文件夹，一个 `build.gradle` 文件，
后面来剖析 build.gradle 文件中的内容。
现在关注 `src` 文件夹，因为 **gradle 本质上与 maven 共享相同的项目结构约定**，所以所有的 java 源代码和所有的 kotlin 源代码，无论有什么，它们都会进入源 `main` 文件夹，你会在那里找到你的包。
还有 main 中的 `resources`，在主资源中，你会发现一些东西，比如你的 `application.properties(应用程序属性)`文件 sql 文件等等。
因为我们是可靠的开发人员，还有源 `test`，所有的测试类都进入了源测试。
现在实际发生的是，当运行 gradle build 时，它会大大地通过源 main，编译所有，然后进入源 test，编译所有并运行测试。最后运行测试，创建测试报告，然后构建整个项目，构建一个最终的可执行文件，我可以把它放在某个地方。要找到所有这些东西，需要进入 `build` 子文件夹，看到有一个 `classes` 子文件夹，里面有你所有编译过的 java 类；有 `reports(报告)`、`test-results(测试结果`)；**最重要的是，有一个 `libs` 目录，默认情况下，这是最终可执行文件放在这里**。
在 libs 文件夹中有一个 jar 文件，如果你知道 springboot 是如何工作的，你就知道你可以**通过 `java -jar 文件名.jar` 来运行这个 jar 文件**。
### Gradle Clean 
回到项目根文件，来执行几个流行的 gradle 命令，例如：`gradle clean`
**功能**：清理项目中所有由 Gradle 生成的构建文件（通常是 `build/` 目录）。
### Gradle Test 
`gradle test`
- **功能**：运行所有测试用例。
- **附加选项**：
    - `--tests "TestClassName.methodName"`：运行指定的测试类或方法。
    - 示例：`gradle test --tests "com.example.MyTest.myMethod"`
找到这些文件并执行它们，运行它们，并生成 reports，实际上也为这些测试生成报告。你可以进入 `build\reports\tests\test`，在这里你会发现一个 index.html 文件，在浏览器打开会看到一个漂亮的测试报告。

回到根目录，有一问题是我是否总是必须构建我的所有子模块，我是否总是必须测试我的所有子模块，不，你也可以选择只测试一个特定的：
`gradle test -p 模块文件名`，-p 意为 project。
### Gradle 的智能功能
Grady 很聪明，他明白，若你自上次测试运行以来没有做任何事情，没有创建任何新类，没有创建任何新测试，所以实际上也没有运行任何测试，因为测试结果仍然被缓存了，这也是 gradle 的魔力，需要运行什么，真正需要构建什么，怎样才能在构建时节省时间。
### Build.Gradle 解释
[[build.gradle]]
在 IDE 中打开项目。
现在是时候进入 build.gradle 文件了，就是配置子模块或 gradle 项目的地方。
了解这些不同的块，但现在从最重要的依赖部分 `dependencies` 开始：
```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation project(':my-backend')
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```
显然可以看到引入的第三方依赖项。
### Dependency Scopes 依赖范围
对于依赖中的 implementation 和 testImplementation 是什么意思？它是依赖范围：
- `implementation`：主代码使用的依赖，编译期和运行期都需要。
- `testImplementation`：测试代码所需的依赖，仅在测试时可用。
- `compileOnly`：仅编译时可用，运行时不需要。
- `runtimeOnly`：仅运行时需要，编译时不需要。
### Adding Dependencies 添加依赖项
假设我们想要将流行的 google guava 库添加到我们的项目中，转到 guava 的 github 页面，你会在某处找到依赖项行，即 gradle 依赖项行，因此你可以将其复制并粘贴到 build.gradle 文件中。
### Package Search 软件包搜索
更高级一些，你可以使用[包搜索](https://mvnrepository.com/)机器，就像 jetbrains 包搜索一样，粘贴依赖，刷新。
一切都按预期工作现在我可以有这样的想法说，my-webapp 应用程序项目对 my-backend 有依赖 my-backend 对 guava 有依赖这意味着传递思维：
```groovy
implementation project(':my-backend')
```
但当在 my-webapp 中测试使用 guava 中的类，却收到此包不存在。为什么呢？这与范围有关。
### Implementation Scope 实施范围
将依赖项范围设置为 implementation，则意味着模块可以使用依赖项，你可以编译模块，但它不是公共 API 的一部分，这是一种非常复杂的说法。
现在，即使 my-webapp 应用程序包含 my-backend，它也不会看到 guava 依赖项，如果想更改它，顺便说一下，最简单的方法是在 my-webapp 模块添加 guava 依赖，但是如果你想更改它，需要 api scope。
### API Scope API 范围
将范围从 implementation 改为 `api`，这样就会出现另一个问题，即 API 范围，如果尝试重新加载我的项目，会收到一条新的错误消息，说它实际上不知道 api 是什么意思，无法找到用于依赖项块参数的方法 api，这将我们需要用到 `plugins`。
### Plugins
`plugins`，这意味着默认情况下 gradle 脚本本身不能做很多事情，你必须告诉他们需要应用的插件。例如，通过将 java 插件应用到项目中：
```groovy
plugins {
	id 'org.springframework.boot' version '2.6.3'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
}
```
java 插件的工作就是源 main java、源 test java、运行所有的 java 类等等。问题是这里的 java 插件不理解 api 范围，为此你需要另一个插件，即 `java-library` 插件。
### Repositories
问题是依赖项来自哪里，它们来自存储库，因此配置存储库：
```groovy
repositories {
	mavenCentral()
}
```
maven 中央存储库下载依赖项或使用我的 maven 本地存储库使用 google 存储库：
```groovy
repositories {
	mavenCentral()
	mavenLocal()
	google()
}
```
### Modifying Gradle Tasks 修改 Gradle 任务
可以配置 gradle 特定的任务，例如运行的 gradle 测试任务，并告诉 gradle 使用 junit 运行这些测试：
```groovy
test {
	useJUnitPlatform()
}
```
### Common Gradle Issues 常见 Gradle 问题
然后还有其他东西，例如 group(组) version(版本) sourceCompatibility(源兼容性)，
### Creating New Gradle Projects 创建新的 Gradle 项目
从头开始创建一个全新的 gradle 项目，而无需从某个已经启用了 maven 包装的地方克隆它，要做到这一点，你需要在你的机器上安装 gradle，然后你可以转到机器上的任何目录，只需调用 `gradle init` 命令。
### Gradle Init
[[gradle init]]
创建一个新的子文件夹 mygradleproject，进入此文件夹，运行 `gradle init`，很可能是使用第二个 application，这是一个有多个子模块的应用程序，然后使用哪个语言，然后选择多模块开发(yes: Application and library project)，然后询问使用哪种 DSL（领域特定语言）来编写构建脚本，建议使用 Kotlin，尽管外面有无数个项目，特别是你的工作场所很可能在 Groovy 中，然后使用新的 API 和行为生成 build(Generate build using new APIs and behavior)。