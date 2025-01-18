在 Vue 中，React 的 `navigate("/")` 对应的是 Vue Router 提供的 `useRouter()` 钩子，它可以在 JavaScript 代码中进行页面跳转。

---

## **方法 1：使用 `useRouter()`（适用于 `<script setup>`）**

如果你使用的是 Vue 3 的 `setup` 语法，可以使用 `useRouter()` 来进行路由跳转：

```vue
<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToHome = () => {
  router.push('/'); // 跳转到首页
};
</script>

<template>
  <button @click="goToHome">跳转到首页</button>
</template>
```

> **解释**：
> 
> - `useRouter()` 获取 Vue Router 实例。
> - `router.push('/')` 实现页面跳转，相当于 `navigate("/")`。

---

## **方法 2：使用 `this.$router.push()`（适用于选项式 API）**

如果你使用的是 Vue 2 或 Vue 3 的**选项式 API**：

```vue
<script>
export default {
  methods: {
    goToHome() {
      this.$router.push('/'); // 跳转到首页
    }
  }
};
</script>

<template>
  <button @click="goToHome">跳转到首页</button>
</template>
```

> **解释**：
> 
> - `this.$router.push('/')` 实现路由跳转。
> - 适用于 Vue 2 和 Vue 3 选项式 API。

---

## **方法 3：在 Vue Router 的 `beforeEnter` 或 `onMounted` 里跳转**

如果需要在某个页面加载时**自动跳转**：

```vue
<script setup>
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();

onMounted(() => {
  router.push('/home'); // 页面加载时自动跳转
});
</script>
```

> **应用场景**：
> 
> - **重定向**：进入某个页面时自动跳转到 `/home`。

---

## **方法 4：`router.replace()`**

如果不希望用户回退（替换当前路由），可以使用 `replace()`：

```js
router.replace('/dashboard'); // 替换当前历史记录，无法回退
```

---

## **方法 5：带参数的动态跳转**

如果要**动态传递参数**：

```js
router.push({ path: '/user', query: { id: 123 } });
// 最终跳转到 /user?id=123
```

或者：

```js
router.push({ name: 'UserProfile', params: { userId: 123 } });
// 需要在 router.js 里定义 name
```

---

### **总结**

|React (`navigate()`)|Vue (`useRouter()`)|
|---|---|
|`navigate("/")`|`router.push("/")`|
|`navigate("/home")`|`router.push("/home")`|
|`navigate("/user?id=1")`|`router.push({ path: "/user", query: { id: 1 } })`|
|`navigate(-1)`|`router.go(-1)`|

如果你使用 Vue Router，就可以很方便地在 JavaScript 代码中进行类似 React `navigate()` 的跳转！ 🚀