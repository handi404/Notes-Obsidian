在 JavaScript 的世界中，[包管理器](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E5%8C%85%E7%AE%A1%E7%90%86%E5%99%A8&zhida_source=entity)是一个重要的工具，它帮助我们管理、安装和升级项目的依赖。在这篇文章中，我们将深入探讨三个最流行的 JavaScript 包管理器：npm、yarn 和 pnpm。

### npm（Node Package Manager）

npm 是 Node.js 的默认包管理器，它随着 Node.js 一起发布。npm 有一个庞大的包[注册中心](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E6%B3%A8%E5%86%8C%E4%B8%AD%E5%BF%83&zhida_source=entity)，其中包含了数十万个可用的包。npm 使用 `package-lock.json` 文件来锁定依赖的具体版本，这有助于确保在不同的环境中，项目的依赖关系始终保持一致。

### 优点

1.  社区支持：npm 是 Node.js 的默认包管理器，拥有庞大的社区支持和丰富的可用包。开发者可以轻松地找到所需的库和工具。
2.  兼容性：npm 与 Node.js 版本紧密相关，因此在兼容性方面表现良好。
3.  易于使用：npm 的命令简单易懂，对于初学者来说非常友好。

### 缺点

1.  [嵌套依赖](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E5%B5%8C%E5%A5%97%E4%BE%9D%E8%B5%96&zhida_source=entity)：npm 使用嵌套的依赖结构，可能导致大量的重复包安装，占用更多的磁盘空间。这种结构可能会导致“依赖地狱”，使得项目难以维护。
2.  安装速度：npm 的安装速度相对较慢，尤其是在处理大型项目时。这可能会影响开发者的生产力。
3.  不稳定的锁定文件：`package-lock.json` 文件会在执行 npm 命令时自动更新，可能导致依赖关系不稳定。

### npx

**npx 是什么**

npx是一个由Node.js官方提供的用于快速执行npm包中的可执行文件的工具。它可以帮助我们在不全局安装某些包的情况下，直接运行该包提供的命令行工具。`npx` 在执行时，如果发现本地项目依赖项中不存在的包，则会自行安装远程 `npm` 库中的包至 `npm` 缓存当中，运行完成后会删除

使用npx时，可以在命令行中输入要执行的包名加上其参数，例如：

```bash
npx create-react-app my-app
```

以上命令会在本地下载并运行create-react-app包中的可执行文件，创建一个名为my-app的React应用程序。

**npx 会把远端的包下载到本地吗?**

npx 不会像 npm 或 yarn 一样将包下载到本地的 node\_modules 目录中。相反，它会在执行命令时，在本地缓存中寻找并下载包，然后执行该包中的命令。这样可以避免在开发过程中在全局安装大量的包，同时也可以确保使用的是最新版本的包。

**npx 执行完成之后， 下载的包是否会被删除？**

是的，npx会在执行完命令后删除下载的包。这是因为npx会在执行命令之前，将需要执行的包下载到一个临时目录中，并在执行完毕后删除该目录。这样可以避免在本地留下不必要的[依赖包](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E4%BE%9D%E8%B5%96%E5%8C%85&zhida_source=entity)。如果需要保留依赖包，可以使用--no-cleanup选项来禁止删除下载的包。

### cnpm

`cnpm` 是 "China Node Package Manager" 的缩写，是一个完全兼容 npm 的命令行工具，为中国大陆地区的开发者提供了一些额外的功能。

由于网络原因，中国大陆地区的开发者在直接使用 npm 安装包时可能会遇到速度慢甚至无法安装的问题。为了解决这个问题，淘宝团队创建了一个 npm 的镜像源 - 淘宝 NPM 镜像，而 `cnpm` 就是这个镜像源的命令行工具。

`cnpm` 的主要特性有：

1.  **快速安装**：`cnpm` 使用淘宝 NPM 镜像，可以快速地从镜像源下载和安装包，显著提高了在中国大陆地区的安装速度。  
    
2.  **兼容 npm**：`cnpm` 完全兼容 npm 的所有功能，你可以像使用 npm 一样使用 `cnpm`。  
    
3.  **同步 npm**：`cnpm` 提供了一个同步命令，可以将 npm 的包同步到淘宝 NPM 镜像，确保镜像源的包总是最新的。  
    

要安装 `cnpm`，你可以使用 npm：

