[[事件监听器]]
# 
- [[#属性监听 （Property Listener）|属性监听 （Property Listener）]]
	- [[#属性监听 （Property Listener）#1. 使用 `ChangeListener` 监听属性变化|1. 使用 `ChangeListener` 监听属性变化]]
	- [[#属性监听 （Property Listener）#2. 使用 `InvalidationListener`|2. 使用 `InvalidationListener`]]
- [[#属性绑定（Property Binding）|属性绑定（Property Binding）]]
	- [[#属性绑定（Property Binding）#3. 使用 `Bindings` 类进行绑定|3. 使用 `Bindings` 类进行绑定]]
- [[#4. 自定义属性监听器|4. 自定义属性监听器]]
	- [[#4. 自定义属性监听器#总结|总结]]

在 JavaFX 中，监听值的变化可以通过以下几种方式实现，具体取决于你要监听的值的类型。JavaFX 提供了一套强大的绑定（Binding）和监听（Listener）机制，方便地处理 UI 组件属性的变化。

## 属性监听 （Property Listener）
### 1. 使用 `ChangeListener` 监听属性变化

`ChangeListener` 是 JavaFX 中最常用的接口，用于监听某个属性值的变化。当属性的值发生变化时，会自动调用 `changed` 方法。

**示例：监听 `Slider` 的值变化**

假设我们有一个滑块（`Slider`），并希望在用户拖动滑块时实时监听滑块的值变化。

```java
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class Controller {

    @FXML
    private Slider mySlider;

    @FXML
    private Label myLabel;

    public void initialize() {
        mySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                myLabel.setText(String.format("%.2f", newValue.doubleValue()));
            }
        });
    }
}
```

**代码说明**：
- `mySlider.valueProperty().addListener(...)`：为滑块的值属性添加监听器。
- `changed` 方法：每当滑块的值发生变化时，该方法会被调用，其中 `newValue` 表示新的滑块值。
- `myLabel.setText(...)`：将新值显示在标签上。

### 2. 使用 `InvalidationListener`

`InvalidationListener` 是另一种监听接口，用于监听某个属性的无效化（状态变化）。它的使用场景与 `ChangeListener` 类似，但不会提供新旧值的具体信息。

**示例：使用 `InvalidationListener` 监听 `ProgressBar` 的进度**

```java
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class Controller {

    @FXML
    private ProgressBar myProgressBar;

    @FXML
    private Label myLabel;

    public void initialize() {
        myProgressBar.progressProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                myLabel.setText(String.format("%.0f%%", myProgressBar.getProgress() * 100));
            }
        });
    }
}
```

**代码说明**：
- `myProgressBar.progressProperty().addListener(...)`：为进度条的进度属性添加监听器。
- `invalidated` 方法：每当进度条的进度属性变得无效（即发生变化）时，该方法会被调用。
- `myProgressBar.getProgress()`：获取当前进度，并将其格式化为百分比显示。

## 属性绑定（Property Binding）
### 3. 使用 `Bindings` 类进行绑定

除了使用 `Listener` 直接监听值的变化，JavaFX 还提供了 `Bindings` 类，可以将一个 UI 组件的属性与另一个属性绑定在一起，使得它们的值自动同步。

**示例：将 `Label` 的文本与 `Slider` 的值绑定**

```java
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class Controller {

    @FXML
    private Slider mySlider;

    @FXML
    private Label myLabel;

    public void initialize() {
        myLabel.textProperty().bind(Bindings.format("%.2f", mySlider.valueProperty()));
    }
}
```

**代码说明**：
- `myLabel.textProperty().bind(...)`：将 `Label` 的文本属性与 `Slider` 的值属性绑定。
- `Bindings.format("%.2f", mySlider.valueProperty())`：格式化滑块的值为两位小数，并将其绑定到标签的文本属性上。

属性绑定是一种将一个属性的值绑定到另一个属性上的机制。当源属性的值发生变化时，目标属性的值也会自动更新。

- **基本用法:**
   ```java
   import javafx.beans.property.SimpleStringProperty;
   
   SimpleStringProperty nameProperty = new SimpleStringProperty("Alice");
   label.textProperty().bind(nameProperty);
   ```
上述代码将一个Label控件的文本属性绑定到一个字符串属性上。当nameProperty的值发生变化时，label的文本也会随之更新。
    
- **双向绑定:**
```java
textField.textProperty().bindBidirectional(nameProperty);
```
双向绑定意味着两个属性的值会相互影响，修改其中一个，另一个也会随之改变。


## 4. 自定义属性监听器

有时候你可能需要监听自定义对象的属性变化，可以通过扩展 `JavaFX` 的 `Property` 类实现自定义属性，并添加监听器。

**示例：自定义对象属性监听**

```java
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Person {
    private IntegerProperty age = new SimpleIntegerProperty();

    public int getAge() {
        return age.get();
    }

    public void setAge(int value) {
        age.set(value);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.ageProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("Age changed from " + oldValue + " to " + newValue);
            }
        });

        person.setAge(25); // 会触发监听器
        person.setAge(30); // 会触发监听器
    }
}
```

**代码说明**：
- `age` 是一个 `IntegerProperty`，它是 JavaFX 提供的可观察属性类型之一。
- `ageProperty().addListener(...)`：为 `age` 属性添加监听器，当属性的值发生变化时，会输出旧值和新值。

### 总结

- **`ChangeListener`**：用于监听属性值的变化，提供新旧值。
- **`InvalidationListener`**：用于监听属性的无效化，不提供新旧值的具体信息。
- **`Bindings`**：通过绑定机制将一个属性的值自动同步到另一个属性。
- **自定义属性监听**：通过 JavaFX 的 `Property` 类可以创建可监听的自定义属性。

这些机制可以帮助你实时地响应 UI 组件属性的变化，创建动态交互的用户界面。