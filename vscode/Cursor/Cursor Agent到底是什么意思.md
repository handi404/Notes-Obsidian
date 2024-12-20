## Cursor 重大能力更新：Agent

## 功能概览

Cursor 4.3版本推出了一系列创新的Agent功能，显著提升了开发者的编码体验。我来给大家介绍一下这次更新内容，特别是agent的几个主要能力。

但是网上所有视频、文章都没有和你解释Agent是什么意思，意味着什么？我来第一个来给大家解释一下，尽量做到通俗易懂。

## agent是什么意思

Agent翻译过来就是「智能代理」。在 Cursor 这样的编程工具中，可以理解为一个具有特定能力和目标的智能助手，能够主动理解和执行编程任务。与传统的代码补全或简单问答不同，Agent 更像是一个"会思考"的编程助手。

请大家着重注意「主动」和「执行」这两个关键词。

实际应用场景 举个例子，当你说："我想做一个具有用户注册和登录功能的 Web 应用"，Agent 不仅会给你代码片段，还会：

-   • 规划整体架构
    
-   • 建议使用的技术栈
    
-   • 考虑安全性问题
    
-   • 自动生成相关的模块代码
    
-   • 提供最佳实践建议
    

这里很多内容以往都是要人工手动实现的，这次cursor的更新因此被人认为是cursor 2.0，一次革命性更新

## 布局的改变

我在这篇Cursor composer教程<sup><span leaf="">[1]</span></sup>文章中介绍了composer的使用，其中提到了composer窗口是在右侧，而在老版本中composer窗口是在cursor的正中间。

目前的布局：

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYrdYwq3k1Picyxomiaf8SRCdCPQEeOv6lCLiaDQmJbSMJckAUmPO8l1rjw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

它们Chat公用右边的Tab栏，当你按下cmd+L 和 cmd+i的时候，会在Tab栏中互相跳转，关于Chat的时候可以看Chat的使用及教程<sup><span leaf="">[2]</span></sup>

## 开启agent

打开compoer以后，我们需要点击下图的agent来切换到agent模式，否则还是停留在旧的composer功能中

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYZSbc6pateHYYtsbB4kb5mu7B5oH8tjdwJn0nvQtCpsibskIV7cOBjfg/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

## 自动收集上下文

这也是本次agent最大的能力，他可以主动、更加智能地识别和收集更多上下文内容，但注意这是cursor官方比较早的版本，因此目前还有这样那样的问题，相信在未来都可以得到修复。

## 终端命令

所有需要执行的命令都会在composer中执行，你需要点击accept，在以前需要你复制这些命令到终端中，自己执行

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYIje6VAm9n5J4fOUqgJhx8AMs80y2gziaaUbUgJcOWc63jRMiczSuwVmA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

## 多任务并行

终端执行命令和对话可以同时进行，二者互不干扰，进而提高了开发效率

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYzCBaA15k9OPz1sdZomHEw3gPibAybYiaGW3HHg3vfn2HutIuvp8aBd2g/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

## 主动解决问题

当生成过程、开发过程、使用过程出现任何问题，它都会主动思考。例如下图，它开始思考依赖为什么安装错误，并主动开始解决问题。

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYComicLRLpO3FFTOAb4QKKxAoeVkhNmSb1jgnKrKLTImkqImD3ibW93hw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

## 像一个程序员一样思考

在你给出需求以后，会像程序员一样来思考问题，列出实现步骤，然后按照步骤慢慢执行。

![图片](https://mmbiz.qpic.cn/mmbiz_png/RBgzkvDqanJY6FJFewtd899DZ9NLCNeYyQlDzE95jTaSQfDjhAlEEjPMfv9QYT9ianDwbMjk1qeT1uA6uoOPtfA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1 "null")

## 总结

Agent 就像是一个聪明的编程助手，不仅仅机械地执行指令，还能像有经验的程序员一样思考和解决问题。 这个概念还在快速发展中，Cursor 只是众多尝试将 AI 引入编程工作流的平台之一。未来，这种智能辅助编程的模式可能会彻底改变我们的开发方式。

#### 引用链接

`[1]` Cursor composer教程: _https://www.lookai.top/cn/cursor/advance/cursor\_composer_  
`[2]` Chat的使用及教程: _https://www.lookai.top/cn/cursor/advance/cursor\_chat_