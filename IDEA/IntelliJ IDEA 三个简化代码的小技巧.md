## IntelliJ IDEA 三个简化代码的小技巧(3 ways to simplify your code )



1.1 Methods（方法）

```
Command+option+M（macOS）
Ctrl+Alt+M（windows/linux） 
```

1.2 Constants 常量

```
Command+option+C（macOS）
Ctrl+Alt+C（windows/linux）  
```

1.3 Fields

```
Command+option+F（macOS）
Ctrl+Alt+F（windows/linux） 
```

1.4 Variables变量

```
Command+option+V（macOS）
Ctrl+Alt+V（windows/linux）  
```

1.5 Parameters参数

```
Command+option+P（macOS）
Ctrl+Alt+P（windows/linux）  
```

1.6 Inline（内嵌）

```
Command+option+N（macOS）
Ctrl+Alt+N（windows/linux）  
```

2.Change Signature

```
Command+F6（macOS）
Ctrl+F6（windows/linux） 
```

3.Rename（重命名）

```
Shift+F6（macOS）
Shift+F6（windows/linux）
```

---

## 1.Extract / Inline（提取/内联）

### 1.1 Methods（方法）

**Command+option+M（macOS）  
Ctrl+Alt+M（windows/linux）**  
第一步 选中需要提取的代码块  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/23279b92318045a5dd8af4994ba8ea99.jpeg#pic_center)

第二步 按下Ctrl+Alt+M 会将选中代码提取到一个方法中，重新命名方法名即可  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d15f4ec7fcea08a6d7d87a4f1f334906.jpeg#pic_center)

### 1.2 Constants 常量

**Command+option+C（macOS）  
Ctrl+Alt+C（windows/linux）**  
提取常量，选择需要提取的数值，按Ctrl+Alt+C  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/97216f4957a3397c3b19e57e08b9eb0d.png#pic_center)

修改常量的名称，完成常量提取。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/879a3717a0a28963990bb5a73b180a0b.jpeg#pic_center)

### 1.3 Fields

**Command+option+F（macOS）  
Ctrl+Alt+F（windows/linux）**  
将重复使用的文本提取出来，增加代码的可读性  
选中需要提取的文本内容  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d0caa028842d531c77c2175fc6f6b3ac.jpeg#pic_center)

按 Ctrl+Alt+F 在对话框中设置好文本field的名称，访问权限，以及替换的选项  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/bd604e17b6f38eb18bf32dabb38b41c7.jpeg#pic_center)

点击ok后会将这些文本内容提取出来，如下图红框所示。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7f290e068a76872d15b90a8359d35094.jpeg#pic_center)

### 1.4 Variables变量

**Command+option+V（macOS）  
Ctrl+Alt+V（windows/linux）**  
挺长的一段代码，在使用的时候，可读性要差一些，这个方法就是教大家如何把一段代码提取为变量。  
首先选择需要提取的内容，按Ctrl+Alt+V，再提示框中选择需要提取的内容。可以根据自己需要选择提取的内容（算上length方法，还是不算length方法，都看个人喜好）。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/e946ae2a9af863f82c6af9583b64f3bc.jpeg#pic_center)

确认后，需要重命名一下变量名称，IDEA会自动重构相关代码。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/38da3084ba121b38806f8ab759d58747.jpeg#pic_center)

### 1.5 Parameters参数

**Command+option+P（macOS）  
Ctrl+Alt+P（windows/linux）**  
提取参数，将代码中重复使用的内容，提取为方法的参数。  
选中需要提取的内容，按Ctrl+Alt+P  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/efa11244ba32868858e4d7ad521564d9.jpeg#pic_center)

再提示框中，选择好参数名称以及替换的范围。点击重构。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ebaf47e6ee302d4c0828072de396b75c.jpeg#pic_center)

重构后的代码，会在方法中增加一个参数，替换方法提中的内容，另外调用该方法的代码也会相应调整。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d4da344ded500edea94d09b1ab42e543.jpeg#pic_center)

### 1.6 Inline（内嵌）

