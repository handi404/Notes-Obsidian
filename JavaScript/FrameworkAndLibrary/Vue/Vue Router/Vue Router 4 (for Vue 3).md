## 入门与核心概念
**第一阶段：入门与核心概念**

这个阶段的目标是让你从零开始，搭建一个最基础的多页面应用，并深刻理解 Vue Router 的核心工作模式。

---

### **1. 思想准备：为什么需要前端路由？**

想象一下传统的网站，比如一个老式的新闻网站。你点击一个新闻标题，整个浏览器会闪一下，页面重新加载，服务器返回一个全新的 HTML 页面。这叫 **多页面应用 (MPA)**。

现在，想象一个现代的 Web 应用，比如网易云音乐的网页版。你在“发现音乐”和“我的音乐”之间切换时，注意到没有？只有中间的内容区域在变，顶部的播放器和侧边的菜单栏都纹丝不动。这就是 **单页面应用 (SPA)** 的魅力。

*   **痛点**: 如果每次切换页面都重新加载所有东西（页头、页脚、侧边栏），既慢又浪费资源，用户体验差。
*   **Vue Router 的作用**: 它就是这个 SPA 的 **“交通指挥官”** 或 **“内容调度员”**。它会监听浏览器地址栏的变化，然后像遥控器换台一样，只把主要内容区域的组件换掉，而其他部分保持不变。这让应用感觉像一个流畅的桌面程序。

**总结一句话：Vue Router 让我们在不刷新整个网页的前提下，实现页面内容的切换。**

---

### **2. 动手实践：从零搭建你的第一个路由应用**

我们来创建一个包含“首页”和“关于”两个页面的应用。

**前提：** 你需要一个基础的 Vue 3 项目。如果没有，可以通过以下命令快速创建一个：

```bash
npm create vue@latest
```
在创建过程中，它会询问你是否要添加 Vue Router，你可以选择 **Yes**。但为了学习，我们这里假设你选了 **No**，我们来手动配置它。

#### **第 1 步：安装 Vue Router**

在你的项目根目录下打开终端，运行：

```bash
npm install vue-router@4
```

#### **第 2 步：创建并配置路由**

这是最核心的一步。最佳实践是为路由创建一个单独的目录和文件。

1.  在 `src` 目录下，新建一个 `router` 文件夹。
2.  在 `src/router` 文件夹下，新建一个 `index.js` 文件。

现在，编辑 `src/router/index.js` 文件，写入以下内容：

```javascript
// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue' // 引入你的页面组件

// 1. 定义路由表（routes）
// 每一条路由规则都是一个对象，至少包含 path 和 component
const routes = [
  {
    path: '/',          // URL路径
    name: 'home',       // 路由名称（可选，但推荐）
    component: HomeView // 该路径对应的组件
  },
  {
    path: '/about',
    name: 'about',
    // 这是一种特殊的写法，叫“路由懒加载”
    // 它只会在用户访问 /about 路径时，才去加载 AboutView.vue 组件的代码
    // 这对于性能优化非常重要，我们先这么写，后面会详细讲解
    component: () => import('../views/AboutView.vue')
  }
]

// 2. 创建路由实例
const router = createRouter({
  // history 模式：使用 createWebHistory() 可以让 URL 看起来更“正常”，没有 #
  // 例如：http://localhost:5173/about
  history: createWebHistory(),
  routes, // 把我们定义的路由表放进来
})

// 3. 导出路由实例
export default router
```

**代码解释:**
*   `createRouter`: 创建路由实例的核心函数。
*   `createWebHistory`: 路由的“历史模式”。它利用了浏览器的 History API，让你的URL看起来像传统网站一样（`example.com/about`），而不是带一个 `#`（`example.com/#/about`，这是 HASH 模式）。`createWebHistory` 是目前的首选。
*   `routes`: 一个数组，你的应用的“导航地图”。`path` 是地址，`component` 是要显示的“目的地”（组件）。

#### **第 3 步：创建页面组件**

上面的配置中，我们引用了 `HomeView.vue` 和 `AboutView.vue`，现在来创建它们。

1.  在 `src` 目录下，新建一个 `views` 文件夹（约定俗成，存放页面级组件）。
2.  创建 `src/views/HomeView.vue`:
    ```vue
    <script setup>
    // 这里可以写 JS 逻辑
    </script>

    <template>
      <h1>这里是首页</h1>
      <p>欢迎来到我的第一个 Vue Router 应用！</p>
    </template>
    ```
3.  创建 `src/views/AboutView.vue`:
    ```vue
    <script setup>
    </script>

    <template>
      <h1>关于我们</h1>
      <p>这是一个用于学习 Vue Router 的页面。</p>
    </template>
    ```

#### **第 4 步：在 Vue 应用中“启用”路由**

我们创建了路由实例，但 Vue 还不知道它的存在。需要去应用的入口文件 `src/main.js` 里告诉它。

