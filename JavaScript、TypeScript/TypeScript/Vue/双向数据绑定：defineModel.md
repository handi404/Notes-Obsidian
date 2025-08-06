`defineModel` 是 Vue 3.3 引入的革命性新特性，它彻底改变了我们编写双向绑定组件的方式。让我们一次性扫清所有盲点。

---

### `defineModel`: 新一代双向绑定利器

`defineModel` 是一个编译时宏 (Compiler Macro)，它将过去繁琐的 `props` + `emit` 模式简化为一行代码，极大地提升了开发效率和代码可读性。

---

### 1. 为什么诞生 (The "Why")?

在 `defineModel` 出现之前，我们实现 `v-model` 的最佳实践是使用一个可写的 `computed` 属性，像这样：

**旧的 “黄金标准” 写法:** 

```typescript
import { computed } from 'vue';

// 1. 声明 Props
const props = defineProps<{ modelValue: string }>();

// 2. 声明 Emits
const emit = defineEmits<{(e: 'update:modelValue', value: string): void}>();

// 3. 用 computed 连接 props 和 emit
const internalValue = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit('update:modelValue', newValue);
  }
});

// 模板中使用 <input v-model="internalValue" />
```

这个模式虽然功能完善且逻辑清晰，但存在明显的**痛点**：

*   **样板代码 (Boilerplate)**: 每个需要 `v-model` 的组件都必须重复 `defineProps` -> `defineEmits` -> `computed` 这三步曲。非常繁琐。
*   **心智负担**: 开发者需要时刻记着这个固定模式，分散了对核心业务逻辑的注意力。
*   **代码冗长**: 简单的功能占用了大量的代码行数，降低了组件的信噪比。

**`defineModel` 的诞生，就是为了彻底消灭这些痛点。** 它的使命是：**用声明式的一行代码，完成过去命令式的三步操作。**

---

### 2. 底层原理 (The "How it Works")

`defineModel` 并不是运行时的一个普通函数，它是一个**编译时宏**。这意味着，在 Vite 或 vue-cli 的构建过程中，Vue 编译器会识别到 `defineModel`，并将其**自动转换**成我们上面看到的那个“黄金标准”写法。

**你写的代码:**

```typescript
const model = defineModel<string>();
```

**Vue 编译器转换后的代码 (概念上):**

```typescript
// 它在 <script setup> 内部帮你生成了这些：
const props = defineProps<{ modelValue: string }>();
const emit = defineEmits<{(e: 'update:modelValue', value: string): void}>();
const model = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
});
```

所以，`defineModel` 返回的 `model` 变量，本质上是一个行为特殊的 `ref`（实际上是一个可写的 `computed` ref）。当你读取 `.value` 时，它会去读取 `prop`；当你修改 `.value` 时，它会自动 `emit` 事件。

**这就是它的魔法：极致的语法糖，将最佳实践内置到了框架核心中。**

---

### 3. 全方位用法 (The "How to Use")

`defineModel` 目前是一个**实验性特性**（截至 Vue 3.4 依然是，但已非常稳定，可以放心使用）。你需要先在 `vite.config.ts` 中开启它：

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue({
      script: {
        defineModel: true, // 开启 defineModel
      }
    })
  ],
})
```
*注：在未来的 Vue 版本中，这可能会成为默认行为，无需手动开启。*

#### 3.1 单 v-model (标准用法)

**子组件 `NewCustomInput.vue`**

```vue
<template>
  <input v-model="model" />
</template>

<script lang="ts" setup>
// 仅需一行！
const model = defineModel<string>();

// 你甚至可以像普通 ref 一样在 script 中操作它
// model.value = 'hello'; // 这会自动 emit 更新
</script>
```

**父组件使用**

```vue
<template>
  <NewCustomInput v-model="message" />
  <p>Message: {{ message }}</p>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import NewCustomInput from './components/NewCustomInput.vue';

