## openGauss下的操作指令【华为根技术】


## 常用运维相关命令

## 第一组：openGauss启停

高斯数据库的启动/停止/重启指令：

```sql
gs_ctl start -D /home/gauss/openGauss/data/

gs_ctl stop-D /home/gauss/openGauss/data/

gs_ctl restart -D /home/gauss/openGauss/data/
```

#查看进程

```bash
ps ux |grep gauss
```

#查看端口号

```bash
netstat -lntup|grep gauss

ss -lntup|grep gauss
```

## 第二组：openGauss库表指令

#进入到高斯数据库

使用jack用户连接到远程主机postgres数据库的15400端口。 复杂格式

`gsql -h 10.180.123.163 -d postgres -U jack -p 15400`

```bash
[gauss@server1 ~]$ gsql -d postgres -p 5432

gsql ((openGauss-lite 6.0.1 build 84c20a90) compiled at 2025-01-17 17:49:04 commit 0 last mr release)

Non-SSL connection (SSL connection is recommended when requiring high-security)

Type "help" for help.

openGauss=#
```

#查看相应的数据库

```bash
openGauss=# \l

List of databases

Name | Owner | Encoding | Collate | Ctype | Access privileges

-------------+-------+----------+-------------+-------------+-------------------

human_staff | gauss | UTF8 | zh_CN.UTF-8 | zh_CN.UTF-8 |

postgres | gauss | UTF8 | zh_CN.UTF-8 | zh_CN.UTF-8 |

template0 | gauss | UTF8 | zh_CN.UTF-8 | zh_CN.UTF-8 | =c/gauss +

| | | | | gauss=CTc/gauss

template1 | gauss | UTF8 | zh_CN.UTF-8 | zh_CN.UTF-8 | =c/gauss +

| | | | | gauss=CTc/gauss

(4 rows)
```

#库的删除

```sql
drop database human_staff;

DROP DATABASE
```

#创建数据库

```sql
openGauss=# create database human_staff;

CREATE DATABASE
```

#和\\l 类似 查看库指令

```sql
openGauss=# SELECT datname FROM pg_database;

datname
-------------
template1

human_staff

template0

postgres

(4 rows)
```

#进入到human\_staff

```sql
openGauss=# \c human_staff

Non-SSL connection (SSL connection is recommended when requiring high-security)

You are now connected to database "human_staff" as user "gauss".

#下面的指令不会成功，不是mysql的指令。
human_staff=# select * from tables;
```

第三#查看表信息

```sql
#human_staff=# \dt

No relations found.
```
```sql
human_staff=# create table users(id int,uname varchar(50));

CREATE TABLE

human_staff=# \dt

List of relations

Schema | Name | Type | Owner | Storage

--------+-------+-------+-------+----------------------------------

public | users | table | gauss | {orientation=row,compression=no}

(1 row)
human_staff=# insert into users values(1,'tom');

INSERT 0 1

human_staff=# select * from users;

id | uname

----+-------

1 | tom

(1 row)
```

#变量的设置

```sql
#设置变量
openGauss=# \set dbname2 shopdb
#显示变量
openGauss=# \echo :dbname2
shopdb

#删除变量
openGauss=# \unset dbname2
```
```bash
#解决：^A H问题;

bash

# 在root用户下，安装rlwrap

sudo yum install rlwrap

# 使用rlwrap运行gsql

rlwrap gsql -d postgres -p 5432
```

## 第三组：openGauss表数据管理

需要提前创建库omm

### 1、创建一张表 products

| 字段名 | 数据类型 | 含义 |
| --- | --- | --- |
| product\_id | integer | 产品编号 |
| product\_name | char(30) | 产品名 |
| category | char(20) | 种类 |

\----建表语句

```sql
create table products
( product_id integer,
product_name char(30),
category char(20)
) ;
```

\----查看表的结构  
omm=# \\d products

### 2、向表中插入数据，分别采用一次插入一条和一次插入多条记录的方式

```sql
insert into products(product_id,product_name,category) values (1502, ‘olympus camera’, ‘electrncs’);

insert into products(product_id,product_name,category) values

(1601,‘lamaze’,‘toys’),
(1700,‘wait interface’,‘Books’),
(1666,‘harry potter’,‘toys’);
```

  


### 3、查询表中所有记录及记录数

```sql
select count(*) from products;

select * from products;
```

### 4、查询表中所有category记录，并将查询结果按升序排序

```sql
select category from products order by category asc;
```

### 5、查询表中category为toys的记录

select \* from products where category = ‘toys’;  


### 6、更改表中某个字段的值

update products set product\_name=‘camera’ where product\_id=1502;  
select \* from products;  


### 6、删除表products

```sql
drop table products;

select * from products;
```

## 总结

openGauss数据库作为一款基于SQL标准的关系型数据库管理系统，支持广泛的SQL指令进行数据操作、查询、管理以及权限控制。OpenGauss提供面向多核的极致性能、全链路的业务和数据安全、基于AI的调优和高效运维的能力让我受益匪浅。尤其是OpenGauss在AI自治方面的能力真是令人惊喜。自调优、自诊断自愈、自组装这三大自治简直是所有dba的福音。