**Command+option+N（macOS）  
Ctrl+Alt+N（windows/linux）**  
如果想将提取出来的方法内嵌回去，则可以使用这个快捷键进行操作。  
选中需要内嵌的方法，按Ctrl+Alt+N，选择相应的内嵌方式（所有都内嵌进来还是单个内嵌？是否移除方法？这些都可以自己选择）  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3f3c47927474e4e8c1fdd0c66515ffc4.jpeg#pic_center)

重构后，方法的实现就会被内嵌到调用的方法中。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/878bf8bca22ac2cfde0056b3f1f8665a.jpeg#pic_center)

## 2.Change Signature （这个不知道怎么翻译合适，更改方法的参数特征？）

**Command+F6（macOS）  
Ctrl+F6（windows/linux）**  
选中参数，按下Ctrl+F6，出现下面的对话框后  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/07abbef0e981a61589347d0304624abe.jpeg#pic_center)

上述步骤操作结束之后，方法会增加一个season的参数。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6a38e5d4e796ffae2b3ac2ac54f90bb4.jpeg#pic_center)

IDEA会自动重构所有调用的方法，对于增加的参数，会直接赋予一个默认值，如下图所示，新增加的参数使用summer作为默认值。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/1f1a8b1bceee3184c0267f5782f60eef.jpeg#pic_center)

## 3.Rename（重命名）

**Shift+F6（macOS）  
Shift+F6（windows/linux）**  
选中方法名，然后按Shift+F6 之后修改方法名  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b79ee3e5a10ae171f917f579731407b3.jpeg#pic_center)

修改后方法名点击后面的图标，会弹出重命名的选项框，选择之后回车即完成重构。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/73da57964c2064c614c900afec069d35.jpeg#pic_center)

也可以点击上图中的more options，弹出框中可以选择更多内容。重命名“类”和“方法”，可以点击“preview”按钮预览重命名内容。也可以点击“refactor”直接重构（这个图是本机器截图，跟官方视频有点不一样）  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2b764ef218f4e05e9c3be412b6d31609.jpeg#pic_center) ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/42d4dd61ddcd65e6a400185303f01bf2.jpeg#pic_center)

点击预览按钮，可以查看到都有哪些内容会被重构  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f6fcc83e8bffae85b4cb7c103c776a06.jpeg#pic_center)

重构之后，会替换掉类的名字和注释中的名字，也会替换掉其他调用这个类的名字。另外这个替换是大小写敏感的。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7e3218dfe1887cac0fca1ba67ffa9b6b.jpeg#pic_center)

