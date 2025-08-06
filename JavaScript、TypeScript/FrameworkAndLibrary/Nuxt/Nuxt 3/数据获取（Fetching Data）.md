数据获取 (Fetching Data) 是 Nuxt 应用的核心功能之一，尤其是在涉及服务端渲染 (SSR) 或静态站点生成 (SSG) 时。Nuxt 3 提供了强大且易用的组合式 API (Composables) 来处理数据获取。

以下是 Nuxt 3 中主要的数据获取方式，从最常用、最推荐的开始：

**1. `useFetch` (最常用，推荐)**

*   **定位**: 这是 Nuxt 3 中进行数据获取的**首选和最便捷**的方式，特别适合从单个 API 端点获取数据。你可以把它想象成 Nuxt 提供的“瑞士军刀”，内置了处理 SSR/SSG、客户端缓存、加载状态、错误处理等常见需求的逻辑。
*   **核心特性**:
    *   **同构 (Isomorphic)**: 在服务端执行获取数据（用于 SSR/SSG），并将数据传输到客户端进行**水合 (Hydration)**，避免客户端重复请求。如果是在纯客户端导航时触发，则只在客户端执行。
    *   **自动状态管理**: 返回一个包含 `data`, `pending`, `error`, `refresh` 等状态的响应式对象，方便你在模板中展示加载、错误状态，并能手动刷新数据。
    *   **自动 Key 生成**: Nuxt 会根据文件名和行号等信息自动生成一个唯一的 key，用于在不同组件/页面间**去重 (Deduplication)** 请求（如果 URL 和参数相同，只请求一次）。
    *   **类型推断**: 与 TypeScript 结合良好，能自动推断返回数据的类型（如果你的 API 返回类型信息或者你手动指定）。
    *   **基于 `$fetch`**: 底层使用 Nuxt 强大的 `$fetch` 工具。
