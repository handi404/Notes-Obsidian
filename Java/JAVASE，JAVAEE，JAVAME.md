一、三个版本

Java是一门[编程语言](https://so.csdn.net/so/search?q=%E7%BC%96%E7%A8%8B%E8%AF%AD%E8%A8%80&spm=1001.2101.3001.7020)。Java分为三大版本

SE即标准版，包含了[Java核心类库](https://so.csdn.net/so/search?q=Java%E6%A0%B8%E5%BF%83%E7%B1%BB%E5%BA%93&spm=1001.2101.3001.7020)，主要用来开发桌面应用；

EE即企业版，包含SE，又有扩展部分（[Servlet](https://so.csdn.net/so/search?q=Servlet&spm=1001.2101.3001.7020)，JDBC等），主要用来开发分布式网络程序；

ME即微型版，包含了SE中部分类库，又有自己扩展部分，主要用来做移动类、嵌入式开发。

二、版本之间的区别

Java SE（Java Platform，Standard Edition），Java标准版，就是一般Java程序的开发就可以(如桌面程序)，可以看作是JavaEE的子集。Java SE 以前称为 J2SE。

它允许开发和部署在桌面、服务器、嵌入式环境和实时环境中使用的 Java 应用程序。Java SE 包含了支持 Java Web 服务开发的类，并为 Java Platform，Enterprise Edition（Java EE）提供基础。

包含了

1.  面向对象
2.  多线程
3.  IO流
4.  javaSwing等

![](https://i-blog.csdnimg.cn/blog_migrate/cfb0604e37b22e5d08a15efc4737d715.png)

![](https://i-blog.csdnimg.cn/blog_migrate/4f7e7da35af50beb82b8639c6b4b7ce1.png)

Java EE（Java Platform，Enterprise Edition）。这个版本以前称为 J2EE。Java EE，Java 平台企业版（Java Platform Enterprise Edition） 之前称为Java 2 Platform, Enterprise Edition (J2EE) 2018年3月更名为 JakartaEE(这个名称应该还没有得到群众认可)。 是 Sun 公司为企业级应用推出的标准平台，用来开发B/S架构软件

企业版本帮助开发和部署可移植、健壮、可伸缩且安全的服务器端 Java 应用程序。javaSE是java的基石，如果将java程序想象成一座高楼大厦，那么javaSE就是地基。Java EE 是在 Java SE 的基础上构建的，它提供 Web 服务、组件模型、管理和通信 API，可以用来实现企业级的面向服务体系结构（service-oriented architecture，SOA）和 Web 2.0 /3.0应用程序。

![](https://i-blog.csdnimg.cn/blog_migrate/bdee9617b7502bce5f0dbd795237464a.png)

![](https://i-blog.csdnimg.cn/blog_migrate/9b3ef085fbd5ac3a8561b6537b9d2840.png)

JavaME即微型版，也是以Java为基础的，之前称为 J2ME。

它是一套运行专门为嵌入式设备设计的api接口规范，主要用于开发移动设备软件和嵌入式设备软件，例如：手机游戏，电视机顶盒和打印机相关的嵌入式设备软件。

三、JavaWeb知识体系

Java web 是指有Java语言开发出来可以在万维网上访问浏览的程序

JavaEE在JavaSE的基础进行了扩展，增加了一些更加便捷的应用框架。比如我们现在常用的Java开发三大框架Spring、Struts和Hibernate，我们可以应用这些框架轻松写出企业级的应用软件。

Java EE也可以说是一个框架也是一种规范，说它是框架是因为它包含了很多我们开发时用到的组件，例如：Servlet，JSP，JSTL等；说它是规范因为我们开发web应用常会用到的一些规范模式，JavaEE提供了很多规范的接口却不实现，将这些接口的具体实现细节转移到厂商身上，这样各家厂商推出的JavaEE产品虽然名称实现不同，但展现给外部使用的却是统一规范的接口。

![](https://i-blog.csdnimg.cn/blog_migrate/3142b6914c2af87c299a315d1950e1fb.png)

![](https://i-blog.csdnimg.cn/blog_migrate/5a34518e7b87c331d751359dc20b809a.png)