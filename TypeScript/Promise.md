### Promise（异步编程的核心）  
**通俗理解**：Promise 是 JavaScript 中处理异步操作的「未来值容器」，它像一张「承诺书」，告诉你「这个操作要么成功（fulfilled）返回结果，要么失败（rejected）抛出错误」。结合 TypeScript 后，Promise 的类型注解能让你提前知道「未来值的类型」，避免运行时错误。

---

#### 1. **基本概念**  
- **状态（State）**：Promise 有三种状态：  
  - **Pending（等待中）**：初始状态，尚未完成。  
  - **Fulfilled（已成功）**：操作成功，返回结果。  
  - **Rejected（已失败）**：操作失败，抛出错误。  

- **生命周期**：  
  状态只能从 `Pending` → `Fulfilled` 或 `Pending` → `Rejected`，一旦确定，不可逆。

---

#### 2. **创建 Promise**  
```ts
const fetchData = new Promise<string>((resolve, reject) => {
  // 模拟异步请求
  setTimeout(() => {
    const success = true;
    if (success) {
      resolve("Data received"); // 类型为 string
    } else {
      reject(new Error("Failed to fetch")); // 类型为 Error
    }
  }, 1000);
});
```

**类型注解**：  
- `Promise<string>` 表示 `resolve` 的值是 `string` 类型。  
- 如果未指定泛型，默认为 `Promise<unknown>`（需手动处理类型）。

---

#### 3. **链式调用（Thenable）**  
- **`.then()`**：处理成功状态，返回新值或新 Promise。  
- **`.catch()`**：处理失败状态。  
- **`.finally()`**：无论成功或失败都会执行（常用于清理资源）。  

```ts
fetchData
  .then((data: string) => {
    console.log(data); // "Data received"
    return data.length; // 返回 number 类型
  })
  .then((length: number) => {
    console.log(`Length: ${length}`); // Length: 13
  })
  .catch((error: Error) => {
    console.error(error.message);
  });
```

**类型推断规则**：  
- `.then()` 的返回值类型会自动推断为下一个 `.then()` 的参数类型。  
- 如果未显式注解类型，TS 会根据上下文尝试推断。

---

#### 4. **Promise 静态方法**  
- **`Promise.resolve(value)`**：创建一个已解决的 Promise。  
  ```ts
  const p1 = Promise.resolve<number>(42); // 类型为 Promise<number>
  ```

- **`Promise.reject(error)`**：创建一个已拒绝的 Promise。  
  ```ts
  const p2 = Promise.reject<string>(new Error("Oops!")); // 类型为 Promise<string>
  ```

- **`Promise.all(promises)`**：等待所有 Promise 完成，返回数组。  
  ```ts
  const p3 = Promise.all([Promise.resolve(1), Promise.resolve("a")]);
  // 类型为 Promise<[number, string]>
  ```

- **`Promise.race(promises)`**：返回第一个完成（无论成功或失败）的 Promise。  
  ```ts
  const p4 = Promise.race([Promise.resolve(1), Promise.reject("Error")]);
  // 类型为 Promise<number | string>
  ```

- **`Promise.allSettled(promises)`**：等待所有 Promise 结束（无论成功或失败）。  
  ```ts
  const p5 = Promise.allSettled([Promise.resolve(1), Promise.reject("Error")]);
  // 类型为 Promise<({ status: "fulfilled", value: number } | { status: "rejected", reason: string })[]>
  ```

---

#### 5. **结合 `async/await`**  
- **`async` 函数**：自动返回 Promise，内部可以使用 `await` 等待异步操作。  
- **`await` 表达式**：暂停函数执行，直到 Promise 解决。  

```ts
async function getData(): Promise<string> {
  try {
    const data = await fetchData; // 类型为 string（自动推断）
    return `Result: ${data}`;
  } catch (error) {
    // error 类型为 unknown，需显式类型守卫
    if (error instanceof Error) {
      throw new Error(`Caught error: ${error.message}`);
    }
    throw error;
  }
}
```

