路由是构建单页面应用 (SPA) 的骨架，而 Vue Router 4 是为 Vue 3 和组合式 API 量身打造的、官方的路由解决方案。

---

### 路由管理：Vue Router 4

**核心思想**：在 SPA 中，我们不希望每次用户点击链接都向服务器请求一个全新的 HTML 页面。相反，我们希望在同一个页面内，根据 URL 的变化，动态地切换显示不同的组件。Vue Router 就是实现这一“URL -> 组件”映射关系的管理器。

#### 1. 安装和配置

首先，将 Vue Router 添加到你的项目中。

```bash
npm install vue-router@4
```

然后，创建并配置路由实例。最佳实践是建立一个专门的 `router` 目录。

**`src/router/index.ts`**
```typescript
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import HomeView from '../views/HomeView.vue'; // 导入你的视图组件

// 1. 定义路由表 (Route Table)
// RouteRecordRaw 是 Vue Router 提供的路由记录的类型定义
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home', // 路由命名，非常重要！
    component: HomeView,
  },
  {
    path: '/about',
    name: 'About',
    // 路由懒加载：只有当用户访问这个路由时，对应的组件代码才会被加载
    // 这对于优化首屏加载速度至关重要
    component: () => import('../views/AboutView.vue'),
  },
];

// 2. 创建 Router 实例
const router = createRouter({
  // history 模式：决定 URL 的格式
  // createWebHistory() -> HTML5 History模式 (如: /users/1) - 推荐
  // createWebHashHistory() -> Hash模式 (如: /#/users/1)
  history: createWebHistory(import.meta.env.BASE_URL),
  routes, // 简写，相当于 routes: routes
});

// 3. 导出 Router 实例
export default router;
```

最后，在你的主入口文件 `main.ts` 中“安装”这个路由实例。

**`src/main.ts`**
```typescript
import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 导入我们创建的 router

const app = createApp(App);

app.use(router); // 将路由实例挂载到 Vue 应用上

app.mount('#app');
```

---

#### 2. 定义路由表：动态路由与嵌套路由

**动态路由 (Dynamic Routes)**
当我们需要一个模式匹配多个 URL 时（例如用户详情页 `/users/1`, `/users/2`），使用动态路由。

```typescript
// in src/router/index.ts
const routes: Array<RouteRecordRaw> = [
  {
    // :id 就是一个动态段 (param)
    path: '/users/:id',
    name: 'UserDetails',
    component: () => import('../views/UserDetailsView.vue'),
  }
];
```
在 `UserDetailsView.vue` 组件中，我们可以通过 `useRoute()` 来获取这个 `id`。

**嵌套路由 (Nested Routes)**
用于构建复杂的布局，例如一个用户中心页面，内部可以切换“个人资料”、“我的帖子”等子视图。

```typescript
// in src/router/index.ts
const routes: Array<RouteRecordRaw> = [
  {
    path: '/user',
    name: 'UserLayout',
    component: () => import('../layouts/UserLayout.vue'), // 父级路由组件，包含一个 <RouterView>
    children: [ // 使用 children 数组定义子路由
      {
        path: 'profile', // 路径不以 '/' 开头，它会拼接在父路径后 -> /user/profile
        name: 'UserProfile',
        component: () => import('../views/user/ProfileView.vue'),
      },
      {
        path: 'posts', // -> /user/posts
        name: 'UserPosts',
        component: () => import('../views/user/PostsView.vue'),
      }
    ]
  }
];
```
**`UserLayout.vue`** 必须包含一个 `<RouterView>` 来为子路由提供渲染出口。

---

#### 3. `<RouterLink>` 和 `<RouterView>`

这两个是 Vue Router 提供的全局组件，用于导航和渲染。

*   **`<RouterView>`**: 路由的出口。它会渲染当前 URL 匹配到的路由组件。
*   **`<RouterLink>`**: 导航的链接。它会被渲染成一个带有正确 `href` 的 `<a>` 标签。

**`App.vue` (或任何需要导航的地方)**
```vue
<template>
  <header>
    <nav>
      <!-- to 属性指定目标路由 -->
      <!-- 使用 name 进行绑定是最佳实践，因为即使 path 改变了，链接依然有效 -->
      <RouterLink :to="{ name: 'Home' }">Home</RouterLink>
      <RouterLink :to="{ name: 'About' }">About</RouterLink>
      <!-- 传递动态参数 -->
      <RouterLink :to="{ name: 'UserDetails', params: { id: 123 } }">User 123</RouterLink>
    </nav>
  </header>

  <main>
    <!-- 匹配到的路由组件将在这里渲染 -->
    <RouterView />
  </main>
</template>

<style>
/* Vue Router 会自动为激活的链接添加 'router-link-active' 和 'router-link-exact-active' class */
.router-link-active {
  font-weight: bold;
}
</style>
```

