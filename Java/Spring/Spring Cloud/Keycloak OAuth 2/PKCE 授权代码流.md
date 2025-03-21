**PKCE 授权代码流**（Proof Key for Code Exchange）是 **OAuth 2.0 授权代码流的安全增强版本**，专为**公共客户端**（如移动应用、单页应用 SPA）设计，防止授权码（Authorization Code）被劫持攻击。核心思想是通过动态生成的密钥对（Code Verifier + Code Challenge）替代传统 `client_secret`，确保只有合法客户端能换取令牌。

---

### **PKCE 流程步骤**
1. **生成密钥对**  
   - **Code Verifier**：客户端生成一个高熵随机字符串（如 64 位字符）。  
   - **Code Challenge**：对 Verifier 进行 SHA-256 哈希并 Base64 编码（称为 `S256` 转换）生成 Challenge。  

2. **发起授权请求**  
   - 客户端将 `code_challenge` 和 `code_challenge_method`（如 `S256`）附加到授权请求中，重定向到授权服务器。  
   ```
   GET /authorize?response_type=code
     &client_id=CLIENT_ID
     &redirect_uri=REDIRECT_URI
     &code_challenge=CHALLENGE
     &code_challenge_method=S256
   ```

3. **用户授权**  
   - 用户登录并同意授权后，授权服务器返回授权码（Authorization Code）到客户端回调地址。

4. **用 Code Verifier 换取令牌**  
   - 客户端向授权服务器发送授权码 **和 Code Verifier**（而非 `client_secret`）：  
   ```
   POST /token
     code=AUTHORIZATION_CODE
     &client_id=CLIENT_ID
     &redirect_uri=REDIRECT_URI
     &code_verifier=VERIFIER
   ```

5. **验证并颁发令牌**  
   - 授权服务器验证 `code_verifier` 哈希后是否与最初提交的 `code_challenge` 一致。  
   - 验证通过后，颁发 Access Token 和 Refresh Token。

---

### **PKCE 的核心价值**
6. **防中间人攻击**  
   - 即使攻击者截获授权码，因无法获取 `code_verifier`，无法换取有效令牌。  
7. **替代 client_secret**  
   - 公共客户端（如移动端）无法安全存储 `client_secret`，PKCE 通过动态密钥消除依赖。  
8. **兼容性**  
   - 与标准授权代码流无缝兼容，仅需客户端和授权服务器支持 PKCE 扩展。

---

### **适用场景**
- **移动应用**：无法保护 `client_secret` 的 Native App。  
- **单页应用（SPA）**：前端代码暴露风险高的场景（如 React/Vue 应用）。  
- **无后端服务**：纯前端应用（如静态网站）的 OAuth 安全实现。  

---

**一句话总结**：PKCE 是 OAuth 2.0 的“安全补丁”，通过动态密钥对 **（Challenge + Verifier）** 确保公共客户端的授权流程安全，避免授权码劫持漏洞。