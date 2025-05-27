## MyBatis简介

MyBatis的前身就是iBatis,iBatis本是由Clinton Begin开发，后来捐给Apache基金会，成立了iBatis开源项目。2010年5月该项目由Apahce基金会迁移到了Google Code，并且改名为MyBatis。

MyBatis是一个数据持久层(ORM)框架。把实体类和SQL语句之间建立了映射关系，是一种半自动化的ORM实现。

## MyBatis的优点

1.基于SQL语法，简单易学。

2.能了解底层组装过程。  

3.SQL语句封装在配置文件中，便于统一管理与维护，降低了程序的耦合度。

4.程序调试方便。

## 与传统jdbc的比较

减少了60%的代码量

最简单的持久化框架

架构级性能增强

SQL代码从程序代码中彻底分离，可重用

增强了项目中的分工

强了移植性

## Mybatis使用基本要素

一、mybatis-config .xml 全局配置文件

二、mapper.xml 核心映射文件

三、SqlSession接口

## mybatis快速搭建

### 1、导入相应jar包或导入依赖

MyBatis是一个数据持久层(ORM)框架，所以进行使用时需要使用3个jar包

（1）mybaties的jar包  （2）log4j的jar包  （3）连接数据库的jar包

```xml
        <!-- mybatis相关依赖 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.5</version>
        </dependency>

        <!-- log4j依赖 -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>

          <!-- mysql驱动依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>
```

### 2、书写配置文件

使用简单配置完成搭建，所以只配置一些基本就可以了，在src下新建mybatis-config.xml

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
                <property name="url" value="jdbc:mysql://localhost:3306/mydb" />
                <property name="username" value="root" />
                <property name="password" value="root" />
            </dataSource>
        </environment>
    </environments>
    <!-- 映射文件的配置-->
    <mappers>
        <mapper resource="mapper/UserMapper.xml" />
    </mappers>
</configuration>
```

### 3、创建相应数据库

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

### 4、创建映射实体类

数据类型必须映射一致，属性名可以与列名不一致，但需要书写requestMap进行映射否则会出现问题

```java
package cn.com.bochy.entity;
import java.util.Date;
public class User {
    private int id;
    private String name;
    private double sal;
    private Date birthday;
    get....
    set.....        
}
```

### 5、存放增删改查sql的配置文件

一般名字为: 类名Mapper.xml或类名Dao.xml

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
    <select id="selectAllUser" resultType="cn.com.bochy.entity.User">
        select * from t_user
    </select>
    <!--  
    <insert id=""></insert>
    <delete id=""></delete>
    <update id=""></update>
    -->
</mapper>
```

### 6、加入log4j日志

查看程序运行的状态。在根目录下新建一个log4j.properties

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

### 7、将SqlSessionFactory设计成单例模式，做一个工具类

就是将读取配置文件的过程封装成单例，避免使用时重复读取配置文件

```java
package cn.com.bochy.util;
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

### 8、代码测试

```java
public class MyBatisTest {
    @Test
    public void test_selectAllUser() throws Exception{
        //1、创建SqlSessionFactory对象，也是单例模式的
        SqlSessionFactory factory = MyBatisUitl.getSqlSessionFactory();
        //2、创建SqlSession对象
        SqlSession session = factory.openSession();
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

## 全局配置文件

mybatis-config.xml是系统的核心配置文件，包含数据源和事务管理器等设置和属性信息，XML文档结构如下：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c6fa1b0dd1239b01eab4846b222f50f9.png)

**configuration**：全局环境配置 ：用于配置mybatis全局属性

**properties** ：可以以properties进行配置属性信息并在配置文件中使用可以省略

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/59bc1b03dca7ba081cc2097cf2a8240a.png)

在当前文件中可以直接使用name当中数据进行使用，也可以读取properties文件，如下：
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/43f544c913b45e0fbef08e065e7abbd7.png)

**settings** ：修改 MyBatis 在运行时的行为方式（通常使用默认值或只修改制定数据值）

```XML
<settings>
    <setting name="cacheEnabled" value="true"/>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
    <setting name="multipleResultSetsEnabled" value="true"/>
    <setting name="useColumnLabel" value="true"/>
    <setting name="useGeneratedKeys" value="false"/>
    <setting name="autoMappingBehavior" value="PARTIAL"/>
    <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
    <setting name="defaultExecutorType" value="SIMPLE"/>
    <setting name="defaultStatementTimeout" value="30"/>
    <setting name="defaultFetchSize" value="200"/>
    <setting name="safeRowBoundsEnabled" value="false"/>
    <setting name="mapUnderscoreToCamelCase" value="false"/>
    <setting name="localCacheScope" value="SESSION"/>
    <setting name="jdbcTypeForNull" value="OTHER"/>
    <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
</settings>
```

