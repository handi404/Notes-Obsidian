

```JSON
db.students.deleteOne({name:"Larry"})
db.students.deleteMany({fullTime:false})
						//删除不存在registerDate字段的
db.students.deleteMany({registerDate:{$exists:false}})
```