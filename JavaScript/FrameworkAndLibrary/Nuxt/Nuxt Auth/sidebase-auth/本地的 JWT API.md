在你改用自己在本地跑的 JWT API 之后，Sidebase Auth（AuthJS）里就不再用 KeycloakProvider 了，而是用一个 CredentialsProvider，直接跟你的 `/authenticate`、`/refresh-token`、`/register` 三个接口打交道。下面是一整套参考配置。

---

## 1. 服务端配置（`~/server/api/auth/[...].ts`）

```ts
import CredentialsProvider from 'next-auth/providers/credentials'
import { NuxtAuthHandler }      from '#auth'
import { useRuntimeConfig }     from '#imports'

async function refreshAccessToken(token: any) {
  try {
    const response = await fetch(
      `${useRuntimeConfig().public.apiBase}/api/v1/auth/refresh-token`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token.accessToken}`
        },
        body: JSON.stringify({ /* 如果你的 refresh-token 在 body 里 */ })
      }
    )
    const data = await response.json()
    if (!response.ok) throw data
    return {
      ...token,
      accessToken: data.accessToken,
      refreshToken: data.refreshToken ?? token.refreshToken,
      accessTokenExpires: Date.now() + data.expiresIn * 1000
    }
  } catch (err) {
    console.error('🔄 RefreshTokenError', err)
    return { ...token, error: 'RefreshTokenError' }
  }
}

export default NuxtAuthHandler({
  secret: useRuntimeConfig().authSecret,
  session: {
    strategy: 'jwt',
    maxAge:   60 * 60,      // 1h
    updateAge: 5 * 60       // 5min 后触发刷新
  },
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        username: { label: '用户名', type: 'text' },
        password: { label: '密码', type: 'password' }
      },
      async authorize(creds) {
        // 先去调用 /authenticate 拿到 access & refresh token
        const res = await fetch(
          `${useRuntimeConfig().public.apiBase}/api/v1/auth/authenticate`,
          {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              username: creds!.username,
              password: creds!.password
            })
          }
        )
        const data = await res.json()
        if (!res.ok || !data.accessToken) {
          // 返回 null 会触发认证失败
          return null
        }
        // 把 token 信息塞进 account & user 里，走下面的 jwt callback
        return {
          id: data.userId,
          name: data.username,
          accessToken:  data.accessToken,
          refreshToken: data.refreshToken,
          expiresIn:    data.expiresIn
        }
      }
    })
  ],
  callbacks: {
    // 初次登录：把 authorize() 返回的属性存到 token
    async jwt({ token, user, account }) {
      if (user && (account as any).accessToken) {
        return {
          accessToken:        (account as any).accessToken,
          refreshToken:       (account as any).refreshToken,
          accessTokenExpires: Date.now() + (account as any).expiresIn * 1000
        }
      }
      // 如果还没过期，直接用旧 token
      if (Date.now() < (token as any).accessTokenExpires) {
        return token
      }
      // 过期了就刷新
      return await refreshAccessToken(token as any)
    },
    // 每次调用 useAuth().getSession() 都把最新的 token 返回给前端
    async session({ session, token }) {
      session.accessToken  = (token as any).accessToken
      session.refreshToken = (token as any).refreshToken
      session.error        = (token as any).error
      return session
    }
  }
})
```

> **注意**
> 
> 1. `useRuntimeConfig().public.apiBase` 指向你 Spring Boot 的根地址，比如 `"http://localhost:8080"`.
>     
> 2. 如果你的 `/refresh-token` 接口需要把 `refreshToken` 放在 body 里，也可以把它从 `token.refreshToken` 传过去；上面示例假设你的后端从 `Authorization: Bearer <oldAccessToken>` 里能拿到原来的 refreshToken。
>     
> 3. `session.updateAge` + `maxAge` 配合上面的 `refreshAccessToken`，可以做到前端透明自动刷新。
>     

---

## 2. TypeScript 增强类型

在项目根目录新建或修改 `types/next-auth.d.ts`，让 TS 知道你在 Session 里加了哪些字段：

```ts
import 'next-auth'

declare module 'next-auth' {
  interface Session {
    accessToken?:  string
    refreshToken?: string
    error?:        string
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    accessToken?:        string
    refreshToken?:       string
    accessTokenExpires?: number
    error?:              string
  }
}
```

并在 `tsconfig.json` 里确保包含了这个文件：

```jsonc
{
  "compilerOptions": {
    "typeRoots": ["./node_modules/@types", "./types"]
  },
  "include": ["types/**/*.d.ts", "server/**/*", "components/**/*"]
}
```

然后重启你的 IDE/TS Server。

---

## 3. 前端使用

在任何页面或组件里，你都可以：

```vue
<script setup lang="ts">
import { useAuth, useFetch } from '#imports'

const { signIn, signOut, getSession, status } = useAuth()

// 1. 登录
await signIn('credentials', {
  username: 'alice',
  password: '123456'
})

// 2. 请求受保护的接口
const session = await getSession()
const { data, error } = await useFetch('/api/v1/protected', {
  baseURL: useRuntimeConfig().public.apiBase,
  headers: {
    Authorization: `Bearer ${session?.accessToken}`
  }
})

if (error.value) {
  console.error('调用失败', error.value)
}

