

db.students.find ()
db.students.find (). sort ({name: 1})
db.students.find (). sort ({name: -1})
db.students.find (). sort ({gpa: 1})
db.students.find (). sort ({gpa: -1})
db.students.find (). limit (1)
db.students.find (). limit (3)
db.students.find (). sort ({gpa: -1}). limit (1)
db.students.find (). sort ({gpa: -1}). limit (3)