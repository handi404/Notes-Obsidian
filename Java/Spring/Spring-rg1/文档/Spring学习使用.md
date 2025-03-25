
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/74ea839abbc14ebc8dbf18b55c153779.png)

# 一、Spring概述
## 1.1 Spring是什么

Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 IoC（Inverse Of Control：反转控制）和 AOP（Aspect Oriented Programming：面向切面编程）为内核。

提供了展现层 SpringMVC和持久层 Spring JDBCTemplate以及业务层事务管理等众多的企业级应用技术，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架

## 1.2 Spring的优势

方便解耦，简化开发

AOP 编程的支持

声明式事务的支持

方便程序的测试

## 1.3 Spring的体系结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200919151212624.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)
# 二、Spring快速入门

## 2.1 导入 Spring 开发的基本包坐标

```xml
<dependencies>
    <!--导入spring的context坐标，context依赖core、beans、expression-->
      <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.0.5.RELEASE</version>
      </dependency>
  </dependencies>
```

## 2.2 编写 接口和实现类

```java
----UserService.java
public interface UserService {  
    public void save();
}
----UserServiceImpl.java
public class UserServiceImpl implements UserService {  
        @Override  
        public void save() {
        	System.out.println("UserService save method running....");
	}
}
```
## 2.3 创建 Spring 核心配置文件

创建resources文件并创建applicationContext.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```
在 Spring 配置文件中配置 UserServiceImpl 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- id:实例对象的名字   class:实例对象全路径 -->
    <bean id="userService" class="com.yh.service.impl.UserServiceImpl"></bean>
    
</beans>
```
## 2.4 使用 Spring 的 API 获得 Bean 实例

```java
public void test(){
		ApplicationContext applicationContext = new  
             ClassPathXmlApplicationContext("applicationContext.xml");
             UserService userService = (UserService) applicationContext.getBean("userService");   				 
             userService.save();
 }
```
##  2.5 Spring相关API 

**ApplicationContext的继承体系**

applicationContext：接口类型，代表应用上下文，可以通过其实例获得 Spring 容器中的 Bean 对象

**ApplicationContext的实现类**

1）ClassPathXmlApplicationContext 

​      它是从类的根路径下加载配置文件 推荐使用这种

2）FileSystemXmlApplicationContext 

​      它是从磁盘路径上加载配置文件，配置文件可以在磁盘的任意位置。

3）AnnotationConfigApplicationContext

​      当使用注解配置容器对象时，需要使用此类来创建 spring 容器。它用来读取注解。

 **getBean()方法使用**

```java
//通过核心配置文件中的id进行获取指定的bean对象
public Object getBean(String name) throws BeansException {  
	assertBeanFactoryActive();   
	return getBeanFactory().getBean(name);
}
//通过匹配spring管理的bean的类型进行获取指定的bean对象
public <T> T getBean(Class<T> requiredType) throws BeansException {   			    	
   assertBeanFactoryActive();
	return getBeanFactory().getBean(requiredType);
}
```

其中，当参数的数据类型是字符串时，表示根据Bean的id从容器中获得Bean实例，返回是Object，需要强转。

当参数的数据类型是Class类型时，表示根据类型从容器中匹配Bean实例，当容器中相同类型的Bean有多个时，则此方法会报错

**getBean()方法使用**

```java
ApplicationContext applicationContext = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
  UserService userService1 = (UserService) applicationContext.getBean("userService");
  UserService userService2 = applicationContext.getBean(UserService.class);
```



# 三、Spring配置文件标签基本介绍

## 3.1 Bean标签基本配置 

用于配置对象交由Spring 来创建。

默认情况下它调用的是类中的无参构造函数，如果没有无参构造函数则不能创建成功。

基本属性：

id：Bean实例在Spring容器中的唯一标识

class：Bean的全限定名称

## 3.2 Bean标签范围配置 

scope:指对象的作用范围，取值如下： 

| 取值范围         | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| singleton        | 默认值，单例的                                               |
| prototype        | 多例的                                                       |
| request          | WEB   项目中，Spring   创建一个   Bean   的对象，将对象存入到   request   域中 |
| session          | WEB   项目中，Spring   创建一个   Bean   的对象，将对象存入到   session   域中 |
| global   session | WEB   项目中，应用在   Portlet   环境，如果没有   Portlet   环境那么globalSession   相当于   session |

1）当scope的取值为singleton时

​      Bean的实例化个数：1个

​      Bean的实例化时机：当Spring核心文件被加载时，实例化配置的Bean实例

​      Bean的生命周期：

对象创建：当应用加载，创建容器时，对象就被创建了

对象运行：只要容器在，对象一直活着

对象销毁：当应用卸载，销毁容器时，对象就被销毁了

2）当scope的取值为prototype时

​      Bean的实例化个数：多个

​      Bean的实例化时机：当调用getBean()方法时实例化Bean

对象创建：当使用对象时，创建新的对象实例

对象运行：只要对象在使用中，就一直活着

对象销毁：当对象长时间不用时，被 Java 的垃圾回收器回收了

## 3.3 Bean生命周期配置

init-method：指定类中的初始化方法名称

destroy-method：指定类中销毁方法名称

**在spring管理的类中分别书写对应的初始化与销毁方法**

```java
    public void init(){
        System.out.println("初始化方法");
    }

    public void destroy(){
        System.out.println("销毁方法");
    }
```
**在配置文件中通过方法名与属性进行配置**

```xml
  <bean id="userDao" class="com.yunhe.dao.impl.UserDaoImpl"  init-method="init" destroy-method="destroy" ></bean>
```

在spring容器创建后才会调用init方法进行初始化，在spring容器销毁时会先自动调用所有管理bean的destroy对象进行销毁后关闭(默认创建对象为单例，在初始化容器时就会创建)



# 四、SpringIOC/DI配置开发

##  4.1 Spring IOC/DI 概念

**IOC控制反转Inversion of Control**
把创建和查找依赖对象的控制权交给了容器，由容器进行注入组合对象，所以对象与对象之间是松散耦合，这样也方便测试，利于功能复用，更重要的是使得程序的整个体系结构变得非常灵活。

**DI依赖注入Dependency Injection**
在程序运行期间由容器动态的将某个依赖关系注入到组件之中

## 4.2 Bean实例化三种方式

#### 1) 使用无参构造方法实例化

​      它会根据默认无参构造方法来创建类对象，如果bean中没有默认无参构造函数，将会创建失败

```xml
<bean id="UserService" class="com.yunhe.dao.impl.UserServiceImpl"></bean>
```

#### 2) 工厂静态方法实例化

​      工厂的静态方法返回Bean实例

