
NVM 虽然经典，但在面对多语言支持和更复杂的项目需求时，显得有些力不从心。

## 1\. VMR

`VMR` 是一款简单、跨平台且经过良好设计的版本管理器，用于管理多种 `SDK` 以及其他工具。

它的出现主要是为了解决现有工具无法同时管理 `多种编程语言` 的问题。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/kzFgl6ibibNKr2EhyXRK6P8bDhiahtvj9WkO0N7biaO5wGyQiaGD6y7CW29Cw0pntZQibibvJ9cmhrQAiaECwQTH1ItVHA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

##### 特点：

- **跨平台** ：支持 Windows、Linux 和 MacOS。
- **多语言支持** ：支持多种编程语言和工具，省心省力。
- **友好的 TUI** ：受到 lazygit 的启发，拥有更友好的文本用户界面（TUI），更符合直觉。
- **项目锁定** ：支持针对项目锁定 SDK 版本，确保项目稳定性。
- **代理设置** ：支持反向代理/本地代理设置，提高国内用户下载体验。

##### 支持的语言：

- **Node.js**
- **Python**
- **Ruby**
- **Go**
- **Java**
- **PHP**
- **等多种语言**

##### Github 地址：

- **https://github.com/gvcgo/version-manager/tree/main**

## 2\. vfox

`vfox` 是一个 `跨平台` 的、 `可扩展` 的版本管理器，旨在简化工具和运行环境的管理。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/kzFgl6ibibNKr2EhyXRK6P8bDhiahtvj9WkE5Ij6WQQ6Wtibqqdjv2YD9koRzhc63rw3YotdMYAxPmeO2B3eBerPvw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

##### 特点：

- **跨平台** ：支持 Windows（非 WSL）、Linux 和 macOS。
- **插件系统** ：通过简单的 API，可以轻松添加对新工具的支持。
- **多 Shell 支持** ：支持 Powershell、Bash、ZSH、Fish 和 Clink，并提供补全功能。
- **向后兼容** ：支持从现有的配置文件平滑迁移。

##### 支持的语言：

- **Node.js**
- **Python**
- **Ruby**
- **Go**
- **Java**
- **Deno**
- **Bun**

##### Github 地址：

- **https://github.com/version-fox/vfox**

## 3\. Volta

`Volta` 是一个现代的 JavaScript 工具链管理器，旨在简化 `Node.js` 、 `npm` 、 `Yarn` 等工具的安装和管理。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/kzFgl6ibibNKr2EhyXRK6P8bDhiahtvj9Wkwmen2cySh4BWF336YYJUzaJHM7Svp3N5tSNMaeqwCCWrxlN0xbbndA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

##### 特点：

- **跨平台** ：支持 Windows、Linux 和 macOS。
- **快速安装** ：通过简单的命令行指令即可安装和切换工具版本。
- **环境隔离** ：**每个项目可以有独立的工具版本**，避免版本冲突。
- **符号链接** ：利用符号链接技术，确保工具的快速访问。

##### 支持的语言：

- **Node.js**

##### Github 地址：

- **https://github.com/document-translate/volta**

## 4\. Fnm

`Fast Node Manager` (fnm) 是一个用 `Rust` 编写的高效 `Node.js` 版本管理器。它的设计目标是快速、简单，并且易于安装和使用。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/kzFgl6ibibNKr2EhyXRK6P8bDhiahtvj9Wk5jlAuDSAibmImMNKIOFhG4zrVv4eSObUq0KBjgpY6ZJGvKY106DicB3A/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

在寻找一个更快、更简单的 Node.js 版本管理器时， `Fast Node Manager` （fnm）无疑是一个值得考虑的选择。

fnm 由 `Rust` 编写，提供了 `跨平台` 支持，并且以其速度和易用性脱颖而出。

##### 特点：

- **跨平台支持** ：支持 macOS、Windows 和 Linux。
- **单文件安装** ：只需一个文件即可轻松安装，启动速度快。
- **速度优先** ：从设计之初就考虑了速度优化。
- **兼容性** ：支持.node-version 和.nvmrc 文件。

##### 支持的语言：

- **Node.js**

##### Github 地址：

- **https://github.com/Schniz/fnm**