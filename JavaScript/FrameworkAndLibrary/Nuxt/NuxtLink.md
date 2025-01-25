# **📌 NuxtLink 详解（与常规 `<a>` 标签的区别）**

在 Nuxt 中，推荐使用 **`<NuxtLink>`** 代替传统的 `<a>` 标签进行**内部页面跳转**。`<NuxtLink>` 是 Nuxt 封装的 **客户端路由跳转组件**，它的性能优于 `<a>`，并且支持**预加载、动态路由、导航守卫等高级功能**。

---

## **🚀 1. 基本用法**

```vue
<template>
  <div>
    <NuxtLink to="/about">关于我们</NuxtLink>
  </div>
</template>
```

📌 作用：点击时跳转到 `/about` 页面（无需刷新页面）。

🔹 **等价于：**

```vue
<a href="/about">关于我们</a>
```

但是，**NuxtLink 有更好的性能**，不会重新加载整个页面！

---

## **📌 2. NuxtLink vs `<a>` 的区别**

||`<a>` (普通链接)|`<NuxtLink>` (Nuxt 内部导航)|
|---|---|---|
|**页面刷新**|**是**（重新加载整个页面）|**否**（仅更新 Vue 组件）|
|**性能优化**|无|**预加载、懒加载、缓存**|
|**动态路由**|手动拼接|**自动解析**|
|**SEO 友好**|依赖 SSR|**支持 Nuxt 预渲染**|
|**状态保留**|页面会重置|**Vue 状态不丢失**|
|**额外功能**|仅提供跳转|**支持 `activeClass`、`exact`、`prefetch`**|

📌 **结论**：

- **Nuxt 内部页面跳转** → **用 `<NuxtLink>`** ✅
- **外部网站跳转（如 `https://google.com`）** → **用 `<a>`** ✅

---

## **📌 3. `<NuxtLink>` 的高级用法**

### **🔹 3.1 `to` 路径**

`to` 属性指定跳转的路径：

```vue
<NuxtLink to="/contact">联系我们</NuxtLink>
```

动态绑定：

```vue
<NuxtLink :to="`/blog/${id}`">查看博客</NuxtLink>
```

---

### **🔹 3.2 `activeClass`（选中时的样式）**

默认情况下，`NuxtLink` 在匹配当前路径时会添加 `router-link-active` 类。你可以自定义选中时的样式：

```vue
<NuxtLink to="/about" active-class="active">关于我们</NuxtLink>
```

```css
.active {
  font-weight: bold;
  color: red;
}
```

**🔹 访问 `/about` 时，这个链接会变成红色并加粗！**

---

### **🔹 3.3 `exact`（精准匹配）**

默认情况下，`NuxtLink` 只要匹配**前缀**，就会被视为“当前路由”。比如：

```vue
<NuxtLink to="/about">关于</NuxtLink>
<NuxtLink to="/about/team">团队</NuxtLink>
```

访问 `/about/team`，**两个链接都会被高亮！**  
👉 **解决方法**：加上 `exact`，确保只有 `/about` 精确匹配：

```vue
<NuxtLink to="/about" exact>关于</NuxtLink>
```

---

### **🔹 3.4 `replace`（替换历史记录）**

默认情况下，`NuxtLink` 会**将页面跳转记录添加到浏览器历史**（可以按 "返回" 键返回）。  
如果你不想保存历史记录，可以使用 `replace`：

```vue
<NuxtLink to="/profile" replace>用户中心</NuxtLink>
```

📌 **点击后，不能用浏览器的“后退”按钮返回上一个页面。**

---

### **🔹 3.5 `prefetch`（预加载）**

Nuxt 默认会**预加载**页面（用户鼠标悬停时），你可以手动关闭：

```vue
<NuxtLink to="/dashboard" :prefetch="false">仪表盘</NuxtLink>
```

**✅ 好处：**

- 预加载加快页面切换速度（默认开启）。
- 适合移动端优化，减少流量消耗。

---

### **🔹 3.6 `external`（跳转外部网站）**

如果要打开外部网站，同时使用 `<NuxtLink>` 的样式：

```vue
<NuxtLink to="https://google.com" external>谷歌</NuxtLink>
```

📌 这个方式比 `<a href="..." target="_blank">` 更安全，Nuxt 处理了 **跨站安全风险**（如 `noopener`）。

---

### **🔹 3.7 `target="_blank"`**

有时候，我们需要让 `NuxtLink` **在新标签页打开**：

```vue
<NuxtLink to="https://google.com" target="_blank" rel="noopener noreferrer">
  谷歌
</NuxtLink>
```

📌 `rel="noopener noreferrer"` 可以防止 **安全漏洞（反向脚本攻击）**。

---

## **📌 4. 在 JavaScript 代码中使用 NuxtLink**

有时，我们需要在 **方法中进行页面跳转**，可以使用 `useRouter()`：

```vue
<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

// 点击按钮后跳转
const goToProfile = () => {
  router.push('/profile');
};
</script>

<template>
  <button @click="goToProfile">跳转到个人中心</button>
</template>
```

📌 **`router.push()`** 相当于 `<NuxtLink>`  
📌 **`router.replace()`** 相当于 `<NuxtLink replace>`

---

## **🎯 总结**

|功能|代码示例|
|---|---|
|**基本跳转**|`<NuxtLink to="/about">关于</NuxtLink>`|
|**动态参数**|`<NuxtLink :to="`/blog/${id}`">博客</NuxtLink>`|
|**选中样式**|`<NuxtLink to="/home" active-class="active">首页</NuxtLink>`|
|**精确匹配**|`<NuxtLink to="/about" exact>关于</NuxtLink>`|
|**替换历史**|`<NuxtLink to="/profile" replace>用户中心</NuxtLink>`|
|**禁用预加载**|`<NuxtLink to="/dashboard" :prefetch="false">仪表盘</NuxtLink>`|
|**外部链接**|`<NuxtLink to="https://google.com" external>谷歌</NuxtLink>`|
|**新标签页**|`<NuxtLink to="https://google.com" target="_blank">谷歌</NuxtLink>`|
|**JavaScript 跳转**|`router.push('/profile')`|

📌 **💡 NuxtLink 是 Vue Router 的增强版，支持 SEO、性能优化和动态路由管理，应该优先使用 NuxtLink 进行内部页面跳转！**