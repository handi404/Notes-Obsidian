## 1、概述

MySQL索引是一种提高查询效率的重要手段，它能够快速定位需要的数据，从而减少查询的开销。MySQL支持多种索引类型，每种类型都有其特点和适用场景。

## 2、索引种类

MySQL常见的索引种类有普通索引、唯一索引、全文索引、单列索引、多列索引和空间索引等。索引可以从不同角度去划分，一般来说主要有以下三个划分角度：

|   |   |   |
|---|---|---|
|**从逻辑功能划分**|**从物理实现划分**|**从作用字段划分**|
|普通索引、唯一索引、主键索引、全文索引|聚簇索引、非聚簇索引|单列索引、联合索引|
||||

## 2.1、逻辑功能划分

### 2.1.1、普通索引

普通索引是 MySQL 中最基本的索引类型之一，它可以加快对表中数据的查询速度，并且它只是用于提高查询效率。下面是在user表中创建普通索引的例子：  
user表结构

```
CREATE TABLE users (
  id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);
```

可以为 “username” 和 “email” 列创建普通索引，以提高对这两列的查询速度。下面是创建普通索引的例子：

```
SELECT * FROM users WHERE username = 'john';
```

### 2.1.2、唯一索引

使用unique参数可以设置为唯一索引，创建唯一索引后，相应列的值在全表必须是唯一的，可以为空。  
还有user表为例，可以为 user表的"username" 和 “email” 列创建唯一索引，以确保这两列中的每个值都是唯一的。下面是创建唯一索引的示例：

```
ALTER TABLE users ADD UNIQUE INDEX idx_username (username);
ALTER TABLE users ADD UNIQUE INDEX idx_email (email);
```

这将为 “username” 和 “email” 列创建名为 “idx_username” 和 “idx_email” 的唯一索引。创建唯一索引后，如果尝试插入重复的值，则会引发错误，这可以帮助确保表中的数据是唯一的。

### 2.1.3、主键索引

主键索引是 MySQL 中的一种特殊的索引类型，它是用于标识每个表中唯一行的索引。主键索引要求主键列中的每个值都必须唯一且不能为空值。  
还以user表为例，其表结构如下：

```
CREATE TABLE users (
  id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);
```

在这个例子中id列被指定为主键列，并且使用了auto_increment属性来自动为每个新行生产唯一的id值。因此，id列中的每个值都是唯一的，且不能为空值。通过将id列指定为主键列，MySQL将自动为该列创建主键索引。这将确保id类中的每个值都是唯一的，并且可以在查询时更快地定位和访问所需的数据。需要注意的是，每个表只能有一个主键，因此，在为表创建主键索引时，需要选择一个唯一的列作为主键列。

### 2.1.4、全文索引

全文索引是MySQL中的一种特殊索引类型，用于对文本字段进行全文搜索，全文索引可以帮助加快对文本数据的搜索速度，并支持全文搜索的高级功能，例如模糊搜索和关键词匹配。下面是一个创建全文索引的示例：

```
CREATE TABLE articles (
  id INT(11) NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);
```

可以为 “content” 列创建全文索引，以便在文章内容中进行全文搜索。下面是创建全文索引的示例：

```
ALTER TABLE articles ADD FULLTEXT INDEX idx_content (content) WITH PARSER ngram;
```

可以为 “content” 列创建全文索引，以便在文章内容中进行全文搜索。下面是创建全文索引的示例：

```
ALTER TABLE articles ADD FULLTEXT INDEX idx_content (content) WITH PARSER ngram;
```

  

注意，上面创建全文索引时我们指定了使用名为“ngram”的分词器来为content列创建全文索引。假设表内有如下的数据：

