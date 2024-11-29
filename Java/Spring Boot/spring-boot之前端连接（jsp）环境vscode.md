

如果想要springboot设置前端页面，你需要在pom.xml里加上以下依赖：

```cobol
<!-- 引入SpringBoot内嵌Tomcat对jsp的解析依赖，不添加这个解析不了jsp -->
     <!-- servlet依赖 -->
     <dependency>
<groupId>javax.servlet</groupId>
<artifactId>javax.servlet-api</artifactId>    
</dependency>
<dependency>
<groupId>javax.servlet</groupId>
<artifactId>jstl</artifactId>
</dependency>
<!-- tomcat的支持-->
<dependency>
 <groupId>org.apache.tomcat.embed</groupId>
 <artifactId>tomcat-embed-jasper</artifactId>
</dependency>     
```

依赖搞完了，那你想springboot项目怎么知道你把.jsp文件（前端页面）放哪儿呢？所以你要在配置文件application.properties里加上视图解析器：

```csharp
#配置视图解析器

#告诉springboot你的jsp文件放在哪里

spring.mvc.view.prefix=/

#告诉springboot你的文件长啥样

spring.mvc.view.suffix=.jsp
```

OK，上面都搞完了。

那你现在要尝试将前后端链接在一起了。我这里简单写一个demo（没有实体类了），在前端输出此时此地的时间。

首先，你得有个控制器吧。在你的springboot中新建一个controllers文件专门用来放controller（控制器），然后你在里面新建一个Mycontroller.java文件，这里控制器的作用就是能把现在的时间传给前端

![](https://i-blog.csdnimg.cn/blog_migrate/cda8fdf8c786d6547bc177ac9450a061.png)

 然后开始码代码，你直接看注释吧

提前说一下，这里标注控制器的时候用@controller而不要用@Restcontroller

为啥呢：

![](https://i-blog.csdnimg.cn/blog_migrate/10d5d67bcbe6a650fa0fa57e6d1498e2.png)

Mycontroller.java

```java
import java.text.DateFormat;
import java.util.Date;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller // 表示这个类是控制器,不要用Restcontroller
@RequestMapping("/cnm") // 意味着你的路由由/cnm开始
public class Mycontroller {
    @RequestMapping(value="/wc")//意味着你的路由由/cnm/wc开始
    public String test (Model m) {
        m.addAttribute("now",DateFormat.getDateTimeInstance().format(new Date()));
        //这边我也不是很懂，反正就是传个键值对 now：时间，待会前端可以调用now,你就当这是个把后端传到前端的方式吧
 
        return "test";//视图定向到test.jsp,这里可不是字符串啊！
      }
  
}
```

然后你在springboot项目中新建一个webapp文件夹，专门用来放jsp文件。![](https://i-blog.csdnimg.cn/blog_migrate/74b891a63ada4db46b289bd4d0fa4fd4.png)

 然后在该文件夹下新建一个test.jsp，注意这里的xxx.jsp取决于你刚刚在controller里的return值

test.jsp

```cobol
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的测试前端页面</title>
</head>
<body>
    ${now}
    <!-- ${now}打印时间，now在controller里面写过，可以去看 -->
    <hr>
 
</body>
</html>
```

最后的最后，去命令行 mvn spring-boot:run

然后在你的浏览器访问下你刚刚建的路由：

![](https://i-blog.csdnimg.cn/blog_migrate/109ad2028b63ea16d492fc3d92642f6e.png)

 成功！