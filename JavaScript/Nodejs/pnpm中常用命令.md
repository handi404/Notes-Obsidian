**pnpm** 是一种高效的 Node.js 包管理工具，类似于 **npm** 和 **yarn**，但具有更高的速度和资源利用效率。以下是 **pnpm** 的常用命令：

### 1. 初始化项目

- **`pnpm init`**：初始化一个新的项目并生成 `package.json` 文件。
  ```bash
  pnpm init          # 交互式创建 package.json
  pnpm init -y       # 自动创建带默认配置的 package.json
  ```

### 2. 安装和卸载依赖

- **`pnpm install`** 或 **`pnpm i`**：安装 `package.json` 中的所有依赖包。
  ```bash
  pnpm install       # 安装所有依赖
  ```

- **`pnpm add <package-name>`**：安装特定的包。
  ```bash
  pnpm add lodash            # 安装 lodash
  pnpm add lodash -D         # 安装为开发依赖
  pnpm add -g http-server    # 全局安装
  ```

- **`pnpm remove <package-name>`**：卸载包。
  ```bash
  pnpm remove lodash         # 卸载 lodash
  ```

### 3. 脚本运行

- **`pnpm run <script>`**：运行 `package.json` 中定义的脚本。
  ```bash
  pnpm run dev               # 运行 "dev" 脚本
  pnpm run build             # 运行 "build" 脚本
  ```

- **`pnpm start`**：运行 `start` 脚本。
  ```bash
  pnpm start                 # 如果 package.json 中定义了 "start" 脚本
  ```

### 4. 包管理和更新

- **`pnpm list`** 或 **`pnpm ls`**：列出已安装的包。
  ```bash
  pnpm list                  # 列出本地项目依赖
  pnpm list -g               # 列出全局依赖
  ```

- **`pnpm outdated`**：检查项目中可更新的包。
  ```bash
  pnpm outdated              # 列出所有过时的包
  ```

- **`pnpm update`** 或 **`pnpm up`**：更新依赖包。
  ```bash
  pnpm update                # 更新所有依赖
  pnpm update lodash         # 更新指定包
  ```

### 5. 全局包管理

- **`pnpm add -g <package-name>`**：全局安装包。
  ```bash
  pnpm add -g serve          # 全局安装 serve
  ```

- **`pnpm remove -g <package-name>`**：全局卸载包。
  ```bash
  pnpm remove -g serve       # 全局卸载 serve
  ```

### 6. 缓存管理

- **`pnpm store prune`**：清理无用的缓存。
  ```bash
  pnpm store prune           # 清理无用缓存
  ```

### 7. 配置和帮助

- **`pnpm config`**：查看和设置 pnpm 的配置。
  ```bash
  pnpm config set registry <registry-url> # 设置自定义的 registry
  pnpm config get registry                # 查看当前 registry 配置
  ```

- **`pnpm help <command>`**：查看命令帮助信息。
  ```bash
  pnpm help install            # 查看 install 命令的帮助
  ```

**pnpm** 命令大多数与 **npm** 类似，但在管理包和缓存方面更高效，更适合大型项目和单体架构。