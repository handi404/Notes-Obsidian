# **📌 Nuxt 组件（Components）详解 & 可复用组件**

在 Nuxt 中，**组件（Components）** 是构建 UI 界面的基础。Nuxt 提供了强大的 **自动导入** 机制，让你可以更高效地管理和使用组件，而不需要手动 `import`。

---

## **🚀 1. 组件基础**

### **📌 组件目录结构**

在 Nuxt 项目中，组件通常存放在 **`components/` 目录**：

```
components/
│── Header.vue       # 头部组件
│── Footer.vue       # 底部组件
│── Button.vue       # 按钮组件
```

✅ **Nuxt 自动导入 `components/` 目录下的组件**，所以在页面或其他组件中可以直接使用，而不需要 `import`！

---

## **📌 2. 创建一个基础组件**

### **📌 `components/Button.vue`**

```vue
<template>
  <button class="btn">
    <slot />  <!-- 插槽，支持动态内容 -->
  </button>
</template>

<style>
.btn {
  background-color: blue;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}
</style>
```

### **📌 在 `pages/index.vue` 使用**

```vue
<template>
  <div>
    <h1>欢迎来到 Nuxt！</h1>
    <Button>点击我</Button>  <!-- 直接使用组件 -->
  </div>
</template>
```

✅ **无需 `import`，Nuxt 会自动注册 `components/` 里的组件！**

---

## **🚀 3. 组件的 `Props`（属性）**

📌 `props` 允许组件接收外部传递的数据。

### **📌 `components/Alert.vue`**

```vue
<script setup>
defineProps({
  type: { type: String, default: 'info' },
  message: { type: String, required: true }
});
</script>

<template>
  <div :class="`alert alert-${type}`">
    {{ message }}
  </div>
</template>

<style>
.alert {
  padding: 10px;
  border-radius: 5px;
}
.alert-info {
  background-color: lightblue;
}
.alert-error {
  background-color: red;
  color: white;
}
</style>
```

### **📌 在 `index.vue` 里使用**

```vue
<template>
  <div>
    <Alert type="error" message="❌ 出错了！" />
    <Alert type="info" message="ℹ️  这是一个通知。" />
  </div>
</template>
```

✅ **`props` 让组件更具复用性，支持不同类型的警告框！**

---

## **🚀 4. 事件处理（`emit`）**

📌 `emit` 允许子组件向父组件发送事件。

### **📌 `components/Counter.vue`**

```vue
<script setup>
import { ref, defineEmits } from 'vue';

const count = ref(0);
const emit = defineEmits(['update']);

const increment = () => {
  count.value++;
  emit('update', count.value);
};
</script>

<template>
  <div>
    <p>当前计数: {{ count }}</p>
    <button @click="increment">增加</button>
  </div>
</template>
```

### **📌 在 `index.vue` 里监听事件**

```vue
<script setup>
import { ref } from 'vue';

const total = ref(0);
const handleUpdate = (newValue) => {
  total.value = newValue;
};
</script>

<template>
  <div>
    <Counter @update="handleUpdate" />
    <p>总计数: {{ total }}</p>
  </div>
</template>
```

✅ **子组件 `emit` 触发 `update` 事件，父组件监听并更新 `total`！**

---

## **🚀 5. 具名插槽（`slot`）**

📌 `slot` 允许组件内容动态填充，**具名插槽**（`name`）可以让不同部分内容可定制。

### **📌 `components/Card.vue`**

```vue
<template>
  <div class="card">
    <header>
      <slot name="header" />  <!-- 具名插槽 -->
    </header>
    <main>
      <slot />  <!-- 默认插槽 -->
    </main>
    <footer>
      <slot name="footer" />
    </footer>
  </div>
</template>

<style>
.card {
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
}
</style>
```

### **📌 在 `index.vue` 里使用**

```vue
<template>
  <Card>
    <template #header>
      <h3>📌 文章标题</h3>
    </template>
    这里是正文内容...
    <template #footer>
      <p>© 2025 版权信息</p>
    </template>
  </Card>
</template>
```

✅ **`slot` 让组件内容高度可定制！**

---

## **🚀 6. `Lazy` 组件（懒加载）**

📌 `Lazy` 组件可用于优化性能，Nuxt **只有在需要时才加载组件**，适合大组件。

### **📌 懒加载 `HeavyComponent.vue`**

```vue
<template>
  <div>
    <h1>⏳ 这是一个大组件</h1>
  </div>
</template>
```

### **📌 在 `index.vue` 里按需加载**

```vue
<template>
  <div>
    <LazyHeavyComponent />  <!-- 仅在需要时才加载 -->
  </div>
</template>
```

✅ **Nuxt 会自动优化组件加载，提高性能！**

---

## **🚀 7. 全局组件**

📌 你可以创建 `components/global/` 目录，并在 `nuxt.config.ts` 里启用全局组件：

```ts
export default defineNuxtConfig({
  components: [{ path: '~/components/global', global: true }]
});
```

✅ **这样 `global/` 目录下的组件可在任何地方直接使用！**

---

## **🎯 总结**

|**功能**|**示例**|
|---|---|
|**自动导入组件**|`components/Button.vue` 直接 `<Button>`|
|**Props 传值**|`<Alert type="error" message="失败了" />`|
|**事件 `emit`**|`<Counter @update="handleUpdate" />`|
|**插槽 `slot`**|`<Card><template #header>...</template></Card>`|
|**Lazy 组件**|`<LazyHeavyComponent />`|
|**全局组件**|`components/global/` 目录|

📌 **💡 Nuxt 组件系统让开发更高效，自动注册、优化加载、支持插槽、事件传递，打造灵活的 UI 组件库！****