## 前言

对于程序原来说都听说过GitHub，GitHub有许多开源的的项目和一些前沿的技术。因为自己在刚刚开始使用Git把自己写的一些小dome放到GitHub上遇到许多的坑，这么长时间过去了，想对第一次使用[Git上传](https://so.csdn.net/so/search?q=Git%E4%B8%8A%E4%BC%A0&spm=1001.2101.3001.7020)代码做一下总结，以免使自己忘记。

## [安装Git](https://so.csdn.net/so/search?q=%E5%AE%89%E8%A3%85Git&spm=1001.2101.3001.7020)

1、安装Git，下载Git软件：https://git-[scm](https://so.csdn.net/so/search?q=scm&spm=1001.2101.3001.7020).com/downloads

![在这里插入图片描述](https://img-blog.csdnimg.cn/7a43ce623302444d8bacbdbf7700f01a.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
2、下载之后安装。

-   **这里提醒一下，git 安装可以直接 “Next” 下一步，直到安装完成！！！但我建议你还是看着我的教程安装。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/b3f66f850bb3488d8e07e95c6b3b3118.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

-   **接着，设置安装路径，点击 “Browse…” 选择安装 Git 到该文件夹，我建议选择 D 盘（非系统盘）。我这里演示用的是 C  
    盘。然后，点击 “Next” 进行下一步。**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/87d16ee2611d492ba1218b6268ad237c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)
-   **这里根据自己的需要选择，我已经把这些都翻译了。我演示就都默认了，然后 “Next” 下一步。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/fc0c5ddf19674509bf21481d6ace94f6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

-   **这里是询问你是否创建开始菜单，并设置名称。我这里不改变文本内容，直接 “Next” 下一步。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/7523bbcc6dc04ed59ff3ec7a9a8f1930.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

-   **这里是设置 Git 默认编辑器，我们这里直接下一步 “Next”。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/feccfd3064684a6f9fd91dc48acf92b1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

-   **调整新仓库中初始分支的名称，你希望 Git 在 "git init "之后给初始分支取什么名字？**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/7a1800ad65fa48f69e280f01671f1a23.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

这里有两种选择：

1）Let git decide（让git决定）  
2）Override the default branch name for new repositories（重写新存储库的默认分支名称）

我们在这里选择 第一种 默认的，然后点击 “Next” 进行下一步。

-   **这是调整您的PATH环境的设置**

![在这里插入图片描述](https://img-blog.csdnimg.cn/539ec61050414974820c3df68286ae19.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
这里有三种选择：

1）Use Git from Git Bash only (只在Git Bash中使用Git)  
2）Git from the command line and also from 3rd-party software (在命令行和第三方软件中使用Git)  
3）Use Git and optional Unix tools from the Command prompt (在命令提示符下使用Git和可选的Unix工具。)

我们这步选择第二项默认的，毕竟还是新手嘛~接着 “Next” 下一步。

-   **选择Https传输后台配置（新版本这里好像有改变）**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/b454c1ca579a48c9b4777f6d7bdc8e03.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
    这里有两种选择：

1）Use the OpenSSL library.（使用OpenSSL库。服务器证书将使用ca-bundle crt文件进行验证。）

2）Use the native Windows Secure Channel library. (使用本机Windows安全通道库。服务器证书将使用Windows证书库进行验证，这个选项也允许你使用公司内部的根CA证书，例如通过活动目录域服务分发。使用本机的Windows安全通道库服务器证书将使用Windows证书库进行验证，这个选项也允许你使用公司内部的根CA证书，例如通过活动目录域服务分发的证书。这个选项也允许你使用公司内部的根CA证书。例如通过Active Directory Domain Services。)

这里我们选择 第一项，接着 "Next"进行下一步。

-   **配置行尾转换，我们选择第一项（Windows推荐），接着 “Next” 下一步。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/51e27df5ebb04105b9bd75e132010891.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

-   **配置与Git Bash一起使用的终端仿真器**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/497e9e98610145bfa44938dec688ef65.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

这里有两种选择：

1）Use MinTTY (the default terminal of MSYS2) 使用MinTTY（MSYS2的默认终端）相对于控制台，MinTTY 有更好的字体显示效果，以及舒服的操作方式。

2）Use windows default console window （使用Windows默认的控制台窗口，这个想必大家都是用过吧，也就是常见的CMD窗口）

我们这里选择默认的第一项，然后点击 “Next” 进行下一步。

-   **选择git pull的默认行为**

![在这里插入图片描述](https://img-blog.csdnimg.cn/53dfb573e5664ec2b5e8ccf9425071b1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

这里有三种选择：

1）Default(fast-forward or merge) 默认(快进或合并)：这是git pull的标准行为：尽可能将当前分支快进到获取的分支，否则就创建一个合并提交。

2）Rebase 重设：如果没有 locacommits 要重设，则将当前分支重垒到获取的分支上，这相当于快进。

3）Only ever fast-forward 只有快进：快进到获取的分支。如果不可能，则失败。

这里我们也选择默认的第一项，然后 "Next"下一步。

-   **配置凭证助手**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/1cd3368872444ab4af22a133c6720c0d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

这里有三种选项：

1）Git Credential Manager Core （Git凭据管理器核心）  
2）Git Credential Manager （Git证书管理器）  
3）None （无，不需要凭证助手）

这里我们选择第一项，Git凭据管理器核心，然后 “Next”

-   **配置额外的选项**  
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/a50e831883144b2abcbb755036698b6f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
    这里有两种选项：

1）Enable file system caching （启用文件系统缓存）  
2）Enable symbolic links （启用符号链接）

我们这里勾选第一项就可以了，然后 “Next”

-   **配置实验选项，我们就不体验了，直接 “Next”。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/3fbbbd65db5c4a5b8760aa9c1afc641a.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

3、安装完成后鼠标右击和者开始->程序会出现，打开Git Bash，进入bash界面。

![在这里插入图片描述](https://img-blog.csdnimg.cn/07b314db8b7048cfb9346152430ec67d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
从名字中带有Bush可以猜到，Git Bash中照搬了许多Bush命令。

## 初始设置

4、设置姓名和邮箱地址

在git bash界面输入如下内容即可完成邮箱的注册：

```bash
$ git config --global user.name "user.name"
```

（说明：双引号中需要你的用户名，这个可以随便输入，比如“zhangsan”）

```bash
$ git config --global user.email "yourmail@youremail.com.cn"
```

（说明： 双引号中需要输入你的有效邮箱，比如“12131312@qq.com”）

![在这里插入图片描述](https://img-blog.csdnimg.cn/6c1ffef3b7cf489cbbcbe1773f3f1b0f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
5、提高命令可读性

```bash
$ git config --global color.ui auto
```

将color.ui设置为auto可以让命令的输出拥有更高的可读性。

6、Git的配置与查看

```bash
//查看所有配置 git config -l //查看系统配置 git config --system --list
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/dbd84e70c58144ae8363b6c507f15051.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

```bash
//查看用户自己配置 git config --global --list
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/8d00cb3a92c449459ed0ca376ce43817.png)  
系统文件配置所在位置：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/539fd36e7820461d9ffcf9da308ef51e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
用户自己配置所在位置：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/22cbd2021efb4d8da4549955e949c88e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)

## 使用GItHub准备

7、创建GitHub账户

由于我早就创建、不多做详细步骤了

8、设置SSH Key

GitHub上连接已有仓库时的认证，是通过使用SSH的公开密钥认证方式来进行的。

运行下面命令创建SSH Key：

```bash
$ssh-keygen -t rsa -C "你的邮箱名" $指定目录: C:\deskbook\（说明：若在此处不输入路径，而直接按回车，则ssh keys生成后存放的路径为C:\User\.ssh） $输入密码: 123456 $确认密码: 123456
```

如此即可在C:\\deskbook\\文件夹中生成ssh keys。包括两个文件rd\_rsa和id\_rsa.pub，第一个是私有密钥，第二个是公有密钥。

9、添加公开密钥

使用该命令可以查看密钥内容。

```bash
$ cat ~/.ssh/id_rsa.pub
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/ddd6e88f110b4695be224d46fdee3e21.png)  
最后面是刚刚注册时的邮箱。

点开GitHub中的setting，找到ssh key  
![在这里插入图片描述](https://img-blog.csdnimg.cn/81f08365735b41ad9fa2738fd8021f9a.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
![在这里插入图片描述](https://img-blog.csdnimg.cn/a26048766532458c87f1117e40c8ac1b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
在Title中输入密钥名称（随便填），在key里面粘贴id\_rsa.pub里面的内容

添加完成后创建账号的邮箱会收到一封邮件提示。

完成以上设置后，就可以利用手中私人密钥与GitHun进行认证通信

```bash
$ ssh -T git@github.com
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/0fece8a4da7145cf94c25689aa9b7418.png)

## 创建仓库上传文件

10、创建仓库

你可以直接点New repository来创建，填写基本信息，.进入仓库  
![在这里插入图片描述](https://img-blog.csdnimg.cn/97f381409347451889e1a07bf93e9ee6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
11、将已有仓库clone到自己开发环境中  
![在这里插入图片描述](https://img-blog.csdnimg.cn/b583bddb295e43149ccca5757a952d46.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_16,color_FFFFFF,t_70,g_se,x_16)

```bash
$ git clone git@github.com:MrLiuMY/Data-Structures-and-Algorithms.git
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/0c207e664d654c0c87dab5dee3a433b1.png)  
就把刚刚的东西clone到自己的开发环境中

12、添加文件上传

在刚刚下载的文件中复制进去自己想要上传的文件  
![在这里插入图片描述](https://img-blog.csdnimg.cn/83d2f2c4a2a843d3a84d89ea71fce20d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
接着继续输入命令 cd “刚刚下载文件夹”，进入文件夹依次执行下面指令

```bash
git add . （注：别忘记后面的.，此操作是把Test文件夹下面的文件都添加进来） git commit -m "提交信息" （注：“提交信息”里面换成你需要，如“first commit”） git push -u origin master （注：此操作目的是把本地仓库push到github上面，此步骤需要你输入帐号和密码）
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/bd201e4db92a4290955d7dd304fd8ec2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5rip6YCX5q27,size_20,color_FFFFFF,t_70,g_se,x_16)  
可以通过 `git log` 命令查看提交日志

本次讲解了初次在Github建立仓库以及公开代码流程

参考内容：

https://www.cnblogs.com/sdcs/p/8270029.html  
https://www.cnblogs.com/52xiaobu/p/14083995.html  
https://www.cnblogs.com/obge/p/14212013.html  
https://www.cnblogs.com/cxk1995/p/5800196.html