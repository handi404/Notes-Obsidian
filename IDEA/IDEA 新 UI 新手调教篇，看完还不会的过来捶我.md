
# IDEA 新 UI 新手调教篇，看完还不会的过来捶我

__ _ _ _ _

我们知道 jetbrains 系列 IDE 在今年的第二个大版本 2024.2 已经把以前的经典版 UI 强行退位了，取而代之的是我们目前看到的新 UI。

其实早在 2020 年的时候，jb 就开始了新 UI 的计划，目的是创造一个现代化，轻量级，符合现代工业潮流的 UI。

到 2023.3 版本的时候，新 UI 就已经可以试用了。

新 UI 相较于经典版 UI，最大的区别就是更加的精简，不常用的功能在新 UI 里都是被雪藏起来的。

这直接导致很多用户的习惯性操作会得不到及时的满足。

我们一般认为一个软件的生命周期，3 年可用，7 年成熟，10 年可红。jb 已经走过了 20 多年，已经是 IDE 老司机了。最后希望自己的 UI
是更简洁的。这不由得让人想起那条少即是多的软件法则。

为了让大家轻松愉快的上手 新 UI，下面就来聊聊干货吧。

第一个，紧凑模式

启用紧凑模式可以降低工具栏的高度，让图标和按钮小一点，从而让编辑区域的空间看起来更宽敞。

修改位置，View | Appearance | Compact Mode。


![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLjicFfdibfw41ibLkYyUtOL0cB2VicIDaUCjnVnrxvc9cr4IlWFf86c9otA/640?wx_fmt=gif)

__  

第二个，显示主菜单到一个独立的工具栏

新 UI 默认在光标在左上角的横杠的时候显示主菜单。

这可能是大家一开始最不适应的地方，但是用久了，这种方式还是挺不错的。

可以在右边的主菜单空白处右击，选择 _Show Main Menu in Separate Toolbar，回到原来你熟悉的味道。_

这个只适合 win 用户和 linux 用户，mac 没有这个问题。

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLD5yqiaI6DuE67nuPQibhsPnncFic1Jn7K902PMKzygfqiaia4ia0rpXXsvug/640?wx_fmt=gif)

第三个，主工具栏中消失的操作按钮

这个问题在我们提交代码的时候会遇到，发现怎么没有 git 的 update 等按钮了呢。

当然如果喜欢用快捷键的同学不会在意这个问题。

要把消失的按钮加回来，很简单，右击主工具栏空白处，选择Add to Main Toolbar，选择语言显示的按钮。

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBL6UwibdTTtWrgSYcXkFiccjh6m8Rul3iauDj0q0Q1dic6hmrwvLtvsa4mWg/640?wx_fmt=gif)

第四个，导航栏去哪里了

现在的文件导航栏被放在了工具的底部，而不是顶部。但是说实话这个在开发的时候不会怎么看这个信心，所以 jb 就把它放在了不显眼的底部。

回到顶部的方式， _View | Appearance | Navigation Bar，选择_ _Top。_

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLaxkNQPO1iblOa9D9cS0Juviap0fttVHymOs1832dP7tGr20z97ToYSVg/640?wx_fmt=gif)

第五个，工具窗口的名字消失了

这个平时可能没怎么注意，新 UI 省去了一些工具窗口的名字，直接以图标示人了，就是这么任性！

但是无伤大雅！

要把名字显示出来，直接右击左边的空白处，选择 _Show Tool Window Names。_

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBL2AbxU5eibicA7km1ianlq77pm67Sb2dGU7LsbvLqVx6A96hicaWd0ia22Mg/640?wx_fmt=gif)

比如工具左边几个万年不变的工具窗口 Project，Git 等。

第六个，显示工具窗口头部图标

有些功能窗口的头部图标是不会显示的，除非你的光标惹到它，它才会冒出头来。

其中也包括代码折叠的图标。

要总是显示它们，也对应 2 个配置

Settings | Advanced settings | Tool windows  ,  勾选  Always show tool window
header icons。

Settings | Editor | General | Code Folding  ,  勾选  Always  ** ** next to the
** ** Show code folding arrows.

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLj908icTxFtOLktyYzKdGNyHoQyokhOJPicno7VQWov2jOs9yM4fH3CBg/640?wx_fmt=gif)

第七个，点击行号选择行

新 UI 点击行号的作用是打个断点，以前是选中当前行的内容。

要切换到以前的模式可以右击禁用， _Appearance | Breakpoints Over Line Numbers。_

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLBp46ecrGRn6uhdH4AfMuxyP0QZXibUQiaibosV4ibFnGIrSW3ZCK5yM4yw/640?wx_fmt=gif)

第八个，debug 中的计算表达式去哪了

在新 UI 中，如果不通过快捷键，我们是看不到 debug 中的计算表达式这个功能的，它被放到了三点菜单里面（  three-dot menu）  了。

我们右击 debug 窗口，选择Add Actions…，英文后面跟着的就是三个点。然后选择需要添加的功能。

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOXybGdibX4yIuZ2l4AjVGEBLcG53B6sh2JYUBicEF2nfpBQxZB5MUo9sFQ0I7oG01qkuxjQ3eOLdUMQ/640?wx_fmt=gif)

针对三个点，，无论是横着的三个点还是竖起来的三个点，这里也有一个规律，点击三个点就可以看到更不常用的功能。

最后

新 UI 是大势所趋，经典版预计会在 2025 年停止维护。据说 jb 团队 99% 都在用新 UI 了，所以开始学习吧！


