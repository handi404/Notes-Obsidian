# **📌 Nuxt 中的 Metadata & `useHead` 详解**

在 Nuxt 中，**Metadata（元数据）** 指的是用于描述页面的各种信息，主要包括：`<title>`、`<meta>` 标签、**SEO（搜索引擎优化）** 配置、**Open Graph（OG）标签**、**Twitter 卡片**等。通过元数据，你可以优化页面的可见性、社交媒体分享效果以及提高 SEO 排名。

Nuxt 提供了灵活的 API 来配置和管理这些元数据，本文将详细解析如何配置全局元数据以及如何在页面中自定义元数据。

---

## **🚀 1. 全局配置 Metadata（`app.config.ts`）**

在 Nuxt 中，你可以通过 **`app.config.ts`** 来配置全局的元数据。这样设置后，**所有页面都会默认继承这些配置**，除非在页面中有单独的配置覆盖。

### **✅ 配置全局 Metadata**

1. 在项目根目录下创建或编辑 `app.config.ts` 文件。
2. 使用 `defineAppConfig` API 配置全局的 `title`、`meta` 标签、SEO 信息等。

### **📌 示例：`app.config.ts` 配置**

```ts
// app.config.ts
export default defineAppConfig({
  name: 'My Nuxt App',
  description: '这是一个关于 Nuxt 3 的网站示例',
  seo: {
    title: 'My Nuxt 网站',
    meta: [
      { name: 'author', content: '开发者姓名' },
      { name: 'keywords', content: 'Nuxt, Vue, SEO' },
      { name: 'description', content: '这是一个关于 Nuxt 3 的网站示例，包含丰富的功能。' },
    ],
    og: {
      title: 'Nuxt 3 网站',
      description: '了解 Nuxt 3 的最佳实践和功能。',
      image: '/default-og-image.jpg',
    },
    twitter: {
      card: 'summary_large_image',
      site: '@nuxt_js',
      creator: '@developer',
    }
  }
});
```

**解释：**

- **`title`**: 页面默认的标题。
- **`meta`**: 默认的 meta 标签，用于 SEO 优化，包含 **`author`**、**`keywords`**、**`description`** 等。
- **`og`**: Open Graph 标签，控制社交媒体平台（如 Facebook、LinkedIn）分享时显示的标题、描述、图片等。
- **`twitter`**: Twitter 卡片配置，控制 Twitter 分享时的显示效果。

### **📌 这样做的效果：**

- 当你没有在页面中覆盖配置时，**所有页面都将使用这些默认的 `title` 和 `meta` 标签**。
- 例如，如果你访问 `/about` 页面，默认的标题和 SEO 设置将会是 `My Nuxt 网站` 和相关的描述信息。

---

## **🚀 2. 页面自定义 Metadata（`useHead`）**

如果你希望在特定页面上自定义元数据（如 `title`、`meta`），Nuxt 提供了 `useHead` API。这使你能够在单独页面或组件中设置页面级别的元数据，覆盖全局配置。

### **✅ 使用 `useHead()` 配置页面 Metadata**

`useHead()` 是 Nuxt 提供的一个 hook，用于动态地设置页面头部的元数据。你可以在页面中使用它来设置页面的标题、描述、SEO 配置等信息。

### **📌 示例：页面自定义 `useHead` 配置**

```vue
<script setup>
import { useHead } from '#imports';

useHead({
  title: '关于我们 - My Nuxt App',
  meta: [
    { name: 'description', content: '了解更多关于我们团队的信息' },
    { name: 'keywords', content: 'Nuxt, 关于我们, 团队' }
  ],
  og: {
    title: '关于我们 - My Nuxt App',
    description: '这是关于我们团队的页面，了解我们的背景和经验。',
    image: '/about-og-image.jpg'
  },
  twitter: {
    card: 'summary',
    site: '@nuxt_js',
    creator: '@developer'
  }
});
</script>

<template>
  <div>
    <h1>关于我们</h1>
    <p>这里是关于我们团队的介绍页面。</p>
  </div>
</template>
```

**解释：**

- **`title`**: 这会设置该页面的 `<title>`。
- **`meta`**: 设置页面的 `meta` 标签，如描述和关键词。
- **`og`**: 设置 Open Graph 标签，用于在社交平台分享时自定义显示内容。
- **`twitter`**: 设置 Twitter 卡片信息。

### **📌 这样做的效果：**

- 当用户访问该页面时，浏览器的标题将显示为 `关于我们 - My Nuxt App`，而不是全局默认的 `My Nuxt 网站`。
- 其他自定义的 `meta` 和社交媒体标签（如 `og:title`）也会在页面头部生效，优化页面的 SEO 和社交媒体分享效果。

---

## **🚀 3. 动态修改 Metadata（结合路由）**

有时候，你需要根据页面的内容动态设置元数据。例如，根据路由参数、API 返回的数据等来修改页面的标题或描述。你可以使用 `useHead` 来根据数据动态设置元数据。

### **✅ 动态设置 `title` 和 `meta`**

```vue
<script setup>
import { useHead, useRoute } from '#imports';

const route = useRoute();
const pageTitle = `页面 ID: ${route.params.id}`;

useHead({
  title: pageTitle,
  meta: [
    { name: 'description', content: `这是一篇关于页面 ID ${route.params.id} 的内容` }
  ]
});
</script>

<template>
  <div>
    <h1>{{ pageTitle }}</h1>
    <p>根据 ID 加载内容...</p>
  </div>
</template>
```

**解释：**

- 页面标题和描述基于路由参数动态变化。例如，访问 `/post/123` 时，页面标题将变为 `页面 ID: 123`，描述将变为 `这是一篇关于页面 ID 123 的内容`。

