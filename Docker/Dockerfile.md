我们来一步步搞定 Dockerfile。把它想象成 **制作 Docker 镜像的 "说明书" 或 "菜谱"**。

**1. Dockerfile 是什么？**

*   **本质：** 它就是一个普通的 **文本文件**，名字通常就叫 `Dockerfile` (没有后缀名)。
*   **内容：** 里面包含了一系列的 **指令 (Instructions)** 和参数。
*   **作用：** Docker 根据这个文件里的指令，一步一步地 **自动化构建 (build)** 出一个 Docker 镜像 (Image)。

**2. Dockerfile 有什么用？ (为什么需要它？)**

*   **自动化构建：** 无需手动执行一堆 `docker commit` 命令，只需一个 `docker build` 命令即可根据 Dockerfile 创建镜像。
*   **可重复性：** 只要 Dockerfile 和相关文件不变，任何人、任何时间、任何地点都能构建出完全相同的镜像。环境一致性 get！
*   **版本控制：** Dockerfile 是文本文件，可以轻松放入 Git 等版本控制系统，追踪镜像构建脚本的变更历史。
*   **易于分享和协作：** 只需分享 Dockerfile，其他人就能复现你的镜像环境。
*   **清晰透明：** 打开 Dockerfile 就知道镜像是如何构建的，包含哪些软件和配置。

**3. 如何编写 Dockerfile？**

*   **基本格式：**
    *   每一行以一个 **指令 (Instruction)** 开头，指令通常 **大写** (约定俗成，易于阅读)。
    *   指令后面跟着 **参数**。
    *   `#` 开头的行是 **注释**。
*   **构建过程：** Docker 会 **从上到下** 逐行执行 Dockerfile 中的指令。每一条成功执行的指令都会在前一层的基础上创建一个新的 **镜像层 (Layer)**。

**一个极简的例子：**

```dockerfile
# 指定基础镜像 (我们从哪个现成的镜像开始构建)
FROM ubuntu:latest

# 设置工作目录 (之后的操作都在这个目录下进行)
WORKDIR /app

# 复制本地文件到镜像中 (把当前目录下的所有文件复制到镜像的 /app 目录)
COPY . .

# 在镜像中执行命令 (安装 Python3)
RUN apt-get update && apt-get install -y python3

# 容器启动时默认执行的命令
CMD ["python3", "hello.py"]
```

**如何使用这个 Dockerfile 构建镜像？**

在包含 `Dockerfile` 和 `hello.py` 文件的目录下，打开终端，运行：

```bash
# -t 给镜像起个名字 (tag) 为 my-python-app:latest
# . 表示 Dockerfile 在当前目录下
docker build -t my-python-app:latest .
```

**4. 常用 Dockerfile 指令 (关键字) 详解：**

*   **`FROM <image>[:<tag>]`**
    *   **作用：** **必须是第一条非注释指令**。指定你构建新镜像所依赖的 **基础镜像**。比如 `FROM python:3.9-slim` 就是基于官方的 Python 3.9 slim 版镜像来构建。
*   **`WORKDIR /path/to/workdir`**
    *   **作用：** 设置 **工作目录**。后续的 `RUN`, `CMD`, `ENTRYPOINT`, `COPY`, `ADD` 指令都会在这个目录下执行。如果目录不存在，它会自动创建。可以多次使用，切换工作目录。
    *   **类比：** 就像在 Linux 终端里执行 `cd /path/to/workdir`。
*   **`COPY <src>... <dest>`**
    *   **作用：** 将 **构建上下文** (通常是 `Dockerfile` 所在的目录及其子目录) 中的文件或目录 **复制** 到镜像内的指定路径 `<dest>`。
    *   **推荐：** 优先使用 `COPY`，因为它更透明、功能更单一。
    *   **例子：** `COPY requirements.txt /app/` (复制 `requirements.txt` 到镜像的 `/app/` 目录下), `COPY . /app/` (复制当前目录下所有内容到镜像的 `/app/` 目录)。
