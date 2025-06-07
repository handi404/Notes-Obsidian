# SSM整合项目创建

## ①导入相应的依赖坐标

```xml
    <!-- spring核心依赖坐标 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.5</version>
    </dependency>

    <!-- springaop织入依赖 -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.9.6</version>
    </dependency>

    <!-- springtx依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.3.5</version>
    </dependency>

    <!-- lombok工具依赖 -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.2</version>
    </dependency>


    <!-- spring jdbc依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.3.5</version>
    </dependency>

    <!-- mysql依赖-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.39</version>
    </dependency>
 
    <!-- Druid连接池 -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.1.10</version>
    </dependency>

    <!-- springmvc依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.5</version>
    </dependency>

    <!-- servlet依赖 -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
    </dependency>
    
    <!-- jsp依赖 -->
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
    </dependency>

    <!-- jackson依赖 -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.9.0</version>
    </dependency>
    
    <!-- mybatis依赖 -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.6</version>
    </dependency>

    <!--  mybatis sping整合依赖-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>2.0.6</version>
    </dependency>
    
    <!--log4j依赖-->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
```

## ②创建相应的包
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210416161139604.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)


## ③书写mybatis核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 加载properties配置文件 -->
    <!--<properties url=""></properties>-->
    
    <!-- 为指定的包下的类设置别名（设置pojo 使用首字母小写形式的别名） -->
    <!--<typeAliases>
        <package name=""/>
    </typeAliases>-->

    <!-- 配置mybatis数据源选择操作 -->
    <!--<environments default="">
        <environment id="">
            <transactionManager type=""></transactionManager>
            <dataSource type=""></dataSource>
        </environment>
    </environments>-->
    
    
    <!-- mybatis的全局配置 基本使用默认 其他可以通过mapper配置文件进行设置 -->
    <settings>
        <setting name="lazyLoadingEnabled" value="true"/><!-- 开启懒加载 -->
        <setting name="aggressiveLazyLoading" value="false"/><!-- 设置懒加载侵入方式 -->
        <setting name="logImpl" value="STDOUT_LOGGING"/><!-- 使用mybatis 自带的日志管理 -->
    </settings>
    
    <!-- 配置mybatis扫描xml文件所在的包 -->
    <!--<mappers>
        <package name=""/>
    </mappers>-->

</configuration>
```

## ④书写log4j与数据连接配置文件

**db.properties**

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mydb?characterEncoding=UTF-8
jdbc.username=root
jdbc.password=root
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



## **⑤配置spring相关核心配置文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    
    <!-- spring组件扫描 -->
    <!-- 将含有对应注解的类交由spring管理 可以实现IOC与DI AOP也必须切入由spring管理的类 -->
    <context:component-scan base-package="com.yunhe"/>
    
    <!-- 加载properties -->
    <!-- 可以在当前的配置文件中使用${key}的形式获取对应的value值 -->
    <context:property-placeholder location="classpath:db.properties"/>
    
    <!-- 配置数据源 -->
    <!-- 使用指定的连接池对数据源进行管理 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!-- 配置spring与mybatis整合 -->
    <!-- 配置SqlSessionFactoryBean对象，整合加载mybatis配置文件 使用mybatis进行数据源操作 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 将数据源交由mybatis进行管理 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置mybatis配置文件 -->
        <!-- 加载mybatis核心配置文件（进行修改时） -->
        <property name="configLocation" value="classpath:mybatis.xml"/>
        <!-- 配置mapper映射文件 -->
        <!-- 在创建mybatis时加载所有的mapper.xml -->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>

    </bean>


    <!-- mybatis.spring自动映射，DAO接口所在包名，Spring会自动查找其下的类 -->
    <!-- 根据加载的mapper.xml创建相应的实现类 并交由spring容器管理 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.yunhe.mapper" />
    </bean>
    
    <!--平台事务管理器-->
    <!-- 创建相应的事务代理对象交由spring容器管理 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 开启事务的注解驱动 -->
    <!-- 将拥有事务处理的方法进行事务代理 -->
    <tx:annotation-driven transaction-manager="transactionManager"/>
    
    <!-- 设置aop织入自动代理 -->
    <!-- 才会将相应的切面类织入指定的切入点（方法） -->
    <aop:aspectj-autoproxy/>
    
    <!-- springmvc视图解析器 -->
    <!-- 在controller返回的视图名进行拼接返回对应视图 -->
    <!-- 如果需要使用其他的视图模板或插件也是在视图解析器进行配置 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/view/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>

    <!-- 开启springmvc注解驱动 -->
    <mvc:annotation-driven/>

    <!-- 开启静态资源默认过滤 -->
    <mvc:default-servlet-handler/>
    
    <!-- 指定静态资源过滤 -->
     <!--<mvc:resources mapping="" location=""></mvc:resources>-->
    
</beans>
```

## ⑥配置web.xml

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>


  <!-- springmvc编码控制器 -->
  <filter>
    <filter-name>Encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>Encoding</filter-name>
    <url-pattern>*</url-pattern>
  </filter-mapping>


  <!-- springmvc前端控制器 -->
  <servlet>
    <servlet-name>mvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>mvc</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>


</web-app>

```

## ⑦书写相应的代码测试ssm整合是否成功

**User.java**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int uid;
    private String uusername;
    private String upassword;
}
```

**UserMapper.java**

```java
public interface UserMapper {
    ArrayList<User> selectAll();
}
```

**UserMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.yunhe.mapper.UserMapper">
    
    
    <select id="selectAll" resultType="com.yunhe.pojo.User">
        select * from user
    </select>
    
    
</mapper>
```

**UserService.java**

```java
public interface UserService {
    public ArrayList<User> findAll();
}
```

**UserServiceImpl.java**

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ArrayList<User> findAll() {
        return userMapper.selectAll();
    }
}
```

**UserController.java**

```java
@Controller
public class UserController {
    @Autowired
    private UserService userServiceImpl;
    @RequestMapping("/all")
    public String find(HttpServletRequest request){
        request.setAttribute("msg",userServiceImpl.findAll());
        return "success";
    }
}
```

**success.jsp**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${msg}
</body>
</html>
```








**UserController.java**


```java
@Controller
public class UserController {
    @Autowired
    private UserService userServiceImpl;
    @RequestMapping("/all")
    public String find(HttpServletRequest request){
        request.setAttribute("msg",userServiceImpl.findAll());
        return "success";
    }
}
```

**success.jsp**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${msg}
</body>
</html>
```







