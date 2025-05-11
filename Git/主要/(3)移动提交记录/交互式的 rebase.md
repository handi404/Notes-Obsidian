交互式 Rebase (`git rebase -i`) 是 Git 中一个非常强大且常用的工具，它允许你像编辑文本一样编辑你的提交历史。主要用于在你将分支推送到远程仓库或与他人共享之前，**清理和美化本地的提交历史**。

记住 Rebase 的黄金法则：**永远不要对已经被推送到共享仓库（即其他人可能已经拉取并基于其工作的分支）的提交进行 rebase！** 交互式 Rebase 也是 Rebase 的一种，所以这条规则同样适用。主要用在私有分支或者合并到主分支之前的特性分支整理。

---

### 什么是交互式 Rebase (`git rebase -i`)？

*   **核心概念：**
    交互式 Rebase 允许你选择一系列提交，然后对这些提交进行各种操作，例如：
    *   **重新排序 (reorder)** 提交。
    *   **编辑 (edit)** 某个提交的内容或信息。
    *   **合并 (squash/fixup)** 多个提交为一个。
    *   **删除 (drop)** 某个提交。
    *   **修改 (reword)** 提交信息。
    *   甚至可以**拆分 (split)** 一个提交（虽然这需要更多手动操作）。
*   **通俗理解：**
    想象你的提交历史是一系列便利贴，每张便利贴是一个 commit。
    *   **普通 Rebase (`git rebase <base>`)：** 是把一叠便利贴（当前分支的提交）整体从一个地方（旧的基底）移到另一个地方（新的基底之后），按顺序一张张贴上去。
    *   **交互式 Rebase (`git rebase -i <base>`)：** 是在你把这叠便利贴移过去之前，Git 给你一个机会，让你仔细审视每一张便利贴，你可以：
        *   调整它们的顺序。
        *   把几张内容相关的便利贴用订书机钉在一起（squash/fixup）。
        *   发现某张写错了，拿下来修改一下再放回去（edit/reword）。
        *   觉得某张没用了，直接扔掉（drop）。
*   **基本语法：**
    `git rebase -i <base>`
    *   `<base>`：指定了你想要**重写**的提交范围的**起点**。交互式 Rebase 会处理从 `<base>` 的**下一个提交**开始，一直到你当前分支的最新提交（`HEAD`）之间的所有提交。
    *   `<base>` 可以是：
        *   一个提交哈希值 (`a1e8fb5`)。
        *   一个分支名 (`main`)：表示从 `main` 分支与当前分支分叉点之后的提交开始。
        *   一个相对引用 (`HEAD~N`)：表示从当前提交往前的第 `N` 个提交的父提交开始，即重写最近的 `N` 个提交。例如 `git rebase -i HEAD~3` 会让你编辑最近的 3 个提交。

---

### 交互式 Rebase 的流程

1.  **启动交互式 Rebase：**
    假设你当前在 `my-feature` 分支，你想整理最近的 3 个提交：
    `git rebase -i HEAD~3`
    或者，你想整理从 `main` 分支分叉出来之后 `my-feature` 上的所有提交：
    `git rebase -i main`

