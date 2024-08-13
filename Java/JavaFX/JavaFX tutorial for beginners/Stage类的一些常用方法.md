在 JavaFX 中，`Stage` 类提供了许多方法来控制应用程序窗口的行为和外观。以下是 `Stage` 类的一些常用方法及其功能：

### 1. **`show()`**
   - **描述**: 显示舞台并启动窗口的事件循环。
   - **用法**: 
     ```java
     stage.show();
     ```

### 2. **`close()`**
   - **描述**: 关闭舞台。关闭主舞台时，应用程序将退出。
   - **用法**: 
     ```java
     stage.close();
     ```

### 3. **`setTitle(String title)`**
   - **描述**: 设置舞台的标题，即窗口的标题栏文本。
   - **用法**: 
     ```java
     stage.setTitle("My Application");
     ```

### 4. **`setScene(Scene scene)`**
   - **描述**: 设置舞台的场景（`Scene`），即舞台的内容区域。
   - **用法**: 
     ```java
     stage.setScene(new Scene(root, 300, 250));
     ```

### 5. **`setWidth(double width)` 和 `setHeight(double height)`**
   - **描述**: 设置舞台的宽度和高度。
   - **用法**: 
     ```java
     stage.setWidth(400);
     stage.setHeight(300);
     ```

### 6. **`setResizable(boolean resizable)`**
   - **描述**: 设置舞台是否可以调整大小。
   - **用法**: 
     ```java
     stage.setResizable(false); // 禁止调整窗口大小
     ```

### 7. **`initModality(Modality modality)`**
   - **描述**: 设置舞台的模态模式。常见的模态模式包括：
     - `Modality.NONE`：非模态窗口。
     - `Modality.WINDOW_MODAL`：阻止与指定窗口的其他窗口交互。
     - `Modality.APPLICATION_MODAL`：阻止与应用程序中的所有窗口交互。
   - **用法**: 
     ```java
     stage.initModality(Modality.APPLICATION_MODAL);
     ```

### 8. **`initOwner(Window owner)`**
   - **描述**: 设置该舞台的所有者窗口。该方法通常与模态窗口一起使用。
   - **用法**: 
     ```java
     Stage newStage = new Stage();
     newStage.initOwner(primaryStage);
     ```

### 9. **`setFullScreen(boolean fullScreen)`**
   - **描述**: 设置舞台是否以全屏模式显示。
   - **用法**: 
     ```java
     stage.setFullScreen(true); // 启用全屏
     ```

### 10. **`setIconified(boolean iconified)`**
    - **描述**: 设置舞台是否最小化。
    - **用法**: 
      ```java
      stage.setIconified(true); // 最小化窗口
      ```

### 11. **`setMaximized(boolean maximized)`**
    - **描述**: 设置舞台是否最大化。
    - **用法**: 
      ```java
      stage.setMaximized(true); // 最大化窗口
      ```

### 12. **`setOpacity(double value)`**
    - **描述**: 设置舞台的透明度。取值范围为 `0.0`（完全透明）到 `1.0`（完全不透明）。
    - **用法**: 
      ```java
      stage.setOpacity(0.8); // 设置为80%透明度
      ```

### 13. **`setAlwaysOnTop(boolean value)`**
    - **描述**: 设置舞台是否总是在所有其他窗口之上显示。
    - **用法**: 
      ```java
      stage.setAlwaysOnTop(true); // 总是置顶
      ```

### 14. **`centerOnScreen()`**
    - **描述**: 将舞台居中显示在屏幕上。
    - **用法**: 
      ```java
      stage.centerOnScreen();
      ```

### 15. **`hide()`**
    - **描述**: 隐藏舞台，但不会销毁它。可以使用 `show()` 再次显示。
    - **用法**: 
      ```java
      stage.hide();
      ```

这些方法让你能够精细地控制 JavaFX 窗口的行为和外观，从而更好地满足应用程序的需求。