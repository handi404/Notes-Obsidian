### 核心理念：组合的力量

Linux命令行的哲学是**KISS (Keep It Simple, Stupid)**。每个命令只做好一件事，但要做到极致。真正的威力来自于使用**管道 `|`** 将这些简单的命令组合起来，形成强大的工作流。

例如：`cat access.log | grep "404" | sort | uniq -c | sort -nr | head -10`
这个命令链条展示了：读取文件 -> 筛选内容 -> 排序 -> 统计频率 -> 按数字反向排序 -> 显示前 10 条。这就是 Linux 命令行的精髓。

现在，我们开始分类详解。

---

### 1. 文件与目录管理 (File & Directory Management)

这是最基础、最高频的一类命令，用于在文件系统中导航和操作。

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `ls` | 列出目录内容 | `ls -alh`: `a` (all)显示所有文件(包括隐藏的), `l` (long)长格式显示详细信息, `h` (human)人性化显示文件大小。 |
| `cd` | 切换目录 | `cd ~` (回家), `cd ..` (去上级目录), `cd -` (回到上一个目录)。 |
| `pwd` | 显示当前工作目录 | `pwd`: 直接使用，告诉你“你在哪”。 |
| `cp` | 复制文件/目录 | `cp -r source_dir/ target_dir/`: `-r` (recursive)递归复制整个目录。 |
| `mv` | 移动/重命名文件 | `mv old_name new_name` (重命名), `mv file.txt /tmp/` (移动)。 |
| `rm` | 删除文件/目录 | `rm -i file.txt` (`-i` interactive交互式提示), `rm -rf directory/` (`-r` 递归 `-f` force 强制删除，**极度危险**)。 |
| `mkdir` | 创建目录 | `mkdir -p project/src/main`: `-p` (parents)自动创建不存在的父目录。 |
| `touch` | 创建空文件/更新时间戳 | `touch new_file.log`: 如果文件不存在则创建，存在则更新其修改时间。 |
| `ln` | 创建链接 | `ln -s /path/to/real_file link_name`: `-s` 创建**符号链接(软链接)**，这是最常用的方式。 |

#### 扩展与应用
*   **Tab 自动补全**是你最好的朋友，按 Tab 键能自动补全命令、文件名和目录名，极大提高效率。
*   `mv` 在同一个文件系统分区内移动文件，速度极快，因为它只修改了文件的元数据（inode 信息），而不是复制数据本身。

#### 要点与注意事项
*   **`rm -rf /` 是Linux世界里最著名的“自杀”命令**。在使用 `-rf` 参数时，请务必再三确认你所在的目录和要删除的目标。
*   **软链接 vs 硬链接**：软链接（`ln -s`）像一个快捷方式，删除源文件后链接失效。硬链接（`ln`）是文件的另一个入口，删除任意一个，只要还有一个存在，文件内容就还在。开发中 99%的情况使用软链接。

---

### 2. 文本处理 (Text Processing)

在 Linux 中，大量配置和日志都是纯文本，因此文本处理能力至关重要。

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `cat` | 查看文件内容（一次性输出） | `cat /etc/os-release`: 查看系统版本信息。**现代替代：`bat`**，带语法高亮和行号，更易读。 |
| `less` | 分页查看文件内容 | `less large_file.log`: 可交互式地上下翻页、搜索(`/`)，按 `q` 退出。比 `more` 更强大。 |
| `head` / `tail` | 查看文件头部/尾部 | `tail -n 100 log.txt`: 查看最后100行。 `tail -f log.txt`: `-f` (follow)实时监控文件追加的新内容。 |
| `grep` | 在文本中搜索匹配行 | `grep -i "error" app.log`: `-i` (ignore case)不区分大小写地搜索"error"。**现代替代：`rg` (ripgrep)**，速度极快。 |
| `sed` | 流编辑器（行编辑） | `sed 's/old_text/new_text/g' file.txt`: `s` 表示替换，`g` 表示全局替换。常用于脚本中批量修改文件内容。 |
| `awk` | 强大的文本分析工具 | `df -h | awk '{print $1, $5}' `: 打印` df `命令输出的第一列和第五列。非常适合处理格式化的列式数据。 |
| `wc` | 统计字数/行数/字符数 | `wc -l file.txt`: `-l` (lines)统计文件的行数。 |
| `sort` / `uniq` | 排序 / 去重 | `sort file.txt | uniq -c `: ` uniq `只能对**已排序**的输入去重，` -c `(count)统计重复次数。 |

