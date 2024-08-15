JUnit 5 提供了丰富的断言方法（Assertions）来帮助开发者验证测试结果是否符合预期。这些断言方法位于 `org.junit.jupiter.api.Assertions` 类中。通过断言，可以方便地检查代码的行为，并在测试失败时提供有用的调试信息。下面是 JUnit 5 中常用的断言方法的详细介绍。

### 1. **assertEquals**

`assertEquals` 用于比较两个值是否相等。可以用于比较基本类型、对象、数组等。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyTests {

    @Test
    void testEquals() {
        assertEquals(2, 1 + 1, "1 + 1 should equal 2");
    }
}
```

- **语法**:
  ```java
  assertEquals(expected, actual);
  assertEquals(expected, actual, message);
  assertEquals(expected, actual, supplierMessage);
  ```

- **参数**:
  - `expected`: 预期的值。
  - `actual`: 实际的值。
  - `message`: 当断言失败时显示的消息（可选）。
  - `supplierMessage`: 以 `Supplier` 形式提供的消息，只有在断言失败时才会生成（可选）。

### 2. **assertNotEquals**

`assertNotEquals` 用于验证两个值不相等。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MyTests {

    @Test
    void testNotEquals() {
        assertNotEquals(2, 1 + 2, "1 + 2 should not equal 2");
    }
}
```

### 3. **assertTrue 和 assertFalse**

`assertTrue` 用于验证条件为 `true`，而 `assertFalse` 用于验证条件为 `false`。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MyTests {

    @Test
    void testTrue() {
        assertTrue(5 > 2, "5 should be greater than 2");
    }

    @Test
    void testFalse() {
        assertFalse(5 < 2, "5 should not be less than 2");
    }
}
```

### 4. **assertNull 和 assertNotNull**

`assertNull` 用于验证对象为 `null`，而 `assertNotNull` 用于验证对象不为 `null`。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MyTests {

    @Test
    void testNull() {
        Object obj = null;
        assertNull(obj, "Object should be null");
    }

    @Test
    void testNotNull() {
        Object obj = new Object();
        assertNotNull(obj, "Object should not be null");
    }
}
```

### 5. **assertThrows**

`assertThrows` 用于验证执行代码块时抛出了指定类型的异常。这个方法非常适合用于异常测试。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyTests {

    @Test
    void testException() {
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Invalid argument");
        });
    }
}
```

- **语法**:
  ```java
  assertThrows(expectedType, executable);
  assertThrows(expectedType, executable, message);
  assertThrows(expectedType, executable, supplierMessage);
  ```

- **参数**:
  - `expectedType`: 预期的异常类型。
  - `executable`: 抛出异常的代码块。
  - `message`: 当断言失败时显示的消息（可选）。
  - `supplierMessage`: 以 `Supplier` 形式提供的消息，只有在断言失败时才会生成（可选）。

### 6. **assertDoesNotThrow**

`assertDoesNotThrow` 用于验证执行代码块时未抛出任何异常。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MyTests {

    @Test
    void testNoException() {
        assertDoesNotThrow(() -> {
            // some code that should not throw an exception
            System.out.println("This is safe");
        });
    }
}
```

### 7. **assertTimeout**

`assertTimeout` 用于验证代码块在指定的时间内完成。如果超时，测试将失败。该断言适用于测试性能或响应时间。

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import java.time.Duration;

class MyTests {

    @Test
    void testTimeout() {
        assertTimeout(Duration.ofSeconds(1), () -> {
            // code that should complete within 1 second
            Thread.sleep(500);
        });
    }
}
```

### 8. **assertAll**

`assertAll` 用于组合多个断言，所有断言都会被执行，即使其中某个断言失败，也会继续执行其他断言。所有失败的断言都会被报告。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyTests {

    @Test
    void testAll() {
        String str = "JUnit 5";

        assertAll("String properties",
            () -> assertEquals(7, str.length(), "Length should be 7"),
            () -> assertTrue(str.startsWith("J"), "Should start with J"),
            () -> assertFalse(str.isEmpty(), "Should not be empty")
        );
    }
}
```

### 9. **assertArrayEquals**

`assertArrayEquals` 用于比较两个数组是否相等。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MyTests {

    @Test
    void testArrayEquals() {
        int[] expected = { 1, 2, 3 };
        int[] actual = { 1, 2, 3 };
        assertArrayEquals(expected, actual, "Arrays should be equal");
    }
}
```

### 10. **assertSame 和 assertNotSame**

`assertSame` 用于验证两个对象引用是否指向同一个对象，而 `assertNotSame` 用于验证两个对象引用是否指向不同的对象。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class MyTests {

    @Test
    void testSame() {
        String str1 = "JUnit";
        String str2 = str1;
        assertSame(str1, str2, "Both references should point to the same object");
    }

    @Test
    void testNotSame() {
        String str1 = new String("JUnit");
        String str2 = new String("JUnit");
        assertNotSame(str1, str2, "References should not point to the same object");
    }
}
```

### 11. **fail**

`fail` 直接将测试标记为失败。可以在捕获到特定异常或满足某个条件时使用 `fail`。

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

class MyTests {

    @Test
    void testFailure() {
        if (someCondition()) {
            fail("Test failed due to some condition");
        }
    }

    boolean someCondition() {
        return true; // replace with actual condition
    }
}
```

通过这些断言方法，JUnit 5 提供了非常灵活的工具集，帮助开发者编写健壮的单元测试，并有效地验证代码行为。