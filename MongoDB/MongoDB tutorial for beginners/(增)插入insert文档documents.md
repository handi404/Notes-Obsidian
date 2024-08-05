例: 往某个数据库中的 students (相当于数据库中的表) 中 insert document

db.students.insertOne（{name：“海绵宝宝”，年龄：30， gpa： 3.2}）

db.students.insertMany（[{name：“帕特里克”， 年龄：38， gpa： 1.5}，
		             {name：“Sandy”， 年龄：27， gpa： 4.0}，
		             {name：“Gary”， 年龄：18， gpa： 2.5}]）
