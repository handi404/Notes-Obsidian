下面把常见会导致拦截器“配置失败”的坑和**可运行**的正确做法一步步给你 — 直接可拷到 Vue 3 项目里试。核心思路：

- Hey API 的 axios 客户端把真实的 `AxiosInstance` 暴露在 `client.instance`，你应该在应用启动（或生成 runtime config）时在这个 `instance` 上注册拦截器，或传入你自己创建并已 attach 拦截器的 `AxiosInstance`。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"))
    

---

# 1) 最简单且推荐（在 app 启动时注册拦截器）

把拦截器注册在生成的 `client` 的 `instance` 上（确保在任何 SDK 调用前完成）：

```ts
// src/main.ts (Vue 3)
import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import { client } from './client/client.gen' // 生成文件位置按你项目实际路径

// 创建自定义 axios 实例（可选）
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'https://api.example.com',
})

// 在 axios 实例上注册拦截器
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers = config.headers || {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, (err) => Promise.reject(err))

// 把自定义实例设置到生成的 client
client.setConfig({
  axios: axiosInstance,
  baseURL: axiosInstance.defaults.baseURL,
})

createApp(App).mount('#app')
```

要点：**用 `client.setConfig({ axios: yourInstance })` 或直接在 `client.instance` 上注册拦截器**。若你先用 `client.instance` 注册也可以（`client.instance.interceptors.request.use(...)`）。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"), [npm](https://www.npmjs.com/package/%40hey-api/client-axios?utm_source=chatgpt.com "hey-api/client-axios - NPM"))

---

# 2) 如果拦截器“看起来没有生效”的常见原因（排查清单）

1. **拦截器在第一次请求发出后才注册** → 确保在 `createApp(...).mount()` 之前或应用入口（main）中注册。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"))
    
2. **你实际没有使用生成 client 的 instance**（例如你直接用 `axios` 另起了实例却没把它传给 SDK）→ 用 `client.setConfig({ axios: yourInstance })` 或在每次 SDK 调用传 `client: myClient`。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"))
    
3. **在 SSR / Nuxt 环境**：每个请求环境需要独立实例（不能用单例全局），需要在 server-side hook 中为该请求创建并传入 client（见下 Runtime / Nuxt 说明）。（参见社区 issue：有用户在 Nuxt/SSR 场景遇到拦截器时序问题）。([GitHub](https://github.com/hey-api/openapi-ts/issues/1703?utm_source=chatgpt.com "Is there a way to properly setup axios client interceptors? · Issue #1703"))
    

---

# 3) 如果你需要拦截器在“生成 client 时就已存在”（推荐用于模块化或 SSR）

使用 `runtimeConfigPath`（在生成配置里指定），在自定义 runtime 文件里返回带拦截器的 axios 实例：

```ts
// hey-api.runtime.ts (示例文件名，可在生成配置 runtimeConfigPath 指定)
// 此文件会在 client.gen.ts 初始化之前被调用
import type { CreateClientConfig } from './client/client.gen'
import axios from 'axios'

export const createClientConfig: CreateClientConfig = (config) => {
  const instance = axios.create({
    baseURL: process.env.VITE_API_BASE_URL || 'https://api.example.com',
  })

  // 注册拦截器（token、统一 error 处理、refresh 流程等）
  instance.interceptors.request.use((c) => {
    const token = /* 从 cookie / localStorage / pinia */ localStorage.getItem('accessToken')
    if (token) {
      c.headers = c.headers || {}
      c.headers.Authorization = `Bearer ${token}`
    }
    return c
  })

  // 返回覆盖后的配置，client.gen.ts 将使用它来初始化 client
  return {
    ...config,
    baseURL: instance.defaults.baseURL,
    axios: instance,
  }
}
```

这样 `client` 在任何地方被导入使用时都是已经带好拦截器的实例。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"))

---

# 4) 简单的 refresh-token（401）处理示例（防止并发刷新重复请求）

```ts
// response interceptor 简化示例
let isRefreshing = false
let queue: Array<(token: string | null) => void> = []

axiosInstance.interceptors.response.use(resp => resp, async (error) => {
  const original = error.config
  if (error.response?.status === 401 && !original._retry) {
    if (isRefreshing) {
      // 等待刷新完成后重试
      return new Promise((resolve, reject) => {
        queue.push((token) => {
          if (!token) return reject(error)
          original.headers.Authorization = `Bearer ${token}`
          resolve(axiosInstance(original))
        })
      })
    }
    original._retry = true
    isRefreshing = true
    try {
      const r = await axios.post('/auth/refresh', { /* ... */ })
      const newToken = r.data.accessToken
      localStorage.setItem('accessToken', newToken)
      queue.forEach(cb => cb(newToken))
      queue = []
      return axiosInstance(original)
    } catch (e) {
      queue.forEach(cb => cb(null))
      queue = []
      return Promise.reject(e)
    } finally {
      isRefreshing = false
    }
  }
  return Promise.reject(error)
})
```

---

# 5) 其它有用的技巧 / API

- 你也可以用 `createClient()` 手动创建一个 client 实例并在调用 SDK 时通过参数传入，适合临时 override。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"))
    
- 如果要移除、替换拦截器：Axios 的 `eject`/`use` 是有效的（Hey API 文档同样建议用 axios 的拦截器接口）。([npm](https://www.npmjs.com/package/%40hey-api/openapi-ts/v/0.30.0?utm_source=chatgpt.com "hey-api/openapi-ts - NPM"))
    

---

# 6) 快速定位问题时把这些贴出来我就能直接改：

- 你是如何导入/使用生成的 client 的（`import { client } from '…/client.gen'`）？
    
- 你的拦截器代码（`client.instance.interceptors…` 或 `axios.create()` + `client.setConfig`）放在哪个文件（main.ts、plugin、composable）？
    
- 项目是否是 Nuxt/SSR？（如果是需要按请求创建实例）
    

---

如果你把你现在的 `main.ts` / `client.ts` 或拦截器相关代码贴上来，我可以直接改成可运行的版本并指出具体的修正点。😊

（参考文档：Hey API 的 Axios 客户端文档，说明 `client.instance` 与 `setConfig/createClient/runtimeConfigPath` 的用法；以及社区 issue 关于 Nuxt/拦截器时序的讨论）。([Hey API](https://heyapi.dev/openapi-ts/clients/axios "Axios client | Hey API"), [npm](https://www.npmjs.com/package/%40hey-api/client-axios?utm_source=chatgpt.com "hey-api/client-axios - NPM"), [GitHub](https://github.com/hey-api/openapi-ts/issues/1703?utm_source=chatgpt.com "Is there a way to properly setup axios client interceptors? · Issue #1703"))