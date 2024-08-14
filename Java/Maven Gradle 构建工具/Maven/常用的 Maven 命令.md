Maven 是一个项目管理和构建工具，可以通过命令行执行各种操作。以下是一些常用的 Maven 命令：

1. **构建项目**：
   ```bash
   mvn clean install
   ```
   - `clean`: 删除 `target` 目录中的所有文件，以确保构建从干净的状态开始。
   - `install`: 编译项目、运行测试、打包并将其安装到本地 Maven 仓库中。

2. **编译项目**：
   ```bash
   mvn compile
   ```
   - 编译 `src/main/java` 下的所有 Java 源文件。

3. **运行测试**：
   ```bash
   mvn test
   ```
   - 编译 `src/test/java` 下的测试源文件并运行测试。

4. **打包项目**：
   ```bash
   mvn package
   ```
   - 编译代码并将其打包成 JAR 或 WAR 文件。

5. **跳过测试进行打包**：
   ```bash
   mvn package -DskipTests
   ```
   - 打包项目并跳过测试。

6. **生成项目骨架**：
   ```bash
   mvn archetype:generate
   ```
   - 通过 Maven 原型生成一个新的 Maven 项目骨架。

7. **查看依赖关系树**：
   ```bash
   mvn dependency:tree
   ```
   - 显示项目的依赖关系树，有助于理解项目依赖的层次结构。

8. **清理项目**：
   ```bash
   mvn clean
   ```
   - 删除 `target` 目录中的构建结果。

9. **执行单个目标**：
   ```bash
   mvn [plugin-name]:[goal]
   ```
   - 执行指定的插件目标。例如，运行 `maven-compiler-plugin` 的 `compile` 目标：
     ```bash
     mvn compiler:compile
     ```

这些命令可以在项目的根目录中运行。如果你使用的是多模块项目，可以在父模块中运行这些命令，它们将应用到所有子模块。