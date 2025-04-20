## Session data 会话数据

本指南介绍如何向用户会话添加自定义数据。在许多情况下，您可能希望调整身份验证流程返回的信息。这可能取决于您的提供商或您为丰富会话数据而进行的任何其他 API 调用。

## Modify the JWT Token 修改 JWT 令牌

为了在会话请求之间持久化数据，我们需要将某些信息注入 JWT 令牌，以便在后续会话请求中访问这些信息。但是，由于 JWT 令牌的大小有限，因此请避免注入过多数据。我们建议仅注入访问令牌或会话令牌，以便在会话回调中用于请求更多用户信息。

JWT callback 提供：

- `token` ：原始 JWT 令牌
- `account` 、 `profile` 、 `isNewUser` ：OAuth 提供程序返回的数据。这些数据因提供程序而异，我们建议记录每个值以检查包含哪些数据。这些值仅在创建 JWT 令牌时可用，在后续请求中只有该 `token` 可用。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  // your authentication configuration here!
  callbacks: {
    jwt({ token, account, profile }) {
      if (account) {
        token.sessionToken = account.session_token
      }
      return token
    },
  }
})
```



==INFO==
注入 JWT 令牌的任何数据都不能从前端直接访问，但可以使用 `getToken` 函数在服务器端访问。

## Inject data into the Session 将数据注入会话

在使用附加数据丰富 JWT 令牌后，您现在可以在 `session` callback 中访问这些数据。每次请求 `session` 数据时都会调用会话回调。这可能发生在使用 `useAuth` 、 `getServerSideSession` 或刷新会话时。

```ts
import { NuxtAuthHandler } from '#auth'

export default NuxtAuthHandler({
  // your authentication configuration here!
  callbacks: {
    async session({ session, token }) {
      // Token we injected into the JWT callback above.
      const token = token.sessionToken

      // Fetch data OR add previous data from the JWT callback.
      const additionalUserData = await $fetch(`/api/session/${token}`)

      // Return the modified session
      return {
        ...session,
        user: {
          name: additionalUserData.name,
          avatar: additionalUserData.avatar,
          role: additionalUserData.role
        }
      }
    },
  }
})
```



==INFO==
`session` 回调中抛出的任何错误都将导致会话终止和用户被注销。

---

就这样！现在您可以在应用程序内部访问新的会话数据了。

```vue
<script setup lang="ts">
const { data } = useAuth()
</script>

<template>
  <div v-if="data">
    <!-- You can access the session data you injected above! -->
    Hello, {{ data.user.name }}. You have the role: {{ data.user.role }}!
  </div>
</template>
```



## Typescript

修改会话或 JWT 对象时，您可能需要相应地调整类型，以确保获得正确的智能感知和类型支持。 [模块增强](https://www.typescriptlang.org/docs/handbook/declaration-merging.html#module-augmentation) 可用于向已安装的模块中注入额外的类型声明，以覆盖或添加额外的数据。

首先在项目根目录中创建一个名为 `next-auth.d.ts` 的新 TypeScript 文件。TypeScript 会自动识别此文件正在扩充 `next-auth` 的模块类型（在后台运行）。

```ts
// file: ~/next-auth.d.ts
import type { DefaultSession } from 'next-auth'

declare module 'next-auth' {
  /* Returned by `useAuth`, `getSession` and `getServerSession` */
  interface Session extends DefaultSession {
    user: {
      name: string
      avatar: string
      role: 'admin' | 'manager' | 'user'
    }
  }
}
```



除了修改 `session` 数据类型之外，您还可以扩展 JWT 令牌的类型。这样，您在 NuxtAuthHandler 中访问 JWT 令牌或在服务器端调用 `getToken` 时，就能获得适当的类型支持。

```ts
// file: ~/next-auth.d.ts
declare module 'next-auth/jwt' {
  /** Returned by the `jwt` callback and `getToken` */
  interface JWT {
    sessionToken?: string
  }
}
```

