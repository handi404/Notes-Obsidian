## 前言

> **JdbcTemplate 是 Spring框架 中提供的一个对象，用于简化JDBC操作。它使得数据库操作变得更为简单和方便，大大提高了开发效率。**

# 文章目录

- [[#为何要使用JdbcTemplate|为何要使用JdbcTemplate]]
  - [[#为何要使用JdbcTemplate#在JdbcTemplate中执行SQL语句的方法大致分为3类：|在JdbcTemplate中执行SQL语句的方法大致分为3类：]]
  - [[#为何要使用JdbcTemplate#案例代码|案例代码]]
- [[#JdbcTemplate实现增删改|JdbcTemplate实现增删改]]
  - [[#JdbcTemplate实现增删改#案例代码|案例代码]]
- [[#JdbcTemplate查询-queryForInt返回一个int整数|JdbcTemplate查询-queryForInt返回一个int整数]]
  - [[#JdbcTemplate查询-queryForInt返回一个int整数#案例代码|案例代码]]
- [[#JdbcTemplate查询-queryForObject返回String|JdbcTemplate查询-queryForObject返回String]]
  - [[#JdbcTemplate查询-queryForObject返回String#案例代码|案例代码]]
- [[#JdbcTemplate查询-queryForMap返回一个Map集合|JdbcTemplate查询-queryForMap返回一个Map集合]]
  - [[#JdbcTemplate查询-queryForMap返回一个Map集合#处理结果|处理结果]]
- [[#JdbcTemplate查询-queryForList返回一个List集合|JdbcTemplate查询-queryForList返回一个List集合]]
  - [[#JdbcTemplate查询-queryForList返回一个List集合#处理结果|处理结果]]
- [[#JdbcTemplate查询-RowMapper返回自定义对象|JdbcTemplate查询-RowMapper返回自定义对象]]
  - [[#JdbcTemplate查询-RowMapper返回自定义对象#案例代码|案例代码]]
- [[#JdbcTemplate查询-BeanPropertyRowMapper返回自定义对象|JdbcTemplate查询-BeanPropertyRowMapper返回自定义对象]]
  
  ## 为何要使用JdbcTemplate

[JDBC](https://so.csdn.net/so/search?q=JDBC&spm=1001.2101.3001.7020)已经能够满足大部分用户最基本的需求，但是在使用JDBC时，必须自己来管理数据库资源如：获取PreparedStatement，设置SQL语句参数，关闭连接等步骤。

当初看到jdbctempate时感觉很诧异，但是用到时候感觉这样使用真的方便，类似于mybatis的封装，但是却比他简单，明了。

org.springframework.jdbc.core.JdbcTemplate类是JDBC核心包中的中心类。它简化了JDBC的使用，并有助于避免常见的错误。 它执行核心JDBC工作流，留下应用程序代码来提供SQL并提取结果。 该类执行SQL查询或更新，在ResultSet类上启动迭代并捕获JDBC异常，并将它们转换为org.springframework.dao包中定义的通用更详细的异常层次结构。使用这个类的代码只需要实现回调接口，给它们一个明确定义的协定。 Preparedstatementcreator 回调接口在给定 Connection 的情况下创建一个准备好的语句，提供 SQL 和任何必要的参数。 Resultsetextractor 接口从 ResultSet 中提取值。 请参阅 PreparedStatementSetter 和 RowMapper 了解两个流行的可选回调接口。

**JdbcTemplate是Spring对JDBC的封装**，目的是使JDBC更加易于使用。JdbcTemplate是Spring的一部分。JdbcTemplate处理了资源的建立和释放。他帮助我们避免一些常见的错误，比如忘了总要关闭连接。他运行核心的JDBC工作流，如Statement的建立和执行，而我们只需要提供SQL语句和提取结果  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/64555b44a06666a06b81871c079bef8b.png)

### 在JdbcTemplate中执行SQL语句的方法大致分为3类：

1. `execute`：**可以执行所有SQL语句，一般用于执行DDL语句**。
2. `update`：用于执行`INSERT`、`UPDATE`、`DELETE`等DML语句。
3. `queryXxx`：用于DQL数据查询语句。

**JdbcTemplate配置连接池**  
org.springframework.jdbc.core.JdbcTemplate类方便执行SQL语句

```java
public JdbcTemplate(DataSource dataSource) 
创建JdbcTemplate对象，方便执行SQL语句
```

```java
public void execute(final String sql) 
execute可以执行所有SQL语句，因为没有返回值，一般用于执行DDL语句。
```

**JdbcTemplate使用步骤**  
准备DruidDataSource连接池  
导入依赖的jar包  
spring-beans-4.1.2.RELEASE.jar  
spring-core-4.1.2.RELEASE.jar  
spring-jdbc-4.1.2.RELEASE.jar  
spring-tx-4.1.2.RELEASE.jar  
com.springsource.org.apache.commons.logging-1.1.1.jar

public void execute(final String sql)  
execute可以执行所有SQL语句，因为没有返回值，一般用于执行DDL语句。

创建JdbcTemplate对象，传入Druid连接池  
调用execute、update、queryXxx等方法

### 案例代码

```java
public class Demo04 {
    public static void main(String[] args) {
        // 创建表的SQL语句
        String sql = "CREATE TABLE product("
                + "pid INT PRIMARY KEY AUTO_INCREMENT,"
                + "pname VARCHAR(20),"
                + "price DOUBLE"
                + ");";


    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
    jdbcTemplate.execute(sql);
}

}
```

## JdbcTemplate实现增删改

**API介绍**

```java
public int update(final String sql) 
用于执行`INSERT`、`UPDATE`、`DELETE`等DML语句。
```

**使用步骤**  
1.创建JdbcTemplate对象  
2.编写SQL语句  
3.使用JdbcTemplate对象的update方法进行增删改

### 案例代码

```java
public class Demo05 {
    public static void main(String[] args) throws Exception {
//        test01();
//        test02();
//        test03();
    }
// JDBCTemplate添加数据
public static void test01() throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());

    String sql = "INSERT INTO product VALUES (NULL, ?, ?);";

    jdbcTemplate.update(sql, "iPhone3GS", 3333);
    jdbcTemplate.update(sql, "iPhone4", 5000);
    jdbcTemplate.update(sql, "iPhone4S", 5001);
    jdbcTemplate.update(sql, "iPhone5", 5555);
    jdbcTemplate.update(sql, "iPhone5C", 3888);
    jdbcTemplate.update(sql, "iPhone5S", 5666);
    jdbcTemplate.update(sql, "iPhone6", 6666);
    jdbcTemplate.update(sql, "iPhone6S", 7000);
    jdbcTemplate.update(sql, "iPhone6SP", 7777);
    jdbcTemplate.update(sql, "iPhoneX", 8888);
}

// JDBCTemplate修改数据
public static void test02() throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());

    String sql = "UPDATE product SET pname=?, price=? WHERE pid=?;";

    int i = jdbcTemplate.update(sql, "XVIII", 18888, 10);
    System.out.println("影响的行数: " + i);
}

// JDBCTemplate删除数据
public static void test03() throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
    String sql = "DELETE FROM product WHERE pid=?;";
    int i = jdbcTemplate.update(sql, 7);
    System.out.println("影响的行数: " + i);
}
    }
```

## JdbcTemplate查询-queryForInt返回一个int整数

**API介绍**

**API介绍**

```java
public long queryForLong(String sql) 
执行查询语句，返回一个long类型的数据。
```

**使用步骤**  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的queryForLong方法  
输出结果

### 案例代码

```java
// queryForLong  返回一个long类型整数
public static void test02() throws Exception {
   String sql = "SELECT COUNT(*) FROM product;";
   // String sql = "SELECT pid FROM product WHERE price=18888;";
   JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
   long forLong = jdbcTemplate.queryForLong(sql);
   System.out.println(forLong);
}
```

## JdbcTemplate查询-queryForObject返回String

**API介绍**

```java
public <T> T queryForObject(String sql, Class<T> requiredType) 
执行查询语句，返回一个指定类型的数据。
```

**使用步骤**  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的queryForObject方法，并传入需要返回的数据的类型  
输出结果

### 案例代码

```java
public static void test03() throws Exception {
   String sql = "SELECT pname FROM product WHERE price=7777;";
   JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
   String str = jdbcTemplate.queryForObject(sql, String.class);
   System.out.println(str);
}
```

## JdbcTemplate查询-queryForMap返回一个Map集合

**API介绍**

```java
public Map<String, Object> queryForMap(String sql) 
执行查询语句，将一条记录放到一个Map中。
```

**使用步骤**  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的queryForMap方法

### 处理结果

```java
public static void test04() throws Exception {
   String sql = "SELECT * FROM product WHERE pid=?;";
   JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
   Map<String, Object> map = jdbcTemplate.queryForMap(sql, 6);
   System.out.println(map);
}
```

## JdbcTemplate查询-queryForList返回一个List集合

**API介绍**

```java
public List<Map<String, Object>> queryForList(String sql) 
执行查询语句，返回一个List集合，List中存放的是Map类型的数据。
```

**使用步骤**  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的queryForList方法

### 处理结果

```java
public static void test05() throws Exception {
   String sql = "SELECT * FROM product WHERE pid<?;";
   JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
   List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, 8);
   for (Map<String, Object> map : list) {
      System.out.println(map);
   }
}
```

**queryForList方法的作用？将返回的一条记录保存在Map集合中，多条记录对应多个Map，多个Map存储到List集合中list里面包含map**

## JdbcTemplate查询-RowMapper返回自定义对象

**API介绍**

```java
public <T> List<T> query(String sql, RowMapper<T> rowMapper) 
执行查询语句，返回一个List集合，List中存放的是RowMapper指定类型的数据。
```

**使用步骤**  
定义Product类  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的query方法，并传入RowMapper匿名内部类  
在匿名内部类中将结果集中的一行记录转成一个Product对象

### 案例代码

```java
// query使用rowMap做映射返回一个对象
public static void test06() throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());

   // 查询数据的SQL语句
   String sql = "SELECT * FROM product;";

   List<Product> query = jdbcTemplate.query(sql, new RowMapper<Product>() {
      @Override
      public Product mapRow(ResultSet arg0, int arg1) throws SQLException {
         Product p = new Product();
         p.setPid(arg0.getInt("pid"));
         p.setPname(arg0.getString("pname"));
         p.setPrice(arg0.getDouble("price"));
         return p;
      }
   });

   for (Product product : query) {
      System.out.println(product);
   }
}

使用JdbcTemplate对象的query方法，并传入RowMapper匿名内部类
在匿名内部类中将结果集中的一行记录转成一个Product对象
```

## JdbcTemplate查询-BeanPropertyRowMapper返回自定义对象

**API介绍**

```java
public <T> List<T> query(String sql, RowMapper<T> rowMapper) 
执行查询语句，返回一个List集合，List中存放的是RowMapper指定类型的数据。
```

```java
public class BeanPropertyRowMapper<T> implements RowMapper<T> 
BeanPropertyRowMapper类实现了RowMapper接口
```

**使用步骤**  
定义Product类  
创建JdbcTemplate对象  
编写查询的SQL语句  
使用JdbcTemplate对象的query方法，并传入BeanPropertyRowMapper对象

```java
// query使用BeanPropertyRowMapper做映射返回对象
public static void test07() throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
// 查询数据的SQL语句
String sql = "SELECT * FROM product;";
List<Product> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));

for (Product product : list) {
    System.out.println(product);
}
}
```

___