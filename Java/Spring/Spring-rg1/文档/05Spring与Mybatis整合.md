## 一、 MyBatis与Spring的集成
在学习mybatis配置时，对于mybatis-config配置的时候我们发现，大致是需要配置三个方面：setting、datasource、mappers

而mybatis的setting往往使用默认配置，所以我们经常配置datasource数据源与mappers映射，但学习spring之后发现，对于datasource的配置交由spring进行管理，所以在spring与mybatis整合后mybatis的配置文件中将不需要配置datasource，mybatis的配置几乎都会在Spring配置之中完成。当然要想要实现spring与mybatis的整合，其中最重要的就是 mybatis-spring.jar 包

 - mybatis-spring会用于帮助你将 MyBatis 代码无缝地整合到 Spring 中。
 - Spring 将会加载必要的 MyBatis 工厂类和 Session 类
 - 提供一个简单的方式来注入 MyBatis 数据映射器和 SqlSession 到业务层的 bean 中。
 - 方便集成 Spring 事务
 - 翻译 MyBatis 的异常到 Spring 的 DataAccessException 异常(数据访问异常)中。

Mybatis-Spring 兼容性，我们在选择Spring、MyBatis以及mybatis-spring时，应注意版本之间的兼容性
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200925162004618.png)


## 二、spring与mybatis快速整合
##### ①导入spring与mybatis相应坐标
```xml
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <!--定义Spring版本号-->
        <org.springframework.version>5.0.5.RELEASE</org.springframework.version>
    </properties>

    <dependencies>
        <!-- 单元测试相关依赖 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>

        <!-- spring必要依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>
        <!-- spring aop织入依赖 -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.13</version>
        </dependency>

        <!-- mysql驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>

        <!-- 数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!-- mybatis相关依赖 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.5</version>
        </dependency>

        <!-- mybatis与spring对接依赖 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.4</version>
        </dependency>

        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>
    </dependencies>
```
##### ②创建数据库连接配置文件
```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8
jdbc.username=root
jdbc.password=root

# 初始化大小，最小，最大
initialSize=10
minIdle=6
maxActive=50
```
##### ③创建mybatis配置文件
mybatis文件与之前不同，之前在mybatis-config.xml中配置数据库连接的，现在要把这些放在spring的配置文件中，所以mybatis配置文件中只写setting配置
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	        <settings>
            <!--  缓冲配置  -->
            <setting name="cacheEnabled" value="true"/>
            <!-- 懒加载 -->
            <setting name="lazyLoadingEnabled" value="true"/>
            <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
            <setting name="aggressiveLazyLoading" value="false"/>
            <!-- 缓冲区配置 -->
            <setting name="localCacheScope" value="SESSION"/>

            <!-- 日志输出  -->
            <setting name="logImpl" value="STDOUT_LOGGING"/>

        </settings>
	<!--<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url"
					value="jdbc:mysql://localhost:3306/test" />
				<property name="username" value="root" />
				<property name="password" value="root" />
			</dataSource>
		</environment>
	</environments> -->
	<!-- 映射文件的配置
	<mappers>
		<package name="com.dao" />
	</mappers> -->
</configuration>
```
**log4j.properties**

```properties
# Global logging configuration
log4j.rootLogger=DEBUG, stdout
# MyBatis logging configuration...
log4j.logger.org.mybatis.example.BlogMapper=TRACE
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```



##### ④创建spring配置文件

**2)spring配置头文件与约束地址**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
```
**2)spring注解开发**

```xml
    <!-- 开启spring 注解扫描 -->
    <context:component-scan base-package="com"/>
```

**3)导入properties配置文件**

```xml
    <!-- 加载类路径下的properties配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties"/>
```

**4)dataSource数据源配置**

```xml
     <!-- datasource数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <property name="initialPoolSize" value="${initialSize}"/>
        <property name="minPoolSize" value="${minIdle}"/>
        <property name="maxPoolSize" value="${maxActive}"/>
    </bean>
```

**5)sqlSessionFactory数据会话工厂配置**