| 设置参数                      | 描述                                                                                                                                                              | 有效值                                                                                        | 默认值                                                          |
| ------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------ |
| cacheEnabled              | 该配置影响的所有映射器中配置的缓存的全局开关                                                                                                                                          | true \| false                                                                              | true                                                         |
| lazyLoadingEnabled        | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态                                                                                                  | true \| false                                                                              | false                                                        |
| aggressiveLazyLoading     | 当启用时，对任意延迟属性的调用会使带有延迟加载属性的对象完整加载；反之，每种属性将会按需加载。                                                                                                                 | true \| false                                                                              | true                                                         |
| multipleResultSetsEnabled | 是否允许单一语句返回多结果集（需要兼容驱动）。                                                                                                                                         | true \| false                                                                              | true                                                         |
| useColumnLabel            | 使用列标签代替列名。不同的驱动在这方面会有不同的表现， 具体可参考相关驱动文档或通过测试这两种不同的模式来观察所用驱动的结果。                                                                                                 | true \| false                                                                              | true                                                         |
| useGeneratedKeys          | 允许 JDBC 支持自动生成主键，需要驱动兼容。 如果设置为 true 则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍可正常工作（比如 Derby）。                                                                                | true \| false                                                                              | False                                                        |
| autoMappingBehavior       | 指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示取消自动映射；PARTIAL 只会自动映射没有定义嵌套结果集映射的结果集。 FULL 会自动映射任意复杂的结果集（无论是否嵌套）。                                                             | NONE, PARTIAL, FULL                                                                        | PARTIAL                                                      |
| defaultExecutorType       | 配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（prepared statements）； BATCH 执行器将重用语句并执行批量更新。                                                                         | SIMPLE REUSE BATCH                                                                         | SIMPLE                                                       |
| defaultStatementTimeout   | 设置超时时间，它决定驱动等待数据库响应的秒数。                                                                                                                                         | Any positive integer                                                                       | Not Set (null)                                               |
| defaultFetchSize          | Sets the driver a hint as to control fetching size for return results. This parameter value can be override by a query setting.                                 | Any positive integer                                                                       | Not Set (null)                                               |
| safeRowBoundsEnabled      | 允许在嵌套语句中使用分页（RowBounds）。                                                                                                                                        | true \| false                                                                              | False                                                        |
| mapUnderscoreToCamelCase  | 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。                                                                                       | true \| false                                                                              | False                                                        |
| localCacheScope           | MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。 | SESSION \| STATEMENT                                                                       | SESSION                                                      |
| jdbcTypeForNull           | 当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。 某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。                                                                   | JdbcType enumeration. Most common are: NULL, VARCHAR and OTHER                             | OTHER                                                        |
| lazyLoadTriggerMethods    | 指定哪个对象的方法触发一次延迟加载。                                                                                                                                              | A method name list separated by commas                                                     | equals,clone,hashCode,toString                               |
| defaultScriptingLanguage  | 指定动态 SQL 生成的默认语言。                                                                                                                                               | A type alias or fully qualified class name.                                                | org.apache.ibatis.scripting.xmltags.XMLDynamicLanguageDriver |
| callSettersOnNulls        | 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这对于有 Map.keySet() 依赖或 null 值初始化的时候是有用的。注意基本类型（int、boolean等）是不能设置成 null 的。                                    | true \| false                                                                              | false                                                        |
| logPrefix                 | 指定 MyBatis 增加到日志名称的前缀。                                                                                                                                          | Any String                                                                                 | Not set                                                      |
| logImpl                   | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。                                                                                                                                 | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | Not set                                                      |
| proxyFactory              | 指定 Mybatis 创建具有延迟加载能力的对象所用到的代理工具。                                                                                                                               | CGLIB \| JAVASSIST                                                                         | JAVASSIST (MyBatis 3.3 or above)                             |

 **typeAliases** ： 为 Java 类型命名一个短的名字（在之后进行数据操作时使用）

 ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f2c90fbe8b509434827236caffc524b6.png)

**typeHandlers** ：类型处理器，MyBatis 在设置预处理语句（PreparedStatement）中的参数或从结果集中取出一个值时， 都会用类型处理器将获取到的值以合适的方式转换成 Java 类型。默认已经提供了默认的类型处理器，可以通过这个属性进行自定义类型处理器的使用，具体可以参考https://blog.csdn.net/qq_41879343/article/details/104915460

**objectFactory** ：对象工厂， 每次 MyBatis 创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成实例化工作。 默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认无参构造方法，要么通过存在的参数映射来调用带有参数的构造方法。 如果想覆盖对象工厂的默认行为，可以通过创建自己的对象工厂来实现，具体可以参考https://blog.csdn.net/qq_41879343/article/details/104915460

 **plugins 插件** ：plugins插件，MyBatis 允许你在映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：

- Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
- ParameterHandler (getParameterObject, setParameters)
- ResultSetHandler (handleResultSets, handleOutputParameters)
- StatementHandler (prepare, parameterize, batch, update, query)

这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看 MyBatis 发行包中的源代码。 如果你想做的不仅仅是监控方法的调用，那么你最好相当了解要重写的方法的行为。 因为在试图修改或重写已有方法的行为时，很可能会破坏 MyBatis 的核心模块。 **这些都是更底层的类和方法，所以使用插件的时候要特别当心。**

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

**mappers** ：映射器

1)用来在mybatis初始化的时候，告诉mybatis需要引入哪些Mapper映射文件

2) mapper逐个注册SQL映射文件

**mapper**：映射 具体的映射方式

resource属性：单独引入相应xml配置（可以通过mapper配置中的namespace与id动态执行）

class属性：单独引入mapper接口对象（可以自动扫描与mapper接口名字相同的xml，class与xml同一目录）

package属性：引入指定包下接口对象（可以自动扫描与mapper接口名字相同的xml，class与xml同一目录）
