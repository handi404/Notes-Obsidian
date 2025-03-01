### Java 12 新特性全面解析

Java 12 于 2019 年 3 月发布，作为非长期支持（Non-LTS）版本，它引入了多项实验性功能和语法改进，旨在提升开发效率和运行时性能。以下是核心特性与语法糖的详细总结：

---

#### 一、Java 12 核心新特性

1. **Switch 表达式（预览功能，JEP 325）**  
   - **语法改进**：支持 `->` 箭头语法、多值匹配和表达式返回值，避免传统 `switch` 的冗余代码和 `break` 遗漏问题。  
   - **`yield` 关键字**：在代码块中返回值（需启用预览功能）。  
   - 示例：  
     ```java
     // 传统写法
     int days = 0;
     switch (month) {
         case 1: case 3: case 5: 
             days = 31;
             break;
         default:
             days = 30;
     }
     
     // Java 12 Switch 表达式（预览）
     int days = switch (month) {
         case 1, 3, 5 -> 31;  // 多值匹配
         case 2 -> {
             if (isLeapYear) yield 29;  // 使用 yield 返回值
             else yield 28;
         }
         default -> 30;
     };
     ```
   - **启用预览功能**：编译时需添加 `--enable-preview` 参数。

2. **Shenandoah 垃圾收集器（实验性，JEP 344）**  
   - **低停顿时间**：通过并发压缩算法，减少 GC 暂停时间（适用于大堆内存应用）。  
   - **启用方式**：  
     ```bash
     java -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
     ```

3. **微基准测试套件（JEP 230）**  
   - 集成 JMH（Java Microbenchmark Harness）到 JDK 源码中，方便开发者编写性能测试：  
     ```java
     @Benchmark
     public void testMethod(Blackhole bh) {
         // 测试代码
         bh.consume(result);  // 避免 JIT 优化消除代码
     }
     ```

4. **默认类数据共享（CDS）归档（JEP 341）**  
   - **优化启动速度**：默认生成 `classes.jsa` 共享归档文件，加速多 JVM 实例的启动。  
   - **手动生成归档**：  
     ```bash
     java -Xshare:dump
     ```

5. **JVM 常量 API（JEP 334）**  
   - 提供 `java.lang.constant` 包，支持以类型安全的方式描述常量（如方法句柄、动态代理），主要用于动态语言和工具开发。

---

#### 二、Java 12 语法糖与 API 增强

6. **字符串增强方法**  
   - `indent(int n)`：调整字符串每行的缩进（按 `n` 个空格）：  
     ```java
     String text = "Hello\nJava\n12";
     System.out.println(text.indent(2));  // 每行缩进 2 空格
     ```
   - `transform(Function)`：链式处理字符串内容：  
     ```java
     String result = "java12"
         .transform(s -> s.toUpperCase())
         .transform(s -> s.repeat(2));  // "JAVA12JAVA12"
     ```

7. **Compact Number Format（紧凑数字格式）**  
   - 支持短格式显示大数字（如 “1 K” 替代 “1000”）：  
     ```java
     NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
     System.out.println(fmt.format(1000));      // "1K"
     System.out.println(fmt.format(1_500_000)); // "1.5M"
     ```

8. **Files 类新增 `mismatch` 方法**  
   - 快速比较两个文件内容，返回第一个不匹配字节的位置：  
     ```java
     Path file1 = Paths.get("a.txt");
     Path file2 = Paths.get("b.txt");
     long mismatch = Files.mismatch(file1, file2);  // -1 表示完全匹配
     ```

---

#### 三、其他重要改进

1. **G 1 垃圾收集器优化（JEP 346）**  
   - **及时返回未使用的堆内存**：在空闲时自动释放内存给操作系统，优化资源利用率。

2. **ABORTABLE_MIXED 集合（JEP 344）**  
   - G 1 在混合回收阶段可中止，避免单次回收时间过长，提升响应速度。

3. **Unicode 11 支持**  
   - 新增 684 个字符，包括表情符号（如 🥟 饺子）和扩展符号。

---

### 总结与应用场景

- **代码简洁性**：Switch 表达式减少模板代码，适合多条件分支处理。  
- **低延迟场景**：Shenandoah GC 适用于实时系统或大数据处理。  
- **性能测试**：集成 JMH 方便开发者验证代码性能。  
- **资源优化**：G 1 的改进适合云原生环境，提升资源利用率。  
- **国际化支持**：紧凑数字格式和 Unicode 11 增强国际化应用的兼容性。

Java 12 虽为非 LTS 版本，但其特性在后续版本（如 Java 17 LTS）中逐步稳定。建议在评估预览功能（如 Switch 表达式）的稳定性后选择性采用，或直接升级到 LTS 版本以获取长期支持。