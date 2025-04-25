Nuxt 3 提供的那些**内置 API**，它们主要是以 **Composables (组合式函数)** 的形式提供，并且得益于 Nuxt 的**自动导入 (Auto Imports)** 功能，你可以在你的 `.vue` 文件、`composables/` 目录以及 `plugins/` 中直接使用它们，无需手动 `import`。

这些内置 API 极大地简化了开发流程，并帮助你处理 Nuxt 应用中的常见任务，尤其是在服务端渲染 (SSR) 和数据获取方面。

以下是一些最核心和常用的 Nuxt 3 内置 API (Composables)，按功能分类：

**1. 数据获取 (Data Fetching)**

这是 Nuxt 的核心优势之一，相关 API 能很好地处理跨服务端/客户端的数据获取。

*   **`useFetch`**:
    *   **用途:** 最常用的数据获取函数，可以在组件设置 (setup) 函数、插件和路由中间件中使用。它能在**服务端和客户端**执行 fetch 请求。
    *   **关键特性:**
        *   **SSR 友好:** 在服务端获取数据，并将数据“载荷 (payload)”传递到客户端，避免客户端二次请求 (hydration)。
        *   **自动管理状态:** 返回 `data`, `pending`, `error`, `refresh` 等响应式状态。
        *   **快捷方式:** 相当于封装了 `useAsyncData` + `$fetch`。
    *   **示例:** `const { data: posts, pending, error } = await useFetch('/api/posts')`

*   **`useAsyncData`**:
    *   **用途:** 当你需要更精细地控制数据获取逻辑时使用，或者当获取的数据不直接来自一个简单的 URL fetch 时（例如，调用一个复杂的 service 函数）。
    *   **关键特性:**
        *   需要提供一个唯一的 `key` 来防止在客户端水合时重复获取数据。
        *   需要提供一个异步处理函数 (handler) 来执行数据获取逻辑。
        *   同样处理 SSR 数据传递和响应式状态。
    *   **示例:** `const { data: user } = await useAsyncData('user', () => $fetch('/api/user/me'))`

*   **`useLazyFetch` / `useLazyAsyncData`**:
    *   **用途:** 与 `useFetch` / `useAsyncData` 类似，但**不会阻塞客户端导航**。数据会在后台加载，加载完成前 `data` 为 `null`。
    *   **关键特性:** 适用于非关键数据的加载，可以提升页面切换的感知速度。`pending` 状态会告诉你数据是否仍在加载中。
    *   **示例:** `const { data: comments, pending } = useLazyFetch('/api/comments')` // 注意没有 await

*   **`refreshNuxtData`**:
    *   **用途:** 手动刷新由 `useAsyncData` 或 `useFetch` 获取的所有数据，或者通过提供 key 来刷新特定数据。
    *   **示例:** `await refreshNuxtData()` 或 `await refreshNuxtData('user')`

*   **`clearNuxtData`**:
    *   **用途:** 手动清除 `useAsyncData` 或 `useFetch` 缓存的数据。
    *   **示例:** `clearNuxtData('user')`

