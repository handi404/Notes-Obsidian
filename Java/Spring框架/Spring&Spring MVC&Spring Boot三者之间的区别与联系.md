-   什么是Spring？它解决了什么问题？
-   什么是Spring MVC？它解决了什么问题？
-   什么是Spring Boot？它解决了什么问题？
-   Spring，Spring MVC，Spring Boot 三者比较

### 什么是Spring？它解决了什么问题？

Spring，一般指代的是Spring Framework，**它是一个开源的应用程序框架**，**提供了一个简易的开发方式，通过这种开发方式，将避免那些可能致使代码变得繁杂混乱的大量的业务/工具对象**，说的更通俗一点就是由框架来帮你管理这些对象，包括它的创建，销毁等，比如基于Spring的项目里经常能看到的Bean，它代表的就是由Spring管辖的对象。

Spring Framework到底解决了哪些核心问题？

Spring Framework最重要也是最核心的特性是依赖注入。所有的**Spring模块的核心就是DI(依赖注入)或者IoC(控制反转)**。依赖注入或控制反转是Spring Framework最大的特性，当我们正确使用DI（依赖注入）或IoC时，可以开发出一个高内聚低耦合的应用程序，而这一一个低耦合的应用程序可以轻松的对其实施单元测试。这就是Spring Framework解决的最核心的问题。

引入依赖注入将会使整个代码看起来很清爽。为了能够**开发出高内聚低耦合**的应用程序，Spring Framework为我们做了大量的准备工作。下面我们使用两个简单的注解@Component和@Autowired来实现依赖注入。

@Component : 该注解将会告诉Spring Framework,被此注解标注的类需要纳入到Bean管理器中。  
@Autowired : 告诉Spring Framework需要找到一与其类型匹配的对象，并将其自动引入到所需要的类中。  
Spring Framework的依赖注入是核心中的核心，在依赖注入核心特性的基础上  
Spring 的高级模块其它特性，如：

Spring AOP  
Spring JDBC  
Spring MVC  
Spring ORM  
Spring JMS  
Spring Test

对于这些新的高级模块，可能会产生这一一个问题：它们是否是一个全新的功能？答案是否定的，在不使用Spring Framework的情况下，我们依然能够使用JDBC连接数据库，依然能够对视图和数据模型进行控制，依然能够使用第三方的ORM框架。那Spring Framework干了什么？Spring Framework站在巨人的肩膀上，对这些**原生的模块进行了抽象**，而抽象可以带来这样一些好处：

-   减少了应用中模板代码的数量
-   降低了原生框架的技术门槛
-   基于依赖注入特性，实现了代码的解耦，真正的高内聚、低耦合
-   更细粒度的单元测试  
    这样的好处是显而易见的，比如与传统的JDBC相比，使用JDBCTemplate操作数据库，首先是代码量小了，其次是我们不需要再面对恐怖的try-catch

### 集成能力

Spring Framework还具备另外一个重要特性，那就是能够快速的与其他三方框架进行整合。与其自己造轮子，还不如想办法将好的轮子整合在一起，我想这句话应该可以用来概况Spring Framework这一特性。Spring Framework对于整合其他的框架，给出了不错的解决方案，下面将列举一些常见的方案：

-   Hibernate ORM框架整合
-   MyBatis 对象映射框架整合
-   Junit单元测试框架整合
-   Log4J日志记录框架整合

### Spring MVC是什么？

Spring MVC提供了构建Web应用程序的全功能MVC模块，实现了Web MVC设计模式以及请求驱动类型的轻量级Web框架，即采用了MVC架构模式的思想，将Web层进行职责解耦。基于请求驱动指的是使用请求-响应模型，视图与数据模型分离，以简化Web应用的开发

Spring MVC是Spring的一部分，Spring 出来以后，大家觉得很好用，于是按照这种模式设计了一个 MVC框架（一些用Spring 解耦的组件），主要用于开发WEB应用和网络接口，它是Spring的一个模块，**通过Dispatcher Servlet, ModelAndView 和 View Resolver**

### 解决了什么问题？

使用Spring MVC提供的Dispatcher Servlet,ModelAndView和ViewResolver等功能，可以轻松的开发出一个Web应用程序

### 什么是Spring Boot？解决了什么问题？

基于 Spring 的项目有很多的配置。当使用 Spring MVC 时，我们需要配置组件扫描，前段控制器 Servlet，视图解析器。当使用 Hibernate 时，我们需要配置数据源，实体工厂，事务管理等

