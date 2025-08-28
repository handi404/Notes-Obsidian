`v-if`, `v-show` 控制的是是否渲染，而真正的性能瓶颈，往往在于是否需要重新渲染。

面对这个问题， `v-if` 和 `v-show` 显得有些无力，而 Vue3 有一个专为解决此问题的指令—— `v-memo` 。

### 什么是 v-memo？

`v-memo` 是一个指令，作用是有条件地跳过一个元素或组件的更新，语法如下：

```vue
<div v-memo="[depA, depB]">
  ...
</div>
```

`v-memo` 接收一个依赖数组，只有当数组中至少一个值与上次渲染相比发生了变化，Vue 才会重新渲染这个 `div` 及其子节点。否则，将跳过 diff 过程。

### v-memo 的使用场景

`v-memo` 不是用来替代 `v-if` 或 `v-show` 的，它是专门解决那些令人棘手的渲染性能问题。

其最经典、最有效的应用场景就是优化超长 `v-for` 列表，假设有一个包含 1000 个用户的列表，每个用户都有 `name` 和 `status` （在线/离线）两个属性：

**没有 `v-memo` 的版本：**

```vue
<template>
  <div v-for="user in users" :key="user.id">
    <p>{{ user.name }}</p>
    <p :class="user.status === 'online' ? 'green' : 'grey'">
      {{ user.status }}
    </p>
  </div>
</template>
```

问题在于，如果其中一个用户的 `status` 发生改变，Vue 理论上需要遍历整个列表，为每个节点创建新的 VNode 并 diff。

**使用 `v-memo` 的版本：**

```vue
<template>
  <div v-for="user in users" :key="user.id" v-memo="[user.status]">
    <p>{{ user.name }}</p>
    <p :class="user.status === 'online' ? 'green' : 'grey'">
      {{ user.status }}
    </p>
  </div>
</template>
```

需要注意的是 `v-memo="[user.status]"` 。

当某个用户的 `status` 改变时，只有他对应的 `div` 的 `v-memo` 依赖项发生了变化，于是只有这个 `div` 会被重新渲染。Vue 会直接跳过其他 999 个用户的整个更新过程。

通过一行代码，我们将 O(n) 的更新检查开销，降低到了 O(1)（只更新变化的那一项）。

### 对比 v-once

`v-once` 本质上就是 `v-memo` 的一个特例， `v-once` 等同于 `v-memo="[]"` 。因为依赖数组是空的，它永远不会改变，所以组件只会在首次渲染后被永久“冻结”。

`v-if` 和 `v-show` 的争论，解决的是“有或无”的渲染问题，但现代前端应用中，更大的性能挑战来自于“多与少”的更新问题。