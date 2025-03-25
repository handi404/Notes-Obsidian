# 1.Spring 的 AOP 简介

## 1.1 什么是 AOP 

AOP 为 Aspect Oriented Programming 的缩写，意思为面向切面编程，是通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。

AOP 是 OOP 的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

可以理解为在对同一个系统应用不同用户的使用过程中，对调用的相同方法进行切面拦截，添加额外的功能在不修改原有代码的前提下进行功能的增强（使用代理模式将方法进行代理增强）
## 1.2 AOP 的作用及其优势

作用：在程序运行期间，在不修改源码的情况下对方法进行功能增强（权限、事务、日志）

优势：减少重复代码，提高开发效率，并且便于维护

## 1.3 AOP 相关概念

Spring 的 AOP 实现底层就是对上面的动态代理的代码进行了封装，封装后我们只需要对需要关注的部分进行代码编写，并通过配置的方式完成指定目标的方法增强。

在正式讲解 AOP 的操作之前，我们必须理解 AOP 的相关术语，常用的术语如下：

- Target（目标对象）：代理的目标对象

- Proxy （代理）：一个类被 AOP 织入增强后，就产生一个结果代理类

- Joinpoint（连接点）：所谓连接点是指那些被拦截到的点。在spring中,这些点指的是方法，因为spring只支持方法类型的连接点

- Pointcut（切入点）：所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义

- Advice（通知/ 增强）：所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知

- Aspect（切面）：是切入点和通知（引介）的结合

- Weaving（织入）：是指把增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入


## 1.4 AOP 开发明确的事项

### 1)需要编写的内容

- 编写核心业务代码（目标类的目标方法）

- 编写切面类，切面类中有通知(增强功能方法)

- 在配置文件中，配置织入关系，即将哪些通知与哪些连接点进行结合

### 2）AOP 技术实现的内容

Spring 框架监控切入点方法的执行。一旦监控到切入点方法被运行，使用代理机制，动态创建目标对象的代理对象，根据通知类别，在代理对象的对应位置，将通知对应的功能织入，完成完整的代码逻辑运行。

### 3）AOP 底层使用哪种代理方式

在 spring 中，框架会根据目标类是否实现了接口来决定采用哪种动态代理的方式。

 

## 1.5 AOP 的底层实现

实际上，AOP 的底层是通过 Spring 提供的的动态代理技术实现的。在运行期间，Spring通过动态代理技术动态的生成代理对象，代理对象方法执行时进行增强功能的介入，在去调用目标对象的方法，从而完成功能的增强。

首先书写相应的增强方法（执行指定方法时额外执行的方法），通过配置或注解形式将增强方法与切入点进行连接，这样当指定的方法被执行时，默认会调用jdk动态代理创建代理对象并将增强方法在指定位置执行，如果被代理的类没有实现任意接口那么会使用cglib动态代理

 

### 1.5.1  AOP 的动态代理技术

常用的动态代理技术

JDK 代理 : 基于接口的动态代理技术

cglib 代理：基于父类的动态代理技术



### 1.5.2 JDK 的动态代理

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



### 1.5.3 cglib 的动态代理

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





# 2. 基于 XML 的 AOP 开发

### 2.1 快速入门

①导入 AOP 相关坐标

②创建目标接口和目标类（内部有切点）

③创建切面类（内部有增强方法）

④将目标类和切面类的对象创建权交给 spring

⑤在 applicationContext.xml 中配置织入关系

⑥测试代码

-----------------------------------------------------------------------------
①导入 AOP 相关坐标

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

②创建目标接口和目标类（内部有切点）

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

③创建切面类（内部有增强方法）

```java
public class MyAspect {
    //前置增强方法
    public void before(){
        System.out.println("前置代码增强.....");
    }
}
```

④将目标类和切面类的对象创建权交给 spring

```xml
<!--配置目标类-->
<bean id="target" class="com.yh.aop.Target"></bean>
<!--配置切面类-->
<bean id="myAspect" class="com.yh.aop.MyAspect"></bean>


```

⑤在 applicationContext.xml 中配置织入关系

导入aop命名空间

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

⑤在 applicationContext.xml 中配置织入关系

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

⑥测试代码

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
TargetInterface target= (TargetInterface ) applicationContext.getBean("target");   				 
target.method();
```

 

### 2.2 XML 配置 AOP 详解

#### 1) 切点表达式的写法

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

#### 2) 通知的类型

通知的配置语法：

```xml
<aop:通知类型 method=“切面类中方法名” pointcut=“切点表达式"></aop:通知类型>
```
1) 前置通知：before 在连接点方法执行之前执行。

```xml
<aop:before method="before" pointcut="timePointcut"></aop:before>
```

```java
    //前置通知
    public void before() {
        System.out.println("前置通知");
    }
```

2) 后置通知：after  在连接点方法执行之后，无论如何都会执行。（finally）
          method：指定切面中的通知方法， pointcut-ref：指定切入点

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

4) 异常通知：after-throwing  在连接点方法发生异常之后执行。
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

5) 返回通知：after-returning  在连接点方法返回结果后执行（如果发生异常则不会执行）。
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



#### 3) 切点表达式的抽取

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

### 2.3 知识要点

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

# 3.基于注解的 AOP 开发

## 3.1 快速入门

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

## 3.2 注解配置 AOP 详解

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



## 3.3 知识要点

- 注解aop开发步骤

①使用@Aspect标注切面类

②使用@通知注解标注通知方法

③在配置文件中配置aop自动代理 \<aop:aspectj-autoproxy/>

 

























