探讨一下 **Java 平台模块系统 (Java Platform Module System, JPMS)**，这通常被简称为**模块化 (Modularity)**。这是自 Java 9 引入的一项重大变革，旨在解决长期以来 Java 生态系统面临的一些核心问题。

**一、什么是模块化系统 (JPMS)？**

简单来说，JPMS 允许你将大型应用程序或库分解成更小、职责更明确的**模块 (Modules)**。每个模块都有一个清晰的边界，并显式声明它依赖哪些其他模块以及它向其他模块暴露哪些功能 (包)。

**核心概念**：

1.  **模块 (Module)**：
    *   一个命名的、自描述的代码和数据集合。
    *   通常，一个模块对应一个或多个包。
    *   它的核心是一个**模块描述符 (Module Descriptor)** 文件：`module-info.java`。

2.  **模块描述符 (`module-info.java`)**：
    *   位于模块的根目录下，与 Java 源代码一起编译。
    *   定义模块的名称。
    *   声明模块的依赖关系 (`requires`).
    *   声明模块导出的包 (`exports`)，即哪些包对其他模块可见。
    *   还可以声明其他元数据，如使用的服务 (`uses`) 或提供的服务 (`provides`).

    ```java
    // 示例：com.example.app/module-info.java
    module com.example.app {
        // 依赖其他模块
        requires java.sql; // 依赖JDK的java.sql模块
        requires com.example.utils; // 依赖我们自己定义的com.example.utils模块

        // 导出包给其他模块使用
        exports com.example.app.api;
        // exports com.example.app.internal; // 如果不导出，这个包对其他模块不可见

        // 使用服务
        uses com.example.app.spi.PluginService;

        // 提供服务实现
        provides com.example.app.spi.PluginService with com.example.app.internal.DefaultPluginImpl;
    }
    ```

**二、为什么需要模块化系统？**

在 JPMS 出现之前，Java 主要依赖 **JAR 文件和类路径 (Classpath)** 来组织代码。这种方式存在几个显著问题，统称为“类路径地狱 (Classpath Hell)”或“JAR 地狱 (JAR Hell)”：

1.  **弱封装 (Weak Encapsulation)**：
    *   JAR文件中的所有 `public` 类对类路径上的任何其他代码都是可见的，即使它们是内部实现细节。这使得库的维护者很难在不破坏向后兼容性的情况下修改内部实现。
    *   **JPMS解决方案**：模块必须显式 `exports` 包，才能让其他模块访问。未导出的包是模块私有的，即使其中的类是 `public` 的，其他模块也无法访问。这提供了**强封装**。

2.  **不可靠的配置 (Unreliable Configuration)**：
    *   类路径上的JAR文件在运行时才被检查。如果缺少依赖的JAR，或者存在版本冲突（例如，两个不同版本的同一个库），应用程序可能会在运行时崩溃 (`NoClassDefFoundError`, `NoSuchMethodError` 等）。
    *   **JPMS 解决方案**：模块依赖在启动时（甚至编译时）通过模块图进行解析和验证。如果依赖缺失或冲突，应用程序会快速失败，而不是在运行时意外出错。这叫**可靠配置**。

3.  **巨大的 JDK/JRE**：
    *   传统的 JRE 包含了 Java 平台的所有标准库，即使你的应用程序只用到了其中一小部分。
    *   **JPMS解决方案**：JDK本身也被模块化了（如 `java.base`, `java.sql`, `java.xml` 等）。使用 `jlink` 工具，开发者可以创建只包含应用程序所需模块的自定义运行时镜像，从而大大减小部署包的大小。这对于微服务和小型设备非常有用。

4.  **命名冲突**：
    *   如果类路径上有两个 JAR 包含相同全限定名的类，JVM 会加载哪个是不确定的（通常是类路径上先出现的那个），这可能导致难以调试的问题。
    *   **JPMS 解决方案**：每个模块都有一个唯一的名称。虽然包名仍然可能在不同模块中重复，但模块系统通过控制可访问性来帮助管理这一点。通常，不应该导出包含相同全限定名类的不同模块。

**三、模块化的主要指令 (`module-info.java` 中的关键字)**

*   `module <module-name>`: 定义模块的名称。
*   `requires <module-name>`: 声明本模块依赖另一个模块。
    *   `requires static <module-name>`: 编译时需要，但运行时可选。
    *   `requires transitive <module-name>`: 如果模块A `requires transitive B`，那么任何依赖A的模块C也会自动隐式地 `requires B`。这对于传递 API 依赖非常有用。
*   `exports <package-name>`: 将指定包中的 `public` 类型暴露给其他模块。
*   `exports <package-name> to <module-name1>, <module-name2>, ...`: 限定导出，只将包暴露给指定的模块。
*   `opens <package-name>`: 允许其他模块在运行时对指定包进行**深度反射**（访问非 `public` 成员和修改 `final` 字段）。
*   `opens <package-name> to <module-name1>, <module-name2>, ...`: 限定开放，只允许指定的模块进行深度反射。
*   `uses <service-interface-name>`: 声明本模块使用了一个服务接口 (通常与 `java.util.ServiceLoader` 配合使用)。
*   `provides <service-interface-name> with <implementation-class-name1>, <implementation-class-name2>, ...`: 声明本模块提供了一个或多个服务接口的实现。


**JPMS 模块化项目结构**
**与Maven 的多模块区别，以及二者项目结构对比**
**结合：Maven 多模块与JPMS 模块**