打开 `src/main.js`，修改成这样：

```javascript
// src/main.js

import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 引入我们刚刚创建的 router

const app = createApp(App)

app.use(router) //  告诉 Vue 应用，使用这个路由规则

app.mount('#app')
```
`app.use(router)` 这行代码至关重要，它就像把“导航系统”的芯片插入了你汽车的中控台。

#### **第 5 步：提供导航链接和内容显示区**

万事俱备，只差在主界面上添加入口了。打开根组件 `src/App.vue`，这是所有页面的“外壳”。

清空 `App.vue` 的内容，替换为：

```vue
<script setup>
// 这里引入了 Vue Router 提供的两个核心组件
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <nav>
      <!-- RouterLink 是 Vue Router 提供的组件，用于生成导航链接 -->
      <!-- 它会被渲染成一个 <a> 标签，但它会阻止浏览器默认的刷新行为 -->
      <RouterLink to="/">首页</RouterLink>
      <RouterLink to="/about">关于</RouterLink>
    </nav>
  </header>

  <main>
    <!-- RouterView 是一个占位符 -->
    <!-- Vue Router 会根据当前 URL，把匹配到的组件渲染在这里 -->
    <RouterView />
  </main>
</template>

<style scoped>
/* 加一点简单的样式，让链接看起来更清晰 */
nav {
  padding: 20px;
  background-color: #f0f0f0;
  border-bottom: 1px solid #ccc;
}
nav a {
  margin-right: 15px;
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
}
/* Vue Router 会为当前激活的链接添加一个 .router-link-active 类 */
nav a.router-link-active {
  color: #42b983;
}
main {
  padding: 20px;
}
</style>
```

**代码解释:**
*   **`<RouterLink>`**: 你的“导航按钮”。它的 `to` 属性指定了目标路径。**千万不要用普通的 `<a>` 标签**，否则页面会刷新，SPA 就没意义了。
*   **`<RouterView>`**: 你的“内容显示区”。它像一个舞台，`HomeView` 或 `AboutView` 会根据你点击的链接，被动态地放到这个舞台上表演。

---

### **验收成果**

现在，启动你的项目：

```bash
npm run dev
```

在浏览器中打开显示的地址（通常是 `http://localhost:5173/`）。

你应该能看到：
1.  顶部有一个包含“首页”和“关于”的导航栏。
2.  下方内容区显示着“这里是首页”。
3.  点击“关于”链接，地址栏变为 `/about`，内容区平滑地切换为“关于我们”的内容，**整个页面没有刷新**！

你已经成功地完成了 Vue Router 的入门，并掌握了它的四大核心：

1.  **`createRouter`**: 创建路由实例。
2.  **`routes` 配置**: 定义你的“导航地图”。
3.  **`<RouterLink>`**: 创建导航链接。
4.  **`<RouterView>`**: 渲染匹配到的组件。


## 进阶核心功能
我们的“导航系统”已经能处理固定路线了。现在，让我们给它升级，让它能处理动态、复杂的路线，并且能在代码中直接命令它去往何方。

**第二阶段：进阶核心功能**

这个阶段，我们将学习在真实项目中不可或缺的三个核心功能：动态路由、编程式导航和命名路由。

---

### **1. 动态路由匹配：处理不固定的 URL**

很多时候，我们的 URL 并不是固定的，比如 `/users/1`, `/users/2` 或者 `/products/iphone`, `/products/macbook`。这些 URL 的模式是相同的，只是末尾的 ID 或名称不同。动态路由就是用来解决这个问题的。

**通俗比喻**: 想象你有一个联系人列表。你不会为每个联系人（张三、李四）都创建一个单独的页面。你会创建一个通用的“联系人详情”模板，然后根据你点击的联系人，往这个模板里填充不同的数据。动态路由就是帮你匹配到这个“通用模板”的。

#### **动手实践：创建用户详情页**

我们来扩展之前的项目，增加一个用户列表，点击用户后可以查看详情。

**第1步：修改路由配置 (`src/router/index.js`)**

在 `routes` 数组中添加一条新的路由规则：

```javascript
// src/router/index.js

// ... (之前的代码)

const routes = [
  // ... (首页和关于页的路由)
  {
    path: '/users', // 用户列表页
    name: 'user-list',
    component: () => import('../views/UserListView.vue')
  },
  {
    // 这就是动态路由！冒号(:)后面跟一个参数名 (id)
    path: '/users/:id',
    name: 'user-detail',
    component: () => import('../views/UserDetailView.vue')
  }
]

// ... (创建和导出 router 的代码)
```
*   `:id` 就是一个 **动态片段 (dynamic segment)**，也叫 **路由参数 (route param)**。它会匹配 `/users/` 后面跟着的任何字符串，比如 `/users/1`, `/users/abc` 等。这个匹配到的值，我们可以在组件中获取到。

