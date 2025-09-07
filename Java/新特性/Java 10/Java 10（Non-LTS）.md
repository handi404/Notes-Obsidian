### Java 10 新特性全面解析

Java 10 于 2018 年发布，是采用新版本发布周期（每 6 个月一版）后的首个版本。虽然非长期支持（LTS），但引入了多项关键改进，尤其是**局部变量类型推断**和性能优化。以下是核心特性和语法糖的详细总结：

---

#### 一、Java 10 核心新特性

1. **局部变量类型推断（`var`）**  
   - 允许通过 `var` 声明局部变量，编译器自动推断类型（**仅限局部变量，不可用于字段、方法参数或返回类型**）。  
   - **典型应用场景**：简化冗长类型声明，增强代码可读性。  
   - 示例：
     ```java
     var list = new ArrayList<String>(); // 推断为 ArrayList<String>
     var stream = list.stream();         // 推断为 Stream<String>
     
     // 遍历 Map 的键值对
     var map = Map.of("Java", 10, "Python", 3);
     for (var entry : map.entrySet()) {
         var key = entry.getKey();
         var value = entry.getValue();
     }
     ```
   - **限制**：  
     - 必须显式初始化（如 `var x;` 会报错）。  
     - 不能用于 Lambda 表达式（Java 11 开始支持）。  
     - 避免滥用（如 `var` 导致类型不明确时，显式声明更优）。

2. **应用类数据共享（AppCDS, JEP 310）**  
   - 扩展 JDK 8 的类数据共享（CDS），允许将应用类元数据缓存在存档文件中，**减少启动时间和内存占用**。  
   - 使用步骤：
     ```bash
     # 1. 生成类列表
     java -Xshare:off -XX:+UseAppCDS -XX:DumpLoadedClassList=classes.lst -jar myapp.jar
     # 2. 创建共享存档
     java -Xshare:dump -XX:+UseAppCDS -XX:SharedClassListFile=classes.lst -XX:SharedArchiveFile=myapp.jsa --class-path myapp.jar
     # 3. 使用共享存档启动
     java -Xshare:on -XX:+UseAppCDS -XX:SharedArchiveFile=myapp.jsa -jar myapp.jar
     ```

3. **并行 Full GC 的 G 1 垃圾收集器（JEP 307）**  
   - G 1 在 Full GC 时改为并行回收（替代单线程的 Serial GC），**减少 Full GC 的停顿时间**。  
   - 启用参数：`-XX:+UseG1GC`（默认在 Java 9+ 中为 G 1）。

4. **线程本地握手（JEP 312）**  
   - 允许在不执行全局 VM 安全点的情况下执行线程回调，**优化 JVM 性能分析工具**（如减少调试时的停顿时间）。

5. **根证书更新（JEP 319）**  
   - 更新 JDK 中的根证书，增强 TLS/SSL 安全性。

---

#### 二、Java 10 常用语法糖

6. **不可变集合的增强（Java 9 基础 + Java 10 优化）**  
   - 结合 `var` 和 `List.copyOf()` 创建不可变集合：
     ```java
     var originalList = new ArrayList<>(List.of(1, 2, 3));
     var immutableCopy = List.copyOf(originalList); // 不可修改
     ```

7. **Optional 的 `orElseThrow()` 简化**  
   - Java 10 允许无参调用 `orElseThrow()`，等同于 `get()` 但更语义化：
     ```java
     Optional<String> optional = Optional.of("Java 10");
     String value = optional.orElseThrow(); // 替代 get()
     ```

8. **改进的 `var` 与 Lambda 结合**  
   - 虽然 `var` 不能直接用于 Lambda 参数，但可通过类型推断简化代码：
     ```java
     var numbers = List.of(1, 2, 3);
     numbers.forEach((var n) -> System.out.println(n)); // 显式类型声明
     ```

---

#### 三、其他重要改进

9. **基于时间的版本号（JEP 322）**  
   - 版本号格式改为 `$FEATURE.$INTERIM.$UPDATE.$PATCH`（如 `10.0.1`），更清晰标识发布时间。

10. **实验性 JIT 编译器（GraalVM, JEP 317）**  
   - 引入 Graal 编译器作为实验性特性，替代 C 2 编译器（需手动启用）：
     ```bash
     java -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler
     ```

11. **Heap Allocation on Alternative Memory Devices（JEP 316）**  
   - 允许在非 DRAM 内存设备（如 NVMe SSD）上分配堆内存，优化成本敏感型应用。

---

### 总结与应用场景

- **代码简洁性**：`var` 显著减少冗余类型声明，适合集合操作、泛型类型复杂的场景。  
- **性能优化**：AppCDS 适用于微服务快速启动，G 1 并行 Full GC 提升高吞吐应用的稳定性。  
- **安全增强**：根证书更新保障 HTTPS 通信安全。  
- **开发体验**：`orElseThrow()` 提升 Optional 代码的可读性。

Java 10 作为短期版本，其核心特性（如 `var`）在后续版本（如 Java 11 LTS）中进一步优化，建议结合 Java 11+ 的新特性升级以获得更完整的功能支持。