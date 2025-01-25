![](https://csdnimg.cn/release/blogv2/dist/pc/img/original.png)

[慕筱佳丶](https://blog.csdn.net/weixin_51138142 "慕筱佳丶") ![](https://csdnimg.cn/release/blogv2/dist/pc/img/newCurrentTime2.png) 于 2021-04-25 00:26:11 发布

**心语：如果路的尽头不是你，再完美的风景也是破铜烂铁。**

今天我使用Git工具推送代码到我的[Github](https://so.csdn.net/so/search?q=Github&spm=1001.2101.3001.7020)账号，Git工具推送成功后，发现Github上面没有代码。捣鼓之后发现Github账号默认的主分支从2020年10月1日起已经由master改为了main，而Git工具默认推送的还是master分支，这就导致推送的代码在Github上面的main主分支看不到，想要看到代码还需要切换分支。

我顿时就很诧异，可能是因为我好久都没有在git贡献过代码的原因，呜呜呜~~~

![](https://i-blog.csdnimg.cn/blog_migrate/796107632aad86e1a9e5d7d73de31734.png)

然后在在网上找了一些解决的办法，比如直接用Git工具向main分支推送，但是奇怪的报错来了

![](https://i-blog.csdnimg.cn/blog_migrate/467f67e0a2511182d7cd11a9d05e651d.png)

这可能是因为我本地没有main分支，于是我就想着删掉本地和Github上的master分支，在本地新建一个main分支，使用Git工具直接推送代码到Github的main分支，

-   使用git checkout -b main 切换到main分支并进入
-   git branch -D master 删除本地的master分支

这样难道就可以解决了吗？ 当我又使用git push 推送代码的时候又出现错误，

![](https://i-blog.csdnimg.cn/blog_migrate/24f736c183962aeb43c723061e29e12e.png)

于是我又开始网上冲浪寻找解决办法，最后才发现，在我创建新仓库的时候，创建了一个rename.md文件，而本地却没有。因此需要先将Github上面的README拉取下来，

Git新版本使用命令：git pull origin main --allow-unrelated-histories，Git老版本使用命令: git pull origin main）

把远程分支上的提交合并到本地分支之后再推送代码。

最后使用 git push -u origin main -f 命令进行强制代码推送，成功推送。

![](https://i-blog.csdnimg.cn/blog_migrate/493efefdc573e11931679e607dda9b50.png)

**我这边尝试了一下，不创建readme就没有main分支，然后上传的时候可以直接上传master分支**