## [使用 Toast](https://ui.nuxt.com/composables/use-toast)
https://ui.nuxt.com/components/toast#usage
用于在您的应用中显示 Toast 通知的组合。

## 用法

使用自动导入的 `useToast` 可组合项来显示 [通知。](https://ui.nuxt.com/components/toast)

```vue
<script setup lang="ts">
const toast = useToast()
</script>
```

- `useToast` 可组合项使用 Nuxt 的 `useState` 来管理 toast 状态，确保整个应用程序的反应性。
- 一次最多显示 5 条 Toast。如果添加的新 Toast 超出此限制，则会自动删除最旧的 Toast。
- 删除提示时，在实际从状态中删除之前会有 200 毫秒的延迟，以便退出动画。

确保使用 [App](https://ui.nuxt.com/components/app) 使用我们 [Toaster](https://github.com/nuxt/ui/blob/v3/src/runtime/components/Toaster.vue) 使用 [ToastProvider](https://reka-ui.com/docs/components/toast#provider) 组件。

在 **Toast** 组件文档中了解如何自定义 toast 的外观和行为。

## API

### add(toast: Partial`<Toast>`): Toast

添加新的 Toast 通知。

- 参数：
	- `toast` ：具有以下属性的部分 `Toast` 对象：
		- `id` （可选）：toast 的唯一标识符。若未提供，则使用时间戳。
		- `open` （可选）：toast 是否打开。默认为 `true` 。
		- `Toast` 界面的其他属性。
- 返回：添加的完整的 `Toast` 对象。

```vue
<script setup lang="ts">
const toast = useToast()

function showToast() {
  toast.add({
    title: 'Success',
    description: 'Your action was completed successfully.',
    color: 'success'
  })
}
</script>
```

### update(id: string | number, toast: Partial`<Toast>`)

更新现有的 Toast 通知。

- 参数：
	- `id` ：要更新的 toast 的唯一标识符。
	- `toast` ：具有要更新的属性的部分 `Toast` 对象。

```vue
<script setup lang="ts">
const toast = useToast()

function updateToast(id: string | number) {
  toast.update(id, {
    title: 'Updated Toast',
    description: 'This toast has been updated.'
  })
}
</script>
```

### remove(id: string | number)

删除 Toast 通知。

- 参数：
	- `id` ：要删除的 toast 的唯一标识符。

```vue
<script setup lang="ts">
const toast = useToast()

function removeToast(id: string | number) {
  toast.remove(id)
}
</script>
```

### clear()

删除所有 Toast 通知。

```vue
<script setup lang="ts">
const toast = useToast()

function clearAllToasts() {
  toast.clear()
}
</script>
```

### toasts

- 类型： `Ref<Toast[]>`
- 描述：包含所有当前 Toast 通知的反应数组。

---


# Nuxt UI 中的 Toast 组件使用指南

**安装与启用**：首先通过 NPM/Yarn 安装 Nuxt UI，然后在 `nuxt.config.ts` 中启用模块。例如，使用 Nuxt 提供的 UI 模板初始化项目：

```bash
npx nuxi init -t ui my-app   # 初始化项目（含预配置 @nuxt/ui）
cd my-app && npm install    # 安装依赖
npm run dev                 # 启动开发服务器
```

或者手动安装：在 `nuxt.config.ts` 中添加 `modules: ['@nuxt/ui']` ([安装 - Nuxt UI - Nuxt 框架](https://ui.nuxtjs.org.cn/getting-started/installation/nuxt#:~:text=nuxt))。另外，需要在应用根组件（`app.vue`）使用 `<UApp>` 包裹整个应用，以提供全局配置并确保 Toast 功能正常工作 ([安装 - Nuxt UI - Nuxt 框架](https://ui.nuxtjs.org.cn/getting-started/installation/nuxt#:~:text=app))。配置示例：

```js
// nuxt.config.ts
export default defineNuxtConfig({
  modules: ['@nuxt/ui'],
  app: {
    // 可在此处全局配置 toaster 属性（见下节）
  }
})
```

**使用方法**：在页面或组件的 `<script setup>` 中调用 Nuxt UI 的 `useToast` 组合函数获取 toast 实例，然后使用 `toast.add({...})` 方法创建通知 ([useToast Vue Composable - Nuxt UI](https://ui.nuxt.com/composables/use-toast#:~:text=function%20showToast%28%29%20,color%3A%20%27success%27))。例如：

```vue
<script setup lang="ts">
import { UButton } from 'u-button'  // 假设已自动导入
const toast = useToast()
function showToast() {
  toast.add({
    title: '操作成功',
    description: '您的操作已成功完成。',
    color: 'success'  // 使用 success 类型颜色（默认为 primary）
  })
}
</script>
<template>
  <UButton @click="showToast" label="显示提示" />
</template>
```

上例中，`title` 和 `description` 分别指定通知标题和内容，`color` 指定颜色主题（示例为 success），更多字段请见下文。

**支持类型**：Toast 支持多种语义颜色，可通过 `color` 字段设置通知的类型。可选值包括：`success`（成功，绿）、`error`（错误，红）、`warning`（警告，橙）、`info`（信息，蓝）以及 `primary`、`secondary`、`neutral` 等 ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=))。默认颜色为 `primary`。例如，`color: 'error'` 会显示红色主题的错误通知 ([useToast Vue Composable - Nuxt UI](https://ui.nuxt.com/composables/use-toast#:~:text=function%20showToast%28%29%20,color%3A%20%27success%27)) ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=))。

**自定义属性**：除了标题和描述外，`toast.add` 支持多种自定义选项：

- **图标（icon）**：可通过 `icon: 'i-lucide-xxx'` 指定通知前的图标 ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=%3Cscript%20setup%20lang%3D,icon%3A%20string))。例如 `icon: 'i-lucide-info'`。
    
- **头像（avatar）**：可通过 `avatar: { src: '头像链接', alt: '说明' }` 在通知左侧显示头像（见文档示例）。
    
- **操作按钮（actions）**：可传入 `actions: [{ label: '重试', icon: 'i-lucide-refresh', color: 'neutral', handler: () => { ... } }]` 添加一个或多个按钮操作。
    
- **关闭按钮**：默认显示关闭按钮，可通过 `close: false` 隐藏它，或传入对象自定义其样式（如大小、颜色、变体） ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Close))。
    
- **关闭图标**：使用 `closeIcon` 可替换默认的关闭图标（默认值为 `appConfig.ui.icons.close`） ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Close%20Icon))。
    
