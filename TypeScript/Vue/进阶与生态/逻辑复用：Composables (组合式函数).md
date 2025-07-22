探讨 Vue 3 中最优雅、最强大的逻辑复用模式——**Composables (组合式函数)**。这不仅是一种技术，更是一种编写可维护、可扩展 Vue 应用的思维方式。

---

### 逻辑复用：Composables (组合式函数)

#### 1. 什么是 Composable？

**一句话定义**：一个 Composable 是一个**利用 Vue 组合式 API 来封装和复用有状态逻辑的函数**。

让我们拆解这个定义：
*   **它是一个函数**：它就是一个普通的 JavaScript/TypeScript 函数，遵循函数的所有规则。
*   **利用组合式 API**：它的“魔力”来源于其内部可以调用 `ref`, `reactive`, `computed`, `watch`, `onMounted` 等 Vue 的响应式 API。
*   **封装和复用**：它的目的是将某个特定的逻辑（如追踪鼠标位置、获取数据、与 localStorage 交互）连同其相关的**状态**（数据）和**方法**一起打包。
*   **有状态的逻辑 (Stateful Logic)**：这是与普通工具函数（如 `formatDate`）的核心区别。Composable 管理着会随时间变化的状态（例如，鼠标的 `x`, `y` 坐标）。

**约定**：Composable 的函数名通常以 `use` 开头，例如 `useMouse`, `useFetch`。这是一种社区约定，能清晰地表明这个函数会产生有状态的逻辑。

#### 核心价值：为什么它如此重要？

在 Vue 2 中，我们使用 Mixins 来复用逻辑，但这带来了很多问题：
*   **数据来源不清晰**：当一个组件混入多个 Mixin 时，你无法一眼看出组件模板中的某个属性（如 `count`）到底是来自哪个 Mixin，还是组件自身。
*   **命名冲突**：不同的 Mixin 可能会定义同名的属性或方法，导致互相覆盖。
*   **类型推断困难**：TypeScript 很难完美地推断出 Mixin 混合后的最终类型。

**Composable 完美地解决了这些问题：**
*   **来源清晰**：状态和方法都是从 Composable 函数中显式地**解构**出来的。`const { x, y } = useMouse()`，我们明确知道 `x` 和 `y` 来自 `useMouse`。
*   **无命名冲突**：你可以自由地重命名解构出来的变量 `const { x: mouseX, y: mouseY } = useMouse()`。
*   **类型安全**：TypeScript 可以完美地推断 Composable 函数的返回值类型，提供顶级的自动补全和类型检查。

---

### 2. 编写你自己的 Composable

让我们通过几个经典案例来学习如何编写 Composable。

#### 案例一：`useMouse` - 追踪鼠标位置

这是一个典型的、与浏览器 API 交互的例子。

**`src/composables/useMouse.ts`**
```typescript
import { ref, onMounted, onUnmounted, type Ref } from 'vue';

// 定义返回值的类型，方便外部使用
interface MousePosition {
  x: Ref<number>;
  y: Ref<number>;
}

export function useMouse(): MousePosition {
  // 1. 状态 (State): 定义需要被追踪的响应式数据
  const x = ref(0);
  const y = ref(0);

  // 2. 逻辑 (Logic): 定义更新状态的函数
  const update = (event: MouseEvent) => {
    x.value = event.pageX;
    y.value = event.pageY;
  };

  // 3. 副作用管理 (Side Effects): 在 Composable 中使用生命周期钩子
  // 组件挂载时，添加事件监听
  onMounted(() => {
    window.addEventListener('mousemove', update);
  });

  // 组件卸载时，清理事件监听，防止内存泄漏
  onUnmounted(() => {
    window.removeEventListener('mousemove', update);
  });

  // 4. 返回 (Return): 将状态和方法暴露出去
  return { x, y };
}
```
**在组件中使用：**
```vue
<script setup lang="ts">
import { useMouse } from '@/composables/useMouse';

// 每次调用 useMouse() 都会创建一套全新的、独立的 x, y, 和事件监听器
const { x, y } = useMouse();
</script>

<template>
  <div>Mouse position is: {{ x }}, {{ y }}</div>
</template>
```

#### 案例二：`useFetch` - 通用的数据获取

这个例子展示了如何封装异步操作和相关的加载、错误状态。

