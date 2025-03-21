以下是一个简化的 **PKCE 授权代码流**的分步演示，使用 **伪代码** 和 **HTTP 请求示例**，展示 PKCE 的核心流程：

---

### **1. 生成 Code Verifier 和 Challenge**
```bash
# 生成 code_verifier（随机 64 位字符串，Base64 安全字符）
code_verifier = "w2bXx59qE8Kp7LzSfT3rA1vYyZ4uI6oO0jGkHlNqBmC5dP...（示例）"

# 计算 code_challenge（SHA-256 哈希 + Base64 URL 安全编码）
code_challenge = base64url(sha256(code_verifier))
# 结果示例：E9Melhoa2OwvWEM...（固定长度 43 字符）
```

---

### **2. 用户发起授权请求**
用户点击登录按钮，客户端构造授权 URL 并重定向到授权服务器：  
```http
GET https://auth-server.com/authorize?
  response_type=code
  &client_id=my-mobile-app
  &redirect_uri=com.myapp://callback
  &scope=openid profile
  &code_challenge=E9Melhoa2OwvWEM...
  &code_challenge_method=S256
  &state=abc123（防 CSRF）
```

---

### **3. 用户登录并授权**
- 用户跳转到授权服务器的登录页面，输入账号密码。  
- 授权服务器验证用户身份，询问用户是否同意授权（如访问头像、邮箱）。  
- 用户点击 **“允许”**。

---

### **4. 授权服务器返回授权码**
授权服务器生成授权码（`code`）并重定向回客户端回调地址：  
```http
HTTP 302 Redirect
Location: com.myapp://callback?
  code=xyz789（授权码）
  &state=abc123（与请求中的 state 一致）
```

---

### **5. 客户端用 Code Verifier 换取令牌**
客户端向授权服务器的令牌端点发送请求，携带 `code_verifier`：  
```http
POST https://auth-server.com/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=xyz789
&redirect_uri=com.myapp://callback
&client_id=my-mobile-app
&code_verifier=w2bXx59qE8Kp7LzSfT3rA1vYyZ4uI6oO0jGkHlNqBmC5dP...
```

---

### **6. 授权服务器验证并返回令牌**
授权服务器执行以下验证：  
1. 检查 `code` 是否有效且未过期。  
2. 用 `code_verifier` 重新计算 `code_challenge`，与授权请求中的值比对。  
3. 验证 `redirect_uri` 和 `client_id` 是否匹配。  

**响应示例（成功）：**  
```json
{
  "access_token": "eyJhbGciOi...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "def456...",
  "id_token": "eyJ0eXAiOi..."  // OIDC 场景下返回
}
```

---

### **关键安全验证点**
- **Code Verifier 绑定**：攻击者截获 `code` 后，因缺少 `code_verifier` 无法换取令牌。  
- **动态 Challenge**：每次请求生成新的 `code_verifier`，防止重放攻击。  
- **无 client_secret**：公共客户端（如移动端）无需存储密钥，依赖动态 PKCE 保护。  

---

### **实际工具演示（以 Keycloak 为例）**
4. **注册客户端**：在 Keycloak 控制台创建客户端，启用 `PKCE` 选项。  
5. **生成 Verifier/Challenge**：使用前端库（如 `react-oauth2-pkce`）自动处理密钥生成。  
6. **完整流程代码示例**：  
   ```javascript
   // 前端（React）
   import { AuthProvider } from 'react-oauth2-pkce';

   const authService = new AuthProvider({
     clientId: 'my-mobile-app',
     location: window.location,
     provider: 'https://auth-server.com',
     redirectUri: 'com.myapp://callback',
     scopes: ['openid', 'profile']
   });

   // 触发登录
   authService.authorize();

   // 处理回调（自动用 code_verifier 换令牌）
   authService.getToken().then(token => console.log(token));
   ```

---

### **总结**
PKCE 授权代码流通过 **动态密钥对**（Verifier + Challenge）和 **无 client_secret 依赖**，为移动端/SPA 等公共客户端提供安全的 OAuth 2.0 实现。开发者只需确保正确生成和传递 `code_verifier`，即可防御授权码劫持攻击。