2.  **编辑 "todo" 列表：**
    执行命令后，Git 会打开一个文本编辑器（通常是你配置的默认编辑器，如 Vim, Nano, VS Code 等），里面会列出所有将被 rebase 的提交，每行一个，格式如下：

    ```
    pick f7f3f6d Change X
    pick 310154e Add Y
    pick a5f4a0d Fix Z

    # Rebase 710f0f8..a5f4a0d onto 710f0f8 (3 commands)
    #
    # Commands:
    # p, pick <commit> = use commit
    # r, reword <commit> = use commit, but edit the commit message
    # e, edit <commit> = use commit, but stop for amending
    # s, squash <commit> = use commit, but meld into previous commit
    # f, fixup <commit> = like "squash", but discard this commit's log message
    # x, exec <command> = run command (the rest of the line) using shell
    # b, break = stop here (continue rebase later with 'git rebase --continue')
    # d, drop <commit> = remove commit
    # l, label <label> = label current HEAD with a name
    # t, reset <label> = reset HEAD to a label
    # m, merge [-c <commit> | -C <commit>] <label> [# <oneline>]
    # .       create a merge commit using the original merge commit's
    # .       message (or the oneline, if no original merge commit was
    # .       specified). Use -c <commit> to re-use the original merge
    # .       commit's statistics and author.
    #
    # These lines can be re-ordered; they are executed from top to bottom.
    #
    # If you remove a line here THAT COMMIT WILL BE LOST.
    #
    # However, if you remove everything, the rebase will be aborted.
    #
    ```

    *   **顺序：** 列表中的提交是按照**时间从旧到新**排列的（最上面的最旧，最下面的最新）。
    *   **操作命令 (Commands)：** 每行开头的 `pick` 是一个命令。你可以将 `pick` 修改为下面列出的其他命令，来对该提交执行不同的操作。

3.  **选择并修改操作命令：**
    以下是常用的操作命令：

    *   **`p`, `pick`**：使用此提交，不做任何改变（这是默认行为）。
    *   **`r`, `reword`**：使用此提交，但在应用它之后，Git 会暂停并让你修改该提交的**提交信息**。
    *   **`e`, `edit`**：使用此提交，但在应用它之后，Git 会暂停。此时，你可以：
        *   修改文件内容，然后 `git add .`
        *   `git commit --amend` (来修改提交内容或信息)
        *   甚至可以创建新的提交。
        *   完成后，使用 `git rebase --continue` 继续 rebase 过程。
    *   **`s`, `squash`**：将此提交**合并到它前面的那个提交中**。Git 会暂停并让你编辑合并后的提交信息（它会把被合并的两个提交的信息都提供给你作为参考）。
    *   **`f`, `fixup`**：类似 `squash`，但会直接**丢弃当前这个提交的提交信息**，使用它前面那个提交的提交信息。通常用于合并一些小的修复性提交（如 "fix typo", "oops"）到主要的提交中，而不污染提交信息。
    *   **`d`, `drop`**：**删除**此提交。这个提交将从历史中移除。你也可以直接删除这一行来达到同样的效果。
    *   **`x`, `exec <command>`**：在处理完前一个 `pick` (或 `edit` 等) 提交之后，但在处理下一个提交之前，执行一个 shell 命令。例如，可以在每个提交应用后运行测试：`exec npm test`。

    **修改示例：**
    假设你想：
    1.  保持第一个提交 `f7f3f6d` 不变。
    2.  将第二个提交 `310154e` (`Add Y`) 和第三个提交 `a5f4a0d` (`Fix Z`) 合并到 `310154e` 中，并且 `Fix Z` 的提交信息不重要。
    3.  并且你想修改合并后 `Add Y` 的提交信息。

    你可以这样修改 "todo" 列表：

    ```
    pick f7f3f6d Change X
    reword 310154e Add Y   # 或者用 squash，然后手动合并信息
    fixup a5f4a0d Fix Z   # 将 Fix Z 合并到 Add Y，并丢弃 Fix Z 的信息
    ```
    或者更常见的 `squash` 做法：
    ```
    pick f7f3f6d Change X
    pick 310154e Add Y
    squash a5f4a0d Fix Z  # 将 Fix Z 合并到 Add Y，Git 会让你编辑合并后的信息
    ```

    **调整顺序：** 你可以直接在编辑器里调整这些行的顺序，Git 就会按照新的顺序来应用提交。

4.  **保存并关闭 "todo" 文件：**
    当你对 "todo" 列表的修改满意后，保存文件并关闭编辑器。Git 就会开始按照你的指示，从 `<base>` 开始，逐个重新应用这些提交。