**`src/composables/useFetch.ts`**
```typescript
import { ref, toValue, watchEffect, type MaybeRef } from 'vue';

// T 是我们期望获取的数据类型
export function useFetch<T>(url: MaybeRef<string>) {
  const data = ref<T | null>(null);
  const error = ref<Error | null>(null);
  const isLoading = ref(true);

  // watchEffect 会自动追踪其依赖 (url)
  // 当 url 变化时，它会重新执行，实现自动重新获取数据
  watchEffect(async () => {
    // 重置状态
    isLoading.value = true;
    error.value = null;
    
    try {
      // toValue 是一个辅助函数，如果 url 是 ref，它会返回 .value，否则返回 url 本身
      const response = await fetch(toValue(url));
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      data.value = await response.json();
    } catch (e) {
      error.value = e as Error;
    } finally {
      isLoading.value = false;
    }
  });

  return { data, error, isLoading };
}
```
**在组件中使用：**
```vue
<script setup lang="ts">
import { ref } from 'vue';
import { useFetch } from '@/composables/useFetch';

interface Post { userId: number; id: number; title: string; body: string; }

const postId = ref(1);
const url = ref(`https://jsonplaceholder.typicode.com/posts/${postId.value}`);

// url 是一个 ref，当 postId 变化导致 url 变化时，useFetch 会自动重新请求
const { data, error, isLoading } = useFetch<Post>(url);

function fetchNextPost() {
  postId.value++;
  url.value = `https://jsonplaceholder.typicode.com/posts/${postId.value}`;
}
</script>

<template>
  <div>
    <button @click="fetchNextPost" :disabled="isLoading">Fetch Next Post</button>
    <div v-if="isLoading">Loading...</div>
    <div v-else-if="error">Error: {{ error.message }}</div>
    <article v-else-if="data">
      <h3>{{ data.title }}</h3>
      <p>{{ data.body }}</p>
    </article>
  </div>
</template>
```

#### 案例三：`useLocalStorage` - 与 `localStorage` 同步

这个例子展示了如何创建一个与外部存储同步的响应式 `ref`。

```typescript
import { ref, watch, type Ref } from 'vue';

export function useLocalStorage<T>(key: string, defaultValue: T): Ref<T> {
  const storedValue = localStorage.getItem(key);
  const value = ref<T>(storedValue ? JSON.parse(storedValue) : defaultValue);

  // 侦听 ref 的变化，并将其写回 localStorage
  watch(value, (newValue) => {
    localStorage.setItem(key, JSON.stringify(newValue));
  }, { deep: true }); // deep: true 确保对象内部属性变化也能被侦听到

  return value;
}
```
**在组件中使用：**
```vue
<script setup lang="ts">
import { useLocalStorage } from '@/composables/useLocalStorage';

// 这个 'settings' ref 会自动与 localStorage 中的 'app-settings' 项保持同步
const settings = useLocalStorage('app-settings', {
  theme: 'light',
  notifications: true,
});
</script>

<template>
  <div>
    Theme: 
    <select v-model="settings.theme">
      <option value="light">Light</option>
      <option value="dark">Dark</option>
    </select>
  </div>
</template>
```

---

### 要点/注意事项

1.  **独立实例**：每次在组件中调用一个 Composable (`useSomething()`)，都会创建一套全新的、独立的响应式状态。组件A的 `useMouse()` 和组件B的 `useMouse()` 互不干扰。

2.  **共享状态**：如果你想在多个组件间共享**同一个**状态实例，你应该将状态提升到 Composable 函数**外部**。这其实就是 Pinia 的基本原理。

    ```typescript
    // src/composables/useSharedState.ts
    import { ref } from 'vue';
    
    // 在函数外部定义状态，它就变成了单例
    const sharedCount = ref(0);
    
    export function useSharedCounter() {
      function increment() {
        sharedCount.value++;
      }
      return { count: sharedCount, increment };
    }
    ```
    现在，任何组件调用 `useSharedCounter()` 都会得到对同一个 `sharedCount` 的引用。

3.  **输入参数**：Composable 也是函数，可以接收参数。这让它们变得极其灵活。`useFetch` 接收 `url` 参数就是一个很好的例子。

4.  **无状态 Composable**：不是所有 `use` 函数都必须返回状态。有些可以只用来注册副作用，例如 `useEventListener`。

5.  **与 `this` 无关**：Composable 内部不依赖 `this` 上下文，这使得它们在逻辑上更纯粹，也更容易进行单元测试。你可以像测试普通 JS 函数一样测试它们。

**总结**：Composables 是 Vue 3 逻辑复用的基石。它鼓励你将复杂的组件逻辑拆分成更小、更专注、可独立测试和复用的功能单元。熟练掌握编写和使用 Composable，是成为一名高级 Vue 开发者的必经之路。