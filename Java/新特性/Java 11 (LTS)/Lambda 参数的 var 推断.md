Java 11 中对 `var` 关键字的一个重要扩展：**Lambda 参数的类型推断**。这个特性虽然看起来很小，但它解决了 `var` 在 Java 10 中留下的一个语法空白，并为 Lambda 表达式带来了更大的灵活性。

---

### Lambda 参数的 `var` 推断

#### 1. 核心概念 (Core Concept)

在 Java 10 引入 `var` 时，它只能用于局部变量的声明，不能用于 Lambda 表达式的参数。Java 11 弥补了这一点。

**核心能力：** 你可以在 Lambda 表达式的参数列表中，使用 `var` 来替代具体的类型声明。

**这有什么用？** 单纯为了少写几个字吗？不完全是。它的主要价值在于**保持 Lambda 参数声明的一致性，并允许你在参数上添加注解**，而这在以前是无法做到的。

让我们看一下 Lambda 参数写法的演变：

*   **Java 8 - 方式一 (显式类型):**
    ```java
    (String s1, String s2) -> s1.length() + s2.length()
    ```
    这种写法很清晰，但比较冗长。

*   **Java 8 - 方式二 (隐式类型):**
    ```java
    (s1, s2) -> s1.length() + s2.length()
    ```
    这是最常用的写法，非常简洁。但有一个限制：**你不能只给一个参数加类型**，要么全加，要么全不加。并且，**你无法在这种写法上添加注解**。

*   **Java 11 - `var` 方式 (推断类型):**
    ```java
    (var s1, var s2) -> s1.length() + s2.length()
    ```
    这种写法结合了前两者的优点：
    1.  **简洁性：** 你不需要写出具体的类型名，如 `String`。
    2.  **一致性：`var`** 关键字让参数看起来仍然像一个“正式”的变量声明。
    3.  **注解能力：** 这是最重要的！因为 `var s1` 是一个完整的变量声明，所以你可以在它前面添加注解。

**通俗比喻：**
*   **隐式类型 `(s1, s2)`** 就像给朋友起了一个**昵称**。很简单，但不正式，你没法在昵称前面加一个“博士”头衔。
*   **显式类型 `(String s1, String s2)`** 就像在正式文件里写**全名和类型**。很规范，但啰嗦。
*   **`var` 类型 `(var s1, var s2)`** 就像在一个半正式场合，你用一个**代称**（比如“这位先生”）来指代朋友。它比昵称正式，保留了变量声明的“形态”，所以你可以在前面加上一个修饰（**注解**），比如“尊敬的这位先生”。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 10 (The Pain Point):**
    `var` 很好用，但为什么唯独 Lambda 参数不能用？这造成了语法上的不一致。更重要的是，当你需要给 Lambda 参数加注解（例如，`@NonNull`、`@RequestBody` 等）时，你必须退回到最冗长的显式类型声明方式，这与 `var` 倡导的简洁风格相悖。

*   **Java 11 (The Solution):**
    JEP 323 (Local-Variable Syntax for Lambda Parameters) 解决了这个问题。它允许 `var` 用于 Lambda 参数，使得所有局部变量风格的声明（包括 Lambda 参数）都可以使用 `var`，语法更加统一。

*   **现代化应用：**
    这个特性在现代框架（如 Spring）和代码质量工具（如 Lombok, Checker Framework）中非常有用。这些工具经常使用注解来增强代码功能或进行静态分析。

#### 3. 代码示例 (Code Example)

```java
import java.util.function.BiFunction;
import javax.annotation.Nonnull; // 假设我们有一个这样的注解

public class VarInLambdaExample {

    public static void main(String[] args) {

        // --- 1. 基础用法 ---
        // Java 8 风格
        BiFunction<String, String, Integer> lenSumFuncOld = (s1, s2) -> s1.length() + s2.length();
        
        // Java 11 `var` 风格
        BiFunction<String, String, Integer> lenSumFuncNew = (var s1, var s2) -> s1.length() + s2.length();

        System.out.println("Result: " + lenSumFuncNew.apply("Java", "11"));

        // --- 2. 核心优势：添加注解 ---
        // 假设我们需要一个非空检查
        
        // Java 8 必须写全类型才能加注解
        BiFunction<String, String, String> concatFuncOld = 
            (@Nonnull String s1, @Nonnull String s2) -> s1 + s2;

        // Java 11 使用 `var` 就可以加注解，代码更简洁
        BiFunction<String, String, String> concatFuncNew = 
            (@Nonnull var s1, @Nonnull var s2) -> s1.concat(s2);
            
        System.out.println("Concatenated: " + concatFuncNew.apply("Hello, ", "var!"));

        // 这在 Spring MVC 等框架中很常见
        // @PostMapping("/users")
        // public Mono<User> createUser(@RequestBody @Valid Mono<User> userMono) { ... }
        // 如果用函数式路由，可能会写成：
        // handler((@RequestBody @Valid var user) -> ...)
    }
}

// 模拟一个注解
@interface Nonnull {}
```

#### 4. 扩展与应用 (Extension & Application)

*   **静态分析与代码质量：**
    使用 Checker Framework 或其他静态分析工具时，可以通过 `@NonNull`, `@Nullable` 等注解来帮助工具检查潜在的 `NullPointerException`。
*   **依赖注入与框架：**
    在一些支持函数式风格的现代框架中，可能会用注解来标记需要进行特殊处理（如反序列化、验证）的 Lambda 参数。
*   **保持代码风格一致性：**
    如果一个团队决定广泛使用 `var`，那么允许在 Lambda 中使用 `var` 可以让整个代码库的风格更加统一。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **要么都用 `var`，要么都不用：**
    在同一个 Lambda 表达式的参数列表中，你不能混合使用 `var` 和具体类型，也不能混合 `var` 和隐式类型。
    ```java
    // (var s1, String s2) -> ...  // 错误！
    // (var s1, s2) -> ...        // 错误！
    ```

2.  **不能省略括号：**
    当 Lambda 只有一个参数时，隐式类型可以省略括号 `s -> ...`。但如果使用 `var`，**括号不能省略**。
    ```java
    // Consumer<String> c1 = s -> System.out.println(s);     // OK
    // Consumer<String> c2 = (var s) -> System.out.println(s); // OK
    // Consumer<String> c3 = var s -> System.out.println(s);   // 错误！
    ```

3.  **类型推断依赖目标类型：**
    和所有 Lambda 表达式一样，`var` 的类型推断依赖于 Lambda 表达式被赋值的**目标类型**（如 `BiFunction<String, String, Integer>`）。如果编译器无法确定目标类型，就无法推断 `var` 的类型。

总结来说，Lambda 参数的 `var` 推断是一个“锦上添花”的特性。它本身不带来革命性的功能，但它修复了 `var` 语法的不一致性，并在需要为 Lambda 参数添加注解时，提供了一种更简洁、更现代的写法。这体现了 Java 语言在细节上不断打磨、追求完美的演进方向。