JSON（JavaScript Object Notation）是一种轻量级的数据交换格式，易于人和机器读取和生成。它的结构简单明了，通常用于在客户端和服务器之间传输数据。JSON 文件格式基于键值对的概念，类似于 JavaScript 对象。以下是 JSON 文件格式的详细说明：

### JSON 的基本语法规则

1. **对象（Object）**：
   - 由花括号 `{}` 包围。
   - 内部包含一个或多个键值对，键值对之间用逗号 `,` 分隔。
   - **键必须是字符串（双引号引起来）**，键和值之间用冒号 `:` 分隔。
   - 值可以是字符串、数字、布尔值、数组、对象或 `null`。

   **示例**：
   ```json
   {
     "name": "John Doe",
     "age": 30,
     "isEmployed": true
   }
   ```

2. **数组（Array）**：
   - 由方括号 `[]` 包围。
   - 包含一个或多个值，值之间用逗号 `,` 分隔。
   - 数组中的值可以是任意合法的 JSON 数据类型。

   **示例**：
   ```json
   [
     "Apple",
     "Banana",
     "Cherry"
   ]
   ```

3. **值（Value）**：
   - JSON 的值可以是以下类型：
     - **字符串（String）**：由双引号 `""` 包围的文本。
     - **数字（Number）**：整数或浮点数，无需引号。
     - **布尔值（Boolean）**：`true` 或 `false`，无需引号。
     - **对象（Object）**：一个包含键值对的 JSON 对象。
     - **数组（Array）**：一个 JSON 数组。
     - **`null`**：表示空值。

   **示例**：
   ```json
   {
     "string": "Hello, World!",
     "number": 12345,
     "boolean": true,
     "object": {
       "nestedKey": "nestedValue"
     },
     "array": [1, 2, 3],
     "nullValue": null
   }
   ```

4. **空白符**：
   - JSON 不要求特定的空白符（如空格、换行、缩进），这些可以用于提高可读性，但不会影响解析。
  
   **示例**（与前一个示例等价）：
   ```json
   {
       "string": "Hello, World!",
       "number": 12345,
       "boolean": true,
       "object": {
           "nestedKey": "nestedValue"
       },
       "array": [1, 2, 3],
       "nullValue": null
   }
   ```

### JSON 文件示例

以下是一个典型的 JSON 文件示例，展示了如何定义一个复杂的数据结构，包括对象和数组的嵌套：

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "johndoe@example.com",
  "isActive": true,
  "age": 28,
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "postalCode": "12345"
  },
  "phoneNumbers": [
    {
      "type": "home",
      "number": "555-555-1234"
    },
    {
      "type": "work",
      "number": "555-555-5678"
    }
  ],
  "hobbies": ["reading", "traveling", "swimming"],
  "metadata": null
}
```

### JSON 文件的注意事项

1. **文件扩展名**：
   - JSON 文件通常使用 `.json` 扩展名。

2. **键名**：
   - 键必须用双引号包围。
   - 键名不允许重复，在同一个对象中每个键名必须唯一。

3. **严格的语法要求**：
   - JSON 不允许尾随逗号（即最后一个键值对或数组项后不能有逗号）。
   - JSON 是区分大小写的，`true`、`false` 和 `null` 必须使用小写字母。

4. **编码**：
   - JSON 文件通常使用 UTF-8 编码。

### JSON 文件的使用场景

JSON 文件广泛用于：
- 前后端数据交换
- 配置文件
- 数据存储
- API 响应和请求格式
- 静态数据文件

JSON 的简单性和通用性使其成为现代 web 开发中的标准数据格式。