Spring Boot 3.x 是一个重要的主版本升级，带来了许多令人兴奋的新特性和显著的变化。它基于 **Spring Framework 6.x** 构建，并要求 **Java 17 或更高版本**作为基线。这使得 Spring Boot 能够充分利用 Java 语言的最新特性。

以下是 Spring Boot 3.x 的一些关键新特性和重要变化：

---

### 1. Java 17 基线

*   **强制要求**: Spring Boot 3.0 及更高版本要求 Java 17 作为最低 JDK 版本。这意味着你不能在低于 Java 17 的环境中使用 Spring Boot 3.x。
*   **好处**:
    *   可以使用 Java 17 的语言特性，如 Records、Sealed Classes、Pattern Matching for `instanceof` 等，来编写更简洁、更安全的代码。
    *   受益于 Java 17 及其后续版本在性能、安全性和 API 方面的改进。

---

### 2. Jakarta EE 9+ 迁移

*   **核心变化**: 这是 Spring Boot 3.x 和 Spring Framework 6.x 最显著的变化之一。所有之前使用 Java EE API 的依赖都已迁移到相应的 Jakarta EE API。
*   **包名变更**: 最直接的影响是包名从 `javax.*` 更改为 `jakarta.*`。
    *   例如：
        *   `javax.servlet.*` -> `jakarta.servlet.*` (用于 Web 应用)
        *   `javax.persistence.*` -> `jakarta.persistence.*` (用于 JPA)
        *   `javax.validation.*` -> `jakarta.validation.*` (用于 Bean Validation)
        *   `javax.annotation.*` (部分，如 `@PostConstruct`, `@PreDestroy`) -> `jakarta.annotation.*`
*   **影响**:
    *   如果你的项目直接使用了这些 `javax.*` 包下的 API，你需要更新你的 `import` 语句和依赖。
    *   许多第三方库也需要升级到支持 Jakarta EE 的版本。Spring Boot 3.x 会尽可能管理这些兼容的依赖版本。
    *   例如，如果你使用 Servlet API，你需要将 `javax.servlet-api` 依赖替换为 `jakarta.servlet-api`。`spring-boot-starter-web` 会自动处理这个问题。

---

### 3. 对 GraalVM 原生镜像的全面支持 (Ahead-of-Time - AOT)

*   **里程碑特性**: Spring Boot 3.x 将对 GraalVM 原生镜像的支持提升到了一个新的水平，使其成为一等公民。
*   **目标**:
    *   **极速启动**: 原生镜像应用可以实现毫秒级启动。
    *   **更小内存占用**: 运行时内存占用显著降低。
    *   **优化部署**: 特别适合 Serverless、容器化等对资源敏感的场景。
*   **工作原理**:
    *   **AOT (Ahead-of-Time) 编译**: Spring 应用程序在**构建时**（而不是运行时）进行大部分的分析和处理。这包括 Bean 的发现、依赖注入的解析、配置的处理等。
    *   **GraalVM Native Image**: 利用 GraalVM 的 `native-image` 工具将 Spring Boot 应用（包括其所有依赖和 JVM 本身的一部分）编译成一个独立的原生可执行文件。这个文件不依赖外部 JVM，直接运行在操作系统上。
*   **如何使用**:
    *   需要 GraalVM JDK。
    *   Spring Boot 提供了 Maven 和 Gradle 插件 (例如 `org.graalvm.buildtools:native-maven-plugin` 和 Spring Boot 自身的构建插件集成) 来简化原生镜像的构建过程。
    *   命令示例 (Maven): `mvn -Pnative spring-boot:build-image`
