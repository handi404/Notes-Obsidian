---
title: "Credentials | NextAuth.js"
source: "https://next-auth.js.org/configuration/providers/credentials"
author:
published: 2024-10-25
created: 2025-04-17
description: "How to"
tags:
  - "clippings"
---
Version: v4 版本： v4

凭证提供程序允许您使用任意凭证处理登录，例如用户名和密码、双因素身份验证或硬件设备（例如 YubiKey U2F / FIDO）。

它旨在支持您拥有需要对其进行用户身份验证的现有系统的使用案例。

pages/api/auth/\[...nextauth\].js

```js
import CredentialsProvider from "next-auth/providers/credentials"
...
providers: [
  CredentialsProvider({
    // The name to display on the sign in form (e.g. 'Sign in with...')
    // 登录表单上要显示的名称（例如 "用......登录）
    name: 'Credentials',
    // 凭据用于在登录页面上生成合适的表单。
    // 您可以指定希望提交的任何字段.
    // e.g. domain, username, password, 2FA token, etc. 
    // 例如，域名、用户名、密码、2FA 令牌等
    // You can pass any HTML attribute to the <input> tag through the object.
    // 您可以通过对象将任何 HTML 属性传递给 <input> 标签
    credentials: {
      username: { label: "Username", type: "text", placeholder: "jsmith" },
      password: { label: "Password", type: "password" }
    },
    async authorize(credentials, req) {
      // You need to provide your own logic here that takes the credentials
      // 您需要在这里提供自己的逻辑，以获取凭证
      // submitted and returns either a object representing a user or value
      // 提交并返回代表用户的对象或值
      // 如果凭证无效，则为 false/null。
      // e.g. return { id: 1, name: 'J Smith', email: 'jsmith@example.com' }
      // You can also use the \`req\` object to obtain additional parameters
      // 您还可以使用 `req\` 对象来获取其他参数
      // (i.e., the request IP address)
      const res = await fetch("/your/endpoint", {
        method: 'POST',
        body: JSON.stringify(credentials),
        headers: { "Content-Type": "application/json" }
      })
      const user = await res.json()

      // If no error and we have user data, return it
      // 如果没有错误，并且我们有用户数据，则返回该数据
      if (res.ok && user) {
        return user
      }
      // Return null if user data could not be retrieved
      // 如果无法检索到用户数据，则返回空值
      return null
    }
  })
]
...
```

有关更多信息，请参阅 [凭证提供程序文档](https://next-auth.js.org/providers/credentials) 。

==注意==
只有在为会话启用了 JSON Web Token 时，才能使用凭据提供程序。使用 Credentials provider 进行身份验证的用户不会保留在数据库中。

| Name           | Description                                                               | Type                                  | Required |
| -------------- | ------------------------------------------------------------------------- | ------------------------------------- | -------- |
| id 身份证         | Unique ID for the provider   提供商的唯一 ID                                    | `string` `字符串`                        | Yes      |
| name 名字        | Descriptive name for the provider   提供程序的描述性名称                            | `string` `字符串`                        | Yes      |
| type 类型        | Type of provider, in this case `credentials`   提供程序类型，在本例中为 `credentials` | `"credentials"` `“凭证”`                | Yes      |
| credentials 凭据 | The credentials to sign-in with   用于登录的凭据                                 | `Object` `对象`                         | Yes      |
| authorize 授权   | Callback to execute once user is to be authorized   用户被授权后执行的回调           | `(credentials, req) => Promise<User>` | Yes      |