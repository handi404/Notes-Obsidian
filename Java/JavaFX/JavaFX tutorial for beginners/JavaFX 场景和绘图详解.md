## JavaFX 场景和绘图详解

### JavaFX 场景（Scene）

JavaFX 场景是应用程序用户界面的根节点，它是一棵层次结构的树，每个节点代表一个 UI 元素。场景可以处理输入并被渲染。

**场景的主要组成部分：**

- **节点 (Node)：** 场景中的基本元素，可以是各种形状、文本、图像、容器等。
- **场景图 (Scene Graph)：** 所有节点组成的树状结构，用于描述 UI 的布局和层次关系。
- **事件处理：** 场景可以处理鼠标、键盘等事件，实现用户交互。
- **渲染：** 场景被渲染成最终的视觉效果。

**场景的创建：**

```Java
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

// 创建一个空的 Pane 作为根节点
Pane root = new Pane();

// 创建一个场景，指定根节点和大小
Scene scene = new Scene(root, 600, 400);
```

### JavaFX 绘图

JavaFX 提供了丰富的绘图 API，可以创建各种形状、图形和文本。

**常用的绘图类：**

- **Shape：** 抽象类，是所有形状的基类。
    - **Circle：** 圆形
    - **Rectangle：** 矩形
    - **Ellipse：** 椭圆
    - **Line：** 直线
    - **Polygon：** 多边形
    - **Path：** 路径
- **Text：** 文本
- **Image：** 图片

**绘图示例：**

```Java
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

// 创建一个圆形
Circle circle = new Circle(100, 100, 50);
circle.setFill(Color.BLUE);
circle.setStroke(Color.BLACK);

// 将圆形添加到场景的根节点
root.getChildren().add(circle);
```

**坐标系统：**

- **原点：** 坐标系统的左上角
- **X 轴：** 水平向右
- **Y 轴：** 垂直向下

**绘图属性：**

- **填充色 (fill)：** 设置形状内部的颜色
- **描边色 (stroke)：** 设置形状边框的颜色
- **描边宽度 (strokeWidth)：** 设置描边线的宽度
- **变换 (transform)：** 缩放、旋转、平移等变换

### 深入绘图

- **Canvas：** 用于自定义绘制，提供一个画布，可以在上面使用 Java2D API 进行绘制。
- **GraphicsContext：** Canvas 的绘图上下文，提供了丰富的绘图方法。
- **特效：** JavaFX 提供了各种特效，如阴影、模糊等。

### 示例：绘制一个简单的图形

```Java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shap   e.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class    DrawShapes extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // 绘制一个矩形
        Rectangle rect = new Rectangle(50, 50, 100, 100);
        rect.setFill(Color.GREEN);

        // 绘制一个圆形
        Circle circle = new Circle(200, 100, 50);
        circle.setFill(Color.RED);

        root.getChildren().addAll(rect, circle);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("绘制图形");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

### 总结

JavaFX 提供了强大的绘图功能，可以创建各种各样的图形界面。通过深入学习场景、节点、绘图类和坐标系统，可以实现复杂的图形绘制和动画效果。

**更多学习资源：**

- **官方文档：** Oracle 官方 JavaFX 文档
- **示例代码：** GitHub 上有很多 JavaFX 示例项目
- **社区：** Stack Overflow 等社区可以找到很多关于 JavaFX 的问题和解答