```xml
 <!-- 配置SqlSessionFactory对象 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置mybatis配置文件 -->
        <property name="configLocation" value="classpath:mybatis.xml"/>
        <!-- 配置mapper映射文件 -->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    </bean>
```
**6)mapper接口配置**

```xml
    <!-- mybatis.spring自动映射，DAO接口所在包名，Spring会自动查找其下的类 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.mapper" />
    </bean>
```

**7)事务管理器**
```xml
    <!--平台事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
```
**8)设置事务扫描**
```xml
    <!-- 开启事务控制的注解支持 --> 
	<tx:annotation-driven transaction-manager="transactionManager" /> 
```
##### ⑤根据配置文件创建相应的包、接口、实体类
**Account**

```java
package com.pojo;
public class Account {
    private String name;
    private double money;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
```
AccountMapper

```java
package com.mapper;
import com.yunhe.pojo.Account;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AccountMapper {
    public List<Account> selectAll();
}
```
##### ⑥书写mapper的sql映射配置文件
AccountMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.yunhe.mapper.AccountMapper">
    <select id="selectAll" resultType="com.yunhe.pojo.Account">
        select * from account
    </select>
</mapper>
```
##### ⑦书写测试代码

```java
import com.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;

@ContextConfiguration({"classpath:spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class T {
    @Resource
    AccountMapper accountMapper;
     @Test
    public void asd(){
         accountMapper.selectAll();
     }
}
```
**结果：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200925172657950.png)

## 三、mybatis-spring整合jar包功能

### ①SqlSessionFactoryBean
在基础的 MyBatis 用法中，是通过 SqlSessionFactoryBuilder 来创建 SqlSessionFactory 的。 而在 MyBatis-Spring 中，则使用 SqlSessionFactoryBean 来创建。
要创建工厂 bean，将下面的代码放到 Spring 的 XML 配置文件中：

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource" />
</bean>
```

**1)DataSource** 
SqlSessionFactory 有一个唯一的必要属性：用于 JDBC 的 DataSource。这可以是任意的 DataSource 对象，它的配置方法和其它 Spring 数据库连接是一样的。

 **2)configLocation**
一个常用的属性是 configLocation，它用来指定 MyBatis 的 XML 配置文件路径。它在需要修改 MyBatis 的基础配置非常有用。通常，基础配置指的是\<settings> 或 \<typeAliases> 元素。
需要注意的是，这个配置文件并不需要是一个完整的 MyBatis 配置。确切地说，任何环境配置（\<environments>），数据源（\<DataSource>）和 MyBatis 的事务管理器（\<transactionManager>）都会被忽略。SqlSessionFactoryBean 会创建它自有的 MyBatis 环境配置（Environment），并按要求设置自定义环境的值。


如果 MyBatis 在映射器类对应的路径下找不到与之相对应的映射器 XML 文件，那么也需要配置文件。这时有两种解决办法：第一种是手动在 MyBatis 的 XML 配置文件中的 \<mappers> 部分中指定 XML 文件的类路径；第二种是设置工厂 bean 的 mapperLocations 属性。

**3)mapperLocations** 
mapperLocations 属性接受多个资源位置。这个属性可以用来指定 MyBatis 的映射器 XML 配置文件的位置。属性的值是一个 Ant 风格的字符串，可以指定加载一个目录中的所有文件，或者从一个目录开始递归搜索所有目录。比如:

```xml
    <!-- 配置SqlSessionFactory对象 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置mybatis配置文件 -->
        <property name="configLocation" value="classpath:mybatis.xml"/>
        <!-- 配置mapper映射文件 -->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    </bean>
```

###  ②MapperScannerConfigurer
**basePackage**
MapperScannerConfigurer:有一个重要的属性basePackage用于扫描映射指定包下所有的mapper映射接口，mybatis执行时会与已经加载的mapper对应的xml进行映射调用相应的方法执行sql语句返回结果

```xml
   <!-- mybatis.spring自动映射，DAO接口所在包名，Spring会自动查找其下的类 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.yunhe.mapper" />
    </bean>
```