---

#### 4. 组合式函数：`useRoute()` & `useRouter()`

这是在 `<script setup>` 中与路由交互的标准方式。

*   **`useRoute()`**: 提供对**当前路由信息**的访问（只读）。你可以把它看作是当前路由的“身份证”。
*   **`useRouter()`**: 提供**路由器实例**。你可以用它来执行导航操作。

**`UserDetailsView.vue`**
```vue
<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';

// 1. 获取当前路由信息
const route = useRoute();
// 2. 获取路由器实例
const router = useRouter();

const userId = ref<string | string[]>(route.params.id);
const user = ref(null);

onMounted(async () => {
  // route.params 是响应式的，但通常在挂载时获取一次即可
  const response = await fetch(`/api/users/${userId.value}`);
  user.value = await response.json();
});

// 3. 编程式导航
function goToNextUser() {
  const nextId = Number(userId.value) + 1;
  // router.push() - 在历史记录中添加一条新记录
  router.push({ name: 'UserDetails', params: { id: nextId } });
}

function goHomeAndReplaceHistory() {
  // router.replace() - 替换当前历史记录，用户无法通过“后退”按钮回来
  // 常用于登录成功后的跳转
  router.replace({ name: 'Home' });
}
</script>

<template>
  <div v-if="user">
    <h1>User Details for ID: {{ userId }}</h1>
    <pre>{{ user }}</pre>
    <button @click="goToNextUser">Next User</button>
  </div>
  <div v-else>Loading...</div>
</template>
```

---

#### 5. 路由守卫：实现页面访问控制

路由守卫是实现权限控制（如登录拦截）的核心。最常用的是**全局前置守卫** `router.beforeEach`。

**核心思想**：在每一次路由跳转**发生之前**，都会执行这个守卫函数。在这个函数里，我们可以检查用户的权限，并决定是放行、中断还是重定向。

**在 `src/router/index.ts` 中配置守卫**
```typescript
// ... (之前的 router 配置代码)

// 添加 meta 字段来标记需要认证的路由
const routes: Array<RouteRecordRaw> = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/DashboardView.vue'),
    meta: { requiresAuth: true } // 自定义元信息
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'),
  },
  // ... 其他路由
];

const router = createRouter({ /* ... */ });

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 模拟一个检查用户是否登录的函数
  const isLoggedIn = !!localStorage.getItem('token');
  
  // 检查目标路由是否需要认证
  if (to.meta.requiresAuth && !isLoggedIn) {
    // 如果用户未登录，则重定向到登录页
    // next() 是 Vue Router 3 的用法，在 Vue Router 4 中，直接 return 一个路由对象即可
    return { name: 'Login', query: { redirect: to.fullPath } }; // 将用户想去的页面作为 query 参数，登录后可以跳回
  }
  
  // 如果一切正常，则允许导航
  // return true; // 等同于 next()
  return true; // 推荐写法，更明确
});

export default router;
```

#### **要点/注意事项**

1.  **命名路由 (`name`)**：始终给你的路由命名。这使得在 `<RouterLink>` 和 `router.push()` 中引用路由时更健壮。如果以后修改了 `path`，只要 `name` 不变，代码就无需更改。
2.  **`useRoute` vs `useRouter`**：初学者最容易混淆。请记住：
    *   `route` 是“路线图”，告诉你**当前在哪**（`params`, `query`）。
    *   `router` 是“汽车”，让你**去往别处**（`push`, `replace`）。
3.  **路由懒加载**：对于非首屏的视图组件，务必使用 `() => import(...)` 的方式进行懒加载，这能极大提升应用的初始加载性能。
4.  **`meta` 字段**：路由的 `meta` 字段是存放自定义信息的理想场所，如页面标题、权限要求 (`requiresAuth`)、布局信息等。
5.  **滚动行为**：可以通过 `createRouter` 的 `scrollBehavior` 选项自定义页面跳转后的滚动位置，例如，模拟浏览器“后退”时的滚动位置，或总是滚动到页面顶部。
    ```typescript
    const router = createRouter({
      history: createWebHistory(),
      routes,
      scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
          return savedPosition; // 如果有保存的位置（例如浏览器后退），则恢复
        } else {
          return { top: 0 }; // 否则滚动到顶部
        }
      },
    });
    ```