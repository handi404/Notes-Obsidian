## 无痛迁移与核心体验
用 Axios 完成最常见的 GET 和 POST 请求，并直观感受它相比 `fetch` 的三大核心优势：**数据处理更简单**、**错误处理更自然**、**功能更集成**。

---

### 1. 安装与引入

首先，在你的项目里安装 Axios。

```bash
npm install axios
```

然后在你的 JavaScript 文件中引入它：

```javascript
import axios from 'axios';
```

---

### 2. 发起 GET 请求（获取数据）

假设我们要从 `/api/users/1` 获取 ID 为 1 的用户信息。

#### **回顾：`fetch` 的方式**

你需要两步操作：1. 发起请求；2. 手动将响应体解析为 JSON。

```javascript
// fetch 方式
async function getUserWithFetch() {
  try {
    const response = await fetch('/api/users/1');
    const data = await response.json(); // <-- 需要手动调用 .json()
    console.log(data);
  } catch (error) {
    console.error('网络请求失败:', error);
  }
}
```

#### **现代：Axios 的方式**

Axios 将这两步合二为一。响应数据直接在 `response.data` 中，并且已经自动帮你解析好了。

```javascript
// axios 方式
async function getUserWithAxios() {
  try {
    // 使用对象解构，直接获取 data
    const { data } = await axios.get('/api/users/1'); // <-- 一步到位
    console.log(data); // data 就是解析好的 JSON 对象
  } catch (error) {
    // 错误处理，稍后详述
    console.error('请求出错:', error);
  }
}
```

> **Axios 核心优势 (1/3): 自动的 JSON 转换**
>
> *   **`fetch`**: 你总是需要检查响应是否成功，然后调用 `res.json()` 或 `res.text()`。
> *   **`axios`**: 响应体 (`response.data`) 已经根据响应头的 `Content-Type` 自动解析完毕。你几乎不需要关心这个过程，直接使用数据即可。这减少了大量重复的模板代码。

**扩展知识：Axios 的响应对象**
虽然我们通常只用 `data`，但 Axios 的完整响应对象 `response` 包含了所有你需要的信息，非常便于调试：
```javascript
const response = await axios.get('/api/users/1');

console.log(response.data);      // { id: 1, name: '张三' } (我们最常用的)
console.log(response.status);    // 200
console.log(response.statusText);// 'OK'
console.log(response.headers);   // { 'content-type': 'application/json', ... }
console.log(response.config);    // 发起这个请求时的完整配置信息
```

---

### 3. 发起 POST 请求（提交数据）

假设我们要提交一个登录表单，数据为 `{ username: 'admin', password: '123' }`。

#### **回顾：`fetch` 的方式**

你需要三步操作：1. 手动将 JS 对象序列化为 JSON 字符串；2. 在 `headers` 中手动设置 `Content-Type`；3. 发起请求。

```javascript
// fetch 方式
async function loginWithFetch() {
  const loginData = { username: 'admin', password: '123' };

  try {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json', // <-- 必须手动设置
      },
      body: JSON.stringify(loginData), // <-- 必须手动序列化
    });
    const result = await response.json();
    console.log(result);
  } catch (error) {
    console.error('网络请求失败:', error);
  }
}
```

#### **现代：Axios 的方式**

Axios 再次简化了一切。你只需将 JS 对象作为第二个参数传入，它会自动帮你完成序列化和设置请求头。

```javascript
// axios 方式
async function loginWithAxios() {
  const loginData = { username: 'admin', password: '123' };

  try {
    // 直接把 JS 对象作为数据传递
    const { data } = await axios.post('/api/login', loginData); // <-- 优雅，简洁
    console.log(data);
  } catch (error) {
    console.error('请求出错:', error);
  }
}
```

> **Axios 核心优势 (2/3): 自动的数据序列化**
>
> *   **`fetch`**: 你需要时刻记住为不同 `Content-Type` 做不同的数据处理（`JSON.stringify` for `json`, `new FormData()` for `multipart/form-data`...）。
> *   **`axios`**: 当你传递一个普通 JS 对象时，它默认会帮你序列化成 JSON 并设置好请求头。这让你能更专注于业务逻辑，而不是 HTTP 的细节。

---

### 4. 错误处理（关键区别！）

这是 Axios 最为人称道的优点之一。

