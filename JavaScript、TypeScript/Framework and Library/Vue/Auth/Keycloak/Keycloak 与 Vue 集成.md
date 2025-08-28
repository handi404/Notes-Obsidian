探讨如何将 `keycloak-js` 与 Vue.js (Vue 3) 进行优雅、高效的集成。

将 `keycloak-js` 集成到 Vue 中，我们的核心目标有两个：

1.  **时机控制**：确保在 Vue 应用渲染之前，Keycloak 的初始化（包括重定向和 Token 获取）已经完成。
2.  **状态共享**：让 Keycloak 的实例（包含认证状态、用户信息、Token 等）在整个 Vue 应用的组件中可以被轻松、响应式地访问。

基于这两个目标，最佳实践是**创建一个 Vue 插件**。

---

### 集成策略：通过插件和响应式封装

我们将分三步走：

1.  **创建 Keycloak 服务**：封装 Keycloak 的初始化逻辑，并使用 Vue 3 的 `reactive` API 使其状态可被追踪。
2.  **创建 Vue 插件**：将 Keycloak 服务注入到整个应用中。
3.  **在主程序中应用**：在 `main.js` 中，先初始化 Keycloak，成功后再挂载 Vue 应用。

---

### 详细步骤

#### 步骤 1: 创建 Keycloak 服务 (`src/services/keycloak.js`)

这个文件是整个集成的心脏。它负责初始化 Keycloak 并将其关键属性包装成一个响应式对象。

```javascript
// src/services/keycloak.js

import Keycloak from 'keycloak-js';
import { reactive } from 'vue';

// 1. Keycloak 配置
const keycloakConfig = {
  url: 'http://your-keycloak-server/auth',
  realm: 'your-realm',
  clientId: 'your-client-id',
};

// 2. 创建 Keycloak 实例
const _keycloak = new Keycloak(keycloakConfig);

// 3. 创建一个响应式对象来存储 Keycloak 的状态
// 这是关键！直接使用 keycloak 实例在 Vue 中不是响应式的。
const state = reactive({
  isAuthenticated: false,
  profile: null,
  token: null,
  roles: [],
  hasRole: (role) => state.roles.includes(role), // 提供一个方便的检查角色的方法
});

/**
 * 更新响应式 state 的函数
 */
const updateState = () => {
  state.isAuthenticated = _keycloak.authenticated;
  state.token = _keycloak.token;
  if (_keycloak.authenticated) {
    // 解析 Token 获取角色信息
    state.roles = _keycloak.tokenParsed?.realm_access?.roles || [];
    // 加载用户信息
    _keycloak.loadUserProfile().then(profile => {
      state.profile = profile;
    });
  } else {
    state.roles = [];
    state.profile = null;
  }
};

/**
 * 初始化函数
 * @returns {Promise<Keycloak>} 返回 Keycloak 实例的 Promise
 */
const init = () => {
  return new Promise((resolve, reject) => {
    _keycloak.init({
      onLoad: 'login-required', // 或者 'check-sso'
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      pkceMethod: 'S256', // 显式指定 PKCE 方法，这是最佳安全实践
    })
    .then(authenticated => {
      console.log(`Keycloak authenticated: ${authenticated}`);
      updateState();
      resolve(_keycloak); // 初始化成功后，resolve Keycloak 实例
    })
    .catch(error => {
      console.error('Keycloak initialization failed', error);
      reject(error);
    });
  });
};

// 定时刷新 Token
_keycloak.onTokenExpired = () => {
  _keycloak.updateToken(30).then(refreshed => {
    if (refreshed) {
      console.log('Token was successfully refreshed');
      updateState();
    } else {
      console.log('Token is still valid');
    }
  }).catch(() => {
    console.error('Failed to refresh the token, or the session has expired');
    _keycloak.logout(); // 刷新失败，强制登出
  });
};


// 导出我们需要的一切
export default {
  keycloak: _keycloak, // 原始实例，用于调用 logout, login 等方法
  state,              // 响应式状态，用于在组件中读取信息
  init,               // 初始化函数
};
```

#### 步骤 2: 创建 Vue 插件 (`src/plugins/keycloak.js`)

这个插件负责将我们的 Keycloak 服务提供给所有组件。

```javascript
// src/plugins/keycloak.js

import keycloakService from '@/services/keycloak';

export default {
  install: (app) => {
    // 使用 provide/inject，这是 Vue 3 推荐的跨组件通信方式
    app.provide('keycloak', keycloakService);

    // 为了方便在 Options API 中使用，也可以挂载到全局属性
    // 在 Vue 3 中，this.$keycloak 这样用
    app.config.globalProperties.$keycloak = keycloakService;
  }
};
```

#### 步骤 3: 在 `main.js` 中使用插件并控制初始化流程

