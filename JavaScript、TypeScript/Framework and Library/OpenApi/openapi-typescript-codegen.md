**官方建议迁移：https://github.com/hey-api/openapi-ts**
"前端如何使用后端生成的 openapi specification 生成 HTTP 服务"
直击现代前端工程化的核心痛点：**如何高效、安全地与后端 API 协作**。

手写 API 请求代码的时代基本已经过去了。现在追求的是**自动化、类型安全和易于维护**。使用后端生成的 OpenAPI Specification (以前叫 Swagger Spec) 来生成 HTTP 服务，正是实现这一目标的核心手段。

---

### 1. 直击核心 (What & Why)

#### 是什么？

简单来说，OpenAPI 规范文件（通常是 `openapi.json` 或 `openapi.yaml`）就是一份 **“API 的机器可读说明书”**。

*   **对人来说**，它像 Swagger UI 那样，能清晰地展示所有 API 接口、参数、返回数据结构。
*   **对机器来说**，它定义了严格的规则，程序可以读取这份说明书，自动生成调用这些 API 的代码。

我们要做的，就是利用工具读取这份“说明书”，一键生成前端项目里所有与后端通信的“服务层 (Service Layer)”代码。

#### 为什么必须这么做？

1.  **效率天花板**：后端有 100 个接口，你不用手写 100 个请求函数。一条命令，几秒钟内全部生成，带完整的参数和返回类型。
2.  **“零”沟通成本与“零”错误率**：
    *   **告别手误**：URL 写错？请求方法用错（GET/POST）？参数名拼错？这些低级错误将彻底消失。代码是生成的，绝对忠于后端定义。
    *   **类型安全**：这是最关键的优势。生成的代码会自带 TypeScript 类型。这意味着，当你调用一个 API 时，IDE 会立刻告诉你需要传哪些参数、参数是什么类型；拿到返回数据时，它的结构和字段也都是有类型的，你可以放心地 `data.user.name`，而不用去 `console.log` 猜测数据结构，也杜绝了因为后端字段变更导致的线上运行时错误。
3.  **同步与维护的救星**：后端修改了接口（比如加了个参数、改了字段名）？没问题。他们更新 `openapi.json` 文件，你重新执行一次生成命令，所有相关的代码和类型就自动更新了。你只需要在代码里根据 TypeScript 的报错提示，修正你的业务逻辑即可。整个过程清晰、可控。

---

### 2. 现代实践 (How)

现在我们来看具体怎么做。虽然市面上工具有一些，但目前社区最主流、最好用的工具是 **`openapi-typescript-codegen`**。它专注于生成 TypeScript 代码，非常轻量且定制性强。

**通用工作流（适用于任何框架）：**

这个流程与你使用 React、Vue 还是 Svelte 无关，它是项目工程化的一部分。

**第零步：拿到 OpenAPI 规范文件**

首先，你需要从后端同学那里拿到 `openapi.json` 或 `openapi.yaml` 文件，或者一个能访问到该文件的 URL（比如 `http://your-api.com/docs/openapi.json`）。把它放到你的项目里，例如 `src/api/spec/openapi.json`。

**第一步：安装代码生成工具**

在你的前端项目中，将其安装为开发依赖：

```bash
npm install openapi-typescript-codegen -D
# 或者 yarn add openapi-typescript-codegen -D
# 或者 pnpm add openapi-typescript-codegen -D
```

**第二步：配置生成脚本**

在 `package.json` 的 `scripts` 中添加一条命令：

```json
// package.json
{
  "scripts": {
    "gen:api": "openapi --input ./src/api/spec/openapi.json --output ./src/api/generated --client axios --useOptions"
  }
}
```

*   `--input`: 指定你的 OpenAPI 规范文件路径。
*   `--output`: 指定生成代码的输出目录。
*   `--client`: 指定底层使用哪个 HTTP 客户端。强烈推荐 `axios` 或 `fetch`。`axios` 方便配置拦截器等高级功能。
*   `--useOptions`: (推荐) 这个参数让生成的函数最后一个参数变为一个可选的 `options` 对象，方便我们传递额外的请求配置，非常灵活。

**第三步：生成代码**

在终端运行：

```bash
npm run gen:api
```

执行完毕后，你的 `src/api/generated` 目录下就会出现一堆 `.ts` 文件，通常包含：
*   `models.ts`: 所有后端定义的 DTO (Data Transfer Object) 的 TypeScript 接口 (interface) 或类型 (type)。
*   `services.ts`: 包含所有 API 请求的函数，按后端 Controller/Tag 分组。
*   `index.ts`: 导出所有内容。

**生成的代码大概长这样（示例）：**

```typescript
// src/api/generated/services/UserService.ts

import type { User, CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class UserService {
    /**
     * @param id The user ID
     * @returns User The user object
     */
    public static getUserById(id: number): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{id}',
            path: {
                id: id,
            },
        });
    }
}
```

---

### 3. 框架集成与应用

生成的代码是纯粹的、框架无关的 TypeScript。所以，在任何框架中使用它都非常简单。关键在于**结合现代数据请求库**来发挥其最大威力。

#### 在 React 中使用 (结合 TanStack Query)

在 React 生态，**TanStack Query (原 React Query)** 是数据请求的王者。它负责处理缓存、重新请求、加载状态等，我们生成的 API Service 负责发起实际的请求。这是天作之合。

