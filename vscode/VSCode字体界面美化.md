
## 必看！VSCode美化教程😍

#### 🆙2024更新

fix: 修复右键菜单字体没有生效的问题

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6e666522ec4c37bed06c5d4d77b22b4c.png)

___

### 前言

VSCode是一款轻量级开源编辑器，在Windows版本中默认的UI其显示效果肯定是没有在Mac中好的，其中一个重要的原因就是Mac所使用的中文字体 `苹方` 和英文字体 `SF Pro` 以及Mac独特的渲染机制，使得界面看起来更加舒适美观，所以本期教程教授大家如何更换VSCode的界面字体，以达到在Win平台下所能达到最佳的视觉体验。

觉得教程对小伙伴有帮助的记得动动小手点个赞或者收藏哦！

### 效果图

首先先直接上效果图。

#### 默认界面

在windows下默认界面的中文字体为Microsoft Yahei，即微软雅黑，英文字体为Segoe UI，代码字体为Consolas，图标风格为VSCode默认的图标。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a449aabb6eb30cd8ad879b53585e1914.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d4a8b8339b246e260c627fdb6e0687d0.png)

#### 美化后的界面

美化后的界面，中文字体为鸿蒙字体，英文为Inter，代码字体为Jetbrains Mono，图标风格为JetBrains系列软件（IDEA、WebStorm等）的图标

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/00c59619fdb52291f950e8300b2ee7fb.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/4b06933249fafa5c98b12a52546ba89d.png)

好了，接下来赶紧进入教程吧~

### 步骤〇 更换文件图标主题

该风格为JetBrains全家桶系列软件新的产品图标，在VSCode插件仓库搜索JetBrains Icon Theme即可

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/96c5bfd0eae489ab853408241ce746cc.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/16332b516e3376bb2fe240459873bc58.png)

### 步骤一 准备字体

默认界面的中文字体是微软雅黑，英文是Segoe UI，所以我们对应的也要寻找美观大方的黑体字，这里我推荐英文字体使用Inter字体，中文字体使用鸿蒙字体

#### Inter字体

官网链接：[https://rsms.me/inter](https://rsms.me/inter)  
**为什么选择Inter字体？**  
在选择UI界面的字体的时候，屏幕可读性是首要考虑的因素。选择Inter字体是因为在多重分辨率下仍具辨识度，更有利于保证内容在屏幕上的正常显示与排版，而且字形与Mac的SF Pro字体相似，果味十足，因此许多知名软件也使用了这款字体。  
例如**Postman**，著名的原型设计软件**Figma**，还有JetBrains最新全家桶，例如**IDEA**、**Webstorm**等也是用Inter作为默认的UI字体，等等…

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/090dcaf8ee3360d046ecbf1d46e5603b.png)  
排版效果  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/572b5c19d446520005683b0fbcf8eea6.png)

#### SF Pro字体（仅做了解）

Apple官网现在用的就是这款英文字体

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/64dbb6680b00dad1f0f80f1efb55fe5e.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/156d2abb69be5409cef05e69c7d69912.png)

#### 鸿蒙字体

由华为开源的一款不错的中文字体，其他厂商开源的（例如小米的MiSans、OPPO的OPPO Sans、阿里巴巴的普惠体）也可以自行选择

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/78a00a8e78a0eea3327d0bcae8d1e9f8.png)

**Q:** 鸿蒙也自带英文字体，为什么不直接用鸿蒙的？  
**A:** 因为鸿蒙的英文字体作为用于UI界面的字体而言，它的宽度和字形还是没有Inter舒服，博主还是更加推荐Inter字体

#### JetBrains Mono

比起默认的Consolas代码字体，我更推荐使用JetBrains Mono字体，由JetBrains所设计和开源

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f540887004dce30ccca64271acb46548.png)

支持连字特性，更加利于阅读

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a4047716063cfe6d29238c7a16a060ef.png)

#### 字体下载地址

