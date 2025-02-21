> 提示：文章写完后，目录可以自动生成，如何生成可参考右边的[帮助文档](https://so.csdn.net/so/search?q=%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3&spm=1001.2101.3001.7020)

#### 文章目录

-   [前言](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#_7)
-   [一、各个常用层级简述](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#_15)
-   -   [1.POJO层](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#1POJO_16)
    -   [2.DAO层](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#2DAO_22)
    -   [3.SERVICE层](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#3SERVICE_88)
    -   [4.CONTROLLER层](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#4CONTROLLER_103)
    -   [5.mapper层](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#5mapper_108)
-   [二、其余包/层级](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#_111)
-   -   [1.ENUM 枚举包](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#1ENUM__112)
    -   [2.VO 返回对象包](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#2VO__132)
    -   [3.EXCEPTION 报错包](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#3EXCEPTION__136)
    -   [4.FORM 表单包](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#4FORM__140)
    -   [5.拦截器](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#5_156)
-   [三、各个层级之间的联系与作用](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#_188)
-   -   [三层架构](https://blog.csdn.net/ZHI_YUE/article/details/132048647?spm=1001.2014.3001.5506#_189)

___

## 前言

在SpringBoot项目中，常常会把代码文件放入[不同的](https://so.csdn.net/so/search?q=%E4%B8%8D%E5%90%8C%E7%9A%84&spm=1001.2101.3001.7020)包中，例如pojo，dao，service，controller等，但各个层级的代码是如何联系起来的呢，又会在项目中起到怎样的作用呢

___

`提示：以下是本篇文章正文内容，下面案例可供参考`

## 一、各个常用层级简述

### 1.POJO层

POJO在springboot项目中的定位，类似于mvc项目里的model模型层。  
POJO（Plain Ordinary Java Object）简单的Java对象，实际就是普通JavaBeans，其中包含多个的属性，同时具备get/set方法，推荐直接使用@lombok注解。  
POJO不会在其中编写逻辑方法，而且  
与数据库表一一对应，属性也需与数据库表的字段保持一致。

### 2.DAO层

DAO层用来存放mapper接口，mapper作用为访问数据库，向数据库发送sql语句，完成数据的增删改查功能，通常将其实现为接口，内部声明的方法将会于mapper层中的对应数据库函数关联

其有两种编写方式  
（1）继承 BaseMapper，BaseMapper 接口是 MyBatis Plus 提供的通用 Mapper 接口，用于执行常用的 CRUD 操作，包括插入、更新、删除和查询等操作，继承该接口后，能够自动获得数据库常用操作的方法，而不用在mapper内编写

```java
public interface UserMapper extends BaseMapper<User>{
    
}
```

（2）通过终端调用命令mvn mybatis-generator:generate  
需要安装maven和调用mybatis相关依赖，在使用该命令时，会检索generatorConfig.xml文件，并根据其中配置，自动生成DAO层，以及pojo和mapper层的基本文件。  
注：需求可通过更改xml配置修改，也可生成后再新编写逻辑方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>

    <classPathEntry location="E:\software\IDEA\mysql-connector-java-8.0.27.jar" />

    <context id="DB2Tables" targetRuntime="MyBatis3">

        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin"/>
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/mall?characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=GMT%2b8"
                        userId="root"
                        password="root">
            <!--MySQL 8.x 需要指定服务器的时区-->
            <property name="serverTimezone" value="UTC"/>
            <!--MySQL 不支持 schema 或者 catalog 所以需要添加这个-->
            <!--参考 : http://www.mybatis.org/generator/usage/mysql.html-->
            <property name="nullCatalogMeansCurrent" value="true"/>
            <!-- MySQL8默认启用 SSL ,不关闭会有警告-->
            <property name="useSSL" value="false"/>
        </jdbcConnection>

        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <javaModelGenerator targetPackage="com.imooc.mall.pojo" targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="mappers"  targetProject="src/main/resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <javaClientGenerator type="XMLMAPPER" targetPackage="com.imooc.mall.dao"  targetProject="src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <table tableName="数据库表名" domainObjectName="想要生成的pojo类名" enableSelectByExample="false"
                       enableCountByExample="false" enableDeleteByExample="false" enableUpdateByExample="false"/>
    </context>
</generatorConfiguration>

```

### 3.SERVICE层

业务逻辑层，完成功能的设计 和dao层一样都是先设计接口，再创建要实现的类，然后在配置文件中进行配置其实现的关联。  
service的impl是把mapper和service进行整合的文件 封装Service层的业务逻辑有利于业务逻辑的独立性和重复利用性。  
基本功能为：

1.  处理业务逻辑：业务逻辑是处理数据、计算等复杂操作的过程，Service层是业务逻辑的核心，负责编写和实现业务逻辑。
    
2.  组织DAO（Data Access Object）层：数据访问对象是用来访问数据库等数据存储的代码层，Service层通常会调用DAO层的方法来处理数据。
    
3.  实现事务控制：在Service层中实现事务控制，确保操作数据库的过程中数据的一致性和完整性。
    
4.  封装业务对象：Service层会封装业务逻辑需要的数据，并将其传递给DAO层进行存储或操作，这样做可以提高代码的可读性和可维护性，以及规范数据的操作。
    

注意：在service的实现类上要加注解@Service，否则会出现无法扫描识别

### 4.CONTROLLER层

控制层，控制业务逻辑service，控制请求和响应，负责前后端交互  
controller层主要调用Service层里面的接口控制具体的业务流程，不会在其中编写大量逻辑代码，同时也会接受并处理一些HTTP参数，例如session

注意：在CONTROLLER的实现类上也要加注解@RestController

### 5.mapper层

存放数据库函数，与DAO层中的方法映射，可通过终端命令生成，也可自行编写（工作量较大），在调用DAO方法则会实际执行mapper层对应的数据库方法，是对数据库curd的接口桥梁，同时也需要增加mapper扫描以完成识别

## 二、其余包/层级

### 1.ENUM 枚举包

在真正项目编写中，通常会遇到根据不同情况设定不同返回值，而直接大量设置int不仅会存在安全漏洞，也会出现阅读及理解上的困难，所以通过枚举可以很好的解决这些问题

```java
@Getter
public enum ProductStatusEnum {
    ON_SALE(1),
    OFF_SALE(2),
    DELETE(3),
    ;
    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }

}
```

其中@Getter注解不可缺少，因为需要取枚举值，也就是取code时，需要调用其提供的getCode()方法

### 2.VO 返回对象包

由于springboot项目一般采用前后端分离，不同层不同方法返回给前端的数据的格式有时会与POJO中的属性不完全一致，可能增多也可能缺少，于是直接构建一个用于构造返回对象的包，根据需求出发，与前端所需数据类型保持完全一致。  
VO类的结构完全类似于POJO类，同样存放一定量的属性+lombok注解(@Data)即可

### 3.EXCEPTION 报错包

由于项目中的一些报错并不是逻辑性报错，在正常运行项目中可能会存在不危险的报错，并需要对其按报错类型做不同处理  
通过@ExceptionHandler(RuntimeException.class)注解，更改括号内的报错类名，捕捉不同的报错，并编写对应的报错处理方法。

### 4.FORM 表单包

有时在项目中对传入的数据对象的参数进行校验，通过对其添加 注解 即可  
注解包为javax.validation.constraints，需要引入依赖  
@Null 被注解的元素必须为null  
@NotNull 被注解的元素必须不为null  
@AssertTrue 被注解的元素必须为true  
@AssertFalse 被注解的元素必须为false  
@Min(value) 被注解的元素必须为数字，其值必须大于等于最小值  
@Max(value) 被注解的元素必须为数字，其值必须小于等于最小值  
@Size(max,min) 被注解的元素的大小必须在指定范围内  
@Past 被注解的元素必须为过去的一个时间  
@Future 被注解的元素必须为未来的一个时间  
@Pattern 被注解的元素必须符合指定的正则表达式

### 5.拦截器

拦截器的主要是基于Java的反射机制，属于面向切面编程(AOP)的一种运用，就是在Service或者一个方法前调用一个方法，或者在方法后调用一个方法，甚至在抛出异常的时候做业务逻辑的操作。

拦截器的作用类似于Servlet 中的Filter，都可以用于对处理器进行预处理和后处理。在[Spring MVC](https://so.csdn.net/so/search?q=Spring%20MVC&spm=1001.2101.3001.7020) 与Spring Boot 中使用拦截器一般是实现HandlerInterceptor 接口

该接口有三个方法  
（1）preHandle()：这个方法可以实现处理器的预处理，也就是它会在handler 方法执行之前就开始执行。当返回值是true 时表示继续执行，返回false 时则不会执行后续的拦截器或处理器。作用：身份验证，身份授权等。  
（2）postHandle()：这个方法是后处理回调方法，也就是在控制器完成后(试图渲染之前)执行。作用：将公用的模型数据传到视图，也可以在这里统一指定视图(菜单导航等)。  
（3）afterCompletion()：这个方法是请求处理完毕后的回调方法，即在视图渲染完毕时调用。作用：进行统一的异常处理，日志处理等。

实现接口后对需要的方法进行重写，编写项目所对应的拦截器逻辑代码。但有时候部分链接需要对外部开放，而不能被拦截器所拦截，例如登录接口，注册接口等，需要另外编写一个类 实现 WebMvcConfigurer接口

```java
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginIntercepter())
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/user/login", "/user/register");
    }
}
```

addInterceptor：需要一个实现HandlerInterceptor接口的拦截器实例  
addPathPatterns：用于设置拦截器的过滤路径规则；addPathPatterns(“/\*\*”)对所有请求都拦截  
excludePathPatterns：用于设置不需要拦截的过滤规则（白名单）  
注意 @Configuration注解不可省略，不然拦截器可能配置无效

此处只讲解 WebMvcConfigurer接口在拦截器的运用，它还有很多其他的功能，想了解的可以看看这篇博客  
_https://blog.csdn.net/weixin\_45433031/article/details/121846207_

## 三、各个层级之间的联系与作用

### 三层架构

从基础的三层架构开始说起吧，三层架构由 Dao 层，Service层，Controller层组成,在这个三层架构之中,Dao层负责与mybatis和数据库打交道,实现对持久化数据的访问，隔离业务逻辑代码和数据访问代码，隔离不同数据库的实现。

而Service则是负责编写业务逻辑的一个层,一般Service层由接口和实现类构成,存在大量业务方法，以供Controller层的调用。在SpringBoot项目中，编写业务逻辑时，常常会声明一个由@Autowired 注释的XXXmapper对象，该对象为Dao层接口的实现类，以供其调用。

Controller，控制器层，controller层的功能为请求和响应控制。  
Controller层负责前后端交互，接受前端请求，调用service层，接收service层返回的数据，最后返回具体的页面和数据到客户端。请注意:部分返回数据队格式和数量都有所要求,要注意前端需求并构建响应的Vo对象返回为佳

> 数据持久化：将数据存入数据库中，将数据库中的数据读取出来的过程，称之为持久，持久化是将程序中的数据在瞬时状态和持久状态间转换的机制