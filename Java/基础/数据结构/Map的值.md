在Java中，Map的值可以是Object类型，因此可以存储任何类型的对象，包括基本数据类型的包装类、自定义对象、集合、甚至其他的Map等。但是，在实际使用中，建议保持Map的值类型一致，以便于代码的可读性和维护性。

以下是一个示例，展示了如何在Map的值中存储多种类型的对象：

```java
import java.util.HashMap;
import java.util.Map;

public class MixedTypeMap {
    public static void main(String[] args) {
        Map<String, Object> mixedMap = new HashMap<>();

        mixedMap.put("name", "John Doe"); // String类型
        mixedMap.put("age", 30); // Integer类型
        mixedMap.put("isStudent", true); // Boolean类型

        // 自定义对象
        mixedMap.put("customObject", new CustomObject("Alice", 25));

        // 集合类型
        mixedMap.put("numbers", new int[]{1, 2, 3});

        // 打印Map中的值
        System.out.println("Mixed Map: " + mixedMap);
    }
}

class CustomObject {
    private String name;
    private int age;

    public CustomObject(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "CustomObject{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

```

在这个例子中，Map的值包含了String、Integer、Boolean、自定义对象CustomObject、以及一个int数组。虽然这种做法在某些情况下可能会很灵活，但是在代码设计时需要格外小心，确保不会造成混乱和不一致性。