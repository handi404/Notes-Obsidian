Java 9 中一个虽小但精妙的特性：**私有接口方法 (Private Interface Methods)**。这个特性是对 Java 8 接口革命的完美补充，它解决了在引入默认方法和静态方法后出现的一个新问题。

---

### 私有接口方法 (Private Interface Methods)

#### 1. 核心概念 (Core Concept)

在 Java 9 之前，接口中的方法要么是 `public abstract`（默认），要么是 Java 8 引入的 `public default` 或 `public static`。它们都是**公开的**。

Java 9 允许我们在接口中定义 `private` 和 `private static` 方法。这些方法**只能在接口内部被调用**，实现该接口的类完全无法访问它们。

**核心目的：**
1.  **代码复用：** 避免在多个 `default` 或 `static` 方法之间出现重复的代码块。
2.  **封装实现细节：** 将接口内部的实现逻辑隐藏起来，不污染对外暴露的 API。

**通俗比喻：**
想象一个餐厅的**公开菜单 (Interface)**。
*   `default` 方法就像菜单上的一个套餐，比如“商务午餐”，顾客（实现类）可以直接点。
*   假设“商务午餐”和“家庭晚餐”两个套餐都需要一道“秘制酱料”。在 Java 8 中，厨师要么在准备每个套餐时都重新调制一遍酱料（**代码重复**），要么就把“调制秘制酱料”也写在公开菜单上作为一个 `default` 选项（**暴露了不该暴露的内部细节**）。
*   Java 9 的 `private` 方法，就像是厨师把“调制秘制酱料”的配方写在了**厨房内部的笔记上**。顾客看不到也点不了这个“秘制酱料”，但厨师在准备“商务午餐”和“家庭晚餐”时，都可以参考这份笔记来制作，既高效又保密。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 8 (The Pain Point):**
    Java 8 引入了 `default` 方法，允许接口演进。但很快就发现一个问题：如果两个 `default` 方法 `methodA()` 和 `methodB()` 有一大段公共逻辑，我们该怎么办？
    *   **方案一（糟糕）：** 代码复制粘贴。这违反了 DRY (Don't Repeat Yourself) 原则，难以维护。
    *   **方案二（不理想）：** 将公共逻辑提取到一个新的 `default` 方法 `helperMethod()` 中。但这会导致 `helperMethod()` 成为接口公共 API 的一部分，所有实现类都能看到并调用它。这是一个不应该被外部使用的实现细节，暴露它会造成 API 的混乱和误用。

*   **Java 9 (The Solution):**
    `private` 方法完美地解决了这个问题。你可以将公共逻辑提取到一个 `private` 方法中，然后让 `methodA()` 和 `methodB()` 去调用它。这个 `private` 方法对外部世界完全不可见，实现了完美的封装。

*   **现代化使用：**
    这个特性主要是为**库和框架的设计者**服务的。当你设计一个包含复杂默认行为的接口时，`private` 方法是保持其 API 干净、整洁和易于维护的关键工具。它鼓励开发者在接口层面编写更健壮、内聚性更高的代码。

#### 3. 代码示例 (Code Example)

让我们用一个 `Mailer` 接口来演示。

```java
// Mailer.java
public interface Mailer {

    // 抽象方法，必须实现
    void send(String to, String subject, String body);

    // 默认方法 A
    default void sendWelcomeEmail(String to) {
        String subject = "Welcome!";
        String body = "Welcome to our platform.";
        // 在发送前，先进行连接和认证
        connectAndAuthenticate();
        send(to, subject, body);
    }

    // 默认方法 B
    default void sendPasswordResetEmail(String to, String token) {
        String subject = "Password Reset";
        String body = "Your reset token is: " + token;
        // 在发送前，也需要连接和认证
        connectAndAuthenticate();
        send(to, subject, body);
    }

    // 私有方法 (Java 9+) - 封装了重复的逻辑
    // 这个方法对实现类是不可见的
    private void connectAndAuthenticate() {
        System.out.println("Connecting to mail server...");
        System.out.println("Authenticating...");
        // 复杂的连接和认证逻辑...
    }

    // 静态方法也可以调用私有静态方法
    static void logStatus() {
        log("Mailer interface is active.");
    }
    
    // 私有静态方法
    private static void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// SmtpMailer.java - 实现类
class SmtpMailer implements Mailer {
    @Override
    public void send(String to, String subject, String body) {
        System.out.printf("Sending email to %s with subject '%s'%n", to, subject);
        // 实现具体的发送逻辑
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        SmtpMailer mailer = new SmtpMailer();
        mailer.sendWelcomeEmail("test@example.com");
        
        System.out.println("---");
        
        mailer.sendPasswordResetEmail("test@example.com", "xyz123");

        // 下面的代码会编译失败，因为私有方法在外部不可见
        // mailer.connectAndAuthenticate(); // COMPILE ERROR!
        
        System.out.println("---");
        
        // 调用接口的静态方法
        Mailer.logStatus();
    }
}
```

**代码解读：**
*   `sendWelcomeEmail` 和 `sendPasswordResetEmail` 都需要执行连接和认证的逻辑。
*   我们将这部分重复的逻辑提取到了 `private void connectAndAuthenticate()` 中。
*   两个 `default` 方法都可以调用这个私有方法，但 `SmtpMailer` 类和 `Main` 方法都无法访问它。
*   同样，`public static` 方法 `logStatus()` 调用了 `private static` 方法 `log()` 来实现其功能。

#### 4. 扩展与应用 (Extension & Application)

*   **JDK 内部实现：** Java 核心库自身就大量使用了私有接口方法来重构代码。例如，`java.util.Collection` 接口的 `of()` 工厂方法背后，就有可能使用私有静态方法来处理复杂的创建逻辑。
*   **API 设计：** 在设计公共 API 时，这个特性让接口的设计者可以提供更丰富的默认功能，而不用担心实现细节的泄露会给使用者带来困惑。
*   **代码整洁性：** 任何时候，当你在接口中发现多个 `default` 方法有重复代码时，就应该考虑使用 `private` 方法来重构。

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **两种私有方法：**
    *   **`private` (实例) 方法：** 只能被接口中的 `default` (实例) 方法调用。
    *   **`private static` 方法：** 可以被接口中的 `default` (实例) 方法和 `static` 方法调用。

2.  **绝对封装：** 私有接口方法**不能**被实现类继承或重写。它们是接口纯粹的内部实现细节。

3.  **必须有方法体：** 与 `abstract` 方法不同，`private` 方法必须提供一个具体的方法体。`private abstract` 是非法的。

4.  **与抽象类的界限：** 虽然接口的功能越来越强大，但它和抽象类仍然有本质区别。接口主要定义**行为契约 (can-do)**，而抽象类主要定义**身份归属 (is-a)**。如果一个设计中包含了状态（成员变量）和复杂的、与状态相关的逻辑，那么抽象类通常是更好的选择。私有接口方法应被视为一种代码整理工具，而不是把接口变成一个“万能类”的手段。