这是将所有部分串联起来的地方。**关键点：必须在 `keycloakService.init()` 的 `.then()` 回调中挂载 Vue 应用。**

```javascript
// src/main.js

import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 假设你使用了 Vue Router
import keycloakService from '@/services/keycloak';
import keycloakPlugin from '@/plugins/keycloak';

// 最关键的一步：先初始化 Keycloak，再创建和挂载 Vue 应用
keycloakService.init()
  .then(() => {
    const app = createApp(App);

    app.use(router);
    app.use(keycloakPlugin); // 使用我们的插件

    app.mount('#app');
  })
  .catch(error => {
    // 如果 Keycloak 初始化失败，你可以在这里渲染一个错误提示
    document.body.innerHTML = '<h1>Error: Could not connect to Keycloak. Please try again later.</h1>';
  });

```

---

### 在组件中使用

现在，你可以在任何组件中轻松地访问 Keycloak 的状态和方法。

#### 使用 Composition API (`<script setup>`) - **推荐方式**

```vue
<template>
  <div>
    <div v-if="keycloak.state.isAuthenticated">
      <h1>Welcome, {{ keycloak.state.profile?.firstName }} {{ keycloak.state.profile?.lastName }}!</h1>
      <p>Username: {{ keycloak.state.profile?.username }}</p>
      <p>Email: {{ keycloak.state.profile?.email }}</p>

      <div v-if="keycloak.state.hasRole('admin')">
        <p>You have admin rights!</p>
      </div>

      <button @click="handleLogout">Logout</button>
    </div>
    <div v-else>
      <h1>Please log in.</h1>
      <button @click="handleLogin">Login</button>
    </div>
  </div>
</template>

<script setup>
import { inject } from 'vue';

// 通过 inject 获取 Keycloak 服务
const keycloak = inject('keycloak');

const handleLogout = () => {
  // 调用原始实例上的方法
  keycloak.keycloak.logout({ redirectUri: window.location.origin });
};

const handleLogin = () => {
  keycloak.keycloak.login();
};
</script>
```

---

### 进阶：保护路由 (Vue Router)

这是实际应用中的必备功能。我们可以使用 Vue Router 的**导航守卫**来实现。

在你的 `src/router/index.js` 文件中：

```javascript
import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Dashboard from '../views/Dashboard.vue';
import AdminPanel from '../views/AdminPanel.vue';
import keycloakService from '@/services/keycloak'; // 导入我们的服务

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      // meta 字段用于定义路由元信息
      requiresAuth: false // 这个页面不需要认证
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: {
      requiresAuth: true // 这个页面需要认证
    }
  },
  {
    path: '/admin',
    name: 'AdminPanel',
    component: AdminPanel,
    meta: {
      requiresAuth: true,
      requiredRoles: ['admin'] // 这个页面不仅需要认证，还需要 'admin' 角色
    }
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const { state } = keycloakService;

  // 1. 检查路由是否需要认证
  if (to.meta.requiresAuth) {
    // 2. 检查用户是否已认证
    if (state.isAuthenticated) {
      // 3. 检查是否需要特定角色
      if (to.meta.requiredRoles && to.meta.requiredRoles.length > 0) {
        const hasAllRoles = to.meta.requiredRoles.every(role => state.hasRole(role));
        if (hasAllRoles) {
          next(); // 权限足够，放行
        } else {
          // 角色不足，可以重定向到无权限页面或首页
          alert("You don't have permission to access this page.");
          next('/'); 
        }
      } else {
        next(); // 不需要特定角色，直接放行
      }
    } else {
      // 用户未认证，但由于我们用了 'login-required'，理论上不会走到这里。
      // 但如果是 'check-sso'，就需要手动触发登录。
      const loginUrl = keycloakService.keycloak.createLoginUrl();
      window.location.href = loginUrl; // 重定向到登录页
    }
  } else {
    // 路由不需要认证，直接放行
    next();
  }
});


export default router;
```

### 总结与要点

1.  **分离关注点**：通过 `service` 和 `plugin` 的模式，将 Keycloak 的逻辑与 Vue 的视图逻辑清晰地分离开。
2.  **响应式是关键**：直接使用 `keycloak` 实例在 Vue 中是非响应式的。必须用 `reactive()` 或 `ref()` 包装其状态，才能让视图自动更新。
3.  **初始化先行**：永远记住，Vue 应用的 `mount` 操作必须等待 Keycloak 的异步 `init` 完成。
4.  **路由保护**：利用 `router.beforeEach` 和路由的 `meta` 字段，可以实现强大且灵活的访问控制。
5.  **API 请求拦截器**：别忘了结合 `axios` 拦截器。你的 API client 应该在每次请求前调用 `keycloak.keycloak.updateToken()` 来确保 Token 的有效性。