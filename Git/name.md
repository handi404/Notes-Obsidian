### 在提交树上移动
HEAD
通过指定提交记录哈希值的方式在 Git 中移动不太方便。在实际应用时，并没有像本程序中这么漂亮的可视化提交树供你参考，所以你就不得不用 git log 来查查看提交记录的哈希值。

并且哈希值在真实的 Git 世界中也会更长（译者注：基于 SHA-1，共 40 位）。例如前一关的介绍中的提交记录的哈希值可能是 fed 2 da 64 c 0 efc 5293610 bdd 892 f 82 a 58 e 8 cbc 5 d 8。舌头都快打结了吧...

比较令人欣慰的是，Git 对哈希的处理很智能。你只需要提供能够唯一标识提交记录的前几个字符即可。因此我可以仅输入 fed 2 而不是上面的一长串字符。

通过哈希值指定提交记录很不方便，所以 Git 引入了相对引用
操作符：^
操作符：~
git checkout HEAD~n
git branch -f 

### 撤销变更
用于本地 local
git reset 
用于远程 pushed
git revert

### 整理提交记录
git cherry-pick

### 交互式的 rebase
交互式 rebase 指的是使用带参数 `--interactive` 的 rebase 命令, 简写为 `-i`
git rebase -i HEAD~n

### 本地栈式提交
