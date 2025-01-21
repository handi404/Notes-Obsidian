
### 一、[环境变量](https://cn.vitejs.dev/guide/env-and-mode.html)

什么是环境变量，所谓环境变量，就是说系统开发过程中需要使用的一些变量，也可以称为宏变量，比如如何标注是开发环境还是生成环境，就相当于是debug模式还是Release模式。环境变量一般不能参与运算，也就是说初始化后不能在被赋值。

#### 1. 在[VUE3]()中使用vite，包含如下基本环境变量：

| 序号  | 变量                         | 类型      | 说明                                                                                             |
| --- | -------------------------- | ------- | ---------------------------------------------------------------------------------------------- |
| 1   | `import.meta.env.MODE`     | string  | 应用运行的[模式](https://cn.vitejs.dev/guide/env-and-mode.html#modes "模式")                            |
| 2   | `import.meta.env.BASE_URL` | string  | 部署应用时的基本 URL。他由[base 配置项](https://cn.vitejs.dev/config/shared-options.html#base "base 配置项")决定。 |
| 3   | `import.meta.env.``PROD`   | boolean | 应用是否运行在生产环境。                                                                                   |
| 4   | `import.meta.env.DEV`      | boolean | 应用是否运行在开发环境 (永远与 `import.meta.env.PROD`相反)。                                                    |
| 5   | `import.meta.env.SSR`      | boolean | 应用是否运行在 [server](https://cn.vitejs.dev/guide/ssr.html#conditional-logic "server") 上。           |
设置时使用红色部分，使用时需要加上特殊的 **`import.meta.env`** 对象，用来标注这些变量。

#### 2. 生产环境注意

在生产环境中，这些环境变量会在构建时被**静态替换**，因此，在引用它们时请使用完全静态的字符串。动态的 key 将无法生效。例如，动态 key 取值 **`import.meta.env[key]`** 是无效的。

> **注意：环境变量是被静态替换的，一定要注意是替换！替换！替换！**

### 二、环境变量定义

#### 1\. 环境变量文件位置说明

环境变量是在环境变量文件中定义，环境变量文件预定如下：

```javascript
.env                # 所有情况下都会加载
.env.local          # 所有情况下都会加载，但会被 git 忽略
.env.[mode]         # 只在指定模式下加载
.env.[mode].local   # 只在指定模式下加载，但会被 git 忽略
```

注意，上面这些是文件名，其中方括号\[mode\]是指当前构建模式，比如：development表示开发环境，production表示生产环境。

> 注意：以上文件默认情况下一定要和 vite.config.ts 配置文件放在同一个目录，否则将导致无法加载到环境变量。当然除了默认情况，也可以通过修改 vite.config.ts 配置文件，把以上环境变量文件在其他目录。
> 
> 需要使用 **envDir** 指示符。

比如，下图表示环境变量文件位于 src 文件目录下，这就需要在 vite.config.js 中设置：

![](https://i-blog.csdnimg.cn/blog_migrate/9f76ea810741315eaa7a0222f2fe1f60.png)  ![](https://i-blog.csdnimg.cn/blog_migrate/abbd336e69bcdaa830d8abb7182b9b2b.png)

#### 2\. 环境变量文件内容定义

.env.development文件定义如下，.env.production可以参照.env.development定义。

```javascript
# //开发.env.development
VITE_MODE_NAME=development
VITE_APP_ID=123456
VITE_AGENT_ID=123456
VITE_LOGIN_TEST=true
VITE_RES_URL=http://www.abitsoft.com
VITE_APP_TITLE=控件网站
VITE_EDITOR="VS Code"
VITE_BASE_URL="http://www.abitsoft.com"
VERSION=23.03.23.0
```

![](https://i-blog.csdnimg.cn/blog_migrate/cfb16bf9c57829ad7ebfa6ca70c4da63.png)

#### 3\. 程序中使用环境变量

程序启动时会按照已经顺序加载环境变量文件，比如：一份用于指定模式的文件（例如 `.env.production`）会比通用形式的优先级更高（例如 `.env`）。

加载的环境变量会通过 **`import.meta.env`** 以字符串形式暴露给客户端源码。

为了防止意外地将一些环境变量泄漏到客户端，只有以 **`VITE_`** 为前缀的变量才会暴露给经过 vite 处理的代码。例如上面定义的.env.development 环境变量：

只有以`VITE_` 为前缀的变量会被暴露，例如 `import.meta.env.`VITE\_RES\_URL 等提供给客户端源码，而 VERSION则不会。

```javascript
console.log(import.meta.env.VITE_RES_URL)//http://www.abitsoft.com
console.log(import.meta.env.VERSION) // undefined
```

请注意，如果想要在环境变量中使用 `$` 符号，则必须使用 `\` 对其进行转义。

```javascript
KEY=123
NEW_KEY1=test$foo   # test
NEW_KEY2=test\$foo  # test$foo
NEW_KEY3=test$KEY   # test123
```

**安全注意事项**
- ==.env.local 文件应是本地的，可以包含敏感变量。你应该将 .local 添加到你的 .gitignore 中，以避免它们被 git 检入。==
- ==由于任何暴露给 Vite 源码的变量最终都将出现在客户端包中，VITE_* 变量应该不包含任何敏感信息。==
####  4. 编辑器中环境变量自动提示

默认情况下，Vite 在 [vite/client.d.ts](https://github.com/vitejs/vite/blob/main/packages/vite/client.d.ts "vite/client.d.ts") 中为 `import.meta.env` 提供了类型定义。随着在 `.env[mode]` 文件中自定义了越来越多的环境变量，你可能想要在代码中获取这些以 `VITE_` 为前缀的用户自定义环境变量的 TypeScript 智能提示。

要想做到这一点，你可以在 `src` 目录下创建一个 `env.d.ts` 文件，接着按下面这样增加 `ImportMetaEnv` 的定义：

![](https://i-blog.csdnimg.cn/blog_migrate/a779accce0a531aaef4b5de83c0a3fa9.png)

####  5. 工作模式

默认情况下，开发服务器 (`dev` 命令) 运行在 `development` (开发) 模式，而 `build` 命令则运行在 `production` (生产) 模式。

这意味着当执行 `vite build` 时，它会自动加载 `.env.production` 中可能存在的环境变量：

```javascript
# .env.production
VITE_APP_TITLE=My App
```

在你的应用中，你可以使用 `import.meta.env.VITE_APP_TITLE` 渲染标题。

在某些情况下，若想在 `vite build` 时运行不同的模式来渲染不同的标题，你可以通过传递 `--mode` 选项标志来覆盖命令使用的默认模式。例如，如果你想在 staging （预发布）模式下构建应用：

```javascript
vite build --mode staging
```

 还需要新建一个 `.env.staging` 文件：

```javascript
# .env.staging
VITE_APP_TITLE=My App (staging)
```

[官网文档地址](https://cn.vitejs.dev/guide/env-and-mode.html "官网文档地址")。