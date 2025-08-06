## **Vue 3 `setup()` 函数默认接收的参数详解**

在 Vue 3 组合式 API（Composition API）中，`setup()` 是组件的核心入口，它会在组件实例创建**之前**执行，且没有 `this` 上下文。因此，Vue 为 `setup()` 提供了**两个默认参数**：

```js
setup(props, context) {
  // 这里是 setup 逻辑
}
```

- `props`：组件从**父组件**接收的 `props`
- `context`：包含 `emit`（事件触发）、`attrs`（非 `props` 绑定的属性）、`slots`（插槽）

---

# **1. `props`（组件属性）**

`props` 是**响应式的**，用于接收父组件传递的属性。在 `setup()` 中，`props` 只能**读取**，不能修改（因为 `props` 由父组件控制）。

### **示例：子组件接收 `props`**

**父组件 (`Parent.vue`)**

```vue
<template>
  <Child msg="Hello Vue!" />
</template>
```

**子组件 (`Child.vue`)**

```vue
<template>
  <p>{{ msg }}</p>
</template>

<script>
export default {
  props: {
    msg: String
  },
  setup(props) {
    console.log(props.msg); // "Hello Vue!"
    return { props };
  }
};
</script>
```

### **📌 解析**

- `setup(props)` 接收 `props` 参数，`props.msg` 即 `"Hello Vue!"`
- `props` 是响应式的，**但不能修改**，如：
    
    ```js
    props.msg = "New Message"; // ❌ 会报错，因为 props 只能由父组件控制
    ```
    
- **如果想修改 `props` 的值，应该使用 `ref()` 或 `computed()` 生成新的响应式值**。

#### **✅ 正确修改 `props`（使用 `computed()`）**

```js
import { computed } from 'vue';

setup(props) {
  const newMsg = computed(() => props.msg + '!!!'); 
  return { newMsg };
}
```

- 这样 `newMsg` 就会根据 `props.msg` 计算新的值。

---

# **2. `context`（上下文对象）**

`setup()` 的第二个参数 `context` 是一个对象，包含：

```js
{
  attrs,  // 非 props 绑定的 attribute
  slots,  // 具名插槽 & 作用域插槽
  emit    // 触发自定义事件
}
```

## **2.1 `context.attrs`（非 `props` 绑定的 attribute）**

`attrs` 存放的是**传递给组件但未声明为 `props` 的属性**，适用于透传 `class`、`id`、`data-*` 等 HTML 属性。

### **示例：`attrs` 透传属性**

**父组件**

```vue
<Child class="custom-class" data-id="123" />
```

**子组件**

```vue
<template>
  <div v-bind="attrs">子组件</div>
</template>

<script>
export default {
  setup(props, { attrs }) {
    console.log(attrs); // { class: "custom-class", "data-id": "123" }
    return { attrs };
  }
};
</script>
```

### **📌 解析**

- **`attrs` 只包含未声明的 `props`**（如 `class="custom-class"`）。
- 使用 `v-bind="attrs"` 让 `attrs` 绑定到 `<div>`，避免手动指定。
- **如果组件有 `inheritAttrs: false`，`attrs` 不会自动绑定到根元素**：
    
    ```js
    export default {
      inheritAttrs: false, // ❌ 禁止自动继承
      setup(props, { attrs }) {
        console.log(attrs); // 需要手动绑定
      }
    };
    ```
    

---

## **2.2 `context.slots`（插槽）**

`slots` 用于**获取插槽内容**，支持默认插槽、具名插槽、作用域插槽。

### **示例：使用 `slots` 渲染默认插槽**

**父组件**

```vue
<Child>我是插槽内容</Child>
```

**子组件**

```vue
<template>
  <div>
    <slot />
  </div>
</template>

<script>
export default {
  setup(props, { slots }) {
    console.log(slots); // { default: ƒ }
    return {};
  }
};
</script>
```

### **📌 解析**

- `slots` 是一个对象，默认插槽的内容在 `slots.default` 中。
- `<slot />` 直接渲染插槽内容。

#### **✅ 访问具名插槽**

**父组件**

```vue
<Child>
  <template #header>我是头部</template>
</Child>
```

**子组件**

```vue
<template>
  <div>
    <slot name="header" />
  </div>
</template>

<script>
export default {
  setup(props, { slots }) {
    console.log(slots.header()); // 渲染具名插槽
    return {};
  }
};
</script>
```

- `slots.header()` 返回插槽内容的 VNode。

---

## **2.3 `context.emit`（子组件触发事件）**

`emit` 用于**子组件向父组件传递数据**。

### **示例：子组件触发 `update-message` 事件**

**子组件**

```vue
<template>
  <button @click="sendMessage">发送</button>
</template>

<script>
export default {
  setup(props, { emit }) {
    const sendMessage = () => {
      emit('update-message', 'Hello from Child!');
    };
    return { sendMessage };
  }
};
</script>
```

**父组件**

```vue
<Child @update-message="handleMessage" />

<script>
setup() {
  const handleMessage = (msg) => {
    console.log('收到子组件消息:', msg);
  };
  return { handleMessage };
}
</script>
```

### **📌 解析**

- `emit('update-message', 'Hello from Child!')` 触发自定义事件 `update-message`，并传递参数。
- 父组件使用 `@update-message="handleMessage"` 监听子组件事件。

---

# **3. `setup()` 的完整参数总结**

|参数|作用|适用场景|
|---|---|---|
|`props`|接收父组件的 `props`（响应式但不可修改）|组件通信|
|`context.attrs`|组件传递的**非 `props`** 属性|透传 `class`、`id`|
|`context.slots`|组件插槽|具名插槽 & 作用域插槽|
|`context.emit`|触发自定义事件|子组件向父组件传值|

---

# **4. `setup()` 的进阶写法**

## **4.1 `setup()` + `defineEmits()`（推荐 `script setup`）**

Vue 3 **推荐使用 `<script setup>`**，避免手动解构 `context`：

```vue
<script setup>
import { defineProps, defineEmits, useAttrs, useSlots } from 'vue';

const props = defineProps(['msg']);
const emit = defineEmits(['update-message']);
const attrs = useAttrs();
const slots = useSlots();

const sendMessage = () => {
  emit('update-message', 'Hello Vue!');
};
</script>
```

- `defineProps()` 代替 `setup(props)`
- `defineEmits()` 代替 `context.emit`
- `useAttrs()` 代替 `context.attrs`
- `useSlots()` 代替 `context.slots`

**📌 这样代码更清晰、简洁！**

---

# **总结**

1. `setup(props, { attrs, slots, emit })` 结构赋值获取参数。
2. `props` 是**只读**的，不能修改。
3. `attrs` 透传**非 `props`** 属性，如 `class`。
4. `slots` 获取默认 & 具名插槽内容。
5. `emit` 触发**自定义事件**，让子组件向父组件传递数据。
6. **推荐使用 `<script setup>` + `defineProps()` & `defineEmits()`**，让代码更清晰。

🚀 **Vue 3 推荐 `<script setup>`，让代码更简洁高效！**