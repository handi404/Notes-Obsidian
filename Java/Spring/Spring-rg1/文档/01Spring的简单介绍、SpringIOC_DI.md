## 1. Spring概述

### 1.1 Spring是什么

Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 IoC（Inverse Of Control：反转控制）和 AOP（Aspect Oriented Programming：面向切面编程）为内核。

提供了展现层 SpringMVC和持久层 Spring JDBCTemplate以及业务层事务管理等众多的企业级应用技术，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架

### 1.2 Spring发展历程 

Rod Johnson （ Spring 之父）

Spring的基础架构起源于2000年早期，它是Rod Johnson在一些成功的商业项目中构建的基础设施
2002后期，Rod Johnson发布了《Expert One-on-One J2EE Design and Development》一书
2003年2月Spring框架正式成为一个开源项目，并发布于SourceForge中

### 1.3 Spring的优势

方便解耦，简化开发

AOP 编程的支持

声明式事务的支持

方便程序的测试

### 1.4 Spring的体系结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200919151212624.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MTg2NDY1,size_16,color_FFFFFF,t_70)
## 2. Spring快速入门

### 2.1 Spring程序开发步骤 

#### 2.1.1 导入 Spring 开发的基本包坐标

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

#### 2.1.2 编写 接口和实现类

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
#### 2.1.3 创建 Spring 核心配置文件

创建resources文件并创建applicationContext.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```
④在 Spring 配置文件中配置 UserServiceImpl 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- id:实例对象的名字   class:实例对象全路径 -->
    <bean id="userService" class="com.yh.service.impl.UserServiceImpl"></bean>
    
</beans>
```
⑤使用 Spring 的 API 获得 Bean 实例

```java
public void test(){
		ApplicationContext applicationContext = new  
             ClassPathXmlApplicationContext("applicationContext.xml");
             UserService userService = (UserService) applicationContext.getBean("userService");   				 
             userService.save();
 }
```
####  2.1.4 **spring相关API**

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





## 3. Spring配置文件

###  Spring IOC/DI 概念

**IOC控制反转Inversion of Control**
把创建和查找依赖对象的控制权交给了容器，由容器进行注入组合对象，所以对象与对象之间是松散耦合，这样也方便测试，利于功能复用，更重要的是使得程序的整个体系结构变得非常灵活。

**DI依赖注入Dependency Injection**
在程序运行期间由容器动态的将某个依赖关系注入到组件之中

### 3.1 Bean标签基本配置 

用于配置对象交由Spring 来创建。

默认情况下它调用的是类中的无参构造函数，如果没有无参构造函数则不能创建成功。

基本属性：

id：Bean实例在Spring容器中的唯一标识

class：Bean的全限定名称

### 3.2 Bean标签范围配置 

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

### 3.3 Bean生命周期配置

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

### 3.4 Bean实例化三种方式

1） 使用无参构造方法实例化

​      它会根据默认无参构造方法来创建类对象，如果bean中没有默认无参构造函数，将会创建失败

```xml
<bean id="UserService" class="com.yunhe.dao.impl.UserServiceImpl"></bean>
```

2） 工厂静态方法实例化

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

3） 工厂实例方法实例化

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


### 3.5 Bean的依赖注入方式
①构造方法
​      创建有参构造
```java
public class UserServiceImpl implements UserService {
userDao userDao;
public UserServiceImpl (userDao userDao){
this.userDao=userDao;
}
 }
```
​      配置Spring容器调用有参构造时进行注入
```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.yh.service.impl.UserServiceImpl">      		   	<constructor-arg name="userDao" ref="userDao"></constructor-arg>
</bean>
```

②set方法

​      在UserServiceImpl中添加setUserDao方法

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

​      配置Spring容器调用set方法进行注入

```xml
<bean id="userDao" class="com.yh.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.yh.service.impl.UserServiceImpl">
	<property name="userDao" ref="userDao"/>
</bean>
```

③自动注入（接口注入、注解注入）
开启注解开发后书写注解自动进行注入操作

### 3.6 Bean的依赖注入的数据类型

上面的操作，都是注入的引用Bean，除了对象的引用可以注入，普通数据类型，集合等都可以在容器中进行注入。

**注入数据的三种数据类型** 

普通数据类型
引用数据类型
集合数据类型

其中引用数据类型，此处就不再赘述了，之前的操作都是对UserDao对象的引用进行注入的，下面将以set方法注入为例，演示普通数据类型和集合数据类型的注入。

**Bean的依赖注入的数据类型**

（1）普通数据类型的注入

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

（2）集合数据类型（List<String>）的注入

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

（3）集合数据类型（List<User>）的注入

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

（4）集合数据类型（ Map<String,User> ）的注入

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

（5）集合数据类型（Properties）的注入

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