```java
public class StaticFactoryBean {
    public static UserService createUserService(){    
    return new UserServiceImpl();
    }
}
```

```xml
<bean id="UserService" class="com.yunhe.factory.StaticFactoryBean" 
      factory-method="createUserService" />
```

#### 3) 工厂实例方法实例化

​      工厂的非静态方法返回Bean实例

```java
public class DynamicFactoryBean {  
	public UserService createUserService(){        
		return new UserServiceImpl(); 
	}
}
```

```xml
<bean id="factoryBean" class="com.yh.factory.DynamicFactoryBean"/>
<bean id="userDao" factory-bean="factoryBean" factory-method="createUserDao"/>
```


## 4.3 Bean的依赖注入方式
### 1) 有参构造注入

创建有参构造

```java
public class UserServiceImpl implements UserService {
userDao userDao;
public UserServiceImpl (userDao userDao){
this.userDao=userDao;
}
 }
```
配置Spring容器调用有参构造时进行注入
```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.yh.service.impl.UserServiceImpl">      		   	<constructor-arg name="userDao" ref="userDao"></constructor-arg>
</bean>
```

### 2) set方法

在UserServiceImpl中添加setUserDao方法

```java
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;  
        } 
    @Override    
    public void save() {      
   		 userDao.save();
	}
}
```

配置Spring容器调用set方法进行注入

```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.yh.service.impl.UserServiceImpl">
	<property name="userDao" ref="userDao"/>
</bean>
```

### 3) 自动注入（接口注入、注解注入）

通过autowire="byName|byType"属性设置根据属性名或类型自动注入

```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.yh.service.impl.UserServiceImpl" autowire="byName|byType">
</bean>
```

开启注解开发后书写注解自动进行注入操作

## 4.4 Bean的依赖注入的数据类型

上面的操作，都是注入的引用Bean，除了对象的引用可以注入，普通数据类型，集合等都可以在容器中进行注入。

**注入数据的三种数据类型** 

普通数据类型
引用数据类型
集合数据类型

### 1) 普通数据类型的注入

```java
public class UserDaoImpl implements UserDao {
private String company;
    private int age;
    public void setCompany(String company) {
        this.company = company;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void save() {
        System.out.println(company+"==="+age);
        System.out.println("UserDao save method running....");   
    }
}

```
```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
    <property name="company" value="云和数据"></property>
    <property name="age" value="15"></property>
</bean>
```

### 2) 自定义引用数据类型（User）的注入

```xml
<bean id="r1" class="com.yh.domain.Role"/>
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
     <property name="r" ref="r1"></property>
</bean>
```

### 3) 集合数据类型（List\<String >）的注入

```java
public class UserDaoImpl implements UserDao {
	private List<String> strList;
	public void setStrList(List<String> strList) {
		this.strList = strList;
	}
	public void save() {
        System.out.println(strList);
        System.out.println("UserDao save method running....");
	}
}
```

```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
    <property name="strList">
        <list>
            <value>aaa</value>
            <value>bbb</value>
            <value>ccc</value>
        </list>
    </property>
</bean>
```

### 4) 集合数据类型（List\<User>）的注入

```java
public class UserDaoImpl implements UserDao {
	private List<User> userList;
	public void setUserList(List<User> userList) {
	this.userList = userList;  
 }
public void save() {
	System.out.println(userList);
	System.out.println("UserDao save method running....");
	}
}
```

```xml
<bean id="u1" class="com.yh.domain.User"/>
<bean id="u2" class="com.yh.domain.User"/>
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
    <property name="userList">
        <list>
            <bean class="com.yh.domain.User"/>
            <bean class="com.yh.domain.User"/>
            <ref bean="u1"/>
            <ref bean="u2"/>       
        </list>
    </property>
</bean>
```

### 5) 集合数据类型（ Map<String,User> ）的注入

```java
public class UserDaoImpl implements UserDao {
    private Map<String,User> userMap;
    public void setUserMap(Map<String, User> userMap) {
    this.userMap = userMap;
    }    
public void save() {      
	System.out.println(userMap);
	System.out.println("UserDao save method running....");
	}
}
```

```xml
<bean id="u1" class="com.yh.domain.User"/>
<bean id="u2" class="com.yh.domain.User"/>
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
    <property name="userMap">
        <map>            
            <entry key="user1" value-ref="u1"/>
            <entry key="user2" value-ref="u2"/>
        </map>
    </property>
</bean>
```

### 6) 集合数据类型（Properties）的注入

```java
public class UserDaoImpl implements UserDao {
    private Properties properties;
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
	public void save() {
		System.out.println(properties);
		System.out.println("UserDao save method running....");
	}
}
```

```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl">
    <property name="properties">
        <props>
            <prop key="p1">aaa</prop>
            <prop key="p2">bbb</prop> 
            <prop key="p3">ccc</prop>
        </props>
    </property>
</bean>
```







﻿#  五、Spring配置数据源

## 5.1 数据源（连接池）的作用

数据源(连接池)是提高程序性能出现的

事先实例化数据源，初始化部分连接资源

使用连接资源时从数据源中获取

使用完毕后将连接资源归还给数据源

常见的数据源(连接池)：DBCP、C3P0、BoneCP、Druid等

**开发步骤**

①导入数据源的坐标和数据库驱动坐标

②创建数据源对象

③设置数据源的基本连接数据

④使用数据源获取连接资源和归还连接资源

## 5.2 数据源的手动创建

### **1) 导入导入对应的jar包坐标**

**spring的jar包**

```xml
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.1.2.RELEASE</version>
    </dependency>
```

**mysql的jar包**

```xml
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.39</version>
    </dependency>
```

**c3p0以及druid连接池jar包**

```xml
   <!-- C3P0连接池 -->
    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.2</version>
    </dependency>
    <!-- Druid连接池 -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.1.10</version>
    </dependency>
```



### **2) 创建C3P0、Druid连接池**

```java
        //创建数据源
        ComboPooledDataSource dataSource1 = new ComboPooledDataSource();
        //设置数据库连接参数
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        //获得连接对象
        Connection connection = dataSource1.getConnection();
        System.out.println(connection);
```

```java
    //创建数据源
    DruidDataSource dataSource2 = new DruidDataSource();
    //设置数据库连接参数
    dataSource.setDriverClassName("com.mysql.jdbc.Driver"); 
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");   
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    //获得连接对象
    Connection connection = dataSource2.getConnection();    
    System.out.println(connection);
```

