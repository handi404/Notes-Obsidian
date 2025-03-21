## [Keycloak](https://so.csdn.net/so/search?q=Keycloak&spm=1001.2101.3001.7020)介绍

Keycloak是一个开源的[身份认证](https://so.csdn.net/so/search?q=%E8%BA%AB%E4%BB%BD%E8%AE%A4%E8%AF%81&spm=1001.2101.3001.7020)和授权管理系统，它提供了一系列的功能，包括单点登录（SSO）、身份验证、社交登录、用户管理、角色管理、权限管理等等。Keycloak可以被集成到各种应用程序中，包括Web应用程序、移动应用程序和服务。它支持OpenID Connect、OAuth 2.0、SAML 2.0标准协议，拥有简单易用的管理控制台，并提供对LDAP、Active Directory以及Github、Google等社交账号登录的支持，做到了非常简单的开箱即用。

Keycloak近两年更新非常频繁，功能和bug也在逐步修复和完善。他在国外非常受欢迎，但在国内相关资料非常少。故此，留下一些内容，希望能够帮助到后来的人。

[Keycloak官网](https://www.keycloak.org/)

### Keycloak 服务搭建与启动

本文使用环境和版本号如下：

> Keycloak 版本：22.0.1 （目前最新版本，其他版本大同小异）  
> 平台：Windows 11  
> 数据库： PostgreSQL 15.3 64-bit

### 设置Keycloak配置文件

从官网下载keycloak压缩包后直接解压，得到文件如下：  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b3fb803b5a661cdef3dbbf3c91ef305a.png)

打开`./conf/keycloak.conf` 文件，修改默认配置：  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/758cd163a6b2db84b798df93e4a66472.png)

主要的配置及说明如下：

```bash
# 设置数据库类型，这里用的是postgres
db=postgres
# 设置数据库的用户名
db-username=postgres
# 设置数据库用户密码
db-password=admin
# 设置数据库连接地址及实例
db-url=jdbc:postgresql://localhost/keycloak
# 设置服务端口（不写的时候，使用默认端口8080）
http-port=8080

12345678910
```

### 创建空的数据库

Keycloak首次启动时会自动创建数据库表和基础数据，但仍然有个前提：在启动之前需要我们手动创建一个空的Database，供keycloak使用。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/e8e2675c206f861fbc4ee7299de61d84.png)  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a344caacdeb6aef93441bf89c9336dac.png)  
这里我们创建一个名为 `keycloak` 的空数据库，点击`Save`即可。

### 启动Keycloak服务

启动 Windows PowerShell ，将路径cd到 `\bin\`文件夹中，输入命令（开发模式）：

```shell
kc.bat start-dev

1
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/dda6452d171eee12758d626959b81e0e.png)

输出如下信息后，表示启动成功。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b3ac088ba6a3531f63b2da79fdd8bad5.png)

接下来打开浏览器，输入 `http://localhost:8080/`，会看到 Keycloak 界面。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/25cfd8f3c9b6d15c50d163734fcb5a4b.png)

初次启动服务时，需要先注册一个管理员账号，作为超级管理员，管理整个Keycloak。  
输入用户名、密码后，点击`Create`，完成创建。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8836f5e3f38247f20b8ae25ebea4570e.png)  
创建成功后，点击 `Administration Console `，登录用户，进入Keycloak控制台。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7e2efc8f1b23cb4cc8ac3cccaa0fd57d.png)

Keycloak控制台如下：  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9869494081860558222c19f9b3368450.png)

至此，Keycloak 服务搭建与启动完成。让我们反过来看一看数据库里的内容。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/12a266757defd597bd72489c28447a8e.png)  
这些就是Keycloak启动时，自动创建的表和基础数据。

好了，以上就是关于Keycloak服务的搭建与启动。

后续会介绍Keycloak与Spring 项目的结合。