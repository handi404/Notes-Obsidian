Bun.js 提供了一组简单且高效的命令行工具，旨在提升开发者的开发体验。下面是一些常用的 Bun.js 命令操作：

### 1. **`bun init`**
   该命令用于初始化一个新的 Bun 项目，类似于 `npm init`。它会创建一个项目目录并生成必要的配置文件（如 `package.json`）。

   ```bash
   bun init
   ```

### 2. **`bun install`**
   该命令用于安装项目依赖，类似于 `npm install` 或 `yarn install`，但速度更快。它会自动检测并安装 `package.json` 中的依赖项。

   ```bash
   bun install
   ```

   - **重新安装所有依赖**：如果需要强制重新安装依赖，可以使用 `bun install --force`。
   
   ```bash
   bun install --force
   ```

### 3. **`bun add <package>`**
   用于将新的依赖包添加到项目中，类似于 `npm install <package>` 或 `yarn add <package>`。

   ```bash
   bun add lodash
   ```

   你也可以通过 `-d` 或 `--dev` 参数将包添加为开发依赖：
   ```bash
   bun add eslint --dev
   ```

### 4. **`bun run <script>`**
   该命令用于运行项目中的脚本文件，类似于 `npm run` 或 `node <file>`，但可以直接运行 TypeScript 文件，甚至不需要配置编译步骤。

   ```bash
   bun run index.js
   ```

   或者运行 TypeScript 文件：
   ```bash
   bun run index.ts
   ```

### 5. **`bun create <template>`**
   用于根据特定的模板创建新的项目。它可以从官方提供的模板或社区模板中快速生成项目。

   ```bash
   bun create react ./my-app
   ```

   这将基于 React 模板创建一个名为 `my-app` 的项目。

### 6. **`bun dev`**
   启动开发服务器并监听文件变更，适用于实时开发调试，类似于 Vite 或 Parcel 的 `dev` 服务器。

   ```bash
   bun dev
   ```

   Bun 会自动处理热重载（Hot Module Replacement，HMR），更新代码时不需要手动刷新浏览器。

### 7. **`bun build`**
   用于打包项目，类似于 Webpack 或 Parcel 的构建命令。它会将项目的文件打包为可部署的格式（例如压缩的 CSS 和 JS）。

   ```bash
   bun build
   ```

   你可以指定入口文件或配置文件来控制打包的行为：
   ```bash
   bun build src/index.js --outdir=dist
   ```

### 8. **`bun remove <package>`**
   删除项目中的依赖包，类似于 `npm uninstall` 或 `yarn remove`。

   ```bash
   bun remove lodash
   ```

### 9. **`bun upgrade`**
   将所有的依赖升级到最新的版本，类似于 `npm update`。

   ```bash
   bun upgrade
   ```

### 10. **`bun x <command>`**
   Bun 还支持运行现有的 npm 包中的可执行文件，类似于 `npx`，你可以直接使用该命令运行 npm 包中的 CLI 工具。

   ```bash
   bun x eslint . --fix
   ```

### 11. **`bun info`**
   显示项目的依赖信息，类似于 `npm list`，可以查看当前依赖树或特定包的依赖信息。

   ```bash
   bun info
   ```

   如果需要查看某个包的具体信息，比如版本和依赖：
   ```bash
   bun info react
   ```

### 12. **`bun remove <package>`**
   该命令用于卸载项目中的依赖包，类似于 `npm uninstall`。

   ```bash
   bun remove lodash
   ```

### 13. **`bun --help`**
   获取 Bun 的帮助文档，显示所有可用的命令及其用法。

   ```bash
   bun --help
   ```

### 14. **`bun completions`**
   该命令用于为 Bun 的命令行自动补全功能生成脚本，比如为 `bash` 或 `zsh` 生成自动补全脚本。

   ```bash
   bun completions >> ~/.zshrc
   ```

---

### 常见使用场景总结
- **开发阶段**：通过 `bun dev` 快速启动开发服务器，结合 HMR 实现代码自动刷新。
- **打包阶段**：使用 `bun build` 进行项目打包，生成生产环境部署的文件。
- **依赖管理**：使用 `bun add` 添加依赖，`bun remove` 删除依赖，`bun upgrade` 升级所有依赖。

这些命令帮助开发者轻松管理项目，并提高 JavaScript 和 TypeScript 开发的效率。