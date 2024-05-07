### 1.truncate

删除表中的内容，不删除表结构，释放空间；

### 2.delete

删除内容，不删除表结构，但不释放空间

### 3.区别

#### 3.1[内存](https://so.csdn.net/so/search?q=%E5%86%85%E5%AD%98&spm=1001.2101.3001.7020)空间

truncate删除数据后重新写数据会从1开始，而delete删除数据后只会从删除前的最后一行续写；内存空间上，truncate省空间

#### 3.2处理速度

因为，truncate是直接从1开始，即全部清空开始，而delete需要先得到当前行数，从而进行续写；所以truncate删除速度比delete快；

#### 3.3语句类型

delete属于DML语句，而truncate和drop都属于DDL语句，这造成了它们在事务中的不同现象：

1. delete在事务中，因为属于DML语句，所以可以进行回滚和提交操作（由操作者）
2. truncate和drop则属于DDL语句，在事务中，执行后会自动commit，所以不可以回滚；

#### 3.4语法

delete from 表名 (where…可写可不写，写的话进行选择性删除，不选清空表中数据)

truncate 表名（删除表中的数据，无法回滚的）

1. delete可以在后续加上where进行针对行的删除
2. truncate和drop后面只能加上表名，直接删除表，无法where

**注意：并且drop和truncate不能够激活触发器，因为该操作不记录各行删除；**

_drop table 表名_

_删除表，内容连带结构一起删除；_