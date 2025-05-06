.find ({query}, {projection})

```JSON
db.students.find ({name: "Spongebob"}) 
db.students.find ({gpa: 4.0})
db.students.find ({fullTime: false})
db.students.find ({}, {name: true})
db.students.find ({}, {_id: false, name: true})
db.students.find ({}, {_id: false, name: true, gpa: true})
db.students.find ({name: "Spongebob"}, {_id: false, name: true, gpa: true})
```

db.students.find ({name: "Spongebob"}) //返回 name 是 Spongebob 的
db.students.find ({gpa: 4.0})
db.students.find ({fullTime: false})
db.students.find ({}, {name: true})  //只返回 name
db.students.find ({}, {id: false, name: true})
db.students.find ({}, {id: false, name: true, gpa: true})

db. students. find ({name: "Spongebob"}, {id: false, name: true, gpa: true})    //只返回 name 是 Spongebob 的 name 和 gpa