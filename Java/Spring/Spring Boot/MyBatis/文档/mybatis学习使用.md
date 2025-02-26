# 1、ORM与JPA

## 1.1 ORM

在使用JDBC 的时候，我们通常将数据直接返回，但现在也会将数据封装到实体类对象中，由对象携带数据。这样操作的时候，可以通过操作对象的方式操作数据。但是手写这类代码通常是繁琐的、重复的，如果有自动完成这些功能的程序就好了。

ORM（Object-Relationship-Mapping）：是对象关系映射的意思，它是一种思想，是指将数据库中的每一行数据用对象的形式表现出来。可以将 ORM 简单理解为上面我们提出的，可以自动将对象与数据进行映射的技术。

其映射关系如下表：

| 代码 | 数据库                     |
| ---- | -------------------------- |
| 类   | 表                         |
| 对象 | 一行数据（表中的某行数据） |
| 属性 | 列（一行数据中的某个列）   |



## 1.2 JPA

JPA（Java-Persistence-API）：是Java持久化接口的意思，它是JavaEE关于 ORM 思想的一套标准接口，仅仅是一套接口，不是具体的实现。

在 Java 生态圈中，早期有 2 个著名的 ORM 映射框架，它们就是 Hibernate 和 Ibatis。我们来看看这两个框架的特点：

Hibernate: JPA --> 完全ORM

- 使用 Hibernate ，可以通过一套程序搞定所有关系型数据库，可以用最小的代价完成关系型数据库的替换。但是完全的封装造成的就是效率不高，调优困难。
- 因为封装的问题，在数据库操作方面，给出了5套解决方案，操作上变得繁琐。

- 有公司会使用的 Spring-Data-JPA 技术便是基于Hibernate的实现

使用Hibernate可以对对象与对象的关系映射设计的好

MyBatis: 没有实现JPA --> 半个 ORM

- 编写原生的SQL，效率高

- 甚至可以不用管对象与对象的关系映射设计



## **1.3 Mybatis与Hibernate区别**

Hibernate是全ORM框架,mybatis是半ORM框架,

Hibernate实现了JPA接口,操作数据库无需书写sql语句

Mybatis没有实现JPA接口,需要书写原生的sql语句进行数据库操作

Hibernate在sql优化方面以及书写效率存在问题(相比较Mybatis)

Hibernate内部封装完善,学习成本高





# 2、MyBatis

## 2.1 MyBatis简介

MyBatis的前身就是iBatis,iBatis本是由Clinton Begin开发，后来捐给Apache基金会，成立了iBatis开源项目。2010年5月该项目由Apahce基金会迁移到了Google Code，并且改名为MyBatis。

MyBatis是一个数据持久层(ORM)框架。把实体类和SQL语句之间建立了映射关系，是一种半自动化的ORM实现。

## 2.2 MyBatis的优缺点

**优点**

- 简单易学，容易上手（相比于 Hibernate ） ： 基于SQL编程
- 消除了JDBC大量冗余的代码，不需要手动开关连接
- 很好的与各种数据库兼容（因为 MyBatis 使用JDBC来连接数据库，所以只要JDBC 支持的数据库 MyBatis 都支持，而JDBC提供了可扩展性，所以只要这个数据库有针对Java的jar包就可以就可以与 MyBatis 兼容），开发人员不需要考虑数据库的差异性。
- 提供了很多第三方插件（分页插件 / 逆向工程）
- 能够与Spring很好的集成
- 如果`使用映射文件`的话，可以让代码和配置文件完全分离。只要方法的定义没有改变，那么只需要修改配置文件就可以达到修改的目的。

**缺点** 

- SQL语句的编写工作量较大，尤其是字段多、关联表多时，更是如此，对开发人员编写SQL语句的功底有一定要求。
- SQL语句依赖于数据库，导致数据库移植性差，不能随意更换数据库



**与传统jdbc的比较**

减少了60%的代码量

最简单的持久化框架

架构级性能增强

SQL代码从程序代码中彻底分离，可重用

增强了项目中的分工

增强了移植性





## 2.3 Mybatis使用基本要素

一、mybatis-config .xml 全局配置文件,配置 Mybatis 的各种信息

二、mapper.xml 核心映射文件,映射实体类与数据库表的关系

三、SqlSession接口,使用mybatis的接口



## 2.4 javaWeb项目进行mybatis搭建

### 2.4.1 导入相应jar包

MyBatis是一个数据持久层(ORM)框架，所以进行使用时需要使用3个jar包

（1）mybaties的jar包  （2）log4j的jar包  （3）连接数据库的jar包

**maven坐标**

```xml
    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>
        
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>
    </dependencies>
```



### 2.4.2 书写配置文件

使用简单配置完成搭建，所以只配置一些基本就可以了，在resouces下新建mybatis-config.xml

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<environments default="development">
		<environment id="development">
			<!-- 使用jdbc的事务，mybatis进行管理 -->
			<transactionManager type="JDBC" />
			<!-- 使用jdbc的连接池连接数据库 -->
            <!-- 如果配置了<properties resource="db.properties"/> -->
            <!-- value可以使用value="${driver}"的形式进行书写 /> -->
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url" value="jdbc:mysql://localhost:3306/test" />
				<property name="username" value="root" />
				<property name="password" value="123456" />
			</dataSource>
		</environment>
	</environments>
	<!-- 映射文件的配置-->
	<mappers>
		<mapper resource="mapper/UserMapper.xml" />
	</mappers>
</configuration>
```


### 2.4.3 创建相应数据库

```sql
create table t_user(
	id int primary key auto_increment,
	name varchar(20),
	sal double,
  	birthday date
);
insert into t_user values(1,'鲁智深',200,now());
insert into t_user values(2,'武松',188,now());
select * from t_user;
```


### 2.4.4 创建映射实体类

数据类型必须映射一致，属性名可以与列名不一致，但需要书写requestMap进行映射否则会出现问题

```java
package com.yh.javabean;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private double sal;
    private Date birthday;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSal() {
        return sal;
    }

    public void setSal(double sal) {
        this.sal = sal;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sal=" + sal +
                ", birthday=" + birthday +
                '}';
    }
}

