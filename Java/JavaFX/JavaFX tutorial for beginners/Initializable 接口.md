`Initializable` 是 JavaFX 中的一个接口，用于在控制器类中初始化界面组件和数据。当你实现了这个接口并重写 `initialize` 方法时，JavaFX 在加载完对应的 FXML 文件并创建出所有的控件对象后，会自动调用 `initialize` 方法。这个方法通常用于在界面加载完成后进行一些初始化操作，比如设置组件的默认值、绑定事件监听器，或是加载一些初始数据。

### `Initializable` 接口的主要作用

1. **初始化界面组件**：在 `initialize` 方法中，你可以对界面组件进行各种初始化操作，比如设置默认值、添加事件监听器、初始化选择框的选项等。
  
2. **确保在界面加载后执行**：`initialize` 方法是在 FXML 文件被加载并且所有的 `@FXML` 注释的字段都被注入后才执行的。这保证了在你尝试操作这些组件时，它们已经全部被实例化。

3. **与 FXML 文件结合使用**：通常，JavaFX 应用程序的界面是通过 FXML 文件定义的。控制器类负责处理这些界面的逻辑。`Initializable` 接口提供了一个标准的机制来初始化这些逻辑。

### 代码示例

假设我们有一个简单的 JavaFX 应用程序，其中有一个按钮和一个标签。我们希望在应用程序启动时，标签显示一个初始消息，并且点击按钮后，标签会更新为另一个消息。

**FXML 文件 (`sample.fxml`)：**
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="sample.Controller">
   <children>
      <Label fx:id="myLabel" text="Initial Text"/>
      <Button text="Click Me" onAction="#handleButtonClick"/>
   </children>
</VBox>
```

**控制器类 (`Controller.java`)：**
```java
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller implements Initializable {

    @FXML
    private Label myLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myLabel.setText("Welcome to JavaFX!"); // 初始化标签的文本
    }

    @FXML
    private void handleButtonClick() {
        myLabel.setText("Button clicked!"); // 按钮点击后更新标签的文本
    }
}
```

**关键点解析：**

1. **`initialize` 方法的使用**：
   - 在 `Controller` 类中实现了 `Initializable` 接口，并重写了 `initialize` 方法。
   - 当 FXML 文件加载完成后，`initialize` 方法自动执行，将标签 `myLabel` 的文本初始化为 `"Welcome to JavaFX!"`。

2. **`@FXML` 注解的作用**：
   - `@FXML` 注解用于标记控制器类中的成员变量和方法，这些变量和方法对应于 FXML 文件中的 UI 组件和事件处理函数。确保这些组件在 `initialize` 方法中可用。

3. **事件处理**：
   - `handleButtonClick` 方法通过 `@FXML` 注解标记，并在 FXML 文件中引用。当用户点击按钮时，JavaFX 会自动调用这个方法。

### 总结
`Initializable` 接口在 JavaFX 应用中用于初始化控制器类。它的 `initialize` 方法在 FXML 文件加载完成后自动调用，确保所有的 UI 组件都已实例化并准备好进行操作。这种机制非常适合用来设置初始数据、绑定事件监听器以及执行其他需要在界面加载后立即进行的操作。