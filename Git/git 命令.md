> 用了这么久的 git 工具，中途也使用过 SourceTree ，但是最后发现，还是代码香啊！  
> 虽然之前也写过一篇学习笔记「[Git教程学习笔记和填坑总结 以及 SourceTree 工具的使用](https://blog.csdn.net/XH_jing/article/details/110822788)」，但是感觉那篇还是笔记冗余，看起来不是很明确，所以，整理这篇命令大全，可以更好的在工作中查阅相关的git命令。

> 下面，我们主要分享一下 Git 常用的命令以及详细的命令行讲解，欢迎各位小伙伴一起讨论学习哦～

#### Git命令大全目录

-   -   [一、项目前的 Git 配置](https://blog.csdn.net/XH_jing/article/details/121900458#_Git__9)
    -   -   -   [1\. 检查 git 版本](https://blog.csdn.net/XH_jing/article/details/121900458#1__git__10)
            -   [2\. 查看 git 相关命令](https://blog.csdn.net/XH_jing/article/details/121900458#2__git__15)
            -   [3\. 查看当前的 git 配置信息](https://blog.csdn.net/XH_jing/article/details/121900458#3__git__20)
            -   [4\. 查看 git 用户名 或 邮箱](https://blog.csdn.net/XH_jing/article/details/121900458#4__git____25)
            -   [5\. 全局配置用户名(设置 git 使⽤者名称)](https://blog.csdn.net/XH_jing/article/details/121900458#5__git__37)
            -   [6\. 设置 （配置）全局邮箱](https://blog.csdn.net/XH_jing/article/details/121900458#6___42)
    -   [二、Git 对项目代码进行管理](https://blog.csdn.net/XH_jing/article/details/121900458#Git__48)
    -   -   -   [1\. 初始化 git 储存](https://blog.csdn.net/XH_jing/article/details/121900458#1__git__49)
            -   [2\. 需要提交的所有修改放到暂存区（Stage）](https://blog.csdn.net/XH_jing/article/details/121900458#2_Stage_54)
            -   [3\. 将暂存区的文件恢复到工作区](https://blog.csdn.net/XH_jing/article/details/121900458#3__65)
            -   [4\. 查看工作区、暂存区的状态](https://blog.csdn.net/XH_jing/article/details/121900458#4__72)
            -   [5\. 移除暂存区的修改](https://blog.csdn.net/XH_jing/article/details/121900458#5__77)
            -   [6\. 将缓存区的文件，提交到本地仓库（版本库 ）](https://blog.csdn.net/XH_jing/article/details/121900458#6___82)
            -   [7\. 撤销 commit 提交](https://blog.csdn.net/XH_jing/article/details/121900458#7__commit__90)
    -   [三、查看日志](https://blog.csdn.net/XH_jing/article/details/121900458#_98)
    -   -   -   [1\. 查看历史提交(commit)记录](https://blog.csdn.net/XH_jing/article/details/121900458#1_commit_99)
            -   [2\. 查看分支合并图](https://blog.csdn.net/XH_jing/article/details/121900458#2__108)
            -   [3\. 查看版本线图](https://blog.csdn.net/XH_jing/article/details/121900458#3__113)
    -   [四、Git 版本控制](https://blog.csdn.net/XH_jing/article/details/121900458#Git__120)
    -   -   -   [1\. 回到指定哈希值对应的版本](https://blog.csdn.net/XH_jing/article/details/121900458#1__121)
            -   [2\. 版本回退](https://blog.csdn.net/XH_jing/article/details/121900458#2__128)
    -   [五、分支管理](https://blog.csdn.net/XH_jing/article/details/121900458#_136)
    -   -   -   [1\. 查看分支](https://blog.csdn.net/XH_jing/article/details/121900458#1__137)
            -   [2\. 创建分支（依然停留在当前的分支）](https://blog.csdn.net/XH_jing/article/details/121900458#2__145)
            -   [3\. 切换分支](https://blog.csdn.net/XH_jing/article/details/121900458#3__151)
            -   [4\. 创建并切换分支（创建一个新的分支，并切换到这个新建的分支上）](https://blog.csdn.net/XH_jing/article/details/121900458#4__157)
            -   [5\. 合并分支（合并某一个分支到当前分支）](https://blog.csdn.net/XH_jing/article/details/121900458#5__162)
            -   [6\. 删除分支](https://blog.csdn.net/XH_jing/article/details/121900458#6__167)
            -   [7\. 删除远程分支](https://blog.csdn.net/XH_jing/article/details/121900458#7__173)
    -   [六、远程仓库（团队协作🌟）](https://blog.csdn.net/XH_jing/article/details/121900458#_180)
    -   -   -   [1\. 克隆远程仓库（从远程仓库拉取代码）](https://blog.csdn.net/XH_jing/article/details/121900458#1__181)
            -   [2\. 本地库与远程库进行关联](https://blog.csdn.net/XH_jing/article/details/121900458#2__187)
            -   [3\. 查看远程仓库地址别名](https://blog.csdn.net/XH_jing/article/details/121900458#3__192)
            -   [4\. 新建远程仓库地址别名](https://blog.csdn.net/XH_jing/article/details/121900458#4__197)
            -   [5\. 删除本地仓库中的远程仓库别名](https://blog.csdn.net/XH_jing/article/details/121900458#5__203)
            -   [6\. 重命名远程仓库地址别名](https://blog.csdn.net/XH_jing/article/details/121900458#6__209)
            -   [7\. 把远程库的修改拉取到本地](https://blog.csdn.net/XH_jing/article/details/121900458#7__215)
            -   [8\. 将本地的分支推送到远程仓库](https://blog.csdn.net/XH_jing/article/details/121900458#8__224)

___

### 一、项目前的 Git 配置

##### 1\. 检查 git 版本

```bash
git --version
```

##### 2\. 查看 git 相关命令

```bash
git --help
```

##### 3\. 查看当前的 git 配置信息

```bash
git config --list
```

##### 4\. 查看 git 用户名 或 邮箱

```bash
查询git所使⽤的用户名 git config user.name 
查询git所使⽤的email git config user.email 
注： --global 表示全局， 没有--global表示只查询在当前项目中的配置 git config --global user.name git config --global user.email
```

##### 5\. 全局配置用户名(设置 git 使⽤者名称)

```bash
git config --global user.name "username"
```

##### 6\. 设置 （配置）全局邮箱

```bash
git config --global user.email "eamil@qq.com"
```

___

### 二、Git 对项目代码进行管理

##### 1\. 初始化 git 储存

```bash
git init
```

##### 2\. 需要提交的所有修改放到暂存区（Stage）

```bash
git add *   将工作区所有修改添加到暂存区 
git add .   将工作区所有修改添加到暂存区 
git add <file-name>   将指定文件添加到暂存区 
git add *.js   提交所有 .js 格式文件 
git add -f <file-name>   强制添加 指定文件添加到暂存区 
注：<file-name> 指的是文件的名称
```

##### 3\. 将暂存区的文件恢复到工作区

```bash
git reset <file-name>   从暂存区恢复指定到工作区 
git reset -- .   从暂存区恢复所有文件到工作区 
git reset --hard   把暂存区的修改退回到工作区
```

##### 4\. 查看工作区、暂存区的状态

```bash
git status
```

##### 5\. 移除暂存区的修改

```bash
git rm --cached <file-name>  // 将本地暂存区的内容移除暂存区
```

##### 6\. 将缓存区的文件，提交到本地仓库（版本库 ）

```bash
git commit <file-name> ... "相关的记录信息"   //将缓存区的指定文件提交到本地仓库 
git commit -m "相关的记录信息"   //将缓存区的所有文件提交到本地仓库 
git commit -am '相关的记录信息'   //跳过暂存区域直接提交更新并且添加备注的记录信息 
git commit --amend '相关的记录信息'  // 使用一次新的commit，替代上一次提交，如果代码没有任何新变化，则用来修改上一次commit的提交记录信息
```

##### 7\. 撤销 commit 提交

```bash
git revert HEAD // 撤销最近的一个提交(创建了一个撤销上次提交(HEAD)的新提交) 
git revert HEAD^ // 撤销上上次的提交
```

___

### 三、查看日志

##### 1\. 查看历史提交(commit)记录

```bash
git log   查看历史commit记录 
git log --oneline   以简洁的一行显示，包含简洁哈希索引值 
git log --pretty=oneline   查看日志且并且显示版本 
git log --stat   显示每个commit中哪些文件被修改,分别添加或删除了多少行 
注：空格向下翻页，b向上翻页，q退出
```

##### 2\. 查看分支合并图

```bash
git log --graph
```

##### 3\. 查看版本线图

```bash
git log --oneline --graph
```

___

### 四、Git 版本控制

##### 1\. 回到指定哈希值对应的版本

```bash
git reset --hard <Hash> # 回到指定 <Hash> 对应的版本 # 注: <Hash> 是版本的哈希值 
git reset --hard HEAD # 强制工作区、暂存区、本地库为当前HEAD指针所在的版本
```

##### 2\. 版本回退

```bash
git reset --hard HEAD~1 # 后退一个版本 # 
注：~ 后面的数字表示回退多少个版本
```

___

### 五、分支管理

##### 1\. 查看分支

```bash
git branch # 查看所有本地分支 
git branch -r # 查看所有远程分支 
git branch -a # 查看所有远程分支和本地分支 
git branch --merged # 查看已经合并的分支
```

##### 2\. 创建分支（依然停留在当前的分支）

```bash
git branch <branch-name> # 创建分支，依然停留在当前的分支 # 
注: <branch-name> 是分支的名称
```

##### 3\. 切换分支

```bash
git checkout <branch-name> # 切换到指定分支，并更新工作区 
git checkout - # 切换到上一个分支
```

##### 4\. 创建并切换分支（创建一个新的分支，并切换到这个新建的分支上）

```bash
git checkout -b <branch-name> # 创建一个新的分支，并切换到这个新建的分支上
```

##### 5\. 合并分支（合并某一个分支到当前分支）

```bash
git merge <branch-name> # 合并<branch-name>分支到当前分支
```

##### 6\. 删除分支

```bash
git branch -d <branch-name> # 只能删除已经被当前分支合并的分支 
git branch -D <branch-name> # 强制删除分支
```

##### 7\. 删除远程分支

```bash
git push origin --delete <remote-branch-name> # 
注：<remote-branch-name> 远程分支名
```

___

### 六、远程仓库（团队协作🌟）

##### 1\. 克隆远程仓库（从远程仓库拉取代码）

```bash
git clone <url> # 
注：<url> 远程仓库的地址
```

##### 2\. 本地库与远程库进行关联

```bash
git remote add origin <url>
```

##### 3\. 查看远程仓库地址别名

```bash
git remote -v
```

##### 4\. 新建远程仓库地址别名

```bash
git remote add <alias> <url> # 
注: <alias> 远程仓库的别名
```

##### 5\. 删除本地仓库中的远程仓库别名

```bash
git remote rm <alias> # 
注: <alias> 远程仓库的别名
```

##### 6\. 重命名远程仓库地址别名

```bash
git remote rename <old-alias> <new-alias> # 
注：<old-alias> 旧的远程仓库，<new-alias> 新的远程仓库
```

##### 7\. 把远程库的修改拉取到本地

```bash
git fetch <alias/url> <remote-branch-name> # 抓取远程仓库的指定分支到本地，但没有合并
git merge <alias-branch-name> # 将抓取下来的远程的分支，跟当前所在分支进行合并 
git pull <alias/url> <remote-branch-name> # 拉取到本地，并且与当前所在的分支进行合并
注: <alias/url> 远程仓库的别名 或者是 远程仓库地址 # <remote-branch-name> 远程分支名
```

##### 8\. 将本地的分支推送到远程仓库

> ⚠️ 在推送前要先拉取哦 git pull

```bash
git push <alias/url> <branch-name> # 将本地的每个分支推送到远程仓库 
git push <alias/url> --force # 强行推送 当前分支到远程仓库，即使有冲突 
git push <alias/url> --all # 推送所有本地分支到远程仓库 # 
注: <alias/url> 远程仓库的别名 或者是 远程仓库地址 # <branch-name> 本地分支名
```

___

到这，我们在工作中常用的命令也基本差不多了，如有不足之处，欢迎各位指正！

少年！加油！