*   **`native` Profile 和 `*-native` 依赖**: Spring Boot 3.x 引入了 `native` profile 的概念，并在其 Maven/Gradle 插件中支持。同时，一些库可能需要特定的 `-native` 后缀的依赖版本以更好地支持原生镜像（尽管 Spring 团队在努力减少这种需求）。
*   **`@NativeHint`**: Spring Framework 6 引入了 `@NativeHint` 注解，用于向 GraalVM `native-image` 工具提供关于反射、资源、序列化、代理等方面的提示，因为这些动态特性在 AOT 编译时难以完全推断。Spring Boot 的自动配置也大量使用了这些提示。

---

### 4. Micrometer Tracing (替代 Spring Cloud Sleuth)

*   **统一的可观测性**: Spring Boot 3.x 引入了 Micrometer Tracing 作为其官方的分布式追踪解决方案，取代了之前广泛使用的 Spring Cloud Sleuth。
*   **Micrometer 生态系统**: Micrometer 是一个流行的 JVM 应用指标收集库。Micrometer Tracing 将追踪能力也整合到了这个生态系统中。
*   **抽象层**: Micrometer Tracing 提供了一个通用的追踪 API 抽象，底层可以对接多种追踪系统，如 OpenZipkin, OpenTelemetry, Jaeger, Wavefront 等。
*   **自动配置**: Spring Boot 3.x 提供了对 Micrometer Tracing 的自动配置，可以轻松集成各种追踪后端。
    *   例如，添加 `micrometer-tracing-bridge-brave` (用于 Zipkin) 或 `micrometer-tracing-bridge-otel` (用于 OpenTelemetry) 依赖，并配置相应的导出器。
*   **平滑过渡**: 对于从 Spring Cloud Sleuth 迁移过来的用户，Micrometer Tracing 旨在提供相似的功能和概念。

---

### 5. Log 4 j 2 扩展和改进

*   Spring Boot 3.x 增强了对 Log 4 j 2 的支持，包括：
    *   **Profile-specific 配置**: 更方便地为不同的 Spring Profile (如 `dev`, `prod`) 定义不同的 Log 4 j 2 配置。
    *   **环境属性查找**: 在 Log 4 j 2 配置中可以直接引用 Spring Environment 中的属性。

---

### 6. 对虚拟线程 (Virtual Threads - Project Loom) 的初步支持和展望

*   虽然 Spring Boot 3.0 发布时 Java 19 (引入虚拟线程预览版) 刚刚发布，但 Spring Framework 6 和 Spring Boot 3.x 在设计上已经考虑了对虚拟线程的兼容性。
*   **目标**: 使得 Spring 应用能够更容易地利用虚拟线程来提高并发应用的吞吐量和可伸缩性，而无需对现有代码进行大规模重写。
*   **当前状态 (截至 Spring Boot 3.x 系列)**:
    *   Tomcat, Jetty, Undertow 等内嵌 Web 服务器的最新版本开始支持在配置后使用虚拟线程来处理请求。Spring Boot 3.2 引入了简单的配置属性 `spring.threads.virtual.enabled=true` 来为内嵌 Web 服务器启用虚拟线程（如果服务器支持）。
    *   Spring 框架的核心组件在逐步适配虚拟线程，确保在虚拟线程环境下能正确工作（例如，避免不必要的 `synchronized` 块，正确处理 `ThreadLocal` 等）。
    *   这是一个持续演进的领域，随着 JDK 对虚拟线程支持的成熟 (Java 21 已正式发布虚拟线程)，Spring Boot 会提供更深入和广泛的支持。

---

### 7. HTTP Interface 客户端 (`@HttpExchange`)