#### **`fetch` 的痛点**

`fetch` 只有在网络层面失败（比如断网、DNS 查询失败）时才会 `reject` Promise (进入 `catch` 块)。对于像 404 (Not Found)、500 (Server Error) 这样的 HTTP 错误，`fetch` 会认为请求是“成功”的（`resolve`），你必须在 `then` 里面通过 `response.ok` 属性来手动判断。

```javascript
// fetch 必须手动检查 HTTP 状态
async function fetchErrorExample() {
  try {
    const response = await fetch('/api/a-path-that-does-not-exist'); // 假设返回 404
    if (!response.ok) { // <-- 必须加这行判断！
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    // ...
  } catch (error) {
    console.error(error);
  }
}
```

#### **Axios 的优雅之处**

Axios 对 HTTP 错误的处理方式更符合编程直觉：**任何非 2xx 的状态码都会导致 Promise 被 `reject`**。这意味着所有类型的错误（网络错误、HTTP 错误）都可以被同一个 `catch` 块捕获。

```javascript
// axios 自动处理 HTTP 错误
async function axiosErrorExample() {
  try {
    // 这一行会直接抛出异常，代码会立即跳转到 catch 块
    await axios.get('/api/a-path-that-does-not-exist'); // 假设返回 404
  } catch (error) {
    if (error.response) {
      // 请求已发出，服务器用状态码响应 (非 2xx)
      console.log('服务器响应错误:', error.response.data);
      console.log('状态码:', error.response.status);
    } else if (error.request) {
      // 请求已发出，但没有收到响应 (例如断网)
      console.log('网络错误，未收到响应:', error.request);
    } else {
      // 在设置请求时触发了一些错误
      console.log('请求配置错误:', error.message);
    }
  }
}
```

> **Axios 核心优势 (3/3): 统一的错误处理模型**
>
> *   **`fetch`**: 你需要区分处理“网络失败”和“HTTP 失败”，容易遗漏判断，导致 bug。
> *   **`axios`**: 统一使用 `try...catch` 处理所有问题。并且 `error` 对象包含了极其丰富的信息（`response`, `request`, `config`），让你能轻松定位问题所在。

---

### **第一阶段总结**

掌握了 Axios 的核心用法，并体验了它相比 `fetch` 的三大便利：

1.  **自动 JSON 解析**：`response.data` 直接可用。
2.  **自动数据序列化**：`post` 请求直接传对象。
3.  **统一错误处理**：所有非 2xx 状态码自动进入 `catch`。


## 掌握精髓 - 全局配置与拦截器
**目标：** 学会使用 Axios 最强大的功能——创建实例和拦截器。这是从“会用”到“会组织”的关键一步，也是区分 Axios 和 `fetch` 的核心所在。完成这个阶段，你就能搭建出企业级的、可维护的请求架构。

---

### 1. 为什么要创建实例？(`axios.create`)

在第一阶段，我们使用的是全局的 `axios` 对象，例如 `axios.get(...)`。这在小练习中没问题，但在真实项目中，**强烈不推荐**。

**问题场景：**
想象你的应用需要同时请求两个不同的 API 服务：
1.  **我方后端服务：** `https://api.my-app.com/v1`，所有请求都需要带上 `Authorization` token。
2.  **一个公开的天气服务：** `https://api.weather.com`，不需要任何特殊配置。

如果使用全局 `axios`，你会很痛苦。为我方后端设置的 `headers` 会被错误地发送到天气服务，为天气服务设置的 `timeout` 也可能不适用于我方后端。它们互相干扰。

**解决方案：`axios.create()`**

`axios.create()` 可以创建一个全新的、隔离的 Axios 实例。每个实例都可以有自己独立的配置（`baseURL`, `headers`, `timeout` 等）。

```javascript
import axios from 'axios';

// 实例1: 用于请求我方后端 API
const myApi = axios.create({
  baseURL: 'https://api.my-app.com/v1',
  timeout: 5000, // 请求超时时间 5s
});

// 实例2: 用于请求天气服务
const weatherApi = axios.create({
  baseURL: 'https://api.weather.com',
  timeout: 10000,
});

// 使用起来和全局 axios 一样，但配置是独立的
async function fetchData() {
  // 请求会发往: https://api.my-app.com/v1/users
  const { data: users } = await myApi.get('/users');

  // 请求会发往: https://api.weather.com/forecast?city=beijing
  const { data: forecast } = await weatherApi.get('/forecast', {
    params: { city: 'beijing' } // GET 请求传参的正确方式
  });
}
```

