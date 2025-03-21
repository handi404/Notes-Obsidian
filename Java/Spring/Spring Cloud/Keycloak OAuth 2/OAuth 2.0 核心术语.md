**OAuth 2.0 核心术语速览**：

1. **角色与组件**  
   - **资源所有者（Resource Owner）**：用户（如微信账号持有者）。  
   - **客户端（Client）**：第三方应用（如用微信登录的 App）。  
   - **授权服务器（Authorization Server）**：颁发令牌的服务器（如微信登录授权页）。还将验证访问令牌是否有效，市场上有许多可用于授权服务器的选项:
	   - Optionsin Market：
		   - AWS Cognito
		   - Microsoft Azure AD
		   - Google ldentity Platform
		   - OKTA
		   - Key Cloak
		   - Spring Authorization Serven
	- 如果你想在自己的本地管理授权，key cloak 是一个非常好的选择，它是一个开源产品。
   - **资源服务器（Resource Server）**：存储用户资源的服务器（如微信存储用户头像的服务器）。  

2. **令牌与凭证**  
   - **访问令牌（Access Token）**：客户端访问资源的“钥匙”，有时效性。  
   - **刷新令牌（Refresh Token）**：用于刷新过期的访问令牌（更安全）。  
   - **授权码（Authorization Code）**：临时凭证，客户端用它换取访问令牌（防中间人攻击）。  

3. **授权流程类型（Grant Types）**  
   - **授权码模式（Authorization Code）**：Web 应用常用，通过授权码中转令牌（最安全）。  
   - **隐式模式（Implicit）**：直接返回令牌（如单页应用），安全性较低，已逐渐被淘汰。  
   - **密码模式（Resource Owner Password Credentials）**：用户直接提供账号密码给客户端（高风险，仅信任场景使用）。  
   - **客户端凭证模式（Client Credentials）**：服务端间互信认证（如内部 API 调用）。  
   - **PKCE（Proof Key for Code Exchange）**：增强授权码模式，防止移动/SPA 应用授权码劫持。  

4. **其他关键术语**  
   - **Scope（权限范围）**：用户授权给客户端的权限粒度（如仅读取头像）。  
   - **Redirect URI（回调地址）**：授权后跳转的 URL（防钓鱼攻击）。  
   - **Client ID/Secret**：客户端的身份凭证（类似 App 的账号密码）。  

**一句话总结**：OAuth 2.0 通过角色分工、令牌机制和多种授权流程，实现安全的第三方资源访问，核心是**不暴露用户密码**的前提下完成权限代授。