在 Vue 3 组合式 API (`setup()`) 中，`emit` 需要通过 **函数参数解构** 来获取，常见的写法如下：

```vue
setup(props, { emit }) { 
  emit('my-event', 'some data'); 
}
```

这是因为 `setup()` 函数默认接收两个参数：

1. `props`（用于接收父组件传递的 `props`）
2. **`context`（上下文对象）**，包含：
    - `emit`（触发事件）
    - `slots`（插槽）
    - `attrs`（非 `props` 绑定的 attribute）

---

## **1. `emit` 不能直接通过 `this` 获取**

在 Vue 2 选项式 API (`Options API`) 中，`this.$emit()` 用于触发事件：

```vue
methods: {
  sendMessage() {
    this.$emit('update-message', 'Hello Vue!');
  }
}
```

但在 Vue 3 **组合式 API (`setup()`)** 中，`this` **不存在**，所有的响应式数据和方法都是局部作用域变量，因此 `emit` 不能直接通过 `this` 访问，而是要通过 `setup()` 的第二个参数 `context` 获取。

---

## **2. `{ emit }` 是结构赋值**

在 Vue 3 `setup()` 函数中，第二个参数是 `context`，包含 `emit`、`slots` 和 `attrs`：

```js
setup(props, context) {
  console.log(context); // { emit: ƒ, slots: {}, attrs: {} }
  context.emit('my-event', 'data');
}
```

为了**简化代码**，通常使用 **解构赋值**：

```js
setup(props, { emit }) {
  emit('my-event', 'data'); 
}
```

这样 `emit` 变量可以直接使用，而不用 `context.emit()`。

---

## **3. 在 `<script setup>` 语法中**

Vue 3 提供了更简洁的 `<script setup>` 语法，直接使用 `defineEmits()` 获取 `emit`：

```vue
<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['update-message']);

const sendMessage = () => {
  emit('update-message', 'Hello Vue!');
};
</script>
```

这里 `defineEmits()` 作用类似于 `setup(_, { emit })`，但**更清晰和直观**。

---

## **总结**

|方式|适用场景|代码简洁度|
|---|---|---|
|`setup(props, { emit })`|组合式 API，适用于 `export default { setup() }` 组件|⭐⭐⭐|
|`defineEmits()`|`<script setup>` 语法，推荐|⭐⭐⭐⭐⭐|

**推荐**：

- 如果使用**普通 `setup()`** → `setup(props, { emit })`
- 如果使用**`<script setup>`** → `defineEmits()` ✅（更清晰）

🚀 **推荐 Vue 3 开发使用 `<script setup>` 语法，让代码更简洁！**