> **Axios 核心优势 (4/5): 实例隔离与模块化**
>
> *   **`fetch`**: 没有实例概念。你需要自己封装大量函数来管理不同 API 的 `baseURL`、`headers` 等，非常繁琐且容易出错。
> *   **`axios`**: 通过 `axios.create()`，可以为不同的服务创建独立的、预配置好的客户端。这是构建大型应用的基础。

---

### 2. 拦截器 (Interceptors) - Axios 的灵魂

拦截器允许你在请求被**发送前**或响应被**处理前**拦截并修改它们。这是 Axios 最强大的功能，没有之一。它让你能将通用逻辑（如认证、日志、格式化）从业务代码中抽离出来。

拦截器分为两种：**请求拦截器**和**响应拦截器**。

#### **请求拦截器 (`request.use`)**

**最经典场景：自动添加 Token**

假设用户登录后，你得到了一个 token，后续所有对我方后端的请求都需要在请求头中携带 `Authorization: Bearer <token>`。

**没有拦截器的做法（很糟糕）：**
你需要在每个请求中手动添加 `headers`。

```javascript
const token = localStorage.getItem('token');
myApi.get('/profile', { headers: { 'Authorization': `Bearer ${token}` } });
myApi.post('/posts', data, { headers: { 'Authorization': `Bearer ${token}` } });
// ... 重复，噩梦！
```

**使用请求拦截器（优雅）：**
在创建 `myApi` 实例后，为其设置一个请求拦截器。

```javascript
// 在 myApi 实例上设置请求拦截器
myApi.interceptors.request.use(
  (config) => {
    // 在请求被发送之前做些什么
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    // 必须返回 config 对象，否则请求会被阻塞
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 现在，所有使用 myApi 的请求都会自动带上 token！
// 业务代码变得极其干净
await myApi.get('/profile');
await myApi.post('/posts', { title: 'New Post' });
```

#### **响应拦截器 (`response.use`)**

**场景 1：简化数据结构**

我们已经知道 Axios 的响应数据在 `response.data` 中。在业务代码里，我们总是写 `const { data } = await myApi.get(...)`。这有点重复。我们可以用响应拦截器来简化它。

```javascript
// 在 myApi 实例上设置响应拦截器
myApi.interceptors.response.use(
  (response) => {
    // 状态码为 2xx 时进入这里
    // 直接返回 response.data，业务代码就可以直接拿到数据了
    return response.data;
  },
  (error) => {
    // 任何非 2xx 的状态码都会进入这里
    // 这里可以做统一的错误处理
    return Promise.reject(error);
  }
);

// 使用起来更爽了！
// 之前: const { data: profile } = await myApi.get('/profile');
// 现在: const profile = await myApi.get('/profile');
// profile 直接就是后端返回的数据对象
```

**场景 2：统一错误处理**

这是响应拦截器最强大的用途。比如，当后端返回 `401 Unauthorized` (通常是 token 失效)，我们应该清除本地 token 并跳转到登录页。

```javascript
myApi.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    // 统一处理 HTTP 错误
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // Token 失效，跳转到登录页
          console.error('认证失败，请重新登录');
          localStorage.removeItem('token');
          // location.href = '/login'; // 实际项目中会这样做
          break;
        case 404:
          console.error('请求的资源未找到');
          break;
        case 500:
          console.error('服务器内部错误');
          break;
        default:
          console.error(`发生错误: ${error.response.status}`);
      }
    }
    // 将错误继续抛出，以便业务代码中的 catch 块可以捕获
    return Promise.reject(error);
  }
);

// 业务代码中
try {
  const user = await myApi.get('/user-that-might-not-exist');
} catch(err) {
  // 这里仍然可以捕获到错误，进行针对性的 UI 处理
  // 但通用的错误提示（如 401 跳转）已经在拦截器中处理了
  console.log('在业务代码中捕获到了错误，可以更新 UI 状态');
}
```

