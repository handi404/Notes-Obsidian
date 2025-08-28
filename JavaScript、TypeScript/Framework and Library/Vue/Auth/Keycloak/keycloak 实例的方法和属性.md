剖析 `keycloak-js` 实例上的每一个重要方法和属性。这不仅仅是一份 API 列表，更是一份实战指南，告诉你“是什么”、“为什么”以及“怎么用”。

我们将把 `keycloak` 实例上的功能分为几个核心类别来讲解，以便更好地理解它们之间的关系。

```javascript
import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: '...',
  realm: '...',
  clientId: '...'
});

// 以下是 keycloak 实例上所有核心方法的详解
```

---

### 类别一：初始化与核心生命周期 (Initialization & Core Lifecycle)

这是整个库的入口和基石。

#### `init(options)`
这是**最重要、必须调用**的方法，它启动了整个认证流程。

*   **作用**: 初始化适配器，检查当前用户的认证状态（例如，从 URL 回调中解析令牌，或通过 SSO 静默登录）。
*   **返回值**: `Promise<boolean>`。一个 Promise，当初始化完成后，会解析出一个布尔值：`true` 表示用户已认证，`false` 表示用户未认证。
*   **核心 `options` 参数**:
    *   `onLoad`: `'login-required' | 'check-sso'`
        *   `'login-required'`: 如果用户未登录，立即将浏览器重定向到 Keycloak 登录页。
        *   `'check-sso'`: 检查用户是否已在 Keycloak 上有登录会话（SSO）。如果有，则静默获取令牌并完成登录，用户无感知；如果没有，则什么也不做，保持未登录状态。这是现代 SPA 的首选。
    *   `promiseType`: `'native' | 'legacy'`
        *   始终使用 `'native'`（默认值）。这会返回标准的浏览器 `Promise`。`'legacy'` 是为了兼容旧版浏览器的过时选项。
    *   `silentCheckSsoRedirectUri`: `string`
        *   一个指向你应用中一个空白 HTML 文件的 URL (例如 `https://myapp.com/silent-check-sso.html`)。当 `onLoad: 'check-sso'` 时，`keycloak-js` 会创建一个隐藏的 iframe 来访问此 URL 以安全地与 Keycloak 通信。**强烈建议配置此项**以获得最佳的静默 SSO 体验。
    *   `pkceMethod`: `'S256'`
        *   用于 PKCE 流程的哈希算法。默认且推荐值为 `'S256'`，通常你不需要更改它。这是现代安全实践的一部分。
    *   `token`, `refreshToken`, `idToken`: `string`
        *   在非常特殊的场景下，如果你已经从别处（如移动端）获得了令牌，可以用它们来初始化 `keycloak-js`，跳过重定向流程。

---

### 类别二：认证与会话管理 (Authentication & Session Management)

这些方法主动地改变用户的登录状态。

#### `login(options)`
*   **作用**: 将用户重定向到 Keycloak 登录页面。通常绑定在“登录”按钮的点击事件上。
*   **`options` 参数**:
    *   `redirectUri`: `string` - 指定登录成功后重定向回的 URL。如果省略，将使用初始化时 Keycloak Client 配置的默认值。
    *   `idpHint`: `string` - 直接跳转到指定的身份提供商（IDP）登录页，如 `'google'`, `'facebook'`。
    *   `scope`: `string` - 请求额外的 OIDC scope，如 `'openid profile email'`。
    *   `prompt`: `'none' | 'login'` - `prompt=none` 尝试无交互登录，如果不行会报错；`prompt=login` 强制用户重新输入凭据。

#### `logout(options)`
*   **作用**: 将用户从 Keycloak 会话中登出，并重定向。
*   **`options` 参数**:
    *   `redirectUri`: `string` - 登出成功后重定向到的 URL。**此 URL 必须在 Keycloak Client 配置的 "Valid Post Logout Redirect URIs" 列表中**，否则会报错。这是一个常见的配置陷阱！

#### `register(options)`
*   **作用**: 将用户重定向到 Keycloak 注册页面。参数与 `login()` 类似。

#### `accountManagement()`
*   **作用**: 将用户重定向到其在 Keycloak 中的个人账户管理页面，他们可以在那里修改密码、设置 MFA 等。这是一个非常方便的快捷方式。

---

### 类别三：令牌管理 (Token Management)

这是与后端 API 安全通信的核心。

#### `updateToken(minValidity)`
*   **作用**: 这是**第二重要**的方法。它检查 Access Token 的有效性。如果令牌即将过期（剩余有效期小于 `minValidity` 秒），它会使用 Refresh Token 在后台静默地获取一对新的令牌（Access Token 和 Refresh Token）。
*   **`minValidity` (number)**: 检查令牌有效期的秒数阈值。
    *   `5` (推荐值): 如果 Access Token 在 5 秒内将过期，则刷新它。
    *   `-1`: 强制立即刷新令牌，无论它是否过期。
*   **返回值**: `Promise<boolean>`。解析为 `true` 表示令牌被成功刷新；`false` 表示令牌仍然有效，无需刷新。
*   **最佳实践**: 在每次调用受保护的后端 API 之前，都调用 `keycloak.updateToken(30)`。使用 `axios` 或 `fetch` 的请求拦截器是实现这一点的完美方式。