#### 扩展与应用
*   **管道 `|`** 是这一类的灵魂。例如，分析 Nginx 日志中访问量最高的 IP：
    `cat access.log | awk '{print $1}' | sort | uniq -c | sort -nr | head -10`
    （打印第一列 IP -> 排序 -> 去重并计数 -> 按数字反向排序 -> 取前 10）

#### 要点与注意事项
*   `grep` 是查找**包含模式的行**。要查找**文件本身**，请使用 `find` 或 `locate`。
*   `sed` 和 `awk` 功能强大，但语法也较复杂，可以从简单的替换和列提取开始学起。

---

### 3. 系统信息与监控 (System Info & Monitoring)

了解系统当前的状态，排查性能问题的必备工具。

| 命令       | 核心功能         | 常用范例 & 解读                                                                        |
| :------- | :----------- | :------------------------------------------------------------------------------- |
| `top`    | 实时显示进程和系统资源  | `top`: 交互式界面，按 `P` 按 CPU 排序, `M` 按内存排序, `q` 退出。**现代替代：`htop`**，彩色、易操作。           |
| `ps`     | 显示当前进程快照     | `ps aux`: `a` 所有用户, `u` 用户导向格式, `x` 包括无终端的进程。这是查看所有进程最常用的组合。                     |
| `free`   | 查看内存使用情况     | `free -h`: `-h` (human)人性化显示。注意 available 内存才是你真正可用的。                            |
| `df`     | 查看磁盘空间使用情况   | `df -h`: `-h` (human)人性化显示各挂载点（分区）的使用情况。                                         |
| `du`     | 查看文件/目录的磁盘占用 | `du -sh /path/to/dir`: `-s` (summary)只显示总计, `-h` (human)人性化显示。                   |
| `uname`  | 显示系统内核信息     | `uname -a`: `-a` (all)显示所有信息，如内核版本、架构等。                                          |
| `ss`     | 查看网络连接/套接字   | `ss -tuln`: `-t` TCP, `-u` UDP, `-l` listening, `-n` numeric。**现代替代：`netstat`**。 |
| `iostat` | 查看磁盘I/O统计    | `iostat -d -x 1`: `-d` 设备, `-x` 扩展信息, `1` 每秒刷新。                                  |

#### 扩展与应用
*   **`htop`** 强烈推荐安装 (`sudo apt install htop` / `sudo dnf install htop`)，它比 `top` 直观太多。
*   当发现磁盘满了 (`df` 报告100%)，用 `du -sh /* | sort -hr` 从根目录开始，一层层找到最占空间的文件或目录。

#### 要点与注意事项
*   `df` 和 `du` 的结果可能不一致。`df` 从文件系统元数据获取信息，`du` 递归计算文件大小。如果一个大文件被删除，但仍有进程持有其文件句柄，`df` 会显示空间已释放，而 `du` 则找不到该文件，导致差异。重启进程后空间才会真正释放。

---

### 4. 进程管理 (Process Management)

控制正在运行的程序。

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `kill` | 向进程发送信号 | `kill PID`: 默认发送 `TERM` (15)信号，礼貌地请求进程退出。`kill -9 PID`: 发送 `KILL` (9)信号，强制杀死进程。 |
| `pkill` | 按名称或其他属性杀死进程 | `pkill -f "python my_app.py"`: `-f` (full)按完整命令行匹配并杀死进程。比 `ps aux | grep ... | kill `更方便安全。 |
| `killall` | 按进程名杀死所有同名进程 | `killall nginx`: 杀死所有名为 `nginx` 的进程。 |
| `&` | 后台运行 | `long_running_script.sh &`: 将命令放入后台执行，你可以继续使用终端。 |
| `jobs`, `fg`, `bg` | 管理后台作业 | `jobs` 列出后台作业, `fg %1` 将 1 号作业调回前台, `bg %2` 让暂停的 2 号作业在后台继续运行。 |

#### 要点与注意事项
*   **`kill -9` 是最后的手段**。它会立即终止进程，不给其任何清理（如保存文件、关闭连接）的机会，可能导致数据损坏。优先使用不带参数的 `kill`。

---

### 5. 用户与权限管理 (User & Permission Management)