> **Axios 核心优势 (5/5): AOP 式的请求/响应处理**
>
> *   **`fetch`**: 没有拦截器。所有通用逻辑都必须通过函数包装来实现，这被称为“高阶函数”或“组合”模式。虽然可行，但代码结构远不如拦截器清晰直观。
> *   **`axios`**: 拦截器提供了完美的“切面”（Aspect），让你能优雅地将通用逻辑注入到所有请求的生命周期中，实现了业务代码和基础设置的完全解耦。

---

### **第二阶段总结**

现在你已经掌握了 Axios 的两大王牌：**实例创建**和**拦截器**。

1.  **`axios.create()`**: 为不同服务创建独立配置的客户端，实现模块化。
2.  **`interceptors.request.use()`**: 在请求前统一处理，如添加 Token、加载动画。
3.  **`interceptors.response.use()`**: 在响应后统一处理，如简化数据、全局错误处理。

## 企业级封装与模块化
将这些武器组合起来，构建一个真正健壮、可维护、可扩展的 API 层。这部分内容更多是关于**工程化和代码组织**，是区分初级和资深开发者的重要分水岭。

**目标：** 将 Axios 实例和所有 API 请求进行模块化组织，形成一个清晰的、团队成员都能轻松理解和使用的 API 层。

---

### 1. 封装统一的 Axios 实例

在真实项目中，我们不会在业务组件里直接导入和使用 `axios` 或 `axios.create()`。我们会创建一个专门的文件来负责这件事。这遵循了“单一职责原则”。

**实践：创建 `request.js` 文件**

在你的项目 `src` 目录下，创建一个类似 `utils/request.js` 或 `api/index.js` 的文件。这个文件将做三件事：
1.  创建并配置 Axios 实例。
2.  设置请求和响应拦截器。
3.  导出这个配置好的实例。

**`src/utils/request.js` 文件示例：**

```javascript
import axios from 'axios';

// 1. 创建 Axios 实例
const service = axios.create({
  // 在 .env 文件中配置 VUE_APP_BASE_API (或 VITE_API_BASE_URL)
  // 这样可以方便地在不同环境（开发、测试、生产）中使用不同的 API 地址
  baseURL: process.env.VUE_APP_BASE_API || '/api', 
  timeout: 10000, // 请求超时时间
});

// 2. 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求前做些什么，比如加上 token
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  error => {
    // 对请求错误做些什么
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// 3. 响应拦截器
service.interceptors.response.use(
  /**
   * 如果你想直接获取像 headers 或 status 这样的信息
   * 请直接 return  response
  */
  response => {
    // 对响应数据做点什么
    // 假设后端返回的数据结构总是: { code: 200, message: '...', data: ... }
    const res = response.data;

    // 如果 code 不是 20000 (或 200)，则判定为错误。
    // 这里只是一个例子，请根据你自己的后端接口规范进行修改
    if (res.code !== 20000 && res.code !== 200) {
      // 弹出错误信息，例如使用 Element Plus 的 Message 组件
      // Message({ message: res.message || 'Error', type: 'error' });
      console.error('API Error: ' + res.message);

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // 可以做一个弹窗，确认后重定向到登录页
        // MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', { ... })
        console.log('Token expired, redirect to login');
      }
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      // 如果 code 正确，则直接返回 data 部分
      return res.data;
    }
  },
  error => {
    // 处理 HTTP 网络错误
    console.log('HTTP Error: ' + error); // for debug
    // Message({ message: error.message, type: 'error' });
    return Promise.reject(error);
  }
);

// 4. 导出实例
export default service;
```

**这个文件做了什么？**

*   **集中配置：** `baseURL` 和 `timeout` 都被集中管理。
*   **环境变量：** `baseURL` 从环境变量读取，这是现代前端项目的标准实践，可以轻松切换开发、测试和生产环境的 API 地址。
*   **统一认证：** 请求拦截器自动处理 token。
*   **统一业务错误处理：** 响应拦截器不仅处理 HTTP 错误，还处理后端返回的**业务逻辑错误**（例如 `code: 4001`, "用户名已存在"）。这是非常关键的一步，它让业务代码无需再关心这些通用的错误判断。
*   **数据简化：** 成功的响应直接返回 `res.data`，进一步简化业务代码。

---

### 2. API 模块化

