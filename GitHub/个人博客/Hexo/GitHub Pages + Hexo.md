本文介绍如何使用GitHub Pages + Hexo搭建个人博客网站，完全免费，所有内容本人亲测，绝对可用。

#### 一、准备工作

##### 1\. GitHub账号

需要有一个 `GitHub` 账号，没有的话到 [官网](https://github.com/) 申请一个。  
注册很简单，不懂的话可以参考 [GitHub申请账号](https://blog.csdn.net/yaorongke/article/details/119086305)

##### 2\. 安装Git

在自己电脑上安装好 `Git` ， [hexo](https://so.csdn.net/so/search?q=hexo&spm=1001.2101.3001.7020) 部署到 `GitHub` 时要用。  
网上找篇教程或者参考 [Git安装(Windows)](https://blog.csdn.net/yaorongke/article/details/119085413)

##### 3\. 安装NodeJS

在自己电脑上安装好 `NodeJS` ， `Hexo` 是基于 `NodeJS` 编写的，所以需要安装 `NodeJS` 和 `npm` 工具。  
网上找篇教程或者参考 [NodeJS安装及配置(Windows)](https://blog.csdn.net/yaorongke/article/details/119084295)

#### 二、创建仓库

在 `GitHub` 上创建一个新的代码仓库用于保存我们的网页。

点击 `Your repositories` ，进入仓库页面。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/2b166635eb155af4280f7a543e8a0c91.png)  
点击 `New` 按钮，进入仓库创建页面。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/996b57865fe5f52123cf99174db0811b.png)  
填写仓库名，格式必须为 `<用户名>.github.io` ，然后点击 `Create repository` 。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/ab8567f9eef0b6250ac609116186d41e.png)  
点击 `creating a new file` 创建一个新文件，作为我们网站的主页。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/1933a3b68a10ff2f84d739f4b5d3bc2e.png)  
新文件的名字必须为 `index.html` ，内容先随便写一个简单的，内容示例如下，填写之后点击 `Commit new file` 提交。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>yaorongke</title>
</head>
<body>
    <h1>yaorongke的个人主页</h1>
    <h1>Hello ~</h1>
</body>
</html>
html1234567891011
```

![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/aed4ef24186c8fd3815407f4efccbafe.png)  
GitHub Pages中找到我们主页的地址为 https://yaorongke.github.io/  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/8a9516667b0276d5dfc4a3ff38fe5097.png)  
浏览器中访问，展示成功。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/4ac25c21108e0fecf331993cac5914c8.png)  
这里创建的网页是非常简陋的，只是为了演示下 `GitHub Pages` 的使用方式。

#### 三、安装Hexo

我们采用 `Hexo` 来创建我们的博客网站， `Hexo` 是一个基于 `NodeJS` 的静态博客网站生成器，使用 `Hexo` 不需开发，只要进行一些必要的配置即可生成一个个性化的博客网站，非常方便。点击进入 [官网](https://hexo.io/zh-cn/) 。

安装 `Hexo`

```shell
npm install -g hexo-cli
```

查看版本

```shell
hexo -v
```

创建一个项目 `hexo-blog` 并初始化

```shell
hexo init hexo-blog
cd hexo-blog
npm install
```

本地启动

```shell
hexo g
hexo server
```

浏览器访问 http://localhost:4000，页面默认主图风格如下  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/1fa17b1845debf8e2670ffeba4a9dee0.png)

#### 四、更换主题

`Hexo` 默认的主题不太好看，不过官方提供了数百种主题供用户选择，可以根据个人喜好更换，官网主题点 [这里](https://hexo.io/themes/) 查看。这里介绍两个主题的使用方法， `Next` 和 `Fluid` ，个人比较喜欢 `Fluid` ，后面章节的功能也是以 `Fluid` 为基础进行讲解的。

##### 1\. NexT 主题

**安装主题**

```shell
cd hexo-blog
git clone https://github.com/iissnan/hexo-theme-next themes/next
```

**使用 NexT 主题**

打开 \_config.yml 文件，该文件为站点配置文件  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/92988aea1bce3f6808d390c818ae198e.png)  
将主题修改为 next

```shell
theme: next
```

**本地启动**

```shell
hexo g -d
hexo s
```

##### 2\. Fluid主题

以下安装步骤摘自 [Fluid官网](https://github.com/fluid-dev/hexo-theme-fluid)

**安装主题**

下载 [最新 release 版本](https://github.com/fluid-dev/hexo-theme-fluid/releases) 解压到 `themes` 目录，并将解压出的文件夹重命名为 `fluid` 。

**指定主题**

如下修改 `Hexo` 博客目录中的 `_config.yml` ：

```shell
theme: fluid  # 指定主题
language: zh-CN  # 指定语言，会影响主题显示的语言，按需修改
```

**创建「关于页」**

首次使用主题的「关于页」需要手动创建：

```
hexo new page about
```

创建成功后，编辑博客目录下 `/source/about/index.md` ，添加 `layout` 属性。

修改后的文件示例如下：

```
---
title: about
date: 2020-02-23 19:20:33
layout: about
---

这里写关于页的正文，支持 Markdown, HTML
1234567
```

**本地启动**

```shell
hexo g -d
hexo s
```

浏览器访问 http://localhost:4000， `Fluid` 主题风格页面如下  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/c740ecd3f5dac4c831d0903d1317e180.png)

#### 五、创建文章

如下修改 Hexo 博客目录中的 `_config.yml` ，打开这个配置是为了在生成文章的时候生成一个同名的资源目录用于存放图片文件。

```yaml
post_asset_folder: true
```

执行如下命令创建一篇新文章，名为《测试文章》

```shell
hexo new post 测试文章
```

执行完成后在 `source\_posts` 目录下生成了一个md文件和一个同名的资源目录(用于存放图片)  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/bc04cff11fa1fa108c48f1e9c4fdd4ce.png)  
在资源目录 `测试文章` 中放一张图片 `test.png`  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/c663af9a3078ce77c1656105deab1de1.png)  
在 `测试文章.md` 中添加内容如下，演示了图片的三种引用方式。第一种为官方推荐用法，第二种为markdown语法，第三种和前两种图片存放位置不一样，是将图片放在 `\source\images` 目录下。这三种写法在md文件中图片是无法显示的，但是在页面上能正常显示。

图片的引入方式可参考官方文档 [https://hexo.io/zh-cn/docs/asset-folders.html](https://hexo.io/zh-cn/docs/asset-folders.html) ，有详细介绍。

```markdown
---
title: 测试文章
date: 2021-06-10 16:35:20
tags:
- 原创
categories:
- Java
---

这是一篇测试文章

{% asset_img test.png 图片引用方法一 %}

![图片引用方法二](test.png)

![图片引用方法三](/images/test.png)
markdown12345678910111213141516
```

**本地启动**

```shell
hexo g -d
hexo s
```

浏览器访问 http://localhost:4000，页面如下，文章添加成功  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/5c10e3ee31a6589c548cb18868158173.png)

#### 六、个性化页面展示

页面的标题等位置显示默认的文字，可以改下显示一些个性化的信息。

##### 1\. 浏览器tab页名称

修改根目录下 `_config.yml` 中的 `title` 字段。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/9f5d2682398a9bf2dd3d22300d1bfa34.png)

##### 2\. 博客标题

主题目录 `themes\fluid` 下 `_config.yml` 中的 `blog_title` 字段。

![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/90a3f984cd9e7fa62c5edf7140968163.png)

##### 3\. 主页正中间的文字

主题目录 `themes\fluid` 下 `_config.yml` 中的 `text` 字段。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/d1d671af5913fc79fceeef00ef91e2bc.png)  
修改好配置后，页面效果如下，可以看到现在显示的内容变成了我们的个人信息。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/f72cdf670c6cf0e0fbee7becf93e9292.png)

#### 七、添加阅读量统计

`Fluid` 主题写好了统计阅读量的代码，但是缺少相应配置所以没有开启，需要借助三方服务来统计阅读量，这里是有 `Leancloud` 的免费服务来进行统计。

##### 1\. 申请LeanCloud账号并创建应用

进入 [官网](https://console.leancloud.cn/) 注册账号  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/a428fe73847ed99303fe3b5013b256f4.png)  
需实名认证，完成后才能使用各项服务  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/05ed44aeccc043fbb0b9e16caea84801.png)  
验证邮箱  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/2f5e9021aebdb7c2bc08f22e0676b89f.png)  
创建应用，选择 `开发版` 即可，免费的  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/c1017a718b6bb23826d7cc1ccf919990.png)  
进入该应用的 `设置->应用凭证` ，找到 `AppID` 和 `AppKey` ，记录下来后面配置要用  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/f87d88766a4ecd13a5f58b79bcc8b65b.png)

##### 2\. 修改Fluid配置

打开主题目录 `themes\fluid` 下的 `_config.yml` 文件，修改如下配置

###### 单篇文章阅读量计数

打开统计开关  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/a031a8e85bcf012bfad1ec3becf022da.png)  
配置 `leancloud` 的 `app_id` 和 `app_key`  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/300f300c219f964dd9ba6ef14cdab59d.png)  
打开计数功能，统计来源改为 `leancloud`  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/796283f28fbff5f9b91360397c826b77.png)  
页面效果  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/a6db78c41b487482be1977e36b64b907.png)

###### 页面底部展示网站的 PV、UV 统计数

![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/70139f6941117c66be425475ca5c65c7.png)  
页面效果  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/92a20c061c5feabccc668810a38926b2.png)

#### 八、添加评论功能

评论功能的代码已经写好了，只不过没有开启，需要修改一些配置

打开主题目录 `themes\fluid` 下的 `_config.yml` 文件，修改如下配置

启用评论插件  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/3156e87e87fd3e2f4ba858bfb7bf098f.png)  
配置 `LeanCloud` 的 `appId` 和 `appkey`  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/820f3cb524e7e73ae7042893cc5a38a6.png)  
重新部署后，查看页面效果，评论功能已开启  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/0b00f4e78c1d058163d89a80431d3954.png)  
部署在本地时评论无法提交，会报跨域问题，发布到 `GitHub Pages` 上之后课正常提交评论

#### 九、发布到GitHub Pages

##### 方式一

安装hexo-deployer-git

```shell
npm install hexo-deployer-git --save
```

修改根目录下的 `_config.yml` ，配置 `GitHub` 相关信息

```yaml
deploy:
  type: git
  repo: https://github.com/yaorongke/yaorongke.github.io.git
  branch: main
  token: ghp_3KakcaPHerunNRyMerofcFd9pblU282FSbsY
```

其中 `token` 为 `GitHub` 的 `Personal access tokens` ，获取方式如下图  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/767d206b7cf26f90b17705f170db40fa.png)  
部署到GitHub

```shell
hexo g -d
```

浏览器访问 https://yaorongke.github.io/，部署成功  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/00ef2fb7c3fd72101036c976ac78dc09.png)

##### 方式二

直接将 `public` 目录中的文件和目录推送至 `GitHub` 仓库和分支中。  
![请添加图片描述](https://i-blog.csdnimg.cn/blog_migrate/d8f74b8200026dee4bd2939fb4c5aacb.png)

#### 十、发布到自己服务器，Nginx代理

如果自己有服务器的话，也可以不使用 `GitHub Pages` ，直接部署的自己的服务器上，通过 `Nginx` 进行代理，我这里有一个阿里云上的 `CentOS 7` 版的 `Linux` 服务器，演示下如何部署，步骤如下。

打开 `hexo-blog` 根目录下的 `_config.yml` ，增加如下配置，这是因为把网站存放在了子目录中，要和 `Nginx` 配置中的 `location /blog` 路径一致。

```shell
root: /blog
```

`hexo-blog` 根目录下执行打包命令，打包好的文件在 `public` 目录下

```shell
hexo g
```

将 `public` 目录下的文件复制到 `Linux` 服务器上的某个目录下，我的存放目录为

```shell
/opt/rkyao/fronted/hexo-blog
```

修改 `Nginx` 配置文件，我的 `Nginx` 安装路径为 `/usr/local` ，大家根据自己实际情况调整

```shell
cd /usr/local/nginx/conf
vim nginx.conf
# server节点下添加如下配置
location /blog {
    alias  /opt/rkyao/fronted/hexo-blog;
    index  index.html index.htm;
}
```

重启 `Nginx`

```shell
cd /usr/local/nginx/sbin
./nginx -s reload
```

访问博客

```shell
http://47.96.106.173/blog/
```

#### 十一、最终效果展示

可访问如下地址查看  
[https://yaorongke.github.io/](https://yaorongke.github.io/)