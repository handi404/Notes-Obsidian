将用户信息（如角色 `roles`、用户ID `sub` 等）存放在 Token 中，特别是 **JWT (JSON Web Token)**，是现代 Web 应用认证和授权的行业标准。

下面，详细讲解如何安全、高效地解析 JWT，并在 Vue 应用中利用这些信息。

---

### 1. 核心概念 (Core Concept)

*   **它是什么？**
    **JSON Web Token (JWT)** 是一种开放标准（RFC 7519），它定义了一种紧凑且自包含的方式，用于在各方之间安全地传输信息。你可以把它想象成一张**“数字身份证”**。

    这张“身份证”由三部分组成，用点 `.` 分隔：
    1.  **Header (头部)**：包含令牌的类型（`JWT`）和使用的签名算法（如 `HS256`）。
    2.  **Payload (载荷)**：包含“声明”（claims），也就是我们关心的信息，比如用户ID、用户名、角色、过期时间（`exp`）等。**这部分是明文的（经过 Base 64 Url 编码），所以绝不能存放敏感信息如密码！**
    3.  **Signature (签名)**：用头部指定的算法，将 `Header`、`Payload` 和一个只有后端知道的**密钥（Secret）**进行签名。它的作用是**验证令牌的完整性**，确保它在传输过程中没有被篡改。

*   **它解决了什么问题？**
    它使得前端可以在不直接查询数据库的情况下，获得用户的基本身份信息和权限信息。当用户登录成功后，后端生成一个 JWT 并返回给前端。前端将其存储起来，在后续的每次 API 请求中都携带这个 JWT。后端通过验证签名来确认用户的身份和权限。

    **在前端，我们的主要任务是：从 Payload 中解码（decode）出用户信息，用于 UI 展示和路由控制。**

---

### 2. 代码示例 (`<script setup>` & TypeScript)

我们将使用一个轻量级、零依赖的库 `jwt-decode` 来完成解码工作。

#### 步骤 1: 安装依赖

```bash
npm install jwt-decode
```

#### 步骤 2: 创建一个 Pinia Store 来管理认证状态

这是最佳实践。将认证逻辑（Token 存储、用户状态、解析逻辑）集中在 Pinia store 中，便于整个应用共享和维护。

**`src/stores/auth.ts`**

```typescript
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { jwtDecode } from 'jwt-decode';

// 1. 定义用户信息的类型接口
interface User {
  id: string;
  name: string;
  roles: string[];
}

// 2. 定义 JWT Payload 的类型接口
// `sub` (subject) 和 `exp` (expiration time) 是 JWT 的标准字段
interface JwtPayload {
  sub: string;       // 用户ID
  name: string;      // 用户名
  roles: string[];   // 角色数组
  exp: number;       // 过期时间 (Unix时间戳)
  iat?: number;      // 签发时间 (Unix时间戳)
}

export const useAuthStore = defineStore('auth', () => {
  // --- State ---
  const token = ref<string | null>(localStorage.getItem('user-token'));
  const user = ref<User | null>(null);

  // --- Getters ---
  const isAuthenticated = computed(() => !!token.value && !!user.value);
  const userRoles = computed(() => user.value?.roles ?? []);

  // --- Actions ---

  /**
   * 登录并处理 Token
   * @param newToken 后端返回的 JWT
   */
  function login(newToken: string) {
    try {
      // 解码 JWT 获取 payload
      const decoded = jwtDecode<JwtPayload>(newToken);

      // 检查 Token 是否过期
      if (decoded.exp * 1000 < Date.now()) {
        console.error('Token is expired.');
        logout();
        return;
      }
      
      // 更新 Pinia state
      token.value = newToken;
      user.value = {
        id: decoded.sub,
        name: decoded.name,
        roles: decoded.roles,
      };

      // 将 Token 存入 localStorage 实现持久化登录
      localStorage.setItem('user-token', newToken);

    } catch (error) {
      console.error('Failed to decode or process token:', error);
      logout(); // 如果解析失败，则清空状态
    }
  }

  /**
   * 登出
   */
  function logout() {
    token.value = null;
    user.value = null;
    localStorage.removeItem('user-token');
  }
  
  /**
   * 应用初始化时，尝试从 localStorage 恢复登录状态
   */
  function tryAutoLogin() {
    if (token.value) {
      login(token.value); // 复用 login 函数的逻辑来解析和验证
    }
  }

  return {
    token,
    user,
    isAuthenticated,
    userRoles,
    login,
    logout,
    tryAutoLogin
  };
});
```

