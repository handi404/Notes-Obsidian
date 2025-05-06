### **一、UNION**

UNION 从操作符用于连接两个或两个以上的 SELECT 语句并将查询结果合并到一个结果集中， UNION 会自动对结果集去重。语法如下：

  

```
SELECT column,... FROM table1
UNION [ALL]
SELECT column,... FROM table2
```

TIP：  

1. 使用 UNION 连接的所有 SELECT 语句必须拥有相同的列
2. UNION 结果集中的列名和第一个 SELECT 语句中的列名一致

  

### **二、UNION 和 UNION ALL 的区别**

默认情况下，UNION 会自动对查询结果集进行去重操作，所以在数据量较大的情况下效率会比较低。如果不需对查询结果集进行去重查询操作，就需要用到 UNION ALL。具体异同如下：

|   |   |
|---|---|
|**UNION**|**UNION ALL**|
|对查询结果集进行并集操作|对查询结果集进行并集操作|
|去除重复记录|不去除重复记录|
|大数据量下性能较底|大数据量下性能较高|

例如：  
有两个表如下所示：

student：

|   |   |
|---|---|
|**stu_no**|**name**|
|2101|张三|
|2102|李四|
|2103|王五|

teacher：

|   |   |   |
|---|---|---|
|**id**|**name**|**phone**|
|1001|赵六|123456|
|1002|田七|123457|
|2101|张三|123456780|

1. 查询出两个表的编号和姓名，人员不能重复

  

```
select stu_no,name from student
union
select id,name from teacher
```

1. 查询两个表所有的人员

  

```
select stu_no,name from student
union all
select id,name from teacher
```