#### `clearToken()`
*   **作用**: 从内存中清除所有令牌。这会使前端应用认为用户已登出。
*   **注意**: 这**不会**让用户从 Keycloak 服务器端登出。它只是一个纯客户端操作。如果你想完全登出，请使用 `logout()`。

---

### 类别四：用户信息与令牌访问 (User Info & Token Access)

认证成功后，你需要通过这些属性和方法来获取数据。

#### 属性 (Properties)

*   `authenticated`: `boolean` - 一个只读属性，表示用户当前是否已认证。
*   `token`: `string | undefined` - 原始的、未解码的 Base64 编码的 **Access Token**。用于放在 HTTP 请求的 `Authorization: Bearer <token>` 头中。
*   `tokenParsed`: `object | undefined` - 已解码的 **Access Token** (JWT)。包含用户的权限信息（如角色）、过期时间 (`exp`)、签发者 (`iss`) 等。
*   `idToken`: `string | undefined` - 原始的、未解码的 **ID Token**。
*   `idTokenParsed`: `object | undefined` - 已解码的 **ID Token** (JWT)。包含用户的身份信息（如 `name`, `email`, `preferred_username`）。这是 OIDC 的核心。
*   `refreshToken`: `string | undefined` - 原始的 Refresh Token。
*   `refreshTokenParsed`: `object | undefined` - 已解码的 Refresh Token。
*   `realmAccess`: `object | undefined` - 从 `tokenParsed` 中提取的快捷方式，包含了用户在 Realm 级别的角色列表（`realmAccess.roles`）。
*   `resourceAccess`: `object | undefined` - 从 `tokenParsed` 中提取的快捷方式，包含了用户在各个 Client (Resource) 下的角色。

#### 方法 (Methods)

#### `loadUserProfile()`
*   **作用**: 调用 Keycloak 的 `/userinfo` 端点来获取最新的用户信息。这与 `idTokenParsed` 的区别在于，后者是登录那一刻的信息快照，而此方法获取的是实时信息。
*   **返回值**: `Promise<KeycloakProfile>`。解析为一个包含用户信息的对象。

#### `loadUserInfo()`
*   **作用**: `loadUserProfile()` 的别名，功能完全相同。

---

### 类别五：权限检查 (Authorization Checks)

用于在前端根据用户角色控制 UI 元素的显示/隐藏。

#### `hasRealmRole(roleName)`
*   **作用**: 检查用户是否拥有指定的 Realm 级别角色。
*   **返回值**: `boolean`。

#### `hasResourceRole(roleName, resource?)`
*   **作用**: 检查用户是否拥有指定 Client (Resource) 的角色。
*   **`resource` (string, 可选)**: Client ID。如果省略，则默认为当前 `keycloak` 实例的 `clientId`。
*   **返回值**: `boolean`。

> **⚠️ 安全警告**: 前端的角色检查仅用于改善用户体验（UI 控制）。**绝对不能**以此作为安全防护的依据。真正的、权威的权限校验**必须**在后端 API 进行，后端需要独立验证每个请求中 Access Token 的签名和内容。

---

### 类别六：事件处理 (Event Handling)

通过设置回调函数来响应认证流程中的各种事件。

*   `onReady`: `(authenticated: boolean) => void` - 当 `init()` 完成时触发。
*   `onAuthSuccess`: `() => void` - 当用户成功登录时触发。
*   `onAuthError`: `(errorData: KeycloakError) => void` - 当登录或 `init()` 过程中发生错误时触发。
*   `onAuthRefreshSuccess`: `() => void` - 当 `updateToken()` 成功刷新令牌后触发。
*   `onAuthRefreshError`: `() => void` - 当 `updateToken()` 刷新失败时触发。
*   `onTokenExpired`: `() => void` - 当 Access Token 过期时触发。你可以在这里调用 `updateToken()`。
*   `onAuthLogout`: `() => void` - 当用户登出时（例如在其他标签页登出，通过 SSO 检测到）触发。

**使用示例**：
```javascript
keycloak.onTokenExpired = () => {
  console.log('Token expired, trying to refresh...');
  keycloak.updateToken(30).catch(() => {
    console.error('Failed to refresh token');
    keycloak.logout();
  });
};
```

---

### 类别七：URL 生成器 (URL Generators)

这些方法不执行重定向，而是返回一个可用于 `<a>` 标签 `href` 属性或自定义逻辑的 URL 字符串。

*   `createLoginUrl(options)`
*   `createLogoutUrl(options)`
*   `createRegisterUrl(options)`
*   `createAccountUrl(options)`

所有这些方法的 `options` 参数与它们对应的执行方法（`login`, `logout` 等）完全相同。

### 总结

`keycloak-js` 的方法和属性共同构成了一个完整的认证授权生命周期管理工具：
1.  用 `init()` 启动并确定初始状态。
2.  用 `login()`, `logout()`, `register()` 改变用户会话。
3.  认证后，通过 `token`, `idTokenParsed` 等属性获取用户信息和凭证。
4.  用 `hasRealmRole()` / `hasResourceRole()` 控制前端 UI。
5.  在调用 API 前，用 `updateToken()` 确保持续的会话有效性。
6.  用事件回调来响应认证状态的变化。