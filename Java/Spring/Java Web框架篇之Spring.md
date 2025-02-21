## Java Web系列文章汇总贴: [Java Web知识总结汇总](https://blog.csdn.net/zangdaiyang1991/article/details/91480641)

___

## 为什么要有Spring(IoC)

### Web发展的几个阶段

-   （1）初级阶段：使用Model1(JSP+JavaBean)/Model2(Jsp+Servlet+JavaBean)/三层模型(表示层(JSP/Servlet)+业务逻辑层+持久化层)进行开发；
-   （2）中级阶段：使用EJB进行分布式应用开发，忍受重量级框架带来的种种麻烦；
-   （3）高级阶段：使用Spring春天带给我们的美好，但是还要忍受很多繁琐的配置；
-   （4）骨灰级阶段：使用Spring Boot，畅享“预定大于配置”带给我们的种种乐趣！

### Web发展初级阶段存在的问题

-   1、面向接口编程的实例化对象，每一个方法中都需要进行实例化我们需要用到的接口的实现类，这就会存在大量的实例化对象，并且他们的生命周期可能就是从方法的调用开始到方法的调用结束为止，加大了GC回收的压力！
-   2、使用单例模式的一次改进，使用单例模式的方式来解决这个问题，以此来避免大量重复的创建对象，但是我们还要考虑到众多的这种对象的创建都需要改成单例模式的话，是一个耗时耗力的操作。对于这个系统来说，如果都把这种面向接口的对象实现类转换为单例模式的方式的话，大概也要写十几个或者上百个这种单例模式代码，而对于一个单例模式的写法来说，往往是模板式的代码。
-   3、使用工厂模式创建对象，也会存在大量的工厂、模板式代码，需要自己管理复杂的实例依赖关系，而且代码的耦合性较高

可以看出，这种方式有两个问题：  
（1）业务代码与单例/工厂模式的模板代码放在一个类里，耦合性较高；  
（2）大量重复的单例/工厂模式的模板代码，需要自己管理对象间复杂的依赖关系