Spring Boot 提供了使用这些框架应用程序所需的基本配置。这就是所谓的自动配置。  
Spring Boot的自动化配置能力放在第一位，因为它极大的降低了我们使用Spring Framework所付出的成本。这是Spring Boot的自动化配置是一个最具价值的解决方案。

这难道不值得我们拍案叫好吗？如果你想要开发一个Web应用程序，你需要做的事情就是将Spring Boot Web包引入到项目的类路径下，Spring Boot就可以帮你解决后续的大多数配置工作。

-   如果Hibernate的依赖被放到了类路径上，Spring Boot会自动配置数据源
-   如果Spring MVC的依赖被放到了类路径上，Spring Boot又会自动配置Dispatcher Servlet  
    当Spring Boot检测到有新的依赖包添加到类路径上，Spring Boot会采用默认的配置对新的依赖包进行设置，如果我们想自己配置依赖包时，只需要手动覆盖默认的配置项即可。

Spring Boot扫描类路径上可用的框架信息

-   获取应用程序现有的配置信息
-   如果应用程序没有提供框架的配置信息，Spring Boot将采用默认的配置来配置框架，这就是Spring Boot的自动配置特性（Auto Configuration）
-   Starter 模块自动构建项目依赖  
    在传统模式的开发过程中，我们需要反复的确认应用程序所需要的第三方JAR包，以及这些JAR的版本和依赖关系。例如，现在我们打算开发一款Web应用程序，应用程序大概需要如下的一些依赖包：Spring MVC，Jackson Databind(用于数据绑定)，Hibernate-Validator(用于服务端的数据校验)和Log4j(用于日志记录)。现在，我们需要去下载对应的jar包到应用程序中，并且还**需要处理依赖包之间版本冲突的问题。**  
    **Spring Boot Starter是一组用于管理依赖关系的描述符，通过这些描述符，我们可以在应用程序中轻松的管理依赖包**，你可以以开箱即用的方式获取想要的依赖包，而无需去Maven仓库总检索对应的依赖，并将依赖配置复制粘贴到应用程序的pom文件中。例如，如果你想要使用Spring和JPA进行数据库访问，只需要在pom中添加spring-boot-starter-data-jpa依赖项就可以

##### Spring Boot的核心目标

Spring Boot的核心目标在于快速实现生产就绪的应用程序，这将包含这样几个部分：

-   执行器 ： 启用高级监控和跟踪应用程序功能
-   嵌入式服务器：Spring Boot已经内置了多个Web服务器，如Undertow,jetty,tomcat，因此我们不需要再额外的配置服务器，就可以完成应用程序的调试工作。
-   默认的异常处理机制
-   开箱即用的依赖项管理机制
-   自动化配置

### Spring，Spring MVC，Spring Boot 三者比较

总的来说，Spring 就像一个大家族，有众多衍生产品例如 Boot，Security，JPA等等。但他们的基础都是Spring 的 IOC 和 AOP，IOC提供了依赖注入的容器，而AOP解决了面向切面的编程，然后在此两者的基础上实现了其他衍生产品的高级功能；Spring MVC是基于 Servlet 的一个 MVC 框架，主要解决 WEB 开发的问题，因为 Spring 的配置非常复杂，各种xml，properties处理起来比较繁琐。于是为了简化开发者的使用，Spring社区创造性地推出了Spring Boot，它遵循约定优于配置，极大降低了Spring使用门槛，但又不失Spring原本灵活强大的功能  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/1afdac945d3d7f9d06ce991aec1c66a8.png)  
**Spring MVC和Spring Boot都属于Spring，Spring MVC 是基于Spring的一个 MVC 框架，而Spring Boot 是基于Spring的一套快速开发整合包**

### 总结

Spring Framework是一个提供了DI(依赖注入)和IoC(控制反转)的开发框架，使用Spring Framework可以帮助我们开发出**高内聚，低耦合的应用程序**，**Spring MVC是在Spring Framework基础上发展出来的基于MVC模式的全功能Web开发框架，实现了Model,View和Controller之间的职责解耦**；Spring Boot为我们提供了一个能够快速使用Spring Framework的优秀解决方案，通过最小化的配置，我们就可以使用Spring Framework，严格意义上讲，Spring Boot并不是某种框架，它只是为开发人员提供了一个更好的更方便的使用Spring Framework的解决方案