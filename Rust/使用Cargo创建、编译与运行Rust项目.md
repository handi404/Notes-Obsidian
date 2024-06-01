在 [Rust](https://so.csdn.net/so/search?q=Rust&spm=1001.2101.3001.7020) 开发中，Cargo 是一个非常重要的工具，它负责项目的构建、管理和依赖管理。以下是如何使用 Cargo 创建、编译和运行 Rust 项目的详细步骤。

### 1\. 创建新项目

首先确保你已经在计算机上安装了 Rust 和 Cargo。然后，在[命令行](https://so.csdn.net/so/search?q=%E5%91%BD%E4%BB%A4%E8%A1%8C&spm=1001.2101.3001.7020)中输入以下命令来创建一个新的 Rust 项目：

```bash
cargo new project_name
```

替换 `project_name` 为你的项目实际名称。这个命令会在当前目录下创建一个以 `project_name` 命名的文件夹，其中包含了初始的 Rust 项目结构，包括 `src` 目录、`Cargo.toml` 配置文件等。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/77354725985d4290bea04e8190bad965.png)

### 2\. 项目结构解析

-   `Cargo.toml`: 这是项目的配置文件，定义了项目的基本信息（如名称、版本、作者等）以及依赖关系。
    
-   `src` 目录：这是源代码存放的地方，其中 `main.rs` 是项目的主入口点。
    

```markdown
project_name/ |-- Cargo.toml |-- src/ |-- main.rs
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/b50f765d530d42fc9a4d871483ea5ebc.png)

### 3\. 编译项目

进入项目目录，通过 Cargo 来编译项目：

```bash
cd project_name cargo build
```

这将执行编译操作，生成的可执行文件默认位于 `target/debug/project_name`（开发模式）。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/4a0c53d0b9424675966d7e31c469e8ba.png)

### 4\. 运行项目

编译成功后，你可以通过以下命令来运行项目：

```bash
cargo run
```

此命令会自动先进行编译（如果需要的话），然后运行生成的可执行文件。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/9189ed1072e24f1b841ff87fb748ac70.png)

### 5\. 发布版编译

如果你想要为生产环境编译优化过的发布版程序，可以使用：

```bash
cargo build --release
```

这将会生成更高效的二进制文件，位于 `target/release/project_name`。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/cca68df170b64cf5b5a2736adcc8e3b2.png)

以上就是使用 Cargo 创建、编译和运行 Rust 项目的完整流程。Cargo 的引入大大简化了 Rust 开发的复杂性，使得我们可以更专注于代码编写本身。