例: 往某个数据库中的 students (集合 Collection相当于数据库中的表) 中 insert 文档document

```json
db.students.insertOne（{name：“海绵宝宝”，年龄：30， gpa： 3.2}）

db.students.insertMany（[{name：“帕特里克”， 年龄：38， gpa： 1.5}，
		             {name：“Sandy”， 年龄：27， gpa： 4.0}，
		             {name：“Gary”， 年龄：18， gpa： 2.5}]）
```


## MongoDB 中插入文档

### 插入单个文档

- **语法：**

```JavaScript
db.collection.insertOne(document)
```

- **参数：**
    - `collection`: 要插入文档的集合名称。
    - `document`: 要插入的文档。
    
- **示例：**

```JavaScript
// 插入一个用户文档
db.users.insertOne({
    name: "Alice",
    age: 30,
    address: {
        street: "123 Main St",
        city: "Anytown"
    }
})
```

### 插入多个文档

- **语法：**

```JavaScript
db.collection.insertMany(documents)
```

- **参数：**
    
    - `collection`: 要插入文档的集合名称。
    - `documents`: 要插入的文档数组。
- **示例：**
    

```JavaScript
// 插入多个产品文档
db.products.insertMany([
    { name: "Product A", price: 10 },
    { name: "Product B", price: 20 },
    { name: "Product C", price: 15 }
])
```

### 插入行为

- **集合创建：** 如果指定的集合不存在，MongoDB 会自动创建该集合。
- **_id 字段：** 如果文档中没有指定 `_id` 字段，MongoDB 会自动为每个文档生成一个唯一的 `_id` 值，通常是一个 ObjectId。
- **原子性：** MongoDB 中的每个插入操作都是原子性的，要么成功，要么失败。
- **写确认：** 可以通过指定写关注 (write concern) 来控制插入操作的确认级别。

### 其他注意事项

- **文档结构：** 每个文档的结构可以不同，但同一个集合中的文档通常具有相似的结构。
- **数据类型：** MongoDB 支持多种数据类型，包括字符串、数字、布尔值、日期、数组、对象等。
- **索引：** 可以为文档中的字段创建索引，以提高查询性能。
- **批量插入：** 对于大量数据的插入，建议使用 `insertMany` 方法，可以提高插入效率。

### 示例：插入带有日期和数组的文档

```JavaScript
db.orders.insertOne({
    customer: "Bob",
    items: ["apple", "banana", "orange"],
    orderDate: new Date()
})
```

### 总结

MongoDB 的插入操作非常简单直观。通过了解 `insertOne` 和 `insertMany` 方法，以及文档的结构和数据类型，你可以轻松地将数据插入到 MongoDB 中。