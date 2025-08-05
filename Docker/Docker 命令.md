我们将命令分为两大类：**原生 Docker 命令 (管理镜像、容器、网络、卷等)** 和 **Docker Compose 命令 (管理多容器应用)**。

---

### 一、原生 Docker 命令 (Docker CLI)

这些命令直接通过 `docker` 可执行文件来操作 Docker 引擎。

#### **1. 镜像管理 (Image Management)**

*   `docker build [OPTIONS] PATH | URL | -`
    *   **作用:** 从 Dockerfile 构建镜像。
    *   **常用选项:**
        *   `-t, --tag name:tag`: 为镜像指定名称和标签 (e.g., `myimage:latest`)。
        *   `.` (点号): 表示 Dockerfile 在当前目录。
        *   `-f, --file path/to/Dockerfile`: 指定 Dockerfile 的路径。
        *   `--no-cache`: 构建时不使用缓存。
        *   `--build-arg key=value`: 设置构建时参数 (对应 Dockerfile 中的 `ARG`)。
*   `docker images` 或 `docker image ls`
    *   **作用:** 列出本地存储的所有镜像。
    *   **常用选项:**
        *   `-a, --all`: 显示所有镜像 (包括中间层镜像)。
        *   `-q, --quiet`: 只显示镜像 ID。
*   `docker pull <image_name>[:<tag>]`
    *   **作用:** 从镜像仓库 (默认为 Docker Hub) 拉取镜像。
*   `docker push <image_name>[:<tag>]`
    *   **作用:** 将本地镜像推送到镜像仓库 (需要先登录 `docker login`)。
*   `docker rmi <image_id_or_name>...` 或 `docker image rm <image_id_or_name>...`
    *   **作用:** 删除一个或多个本地镜像。
    *   **常用选项:**
        *   `-f, --force`: 强制删除 (即使有容器正在使用它，慎用！通常需要先停止并删除使用该镜像的容器)。
*   `docker tag <source_image>[:<tag>] <target_image>[:<tag>]`
    *   **作用:** 为本地镜像创建一个新的标签 (相当于一个别名)。
*   `docker history <image_name_or_id>`
    *   **作用:** 查看镜像的构建历史 (各层信息)。
*   `docker image prune`
    *   **作用:** 删除所有悬空的镜像 (dangling images，即没有标签且不被任何容器使用的镜像)。
    *   **常用选项:**
        *   `-a, --all`: 删除所有未被任何容器使用的镜像 (不仅仅是悬空的)。
        *   `-f, --force`: 无需确认直接删除。

#### **2. 容器生命周期管理 (Container Lifecycle)**

*   `docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`
    *   **作用:** 基于指定镜像创建一个**新**容器并**启动**它。这是最核心、选项最多的命令之一。
    *   **常用选项:**
        *   `-d, --detach`: 在后台运行容器并打印容器 ID。
        *   `-it`: 以交互模式运行容器 (`-i` 交互式, `-t` 分配一个伪终端)，通常用于进入容器 shell。
        *   `-p, --publish host_port:container_port`: 将主机的端口映射到容器的端口 (e.g., `-p 8080:80`)。
        *   `-P, --publish-all`: 随机映射容器所有暴露 (`EXPOSE`) 的端口到主机。
        *   `-v, --volume host_path:container_path[:ro]`: 挂载数据卷或绑定挂载主机目录 (e.g., `-v mydata:/data`, `-v /home/user/app:/app:ro`)。
        *   `--name <container_name>`: 为容器指定一个名称。
        *   `-e, --env KEY=VALUE`: 设置环境变量。
        *   `--network <network_name>`: 将容器连接到指定网络。
        *   `--rm`: 容器退出时自动删除容器。
        *   `--restart <policy>`: 设置容器重启策略 (`no`, `on-failure[:max-retries]`, `always`, `unless-stopped`)。
*   `docker ps` 或 `docker container ls`
    *   **作用:** 列出**正在运行**的容器。
    *   **常用选项:**
        *   `-a, --all`: 列出所有容器 (包括已停止的)。
        *   `-q, --quiet`: 只显示容器 ID。
*   `docker start <container_id_or_name>...`
    *   **作用:** 启动一个或多个**已停止**的容器。
*   `docker stop <container_id_or_name>...`
    *   **作用:** 优雅地停止一个或多个**正在运行**的容器 (发送 SIGTERM 信号，等待一段时间后发送 SIGKILL)。
*   `docker restart <container_id_or_name>...`
    *   **作用:** 重启一个或多个容器。
*   `docker kill <container_id_or_name>...`
    *   **作用:** 强制停止一个或多个容器 (直接发送 SIGKILL 信号)。
