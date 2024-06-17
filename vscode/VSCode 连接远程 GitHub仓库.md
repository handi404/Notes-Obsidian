
## [VSCode](https://so.csdn.net/so/search?q=VSCode&spm=1001.2101.3001.7020) 连接远程 GitHub仓库 教程

### 1、[GitHub](https://so.csdn.net/so/search?q=GitHub&spm=1001.2101.3001.7020) 新建远程仓库

#### 一、点击新建仓库

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7bb6fbcb03a945ecbf287c599fa794c0.png)

#### 二、添加仓库名称与描述并创建

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/5a995ecaa4804801bec8f24e8b3d00ae.png)

#### 三、获取仓库链接地址

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/71793d6becc74411870db87a4e7684e0.png)

**目前GitHub已经完成相关操作了，接下来我们操作VSCode**

### 2、新建文件夹

#### 一、新建文件夹

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/4ac2c9bc3e584f189c04d3f3b16046b3.png)

#### 二、VSCode打开对应新建的文件夹

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/2065304471724dd7a9a1c6ee6fe2c102.png)

**当前页面：**

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/0048f0edda144ea7b5ebf4f935c4ab6e.png)

### 3、使用终端命令 连接远程仓库并提交

#### 一、打开终端命令行

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/66c69922499d4f8f9ff50d35c8e4a0eb.png)

#### 二、创建README.md文档

创建README.md文档，用于向其他人介绍你的项目。它可以包含项目的描述、使用说明、安装指南、[示例代码](https://so.csdn.net/so/search?q=%E7%A4%BA%E4%BE%8B%E4%BB%A3%E7%A0%81&spm=1001.2101.3001.7020)等信息。README文档对于协作开发和项目共享非常重要，它可以帮助其他人快速了解你的项目，并提供必要的上下文和指导。

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/65f4de455fe94880a66fcc8cb5ce14aa.png)

#### 三、依次输入以下命令

```java
git init 
git add README.md 
git config --global user.email "you@example.com" 
git config --global user.name "Your Name" 
git commit -m "first commit" 
git branch -M main 
git remote add origin https://github.com/Wang-Phil/test.git git push -u origin main
```

**每句话的含义如下：**

1.  `git init`：该命令用于在当前目录中初始化一个新的Git仓库。它会创建一个名为`.git`的隐藏文件夹，用于存储Git仓库的相关信息。
2.  `git add README.md`：该命令将名为"README.md"的文件添加到Git的暂存区。暂存区是Git用来跟踪文件更改的一个中间区域。
3.  `git config --global user.email "you@example.com"`：该命令用于设置Git的全局配置，其中`user.email`是你的邮箱地址。这个配置将与你的提交记录相关联。
4.  `git config --global user.name "Your Name"`：该命令用于设置Git的全局配置，其中`user.name`是你的用户名。这个配置将与你的提交记录相关联。
5.  `git commit -m "first commit"`：该命令用于将暂存区中的文件提交到Git仓库。`-m`选项后面的内容是提交的描述信息，用于解释本次提交的目的。
6.  `git branch -M main`：该命令用于重命名当前分支。这里将当前分支重命名为"main"，这是GitHub默认的主分支名称。
7.  `git remote add origin https://github.com/Wang-Phil/test.git`：该命令用于将本地仓库与远程GitHub仓库关联起来。`origin`是远程仓库的别名，`https://github.com/Wang-Phil/test.git`是远程仓库的URL。
8.  `git push -u origin main`：该命令用于将本地仓库的内容推送到远程GitHub仓库。`-u`选项表示将本地的"main"分支与远程仓库的"main"分支关联起来。这样，在以后的推送中，你只需要运行`git push`命令即可。

**VSCode界面显示如下：**

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/d79ecad8d75446b4ad10d888170b1e32.png)

**GitHub界面显示如下：**

![外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传](https://img-blog.csdnimg.cn/direct/7cc6f6d4434b4d479c0ba8e9e57d05e2.png)

**恭喜你，已经完成了仓库的创建，后续可以在本地编写完代码进行代码的提交。**