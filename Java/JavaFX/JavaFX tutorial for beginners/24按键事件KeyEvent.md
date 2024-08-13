```java
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));
		Parent root = loader.load();	
		Controller controller = loader.getController();
		Scene scene = new Scene(root);		
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				switch(event.getCode()) {
				
				case W:
					controller.moveUp();
					break;
				case S:
					controller.moveDown();
					break;
				case A:
					controller.moveLeft();
					break;
				case D:
					controller.moveRight();
					break;
				default:
					break;
				}		
			}	
		});

		stage.setScene(scene);
		stage.show();
	}	

	public static void main(String[] args) {
		launch(args);
	}
}
```

```java
public class Controller{
	
	public void moveUp() {
		
		System.out.println("MOVIN' UP!");
	}
	
	public void moveDown() {
		
		System.out.println("MOVIN' DOWN!");
	}
	
	public void moveLeft() {
		
		System.out.println("MOVIN' LEFT!");
	}
	
	public void moveRight() {
		
		System.out.println("MOVIN' RIGHT!");
	}
}
```

这段 Java 代码是一个使用 JavaFX 框架构建的简单应用程序，它展示了如何加载 FXML 文件、设置场景、以及处理键盘事件。下面是代码的逐行解释：

```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));
Parent root = loader.load();	
Controller controller = loader.getController();
Scene scene = new Scene(root);	
```

- **`FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));`**：创建一个 `FXMLLoader` 对象，并从资源路径加载 `Scene.fxml` 文件。
- **`Parent root = loader.load();`**：加载 `Scene.fxml` 并返回根节点对象 `Parent`。
- **`Controller controller = loader.getController();`**：获取与 FXML 文件关联的控制器类实例 `Controller`。
- **`Scene scene = new Scene(root);`**：使用根节点创建一个新的 `Scene` 对象。

### 处理键盘事件

```java
scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

	@Override
	public void handle(KeyEvent event) {
		
		switch(event.getCode()) {
		
		case W:
			controller.moveUp();
			break;
		case S:
			controller.moveDown();
			break;
		case A:
			controller.moveLeft();
			break;
		case D:
			controller.moveRight();
			break;
		default:
			break;
		}		
	}	
});
```

- **`scene.setOnKeyPressed(new EventHandler<KeyEvent>() { ... })`**：为场景设置键盘按下事件的处理器，当用户在场景中按下键时，触发事件。
- **`handle(KeyEvent event)`**：重写 `EventHandler` 接口的 `handle` 方法，处理键盘事件。
- **`switch(event.getCode())`**：根据按下的键码（`KeyCode`）来判断具体的按键。
- **`controller.moveUp();`**等方法：调用控制器中的方法，如 `moveUp()`、`moveDown()` 等，处理相应的动作。例如，W 键触发 `moveUp()` 方法，可能是移动某个游戏角色向上。

### 显示舞台

```java
stage.setScene(scene);
stage.show();
```

- **`stage.setScene(scene);`**：将创建的 `Scene` 设置为 `Stage` 的当前场景。
- **`stage.show();`**：显示舞台（窗口）。

### `main` 方法

```java
public static void main(String[] args) {
	launch(args);
}
```

- **`launch(args)`**：启动 JavaFX 应用程序，这是 `Application` 类的静态方法，会调用 `start(Stage)` 方法。

### 总结

这段代码实现了一个简单的 JavaFX 应用，利用 `FXMLLoader` 加载 FXML 文件，创建一个场景，并将其设置到主舞台中。程序还设置了键盘事件监听器，用于捕捉 `W`、`A`、`S`、`D` 键的按下事件，并调用控制器中的相应方法来处理这些事件（例如，移动角色）。