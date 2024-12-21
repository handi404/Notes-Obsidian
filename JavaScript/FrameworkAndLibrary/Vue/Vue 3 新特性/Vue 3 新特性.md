Vue 3 相比于 Vue 2 引入了多个新特性和改进，提升了性能、灵活性和开发体验。以下是 Vue 3 增加的主要功能和变化：

### 1. **Composition API**
- **功能**：Vue 3 引入了全新的 `Composition API`，它允许你以一种更灵活和可复用的方式组织逻辑。
- **优点**：
  - 更好地组织复杂组件中的逻辑。
  - 逻辑复用比 `Vue 2` 中的 `Mixins` 更加灵活和明确。
  - 提供了 Hooks 风格的函数，比如 `setup`，可以处理响应式状态和生命周期逻辑。

**示例**：
```javascript
import { ref } from 'vue';

export default {
  setup() {
    const count = ref(0);
    
    function increment() {
      count.value++;
    }
    
    return {
      count,
      increment,
    };
  },
};
```

### 2. **性能改进**
- **打包体积更小**：Vue 3 的核心库大小减少到约 `10KB`（gzip 后），比 Vue 2 更轻量。
- **优化虚拟 DOM**：Vue 3 引入了更高效的虚拟 DOM 更新算法，提高了渲染性能。
- **Tree-shaking 支持**：Vue 3 是完全 tree-shakable 的，未使用的代码不会被打包。

### 3. **Fragment（片段）**
- **功能**：Vue 3 允许组件返回多个根节点元素，而不需要像 Vue 2 一样必须有一个单一的根元素。
- **优点**：消除了 Vue 2 中创建多余包裹元素的需要，使模板更加干净和灵活。

**示例**：
```html
<template>
  <div>First element</div>
  <div>Second element</div>
</template>
```

### 4. **Teleport（传送门）**
- **功能**：`Teleport` 允许你将某些 DOM 元素渲染到组件外的任意位置。
- **优点**：非常适合实现模态框、通知、悬浮菜单等，方便你把 DOM 节点挂载到文档树的不同位置。

**示例**：
```html
<template>
  <teleport to="body">
    <div class="modal">I'm teleported to the body tag!</div>
  </teleport>
</template>
```

### 5. **Emits 选项**
- **功能**：Vue 3 新增了 `emits` 选项，明确声明组件可以发出的事件，并且提供事件的类型检查。
- **优点**：提高了代码的可读性和维护性，增强了类型检查功能。

**示例**：
```javascript
export default {
  emits: ['update'],
  methods: {
    updateValue() {
      this.$emit('update', newValue);
    }
  }
}
```

### 6. **更好的 Typescript 支持**
- Vue 3 在设计上完全支持 TypeScript，提供了更好的类型推断和开发体验。相比 Vue 2，Vue 3 具有更优的 TypeScript 集成，不需要额外的工具进行类型补全。

### 7. **自定义渲染器 API**
- Vue 3 提供了更加灵活的 `Custom Renderer API`，使开发者能够轻松地将 Vue 应用于非浏览器环境（如移动端、桌面应用）。
  
### 8. **响应性系统的改进**
- Vue 3 的响应式系统使用 `Proxy` 替代了 Vue 2 中的 `Object.defineProperty`，解决了 Vue 2 中无法检测到对象或数组属性新增/删除的问题。
- 新的响应式 API (`ref`、`reactive`) 提供了更加细粒度的响应性控制。

### 9. **Suspense**
- **功能**：`Suspense` 允许你等待异步组件的加载，期间可以显示加载状态，非常类似于 React 的 `Suspense` 组件。
- **优点**：提供了更好的异步渲染控制，方便实现更复杂的加载逻辑。

**示例**：
```html
<template>
  <suspense>
    <template #default>
      <AsyncComponent />
    </template>
    <template #fallback>
      <div>Loading...</div>
    </template>
  </suspense>
</template>
```

### 10. **新指令：v-memo**
- **功能**：`v-memo` 是 Vue 3 中的新指令，用于缓存组件的渲染结果，只有在依赖项变化时才重新渲染。
- **优点**：可以优化性能，减少不必要的 DOM 更新。

### 11. **v-model 的改进**
- Vue 3 中的 `v-model` 现在支持多个绑定值，并且可以自定义 `v-model` 的绑定属性和事件名称。

**示例**：
```html
<child-component v-model:title="title" v-model:content="content" />
```

### 12. **异步组件语法改进**
- Vue 3 提供了更加简洁的异步组件加载方式，使用动态导入和 `defineAsyncComponent` API 替代了 Vue 2 中的异步组件声明方式。

### 结论
Vue 3 在保持向后兼容性的同时，通过引入 `Composition API`、性能优化、`Fragment`、`Teleport`、`Suspense` 等特性，极大地提高了开发效率和应用的灵活性。对于大型项目和团队合作来说，Vue 3 提供了更现代和高效的工具链和支持。