### 3) 提取jdbc.properties配置文件

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root
```

### **4) 读取jdbc.properties配置文件创建连接池**

```java
    //加载类路径下的jdbc.properties
    ResourceBundle rb = ResourceBundle.getBundle("jdbc");
	//c3p0连接池
    ComboPooledDataSource dataSource 1= new ComboPooledDataSource(); 
    dataSource.setDriverClass(rb.getString("jdbc.driver"));   
    dataSource.setJdbcUrl(rb.getString("jdbc.url")); 
    dataSource.setUser(rb.getString("jdbc.username")); 
    dataSource.setPassword(rb.getString("jdbc.password"));
    Connection connection1 = dataSource1.getConnection();   
	//druid连接池
	DruidDataSource dataSource2 = new DruidDataSource();
    dataSource.setDriverClassName(rb.getString("jdbc.driver")); 
    dataSource.setUrl(rb.getString("jdbc.url"));   
    dataSource.setUsername(rb.getString("jdbc.username"));
    dataSource.setPassword(rb.getString("jdbc.password"));
    Connection connection2 = dataSource2.getConnection();    
```

## 5.3 Spring配置数据源

### 1) 连接池配置

可以将DataSource的创建权交由Spring容器去完成

DataSource有无参构造方法，而Spring默认就是通过无参构造方法实例化对象的

DataSource要想使用需要通过set方法设置数据库连接信息，而Spring可以通过set方法进行字符串注入

```xml
 <!-- 将c3p0连接池交由spring容器管理 -->
    <bean id="c3p0" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!-- 将druid连接池交由spring容器管理 -->
    <bean id="druid" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
```

测试从容器当中获取数据源

```java
ApplicationContext applicationContext = new 
           ClassPathXmlApplicationContext("applicationContext.xml");
               DataSource dataSource = (DataSource) 
applicationContext.getBean("c3p0");
Connection connection = dataSource.getConnection();
System.out.println(connection);

```

### 2) 抽取jdbc配置文件

applicationContext.xml加载jdbc.properties配置文件获得连接信息。

首先，需要引入context命名空间和约束路径：

这样才能使用context:property-placeholder标签加载properties配置文件

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
	<!-- 使用spring进行propertis文件的读取 -->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <!-- classpath加载类路径下指定文件 在spring中通过${key}直接获取 -->
    
	<!-- spring管理c3p0连接池数据源 -->
	<bean id="dataSource1" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    	<property name="driverClass" value="${jdbc.driver}"/>
    	<property name="jdbcUrl" value="${jdbc.url}"/>
    	<property name="user" value="${jdbc.username}"/>
    	<property name="password" value="${jdbc.password}"/>
	</bean>
	
	<!-- spring管理druid连接池数据源 -->
    <bean id="dateSource2" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>

```

### 3) 知识要点

Spring容器加载properties文件

```xml
<context:property-placeholder location="classpath:xx.properties"/>
<property name="" value="${key}"/>

```

注意:key尽量不要使用常用关键字所以一般在配置文件中进行配置时会使用

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root

```













# 六、SpringIOC/DI注解开发

## 6.1 Spring原始注解

Spring是轻代码而重配置的框架，配置比较繁重，影响开发效率，所以注解开发是一种趋势，注解代替xml配置文件可以简化配置，提高开发效率。

Spring原始注解主要是替代<Bean>的配置



### 6.1.1 基本配置注解

**替代spring核心配置文件bean的书写（IOC操作）**

| 注解        | 说明                              |
| ----------- | --------------------------------- |
| @Component  | 使用在类上用于实例化Bean          |
| @Controller | 使用在web层类上用于实例化Bean     |
| @Service    | 使用在service层类上用于实例化Bean |
| @Repository | 使用在dao层类上用于实例化Bean     |

这四个注解功能相同,只不过在之后为了区分spring管理对象的功能,额外定义了功能相同但是名称不同的注解用于配置对象,就相当于在spring核心配置文件中书写bean标签

**spring.xml**

```xml
    <!-- soring ioc/di 注解开发 -->
    <!-- 需要额外引入context头文件以及约束文件 -->

    <!--  spring由于配置较为繁琐 所以基本只会在核心配置文件中配置核心的一些配置 -->
    <!-- 在实际开发过程中一般都会使用注解开发 来简化配置 -->

    <!-- spring注解开发很简单 只需要在引入头文件和约束后 书写标签开启注解扫描即可 -->
    <!-- 将需要使用注解开发的包进行配置 spring容器在创建时会自动扫描指定的包以及包下的书写注解的类
     并进行配置
     -->
    <context:component-scan base-package="com.yunhe.bean"/>

```

**Student.java**

```java
//@Component
//Controller
//Repository
//四个注解功能相同 都是相当于配置了bean标签
/* spring扫描后会直接创建指定对象 name就是类名首字母小写 */
//@Service("abc")
//如果在标签后书写了字符串 那么字符串就是对应的name
public class Student {
    private String name;
    private String age;
    private Teacher teacher;
    ...
}    

```

#### @Component注解

用于通过无参构造方法创建实例化对象

```java
@Component("u1")
//使用spring将当前类进行实例化管理 默认使用类名进行创建 使用首字母小写的类名
//如果想指定实例的id使用()进行设置
//这是一个通用bean配置注解 为了区分开发过程中实例的不同的baen spring提供了额外相同功能不同名字的注解
public class User {}

```

#### @Repository 注解

与Component注解功能一致，就是名称不同用于区分实例化bean的功能

```java
@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public List<User> selectAll() {
        System.out.println("UserDaoImpl执行");
        return null;
    }
}

```

#### @Service 注解

与Component注解功能一致，就是名称不同用于区分实例化bean的功能

```java
@Service
public class UserServiceImpl implements UserService {
    UserDao userDao=new UserDaoImpl();
    @Override
    public List<User> findAll() {
        System.out.println("UserServiceImpl执行");
        userDao.selectAll();
        return null;
    }
}

```

#### @Controller 注解

与Component注解功能一致，就是名称不同用于区分实例化bean的功能

```java
@Controller
public class UserController {
    UserService userService=new UserServiceImpl();
    
    public void all(){
        userService.findAll();
    }
}

```

### 6.1.2 生命周期注解

**替代spring核心配置生命周期的配置**

| 注解           | 说明                                     |
| -------------- | ---------------------------------------- |
| @Scope         | 标注Bean的作用范围                       |
| @PostConstruct | 使用在方法上标注该方法是Bean的初始化方法 |
| @PreDestroy    | 使用在方法上标注该方法是Bean的销毁方法   |

>注意：
>使用注解进行开发时，需要在applicationContext.xml中配置组件扫描，作用是指定哪个包及其子包下的Bean需要进行扫描以便识别使用注解配置的类、字段和方法。


```xml
<!--注解的组件扫描-->
<!-- 开启spring注解扫描 扫描指定包下的类以及其子包下的所有类 -->
<context:component-scan base-package="com.yunhe"/>