- **额外样式**：支持直接设置 `class` 和 `style` 来调整 Toast 根元素的样式。
    

**全局配置**：可以在 Nuxt 应用的全局配置中设置 Toast 的默认行为（通过 `AppConfig` 或 `UApp` 的 `toaster` 属性）。常用选项包括：

- `toaster.position`：通知显示位置，可选 `top-right`、`bottom-right` 等，默认 `bottom-right` ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Change%20global%20position))。
    
- `toaster.duration`：通知持续时间（毫秒），默认值视版本而定，可在配置中修改 ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Change%20global%20duration))。
    
- `toaster.expand`：是否展开堆叠显示（`true` 时一条条展开显示，`false` 时堆叠列表，可在悬停时展开） ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Set%20the%20,component%20to%20display%20stacked%20toasts))。
    

例如，在 `app.vue` 中设置：

```vue
<template>
  <UApp :toaster="{ position: 'bottom-right', duration: 3000, expand: false }">
    <NuxtPage/>
  </UApp>
</template>
```

或者在 `nuxt.config.ts` 中使用 `app` 配置：

```js
export default defineNuxtConfig({
  modules: ['@nuxt/ui'],
  app: {
    toaster: {
      position: 'bottom-right',
      duration: 3000,
      expand: false
    }
  }
})
```

**SSR 支持与注意事项**：Nuxt UI 的 Toast 在 Nuxt 3 默认启用 SSR 情况下可以正常工作。然而，**如果将 Nuxt 的 SSR 设为 `false`（纯客户端渲染）**，目前已知 Toast 可能无法正常显示 ([Notifications (Toast) do not work with NUXT v3.12.1 & SSR disabled · Issue #1871 · nuxt/ui · GitHub](https://github.com/nuxt/ui/issues/1871#:~:text=Description))。这是一个已报告的问题（相关于 Nuxt v3.12+ 的配置），在 SSR 模式下使用则不会出现此问题。建议保持默认的 SSR 模式，或关注后续版本更新以解决此问题。

**示例代码**：以下示例演示了在组件中调用 Toast 和全局配置的完整流程。

```vue
<!-- 示例：在组件中使用 toast -->
<script setup lang="ts">
const toast = useToast()
function notifySuccess() {
  toast.add({
    title: '提示',
    description: '操作成功完成。',
    color: 'success',
    icon: 'i-lucide-check-circle',
    close: { color: 'primary', variant: 'outline', class: 'rounded-full' }
  })
}
</script>
<template>
  <UButton @click="notifySuccess" label="显示成功提示" color="primary" />
</template>
```

```js
// 示例：全局配置 nuxt.config.ts
export default defineNuxtConfig({
  modules: ['@nuxt/ui'],
  app: {
    toaster: {
      position: 'bottom-right',  // 通知弹出位置
      duration: 5000,            // 默认显示 5 秒
      expand: true              // 展开显示模式
    }
  }
})
```

**参考资料**：有关更多选项和高级用法，请参阅 Nuxt UI 官方文档 ([useToast Vue Composable - Nuxt UI](https://ui.nuxt.com/composables/use-toast#:~:text=function%20showToast%28%29%20,color%3A%20%27success%27)) ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Change%20global%20position)) ([Toast Vue Component - Nuxt UI](https://ui.nuxt.com/components/toast#:~:text=Change%20global%20duration)) ([Notifications (Toast) do not work with NUXT v3.12.1 & SSR disabled · Issue #1871 · nuxt/ui · GitHub](https://github.com/nuxt/ui/issues/1871#:~:text=Description))。上述示例展示了常用的配置方法和调用模式。