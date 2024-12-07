
**目录**

[前言](https://blog.csdn.net/weixin_47698656/article/details/139704477?spm=1001.2014.3001.5506#%E5%89%8D%E8%A8%80%E5%BF%85%E8%AF%BB)

[构建项目](https://blog.csdn.net/weixin_47698656/article/details/139704477?spm=1001.2014.3001.5506#t0)

___

## 前言

在创建Spring Boot项目时，为了确保项目的顺利构建和运行，我们依赖于JDK（[Java开发工具包](https://so.csdn.net/so/search?q=Java%E5%BC%80%E5%8F%91%E5%B7%A5%E5%85%B7%E5%8C%85&spm=1001.2101.3001.7020)）和Maven仓库。

JDK作为Java编程的基础，提供了编译和运行Java应用程序所需的核心类库和工具。

> JDK安装配置教程：
> 
> ps：不同的JDK版本，除下载安装包不同外，其余均可参考该教程
> 
> [JDK8卸载、下载、安装、配置-Windows篇](http://t.csdnimg.cn/p5oOx "JDK8卸载、下载、安装、配置-Windows篇")

[Maven仓库](https://so.csdn.net/so/search?q=Maven%E4%BB%93%E5%BA%93&spm=1001.2101.3001.7020)则是Java项目的依赖管理工具，它帮助我们自动下载和管理项目所需的第三方库和框架。

> Maven仓库安装配置教程：
> 
> [Maven下载、安装、配置教程（超详细+配置idea）](http://t.csdnimg.cn/T16RA "Maven下载、安装、配置教程（超详细+配置idea）")

本文章基于Spring Boot 2.x 版本创建项目，通常建议使用的 JDK（Java Development Kit）版本是 JDK 8、JDK 11 或更高版本，具体取决于 Spring Boot 的子版本。以下是 Spring Boot版本与推荐的 JDK 版本的对应关系：

> Spring Boot 2.0.x：推荐使用 JDK 8。  
> Spring Boot 2.1.x：推荐使用 JDK 8 或 JDK 11。  
> Spring Boot 2.2.x：推荐使用 JDK 8 或 JDK 11。  
> Spring Boot 2.3.x：推荐使用 JDK 8、JDK 11 或 JDK 14。  
> Spring Boot 2.4.x：推荐使用 JDK 8、JDK 11 或 JDK 15。  
> Spring Boot 2.5.x：推荐使用 JDK 8、JDK 11 或 JDK 16。  
> Spring Boot 3.x 使用 JDK17。

### 构建项目

1、在IDEA中创建springboot项目如图，依次点击左上角的菜单栏中的File >> New >> Project。

![](https://i-blog.csdnimg.cn/blog_migrate/d16792446a0494249a8de0a13cfbb086.png)

2、打开的窗口左侧选择Spring Initilizer，然后按照提示输入项目名称、存储路径、选择相应的JDK版本、开发语言以及打包方式，完善好上述项目信息，然后点击Next按钮。

-   Server URL：用于初始化Spring Boot项目的服务器的地址，它负责根据用户的选择生成项目结构，此处建议跟作者保持一致使用国内镜像源[https://start.aliyun.com/](https://start.aliyun.com/ "https://start.aliyun.com/")。

> 在创建Spring Boot项目时，Spring Initializr工具需要从一个服务器获取项目模板和依赖信息。这个服务器的地址就是Server URL。Spring Initializr默认使用官方的Server URL（如https://start.spring.io/），但也可以配置为使用其他服务器，例如国内的镜像源（如阿里云镜像仓库https://start.aliyun.com/），以提高下载速度和稳定性。

-   Name：Springboot项目名，自定义即可
-   Location：项目存放位置，自定义即可
-   Language：开发语言，选择Java
-   Type：构建项目的方式，选择Maven
-   Group：项目的groupId，自定义即可

> groupId是项目组织唯一的标识符，它实际对应Java的包结构，即Java的目录结构。
> 
> groupId通常遵循反向域名的命名规则，比如com.example，其中com是域，而example是组织或项目的名称。这种命名方式有助于确保groupId在全球范围内是唯一的。

-   Artifact：应用程序的名称或者是项目的模块名，自定义即可

> Artifact是Maven管理项目包时用作区分的字段之一，类似于地图上的坐标点，用于唯一标识一个项目或模块。在创建Spring Boot项目时，Artifact ID是必填项，并且需要确保它在全局范围内是唯一的，特别是当项目需要上线时。
> 
> Artifact ID与项目的功能或用途相关，例如，一个用于用户管理的模块可能命名为user-management。

-   Package name：软件包名，自定义即可

> Package name（包名）指的是Java项目的包结构，它用于组织项目的类和接口。包名是Java命名空间的一部分，通过包名，我们可以避免类名冲突，并且可以清晰地表示类之间的隶属关系或层次关系。
> 
> 包名由多个部分组成，每部分之间用点号.分隔。例如，一个典型的包名可能是com.example.myproject，其中com是顶级域名，example是组织或公司的名称，而myproject是项目的名称或标识。

-   JDK：Java开发工具包，根据个人电脑安装JDK版本选择，作者使用JDK1.8

> 它是Java开发人员用来开发Java应用程序的核心工具集。JDK包含了Java运行时环境（JRE）、Java编译器（javac）、Java文档生成器（javadoc）等工具，同时还包含了许多Java开发所需的库和工具。这些工具可以帮助开发人员编写、编译、调试和运行Java程序。

-   Java：项目将使用的编程语言版本，作者使用8版本
-   Packaging：项目打包方式，作者使用Jar

> 选择打包方式时，你应该考虑你的项目需求以及你打算如何部署和运行你的应用程序。对于大多数独立的 Spring Boot 应用程序来说，JAR 是一个方便且推荐的选择，因为它简化了部署过程，并允许应用程序在任何支持 Java 的环境中运行。

![](https://i-blog.csdnimg.cn/blog_migrate/6896a2a3609e372007c1075614a62508.png)

3、选择springBoot的版本，在依赖管理部分，选择“Web”下面的“Spring Web”以及其他所需的依赖，然后点击Create按钮。

![](https://i-blog.csdnimg.cn/blog_migrate/268f4def1405857b487dc8bda90a692d.png)

4、创建完成，等待依赖下载完成后，IDEA会自动创建SpringBoot项目，并配置好基本的目录结构和文件，点击src >>main >>java >>包名，里面找到自动生成的主类然后右键点击运行，查看控制台输出的日志，无报错信息即可。

![](https://i-blog.csdnimg.cn/blog_migrate/4639746daa7b10d5e42c1535250816bf.png)

至此我们的SpringBoot项目就创建成功！！！

> ps：后续作者会持续更新Springboot集成各类技术框架，如感兴趣关注点一下！！！