更多：  
[通过Web开发演进过程了解一下为什么要有Spring](https://blog.csdn.net/xlgen157387/article/details/78884005)

  

## Spring概述

### 是什么?

一个[开源](https://edu.csdn.net/cloud/pm_summit?utm_source=blogglc)的轻量级开发框架，是为了解决企业应用程序的复杂性而创建的。

### 为什么？

EJB时代，企业级应用开发困难。Spring设计初衷是使JavaEE更加容易，为[JavaBean](https://so.csdn.net/so/search?q=JavaBean&spm=1001.2101.3001.7020)提供配置框架，使程序易于测试，设计目标是简单易用，与应用程序解耦，致力于集成其他解决方案，而不是竞争。Spring不仅仅限于服务器端的开发，从简单性、可测试性和松耦合性角度而言，绝大部分Java应用都可以从Spring中学习受益。

### 怎么做?

Spring包括Core+Context，Aop，Dao，ORM，Web(SpringMVC)，JEE 等模块。  
![Spring框架概述](https://i-blog.csdnimg.cn/blog_migrate/36c6ed14dbfea05e0c1124f62e85ba3c.png)

这里着重介绍下Ioc和Aop两大核心模块。

#### IoC简介

IoC(Inversion of Control)控制反转，对象创建责任的反转，在spring中BeanFacotory是IoC容器的核心接口，负责实例化，定位，配置应用程序中的对象及建立这些对象间的依赖。XmlBeanFacotory实现BeanFactory接口，通过获取xml配置文件数据，组成应用对象及对象间的依赖关系。  
spring中有三种注入方式，一种是set注入，一种是接口注入，另一种是构造方法注入。

IOC，字面理解是控制反转，即对象的控制权被反转了(是什么)。之前一个对象中依赖另一个对象，需要自己new出来，当对象间的依赖关系非常复杂时，这个过程就变得很繁琐，并且代码间的耦合会很高。现在可以通过Ioc容器来管理控制对象的生成，可以把对象的实例化过程简单化，代码间解耦(为什么)。具体可以从DI(Dependency Injection) DL(Dependency Lookup)两个角度理解Ioc。DI中注入的方式包括属性，构造器，setter注入，DL含义是通过容器的API来查找所依赖的资源和协作对象，从Ioc容器维护的bean map中取出来(怎么做)

#### Aop简介

Aop就是面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。

为什么？  
利用Aop可以对业务逻辑的各部分进行隔离，从而降低各部分耦合度，提高程序的可重用性，提高开发效率

怎么做？  
spring中面向切面变成的实现有两种方式，一种是动态代理，一种是CGLIB，动态代理必须要提供接口，而CGLIB实现是有继承，即，

-   接口+实现类，spring采用jdk的动态代理实现，
-   实现类，spring采用cglib字节码增强实现。

当然也可以通过集成AspectJ可以更方便的实现自定义切面。Spring Aop支持前/后/环绕等多种类型的通知机制：

-   before(前置通知)：在一个方法之前执行的通知。
-   after(最终通知)：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
-   after-returning(后置通知)：在某连接点正常完成后执行的通知。
-   after-throwing(异常通知)：在方法抛出异常退出时执行的通知。
-   around(环绕通知)：在方法调用前后触发的通知。

好处?

-   轻量级的容器框架没有侵入性
-   使用IoC容器更加容易组合对象直接间关系，面向接口编程，降低耦合
-   Aop可以更加容易的进行功能扩展，遵循ocp开发原则
-   创建对象默认是单例的，不需要再使用单例模式进行处理
-   基于Ioc Aop，Spring提供了事务管理，Spring Web，日志等一系列经典应用

缺点?

-   业务功能依赖spring特有的功能，依赖与spring环境。

更多：  
[Spring框架介绍及使用](https://blog.csdn.net/qq_22583741/article/details/79589910)  
[Spring简介](https://blog.csdn.net/yerenyuan_pku/article/details/52830571)  
[AOP实践(AspectJ)-日志实现](https://blog.csdn.net/zangdaiyang1991/article/details/85163412)

  

## Spring IoC

### Ioc理解

IOC（DI）：java程序中的每个业务逻辑至少需要两个或以上的对象来协作完成。通常，每个对象在使用他的合作对象时，自己均要使用像new object() 这样的语法来完成合作对象的申请工作。你会发现：对象间的耦合度高了。而IOC的思想是：Spring容器来实现这些相互依赖对象的创建、协调工作。对象只需要关系业务逻辑本身就可以了。从这方面来说，对象如何得到他的协作对象的责任被反转了（IOC、DI）。

这是我对Spring的IOC的体会。DI其实就是IOC的另外一种说法。DI是由Martin Fowler 在2004年初的一篇论文中首次提出的。他总结：控制的什么被反转了？就是：获得依赖对象的方式反转了。

如果对这一核心概念还不理解：这里引用一个叫Bromon的blog上找到的浅显易懂的答案：

### IoC与DI

-   首先想说说IoC（Inversion of Control，控制倒转）。  
    这是spring的核心，贯穿始终。所谓IoC，对于spring框架来说，就是由spring来负责控制对象的生命周期和对象间的关系。举个例子，我们是如何找女朋友的？常见的情况是，我们到处去看哪里有长得漂亮身材又好的mm，然后打听她们的兴趣爱好、qq号、电话号…，想办法认识她们，投其所好送其所好，然后嘿嘿…这个过程是复杂深奥的，我们必须自己设计和面对每个环节。  
    传统的程序开发也是如此，在一个对象中，如果要使用另外的对象，就必须得到它（自己new一个，或者从JNDI中查询一个），使用完之后还要将对象销毁（比如Connection等），对象始终会和其他的接口或类耦合起来。
    
-   那么IoC是如何做的呢？有点像通过婚介找女朋友，在我和女朋友之间引入了一个第三者：婚姻介绍所。婚介管理了很多男男女女的资料，我可以向婚介提出一个列表，告诉它我想找个什么样的女朋友，比如长得像李嘉欣，身材像林熙雷，技术像齐达内之类的，然后婚介就会按照我们的要求，提供一个mm，我们只需要去和她谈恋爱、结婚就行了。简单明了，如果婚介给我们的人选不符合要求，我们就会抛出异常。整个过程不再由我自己控制，而是有婚介这样一个类似容器的机构来控制。
    
-   Spring所倡导的开发方式就是如此：所有的类都会在spring容器中登记，告诉spring你是个什么东西，你需要什么东西，然后spring会在系统运行到适当的时候，把你要的东西主动给你，同时也把你交给其他需要你的东西。所有的类的创建、销毁都由 spring来控制，也就是说控制对象生存周期的不再是引用它的对象，而是spring。对于某个具体的对象而言，以前是它控制其他对象，现在是所有对象都被spring控制，所以这叫控制反转。
    
-   IoC的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。这一点是通过DI（Dependency Injection，依赖注入）来实现的。比如对象A需要操作数据库，以前我们总是要在A中自己编写代码来获得一个Connection对象，有了 spring我们就只需要告诉spring，A中需要一个Connection，至于这个Connection怎么构造，何时构造，A不需要知道。在系统运行时，spring会在适当的时候制造一个Connection，然后像打针一样，注射到A当中，这样就完成了对各个对象之间关系的控制。A需要依赖 Connection才能正常运行，而这个Connection是由spring注入到A中的，依赖注入的名字就这么来的。  
    那么DI是如何实现的呢？ Java 1.3之后一个重要特征是反射（reflection），它允许程序在运行的时候动态的生成对象、执行对象的方法、改变对象的属性，spring就是通过反射来实现注入的。
    

摘自：  
[最好理解的： spring ioc原理讲解，强烈推荐](https://blog.csdn.net/jiangyu1013/article/details/72654373)  
[控制反转和依赖注入的理解(通俗易懂)](https://blog.csdn.net/sinat_21843047/article/details/80297951)  
[Spring源码剖析——核心IOC容器原理](https://blog.csdn.net/lisongjia123/article/details/52129340)  
[Spring源码剖析——依赖注入实现原理](https://blog.csdn.net/lisongjia123/article/details/52134396)

  

## Spring装配Bean

### 生命周期流程图

![ Spring Bean的完整生命周期](https://i-blog.csdnimg.cn/blog_migrate/022130557a376e0bbb2518c871a0e0d0.png)

以BeanFactory为例，说明一个Bean的生命周期活动

-   Bean的建立， 由BeanFactory读取Bean定义文件，并生成各个实例
-   Setter注入，执行Bean的属性依赖注入
-   BeanNameAware的setBeanName(), 如果实现该接口，则执行其setBeanName方法
-   BeanFactoryAware的setBeanFactory()，如果实现该接口，则执行其setBeanFactory方法
-   BeanPostProcessor的processBeforeInitialization()，如果有关联的processor，则在Bean初始化之前都会执行这个实例的processBeforeInitialization()方法
-   InitializingBean的afterPropertiesSet()，如果实现了该接口，则执行其afterPropertiesSet()方法
-   Bean定义文件中定义init-method
-   BeanPostProcessors的processAfterInitialization()，如果有关联的processor，则在Bean初始化之前都会执行这个实例的processAfterInitialization()方法
-   DisposableBean的destroy()，在容器关闭时，如果Bean类实现了该接口，则执行它的destroy()方法
-   Bean定义文件中定义destroy-method，在容器关闭时，可以在Bean定义文件中使用“destory-method”定义的方法

![](https://img2018.cnblogs.com/blog/1082754/201810/1082754-20181030104208937-1907545744.png)

### Spring装配Bean的过程

#### 装配bean过程

1.  实例化;
2.  设置属性值;
3.  如果实现了BeanNameAware接口,调用setBeanName设置Bean的ID或者Name;
4.  如果实现BeanFactoryAware接口,调用setBeanFactory 设置BeanFactory;
5.  如果实现ApplicationContextAware,调用setApplicationContext设置ApplicationContext
6.  调用BeanPostProcessor的预先初始化方法;
7.  调用InitializingBean的afterPropertiesSet()方法;
8.  调用定制init-method方法；
9.  调用BeanPostProcessor的后初始化方法;

#### Spring容器关闭过程

1.  调用DisposableBean的destroy();
2.  调用定制的destroy-method方法;

#### 其他说明

懒加载：就是我们在spring容器启动的是先不把所有的bean都加载到spring的容器中去，而是在当需要用的时候，才把这个对象实例化到容器中。

spring配置文件中bean默认是lazy-init=“false”为非懒加载。下面具体说明。

1、默认情况下bean实例化过程：  
AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("/beans.xml"); //随着spring容器加载，就实例化了bean。

2、给bean设置 lazy-init=“true”  
AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("/beans.xml"); //随着spring容器加载，就不会实例化bean。  
Person person = ctx.getBean(“person”);//这一步才在实例化bean。就是前面说的需要的时候再实例化了。

简单描述实例化bean的过程如下：

实例化bean测试结果：先构造函数——>然后是b的set方法注入—— >InitializingBean 的afterPropertiesSet方法——>init- method方法

相关：  
[Spring Bean的生命周期（非常详细）](https://www.cnblogs.com/zrtqsk/p/3735273.html)  
[Spring Bean的生命周期](https://www.cnblogs.com/redcool/p/6397398.html)  
[Spring学习之Bean详解](https://blog.csdn.net/xlgen157387/article/details/40148567)

  

## 为什么要有Spring AOP

### 存在的问题

业务代码已经被这些非核心的代码所混淆，并且占据了大量的空间！显然这种显示的调用过程成为了我们开发过程中的一个痛点，如何将类似这种的非核心的代码剥离出去成为一个迫切需要解决的问题。  
诸如日志记录，登录权限控制，还有数据库事务的控制，数据库连接的创建和关闭等等，这些都充斥这大量重复性的模板代码！

### 使用设计模式进行一次改进

可以应用JDK动态代理设计模式（动态代理设计模式可以在原有的方法前后添加判断、选择或其他逻辑）。

### SpringAOP的实现

#### AOP思想

在动态代理的invoke方法里边，我们相当于在原有方法的调用前后“植入”了我们的通用日志记录代码，如果你看到这一层的话，那么恭喜你！你已经领悟到了AOP思想最核心的东西了！上述抽取公共代码其实就是AOP中横切的过程，代理对象中在方法调用前后“植入”自己写的通用日志记录代码其实就是AOP中织入的过程！这个织入的代码也就是横切逻辑，织入代码的过程其实就是在原有的方法前后增强 原方法的过程！总的来说，我们想解决我们开发中的痛点，然后就出现了一种技术，这种技术手段就是AOP。

AOP（Aspect Oriented Programming）意为：面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容（Spring核心之一），是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

#### Spring AOP

##### AOP与SpringAOP

AOP是一种思想，不同的厂商或企业可能有不同的实现方式，为了更好的应用AOP技术，技术专家们成立了AOP联盟来探讨AOP的标准化，AOP联盟定义的AOP体系结构把与AOP相关的概念大致分为由高到低、从使用到实现的三层关系。  
在AOP联盟定义的AOP体系结构下有很多的实现者，例如：AspectJ、AspectWerkz、JBoss AOP、Spring AOP等。Spring AOP就是在此标准下产生的。

##### Spring AOP的实现方式

-   通过动态代理的方式实现了简单的AOP，但是值得注意的是，我们的代理目标对象必须实现一个接口，要是一个接口的实现类，这是因为再生成Proxy对象的时候这个方法需要一个目标对象的接口。
-   对于没有接口的对象，Spring使用CGLib的代理方式实现了这种诉求。CGLib采用底层的字节码技术，可以为一个类创建子类，在子类中采用方法拦截的技术拦截所有父类方法的调用并顺势的织入横切逻辑。这两种方式已经实现了我们绝大多数的场景

参考；  
[通过Web开发演进过程了解一下为什么要有Spring AOP](https://blog.csdn.net/xlgen157387/article/details/78892509)  
[Spring历史版本变迁和如今的生态帝国](https://blog.csdn.net/xlgen157387/article/details/78910985)

  

## Spring AOP中的JDK和CGLib动态代理

### 概述

JDK动态代理主要涉及java.lang.reflect包下边的两个类：Proxy和InvocationHandler。其中，InvocationHandler是一个接口，可以通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，动态地将横切逻辑和业务逻辑贬值在一起。

JDK动态代理的话，他有一个限制，就是它只能为接口创建代理实例，而对于没有通过接口定义业务方法的类，如何创建动态代理实例哪？答案就是CGLib。

CGLib采用底层的字节码技术，全称是：Code Generation Library，CGLib可以为一个类创建一个子类，在子类中采用方法拦截的技术拦截所有父类方法的调用并顺势织入横切逻辑。

### 区别

#### 1、JDK动态代理具体实现原理：

-   通过实现InvocationHandlet接口创建自己的调用处理器；
-   通过为Proxy类指定ClassLoader对象和一组interface来创建动态代理；
-   通过反射机制获取动态代理类的构造函数，其唯一参数类型就是调用处理器接口类型；
-   通过构造函数创建动态代理类实例，构造时调用处理器对象作为参数参入；

JDK动态代理是面向接口的代理模式，如果被代理目标没有接口那么Spring也无能为力，Spring通过Java的反射机制生产被代理接口的新的匿名实现类，重写了其中AOP的增强方法。

#### 2、CGLib动态代理：

CGLib是一个强大、高性能的Code生产类库，可以实现运行期动态扩展java类，Spring在运行期间通过 CGlib继承要被动态代理的类，重写父类的方法，实现AOP面向切面编程呢。

#### 3、两者对比：

JDK动态代理是面向接口的。

CGLib动态代理是通过字节码底层继承要代理类来实现（如果被代理类被final关键字所修饰，那么抱歉会失败）。

#### 4、使用注意：

如果要被代理的对象是个实现类，那么Spring会使用JDK动态代理来完成操作（Spirng默认采用JDK动态代理实现机制）；

如果要被代理的对象不是个实现类那么，Spring会强制使用CGLib来实现动态代理。

### 二者性能对比

#### 教科书上的描述：

JDK动态代理所创建的代理对象，在以前的JDK版本中，性能并不是很高，虽然在高版本中JDK动态代理对象的性能得到了很大的提升，但是他也并不是适用于所有的场景。主要体现在如下的两个指标中：

1、CGLib所创建的动态代理对象在实际运行时候的性能要比JDK动态代理高不少，有研究表明，大概要高10倍；  
2、但是CGLib在创建对象的时候所花费的时间却比JDK动态代理要多很多，有研究表明，大概有8倍的差距；  
3、因此，对于singleton的代理对象或者具有实例池的代理，因为无需频繁的创建代理对象，所以比较适合采用CGLib动态代理，反正，则比较适用JDK动态代理。

#### 实际验证结论

在1.6和1.7的时候，JDK动态代理的速度要比CGLib动态代理的速度要慢，但是并没有教科书上的10倍差距，在JDK1.8的时候，JDK动态代理的速度已经比CGLib动态代理的速度快很多了

摘自：  
[Spring AOP中的JDK和CGLib动态代理哪个效率更高](https://blog.csdn.net/xlgen157387/article/details/82497594)

### 动态代理的原理

JDK动态代理原理：反射机制，运行时增强  
CGLib代理原理：字节码，改变字节码的编译，运行期增强  
AspectJ：静态代理，编译时改变

参考：  
[动态代理的原理及其应用](https://blog.csdn.net/pjmike233/article/details/81489825)  
[Java动态代理的两种实现方法](https://blog.csdn.net/HEYUTAO007/article/details/49738887)  
[java动态代理原理及解析](https://blog.csdn.net/Scplove/article/details/52451899)

### AspectJ原理及与动态代理区别

参考：  
[Spring AOP 实现原理----AspectJ与CGLIB介绍](https://blog.csdn.net/wenbingoon/article/details/8988553)  
[静态代理和动态代理的理解](https://blog.csdn.net/WangQYoho/article/details/77584832)  
[java经典讲解-静态代理和动态代理的区别](https://blog.csdn.net/fangqun663775/article/details/78960545)

  

## Spring AOP的应用

-   日志、数据库读写分离框架
-   Spring事务
-   SpringMVC

相关：  
[AOP实践(AspectJ)-日志实现](https://blog.csdn.net/zangdaiyang1991/article/details/85163412)  
[使用Spring AOP实现MySQL数据库读写分离案例分析](https://blog.csdn.net/xlgen157387/article/details/53930382)  
[JDK动态代理给Spring事务埋下的坑](https://blog.csdn.net/xlgen157387/article/details/79026285)  
[Spring事务配置的五种方式和spring里面事务的传播属性和事务隔离级别](https://blog.csdn.net/it_man/article/details/5074371)

  

## Spring知识点

[Spring常见问答总结（超详细回答）](https://blog.csdn.net/a745233700/article/details/80959716)  
[myBatis+Spring+SpringMVC框架知识点(一)](https://blog.csdn.net/qq_41541619/article/details/82459873)  
[myBatis+Spring+SpringMVC框架知识点(二)](https://blog.csdn.net/qq_41541619/article/details/82459965)