#### 步骤 3: 在应用入口处初始化认证状态

**`src/main.ts`**

```typescript
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useAuthStore } from './stores/auth'; // 引入 auth store

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

// 在挂载应用前，尝试自动登录
// highlight-start
const authStore = useAuthStore();
authStore.tryAutoLogin();
// highlight-end

app.use(router);
app.mount('#app');
```

#### 步骤 4: 在路由守卫中使用

现在，我们的路由守卫可以变得更强大、更真实。

**`src/router/index.ts`**

```typescript
import { useAuthStore } from '../stores/auth';

router.beforeEach((to, from) => {
  const authStore = useAuthStore();

  // 检查需要认证的路由
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'Login', query: { redirect: to.fullPath } };
  }

  // 检查需要特定角色的路由
  const requiredRoles = to.meta.roles as string[] | undefined;
  if (requiredRoles && !requiredRoles.every(role => authStore.userRoles.includes(role))) {
    // 如果用户的角色不满足要求，重定向到无权限页面
    return { name: 'Unauthorized' };
  }
  
  return true;
});
```

---

### 3. 扩展与应用 (Extension & Application)

#### 1. 自动附加 Token 到 API 请求 (Axios Interceptors)

手动在每个 API 请求中添加 `Authorization` 头是繁琐且易错的。使用 Axios 拦截器可以自动化这个过程。

```typescript
// services/api.ts (或者你封装 axios 的地方)
import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const apiClient = axios.create({
  baseURL: 'https://api.example.com',
});

// 请求拦截器
apiClient.interceptors.request.use(config => {
  const authStore = useAuthStore();
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});
```

#### 2. Token 自动续期 (Silent Refresh)

Access Token 通常有较短的有效期（如 15 分钟-1 小时）。当它过期时，用户体验会很差。常见的解决方案是使用 **Refresh Token**。

*   **流程**：
    1.  登录时，后端返回 `accessToken` 和 `refreshToken`。`refreshToken` 有效期更长（如 7 天）。
    2.  `accessToken` 过期，API 返回 401 Unauthorized。
    3.  Axios **响应拦截器**捕获 401 错误。
    4.  拦截器使用 `refreshToken` 调用后端的 `/refresh_token` 接口。
    5.  后端验证 `refreshToken`，返回一个新的 `accessToken`。
    6.  拦截器用新的 `accessToken` 重新发起之前失败的请求。
    7.  整个过程对用户透明。

这是一个更高级但非常实用的模式，能极大提升用户体验。

---

### 4. 要点与注意事项 (Key Points & Precautions)

1.  **安全第一：前端只解码，不校验**
    前端使用 `jwt-decode` 只是为了读取 Payload 中的数据。**我们绝对不能、也无法在前端验证 JWT 的签名**，因为这需要后端的密钥（secret），而密钥绝不能泄露到前端。**所有权限的最终决策权必须在后端**，后端在处理每个受保护的 API 请求时，都必须重新验证 JWT 签名。

2.  **Payload 是透明的**
    再次强调，JWT 的 Payload 只是 Base 64 Url 编码，不是加密。任何能拿到你 Token 的人都可以轻松解码并看到其中的内容。**绝对不要在 Payload 中存放密码、身份证号等高度敏感信息。**

3.  **Token 存储位置的选择：`localStorage` vs `HttpOnly Cookie`**
    *   **`localStorage`**: 实现简单。但容易受到 **XSS（跨站脚本攻击）** 的影响。如果你的网站有 XSS 漏洞，攻击者可以执行脚本读取 `localStorage` 中的 Token 并盗用。
    *   **`HttpOnly` Cookie**: 更安全的选择。设置了 `HttpOnly` 标志的 Cookie 不能被 JavaScript 访问，可以有效防御 XSS 攻击。但需要后端 API 配合设置 Cookie，并且可能需要处理 **CSRF（跨站请求伪造）** 的风险（可以通过 `SameSite` 属性缓解）。
    *   **结论**：对于安全要求极高的应用，`HttpOnly` Cookie 是首选。对于大多数中小型项目，使用 `localStorage` 并做好 XSS 防护是可接受的折衷方案。

4.  **处理 Token 过期**
    在 `useAuthStore` 的 `tryAutoLogin` 方法中，我们已经包含了对过期时间的检查。这是非常重要的一步，可以防止应用使用一个已经失效的 Token，导致后续 API 请求全部失败。