## NuxtAuthHandler

### NuxtAuthHandler 是什么？

- **身份验证中枢**：它是 Nuxt 3 应用的身份验证核心配置入口，负责管理登录、注册、会话等认证流程
- **配置容器**：通过它配置认证提供商（如 GitHub、Google）、会话策略、回调函数等

NuxtAuthHandler 是 AuthJS 中内置的 [NextAuthHandler](https://next-auth.js.org/configuration/options) 的改编版。您可以在 [Quick Start 部分](https://auth.sidebase.io/guide/authjs/quick-start#nuxtauthhandler) 了解有关如何设置 NuxtAuthHandler 的最低版本的更多信息。

核心配置项：Secret  Providers Callbacks Events Pages 

## Secret

密钥是一个随机字符串，用于对令牌进行哈希处理、对 Cookie 进行签名和加密以及生成加密密钥。在开发中， ` 密钥` 会自动设置为 `NuxtAuthHandler` 的 SHA 哈希。在生产环境中，我们建议设置 [runtimeConfig](https://nuxt.com/docs/guide/going-further/runtime-config) 值来定义更安全的值。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  secret: useRuntimeConfig().authSecret,
  // your authentication configuration here!
})
```

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    authSecret: process.env.AUTH_SECRET,
  }
})
```

```ts
AUTH_SECRET="YOUR-SUPER-SECURE-SECRET"
```

## Providers 供应商

提供程序是用户可用于登录应用程序的已注册身份验证方法。NuxtAuth 提供了许多预配置的提供程序，您可以使用它们来快速引导您的项目。其中包括 OAuth 提供商、 [基于电子邮件的提供商](https://next-auth.js.org/configuration/providers/email) （Magic URL） 和 [凭据提供商](https://next-auth.js.org/configuration/providers/credentials) 。除了使用预构建的提供程序外，您还可以创建自己的提供程序。

您可以 [在此处](https://next-auth.js.org/providers/) 找到所有预构建提供程序的概述。如果您想创建自己的提供商，请访问 [NextAuth 文档](https://next-auth.js.org/configuration/providers/oauth#using-a-custom-provider) 。

==WARNING  警告==
`next-auth@4` 提供程序需要额外的 `.default` 才能在 Vite 中工作。在 `next-auth@5` （`authjs`） 中将不再需要这样做。
2025-04-17 next-auth 为 v4
```ts
import { NuxtAuthHandler } from "#auth"
import GithubProvider from "next-auth/providers/github"

export default NuxtAuthHandler({
  providers: [
      // 内置提供商（Github/Google等）
    // @ts-expect-error 您需要在此处使用 .default 才能在 SSR 期间正常工作。可能会在某个时候通过 Vite 进行修复
    GithubProvider.default({
      clientId: process.env.GITHUB_CLIENT_ID,
      clientSecret: process.env.GITHUB_CLIENT_SECRET
  	})
      // 自定义邮箱/密码登录
    CredentialsProvider.default({
      credentials: { /* 表单字段 */ },
      authorize: async (credentials) => { /* 验证逻辑 */ }
    })
  ]
})
```


## session(会话设置)

```ts
session: {
  strategy: "jwt",       // 推荐使用 JWT 策略
  maxAge: 30 * 24 * 60 * 60 // 30天有效期
}
```



## Callbacks 回调

NuxtAuthHandler 中的回调是异步函数，允许您挂接和修改身份验证流。当您需要执行以下作时，这非常有用：

- 更改 JWT 令牌或会话数据中的数据
- 添加对刷新令牌的支持

回调功能非常强大，允许您根据需要修改身份验证流程。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  // your authentication configuration here!
  callbacks: {
    /* on before signin */
    async signIn({ user, account, profile, email, credentials }) {
      return true
    },
    /* on redirect to another url */
    async redirect({ url, baseUrl }) {
      return baseUrl
    },
    /* on session retrival */
    async session({ session, user, token }) {
      return session
    },
    /* on JWT token creation or mutation */
    async jwt({ token, user, account, profile, isNewUser }) {
      return token
    }
  }
})
```

每个回调的一些用例可能是：

- `signIn` ：检查用户是否受到限制，例如无法访问应用程序并终止登录流程。
- `redirect` ：根据需要动态计算且无法在启动时设置的参数（例如，通过功能标志或数据库值）自定义回调 url。
- `session` ：获取其他数据并将其注入 session。 [在此处](https://auth.sidebase.io/guide/authjs/session-data) 阅读更多内容。
- `jwt` ：在 JWT 令牌中注入或更新数据，并管理刷新和访问令牌。

您可以在官方 [NextAuth 文档中](https://next-auth.js.org/configuration/callbacks) 阅读有关每个回调的更多信息，它们提供的数据以及它们期望的返回值。

## Events 事件

事件是在身份验证流程中的某些作期间调用的异步回调函数。它们可用于记录某些事件或调试您的身份验证流程。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  // your authentication configuration here!
  events: {
    async signIn(message) { /* on successful sign in */ },
    async signOut(message) { /* on signout */ },
    async createUser(message) { /* user created */ },
    async updateUser(message) { /* user updated - e.g. their email was verified */ },
    async linkAccount(message) { /* account (e.g. GitHub) linked to a user */ },
    async session(message) { /* session is active */ },
  }
})
```

您可以在官方 [NextAuth 文档中](https://next-auth.js.org/configuration/events) 阅读有关每个事件以及它们提供的数据的更多信息。

## Pages 页面

在 pages 配置中，您可以定义与身份验证相关页面匹配的自定义路由。在此处设置新页面，将覆盖模块中包含的默认身份验证页面。

如果您想了解有关自定义页面和自定义的更多信息，请 [在此处](https://auth.sidebase.io/guide/authjs/custom-pages) 阅读完整指南。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  // your authentication configuration here!
  pages: {
    signIn: '/auth/signIn',
    signOut: '/auth/signOut',
    error: '/auth/error',
    verifyRequest: '/auth/verify-request',
    newUser: '/auth/new-user'
  }
})
```