```



#### @Scope 注解

用于标识当前由spring管理的类的作用域（单例/多例）

```java
@Scope("prototype")//多例
//@Scope("singleton")//单例
//用于标识作用域范围 默认为单例 不书写也是默认单例
public class User {}

```

#### @PostConstruct与PreDestroy  注解

用于在标识由spring管理的类中的初始化与销毁方法

```java
    @PostConstruct
    public void init(){
        System.out.println("初始化方法....");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("销毁方法.....");
    }

```



### 6.1.3 注入注解

**替代spring核心配置文件注入的书写(DI操作)**

| 注解       | 说明                                           |
| ---------- | ---------------------------------------------- |
| @Autowired | 使用在字段上用于根据类型依赖注入               |
| @Qualifier | 结合@Autowired一起使用用于根据名称进行依赖注入 |
| @Resource  | 相当于@Autowired+@Qualifier，按照名称进行注入  |
| @Value     | 注入普通属性                                   |


​	

#### @Value 注解

用于对由spring创建的对象基本属性值进行属性赋值注入

```java
public class User {
    @Value("张三")
    //使用set注入的方式将指定数据注入到指定属性
    private String name;
    @Value("18")
    private int age;
    @Value("男")
    private String sex;
//构造 getter setter toString方法    
}

```

#### @Autowired与@Qualifier 注解

结合使用根据类型与名称进行属性的注入

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userDaoImpl")
   //先通过类型匹配进行注入 如果没有相应的spring管理对象的类型 通过名称进行赋值         
    UserDao userDao;
    @Override
    public List<User> findAll() {
        System.out.println("UserServiceImpl执行");
        userDao.selectAll();
        return null;
    }
}

```

#### @Resource 注解

按照名称与类型的形式进行属性注入

```java
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDaoImpl;
    //先通过变量的名字进行注入 如果没有与变量名相同的bean 则通过类型进行赋值
    //建议在进行声明时使用spring管理对象id当做标识符进行命名，这样可以快速的通过id进行查找
    @Override
    public List<User> findAll() {
        System.out.println("UserServiceImpl执行");
        userDaoImpl.selectAll();
        return null;
    }
}

```





## 6.2 spring注解配置类注解

原始注解的使用简化的spring核心配置文件中对于自定义对象的管理，只需要添加context命名空间与约束路径，开启注解扫描，但是即使这样也不能完全替代核心配置文件，还需要使用注解替代的配置如下：

>非自定义的Bean的配置：bean
>
>加载properties文件的配置：context:property-placeholder
>
>组件扫描的配置：context:component-scan
>
>引入其他文件：import	

**替代如上配置的注解**

| 注解            | 说明                                                         |
| :-------------- | ------------------------------------------------------------ |
| @Configuration  | 用于指定当前类是一个 Spring 配置类，当创建容器时会从该类上加载注解 |
| @ComponentScan  | 用于指定 Spring 在初始化容器时要扫描的包。 作用和在 Spring 的 xml 配置文件中的 <context:component-scan base-package="com.yh"/>一样 |
| @Bean           | 使用在方法上，标注将该方法的返回值存储到 Spring 容器中       |
| @PropertySource | 用于加载.properties 文件中的配置                             |
| @Import         | 用于导入其他配置类                                           |

​	

#### @Configuration注解

用于标识新建的class类为spring配置类

```java
@Configuration
//同于标注当前类为spring核心配置类
//spring核心配置类
public class SpringConfiguration {
    
}
//相当于创建spring.xml并引入头文件

```



@ComponentScan注解

用于配置当前spring容器创建时扫描并管理的类所在的包

```java
@Configuration
//同于标注当前类为spring核心配置类
//spring核心配置类
@ComponentScan("com.yunhe")
//用于标识扫描进行管理的类所在的包
public class SpringConfiguration {

}
//相当于spring核心配置文件中 配置包的扫描 <context:component-scan base-package=“com.yh”/>

```



#### @PropertySource 与value注解

PropertySource 负责导入propertie配置文件  value负责进行属性的注入

```java
    @PropertySource("classpath:jdbc.properties")
    //用于加载properties配置文件相当于  <context:property-placeholder location="classpath:jdbc.properties"/>
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
}

```



#### @Import注解

如果使用配置类进行配置，那么配置的类也可以使用类的形式进行书写进行导入

```java
@Configuration
//同于标注当前类为spring核心配置类
//spring核心配置类
@ComponentScan("com.yunhe")
//用于标识扫描进行管理的类所在的包
@Import({DataSourceConfiguration.class})
//用于导入其他类的管理多个类使用,分隔
public class SpringConfiguration {


}

```



#### @bean注解

用于配置由spring管理的对象

```java
    @Bean(name="dataSource")
    //创建指定id的bean
    public DataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
//相当于 <bean id="dateSource" class="com.alibaba.druid.pool.DruidDataSource">通过工厂方法进行获取

```



**需要使用专门读取注解配置类的方式进行读取初始化spring容器**

```java
public class Test {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Object u1 = applicationContext.getBean("u1");
        System.out.println(u1);
    }
}

```





﻿# 七、Spring 的 AOP 简介

## 7.1 什么是 AOP 

AOP 为 Aspect Oriented Programming 的缩写，意思为面向切面编程，是通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。

AOP 是 OOP 的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

可以理解为在对同一个系统应用不同用户的使用过程中，对调用的相同方法进行切面拦截，添加额外的功能在不修改原有代码的前提下进行功能的增强（使用代理模式将方法进行代理增强）

## 7.2 AOP 的作用及其优势

作用：在程序运行期间，在不修改源码的情况下对方法进行功能增强（权限、事务、日志）

优势：减少重复代码，提高开发效率，并且便于维护

## 7.3 AOP 相关概念

Spring 的 AOP 实现底层就是对上面的动态代理的代码进行了封装，封装后我们只需要对需要关注的部分进行代码编写，并通过配置的方式完成指定目标的方法增强。

在正式讲解 AOP 的操作之前，我们必须理解 AOP 的相关术语，常用的术语如下：

- Target（目标对象）：代理的目标对象

- Proxy （代理）：一个类被 AOP 织入增强后，就产生一个结果代理类

- Joinpoint（连接点）：所谓连接点是指那些被拦截到的点。在spring中,这些点指的是方法，因为spring只支持方法类型的连接点

- Pointcut（切入点）：所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义

- Advice（通知/ 增强）：所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知

- Aspect（切面）：是切入点和通知（引介）的结合

- Weaving（织入）：是指把增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入


