1.  每一次创建新的Servlet类或则是对servlet类进行了修改都要重启tomcat才可以执行修改或则添加之后的servlet类。如果不重启服务则会在网页报404的错误，如图。![](https://i-blog.csdnimg.cn/blog_migrate/a474417ef04553c131897faa69480df7.png)
2.  首先在tomcat的服务之中的选择设置为 Updata and classes resources 如图;

![](https://i-blog.csdnimg.cn/blog_migrate/52def924d50868ceca348172dbe5a19e.png)

其次在tomcat的conf文件下找到context.[xml文件](https://so.csdn.net/so/search?q=xml%E6%96%87%E4%BB%B6&spm=1001.2101.3001.7020)，再找到其中的<context>标签

修改前                                   修改后

<Context reloadable\="true"\>           <Context reloadable\="true"\>

……                                         ……

……                                         ……

……                                         ……

<Context>                               <Context>

修改之后再次重启tomcat即可。

1.  每次对Servlet类进行添加、修改等操作时，只需按着以下的步骤来即可。如图：![](https://i-blog.csdnimg.cn/blog_migrate/431a75700ff8605a2e6bb48ad1f6c88e.png)

其中再Deployment已进行更新的项目点击小红色的方块，即可不用重启tomcat即可让项目进行更新的操作。

1.  或则更加简便的方法是：先点击删除的按钮让其配置文件再次运行，以及删除上一次的所运行时所显示的信息，如图：红色部分为运行时所产生的信息，需要点击的是黑色的部分![](https://i-blog.csdnimg.cn/blog_migrate/85f64615fe087a93fc02c0d03068fd6f.png)

其次在：直接点击电脑下的任务栏，让项目直接进行更新操作如图：(点击红色的部分，让build跑完即可)