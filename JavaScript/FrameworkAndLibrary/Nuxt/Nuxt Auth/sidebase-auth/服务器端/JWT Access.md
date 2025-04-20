## JWT Access

获取当前用户的（解码的）JWT 令牌可能会有所帮助，例如，使用它来访问需要此令牌进行身份验证或授权的外部 API。

您可以使用 `getToken` 获取与请求一起传递的 JWT 令牌：

```ts
// file: ~/server/api/token.get.ts
import { getToken } from '#auth'

export default eventHandler(async (event) => {
  const token = await getToken({ event })

  return token || 'no token present'
})
```


该函数的行为与 [NextAuth.js 中的 `getToken` 函数](https://next-auth.js.org/tutorials/securing-pages-and-api-routes#using-gettoken)相同，但有一点不同：必须传入 h3- `event` 而不是 `req` 。这是因为在 h3 上访问 cookie 的方式不同：不是通过 `req.cookies` 而是通过 `useCookies(event)` 。

您无需传入任何其他参数，例如 `secret` 、 `secureCookie` 等。如果未设置，它们会自动推断为您配置的值，并且读取令牌将立即生效。您_可以_传递这些选项，例如，要获取原始的、已编码的 JWT 令牌，您可以传递 `raw: true` 。

## 应用程序端 JWT 令牌访问

要在应用程序端访问 JWT 令牌，例如在 `.vue` 页面中，你可以：

- 创建 API 端点以返回解码后的 JWT 令牌
- 修改 NuxtAuthHandler 中的 `jwt` 回调，将 token 数据注入应用程序端会话。点击[此处](https://auth.sidebase.io/guide/authjs/session-data)了解更多信息

### 通过 API 端点访问 JWT 令牌

要通过 API 端点访问 JWT 令牌，您首先需要创建一个可以处理 JWT 令牌请求的新服务器端路由。

```ts
// file: ~/server/api/token.get.ts
import { getToken } from '#auth'

export default eventHandler(event => getToken({ event }))
```



然后，您可以从应用程序端代码中像这样获取它：

```vue
<script setup lang="ts">
const headers = useRequestHeaders(['cookie']) as HeadersInit
const { data: token } = await useFetch('/api/token', { headers })
</script>

<template>
  <div>{{ token || 'no token present, are you logged in?' }}</div>
</template>
```


==注意：==
为了使其正常工作，您需要使用 [`useRequestHeaders`](https://nuxt.com/docs/api/composables/use-request-headers/) 手动传递 cookie 标头，以便在[通用渲染过程](https://nuxt.com/docs/guide/concepts/rendering#universal-rendering)中在服务器端渲染此页面时也能正确传递 cookie。