![](https://kunyu.csdn.net/1.png?p=58&adBlockFlag=1&adId=1067762&a=1067762&c=2666352&k=DeepOps%E7%9A%84%E5%B7%A5%E5%85%B7%E5%B0%8F%E7%AC%94%E8%AE%B0-IntelliJ%20IDEA%20%E4%B8%89%E4%B8%AA%E7%AE%80%E5%8C%96%E4%BB%A3%E7%A0%81%E7%9A%84%E5%B0%8F%E6%8A%80%E5%B7%A73%20ways%20to%20simplify%20your%20code&spm=1001.2101.3001.5002&articleId=112862700&d=1&t=3&u=03ed7efca82f4af0bcbc5522d540494b)[*IDEA* *\-* 优化 *IDEA* 的默认 *代码* 模版，减少简单 *代码* 的编写](https://wretchant.blog.csdn.net/article/details/104836746)

[简简单单Onlinezuozuo](https://blog.csdn.net/qq_15071263)

03-13 979[文章目录 *IDEA* *\-* 优化 *IDEA* 的默认 *代码* 模版，减少简单 *代码* 的编写1、找到 *代码* 模版2、给 *Java* 类模版默认添加2个 *方法* *3* 、给接口添加2个空 *方法* ，反正都是要写4、添加默认枚举值5、新建注解自动添加基础注解 *IDEA* *\-* 优化 *IDEA* 的默认 *代码* 模版，减少简单 *代码* 的编写 1、找到 *代码* 模版 2、给 *Java* 类模版默认添加2个 *方法* #if *(*${PACKAGE\_NAME} &&...](https://wretchant.blog.csdn.net/article/details/104836746)[提升效率:*IntelliJ* *IDEA* 必备插件推荐](https://blog.csdn.net/zeal9s/article/details/85242504)

3-27[*IDEA* 中高效率编码插件 摘要由CSDN通过智能技术生成 展开 在 *IntelliJ* *IDEA* 的安装讲解中我们其实已经知道,*IntelliJ* *IDEA* 本身很多功能也都是通过插件的方式来实现的,只是 *IntelliJ* *IDEA* 本身就是它自己的插件平台最大的开发者而已,开发了很多优秀的插件。 官网插件库:https://plugins.*jetbrains*.com/...](https://blog.csdn.net/zeal9s/article/details/85242504)[*IntelliJ* *IDEA* 安装教程（以 *IDEA* 2020.1为例）](https://lddwarehouse.blog.csdn.net/article/details/121928344)

[

热门推荐

](https://lddwarehouse.blog.csdn.net/article/details/121928344)

[蓝多多的小仓库](https://blog.csdn.net/qq_43554335)

12-15 47万+[不然会报错嗷（即：第六节所示）！因为我之前有JDK，所以为了更好的演示，我先把JDK17卸载了。](https://lddwarehouse.blog.csdn.net/article/details/121928344)[*idea* 安装lombok，优雅地 *简化* 冗长 *代码*](https://blog.csdn.net/qq_40096230/article/details/82527115)

[回首一辈子](https://blog.csdn.net/qq_40096230)

09-10 453[lombok是一个可以通过简单的注解的形式来帮助我们 *简化* 消除一些必须有但显得很臃肿的 *Java* *代码* 的 *工具* ，简单来说，比如我们新建了一个类，然后在其中写了几个字段，然后通常情况下我们需要手动去建立getter和setter *方法* 啊，构造函数啊之类的，lombok的作用就是为了省去我们手动创建这些 *代码* 的麻烦，它能够在我们编译源码的时候自动帮我们生成这些 *方法* 。 lombok能够达到的效果就是在源码中不...](https://blog.csdn.net/qq_40096230/article/details/82527115)[*Idea* 开发 *工具* 常用的快捷键\_ *idea* 开发 *工具* 快捷键](https://blog.csdn.net/csdnZCjava/article/details/75222584)

4-21[8.*Idea* 用在Git上 alt+~ *代码* 提交到github上 二.编辑相关 1.shift+enter 另起一行 2.ctrl+r 当前文件替换特定内容 *3*.ctrl+shift+r 当前项目替换特定内容 4.shift+F6 非常非常省心省力的一个快捷键,可以重命名你的类、 *方法* 、变量等等,而且这个重命名甚至可以选择替换掉注释中的内容...](https://blog.csdn.net/csdnZCjava/article/details/75222584)[*IDEA* 中常用的Git操作指南\_ *idea* git 常用场景模拟](https://blog.csdn.net/nangongyanya/article/details/80816001)

4-12[下面来看以上各场景在 *IDEA* 中对应的操作。 场景一:小张创建项目并提交到远程Git仓库 创建好项目,选择VCS *\-* > Import in *to* Version Control *\-* > Create Git Reposi *to* ry 接下来指定本地仓库的位置,按个人习惯指定即可,例如这里选择了项目源 *代码* 同目录 点击OK后创建完成本地仓库,注意,这里仅仅是本地的。下面把项目源码添...](https://blog.csdn.net/nangongyanya/article/details/80816001)[不愧是阿里，DeepSeek满血版在 *IDEA* 中快的飞起，从零基础到精通，收藏这篇就够了！](https://blog.csdn.net/Javachichi/article/details/146421485)

[

最新发布

](https://blog.csdn.net/Javachichi/article/details/146421485)

[Javachichi的博客](https://blog.csdn.net/Javachichi)

03-21 713[大家好，我是二哥呀。有没有发现？不知不觉中，阿里巴巴已经完成了从电商巨头到硬核科技公司的蜕变。第一个标志事件，一向挑剔的苹果宣布和阿里合作，一起为国行版 iPhone 提供 AI 技术服务。第二个标志事件，DeepSeek R1 蒸馏了 6 个模型开源给社区，其中有 4 个来自阿里的 Qwen。第 *三个* 标志事件，阿里云为开发者量身定制的通义灵码插件，也在第一时间集成了 DeepSeek *\-* R1 满血版。](https://blog.csdn.net/Javachichi/article/details/146421485)[*Intellij* *IDEA* 中快速生成简单 *代码*](https://blog.csdn.net/u014163312/article/details/109731299)

[拾年一剑 的专栏](https://blog.csdn.net/u014163312)

11-16 1273[借助 *IDEA* 快速生成 *代码* *工具* ，能够提高开发效率，汇总常见的用法](https://blog.csdn.net/u014163312/article/details/109731299)[*IDEA* 开发 *工具* 使用 git 创建项目、拉取分支、合并分支\_branches *to* merge...](https://blog.csdn.net/c851204293/article/details/84612392)

4-16[下面来看以上各场景在 *IDEA* 中对应的操作。 场景一:小张创建项目并提交到远程Git仓库 创建好项目,选择VCS *\-* > Import in *to* Version Control *\-* > Create Git Reposi *to* ry 接下来指定本地仓库的位置,按个人习惯指定即可,例如这里选择了项目源 *代码* 同目录 点击OK后创建完成本地仓库,注意,这里仅仅是本地的。下面把项目源码添...](https://blog.csdn.net/c851204293/article/details/84612392)[*IntelliJ* *idea* 开发者必备 *\-* 快捷键大全及常用插件\_ *idea* alt+| 找到 *方法*...](https://blog.csdn.net/itsoftchenfei/article/details/82888497)

4-17[可以设置 *idea* 背景图片的插件 Nyan progress bar 所有的进度条都变成萌新动画的小插件 Key promoter 点击鼠标一个功能的时候,可以提示你这个功能快捷键 *Code* Glance *代码* 右侧缩略图 *(*类似SublimeText的Mini Map插件*)* activate *\-* power *\-* mode 装B插件 *(*输入有雪花*)* Lombok plugin...](https://blog.csdn.net/itsoftchenfei/article/details/82888497)[简洁 *代码*](https://blog.csdn.net/luo_xia530/article/details/52689357)

[博博博博博博博客](https://blog.csdn.net/luo_xia530)

09-28 381[@Scheduled *(*cron="0 0/1 \* \* \*?"*)* public void checkUserRole *(**)* { logger.info *(*"start checkUserRole "+System.currentTimeMillis *(**)**)*; UserInfoExample userInfoExample=new UserInfoEx](https://blog.csdn.net/luo_xia530/article/details/52689357)[使用 *idea* 安装lombok插件 *简化* *代码*](https://qingcha.blog.csdn.net/article/details/86676912)

[清茶的博客](https://blog.csdn.net/m0_38001814)

01-28 412[前言：lombok是一款可以通过注解的形式来帮助我们 *简化* 项目中一些臃肿的 *代码* ，例如定义完实体类属性后，还需通过 *IDE* *工具* 生成get/set *方法* 等等，每次修改实体类属性时还需增增减减对应的get/set *方法* ，且这些 *代码* 看着很是臃肿且毫无用处。lombok就是为 *简化* *代码* 而生，其不需在源码中编写通用的 *方法* ，只在编译时起作用，编译后生成的字节码文件中即有了对应的 *方法* 。 *idea* 安装lombok插件...](https://qingcha.blog.csdn.net/article/details/86676912)[*IntelliJ* *IDEA* 中快捷键一览\_ *idea* alt + w](https://blog.csdn.net/w_basketboy/article/details/8249571)

3-31[*IntelliJ* *IDEA* 中快捷键一览 本文提供 *IntelliJ* *IDEA* 的一系列快捷键介绍,包括 *代码* 生成、导航、搜索等功能,帮助提升开发效率。 摘要由CSDN通过智能技术生成 Alt+回车 导入包,自动修正 Ctrl+N 查找类 Ctrl+Shift+N 查找文件 Ctrl+Alt+L 格式化 *代码* Ctrl+Alt+O 优化导入的类和包...](https://blog.csdn.net/w_basketboy/article/details/8249571)[*Idea* /webs *to* rm/pycharm202 *3*.*3*.6启动无反应\_ClassNotFoundException: com.licel.b.Z@ *\-* *\-* *\-* *IntelliJ* *Idea* 工作 *笔记* 012](https://credream.blog.csdn.net/article/details/137098400)

[添柴程序猿的专栏](https://blog.csdn.net/lidew521)

03-28 4085[有一句这个 *\-* *java* agent这个配置,就是新版,刚安装的这个 *idea*,去找这个配置了,直接把这个文件中的。之前旧版本的2020版本的,卸载的时候没有卸载干净,导致,新版启动的时候,去找了旧版的配置文件。 *idea* 202 *3*.*3*.4最新版安装的时候报错,说一下原因,是因为。 *\-* *java* agent,这一行删除,是之前我们做优化用的对吧.具体就是这个:,其他系列的也一样处理就行。](https://credream.blog.csdn.net/article/details/137098400)[*idea* 又闹腾了,怪哉怪哉,急死人那种~需要class，interface或enum *\-* *\-* *\-* *IntelliJ* *Idea* 工作 *笔记* 010](https://credream.blog.csdn.net/article/details/125229135)

[添柴程序猿的专栏](https://blog.csdn.net/lidew521)

06-10 2586[编译 *代码* 的时候出了一堆的错误: 明明没有错误的好嘛~就是报错就是报错~错误内容是:需要class，interface或enum 来吧解决问题:1.解决方案1:这是 *Java* 文件的编码导致的，通常使用 *java* c FirstSample.*java* 编译UTF *\-* 8编码的.*java* 源文件。没有指定编码参数encoding的情况下，默认使用的是GBK编码。当编译器用GBK编码来编译UTF *\-* 8文件时，就会把UTF *\-* 8编码文件的 *3* 个字节的文件头，按照GBK中汉字占2字节、英文占1字节的特性解码成了“乱码”的两个汉字。这个源文](https://credream.blog.csdn.net/article/details/125229135)[*IntelliJ* *IDEA* 总结 *(*6*)* *\-* *\-* *IntelliJ* *IDEA* 修改 *代码* 不用重启](https://clevercode.blog.csdn.net/article/details/111408738)

[CleverCode的博客](https://blog.csdn.net/CleverCode)

12-19 1万+[目前有两个选项： On Update action: 顾名思义，当 *代码* 改变的时候，需要 *IDEA* 为你做什么； On Frame deactivation: 当失去焦点（比如你最小化了 *IDEA* 窗口），需要 *IDEA* 为你做什么。 On Update action 里面有四个选项（一般选Update classes and resources）： *\-* Update resources ：如果发现有更新，而且更新的是资源文件（\*.jsp，\*.xml等，不包括 *java* 文件）,就会立刻生效 *\-* Update class](https://clevercode.blog.csdn.net/article/details/111408738)[*IntelliJ* *IDEA* 插件开发 *\-* *代码* 补全插件入门开发](https://blog.csdn.net/tianruirui/article/details/143728145)

[JavaQ](https://blog.csdn.net/tianruirui)

11-12 2284[使用 *IntelliJ* *IDEA* 想必大家都有使用过 *代码* 自动补全功能，如输入ab，会自动触发补全，提供相应的补全建议列表。作为有追求的程序员，有没有想过这样的功能是如何实现的？本节将详细介绍如何实现一个类似的 *代码* 自动补全插件。在 *IntelliJ* *IDEA* 插件开发中，CompletionContribu *to* r是一个关键的API，它允许开发者为特定语言的 *代码* 编辑器添加 *代码* 补全功能。本节将详细介绍如何使用C...](https://blog.csdn.net/tianruirui/article/details/143728145)[Android studio 快捷键大全！必备收藏](https://xuyuanzhi051.blog.csdn.net/article/details/105219829)

[池鱼](https://blog.csdn.net/qq_44867340)

03-31 291[工欲善其事必先利其器，一个好的程序员一定会灵活的使用自己的编译 *工具* ，而快捷键便可以更好的让我们操作管理 *代码* ，下面就给大家介绍一下几个常用Android studio的快捷键 Ctrl+D 复制光标当前所在行 Ctrl+F 搜索 Alt+Enter 快速导入包 Ctrl+N 可以快速打开类 Ctrl+Alt+L 格式化 *代码* Ctrl+Alt+O 优化导入的类和包...](https://xuyuanzhi051.blog.csdn.net/article/details/105219829)[*IntelliJ* *Idea* 常用快捷键列表：让编码高效起来！](https://blog.csdn.net/Logicr/article/details/80616785)

[Logicr的博客](https://blog.csdn.net/Logicr)

06-07 2493[工欲善其事，必先利其器 Alt+回车 导入包,自动修正 Ctrl+N 查找类 Ctrl+Shift+N 查找文件 Ctrl+Alt+L 格式化 *代码* Ctrl+Alt+O 优化导入的类和包 Alt+Insert 生成 *代码* *(*如get,set *方法*,构造函数等*)* Ctrl+E或者Alt+Shift+C 最近更改的 *代码* Ctrl+R 替换文本 Ctrl+F 查找文本 Ct...](https://blog.csdn.net/Logicr/article/details/80616785)[使用 *IntelliJ* *IDEA* 的 *代码* 样式功能，编写美观又简洁的 *代码*](https://blog.csdn.net/u013643074/article/details/134508027)

[u013643074的博客](https://blog.csdn.net/u013643074)

11-20 2517[本文介绍 *IntelliJ* *IDEA* 提供的各种 *代码* 风格功能，帮助开发人员提高 *代码* 质量、可读性和可维护性，减少错误，提高生产效率。](https://blog.csdn.net/u013643074/article/details/134508027)[引入 lombok *简化* *代码* 及相关 *IDE* 设置](https://blog.csdn.net/ShawGolden/article/details/131555969)

[Just do IT](https://blog.csdn.net/ShawGolden)

07-06 566[简要介绍了 lombok 的特性, 以及如何在 maven 引入和 *IDE* 中的设置 *(*包括Eclipse 及 *Intellij* *IDEA**)*](https://blog.csdn.net/ShawGolden/article/details/131555969)[*IDEA* 部分基本设置](https://blog.csdn.net/weixin_45151795/article/details/104708294)

[段远山](https://blog.csdn.net/weixin_45151795)

03-07 279[1.字体 a.界面字体 File *\-* >Settings *\-* >Appearance & Behavior *\-* >Appearance b.程序字体 Edi *to* r *\-* > Colors & Fonts *\-* >Font 先duplicate（复制）一份，然后重命名，设置字体、字号、行间距 2.全局编码 File *\-* > Other Setti...](https://blog.csdn.net/weixin_45151795/article/details/104708294)[Deep *Code* 的主要发现＃ *3* ： *Java* 缺少“关闭”或“刷新”](https://blog.csdn.net/cunxiedian8614/article/details/105692126)

[专业的开发者“讨论”](https://blog.csdn.net/cunxiedian8614)

04-22 211[嘿， &# *3* 5821;&# *3* 5 *3* 28;&#65 *3* 06;爪&#2170 *3*; &# *3* 2570;&# *3* 8519;&#65 *3* 06;&# *3* 2570;&#2 *3* 569;&#12 *3* 16;冲洗&#12 *3* 16;或关&# *3* 8 *3* 81;（&# *3* 1867;&#210 *3* 5;2） De...](https://blog.csdn.net/cunxiedian8614/article/details/105692126)[*IDEA* *代码* 警告 *(*warning*)* 整理以及解决办法](https://devpress.csdn.net/v1/article/detail/119803040)

[笔墨的专栏](https://blog.csdn.net/csdn_mrsongyang)

08-19 5万+[Redundant boxing ins *ide* ‘Integer.valueOf *(*xx*)* ’ 解决办法： 改为Integer.parseInt *(*xx*)* 即可。因为Integer.valueOf内部调用了parseInt，会提示多余的拆箱操作 ‘xxx == null? false: yyy’ can be simplified *to* 'xxx!=null && yyy 解决办法： 按照提示修改即可。 *简化* 写法，原来写法比较啰嗦 ‘OptionalInt.getAsInt *(**)* ’ witho.](https://devpress.csdn.net/v1/article/detail/119803040)[打开 *IDEA* ，程序员思考的永远只有两件事！！！](https://blog.csdn.net/weixin_45479946/article/details/140305005)

[weixin\_45479946的博客](https://blog.csdn.net/weixin_45479946)

07-09 890[当年面试时背了很多八股文，但在日渐重复的机械工作中（产品业务开发），计算机网络、操作系统、算法等很多晦涩难懂的基础知识已在脑海日渐模糊，每天打开 *IDEA* ，思考的永远只有两件事。](https://blog.csdn.net/weixin_45479946/article/details/140305005)

评论

被折叠的 0 条评论 [为什么被折叠?](https://blogdev.blog.csdn.net/article/details/122245662)[到【灌水乐园】发言](https://bbs.csdn.net/forums/FreeZone)

添加红包

![](https://csdnimg.cn/release/blogv2/dist/pc/img/guideRedReward02.png) ![](https://csdnimg.cn/release/blogv2/dist/pc/img/guideRedReward03.png)

实付 元

[使用余额支付](https://blog.csdn.net/opperlili/article/details/)

点击重新获取

扫码支付

钱包余额 0

抵扣说明：

1.余额是钱包充值的虚拟货币，按照1:1的比例进行支付金额的抵扣。  
2.余额无法直接购买下载，可以购买VIP、付费专栏及课程。

[余额充值](https://i.csdn.net/#/wallet/balance/recharge)

举报

 [![](https://csdnimg.cn/release/blogv2/dist/pc/img/toolbar/Group.png) 点击体验  
DeepSeekR1满血版](https://ai.csdn.net/?utm_source=cknow_pc_blogdetail&spm=1001.2101.3001.10583) 隐藏侧栏 ![程序员都在用的中文IT技术交流社区](https://g.csdnimg.cn/side-toolbar/3.6/images/qr_app.png)

程序员都在用的中文IT技术交流社区

![专业的中文 IT 技术社区，与千万技术人共成长](https://g.csdnimg.cn/side-toolbar/3.6/images/qr_wechat.png)

专业的中文 IT 技术社区，与千万技术人共成长

![关注【CSDN】视频号，行业资讯、技术分享精彩不断，直播好礼送不停！](https://g.csdnimg.cn/side-toolbar/3.6/images/qr_video.png)

关注【CSDN】视频号，行业资讯、技术分享精彩不断，直播好礼送不停！

客服 返回顶部

![](https://i-blog.csdnimg.cn/blog_migrate/23279b92318045a5dd8af4994ba8ea99.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/d15f4ec7fcea08a6d7d87a4f1f334906.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/97216f4957a3397c3b19e57e08b9eb0d.png#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/879a3717a0a28963990bb5a73b180a0b.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/d0caa028842d531c77c2175fc6f6b3ac.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/bd604e17b6f38eb18bf32dabb38b41c7.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/7f290e068a76872d15b90a8359d35094.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/e946ae2a9af863f82c6af9583b64f3bc.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/38da3084ba121b38806f8ab759d58747.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/efa11244ba32868858e4d7ad521564d9.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/ebaf47e6ee302d4c0828072de396b75c.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/d4da344ded500edea94d09b1ab42e543.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/3f3c47927474e4e8c1fdd0c66515ffc4.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/878bf8bca22ac2cfde0056b3f1f8665a.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/07abbef0e981a61589347d0304624abe.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/6a38e5d4e796ffae2b3ac2ac54f90bb4.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/1f1a8b1bceee3184c0267f5782f60eef.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/b79ee3e5a10ae171f917f579731407b3.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/73da57964c2064c614c900afec069d35.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/2b764ef218f4e05e9c3be412b6d31609.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/42d4dd61ddcd65e6a400185303f01bf2.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/f6fcc83e8bffae85b4cb7c103c776a06.jpeg#pic_center) ![](https://i-blog.csdnimg.cn/blog_migrate/7e3218dfe1887cac0fca1ba67ffa9b6b.jpeg#pic_center)