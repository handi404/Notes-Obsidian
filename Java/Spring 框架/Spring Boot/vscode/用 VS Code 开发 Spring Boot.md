
对于VS Code情有独钟。于是尝试了一下在Windows平台的VS Code中搭建Spring Boot的开发环境。  

### 必要准备

Spring Boot属于Java框架，属于Java开发的必要条件，要先安装：

-   安装JDK
    

这一步，我们直接在Oracle官网下载最新的JDK安装即可：

![](https://i-blog.csdnimg.cn/blog_migrate/1d84324972d3d11dfd1689bb81171698.png)

然后双击安装，多次下一步之后，完成安装。

安装完成之后，记得配置一下环境变量（不知道为什么JDK的这个环境变量，一直不是自动配置的）：

![](https://i-blog.csdnimg.cn/blog_migrate/48193da0ad1559034de5aeba8bfd6861.png) ![](https://i-blog.csdnimg.cn/blog_migrate/79bb84212b5af6ab34b6baf8e6dd3bbd.png)

当你的 VS Code 遇上 Spring Boot

配置好环境变量之后，在控制台中，执行命令来检查是否安装成功：

```go
java -version
```

![](https://i-blog.csdnimg.cn/blog_migrate/c94a97ecd73fb26ad20b594a1a7827af.png)

如果执行以上命令，成功看到Java的版本号，恭喜您，Java JDK安装成功。

___

-   安装maven
    

maven作为Java界最流行的项目管理工具，在Spring Boot开发中，也是日常必须品，需要下载。

我们这里选择提前编译好的zip二进制包即可：

![](https://i-blog.csdnimg.cn/blog_migrate/4a015da54d437777d6c67acdbe74b53f.png)

开载好maven在包之后，解压出来，找一个自己喜欢的位置，保存即可。记住您保存的位置，因为后面我们需要在VS Code中，配置maven相关的参数。

![](https://i-blog.csdnimg.cn/blog_migrate/7fc4c28fe71f6dfc474b3832f948bb85.jpeg)

maven

___

-   安装VS Code
    

![](https://i-blog.csdnimg.cn/blog_migrate/8a406bcb35505f8d6310878104f63184.jpeg)

### 安装必要插件

现在打开VS Code,安装下面两个插件：

```go
Java Extension Pack

Spring Boot Extension Pack
```

![](https://i-blog.csdnimg.cn/blog_migrate/9f2d6561b038477ddfcb4fd6aa757d78.png)

Java Extension PackJava Extension Pack 安装完成

![](https://i-blog.csdnimg.cn/blog_migrate/fbda15f19e902678ff078a7721584db9.png)

Spring Boot Extension Pack 安装完成

### 配置maven选项

现在还不能进行开发，还需要进行一些配置，不要着急：

![](https://i-blog.csdnimg.cn/blog_migrate/230bcea8da0a7489da936b6a209bc0fa.png)

配置maven的settings.xml路径

为了提升maven的访问速度，我们修改其源为 aliyun maven ：

![](https://i-blog.csdnimg.cn/blog_migrate/87225029617214fb22b7b85b05309a75.png)

修改maven的镜像源为aliyun

maven的settings.xml中的局部代码如下：

```go
    <mirror>

        <id>alimaven</id>

        <name>aliyun maven</name>

        <url>http:

        <mirrorOf>central</mirrorOf>

    </mirror>
```

![](https://i-blog.csdnimg.cn/blog_migrate/5d3ac33ff730033e60eb44f4b116007f.png)

配置maven的执行路径

好了，到这里，我们的环境配置就完成了。重启VS Code，下一步就可以进行开发了！

### 创建测试Demo项目

再次启动VS Code，按 Ctrl + Shift + P 快捷键，调出命令，输入spring，然后新建一个maven项目：

![](https://i-blog.csdnimg.cn/blog_migrate/233de42eec447d5cf0c71e210ac7c0ce.png)

新建maven项目

![](https://i-blog.csdnimg.cn/blog_migrate/2588a3d323eaf94e82205b887f23a73d.png)

选择Spring Boot版本

![](https://i-blog.csdnimg.cn/blog_migrate/2ecf04e7dc121075f6286d2ab33308c0.png)

选择语言

![](https://i-blog.csdnimg.cn/blog_migrate/6ab0753992fea416534c1e35f9942ba0.png)

输入Group Id

**注意：输入Group Id之后，敲回车键**

![](https://i-blog.csdnimg.cn/blog_migrate/86ff745439ca59974742d3570991aa82.jpeg)

输入Artifact Id

**敲回车键**

![](https://i-blog.csdnimg.cn/blog_migrate/d4750a98d04250d244a532204cfa6e69.jpeg)

选择包类型

![](https://i-blog.csdnimg.cn/blog_migrate/50b7e18b06510ffdbdab44681898ba79.jpeg)

选择Java版本

![](https://i-blog.csdnimg.cn/blog_migrate/09735890cac4b7c0f94799d71e2edd3d.png)

选择常用依赖包

**敲回车键**

依赖包功能简介：

```go
Spring Boot DevTools: 支持代码修改热更新，无需重启

Spring Web: 集成Tomcat SpringMVC

Lombok: 智能生成 setter getter toString等

Thymeleaf: 模板引擎
```

![](https://i-blog.csdnimg.cn/blog_migrate/988ea95577aab96702ff6684c9cae445.png)

选择保存位置

等待项目创建完成，然后打开：

![](https://i-blog.csdnimg.cn/blog_migrate/c6b1537c1e8bd046aefae38f9359eff3.jpeg)

打开项目

![](https://i-blog.csdnimg.cn/blog_migrate/dbd0eec2bca78a6bcda46eedfdddb821.png)

项目打开成功

如果看到上面的源代码结构，那么恭喜您，您成功了！

### 添加测试业务代码

项目创建成功，接下来，添加测试业务代码：

![](https://i-blog.csdnimg.cn/blog_migrate/c65b480c796609fc0ac0742e4686dddb.png)

添加Rest

代码如下 ：

```go
package alien.learn.ademo.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerRest{

    @RequestMapping("/test")
    public String testRest(){

		return "欢迎使用VS Code 和 Spring Boot";

    }

}
```

再添加一个html模板：

![](https://i-blog.csdnimg.cn/blog_migrate/3264f48c4274b9114d3066cfb8a8a60a.png)

代码：

```go
<!DOCTYPE html>

<html lang="ch">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>主页</title>

</head>

<body>

    <h1>Spring Boot Page!</h1>

    <p th:text="${title}"></p>

</body>

</html>
```

再添加一个Controller：

![](https://i-blog.csdnimg.cn/blog_migrate/c0a596fe498ee9f236596468e28022c2.png)

代码：

```go
package alien.learn.ademo.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

@Controller
public class TestController{

    @RequestMapping("/hello")
    public String getPage(HashMap<String, String> map){

		map.put("title", "欢迎来到Spring Boot!");
		return "/index";

    }

}
```

然后F5调试服务：

![](https://i-blog.csdnimg.cn/blog_migrate/e32fe007973321741fc2c41e9f227e77.png)

调试服务

然后在浏览器中尝试访问服务：

![](https://i-blog.csdnimg.cn/blog_migrate/32d39867a07649fb60a8623e1049e121.jpeg)

再访问一下hello:

![](https://i-blog.csdnimg.cn/blog_migrate/8b54d481e1bb89093879a9fd0160312d.jpeg)