## 7.4 AOP 开发明确的事项

### 1) 需要编写的内容

- 编写核心业务代码（目标类的目标方法）

- 编写切面类，切面类中有通知(增强功能方法)

- 在配置文件中，配置织入关系，即将哪些通知与哪些连接点进行结合

### 2) AOP 技术实现的内容

Spring 框架监控切入点方法的执行。一旦监控到切入点方法被运行，使用代理机制，动态创建目标对象的代理对象，根据通知类别，在代理对象的对应位置，将通知对应的功能织入，完成完整的代码逻辑运行。

### 3) AOP 底层使用哪种代理方式

在 spring 中，框架会根据目标类是否实现了接口来决定采用哪种动态代理的方式。

 

## 7.5 AOP 的底层实现

实际上，AOP 的底层是通过 Spring 提供的的动态代理技术实现的。在运行期间，Spring通过动态代理技术动态的生成代理对象，代理对象方法执行时进行增强功能的介入，在去调用目标对象的方法，从而完成功能的增强。

首先书写相应的增强方法（执行指定方法时额外执行的方法），通过配置或注解形式将增强方法与切入点进行连接，这样当指定的方法被执行时，默认会调用jdk动态代理创建代理对象并将增强方法在指定位置执行，如果被代理的类没有实现任意接口那么会使用cglib动态代理

 

### 7.5.1  AOP 的动态代理技术

常用的动态代理技术

JDK 代理 : 基于接口的动态代理技术

cglib 代理：基于父类的动态代理技术



### 7.5.2 JDK 的动态代理

①目标类接口

 ```java
public interface TargetInterface {
    public void method();
}
 ```

②目标类

```java
public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
```

③动态代理代码

```java
Target target = new Target(); //创建目标对象
//创建代理对象
TargetInterface proxy = (TargetInterface) Proxy.newProxyInstance(target.getClass()
.getClassLoader(),target.getClass().getInterfaces(),new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) 
            throws Throwable {
                System.out.println("前置增强代码...");
                Object invoke = method.invoke(target, args);
                System.out.println("后置增强代码...");
                return invoke;
            }
        }
);
```

④  调用代理对象的方法测试

```java
// 测试,当调用接口的任何方法时，代理对象的代码无需修改
proxy.method();
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200921201812534.png)



### 7.5.3 cglib 的动态代理

需要引入cglib坐标

```xml
     <!-- https://mvnrepository.com/artifact/cglib/cglib -->
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.2.2</version>
        </dependency>
```

①目标类

  ```java
public class Target {
    public void method() {
        System.out.println("Target running....");
    }
}
  ```

②动态代理代码

```java
Target target = new Target(); //创建目标对象
Enhancer enhancer = new Enhancer();   //创建增强器
enhancer.setSuperclass(Target.class); //设置父类
enhancer.setCallback(new MethodInterceptor() { //设置回调
    @Override
    public Object intercept(Object o, Method method, Object[] objects, 
    MethodProxy methodProxy) throws Throwable {
        System.out.println("前置代码增强2....");
        Object invoke = method.invoke(target, objects);
        System.out.println("后置代码增强2....");
        return invoke;
    }
});
Target proxy = (Target) enhancer.create(); //创建代理对象

```

③调用代理对象的方法测试

```java
//测试,当调用接口的任何方法时，代理对象的代码都无序修改
proxy.method();
```

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200921201924416.png)





# 八、SpringAOP配置开发

## 8.1 快速入门

①导入 AOP 相关坐标

②创建目标接口和目标类（内部有切点）

③创建切面类（内部有增强方法）

④将目标类和切面类的对象创建权交给 spring

⑤在 applicationContext.xml 中配置织入关系

⑥测试代码

-----------------------------------------------------------------------------

### 1) 导入 AOP 相关坐标

```xml
<!--导入spring的context坐标，context依赖aop-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.0.5.RELEASE</version>
</dependency>
<!-- aspectj的织入 -->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.13</version>
</dependency>
```

### 2) 创建目标接口和目标类（内部有切点）

```java
public interface TargetInterface {
    public void method();
}

public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
```

### 3) 创建切面类（内部有增强方法）

```java
public class MyAspect {
    //前置增强方法
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

### 4) 将目标类和切面类的对象创建权交给 spring

```xml
<!--配置目标类-->
<bean id="target" class="com.yh.aop.Target"></bean>
<!--配置切面类-->
<bean id="myAspect" class="com.yh.aop.MyAspect"></bean>
```

### 5)在 applicationContext.xml 中配置织入关系

**①导入aop命名空间**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

```

**②在 applicationContext.xml 中配置织入关系**

配置切点表达式和前置增强的织入关系

```xml
<aop:config>
    <!--引用myAspect的Bean为切面对象-->
    <aop:aspect ref="myAspect">
        <!--配置Target的method方法执行时要进行myAspect的before方法前置增强-->
        <aop:before method="before" pointcut="execution(public void com.yh.aop.Target.method())"></aop:before>
    </aop:aspect>
</aop:config>

```

### 6) 测试代码

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
TargetInterface target= (TargetInterface ) applicationContext.getBean("target");   				 
target.method();

```

 

## 8.2 XML 配置 AOP 详解

### 1) 切点表达式的写法

表达式语法：

```java
execution([修饰符] 返回值类型 包名.类名.方法名(参数))

```

- 访问修饰符可以省略

- 返回值类型、包名、类名、方法名可以使用星号*  代表任意

- 包名与类名之间一个点 . 代表当前包下的类，两个点 .. 表示当前包及其子包下的类

- 参数列表可以使用两个点 .. 表示任意个数，任意类型的参数列表

例如：

```xml
<!-- 修饰符为public 返回值为void 在aop包下的Target类中的无参method方法 -->
execution(public void com.yh.aop.Target.method())	

<!-- 修饰符任意 返回值为void aop包下Target类的所有方法 -->
execution(void com.yh.aop.Target.*(..))

<!-- 修饰符任意 返回值任意 aop包下所有类 不包含子包 的所有方法 -->
execution(* com.yh.aop.*.*(..))

<!-- 修饰符任意 返回值任意 aop包下所有类 包含子包 的所有方法 -->
execution(* com.yh.aop..*.*(..))

<!-- 修饰符任意 返回值任意 aop包下所有类 包含子包 名包含s的所有方法 -->
execution(* com.yh.aop..*.*s*(..))

<!-- 所有交由spring管理的类的所有方法 -->
execution(* *..*.*(..))

```

### 2) 通知的类型

通知的配置语法：

```xml
<aop:通知类型 method=“切面类中方法名” pointcut=“切点表达式"></aop:通知类型>

```

**1) 前置通知：before 在连接点方法执行之前执行。**