5.  **处理暂停和冲突 (如果发生)：**
    *   如果选择了 `reword`、`edit`、`squash`，Git 会在适当的时候暂停，让你进行操作（修改信息、修改代码、合并信息等）。
        *   对于 `reword` 和 `squash`，编辑完提交信息后保存关闭即可。
        *   对于 `edit`，修改完代码并 `git commit --amend` (或创建新提交) 后，使用 `git rebase --continue` 来继续。
    *   **冲突解决：** 在 rebase 过程中，如果某个提交的更改与它之前（在新的基底上）的更改发生冲突，Git 会暂停并提示你解决冲突。
        1.  像解决合并冲突一样，编辑冲突文件，手动解决。
        2.  `git add <解决冲突的文件>`
        3.  `git rebase --continue`
        *   如果你想跳过当前这个导致冲突的提交：`git rebase --skip` (慎用，会丢失该提交的更改)。
        *   如果你想完全放弃本次 rebase，回到 rebase 开始前的状态：`git rebase --abort`。

6.  **完成 Rebase：**
    当所有提交都按照你的指示成功应用后，交互式 rebase 就完成了。你的分支历史已经被改写。

---

### 交互式 Rebase 的常见用例

1.  **合并多个零散的提交：**
    开发过程中，你可能为了保存进度而创建了很多小的、临时的提交（例如 "WIP", "fix typo", "add console.log"）。在准备合并到主分支或发起 Pull Request 之前，可以用 `squash` 或 `fixup` 将它们合并成一个或几个更有意义的、原子性的提交。
    ```
    pick abc111 Implement feature part 1
    fixup def222 WIP
    fixup ghi333 Fix typo in feature part 1
    pick jkl444 Implement feature part 2
    squash mno555 Add tests for feature part 2
    ```
    最终可能会变成两个干净的提交。

2.  **修改提交信息：**
    发现某个过去的提交信息写得不清楚或有错别字，可以用 `reword` 来修正。

3.  **重新排序提交：**
    有时，提交的逻辑顺序可能比实际的提交时间顺序更重要。可以调整 "todo" 列表中的行序。

4.  **删除不必要的提交：**
    某个提交后来发现是多余的或者引入了错误，可以用 `drop` 删除它。

5.  **拆分一个大提交 (较复杂)：**
    如果一个提交做了太多的事情，你想把它拆分成多个更小的提交：
    1.  在 "todo" 列表中，将该提交的命令从 `pick` 改为 `edit`。
    2.  当 rebase 暂停在该提交时，你的工作目录会处于该提交完成后的状态。
    3.  使用 `git reset HEAD^` (mixed reset)，这会撤销该提交，但保留所有更改在工作目录中。
    4.  现在，你可以分多次 `git add <部分文件>` 和 `git commit -m "..."` 来创建多个新的、更小的提交。
    5.  完成后，`git rebase --continue`。

---

### 注意事项和最佳实践

*   **只在本地私有分支上进行：** 再次强调，不要 rebase 已经推送到共享仓库的分支。
*   **小步快跑：** 如果要进行复杂的 rebase (比如修改很多提交，或者有很多 squash/edit)，最好分多次小范围地进行，这样如果出错了更容易回溯和修复。
*   **备份分支：** 在进行复杂的 rebase 操作之前，可以先创建一个备份分支，以防万一搞砸了可以恢复：
    `git branch backup-my-feature my-feature`
    如果 rebase 出问题，可以用 `git reset --hard backup-my-feature` 回到 rebase 前的状态。
*   **`git reflog` 是你的朋友：** 如果 rebase 后发现丢失了某些提交或者历史变得混乱，`git reflog` 可以显示 `HEAD` 的移动历史，帮助你找到 rebase 前的提交哈希，然后用 `git reset --hard <hash>` 来恢复。
*   **谨慎使用 `drop` 和 `fixup`：** 确保你真的不需要那个提交或其信息了。
*   **理解 `<base>` 的含义：** `git rebase -i <base>` 操作的是 `<base>` **之后**的提交。

---

交互式 Rebase 是一个强大的工具，它能让你的提交历史变得清晰、专业且易于理解。熟练掌握它，能显著提升你的 Git 使用水平和代码管理能力。