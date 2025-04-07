深入了解 Nuxt 3 的一个非常强大的特性：**服务端 API 路由 (Server API Routes)**。

简单来说，这允许你**在你的 Nuxt 项目内部直接编写后端 API 接口**，而无需创建和维护一个完全独立的后端服务（对于很多场景而言）。

**核心理念：全栈在同一个地方**

Nuxt 3 借助其强大的 **Nitro** 服务器引擎，让你可以在项目中的特定目录下编写文件，这些文件会自动转换成可供你的前端或其他服务调用的 API 端点。

**运作方式：`server/api/` 目录约定**

1.  **创建目录**：在你的 Nuxt 项目根目录下，创建一个名为 `server` 的文件夹，然后在 `server` 文件夹内再创建一个 `api` 文件夹。
    ```
    your-nuxt-project/
    ├── server/
    │   └── api/      <-- API 路由文件放在这里
    │   └── middleware/ (可选，用于服务器中间件)
    │   └── plugins/    (可选，用于服务器插件)
    │   └── utils/      (可选，用于服务器工具函数)
    ├── pages/
    ├── components/
    └── nuxt.config.ts
    ```
2.  **创建路由文件 (`.js` 或 `.ts`)**：在 `server/api/` 目录下创建 JavaScript 或 TypeScript 文件。**文件名和目录结构直接决定了 API 的 URL 路径**。
3.  **编写处理器 (Handler)**：每个文件需要导出一个由 `defineEventHandler()` 函数包装的**事件处理器 (event handler)**。这个处理器负责接收请求、处理逻辑并返回响应。

**基本示例：创建一个简单的 `GET` 接口**

*   **文件**：`server/api/hello.ts`
*   **对应 URL**：`GET /api/hello`

```typescript
// server/api/hello.ts
export default defineEventHandler((event) => {
  // event 对象包含了请求的所有信息 (headers, method, url, context 等)
  // 你可以在这里执行任何服务端逻辑，如数据库查询、调用外部 API 等

  // 直接返回对象或数组，Nitro 会自动处理为 JSON 响应
  return {
    message: '你好，来自 Nuxt 服务端 API！',
    timestamp: new Date().toISOString()
  };
});
```

**如何从前端调用这个 API？**

在你的页面或组件中，使用 Nuxt 提供的 `$fetch` 或 `useFetch` / `useAsyncData` 即可，使用相对路径：

```vue
<!-- pages/index.vue -->
<script setup lang="ts">
// 使用 useFetch 在页面加载时获取数据 (SSR 友好)
const { data, pending, error } = await useFetch('/api/hello');

// 或者在事件处理器中使用 $fetch
async function fetchGreeting() {
  try {
    const result = await $fetch('/api/hello');
    console.log(result.message);
  } catch (e) {
    console.error("获取问候失败:", e);
  }
}
</script>
<template>
  <div>
    <div v-if="pending">加载中...</div>
    <div v-else-if="error">错误: {{ error.message }}</div>
    <p v-else>{{ data?.message }} ({{ data?.timestamp }})</p>
    <button @click="fetchGreeting">手动获取问候</button>
  </div>
</template>
```

**处理不同的 HTTP 方法 (GET, POST, PUT, DELETE 等)**

*   **方法一：在处理器内部检查**：`defineEventHandler` 默认会处理所有 HTTP 方法。你可以在处理器内部检查 `event.method` (或使用 H3 提供的 `getMethod(event)` 辅助函数) 来执行不同的逻辑。

    ```typescript
    // server/api/items.ts
    import { getMethod, readBody } from 'h3';

    let items = ['苹果', '香蕉']; // 示例数据 (实际应用会用数据库)

    export default defineEventHandler(async (event) => {
      const method = getMethod(event);

      if (method === 'GET') {
        return items;
      } else if (method === 'POST') {
        const body = await readBody(event); // 读取请求体 (异步)
        if (body && body.item) {
          items.push(body.item);
          setResponseStatus(event, 201); // 设置响应状态码为 201 Created
          return { success: true, items };
        } else {
          throw createError({ // 返回错误
            statusCode: 400,
            statusMessage: 'Bad Request',
            message: '请求体需要包含 "item" 字段',
          });
        }
      } else {
         throw createError({ statusCode: 405, message: 'Method Not Allowed' });
      }
    });
    ```

*   **方法二：使用方法后缀命名文件 (推荐用于区分)**：你可以为同一个路由创建特定于 HTTP 方法的文件。
    *   `server/api/items.get.ts` -> 只处理 `GET /api/items`
    *   `server/api/items.post.ts` -> 只处理 `POST /api/items`
    *   `server/api/items.put.ts` -> 只处理 `PUT /api/items`
    *   ...等等

    这样可以让你的代码更清晰地按方法组织。

    ```typescript
    // server/api/items.get.ts
    export default defineEventHandler((event) => {
      // ... 只处理 GET 的逻辑 ...
      return ['苹果', '香蕉'];
    });
    ```

    ```typescript
    // server/api/items.post.ts
    export default defineEventHandler(async (event) => {
      const body = await readBody(event);
      // ... 只处理 POST 的逻辑 ...
      console.log("收到新物品:", body.item);
      setResponseStatus(event, 201);
      return { success: true, item: body.item };
    });
    ```

**动态路由参数**

与页面路由类似，你可以在文件名或目录名中使用方括号 `[]` 来创建动态 API 路由。

