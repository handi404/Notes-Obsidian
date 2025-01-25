# **📌 Nuxt 数据获取（Fetching Data）详解**

在 Nuxt 中，获取数据（Fetching Data）是开发应用的重要部分，尤其是**SSR（服务器端渲染）**和**CSR（客户端渲染）**模式下的数据处理。Nuxt 提供了一些强大的 API，如 `useFetch`、`useAsyncData`、`useLazyAsyncData` 等来管理数据请求。

---

## **🚀 1. Nuxt 数据获取 API 对比**

|方法|运行时|是否缓存|适用场景|
|---|---|---|---|
|`useFetch`|**客户端 & 服务器端**|✅|获取 API 数据，自动缓存|
|`useAsyncData`|**客户端 & 服务器端**|✅|在 `setup` 中获取数据|
|`useLazyAsyncData`|**客户端 & 服务器端**|✅|仅在需要时加载，适合懒加载|
|`onMounted`|**仅客户端**|❌|仅客户端获取数据，不支持 SSR|
|`useState`|**客户端 & 服务器端**|✅|共享全局状态，缓存数据|

✅ **推荐优先使用 `useFetch` 或 `useAsyncData`，因为它们支持 Nuxt 的自动缓存优化！**

---

# **📌 2. `useFetch`（最推荐）**

📌 `useFetch` 是 Nuxt 推荐的 API，**支持 SSR 和客户端渲染**，还能自动缓存数据。

### **✅ 基本用法**

```vue
<script setup>
const { data, pending, error } = useFetch('https://jsonplaceholder.typicode.com/posts/1');
</script>

<template>
  <div v-if="pending">⏳ 加载中...</div>
  <div v-else-if="error">❌ 发生错误：{{ error }}</div>
  <div v-else>
    <h1>{{ data.title }}</h1>
    <p>{{ data.body }}</p>
  </div>
</template>
```

📌 **特点：**

- `data` 👉 响应式变量，存放 API 数据
- `pending` 👉 是否加载中
- `error` 👉 是否出错

---

## **🚀 3. `useAsyncData`（用于 `setup()`）**

📌 `useAsyncData` 适用于 `setup` 语法，可以手动请求 API，并支持缓存数据。

### **✅ 基本用法**

```vue
<script setup>
const { data, pending, error } = useAsyncData('posts', () =>
  $fetch('https://jsonplaceholder.typicode.com/posts/1')
);
</script>

<template>
  <div v-if="pending">⏳ 加载中...</div>
  <div v-else-if="error">❌ 发生错误：{{ error }}</div>
  <div v-else>
    <h1>{{ data.title }}</h1>
    <p>{{ data.body }}</p>
  </div>
</template>
```

📌 **特点：**

- `useAsyncData` **允许你缓存数据**
- 使用 `$fetch` 代替 `fetch`，更符合 Nuxt 生态

---

## **🚀 4. `useLazyAsyncData`（懒加载数据）**

📌 `useLazyAsyncData` 只有在组件渲染后才会加载数据，适合**懒加载**的情况。

### **✅ 基本用法**

```vue
<script setup>
const { data, pending, error } = useLazyAsyncData('posts', () =>
  $fetch('https://jsonplaceholder.typicode.com/posts/1')
);
</script>

<template>
  <button @click="data.refresh()">🔄 重新加载</button>
  <div v-if="pending">⏳ 加载中...</div>
  <div v-else-if="error">❌ 发生错误：{{ error }}</div>
  <div v-else>
    <h1>{{ data.title }}</h1>
    <p>{{ data.body }}</p>
  </div>
</template>
```

📌 **特点：**

- 只有在需要时才加载数据（**不会自动在 SSR 阶段执行**）
- 适合**用户交互触发的数据请求**

---

## **🚀 5. 客户端专用 `onMounted` 获取数据**

📌 如果你只想在客户端加载数据（不支持 SSR），可以用 `onMounted`：

```vue
<script setup>
import { ref, onMounted } from 'vue';

const data = ref(null);
const error = ref(null);

onMounted(async () => {
  try {
    const res = await fetch('https://jsonplaceholder.typicode.com/posts/1');
    data.value = await res.json();
  } catch (err) {
    error.value = err;
  }
});
</script>

<template>
  <div v-if="!data">⏳ 加载中...</div>
  <div v-else-if="error">❌ 发生错误：{{ error }}</div>
  <div v-else>
    <h1>{{ data.title }}</h1>
    <p>{{ data.body }}</p>
  </div>
</template>
```

📌 **特点：**

- 仅在客户端运行（不支持 SSR）
- 适用于**只在浏览器端获取数据**（如 localStorage）

---

# **📌 6. `useState`（全局状态管理）**

📌 `useState` 可用于**跨组件共享数据**，适合存储全局数据，如用户信息。

### **✅ 在 `store.js` 中创建全局状态**

```vue
// composables/useUser.js
export const useUser = () => useState('user', () => null);
```

### **✅ 在组件中使用**

```vue
<script setup>
const user = useUser();
user.value = { name: '张三', age: 25 };
</script>

<template>
  <p>👤 用户：{{ user.name }}（{{ user.age }}岁）</p>
</template>
```

📌 **特点：**

- `useState` 会在**服务器端渲染**后持久化
- 适合存储**全局状态**（比如登录用户信息）

---

# **📌 7. 数据重新获取（刷新数据）**

📌 你可以在 `useFetch` 或 `useAsyncData` 里手动刷新数据：

```vue
<script setup>
const { data, refresh } = useFetch('https://jsonplaceholder.typicode.com/posts/1');
</script>

<template>
  <button @click="refresh()">🔄 重新加载数据</button>
  <p>{{ data }}</p>
</template>
```

✅ **`refresh()`** 允许你手动重新获取数据！🚀

---

# **🎯 8. 总结**

|方法|适用场景|SSR 支持|是否缓存|
|---|---|---|---|
|**`useFetch`**|获取 API 数据（推荐）|✅|✅|
|**`useAsyncData`**|`setup` 获取数据|✅|✅|
|**`useLazyAsyncData`**|懒加载数据|✅|✅|
|**`onMounted`**|仅客户端请求数据|❌|❌|
|**`useState`**|共享全局状态|✅|✅|

📌 **💡 选择合适的方法来优化你的数据请求，Nuxt 会自动处理缓存和 SEO！**