Linux 是多用户系统，权限是安全的基石。

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `sudo` | 以其他用户身份执行命令 | `sudo apt update`: 以 `root` 身份执行更新命令。Linux 系统管理的入口。 |
| `su` | 切换用户 | `su - username`: `-` 表示同时切换到用户的环境变量，推荐使用。 |
| `chmod` | 修改文件/目录权限 | `chmod 755 script.sh` (数字模式), `chmod u+x script.sh` (符号模式，给 user 增加可执行权限)。 |
| `chown` | 修改文件/目录所有者 | `sudo chown -R user:group /path/to/dir`: `-R` 递归修改，同时修改用户和用户组。 |
| `whoami`, `id` | 查看当前用户身份 | `id`: 显示更详细的信息，包括用户 ID(uid)、组 ID(gid)和所属的所有组。 |

#### 要点与注意事项
*   **永远优先使用 `sudo`**，而不是直接用 `su` 切换到 `root` 用户长期操作。这遵循**最小权限原则**，也让操作有日志记录。
*   理解 `rwx` 权限：`r` (读, 4), `w` (写, 2), `x` (执行, 1)。`755` 表示 `u=rwx, g=rx, o=rx`（用户可读写执行，组用户和其他用户可读可执行）。

---

### 6. 网络工具 (Networking Tools)

与网络世界的交互。

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `ping` | 测试网络连通性 | `ping google.com`: 发送 ICMP 包，测试到目标主机的延迟和丢包。 |
| `ip` | 显示和管理路由、设备、策略路由和隧道 | `ip addr show` (查IP), `ip route` (查路由)。**现代替代：`ifconfig` 和 `route`**。 |
| `dig` | DNS查询工具 | `dig google.com`: 查询域名的A记录。比 `nslookup` 更详细。 |
| `curl` / `wget` | HTTP/S 文件传输/下载 | `curl -I httpbin.org/get` (`-I` 只看头), `wget https://.../file.zip` (下载文件)。 |
| `ssh` | 安全远程登录 | `ssh user@hostname`: 登录到远程服务器。是远程管理的基石。 |
| `scp` / `rsync` | 安全远程复制 | `scp file.txt user@host:/remote/path/`, `rsync -avz local_dir/ user@host:/remote_dir/` (`rsync` 更高效，支持增量同步)。 |

#### 要点与注意事项
*   **优先使用 `ip` 命令**，`ifconfig` 等 net-tools 工具在许多新系统中已不再默认安装。
*   `curl` 是一个瑞士军刀，能做各种网络请求，非常适合API测试和脚本编写。`wget` 更专注于下载文件。

---

### 7. 软件包管理 (Software Management)

安装、更新、卸载软件。**这类命令因发行版而异**。

| 发行版家族 | 包管理器 | 常用操作 |
| :--- | :--- | :--- |
| **Debian/Ubuntu** | `apt` | `sudo apt update`, `sudo apt install <pkg>`, `sudo apt remove <pkg>`, `apt search <keyword>` |
| **RHEL/CentOS/Fedora** | `dnf` (或旧的 `yum`) | `sudo dnf check-update`, `sudo dnf install <pkg>`, `sudo dnf remove <pkg>`, `dnf search <keyword>` |
| **Arch Linux** | `pacman` | `sudo pacman -Syu`, `sudo pacman -S <pkg>`, `sudo pacman -Rns <pkg>`, `pacman -Ss <keyword>` |

---

### 8. 搜索文件 (Searching for Files)

| 命令 | 核心功能 | 常用范例 & 解读 |
| :--- | :--- | :--- |
| `find` | 按各种条件实时查找文件 | `find /home/user -name "*.log" -mtime -7`: 在指定目录下，按文件名和 7 天内修改过查找。 |
| `locate` | 从数据库中快速查找文件 | `locate my_file.conf`: 速度极快，但依赖于一个后台更新的数据库(`updatedb`)，可能找不到最新的文件。 |
| `which` | 查找可执行文件的路径 | `which python3`: 显示 `python3` 命令的完整路径。 |

#### 扩展与应用
*   `find` 的 `-exec` 或与 `xargs` 结合是其强大之处。例如，查找并删除所有 `.tmp` 文件：
    `find . -name "*.tmp" -delete` (更安全的内置方法)
    `find . -name "*.tmp" | xargs rm` (传统方法)

这八类命令构成了你日常使用Linux的骨架。从这里出发，再遇到未知命令，使用 `man command_name` 或 `command_name --help` 去探索，将无往不利。