*   `docker rm <container_id_or_name>...` 或 `docker container rm <container_id_or_name>...`
    *   **作用:** 删除一个或多个**已停止**的容器。
    *   **常用选项:**
        *   `-f, --force`: 强制删除正在运行的容器 (先发送 SIGKILL)。
        *   `-v, --volumes`: 删除与容器关联的匿名卷。
*   `docker pause <container_id_or_name>...`
    *   **作用:** 暂停容器内的所有进程。
*   `docker unpause <container_id_or_name>...`
    *   **作用:** 恢复容器内暂停的进程。
*   `docker container prune`
    *   **作用:** 删除所有已停止的容器。
    *   **常用选项:**
        *   `-f, --force`: 无需确认直接删除。

#### **3. 容器交互与检查 (Container Interaction & Inspection)**

*   `docker exec [OPTIONS] <container_id_or_name> <command>`
    *   **作用:** 在**正在运行**的容器内部执行命令。
    *   **常用选项:**
        *   `-d, --detach`: 在后台执行命令。
        *   `-it`: 以交互模式执行命令 (常用于获取 shell: `docker exec -it mycontainer bash`)。
*   `docker logs [OPTIONS] <container_id_or_name>`
    *   **作用:** 获取容器的日志。
    *   **常用选项:**
        *   `-f, --follow`: 持续跟踪日志输出。
        *   `--tail <number>`: 只显示最后 N 行日志。
        *   `--since <timestamp>` / `--until <timestamp>`: 显示指定时间范围内的日志。
*   `docker inspect <object_id_or_name>...`
    *   **作用:** 显示一个或多个 Docker 对象 (容器、镜像、网络、卷等) 的详细底层信息 (JSON 格式)。
*   `docker cp <container>:<src_path> <host_dest_path>` 或 `docker cp <host_src_path> <container>:<dest_path>`
    *   **作用:** 在主机和容器之间复制文件/目录。

#### **4. 网络管理 (Network Management)**

*   `docker network ls`
    *   **作用:** 列出 Docker 网络。
*   `docker network create [OPTIONS] <network_name>`
    *   **作用:** 创建一个自定义 Docker 网络 (通常是 `bridge` 或 `overlay` 类型)。
    *   **常用选项:**
        *   `--driver <driver_name>`: 指定网络驱动 (e.g., `bridge`, `overlay`)。
        *   `--subnet <subnet>` / `--gateway <gateway>`: 指定子网和网关。
*   `docker network rm <network_name_or_id>...`
    *   **作用:** 删除一个或多个 Docker 网络。
*   `docker network connect <network_name_or_id> <container_id_or_name>`
    *   **作用:** 将一个正在运行的容器连接到指定网络。
*   `docker network disconnect <network_name_or_id> <container_id_or_name>`
    *   **作用:** 断开容器与指定网络的连接。
*   `docker network inspect <network_name_or_id>...`
    *   **作用:** 查看一个或多个网络的详细信息。
*   `docker network prune`
    *   **作用:** 删除所有未被任何容器使用的网络。
    *   **常用选项:**
        *   `-f, --force`: 无需确认直接删除。

#### **5. 数据卷管理 (Volume Management)**

*   `docker volume ls`
    *   **作用:** 列出 Docker 数据卷 (主要是命名卷)。
*   `docker volume create [OPTIONS] [volume_name]`
    *   **作用:** 创建一个命名卷。
*   `docker volume rm <volume_name>...`
    *   **作用:** 删除一个或多个数据卷。**注意：会丢失卷内数据！**
    *   **常用选项:**
        *   `-f, --force`: 强制删除。
*   `docker volume inspect <volume_name>...`
    *   **作用:** 查看一个或多个卷的详细信息。
*   `docker volume prune`
    *   **作用:** 删除所有未被任何容器使用的本地卷 (主要是匿名卷和未被引用的命名卷)。**注意：小心使用，确认不再需要这些卷内的数据！**
    *   **常用选项:**
        *   `-f, --force`: 无需确认直接删除。

#### **6. 系统管理与清理 (System Management & Cleanup)**

*   `docker info`
    *   **作用:** 显示 Docker 系统范围的信息 (版本、存储驱动、镜像/容器数量等)。
*   `docker version`
    *   **作用:** 显示 Docker 客户端和服务端的版本信息。
*   `docker system df`
    *   **作用:** 显示 Docker 磁盘使用情况 (镜像、容器、本地卷、构建缓存)。