// 3. 登出
await signOut()
</script>
```

- `getSession()` 会自动在后台判断是否需要刷新 token
    
- 如果你的页面需要登录态保护，检查 `status.value === 'authenticated'` 或者 `session?.accessToken` 即可
    

---

这样一来，Nuxt 前端就能跟你本地的 JWT API 完美对接，实现注册、登录、自动刷新、登出等功能。


根据 Sidebase 官方的「Local Provider」快速上手文档，你可以在 `nuxt.config.ts` 里一行配置，把你的本地 JWT API 接入到 `@sidebase/nuxt-auth`。下面给出一个完整示例，假设你的 Spring Boot 接口如下：

```text
POST   /api/v1/auth/register       → 注册，返回 { accessToken, refreshToken, expiresIn, … }
POST   /api/v1/auth/authenticate   → 登录，返回 { accessToken, refreshToken, expiresIn, … }
POST   /api/v1/auth/refresh-token  → 刷新，返回 { accessToken, refreshToken?, expiresIn, … }
GET    /api/v1/auth/session       → 会话，返回 { user, accessToken, refreshToken, expiresIn, … }
```

> **注意**：`getSession` 端点必须有，而且必须是 GET，你需要在后端自己实现 `/session`，返回当前用户和 token 数据；NuxtAuth 内部依赖它来判断登录状态([auth.sidebase.io](https://auth.sidebase.io/guide/local/quick-start "Local provider - by sidebase"))。

```ts
// nuxt.config.ts
export default defineNuxtConfig({
  modules: ['@sidebase/nuxt-auth'],

  // 如果你希望通过环境变量控制 baseURL，可以设置 originEnvKey
  runtimeConfig: {
    // 在浏览器里可通过 useRuntimeConfig().public.apiBase 拿到
    public: {
      apiBase: process.env.NUXT_BASE_URL || 'http://localhost:8080'
    }
  },

  auth: {
    // 从环境变量 NUXT_BASE_URL 读取 origin，默认为 window.location.origin
    // baseUrl:
    originEnvKey: 'NUXT_BASE_URL',

    provider: {
      type: 'local',

      // 定义你的四个 Auth 端点
      endpoints: {
        signIn:    { path: '/api/v1/auth/authenticate', method: 'post' },
        signUp:    { path: '/api/v1/auth/register',     method: 'post' },
        signOut:   false,  // 如果后端没 logout 接口，可设为 false
        getSession:{ path: '/api/v1/auth/session',      method: 'get' }
      },

      // Token 在各接口里字段的映射，按你的后端返回结构填写
      token: {
        // 从 /authenticate 或 /register 接口的 JSON 提取 accessToken
        signInResponseTokenPointer:    '/accessToken',
        // 如果不需要 cookie 持久化可按需调整
        type:                          'Bearer',
        headerName:                    'Authorization',
        maxAgeInSeconds:               3600,    // 对应 expiresIn
      },

      // 启用刷新逻辑（v0.9.0+ 支持）
      refresh: {
        isEnabled: true,
        // 调用 /refresh-token 来换取新的 accessToken
        endpoint:      { path: '/api/v1/auth/refresh-token', method: 'post' },
        // 后端返回 refreshToken 字段时才需要，保持默认即可
        refreshOnlyToken: true,
        token: {
          // 从 signIn(register) 响应里提取 refreshToken
          signInResponseRefreshTokenPointer: '/refreshToken',
          // 提交给 /refresh-token 时，从请求体里读取 refreshToken
          refreshRequestTokenPointer:       '/refreshToken',
          // 从 /refresh-token 响应里提取新的 accessToken
          refreshResponseTokenPointer:      '/accessToken',
          maxAgeInSeconds:                  86400, // 刷新 token 的有效期（按需调整）
        }
      },

      // 如果你有自定义登录页，也可以配置：
      // pages: { login: '/login' }
    }
  }
})
```

**要点解析：**

1. **`runtimeConfig.public.apiBase`**  
    用来拼接请求 URL，`/api/v1/auth/...` 会自动 prepend 这个 `apiBase`（浏览器端／服务端都生效）。
    
2. **`endpoints`**
    
    - `signIn` → 对接你的 `POST /authenticate`。
        
    - `signUp` → 对接 `POST /register`。
        
    - `getSession` → 必须有 GET 接口拿当前登录状态。
        
    - `signOut` 可设为 `false`（没有登出接口时）。
        
3. **`token` & `refresh`**
    
    - `signInResponseTokenPointer`、`signInResponseRefreshTokenPointer` 用 [JSON Pointer](https://www.rfc-editor.org/rfc/rfc6901) 指定在响应体里哪里能取到 `accessToken` / `refreshToken`。
        
    - `refresh.endpoint` 指定刷新接口，`refresh.isEnabled: true` 打开刷新逻辑。
        
    - `refreshRequestTokenPointer` 告诉插件如何把旧的 refreshToken 发给 `/refresh-token`。
        
4. **后端额外工作**
    
    - **实现 `GET /api/v1/auth/session`**：根据请求头里的 `Authorization: Bearer <token>`（或 cookie）返回当前用户信息与最新的 token 数据。
        
    - 确保所有接口都按约定返回 `{ accessToken, refreshToken, expiresIn, … }`。
        

配置完后，你就可以在组件里直接使用：

```ts
const { getSession, signIn, signOut, status } = useAuth()

// 登录
await signIn({ email, password })

// 自动刷新／获取 session
const session = await getSession()
console.log(session.accessToken, session.refreshToken)

// 调用鉴权接口
const { data } = await useFetch('/api/v1/protected', {
  baseURL: useRuntimeConfig().public.apiBase,
  headers: { Authorization: `Bearer ${session.accessToken}` }
})
```

这样就完全按照官方文档接入了 Local Provider，支持注册、登录、会话状态查询和自动刷新。