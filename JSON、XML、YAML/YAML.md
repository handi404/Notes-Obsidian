YAML（YAML Ain't Markup Language）是一种**简洁、易读的数据序列化语言**，广泛应用于配置文件（如 Kubernetes、Ansible、Spring Boot）和数据交换。本文将带你彻底掌握 YAML 的核心概念和用法。

---

### 一、YAML 是什么？
- **设计目标**：以**人类可读**的方式表示数据结构，同时便于机器解析。
- **特点**：
  - **缩进敏感**：通过缩进表示层级关系（类似 Python）。
  - **无标签闭合**：用冒号 `:` 分隔键值对，无需闭合标签。
  - **支持复杂数据类型**：如数组、字典、多行字符串等。

**例子**：
```yaml
# 简单键值对
name: "张三"
age: 30

# 嵌套结构
address:
  city: 北京
  country: 中国

# 列表
hobbies:
  - 读书
  - 运动
  - 写代码
```

---

### 二、YAML 核心语法
#### 1. **缩进规则**
- **缩进必须用空格**，不能使用 Tab。
- **同一层级的元素必须左对齐**。
  ```yaml
  # 正确
  fruits:
    - apple
    - banana

  # 错误（混合空格和Tab）
  fruits:
  - apple
  - banana
  ```

#### 2. **键值对**
- **键与值用冒号+空格分隔**：`key: value`。
- **字符串无需引号**，但特殊字符需用引号包裹。
  ```yaml
  message: 这是一个普通字符串
  special: "带双引号的字符串（可包含 ': '等符号）"
  ```

#### 3. **列表（数组）**
- **用短横线 `-` 表示列表项**，后面跟一个空格。
  ```yaml
  colors:
    - red
    - green
    - blue
  ```

#### 4. **字典（映射）**
- **键值对嵌套**表示字典结构。
  ```yaml
  person:
    name: 李四
    age: 25
    skills:
      - Python
      - Java
  ```

#### 5. **多行字符串**
- **`|`**：保留换行符。
- **`>`**：合并多行文本为一行。
  ```yaml
  description: |
    这是第一行，
    这是第二行。
    换行会被保留。

  summary: >
    这是一段长文本，
    会被合并成一行。
  ```

---

### 三、YAML 高级特性
#### 1. **锚点（Anchors）与引用（Aliases）**
- **重复内容用 `&` 定义锚点，用 `*` 引用**。
  ```yaml
  defaults: &defaults
    env: production
    timeout: 30s

  server:
    <<: *defaults  # 合并锚点内容
    port: 8080
  ```

#### 2. **多文档**
- **用 `---` 分隔多个文档**（常用于单个文件中定义多个配置）。
  ```yaml
  ---
  document: 第一个文档
  ---
  document: 第二个文档
  ```

#### 3. **合并键（Merge Key）**
- **`<<`**：合并多个字典。
  ```yaml
  base: &base
    key1: value1

  extended:
    <<: *base
    key2: value2  # 最终包含 key1 和 key2
  ```

#### 4. **类型转换**
- **显式声明数据类型**（如布尔值、数字）。
  ```yaml
  is_active: true     # 布尔值
  count: 100          # 整数
  price: 99.99        # 浮点数
  null_value: null    # 空值
  ```

---

### 四、YAML vs JSON vs XML
| **特性**       | **YAML**               | **JSON**               | **XML**                |
|----------------|------------------------|------------------------|------------------------|
| **可读性**     | 极佳（简洁、无冗余）   | 一般（标签较多）       | 差（标签冗余）         |
| **语法复杂度** | 简单（缩进敏感）       | 简单（严格闭合）       | 复杂（标签闭合、嵌套） |
| **注释支持**   | ✅ 支持 `#` 注释        | ❌ 不支持注释           | ✅ 支持 `<!-- -->`      |
| **数据类型**   | 自动推断（如布尔值）   | 严格类型（字符串需引号）| 无类型（需自定义解析） |
| **典型用途**   | 配置文件、CI/CD        | API 数据传输           | 旧系统、复杂文档       |

---

### 五、YAML 常见陷阱
1. **缩进错误**  
   - 缩进必须是**2 个或 4 个空格**（常见约定是 2 个）。
   - 混合使用空格和 Tab 会导致解析失败。

2. **特殊字符**  
   - 冒号 `:`、短横线 `-` 等符号需用引号包裹。
     ```yaml
     # 错误
     path: C:\Users\name
     # 正确
     path: "C:\\Users\\name"
     ```

3. **布尔值陷阱**  
   - `yes`, `no`, `on`, `off` 等会被解析为布尔值。
     ```yaml
     answer: yes    # 解析为 true
     answer: "yes"  # 解析为字符串 "yes"
     ```

---

### 六、YAML 实战示例
#### 1. **Spring Boot 配置文件**
```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: secret
```

#### 2. **Kubernetes Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
        - name: nginx
          image: nginx:1.14.2
          ports:
            - containerPort: 80
```

---

### 七、YAML 解析工具
- **Python**：使用 `PyYAML` 库。
  ```python
  import yaml

  data = yaml.safe_load("""
  name: 张三
  age: 30
  """)
  print(data["name"])  # 输出：张三
  ```

- **在线解析器**：[YAML Lint](http://www.yamllint.com/) 可验证语法。

---

### 八、总结
YAML 通过**简洁的语法和强大的数据表达能力**，成为配置文件的首选格式。其核心是：
- **缩进定义层级**，**冒号分隔键值**，**短横线表示列表**。
- 掌握锚点、多行字符串、类型转换等高级特性，能高效处理复杂配置。