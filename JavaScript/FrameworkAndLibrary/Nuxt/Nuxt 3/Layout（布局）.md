Nuxt 3 中的 **Layout (布局)**。这是一个非常有用的功能，可以帮你轻松管理应用中多个页面的**通用 UI 结构**。

**核心理念：提取公共框架**

想象一下，你的网站或应用通常都有一些**固定不变的部分**，比如：

*   **顶部导航栏 (Header)**
*   **底部页脚 (Footer)**
*   **侧边栏菜单 (Sidebar)**

如果每个页面 (`pages/*.vue`) 都要重复写这些公共部分的 HTML 和逻辑，那会非常繁琐且难以维护。

**Layout (布局) 就是用来解决这个问题的！**

你可以把这些公共的 UI 结构提取到一个**布局组件**中，然后让你的页面**嵌套**在这个布局里面。布局就像一个**“相框”**，而你的页面内容就是放进相框里的**“照片”**。

**如何工作：**

1.  **创建 `layouts/` 目录**：在项目根目录下创建 `layouts` 文件夹。
2.  **创建布局文件 (`.vue`)**：在 `layouts/` 目录下创建 Vue 组件文件。文件名就是布局的名称（比如 `default.vue`, `custom.vue`）。
3.  **使用 `<slot />`**：在布局组件的模板中，你需要使用内置的 `<slot />` 组件。**这个 `<slot />` 就是占位符，用来告诉 Nuxt “把当前页面的内容渲染到这里”**。

**默认布局 (`layouts/default.vue`)**

*   Nuxt 有一个特殊的约定：如果存在 `layouts/default.vue` 文件，它将**自动**应用于所有**没有**明确指定其他布局的页面。
*   这是最常用的布局，通常用来放置网站的通用页头和页脚。

**示例：创建一个默认布局**

```vue
<!-- layouts/default.vue -->
<template>
  <div class="app-layout">
    <header class="app-header">
      <!-- 网站 Logo, 导航链接等 -->
      <nav>
        <NuxtLink to="/">首页</NuxtLink> |
        <NuxtLink to="/about">关于</NuxtLink> |
        <NuxtLink to="/products">产品</NuxtLink>
      </nav>
    </header>

    <main class="app-content">
      <!-- 关键：页面组件的内容会在这里渲染 -->
      <slot />
    </main>

    <footer class="app-footer">
      <!-- 版权信息等 -->
      <p>&copy; 2024 我的 Nuxt 应用</p>
    </footer>
  </div>
</template>

<script setup>
// 布局文件也可以有自己的 setup 逻辑，
// 比如获取全局数据、设置头部信息等
// useHead({ titleTemplate: '%s - 我的网站' });
</script>

<style scoped>
/* 布局相关的样式 */
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.app-header {
  background-color: #f0f0f0;
  padding: 1rem;
}
.app-content {
  flex-grow: 1; /* 让内容区域填充剩余空间 */
  padding: 1rem;
}
.app-footer {
  background-color: #333;
  color: white;
  padding: 1rem;
  text-align: center;
}
</style>
```

**如何为页面指定布局：**

*   **自动使用默认布局**：如果 `layouts/default.vue` 存在，并且你的页面文件（如 `pages/about.vue`）没有特殊指定，那么它会自动被 `default.vue` 包裹。
*   **使用特定布局**：如果你想让某个页面使用**不同**的布局（比如一个管理后台页面使用 `admin.vue` 布局），你需要在该页面的 `<script setup>` 中使用 `definePageMeta` 宏：

    1.  先创建自定义布局文件：

        ```vue
        <!-- layouts/admin.vue -->
        <template>
          <div class="admin-layout">
            <aside class="admin-sidebar">
              <!-- 管理后台侧边栏 -->
              <ul>
                <li><NuxtLink to="/admin/dashboard">仪表盘</NuxtLink></li>
                <li><NuxtLink to="/admin/settings">设置</NuxtLink></li>
              </ul>
            </aside>
            <main class="admin-content">
              <slot /> <!-- 页面内容放这里 -->
            </main>
          </div>
        </template>

        <style scoped>
        .admin-layout { display: flex; }
        .admin-sidebar { width: 200px; background: lightgray; padding: 1rem; }
        .admin-content { flex-grow: 1; padding: 1rem; }
        </style>
        ```

    2.  在需要使用此布局的页面中指定：

        ```vue
        <!-- pages/admin/dashboard.vue -->
        <template>
          <h1>管理后台仪表盘</h1>
          <p>这里是仪表盘内容...</p>
        </template>

        <script setup>
        // 关键：告诉 Nuxt 这个页面使用 'admin' 布局
        definePageMeta({
          layout: 'admin' // 对应 layouts/admin.vue 文件名 (小写)
        });
        </script>
        ```

*   **禁用布局**：如果某个页面（比如一个完全独立的登录页）**不希望**使用任何布局（即使 `default.vue` 存在），可以设置 `layout: false`：

    ```vue
    <!-- pages/login.vue -->
    <template>
      <div class="login-page">
        <h1>请登录</h1>
        <!-- 登录表单 -->
      </div>
    </template>

    <script setup>
    // 关键：禁用布局
    definePageMeta({
      layout: false
    });
    </script>
    <style scoped>
    /* 这个页面的样式将完全独立 */
    .login-page { /* ... */ }
    </style>
    ```

**总结：**

Nuxt Layouts 是一个强大的特性，用于创建可复用的页面框架：

1.  **`layouts/` 目录** 存放布局组件。
2.  **`<slot />` 组件** 在布局中标记页面内容的插入点。
3.  **`layouts/default.vue`** 是默认布局，自动应用于未指定布局的页面。
4.  使用页面组件的 **`definePageMeta({ layout: 'layout-name' })`** 来指定特定布局。
5.  使用 **`definePageMeta({ layout: false })`** 来禁用布局。

合理使用布局可以大大提高代码的**复用性**和**可维护性**，让你的项目结构更清晰。