*   `docker system prune [OPTIONS]`
    *   **作用:** **强大的清理命令！** 一次性删除所有停止的容器、未被任何容器使用的网络、所有悬空镜像以及所有构建缓存。
    *   **常用选项:**
        *   `-a, --all`: 同时删除所有未被任何容器使用的镜像 (不仅仅是悬空的)。
        *   `--volumes`: **危险！** 同时删除所有未被任何容器使用的本地卷。**使用前务必确认！**
        *   `-f, --force`: 无需确认直接执行清理。
*   `docker login [SERVER]` / `docker logout [SERVER]`
    *   **作用:** 登录/登出 Docker 镜像仓库。

---

### 二、Docker Compose 命令

这些命令通过 `docker-compose` (旧版独立安装) 或 `docker compose` (新版作为 Docker 插件) 来操作，基于 `docker-compose.yml` 文件管理多容器应用。

*   `docker-compose up [OPTIONS] [SERVICE...]`
    *   **作用:** 根据 `docker-compose.yml` 构建 (如果需要)、创建、启动并附加到服务的容器。
    *   **常用选项:**
        *   `-d`: 在后台运行。
        *   `--build`: 在启动前强制重新构建镜像。
        *   `--force-recreate`: 强制重新创建容器，即使配置没有改变。
        *   `--no-deps`: 不启动服务所依赖的其他服务。
        *   `<SERVICE...>`: 只启动指定的服务及其依赖。
*   `docker-compose down [OPTIONS]`
    *   **作用:** 停止并移除由 `up` 创建的容器、网络。
    *   **常用选项:**
        *   `-v, --volumes`: 同时移除在 `volumes` 配置块中定义的命名卷。**危险！会丢失数据！**
        *   `--rmi <type>`: 移除镜像 (`all`: 所有使用的镜像, `local`: 仅本地构建的无标签镜像)。
*   `docker-compose ps [OPTIONS] [SERVICE...]`
    *   **作用:** 列出 Compose 应用中的容器状态。
*   `docker-compose logs [OPTIONS] [SERVICE...]`
    *   **作用:** 查看服务的日志输出。
    *   **常用选项:**
        *   `-f, --follow`: 持续跟踪日志。
        *   `--tail <number>`: 显示最后 N 行。
*   `docker-compose exec [OPTIONS] <SERVICE> <COMMAND>`
    *   **作用:** 在**正在运行**的指定服务的容器内执行命令 (e.g., `docker-compose exec web bash`)。
*   `docker-compose run [OPTIONS] <SERVICE> [COMMAND]`
    *   **作用:** 为指定服务启动一个**新**的**临时**容器并执行命令 (常用于一次性任务，如数据库迁移)。
    *   **常用选项:**
        *   `--rm`: 命令执行后自动移除容器。
        *   `-e KEY=VALUE`: 设置环境变量。
        *   `--service-ports`: 映射服务定义的端口。
*   `docker-compose build [OPTIONS] [SERVICE...]`
    *   **作用:** 构建或重新构建服务的镜像。
    *   **常用选项:**
        *   `--no-cache`: 构建时不使用缓存。
*   `docker-compose pull [OPTIONS] [SERVICE...]`
    *   **作用:** 拉取服务所需的镜像。
*   `docker-compose start [SERVICE...]` / `docker-compose stop [SERVICE...]` / `docker-compose restart [SERVICE...]`
    *   **作用:** 启动/停止/重启**已经存在**的服务容器。
*   `docker-compose rm [OPTIONS] [SERVICE...]`
    *   **作用:** 移除**已停止**的服务容器。
    *   **常用选项:**
        *   `-f, --force`: 强制移除。
        *   `-s, --stop`: 先停止再移除。
        *   `-v`: 移除关联的匿名卷。
*   `docker-compose config [OPTIONS]`
    *   **作用:** 验证并查看最终合并后的 Compose 配置。非常适合调试 `docker-compose.yml` 文件。
    *   **常用选项:**
        *   `--services`: 只列出服务名称。
        *   `--volumes`: 只列出卷名称。
        *   `--networks`: 只列出网络名称。

---

**通用提示:**

*   **`--help`:** 几乎所有 Docker 和 Docker Compose 命令都支持 `--help` 选项，可以查看该命令的详细用法和所有可用选项。这是最好的学习工具！
*   **理解对象:** 清楚你在操作的是镜像 (Image)、容器 (Container)、网络 (Network) 还是卷 (Volume)。
*   **区分 `docker` 和 `docker-compose`:** `docker` 命令通常操作单个对象，而 `docker-compose` 基于配置文件操作整个应用栈。
*   **谨慎使用 `-f` (force) 和 `--volumes` (in prune/down):** 这些选项可能导致数据丢失或意外删除，使用前请三思。

这份总结涵盖了 Docker 和 Docker Compose 的核心常用命令。随着你实践的深入，你会越来越熟练地运用它们来管理你的容器化应用。