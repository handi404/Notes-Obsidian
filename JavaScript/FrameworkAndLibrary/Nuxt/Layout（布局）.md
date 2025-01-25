# **📌 Nuxt Layout（布局）详解 & 自定义布局**

在 Nuxt 中，**布局（Layout）** 允许你定义 **全局结构**，例如 **头部（Header）、侧边栏（Sidebar）、底部（Footer）**，然后不同的页面可以继承这些布局，而不需要重复代码。

---

# **🚀 1. 什么是 Layout？为什么要用 Layout？**

### **📌 为什么要用 Layout？**

✅ **减少代码重复** ：避免在每个页面都写相同的 `Header`、`Footer`。  
✅ **管理不同页面结构** ：可以为 **登录页面**、**后台管理**、**移动端页面** 定义不同的布局。  
✅ **提升代码可维护性** ：一处修改，所有使用该布局的页面都会更新。

---

# **📌 2. 默认布局（`default.vue`）**

Nuxt 会自动使用 **`layouts/default.vue`** 作为所有页面的默认布局。

### **📌 目录结构**

```
layouts/
│── default.vue   # 默认布局
pages/
│── index.vue     # 首页
│── about.vue     # 关于我们
```

### **📌 `layouts/default.vue`**

```vue
<template>
  <div>
    <header>🚀 这是全局导航栏</header>
    <slot /> <!-- 这里会渲染页面内容 -->
    <footer>📌 这是全局底部</footer>
  </div>
</template>
```

📌 `slot` 代表页面内容，例如 `pages/index.vue` 或 `pages/about.vue`。

---

## **🚀 3. 页面如何使用 Layout？**

### **📌 页面 `pages/index.vue`**

```vue
<template>
  <div>
    <h1>首页内容</h1>
  </div>
</template>
```

✅ **默认情况下，`index.vue` 会自动使用 `default.vue` 作为布局**，最终结构如下：

```html
<header>🚀 这是全局导航栏</header>
<h1>首页内容</h1>
<footer>📌 这是全局底部</footer>
```

---

# **📌 4. 自定义布局**

有时，我们需要不同的页面使用不同的布局，比如：

- **`default.vue`** 👉 适用于普通页面
- **`admin.vue`** 👉 适用于后台管理
- **`auth.vue`** 👉 适用于登录页面

### **📌 目录结构**

```
layouts/
│── default.vue   # 默认布局
│── admin.vue     # 后台管理布局
│── auth.vue      # 登录页面布局
pages/
│── index.vue     # 首页（使用 default）
│── admin.vue     # 后台（使用 admin）
│── login.vue     # 登录页（使用 auth）
```

---

## **🚀 5. 创建 `admin.vue` 布局**

```vue
<template>
  <div>
    <header>🔧 管理员后台</header>
    <div class="admin-container">
      <aside>📌 侧边栏</aside>
      <main>
        <slot />
      </main>
    </div>
  </div>
</template>

<style>
.admin-container {
  display: flex;
}
aside {
  width: 200px;
  background: lightgray;
}
main {
  flex-grow: 1;
  padding: 20px;
}
</style>
```

---

## **🚀 6. 让页面使用特定的 Layout**

### **📌 `pages/admin.vue` 使用 `admin` 布局**

```vue
<script setup>
definePageMeta({
  layout: 'admin'  // 指定使用 admin 布局
});
</script>

<template>
  <h1>后台管理系统</h1>
</template>
```

📌 **效果：**

```
🔧 管理员后台
📌 侧边栏  |  后台管理系统
```

---

# **📌 7. 动态切换布局**

有时，我们想在 **同一个页面** 动态切换布局，比如：

- **深色模式**（Dark Mode）
- **桌面 & 移动端不同布局**

### **📌 动态切换布局**

```vue
<script setup>
import { ref } from 'vue';

const layout = ref('default'); // 默认布局

const toggleLayout = () => {
  layout.value = layout.value === 'default' ? 'admin' : 'default';
};
</script>

<template>
  <div>
    <button @click="toggleLayout">切换布局</button>
    <NuxtLayout :name="layout">
      <p>这是页面内容</p>
    </NuxtLayout>
  </div>
</template>
```

📌 **`<NuxtLayout>` 允许你在页面内切换不同布局！**

---

# **📌 8. 全局修改 Layout**

如果你想在 **所有页面** 统一更改 Layout，可以在 `app.vue` 里使用：

```vue
<script setup>
definePageMeta({
  layout: 'admin' // 全局默认使用 admin 布局
});
</script>
```

📌 这样所有页面都会使用 `admin` 布局。

---

# **📌 9. Layout 进阶功能**

### **✅ 9.1 传递 Props 给 Layout**

有时，你希望 `layout` 里的内容根据不同页面动态变化，可以用 `props` 传值：

```vue
<!-- layouts/admin.vue -->
<template>
  <div>
    <header>🔧 {{ title }}</header>
    <slot />
  </div>
</template>

<script setup>
defineProps(['title']);
</script>
```

### **📌 `pages/admin.vue` 传值**

```vue
<script setup>
definePageMeta({
  layout: 'admin'
});
</script>

<template>
  <NuxtLayout name="admin" title="后台管理中心">
    <h1>后台首页</h1>
  </NuxtLayout>
</template>
```

📌 **这样 `title` 会动态改变！**

---

# **🎯 总结**

|**功能**|**代码示例**|
|---|---|
|**默认布局**|`layouts/default.vue`|
|**自定义布局**|`layouts/admin.vue`|
|**指定页面布局**|`definePageMeta({ layout: 'admin' })`|
|**动态切换布局**|`<NuxtLayout :name="layoutName">`|
|**全局修改 Layout**|`definePageMeta({ layout: 'admin' })` in `app.vue`|
|**传递 Props**|`<NuxtLayout name="admin" title="后台管理">`|

📌 **💡 Layout 是 Nuxt 中强大的页面结构管理工具，帮助你减少重复代码，提高开发效率！**