**类型注解规则**：  
- `async` 函数返回类型必须是 `Promise<T>`，其中 `T` 是函数体中 `return` 的值类型。  
- `await` 表达式的类型由 Promise 的泛型决定（如 `await Promise.resolve<number>(42)` 类型为 `number`）。

---

#### 6. **错误处理（Error Handling）**  
- **`.catch()`**：捕获链式调用中的错误。  
- **`try/catch`**：在 `async/await` 中使用更直观。  
- **最佳实践**：  
  - 显式注解错误类型（如 `Error`）。  
  - 使用类型守卫（`instanceof Error`）确保类型安全。  

```ts
try {
  await fetchData;
} catch (error: unknown) {
  if (error instanceof Error) {
    console.error(error.message); // 类型为 string
  } else {
    console.error("Unknown error");
  }
}
```

---

#### 7. **类型推断与显式注解**  
- **自动推断**：TS 会根据 `resolve` 和 `reject` 的参数推断类型。  
  ```ts
  const p = new Promise((resolve, reject) => {
    resolve("Hello"); // 推断为 Promise<string>
  });
  ```

- **显式注解**：避免隐式 `any` 或复杂类型推断错误。  
  ```ts
  const p = new Promise<string>((resolve, reject) => {
    resolve("Hello"); // 显式注解为 string
  });
  ```

- **工具类型**：  
  - `ReturnType<typeof fn>`：提取函数返回的 Promise 类型。  
  - `Awaited<T>`（TS 4.5+）：提取 Promise 解析后的类型。  
    ```ts
    type Result = Awaited<Promise<string>>; // 类型为 string
    ```

---

#### 8. **常见陷阱与解决方案**  
- **未处理的 Promise 拒绝**：  
  - 启用 `noImplicitAny` 和 `strict` 模式，强制类型检查。  
  - 使用 `try/catch` 或 `.catch()` 捕获错误。  

- **错误类型未明确**：  
  ```ts
  catch (error) {
    // ❌ error 类型为 unknown
    console.log(error.message); // 报错！
  }
  ```
  **解决方案**：使用类型守卫或强制注解类型。  

- **Promise 链中断**：  
  ```ts
  fetchData.then((data) => {
    return data.length; // ✅ 正确链式调用
  }).then((length) => { ... });
  ```
  **错误示例**：忘记 `return` 会导致链式中断。  

---

#### 9. **最佳实践总结**  
| 场景 | 建议 |
|------|------|
| 创建 Promise | 显式注解泛型类型（如 `Promise<number>`）。 |
| 链式调用 | 使用 `.then().catch()` 明确处理流程。 |
| 异步函数 | 优先使用 `async/await` 提升可读性。 |
| 错误处理 | 结合 `try/catch` 和类型守卫确保类型安全。 |
| 多 Promise 并发 | 使用 `Promise.all` 或 `Promise.allSettled`。 |
| 调试与测试 | 通过 `jest` 或 `vitest` 模拟 Promise 行为。 |

---

#### 10. **进阶用法**  
- **生成器函数与 Promise 结合**：通过 `co` 库或自定义运行器实现异步流程控制。  
- **可观察对象（Observables）**：使用 RxJS 替代 Promise 实现更复杂的异步逻辑（如取消请求、重试）。  
- **类型安全的 HTTP 客户端**：结合 `axios` 或 `fetch`，通过泛型明确接口返回类型。  

---

### Promise 与 Vue 生命周期、Pinia 状态管理、Web Workers 的结合  
**通俗理解**：Promise 是异步操作的核心工具，与 Vue 的生命周期、Pinia 状态管理和 Web Workers 结合时，能实现更高效的异步逻辑。结合 TypeScript 的类型系统后，可以确保异步操作的类型安全性和可维护性。

---

#### 1. **Promise 与 Vue 生命周期**  
**场景**：在组件挂载时发起异步请求（如 API 调用），并根据结果更新 UI 或处理加载状态。  

