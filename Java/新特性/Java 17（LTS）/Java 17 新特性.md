### Java 17 新特性全面详解

Java 17 于 2021 年 9 月发布，是继 Java 11 之后的第二个长期支持版本（LTS），提供了一系列增强语言表达能力、安全性和性能的重要特性。以下是核心新特性与常用语法糖的详细解析：

---

#### 一、Java 17 核心新特性

1. **密封类（Sealed Classes，JEP 409）**  
   - **目的**：限制类或接口的子类范围，增强领域模型的精准控制。  
   - **语法**：使用 `sealed` 关键字声明父类，并通过 `permits` 明确允许继承的子类。  
   - **示例**：  
     ```java
     public sealed class Shape permits Circle, Rectangle, Triangle {
         // 父类定义
     }
     
     // 子类必须为 final、sealed 或 non-sealed
     public final class Circle extends Shape { /*...*/ }
     public non-sealed class Rectangle extends Shape { /*...*/ }
     public sealed class Triangle extends Shape permits EquilateralTriangle { /*...*/ }
     ```
   - **应用场景**：定义严格的类层次结构（如金融交易类型、几何图形），避免非法子类扩展。

2. **模式匹配的 switch（Pattern Matching for switch，JEP 406，预览功能）**  
   - **增强功能**：  
     - 支持类型匹配（类似 `instanceof` 模式匹配）。  
     - 支持 `null` 值处理。  
   - **示例**：  
     ```java
     Object obj = "Java 17";
     String result = switch (obj) {
         case Integer i -> "整数: " + i;
         case String s && s.length() > 5 -> "长字符串: " + s; // 条件分支
         case String s -> "字符串: " + s;
         case null -> "空值"; // 显式处理 null
         default -> "其他类型";
     };
     ```
   - **启用预览**：需添加编译参数 `--enable-preview --release 17`。

3. **外部函数与内存 API（Foreign Function & Memory API，JEP 412，孵化器模块）**  
   - **目标**：替代 JNI，提供安全高效的原生代码交互。  
   - **示例**：调用 C 标准库函数：  
     ```java
     try (MemorySegment segment = MemorySegment.allocateNative(1024)) {
         CLinker linker = CLinker.getInstance();
         MethodHandle strlen = linker.downcallHandle(
             linker.lookup("strlen").get(),
             MethodType.methodType(long.class, MemoryAddress.class),
             FunctionDescriptor.of(CLinker.C_LONG, CLinker.C_POINTER)
         );
         long length = (long) strlen.invoke(segment.address());
     }
     ```

4. **伪随机数生成器增强（JEP 356）**  
   - **新 API**：`RandomGenerator` 接口统一随机数生成器，支持算法选择（如 `L32X64MixRandom`）。  
   - **示例**：  
     ```java
     RandomGenerator generator = RandomGenerator.of("L64X128MixRandom");
     int randomNumber = generator.nextInt(100);
     ```

5. **移除实验性 AOT 和 JIT 编译器（JEP 410）**  
   - 移除 GraalVM JIT 和 AOT 编译器的实验性支持，推荐直接使用 GraalVM 发行版。

6. **文本块增强（JEP 378，Java 15 引入，Java 17 优化）**  
   - **改进**：支持自动删除行尾空白字符（使用 `\` 显式保留）。  
   - **示例**：  
     ```java
     String json = """
         {
             "name": "Java",
             "version": 17\s
         }"""; // "\s" 保留行末空格
     ```

---

#### 二、Java 17 常用语法糖

1. **记录类（Records，Java 16 正式化）**  
   - **作用**：简化不可变数据类的定义，自动生成 `equals()`、`hashCode()` 和 `toString()`。  
   - **示例**：  
     ```java
     public record User(String name, int age) {}
     
     User user = new User("Alice", 30);
     System.out.println(user.name()); // 自动生成访问方法
     ```

2. **instanceof 模式匹配（Java 16 正式化）**  
   - **简化代码**：直接提取对象属性，避免冗余类型转换。  
   - **示例**：  
     ```java
     Object obj = "Java 17";
     if (obj instanceof String s && s.length() > 5) {
         System.out.println(s.toUpperCase());
     }
     ```

3. **Stream.toList() 快捷方法（Java 16 引入）**  
   - **替代**：`collect(Collectors.toList())`。  
   - **示例**：  
     ```java
     List<String> list = Stream.of("a", "b", "c").toList();
     ```

4. **空指针异常信息增强（Java 14 引入，持续优化）**  
   - **精准定位**：明确提示空指针发生的具体变量。  
   - **示例**：  
     ```java
     String str = null;
     System.out.println(str.length()); 
     // 错误信息：Cannot invoke "String.length()" because "str" is null
     ```

---

#### 三、其他重要改进

1. **强封装 JDK 内部 API（JEP 403）**  
   - 禁止通过反射访问 JDK 内部 API（如 `sun.misc.Unsafe`），需通过 `--add-opens` 显式授权。

2. **移除已弃用功能**  
   - 移除 Applet API、Security Manager 的默认启用（标记为废弃）。

3. **新的 macOS 渲染管道（JEP 382）**  
   - 使用 Apple Metal API 替代 OpenGL，提升图形渲染性能。

4. **动态 CDS 归档（JEP 351）**  
   - 简化类数据共享归档的生成流程：  
     ```bash
     java -XX:ArchiveClassesAtExit=app.jsa -jar myapp.jar
     ```

---

### 总结与应用场景

- **领域建模**：密封类 + 记录类，精准定义数据模型（如金融交易类型、配置对象）。  
- **模式匹配**：简化类型检查和逻辑分支处理（如 JSON 解析、状态机）。  
- **原生交互**：通过 FFM API 安全调用 C/C++ 库（如高性能计算、硬件操作）。  
- **代码简洁性**：文本块、Stream.toList() 减少模板代码。  
- **安全性**：强封装内部 API，降低非法访问风险。  

Java 17 是现代化企业应用的首选版本，其 LTS 状态和语法改进（如模式匹配、记录类）大幅提升了开发效率与代码可维护性。建议结合 GraalVM 原生编译和 ZGC 垃圾收集器，构建高性能、低延迟的应用。