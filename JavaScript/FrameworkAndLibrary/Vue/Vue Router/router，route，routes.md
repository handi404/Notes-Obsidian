在 Vue Router 中，`router`、`route` 和 `routes` 具有不同的作用和使用场景。下面是它们的详细解析：

---

## **1. `routes`（路由配置表）**

- **定义**：`routes` 是一个 **数组**，包含应用程序中的所有路由配置，每个对象代表一个路由规则。
- **作用**：指定 URL 路径与组件的对应关系。
- **位置**：通常在 `router.js` 或 `router/index.js` 中定义。

### **示例**

```js
import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import About from '../components/About.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/about', component: About }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
```

---

## **2. `router`（路由实例对象）**

- **定义**：`router` 是 `createRouter()` 创建的 **路由实例**，用于管理路由的整体逻辑。
- **作用**：
    - 控制**全局的路由跳转**
    - 监听和修改当前的**路由状态**
    - 提供**路由导航守卫**
    - 通过 `app.use(router)` 挂载到 Vue 实例

### **示例**

```js
import router from './router';

// 在 `main.js` 中挂载
const app = createApp(App);
app.use(router);
app.mount('#app');
```

### **常用 API**

|方法|作用|
|---|---|
|`router.push('/about')`|跳转到 `/about`|
|`router.replace('/home')`|替换当前 URL，不保留历史记录|
|`router.back()`|返回上一页|
|`router.forward()`|前进到下一页|
|`router.beforeEach((to, from, next) => { ... })`|路由守卫（拦截导航）|

---

## **3. `route`（当前路由对象）**

- **定义**：`route` 是 **当前激活的路由信息对象**，包含路径、参数、查询参数等。
- **作用**：获取**当前页面的路由信息**，如 `path`、`params`、`query` 等。
- **位置**：可以通过 `useRoute()` 访问（Vue 3），或在 `this.$route` 中获取（Vue 2）。

### **示例**

```vue
<script setup>
import { useRoute } from 'vue-router';

const route = useRoute();
console.log(route.path);  // 当前路径
console.log(route.params);  // 路由参数
console.log(route.query);  // 查询参数
</script>
```

### **常见属性**

|属性|作用|示例|
|---|---|---|
|`route.path`|当前 URL 路径|`/about`|
|`route.params`|动态路由参数|`{ id: '123' }`|
|`route.query`|URL 查询参数|`{ search: 'vue' }`|
|`route.fullPath`|完整的 URL|`/about?search=vue`|
|`route.name`|命名路由的名称|`"home"`|

---

## **4. 关系总结**

- **`routes`**：是所有路由的配置表，定义 URL 和组件的映射关系。
- **`router`**：是路由实例对象，控制全局的路由跳转和导航守卫。
- **`route`**：是当前激活的路由信息，存储了路径、参数等信息。

### **对比表**

|名称|作用|访问方式|
|---|---|---|
|`routes`|定义路由规则|`const routes = [ { path: '/home', component: Home } ]`|
|`router`|控制路由跳转|`import router from './router'` 或 `const router = useRouter()`|
|`route`|获取当前路由信息|`const route = useRoute()` 或 `this.$route`|

---

## **5. 示例：结合 `router` 和 `route`**

### **完整示例**

```vue
<script setup>
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

function goToHome() {
  router.push('/home');  // 使用 router 跳转
}
</script>

<template>
  <div>
    <h2>当前路径: {{ route.path }}</h2>
    <p>当前参数: {{ route.params }}</p>
    <p>当前查询参数: {{ route.query }}</p>
    <button @click="goToHome">跳转到 Home</button>
  </div>
</template>
```

---

## **总结**

|概念|定义|作用|
|---|---|---|
|`routes`|路由配置数组|定义路径与组件的映射|
|`router`|路由实例对象|控制跳转、守卫、历史管理|
|`route`|当前激活的路由对象|获取路径、参数、查询信息|

它们在 Vue Router 中各司其职，共同完成路由管理的功能。 🚀