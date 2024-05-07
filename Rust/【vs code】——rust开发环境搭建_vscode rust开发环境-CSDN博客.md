#### 文章目录

-   [前言](https://blog.csdn.net/weixin_43822014/article/details/130468004#_5)
-   [1\. 在windows上安装rust](https://blog.csdn.net/weixin_43822014/article/details/130468004#1_windowsrust_10)
-   [2\. 在vscode上安装rust相关插件](https://blog.csdn.net/weixin_43822014/article/details/130468004#2__vscoderust_44)
-   -   [rust-analyzer](https://blog.csdn.net/weixin_43822014/article/details/130468004#rustanalyzer_45)
    -   [Rust Syntax](https://blog.csdn.net/weixin_43822014/article/details/130468004#Rust_Syntax_48)
    -   [Rust Test Lens](https://blog.csdn.net/weixin_43822014/article/details/130468004#Rust_Test_Lens_52)
    -   [还有其他插件都可安装哦](https://blog.csdn.net/weixin_43822014/article/details/130468004#_56)
-   [3.创建一个rust项目并运行](https://blog.csdn.net/weixin_43822014/article/details/130468004#3rust_57)

___

## 前言

vscode下搭建 `rust` 语言开发环境

___

## 1\. 在windows上安装[rust](https://so.csdn.net/so/search?q=rust&spm=1001.2101.3001.7020)

> 参考官方[安装教程](https://so.csdn.net/so/search?q=%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B&spm=1001.2101.3001.7020)：[https://www.rust-lang.org/tools/install](https://www.rust-lang.org/tools/install)  
> 我们通过快速方式 rustup安装

`Tips: rust依赖C/C++环境 在安装rust前需要先安装C/C++编译环境`

有两种:  
1、msvc Visual Studio使用的是[msvc](https://so.csdn.net/so/search?q=msvc&spm=1001.2101.3001.7020) 这个安装rust一路默认即可  
2、mingw  
大家自行安装下C/C++环境哦

这里介绍基于 mingw 安装 rust  
![在这里插入图片描述](https://img-blog.csdnimg.cn/ee8e221080114317a873b3af9f81ac96.png)

下载好运行后  
![在这里插入图片描述](https://img-blog.csdnimg.cn/8204dba5f36e47e19fb9b2e92e6b1665.png)  
输入y继续  
弹出安装选项  
![在这里插入图片描述](https://img-blog.csdnimg.cn/f35691ca332d44c99b98b3824d88fc05.png)  
`默认安装的时msvc 我安装 gnu的 需要我们修改默认安装配置`  
输入2 选择自定义配置  
![在这里插入图片描述](https://img-blog.csdnimg.cn/3a65f14a55654e00a34e05541df1d226.png)  
修改为x86\_64-pc-windows-gnu 其他默认即可  
![在这里插入图片描述](https://img-blog.csdnimg.cn/1152ec98191d4b9ab822ff0ec8623503.png)  
后面几步回车即可  
![在这里插入图片描述](https://img-blog.csdnimg.cn/34a5bfe3b5264926934f2f09ef2d9f82.png)  
可以看到默认安装配置已经修改好了  
接下来输入1安装就好  
![在这里插入图片描述](https://img-blog.csdnimg.cn/f3e02fcbcfea40009813645a4b1694c0.png)  
`全部操作动图演示`  
![在这里插入图片描述](https://img-blog.csdnimg.cn/c49f732b9c91459f9da922f65dd3d411.gif#pic_center)  
安装完通过rustup show命令 查看是否安装成功  
![在这里插入图片描述](https://img-blog.csdnimg.cn/582cdca2e7484af89ff9fec3dc8305bf.png)

## 2\. 在vscode上安装rust相关插件

### rust-analyzer

`插件简介：编译/分析 rust代码`  
![在这里插入图片描述](https://img-blog.csdnimg.cn/b489bb9ee8554e2697374c333aaa5b99.png)

### Rust Syntax

`插件简介：为rust代码提供语法高亮显示`

![在这里插入图片描述](https://img-blog.csdnimg.cn/8ddf2e88d6e04213a3c8db4e0fd0c5ac.png)

### Rust Test Lens

`插件简介：快速运行或调试rust代码`

![在这里插入图片描述](https://img-blog.csdnimg.cn/1b35109289d347efb605efb83e499d9c.png)

### 还有其他插件都可安装哦

## 3.创建一个rust项目并运行

在终端命令行输入 cargo new 项目名  
然后回车  
![在这里插入图片描述](https://img-blog.csdnimg.cn/8f238938c9d24cb4a6af8ae3e6cf39e9.png)  
![在这里插入图片描述](https://img-blog.csdnimg.cn/743c43103e834b4796494a7a15318128.png)  
好啦开启你的rust之旅吧