**Inter下载:**  
由于Inter是开源字体，所以可以到GitHub下载，或者从网上搜索下载  
下载地址： [https://github.com/rsms/inter/releases/](https://github.com/rsms/inter/releases/)  
下载完成后**解压**，**选中以下三个字体(ttf 文件)** → 右键“**为所有用户安装**” 即可

![安装Inter字体](https://i-blog.csdnimg.cn/blog_migrate/03cc4ce8207af8fda111617455a27fff.png)

**HarmonyOS Sans**  
鸿蒙字体可以到官网直接下载  
下载地址：  
[https://developer.harmonyos.com/cn/design/resource/](https://developer.harmonyos.com/cn/design/resource/)  
下载完成后**解压**，**选中以下字体** → 右键“**为所有用户安装**” 即可

![安装HarmonyOS Sans SC字体](https://i-blog.csdnimg.cn/blog_migrate/47ac1c6b0ac22ccf103ee0404529c70f.png)

**JetBrains Mono**  
由于JetBrains Mono是开源字体，所以可以到官网直接下载或者到github下载  
下载地址：  
[https://www.jetbrains.com/zh-cn/lp/mono/](https://www.jetbrains.com/zh-cn/lp/mono/)

下载完成后**解压**到任意位置 **打开ttf文件夹 → Ctrl+A 全选然后右键 → 为所有用户安装** 即可

![安装字体](https://i-blog.csdnimg.cn/blog_migrate/5750692219f5d1abc1fd8bb6a3a0b4bd.png)

### 步骤二 更换字体

#### 打开VsCode的css文件

打开VsCode安装目录，按照下图或者类似路径定位到 `workbench.desktop.main.css`，如果你是默认安装的话则路径为以下路径

```
C:\Program Files\Microsoft VS Code\resources\app\out\vs\workbench\workbench.desktop.main.css
```

以下为每层目录的点击过程

1.  进入resources文件夹  
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8879afc207535f6367cdc24ec9ec9fe8.png)
    
2.  进入app文件夹
    

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/054d6737b4b234a1469caba94198e71c.png)

3.  进入out文件夹

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/29a515ebf4bbd19e3ea6632971031f87.png)

4.  进入vs文件夹  
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/487c4b26748001bbbd802bab28c9256c.png)
5.  进入workbench文件夹  
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/65e4fb633feabd706c4c671a6a034bcc.png)
6.  进入api文件夹，找到workbench.desktop.main.css  
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3797d97a18e21eda3f527c51cc5c7038.png)  
    然后右键用VSCode打开

#### 修改css代码

Ctrl + F定位到这段代码

```css
.windows{font-family:Segoe WPC,Segoe UI,sans-serif}.windows:lang(zh-Hans){font-family:Segoe WPC,Segoe UI,Microsoft YaHei,sans-serif}
```

然后修改为

```css
.windows{font-family:Inter,HarmonyOS Sans SC,Segoe WPC,Segoe UI,sans-serif;}.windows:lang(zh-Hans){font-family:Inter,HarmonyOS Sans SC,Segoe WPC,Segoe UI,Microsoft YaHei,sans-serif}
```

`.windows` 表示英文界面，`.windows:lang(zh-Hans)` 表示简体中文下的界面，暂时修改这两个即可

**注意：一般为英文字体的名称在前，中文字体的名称在后，上述为Inter, HarmonyOS Sans SC，意思是先使用Inter的字符，没有的字符则使用HarmonyOS Sans SC的字符候补，以此类推。在这里主要想使用Inter的英文和符号等字符，以及鸿蒙的中文字符。**

然后，由于此时编辑器的右键菜单样式不会受到上述css影响，因此还要单独修改，直接在当前css（workbench.desktop.main.css）最后一行加上这段css即可

```css
.shadow-root-host { font-family: Inter, HarmonyOS Sans SC; }
```

**❗❗❗注意鸿蒙字体要写HarmonyOS Sans SC**

Ctrl + S 保存，如果提示权限不足，则点击 **以管理员身份重试…**

#### 修改前

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d0db59f0b23ec9d4d709ff25792408a7.png)

#### 修改后

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7daed304791fc14464ac530a6dfcffe8.png)

按Ctrl+S保存的时候可能会弹出

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/059d9f11814f3e8c655c2c99d2116159.png)  
点击 **以管理员身份重试…** 即可

#### 重启VS Code

然后关闭VSCode重新打开，右下角会显示Code安装似乎损坏

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/bf3fbca1cc2198887557649ae37301f7.png)

可以点击设置按钮然后点击“不再显示”

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/89564bbbd3877d2175210231e1021a22.png)  
到此处VSCode的UI界面字体就配置完成了，接下来配置代码的字体

#### 设置代码字体

接下来设置一下代码字体，代码的字体一般会显示在编辑器和VSCode的终端中，由于VSCode编辑器区域默认没有设置中文字体，所以默认中文字体显示宋体，英文字体默认设置为Consolas

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d4a8b8339b246e260c627fdb6e0687d0.png)

接下来我们打开设置

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/258174db50d1d74ff777077589ec1804.png)

可以看到默认的字体设置是

```css
Consolas, 'Courier New', monospace
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c43d101f634ad1b0e502ffe8a136eb1b.png)  
我们将其改为：

```css
JetBrains Mono, HarmonyOS Sans SC, Consolas, 'Courier New', monospace
```

这样英文代码就会使用JetBrains Mono，中文就会使用鸿蒙字体

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/836dda409b8930f1d77673fc2b432261.png)

Ctrl+ S保存，这样就可以了，会在编辑器实时显示效果

___

以上就是VSCode的美化教程！

### 不足之处

每次更新VSCode都会覆盖这个css文件，导致样式失效，需要手动再次修改