*   **`$fetch` (全局可用工具)**:
    *   **用途:** Nuxt 提供的**通用同构 Fetch 库** (基于 [ofetch](https://github.com/unjs/ofetch))。`useFetch` 等内部就是使用了它。你也可以直接用它进行简单的 API 调用，特别是在非 setup 环境或需要更底层控制时。
    *   **关键特性:** 自动处理 JSON 解析、错误处理、提供 Base URL 配置、请求拦截等。
    *   **示例:** `const data = await $fetch('/api/config')`

**2. 状态管理 (State Management)**

*   **`useState`**:
    *   **用途:** 创建**跨组件、SSR 友好**的响应式状态。这是 Nuxt 内置的、轻量级的状态管理方案。
    *   **关键特性:**
        *   在服务端创建的状态会自动序列化并传递到客户端，并在客户端恢复。
        *   需要提供一个唯一的 `key`。
        *   返回一个 `Ref`。
    *   **示例:**
        *   定义: `const counter = useState('counter', () => 0)`
        *   使用: `const counter = useState('counter')` (在其他组件中获取已定义的状态)

**3. 访问 Nuxt 应用上下文 (App Context)**

*   **`useNuxtApp`**:
    *   **用途:** 获取当前的 Nuxt 应用实例。这非常强大，可以访问很多底层信息和功能。
    *   **关键特性:**
        *   可以访问 Vue app 实例 (`nuxtApp.vueApp`)。
        *   可以访问运行时配置、插件注入的内容、生命周期钩子 (`hook`)、载荷 (`payload`) 等。
        *   是编写 Nuxt 插件时的核心 API。
    *   **示例:** `const nuxtApp = useNuxtApp(); console.log(nuxtApp.payload.data); nuxtApp.hook('page:finish', () => { /* ... */ })`

**4. 路由与导航 (Routing & Navigation)**

*   **`useRoute`**:
    *   **用途:** 获取当前路由的详细信息 (响应式的)。类似 Vue Router 的 `useRoute`。
    *   **关键特性:** 包含 `path`, `params`, `query`, `hash`, `meta` 等信息。
    *   **示例:** `const route = useRoute(); console.log(route.params.id)`

*   **`useRouter`**:
    *   **用途:** 获取 Vue Router 实例，用于编程式导航。类似 Vue Router 的 `useRouter`。
    *   **关键特性:** 提供 `push`, `replace`, `go`, `back`, `forward` 等方法。
    *   **示例:** `const router = useRouter(); router.push('/about')`

*   **`navigateTo`**:
    *   **用途:** Nuxt 提供的一个更方便的**导航工具函数**，可以在 setup、插件、中间件甚至服务端 API 中使用。
    *   **关键特性:**
        *   可以在服务端触发客户端重定向。
        *   支持外部 URL 导航。
        *   接受路由对象或 URL 字符串。
    *   **示例:** `await navigateTo('/dashboard')` 或 `await navigateTo('https://nuxtjs.org', { external: true })`

**5. Meta 标签与 SEO**

*   **`useHead`**:
    *   **用途:** **响应式地管理**页面的 `<head>` 标签内容 (如 `title`, `meta`, `link`, `script`)。
    *   **关键特性:** 支持 SSR，自动处理标签的合并与覆盖。
    *   **示例:** `useHead({ title: 'My Awesome Page', meta: [{ name: 'description', content: '...' }] })`

*   **`useSeoMeta`**:
    *   **用途:** `useHead` 的一个更专注于常见 SEO meta 标签的便捷封装。
    *   **关键特性:** 简化了 `title`, `description`, `ogTitle`, `ogDescription`, `ogImage`, `twitterCard` 等标签的设置。
    *   **示例:** `useSeoMeta({ title: 'Page Title', description: 'My page description.', ogImage: 'image.png' })`

**6. 运行时配置 (Runtime Config)**

*   **`useRuntimeConfig`**:
    *   **用途:** 访问在 `nuxt.config.ts` 中定义的**运行时配置**变量。
    *   **关键特性:**
        *   区分 `public` (客户端和服务端都可用) 和 `private` (仅服务端可用) 配置。
        *   允许通过环境变量覆盖配置。
    *   **示例:** `const config = useRuntimeConfig(); console.log(config.public.apiBase); // 在 server/ 目录下可以访问 config.privateKey`

**7. Cookie 管理**

*   **`useCookie`**:
    *   **用途:** **SSR 友好**地读取和写入 Cookie。
    *   **关键特性:** 返回一个 `Ref`，可以像操作 `ref` 一样操作 Cookie 值，并自动同步。支持设置 Cookie 选项 (如 `maxAge`, `httpOnly`)。
    *   **示例:** `const userToken = useCookie('user_token'); userToken.value = 'new-token';`

**8. 错误处理 (Error Handling)**

*   **`showError`**:
    *   **用途:** 手动触发 Nuxt 的错误页面展示。
    *   **关键特性:** 可以传递错误对象或简单的消息和状态码。
    *   **示例:** `showError({ statusCode: 404, statusMessage: 'Page Not Found' })`

*   **`clearError`**:
    *   **用途:** 清除当前显示的 Nuxt 错误，并尝试重定向回之前的页面或指定页面。
    *   **示例:** `clearError({ redirect: '/' })`

**9. 服务端专用 API (仅在 `server/` 目录下可用)**

Nuxt 3 的 `server/` 目录允许你编写 API 路由、服务器中间件等。在这个上下文中，还有一些特殊的 Composable 或工具函数：

*   **`useRequestEvent`**: 获取当前请求的 H 3 Event 对象 (底层服务器引擎是 H 3)。
*   **`useRequestHeaders` / `getRequestHeaders`**: 获取请求头。
*   **`readBody`**: 读取请求体。
*   **`setCookie` / `parseCookies`**: 处理 Cookie。
*   **`sendRedirect`**: 发送重定向响应。
*   ... 等等，这些主要用于构建 API 和处理服务器逻辑。

**总结:**

Nuxt 3 的内置 API (Composables) 是其强大功能和优秀开发体验的核心组成部分。它们：

*   **易于使用:** 得益于自动导入，可以直接使用。
*   **功能强大:** 解决了 SSR、数据获取、状态管理、路由、SEO 等关键问题。
*   **SSR 友好:** 大部分 API 都考虑到了服务端渲染的场景。
*   **类型安全:** Nuxt 3 对 TypeScript 有良好支持，这些 API 都有类型定义。

熟练掌握这些 API 是高效开发 Nuxt 3 应用的关键。建议多查阅 Nuxt 3 官方文档，了解每个 API 的详细选项和用法。

还有哪个具体的 API 或者方面你想深入了解吗？