const message = ref('Initial value');
</script>
```
代码量减少了 80%，意图清晰无比。

#### 3.2 多 v-model (带参数)

`defineModel` 的第一个参数可以用来指定 `v-model` 的名字。

**子组件 `NewNameEditor.vue`**

```vue
<template>
  <input v-model="firstName" placeholder="姓" />
  <input v-model="lastName" placeholder="名" />
</template>

<script lang="ts" setup>
const firstName = defineModel<string>('firstName');
const lastName = defineModel<string>('lastName');
</script>
```

**父组件使用**

```vue
<template>
  <NewNameEditor v-model:firstName="user.firstName" v-model:lastName="user.lastName" />
</template>

<script lang="ts" setup>
import { reactive } from 'vue';
import NewNameEditor from './components/NewNameEditor.vue';

const user = reactive({ firstName: '张', lastName: '三' });
</script>
```
同样，比 `defineProps` + `defineEmits` 的方式简洁得多。

#### 3.3 TypeScript 与选项

`defineModel` 的类型和选项定义非常强大。

```typescript
// 基础类型
const model = defineModel<string>();

// 设置为必需
const modelRequired = defineModel<string>({ required: true });

// 提供默认值
const modelDefault = defineModel<string>({ default: 'default value' });

// 结合使用
const firstName = defineModel<string>('firstName', { required: true });
```
编译器会根据这些信息，自动生成正确的 `defineProps` 定义。

#### 3.4 处理修饰符 (Modifiers)

`v-model` 的修饰符 (`.trim`, `.number` 或自定义修饰符) 也可以通过 `defineModel` 优雅地处理。它会返回一个元组 `[modelRef, modifiersRef]`。

**场景**: 创建一个输入框，如果父组件使用了 `.capitalize` 修饰符，则自动将输入的首字母大写。

**子组件 `CapitalizeInput.vue`**

```vue
<template>
  <input v-model="model" />
</template>

<script lang="ts" setup>
// 解构获取 model 和 modifiers
const [model, modifiers] = defineModel<string>();

// 监听 model 的变化
watch(model, (newValue) => {
  if (modifiers.capitalize && newValue) {
    // 如果父组件使用了 .capitalize 修饰符，则执行逻辑
    const capitalized = newValue.charAt(0).toUpperCase() + newValue.slice(1);
    if (capitalized !== newValue) {
      model.value = capitalized; // 更新 model，会自动 emit
    }
  }
});
</script>
```

**父组件使用**

```vue
<!-- 这个组件的输入会自动首字母大写 -->
<CapitalizeInput v-model.capitalize="name" />

<!-- 这个组件则不会 -->
<CapitalizeInput v-model="anotherName" />
```
`modifiers` 是一个只读的 ref，其 `.value` 是一个对象，包含了所有父组件传递的修饰符（如 `{ capitalize: true }`）。这比以前处理修饰符的方式清晰了不止一个量级。

---

### 4. 现场避坑 (Pitfall Avoidance)

1.  **环境配置**: 确保你的 `vue` 版本 >= 3.3，并且在构建工具中启用了该特性。否则，`defineModel` 会被视为未定义的普通函数而报错。
2.  **不要解构 `defineModel` 的返回值**: `defineModel` 返回的是一个 `ref`。如果你这样做，会丢失响应性。
    ```typescript
    // 错误 ❌
    const { value } = defineModel<string>(); // value 将是一个静态值，不是 ref

    // 正确 ✅
    const model = defineModel<string>();
    console.log(model.value);
    model.value = 'new';
    ```
3.  **修饰符逻辑需自行实现**: `defineModel` 只会告诉你父组件**是否**使用了某个修饰符，它**不会**自动应用修饰符的逻辑。例如，`.number` 修饰符，你仍然需要在 `input` 事件中自行处理 `Number(value)` 的转换。
4.  **与 `props` 默认值冲突**: 如果你同时在 `defineModel` 和 `defineProps` 中为一个 prop 定义了 `default` 值，Vue 会发出警告。`defineModel` 的选项是首选。
    ```typescript
    // 避免这样做
    defineProps({ modelValue: { default: 'from props' } });
    const model = defineModel<string>({ default: 'from model' }); // 会有警告
    ```

---

**总结**

`defineModel` 是对 Vue 组件开发体验的一次巨大飞跃。它将久经考验的最佳实践封装成了最简洁的 API，让我们能够更专注于业务逻辑本身。它不是一个颠覆性的新概念，而是对现有模式的**终极进化**。


---


**Vue3.4 早在 2023 年 12 月就把 `defineModel` 转正** ，可直到今天，还有人用 Vue2 时代的 `props + emit` 手写双向绑定，代码又长又臭，Bug 一堆。

## defineModel 到底是什么？

一句话定义：让子组件像原生 `<input>` 一样直接支持 `v-model` 的语法糖；说白了就是一个 **宏（macro）** ，在编译期把 `defineModel()` 展开成 `props` \+ `emit`

### 宏 VS 函数

- **宏** ：编译期代码生成，运行时 `0` 额外开销。
- **函数** ：运行时真实调用。  
	因此 `defineModel` 不需要 `import` ，也不能在普通 `<script>` 或 `.js/.ts` 文件里使用。

### 生成的等价代码

```ts
// 你写的
const model = defineModel<string>({ default: 'hello' })

