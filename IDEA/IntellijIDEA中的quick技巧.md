![cover_image](https://mmbiz.qpic.cn/sz_mmbiz_jpg/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMm0H4DVyHpkSWjKhITZeZxs9bhMibIfiaa9WnpwcxbDm11ttIxkBiaibFibg/0?wx_fmt=jpeg)

#  Intellij IDEA 中的"quick"技巧，真的挺快的

在 IDEA 中有一种容易被很多人忽略的一个特性，官方的叫法是 quick，翻译过来就是快。

quick 技巧实际上是一系列的技巧的总称，分为 quick list，quick switch schema，quick definition
，quick documentation 。

  
第一个，quick lists

对于喜欢使用 IDEA 快捷键的同学，一定知道并不是所有的操作都有快捷键的，如果你想让自己用到的功能都加上快捷键也不是很现实的。

  

一方面新加的快捷键必须是唯一的组合键，另一方面一个快捷键一般最大包含 4 个键，再多的话就算不上快捷键了。最少 2 个键，这是最好的，所以我会把一些默认 3
个键的快捷键改成 2 个来使用，每次就少按一个键，也算是对键盘的保护了。

  

那么这个时候 quick lists 就可以帮到我们了，我们可以创建一个 quick lists，然后在里面添加几个你喜欢的操作，然后给这个 quick
lists 一个快捷键，这样就可以通过一个快捷键访问到好几个操作了。

  

下面来实操看效果吧

  

先去到File | Settings | Appearance and Behavior | Quick Lists

  

![](https://mmbiz.qpic.cn/sz_mmbiz_jpg/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMicGwT6ibcb3Ilic22TO29apT7fOGTyMfD9KCkQQp8IZ4ZBUYWn452HT9A/640?wx_fmt=jpeg)

  

  

点击左边框框里面的➕号，添加一个新的 quick lists，输入显示的名字 Display
Name，这个名字会在你按了对应的快捷键之后弹出的对话框的上面显示。比如这里的 VSC list。

  

接着在右边框架里面点击➕号，添加操作到这个 quick lists 里面。比如这里的 commit，push，update project。

  

点击 OK ！就保存好了新加的 quick lists。

  

接着我们在 IDEA 中的 keymap 里面搜索，VSC list，或者直接用鼠标下拉找到 quick lists，点进去选中 VSC
list，给她一个快捷键，比如 alt v，v 取的是 vsc 的第一个字母。

  

![](https://mmbiz.qpic.cn/sz_mmbiz_jpg/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMHzCkt4xsYSzPpiblSMopCZQibNibHbQyyZCBmudAtPKjut421kIL5icHhw/640?wx_fmt=jpeg)

  

IDEA 自己也预置了一些 quick lists，有兴趣的同学可以去了解一下。

  

另外有一个最佳实践是，尽量控制 quick lists 里面的操作个数在 1 到 9 个，这样在使用快捷键弹出的操作列表，可以直接按数字来访问列表中的操作。

  

你也可以看到第一个图还有 2 个小箭头，是可以调整每个操作在列表的相对位置的。你可以根据自己的使用频率来把最多使用的操作排在前面。

  

第二个，quick switch schema

这个是 IDEA 给我们预定的一些 quick lists，比如切换试图模式，快捷键方案，主题等等。

  

你可以直接按 ctrl ~ 来调出 quick switch schema 。

  

这里以切换主题为栗子，当你选择了 Theme，然后在其中一个主题上停留了 3 秒，你就可以看到切换后的效果。

  

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMGQYuu2D7K0twMDiamDeZBIkFqib3LQfqysLVZ0q0PceFppOibsSS8fHfQ/640?wx_fmt=gif)

  

第三个，quick definition

这个同学们可能用的比较多，使用快捷键 ctrl shift i，使用这个技巧可以有利于阅读代码的连贯性，不被次要的信息干扰。

  

  

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMXGd46zcUO6IannnBs8MYUhU75dn5HvOY1rBeLCG5Twf86TOnqoFvqw/640?wx_fmt=gif)

  

  

另外这个技巧也可以帮助我们查看某些符号在哪里定义的或者如何定义的，比如变量，字段，方法，函数。

  

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMoje2LEYicZ9lzCSiaL6aib8zPJCeicJXM4bI2pRPIxwIJESZ0Gv9CG2W0A/640?wx_fmt=gif)

  

  

第四个，quick documentation

这个个 quick definition 有点类似，都是显示一些额外的信息，快捷键是 ctrl q。

  

documentation 显示的是/** */注释里面的内容

  

definition 显示的是 Java 里面单行注释和/* */注释。所以她们还是有点讲究的。

  

![](https://mmbiz.qpic.cn/sz_mmbiz_gif/G7QIhdahIOUAW5RgBv7R4R9D8DoLwlGMgCcYWueY6Os34ou2ia3UZ50kicWxbsRuXThGaicLEBGCOoNY9BtcEOCicw/640?wx_fmt=gif)
