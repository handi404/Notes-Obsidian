![](https://csdnimg.cn/release/blogv2/dist/pc/img/original.png)

[xxxxxue](https://blog.csdn.net/qq_37214567 "xxxxxue") ![](https://csdnimg.cn/release/blogv2/dist/pc/img/newUpTime2.png) 已于 2024-04-28 15:20:50 修改

## Github

[https://github.com/oven-sh/bun](https://github.com/oven-sh/bun)

## 版本号

bun 1.1.5

windows 安装 bun

如果本机有 nodejs 环境,  
可以 `npm install -g bun` 安装  
( 官方把 exe 已经传到了 npm 仓库, 走的国内 [npm 镜像](https://so.csdn.net/so/search?q=npm%20%E9%95%9C%E5%83%8F&spm=1001.2101.3001.7020), 下载速度会很快)

没有 nodejs, 可以用 `powershell 脚本`安装

## 具体操作

### 全局

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b3029b0527e865c31d3814870bb918cc.png)

估计是 bun 1.1 才支持的 windows.  
官方文档也没有来得及补充太多关于 windows 的配置  
写的还是类 Unix 系统的 `$HOME`  
很多 windows 用户可能会看不懂这个变量指的是哪里.

在 windows 上 home 变量  
传统 cmd 中是 `%homepath%`  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b6358aad613bdaa32d59fbaae6ae27cf.png)  
powershell 更高级一些, 支持 `$HOME`  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3e515bd6f6ef5f1a8cabfd003eec8bca.png)

输出后发现, 是 当前用户 的文件夹

所以全局换源就是在这个路径下创建一个 `.bunfig.toml`  
( 一定要注意文件名前面有一个 `点` )

`.bunfig.toml` 内容如下

```toml
[install] registry = "https://registry.npmmirror.com/"
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ec0335b5f3da223b950e7370bf574f7a.png)

### 单项目

单个项目的换源直接在根目录创建 `bunfig.toml` (名称前面`没有点`),  
`局部会覆盖全局配置`