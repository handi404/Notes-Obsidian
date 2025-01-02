是的，在 Kotlin 脚本中使用第三方库时，依赖会下载到本地，但具体的存储位置和方式取决于你的环境设置。以下是相关细节：

---

### **依赖下载到本地的过程**

1. **`@file:DependsOn` 注解**  
    当你在 Kotlin 脚本中使用 `@file:DependsOn` 添加依赖时，Kotlin 脚本编译器会解析依赖，并从远程仓库（如 Maven Central 或 JCenter）下载所需的 `.jar` 文件。
    
    ```kotlin
    @file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    import kotlinx.coroutines.*
    ```
    
2. **下载位置**  
    默认情况下，依赖会下载到你本地的 Maven 缓存目录中。
    
    **路径**：
    
    - **Linux/MacOS**: `~/.m2/repository`
    - **Windows**: `%USERPROFILE%\.m2\repository`
    
    每个依赖会按照 Maven 的目录结构存储，例如：
    
    ```
    ~/.m2/repository/org/jetbrains/kotlinx/kotlinx-coroutines-core/1.7.3/kotlinx-coroutines-core-1.7.3.jar
    ```
    
3. **缓存机制**  
    如果依赖已经存在于本地缓存中（即 Maven 仓库目录中），Kotlin 编译器会直接使用缓存，而不会重新下载。
    

---

### **临时缓存的情况**

如果你使用了一些脚本管理工具（例如 **Kotlin Scripting Host** 或特定脚本执行环境），依赖可能会下载到 **临时缓存目录**。  
具体实现取决于工具的配置。例如：

- KScript 默认会将依赖缓存到 `~/.kscript/` 目录中。
- IntelliJ IDEA 中的脚本可能依赖 IDE 本身的依赖管理机制。

---

### **清理依赖**

如果你想清理这些缓存的依赖，可以手动删除 `.m2/repository` 或相关缓存目录：

```bash
rm -rf ~/.m2/repository/org/jetbrains/kotlinx
```

或者，使用 Maven 的清理命令清除本地缓存：

```bash
mvn dependency:purge-local-repository
```

---

### **避免依赖被永久缓存**

如果你不想将依赖缓存到系统范围的 Maven 仓库中，可以选择以下方法：

1. **使用容器化环境**：通过 Docker 创建一个临时的开发环境，不会污染本地依赖。
2. **自定义缓存目录**：通过修改 `MAVEN_USER_HOME` 环境变量指定缓存位置：
    
    ```bash
    export MAVEN_USER_HOME=/path/to/temp/cache
    ```
    

---

### **总结**

- **是的，第三方库会下载到本地**，通常在 `~/.m2/repository`。
- **本地缓存机制**：相同的依赖不会重复下载，提升效率。
- **可以清理或更改缓存位置**，通过修改环境变量或使用特定工具实现。