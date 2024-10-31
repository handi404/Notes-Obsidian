在 **npm**（Node Package Manager）中，常用命令涵盖项目管理、包安装、构建、调试、版本管理等操作。以下是一些重要的 npm 命令及其用法：

### 1. 包管理和安装

- **`npm init`**：初始化一个新的 Node.js 项目并创建 `package.json` 文件。
  ```bash
  npm init           # 交互式初始化
  npm init -y        # 自动创建默认配置的 package.json
  ```

- **`npm install`** 或 **`npm i`**：安装项目的依赖包。
  ```bash
  npm install        # 根据 package.json 安装所有依赖
  npm install <package-name>    # 安装指定包
  npm install <package-name> -g # 全局安装指定包
  npm install <package-name> --save-dev # 安装为开发依赖
  ```

- **`npm uninstall <package-name>`**：卸载包。
  ```bash
  npm uninstall <package-name>
  ```

### 2. 运行脚本

- **`npm start`**：运行 `package.json` 文件中的 `start` 脚本。
  ```bash
  npm start          # 默认执行 "start" 脚本
  ```

- **`npm run <script-name>`**：运行自定义脚本（`package.json` 中的 `scripts` 字段）。
  ```bash
  npm run dev        # 例如，运行 "dev" 脚本
  npm run build      # 运行 "build" 脚本
  ```

### 3. 包信息和管理

- **`npm list`**：列出项目已安装的依赖包。
  ```bash
  npm list           # 列出所有依赖包
  npm list -g        # 列出全局安装的包
  ```

- **`npm outdated`**：检查过时的包。
  ```bash
  npm outdated       # 列出可更新的包
  ```

- **`npm update`**：更新依赖包。
  ```bash
  npm update         # 更新项目依赖包
  npm update -g      # 更新全局包
  ```

### 4. 缓存管理

- **`npm cache clean --force`**：清除缓存。
  ```bash
  npm cache clean --force
  ```

### 5. 包发布和版本管理

- **`npm version <update_type>`**：更新项目的版本号。
  ```bash
  npm version patch  # 更新补丁版本号 (e.g., 1.0.1)
  npm version minor  # 更新小版本号 (e.g., 1.1.0)
  npm version major  # 更新主版本号 (e.g., 2.0.0)
  ```

- **`npm publish`**：将包发布到 npm 仓库。
  ```bash
  npm publish
  ```

### 6. 其他命令

- **`npm help <command>`**：查看某个命令的帮助信息。
  ```bash
  npm help install   # 查看 "install" 命令帮助
  ```

- **`npm config`**：查看和设置 npm 配置。
  ```bash
  npm config list    # 查看所有配置项
  npm config set registry <registry-url>  # 设置仓库地址
  ```

这些命令可以帮助在 Node.js 项目中管理依赖、运行脚本和配置环境。