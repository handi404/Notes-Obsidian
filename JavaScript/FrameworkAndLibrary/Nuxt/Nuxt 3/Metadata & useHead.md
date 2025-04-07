如何在 Nuxt 3 中管理页面的 **Metadata (元数据)** 以及如何使用核心的 **`useHead`** 组合式函数。

**什么是 Metadata？**

Metadata 是描述你网页内容的数据，主要放在 HTML 的 `<head>` 部分。它对于以下方面至关重要：

1.  **搜索引擎优化 (SEO)**:
    *   **`<title>`**: 浏览器标签页和搜索结果中显示的标题。
    *   **`<meta name="description" content="...">`**: 页面内容的简短描述，经常显示在搜索结果中。
    *   **`<meta name="keywords" content="...">`**: (现在 SEO 权重很低，但有时仍会使用)。
    *   **`<link rel="canonical" href="...">`**: 指定页面的首选 URL，避免重复内容问题。
    *   **`<meta name="robots" content="...">`**: 指示搜索引擎爬虫如何索引页面 (e.g., `index, follow`, `noindex`).
2.  **社交媒体分享 (Open Graph, Twitter Cards)**:
    *   控制当链接分享到 Facebook, Twitter, LinkedIn 等平台时显示的标题、描述、图片和卡片类型。 (e.g., `<meta property="og:title" content="...">`, `<meta name="twitter:card" content="...">`)
3.  **浏览器行为与外观**:
    *   **`<link rel="icon" href="/favicon.ico">`**: 设置网站图标 (Favicon)。
    *   **`<meta name="viewport" content="...">`**: 控制移动设备上的视口。
    *   **`<meta name="theme-color" content="#ffffff">`**: 设置浏览器 UI (如地址栏) 的主题颜色。
4.  **链接外部资源**:
    *   **`<link rel="stylesheet" href="...">`**: 引入 CSS 样式表。
    *   **`<script src="...">`**: 引入 JavaScript 文件 (虽然 Nuxt 通常有更好的方式管理 JS)。

**Nuxt 3 的解决方案：`useHead` Composable**

Nuxt 3 提供了一个非常强大且灵活的组合式函数 (Composable) —— `useHead`，用于在你的组件（页面、布局或普通组件）内部以**响应式**的方式管理文档的 `<head>` 内容。

*   **核心优势**:
    *   **响应式**: 你可以使用 Vue 的 `ref`, `computed` 等响应式数据来动态更新 `<head>` 标签。当数据变化时，对应的 `<head>` 标签会自动更新。
    *   **组件化管理**: 可以在页面、布局甚至子组件中定义各自的 `<head>` 信息，Nuxt 会智能地合并它们。
    *   **SSR/SSG 友好**: `useHead` 在服务端渲染时就能正确生成 `<head>` 内容，这对 SEO 至关重要。客户端水合后，它继续接管 `<head>` 的管理。
    *   **类型安全**: 与 TypeScript 配合良好。
    *   **基于 `@unhead/vue`**: 底层使用了强大的 `@unhead/vue` 库。

**如何使用 `useHead`：**

你可以在任何 `.vue` 文件的 `<script setup>` 中调用 `useHead`。它接受一个**对象**作为参数，对象的键通常对应 HTML `<head>` 中的标签名或属性。

**基本示例 (设置标题和描述)**

```vue
<!-- pages/about.vue -->
<template>
  <div>
    <h1>关于我们</h1>
    <p>这里是关于页面的内容。</p>
  </div>
</template>

<script setup>
import { useHead } from '#app'; // 或 #imports

useHead({
  title: '关于我们 - 我的 Nuxt 网站', // 设置 <title>
  meta: [
    { name: 'description', content: '了解更多关于我们的信息，以及我们的 Nuxt 应用。' } // 设置 <meta name="description">
  ]
});
</script>
```

**动态更新 Head (使用响应式数据)**

```vue
<!-- pages/posts/[id].vue -->
<template>
  <div>
    <div v-if="pending">加载中...</div>
    <article v-else-if="post">
      <h1>{{ post.title }}</h1>
      <!-- 文章内容 -->
    </article>
  </div>
</template>

<script setup>
import { useRoute } from '#imports';
import { computed } from 'vue'; // 或 #imports

const route = useRoute();
const postId = route.params.id;

// 假设这是通过 useFetch 或 useAsyncData 获取的数据
const { data: post, pending } = await useFetch(() => `/api/posts/${postId}`);

// 使用 computed 来动态生成标题
const pageTitle = computed(() =>
  post.value ? `${post.value.title} - 文章详情` : '加载文章...'
);

// 使用 computed 来动态生成描述
const pageDescription = computed(() =>
  post.value ? post.value.excerpt : '正在加载文章摘要...'
);

useHead({
  title: pageTitle, // 动态标题
  meta: [
    { name: 'description', content: pageDescription } // 动态描述
  ]
});
</script>
```

**管理多个 Meta 标签和 Link 标签**

