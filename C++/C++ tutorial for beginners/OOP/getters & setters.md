```cpp
#include <iostream>

class Stove{
    private:
        int temperature = 0;
    public:
	Stove(int temperature){
		setTemperature(temperature);
	}
    int getTemperature(){
        return temperature;
    }
    void setTemperature(int temperature){
        if(temperature < 0){
            this->temperature = 0;
        }
        else if(temperature >= 10){
            this->temperature = 10;
        }
        else{
            this->temperature = temperature;
        }
    }
};

int main() {

    Stove stove(0);
	std::cout << "The temperature setting is: " << stove.getTemperature();
    stove.setTemperature(5);

    std::cout << "The temperature setting is: " << stove.getTemperature();

    return 0;
}
```

## C++ 中的 getters 和 setters

在 C++ 中，getters 和 setters 是用于访问和修改类成员变量的函数。它们提供了一种封装数据的方式，有助于保护数据的一致性和完整性。

### getters（获取器）

- **作用：** 获取私有成员变量的值。
- **语法：**
    
    ```C++
    数据类型 get成员变量名() {
        return 成员变量名;
    }
    ```
    
- **示例：**
    
    ```C++
    class Person {
    private:
        std::string name;
        int age;
    public:
        std::string getName() const {
            return name;
        }
        int getAge() const {
            return age;
        }
    };
    ```
    

### setters（设置器）

- **作用：** 修改私有成员变量的值。
- **语法：**
    
    ```C++
    void set成员变量名(数据类型 newValue) {
        成员变量名 = newValue;
    }
    ```
    
- **示例：**
    
    ```C++
    class Person {
    // ... (同上)
    public:
        void setName(const std::string& newName) {
            name = newName;
        }
        void setAge(int newAge) {
            if (newAge >= 0) {
                age = newAge;
            } else {
                // 处理无效输入
            }
        }
    };
    ```
    

### 为什么使用 getters 和 setters？

- **封装性：** 隐藏了类的内部实现细节，外部代码只能通过 getters 和 setters 来访问和修改数据。
- **数据验证：** 可以对传入的数据进行验证，确保数据的合法性。
- **灵活性：** 可以对 getters 和 setters 进行重载，实现不同的行为。
- **可维护性：** 提高了代码的可读性和可维护性。

### 注意事项

- **const 关键字：** 在 getters 中使用 `const` 关键字可以保证成员函数不会修改对象的状态。
- **引用传递：** 对于大型对象，使用引用传递可以避免不必要的拷贝。
- **返回值：** getters 的返回值类型应该与成员变量的类型一致。
- **参数验证：** setters 应该对传入的参数进行验证，防止非法数据导致程序错误。

### 示例：

```C++
Person person;
person.setName("Alice");
person.setAge(30);

std::cout << person.getName() << " is " << person.getAge() << " years old." << std::endl;
```

### 总结

getters 和 setters 是面向对象编程中的重要概念，它们通过封装数据来提高代码的质量和可维护性。合理地使用 getters 和 setters 可以使你的 C++ 代码更加健壮和优雅。

## getters 和 setters 的高级用法

getters 和 setters 在 C++ 中是一个基础且强大的概念，除了基本的访问和修改数据，它们还有很多高级的用法，可以使你的代码更加灵活和高效。

### 1. **属性（Property）**

- **概念：** 将 getter 和 setter 组合起来，作为一个属性来访问。
- **优势：** 提高代码的可读性，更像是在访问一个变量。
- **示例：**
    
    ```C++
    class Person {
    private:
    	std::string name_;
    public:
        std::string getName() const { return name_; }
        void setName(const std::string& name) { name_ = name; }
    
        // 属性
        std::string name() const { 
        	return getName(); 
        }
        void name(const std::string& name) { 
        	setName(name); 
        }
    };
    ```
    
    使用时：
    
    ```C++
    Person person;
    person.name("Alice"); // 等同于 person.setName("Alice");
    ```
    

### 2. **延迟加载（Lazy Loading）**

- **概念：** 在需要时才初始化数据，提高性能。
- **应用场景：** 数据量大，初始化耗时较长。
- **示例：**
    
    ```C++
    class Data {
    private:
    	bool isLoaded_ = false;
    	int value_;
    public:
        int getValue() {
            if (!isLoaded_) {
                // 从数据库或其他地方加载数据
                value_ = loadData();
                isLoaded_ = true;
            }
            return value_;
        }
    };
    ```
    

### 3. **计算属性**

- **概念：** getter 返回值不是直接存储的成员变量，而是通过计算得到的。
- **示例：**
    
    ```C++
    class Circle {
    private:
    	double radius_;
    public:
        double getArea() const {
            return 3.14159 * radius_ * radius_;
        }
    };
    ```
    

### 4. **可观察属性（Observable Property）**

- **概念：** 当属性值发生变化时，可以触发一些事件或通知。
- **应用场景：** 构建用户界面，数据绑定等。
- **实现方式：** 使用信号槽机制（如 Qt）、观察者模式等。

### 5. **自定义逻辑**

- **验证：** 在 setter 中进行数据验证，确保数据的合法性。
- **转换：** 在 getter 或 setter 中进行数据类型转换。
- **缓存：** 在 getter 中缓存计算结果，提高性能。

### 6. **与其他设计模式结合**

- **工厂模式：** 在 getter 中返回不同类型的对象。
- **策略模式：** 在 getter 中根据不同的条件返回不同的实现。
- **状态模式：** 在 getter 中根据对象的状态返回不同的值。

### 注意事项

- **性能：** 过多的 getters 和 setters 会带来性能开销，尤其是在频繁调用时。
- **可读性：** 合理命名 getters 和 setters，提高代码的可读性。
- **设计模式：** 根据具体需求选择合适的设计模式，避免过度设计。

### 总结

getters 和 setters 不仅仅是简单的访问和修改数据，它们可以与其他设计模式和语言特性结合，实现更加灵活和强大的功能。合理地使用 getters 和 setters，可以使你的 C++ 代码更加优雅和高效。