现在我们有了一个强大的、配置好的 `request` 实例。下一步是根据业务功能来组织我们的 API 请求。

**问题：** 如果把所有 API 请求函数都写在一个文件里，当项目变大时，这个文件会变得臃肿不堪，难以维护。

**解决方案：按业务模块拆分 API 文件**

假设你的应用有“用户管理”和“商品管理”两大功能。我们就可以创建两个对应的 API 文件。

**目录结构示例：**
```
src/
├── api/
│   ├── index.js     (就是我们上面写的 request.js，有些人喜欢叫 index.js)
│   ├── user.js      (所有与用户相关的 API)
│   └── product.js   (所有与商品相关的 API)
└── views/
    ├── user/
    │   └── Profile.vue
    └── product/
        └── List.vue
```

**`src/api/user.js` 文件示例：**

```javascript
// 导入我们封装好的 request 实例
import request from '@/utils/request'; // @ 是 webpack/vite 的 alias，指向 src

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data, // ES6 简写，等同于 data: data
  });
}

export function getUserInfo(id) {
  return request({
    url: `/user/info/${id}`,
    method: 'get',
  });
}

// 使用更简洁的别名方法
export function updateUser(id, data) {
  return request.put(`/user/${id}`, data);
}
```

**`src/api/product.js` 文件示例：**

```javascript
import request from '@/utils/request';

export function getProductList(params) {
  return request({
    url: '/product/list',
    method: 'get',
    params, // GET 请求的参数放在 params 中
  });
}

export function createProduct(data) {
  return request.post('/product/create', data);
}
```

---

### 3. 在业务组件中优雅地调用

现在，在你的 Vue 或 React 组件中，调用 API 变得非常简单、清晰和直观。

**`src/views/user/Profile.vue` 组件示例：**

```vue
<template>
  <div>
    <h1>{{ user.name }}</h1>
    <p>{{ user.email }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// 1. 只导入需要的 API 函数
import { getUserInfo } from '@/api/user';

const user = ref({});
const userId = 1; // 假设从路由参数或 store 中获取

onMounted(async () => {
  try {
    // 2. 调用 API，代码清晰，语义明确
    // 你完全不需要关心 token, baseURL, 错误处理等细节
    const userInfo = await getUserInfo(userId);
    user.value = userInfo;
  } catch (error) {
    // 捕获到的通常是拦截器处理后，仍然需要业务组件关心的错误
    console.error('获取用户信息失败，可能是该用户不存在:', error);
    // 在这里可以做一些 UI 反馈，比如显示 "用户不存在" 的提示
  }
});
</script>
```

**这种架构的巨大优势：**

1.  **高内聚，低耦合：** `request.js` 负责所有请求的通用逻辑。`user.js` 只负责用户相关的 API 定义。业务组件只负责调用 API 并处理 UI。职责清晰。
2.  **可维护性极强：** 如果后端修改了某个接口的 URL 或方法，你只需要去对应的 API 模块文件（如 `user.js`）修改一行代码即可，所有使用该 API 的组件都无需改动。
3.  **可读性高：** `getUserInfo(1)` 比 `request.get('/user/info/1')` 更能表达业务意图。
4.  **易于团队协作：** 团队成员可以并行开发不同的 API 模块和业务组件，互不干扰。

---

### **第三阶段总结**

你已经学会了如何像资深工程师一样组织项目的 API 层。这是从“能用”到“好用、好维护”的质变。

**核心思想：**
1.  **集中管理：** 将 Axios 实例和拦截器封装在单一文件中。
2.  **模块化：** 按业务功能拆分 API 请求函数到不同文件中。
3.  **面向业务：** 导出的 API 函数名应体现业务意图，而不是 HTTP 方法。

## 高阶技巧与现代应用 (持续学习)
学习一些能让你从容应对复杂需求、并让代码更健壮的高级技巧。

**目标：** 掌握请求取消、文件处理、并发请求以及与 TypeScript 的集成。这些是你在真实复杂项目中一定会遇到的场景。

---

### 1. 取消请求 (`AbortController`) - 现代标准

**为什么要取消请求？**
想象一个场景：一个搜索框，用户每输入一个字就向后端发送请求。如果用户快速输入 "axios"，会依次发送 "a", "ax", "axi", "axio", "axios" 五个请求。实际上，我们只关心最后一个请求的结果。前面的请求不仅浪费服务器资源，还可能因为返回顺序错乱导致页面显示错误的数据（"axio" 的结果可能比 "axios" 的结果后返回）。

