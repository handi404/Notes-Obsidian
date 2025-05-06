.updateOne ({filter}, {update})

```JSON
db.students.updateOne({name:"Spongebob"}, {$set:{fullTime:true}})

db.students.updateOne({_id: ObjectId("642c0e70985f18e1bcf24d35")}, {$set:{fullTime:false}})

db.students.updateOne({_id: ObjectId("642c0e70985f18e1bcf24d35")}, {$unset:{fullTime:""}})

db.students.updateMany({}, {$set:{fullTime:false}})
db.students.updateOne({name:"Gary"}, {$unset:{fullTime:""}}) //删除name为Gary的字段fullTime
db.students.updateOne({name:"Sandy"}, {$unset:{fullTime:""}})

db.students.updateMany({fullTime:{$exists:false}}, {$set:{fullTime:true}}) 
```

- **db.students.updateMany()**: 更新 `students` 集合中的多个文档。
- **{fullTime:{$exists: false}}**: 找到所有没有 `fullTime` 字段的文档。 //exists: 存在
- **{$set:{fullTime:true}}**: 将找到的文档中的 `fullTime` 字段设置为 `true`。

**简而言之：**

这个命令会将 `students` 集合中所有没有 `fullTime` 字段的文档的 `fullTime` 字段设置为 `true`。

**例如：**

假设 `students` 集合中有以下文档：

```JSON
{ "_id": 1, "name": "Alice", "age": 20 }
{ "_id": 2, "name": "Bob", "age": 22, "fullTime": false }
{ "_id": 3, "name": "Charlie", "age": 21 }
```

执行命令后，集合将变为：

```JSON
{ "_id": 1, "name": "Alice", "age": 20, "fullTime": true }
{ "_id": 2, "name": "Bob", "age": 22, "fullTime": false }
{ "_id": 3, "name": "Charlie", "age": 21, "fullTime": true }
```

可以看到，只有第一个和第三个文档的 `fullTime` 字段被设置为 `true`，因为它们之前没有这个字段。第二个文档的 `fullTime` 字段保持不变，因为它的值已经存在。