##### **示例代码**  
```ts
<script setup lang="ts">
import { ref, onMounted } from 'vue';

const data = ref<string | null>(null);
const loading = ref<boolean>(true);
const error = ref<Error | null>(null);

onMounted(async () => {
  try {
    const response = await fetchData(); // 异步请求
    data.value = response;
  } catch (err) {
    // 显式类型守卫
    if (err instanceof Error) {
      error.value = err;
    } else {
      error.value = new Error('Unknown error');
    }
  } finally {
    loading.value = false;
  }
});

// 类型注解的 Promise
function fetchData(): Promise<string> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve('Data from API');
      // reject(new Error('Network error')); // 测试错误
    }, 1000);
  });
}
</script>
```

##### **关键点**  
- **生命周期钩子中的异步逻辑**：  
  - 使用 `onMounted`、`onBeforeUnmount` 等钩子管理异步操作的生命周期，避免内存泄漏。  
  - 在 `setup` 语法糖中，直接使用 `async` 会导致组件变成 `Promise`（需配合 `Suspense` 使用）。  

- **加载状态与错误处理**：  
  - 使用 `loading` 和 `error` 状态控制 UI 反馈（如加载动画、错误提示）。  
  - 显式注解错误类型（如 `Error`），并通过类型守卫确保类型安全。  

---

#### 2. **Promise 与 Pinia 状态管理**  
**场景**：在 Pinia Store 中定义异步 action（如 API 请求），并通过 Promise 返回结果，组件中调用时保持类型一致。  

##### **示例代码**  
```ts
// stores/userStore.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
  const user = ref<{ name: string; age: number } | null>(null);
  const loading = ref<boolean>(false);
  const error = ref<Error | null>(null);

  // 异步 action 返回 Promise
  async function fetchUser(id: number): Promise<void> {
    loading.value = true;
    try {
      const response = await fetch(`https://api.example.com/users/${id}`);
      if (!response.ok) throw new Error('Network response was not ok');
      user.value = await response.json();
    } catch (err) {
      if (err instanceof Error) {
        error.value = err;
      } else {
        error.value = new Error('Unknown error');
      }
    } finally {
      loading.value = false;
    }
  }

  return { user, loading, error, fetchUser };
});
```

##### **组件中使用**  
```ts
<script setup lang="ts">
import { useUserStore } from '@/stores/userStore';

const userStore = useUserStore();

// 调用异步 action
userStore.fetchUser(1).then(() => {
  console.log('User data loaded:', userStore.user);
}).catch((err) => {
  console.error('Failed to load user:', err);
});
</script>
```

##### **关键点**  
- **Pinia Action 的类型注解**：  
  - 异步 action 必须返回 `Promise<void>` 或 `Promise<T>`，确保调用时能使用 `.then()` 或 `await`。  
  - 使用 `ref` 管理状态（如 `loading`、`error`），在组件中直接响应式绑定。  

- **错误处理与类型安全**：  
  - 在 Pinia 中捕获错误并更新 `error` 状态，组件中统一处理错误提示。  
  - 显式注解响应类型（如 `Promise<{ name: string; age: number }>`）。  

---

#### 3. **Promise 与 Web Workers**  
**场景**：在 Web Worker 中执行耗时计算（如数据处理、图像渲染），通过 Promise 与主线程通信。  

##### **示例代码**  
**Worker 脚本（worker.ts）**  
```ts
// 显式类型注解：接受数字数组，返回平均值
onmessage = (event: MessageEvent<number[]>) => {
  const data = event.data;
  const average = data.reduce((sum, num) => sum + num, 0) / data.length;
  // 返回 Promise 化的结果
  postMessage(average);
};
```

**主线程调用**  
```ts
<script setup lang="ts">
import { ref } from 'vue';

const result = ref<number | null>(null);
const loading = ref<boolean>(false);