*   Spring Framework 6 引入了一种新的声明式 HTTP 客户端方式，通过接口和注解来定义 HTTP 服务调用。
*   **`@HttpExchange`**: 类似于 Feign 或 Retrofit，你可以定义一个 Java 接口，并使用 `@HttpExchange` (或更具体的 `@GetExchange`, `@PostExchange` 等) 注解其方法来描述 HTTP 请求。
*   Spring Boot 3.x 提供了对这类 HTTP Interface 客户端的自动配置，可以轻松地创建和注入这些接口的代理实现。
    ```java
    // 定义一个 HTTP 服务接口
    public interface MyApiClient {
        @GetExchange("/users/{id}")
        User getUserById(@PathVariable Long id);
    }

    // 在配置类中创建代理实例
    @Configuration
    public class ApiClientConfig {
        @Bean
        MyApiClient myApiClient(WebClient.Builder builder) {
            WebClient webClient = builder.baseUrl("https://api.example.com").build();
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
            return factory.createClient(MyApiClient.class);
        }
    }
    ```
    Spring Boot 会进一步简化这个创建过程。

---

### 8. 移除的特性和重要的依赖升级

*   **移除了 `spring.factories` 对自动配置的支持**: 如前所述，自动配置类现在通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件注册。`spring.factories` 仍然用于其他类型的组件注册 (如 `EnvironmentPostProcessor`, `FailureAnalyzer` 等)，但其用于自动配置的键 `org.springframework.boot.autoconfigure.EnableAutoConfiguration` 已被移除。
*   **不再支持某些旧的或不常用的技术**: 例如，移除了对 Apache ActiveMQ "Classic" 的 `spring-boot-starter-activemq` 中的 PooledConnectionFactory 的自动配置。
*   **依赖升级**: 大量的第三方依赖都升级到了最新版本，以兼容 Java 17 和 Jakarta EE。例如：
    *   Hibernate 6.x
    *   Flyway 9.x
    *   R 2 DBC 1.0
    *   Jetty 11
    *   Tomcat 10.1
    *   Undertow 2.3
    *   Thymeleaf 3.1 (支持 Jakarta EE)

---

### 9. 其他值得注意的改进

*   **`@ConfigurationProperties` 改进**:
    *   更好的构造器绑定支持。
    *   可以通过 `@NestedConfigurationProperty` 注解来更好地处理嵌套配置对象的元数据。
*   **Spring Data 2022.0 (代号 Turing)**: 带来了许多 JPA、MongoDB、Redis 等模块的增强。
*   **Actuator 端点改进**:
    *   `/actuator/startup` 端点 (Spring Boot 2.6 引入，3.x 持续改进) 提供了应用启动步骤的详细信息，有助于分析和优化启动时间。
    *   对可观测性的整体增强。
*   **测试改进**:
    *   `@SpringBootTest` 的集成测试对 AOT 和原生镜像更加友好。
    *   对 JUnit 5 的持续优化支持。
*   **Spring Batch 5.0**: 与 Spring Boot 3.x 集成，也基于 Java 17 和 Jakarta EE，并带来了注解驱动的 Job/Step 配置等新特性。

---

### 迁移到 Spring Boot 3.x 的注意事项：

*   **JDK 版本**: 必须升级到 Java 17 或更高。
*   **Jakarta EE**: 这是最大的迁移工作，需要检查并更新所有 `javax.*` 包的引用。Spring Boot 提供了迁移指南和工具（如 `spring-boot-properties-migrator` 和 OpenRewrite 脚本）来帮助这个过程。
*   **依赖兼容性**: 确保你项目中的所有第三方依赖都有支持 Jakarta EE 和 Java 17 的版本。
*   **检查移除的特性**: 查阅官方发布说明和迁移指南，了解是否有你项目中使用的特性被移除或更改。
*   **Spring Cloud**: 如果你使用 Spring Cloud，你需要升级到与 Spring Boot 3.x 兼容的 Spring Cloud 版本 (例如 Spring Cloud 2022.0.x "Kilburn" 及更高版本)。

---

**总结一下，Spring Boot 3.x 是一次重要的技术飞跃，它拥抱了最新的 Java 和 Jakarta EE 标准，并为云原生应用（特别是通过 GraalVM 原生镜像）提供了前所未有的性能和资源效率。虽然迁移可能需要一些工作，但其带来的长期收益是显著的。**