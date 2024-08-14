`@FXML` 是 JavaFX 框架中一个常用的**注解**，它主要用于将 FXML 文件中定义的 UI 组件与控制器类中的字段和方法进行绑定。具体来说，`@FXML` 有以下主要作用：

### 1. **与 FXML 文件中的 UI 组件绑定**
   - 当你在 FXML 文件中定义了一个 UI 组件（如 `Button`、`TextField` 等），并希望在控制器类中操作这个组件，你需要在控制器类中定义一个与该组件对应的字段，并使用 `@FXML` 注解。
   - **示例**：
     ```xml
     <!-- 在FXML文件中 -->
     <Button fx:id="myButton" text="Click Me" />
     ```
     ```java
     // 在控制器类中
     public class MyController {
         @FXML
         private Button myButton;  // 通过 @FXML 注解，myButton 与 FXML 文件中的 Button 绑定
     }
     ```
     上面代码中，`@FXML` 注解将 `myButton` 字段与 FXML 文件中具有相同 `fx:id` 的 `Button` 组件绑定在一起。这样，你可以在控制器类中直接使用 `myButton` 字段来访问和操作这个按钮。

### 2. **与 FXML 文件中的事件处理方法绑定**
   - 除了字段绑定外，`@FXML` 注解还可以用于方法绑定。你可以在 FXML 文件中定义某个 UI 组件的事件（如按钮的点击事件），然后在控制器类中使用 `@FXML` 注解标记对应的事件处理方法。
   - **示例**：
     ```xml
     <!-- 在FXML文件中 -->
     <Button text="Click Me" onAction="#handleButtonClick" />
     ```
     ```java
     // 在控制器类中
     public class MyController {
         @FXML
         private void handleButtonClick() {
             // 处理按钮点击事件的逻辑
             System.out.println("Button clicked!");
         }
     }
     ```
     这里，`handleButtonClick` 方法被 `@FXML` 注解标记，并与 FXML 文件中按钮的 `onAction` 事件绑定。当用户点击按钮时，`handleButtonClick` 方法就会被调用。

### 3. **FXML 文件中组件的私有访问权限**
   - 在控制器类中，如果你希望将 FXML 文件中的组件字段或事件处理方法定义为 `private`（私有），你仍然可以通过 `@FXML` 注解让 FXML 文件访问这些字段或方法。
   - **示例**：
     ```java
     public class MyController {
         @FXML
         private Button myButton;  // 通过 @FXML 注解，即使字段是私有的，FXML 仍然可以访问它

         @FXML
         private void handleButtonClick() {  // 私有方法也可以通过 @FXML 注解绑定到 FXML 文件中的事件
             System.out.println("Button clicked!");
         }
     }
     ```
     使用 `@FXML` 注解，可以在控制器类中安全地使用私有字段和方法，这有助于封装控制器的实现细节，遵循面向对象编程中的封装原则。

### 4. **加载 FXML 文件时的反射机制**
   - 在 JavaFX 中，当你使用 `FXMLLoader` 加载 FXML 文件时，JavaFX 通过反射机制自动将 FXML 文件中的组件与控制器类中的字段和方法进行绑定。`@FXML` 注解标记的字段和方法会被反射机制识别，并自动完成绑定。
   - **示例**：
     ```java
     FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
     loader.setController(new MyController());
     Parent root = loader.load();
     ```
     通过 `FXMLLoader` 加载 FXML 文件时，`@FXML` 注解标记的字段和方法将被自动绑定到 FXML 文件中的相应组件和事件。

### 总结
`@FXML` 注解在 JavaFX 应用程序中用于：
1. 将控制器类中的字段与 FXML 文件中的 UI 组件绑定，使得你可以在控制器类中直接操作这些组件。
2. 将控制器类中的方法与 FXML 文件中的事件处理绑定，使得你可以在控制器类中编写事件处理逻辑。
3. 允许控制器类中的私有字段和方法通过 FXML 进行绑定和访问。

这个注解是 JavaFX 中实现视图与控制器分离的重要工具，使得你可以在控制器类中更好地管理和操作 UI 组件。