**解决方案：`AbortController`**
`AbortController` 是浏览器和 Node.js 的原生 API，用于创建一个可以中止一个或多个 Web 请求的信号。Axios v0.22.0 开始全面拥抱这个标准，并**废弃了**旧的 `CancelToken` API。**请务必学习 `AbortController`**。

**如何使用：**
1.  创建一个 `AbortController` 实例。
2.  将其 `signal` 传递给 Axios 请求的 `config`。
3.  在需要取消时，调用 `controller.abort()`。

**代码示例 (以 React/Vue 搜索框为例):**

```javascript
import { useState, useEffect, useRef } from 'react'; // React 示例
import { searchApi } from '@/api/search';

function SearchComponent() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  // 使用 useRef 存储 controller，以便在多次渲染间保持同个引用
  const controllerRef = useRef(null);

  useEffect(() => {
    // 每次 query 变化时，如果 query 为空则不处理
    if (!query) {
      setResults([]);
      return;
    }

    // 1. 如果存在上一个 controller，则中止上一次的请求
    if (controllerRef.current) {
      controllerRef.current.abort();
    }

    // 2. 创建一个新的 AbortController
    const newController = new AbortController();
    controllerRef.current = newController;

    const performSearch = async () => {
      try {
        const data = await searchApi(query, {
          // 3. 将 signal 传递给请求
          signal: newController.signal
        });
        setResults(data);
      } catch (error) {
        // 当请求被中止时，axios 会抛出一个名为 'CanceledError' 的错误
        if (axios.isCancel(error)) {
          console.log('Request canceled:', error.message);
        } else {
          // 处理其他错误
          console.error('Search failed:', error);
        }
      }
    };

    // 通常会结合 debounce 使用
    const debounceTimer = setTimeout(performSearch, 300);

    // 清理函数，在组件卸载或 query 再次变化时执行
    return () => clearTimeout(debounceTimer);

  }, [query]);

  return (
    <input type="text" value={query} onChange={e => setQuery(e.target.value)} />
    // ...渲染 results
  );
}
```

**关键点：**
*   当 `controller.abort()` 被调用，与该 `signal` 关联的 Axios 请求会立即 `reject` Promise。
*   Axios 会抛出一个特殊的 `CanceledError`。你可以使用 `axios.isCancel(error)` 来判断错误是否由取消操作引起，从而避免向用户显示不必要的错误提示。

---

### 2. 与 TypeScript 结合 - 追求类型安全

使用 TypeScript 可以为你的 API 请求和响应数据提供强大的类型约束，极大减少运行时错误。

**实践：为 API 添加类型**

1.  **定义接口类型**
    创建一个 `types/api.ts` 文件或在 API 模块旁定义类型。

    ```typescript
    // src/api/types/user.ts
    export interface User {
      id: number;
      name: string;
      username: string;
      email: string;
    }
    ```

2.  **在 API 函数中使用泛型**
    修改你的 API 模块文件（例如 `user.ts`）。Axios 的 `get`, `post` 等方法都支持泛型。

    ```typescript
    // src/api/user.ts
    import request from '@/utils/request';
    import type { User } from './types/user'; // 导入类型

    // 泛型 <User> 指定了 `request.get` 成功后返回的数据类型
    // 因为我们的响应拦截器返回 res.data，所以这个类型对应的是 data 的类型
    export function getUserInfo(id: number): Promise<User> {
      return request.get<User>(`/users/${id}`);
    }

    // 对于 POST/PUT，可以为请求体和响应体都指定类型
    export function updateUser(id: number, data: Partial<User>): Promise<User> {
      // Partial<User> 表示 data 可以是 User 的部分属性
      return request.put<User>(`/users/${id}`, data);
    }
    ```
    **注意：** 这里的 `Promise<User>` 是因为我们的 `request` 实例的响应拦截器直接返回了 `res.data`。如果你的拦截器返回的是完整的 `response` 对象，那么类型应该是 `Promise<AxiosResponse<User>>`。

