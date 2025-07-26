axios 最核心、最强大的功能之一：**拦截器（Interceptors）**。

### 一、拦截器是什么？一个生动的比喻

想象一下你的每一次 API 请求和响应，都必须经过一个**“机场安检口”**。

*   **请求拦截器 (Request Interceptor)**：就是你出国前的 **“出境安检”**。在你的“包裹”（请求数据）发往“国外”（服务器）之前，安检员（拦截器）会打开它，检查一遍。他可以：
    *   **加点东西**：比如给你的护照（请求头）盖个章，贴上一个“签证”（`Authorization` Token）。
    *   **改变包裹形态**：对你的数据进行加密或格式化。
    *   **拦下不合格的包裹**：如果发现你没带护照，就直接取消本次行程（取消请求）。

*   **响应拦截器 (Response Interceptor)**：就是你回国后的 **“入境海关”**。在“国外”（服务器）的包裹送达到你手上之前，海关人员（拦截器）会先检查一遍。他可以：
    *   **拆开外包装**：服务器返回的数据可能包了好几层（如 `res.data.data`），他可以直接帮你拆开，只把最有用的核心物品（`data`）给你。
    *   **检查违禁品**：如果发现返回的状态是 `401`（未授权），他会立刻把你引导到“重新登录”页面，而不是把错误信息直接给你。
    *   **统一处理问题件**：对于所有服务器返回的错误（比如 500 Internal Server Error），他可以统一贴上“问题件”标签并进行上报，而不用你每次都自己处理。

**一句话总结**：拦截器提供了一个在 **发送请求前** 和 **收到响应后** 对数据进行全局、统一处理的机制。这是构建健壮、可维护的前端应用的关键。

---

### 二、如何使用拦截器？（代码实战）

拦截器分为两种，都是通过 `axios.interceptors` 对象来使用。在实际项目中，我们强烈推荐为 axios **创建一个实例** 来配置拦截器，这样可以避免污染全局的 axios 对象，方便管理。

#### 1. 请求拦截器 (`.request.use`)

它的作用是在请求被 `then` 或 `catch` 处理之前拦截。

```javascript
// request.js - 实际项目中，我们通常会创建一个这样的文件来封装 axios

import axios from 'axios';

// 1. 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // 统一设置基础路径
  timeout: 10000, // 请求超时时间
});

// 2. 添加请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    console.log('请求要出发了，这是我的配置:', config);

    // 经典应用：统一添加 token
    const token = localStorage.getItem('user-token');
    if (token) {
      // 'Bearer ' 是一种常见的认证方案，具体看后端要求
      config.headers['Authorization'] = 'Bearer ' + token;
    }

    // 必须返回 config，不然请求会中断
    return config; 
  },
  error => {
    // 对请求错误做些什么
    console.error('请求出错了:', error); 
    return Promise.reject(error); // 将错误继续向外抛出
  }
);
```

**核心逻辑**：
*   `use` 方法接收两个函数作为参数：`onFulfilled` (请求成功) 和 `onRejected` (请求错误)。
*   在 `onFulfilled` 函数中，你**必须** `return config` 对象，否则请求链会在此中断，请求无法被发送出去。
*   你可以在这里修改 `config` 的任何内容，比如 `headers`, `params`, `data` 等。

#### 2. 响应拦截器 (`.response.use`)

它的作用是在响应数据被 `then` 或 `catch` 处理之前拦截。

```javascript
// ...接上文 request.js

// 3. 添加响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    // HTTP 状态码在 2xx 范围内会进入这里

    console.log('收到响应了:', response);

    // 经典应用：数据简化，直接返回 response.data
    // 后端返回的数据结构通常是 { code: 200, message: 'success', data: { ... } }
    const res = response.data;

    // 根据自定义 code 判断，而不是 HTTP status
    if (res.code !== 200) {
      // 比如 code 是 401，代表未授权
      if (res.code === 401) {
         // 做一些如“跳转到登录页”之类的操作
         console.log('Token 过期或无效，请重新登录');
         // window.location.href = '/login';
      } else {
         // 其他业务错误，可以统一弹窗提示
         console.error('业务错误:', res.message);
         // alert(res.message);
      }
      
      // 抛出一个错误，这样请求的 catch 分支就能捕获到
      return Promise.reject(new Error(res.message || 'Error'));
    }

    // 如果 code 是 200，则只返回核心的 data 部分
    return res.data; 
  },
  error => {
    // 对响应错误做点什么
    // 超出 2xx 范围的 HTTP 状态码会进入这里
    
    console.error('HTTP 错误:', error.message);
    // 可以在这里做统一的错误提示，比如弹出一个 Toast
    // alert(`网络错误: ${error.message}`);
    
    return Promise.reject(error); // 将错误继续向外抛出
  }
);

// 4. 导出配置好的实例
export default service;
```

**核心逻辑**：
*   同样接收 `onFulfilled` 和 `onRejected` 两个函数。
*   `onFulfilled`：处理 HTTP 状态码为 2 xx 的情况。在这里，你可以：
    *   **简化数据层级**：直接 `return response.data`，这样业务代码里 `await api.getUser()` 拿到的直接就是数据，而不是整个 `response` 对象。
    *   **处理业务错误**：根据后端定义的业务状态码（如 `code: 50001`）进行统一处理。