```

 

### 2.4.5 存放增删改查sql的配置文件

在resources下创建mapper包,保存名字为: 类名Mapper.xml或类名Dao.xml的配置文件

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 
	namespace唯一表示此名字下的crud语句
 -->
<mapper namespace="user">
	<!-- 
		id:在此命名空间下唯一标识
		resultType:查询结果的返回类型或者集合的泛型
	 -->
	<select id="selectAllUser" resultType="com.yh.javabean.User">
		select * from t_user
	</select>
	<!--  
	<insert id=""></insert>
	<delete id=""></delete>
	<update id=""></update>
	-->
</mapper>
```

 

### 2.4.6 加入log4j日志

查看程序运行的状态。新建一个log4j.properties

```XML
# Global logging configuration
log4j.rootLogger=DEBUG, stdout
# MyBatis logging configuration...
log4j.logger.org.mybatis.example.BlogMapper=TRACE
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

 

### 2.4.7 将SqlSessionFactory设计成单例模式，做一个工具类

就是将读取配置文件的过程封装成单例，避免使用时重复读取配置文件

```java
package com.yh.util;
import java.io.IOException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public  class MyBatisUtil {
	private MyBatisUtil(){}
	private static SqlSessionFactory sqlSessionFactory;
	static{
		try {
			sqlSessionFactory = new SqlSessionFactoryBuilder()
.build(Resources.getResourceAsStream("mybatis-config.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
}
```

### 2.4.8 代码测试

```java
package com.yh.util;

import com.yh.javabean.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MyBatisTest {

    public static void main(String[] args) {
        //1、创建SqlSessionFactory对象，也是单例模式的
        SqlSessionFactory factory = MyBatisUtil.getSqlSessionFactory();
        //2、创建SqlSession对象 true 自动提交事务 不写也没事 因为默认在sqlsession关闭时提交事务
        SqlSession session = factory.openSession(true);
        //3、调用session的方法namespace的名字.id
        List<User> users = session.selectList("user.selectAllUser");
        //4、打印测试
        for(User u:users){
            System.out.println(u);
        }
        //5、关闭资源
        session.close();
    }
}
```

 

**注意:**

sqlsession工具类加载mybatis核心配置文件一般在resources下并且名字为mybatis-config.xml

在执行方法时,通过对应mapper中定义的namespace.id的形式调用对应sql执行

在mybatis-config.xml中一定要配置sql映射文件 否则无法识别

核心配置文件与mapper映射配置都有头文件 但是不相同

mapper返回数据类型属性类全路径



## 2.5全局配置文件

mybatis-config.xml是系统的核心配置文件，包含数据源和事务管理器等设置和属性信息，XML文档结构如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/f01ef09912144980884476372c133c10.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

**configuration**：全局环境配置 ：用于配置mybatis全局属性

### 2.5.1 properties

**properties** ：可以以properties进行配置属性信息并在配置文件中使用可以省略

![在这里插入图片描述](https://img-blog.csdnimg.cn/52464ac055644acf886445fab48fbc4e.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

在当前文件中可以直接使用name当中数据进行使用，也可以读取properties文件，如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/207c9ae16435433095a6bd61396b181c.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

在配置文件中使用${key}的形式获取
```xml
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${name}"/>
                <property name="password" value="${password}"/>
```



### 2.5.2 settings

**settings** ：修改 MyBatis 在运行时的行为方式（通常使用默认值或只修改制定数据值）,注意不同版本mybatis可能默认值不同

```XML
<!--设置 -->
    <settings>
        <!--缓存配置的全局开关：如果这里设置成false，那么即便在映射器中配置开启也无济于事 -->
        <setting name="cacheEnabled" value="true" />
        <!--延时加载的全局开关 -->
        <setting name="lazyLoadingEnabled" value="false" />
        <!-- 是否允许单一语句返回多结果集 -->
        <setting name="multipleResultSetsEnabled" value="true" />
        <!-- 使用列标签代替列名，需要兼容驱动 -->
        <setting name="useColumnLabel" value="true" />
        <!-- 允许JDBC自动生成主键，需要驱动兼容。如果设置为true，则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍能正常工作 -->
        <setting name="useGeneratedKeys" value="false" />
        <!-- 指定MyBatis该如何自动映射列到字段或属性：NONE表示取消自动映射；PARTIAL表示只会自动映射，没有定义嵌套结果集和映射结果集；FULL会自动映射任意复杂的结果集，无论是否嵌套 -->
        <setting name="autoMappingBehavior" value="PARTIAL" />
        <!-- 配置默认的执行器：SIMPLE是普通的执行器；REUSE会重用预处理语句；BATCH会重用语句并执行批量更新 -->
        <setting name="defaultExecutorType" value="SIMPLE" />
        <!--设置超时时间：它决定驱动等待数据库响应的秒数,任何正整数-->
        <setting name="defaultStatementTimeout" value="25"/>
        <!--设置数据库驱动程序默认返回的条数限制，此参数可以重新设置,任何正整数 -->
        <setting name="defaultFetchSize" value="100" />
        <!-- 允许在嵌套语句中使用分页（RowBounds） -->
        <setting name="safeRowBoundsEnabled" value="false" />
        <!-- 是否开启自动驼峰命名规则，即从a_example到aExample的映射 -->
        <setting name="mapUnderscoreToCamelCase" value="true" />
        <!-- 本地缓存机制，防止循环引用和加速重复嵌套循环 -->
        <setting name="localCacheScope" value="SESSION" />
        <!-- 当没有为参数提供特定JDBC类型时，为空值指定JDBC类型。某些驱动需要指定列的JDBC类型，多数情况直接用一般类型即可，如NULL/VARCHAR/OTHER -->
        <setting name="jdbcTypeForNull" value="OTHER" />
        <!-- 指定触发延迟加载的方法，如equals/clone/hashCode/toString -->
        <setting name="lazyLoadTriggerMethods" value="equals" />
    </settings>
```



### 2.5.3 typeAliases

 **typeAliases** ： 为 Java 类型命名一个短的名字（在之后进行数据操作时使用）,扫描指定的包,使用 Bean 的首字母小写的非限定类名来作为它的别名 

```xml
    <!--  为指定的类起别名 一般都是为javabean起别名  -->
    <!--  可以使用别名简化javabean的书写 -->
    <typeAliases>
        <!--  1.为指定的类设置别名  -->
        <!--  这样在mapper中就可以直接使用account代替对应的类 -->
          <typeAlias type="com.yunhe.javabean.Account" alias="account"/>

        <!--  2.为指定包下所有的类设置别名  -->
        <!--  会自动扫描指定的包 并使用首写字母小写的类名当做别名Accpunt->account   Student->student  -->
       <!-- <package name="com.yunhe.javabean"/>-->
    </typeAliases>
```



### 2.5.4 typeHandlers

**typeHandlers** ：类型处理器，MyBatis 在设置预处理语句（PreparedStatement）中的参数或从结果集中取出一个值时， 都会用类型处理器将获取到的值以合适的方式转换成 Java 类型。默认已经提供了默认的类型处理器，可以通过这个属性进行自定义类型处理器的使用，具体可以参考https://blog.csdn.net/qq_41879343/article/details/104915460



### 2.5.5 objectFactory

**objectFactory** ：对象工厂， 每次 MyBatis 创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成实例化工作。 默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认无参构造方法，要么通过存在的参数映射来调用带有参数的构造方法。 如果想覆盖对象工厂的默认行为，可以通过创建自己的对象工厂来实现，具体可以参考https://blog.csdn.net/qq_41879343/article/details/104915460



### 5.5.6 plugins

 **plugins 插件** ：plugins插件，MyBatis 允许你在映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：

- Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
- ParameterHandler (getParameterObject, setParameters)
- ResultSetHandler (handleResultSets, handleOutputParameters)
- StatementHandler (prepare, parameterize, batch, update, query)

这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看 MyBatis 发行包中的源代码。 如果你想做的不仅仅是监控方法的调用，那么你最好相当了解要重写的方法的行为。 因为在试图修改或重写已有方法的行为时，很可能会破坏 MyBatis 的核心模块。 **这些都是更底层的类和方法，所以使用插件的时候要特别当心。**



### 2.5.7 environments

 **environments** ：配置数据库相关环境

1)MyBatis可以配置多种环境，比如开发、测试和生产环境需要有不同的配置

2)每种环境使用一个environment标签进行配置并指定唯一标识符

3)可以通过environments标签中的default属性指定一个环境的标识符来快速的切换环境

4)environment-指定具体环境

**environment** ：环境变量 ，书写在environments中  拥有id属性指定当前环境的唯一标识 可以用environments defaule属性指向

**transactionManager** :事务管理器,书写在environment中 拥有type属性，值可以书写：

1)JDBC - 这个类型直接全部使用 JDBC 的提交和回滚功能。它依靠使用连接的数据源来管理事务的作用域。

2)MANAGED - 这个类型什么不做 ， 它从不提交 、 回滚和关闭连接 。 而是让窗口来管理事务的全部生命周期 。

3)自定义 - 实现TransactionFactory接口，type=全类名/别名

**dataSource** ：数据源 ，书写在environment中 拥有type属性，值可以书写：

​     1)UNPOOLED：不使用连接池， UnpooledDataSourceFactory

​     2)POOLED：使用连接池， PooledDataSourceFactory

​     3)JNDI： 在EJB 或应用服务器这类容器中查找指定的数据源

​     4)自定义：实现DataSourceFactory接口，定义数据源的获取方式。



### 2.5.8 mappers

**mappers** ：映射器

1)用来在mybatis初始化的时候，告诉mybatis需要引入哪些Mapper映射文件

2) mapper逐个注册SQL映射文件

**mapper**：映射 具体的映射方式

resource属性：单独引入相应xml配置（可以通过mapper配置中的namespace与id动态执行）

class属性：单独引入mapper接口对象（可以自动扫描与mapper接口名字相同的xml，class与xml同一目录）

package属性：引入指定包下接口对象（可以自动扫描与mapper接口名字相同的xml，class与xml同一目录）



**Mapper映射执行sql**

通过mybatis生成接口的实现类并保存,调用接口的形式执行对应方法

1.在mapper.xml保存的文件夹中创建同名的接口

```java
//书写在与对应xml同级目录下 且名字相同 AccountMapper.xml=>AccountMapper.java
public interface AccountMapper {
    //方法名与xml中对应id相同
    public List<Account> selectAll();
}
```

2.创建与对应xml中id相同的方法(一一对应不要多写)

```xml
    <select id="selectAll" resultType="account">
        select * from account
    </select>
```

3.设置当前xml配置文件的namespace为接口全路径

```xml
<!--  设置当前的xml是为指定mapper映射书写的配置文件  -->
<mapper namespace="mapper.AccountMapper">
    <select id="selectAll" resultType="account">
        select * from account
    </select>
</mapper>
```

3.在核心配置文件中配置映射接口

```xml
    <mappers>
        <!--  class用于引入mapper的映射接口 会自动根据接口名寻找同级下的对应的xml文件  -->
        <mapper class="mapper.AccountMapper"/>
    </mappers>
```

5.使用getMapper方法生成实现类并调用对应方法执行

```java
        SqlSessionFactory sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用getMapper方法 将需要创建映射对象的mapper.class传入
        //执行后会根据对应的xml生成对应实现类
        AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
        List<Account> accounts = mapper.selectAll();
        for (Account a:accounts   ) {
            System.out.println(a);
        }
        sqlSession.close();
```







## 2.6 Mapper配置文件

MyBatis 的真正强大在于它的映射语句，也是它的魔力所在。由于它的异常强大，映射器的 XML 文件就显得相对简单。如果拿它跟具有相同功能的 JDBC 代码进行对比，你会立即发现省掉了将近 95% 的代码。MyBatis 就是针对 SQL 构建的，并且比普通的方法做的更好。

SQL 映射文件有很少的几个顶级元素（按照它们应该被定义的顺序）： ![在这里插入图片描述](https://img-blog.csdnimg.cn/6d94c9e303404438a091051b1e8ccdaf.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

**mapper**：mapper配置文件根目录，拥有namespace属性

在MyBatis中，Mapper中的namespace用于绑定Dao接口的，即面向接口编程。 它的好处在于当使用了namespace之后就可以不用写接口实现类，业务逻辑会直接通过这个绑定寻找到相对应的SQL语句进行对应的数据处理

接口完全限定名

```XML
<!--填写接口名-->
<mapper namespace="com.dao.UserDao"><mapper/>
```

### 2.6.1 select标签

**select**：书写在mapper标签中的标签，拥有如下常用属性

| 属性          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| id            | 在命名空间中唯一的标识符，可以被用来引用这条语句。           |
| parameterType | 将会传入这条语句的参数类的完全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset。 |
| resultType    | 从这条语句中返回的期望类型的类的完全限定名或别名。注意如果是集合情形，那应该是集合可以包含的类型，而不能是集合本身。使用 resultType 或 resultMap，但不能同时使用。 |
| resultMap     | 外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，对其有一个很好的理解的话，许多复杂映射的情形都能迎刃而解。使用 resultMap 或 resultType，但不能同时使用。 |
| flushCache    | 将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：false。 |
| useCache      | 将其设置为 true，将会导致本条语句的结果被二级缓存，默认值：对 select 元素为 true。 |




```java
public interface AccountMapper {
    //查找所有数据
    ArrayList<Account> selectAll();

    //根据id查找数据
    Account selectById(int id);

    //根据username与password查找数据
    Account selectByUsernameAndPassword(String username,String password);

    //根据Account查找数据
    Account selectByAccount(Account account);

    //根据ID集合查找数据
    ArrayList<Account>  selectByIdList(ArrayList<Integer> IdList);

    //根据map集合查找数据
    ArrayList<Account> selectByMap(HashMap<String,String> map);

}
```



```xml
<mapper namespace="mapper.AccountMapper">

    <select id="selectAll" resultType="account">
        select * from account
    </select>

    <!-- mybatis拥有类型转换器 会自动解析请求中的数据类型(非自定义类型) -->
    <select id="selectById" resultType="account" >
        select * from account where id= #{arg0}
    </select>

    <!-- 如果参数为多个不同类型那么基本使用自动的类型解析 -->
    <!-- 在进行多个参数传递时 如果没有指定参数的类型 那么会自动进行转换识别  -->
    <!-- 在使用时必须使用解析自定义的类型代替参数 arg0.... 或param1.... -->
    <select id="selectByUsernameAndPassword" resultType="account" >
        select * from account where username= '${param1}' and password='${param2}'
    </select>
    <!-- #{}与${}的区别 -->
    <!--  都是对指定的参数进行获取填入,#{}使用的是占位符的形式进行填值 可以有效解决sql注入
      ${} 使用的是字符串拼接形式 需要注意sql语句的书写规则 不能防止sql注入-->


    <!--  当使用引用类型时(自定义)时  需要设置引用类型的传入  -->
    <!--  可以直接通过属性名 获取对应对象属性的值(必须提供getter方法) -->
    <select id="selectByAccount" resultType="account" parameterType="account">
        select * from account where id=#{id} and username=#{username} and password=#{password}
    </select>

    <!--  可以获取集合参数中某一数据进行赋值  -->
    <select id="selectByIdList" resultType="account">
        select * from account where id in (${list[0]},${list[1]})
    </select>

    <select id="selectByMap" resultType="account">
        select * from account where username=#{username} and password=#{password}
    </select>
    
</mapper>
```



### 2.6.2 Insert、Update、Delete标签

**Insert, Update, Delete**：书写在mapper标签中的标签，拥有如下常用属性

| 属性             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| id               | 命名空间中的唯一标识符，可被用来代表这条语句。               |
| parameterType    | 将要传入语句的参数的完全限定类名或别名。这个属性是可选的，因为 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset。 |
| flushCache       | 将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true（对应插入、更新和删除语句）。 |
| useGeneratedKeys | （仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段），默认值：false。 |
| keyProperty      | （仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 子元素设置它的键值，默认：unset。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| keyColumn        | （仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |

```java
public interface AccountMapper {
    //添加数据
    int insertAccount(Account account);

    //修改数据
    int updatePasswordById(int id,String password);

    //删除数据
    int deleteById(int id);

}
```



```xml
<mapper namespace="mapper.AccountMapper">
 

    <insert id="insertAccount" parameterType="account">
        insert into account (username,password) values (#{username},#{password})
    </insert>

    <update id="updatePasswordById">
        update account set password=#{arg1} where id=#{arg0}
    </update>

    <delete id="deleteById">
        delete from account where id=#{arg0}
    </delete>

</mapper>
```

### 2.6.3 cache标签

**cache:** 二级缓存标签,书写在mapper标签中 无属性,需要在mybatis.xml中配置开启二级缓存(默认开启)

```XML
<settings>
	<setting name="cacheEnabled" value="true"/>
</settings>
```
mybatis的二级缓存默认在缓存在内存中，不支持分布式服务器的缓存，所以，一般使用第三方的缓存插件，比如ehcache,redis

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/89b94c91506846409f5eaaec2b371145.png)

**useCache**：属性，对于开启二级缓存的查询，每次会先去缓存进行查找之后才会执行，如果不想让当前执行查询使用缓存需要将值设置为false

```XML
<select id="findOrderListResultMap" resultMap="ordersUserMap" useCache="false">
	sql…
</select>
```

 

**flushCache**： 属性在mapper的同一个namespace中，如果有其它insert、update、delete操作数据后需要刷新缓存，如果不执行刷新缓存会出现脏读。 设置statement配置中的flushCache="true" 属性，默认情况下为true即刷新缓存，如果改成false则不会刷新。使用缓存时如果手动修改数据库表中的查询数据会出现脏读。

```XML
<insert id="insertUser" parameterType="com.yh.mybatis.po.User" flushCache="true">
	sql…
</insert>
```



**sql**：标签， 可以被用来定义可重用的 SQL 代码段，可以包含在其他语句中。它可以被静态地(在加载参数) 参数化. 不同的属性值通过包含的实例变化. 比如：

```XML
<sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
```

**include**:标签， 可以调用定义的sql标签，可以设置property标签  设置name属性与value属性为sql标签中设置的参数赋值

```XML
<select id="selectUsers" resultType="map">
  select
    <include refid="userColumns"><property name="alias" value="t1"/></include>,
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  from some_table t1
    cross join some_table t2
</select>
```



```xml
    <!--  sql标签 用于预定义代码块 拼写sql语句 通常与include一起使用  -->
    <!--  在查询时建议使用指定字段代替* 但是在语句书写时会书写多次 这个时候就可以通过sql进行定义和使用  -->
    <sql id="accountField"> account.id,account.username,account.password </sql>
    <!-- include标签可以引用定义的sql标签 拼接sql -->
    <!--  refid书写书写要引入sql标签的id -->
    <select id="selectAll" resultType="account">
        select
        <include refid="accountField"></include>
        from account
    </select>

    <!-- sql标签也支持参数的设置 但是由于使用的是sql拼接 所以必须使用${} -->
    <sql id="name">${name}.id,${name}.password,${name}.username</sql>
    <!--  在include引用的通过属性标签设置对应的参数  -->
    <!-- mybatis拥有类型转换器 会自动解析请求中的数据类型(非自定义类型) -->
    <select id="selectById" resultType="account" >
        select
        <include refid="mapper.AccountMapper.name"><property name="name" value="account"/></include>
        from account where id= #{arg0}
    </select>
```







### **2.6.4 获取添加中自增主键**

(1)通过属性进行获取

| 属性             | 描述                                         |
| ---------------- | -------------------------------------------- |
| useGeneratedKeys | 为 true 则返回主键的值                       |
| keyProperty      | 实体类中属性名                               |
| keyColumn        | 数据库主键字段名(如果id是第一列可以省略不写) |

```xml
    <!-- 只需要在对应的添加标签中书写对应属性即可  -->
    <!-- useGeneratedKeys 是否使用自动生成的主键 默认为false -->
    <!-- keyProperty 传入对象保存主键的属性名 -->
    <!-- keyColumn 结果集中主键列的名字 如果 主键为第一列可以省略 -->
    <!-- 在执行添加后会自动将新增数据的主键赋值给对对应参数的对应属性值 -->
    <insert id="insertUser" parameterType="user" useGeneratedKeys="true" keyProperty="uid" keyColumn="uid">
        insert into user (uusername,upassword) values (#{uusername},#{upassword})
    </insert>
```

 

(2)通过selectKey标签进行获取

| 属性        | 描述                                         |
| ----------- | -------------------------------------------- |
| resultType  | 查询主键结果类型,可以不写会自动识别          |
| order       | BEFORE 或 AFTER。指定sql执行的顺序           |
| keyProperty | 实体类中属性名                               |
| keyColumn   | 数据库主键字段名(如果id是第一列可以省略不写) |

```xml
    <!-- 正常书写添加数据 额外定义添加语句执行后的查询语句查询新增的key -->
     <insert id="insertUser" parameterType="user">
        /*
         resultType 查询返回的结果
         keyProperty 传入对象保存数据的属性
         keyColumn 查询结果主键的列名
         order 执行查询语句在其他语句的时间 after之后  befor之前
        */
        <selectKey resultType="int" keyColumn="uid" keyProperty="uid" order="AFTER">
            SELECT LAST_INSERT_ID()  as uid
        </selectKey>
        insert into user (uusername,upassword) values (#{uusername},#{upassword})
     </insert>
```





### **2.6.5 使用uuid生成主键**

(1)通过后台生成

```java
        //方式1
        //由后台使用UUID工具类生成UUID进行存入
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //System.out.println(uuid);
        Student student1=new Student(uuid,"张三");
```

(2)通过selectKey标签生成

| 属性        | 描述                                         |
| ----------- | -------------------------------------------- |
| resultType  | 查询主键结果类型,可以不写会自动识别          |
| order       | BEFORE 或 AFTER。指定sql执行的顺序           |
| keyProperty | 实体类中属性名                               |
| keyColumn   | 数据库主键字段名(如果id是第一列可以省略不写) |

```xml
        <!-- 先使用selectkey获取主键值 之后进行添加 -->
        <insert id="insertUser" parameterType="user">
            <selectKey resultType="string" keyProperty="uid" keyColumn="uid" order="BEFORE">
                select replace(UUID(),'-','') as uid from dual
            </selectKey>
            insert into user(uid,uusername,upassword) values(#{uid},#{uusername},#{upassword})
        </insert>
```





### 2.6.6 resultmap结果映射

#### 2.6.6.1 基本标签属性

**resultMap** ：用来描述获取数据后与对象的映射（数据库字段与对象属性名），通常情况下使用默认的映射（数据库字段与属性名相同），但有时需要进行不同的映射，这时候就需要使用requestMap进行设置在resultType中使用。具体如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/bb16decfc4124aed9bd1d7dd0e61b89c.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

**id**：标签，用来标识主键

column：数据库字段属性  property：实体类字段属性 

**result**：标签 ，用于其他列标识

**colleaction**：标签，用来标识集合属性

property：实体类字段属性 

ofType：集合存储数据类型属性

column属性：用于标识查询数据列（会获取对应列数据传入相应的查询语句）

select属性：懒加载执行时调用获取数据的方法(可以调用其他mapper中的方法，通过namespace.id使用)

fetchType：局部懒加载默认fetchType="lazy"深入式  eager侵入式

**association**：标签，用于标识类属性

property：实体类字段属性 

javaType：类型属性(配置数据库数据与java的映射 一般不配置)

column属性：用于标识查询数据列（会获取对应列数据传入相应的查询语句）

select属性：懒加载执行时调用获取数据的方法(可以调用其他mapper中的方法，通过namespace.id使用)

fetchType：局部懒加载默认fetchType="lazy"深入式  eager侵入式



**数据库sql**

```sql


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '/select', '查询权限');
INSERT INTO `permission` VALUES ('2', '/insert', '新增权限');
INSERT INTO `permission` VALUES ('3', '/update', '修改权限');
INSERT INTO `permission` VALUES ('4', '/delete', '删除权限');
INSERT INTO `permission` VALUES ('5', '/liuda', '瞎溜达权限');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin', '超级管理员');
INSERT INTO `role` VALUES ('2', 'user', '普通用户');

-- ----------------------------
-- Table structure for `role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `rid` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '4');
INSERT INTO `role_permission` VALUES ('1', '5');
INSERT INTO `role_permission` VALUES ('2', '1');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'zhangsan', '123456', '138468885889');
INSERT INTO `user` VALUES ('2', 'lisi', '123456', '1384688888588');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('2', '2');

```











#### **2.6.6.2 resultMap** **使用场景**

##### 1.为查询结果列与对象不同进行映射

当数据库列与对应的bean属性不一致时,不能使用自动映射,需要书写resultMap进行手动映射配置

```xml
    <!-- 当数据库列名与对象属性名不匹配时 无法存入对应数据 -->
    <!-- 这个时候就不能直接使用resultType进行映射 需要自己定义映射 -->

    <!-- id 当前结果集唯一标识 查询语句可以使用resultMap指定对应id的映射 -->
    <!-- type 就是结果集需要的映射对象 建议书写全面 防止混淆-->
    <resultMap id="mu" type="com.yunhe.javabean.MyUser">
        <!-- id标签用于设置当前对象的主键存储 -->
        <!-- javatype jdbctype typehandler 都会自动识别不需要定义 -->
        <!-- property用于定义java中id的属性名 -->
        <!-- column 用于定义数据结果中对应的列名 -->
        <id property="id" column="uid" />
        <!-- result用于当前对象非主键属性的定义 -->
        <result property="username" column="uusername"/>
        <result property="password" column="upassword"/>
    </resultMap>

    <!-- 将查询语句的结果改为resultMap=id的形式 -->
    <select id="selectAllMyUser" resultMap="mu" >
        select * from user
    </select>
```



优点：

1.减少了后台直接对数据库的展示（隐藏数据库实际字段）

2.对已有系统实现兼容（数据库重用）







##### 2.进行一对一查询自定义映射

需要在定义bean对象时将其关系进行定义

与角色一对一关系所以需要先定义角色对象

```java
public class Role implements Serializable {
    private int rid;
    private String rname;
    private String rdesc;
    ...
}
```

定义用户对象 ,通过设置属性的形式设置一对一关系,就是设置其拥有对应的属性

```java
public class User implements Serializable {
    private int uid;
    private String uusername;
    private String upassword;
    private Role role;
    ...
}
```



```xml
  <!-- 一对一映射必须使用resultMap进行定义  -->
    <resultMap id="u" type="com.yunhe.javabean.User">
        <!-- 先书写当前对象可以直接映射的属性与列映射 -->
        <id property="uid" column="uid"/>
        <result property="uusername" column="uusername"/>
        <result property="upassword" column="upassword"/>
        <!-- 对应当前对象中的自定义属性 可以使用association标签进行标识 -->
        <!-- property属性名 就是在对象中定义的属性名称 -->
        <association property="role" javaType="com.yunhe.javabean.Role">
            <!-- 之后书写方式与直接在resultMap书写该属性相同 -->
            <id property="rid" column="rid"/>
            <result property="rname" column="rname"/>
            <result property="rdesc" column="rdesc"/>
        </association>
    </resultMap>

    <select id="selectAllUser" resultMap="u">
        select u.*, r.*
        from `user` u
                 left join user_role ur on u.uid = ur.uid
                 left join role r on ur.rid = r.rid
    </select>
```

如果设置的映射数据列与属性匹配可以使用autoMapping="true" 进行自动映射 否则必须书写对应属性



##### 3.进行一对多查询自定义映射

就是在对象中书写集合属性 保存指定对象的集合

```java
public class Permission implements Serializable {
    private int pid;
    private String purl;
    private String pdesc;
 	....   
}
```

书写角色对象保存权限集合属性

```java
public class Role implements Serializable {
    private int rid;
    private String rname;
    private String rdesc;
    private ArrayList<Permission> plist;
	...    
}
```

```xml
    <resultMap id="r" type="com.yunhe.javabean.Role" autoMapping="true">
        <!-- 在进行一对多查询时 需要将多条对应语句存入指定集合 所以如果使用autoMapping -->
        <!-- 没有定义主键列的话 可能导致数据的全部赋值(就不会存储至对应集合中了) -->
        <!-- 一定要设置id列 -->
        <id property="rid" column="rid"/>
<!--        <result property="rname" column="rname"/>-->
<!--        <result property="rdesc" column="rdesc"/>-->
        <!--  autoMapping数据列与属性相同不用配置      -->
        <collection property="plist" ofType="com.yunhe.javabean.Permission" autoMapping="true">
<!--            <id property="pid" column="pid"/>-->
<!--            <result property="purl" column="purl"/>-->
<!--            <result property="pdesc" column="pdesc"/>-->
        </collection>
    </resultMap>

    <select id="selectAllRole" resultMap="r">
        select r.*, p.*
        from role r
                 left join role_permission rp on r.rid = rp.rid
                 left join permission p on rp.pid = p.pid
    </select>
```



##### 4.懒加载/延迟加载

在进行查询时有时需要根据需求加载对应数据(淘宝详情页,会随着下拉加载)

懒加载的配置有两种 

一种是在mybatis全局配置文件中配置全局延迟加载

```xml
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
```

二是在resultmap中使用属性进行全局配置的覆盖

```xml
fetchType="lazy"
```



延迟加载的使用更便于面向对象编程思路的理解,会将查询对应数据的语句放置在对应的xml中进行调用

```xml
<mapper namespace="mapper.RoleMapper">
    <resultMap id="pp" type="role" autoMapping="true">
        <id property="rid" column="rid"/>
        <!-- property 保存集合的属性名 -->
        <!-- column 指定查询需要的参数  会将当前对应数据传入查询语句 -->
        <!-- select可以调用对应空间下查询语句进行结构的查询 -->
        <!-- fetchType=lazy 开启懒加载   默认为eager 侵入式查询 会直接调用查询语句执行-->
        <collection property="ps" column="rid" select="mapper.PermissionMapper.selectByRid"
                    fetchType="lazy"/>
    </resultMap>

    <select id="selectAll" resultMap="pp">
        select *
        from role
    </select>

</mapper>
```



```xml
<mapper namespace="mapper.PermissionMapper">

    <select id="selectByRid" resultType="permission">
        select rp.rid,p.* from role_permission rp left join permission p on rp.pid=p.pid where rp.rid=#{arg0}
    </select>
</mapper>
```



在对查询结果操作时,只有使用到对应权限数据时才会执行对应的查询将对应角色的权限查询出来







## 2.7 动态sql标签

动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。

使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。

### 2.7.1 if 标签 

使用动态 SQL 最常见情景是根据条件包含 where 子句的一部分。比如：

实例:书写根据id与username查询的方法,如果id为null则根据username查询 如果都不为null根据两个查询

```XML
    <select id="selectByUser" parameterType="user" resultType="user">
        select * from user where  1=1
           /* 如果id为int类型 那么默认值为0 可以使用interger设置id类型 */
        <if test="uid!=0">
            and uid=#{uid}
        </if>
        <if test="uusername!=null">
            and uusername=#{uusername}
        </if>
        <if test="upassword!=null">
            and upassword=#{upassword}
        </if>
    </select>
```



### 2.7.2 choose、when、otherwise 标签

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句

```XML
  <!--    /*根据传入参数数据查询 如果id不为空根据id查询 为空则根据uusername 为空则继续根据upassword 都为空查询全部 */-->
    <select id="selectByOneUser" parameterType="user" resultType="user">

        select * from user
        <choose>
            <when test="uid!=0">
                where uid=#{uid}
            </when>
            <when test="uusername!=null">
                where uusername=#{uusername}
            </when>
            <when test="upassword!=null">
                where upassword=#{upassword}
            </when>
            <otherwise>
                where 1=1
            </otherwise>
        </choose>
    </select>
```

### **2.7.3 trim、where、set标签**

*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

```XML
<select id="findUser" resultType="User">
  SELECT * FROM User
  <where>
    <if test="id != null">
         state = #{state}
    </if>
    <if test="name!= null">
        AND title like #{title}
    </if>
  </where>
</select>
```

 

如果 *where* 元素与你期望的不太一样，你也可以通过自定义 trim 元素来定制 *where* 元素的功能。比如，和 *where* 元素等价的自定义 trim 元素

*prefixOverrides* 属性会忽略通过管道符分隔的文本序列（注意此例中的空格是必要的）.上述例子会移除所有prefixOverrides属性中指定的内容，并且插入 *prefix* 属性中指定的内容。

```XML
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

用于动态更新语句的类似解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，忽略其它不更新的列。比如：

```XML
<update id="updateUserIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```


### 2.7.4 foreach遍历标签

动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）。比如：

```XML
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list" open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

item：遍历取出的数据

index：遍历数据的索引

colleaction：遍历的数据集合

open：开始拼接时添加的字符串

separator：每次遍历中间的字符串

close：结束后最后添加的字符串





#### 批量删除

传入要删除数据的主键，通过foreach标签遍历组成sql语句进行删除

```sql
    <delete id="deleteList">
        delete from user where uid in
        <foreach collection="list" index="i" item="uid" open="(" close=")" separator=",">
            #{uid}
        </foreach>
    </delete>
```



#### 批量添加

传入添加的对象集合，通过标签遍历组成sql语句进行添加

```sql
    <insert id="insertList">
        insert into user (username,password,phone) values
        <foreach collection="list" item="user" separator=",">
            (#{user.username},#{user.password},#{user.phone})
        </foreach>
    </insert>

```










## 2.8 mybatis注解开发

mybatis可以使用xml文件来写sql语句，也可以通过注解来编写简单的sql语句，参考[官方文档](http://www.mybatis.org/mybatis-3/zh/java-api.html) 

在之前的开发中，我们使用mybatis，需要

1，配置文件，

2，然后创建dao接口，定义方法

3，再然后就要创建mapper.xml文件，在mapper.xml文件中编写sql语句，

4，最后再把mapper文件配置在mybatis主配置文件中就可以进行测试了

使用注解的方式，我们可以在dao接口中直接在方法上写sql语句，不需要创建mapper.xml文件了

使用注解也需要在mybatis配置文件中配置mapper接口的package扫描

### 2.8.1 基础常用注解

#### @Param()注解

语法:@Param(value="绑定值")可以简写为@Param("绑定值")

用于接口方法存在多个参数进行绑定传值

```java
public int selectByNameAndJob(@Param("name")int name,@Param("job")int job);
```

 

#### @Select()注解

**语法**:@Select(value="查询语句")可以简写为@Select("查询语句")

等价于mapper.xml中的<select>

```java
    @Select("select * from emp")
    List<Emp> allEmp();
```

 


#### @Insert()注解

**语法**:@Insert(value="添加语句")可以简写为@Insert("添加语句")

等价于mapper.xml中的<insert>

```java
    @Insert("insert into emp (ename,job,deptno,sal,hiredate) value(#{ename},#{job},#{deptno},#{sal},#{hiredate})")
    int insertEmp(Emp emp);
```


#### @Update()注解

**语法**:@Update(value="修改语句")可以简写为@Update("修改语句")

等价于mapper.xml中的<update>

```java
    @Update("update emp set ename=#{ename} where empno=#{empno}")
    int updateEmp(Emp emp);
```

 

#### @Delete()注解

**语法**:@Delete(value="删除语句")可以简写为@Select("删除语句")

等价于mapper.xml中的<delete>

```java
    @Delete("delete from emp where empno=#{empno}")
    int deleteEmp(int empno);
```

 



### 2.8.2 使用注解完成自定义结果集映射

#### @Results()注解

**语法**:@Results(id="唯一标识",value={result标签数组})

等价于mapper.xml中的<resultMap>

#### @Result()注解

**语法**:@Result(id="true/false是否为主键",column="数据库列",property="属性名")

等价于mapper.xml中的<result>与<id>

```java
    //查询全部数据
    /* 因为该映射书写在当前方法上 所以无需通过id调用 */
    @Select("select * from user")
    /*  Results 用于在当前方法上定义关系映射相当于mapper中的resultMap标签 */
    @Results(id = "mu", value = {
            /*  Result根据id是否为true 相当于mapper中的id 与 result标签 */
            @Result(id = true, column = "uid", property = "id"),
            @Result(id = false, column = "uusername", property = "username"),
            @Result(id = false, column = "upassword", property = "password")
    })
    ArrayList<MyUser> selectAllMyUser();
```

#### @resultMap()注解

**语法**:@resultMap(value="返回结果集映射id")可以简写为@resultMap("返回结果集映射id")

等价于mapper.xml中的resultMap属性

```java
    @Select("select * from user where uid=#{uid}")
    @ResultMap("mu")
    /* 可以通过ResultMap标签 调用其他方法定义的映射 相当于mapper中的resultMap属性  */
    ArrayList<MyUser> selectMyUserById(@Param("uid") int uid);

```





### 2.8.3 使用注解完成一对一sql的书写

#### @One()注解

**语法**:@One(select="执行方法",fetchType="加载方式(FetchType.LAZY深入懒加载)")

one是@Result标签的一个属性,添加后相当于将result标签转换为mapper.xml中的<association>

```java
public interface UserMapper {
    @Select("select * from user")
    @Results(id = "u_r", value = {
            /* 定义主键列*/
            @Result(id = true, property = "uid", column = "uid"),
            @Result( property = "uusername", column = "uusername"),
            @Result(property = "upassword",column = "upassword"),
            /* 对于一对一查询 property代表对应的存储数据的对象   column代表查询条件对应的值 一般使用主键
            * one 代表当前查询结果类型为一个数据(一对一查询)
            * one标签用于定义一对一查询相关属性
            * select 代表当前一对一查询调用的其他查询结果 使用mapper全路径.方法名的形式进行调用
            * fetchType = FetchType.LAZY 代表开启懒加载
            * */
            @Result(property = "role",column = "uid",one = @One(select="mapper.RoleMapper.selectByUid",fetchType = FetchType.LAZY))
    })
    ArrayList<User> selectAll();
}
```

 

```java
public interface RoleMapper {
    @Select("select * from user_role ur left join role r on ur.rid=r.rid where uid = #{uid}")
    Role selectByUid(@Param("uid") int uid);
}
```



### 2.8.4 使用注解完成一对多sql的书写

#### @Many()注解

**语法**:@Many(select="执行方法",fetchType="加载方式(FetchType.LAZY深入懒加载)")

Many是@Result标签的一个属性,添加后相当于将result标签转换为mapper.xml中的<colleaction>

```java
public interface RoleMapper {
    @Select("select * from role")
    @Results(id = "r_p", value = {
            @Result(id = true,property = "rid",column = "rid"),
            @Result(property = "rname",column = "rname"),
            @Result(property = "rdesc",column = "rdesc"),
            /* 与一对一书写方式一样只不过一对多使用的是many属性 进行多条结果的映射配置 */
            @Result(property ="ps",column = "rid",many = @Many(select="mapper.PermissionMapper.selectByRid",fetchType = FetchType.LAZY))
    })
    ArrayList<Role> selectAll();
}
```



```java
public interface PermissionMapper {

    //根据rid查询
    @Select("select * from role_permission rp left join permission p on rp.pid=p.pid where rid=#{rid}")
    ArrayList<Permission> selectByRid(@Param("rid")int rid);
}
```





注意:在进行sql书写时常常将不同表sql书写在不同的位置,所以在进行一对一、一对多书写是select对应的值可以书写为接口全路径.方法名







### 2.8.5 使用注解完成动态sql的书写

使用Mybatis注解实现sql语句，但是有些时候有些字段是空的，这时候这个空的字段就要从条件查询语句中删除，这个时候就需要用到动态Sql。

使用方式很简单在原有的基础上使用{<script> 动态sql语句 </script>}进行包裹即可

```java
    //根据集合查询数据
    @Select({"<script>" +
            "select * from user where uid in " +
            "<foreach collection='idList' item='i' open='(' close=')' separator=',' >" +
            "            #{i}" +
            "</foreach>" +
            "</script>"})
    ArrayList<User> selectByList(@Param("idList") ArrayList<Integer> idList);
```





### 