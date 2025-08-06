使用以下配置将 access_token 添加到会话中：
```ts
callbacks: {

jwt: async ({ token, user, account }) => {
  if (account && account.access_token) {                
    token.accessToken = account.access_token; // <-- adding the access_token here
    token.refreshToken =account.refresh_token;
  }

  const userResponse = await $fetch("http://localhost:8000/api/v1/me/");

  token.data = userResponse;

  return token;
},
async redirect({ url, baseUrl }) {
  return baseUrl + "/dashboard";
},
async session({ session, user, token }) {
  session.token = token;
  
  return session;
},
```

并创建可组合项 composables/useUserData.js：
```js
export const useUserData = async () => {
	const { getSession} = useAuth();
	const userData = await getSession();
	return userData.token|| null
};
```

现在，您可以通过在组件中调用可组合项来访问 access_token 和用户数据：
```js
const user = await useUserData();
```
https://stackoverflow.com/questions/78254841/issues-with-access-token-retrieval-and-validation-in-nuxt-3-application-using-si


chatgpt：

**提供认证信息**

用户提到了在后端API网关配置了Keycloak，而前端使用Nuxt.js和sidebase auth配置了Keycloak提供者。他们的需求是了解如何从前端调用后端接口。需要在请求中包含访问令牌，可以通过使用`useAuth`从`@sidebase/nuxt-auth`获取访问令牌，并管理会话。如果是基于cookie的会话，可使用`credentials: 'include'`，或者手动获取令牌并添加到请求的`Authorization`头。

**配置 Keycloak 提供者**

在 `/server/api/auth/[...].ts` 中，配置 Keycloak 提供者，使用 `NuxtAuthHandler` 进行身份验证处理：

```ts
import KeycloakProvider from 'next-auth/providers/keycloak'
import { NuxtAuthHandler } from '#auth'
export default NuxtAuthHandler({
  secret: useRuntimeConfig().authSecret,
  providers: [
    KeycloakProvider.default({
      clientId: 'xxx',
      clientSecret: 'yyy',
      issuer: 'https://your-domain/auth/realms/...'
    })
  ]
})
```

前端的 Nuxt.js 配置将生成 REST API 接口，例如 `/api/auth/session`、`/api/auth/signin` 等。

已思考 19 秒

要在 Nuxt 前端拿到 Keycloak 下发的 access token 并调用你在 API 网关上暴露的后端接口，大致流程是：

1. **在服务端配置 Keycloak Provider**  
    在 `~/server/api/auth/[...].ts` 中，用 Sidebase Nuxt-Auth（基于 NextAuth.js）配置 KeycloakProvider：
    
    ```ts
    // ~/server/api/auth/[...].ts
    import KeycloakProvider from 'next-auth/providers/keycloak'
    import { NuxtAuthHandler } from '#auth'
    import { useRuntimeConfig } from '#imports'
    
    export default NuxtAuthHandler({
      secret: useRuntimeConfig().authSecret,
      providers: [
        // @ts-expect-error SSR 下要用 .default()
        KeycloakProvider.default({
          clientId: process.env.KEYCLOAK_CLIENT_ID!,
          clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
          issuer: process.env.KEYCLOAK_ISSUER_URL!
        })
      ],
      callbacks: {
        jwt: async ({ token, account }) => {
          // 把 Keycloak 的 access_token 挂到 JWT 里
          if (account?.access_token) token.accessToken = account.access_token
          return token
        },
        session: async ({ session, token }) => {
          // 把 access_token 放到 session 里
          session.accessToken = token.accessToken as string
          return session
        }
      }
    })
    ```
    
    这样登录成功后，你在客户端通过 `useAuth().getSession()` 能拿到 `session.accessToken` citeturn1view0。
    
2. **在客户端拿到 Token**  
    在组件或页面里，用 `useAuth` 这个 composable：
    
    ```ts
    <script setup lang="ts">
    import { useAuth } from '#imports'
    
    const { getSession, signIn, status } = useAuth()
    // 如果还没登录，先触发登录
    if (status.value === 'unauthenticated') {
      await signIn('keycloak') // 跳转到 Keycloak 登录
    }
    // 登录后取 session，其中就包含 accessToken
    const session = await getSession()
    const token = session?.accessToken
    </script>
    ```
    
    Sidebase 文档中提到，`useAuth` 提供了 `getSession()`、`status`、`signIn()`、`signOut()` 等方法，可以方便管理整个认证生命周期 citeturn6search1。
    
