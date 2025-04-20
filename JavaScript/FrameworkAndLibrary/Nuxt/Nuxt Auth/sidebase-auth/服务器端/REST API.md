## REST API

NextAuth.js 支持的所有端点也都受 `nuxt-auth` 支持：

| Endpoint(端点)                    | Request(请求)  | Description(描述)       |
| ------------------------------- | ------------ | --------------------- |
| `${baseURL}/signin`             | `GET`        | 显示内置/无品牌登录            |
| `${baseURL}/signin/:provider`   | `POST`       | 启动特定提供商的登录            |
| `${baseURL}/callback/:provider` | `GET` `POST` | 处理登录期间从 OAuth 服务返回的请求 |
| `${baseURL}/signout`            | `GET` `POST` | 显示内置/无品牌注销            |
| `${baseURL}/session`            | `GET`        | 返回客户端安全会话对象           |
| `${baseURL}/csrf`               | `GET`        | 返回包含 CSRF 的对象         |
| `${baseURL}/providers`          | `GET`        | 返回已配置的 OAuth 提供商列表    |

`baseURL` 默认为 `/api/auth` ， [可以在 `nuxt.config.ts` 中配置](https://auth.sidebase.io/guide/application-side/configuration#baseurl) 。

如果你愿意，你可以直接与这些 API 端点交互，不过尽可能使用 `useAuth` 可能是一个更好的主意。 [在此处查看 NextAuth.js 的完整 rest API 文档](https://next-auth.js.org/getting-started/rest-api) 。