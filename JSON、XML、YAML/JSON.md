JSON（JavaScript Object Notation）是一种**轻量级的数据交换格式**，凭借其简洁性和易读性，已成为现代 Web 开发、API 交互的事实标准。本文将带你从零开始彻底掌握 JSON。

---

### 一、JSON 是什么？
- **全称**：JavaScript Object Notation。
- **特点**：
  - **纯文本格式**：基于键值对，支持所有编程语言解析。
  - **轻量高效**：数据体积小，解析速度快。
  - **自描述性**：结构清晰，人类和机器都能轻松理解。
- **典型用途**：前后端数据交互、配置文件、NoSQL 数据库存储（如 MongoDB）。

**例子**：
```json
{
  "name": "张三",
  "age": 30,
  "isStudent": false,
  "hobbies": ["阅读", "运动"],
  "address": {
    "city": "北京",
    "country": "中国"
  }
}
```

---

### 二、JSON 核心语法
#### 1. **数据类型**
JSON 支持以下数据类型：
- **字符串**：必须用双引号 `"` 包裹。
- **数字**：直接写数值（如 `3.14`、`-10`）。
- **布尔值**：`true` 或 `false`。
- **数组**：用方括号 `[]` 包裹，元素用逗号分隔。
- **对象**：用花括号 `{}` 包裹的键值对集合。
- **空值**：`null`。

#### 2. **键值对规则**
- **键必须是字符串**，且用双引号包裹。
- **值可以是任意合法的 JSON 数据类型**。
  ```json
  {
    "username": "alice",
    "score": 95.5,
    "isAdmin": true,
    "tags": ["user", "editor"],
    "profile": {
      "bio": "Hello World"
    }
  }
  ```

#### 3. **嵌套结构**
- 对象和数组可以无限嵌套。
  ```json
  {
    "id": 1,
    "metadata": {
      "created_at": "2023-01-01",
      "versions": [1.0, 2.0, 3.0]
    }
  }
  ```

---

### 三、JSON 语法规则
1. **严格双引号**  
   键和字符串值必须用双引号包裹，单引号会报错。
   ```json
   // 错误
   { 'name': '张三' }
   
   // 正确
   { "name": "张三" }
   ```

2. **末尾无逗号**  
   最后一个元素后不能有逗号（称为“尾随逗号”）。
   ```json
   // 错误
   {
     "a": 1,
     "b": 2,
   }
   
   // 正确
   {
     "a": 1,
     "b": 2
   }
   ```

3. **特殊字符转义**  
   字符串中的双引号、反斜杠需用 `\` 转义。
   ```json
   {
     "path": "C:\\Users\\name",
     "message": "He said: \"Hello!\""
   }
   ```

---

### 四、JSON 的应用场景
4. **Web API 数据交互**  
   前后端通过 JSON 传输数据（如 RESTful API）。
   ```json
   // GET /users/1 返回的 JSON
   {
     "id": 1,
     "name": "李四",
     "email": "li@example.com"
   }
   ```

5. **配置文件**  
   轻量级配置（如 VS Code 的 `settings.json`）。
   ```json
   {
     "editor.fontSize": 14,
     "files.autoSave": "afterDelay"
   }
   ```

6. **NoSQL 数据库**  
   MongoDB 等数据库直接存储 JSON 格式文档。
   ```json
   {
     "_id": "507f1f77bcf86cd799439011",
     "title": "博客文章",
     "content": "..."
   }
   ```

---

### 五、JSON vs XML vs YAML
| **特性**       | **JSON**               | **XML**                | **YAML**               |
|----------------|------------------------|------------------------|------------------------|
| **可读性**     | 良好                   | 较差（标签冗余）       | 极佳（无标签）         |
| **语法复杂度** | 简单（键值对+双引号）  | 复杂（标签闭合）       | 简单（缩进敏感）       |
| **注释支持**   | ❌ 不支持               | ✅ 支持 `<!-- -->`      | ✅ 支持 `#`            |
| **数据类型**   | 支持数字、布尔值等     | 无类型（需自定义解析） | 自动推断               |
| **典型用途**   | API 数据传输           | 旧系统、复杂文档       | 配置文件、CI/CD        |

---

### 六、JSON 解析与生成
#### 1. **JavaScript**
```javascript
// 解析 JSON 字符串
const data = JSON.parse('{"name": "张三", "age": 30}');
console.log(data.name); // 输出：张三

// 生成 JSON 字符串
const obj = { a: 1, b: true };
const jsonStr = JSON.stringify(obj); // {"a":1,"b":true}
```

#### 2. **Python**
```python
import json

# 解析 JSON
data = json.loads('{"name": "李四", "score": 90}')
print(data["score"])  # 输出：90

# 生成 JSON
obj = {"id": 1, "tags": ["python", "dev"]}
json_str = json.dumps(obj)  # {"id": 1, "tags": ["python", "dev"]}
```

#### 3. **Java**
```java
import com.alibaba.fastjson.JSON;

// 解析 JSON
String json = "{\"name\":\"王五\"}";
User user = JSON.parseObject(json, User.class);

// 生成 JSON
User user = new User("赵六");
String json = JSON.toJSONString(user);  // {"name":"赵六"}
```

---

### 七、JSON 的优缺点
#### 优点：
- **轻量高效**：数据体积小，解析速度快。
- **跨语言支持**：几乎所有编程语言都有解析库。
- **结构清晰**：键值对直观易读。

#### 缺点：
- **不支持注释**：无法在 JSON 中直接添加注释。
- **类型有限**：不支持函数、日期等复杂类型（需自定义格式）。
- **安全性**：直接解析不可信的 JSON 可能引发安全问题（如 JSON 拒绝服务攻击）。

---

### 八、常见问题解答
#### 1. JSON 中如何表示日期？
- JSON 本身无日期类型，通常用字符串（ISO 8601 格式）：
  ```json
  {
    "created_at": "2023-10-01T12:34:56Z"
  }
  ```

#### 2. JSON 中的注释怎么写？
- **标准 JSON 不支持注释**，但某些解析库允许特殊注释（如 `//` 或 `#`），非通用。

#### 3. JSON 与 JSON 5 的区别？
- **JSON 5** 是 JSON 的扩展，支持注释、单引号、尾随逗号等，兼容性更高：
  ```json5
  {
    // 这是注释
    name: '张三',  // 允许单引号
    list: [1, 2, 3,],  // 允许尾逗号
  }
  ```

---

### 九、总结
JSON 凭借**简洁的语法、高效的解析能力和跨平台特性**，成为现代数据交互的基石。掌握以下核心点：
- **键值对**、**双引号**、**无注释**。
- **嵌套对象和数组**的灵活使用。
- **解析与生成**的代码实现。

如果需要处理复杂配置（如注释、多行文本），可结合 YAML 或 JSON5。遇到问题时，可用 [JSONLint](https://jsonlint.com/) 在线验证语法！