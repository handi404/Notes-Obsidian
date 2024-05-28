#### 文章目录

-   -   [一、\`git log\` 的退出](https://blog.csdn.net/m0_46278037/article/details/119628828#git_log__1)
    -   [二、\`git commit\` 的退出](https://blog.csdn.net/m0_46278037/article/details/119628828#git_commit__15)
    -   -   [1 、保存并退出：](https://blog.csdn.net/m0_46278037/article/details/119628828#1__22)
        -   [2 、不保存退出：](https://blog.csdn.net/m0_46278037/article/details/119628828#2__27)

### 一、`git log` 的退出

-   当`commit`（提交）比较多，`git log` 的内容在一页显示不完整，满屏放不下的时候，就会显示冒号。
    
-   回车（往下滚一行）、空格（往下滚一页）可以继续查看剩余内容。
    
-   退出：英文状态下 按 `q` 可以退出`git log` 状态。
    

`git log` 命令：  
![请添加图片描述](https://img-blog.csdnimg.cn/f41004dd9e2f4607ae2dbcfd19768ecf.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzQ2Mjc4MDM3,size_16,color_FFFFFF,t_70)  
内容太多放不下时显示冒号：  
![请添加图片描述](https://img-blog.csdnimg.cn/8b7cae25e4f34b589ce733c1bd13036f.png)  
空格键显示下一页，出现`（END）`表示到显示最后了：  
![请添加图片描述](https://img-blog.csdnimg.cn/d532f8003b494ace86026a863b1de420.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzQ2Mjc4MDM3,size_16,color_FFFFFF,t_70)

### 二、`git commit` 的退出

-   当使用`git commit`而不使用`git commit -m`命令（没有带`-m`参数）时，会进入到vim编辑器中。
-   `vim`编辑器是`Linux`系统中必备的编辑器，`Git`工具由`Linux`创始人写出来的，所有就把`vim`编辑器也用在`Git`上。
-   和`Linux`的使用一样的，因为涉及到是否要保存编辑内容，所以退出命令有多种。

![请添加图片描述](https://img-blog.csdnimg.cn/03b69df7b2ef4a5b9032f8ed5c915b94.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzQ2Mjc4MDM3,size_16,color_FFFFFF,t_70)

#### 1 、保存并退出：

（1）按 `Esc` 键退出编辑模式，英文模式下输入 `:wq` ，然后`回车`(write and quit)。

（2）按 `Esc` 键退出编辑模式，大写英文模式下输入 `ZZ` ，然后`回车`。

#### 2 、不保存退出：

按 `Esc` 键退出编辑模式，英文模式下输入 `:q!` ，然后`回车`。

按 `Esc` 键退出编辑模式，英文模式下输入 `:qa!` ，然后`回车`。