*   `onRejected`：处理 HTTP 状态码非 2 xx 的情况（网络错误、404、500 等）。在这里可以做全局的错误上报或提示。

---

### 三、扩展与应用：一个完整的请求模块

将上面的代码整合，你的项目中就会有一个非常优雅的 `request.js` 文件。

**`src/api/request.js` (完整示例)**
```javascript
import axios from 'axios';
// 引入你的UI库的提示组件，例如 Element Plus
// import { ElMessage } from 'element-plus'; 

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // 使用环境变量，更灵活
  timeout: 10000,
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('user-token');
    if (token && config.headers) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== 200) {
      // ElMessage.error(res.message || 'Error'); // 统一错误提示
      if (res.code === 401) {
        // 处理 token 过期
        // showLoginModal();
      }
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return res.data; // 返回核心数据
    }
  },
  error => {
    console.error('Network Error:', error);
    // ElMessage.error(error.message); // 网络错误提示
    return Promise.reject(error);
  }
);

export default service;
```

**在业务代码中使用：**

```javascript
// src/api/user.js
import request from './request';

export function getUserInfo(id) {
  return request({
    url: `/user/${id}`,
    method: 'get',
  });
}

// 在 Vue 组件或 React 组件中使用
import { getUserInfo } from '@/api/user';

async function fetchUser() {
  try {
    const userInfo = await getUserInfo(1); // 直接拿到的是 res.data
    console.log(userInfo); 
  } catch (error) {
    // 这里捕获到的是拦截器中 reject 的错误
    console.error('获取用户信息失败:', error);
  }
}
```
看到好处了吗？业务代码变得极其干净，只关心“调用”和“使用数据”，所有认证、错误处理、数据转换的脏活累活都交给了拦截器。

---

### 四、要点与注意事项

1.  **必须 `return`**：在拦截器的 `onFulfilled` 函数中，无论是请求拦截器的 `config` 还是响应拦截器的 `response` / `data`，**都必须返回**。否则，请求/响应链会中断。
2.  **必须 `Promise.reject`**：在拦截器中处理完错误后，如果你希望调用方的 `.catch` 或 `try...catch` 能够捕获到这个错误，你**必须** `return Promise.reject(error)`。
3.  **实例 vs 全局**：始终使用 `axios.create()` 创建实例来配置拦截器。这能有效隔离不同业务模块的请求配置（比如有的需要 token，有的不需要）。
4.  **执行顺序**：
    *   **请求拦截器**：后定义的先执行（像穿衣服，后穿的在外层）。
    *   **响应拦截器**：先定义的先执行（像脱衣服，先脱外层的）。
5.  **异步拦截器**：拦截器函数可以是 `async` 函数。例如，你可能需要异步获取一个动态的 token。
    ```javascript
    service.interceptors.request.use(async (config) => {
      const dynamicToken = await getDynamicToken(); // 异步操作
      config.headers['Authorization'] = 'Bearer ' + dynamicToken;
      return config;
    });
    ```
6.  **移除拦截器**：虽然不常用，但你可以通过 `eject` 方法移除拦截器，这在一些动态场景（如单页应用中组件卸载时）可能有用。
    ```javascript
    const myInterceptor = axios.interceptors.request.use(function () {/*...*/});
    axios.interceptors.request.eject(myInterceptor);
    ```


## 全局拦截器
**全局拦截器**。这是一个非常重要但又需要谨慎使用的概念。

### 一、什么是全局拦截器？

全局拦截器，顾名思义，是直接在 `axios` 这个根对象上设置的拦截器。一旦设置，它将对你项目中 **所有通过 `axios` 发起的请求** 生效，无论这个请求是通过 `axios(...)` 直接调用，还是通过 `axios.create()` 创建的实例发出的。

**代码上的区别非常直观：**

*   **实例拦截器 (我们之前讨论的)**：
    ```javascript
    const service = axios.create();
    service.interceptors.request.use(/* ... */); // 只对 service 实例生效
    ```

*   **全局拦截器 (我们现在讨论的)**：
    ```javascript
    import axios from 'axios';
    axios.interceptors.request.use(/* ... */); // 对所有 axios 请求生效
    ```

### 二、全局拦截器 vs. 实例拦截器：终极对决

这自然引出了一个核心问题：我应该使用全局拦截器，还是像我们之前讨论的那样，使用 `axios.create()` 创建的实例拦截器？

**答案是：在 99% 的现代前端项目中，你应该优先且总是使用“实例拦截器”。**

为了让你彻底理解，我们用一个表格来清晰地对比它们：