3. **带 Token 调用你的后端接口**  
    拿到 `token` 后，用浏览器的 `fetch`／Nuxt 自带的 `$fetch`（或 `useFetch`）去请求你 API 网关上的接口，并在请求头加上 `Authorization: Bearer <token>`。比如：
    
    ```ts
    <script setup lang="ts">
    import { useAuth, useFetch } from '#imports'
    
    const { getSession } = useAuth()
    // 在请求前确保已经有 session
    const session = await getSession()
    const token = session?.accessToken
    
    // 用 useFetch（封装了服务端和客户端都能用的 fetch）
    const { data, error } = await useFetch('https://api.example.com/protected', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    
    if (error.value) {
      console.error('调用失败：', error.value)
    } else {
      console.log('接口返回：', data.value)
    }
    </script>
    ```
    
4. **可选：封装一个 `authFetch`**  
    如果有很多接口都要带 token，可以在 `plugins/authFetch.ts` 里封装：
    
    ```ts
    // plugins/authFetch.ts
    import { defineNuxtPlugin } from '#app'
    export default defineNuxtPlugin(() => {
      return {
        provide: {
          authFetch: async (url: string, opts: any = {}) => {
            const { getSession } = useAuth()
            const session = await getSession()
            opts.headers = {
              ...opts.headers,
              Authorization: `Bearer ${session?.accessToken}`
            }
            return $fetch(url, opts)
          }
        }
      }
    })
    ```
    
    然后在组件里：
    
    ```ts
    const { $authFetch } = useNuxtApp()
    const data = await $authFetch('https://api.example.com/another-endpoint')
    ```
    

---

通过以上步骤，前端就能拿到 Keycloak 的 access token，并在每次请求 API 网关时，把它放到 `Authorization: Bearer` 头里，完成对后端受保护接口的调用。

---
报错：类型“Session”上不存在属性“accessToken”

这个报错是因为 TypeScript 默认的 `Session` 类型里并没有 `accessToken` 这个字段——虽然你在回调里把它挂上去了，但要让编译器“看见”它，必须做一次类型声明的补充（module augmentation）。

下面给你两种常见做法，你二选一就行：

---

## 方案 A：全局模块声明（推荐）

1. 在项目根目录下新建一个 `types/next-auth.d.ts`（目录名随意，但要在 `tsconfig.json` 的 `include` 范围内）：
    
    ```ts
    // types/next-auth.d.ts
    import 'next-auth'
    
    declare module 'next-auth' {
      interface Session {
        /** 从 Keycloak 拿到的 access token */
        accessToken?: string
      }
    }
    
    declare module 'next-auth/jwt' {
      interface JWT {
        accessToken?: string
      }
    }
    ```
    
2. 确保你的 `tsconfig.json` 包含了这个 `types` 目录：
    
    ```jsonc
    {
      "compilerOptions": {
        // … 其他配置 …
        "typeRoots": ["./node_modules/@types", "./types"]
      },
      "include": ["types/**/*.d.ts", "nuxt.config.ts", "server/**/*", "components/**/*"]
    }
    ```
    

这样编译器就知道你的 `session.accessToken`、`token.accessToken` 是合法的属性了。

---

## 方案 B：在 `useAuth` 调用时带上泛型

如果你不想全局声明，也可以在取 session 的地方直接给 `useAuth` 传类型参数：

```ts
<script setup lang="ts">
import { useAuth } from '#imports'

// 告诉 TS：我们的 Session 里有 accessToken
type MySession = {
  accessToken?: string
}

const { getSession, status, signIn } = useAuth<MySession>()

if (status.value === 'unauthenticated') {
  await signIn('keycloak')
}

const session = await getSession()
// 这里 TS 就不会报错了
const token = session?.accessToken
</script>
```

> **注意**：这种方式只在你自己写的地方有效，不会全局生效。如果后面还在其他文件里调用 `useAuth` 没带泛型，还是会报同样的错。

---

### 最后别忘了

不管哪种方式，改完之后重启一下你的 IDE/TS Server，TypeScript 才能重新读取到新的声明。完成后，`session.accessToken` 就能正常用了。