*   **`ADD <src>... <dest>`**
    *   **作用：** 功能类似 `COPY`，但有 **额外特性**：
        *   如果 `<src>` 是一个 **URL**，它会下载文件到 `<dest>`。
        *   如果 `<src>` 是一个本地的 **tar 压缩包** (如 `.tar.gz`, `.tar.bz2`, `.tar.xz`)，它会自动 **解压** 到 `<dest>`。
    *   **注意：** 因为 `ADD` 的行为不那么明确（特别是自动解压），一般 **推荐优先使用 `COPY`**，除非你确实需要 `ADD` 的特殊功能。
*   **`RUN <command>`** (shell 格式) 或 `RUN ["executable", "param1", "param2"]` (exec 格式)
    *   **作用：** 在 **镜像构建过程** 中执行命令。这是构建镜像的核心步骤，常用于安装软件包、创建目录、编译代码等。
    *   **注意：** 每条 `RUN` 指令会创建一个新的镜像层。为了减少层数、优化镜像大小，通常使用 `&&` 连接多个命令。
    *   **例子：** `RUN apt-get update && apt-get install -y vim curl && rm -rf /var/lib/apt/lists/*` (更新源、安装 vim 和 curl、清理缓存，都在一层完成)。
*   **`CMD ["executable","param1","param2"]`** (exec 格式, **推荐**) 或 `CMD command param1 param2` (shell 格式) 或 `CMD ["param1","param2"]` (作为 ENTRYPOINT 的默认参数)
    *   **作用：** 指定 **容器启动时默认执行的命令**。
    *   **特点：**
        *   一个 Dockerfile 中 **只能有一条 `CMD` 指令生效** (如果写了多条，只有最后一条生效)。
        *   `docker run` 命令后面如果跟了其他命令，会 **覆盖** `CMD` 指定的命令。
    *   **用途：** 为容器提供一个默认的执行入口。
    *   **例子：** `CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]`
*   **`ENTRYPOINT ["executable", "param1", "param2"]`** (exec 格式, **推荐**) 或 `ENTRYPOINT command param1 param2` (shell 格式)
    *   **作用：** 配置容器启动时 **总是执行的命令**。
    *   **特点：**
        *   `docker run` 命令后面跟的参数会 **追加** 到 `ENTRYPOINT` 命令之后（exec 格式下），而不是覆盖它。
        *   可以通过 `docker run --entrypoint <new_command>` 来覆盖。
    *   **用途：** 让容器像一个可执行文件一样运行，或者固定容器的主程序，让 `CMD` 提供默认参数。
    *   **`CMD` 与 `ENTRYPOINT` 结合使用：**
        ```dockerfile
        ENTRYPOINT ["ping"]
        CMD ["baidu.com"]
        # 运行时：
        # docker run my-image         -> 执行 ping baidu.com
        # docker run my-image google.com -> 执行 ping google.com (CMD 被覆盖)
        ```
*   **`EXPOSE <port> [<port>/<protocol>...]`**
    *   **作用：** **声明** 容器运行时 **计划** 监听的网络端口。这 **不会** 实际发布端口，只是一个 **文档性质** 的元数据，告诉使用者这个镜像的服务会监听哪个端口。
    *   **实际发布端口：** 需要在 `docker run` 时使用 `-p` 或 `-P` 参数。
    *   **例子：** `EXPOSE 80/tcp` (声明监听 TCP 协议的 80 端口)。
*   **`ENV <key>=<value>` 或 `ENV <key1>=<value1> <key2>=<value2>...`**
    *   **作用：** 设置 **环境变量**。这些环境变量在 **构建过程** (后续的 `RUN` 指令等) 和 **容器运行时** 都可用。
    *   **例子：** `ENV APP_HOME=/app` `ENV PYTHONUNBUFFERED=1`
*   **`ARG <name>[=<default value>]`**
    *   **作用：** 定义 **构建时参数**。这些变量 **只在 `docker build` 过程中可用**，容器运行时不可用。可以通过 `docker build --build-arg <name>=<value>` 来传递值。
    *   **与 `ENV` 的区别：** `ARG` 主要用于构建过程的配置，`ENV` 用于运行时环境。如果 `ARG` 和 `ENV` 同名，`ENV` 会覆盖 `ARG`。
    *   **例子：** `ARG USER=guest` `RUN useradd $USER`
