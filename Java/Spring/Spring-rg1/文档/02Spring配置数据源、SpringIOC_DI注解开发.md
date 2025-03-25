##  1.Spring配置数据源

### 1.1 数据源（连接池）的作用

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

### 1.2 数据源的手动创建
**①导入导入对应的jar包坐标**

spring的jar包

```xml
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.1.2.RELEASE</version>
    </dependency>
```

mysql的jar包

```xml
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.39</version>
    </dependency>
```

c3p0以及druid连接池jar包

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



**②创建C3P0连接池**

```java
        //创建数据源
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //设置数据库连接参数
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        //获得连接对象
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
```



**②创建Druid连接池** 

```java
    //创建数据源
    DruidDataSource dataSource = new DruidDataSource();
    //设置数据库连接参数
    dataSource.setDriverClassName("com.mysql.jdbc.Driver"); 
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");   
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    //获得连接对象
    Connection connection = dataSource.getConnection();    
    System.out.println(connection);
```

**③提取jdbc.properties配置文件**

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root
```

**④读取jdbc.properties配置文件创建连接池**

```java
    //加载类路径下的jdbc.properties
    ResourceBundle rb = ResourceBundle.getBundle("jdbc");
    ComboPooledDataSource dataSource = new ComboPooledDataSource(); 
    dataSource.setDriverClass(rb.getString("jdbc.driver"));   
    dataSource.setJdbcUrl(rb.getString("jdbc.url")); 
    dataSource.setUser(rb.getString("jdbc.username")); 
    dataSource.setPassword(rb.getString("jdbc.password"));
    Connection connection = dataSource.getConnection();   
    System.out.println(connection);
```


### 1.3 Spring配置数据源
可以将DataSource的创建权交由Spring容器去完成

DataSource有无参构造方法，而Spring默认就是通过无参构造方法实例化对象的

DataSource要想使用需要通过set方法设置数据库连接信息，而Spring可以通过set方法进行字符串注入

```xml
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/test"/>
    <property name="user" value="root"/>
    <property name="password" value="root"/>
</bean>
```
测试从容器当中获取数据源
```java
ApplicationContext applicationContext = new 
           ClassPathXmlApplicationContext("applicationContext.xml");
               DataSource dataSource = (DataSource) 
applicationContext.getBean("dataSource");
Connection connection = dataSource.getConnection();
System.out.println(connection);
```

### 1.4 抽取jdbc配置文件
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

### 1.5 知识要点
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













## 2. Spring注解开发

### 2.1 Spring原始注解
Spring是轻代码而重配置的框架，配置比较繁重，影响开发效率，所以注解开发是一种趋势，注解代替xml配置文件可以简化配置，提高开发效率。

Spring原始注解主要是替代<Bean>的配置

**注解说明**

**替代spring核心配置文件bean的书写（IOC操作）**
@Component	使用在类上用于实例化Bean
@Controller	使用在web层类上用于实例化Bean
@Service	使用在service层类上用于实例化Bean
@Repository	使用在dao层类上用于实例化Bean

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



**替代spring核心配置文件注入的书写(DI操作)**
@Autowired	使用在字段上用于根据类型依赖注入
@Qualifier	结合@Autowired一起使用用于根据名称进行依赖注入
@Resource	相当于@Autowired+@Qualifier，按照名称进行注入
@Value	注入普通属性

**替代spring核心配置生命周期的配置**
@Scope	标注Bean的作用范围
@PostConstruct	使用在方法上标注该方法是Bean的初始化方法
@PreDestroy	使用在方法上标注该方法是Bean的销毁方法

注意：
使用注解进行开发时，需要在applicationContext.xml中配置组件扫描，作用是指定哪个包及其子包下的Bean需要进行扫描以便识别使用注解配置的类、字段和方法。


```xml
<!--注解的组件扫描-->
<!-- 开启spring注解扫描 扫描指定包下的类以及其子包下的所有类 -->
<context:component-scan base-package="com.yunhe"/>
```
#### Component注解

用于通过无参构造方法创建实例化对象

```java
@Component("u1")
//使用spring将当前类进行实例化管理 默认使用类名进行创建 使用首字母小写的类名
//如果想指定实例的id使用()进行设置
//这是一个通用bean配置注解 为了区分开发过程中实例的不同的baen spring提供了额外相同功能不同名字的注解
public class User {}
```

#### Repository 注解
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

#### Service 注解

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

#### Controller 注解

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

#### Scope 注解

用于标识当前由spring管理的类的作用域（单例/多例）

```java
@Scope("prototype")//多例
//@Scope("singleton")//单例
//用于标识作用域范围 默认为单例 不书写也是默认单例
public class User {}
```

#### PostConstruct与PreDestroy  注解

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

#### Value 注解

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

#### Autowired与Qualifier 注解

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

#### Resource 注解

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





### 2.2 spring注解配置类注解
原始注解的使用简化的spring核心配置文件中对于自定义对象的管理，只需要添加context命名空间与约束路径，开启注解扫描，但是即使这样也不能完全替代核心配置文件，还需要使用注解替代的配置如下：

非自定义的Bean的配置：bean

加载properties文件的配置：context:property-placeholder

组件扫描的配置：context:component-scan

引入其他文件：import

注解	说明
@Configuration	用于指定当前类是一个 Spring 配置类，当创建容器时会从该类上加载注解
@ComponentScan	用于指定 Spring 在初始化容器时要扫描的包。 作用和在 Spring 的 xml 配置文件中的 <context:component-scan base-package="com.yh"/>一样
@Bean	使用在方法上，标注将该方法的返回值存储到 Spring 容器中
@PropertySource	用于加载.properties 文件中的配置
@Import	用于导入其他配置类

#### Configuration注解

用于标识新建的class类为spring配置类

```java
@Configuration
//同于标注当前类为spring核心配置类
//spring核心配置类
public class SpringConfiguration {
    
}
//相当于创建spring.xml并引入头文件
```



#### ComponentScan注解

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



#### PropertySource 与value注解

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



#### Import注解

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



#### bean注解

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





