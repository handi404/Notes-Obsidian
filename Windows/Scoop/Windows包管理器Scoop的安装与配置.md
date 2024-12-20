摘要：Scoop是一款Windows上的[命令行](https://so.csdn.net/so/search?q=%E5%91%BD%E4%BB%A4%E8%A1%8C&spm=1001.2101.3001.7020)包管理器，它可以让用户更加方便地安装、更新和管理软件。与其他包管理器相比，Scoop具有不需要[管理员权限](https://so.csdn.net/so/search?q=%E7%AE%A1%E7%90%86%E5%91%98%E6%9D%83%E9%99%90&spm=1001.2101.3001.7020)、易于安装和隔离软件包等优点。本教程将详细介绍如何在Windows上安装和配置Scoop，包括设置PowerShell远程权限、更新PSReadLine以支持自动补全、下载安装Scoop以及添加额外的软件仓库。

#### 正文：

##### 1\. 在PowerShell中设置远程权限

在安装Scoop之前，首先需要确保你的PowerShell具有执行远程脚本的权限。这是因为Scoop的安装脚本需要从Internet下载。打开PowerShell，并输入以下命令：

```powershell
Set-ExecutionPolicy RemoteSigned -scope CurrentUser
```

此命令将当前用户的执行策略设置为`RemoteSigned`，允许运行本地脚本和已签名的远程脚本。

##### 2\. 更新PSReadLine以支持自动补全

为了提升命令行使用体验，建议更新PSReadLine模块。PSReadLine提供了诸如语法高亮、自动补全等功能。运行以下命令来更新PSReadLine并配置Tab键自动补全：

```powershell
sudo Install-Module PSReadLine -Force 
Set-PSReadlineKeyHandler -Chord Tab -Function MenuComplete
```

##### 3\. 下载并安装Scoop

接下来，使用以下命令下载Scoop的安装脚本并执行安装。你可以指定Scoop的安装目录和全局软件安装目录：

```powershell
irm get.scoop.sh | iex
```

如果你想自定义Scoop及其全局应用程序的安装位置，可以使用如下命令：

```powershell
irm get.scoop.sh -outfile 'install.ps1'.\install.ps1 -ScoopDir '你想把scoop安装到那个目录' -ScoopGlobalDir 'Scoop未来安装全局软件的目录' -NoProxy
```

对于需要管理员权限安装到系统范围的情况，使用以下命令：

```powershell
iwr -useb get.scoop.sh -outfile 'install.ps1' 
.\install.ps1 -RunAsAdmin
```

##### 4\. 添加额外的软件仓库

Scoop本身带有主要仓库，但是你可以通过添加额外的bucket（仓库）来扩展可安装的软件范围。以下是一些常用的额外仓库及安装命令：

```powershell
scoop bucket add extras 
scoop bucket add scoopet https://github.com/ivaquero/scoopet 
scoop bucket add java 
scoop bucket add versions 
scoop bucket add dorado https://github.com/chawyehsu/dorado 
scoop bucket add nerd-fonts
```

##### 5\. 启用Aria2加速下载

Scoop支持使用Aria2作为下载工具来加速下载过程。首先，安装Aria2：

```powershell
scoop install aria2
```

然后，启用Aria2下载加速：

```powershell
scoop config aria2-enabled true
```

继续我们的Scoop教程，下面我将介绍一些Scoop的常用命令，这些命令将帮助你更有效地管理Windows上的软件。

#### Scoop的常用命令

##### 查看已安装的软件

```powershell
scoop list
```

这个命令会列出你通过Scoop安装的所有软件包，让你快速了解当前安装了哪些工具和应用。

##### 安装软件

```powershell
scoop install <软件名>
```

这是Scoop最常用的命令之一，用于安装你需要的软件。例如，如果你想安装Git，只需运行`scoop install git`。

##### 更新软件

```powershell
scoop update <软件名>
```

当软件发布新版本时，你可以使用这个命令来更新特定的软件。如果想更新所有已安装的软件，可以省略软件名，直接运行`scoop update`。

##### 卸载软件

```powershell
scoop uninstall <软件名>
```

当你不再需要某个软件时，可以使用这个命令将其从系统中卸载。

##### 搜索软件

```powershell
scoop search <软件名>
```

如果你不确定Scoop是否有你需要的软件，可以使用这个命令进行搜索。它会在所有已知的仓库中查找匹配的软件包。

##### 查看软件信息

```powershell
scoop info <软件名>
```

这个命令提供了关于特定软件的详细信息，包括版本、依赖、安装后的大小等。

##### 清理旧版本软件

```powershell
scoop cleanup <软件名>
```

随着时间的推移，更新软件会留下旧版本的文件。使用这个命令可以删除旧版本，释放磁盘空间。如果省略软件名，Scoop将尝试清理所有软件的旧版本。

##### 查看已添加的仓库（Bucket）

```powershell
scoop bucket list
```

这个命令显示所有你已添加的仓库。仓库是包含软件包的集合，Scoop通过仓库来组织软件。

##### 添加仓库

```powershell
scoop bucket add <仓库名> [仓库地址]
```

通过这个命令，你可以添加更多的仓库来扩展可安装软件的范围。仓库名是必需的，仓库地址是可选的，如果是已知的仓库，可以省略地址。

##### 查看可用的内核（Kernel）

```powershell
scoop status
```

这个命令可以查看Scoop的状态，包括是否有软件需要更新。

通过掌握这些基本命令，你就能充分利用Scoop来管理Windows上的软件了。Scoop的设计理念是简化Windows软件的安装和管理过程，希望这些命令能帮你更好地实现这一目标。

#### 结语

通过以上步骤，你已经成功安装并配置了Scoop。Scoop的设计理念是“使命令行成为Windows的一等公民”，通过Scoop，你可以享受到类似Linux下包管理器的便捷体验。现在，你可以开始探索Scoop的世界，安装你需要的软件了。如果你遇到任何问题，Scoop的GitHub页面和官方文档是获取帮助的好地方。

[Scoop官网](https://scoop.sh/)  
[Scoop Github](https://github.com/ScoopInstaller/Install#for-admin)  