// 封装 Worker 为 Promise
function runWorker(data: number[]): Promise<number> {
  return new Promise((resolve, reject) => {
    const worker = new Worker(new URL('./worker.ts', import.meta.url), { type: 'module' });
    worker.postMessage(data);
    worker.onmessage = (event) => {
      resolve(event.data as number);
      worker.terminate(); // 完成后终止 Worker
    };
    worker.onerror = (error) => {
      reject(error);
      worker.terminate();
    };
  });
}

// 调用 Worker
async function calculateAverage() {
  loading.value = true;
  try {
    const avg = await runWorker([1, 2, 3, 4, 5]);
    result.value = avg;
  } catch (err) {
    console.error('Worker error:', err);
  } finally {
    loading.value = false;
  }
}
</script>
```

##### **关键点**  
- **Worker 通信的类型安全**：  
  - 使用 `MessageEvent<T>` 显式注解消息类型（如 `number[]`）。  
  - 主线程通过 `postMessage(data)` 发送数据，Worker 通过 `onmessage` 处理。  

- **Promise 封装 Worker**：  
  - 将 `Worker` 的异步通信封装为 `Promise`，简化主线程调用逻辑。  
  - 使用 `terminate()` 避免内存泄漏，确保 Worker 仅在需要时运行。  

---

#### 4. **综合场景：Pinia + Web Workers**  
**场景**：在 Pinia Store 中管理 Web Worker 任务，实现全局异步计算。  

##### **示例代码**  
```ts
// stores/workerStore.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useWorkerStore = defineStore('worker', () => {
  const result = ref<number | null>(null);
  const loading = ref<boolean>(false);
  const error = ref<Error | null>(null);

  // 封装 Worker 为异步 action
  async function calculateAverage(data: number[]): Promise<void> {
    loading.value = true;
    try {
      const worker = new Worker(new URL('./worker.ts', import.meta.url), { type: 'module' });
      worker.postMessage(data);
      return new Promise((resolve, reject) => {
        worker.onmessage = (event) => {
          result.value = event.data;
          worker.terminate();
          resolve();
        };
        worker.onerror = (err) => {
          error.value = err;
          worker.terminate();
          reject(err);
        };
      });
    } catch (err) {
      error.value = err instanceof Error ? err : new Error('Unknown error');
      throw err;
    } finally {
      loading.value = false;
    }
  }

  return { result, loading, error, calculateAverage };
});
```

##### **组件中调用**  
```ts
<script setup lang="ts">
import { useWorkerStore } from '@/stores/workerStore';

const workerStore = useWorkerStore();

workerStore.calculateAverage([1, 2, 3, 4, 5])
  .then(() => {
    console.log('Result:', workerStore.result);
  })
  .catch((err) => {
    console.error('Calculation failed:', err);
  });
</script>
```

---

#### 5. **最佳实践总结**  
| 场景 | 建议 |
|------|------|
| Vue 生命周期 | 在生命周期钩子中使用 `async/await`，避免直接在 `setup` 中使用 `async`（除非搭配 `Suspense`）。 |
| Pinia 异步操作 | 在 Store 的 action 中返回 `Promise`，结合 `ref` 管理状态（如 `loading`、`error`）。 |
| Web Workers | 通过 `Promise` 封装 Worker 通信，显式注解消息类型（如 `MessageEvent<T>`）。 |
| 错误处理 | 统一使用 `try/catch` 或 `.catch()`，结合类型守卫确保错误类型安全。 |
| 类型推断 | 显式注解 Promise 泛型（如 `Promise<string>`），避免隐式 `any`。 |

---

#### 6. **常见问题**  
- **Q：在 `setup` 中使用 `async` 会导致什么问题？**  
  A：组件会被包裹为 `Promise`，导致无法正常渲染。需使用 `Suspense` 组件或改用生命周期钩子（如 `onMounted`）。

- **Q：如何确保 Worker 的消息类型安全？**  
  A：使用 `MessageEvent<T>` 注解消息类型，并在主线程和 Worker 中保持类型一致。

- **Q：如何取消未完成的 Promise？**  
  A：使用 `AbortController` 或封装可取消的 Promise，避免内存泄漏（例如：在 `onBeforeUnmount` 中取消请求）。

---