**第2步：创建用户列表页 (`UserListView.vue`)**

这个页面会展示一个用户列表，并提供链接到他们的详情页。

创建 `src/views/UserListView.vue`:
```vue
<script setup>
import { RouterLink } from 'vue-router'

// 假设我们有一些用户数据
const users = [
  { id: 1, name: '张三' },
  { id: 2, name: '李四' },
  { id: 3, name: '王五' }
]
</script>

<template>
  <h1>用户列表</h1>
  <ul>
    <li v-for="user in users" :key="user.id">
      <!-- 
        使用 RouterLink 跳转到动态路由。
        我们用模板字符串动态地构建 `to` 属性。
      -->
      <RouterLink :to="`/users/${user.id}`">
        {{ user.name }}
      </RouterLink>
    </li>
  </ul>
</template>
```

**第3步：创建用户详情页 (`UserDetailView.vue`)**

这个组件需要获取 URL 中的 `id`，然后根据这个 `id` 来显示信息。

创建 `src/views/UserDetailView.vue`:
```vue
<script setup>
import { useRoute } from 'vue-router'

// 1. 引入并调用 useRoute() 来获取当前路由对象
const route = useRoute()

// 2. 从路由对象中访问 params
// route.params 是一个包含了所有动态片段的对象
// 我们的路径是 /users/:id，所以这里就是 route.params.id
const userId = route.params.id

// 在真实应用中，你可能会用这个 userId 去请求服务器获取用户数据
// fetch(`/api/users/${userId}`).then(...)
</script>

<template>
  <h1>用户详情页</h1>
  <p>当前查看的用户 ID 是：<strong>{{ userId }}</strong></p>
  <p>（这里应该显示该用户的详细信息）</p>
</template>
```
*   **关键点**: `useRoute()` 是组合式 API 提供的钩子（Hook），它返回一个响应式的路由对象，包含了当前激活路由的所有信息，如路径 (`path`)、参数 (`params`)、查询参数 (`query`) 等。**它是只读的**。

**第4步：更新导航栏 (`App.vue`)**

在 `src/App.vue` 的 `<nav>` 中添加入口：
```html
<nav>
  <RouterLink to="/">首页</RouterLink>
  <RouterLink to="/about">关于</RouterLink>
  <RouterLink to="/users">用户列表</RouterLink> <!-- 新增 -->
</nav>
```

**验收成果：**
1.  运行项目，点击导航栏的“用户列表”。
2.  在用户列表页，点击“张三”，浏览器地址栏会变成 `/users/1`，页面会显示“当前查看的用户 ID 是：1”。
3.  返回，点击“李四”，地址栏变为 `/users/2`，页面显示 ID 为 2。

你已经掌握了如何处理成千上万个相似页面的能力！

---

### **2. 编程式导航：在 JavaScript 中控制跳转**

除了用户点击 `<RouterLink>`，我们经常需要在特定逻辑完成后（如登录、注册、表单提交）用代码来跳转页面。

**通俗比喻**: `<RouterLink>` 像是汽车上的物理按钮（比如“一键回家”）。而编程式导航，则像是你对车载语音助手说：“嘿，Siri，带我回家”。你是在通过代码命令导航系统工作。

#### **动手实践：添加一个“返回列表”的按钮**

在用户详情页，我们加一个按钮，点击后能返回用户列表页。

**第1步：修改用户详情页 (`UserDetailView.vue`)**

```vue
<script setup>
import { useRoute, useRouter } from 'vue-router' // 1. 引入 useRouter

const route = useRoute()
const userId = route.params.id

// 2. 引入并调用 useRouter() 来获取路由器实例
const router = useRouter()

// 3. 定义一个方法，在其中调用路由器的导航方法
function goBackToList() {
  // router.push() 会向 history 栈添加一个新的记录
  // 用户可以点击浏览器的“后退”按钮回到之前的页面
  router.push('/users')
}
</script>

<template>
  <h1>用户详情页</h1>
  <p>当前查看的用户 ID 是：<strong>{{ userId }}</strong></p>
  
  <!-- 4. 绑定点击事件 -->
  <button @click="goBackToList">返回用户列表</button>
</template>
```
*   **关键点**: `useRouter()` 提供了路由器实例，它包含了 `push`, `replace`, `go` 等 **可执行操作的方法**。
*   **`useRoute` vs `useRouter`**:
    *   `useRoute()`: 获取当前路由信息（**读**），像一张地图。
    *   `useRouter()`: 获取路由器实例（**写/操作**），像一个遥控器。

**`router.push()` 的其他用法：**
*   **`router.push('/about')`**: 跳转到指定路径。
*   **`router.replace('/about')`**: 跳转，但**不会**留下历史记录。用户无法点击后退按钮返回。适用于登录页跳转到主页的场景。
*   **`router.go(-1)`**: 后退一步，相当于 `history.back()`。`router.go(1)` 前进一步。

