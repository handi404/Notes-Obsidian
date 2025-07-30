**路由保护（Route Guards）**，这是构建任何非展示性 Web 应用都必不可少的一环。

---

### 1. 核心概念 (Core Concept)

*   **它是什么？**
    路由守卫，可以想象成是**应用内部的“门禁系统”**。每当用户尝试从一个页面跳转到另一个页面时，这个“门禁”就会被触发，允许我们在此刻介入，执行检查或操作。

*   **它解决了什么问题？**
    1.  **权限控制**：最常见的场景。例如，用户必须登录后才能访问个人中心（`/profile`），或者只有管理员（admin）才能访问后台管理页面（`/dashboard`）。
    2.  **数据预加载**：在进入某个路由前，提前请求该页面所需的数据。加载完成后再显示页面，可以避免页面内容一块一块地出现，提升用户体验。
    3.  **操作确认**：在用户离开某个页面时（例如一个未保存的表单），弹出确认框，防止用户意外丢失数据。

`vue-router` 提供了三种类型的路由守卫，它们在不同时机触发：
*   **全局守卫 (Global Guards)**：对所有路由都生效。
*   **路由独享守卫 (Per-Route Guard)**：只对单个路由配置生效。
*   **组件内守卫 (In-Component Guards)**：直接在组件内部定义，只对该组件的路由生效。

---

### 2. 代码示例 (`<script setup>` & TypeScript)

我们以最经典的 **“登录验证”** 为例，展示如何使用**全局前置守卫 (`beforeEach`)** 来保护路由。

#### 场景设定

*   `'/'`：首页，公开访问。
*   `'/login'`：登录页，公开访问。
*   `'/dashboard'`：仪表盘，需要登录后才能访问。

#### 步骤 1: 定义路由并添加 `meta` 字段

在路由配置中，我们使用 `meta` 字段来标记哪些路由需要权限验证。这是一种非常优雅和可扩展的方式。

**`src/router/index.ts`**

```typescript
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import HomeView from '../views/HomeView.vue';

// 模拟一个简单的认证检查函数
// 在真实项目中，这里通常会检查 Pinia store 或 localStorage/cookie 中的 token
const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('user-token');
};

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
  {
    path: '/login',
    name: 'Login',
    // 懒加载组件，提高性能
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/DashboardView.vue'),
    // highlight-start
    // 使用 meta 字段来标记此路由需要认证
    meta: {
      requiresAuth: true,
    },
    // highlight-end
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// highlight-start
// 全局前置守卫 (Global Guard)
router.beforeEach((to, from) => {
  // `to` 是目标路由对象, `from` 是源路由对象
  // 返回值决定了导航的行为：
  // - `true` 或 `undefined`: 允许导航
  // - `false`: 取消当前导航
  // - 路由地址 (字符串或对象): 重定向到该地址

  // 1. 检查目标路由是否需要认证
  if (to.meta.requiresAuth && !isAuthenticated()) {
    // 2. 如果需要认证但用户未登录，则重定向到登录页
    // 将用户尝试访问的路径作为查询参数保存，以便登录后跳回
    return {
      name: 'Login',
      query: { redirect: to.fullPath },
    };
  }

  // 3. 如果不需要认证，或者用户已登录，则允许导航
  return true;
});
// highlight-end

export default router;

```

#### 步骤 2: 在主入口文件中注册路由

**`src/main.ts`**

```typescript
import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 引入我们配置好的 router

const app = createApp(App);

app.use(router); // 注册路由

app.mount('#app');
```

这样，一个基础的路由保护就完成了。当未登录的用户尝试访问 `/dashboard` 时，会被自动重定向到 `/login?redirect=%2Fdashboard`。

---

### 3. 扩展与应用 (Extension & Application)

#### 1. 基于角色的访问控制 (RBAC)

如果你的应用有不同用户角色（如 `admin`, `editor`, `viewer`），可以扩展 `meta` 字段。