*   **文件**：`server/api/users/[id].ts`
*   **对应 URL**：`/api/users/123`, `/api/users/abc`

```typescript
// server/api/users/[id].ts
export default defineEventHandler((event) => {
  // Nuxt 3.8+ 推荐使用 getRouterParams
  const userId = getRouterParams(event).id;
  // 或者老版本/兼容方式：
  // const userId = event.context.params?.id;

  if (!userId) {
       throw createError({ statusCode: 400, message: '缺少用户 ID' });
  }

  console.log(`正在获取用户 ${userId} 的信息...`);

  // 假设这里根据 userId 查询数据库
  if (userId === '1') {
    return { id: userId, name: 'Alice', email: 'alice@example.com' };
  } else if (userId === '2') {
    return { id: userId, name: 'Bob', email: 'bob@example.com' };
  } else {
    throw createError({
      statusCode: 404,
      statusMessage: 'Not Found',
      message: `用户 ${userId} 未找到`,
    });
  }
});
```

**捕获所有 (Catch-all) 路由**

使用带有三个点的方括号 `[...]`。

*   **文件**：`server/api/files/[...path].ts`
*   **对应 URL**：`/api/files/a/b/c.txt`, `/api/files/image.jpg`

```typescript
// server/api/files/[...path].ts
export default defineEventHandler((event) => {
  const pathParams = getRouterParams(event).path; // 这是个数组或单个字符串，取决于框架解析
  const fullPath = Array.isArray(pathParams) ? pathParams.join('/') : pathParams;

  console.log(`请求的文件路径: ${fullPath}`);
  // ... 根据 fullPath 处理文件逻辑 ...
  return { requestedPath: fullPath };
});
```

**获取请求信息 (Query, Body, Headers)**

Nitro (基于 H 3) 提供了很多辅助函数来方便地获取请求信息：

```typescript
import { getQuery, readBody, getHeaders, getCookie } from 'h3';

export default defineEventHandler(async (event) => {
  // 获取查询参数 (?name=Bob&age=30) => { name: 'Bob', age: '30' }
  const query = getQuery(event);

  // 读取请求体 (通常用于 POST, PUT, PATCH)
  // 注意：readBody 是异步的！
  let body = null;
  if (['POST', 'PUT', 'PATCH'].includes(getMethod(event))) {
      try {
        body = await readBody(event); // 自动解析 JSON, form-data 等
      } catch (e) {
         console.error("读取请求体失败:", e);
         // 可以选择返回错误或处理空/无效 body
      }
  }


  // 获取所有请求头
  const headers = getHeaders(event);

  // 获取单个请求头 (不区分大小写)
  const userAgent = getHeader(event, 'user-agent');
  const contentType = getHeader(event, 'content-type');

  // 获取 cookie
  const myCookieValue = getCookie(event, 'my_cookie_name');

  return {
    query,
    body,
    userAgent,
    contentType,
    allHeaders: headers, // 注意：在生产中可能不应暴露所有 headers
    myCookieValue,
  };
});
```

**返回响应 (设置状态码, Headers)**

```typescript
import { setResponseStatus, setResponseHeader, sendRedirect } from 'h3';

export default defineEventHandler((event) => {
  // 1. 设置状态码 (默认为 200 OK)
  setResponseStatus(event, 201); // 设置为 201 Created

  // 2. 设置响应头
  setResponseHeader(event, 'Content-Type', 'text/plain');
  setResponseHeader(event, 'Cache-Control', 'no-cache');

  // 3. 发送重定向 (会结束请求处理)
  // await sendRedirect(event, '/new-location', 302); // 302 临时重定向

  // 4. 返回普通文本
  return '操作成功！';

  // 5. 直接返回数据（自动 JSON 化并设置 Content-Type: application/json）
  // return { success: true };
});
```

**错误处理**

使用 `createError` 抛出错误。`$fetch` 或 `useFetch` 在客户端会自动识别这些错误。

```typescript
export default defineEventHandler((event) => {
  const shouldFail = Math.random() > 0.5;

  if (shouldFail) {
    throw createError({
      statusCode: 500, // HTTP 状态码
      statusMessage: 'Internal Server Error', // 状态消息
      message: '糟糕，服务器内部发生了随机错误！', // 具体错误信息
      data: { retryable: false } // 可选的附加数据
    });
  }

  return { message: '这次很幸运，没有出错！' };
});
```

**优势总结：**

*   **开发便捷**：前后端代码库统一，共享配置和类型成为可能。
*   **快速原型**：无需搭建独立后端即可快速实现 API。
*   **部署简单**：Nuxt 构建 (`nuxi build`) 会将 API 和前端一起打包到 `.output` 目录，可以作为一个整体部署到 Node 服务器、Serverless 平台（Vercel, Netlify, Cloudflare Workers 等）或静态托管（如果 API 不需要动态计算）。
*   **类型安全**：如果使用 TypeScript，前后端可以共享接口定义。
*   **利用 Nitro 能力**：高性能、跨平台部署、代码分割等。

**何时不适用？**

*   **非常复杂的后端逻辑**：如果后端业务极其复杂，需要大量微服务、队列、复杂状态管理等，独立的后端框架可能更合适。
*   **团队分离**：如果前后端由完全不同的团队开发和部署，分离的代码库可能更清晰。

总而言之，Nuxt 的服务端 API 路由是其全栈能力的重要体现，极大地简化了许多 Web 应用的开发流程。