Spring 是轻代码而重配置的框架，配置比较繁重，影响开发效率，所以注解开发是一种趋势，注解代替 xml 配置文件可以简化配置，提高开发效率。
## 原始注解
---
**注解说明**
**替代 spring 核心配置文件 bean 的书写（IOC 操作）**
@Component	使用在类上用于实例化 Bean
@Controller	使用在 web 层类上用于实例化 Bean
@Service	使用在 service 层类上用于实例化 Bean
@Repository	使用在 dao 层类上用于实例化 Bean

这四个注解**功能相同**,只不过在之后为了区分 spring 管理对象的功能,额外定义了功能相同但是名称不同的注解用于配置对象,就相当于在 spring 核心配置文件中书写 bean 标签。
### IOC 注解示例
- spring 核心配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

<!--    <bean id="r" class="com.sqsf.javabean.Role" scope="prototype">-->
<!--        <property name="id" value="1"/>-->
<!--        <property name="name" value="管理员"/>-->
<!--    </bean>-->
<!--    <bean id="u" class="com.sqsf.javabean.User" autowire="byType" init-method="a" destroy-method="b">-->
<!--        <property name="id" value="1"/>-->
<!--        <property name="name" value="zhangsan"/>-->
<!--        <property name="age" value="21"/>-->
<!--    </bean>-->

    <bean id="druidFactory" class="com.sqsf.util.druidFactory"/>
    <bean id="druid" factory-bean="druidFactory" factory-method="druid"/>

    <!-- 如果想使用注解开发 那么需要配置 告诉spring从哪查找类-->
    <!-- 开启组件扫描 会扫描指定包及其子包下所有书写注解的类 -->
    <context:component-scan base-package="com.sqsf"/>
</beans>
```

```java
//用于标识当前类是spring容器的组件(当前类的对象交由spring容器管理)
//默认使用无参构造方法创建对象交由spring容器管理
//默认使用类名首字母小写作为id
@Component("r")//等价于<bean id="r" class="com.sqsf.javabean.Role"/>
public class Role {
    private int id;
    private String name;
    ...
}

//4个注解功能相同就是用于区分作用
@Controller("u")//等价于<bean id="u" class="com.sqsf.javabean.User"></bean>
public class User {
    private int id;
    private String name;
    private int age;  
	private Role r;
	...
}
```

- 测试
```Java
public class MyTest {
    @Test
    public void r() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Object r1 = applicationContext.getBean("r");
        Object r2 = applicationContext.getBean("r");
        System.out.println(r1);
        System.out.println(r1==r2);
        System.out.println(r1.hashCode()+"|"+r2.hashCode());
        applicationContext.close();
    }

    @Test
    public void u() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Object u = applicationContext.getBean("u");
        Object u2 = applicationContext.getBean("u");
        System.out.println(u);
        System.out.println(u2==u);
        System.out.println(u.hashCode()+"|"+u2.hashCode());
        applicationContext.close();
    }
    @Test
    public void druid() throws SQLException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        DruidDataSource druid = (DruidDataSource) applicationContext.getBean("druid");
        System.out.println(druid.getConnection());
        applicationContext.close();
    }
}
```


### DI注解

**注解说明**
@Value	用于对由 spring 创建的对象基本属性值进行**属性赋值**注入。
@Autowired 与 @Qualifier	结合使用根据**类型与名称**进行属性的注入。
@Resource  java 提供的可以注入的注解，先根据属性名查找再根据类型查找。

```Java
//用于标识当前类是spring容器的组件(当前类的对象交由spring容器管理)
//默认使用无参构造方法创建对象交由spring容器管理
//默认使用类名首字母小写作为id
@Component("r")//等价于<bean id="r" class="com.sqsf.javabean.Role"></bean>
public class Role {
    //对于常用属性 可以直接使用@Value进行赋值
    @Value("2")
    private int id;
    @Value("用户")
    private String name;
    ...
}

//4个注解功能相同就是用于区分作用
@Controller("u")//等价于<bean id="u" class="com.sqsf.javabean.User"/>
public class User {
    @Value("2")
    private int id;
    @Value("李四")
    private String name;
    @Value("27")
    private int age;
    //对于自定义类型而言需要现将对应的对象交由spring管理
    //自动查找spring容器中类型相同的对象存入
    @Autowired //等价与bean标签属性配置autowire="byType"
    @Qualifier("r") //如果类型匹配失败 使用指定的id进行匹配

    //java也提供了可以注入的注解
    //@Resource//先根据属性名查找 再根据类型查找
    private Role r;
    ...
}
```


### 范围注解

**注解说明**
@Scope 用于标识当前由 spring 管理的类的**作用域（单例/多例）**

```Java
//用于标识当前类是spring容器的组件(当前类的对象交由spring容器管理)
//默认使用无参构造方法创建对象交由spring容器管理
//默认使用类名首字母小写作为id
@Component("r")//等价于<bean id="r" class="com.sqsf.javabean.Role"></bean>

//配置对象范围
@Scope("prototype")//等价与bean标签属性scope="prototype"
public class Role {
	...
}
```

### 生命周期注解

**注解说明**
@PostConstruct 用于在标识由 spring 管理的类中的**初始化方法**
@PreDestroy 用于在标识由 spring 管理的类中的**销毁方法**

```java
//4个注解功能相同就是用于区分作用
@Controller("u")//等价于<bean id="u" class="com.sqsf.javabean.User"/>
public class User {
	...
	//生命周期注解 对于单例对象 spring容器创建时创建 关闭时自动调用销毁方法
    @PostConstruct//等价于bean标签属性init-method="a"
    public void a(){
        System.out.println("初始化");
    }
    @PreDestroy//等价于bean标签属性destroy-method="b"
    public void b(){
        System.out.println("销毁");
    }
}
```


## 配置类注解
---
如果想完全使用注解开发(替代 spring.xml)
注解是需要书写在类中的所以书写配置注解的类称之为注解配置类
**注解说明**
- @Configuration 用于标识新建的 class 类为 spring 配置类，等价于导入了 spring 核心配置文件：
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	       http://www.springframework.org/schema/beans/spring-beans.xsd
	       http://www.springframework.org/schema/context
	       http://www.springframework.org/schema/context/spring-context.xsd">
	
	</beans>
	```
