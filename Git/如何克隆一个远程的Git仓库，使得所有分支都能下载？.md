要将远程 Git 仓库克隆到本地，可以使用 git clone 命令。

如果想要将所有分支都下载下来，可以使用 --mirror 参数，这个参数会将整个远程仓库都复制到本地。

命令示例如下：

```
git clone --mirror git@github.com:username/repo.git
```

这个命令会将远程仓库所有分支都克隆到本地，并且在本地创建一个 bare 仓库，这个仓库包含所有分支和提交历史。

如果想要克隆某个分支，可以使用以下命令：

```
git clone -b branch-name git@github.com:username/repo.git
```

其中，branch-name 表示想要克隆的分支名称。如果想要将某个分支克隆到本地之外的一个目录，可以使用以下命令：

```
git clone --branch branch-name --single-branch git@github.com:username/repo.git local-directory
```

这个命令会将指定分支克隆到 local-directory 目录下。

希望这些命令可以帮助你克隆远程 Git 仓库并下载所有分支。