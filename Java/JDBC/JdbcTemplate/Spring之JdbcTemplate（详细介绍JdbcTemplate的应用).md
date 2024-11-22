# **目录**

- [[#**什么是 JdbcTemplate**|**什么是 JdbcTemplate**]]
- [[#使用|使用]]
  - [[#使用#引入依赖|引入依赖]]
  - [[#使用#在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)|在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)]]
    - [[#在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)#将数据参数写信写入jdbc.properties|将数据参数写信写入jdbc.properties]]
    - [[#在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)#在xml配置文件中引入jdbc.properties|在xml配置文件中引入jdbc.properties]]
    - [[#在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)#在xml配置文件中创建druid数据库连接池|在xml配置文件中创建druid数据库连接池]]
  - [[#使用#配置 JdbcTemplate 对象，注入 DataSource|配置 JdbcTemplate 对象，注入 DataSource]]
  - [[#使用#创建数据库表以及创建对应的类|创建数据库表以及创建对应的类]]
    - [[#创建数据库表以及创建对应的类#创建数据库表t\_man|创建数据库表t\_man]]
    - [[#创建数据库表以及创建对应的类#实体类|实体类]]
    - [[#创建数据库表以及创建对应的类#Dao接口、实现类以及Service类|Dao接口、实现类以及Service类]]
  - [[#使用#开启组件扫面并将对应的类加上注解|开启组件扫面并将对应的类加上注解]]
  - [[#使用#**JdbcTemplate** **操作数据库（添加）**|**JdbcTemplate** **操作数据库（添加）**]]
  - [[#使用#查询某个值|查询某个值]]
  - [[#使用#根据条件查询返回某个对象|根据条件查询返回某个对象]]
  - [[#使用#查询对象集合|查询对象集合]]

___

## **什么是 JdbcTemplate**

Spring 框架对 JDBC 进行封装，使用 [JdbcTemplate](https://so.csdn.net/so/search?q=JdbcTemplate&spm=1001.2101.3001.7020) 方便实现对数据库操作。

## 使用

### 引入依赖

我这里将之前的项目中的依赖也放进来了，有的可能用不到。

```undefined
 <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.2</version>
        </dependency>
        <dependency>
            <groupId>aopalliance</groupId>
            <artifactId>aopalliance</artifactId>
            <version>1.0</version>
        </dependency>
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>3.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.7</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.9</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.19</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.3.16</version>
        </dependency>

    </dependencies>
```

### 在 spring 配置文件配置[数据库连接池](https://so.csdn.net/so/search?q=%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%9E%E6%8E%A5%E6%B1%A0&spm=1001.2101.3001.7020)

#### 将数据参数写信写入jdbc.properties

![](https://i-blog.csdnimg.cn/blog_migrate/7bb3c0bd3c5fd48a018a268f07608044.png)

```undefined
jdbc.properties内容

url=jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false
username=root
password=1230
driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 在xml配置文件中引入jdbc.properties

```undefined
<context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>
```

#### 在xml配置文件中创建druid数据库连接池

```undefined
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${username}"></property>
        <property name="password" value="${password}"></property>
        <property name="url" value="${url}"></property>
        <property name="driverClassName" value="${driver-class-name}"></property>
    </bean>
```

### 配置 JdbcTemplate 对象，注入 DataSource

```undefined
  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
```

### 创建数据库表以及创建对应的类

#### 创建数据库表t\_man

![](https://i-blog.csdnimg.cn/blog_migrate/795dbb1b4e4c0732e4b200ce55155a8c.png)

#### 实体类

![](https://i-blog.csdnimg.cn/blog_migrate/8d2458ccaa18e01e64b89b85c8bf86d6.png)

#### Dao接口、实现类以及Service类

分别为 ManDao、ManDaoImpl、ManService 里面目前没什么方法

### 开启组件扫面并将对应的类加上注解

```undefined
<context:component-scan base-package="com.csdn.dao,com.csdn.service"></context:component-scan>
```

![](https://i-blog.csdnimg.cn/blog_migrate/8f9e707365a6dfed9dc16f152fa33873.png)

![](https://i-blog.csdnimg.cn/blog_migrate/6b2bc0ce966acb166b3d065166f3da0c.png)

### **JdbcTemplate** **操作数据库（添加）**

我们是在测试方法中调用Service对象的方法，然后Service调用Dao的实现类的方法来实现的，因此我们需要现在Dao以及Service中增加方法并且在Service中注入Dao对象，在Dao中需要注入JdbcTemplate来实现方法。

```java
Dao接口
public interface ManDao {

    int addEntity(Man man);
}
```

```java
Dao实现类
@Repository
public class ManDaoImpl implements ManDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int addEntity(Man man) {
        String sql = "insert into t_man(user_name,sex) values(?,?)";
        int update = jdbcTemplate.update(sql, man.getUserName(), man.getSex());
        return update;
    }
}
```

```java
Service类
@Service
public class ManService {
    @Autowired
    private ManDao dao;
    public int insertMan(Man man) {
       return  dao.addEntity(man);
    }
}
```

测试

![](https://i-blog.csdnimg.cn/blog_migrate/487873c58a049fdd126fcc8d97efb4c9.png)

 ![](https://i-blog.csdnimg.cn/blog_migrate/e9c6584602bdd07771695f8968a88e27.png)

修改与删除调用的都是差不多这里不再写。

### 查询某个值

查询id为1的 username

Dao接口中加入以下方法

```java
String getNameByUserId(int id);
```

Dao实现类实现上述方法

```typescript
  @Override
    public String getNameByUserId(int id) {
        String sql = "select user_name from t_man where uid = ?" ;
       return  jdbcTemplate.queryForObject(sql,String.class,id);

    }
```

Service中调用

```typescript
 public String getNameById(int id) {
        return dao.getNameByUserId(id);
    }
}
```

测试

![](https://i-blog.csdnimg.cn/blog_migrate/ddf27f08656bcb6cab43b823497dbb67.png)

### 根据条件查询返回某个对象

加入如下方法

```java
Dao中方法
  Man getEntityById(int id);

实现类实现方法
    @Override
    public Man getEntityById(int id) {
        String sql = "select uid, user_name userName ,sex from t_man where uid =?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Man>(Man.class),id);
    }

Service中调用
    public Man getManById(int id) {
      return  dao.getEntityById(id);
    }
```

测试

![](https://i-blog.csdnimg.cn/blog_migrate/9c524f9cd238d68d593914109dda9bdc.png)

### 查询对象集合

![](https://i-blog.csdnimg.cn/blog_migrate/d479f942a11fb1e57c4438a165f95ecd.png)

 代码

```java
Dao方法
   List<Man> findAll();
实现类实现方法
  @Override
    public List<Man> findAll() {
        String sql = "select uid, user_name userName ,sex from t_man";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Man>(Man.class));
    }

Service中方法调用
    public List<Man> getAllMan() {
       return dao.findAll();
    }
```

测试

![](https://i-blog.csdnimg.cn/blog_migrate/0ec9fe69d3dfa6b6c283b945a1bfe7a0.png)