```typescript
// in router/index.ts
{
  path: '/admin',
  name: 'AdminPanel',
  component: () => import('../views/AdminView.vue'),
  meta: {
    requiresAuth: true,
    roles: ['admin', 'super-admin'], // 只有这些角色的用户才能访问
  }
}

// in router.beforeEach
router.beforeEach((to, from) => {
  const userRole = getUserRole(); // 假设这个函数能获取当前用户的角色

  if (to.meta.requiresAuth) {
    if (!isAuthenticated()) {
      return { name: 'Login', query: { redirect: to.fullPath } };
    }
    
    const requiredRoles = to.meta.roles as string[] | undefined;
    if (requiredRoles && !requiredRoles.includes(userRole)) {
      // 如果需要特定角色，但用户角色不匹配，可以重定向到无权限页面
      return { name: 'Unauthorized' }; // 假设你有一个名为 'Unauthorized' 的403页面
    }
  }
  
  return true;
});
```

#### 2. 组件内守卫：防止未保存的数据丢失

使用 `onBeforeRouteLeave` 可以在用户离开当前组件绑定的路由时进行拦截。

**`src/views/EditFormView.vue`**

```vue
<script setup lang="ts">
import { ref } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';

const isFormDirty = ref(false); // 标记表单内容是否被修改过

const onFormChange = () => {
  isFormDirty.value = true;
};

// 组件内守卫
onBeforeRouteLeave((to, from) => {
  if (isFormDirty.value) {
    const answer = window.confirm(
      '您有未保存的更改，确定要离开吗？'
    );
    // 如果用户点击“取消”，则返回 false，导航将被中止
    if (!answer) {
      return false;
    }
  }
  // 否则，允许导航
  return true;
});
</script>

<template>
  <form @input="onFormChange">
    <textarea></textarea>
    <button type="submit">保存</button>
  </form>
</template>
```

#### 3. 异步守卫与数据预加载

守卫可以是异步的。我们可以利用这一点在进入路由前加载数据。

```typescript
// in router/index.ts
{
  path: '/users/:id',
  name: 'UserProfile',
  component: () => import('../views/UserProfileView.vue'),
  // 路由独享守卫 (Per-Route Guard)
  async beforeEnter(to, from) {
    try {
      // 假设 useProfileStore().fetchProfile 返回一个 Promise
      // 在进入路由前，等待数据获取完成
      await useProfileStore().fetchProfile(to.params.id as string);
      return true; // 数据加载成功，允许导航
    } catch (error) {
      console.error('Failed to fetch user profile:', error);
      // 加载失败，可以重定向到错误页
      return { name: 'Error' };
    }
  }
}
```

---

### 4. 要点与注意事项 (Key Points & Precautions)

1.  **`next` 函数的演变**：在 `vue-router` v3 中，必须调用 `next()`、`next(false)` 或 `next('/path')`。在 **v4 中，这是可选的**。直接 `return true / false / 'path'` 更简洁、更不易出错。**请务必使用 `return` 的方式**。

2.  **避免无限重定向循环**：在 `beforeEach` 的例子中，如果你的 `/login` 页面也设置了 `meta: { requiresAuth: true }`，那么当未登录用户访问 `/dashboard` 时，会被重定向到 `/login`，然后守卫再次检查 `/login`，发现需要认证且用户未登录，又会重定向到 `/login`... 造成无限循环。**务必确保你的重定向目标（如登录页）是不需要认证的。**

3.  **全局后置钩子 `afterEach`**：它在导航**确认**之后触发，不会改变导航本身。它非常适合用来做页面分析、统计或更新页面标题 `document.title`。

    ```typescript
    router.afterEach((to, from) => {
      // 动态设置页面标题
      const title = to.meta.title as string || 'My Awesome App';
      document.title = title;
    });
    ```

4.  **前端保护 vs 后端保护**：**切记，前端的路由保护主要是为了提升用户体验（UX），而不是真正的安全（Security）**。聪明的用户可以通过开发者工具绕过前端限制。所有敏感数据和操作的权限验证，**必须在后端 API 层面再次进行**，这才是安全的基石。

5.  **类型安全**：在 TypeScript 项目中，为 `meta` 字段创建类型定义可以获得更好的类型提示和安全。

    ```typescript
    // src/router/typed.d.ts
    import 'vue-router';

    declare module 'vue-router' {
      interface RouteMeta {
        requiresAuth?: boolean;
        roles?: string[];
        title?: string;
      }
    }
    ```
    将此文件包含在 `tsconfig.json` 的 `include` 数组中，`to.meta` 就会拥有完美的类型提示。