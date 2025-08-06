重点讲解 Nuxt 3 中用于页面导航的核心组件：`<NuxtLink />`。

简单来说，`<NuxtLink />` 是 Nuxt 提供的**专门用于在应用内部页面之间进行跳转的组件**。你应该**优先使用它**来进行导航，而不是使用标准的 HTML `<a>` 标签。

**为什么必须用 `<NuxtLink />` 而不是 `<a>`？**

*   **客户端路由 (Client-Side Routing)**：
    *   使用 `<NuxtLink />` 会触发 **Vue Router**（Nuxt 底层使用的路由库）的导航机制。这意味着浏览器**不会**重新加载整个页面。
    *   相反，它只会更新 URL，并动态地在 `<NuxtPage />` 组件的位置渲染目标页面的内容。这提供了**单页应用 (SPA)** 的流畅体验。
    *   如果使用 `<a href="...">`，浏览器会发起一个新的 HTTP 请求，导致整个页面刷新，丢失当前应用状态，体验较差且效率低下。

*   **智能预加载 (Prefetching)**：
    *   这是 `<NuxtLink />` 的一个**核心性能优势**。当一个 `<NuxtLink />` 组件进入用户的视口（viewport）时，Nuxt 会**自动在后台预先加载**目标页面所需的 JavaScript 代码（以及可能的数据，如果配置了的话）。
    *   这样，当用户真正点击链接时，页面资源已经准备就绪，跳转几乎是**瞬间完成**的，极大提升了用户感受到的速度。
    *   默认是开启的，可以通过 `no-prefetch` 属性或全局配置禁用。

*   **激活状态类名 (Active Class Handling)**：
    *   `<NuxtLink />` 能自动判断当前链接是否对应着浏览器地址栏中的当前路由。
    *   它会自动为匹配的链接添加 CSS 类名（默认为 `router-link-active` 和 `router-link-exact-active`），方便你为当前激活的导航项设置不同的样式（如下划线、加粗、变色等）。

**如何使用 `<NuxtLink />`：**

最基本的使用方式是传递 `to` 属性，指定目标路径：

```vue
<template>
  <nav>
    <ul>
      <li><NuxtLink to="/">首页</NuxtLink></li>
      <li><NuxtLink to="/about">关于我们</NuxtLink></li>
      <li><NuxtLink to="/posts/my-first-post">我的第一篇文章</NuxtLink></li>
      <li><NuxtLink to="/users/123">用户 123</NuxtLink></li>
    </ul>
  </nav>
</template>
```

**绑定动态路径或传递参数：**

当链接目标是动态的，或者需要传递路由参数/查询参数时，你需要使用 `v-bind:` 或其简写 `:` 来绑定 `to` 属性为一个**对象**：

```vue
<template>
  <div>
    <!-- 假设 userId 是一个变量 -->
    <NuxtLink :to="`/users/${userId}`">查看用户 {{ userId }} (字符串模板)</NuxtLink>

    <!-- 推荐：使用路由名称和参数（更健壮，不易因 URL 结构改变而失效） -->
    <!-- Nuxt 根据 pages/users/[id].vue 自动生成路由名称 'users-id' -->
    <NuxtLink :to="{ name: 'users-id', params: { id: userId } }">
      查看用户 {{ userId }} (命名路由)
    </NuxtLink>

    <!-- 传递查询参数 -->
    <NuxtLink :to="{ path: '/search', query: { keyword: 'nuxt' } }">
      搜索 "nuxt"
    </NuxtLink>
  </div>
</template>

<script setup>
const userId = ref('abc'); // 假设从 API 或其他地方获取
</script>
```

**重要属性 (Props)：**

*   `to` (必填): 目标路由。可以是字符串路径，也可以是描述路由的对象（包含 `path`, `name`, `params`, `query` 等）。
*   `href`: `<NuxtLink>` 最终会渲染成一个 `<a>` 标签，`href` 属性会被自动根据 `to` 的值计算出来。通常你不需要直接设置 `href`。
*   `active-class` (String, 默认: `'router-link-active'`): 当链接**匹配**当前路由时（包括父路由）应用的 CSS 类名。
*   `exact-active-class` (String, 默认: `'router-link-exact-active'`): 当链接**精确匹配**当前路由时应用的 CSS 类名。
*   `replace` (Boolean, 默认: `false`): 如果为 `true`，导航时会调用 `history.replaceState()` 而不是 `history.pushState()`。这意味着用户点击浏览器后退按钮时，不会回到这个链接之前的页面。
*   `prefetch` / `no-prefetch` (Boolean): 控制单个链接的预加载行为。`no-prefetch` 会禁用该链接的预加载。`prefetch` (Nuxt 3.7+ 可用) 可以强制开启（如果全局禁用了）。通常用默认行为即可。
*   `external` (Boolean, 默认: `false`): 如果设置为 `true`，Nuxt 会将此链接视为**外部链接**。它仍然渲染为 `<a>` 标签，但**不会**触发客户端路由，并且会自动添加 `target="_blank"` 和 `rel="noopener noreferrer"` 属性，使其在新标签页中安全打开。适用于指向其他网站的链接。

```vue
<template>
  <NuxtLink to="/settings" active-class="my-active-link">设置 (自定义激活样式)</NuxtLink>
  <NuxtLink to="https://nuxtjs.org" external>Nuxt 官网 (外部链接)</NuxtLink>
  <NuxtLink to="/dashboard" no-prefetch>仪表盘 (禁用预加载)</NuxtLink>
</template>

<style>
.my-active-link {
  font-weight: bold;
  color: green;
}
/* 默认的激活类也可以直接用 */
.router-link-exact-active {
  text-decoration: underline;
}
</style>
```

**总结：**

`<NuxtLink />` 是 Nuxt 应用内部导航的**标准且推荐**的方式。

*   它提供**无缝的客户端路由**体验。
*   通过**智能预加载**提升感知性能。
*   自动处理**激活状态**的 CSS 类名，方便样式化。
*   使用 `to` 属性指定目标，支持字符串和对象形式，可以方便地处理动态路由和参数。
*   提供了 `external`, `no-prefetch` 等属性来处理特殊情况。

在你的 Nuxt 项目中，只要是在页面间跳转，就应该优先考虑使用 `<NuxtLink />`。