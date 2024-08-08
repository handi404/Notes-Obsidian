
1、在[Docker Hub]中创建一个新的存储库。点击右上角"Create Repository"按钮，给存储库取一个名字，并选择是公有存储库还是私有存储库。

2、在本地构建Docker镜像。进入包含Dockerfile的目录，使用以下命令构建镜像：

```less
docker image build -t [username]/[repository]:[tag] .
```

这里的\[username\]是您的Docker Hub用户名，\[repository\]是您在Docker Hub上创建的存储库名称，\[tag\]是镜像的标签。例如：

```cobol
docker image build -t fox666/tulingmall-product:0.0.1 .
```

3、登录Docker Hub。在本地使用以下命令登录：

```undefined
docker login
```

输入您的Docker Hub用户名和密码进行登录。

4、将本地构建的Docker镜像推送到Docker Hub。使用以下命令：

```less
docker push [username]/[repository]:[tag]
```

这里的\[username\]是您的Docker Hub用户名，\[repository\]是您在Docker Hub上创建的存储库名称，\[tag\]是镜像的标签。例如：

```cobol
docker push fox666/tulingmall-product:0.0.1
```

上传过程中，Docker将会逐层上传镜像的每个层，并计算每个层的SHA256哈希值。在上传完成后，可以在Docker Hub上看到已上传的镜像。


要在 Docker Hub 上共享镜像，你需要完成以下几个步骤。这些步骤包括创建 Docker Hub 账户、构建镜像、登录 Docker Hub、为镜像打标签以及推送镜像到 Docker Hub。

### 步骤 1：创建 Docker Hub 账户
如果你还没有 Docker Hub 账户，首先需要创建一个账户。访问 [Docker Hub](https://hub.docker.com/) 并点击“Sign Up”进行注册。

### 步骤 2：构建 Docker 镜像
确保你已经编写了 Dockerfile，并在本地构建了镜像。以下是一个简单的例子：

```sh
docker build -t myapp .
```

`myapp` 是本地镜像的名称。

### 步骤 3：登录 Docker Hub
在命令行中使用 `docker login` 命令登录 Docker Hub。

```sh
docker login
```

输入你的 Docker Hub 用户名和密码。

### 步骤 4：为镜像打标签
为你的镜像打上 Docker Hub 所需的标签，标签格式通常是 `your-dockerhub-username/repository-name:tag`。例如：

```sh
docker tag myapp your-dockerhub-username/myapp:latest
```

### 步骤 5：推送镜像到 Docker Hub
使用 `docker push` 命令将镜像推送到 Docker Hub。

```sh
docker push your-dockerhub-username/myapp:latest
```

### 示例
假设你的 Docker Hub 用户名是 `myusername`，你想要共享一个名为 `myapp` 的镜像：

1. 构建镜像：

```sh
docker build -t myapp .
```

2. 登录 Docker Hub：

```sh
docker login
```

3. 打标签：

```sh
docker tag myapp myusername/myapp:latest
```

4. 推送镜像到 Docker Hub：

```sh
docker push myusername/myapp:latest
```

### 在 `docker-compose.yml` 文件中使用共享镜像
一旦镜像推送到 Docker Hub，你可以在 `docker-compose.yml` 文件中使用该镜像：

```yaml
version: "3.8"
services:
  api:
    image: myusername/myapp:latest
    container_name: api_c
    ports:
      - '4000:4000'
    volumes:
      - ./api:/app
      - ./app/node_modules
```

这里的 `image` 字段使用了你在 Docker Hub 上共享的镜像。

### 总结
通过上述步骤，你可以将 Docker 镜像推送到 Docker Hub 并在 Docker Compose 配置中使用这些镜像，从而方便地共享和部署你的应用程序。