*   **`VOLUME ["/path/to/volume"]`**
    *   **作用：** 创建一个 **挂载点**，用于 **持久化存储** 或 **共享数据**。当容器启动时，Docker 会将此路径映射到主机上的一个匿名卷或指定卷。这有助于将数据与容器生命周期分离。
    *   **例子：** `VOLUME /var/log` (容器的 `/var/log` 目录会成为一个卷)。
*   **`USER <user>[:<group>]`**
    *   **作用：** 指定运行后续 `RUN`, `CMD`, `ENTRYPOINT` 指令时使用的 **用户名或 UID** (以及可选的组名或 GID)。
    *   **安全实践：** 避免使用 root 用户运行容器，推荐创建一个非 root 用户并切换过去。
    *   **例子：** `RUN useradd -ms /bin/bash myuser` `USER myuser`
*   **`LABEL <key>=<value> <key>=<value> ...`**
    *   **作用：** 为镜像添加 **元数据 (Metadata)**，如作者、版本、描述等。
    *   **例子：** `LABEL maintainer="Your Name <you@example.com>" version="1.0"`

**5. 多阶段构建 (Multi-stage Builds)**

*   **解决了什么问题？**
    *   传统的 Dockerfile 构建，最终镜像通常包含了很多 **构建时依赖** (比如编译器、构建工具、源代码、测试库等)，这些东西在 **运行时并不需要**，导致最终镜像非常 **臃肿庞大**。
*   **如何实现？**
    *   在一个 Dockerfile 中使用 **多个 `FROM` 指令**。每个 `FROM` 开始一个新的构建 **阶段 (Stage)**。
    *   可以给每个阶段 **命名** (使用 `AS <stage_name>`)。
    *   后面的阶段可以使用 `COPY --from=<stage_name> <src> <dest>` 指令，从 **前面的阶段** 中 **精确地复制** 所需的文件（比如编译好的可执行文件、静态资源等）到当前阶段。
    *   **最终镜像只包含最后一个阶段的内容**。
*   **好处：**
    *   最终镜像 **非常小**，只包含运行应用所必需的文件。
    *   Dockerfile 依然保持简洁，所有构建逻辑都在一个文件中。
*   **例子 (构建一个 Go 应用)：**

```dockerfile
# ---- Stage 1: Build ----
# 使用包含 Go 编译环境的镜像作为构建阶段的基础镜像，并命名为 builder
FROM golang:1.19-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 Go 模块文件
COPY go.mod ./
COPY go.sum ./
# 下载依赖 (利用 Docker 缓存)
RUN go mod download

# 复制源代码
COPY . .

# 构建 Go 应用，禁用 CGO，生成静态链接的可执行文件 app
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o app .

# ---- Stage 2: Final ----
# 使用一个非常小的 Alpine Linux 作为最终镜像的基础
FROM alpine:latest

# 设置工作目录
WORKDIR /root/

# 从上一个名为 builder 的阶段，复制构建好的可执行文件 app 到当前阶段
COPY --from=builder /app/app .

# (可选) 暴露端口
EXPOSE 8080

# 容器启动时运行这个可执行文件
CMD ["./app"]
```

在这个例子中：

1.  **`builder` 阶段：** 基于 `golang:1.19-alpine` (包含 Go 编译器)，下载依赖、编译代码，生成可执行文件 `/app/app`。这个阶段可能比较大。
2.  **最终阶段：** 基于极小的 `alpine:latest` 镜像，**只** 从 `builder` 阶段复制了编译好的 `./app` 文件。这个阶段非常小，不包含任何 Go 编译工具或源代码。

**总结一下：**

*   Dockerfile 是构建镜像的自动化脚本。
*   掌握 `FROM`, `WORKDIR`, `COPY`, `RUN`, `CMD` / `ENTRYPOINT`, `EXPOSE`, `ENV` 等核心指令是关键。
*   多阶段构建是优化镜像大小、保持构建逻辑清晰的利器。