### Java 9 新特性全面解析

Java 9 于 2017 年发布，是 Java 平台模块化（Project Jigsaw）的首个版本，带来了模块化系统、工具链增强和多项语法优化。以下是核心特性与语法糖的详细总结：

---

#### 一、Java 9 核心新特性

1. **模块化系统（Jigsaw）**  
   - 通过 `module-info.java` 定义模块，解决类路径混乱问题，实现强封装和依赖管理。
   - 示例：
     ```java
     module com.example.myapp {
         requires java.base;        // 隐式依赖
         requires java.sql;         // 显式依赖
         exports com.example.api;   // 导出包
     }
     ```

2. **JShell（REPL 交互式编程）**  
   - 命令行工具直接执行 Java 代码片段，适合快速测试：
     ```bash
     jshell> var list = List.of(1, 2, 3);
     jshell> list.stream().sum()  // 输出 6
     ```

3. **集合工厂方法**  
   - 通过 `List.of()`, `Set.of()`, `Map.ofEntries()` 创建**不可变集合**，替代传统 `Arrays.asList()`：
     ```java
     List<String> list = List.of("A", "B", "C");
     Set<Integer> set = Set.of(1, 2, 3);
     Map<String, Integer> map = Map.ofEntries(
         Map.entry("Java", 9),
         Map.entry("Python", 3)
     );
     // 尝试修改会抛出 UnsupportedOperationException
     ```

4. **接口私有方法**  
   - 允许在接口中定义 `private` 方法，减少冗余代码：
     ```java
     public interface Logger {
         default void logInfo(String message) {
             log(message, "INFO");
         }
         private void log(String message, String level) {
             System.out.println(level + ": " + message);
         }
     }
     ```

5. **改进的 Try-with-Resources**  
   - 资源对象可在 `try` 外声明，自动关闭：
     ```java
     BufferedReader reader = new BufferedReader(...);
     try (reader) { // Java 9 允许直接使用已存在的变量
         String line = reader.readLine();
     }
     ```

6. **Stream API 增强**  
   - `takeWhile()` / `dropWhile()`：根据条件截取流：
     ```java
     List<Integer> list = List.of(2, 4, 6, 7, 8);
     list.stream()
         .takeWhile(n -> n % 2 == 0) // 输出 [2,4,6]
         .forEach(System.out::println);
     ```

7. **多版本兼容 JAR（MRJAR）**  
   - 为不同 Java 版本提供独立实现，保持兼容性：
     ```
     myapp.jar
     ├── META-INF
     └── com
         ├── example
         │   └── Main.class        // 公共代码
         └── META-INF
             └── versions
                 └── 9
                     └── com
                         └── example
                             └── Utils.class // Java 9 专用实现
     ```

---

#### 二、Java 9 常用语法糖

8. **钻石操作符增强**  
   - 允许在匿名内部类中使用钻石操作符：
     ```java
     List<String> list = new ArrayList<>() { // Java 9 支持
         {
             add("Java");
             add("9");
         }
     };
     ```

9. **Optional 类增强**  
   - `ifPresentOrElse()`：提供替代方案：
     ```java
     optionalValue.ifPresentOrElse(
         v -> System.out.println("Value: " + v),
         () -> System.out.println("No value")
     );
     ```

10. **改进的 @SafeVarargs**  
   - 允许在私有方法上使用 `@SafeVarargs`，减少泛型警告：
     ```java
     @SafeVarargs
     private final <T> void mergeLists(List<T>... lists) {
         // 合并操作
     }
     ```

11. **HTTP/2 客户端（孵化模块）**  
   - 早期版本的 HTTP/2 客户端，需手动启用：
     ```java
     HttpClient client = HttpClient.newHttpClient();
     HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("https://example.com"))
         .build();
     ```

---

#### 三、其他重要改进

12. **进程 API 增强**  
   - 获取进程信息（PID、命令行参数）：
     ```java
     ProcessHandle current = ProcessHandle.current();
     System.out.println("PID: " + current.pid());
     ```

13. **响应式流（Reactive Streams）**  
   - 支持响应式编程模型，提供 `java.util.concurrent.Flow` 接口。

14. **StackWalker**  
   - 按需遍历堆栈帧，避免生成完整堆栈跟踪的开销：
     ```java
     StackWalker.getInstance().walk(stack -> {
         stack.forEach(System.out::println);
         return null;
     });
     ```

---

### 总结与应用场景
- **模块化开发**：适合大型项目，解决依赖管理和封装问题。
- **快速原型**：JShell 实时测试代码片段。
- **不可变集合**：简化代码并提升线程安全性。
- **流处理优化**：`takeWhile` / `dropWhile` 提升数据处理灵活性。
- **兼容性维护**：MRJAR 支持多版本共存。

Java 9 是模块化转型的里程碑，尽管部分特性（如 Jigsaw）需要开发者适应，但其语法糖和工具链改进显著提升了代码简洁性和可维护性。