![](https://cdn.nlark.com/yuque/0/2023/png/40581665/1702553336008-5e381f29-b8c0-49e8-adfa-f95aa56cca8a.png)  

当我们使用两个关键字进行查询时，可以查询记录：

```
mysql> select * from articles where match (content) against('不认');
+----+-------+--------------------------+---------------------+
| id | title | content                  | created_at          |
+----+-------+--------------------------+---------------------+
|  2 | 1     | 中文搜索，不认知         | 2023-07-14 14:49:24 |
+----+-------+--------------------------+---------------------+
1 row in set (0.00 sec)
```

但是如果使用的关键词只有一个字，则查不出任何记录：

```
mysql> select * from articles where match (content) against('不');
Empty set (0.00 sec)
```

这是因为MySQL中ngram_token_size变量的值默认为2，即要查询的词的最少个数为2，如果用一个词去查询自然查不到任何内容。如果要搜索单字，就要把ngram_token_size设置为1。在 MySQL 中，ngram_token_size 是一个全文索引配置选项，用于指定 ngram 索引中单个词语的长度。ngram 索引是一种全文索引算法，它将文本分成连续的 n 个字母或单词，以便更高效地进行搜索。ngram_token_size 决定了 ngram 索引中单个词语的长度，从而影响了全文索引的性能和搜索结果。  
使用中文分词器时，需要确保MySQL的字符集设置与文本的字符集匹配，以确保正确的分词和搜索。此外，中文分词器的性能可能会受到一些限制，因此在使用中文分词器时，需要进行适当的性能测试。

然而遗憾的是，在ElasticSearch等专门搜索引擎面前，关系型数据库的全文检索功能使用的并不多。

## 2.2、物理实现上划分

### 2.2.1、聚簇索引

在 MySQL 中，聚簇索引是一种特殊的索引类型，它将表中的数据按照索引键的顺序存储在磁盘上，以提高数据访问的效率。聚簇索引的特点是，索引和数据存储在一起，因此在查询时可以直接访问数据而无需再次查找磁盘上的数据块。

### 2.2.2、非聚簇索引

非聚簇索引是一种索引类型，它将索引和数据分开存储在磁盘上。与聚簇索引不同，非聚簇索引将索引和数据存储在不同的位置，因此在查询时需要先访问索引，再根据索引中的指针访问磁盘上的数据块，从而增加了查询的开销。非聚簇索引常见的类型有 B-tree 索引、哈希索引和全文索引等。  
B-tree 索引是一种常见的非聚簇索引类型，它将索引键和指向数据的指针存储在一棵平衡树中。B-tree 索引的特点是支持快速的范围查询和排序操作，因此常用于对范围较大的列进行索引，例如日期、价格等。  
哈希索引是另一种常见的非聚簇索引类型，它将索引键通过哈希函数计算出一个唯一的哈希值，并将哈希值和指向数据的指针存储在哈希表中。哈希索引的特点是支持快速的等值查询，但不支持范围查询和排序操作。  
全文索引是一种特殊的非聚簇索引类型，它可以对文本内容进行索引和搜索。全文索引的特点是支持对文本内容进行快速的关键字搜索和匹配，因此常用于搜索引擎和文本处理应用中。  
需要注意的是，非聚簇索引通常需要占用更多的磁盘空间，因为需要存储索引和指向数据的指针。同时，非聚簇索引的查询效率可能会受到磁盘 I/O 速度的限制，因此需要仔细评估其适用性和性能，以选择最合适的索引类型。

### 2.2.3、聚簇索引与非聚簇索引区别

聚簇索引和非聚簇索引的区别主要有以下几个：

聚簇索引叶子节点存储的是行数据；而非聚簇索引叶子节点存储的是聚簇索引（通常是主键 ID）。  
聚簇索引查询效率更高，而非聚簇索引需要进行回表查询，因此性能不如聚簇索引。  
聚簇索引一般为主键索引，而主键一个表中只能有一个，因此聚簇索引一个表中也只能有一个，而非聚簇索引则没有数量上的限制。

## 2.3、作用字段个数划分

### 2.3.1、单列索引

单列索引是一种索引类型，它只包含一个列的值，如我们在上面创建的普通索引就是单列索引。

### 2.3.2、组合索引

组合索引是一种索引类型，它包含多个列的值。与单列索引不同，组合索引将多个列的值组合在一起作为索引键，以提高多列查询的效率。组合索引可以根据多个列的值来快速定位需要的数据，从而减少查询的开销。下面是一个组合索引的示例：  
假设有一个名为 “users” 的表，表结构如下：

  

```
CREATE TABLE users (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  age INT(11) NOT NULL,
  gender VARCHAR(10) NOT NULL,
  PRIMARY KEY (id),
  INDEX age_gender_index (age, gender)
) ENGINE=InnoDB;
```

在这个表中，id 列是主键，因此自动创建了一个聚簇索引。同时，我们手动创建了一个名为 “age_gender_index” 的组合索引，以提高根据年龄和性别查询的效率。例如，查询年龄等于 30 且性别为男的用户记录：

  

```
SELECT * FROM users WHERE age = 30 AND gender = 'male';
```

MySQL 将使用 “age_gender_index” 索引来快速定位符合条件的用户记录，而无需扫描整个表。这样可以大大提高查询效率，尤其是在数据量较大时。

需要注意的是，组合索引的顺序很重要，因为索引键的顺序影响查询效率。在上面的例子中，我们将 age 列放在前面，因为根据年龄查询的条件更加常见。此外，组合索引的列数也需要仔细考虑，过多的列可能会降低索引的效率。因此，需要仔细评估索引的适用性和性能，以选择最合适的索引类型和索引键的顺序。