*   **基本用法**:

    ```vue
    <!-- pages/posts/[id].vue -->
    <template>
      <div>
        <div v-if="pending">加载中...</div>
        <div v-else-if="error">加载文章失败: {{ error.message }}</div>
        <article v-else-if="post">
          <h1>{{ post.title }}</h1>
          <p>{{ post.body }}</p>
          <button @click="refresh()">刷新文章</button>
        </article>
      </div>
    </template>
    
    <script setup>
    import { useRoute } from '#imports'; // 或 vue-router

    const route = useRoute();
    const postId = route.params.id;

    // useFetch 会自动处理 URL 的响应式依赖
    // 当 postId 变化时，它理论上会自动重新获取 (取决于具体实现和 key)
    // 更可靠的方式是传入一个返回 URL 的函数
    const { data: post, pending, error, refresh } = await useFetch(() => `/api/posts/${postId}`, {
      // ----- 常用选项 -----
      // key: `post-${postId}`, // 手动指定 key，用于更精细的缓存控制或当自动 key 不足时
      // method: 'POST',
      // body: { ... },
      // query: { sort: 'asc' },
      // pick: ['id', 'title', 'body'], // 只选择需要的字段，减小 payload 体积
      // transform: (rawPost) => { // 在数据赋值给 data 之前转换数据
      //   return { ...rawPost, title: rawPost.title.toUpperCase() };
      // },
      // server: true, // (默认 true) 是否在服务端获取
      // lazy: false, // (默认 false) 是否懒加载 (见 useLazyFetch)
      // default: () => ({ title: '加载中...', body: '' }), // 提供 data 的初始/默认值，在 lazy:true 时有用
      // watch: [someRef], // 监听其他响应式 ref 的变化来触发重新获取
    });

    // 注意：顶层的 `await` 使得 setup 在数据获取完成前是阻塞的 (除非 lazy: true)。
    // 这确保了 SSR 时页面包含数据。

    // 如果 API 在 Nuxt 项目内部 (server/api/)，可以直接写相对路径 `/api/...`
    // 如果是外部 API，写完整 URL: `https://api.example.com/posts/${postId}`
    </script>
    ```

**2. `useLazyFetch` (优化首屏)**

*   **定位**: 与 `useFetch` 基本相同，但有一个关键区别：它**不会阻塞客户端导航**。
*   **核心特性**:
    *   **非阻塞**: 在服务端渲染时，它仍然会尝试获取数据，但**不会**等待请求完成。页面会先渲染并发送给客户端，数据在客户端加载完成后再更新。
    *   **改善 TTI (Time To Interactive)**: 对于非首屏关键数据，使用 `useLazyFetch` 可以让用户更快地看到页面骨架并与之交互。
    *   **`data` 初始值为 `null`**: 因为是懒加载，`data` 的初始值会是 `null` (或者你通过 `default` 选项提供的默认值)，直到客户端获取完成。因此模板中需要处理 `null` 或 `pending` 状态。
    *   **`pending` 状态**: `pending` 初始为 `true` (如果在服务端未完成) 或 `false` (如果在服务端已完成或出错)，并在客户端获取过程中变为 `true`。
*   **用法**: 与 `useFetch` 完全一致，只是名字不同，并且 `lazy` 选项默认为 `true`。

    ```vue
    <script setup>
    // 页面会立即渲染，评论稍后加载显示
    const { data: comments, pending, error } = useLazyFetch(() => `/api/posts/${postId}/comments`, {
        default: () => [] // 提供一个默认空数组，避免模板渲染错误
    });
    </script>
    <template>
        <!-- 需要处理 comments 为 null 或 pending 状态 -->
        <div v-if="pending && !comments?.length">加载评论中...</div>
        <ul v-else-if="comments">
            <li v-for="comment in comments" :key="comment.id">{{ comment.body }}</li>
        </ul>
    </template>
    ```

**3. `useAsyncData` (更灵活)**

*   **定位**: 当你需要更复杂的异步逻辑（比如需要组合多个请求、依赖其他异步操作、或使用非 `$fetch` 的数据源）时，`useAsyncData` 提供了更大的灵活性。`useFetch(url, options)` 实际上是 `useAsyncData(key, () => $fetch(url, options.fetchOptions), options)` 的语法糖。
*   **核心特性**:
    *   **接收 Handler 函数**: 第一个参数是一个**唯一的 key**（必须手动提供），第二个参数是一个返回 Promise 的**异步函数 (handler)**。Nuxt 会执行这个 handler 来获取数据。
    *   **手动 Key**: **必须**为 `useAsyncData` 提供一个唯一的 `key`，用于 SSR 数据传输和去重。
    *   **灵活性**: Handler 函数内部可以执行任何异步操作，比如多次调用 `$fetch`、访问本地存储（仅客户端）、执行计算等。
*   **用法**:

    ```vue
    <script setup>
    const userId = ref('user1');

    const { data: userData, pending, error, refresh } = await useAsyncData(
      `user-data-${userId.value}`, // 1. 唯一的 Key，通常包含动态部分
      async () => { // 2. Handler 异步函数
        const [profile, posts] = await Promise.all([
          $fetch(`/api/users/${userId.value}/profile`),
          $fetch(`/api/users/${userId.value}/posts?limit=5`)
        ]);
        return { profile, posts }; // Handler 返回的数据会赋值给 data
      },
      { // 3. 选项 (与 useFetch 类似，如 lazy, server, transform, watch, pick 等)
        watch: [userId] // 当 userId 变化时，自动重新执行 handler
      }
    );
    </script>
    <template>
        <div v-if="pending">加载用户数据...</div>
        <div v-else-if="error">错误: {{ error.message }}</div>
        <div v-else-if="userData">
            <h2>{{ userData.profile.name }}</h2>
            <ul>
                <li v-for="post in userData.posts" :key="post.id">{{ post.title }}</li>
            </ul>
            <button @click="refresh()">刷新数据</button>
        </div>
    </template>
    ```

**4. `useLazyAsyncData` (灵活 + 优化首屏)**

*   **定位**: `useAsyncData` 的懒加载版本，同样不会阻塞客户端导航。
*   **用法**: 与 `useAsyncData` 相同，只是名字不同，并且 `lazy` 选项默认为 `true`。`data` 初始值为 `null` 或 `default` 值。

**5. `$fetch` (底层工具)**

*   **定位**: 这是 Nuxt 提供的**全局、同构**的 HTTP 请求工具 (基于 ofetch)。`useFetch` 和 `useAsyncData` 底层都使用了它。它可以在应用的任何地方（服务端、客户端、插件、API 路由）使用。
*   **核心特性**:
    *   **通用**: 服务端和客户端表现一致。
    *   **自动解析**: 自动解析 JSON 响应体。
    *   **智能错误处理**: 对非 2xx 响应自动抛出包含 `response` 信息的错误。
    *   **API 代理**: 在客户端请求 Nuxt 内部 API (`/api/...`) 时，能智能处理 cookies 和代理。
    *   **无缝集成**: 可以直接请求内部 API 路由 (`server/api`)。
*   **何时直接使用 `$fetch`**?
    *   **事件处理器中**: 当用户点击按钮、提交表单时触发的请求（不是页面加载时就需要的数据）。
    *   **`setup` 之外**: 在 Vue 组件的 `methods` (Options API) 或普通的 JavaScript/TypeScript 文件中。
    *   **简单的客户端请求**: 如果你不需要 SSR/SSG、缓存、去重、pending 状态管理等 `useFetch` 提供的功能。
    *   **在 `useAsyncData` 的 handler 内部**: 这是最常见的场景，用 `$fetch` 执行实际的 HTTP 调用。
*   **注意**: **直接在 `<script setup>` 的顶层使用 `$fetch` 进行页面初始化数据获取通常不是最佳实践**，因为它不会自动处理 SSR 数据传输、去重和加载状态。你应该优先使用 `useFetch` 或 `useAsyncData`。

    ```vue
    <script setup>
    import { ref } from '#imports';

    const message = ref('');

    async function sendMessage() {
      try {
        const response = await $fetch('/api/contact', {
          method: 'POST',
          body: { message: message.value }
        });
        console.log('消息发送成功:', response);
        message.value = ''; // 清空输入
      } catch (error) {
        console.error('发送失败:', error.data || error.message);
        // 这里可以显示错误提示给用户
      }
    }
    </script>
    <template>
      <input type="text" v-model="message" />
      <button @click="sendMessage">发送消息</button>
    </template>
    ```

**总结与选择：**

| 方法                     | 主要场景                                  | 是否阻塞 (SSR/导航) | Key    | 状态管理 | 推荐度          |
| :--------------------- | :------------------------------------ | :------------ | :----- | :--- | :----------- |
| **`useFetch`**         | **单个 API 端点，页面加载时需要数据**               | 是 (默认)        | 自动     | 内置   | ★★★★★        |
| **`useLazyFetch`**     | 单个 API 端点，非关键数据，**优化首屏**              | 否             | 自动     | 内置   | ★★★★☆        |
| **`useAsyncData`**     | **复杂异步逻辑**，多请求组合，非 HTTP 源             | 是 (默认)        | **手动** | 内置   | ★★★★☆        |
| **`useLazyAsyncData`** | 复杂逻辑 + **优化首屏**                       | 否             | **手动** | 内置   | ★★★☆☆        |
| **`$fetch`**           | **事件驱动请求**，`setup` 外，`useAsyncData` 内 | 不适用           | N/A    | 手动   | ★★★☆☆ (特定场景) |

**核心优势（为什么用 Nuxt 的方法）:**

*   **SSR/SSG 兼容**: 无缝处理服务端获取和客户端水合，避免重复请求。
*   **状态管理**: `pending`, `error` 等状态开箱即用。
*   **缓存与去重**: 避免对相同资源发起多次请求。
*   **开发体验**: 简洁的 API，与 Vue 的响应式系统和 `<Suspense>` (Nuxt 内部使用) 结合良好。
*   **类型安全**: 配合 TypeScript 效果更佳。

掌握这些数据获取方法是高效开发 Nuxt 3 应用的关键！