在 Nuxt.js 中，`props` 的使用方式与 Vue.js 类似，但结合了 Nuxt 的路由自动生成和页面组件特性。以下是 Nuxt 中 `props` 的详细使用方式及示例：

---

### 1. **组件 Props（父子组件通信）**
父组件向子组件传递静态或动态数据，子组件通过 `props` 接收。

#### 子组件定义 Props
```vue
<!-- components/Child.vue -->
<script>
export default {
  props: ['title', 'content'], // 数组形式
  // 或对象形式（带验证）
  props: {
    title: {
      type: String,
      required: true
    },
    content: {
      type: String,
      default: '默认内容'
    }
  }
}
</script>
```

#### 父组件传递 Props
```vue
<!-- pages/parent.vue -->
<template>
  <Child title="静态标题" :content="dynamicContent" />
</template>
<script setup>
const dynamicContent = ref('动态内容');
</script>
```

---

### 2. **路由参数自动映射为 Props**
在动态路由页面组件中，通过声明 `props` 接收路由参数。

#### 动态路由示例
- 文件路径：`pages/post/[id].vue`
- 访问路径：`/post/123`

```vue
<!-- pages/post/[id].vue -->
<script setup>
const props = defineProps({
  id: String // 自动接收路由中的 `id` 参数
});
</script>
```

#### 自定义 Props 映射
通过 `definePageMeta` 使用函数形式处理复杂逻辑：
```vue
<script setup>
definePageMeta({
  // 将路由参数转换为数字类型的 `postId`
  props: (route) => ({ postId: parseInt(route.params.id) })
});

const props = defineProps({
  postId: Number
});
</script>
```

---

### 3. **接收查询参数作为 Props**
将 URL 查询参数映射为组件的 `props`。

```vue
<!-- pages/search.vue -->
<script setup>
definePageMeta({
  // 映射查询参数 `q` 到 `searchQuery`
  props: (route) => ({ searchQuery: route.query.q })
});

const props = defineProps({
  searchQuery: String
});
</script>
```

---

### 4. **嵌套路由中的 Props 传递**
在父路由组件中通过 `<NuxtChild>` 传递 `props` 给子路由组件。

#### 父组件传递 Props
```vue
<!-- pages/parent.vue -->
<template>
  <div>
    <NuxtChild :childProp="data" />
  </div>
</template>
<script setup>
const data = ref('传递给子路由的数据');
</script>
```

#### 子组件接收 Props
```vue
<!-- pages/parent/child.vue -->
<script setup>
const props = defineProps({
  childProp: String
});
</script>
```

---

### 5. **Props 验证与默认值**
在定义 `props` 时添加类型检查、默认值和验证函数。

```vue
<script setup>
const props = defineProps({
  id: {
    type: Number,
    required: true,
    validator: (value) => value > 0 // 确保 id 为正数
  },
  content: {
    type: String,
    default: '默认内容'
  }
});
</script>
```

---

### 6. **组合式 API 中的 Props**
在 `<script setup>` 中使用 `defineProps` 定义 `props`。

```vue
<script setup>
const props = defineProps({
  title: String,
  likes: {
    type: Number,
    default: 0
  }
});
</script>
```

---

### 7. **异步数据与 Props**
在 `asyncData` 或 `fetch` 中获取数据，结合 `props` 使用。

```vue
<!-- pages/post/[id].vue -->
<script>
export default {
  async asyncData({ params }) {
    const post = await fetchPost(params.id);
    return { post };
  },
  props: ['id']
}
</script>
```

---

### 8. **高级路由配置**
在 `nuxt.config.js` 中全局配置路由的 `props` 行为（不常用，通常优先使用页面级配置）。

```javascript
// nuxt.config.js
export default {
  router: {
    extendRoutes(routes) {
      routes.forEach(route => {
        if (route.path.includes('/post/')) {
          route.props = true; // 自动传递路由参数为 props
        }
      });
    }
  }
};
```

---

### **总结**
- **组件通信**：父子组件通过 `props` 传值，支持静态和动态数据。
- **路由参数映射**：动态路由参数自动注入同名 `props`，需在页面组件中声明。
- **查询参数处理**：通过 `definePageMeta` 自定义函数映射查询参数到 `props`。
- **嵌套路由**：父组件通过 `<NuxtChild>` 向子路由传递 `props`。
- **验证与默认值**：增强代码健壮性，确保数据符合预期格式。
- **组合式 API**：使用 `defineProps` 简化 `props` 定义。

通过灵活运用这些方式，可以在 Nuxt.js 中高效管理组件间的数据流和路由参数传递。