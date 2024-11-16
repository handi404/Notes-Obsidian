

## 一、概述

> **maven功能与python的pip类似。**
> 
> Apache Maven是一个用于[软件项目管理](https://so.csdn.net/so/search?q=%E8%BD%AF%E4%BB%B6%E9%A1%B9%E7%9B%AE%E7%AE%A1%E7%90%86&spm=1001.2101.3001.7020)和构建的强大工具。它是基于项目对象模型的，用于描述项目的构建配置和依赖关系。以下是一些关键的 Maven 特性和概念：
> 
> 1.  **POM（Project Object Model）：**Maven项目通过一个XML文件，通常称为`pom.xml`，来描述项目的元数据和配置信息。POM包含项目的基本信息、构建配置、[依赖关系](https://so.csdn.net/so/search?q=%E4%BE%9D%E8%B5%96%E5%85%B3%E7%B3%BB&spm=1001.2101.3001.7020)等。
>     
> 2.  **依赖管理：**Maven简化了项目的依赖管理。通过在`pom.xml`文件中声明依赖关系，Maven会自动下载所需的库并添加到项目构建路径中。
>     
> 3.  **生命周期和插件：**Maven定义了一组构建生命周期（Build Lifecycle），包括清理、编译、测试、打包、部署等阶段。每个生命周期包含一组阶段，而插件则用于执行这些阶段的任务。
>     
> 4.  **仓库（Repository）：**Maven使用本地和远程仓库来存储和获取构建所需的依赖。本地仓库存储在开发者本地机器上，而远程仓库通常是中央仓库（Central Repository）或其他自定义仓库。
>     
> 5.  **中央仓库：**Maven的中央仓库是一个集中的、可公共访问的仓库，包含了大量的[开源](https://edu.csdn.net/cloud/pm_summit?utm_source=blogglc)Java库和工具。Maven会自动从中央仓库下载依赖。
>     
> 6.  **插件体系结构：**Maven的插件体系结构允许开发者扩展和定制构建过程。插件可以提供额外的目标和任务，以满足特定项目的需求。
>     
> 7.  **多模块项目：**Maven支持多模块项目，允许将大型项目划分为若干个模块，每个模块都有自己的`pom.xml`文件。这种结构有助于组织和管理复杂的项目。
>     
> 8.  **约定优于配置：**Maven遵循“约定优于配置”的原则，通过定义一些默认的规则和标准目录结构，简化了项目的配置。这意味着，如果项目结构符合约定，很多配置可以省略。
>     
> 
> 总体而言，Maven是一个广泛使用的构建工具，它提供了一种简单的方式来管理项目的构建、依赖和发布。通过采用约定优于配置的理念，Maven使得项目构建过程更加标准化和易于维护。

___

## 🍀二、下载maven

> ********[maven官网](https://maven.apache.org/download.cgi "maven官网")******：**Maven – Download Apache Maven Windows下载apache-maven-3.9.5-bin.zip
> 
> **资源获取：关注公众号【科创视野】回复  maven安装包**

![](https://i-blog.csdnimg.cn/blog_migrate/91d7dbdecb678ba09ea6dcd120be2c4f.png)

___

## 🌷三、解压maven

比如我将其存放在C:\\Program Files\\Java\\maven

![](https://i-blog.csdnimg.cn/blog_migrate/fa1eb6f4a60d6d5b880b99890b5e4c0c.png)

___

## 🍁**四、配置**maven

### a 配置环境变量

打开环境变量，新建系统变量，设置如下：

![](https://i-blog.csdnimg.cn/blog_migrate/6b320749fb50e9755c523641644b093d.png)

在系统变量的Path路径下加入

![](https://i-blog.csdnimg.cn/blog_migrate/a0d189c8dbe7c5b40a14ca25914353d1.png)

检查是否配置完成

打开终端输入

```undefined
mvn -v
```

![](https://i-blog.csdnimg.cn/blog_migrate/be36cca7af528e515bc43616babab293.png)

**显示这样说明配置成功！**

### **b 配置镜像【可选，为了下载更快】**

**打开setting.xml**

![](https://i-blog.csdnimg.cn/blog_migrate/ee940aa5e7fb292484df3db5d6e7f728.png)

找到内容  
![](https://i-blog.csdnimg.cn/blog_migrate/0e83c0e1dc2bfcb3840ccba6e88e1198.png)  
将原来的内容修改成

```cobol
<mirrors>

<mirror>

<id>nexus-aliyun</id>

<mirrorOf>central</mirrorOf>

<name>Nexus-aliyun</name>

<url>https://maven.aliyun.com/nexus/content/groups/public</url>

</mirror>

</mirrors>
```

![](https://i-blog.csdnimg.cn/blog_migrate/1c1112422eb50732c3a41e627d5fe50b.png)

### **c 配置私服仓库【可选，为了确定jar包存储位置】**

**后下载的jar包都会放在该目录下。**

![](https://i-blog.csdnimg.cn/blog_migrate/ad50f1d0daeee117ffc6af70c6c7cf06.png)

```xml
<localRepository>D:\maven\mvnRespo</localRepository>
```

在D盘创建D:\\maven\\mvnRespo文件夹

![](https://i-blog.csdnimg.cn/blog_migrate/944a21d42b43c0a4d7d525dc3cea37f1.png)

___

## 🍁五**、常用**maven指令

> **1\. maven常用命令**  
> **命令                      描述**  
> mvn clean             对项目进行清理，删除target目录下编译的内容  
> mvn compile         编译项目源代码  
> mvn test                对项目进行运行测试  
> mvn package        打包文件并存放到项目的target目录下，打包好的文件通常都是编译后的                                  class文件  
> mvn install            在本地仓库生成仓库的安装包，可供其他项目引用，同时打包后的文件放                               到项目的target目录下  