```tsx
// src/components/UserProfile.tsx
import { useQuery } from '@tanstack/react-query';
import { UserService } from '@/api/generated'; // 引入生成的服务

function UserProfile({ userId }: { userId: number }) {
  const { data, isLoading, isError } = useQuery({
    // queryKey 是缓存的唯一标识
    queryKey: ['user', userId], 
    // queryFn 就是实际的请求函数，直接调用我们生成的 service
    queryFn: () => UserService.getUserById(userId), 
  });

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error fetching user!</div>;

  // data 的类型是 `User`，已经被自动推断出来了！
  // 你可以安全地访问 data.name, data.email 等属性，IDE 会有智能提示。
  return (
    <div>
      <h1>{data.name}</h1>
      <p>{data.email}</p>
    </div>
  );
}
```

#### 在 Vue 中使用 (结合 TanStack Query)

同样的，Vue 生态也可以完美使用 TanStack Query 的 Vue 版本 `@tanstack/vue-query`。

```vue
<!-- src/components/UserProfile.vue -->
<script setup lang="ts">
import { useQuery } from '@tanstack/vue-query';
import { UserService } from '@/api/generated';

const props = defineProps<{ userId: number }>();

const { data, isLoading, isError } = useQuery({
  queryKey: ['user', props.userId],
  queryFn: () => UserService.getUserById(props.userId),
});
</script>

<template>
  <div v-if="isLoading">Loading...</div>
  <div v-else-if="isError">Error fetching user!</div>
  <!-- data 的类型同样被完美推断 -->
  <div v-else>
    <h1>{{ data.name }}</h1>
    <p>{{ data.email }}</p>
  </div>
</template>
```

#### 其他框架 (Svelte, Angular...)

原理完全一致。生成的 API Service 就是一个普通的 TS/JS 模块。在 Svelte 中你可以结合 Svelte Query，在 Angular 中，你可以将生成的服务注入到组件中使用。Angular 甚至有工具可以生成带 `@Injectable()` 装饰器的服务，与 Angular 的 DI 系统深度集成（可以使用 `openapi-generator-cli` 并指定 `typescript-angular` 生成器）。

---

### 4. 扩展

#### 扩展应用：

1.  **统一 API 配置（如添加 Token）**:
    生成的代码通常会有一个全局配置。例如，`openapi-typescript-codegen` 会生成一个 `OpenAPI` 对象，你可以在应用初始化时配置它，为所有请求添加 `Authorization` 头。

    ```typescript
    // 在你的应用入口文件，如 main.ts
    import { OpenAPI } from '@/api/generated';

    OpenAPI.BASE = 'https://api.yourdomain.com'; // 配置基础路径
    OpenAPI.TOKEN = async () => {
      // 从 localStorage, Pinia, Redux 等地方获取 token
      return `Bearer ${localStorage.getItem('token')}`; 
    };
    ```

2.  **集成到 CI/CD**:
    将 `npm run gen:api` 命令加入到你的 CI/CD 流程中。比如，在构建前自动执行一次，确保前端代码永远和最新的后端 API 规范保持同步。如果后端改动导致编译失败，CI/CD 会阻断发布，从而提前发现问题。

#### 未来趋势：

虽然 OpenAPI + 代码生成已经非常高效，但前端领域总是在探索更极致的开发体验。

*   **tRPC (TypeScript Remote Procedure Call)**: 这是目前最火的“下一代”方案。如果你的前后端都使用 TypeScript，tRPC 可以让你**完全跳过代码生成这一步**。你在后端定义路由和函数，前端可以直接像调用本地函数一样调用后端 API，并且享受端到端的类型安全。它的优点是极致的开发体验和类型安全，缺点是前后端技术栈强绑定（通常是 Node.js + TS）。

*   **GraphQL + 代码生成**: GraphQL 是 API 的另一种查询语言。与 REST+OpenAPI 不同，它允许前端按需请求数据。但同样，我们可以使用工具（如 `graphql-code-generator`）根据 GraphQL Schema 来生成类型安全的前端 Hooks 和 SDK，思路与 OpenAPI 类似。

### 总结

| 方案                    | 优点                         | 缺点               | 适用场景                                    |
| :-------------------- | :------------------------- | :--------------- | :-------------------------------------- |
| **OpenAPI + Codegen** | **技术栈解耦**、生态成熟、自动化、类型安全    | 需要维护规范文件，有代码生成步骤 | **最通用、最主流的场景**，尤其适合前后端分离、团队协作、多语言后端的项目。 |
| **tRPC**              | **端到端类型安全**、无需代码生成、极佳的开发体验 | **前后端强耦合 (TS)**  | 全栈 TypeScript 项目，追求极致开发效率的小到中型团队。       |
| **GraphQL + Codegen** | **前端按需取数**、解决数据冗余/缺失、类型安全  | 后端实现较复杂，需要额外学习成本 | 数据结构复杂、多端（Web, App）共用 API、对网络性能要求高的场景。  |

对于绝大多数项目而言，**掌握 OpenAPI + 代码生成的工作流，是你作为一名资深前端工程师必备的核心技能**。它能极大地提升你的开发效率、代码质量和项目的可维护性。