![cover_image](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP5TSBex5ar88acAWVjsaxk4ibyjxXG8Oddy9OxiaYp8SBeibT3dEI9EW7g/0?wx_fmt=jpeg)

#  让 VSCode 更好用的设置——前端开发角度

##  前言

刚开始学习前端的时候，还没有 VSCode，那时我用的是
WebStorm（当时是学生，所以用的盗版）。开箱即用的体验让人爱不释手。后来由于办公电脑配置的下沉，以及它对 4K
及多显示器卡顿问题的长久不解决，再加上周围同事的影响， 最终一击是「配置同步」让我最终切换到 VSCode 。

在适应了没有单独的悬浮搜索框这一史诗级割裂之后，很快就摸索出了我个人认为好用的配置，下面就详细得说一说。如果有人觉得自己的设置比我的更好的，欢迎在下方留言然后附上原因和效果截图。

📖  默认的设置我基本不说了（除非非常好用），我就说我对于默认配置的修改部分。VSCode
中大部分配置都能修改，比如「是否在右侧小地图位置显示光标行」这种的都能，非常好。

##  样式

###  主题/字体

主题是 One Dark Pro：

https://marketplace.visualstudio.com/items?itemName=zhuangtongfa.Material-
theme

字体是 Fira Code：

https://github.com/tonsky/FiraCode?tab=readme-ov-file#download--install

Fira Code 是官方推荐字体，内部也在使用：https://code.visualstudio.com/docs/getstarted/tips-
and-
tricks#:~:text=zoomLevel%22%3A%205-,Font%20ligatures,-%22editor.fontFamily%22。

Fira Code 对一些符号的变体支持非常好看，如 ` === ` 和 ` <= ` 等（有些需要手动启用字符集和变体）：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPG7aMVwwzBnk07FES25Zz8YPwnjvFGdBKMSdUrjt212mW3o0fwn0Kuw/640?wx_fmt=other&from=appmsg)

很多人不习惯 Fira Code 默认的 ` & ` 符号，这可以通过配置来禁用它的变体，具体可以参看其 Github 的介绍，我的设置是：

    {  
      "workbench.colorTheme":"One Dark Pro",  
      "editor.fontFamily":"'Fira Code', Monaco, 'Courier New', monospace",  
      "editor.fontLigatures":"'ss01', 'ss02' off, 'ss03', 'ss04', 'ss05', 'ss07', 'cv29', 'cv28', 'cv13'"  
    }  
      
  