---

## **🚀 4. Open Graph 和 Twitter Card 配置**

Nuxt 允许你通过 `useHead` 动态设置 **Open Graph** 和 **Twitter 卡片** 元数据，帮助优化社交媒体分享。

### **✅ 配置 Open Graph 和 Twitter Card**

```vue
<script setup>
import { useHead } from '#imports';

useHead({
  title: '我的博客文章',
  meta: [
    { name: 'description', content: '阅读我的最新博客文章，了解最新的 Nuxt 3 进展。' }
  ],
  og: {
    title: '我的博客文章',
    description: '了解关于 Nuxt 3 的最新博客文章。',
    image: '/blog-og-image.jpg',
    url: 'https://example.com/blog/my-latest-article'
  },
  twitter: {
    card: 'summary_large_image',
    site: '@nuxt_js',
    creator: '@developer',
    title: '我的博客文章',
    description: '阅读我的最新博客文章，了解 Nuxt 3 的发展。'
  }
});
</script>
```

**解释：**

- **`og` 标签**：控制社交平台（如 Facebook）分享时显示的标题、描述、图片和 URL。
- **`twitter` 标签**：控制 Twitter 分享时的显示效果，使用 `summary_large_image` 卡片样式。

---

## **🎯 总结**

|**配置方式**|**作用**|**示例**|
|---|---|---|
|**全局配置（`app.config.ts`）**|设置全局默认的 `title`、`meta`、SEO 配置|设置 `name`、`description` 等，所有页面默认使用|
|**页面自定义配置（`useHead`）**|设置页面级别的 `title`、`meta`、SEO 配置|根据路由、API 动态修改 `<head>`|
|**Open Graph & Twitter Card**|优化社交媒体分享|`og:title`, `twitter:card`|
|**动态元数据**|根据数据动态修改 `title` 和 `meta`|根据路由参数动态设置|

📌 **💡 Nuxt 提供了灵活的方式来控制页面的元数据，包括全局默认配置和页面级别的自定义配置。使用 `useHead` 和 `defineAppConfig`，你可以轻松优化页面的 SEO 和社交媒体效果。**

---

除了通过 **`useHead`** 和 **`definePageMeta`** 等 API 来动态设置页面的元数据之外，你还可以直接在页面模板中使用 HTML 标签来配置一些静态的元数据。这种方法在简单的场景下非常有效，尤其是当你需要设置 `title`、`meta` 标签时。

在 Nuxt 中，你可以直接在模板中使用 `<head>` 标签。通过在 `<template>` 中加入 `<head>` 标签，Nuxt 会自动将这些内容注入到页面的 `<head>` 部分。

### **✅ 直接使用 HTML 标签来设置 Metadata**

例如，在某些简单的场景下，你可能更喜欢直接在页面模板中声明 `title` 和 `meta` 标签，而不是使用 JavaScript 来动态设置。你可以像传统的 HTML 页面一样，在页面中加入 `<head>` 部分。

### **📌 示例：直接在页面中使用 HTML 标签**

```vue
<template>
  <div>
    <h1>关于我们</h1>
    <p>这是关于我们的页面。</p>
  </div>
</template>

<head>
  <title>关于我们 - 我的 Nuxt 应用</title>
  <meta name="description" content="这是关于我们团队的介绍页面。">
  <meta name="keywords" content="Nuxt, 关于我们, 团队">
  <meta property="og:title" content="关于我们 - 我的 Nuxt 应用">
  <meta property="og:description" content="这是关于我们团队的页面，了解我们的背景和经验。">
  <meta property="og:image" content="/images/about-og-image.jpg">
  <meta name="twitter:card" content="summary_large_image">
</head>
```

### **解释：**

- 在页面模板中，直接使用 `<head>` 标签来设置页面的 `title` 和 `meta` 标签。
- Nuxt 会将这些标签自动注入到 HTML `<head>` 部分，从而达到设置页面元数据的效果。

---

## **🚀 使用 `head` 标签的优缺点**

### **优点：**

- **简单直观**：对于一些静态页面或简单配置，直接使用 HTML 标签的方式直观且容易理解。
- **无需 JavaScript**：无需编写额外的 JavaScript 代码来动态更新元数据，适合那些不需要动态设置的场景。

### **缺点：**

- **缺乏动态能力**：这种方式不支持基于路由或其他动态数据的变化来改变元数据。如果你需要在页面加载时动态修改 `title` 或其他 `meta` 标签，`useHead` 或 `definePageMeta` 是更好的选择。
- **较少灵活性**：这种方法无法像 `useHead` 或 `definePageMeta` 那样与 Vue 的反应式系统配合工作，也无法使用一些额外的功能，如 Open Graph 标签、Twitter 卡片等。

---

## **🎯 总结**

在 Nuxt 中，你可以通过以下几种方式配置页面的元数据：

|**配置方式**|**作用**|**示例**|
|---|---|---|
|**直接使用 `<head>` 标签**|在页面模板中直接声明元数据|`<head><title>...</title><meta ... /></head>`|
|**使用 `useHead()`**|动态设置页面的 `title`、`meta`、`SEO` 配置|在页面中动态修改 `<head>`|
|**使用 `definePageMeta()`**|设置页面的 `layout`、`middleware` 和元数据|用于页面级别的自定义配置|

**💡 结论：**

- **直接使用 `<head>` 标签** 适合简单的场景，当页面不需要根据动态数据改变元数据时非常方便。
- 对于更复杂的需求（如动态修改页面元数据、SEO 配置、社交媒体标签等），推荐使用 **`useHead()`** 和 **`definePageMeta()`**。