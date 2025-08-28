
## 1. 服务端：在 `NuxtAuthHandler` 中实现 Refresh Token Rotation

这里参考 NextAuth.js 的[Refresh Token Rotation 示例](https://next-auth.js.org/v3/tutorials/refresh-token-rotation) ([NextAuth.js](https://next-auth.js.org/v3/tutorials/refresh-token-rotation "Refresh Token Rotation | NextAuth.js"))，只是把 Google 换成你自己的 Keycloak。

```ts
// ~/server/api/auth/[...].ts
import KeycloakProvider from 'next-auth/providers/keycloak'
import { NuxtAuthHandler } from '#auth'
import { useRuntimeConfig } from '#imports'

// refresh token
/* 使用刷新令牌请求新的访问令牌
 *
 */
async function refreshAccessToken(token: any) {
  try {
    const url = `${process.env.KEYCLOAK_ISSUER_URL}/protocol/openid-connect/token`
    const params = new URLSearchParams({
      client_id: process.env.KEYCLOAK_CLIENT_ID!,
      client_secret: process.env.KEYCLOAK_CLIENT_SECRET!,
      grant_type: 'refresh_token',
      refresh_token: token.refreshToken
    })
	// 向令牌端点发出请求
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params
    })
    // 得到刷新的令牌
    // console.log(`response: ${JSON.stringify(response)}`);
    const refreshedTokens = await response.json()
    if (!response.ok) throw refreshedTokens

    return {
      ...token,
      accessToken: refreshedTokens.access_token,
      // Date 对象表示从 1970 年 1 月 1 日 00:00:00 UTC (Unix 纪元) 开始计算的一个特定时间点，单位是毫秒
      accessTokenExpires: Date.now() + refreshedTokens.expires_in * 1000,  // expires_in is access_token 到期时间(单位：秒)
      refreshToken: refreshedTokens.refresh_token ?? token.refreshToken
    }
  } catch (err) {
    console.error('🔄 RefreshAccessTokenError', err)
    return {
      ...token,
      error: 'RefreshAccessTokenError'
    }
  }
}

export default NuxtAuthHandler({
  // 可选：控制 session 的生命周期
  session: {
    strategy: 'jwt',
    maxAge:   60 * 60,    // 1 小时后 session 过期
    updateAge: 5 * 60     // 5 分钟后强制走一次刷新逻辑
  },
  providers: [
    // 只保留 KeycloakProvider
    // （如果你还配置了 GithubProvider，请移除，否则拿不到 Keycloak token）
    KeycloakProvider.default({
      clientId: process.env.KEYCLOAK_CLIENT_ID!,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
      issuer: process.env.KEYCLOAK_ISSUER_URL!
    })
  ],
  callbacks: {
    // JWT callback：初次登录存下 access & refresh token，过期后自动刷新
    async jwt({ token, account }) {
      // 初次签入：account 里带有 access_token、refresh_token、expires_in
      if (account?.access_token) {
        return {
          accessToken:        account.access_token,
          refreshToken:       account.refresh_token!,
          accessTokenExpires: Date.now() + (account.expires_in as number) * 1000
        }
      }
      // 如果还没过期，直接返回旧 token
      if (Date.now() < (token as any).accessTokenExpires) {
        return token
      }
      // 否则 token 过期，调用刷新逻辑
      return refreshAccessToken(token as any)
    },
    // Session callback：把最新的 accessToken 注入到 session
    async session({ session, token }) {
      session.accessToken = (token as any).accessToken
      // 可选：把错误信息也传给前端，供业务做判断
      session.error = (token as any).error
      return session
    }
  }
})
```

> 这样，当前端调用任何需要登录态的接口时，服务端都会检查 `accessTokenExpires`，自动用 `refresh_token` 去 Keycloak 换新的 `access_token`，并更新到 JWT 和 session 里。

---

## 2. 客户端：触发刷新

### 2.1 手动刷新

用 `useAuth` 拿到 `getSession`，它会拉取最新的 session 并更新前端状态：

```vue
<script setup lang="ts">
import { useAuth } from '#imports'

const { data, status, lastRefreshedAt, getSession } = useAuth()

// 在你认为需要刷新 token 的时机调用
// 比如：页面进入时、路由跳转前、某个定时器里……
await getSession()

console.log('最新 accessToken：', data.value?.accessToken)
console.log('最后刷新时间：', lastRefreshedAt.value)
</script>
```

### 2.2 自动刷新

如果你想更优雅地自动刷新，可以利用你在 `NuxtAuthHandler.session.updateAge` 中配置的 `updateAge`。

- `maxAge`：session 最大有效期（秒）
    
- `updateAge`：超过这个秒数后，`getSession()` 会自动走一次新的刷新逻辑
    

并且你可以在程序入口页面（如 `layouts/default.vue`）用 `onMounted` + 定时器定期调用 `getSession()`，或者监听 `visibilitychange`（切后台/前台）时自动刷新。

```ts
// 比如：每 10 分钟检查一次
setInterval(() => {
  getSession().catch(() => {
    // 刷新失败（可能 refresh_token 也过期了），可以主动让用户重新登录
    signIn('keycloak')
  })
}, 10 * 60 * 1000)
```

---

### 小结

1. **服务端**：在 `NuxtAuthHandler` 的 `jwt` 回调里把 `refresh_token` 存下来，过期后调用 Keycloak 的 `/token?grant_type=refresh_token` 接口换新 token。 ([NextAuth.js](https://next-auth.js.org/v3/tutorials/refresh-token-rotation "Refresh Token Rotation | NextAuth.js"))
    
2. **客户端**：用 `useAuth().getSession()` 来拉取最新 session；结合 `session.updateAge`、定时器、或 `visibilitychange` 事件，可实现后台自动刷新，保证 `session.accessToken` 永远有效。 ([NuxtAuth](https://auth.sidebase.io/guide/application-side/session-access "Session Access and Management - by sidebase"))
    
这样就能做到在前端自动、透明地更新 Keycloak 的 access token，无需用户二次登录。