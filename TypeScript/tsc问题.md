前言： `npm install typescript -g` 可以运行安装成功。但是在 vscode 中使用 tsc 命令时会报错。

-   在 `cmd` 中，可以运行

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bc58f8174ec043a7b29f4c7bb5277df8~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

-   在 `vscode` 终端中运行失败：

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f81108da3b714b2ba3d6078a08a6e9d5~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

-   解决方法：

找到 tsc.cmd 文件的位置，复制该路径，添加到系统环境变量的 path 中。