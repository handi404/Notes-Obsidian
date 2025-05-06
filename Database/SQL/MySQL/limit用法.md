##### 1、基本介绍

1. limit 也被叫做分页查询，但是limit不仅仅是用于分页查询。
2. 整个select查询语句的执行顺序如下所示：

```
select                  5
    ...
    字段        
    ...    
from                    1
    ...  
    表名
    ...
where                   2
    ...
    条件
    ...
group by                3
    ...
    分组字段
    ...
having                  4
    ...
    二次过滤
    ...
order by                6
    ...
    排序
    ... 
limit start, size       7
    ...
    分页
    ...    
```

可以看到limit在这些select的关键词中是最后一个使用的；

**结论：因此可以将所有的查询结果当做一个字符串，而limit就相当于从这个字符串中快速截取指定的数据。**

##### 2、使用细节

limit start, size的含义：start是表示起始数据的行数、size表示展示的数据数量；特别需要注意的是MySQL中的第一条数据是从**下标index = 0开始**算的。

例如：查询工资的前1-5名，和工资的2-6名。

```
-- 前1-5名
select ename, sal from emp order by sal desc  limit 0, 5

-- 前2-6名
select ename, sal from emp order by sal desc  limit 1, 5
```

![](https://cdn.nlark.com/yuque/0/2023/png/40581665/1701939907066-55479c62-8b2a-4ef1-9553-d8d48c4d467d.png)

limit [start], size中的start可以不写，如果不写等价于start = 0，size表示需要展示的数据条数。

```
select ename, sal from emp order by sal desc limit 0, 5

select ename, sal from emp order by sal desc limit 5
```

上述两种写法是等价的，**如果只给一个参数那么这个参数一定是size，start默认就是0**。

##### 3、常见使用

###### 3.1、数据的分页展示

limit经常被用来分页查询，这是非常重要的。当服务器中的数据非常多时不可能一次性全部展示给用户，需要用户手动翻页！

```
limit start, size
第1页: 0, size
第2页: size, size
第3页: 2*size, size
第4页: 3*size, size
第n页: (n-1)*size, size
```

根据简单的推导可以看到一个等差数列，第n页的第一条数据是start = (n - 1) * size，最后一条是n *size- 1。

###### 3.2、查询第k名

对于查询最大、最小的时候MySQL提供了良好的聚合函数进行解决这个问题、但是可以思考一下查询第k名（升序、逆序）时聚合函数似乎显得力不从心了，这时候可以使用order by 排序 + limit 进行一下挑选。

```
查询某个字段第k名的数据。注意是k-1（从0开始！）
select * from 表名 order by 字段 desc/asc limit k-1, 1
```

举例：力扣176（第二高的薪水）

```
select (select distinct Salary from Employee order by Salary desc limit 1,1) 
SecondHighestSalary
```