*   对于可以有多个实例的标签（如 `meta`, `link`, `script`, `style`），你需要传递一个**对象数组**。
*   **`hid` (或 `key`)**: 为了能够覆盖或更新**同类型**的标签（比如多个 `meta` 标签或者 Open Graph 标签），建议为每个标签对象提供一个唯一的标识符 `hid` (Head ID) 或 `key`。当 Nuxt 合并来自不同层级（组件、页面、布局）的 `head` 配置时，拥有相同 `hid` / `key` 的标签会被后面定义的覆盖。

```vue
<script setup>
import { useHead } from '#app';

const postData = { // 假设这是从 API 获取的数据
  title: 'Nuxt 3 真棒',
  description: '探索 Nuxt 3 的 useHead 功能。',
  image: 'https://example.com/images/nuxt-og.png',
  url: 'https://example.com/posts/nuxt-3-awesome'
};

useHead({
  title: postData.title,
  link: [
    { rel: 'canonical', href: postData.url, hid: 'canonical' } // 设置 canonical URL
  ],
  meta: [
    // 基本描述
    { name: 'description', content: postData.description, hid: 'description' },
    // Open Graph (用于 Facebook, LinkedIn 等)
    { property: 'og:title', content: postData.title, hid: 'og:title' },
    { property: 'og:description', content: postData.description, hid: 'og:description' },
    { property: 'og:image', content: postData.image, hid: 'og:image' },
    { property: 'og:url', content: postData.url, hid: 'og:url' },
    { property: 'og:type', content: 'article', hid: 'og:type' },
    // Twitter Card
    { name: 'twitter:card', content: 'summary_large_image', hid: 'twitter:card' },
    { name: 'twitter:title', content: postData.title, hid: 'twitter:title' },
    { name: 'twitter:description', content: postData.description, hid: 'twitter:description' },
    { name: 'twitter:image', content: postData.image, hid: 'twitter:image' },
  ]
});
</script>
```

**其他常用 Head 配置：**

```vue
<script setup>
import { useHead } from '#app';

useHead({
  htmlAttrs: { // 设置 <html> 标签的属性
    lang: 'zh-CN'
  },
  bodyAttrs: { // 设置 <body> 标签的属性
    class: 'theme-dark'
  },
  link: [ // 添加 Link 标签
    { rel: 'icon', type: 'image/png', href: '/favicon-32x32.png', sizes: '32x32' },
    { rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: '' }
  ],
  script: [ // 添加 Script 标签 (小心使用，通常有更好的方式集成第三方库)
    // { src: 'https://third-party.com/script.js', async: true, defer: true, body: true }, // body: true 会尝试将脚本放在 body 尾部
    // 添加 JSON-LD 结构化数据
    {
      type: 'application/ld+json',
      hid: 'ld-json-schema',
      innerHTML: JSON.stringify({
        '@context': 'https://schema.org',
        '@type': 'WebSite',
        url: 'https://example.com/',
        name: '我的 Nuxt 网站'
      })
    }
  ],
  style: [ // 直接内联样式 (不常用)
    // { children: 'body { color: red; }', type: 'text/css' }
  ]
});
</script>
```

**全局 Head 配置 (`nuxt.config.ts`)**

你可以在 `nuxt.config.ts` 中设置**全局默认**的 `<head>` 信息。这些信息会应用于所有页面，但可以被页面或布局中的 `useHead` 配置**覆盖**（如果 `hid` / `key` 相同）或**补充**。

```typescript
// nuxt.config.ts
export default defineNuxtConfig({
  app: {
    head: {
      charset: 'utf-8',
      viewport: 'width=device-width, initial-scale=1',
      title: '我的 Nuxt 应用', // 默认标题
      titleTemplate: '%s - 我的 Nuxt 应用', // 标题模板，%s 会被页面设置的 title 替换
      meta: [
        { name: 'description', content: '这是一个使用 Nuxt 3 构建的很棒的应用。' },
        { name: 'theme-color', content: '#3498db' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ],
      // 全局脚本或样式也可以在这里添加
    }
  }
})
```

**`titleTemplate`**:
`titleTemplate` 非常有用。如果页面 `useHead` 设置了 `title: '关于我们'`，并且 `nuxt.config.ts` 中有 `titleTemplate: '%s - 我的网站'`，那么最终渲染的标题将是 `"关于我们 - 我的网站"`。如果页面没有设置 `title`，则会使用 `nuxt.config.ts` 中 `head.title` 的默认值。

**总结:**

*   使用 `useHead` 组合式函数在 Nuxt 3 组件（页面、布局等）中管理 `<head>` 元数据。
*   它是响应式的，与 Vue 的 `ref`, `computed` 配合良好。
*   通过传递对象给 `useHead`，对象的键对应 `<head>` 标签或属性。
*   使用数组和 `hid` (或 `key`) 来管理多个同类型标签（如 `meta`, `link`）并处理覆盖。
*   在 `nuxt.config.ts` 的 `app.head` 中设置全局默认值和 `titleTemplate`。
*   `useHead` 对 SEO 和社交媒体分享至关重要，并且与 SSR/SSG 无缝集成。

掌握 `useHead` 是构建专业、SEO 友好的 Nuxt 3 应用的关键一步！