```xml
<aop:before method="before" pointcut="timePointcut"></aop:before>

```

```java
    //前置通知
    public void before() {
        System.out.println("前置通知");
    }

```

**2) 后置通知：after  在连接点方法执行之后，无论如何都会执行。（finally）**
        

```xml
<aop:after method="after" pointcut="timePointcut"></aop:after>

```

```java
 //后置（最终）通知
    public void b() {
        System.out.println("后置通知");
    }

```

3) 环绕通知：around 在连接点方法执行之前和之后执行。（拦截器）

```xml
<aop:around method="around" pointcut="timePointcut"></aop:around>

```

```java
    //环绕通知
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知");
        Object[] args = joinPoint.getArgs();//获取请求参数
        Object proceed = joinPoint.proceed(args);//调用源方法
        System.out.println("环绕通知");
        // ProceedingJoinPoint 环绕通知中使用的获取原方法的参数
        //getArgs()获取原方法请求执行时传入的参数
        //proceed(args)调用执行原方法传入参数
        return proceed;
    }

```

**4) 异常通知：after-throwing  在连接点方法发生异常之后执行。**
       throwing:关联到方法异常参数

```xml
<aop:after-throwing method="afterThrowing" throwing="e" pointcut="timePointcut"></aop:after-throwing>

```

```java
    //异常通知
    public void afterThrowing(Exception e) {
        System.out.println(e);
    }

```

**5) 返回通知：after-returning  在连接点方法返回结果后执行（如果发生异常则不会执行）。**
      returning：指定返回结果参数

```xml
<aop:after-returning method="afterReturning" pointcut="timePointcut" returning="result"></aop:after-returning>

```

```java
    //返回通知
    public void returning(JoinPoint joinPoint, Object result) throws Throwable {
        System.out.println(Arrays.toString(joinPoint.getArgs()));
        System.out.println(result);
    }

```



### 3) 切点表达式的抽取

当多个增强的切点表达式相同时，可以将切点表达式进行抽取，在增强中使用 pointcut-ref 属性代替 pointcut 属性来引用抽取后的切点表达式。

```xml
<aop:config>
    <!--引用myAspect的Bean为切面对象-->
    <aop:aspect ref="myAspect">
        <!-- 抽象切点表达式 设置id  如果在当前配置中抽取 那么只能在当前使用 如果在aop配置中进行书写 所有的aop配置都可以使用-->
        <aop:pointcut id="myPointcut" expression="execution(* com.yh.aop.*.*(..))"/>
        <aop:before method="before" pointcut-ref="myPointcut"></aop:before>
    </aop:aspect>
</aop:config>

```

## 8.3 知识要点

- aop织入的配置

```xml
<aop:config>
    <aop:aspect ref=“切面类”>
        <aop:before method=“通知方法名称” pointcut=“切点表达式"></aop:before>
    </aop:aspect>
</aop:config>

```

- 通知的类型：前置通知、后置通知、环绕通知、异常抛出通知、最终通知
- 切点表达式的写法：

```xml
execution([修饰符] 返回值类型 包名.类名.方法名(参数))
```

# 九、SpringAOP注解开发

## 9.1 快速入门

基于注解的aop开发步骤：

①创建目标接口和目标类（内部有切点）

②创建切面类（内部有增强方法）

③将目标类和切面类的对象创建权交给 spring

④在切面类中使用注解配置织入关系

⑤在配置文件中开启组件扫描和 AOP 的自动代理

⑥测试

--------------------------------------------------

①创建目标接口和目标类（内部有切点）

```java
public interface TargetInterface {
    public void method();
}

public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}

```

②创建切面类（内部有增强方法)

```java
public class MyAspect {
    //前置增强方法
    public void before(){
        System.out.println("前置代码增强.....");
    }
}

```

③将目标类和切面类的对象创建权交给 spring

```java
@Component("target")
public class Target implements TargetInterface {
    @Override
    public void method() {
        System.out.println("Target running....");
    }
}
@Component("myAspect")
public class MyAspect {
    public void before(){
        System.out.println("前置代码增强.....");
    }
}

```

④在切面类中使用注解配置织入关系

```java
@Component("myAspect")
@Aspect
public class MyAspect {
    @Before("execution(* com.yh.aop.*.*(..))")
    public void before(){
        System.out.println("前置代码增强.....");
    }
}

```

⑤在配置文件中开启组件扫描和 AOP 的自动代理

```xml
	<!--组件扫描-->
	<context:component-scan base-package="com.yh.aop"/>

	<!--aop的自动代理-->
    <!-- 默认使用jdk动态代理如果代理类没有接口则使用cglib代理 -->
    <!-- <aop:aspectj-autoproxy/>-->
    <!-- 如果想强制直接使用cglib代理可以通过配置 -->
    <aop:aspectj-autoproxy proxy-target-class="true"/>


```

⑥测试代码

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
TargetInterface target= (TargetInterface ) applicationContext.getBean("target");   				 
target.method();

```

## 9.2 注解配置 AOP 详解

### 1) 注解配置切面类

**@Aspect** 

用于标识当前类为切面类等价于spring核心配置文件中书写在aop：config标签中的aop:aspect标签
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210409105852695.png)
**注意：需要与@Component标签一起使用标识切面类**

### 2) 注解通知的类型

通知的配置语法：@通知类型(value="execution(切点表达式)")

#### @Before注解

前置通知通过切点表达式对指定方法进行前置增强，在方法执行前增强

```java
//前置通知
@Before("execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))")
public void before() {
    System.out.println("原方法执行前增强的方法");
}

```

等价于xml配置

```xml
<aop:before method="before"  pointcut="execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))"></aop:before>

```



#### @After注解

后置通知也叫最终通知，通过切点表达式对指定方法进行后置增强，在方法执行后增强（无论方法是否执行成功）

```java
//后置通知
@After("execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))")
public void after() {
    System.out.println("原方法执行后增强的方法");
}

```

等价于xml配置

```xml
<aop:after method="after"  pointcut="execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))"></aop:after>

```



#### @Around注解

环绕通知，通过切点表达式对指定方法进行环绕增强，在方法执行前后进行增强（需要在增强方法中进行原方法的调用否则可能导致方法不能正常执行）

```java
//环绕通知
@Around("execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("环绕通知");
    Object[] args = joinPoint.getArgs();//获取请求参数
    Object proceed = joinPoint.proceed(args);//调用源方法
    System.out.println("环绕通知");
    // ProceedingJoinPoint 环绕通知中使用的获取原方法的参数
    //getArgs()获取原方法请求执行时传入的参数
    //proceed(args)调用执行原方法传入参数
    return proceed;
}