---|---  
`

另外，行高是 1.4，字号是 13。

##  编辑器

最主要的就是编辑器设置了，好的编辑器当然是为了提高编码效率，下面逐个说说。

###  渲染空白字符

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPZcKtEMFWESl2K16jH8gf6NPTbcPmHXRyXg4Kk3FvRuq9W2tdO6A4EA/640?wx_fmt=other&from=appmsg)

这个我是使用默认的 selection，即只在划选的时候，如果内容有空白符（空格）才会显示出来，否则不显示，不然影响美观。 ` boundary `
的设置是总是显示，不好看：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPl385ft0skTJCiccbSYDKhibtbRiarVkcibuwMcI1VFaNHIOjra6d9j9mwQ/640?wx_fmt=other&from=appmsg)

###  自动添加/删除配对括号

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPSiaT2N5ibWm9Z6MSTXIkMIgq2YeFD98DbS98Mjymyj533oWjmu41zDIA/640?wx_fmt=other&from=appmsg)

这个几个设置使用场景是，如果你输入一个起始括号，如 ` ({[ ` 会自动在后面给你生成一个 ` )}] `
，删除的设置也是同理。默认是插入的时候配对，删除的时候也同步配对删除。

###  括号着色（池）

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPBLxJOcfJ8z6a1uQAfeibLvvZWVFIdy6oDUcrW2RR0QoKEqswCPwqDrw/640?wx_fmt=other&from=appmsg)

第一个打开后，你的各个括号就会有颜色（而不是白色）。第二个打开后，每种类型的括号，拥有自己独立的一套颜色配置（其实也会不同的括号颜色重复，但不再是按不同括号的显示顺序，而是同种括号的显示顺序来着色了——我的理解和测试）。

###  矩形选区

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPhAS2DEt3PJUEXldx0KVL8KEicvAajg6mj9BTicrz8j1qUpsexHnBy2iaQ/640?wx_fmt=other&from=appmsg)

默认情况从上往下选择，如果经过某行的行首和行尾，是选中整行的：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP2PnQrTjDGpliaCAoKb1Pfm3RgcJFHwbvNAwM3UnxP02QcE99eZDLYuw/640?wx_fmt=other&from=appmsg)

如果这个开关打开后，就变成了鼠标划选是一个矩形选区（根据鼠标位置，而不是代码位置进行选择），常用场景是同时编辑多行类似缩进的内容，如 JSON 的键等：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPSehL6ibWOVMLib2gVwm3qkibdmlEoa7jpjibN8piaaocqz41AJWp1vznqTA/640?wx_fmt=other&from=appmsg)

![](https://mmbiz.qpic.cn/mmbiz_gif/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP0v79ab1JYtx4icYLHAlBWRrfuz2Cud1VodMRwbLhtXgoKTwkVtCQIpQ/640?wx_fmt=gif&from=appmsg)

多说一句，在终端中选中的时候按下 Opt 键，也是这个效果。

###  复制内容的时候带语法高亮

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPGkk4SIfiaEviamI5RgjdAKrje5iaLIHLBrQRmX5AicRdGv1wocYpV2EMyA/640?wx_fmt=other&from=appmsg)

有些富文本编辑器，没有特殊处理，因此在直接复制 VSCode
中的代码到富文本编辑器的时候，会将颜色也带上，这通常不是预期。此设置可以让你复制出来的内容不带颜色。

###  拖拽

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPYg2vy7JLrYyLJNCYvBwGtTAv1duibKa9xBkvicpzl59lexibibfdhAbPLA/640?wx_fmt=other&from=appmsg)

我写码这么多年，几乎没有使用「拖拽」来实现移动代码块的操作，因此建议取消。第二个按住 shift 后拖拽文件到
VSCode，如果是媒体文件则松手后只会显示文件名，如果不按住 shift 则会打开媒体文件，多一个功能挺好的，以备不时之需（这个默认是打开的）。

###  空选区复制当前行

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP1k7T5X5FGgMU9xWNYHu8aGWZogY0BSVBhrTF3xJ5FYLLq431f29SDA/640?wx_fmt=other&from=appmsg)

如果选区只是光标，没有选中任何内容，此时进行复制操作会选中当前行。复制当前行更简单了（默认开启）。

###  自动折叠

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPx6cY4mIQJKyDuF1Af3yg5rLLiaIUZeFUdnP2ZMkAgLasdppRbTf4Wzw/640?wx_fmt=other&from=appmsg)

代码折叠肯定是需要的。突出显示折叠范围也是需要的（会跟鼠标在那一行一行的效果，当前行高亮），不然不知道当前行是否折叠了。最后一个是自动折叠 import
部分，我觉得没必要。

折叠我个人喜欢始终显示，因为这个功能太常用了，我经常需要先 hover
到位置，看哪行是被折叠了，然后再点打开折叠，效率太低。我喜欢一眼看到哪些地方被折叠的，所以需要设置成 always：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPjCftQJbVIoymN0EfYF5xfpzmTXicHWqHTIeYxfOfs1yrVEZLUg4RLXA/640?wx_fmt=other&from=appmsg)

###  括号/缩进参考线

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPibqwcf66zwGFdIZ9icaR9Y7JQ19iccq2gA1wp9yhF1ktGgBBaibXBRsnzA/640?wx_fmt=other&from=appmsg)

如下图，不过我没测试出什么是「缩进参考线」，先打开吧。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPqe6bcibZqg9VMwAI96kU7IznLd8JPjW0F80Z3dZeLPI6iabqA7BfqVkw/640?wx_fmt=other&from=appmsg)

###  hover 时浮窗出现的位置

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPOhcibuB7UCQs3HPcWsYVh6oIHpmqU46vr4mibzF86Pz6oZiaGaFJ5GFQw/640?wx_fmt=other&from=appmsg)

一般情况我们看代码是从上往下看的，这个设置 hover 代码后浮窗出现在上方，挡住了内容，还得移动一下鼠标让浮窗消失再出现，建议取消。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPiaO3BWF8NYC1pVDKDEsibbjIZh0soJ8yfQPplUKIYJv5DNcvCJaOAkUA/640?wx_fmt=other&from=appmsg)

###  悬浮出提示

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPnVes3vLa45vUQsibMOwYjHLCNXn4fNlkut3oBAfUxDDSHnSzNjrsL9A/640?wx_fmt=other&from=appmsg)

鼠标移出一般就是不想让它显示，直接设置为 0。

###  鼠标缩放字体

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPEX7dALj3zFZhQeSkhHOjibzV3qRkkVtYicib5SUQE4uEbomTrbSslyN6g/640?wx_fmt=other&from=appmsg)

经常误触，关了。

###  编辑器区域顶部 padding

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPyJQ3MyD8KMw3KTe39zRbpib2rV3MjYOMazKicXHxsvCt1lRjFtfPqvFQ/640?wx_fmt=other&from=appmsg)

我设置为 2。底部 padding 就没必要了。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP0jFlEM5fFWHyc94NhAlQKvSfWiaDem8jnxVe9XArQCCZa744AV5bOZw/640?wx_fmt=other&from=appmsg)

###  滚动条

水平滚动条为 6 宽度，竖直为 25（默认水平 12，竖直 14）：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPCl8f0u1fxol0HgnnyObVLdUadTVHvKpC3JMPkcSyZEsT4F3LxMPP8A/640?wx_fmt=other&from=appmsg)

我个人是不喜欢滚动到范围外，会导致明明一屏显示完全的内容，出现滚动条，所以最后一个 Scroll Beyond Last Line 关了。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPTjd24YJTicnvcACyu73tbxfPn90Ap95XNd2Bhbhu7YPVIeWorD7m89A/640?wx_fmt=other&from=appmsg)

这里要说下为什么竖直滚动条调大为 20，因为在那个区域其实不只是滚动条，还含有三个信息：

  1. 滚动条右侧亮黄色的是编辑器警告信息。 

  2. 滚动条中间暗黄色块是匹配的搜索项（含全局搜索和当前编辑器搜索）。其中，暗黄色块也可能是灰色（表示光标选中的部分和类似内容），也可能是淡粉色，表示光标选中的的内容的声明处。 

  3. 占滚动条整行的蓝色线是光标所在的行。 

  4. 滚动条左侧的绿色部分是代码变动的部分。其中，也可能是淡黄色，表示修改部分（如果启用了 git 的话）。 

可以看到这部分的信息显示很丰富，所以调宽一点。

###  平滑滚动

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPE8FugIuJ7GkEyNr4tJyPfic3x7gMn75ibNJ7oqz6WU52vXokptnxQUkg/640?wx_fmt=other&from=appmsg)

强烈建议开启，这样在滚动的时候就可以知道你大概滚动了多少行，而不是突然跳过去，「不知道滚动到哪里去了」。

###  滚动吸顶

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPRd3E6pKiczZshUWrCsYBn1ZqKib88JkvBtNhgLsGV5CalYmZTwgLEtzQ/640?wx_fmt=other&from=appmsg)

滚动的时候可能需要查看超出当前屏幕的作用域，打开该选项即可。另外，水平滚动的时候会把该 sticky 的函数滚走，我倾向于不滚动它，所以把最后一个选项取消。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPecxlXUib16gKvibbicIhbLiaY9lQ4ia17LVxoibTJEDOpAV0ibOYuibwolcN4g/640?wx_fmt=other&from=appmsg)

###  光标

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPlCRkgEPQx73cIAHCcF7iaAa84lP0fdDQuvCvAibevicHv6xk50icgOzn0g/640?wx_fmt=other&from=appmsg)

第一个是光标闪烁的淡入淡出，第二个是你在点击不同位置的时候，光标是从上一个位置动画移动到点击位置的，可以让你知道本次点击光标位置相对上一个编辑位置是哪里，信息更丰富了。

###  查找

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPg7RjVs67HIl6tJQsZqGP0HygGqmMQgN3UESONMnS0SEIkqjiasRZUWg/640?wx_fmt=other&from=appmsg)

这个建议关掉，搜索的时候，如果不关，会在文件顶部凭空产生一些距离导关闭搜索框的时候编辑器跳动一下，难受。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP8kgObdcq1icXkokqvrW27urtDbGbAYskJfR0fHaC4GJbcR5G6QupTlw/640?wx_fmt=other&from=appmsg)

不过该选项打开后可能会遮挡住编辑器内容，自己取舍（一般顶部都是 import 后的换行内容，挡住也无所谓）。

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPib55yWPMJVkxLiaBRON4FEnQZTTiasMzPmRHSgib2O6DxoeUPbtzdiaicVTA/640?wx_fmt=other&from=appmsg)

###  自动带入搜索小组件

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPiaiaezsaqGBSYG1qvTjQLqtoYbj6EVlzkpgyIntwfnSpa1vU4axGN5AQ/640?wx_fmt=other&from=appmsg)

这个建议关掉。我经常会使用搜索，然后搜索后选中某个内容后再搜索（非选中内容），此时编辑器自作聪明的把我选中的内容给带到搜索框中，导致我之前搜索的内容没了，很烦。

###  缩略图

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPsFZsrkibCic8wXicdxpJ96x2uRjKnzuwtnHe7QXCt2aiaYUGmLf1eMucJQ/640?wx_fmt=other&from=appmsg)

编辑器右侧的缩略图我始终显示出来，它的作用一般是让我知道我当前处于编辑的哪个位置，以及相对于某个函数、组件，我所处的位置，因此我需要缩略图不滚动，同时仅渲染色块即可，不用将每行都渲染出来。

###  建议

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPRHDx3dgfiavF1tO3rDjmvSb307ibj7CMkRJbBuraLa3Y6gQFhKa4912w/640?wx_fmt=other&from=appmsg)

这个开关建议关闭（默认），因为可能跟 copilot 建议弄混淆，如图是 copilot 的建议：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPAxib7NibIpvnMgGHXI7EGCMfr60A4sM3JrZSMQvBpRrSicniaape7RJfhg/640?wx_fmt=other&from=appmsg)

而这个是预览的建议：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPPuLW01eOeZLQlDUctecrvibfAR8MHhhnEVl6jZA8dLodXfu4gzcS9Qg/640?wx_fmt=other&from=appmsg)

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPgl8C5LicN28pzKPQ4P5NU7xBYbom0QQfKwV02icbtoZPqxw728rvVM3Q/640?wx_fmt=other&from=appmsg)

这个选项默认是 first，即始终使用默认选择第一个建议，但是我经常遇到的问题是，在 CSS 中，我输入 ` wid ` 当然预期是 ` width `
，但是它会给我建议是 ` widow `
我当然不用这个属性，但每次都是排在第一个，我就每次需要通过箭头来切换，所以此处建议修改成「最近使用」，类似与输入法的「动态调频」:

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPcKqgmb8AF7mJGphZX6Jbudpl1tBiaXIiaqvjAH7I0oIWtHmV98sDibPLA/640?wx_fmt=other&from=appmsg)

##  工作台

###  命令提示框

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPzJ6bWDNtzTFyS9y3NqDUDdae1mGMR9icrhuSfuwnfiakoZgic4dviav1XA/640?wx_fmt=other&from=appmsg)

有时候会经常反复输入一个命令，所以打开这个历史命令列表很有用。除此之外，保留输入内容也很有用，比如以 toggle 开头的命令（如 Toggle
Screen Capture Mode）。

注意，如果输入内容后按了 esc 导致输入框消失，下次再次唤起不会保留输入内容，只有选择了一个命令执行后，再次唤起，才会保留上次输入的内容。

###  目录树

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPMa82cbHxtL9Bk3Fic8ScaOw4I1ibcicmCiblPaWwMGDv16zWI5ZA72aHrA/640?wx_fmt=other&from=appmsg)

一般动画我都会打开因为「优雅永不过时」。这个设置也影响「设置」界面的滚动（之前对编辑器设置平滑滚动不会影响「设置」界面和目录树界面的滚动效果 ）。

###  快速打开记录历史

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPuc4oJo6XxOKlSiboREr2TibrOkiaAKfibfdm8s51S3DdLX3162zqgyibjJw/640?wx_fmt=other&from=appmsg)

按下 cmd + p 会出现 quick open
输入框，记住历史挺好的。另外还有个选项是失焦是否自动消失，大部分场景下需要自动消失，偶尔不需要，先保持默认自动消失了。

###  工作台减少动画效果

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPuE6IYOgRh0QVDE0HG4vKqbTJbicic76IYtFl5JaXOYloQSm3k1pw3Icg/640?wx_fmt=other&from=appmsg)

我的 64G 内存 M1 Max，不需要减少动画（默认是 auto，根据系统配置自动适应，适用于多台电脑间配置同步的问题）。

###  字体平滑

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPJrUrvwcMTFs6mZjPzvUjzchoYdy6IhIJ3ta57UxTDvcwQwmm1FNiavg/640?wx_fmt=other&from=appmsg)

类似于 css 中的 ` -webkit-font-smoothing: antialiased; ` ，default 用于在大多数非 retina
屏上显示最清晰的字体（次像素级），antialiased 是像素级平滑，可能会导致字体更细，见图：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPxnicYE5KVYJ3LxMtS2GKkXbGXLS82aeiaQrnHuDzOUiaOjx3mKQvkeKpg/640?wx_fmt=other&from=appmsg)

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPSEniaXoXMOHT4LD2mZvZSr7ZDMWD8MoqY4jW5TldiaAQpnWBf6HEjJHw/640?wx_fmt=other&from=appmsg)

这个设置虽然是在「工作台」块，但是也影响编辑器区域。可以看到开启了 antialiased
的时候，无论是编辑器区域还是工作台区域，字体都更暗（对比度更弱）、更细了。我喜欢后者，所以开启了。

注意，这个「次像素级」，并不是说比像素还小的级别，而是指「还没到像素」的级别，意思是更低级，而不是更高级。

###  目录树 sticky

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPo5eZqPRMRxWXY3lPxvwGicwADhzeC2OZAiaiawKrGMZ9KeHWuO1W0nCuQ/640?wx_fmt=other&from=appmsg)

非常好用，滚动的时候可以知道当前的滚动路径，唯一美中不足的是如果能加个 box-shadow 阴影就好了，不然不太好区分的：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPtuv1Z0puVcq5dx1YEEkBVF7lLwWuuhpeY4e98kG7t3TK0Zc0EcA55A/640?wx_fmt=other&from=appmsg)

sticky 的最大级数也可以修改，默认是 7，足够了（编辑器 sticky 默认是 5 级）。

注意，此设置也同样适用于「设置」界面（原来设置界面属于工作台，而不是编辑器）：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPicMnCZmWGWRasIIHD2tfayPwlOgblUQlsAKgkfeJSXdUk7HmfceD9SQ/640?wx_fmt=other&from=appmsg)

目录树的缩进我改成 14 了，参考线我喜欢始终显示，不然同级文件太多，不好找：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPXMz9KIIACf2nx9OPacKCXdVS4VjOBKIkV8uNkOdqer7MiaAdvthIhxQ/640?wx_fmt=other&from=appmsg)

###  目录导航

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP6poOE8kB6Ny7pseqWs6opA2mVd1dujC5HJVrwrX5AtSuVaCk3g6AJA/640?wx_fmt=other&from=appmsg)

目录导航还是需要的，但是不需要文件/文件夹 icon，这样可以显著的和文件内的数组、类进行区分，非常好用：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPZia9L1dlbKQZtCVRbbeMEOqb9gFnvGAALRv6JQ0abcCFh4RNzBMlHtQ/640?wx_fmt=other&from=appmsg)

###  修改过的 tab

与此相关的有多个，如在修改后未保存的文件上方显示高亮线：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP593ibhxiaNY1SzZzNYXy5wKZ31kAXf0EvO9pGtNyk7ib5pp3micAkq0NkQ/640?wx_fmt=other&from=appmsg)

默认显示的是点，此选项打开后，会点和线同时显示，重启编辑器会只显示上方蓝色线（可能是 bug，其实应该不用重启编辑器也能生效）。

效果：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPd2fTGf7Gj5zMuBN2TicxlI74VHBmKicqSugaazyaaRvn9aiaDJHIk2CIw/640?wx_fmt=other&from=appmsg)

因为「点」也占用一部分的 tab 空间，会导致无法显示更多 tab 内容信息，所以建议打开该选项。

###  鼠标导航

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPicyDdLBdqgImtkSglBV2icqDg52xMGMDHsjZB9CVI7erHE1E7dIuwZ0A/640?wx_fmt=other&from=appmsg)

这是个默认选项，但是我也说一下，对于有左侧按键（右手），也即 4、5按键的鼠标而言，的鼠标直接就可以用来导航，非常好用。

###  tab 固定

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPx7PMhNnCu4wiaTgSpNDUoOJfia5NFiamPF98ujnw7VwccVd7ZXn4XYicxw/640?wx_fmt=other&from=appmsg)

固定后的 tab 默认出现在编辑器组的左侧，但是如果将其单独排成一行会更直观，与非固定的 tab 区分开，效果如下：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPNKfkZLYF8wR6nGGZoel8kcKhW1gFrUa6cIsPI6r1ELibR7c69ic3FJbg/640?wx_fmt=other&from=appmsg)

注意，默认情况下，固定的 tab 是无法通过鼠标中键或者 cmd + w 关闭的（按下会打开非固定 tab 而不是关闭固定 tab），此行为可以修改：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPnG5JNvvXRI6K15NPiboMwohvBwh7ZCRWict8wia5uBMiaYpOQN0O2GohsA/640?wx_fmt=other&from=appmsg)

###  tab 关闭按钮

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPHbcqT4hIkoicxTuibUiaiayGib4BZ6K6VtUlO2ZQ60DO9jroZw5agVEzNyA/640?wx_fmt=other&from=appmsg)

一直使用左手 cmd + w 关闭 tab，所以此选项可以取消。另外，我其实更习惯双击 tab 关闭，但是官方回复不会做，见：

![](https://mmbiz.qpic.cn/mmbiz_png/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPAo20uTicaMq3xVYXrJr1IahZK9RG8dc4w38915VqfwkBUnYPdxAYA2A/640?wx_fmt=png&from=appmsg)
Allow to double click on a tab to close it · Issue #52628 · microsoft/vscode
Some of the editors like notepad++ provide Double Click to close a TAB. It
would be a great edition to VS Code.
https://github.com/Microsoft/vscode/issues/52628#issuecomment-420887497

###  tab wrap

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPvcuaSAlaGCtBaIFfNE2r35wGTxxl13AgQboorCnO5ePicoxsibUY5gew/640?wx_fmt=other&from=appmsg)

如果打开 tab 较多，滚动 tab 的时候就会比较费劲，无法掌控全局，所以我喜欢 wrap tab，效果如下：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPjicltn5p03mFC3T6tnJibsyNRgp7MajbEuV40xicf7LY1ZF784lNMYTOw/640?wx_fmt=other&from=appmsg)

比较尴尬的一点是，wrap 效果产生的多行 tab，可能跟上面提到的「修改 tab 上方蓝色高亮」搞的比较混乱（蓝色的线不知道是上面 tab 的还是下面
tab 的，得反应一下不直观）。是在 tab 显示更多内容，还是更直观，自己取舍：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPVXmqHsZSsboCzeHJLJyJLTU1dA6O2lmoBtKicVVu4hia2z2wqIo0Xelw/640?wx_fmt=other&from=appmsg)

###  tab 高度

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPdnPBxyicJD9fc25XibfeicHeEiad50BgYuC7msTa4BAc1tlZZAUqGlkrrQ/640?wx_fmt=other&from=appmsg)

紧凑布局有利于掌控全局+不占地方。

###  双击 tab 关闭（？）

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP6jjHa4vMM8RdPh1ZHjaZDQWEg1VdtLk8fjY4FRtgx5K6E4DNBBJZhQ/640?wx_fmt=other&from=appmsg)

看字面意思这个选项是官方号称不会做的「双击 tab 关闭」（如上面所言），但即使我关闭了可能会冲突的「双击 tab
自动扩展编辑器组」，该设置依然不生效，不知是不是我理解有误还是 bug。

###  原生 tab

与此相关的有两个：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPTnkDGI0vuLI1lUSVLMuWHvtUndwCiacTJyfvJ4iafK0alicCIYtClEUAQ/640?wx_fmt=other&from=appmsg)

第一个设置，启用后，可以将多个项目窗口，合并到一个窗口。「窗口」选项中会出现「合并所有窗口」的选项，这样可以在一个窗口中来回切换多个项目，非常好用：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP8lZ41rvSvjUBcnRh9mAZE3tfnYrO0OBZCgIvRIsibpGKsgE7VebwVvQ/640?wx_fmt=other&from=appmsg)

但是，这样的话就无法使用自定义的标题（其实我觉得也么啥用），自定义标题是这样的：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPU2e6pX4ZZqwC2QcIco8EhTDED49nW8IILaniaPOcX37ptnjx51HItcQ/640?wx_fmt=other&from=appmsg)

第一个设置如果打开了，那第二个就无效了，无论设置为 native 和 custom。如果第一个设置不打开，第二个设置设置为
native，那就没有「合并所有窗口」，也没有「自定义标题栏」（不知道这个设置意义何在）。

###  目录树拖放

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPEVOUynWyN2iantJU1ZOBJSX6rPus0DYvMUVHHoeLWwzzI48wIicakR9g/640?wx_fmt=other&from=appmsg)

我经常误触，然后导致上百个修改…所以关了。

###  搜索结果自动折叠

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPfp3vv0qL05ia4lG0PunQU3Drr3yn9jiafIukNicUcx2VEPuiaiafk29Spqg/640?wx_fmt=other&from=appmsg)

默认总是展开，但是如果搜索结果过多（通常是因为你还没有输入完成），此时展开是没有必要的，而且会耽误你掌控全局。

另外，如果你没有在搜索栏中加入「排除的文件」，那么也可能出现海量搜索结果，如 NextJS 项目的 .next 目录等，因此此设置也是必要的。

需要注意的是，这个「展开」、「折叠」的 10 个文件限制，指的是搜索结果中，出现在某个文件夹下的文件数量，而不是整个搜索结果的文件夹数量：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP4iaR5w60cXxnlkia9y9fejLiauTCHXa1z66ZUqerxC47VfrQJ3BJibTmLQ/640?wx_fmt=other&from=appmsg)

因此，如果某个文件夹下，出现符合搜索结果的文件过多（文件夹被折叠），通常你就需要检查是否需要提供更多搜索信息了。

###  搜索框自动填入选择内容

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPjBHfU47pTKCzFah4JNJMCThkzXHAIu4XvO35OrU7cDuh1eLbSzUYAQ/640?wx_fmt=other&from=appmsg)

通常你选中一个内容后想搜索它，因此「Seed On Focus」此选项让你可以节省一个 cmd + v 的操作。

注意，这有区别于「搜索小组件」中的
选中后聚焦到搜索时自动带入。因为在编辑器中你去选中内容，然后聚焦到搜索小组件，不一定是为了搜索，还可能只是为了简单在当前编辑器高亮选中的相同内容以便于查看，但是此时选中后聚焦到搜索小组件，就自动替换成选中内容了，很多时候不符合预期。

而如果你在选中内容后，聚焦到搜索视图（右侧），那大概率是为了搜索内容。

另外搜索结果我会想知道它所处的行号，以确定它在其文档中的位置，所以显示行号也是很有必要的。

最后的 Smart Case
算是一个小技巧，如果都用小写，就表示自己记不太清搜索名字了，如果很确定搜索内容（如驼峰的函数名）的某个字母是大写，那么就区分大小写进行搜索，非常好用。

除此之外，如果能记住上次输入的内容，其实记住也是选中状态，如果不符合自己的输入预期，直接输入内容即可，对自己即将想要搜索的内容没有影响，而如果之前搜索的内容还有用，那岂不是更好？↓

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPMQyalNW1BukHweMwB4VrEqXBYeGgAj3vEWGyoEeES5aYlCFcU5rcMw/640?wx_fmt=other&from=appmsg)

###  搜索忽略全局的 ignore

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP3ficmtQ0qg4ia7XdAvV4obKr4u3kKKKutDLzeYtmgrg8WfSKcOiasj48Q/640?wx_fmt=other&from=appmsg)

git 有个全局默认的 ignore，打开该选项可以在搜索的时候将其中列出的文件、文件夹忽略掉，通常是有必要的。

另外还有个在父级目录中启用 ignore，没明白什么意思，可能是多级 git 管理吧，我也勾选上了，既然都 ignore 了嘛：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPSO1icibe1eGjOuGvMcibUMVBpT4eF7fL6pZ21dRbxlwc7lyRXqqnYbcTQ/640?wx_fmt=other&from=appmsg)

###  调试和测试

恕本人技术水平有限，VSCode 的调试和测试功能用的较少，只用来调试过诸如 NextJS 这类的 NodeJS 应用，使用起来跟 Chrome
差不多。因为用的少，所以没发现有什么痛点，所以没有什么配置可以优化的，这里就不说了。

###  文件修改效果

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPCXic8VvOjspicGU2uc9Wr7FCM9Wic4icib9aS7BLRrVjTXtc3blDEATjvFQ/640?wx_fmt=other&from=appmsg)

在显示行号那一列，可以设置是实线还是「装订线」来显示差异，如：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPo1NHLkicmciaAMCMRMdofAvzsDic9HpgT3nGcqdhoE4s1McnO9PPEzBBw/640?wx_fmt=other&from=appmsg)

我更喜欢实线，所以这两个选项都取消了。

###  取消 git 提交按钮

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPB524bX1altEFFTRBXoT0USFuxyXbh2aQ9Byziauicsic9dWCOdYXeERJA/640?wx_fmt=other&from=appmsg)

说实话，左侧的这个提交按钮我从来没用过，都是使用命令行操作的 git，所以这个选项我取消了。

同理，这个按钮（看起来是 github copilot 的按钮，自动生成提交注释），我也取消了，尤其是对于公司项目，强制要求输入内容带上需求/bug
卡片编号的时候，这个智能写提效信息就更没用了：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPQkPwD8iaOEe4YltncSo2QkL6WfZvJWmkyNn3mF4zudBt86mpGThgcVg/640?wx_fmt=other&from=appmsg)

##  扩展

###  取消通知

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP3xXxaQyc6zT6kAUqG5ic4aXIqsbU4Qh9sr4tgMV2gicGgYibglFkiaS8UA/640?wx_fmt=other&from=appmsg)

我不需要任何扩展告诉我应该怎么做，如果有需要，我会主动找它。

##  终端

###  右键行为

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPFnu3WicAVHTXTXNotPueytL1A5XoVOyeQwkhYrM5TC68dEicONy46BnQ/640?wx_fmt=other&from=appmsg)

一般是鼠标左键选中后，右键出上下文操作。但 VSCode 默认行为居然是选中内容（单词）后出右键，可以，但没必要。

###  终端最大行数

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPO7EgKsoehjm2d7hCdDicWNzNfh89cMVPUBWYw5LKiawmhT9L3ficHr1zw/640?wx_fmt=other&from=appmsg)

这个其实改不改都行，我偶然情况下需要看很久之前的 log 信息，加上我的 64G 内存，调大点无所谓。

###  终端滚动动画

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPpuClmXZqSvnXric0WhytRoHrnXQzOzWaFiaTQ8RBJAnnqNAJ0TiaiahDVA/640?wx_fmt=other&from=appmsg)

虽然我喜欢动画（优雅），但是很奇怪，在终端的动画滚动似乎有点惯性，很难掌控滚动量，跟编辑器或者工作台内的滚动效果有很大差异，所以我关了。

##  CSS/Less/Sass

###  lint 重复属性警告

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPLZcTdGn58XCmdnJ6hBjhptysxkPp412rW2P3YA41iaxSN5zibn6CUlbw/640?wx_fmt=other&from=appmsg)

这个很有必要，有时候你是从外面复制多个属性值粘贴（常见的是从浏览器检查元素的 style 上复制），然后去除重复的属性。

##  Git

###  自动 Stash

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplP8szNhMexwicJeBnObKrs2vbwz2F4aqQ4oDLgW7Z1vbASibPMc3YmbWiag/640?wx_fmt=other&from=appmsg)

如图，描述的很清楚了，建议开启，少一步操作。

##  第三方扩展

其实没什么好说的，毕竟都装扩展了，肯定是有自己的需求才会装的，所以按照自己的需求配置即可。

###  GitLens

不过有些插件，是可以关闭付费推荐的，对，说的就是 ` GitLens ` ，在我（意外）查看 git
分支合入情况的时候，会触发到付费功能的提示，这个可以关闭（感谢插件开发者的大度）：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPPU862xia8EQGoNX8MgvTybV3lrIw07ray0TQrTibCkMYhvYiaPU7fHNFw/640?wx_fmt=other&from=appmsg)

###  One Dark Pro

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPJ6AErqekgIHwBb1nIYwhZL65Ycfrg5HS1ibOkhwrqlic8zNDWe3ett6Q/640?wx_fmt=other&from=appmsg)

这个我喜欢，更显著的看到方法、函数名：

![](https://mmbiz.qpic.cn/mmbiz_jpg/FgQBGjCgibicxCSgGfDict8NmykgrZhyplPCfE24Z03Bf1Rk2Qqfn6hXia4iaiandfavibXszj69DoPOkMyiapicRvge5lg/640?wx_fmt=other&from=appmsg)

##  后记

说了这么多设置，适合自己的才是最重要的