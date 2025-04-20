## Session Access and Route Protection 会话访问和路由保护

在服务器端，您可以像这样访问当前会话：

这是受 NextAuth.js 的 [`getServerSession`](https://next-auth.js.org/tutorials/securing-pages-and-api-routes#securing-api-routes) 启发的。它还避免了对 `/api/auth/sessions` 端点的外部 HTTP `GET` 请求，而是直接调用纯 JS 方法。

==注意：==
如果您在应用组件中使用 [Nuxt 的 `useFetch`](https://nuxt.com/docs/api/composables/use-fetch) 从使用 `getServerSession` 或 `getToken` 端点获取数据，则需要手动传递 Cookie，因为 [Nuxt 3 通用渲染](https://nuxt.com/docs/guide/concepts/rendering#universal-rendering) 在服务端运行时默认不会执行此操作。如果不传递 Cookie，则 `getServerSession` 在服务端调用时将返回 `null` ，因为不存在任何身份验证 Cookie。以下是手动传递 Cookie 的示例：

```ts
const headers = useRequestHeaders(['cookie']) as HeadersInit
const { data: token } = await useFetch('/api/token', { headers })
```



## Endpoint Protection 端点保护

为了保护端点，请在获取 session 后检查 session：

```ts
// file: ~/server/api/protected.get.ts
import { getServerSession } from '#auth'

export default eventHandler(async (event) => {
  const session = await getServerSession(event)
  if (!session) {
    return { status: 'unauthenticated!' }
  }
  return { status: 'authenticated!' }
})
```



## Server Middleware 服务器中间件

您还可以在 [Nuxt 服务器中间件](https://nuxt.com/docs/guide/directory-structure/server#server-middleware) 中使用它来同时保护多个页面，并将身份验证逻辑排除在您的端点之外：

```ts
// file: ~/server/middleware/auth.ts
import { getServerSession } from '#auth'

export default eventHandler(async (event) => {
  const session = await getServerSession(event)
  if (!session) {
    throw createError({
      statusMessage: 'Unauthenticated',
      statusCode: 403
    })
  }
})
```