---

### **3. 命名路由与 404 页面**

#### **命名路由 (Named Routes)**

直接在代码或模板里写死 URL 路径 (`/users/1`) 有个缺点：如果以后你想把 `/users` 改成 `/members`，你就得去项目里到处找这个路径进行修改，非常麻烦。

命名路由就是给路由起一个唯一的名字，我们通过名字来跳转，这样就和具体的 URL 路径解耦了。

**第 1 步：检查路由配置**
我们之前在 `src/router/index.js` 中已经给路由命名了，比如 `name: 'user-detail'`。

**第 2 步：改造跳转方式**

*   **改造 `<RouterLink>` (`UserListView.vue`)**:

    ```vue
    <template>
      <h1>用户列表</h1>
      <ul>
        <li v-for="user in users" :key="user.id">
          <!-- 
            使用 v-bind:to 或 :to 来绑定一个对象。
            通过 name 指定路由，通过 params 传递动态参数。
          -->
          <RouterLink :to="{ name: 'user-detail', params: { id: user.id } }">
            {{ user.name }}
          </RouterLink>
        </li>
      </ul>
    </template>
    ```

*   **改造编程式导航 (`UserDetailView.vue`)**:

    ```javascript
    function goBackToList() {
      // 同样，push 方法也可以接收一个对象
      router.push({ name: 'user-list' })
    }
    ```

现在，即使你把 `path: '/users'` 改成 `path: '/all-users'`，只要 `name` 不变，你的跳转代码就完全不需要修改！**这是一个非常重要的最佳实践。**

#### **404 Not Found 页面**

如果用户访问了一个不存在的路径，比如 `/hahaha`，我们应该显示一个“页面未找到”的提示，而不是一个空白页。

**第 1 步：创建 404 组件**

创建 `src/views/NotFoundView.vue`:
```vue
<template>
  <h1>404 - 页面未找到</h1>
  <p>您访问的页面不存在，请检查 URL 是否正确。</p>
  <RouterLink to="/">返回首页</RouterLink>
</template>
```

**第 2 步：添加捕获所有路由的配置**

在 `src/router/index.js` 的 `routes` 数组 **末尾**，添加以下规则：

```javascript
// ... (之前的 routes)
const routes = [
  // ...
  {
    // :pathMatch(.*)* 是一个固定的正则表达式，意思是匹配所有路径
    // 它必须放在路由表的最后，因为它会捕获所有之前没有匹配到的路径
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFoundView.vue')
  }
]
```
**注意：** 这个规则一定要放在最后，因为路由是按顺序匹配的。

**验收成果：**
现在尝试访问一个不存在的地址，比如 `http://localhost:5173/some/random/path`，你会看到优雅的 404 页面。

---

### **第二阶段总结：**

你现在已经掌握了 Vue Router 的“三驾马车”：

*   **动态路由 (`/users/:id`)**: 使用 `useRoute().params` 来获取动态参数。
*   **编程式导航 (`router.push`)**: 使用 `useRouter()` 在 JS 中控制路由。
*   **命名路由 (`name: 'user-detail'`)**: 解耦 URL，让代码更健壮。
*   同时，你也学会了如何为你的应用增加一个必备的 **404 页面**。

这些是日常开发中使用频率最高的功能。请确保你已经亲手实现了这些例子，并理解了 `useRoute` 和 `useRouter` 的核心区别。


## 高级应用与真实场景
现在我们已经掌握了构建一个功能完备的路由应用所需的核心技能。接下来，我们要进入第三阶段，学习如何构建更复杂、更专业、性能更好的大型应用。

**第三阶段：高级应用与真实场景 (The "Production Ready" Stage)**

这一阶段我们将聚焦于三个在企业级项目中至关重要的高级特性：嵌套路由、导航守卫和路由懒加载。

---

### **1. 嵌套路由 (Nested Routes): 构建复杂布局**

在很多应用中，页面布局是分层的。比如一个后台管理系统，通常有一个固定的侧边栏和顶部栏，只有中间的内容区域会根据用户的操作而变化。嵌套路由就是为了完美实现这种“UI 中有 UI”的布局。

**通俗比喻**: 想象一个文件柜 (`父路由组件`)，它本身占据一个位置。柜子里有多个抽屉 (`子路由`)，比如“客户资料”、“销售合同”。当你打开不同的抽屉时，改变的只是抽屉里的内容，而整个文件柜的外观和位置是不变的。

#### **动手实践：创建一个后台管理布局**

我们将创建一个 `/admin` 路径，它包含一个固定的侧边栏，侧边栏里有“仪表盘”和“用户管理”两个链接，点击后只刷新右侧的内容区。

**第 1 步：创建父路由组件和子路由组件**

