在大型项目中，如果 API 请求散落在各个组件中，将是一场维护的噩梦。我们需要一个清晰、可扩展、易于维护的 API 层。

我们将采用一个分层设计的思想，这是构建大型应用的事实标准。

### 核心思想：三层分离架构

我们将把 API 相关的代码清晰地划分为三层，各司其职：

1.  **Axios 实例层 (The Core Engine)**：
    *   **职责**：这是最底层。负责创建和配置axios实例，设置 `baseURL`、`timeout`，以及最重要的——**全局拦截器**（我们之前讨论的实例拦截器）。它处理所有请求的共性问题，如 Token 认证、统一错误处理、数据格式化等。
    *   **文件名**：通常是 `src/utils/request.ts` 或 `src/plugins/axios.ts`。

2.  **API 模块层 (The Business Logic)**：
    *   **职责**：这一层负责定义具体的API请求函数。根据后端的业务模块（如用户、订单、商品）来组织文件。每个函数封装一个API端点，明确定义其 `url`、`method` 以及所需的参数。它调用第一层的 axios 实例来发送请求。
    *   **文件结构**：通常是 `src/api/modules/user.ts`, `src/api/modules/order.ts` 等。

3.  **视图/业务层 (The Consumer)**：
    *   **职责**：这是你的 Vue/React 组件或业务逻辑文件。这一层 **不应该直接接触到 axios**。它只需要从第二层导入 API 函数，像调用一个普通的异步函数一样使用它们，然后处理返回的数据或捕获错误。
    *   **文件名**：例如 `src/views/UserManagement.vue`。

这种分层的好处是**关注点分离**：
*   改 `baseURL` 或 `Token` 逻辑？只需要动 **第一层**。
*   后端某个接口的 URL 或参数变了？只需要动 **第二层**。
*   UI 组件的业务逻辑调整？只需要动 **第三层**。

---

### 实战演练：一步步构建优雅的 API 层（使用 TypeScript）

在大型项目中，强烈推荐使用 TypeScript，它能为你的 API 层带来无与伦比的健壮性和可维护性。

#### 第 1 步：创建目录结构

```
src/
├── api/
│   ├── modules/
│   │   ├── user.ts      # 用户相关的所有API
│   │   └── order.ts     # 订单相关的所有API
│   └── index.ts         # 统一导出所有API模块
│
└── utils/
    └── request.ts       # 我们封装的Axios实例（第一层）
```

#### 第2步：实现第一层 - `request.ts` (核心引擎)

这个文件我们之前已经写过，现在我们用 TypeScript 来增强它，加入泛型和类型定义。

**`src/utils/request.ts`**
```typescript
import axios, { type AxiosRequestConfig } from 'axios';

// 定义后端返回数据的通用结构，T 是泛型，代表 data 的具体类型
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // .env 文件中的基础路径
  timeout: 10000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' },
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('user-token');
    if (token && config.headers) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
// 这里我们使用了泛型，使得调用时可以直接获得类型提示
service.interceptors.response.use(
  (response) => {
    const res: ApiResponse = response.data;
    if (res.code !== 200) {
      // 统一错误处理，例如弹窗提示
      // ElMessage.error(res.message || 'Error');
      return Promise.reject(new Error(res.message || 'Error'));
    }
    // 直接返回核心数据，调用方可以直接拿到 data
    return res.data;
  },
  (error) => {
    // ElMessage.error(error.message || 'Network Error');
    return Promise.reject(error);
  }
);

// 导出一个通用的请求函数，并为其添加了更强的类型支持
// T 是期望的响应数据类型
const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return service(config);
};

export default request;
```
**亮点**：通过封装 `request` 函数并使用泛型 `<T>`，我们在调用 API 时可以精确地指定期望返回的数据类型，实现了端到端的类型安全。

#### 第3步：实现第二层 - `api/modules/user.ts` (API 模块)

现在我们来定义和用户相关的具体 API。

**`src/api/modules/user.ts`**
```typescript
import request, { type ApiResponse } from '@/utils/request';

// 定义用户相关的类型
export interface User {
  id: number;
  name: string;
  email: string;
}

export interface UserListParams {
  page: number;
  pageSize: number;
  keyword?: string;
}

// API 函数定义

/**
 * 获取用户列表
 * @param params - 查询参数
 */
export function getUserList(params: UserListParams) {
  // 注意这里的 <User[]>，它告诉 request 函数我们期望的 data 类型是用户数组
  return request<User[]>({
    url: '/users',
    method: 'get',
    params, // GET 请求用 params
  });
}

/**
 * 获取单个用户信息
 * @param id - 用户ID
 */
export function getUserInfo(id: number) {
  return request<User>({
    url: `/users/${id}`,
    method: 'get',
  });
}

/**
 * 创建新用户
 * @param data - 用户数据
 */
export function createUser(data: Omit<User, 'id'>) {
  return request<User>({
    url: '/users',
    method: 'post',
    data, // POST, PUT, PATCH 请求用 data
  });
}

/**
 * 更新用户信息
 * @param id - 用户ID
 * @param data - 用户数据
 */
export function updateUser(id: number, data: Partial<User>) {
  return request<User>({
    url: `/users/${id}`,
    method: 'put',
    data,
  });
}
```

#### 第4步：实现统一出口 - `api/index.ts`

为了方便在组件中导入，我们创建一个统一的出口文件。

**`src/api/index.ts`**
```typescript
import * as userApi from './modules/user';
import * as orderApi from './modules/order';

export {
  userApi,
  orderApi,
};
```
这样，我们在任何地方都可以通过 `import { userApi } from '@/api';` 来获取所有用户相关的 API。

#### 第5步：实现第三层 - `views/UserManagement.vue` (消费 API)

最后，在我们的 Vue 组件中，优雅地调用这些 API。

**`src/views/UserManagement.vue`**
```vue
<template>
  <div>
    <h1>User Management</h1>
    <div v-if="loading">Loading...</div>
    <ul v-else-if="users.length">
      <li v-for="user in users" :key="user.id">{{ user.name }} - {{ user.email }}</li>
    </ul>
    <div v-else>No users found.</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { userApi } from '@/api'; // 从统一出口导入
import type { User } from '@/api/modules/user'; // 导入类型

const users = ref<User[]>([]);
const loading = ref(false);

const fetchUsers = async () => {
  try {
    loading.value = true;
    // 调用 API 就像调用一个本地函数，非常清晰
    const userList = await userApi.getUserList({ page: 1, pageSize: 10 });
    // userList 已经被拦截器处理过，直接就是 User[] 类型的数据
    users.value = userList;
  } catch (error) {
    console.error('Failed to fetch users:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchUsers();
});
</script>
```

---

### 总结与最佳实践

1.  **分层架构**：严格遵守 `实例层 -> 模块层 -> 视图层` 的数据流，保持代码清晰。
2.  **TypeScript 全覆盖**：从请求到响应，为所有数据定义类型接口，利用泛型实现端到端的类型安全。
3.  **按模块组织**：在 `api/modules/` 目录下，根据后端微服务或业务领域划分 API 文件。
4.  **统一出口**：使用 `api/index.ts` 提供一个简洁的 API 导入入口，提升开发体验。
5.  **命名规范**：API函数名应清晰地反映其操作，如 `getUsers`、`createUser`、`deleteUser`。
6.  **环境变量**：使用 `.env` 文件（如 Vite 的 `import.meta.env.VITE_API_BASE_URL`）管理不同环境的 `baseURL`，不要硬编码。

这套架构几乎可以应对任何规模的项目，它不仅优雅，而且极其健壮和可维护，是现代前端开发的标准实践。