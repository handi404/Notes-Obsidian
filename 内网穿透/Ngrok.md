
### 为什么要内网穿透？

我们的[个人电脑](https://so.csdn.net/so/search?q=%E4%B8%AA%E4%BA%BA%E7%94%B5%E8%84%91&spm=1001.2101.3001.7020)都处于内网，即没有可公开访问的独立 IP 地址，因此其他内网用户无法找到你，就没办法和你建立网络连接。

### 什么是内网穿透？

将内网的电脑 “暴露” 到公共网络。可以简单理解为一个中间人，由于他知道你的电脑[地址](https://so.csdn.net/so/search?q=%E5%9C%B0%E5%9D%80&spm=1001.2101.3001.7020)，所以能建立一条隧道，帮助其他用户访问到你的计算机。

### 实现内网穿透

实现内网穿透的工具很多，本博客使用Ngrok进行教程

[Download](https://ngrok.com/download "Download")

1、首先登录自己的账号，可以直接使用GitHub进行注册登录

![](https://img-blog.csdnimg.cn/img_convert/4a2928fff16671292a03a1f67f967c76.png)

2、登陆注册后根据自己的操作系统选择安装

![](https://img-blog.csdnimg.cn/img_convert/782f669182515d5f4e1fae24bb02fbd7.png)

![](https://img-blog.csdnimg.cn/img_convert/bb9f1ab4fb5d26aaaf6f4101629ffe07.png)

3、将下载好的压缩包解压到自己想要的目录，通过cmd进入该目录（目录下只有一个exe文件，不需要双击运行什么的）

![](https://img-blog.csdnimg.cn/img_convert/e21b63f083d506e221cd9a8c47a1ccef.png)

4、复制官方提供的代码，在命令窗口执行，添加用户token

![](https://img-blog.csdnimg.cn/img_convert/e5947bf49060db7898c0f7ed6fbc2453.png)

5、执行完毕后就可以使用以下代码进行穿透了，同样在也是在命令终端进行

```undefined
ngrok http 端口号
```

我这里开放8101端口，效果如下，分享https链接给其他内网用户，他们就可以直接访问你的网站了

![](https://img-blog.csdnimg.cn/img_convert/00c1b62614a2023b0478330a41c7b310.png)

\*6、固定域名（选做）

为了避免每次都要切换域名，ngrok提供了固定域名，初次直接申请创建即可

创建：

![](https://img-blog.csdnimg.cn/direct/c882929d7fbb4c85b5090f376306895a.png)

查看：

![](https://img-blog.csdnimg.cn/img_convert/425e30943a7cea882b5ddda7bf9a589a.png)

域名创建好后复制代码运行即可，代码最后带着端口号哦！！！

```cobol
ngrok http --doman************ 端口号
```

![](https://img-blog.csdnimg.cn/img_convert/4e62a702ae1dd8c5c4cd660badc33775.png)