1.  **父组件 `AdminLayout.vue`**: 这是我们的“文件柜”，包含了整体布局和子路由的渲染出口 `<RouterView>`。
    创建 `src/views/admin/AdminLayout.vue`:
    ```vue
    <script setup>
    import { RouterLink, RouterView } from 'vue-router'
    </script>

    <template>
      <div class="admin-layout">
        <aside class="sidebar">
          <h2>管理后台</h2>
          <nav>
            <!-- 注意这里的 to 路径，它们是 /admin 的子路径 -->
            <RouterLink to="/admin/dashboard">仪表盘</RouterLink>
            <RouterLink to="/admin/settings">系统设置</RouterLink>
          </nav>
        </aside>
        <main class="content">
          <!-- 
            这是子路由的渲染出口！
            /admin/dashboard 或 /admin/settings 匹配到的组件将会在这里显示
          -->
          <RouterView />
        </main>
      </div>
    </template>

    <style scoped>
    .admin-layout { display: flex; height: 100vh; }
    .sidebar { width: 200px; background-color: #2c3e50; color: white; padding: 20px; }
    .sidebar h2 { margin-top: 0; }
    .sidebar nav a { display: block; color: #aaa; text-decoration: none; margin-bottom: 10px; }
    .sidebar nav a.router-link-active { color: #42b983; font-weight: bold; }
    .content { flex-grow: 1; padding: 20px; }
    </style>
    ```

2.  **子组件 `DashboardView.vue`**:
    创建 `src/views/admin/DashboardView.vue`:
    ```vue
    <template>
      <h2>仪表盘</h2>
      <p>这里是后台的核心数据统计。</p>
    </template>
    ```

3.  **子组件 `SettingsView.vue`**:
    创建 `src/views/admin/SettingsView.vue`:
    ```vue
    <template>
      <h2>系统设置</h2>
      <p>在这里配置你的系统参数。</p>
    </template>
    ```

**第2步：配置嵌套路由 (`src/router/index.js`)**

修改路由配置，使用 `children` 属性来定义嵌套关系。

```javascript
// src/router/index.js

// ...

const routes = [
  // ... (之前的路由)
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/admin/AdminLayout.vue'), // 父路由组件
    children: [ // 定义子路由
      {
        // 当访问 /admin 时，默认重定向到 /admin/dashboard
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        // 子路由的 path 不需要写 /，它会自动拼接在父路径后面
        // 所以这里的 path: 'dashboard' 最终会匹配 /admin/dashboard
        path: 'dashboard',
        name: 'admin-dashboard',
        component: () => import('../views/admin/DashboardView.vue')
      },
      {
        path: 'settings', // 匹配 /admin/settings
        name: 'admin-settings',
        component: () => import('../views/admin/SettingsView.vue')
      }
    ]
  }
  // ... (404 路由)
]
```
*   **关键点**: `children` 是一个路由配置数组，和顶层的 `routes` 数组结构完全一样。父路由的组件 (`AdminLayout.vue`) 必须包含一个 `<RouterView>` 来为子路由提供渲染场地。
*   `redirect`: 可以用来设置默认显示的子页面。

**第3步：更新主导航 (`App.vue`)**

在 `src/App.vue` 的 `<nav>` 中添加入口：
```html
<nav>
  <!-- ... (之前的链接) -->
  <RouterLink to="/admin">后台管理</RouterLink> <!-- 新增 -->
</nav>
```

**验收成果：**
1.  点击“后台管理”，页面跳转到 `/admin/dashboard`。你会看到左侧是固定的侧边栏，右侧显示着“仪表盘”的内容。
2.  点击侧边栏的“系统设置”，URL 变为 `/admin/settings`，只有右侧内容刷新为“系统设置”，侧边栏保持不变。

你现在已经能构建出非常专业的、多层级的应用布局了！

---

### **2. 导航守卫 (Navigation Guards): 路由的“门卫”**

导航守卫是 Vue Router 最强大的功能之一。它允许你在路由即将发生变化时介入，执行一些逻辑，然后决定是 **放行**、**取消** 还是 **跳转到其他地址**。最经典的应用场景就是 **登录权限验证**。

**通俗比喻**: 导航守卫就像是一个小区的保安。
*   你想进入小区（**导航发生**）。
*   保安拦住你问：“有门禁卡吗？”（**`router.beforeEach` 全局前置守卫**）。
*   你有卡，保安放你进去（**调用 `next()`**）。
*   你没卡，保安让你去物业办理（**`next('/login')`**）。
*   或者，某个特定的别墅（**路由独享守卫**）要求除了门禁卡还要核对访客名单。
*   你准备离开家时（**组件内守卫 `onBeforeRouteLeave`**），想起来煤气没关，你先不离开，回去关了再说。

#### **动手实践：保护后台管理页面**

我们来设定一个规则：只有“已登录”的用户才能访问 `/admin` 下的所有页面。