// 编译后（伪代码）
const props = defineProps({
  modelValue: { type: String, default: 'hello' }
})
const emit  = defineEmits(['update:modelValue'])
const model = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})
```

说白了就是一个宏（macro），在编译期把 defineModel() 展开成 props + emit

## 快速上手（3 个例子包会）

### 单 v-model —— 最常用 90% 场景

- 父组件
```vue
<template>
  <UserName v-model="name" />
  <p>父组件拿到的值：{{ name }}</p>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import UserName from './UserName.vue'

const name = ref('张三')
</script>
```
- 子组件 `UserName.vue`
```vue
<template>
  <input v-model="modelValue" />
</template>

<script setup lang="ts">
const modelValue = defineModel<string>()
// 等价于 const modelValue = defineModel<string>({ required: true })
</script>
```

### 多个 v-model —— 表单类组件刚需

- 父组件
```vue
<template>
  <UserForm
    v-model="form.name"
    v-model:age="form.age"
    v-model:phone="form.phone"
  />
  <pre>{{ form }}</pre>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import UserForm from './UserForm.vue'

const form = reactive({
  name: '张三',
  age: 18,
  phone: '13800138000'
})
</script>
```
- 子组件 `UserForm.vue`
```vue
<template>
  <input v-model="name" placeholder="姓名" />
  <input v-model="age"   placeholder="年龄" />
  <input v-model="phone" placeholder="手机号" />
</template>

<script setup lang="ts">
const name  = defineModel<string>('name')
const age   = defineModel<number>('age')
const phone = defineModel<string>('phone')
</script>
```

### 带修饰符 & 转换器 —— 再也不用手动.trim

- 父组件
```vue
<template>
  <TrimInput v-model.trim="keyword" />
  <p>父组件值：{{ keyword }}</p>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import TrimInput from './TrimInput.vue'

const keyword = ref('')
</script>
```
- 子组件 `TrimInput.vue`
```vue
<template>
  <input v-model="modelValue" />
</template>

<script setup lang="ts">
const [modelValue, modifiers] = defineModel<string, 'trim'>()

// 当父组件写 v-model.trim 时，modifiers.trim === true
if (modifiers.trim) {
  // 通过 set 函数实时转换
}
</script>
```
- 如果你需要 **实时转换** ，用 `get / set` ：
```ts
const [modelValue, modifiers] = defineModel<string, 'trim'>({
  set(val) {
    return modifiers.trim ? val.trim() : val
  }
})
```

## TypeScript 高阶姿势

| 需求 | 写法 |
| --- | --- |
| 必填 | `defineModel<string>({ required: true })` |
| 可选+默认值 | `defineModel<string>({ default: '张三' })` |
| 联合类型 | `defineModel<'male' \| 'female'>()` |
| 复杂对象 | `defineModel<User>()` |

- 注意：默认值如果是 `对象` / `数组` ，请用函数返回新实例，避免引用共享：
```ts
defineModel<string[]>({ default: () => ['A', 'B'] })
```