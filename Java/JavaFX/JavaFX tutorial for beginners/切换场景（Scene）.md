在 JavaFX 中，切换场景（Scene）是一个常见的需求，通常用于在同一个应用程序中显示不同的界面内容。你可以通过以下步骤实现场景切换：

### 1. **准备两个或多个 FXML 文件**
   假设你有两个 FXML 文件，分别对应两个不同的场景，比如 `Scene1.fxml` 和 `Scene2.fxml`。每个 FXML 文件定义了不同的界面布局。

### 2. **加载 FXML 文件并切换场景**
   你可以在控制器类中通过加载不同的 FXML 文件来切换场景。以下是一个简单的示例：

   假设当前场景是 `Scene1.fxml`，你希望点击一个按钮后切换到 `Scene2.fxml`。

   ```java
   import javafx.fxml.FXMLLoader;
   import javafx.scene.Parent;
   import javafx.scene.Scene;
   import javafx.stage.Stage;
   import javafx.event.ActionEvent;
   import javafx.scene.Node;

   public class Scene1Controller {
       // 在按钮点击事件中切换场景
       public void switchToScene2(ActionEvent event) throws Exception {
           // 加载 Scene2.fxml
           Parent root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
           
           // 获取当前窗口 (Stage)
           Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           
           // 创建一个新场景并设置到当前窗口
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
       }
   }
   ```

   ### 详细步骤说明：
   
   1. **加载新的 FXML 文件**：
      ```java
      Parent root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
      ```
      `FXMLLoader.load()` 方法用于加载 `Scene2.fxml` 文件并返回其根节点。`Parent` 是 JavaFX 中所有场景图形组件的基类。

   2. **获取当前的 `Stage` 对象**：
      ```java
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      ```
      使用 `event.getSource()` 获取当前触发事件的组件，并通过该组件获取当前的 `Scene` 和 `Stage`。`Stage` 是 JavaFX 应用的顶层容器，表示一个窗口。

   3. **创建新的 `Scene` 并设置 `Stage`**：
      ```java
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
      ```
      创建一个新的 `Scene`，并将其设置为当前 `Stage` 的场景。然后调用 `stage.show()` 来显示新场景。

### 3. **在 FXML 文件中定义按钮事件**
   在 `Scene1.fxml` 中，你可以为按钮绑定 `switchToScene2` 方法：

   ```xml
   <Button text="Go to Scene 2" onAction="#switchToScene2" />
   ```

   这段代码表示，当用户点击这个按钮时，将调用 `Scene1Controller` 类中的 `switchToScene2` 方法。

### 4. **在 Main 类中启动初始场景**
   在 JavaFX 应用的 `Main` 类中，你可以指定应用启动时加载的初始场景：

   ```java
   import javafx.application.Application;
   import javafx.fxml.FXMLLoader;
   import javafx.scene.Parent;
   import javafx.scene.Scene;
   import javafx.stage.Stage;

   public class Main extends Application {
       @Override
       public void start(Stage primaryStage) throws Exception {
           Parent root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
           primaryStage.setTitle("Scene Switching Example");
           primaryStage.setScene(new Scene(root));
           primaryStage.show();
       }

       public static void main(String[] args) {
           launch(args);
       }
   }
   ```

### 5. **返回原场景或切换到其他场景**
   如果需要在 `Scene2.fxml` 中切换回 `Scene1.fxml`，可以在 `Scene2Controller` 中编写类似的代码：

   ```java
   public class Scene2Controller {
       public void switchToScene1(ActionEvent event) throws Exception {
           Parent root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
           Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
       }
   }
   ```

### 6. **使用多个控制器类**
   每个 FXML 文件通常都有自己的控制器类，用于处理该场景中的交互逻辑。在上面的例子中，`Scene1.fxml` 和 `Scene2.fxml` 分别对应 `Scene1Controller` 和 `Scene2Controller` 类。

### 总结
通过这些步骤，你可以在 JavaFX 应用程序中轻松实现场景切换。核心步骤是通过 `FXMLLoader` 加载新的 FXML 文件，并将其设置为当前 `Stage` 的场景。这种方法可以用于构建复杂的应用程序，如多窗口、导航流程和不同功能模块的界面切换。