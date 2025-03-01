### Java 11 新特性全面解析

Java 11 是 Oracle 在 2018 年发布的长期支持版本（LTS），提供了多项重要改进和语法增强。以下从核心特性、语法糖和底层优化三个方面详细总结：

---

#### 一、Java 11 核心新特性

1. **HTTP Client 标准化**  
   - 原 `java.net.http` 孵化模块正式成为标准 API，支持同步/异步请求和 WebSocket。
   - 示例：
     ```java
     HttpClient client = HttpClient.newHttpClient();
     HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("https://example.com"))
         .build();
     HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
     System.out.println(response.body());
     ```

2. **String 增强方法**  
   - `lines()`：按换行符分割字符串为 Stream。
   - `repeat(int)`：重复字符串指定次数。
   - `strip()` / `stripLeading()` / `stripTrailing()`：去除 Unicode 空白字符（比 `trim()` 更严格）。
   - 示例：
     ```java
     String str = "  Hello\u2005Java11  ";
     System.out.println(str.strip()); // "Hello\u2005Java11"
     System.out.println("abc".repeat(3)); // "abcabcabc"
     ```

3. **局部变量类型推断增强（var in Lambda）**  
   - 允许在 Lambda 参数中使用 `var`，需显式注解类型：
     ```java
     BiFunction<String, Integer, String> func = 
         (@NonNull var s, var i) -> s.repeat(i); // 需注解时使用 var
     ```

4. **单文件源码直接运行**  
   - 无需手动编译 `.java` 文件：
     ```bash
     $ java HelloWorld.java
     ```

5. **垃圾收集器改进**  
   - **Epsilon GC**：无操作的“无回收”GC，用于性能测试。
   - **ZGC（实验性）**：低延迟 GC，暂停时间 < 10 ms，支持 TB 级堆内存。

6. **Flight Recorder（JFR）开源化**  
   - 原商业特性 JFR 成为开源工具，支持低开销性能监控：
     ```bash
     $ java -XX:StartFlightRecording=duration=60s,filename=recording.jfr ...
     ```

7. **Unicode 10 支持**  
   - 新增 8,518 个字符，包括比特币符号 `₿` 和表情符号。

---

#### 二、Java 常用语法糖（跨版本）

8. **局部变量类型推断（Java 10+）**  
   ```java
   var list = new ArrayList<String>(); // 自动推断为 ArrayList<String>
   ```

9. **Switch 表达式（Java 12+，预览）**  
   ```java
   int days = switch (month) {
       case 1,3,5,7,8,10,12 -> 31;
       case 4,6,9,11 -> 30;
       default -> 28;
   };
   ```

10. **文本块（Java 13+，预览）**  
   ```java
   String json = """
       {
           "name": "Java",
           "version": 11
       }
       """;
   ```

11. **Optional 增强（Java 9+）**  
   ```java
   optional.ifPresentOrElse(
       value -> System.out.println(value),
       () -> System.out.println("Not found")
   );
   ```

12. **Try-with-resources 自动关闭（Java 7+）**  
   ```java
   try (BufferedReader br = new BufferedReader(new FileReader(path))) {
       return br.readLine();
   }
   ```

---

#### 三、底层优化与移除内容

13. **移除非核心模块**  
   - Java EE（CORBA、JAXB 等）被标记为废弃，需手动添加依赖。

14. **动态类文件常量（JEP 309）**  
   - 提升 JVM 动态语言性能，优化字节码处理。

15. **TLS 1.3 支持**  
   - 提升安全协议性能，减少握手延迟。

---

### 总结与应用场景
- **快速开发**：单文件运行、`var` 和文本块简化代码。
- **高性能场景**：ZGC 适合低延迟应用，JFR 用于生产监控。
- **现代 API**：HTTP Client 替代旧 `HttpURLConnection`。

Java 11 是生产环境升级的重要选择，结合后续版本的语法糖（如 Switch 表达式）可大幅提升代码简洁性。开发者需注意模块化调整和废弃 API 的迁移。