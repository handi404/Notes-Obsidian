在 Vue 3 的 TypeScript 项目中使用 `emits` 事件，可以通过 Composition API 和 `<script setup>` 语法结合类型声明来实现类型安全的事件通信。以下是详细步骤和示例：

### 1. 子组件定义和触发事件

在子组件中使用 `defineEmits` 定义事件及其参数类型，并通过 `emit` 函数触发事件：

```vue
<!-- ChildComponent.vue -->
<script setup lang="ts">
// 定义事件及其参数类型
const emit = defineEmits<{
  (e: 'update', value: string): void
  (e: 'delete', id: number): void
}>()

const handleUpdate = () => {
  emit('update', 'new value') // 触发事件并传递参数
}

const handleDelete = () => {
  emit('delete', 123)
}
</script>

<template>
  <button @click="handleUpdate">更新</button>
  <button @click="handleDelete">删除</button>
</template>
```

### 2. 父组件监听和处理事件

在父组件中引入子组件，并通过 `@事件名` 监听事件，处理函数接收参数并指定类型：

```vue
<!-- ParentComponent.vue -->
<script setup lang="ts">
import ChildComponent from './ChildComponent.vue'

// 处理 update 事件
const handleUpdate = (value: string) => {
  console.log('收到更新值:', value)
}

// 处理 delete 事件
const handleDelete = (id: number) => {
  console.log('删除项目 ID:', id)
}
</script>

<template>
  <ChildComponent 
    @update="handleUpdate"
    @delete="handleDelete"
  />
</template>
```

### 关键点说明

1. **类型安全的事件定义**：
   - 使用 `defineEmits<{...}>()` 在子组件中声明事件签名，确保触发事件时参数类型正确。
   - 每个事件用函数形式定义，例如 `(e: '事件名', 参数: 类型): void`。

2. **触发事件**：
   - 通过 `emit('事件名', 参数)` 触发事件，参数必须与定义的类型一致。
   - 事件名建议使用 **camelCase**，父组件监听时转换为 **kebab-case**（如 `@update-value`）。

3. **父组件监听事件**：
   - 使用 `@事件名` 绑定处理函数，函数参数类型与子组件定义匹配。

### 补充场景示例

#### 无参数事件

```typescript
// 子组件
const emit = defineEmits<{
  (e: 'notify'): void
}>()

emit('notify') // 触发无参数事件
```

```vue
<!-- 父组件 -->
<ChildComponent @notify="handleNotify" />
```

#### 多个参数事件

```typescript
// 子组件
const emit = defineEmits<{
  (e: 'submit', name: string, age: number): void
}>()

emit('submit', 'Alice', 30)
```

```typescript
// 父组件处理函数
const handleSubmit = (name: string, age: number) => {
  // ...
}
```

### 注意事项

- **事件名大小写**：在模板中监听事件时，建议使用 **kebab-case**（如 `@update-value`），即使子组件中事件名是 camelCase（如 `updateValue`）。
- **类型检查**：确保子组件 `emit` 时参数类型和顺序与定义一致，避免 TypeScript 报错。
- **组合式 API**：如果未使用 `<script setup>`，可在 `setup()` 函数中使用 `context.emit`，但仍需通过 `emits` 选项声明事件。

通过以上步骤，你可以在 Vue 3 + TypeScript 项目中实现类型安全的事件通信，提高代码的可靠性和维护性。