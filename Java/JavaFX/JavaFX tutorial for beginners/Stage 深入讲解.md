## JavaFX Stage 深入讲解

### 什么是 Stage？

在 JavaFX 中，**Stage** 代表一个应用程序窗口。它是最顶层的容器，所有的用户界面元素都最终会显示在 Stage 上。你可以把它想象成一个舞台，而你的 UI 元素就是在这个舞台上表演的演员。

### Stage 的主要作用

- **窗口容器：** Stage 是所有场景（Scene）的容器。一个 Stage 可以包含多个 Scene，但同一时间只能显示一个 Scene。
- **窗口属性设置：** 你可以通过 Stage 设置窗口的标题、大小、位置、图标、是否可调整大小、是否可关闭等属性。
- **窗口事件处理：** 可以为 Stage 添加事件监听器，处理窗口的各种事件，比如窗口关闭事件、窗口大小改变事件等。
- **多窗口应用程序：** 通过创建多个 Stage 实例，可以实现多窗口应用程序。

### Stage 的常用方法

- **setTitle(String title):** 设置窗口标题。
- **setScene(Scene scene):** 将一个 Scene 设置到 Stage 上。
- **show():** 显示窗口。
- **hide():** 隐藏窗口。
- **close():** 关闭窗口。
- **setMaxHeight(double height):** 设置窗口最大高度。
- **setMaxWidth(double width):** 设置窗口最大宽度。
- **setMinHeight(double height):** 设置窗口最小高度。
- **setMinWidth(double width):** 设置窗口最小宽度。
- **setResizable(boolean resizable):** 设置窗口是否可调整大小。
- **setIconified(boolean iconified):** 设置窗口是否最小化。

### 创建一个简单的 Stage

```Java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public    class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        //    创建一个根布局
        StackPane root = new StackPane();

        // 创建一个场景
        Scene scene = new Scene(root, 300, 250);

        // 设置窗口标题
        primaryStage.setTitle("Hello, JavaFX!");

        // 将场景设置到舞台上
        primaryStage.setScene(scene);

        // 显示窗口
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

### Stage 和 Scene 的关系

- **一个 Stage 可以包含多个 Scene:** 但同一时间只能显示一个 Scene。
- **Scene 是 Stage 的内容:** Scene 是 Stage 的视觉内容，包含了所有的 UI 元素。
- **Stage 控制 Scene 的显示:** 通过 Stage 的 show() 和 hide() 方法可以控制 Scene 的显示和隐藏。

### Stage 的更多特性

- **StageStyle:** 可以设置窗口的样式，比如是否显示标题栏、边框等。
- **Modality:** 可以设置窗口的模态性，模态窗口会阻止用户与其他窗口交互。
- **Owner:** 可以设置一个 Stage 的所有者，用于控制窗口之间的关系。

### 示例：创建多个 Stage

```Java
// 创建第二个 Stage
Stage secondaryStage = new Stage();
secondaryStage.setTitle("Secondary Stage");
// ... 设置第二个 Stage 的其他属性
```

### 总结

Stage 是 JavaFX 应用程序的窗口容器，它提供了对窗口的各种控制。理解 Stage 的概念和用法是构建 JavaFX 应用程序的基础。