3.  **在组件中享受类型提示和安全**

    ```typescript
    // src/views/Profile.vue
    import { ref, onMounted } from 'vue';
    import { getUserInfo } from '@/api/user';
    import type { User } from '@/api/types/user';

    const user = ref<User | null>(null);

    onMounted(async () => {
      const userInfo = await getUserInfo(1);
      // 'userInfo' 在这里被 VSCode 自动推断为 User 类型
      // 如果你试图访问 userInfo.age (一个不存在的属性)，TS 会立刻报错
      user.value = userInfo;
    });
    ```

---

### 3. 其他实用功能

#### **a. 文件上传**

使用 `FormData` 对象，Axios 会自动设置 `Content-Type` 为 `multipart/form-data`。

```javascript
// HTML: <input type="file" id="fileInput" />
const fileInput = document.getElementById('fileInput');
const file = fileInput.files[0];

const formData = new FormData();
formData.append('file', file); // 'file' 是后端接收此文件的字段名
formData.append('userId', '123'); // 也可以附加其他数据

// 在 api/file.js 中
export function uploadFile(data) {
  return request.post('/upload', data, {
    // 覆盖默认的 JSON header
    headers: {
      'Content-Type': 'multipart/form-data',
    }
  });
}

// 调用
await uploadFile(formData);
```

#### **b. 进度监控**

Axios 提供了 `onUploadProgress` 和 `onDownloadProgress` 回调函数来监控进度。

```javascript
function uploadWithProgress(data, onProgress) {
  return request.post('/upload', data, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress: (progressEvent) => {
      const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
      onProgress(percentCompleted); // 调用外部传入的回调函数更新 UI
    },
  });
}

// 在组件中
await uploadWithProgress(formData, (percent) => {
  console.log(`上传进度: ${percent}%`);
  // 更新进度条 UI
});
```

#### **c. 并发请求 (`axios.all` 和 `axios.spread`)**

当你需要同时发起多个请求，并在所有请求都完成后再进行操作时，可以使用 `axios.all`。它本质上是 `Promise.all` 的一个语法糖。

```javascript
import axios from 'axios'; // 这里直接用全局 axios 举例，实践中用你的实例

async function getInitialData() {
  try {
    const [userResponse, permissionsResponse] = await axios.all([
      request.get('/user/1'),
      request.get('/user/1/permissions')
    ]);

    // 使用解构来处理结果
    const userData = userResponse; // 假设拦截器已处理 .data
    const permissionsData = permissionsResponse;

    console.log('User:', userData);
    console.log('Permissions:', permissionsData);

  } catch (error) {
    console.error('Failed to fetch initial data:', error);
  }
}

// 旧的 axios.spread 写法（现在不推荐，用数组解构更现代）
// axios.all([...]).then(axios.spread((user, perms) => { ... }));
```
**现代实践：** 直接使用 `Promise.all`，它更通用，功能完全一样。
```javascript
async function getInitialDataModern() {
  try {
    const [userData, permissionsData] = await Promise.all([
      request.get('/user/1'),
      request.get('/user/1/permissions')
    ]);

    console.log('User:', userData);
    console.log('Permissions:', permissionsData);
  } catch (error) {
    console.error('Failed to fetch initial data:', error);
  }
}
```

---

### **第四阶段与学习规划总结**

完成了从 `fetch` 到 Axios 的全部学习路径。回顾：

*   **阶段一：** 你学会了 Axios 的基本用法，并体会到它相比 `fetch` 在数据处理和错误处理上的便利性。
*   **阶段二：** 你掌握了 Axios 的灵魂——实例创建和拦截器，学会了如何将通用逻辑与业务代码解耦。
*   **阶段三：** 你学会了如何进行企业级的 API 层封装和模块化，让项目结构清晰、可维护。
*   **阶段四：** 你掌握了请求取消、TS 集成、文件处理等高级技巧，能从容应对各种复杂场景。

**下一步是什么？**
*   **持续实践：** 在你的下一个项目中，完整地实践这套架构。
*   **深入探索：** 当你遇到更奇特的需求时（例如自定义适配器、序列化参数等），查阅 Axios 的官方文档，你会发现它提供了非常多深入的配置项。
*   **保持更新：** 关注 Axios 在 GitHub 上的发布日志，了解新的特性和最佳实践的变化（比如从 `CancelToken` 到 `AbortController` 的迁移）。