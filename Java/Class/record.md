Java 中的 `record` 类（自 Java 16 正式引入）是一种特殊的类，旨在**简化不可变数据载体的定义**。它通过自动生成标准方法（如 `equals()`、`hashCode()`、`toString()` 等）和不可变字段，显著减少了样板代码。以下是详细说明：

---

### **1. `record` 的核心作用**
- **不可变性**：所有字段默认为 `final`，确保实例不可变。
- **数据透明性**：字段通过访问器方法（如 `fieldName()`）公开。
- **自动生成方法**：包括构造方法、`equals()`、`hashCode()`、`toString()`。
- **简化代码**：替代传统的 POJO（如 DTO、VO）的冗长定义。

---

### **2. 基本用法**
#### 定义 `record`：
```java
public record Point(int x, int y) {}
```
- **组件**：`x` 和 `y` 是 `record` 的组件，自动成为 `private final` 字段。
- **构造方法**：自动生成规范构造方法（参数顺序与组件声明一致）。
- **访问器**：生成 `x()` 和 `y()` 方法，直接返回字段值。

#### 使用示例：
```java
Point p = new Point(3, 4);
System.out.println(p.x()); // 输出 3
System.out.println(p);     // 输出 Point[x=3, y=4]
```

---

### **3. 自定义构造方法**
#### 紧凑构造方法（无参数列表）：
```java
public record Point(int x, int y) {
    public Point { // 紧凑构造方法，无需写参数列表
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("坐标不能为负数");
        }
    }
}
```

#### 重载构造方法：
```java
public record Point(int x, int y) {
    public Point() { // 委托给主构造方法
        this(0, 0);
    }
}
```

---

### **4. 方法重写与添加**
#### 重写自动生成的方法：
```java
public record Point(int x, int y) {
    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}
```

#### 添加自定义方法：
```java
public record Point(int x, int y) {
    public double distanceToOrigin() {
        return Math.sqrt(x * x + y * y);
    }
}
```

---

### **5. 嵌套使用**
#### `record` 嵌套其他 `record`：
```java
public record Address(String city, String street) {}

public record Person(String name, int age, Address address) {}

// 使用
Address addr = new Address("北京", "长安街");
Person person = new Person("张三", 30, addr);
System.out.println(person.address().city()); // 输出 "北京"
```

#### 内部 `record`：
```java
public class Outer {
    public record InnerRecord(int id, String value) {}
}

// 使用
Outer.InnerRecord inner = new Outer.InnerRecord(1, "test");
```

---

### **6. 其他特性**
#### 实现接口：
```java
public record Point(int x, int y) implements Serializable {
    // 自动生成字段和方法，同时实现 Serializable
}
```

#### 静态成员：
```java
public record Point(int x, int y) {
    public static final Point ORIGIN = new Point(0, 0);
    
    public static Point of(int x, int y) {
        return new Point(x, y);
    }
}
```

---

### **7. 注意事项**
1. **不可变限制**：所有字段为 `final`，无法直接修改。
2. **不能继承其他类**：但可以实现多个接口。
3. **组件不可变**：`record` 的组件必须是不可变类型（如基本类型、`String`、其他 `record`）。
4. **反射限制**：`record` 的构造方法在反射中无法直接调用。

---

### **8. 典型应用场景**
- 数据传输对象（DTO）
- 方法返回多个值（替代 `Pair` 或自定义类）
- 模式匹配（Java 17+ 的 `switch` 表达式）
- 临时数据聚合（如解析 JSON 的中间结果）

---

通过合理使用 `record`，可以显著减少样板代码，提高代码可读性，同时确保数据的不可变性。