```

等价于xml配置

```xml
<aop:around method="around"  pointcut="execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))"></aop:around>

```



#### @AfterThrowing注解

异常通知，通过切点表达式对指定方法进行异常增强，通常用于进行异常的提示

```java
    //异常通知
    @AfterThrowing(value = "execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))", throwing = "e")
    public void exception(Exception e) {
        System.out.println(e);
    }

```

等价于xml配置

```xml
<aop:after-throwing method="exception" throwing="e" pointcut="execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))"></aop:after-throwing>

```



#### @AfterReturning注解

返回通知，通过切点表达式对指定方法进行增强,在方法执行结束返回结果后执行，但是如果方法执行发生异常则不会执行

```java
    //返回通知
    @AfterReturning(value = "execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))",returning = "o")
    public void returning(JoinPoint joinPoint, Object o) throws Throwable {
        System.out.println(Arrays.toString(joinPoint.getArgs()));
        System.out.println(o);
    }

```

等价于xml配置

```xml
<aop:after-returning method="returning"  returning="o" pointcut="execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))"></aop:after-returning>

```







### 3) 切点表达式的抽取

**@Pointcut注解**

当多个切点使用相同切点表达式时可以创建方法使用Pointcut注解书写切点表达式，其他通知方法通过类名.方法名获取切点

```java
@Pointcut("execution(* com.yunhe.service.impl.BankServiceImpl.*Money(..))")
//书写方法使用注解定义通用切点表达式
public void pointcut(){}

//前置通知
@Before("MoneyAspect.pointcut()")
//使用类名.配置切点表达式方法名进行获取
public void before() {
    System.out.println("原方法执行前增强的方法");
}

```



在实际开发过程中通常会创建一个单独保存切点表达式的切面类，其他切面类通过类名.方法名使用对应切点，当然也可以为不同功能aop创建不用的切面类将对应功能的切点表达式书写在对应功能的切面类中



## 9.3 知识要点

- 注解aop开发步骤

①使用@Aspect标注切面类

②使用@通知注解标注通知方法

③在配置文件中配置aop自动代理 \<aop:aspectj-autoproxy/>

 



﻿# 十、JdbcTemplate基本使用

## 10.1 概述

JdbcTemplate是spring框架中提供的一个对象，是对原始繁琐的Jdbc API对象的简单封装。spring框架为我们提供了很多的操作模板类。例如：操作关系型数据的JdbcTemplate和HibernateTemplate，操作nosql数据库的RedisTemplate，操作消息队列的JmsTemplate等等。

## 10.2 JdbcTemplate基本使用

###  1) 导入spring-jdbc和spring-tx等坐标

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.0.5.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.5.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.5.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.13</version>
        </dependency>
```

###  2) 创建数据库表和实体

```java
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

###  3) 创建JdbcTemplate对象

```java
  		//创建数据源对象
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUser("root");
        dataSource.setPassword("root");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        //设置数据源对象  知道数据库在哪
        jdbcTemplate.setDataSource(dataSource);
```

###  4) 执行数据库操作

```java
 		//执行操作
        int row = jdbcTemplate.update("insert into account values(?,?)", "zhangsan", 5000);
        System.out.println(row);
```

## 10.3 使用spring对JdbcTemplate管理

我们可以将JdbcTemplate的创建权交给Spring，将数据源DataSource的创建权也交给Spring，在Spring容器内部将数据源DataSource注入到JdbcTemplate模版对象中,然后通过Spring容器获得JdbcTemplate对象来执行操作。

### **1) 配置文件**

```xml
<!--数据源对象-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/test"></property>
        <property name="user" value="root"></property>
        <property name="password" value="root"></property>
    </bean>

    <!--jdbc模板对象-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
```

### **2) 测试代码**

```java
    	//测试Spring产生jdbcTemplate对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        JdbcTemplate jdbcTemplate = app.getBean(JdbcTemplate.class);
        int row = jdbcTemplate.update("insert into account values(?,?)", "lisi", 5000);
        System.out.println(row);
```

###  3) 使用proerties实现配置抽取

**jdbc.properties**

```xml
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root
```

**配置文件**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
">
    <!--加载jdbc.properties-->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <!--数据源对象-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
    <!--jdbc模板对象-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
</beans>
```

## 10.4 JdbcTemplate常用方法

```java
public class JdbcTemplateCRUDTest {
	AbstractApplicationContext ac=new ClassPathXmlApplicationContext("spring.xml");;
    JdbcTemplate jdbcTemplate=(JdbcTemplate)ac.getBean("jdbcTemplate");
	//新增
 	@Test
    public void testDelete(){
        jdbcTemplate.update("insert into account values (?,?)","zhagnsan","5000");
    }
	//删除
    @Test
    public void testDelete(){
        jdbcTemplate.update("delete from account where name=?","zhangsan");
    }
	//修改更新
    @Test
    public void testUpdate(){
        jdbcTemplate.update("update account set money=? where name=?",10000,"zhangsan");
    }
    //聚合查询
    @Test
    public void testQueryCount(){
        Long count = jdbcTemplate.queryForObject("select count(*) from account", Long.class);
        System.out.println(count);
    }
	//查询一个
    @Test
    public void testQueryOne(){
        Account account = jdbcTemplate.queryForObject("select * from account where name=?", new BeanPropertyRowMapper<Account>(Account.class), "zhangsan");
        System.out.println(account);
    }
	//查询所有
    @Test
    public void testQueryAll(){
        List<Account> accountList = jdbcTemplate.query("select * from account", new BeanPropertyRowMapper<Account>(Account.class));
        System.out.println(accountList);
    }
}
```

# 十一、Spring声明式事务控制

## 11.1 编程式事务控制相关对象

### 1) PlatformTransactionManager 

PlatformTransactionManager 接口是 spring 的事务管理器，它里面提供了我们常用的操作事务的方法。

