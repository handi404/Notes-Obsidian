在 Vue 3 的 TypeScript 项目中，使用 **Props** 可以通过 `<script setup>` 和类型声明实现类型安全的组件通信。以下是详细步骤和示例：

---

### 1. 子组件定义 Props（类型声明）

在子组件中使用 `defineProps` 定义 Props 的类型和验证规则：

#### 基本类型声明（推荐方式）
```vue
<!-- ChildComponent.vue -->
<script setup lang="ts">
// 直接通过泛型定义 Props 类型
const props = defineProps<{
  title: string
  count: number
  isActive: boolean
  tags?: string[]   // 可选属性
}>()
</script>

<template>
  <div>
    <h2>{{ title }}</h2>
    <p>Count: {{ count }}</p>
    <p v-if="isActive">状态: 激活</p>
    <ul v-if="tags">
      <li v-for="tag in tags" :key="tag">{{ tag }}</li>
    </ul>
  </div>
</template>
```

#### 复杂类型（使用 `PropType` 工具）
处理对象或自定义类型时，需导入 `PropType`：
```vue
<script setup lang="ts">
import type { PropType } from 'vue'

interface User {
  id: number
  name: string
}

const props = defineProps({
  user: {
    type: Object as PropType<User>,
    required: true
  },
  scores: {
    type: Array as PropType<number[]>,
    default: () => [0]
  }
})
</script>
```

---

### 2. 父组件传递 Props

父组件通过 `v-bind` 或静态属性传递数据，TypeScript 会自动检查类型匹配：

```vue
<!-- ParentComponent.vue -->
<script setup lang="ts">
import ChildComponent from './ChildComponent.vue'
import { ref } from 'vue'

const count = ref(0)
const userData = { id: 1, name: 'Alice' }
</script>

<template>
  <ChildComponent
    title="用户信息"          <!-- 静态字符串 -->
    :count="count"          <!-- 动态绑定响应式变量 -->
    :is-active="true"       <!-- 布尔值需用 v-bind（或 :） -->
    :user="userData"        <!-- 传递对象 -->
    :tags="['vue', 'ts']"   <!-- 传递数组 -->
  />
</template>
```

---

### 关键点说明

#### 1. 类型安全
- **基本类型**：直接通过泛型定义（如 `defineProps<{ title: string }>()`）。
- **复杂类型**：使用 `PropType` 包装（如 `Object as PropType<User>`）。
- **可选属性**：通过 `?` 标记（如 `tags?: string[]`）。

#### 2. 默认值
如果使用泛型语法，需通过 `withDefaults` 定义默认值：
```typescript
const props = withDefaults(defineProps<{
  title?: string
  count: number
}>(), {
  title: '默认标题',  // 可选属性的默认值
  count: 0           // 必填属性无需默认值（但父组件必须传递）
})
```

#### 3. 验证函数
通过对象语法添加自定义验证：
```typescript
const props = defineProps({
  score: {
    type: Number,
    required: true,
    validator: (value: number) => {
      return value >= 0 && value <= 100
    }
  }
})
```

---

### 补充场景示例

#### 1. 联合类型
```typescript
defineProps<{
  status: 'pending' | 'success' | 'error'   // 明确允许的字符串值
  value: string | number                   // 多类型支持
}>()
```

#### 2. 函数类型
```typescript
import type { PropType } from 'vue'

defineProps({
  callback: {
    type: Function as PropType<(data: string) => void>,
    required: true
  }
})
```

#### 3. 响应式转换
直接解构 Props 会失去响应性，需用 `toRefs`：
```typescript
import { toRefs } from 'vue'

const props = defineProps<{ count: number }>()
const { count } = toRefs(props) // 保持响应性
```

---

### 注意事项

1. **命名规范**：
   - Props 在子组件中使用 **camelCase** 定义（如 `isActive`）。
   - 父组件传递时使用 **kebab-case**（如 `:is-active="true"`）。

2. **单向数据流**：
   - 避免直接修改 Props 的值，如需改动应通过 `emit` 事件通知父组件。

3. **`required` 与 `default`**：
   - 若未标记 `required: true` 且未提供 `default`，TypeScript 会推断为可选类型。
   - 使用泛型语法时，可选属性需显式添加 `?`（如 `title?: string`）。

4. **复杂类型处理**：
   - 对象和数组的默认值需使用函数返回（如 `default: () => []`）。

---

通过以上方法，可以在 Vue 3 + TypeScript 项目中实现严格的 Props 类型检查，确保组件间数据传递的安全性和可维护性。