| 对比维度 | 全局拦截器 (`axios.interceptors`) | 实例拦截器 (`instance.interceptors`) |
| :--- | :--- | :--- |
| **作用范围** | **应用级**：影响项目里所有 axios 请求，包括你可能引入的第三方库中的 axios 请求。 | **实例级**：只影响通过这个特定实例发出的请求。 |
| **核心优势** | 真正意义上的“一处配置，处处生效”，对于极少数绝对通用的逻辑（如全局 loading）很方便。 | ✅ **隔离性与模块化**：不同业务模块可以有不同的 `baseURL`、`timeout`、`headers` 和拦截逻辑，互不干扰。 |
| **主要风险** | ⚠️ **“污染”风险**：可能会意外地影响到你不想影响的请求，尤其是第三方库。这是它最大的弊端。 | **无风险**：每个实例都是一个沙箱，行为可预测，没有副作用。 |
| **典型用例** | 1. 启动/关闭全局 Loading 动画。<br>2. 极简项目的快速配置（不推荐）。 | 🚀 **企业级标准**：<br>1. 添加认证 `Token`。<br>2. 统一处理业务错误码。<br>3. 统一处理 `baseURL`。<br>4. 针对不同后端服务创建不同实例。 |
| **推荐度** | ⭐⭐（谨慎使用） | ⭐⭐⭐⭐⭐（**最佳实践**） |

#### “污染”风险的生动例子

想象一下，你的项目中用了一个第三方的数据可视化库（比如 `some-chart-lib`），这个库的内部也使用了 `axios` 来获取地图边界数据。

现在，你在全局拦截器里添加了逻辑：“所有请求都必须带上 `Authorization` 请求头”。

```javascript
// main.js - 你在项目入口设置了全局拦截器
import axios from 'axios';
axios.interceptors.request.use(config => {
  config.headers['Authorization'] = 'Bearer ' + myToken;
  return config;
});
```

**结果会发生什么？**
`some-chart-lib` 内部发起的请求（比如 `axios.get('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')`）也会被你的全局拦截器捕捉到，并被强行加上 `Authorization` 请求头。

阿里的地图数据接口服务器并不需要这个请求头，甚至可能因为这个非预期的 `header` 而拒绝请求，导致你的图表库无法正常工作。**这就是“全局污染”**。

而如果你使用实例拦截器，这个问题就迎刃而解了：

```javascript
// src/api/request.js
const service = axios.create({ baseURL: '/my-api' });
service.interceptors.request.use(/* 添加 token 的逻辑 */); // 只会影响你的业务API

// some-chart-lib 内部的 axios 不受任何影响
```

---

### 三、全局与实例拦截器的执行顺序

一个有趣的问题是：如果我同时设置了全局拦截器和实例拦截器，它们会如何执行？

**记住这个顺序，就像一个三明治：全局拦截器是面包，实例拦截器是夹心。**

1.  **请求阶段（发出请求）**
    *   `全局请求拦截器` (先执行，像最外层的面包)
    *   `实例请求拦截器` (后执行，像里面的夹心)
    *   **[请求正式发出]**

2.  **响应阶段（收到响应）**
    *   **[收到服务器响应]**
    *   `实例响应拦截器` (先执行，先处理夹心)
    *   `全局响应拦截器` (后执行，最后处理面包)

这个顺序设计得非常合理：**全局拦截器总是包在实例拦截器的外面**。

---

### 四、全局拦截器的极少数合理用例

尽管我们强烈推荐实例拦截器，但在某些极端通用、且与业务逻辑完全无关的场景下，全局拦截器可以派上用场。

**用例：实现全局的 `loading` 状态**

你可以用全局拦截器来轻松实现“只要有请求在飞，就显示一个全局的 loading 动画”。

```javascript
// loading.js
let requestCount = 0;

function showLoading() {
  if (requestCount === 0) {
    // 调用你的 UI 库的 showLoading() 方法
  }
  requestCount++;
}

function hideLoading() {
  requestCount--;
  if (requestCount === 0) {
    // 调用你的 UI 库的 hideLoading() 方法
  }
}

// main.js - 在项目入口配置
import axios from 'axios';
import { showLoading, hideLoading } from './loading';

axios.interceptors.request.use(config => {
  showLoading();
  return config;
}, error => {
  hideLoading(); // 请求失败也要隐藏 loading
  return Promise.reject(error);
});

axios.interceptors.response.use(response => {
  hideLoading();
  return response;
}, error => {
  hideLoading(); // 响应错误也要隐藏 loading
  return Promise.reject(error);
});
```
这个用例是合理的，因为它不修改请求的认证或业务数据，只是一个纯粹的 UI 行为，对所有请求一视同仁通常是可接受的。

---

### 五、要点与注意事项总结

1.  **首选实例拦截器**：这是现代前端工程化的基石，能保证代码的模块化、可维护性和无副作用。
2.  **警惕全局污染**：使用全局拦截器前，请三思：“这个逻辑是否真的需要应用于项目中的**每一个** `axios` 请求，包括我无法控制的第三方库？”
3.  **理解执行顺序**：当你需要混合使用时，记住“全局在外，实例在内”的三明治模型。
4.  **合理用于纯粹的副作用**：全局 `loading`、全局请求日志记录等，是全局拦截器为数不多的“舒适区”。

总而言之，将实例拦截器作为你的默认武器，而将全局拦截器看作是只有在特殊情况下才会动用的“大招”，并且使用时要格外小心。