**第 1 步：模拟登录状态**

在真实项目中，登录状态通常用 Pinia 或 Vuex 管理。为了简化，我们暂时用一个 `localStorage` 来模拟。

*   **假装登录**: 在浏览器开发者工具的 Console 中执行 `localStorage.setItem('token', 'true')`
*   **假装登出**: 执行 `localStorage.removeItem('token')`

**第2步：添加全局前置守卫 (`src/router/index.js`)**

在 `createRouter` 之后，挂载应用之前，添加守卫逻辑。

```javascript
// src/router/index.js

// ... (import 和 routes 定义)

const router = createRouter({ /* ... */ })

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // to: 即将要进入的目标路由对象
  // from: 当前导航正要离开的路由对象
  // next: 一个函数，必须调用它来 resolve 这个钩子。

  // 1. 检查要访问的路径是否需要登录
  if (to.path.startsWith('/admin')) {
    // 2. 检查用户是否已登录 (这里用 localStorage 模拟)
    const isLoggedIn = !!localStorage.getItem('token')

    if (isLoggedIn) {
      // 如果已登录，直接放行
      next() 
    } else {
      // 如果未登录，重定向到登录页
      alert('请先登录！') // 实际项目中会跳转
      next({ name: 'home' }) // 或者 next('/login')
    }
  } else {
    // 如果访问的不是需要权限的页面，直接放行
    next()
  }
})

export default router
```
*   **`to.path.startsWith('/admin')`**: 这是一个简单的判断，更好的方式是给路由添加 `meta` 字段，比如 `meta: { requiresAuth: true }`，然后在守卫里检查 `to.meta.requiresAuth`。这样更灵活。
*   **`next()`**: **必须被调用**。
    *   `next()`: 放行。
    *   `next(false)`: 中断当前的导航。
    *   `next('/')` 或 `next({ name: 'home' })`: 跳转到一个不同的地址。

**验收成果：**
1.  确保你已“登出”（`localStorage` 中没有 `token`）。
2.  尝试访问 `http://localhost:5173/admin`。你会看到一个 `alert`，然后页面被重定向回首页。
3.  在控制台执行 `localStorage.setItem('token', 'true')` 来“登录”。
4.  再次访问 `/admin`，这次你就可以成功进入了。

你已经学会了如何为你的应用加上最关键的安全防线！

---

### **3. 路由懒加载 (Lazy Loading): 优化应用性能**

当你的应用越来越大，包含几十个甚至上百个页面时，如果把所有页面的 JS 代码打包到一个文件里，这个文件会非常巨大。用户首次访问时需要下载整个大文件，会导致**首页白屏时间（FCP）**很长，严重影响用户体验。

路由懒加载就是解决方案。它会将每个路由对应的组件打包成一个独立的 JS 文件（chunk），只有当用户访问这个路由时，浏览器才会去下载对应的文件。

**通俗比喻**: 你去图书馆，不是一次性把所有书都搬回家，而是先只借一本《Vue 入门》。看完后，再去借《Vue Router 进阶》。懒加载就是按需借阅，减轻你初次出门的负担。

#### **动手实践：改造所有路由为懒加载**

其实我们在第一阶段就已经不自觉地使用了懒加载的语法。现在我们来正式地理解并应用它。

**检查路由配置 (`src/router/index.js`)**

把所有 `component` 的导入方式都改成函数形式：

```javascript
// 之前可能是这样（不推荐）:
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    component: HomeView // 这样会把 HomeView 打包进主文件
  },
  // ...
]
```

**改造为懒加载（推荐）:**

```javascript
// src/router/index.js

const routes = [
  {
    path: '/',
    name: 'home',
    // 使用动态 import()，这会告诉打包工具 (Vite/Webpack)
    // 将 ../views/HomeView.vue 单独打包
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue')
  },
  // ... 将你所有的路由都改成这种写法
]
```
就是这么简单！Vite 和 Webpack 会自动处理剩下的事情。

**验收成果：**
1.  运行 `npm run build` 来打包你的应用。
2.  查看 `dist/assets` 目录，你会发现多了很多 `index-xxxx.js`, `AboutView-xxxx.js` 这样的小 JS 文件，而不是只有一个巨大的 `app.js`。
3.  打开浏览器开发者工具的 "Network" (网络) 面板。
4.  首次访问首页，只会加载首页相关的 JS。当你点击“关于”时，你会看到浏览器发起一个新的网络请求，去加载 `AboutView` 对应的那个 JS 文件。

你已经掌握了优化大型单页应用性能的核心武器！

---

### **第三阶段总结：**

你现在已经具备了构建一个**复杂、安全且高性能**的 Vue 应用的能力。

*   **嵌套路由 (`children`)**: 解决了复杂页面布局的问题。
*   **导航守卫 (`beforeEach`)**: 解决了权限控制和路由拦截的问题。
*   **路由懒加载 (`import()`)**: 解决了大型应用的性能和加载速度问题。