![](https://img-blog.csdnimg.cn/2020092319063752.png) 
**注意：**
PlatformTransactionManager 是接口类型，不同的 Dao 层技术则有不同的实现类，例如：
Dao 层技术是jdbc 或 mybatis 时：org.springframework.jdbc.datasource.DataSourceTransactionManager 
Dao 层技术是hibernate时：org.springframework.orm.hibernate5.HibernateTransactionManager

### 2) TransactionDefinition

TransactionDefinition 是事务的定义信息对象 
事务的定义包括: 事务的隔离级别，事务的传播属性，超时时间设置，是否只读
事务的隔离级别是数据库本身的事务功能，事务的传播属性则是spring为我们提供的功能
该接口的实现DefaultTransactionDefinition,默认的事务定义

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200923190707697.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

#### 1. 事务隔离级别

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200923190746293.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

设置隔离级别，可以解决事务并发产生的问题，如脏读、不可重复读和虚读。

- ISOLATION_DEFAULT
- ISOLATION_READ_UNCOMMITTED
- ISOLATION_READ_COMMITTED
- ISOLATION_REPEATABLE_READ
- ISOLATION_SERIALIZABLE

#### 2. 事务传播行为

- REQUIRED：如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。一般的选择（默认值）
- SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行（没有事务）
- MANDATORY：使用当前的事务，如果当前没有事务，就抛出异常
- REQUERS_NEW：新建事务，如果当前在事务中，把当前事务挂起。
- NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
- NEVER：以非事务方式运行，如果当前存在事务，抛出异常
- NESTED：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行 REQUIRED 类似的操作
- 超时时间：默认值是-1，没有超时限制。如果有，以秒为单位进行设置
- 是否只读：建议查询时设置为只读

### 3) TransactionStatus

TransactionStatus 接口提供的是事务具体的运行状态
TransactionStatus它继承了SavepointManager接口，SavepointManager是对事务中上述保存点功能的封装

```java
public interface SavepointManager {
    Object createSavepoint() throws TransactionException;
    void rollbackToSavepoint(Object savepoint) throws TransactionException;
    void releaseSavepoint(Object savepoint) throws TransactionException;
}
```

TransactionStatus本身更多存储的是事务的一些状态信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200923190906918.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)

## 11.2 基于 XML 的声明式事务控制

### 1) 什么是声明式事务控制

Spring 的声明式事务顾名思义就是采用声明的方式来处理事务。这里所说的声明，就是指在配置文件中声明，用在 Spring 配置文件中声明式的处理事务来代替代码式的处理事务。

**声明式事务处理的作用**

- 事务管理不侵入开发的组件。具体来说，业务逻辑对象就不会意识到正在事务管理之中，事实上也应该如此，因为事务管理是属于系统层面的服务，而不是业务逻辑的一部分，如果想要改变事务管理策划的话，也只需要在定义文件中重新配置即可

- 在不需要事务管理的时候，只要在设定文件上修改一下，即可移去事务管理服务，无需改变代码重新编译，这样维护起来极其方便

**注意：Spring 声明式事务控制底层就是AOP。**

### 2) 声明式事务控制的实现

声明式事务控制明确事项：

- 谁是切点？
- 谁是通知？
- 配置切面？

**使用步骤**

#### 1.引入tx命名空间

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

####  2.配置事务增强

```xml
<!--平台事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"></property>
</bean>

<!--事务增强配置-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="*"/>
    </tx:attributes>
</tx:advice>
```

#### 3.配置事务 AOP 织入

```xml
<!--事务的aop增强-->
<aop:config>
    <aop:pointcut id="myPointcut" expression="execution(* com.yh.service.impl.*.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="myPointcut"></aop:advisor>
</aop:config>
```

#### 4.测试事务控制转账业务代码

```java
@Override
public void transfer(String outMan, String inMan, double money) {
    accountDao.out(outMan,money);
    int i = 1/0;
    accountDao.in(inMan,money);
}
```

### 3) 切点方法的事务参数的配置

```xml
<!--事务增强配置-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="*"/>
    </tx:attributes>
</tx:advice>
```

其中，<tx:method> 代表切点方法的事务参数的配置，例如：

```xml
<tx:method name="transfer" isolation="REPEATABLE_READ" propagation="REQUIRED" timeout="-1" read-only="false"/>
```

- name：切点方法名称
- isolation:事务的隔离级别
- propogation：事务的传播行为
- timeout：超时时间
- read-only：是否只读



## 11.3 基于注解的声明式事务控制

### 1) 使用注解配置声明式事务控制

####  1.编写 spring 配置文件

```xml
	<!-- 加载properties -->
	<context:property-placeholder location="classpath:jdbc.properties"/>
   <!-- aop注解 -->
    <aop:aspectj-autoproxy/>
    <!--数据源对象-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"></property>
        <property name="jdbcUrl" value="${jdbc.url}"></property>
        <property name="user" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <!--jdbc模板对象-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--平台事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--组件扫描-->
    <context:component-scan base-package="com.yh"/>
    <!--事务的注解驱动-->
    <tx:annotation-driven/>

```

####  2.编写 AccoutDao

```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void out(String outMan, double money) {
        jdbcTemplate.update("update account set money=money-? where name=?",money,outMan);
    }
    public void in(String inMan, double money) {
        jdbcTemplate.update("update account set money=money+? where name=?",money,inMan);
    }
}

```

####  3.编写 AccoutService与实现类

```java
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void transfer(String outMan, String inMan, double money) {
        accountDao.out(outMan,money);
        int i = 1/0;
        accountDao.in(inMan,money);
    }
}

```

### 2) 注解配置声明式事务控制解析

①使用 @Transactional 在需要进行事务控制的类或是方法上修饰，注解可用的属性同 xml 配置方式，例如隔离级别、传播行为等。

②注解使用在类上，那么该类下的所有方法都使用同一套注解参数配置。

③使用在方法上，不同的方法可以采用不同的事务参数配置。

④Xml配置文件中要开启事务的注解驱动<tx:annotation-driven />





# 十二、MyBatis与Spring的集成



﻿## 12.1 MyBatis与Spring的集成

在学习mybatis配置时，对于mybatis-config配置的时候我们发现，大致是需要配置三个方面：setting、datasource、mappers

而mybatis的setting往往使用默认配置，所以我们经常配置datasource数据源与mappers映射，但学习spring之后发现，对于datasource的配置交由spring进行管理，所以在spring与mybatis整合后mybatis的配置文件中将不需要配置datasource，mybatis的配置几乎都会在Spring配置之中完成。当然要想要实现spring与mybatis的整合，其中最重要的就是 mybatis-spring.jar 包

 - mybatis-spring会用于帮助你将 MyBatis 代码无缝地整合到 Spring 中。
 - Spring 将会加载必要的 MyBatis 工厂类和 Session 类
 - 提供一个简单的方式来注入 MyBatis 数据映射器和 SqlSession 到业务层的 bean 中。
 - 方便集成 Spring 事务
 - 翻译 MyBatis 的异常到 Spring 的 DataAccessException 异常(数据访问异常)中。
 - 

Mybatis-Spring 兼容性，我们在选择Spring、MyBatis以及mybatis-spring时，应注意版本之间的兼容性
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200925162004618.png)


## 12.2 spring与mybatis快速整合

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

## 12.3 mybatis-spring整合jar包功能

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