```bash
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

然后，你就可以像使用 npm 一样使用 `cnpm` 了：

```bash
cnpm install [package-name]
```

总的来说，`cnpm` 是一个为中国大陆地区的开发者提供的 npm 的替代品，它提供了快速的安装速度和 npm 的完全兼容性。

### yarn

yarn 是 Facebook 开发的一个 npm 的替代品，它致力于解决 npm 的一些问题。yarn 使用 `yarn.lock` 文件来锁定依赖的具体版本，这与 npm 非常相似。

### 优点

1.  扁平化[依赖结构](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=2&q=%E4%BE%9D%E8%B5%96%E7%BB%93%E6%9E%84&zhida_source=entity)：yarn 使用扁平化的依赖结构，避免了重复包的安装，减少了磁盘空间占用。这有助于提高项目的可维护性。
2.  安装速度：Yarn 缓存了每个下载过的包，所以再次使用时无需重复下载。 同时利用并行下载以最大化资源利用率，因此安装速度更快，尤其是在处理大型项目时。这有助于提高开发者的生产力。
3.  离线模式：yarn 提供了离线模式，可以在没有互联网连接的情况下安装之前已经缓存过的包。这在有限的网络环境下非常有用。
4.  安全：在执行代码之前，Yarn 会通过算法校验每个安装包的完整性。
5.  更好的用户体验：与 npm 相比，yarn 的[命令行界面](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E5%91%BD%E4%BB%A4%E8%A1%8C%E7%95%8C%E9%9D%A2&zhida_source=entity)（CLI）提供了更好的用户体验，包括清晰的输出和进度条。

### 缺点

1.  社区支持：虽然 yarn 的社区支持也很好，但相较于 npm，它的社区和可用包相对较少。
2.  需要额外安装：yarn 不是 Node.js 的内置包管理器，需要单独安装。

可以通过 npm 安装 yarn：
```bash
npm install -g yarn
```
### 使用

安装完成后，可以使用 yarn 命令。以下是一些常用的 yarn 命令：

1.  初始化一个新项目：
```bash
yarn init
```
1.  安装项目依赖：
```bash
yarn install
```
1.  添加一个新的依赖：
```bash
yarn add [package-name]
```
1.  移除一个依赖：

```bash
yarn remove [package-name]
```

1.  更新一个依赖：

```bash
yarn upgrade [package-name]
```

1.  运行一个脚本：
```bash
yarn run [script-name]
```
### yarn 和 npm 命令对比

| NPM                         | Yarn                      | 说明                                                                                                                                                             |
| --------------------------- | ------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| npm init                    | yarn init                 | 初始化某个项目                                                                                                                                                        |
| npm install/link            | yarn install/link         | 默认安装依赖                                                                                                                                                         |
| npm install taco --save     | yarn add taco             | 安装某个依赖并默认保存到package                                                                                                                                            |
| npm uninstall taco --save   | yarn remove taco          | 移除某个依赖                                                                                                                                                         |
| npm install taco --save-dev | yarn add taco --dev       | 安装某个开发时的依赖                                                                                                                                                     |
| npm update taco --save      | yarn upgrade taco         | 更新某个依赖项目                                                                                                                                                       |
| npm install taco --global   | yarn global add taco      | 安装某个[全局依赖](https://zhida.zhihu.com/search?content_id=663035713&content_type=Answer&match_order=1&q=%E5%85%A8%E5%B1%80%E4%BE%9D%E8%B5%96&zhida_source=entity)项目 |
| npm publish/login/logout    | yarn publish/login/logout | 发布/登录/退出                                                                                                                                                       |
| npm run/test                | yarn run/test             | 运行某个命令                                                                                                                                                         |

### pnpm

pnpm 是一个相对较新的包管理器，它的主要目标是提高性能和减少磁盘空间占用。pnpm 使用 `pnpm-lock.yaml` 文件来锁定依赖的具体版本，这与 npm 和 yarn 非常相似。

### 优点

1.  磁盘空间优化：pnpm 使用了一种称为“symlink”的技术，将所有依赖项存储在一个共享的位置，而不是为每个项目单独安装。这可以大大减少磁盘空间的占用。
2.  安装速度：pnpm 的安装速度相对较快，尤其是在处理大型项目时。这有助于提高开发者的生产力。
3.  严格的依赖管理：pnpm 严格遵循依赖关系，这有助于避免在项目中意外地引入未在 `package.json` 中声明的依赖项。这有助于提高项目的可维护性。

### 缺点

1.  社区支持：pnpm 是一个相对较新的包管理器，其社区支持和可用包相对较少。
2.  兼容性问题：由于 pnpm 使用 symlink 技术，可能会导致某些工具或库在处理依赖关系时出现兼容性问题。

### 安装

全局安装
```js
npm install pnpm -g
```
设置源

```js
//查看源 
pnpm config get registry 
//切换淘宝源 
pnpm config set registry https://registry.npmmirror.com/
```

使用
```js
pnpm install 包  // 
pnpm i 包
pnpm add 包    // -S  默认写入dependencies
pnpm add -D    // -D devDependencies
pnpm add -g    // 全局安装
```
移除

```js
pnpm remove 包 //移除包 
pnpm remove 包 --global //移除全局包
```

更新

```js
pnpm up //更新所有依赖项 
pnpm upgrade 包 //更新包 
pnpm upgrade 包 --global //更新全局包
```