这些都是衡量一个 Vue 开发者是否达到中高级水平的重要标准。


## 精通与生态整合
在这一阶段，我们将不再学习全新的、颠覆性的功能，而是聚焦于**打磨细节、提升用户体验、优化开发实践**，并探索 Vue Router 如何与 Vue 生态中的其他核心成员（如 Pinia）无缝协作。

**第四阶段：精通与生态整合 (The "Mastery" Stage)**

这个阶段的目标是让你从一个“会用”Vue Router 的开发者，转变为一个能用得“优雅”、“高效”且“全面”的资深开发者。

---

### **1. 再次深入组合式 API：`useRoute` 与 `useRouter`**

我们在前面已经用过 `useRoute` 和 `useRouter`，现在是时候做一个精辟的总结，以巩固你的理解，确保在任何场景下都能做出正确的选择。

*   **`useRoute()`**
    *   **本质**: 获取 **当前** 激活路由的 **只读** 信息对象。
    *   **包含**: `path`, `params`, `query`, `hash`, `name`, `meta` 等。
    *   **特性**: 它是响应式的。如果 URL 参数变化（例如从 `/users/1` 导航到 `/users/2`），组件中依赖 `route.params.id` 的地方会自动更新。
    *   **何时使用**: 当你需要 **读取** 当前 URL 的信息时。比如，获取动态 ID 来请求数据，或者根据查询参数 `?source=email` 来显示不同内容。
    *   **代码示例**: `const route = useRoute(); console.log(route.params.id);`

*   **`useRouter()`**
    *   **本质**: 获取 Vue Router 的 **全局实例**。
    *   **包含**: `push()`, `replace()`, `go()`, `back()`, `forward()` 等 **方法**。
    *   **特性**: 它是一个包含了各种导航方法的对象，用于 **触发** 导航行为。
    *   **何时使用**: 当你需要 **执行** 页面跳转、后退等 **操作** 时。比如，登录成功后跳转到后台，或者提交表单后返回列表页。
    *   **代码示例**: `const router = useRouter(); router.push('/dashboard');`

**一个比喻帮你记忆**:
*   `useRoute` 就像你车上的 **GPS 屏幕**，它告诉你“你现在在哪条路上 (`path`)”、“门牌号是多少 (`params`)”，你只能看，不能操作屏幕本身来改变路线。
*   `useRouter` 就像你手中的 **方向盘和油门**，你可以用它来决定“要去哪里 (`push`)”、“掉头 (`replace`)”或者“倒车 (`go(-1)`)”。

---

### **2. 滚动行为 (Scroll Behavior): 控制页面跳转后的滚动条**

默认情况下，当你在路由之间切换时，滚动条会保持在原来的位置。这在某些情况下体验并不好。比如，从一个很长的列表页底部，点击进入详情页，你希望详情页是从顶部开始显示的。滚动行为就是用来定制这个体验的。

**动手实践：让新页面总是滚动到顶部**

在创建路由实例时，配置 `scrollBehavior` 函数。

修改 `src/router/index.js`:
```javascript
// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// ... routes 定义 ...

const router = createRouter({
  history: createWebHistory(),
  routes,
  
  // 添加 scrollBehavior 配置
  scrollBehavior(to, from, savedPosition) {
    // savedPosition 只有在通过浏览器的“前进/后退”按钮导航时才可用
    if (savedPosition) {
      // 如果有保存的位置，则恢复到该位置
      return savedPosition
    } else {
      // 否则，总是滚动到页面顶部
      return { top: 0, left: 0 }
    }
  }
})

export default router
```
*   **`scrollBehavior` 函数**:
    *   它在每次路由跳转后被调用。
    *   `to` 和 `from` 是路由对象。
    *   `savedPosition` 是一个 `{ top, left }` 对象，记录了 `from` 路由离开时的滚动位置。
*   **返回值**:
    *   返回 `{ top: 0, left: 0 }` 会让页面滚动到顶部。
    *   返回 `savedPosition` 会实现类似浏览器原生的“记住滚动位置”行为。
    *   你还可以返回一个 CSS 选择器，比如 `return { el: '#main' }`，它会滚动到该元素的位置。

现在，你应用中的所有页面跳转都会自动滚动到顶部，提供了更一致、更符合预期的用户体验。

---

### **3. 过渡动画 (Transitions): 提升用户体验的“魔法”**

为路由切换添加平滑的动画效果，能极大地提升应用的“高级感”和用户体验。Vue 的内置 `<Transition>` 组件和 Vue Router 配合得天衣无缝。

**动手实践：为路由切换添加淡入淡出效果**

修改根组件 `src/App.vue`，用 `<Transition>` 组件包裹 `<RouterView>`。