- @Bean 用于配置由 spring 管理的对象
- @ComponentScan 用于配置当前 spring 容器创建时扫描并管理的类所在的包
- @PropertySource 与 @Value PropertySource 负责导入 propertie 配置文件，value 负责进行属性的注入。

### 示例 ：配置 Alibaba 的 druid 数据源：
```Java
//用于标识当前类为注解配置类
@Configuration//等价于导入了文件的spring.xml
//开启组件扫描
@ComponentScan("com.sqsf")//等价于<context:component-scan base-package="com.sqsf"/>
//读取properties配置文件
@PropertySource("classpath:jdbc.properties")//等价于<context:property-placeholder location="classpath:jdbc.properties"/>
public class SpringConfig {
    //与配置文件不同 需要声明变量保存读取数据
    //使用@Value("${name}")的形式注入数据
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    //配置当前方法返回的对象给spring容器管理
    //<bean id="druid" factory-bean="factory" factory-method="druid"/>
    @Bean("druid")
    public DruidDataSource druid(){
        DruidDataSource druid=new DruidDataSource();
        druid.setDriverClassName(driver);
        druid.setUrl(url);
        druid.setUsername(username);
        druid.setPassword(password);
        return  druid;
    }
}
```
jdbc.properties：
```properties
jdbc.driver=com.mysql.cj.jdbc.Driver  
jdbc.url=jdbc:mysql://localhost:3306/  
jdbc.username=root  
jdbc.password=123456
```

测试：
```Java
public class AnnotationTest {
    @Test
    public void r(){
        AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(SpringConfig.class);
        Object r1 = applicationContext.getBean("r");
        Object r2 = applicationContext.getBean("r");
        System.out.println(r1);
        System.out.println(r1==r2);
        System.out.println(r1.hashCode()+"|"+r2.hashCode());
        applicationContext.close();
    }

    @Test
    public void u(){
        AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(SpringConfig.class);
        Object u1 = applicationContext.getBean("u");
        Object u2 = applicationContext.getBean("u");
        System.out.println(u1);
        System.out.println(u1==u2);
        System.out.println(u1.hashCode()+"|"+u2.hashCode());
        applicationContext.close();
    }
    @Test
    public void druid() throws SQLException {
        AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(SpringConfig.class);
        DruidDataSource druid= (DruidDataSource) applicationContext.getBean("druid");
        System.out.println(druid.getConnection());
        applicationContext.close();
    }
}
```


## 导入注解
---
**注解说明**
@Import 如果使用配置类进行配置，那么配置的类也可以使用类的形式进行书写进行导入。
### 示例：将配置 Alibaba 的 druid 数据源与配置类分离
- DruidConfig.java
```Java
//读取properties配置文件
@PropertySource("classpath:jdbc.properties")//等价于<context:property-placeholder location="classpath:jdbc.properties"/>
public class DruidConfig {
    //与配置文件不同 需要声明变量保存读取数据
    //使用@Value("${name}")的形式注入数据
    @Value("${jdbc.driver}")
    private  String driver;
    @Value("${jdbc.url}")
    private  String url;
    @Value("${jdbc.username}")
    private  String username;
    @Value("${jdbc.password}")
    private  String password;

    //配置当前方法返回的对象给spring容器管理
    //<bean id="druid" factory-bean="factory" factory-method="druid"/>
    @Bean("druid")
    public DruidDataSource druid(){
        DruidDataSource druid=new DruidDataSource();
        druid.setDriverClassName(driver);
        druid.setUrl(url);
        druid.setUsername(username);
        druid.setPassword(password);
        return druid;
    }
}
```

- SpringConfig.java
```java
//用于标识当前类为注解配置类
@Configuration//等价于导入了文件的spring.xml
//开启组件扫描
@ComponentScan("com.sqsf")//等价于<context:component-scan base-package="com.sqsf"/>
//读取其他注解配置类内容
@Import({DruidConfig.class})
public class SpringConfig {
}
```


### Spring 核心配置文件导入配置
在实际开发中，spring 配置文件基于系统需要配置很多内容那么都配置在一个 spring.xml中，不利于查找与修改所以可以通过创建多个 spring 配置文件分别配置指定功能在加载的spring中使用import标签进行导入。

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
    <!-- 在实际开发中 spring配置文件基于系统需要配置很多内容 那么都配置在一个spring.xml中
     不利于查找与修改 所以可以通过创建多个spring配置文件分别配置指定功能
     在加载的spring中使用import标签进行导入
     -->
        <context:property-placeholder location="classpath:jdbc.properties"/>
        <bean id="druidFactory" class="com.sqsf.util.DruidFactory"/>
        <bean id="druid" factory-bean="druidFactory" factory-method="druid"/>

</beans>
```

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
       
    <!-- 如果想使用注解开发 那么需要配置 告诉spring从哪查找类-->
    <!-- 开启组件扫描 会扫描指定包及其子包下所有书写注解的类 -->
    <context:component-scan base-package="com.sqsf"/>

    <import resource="classpath:spring-druid.xml"/>

</beans>
```