```vue
<!-- src/App.vue -->
<template>
  <header>
    <!-- ... nav ... -->
  </header>

  <main>
    <RouterView v-slot="{ Component }">
      <Transition name="fade" mode="out-in">
        <component :is="Component" />
      </Transition>
    </RouterView>
  </main>
</template>

<style>
/* 定义我们的过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
```

**代码解析**:
1.  **`<RouterView v-slot="{ Component }">`**: 这是关键。我们使用了 `<RouterView>` 的[作用域插槽](https://cn.vuejs.org/api/built-in-components.html#routerview-s-v-slot)功能，这允许我们拿到将要被渲染的路由组件，并将其赋值给一个变量（这里是 `Component`）。
2.  **`<Transition>`**: 我们将 `<RouterView>` 的内容用 `<Transition>` 包裹起来。`name="fade"` 定义了动画效果的 CSS 类名前缀。`mode="out-in"` 确保了旧组件先完成离开动画，新组件再开始进入动画，避免了内容重叠。
3.  **`<component :is="Component" />`**: 这是一个动态组件，它会渲染 `v-slot` 传过来的 `Component`。
4.  **CSS 类**: `<Transition>` 会在组件进入和离开的不同阶段自动添加/移除 CSS 类（如 `.fade-enter-from`, `.fade-enter-active` 等），我们只需为这些类定义 CSS 过渡或动画即可。

现在，在你应用中切换页面时，就会看到平滑的淡入淡出效果了。你可以尝试定义更复杂的动画，比如滑动效果，来让你的应用更生动。

---

### **4. 与状态管理 (Pinia) 结合：构建强大的数据驱动应用**

在真实的大型应用中，路由守卫逻辑往往很复杂，并且需要依赖全局状态（如用户信息、权限列表）。这时，Vue Router 与官方推荐的状态管理库 Pinia 的结合就显得至关重要。

**场景：使用 Pinia 来实现更健壮的登录验证**

**前提**: 你已经在项目中安装并设置好了 Pinia。
`npm install pinia`

**第1步：创建一个用户状态的 Store (`src/stores/user.js`)**

```javascript
// src/stores/user.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || null)
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  function setToken(newToken) {
    token.value = newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }

  // 模拟一个登录方法
  async function login(username, password) {
    // 假设调用 API 成功，拿到了 token
    const fakeToken = 'user-token-12345'
    setToken(fakeToken)
    // 可以在这里获取用户信息
    // userInfo.value = await fetchUserInfoAPI();
  }
  
  function logout() {
    setToken(null)
    userInfo.value = null
  }

  return { token, userInfo, isLoggedIn, login, logout }
})
```

**第2步：在导航守卫中使用 Store (`src/router/index.js`)**

**重要**: 在 `router.js` 文件中，你不能像在 Vue 组件里那样直接使用 `useUserStore()`，因为此时 Pinia 实例可能还未挂载到 Vue 应用上。正确的做法是在守卫函数内部导入并使用它。

```javascript
// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user' // 引入 store

// ... routes ...

const router = createRouter({ /* ... */ })

router.beforeEach((to, from, next) => {
  // 在守卫内部获取 store 实例
  const userStore = useUserStore()

  // 使用 store 中的状态来判断
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    // 如果目标路由需要认证，但用户未登录
    next({ name: 'login' }) // 跳转到登录页
  } else {
    next() // 其他情况一律放行
  }
})

// 同时，为了让 meta.requiresAuth 生效，我们需要在路由配置中添加它
// const routes = [
//   {
//     path: '/admin',
//     name: 'admin',
//     component: ...,
//     meta: { requiresAuth: true }, // 添加 meta 字段
//     children: [...]
//   }
// ]
```

通过这种方式，你的权限逻辑和用户状态被集中管理在 Pinia 中，路由守卫只负责读取状态并作出决策，代码结构变得异常清晰和可维护。

---

### **回顾**

回顾一下我们的旅程：

*   **第一阶段**: 你学会了 Vue Router 的基本工作原理和核心组件，能搭建起一个简单的 SPA。
*   **第二阶段**: 你掌握了动态路由、编程式导航和命名路由，能处理真实世界的复杂 URL 结构和交互。
*   **第三阶段**: 你学会了嵌套路由、导航守卫和懒加载，能构建出专业、安全、高性能的大型应用。
*   **第四阶段**: 你打磨了应用的交互细节（滚动、动画），并学会了将路由与状态管理（Pinia）优雅地结合，达到了精通和融会贯通的水平。

你现在不仅知道 Vue Router 的“每一个按钮”是干什么的，更理解了“在什么情况下该按哪个按钮”，以及如何将它与整个 Vue 生态系统串联起来，构建出坚实而优雅的应用程序。

接下来，就是不断在真实项目中实践、应用这些知识，并去探索更多细节，比如路由元信息(meta)的更多玩法、动态添加/删除路由等更高级的主题。