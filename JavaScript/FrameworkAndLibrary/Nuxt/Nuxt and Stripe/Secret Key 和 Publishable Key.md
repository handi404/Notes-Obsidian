在 Stripe 支付系统中，**Secret Key** 和 **Publishable Key** 是两种不同权限的 API 密钥，它们的核心区别在于**使用场景和权限范围**。以下是详细说明：

---

### **1. Secret Key（密钥）**
- **作用**：  
  - 用于**后端服务器**与 Stripe 的 API 交互，执行敏感或高权限操作。
  - 可以访问所有 Stripe API 接口，例如：
    - 创建支付意图（PaymentIntent）
    - 管理客户（Customer）、订阅（Subscription）
    - 处理退款（Refund）
    - 查询交易记录等。
- **权限级别**：  
  **完全访问权限**（Full access），相当于“管理员权限”。
- **安全性要求**：  
  **必须严格保密**，**绝不能暴露在客户端（如浏览器、移动 App）或公开代码中**。如果泄露，攻击者可能直接操控你的账户资金。

**示例用法（后端代码片段）**：
```javascript
// Node.js 示例（后端）
const stripe = require('stripe')('sk_test_XXXXXXXXXXXXXXXXXXXXXXXX'); // Secret Key

app.post('/create-payment-intent', async (req, res) => {
  const paymentIntent = await stripe.paymentIntents.create({
    amount: 1099,
    currency: 'usd',
  });
  res.send(paymentIntent.client_secret);
});
```

---

### **2. Publishable Key（可发布密钥）**
- **作用**：  
  - 用于**前端客户端**（如网页、移动端）初始化 Stripe SDK 或 Elements 组件，收集支付信息。
  - 主要用于生成一次性支付令牌（Token）或调用低权限的公共 API。
  - 例如：
    - 使用 Stripe Elements 收集信用卡信息。
    - 获取可用付款方式（如 Apple Pay）。
- **权限级别**：  
  **受限访问权限**（Restricted access），只能执行安全的、非敏感操作。
- **安全性要求**：  
  可以安全地嵌入在前端代码中（如 HTML/JavaScript），但仍需避免滥用（例如防止恶意网站盗用你的 Key 产生垃圾流量）。

**示例用法（前端代码片段）**：
```html
<!-- HTML + JavaScript 示例（前端） -->
<script src="https://js.stripe.com/v3/"></script>
<script>
  const stripe = Stripe('pk_test_XXXXXXXXXXXXXXXXXXXXXXXX'); // Publishable Key

  // 使用 Stripe Elements 创建信用卡输入框
  const elements = stripe.elements();
  const card = elements.create('card');
  card.mount('#card-element');
</script>
```

---

### **关键区别总结**

| **特性**              | **Secret Key**                          | **Publishable Key**                     |
|-----------------------|-----------------------------------------|-----------------------------------------|
| **使用位置**          | 后端服务器（Node.js、Python、Java 等）   | 前端客户端（浏览器、移动端）            |
| **权限范围**          | 完全访问所有 API（包括修改资金）         | 仅限安全操作（如收集支付信息）          |
| **是否可公开**        | **绝对不可公开**                        | **可以公开**（但需合理防护）            |
| **泄露后果**          | 账户资金可能被盗用                      | 他人可能滥用你的 Key 生成无效请求       |
| **测试环境 Key**      | `sk_test_` 开头                         | `pk_test_` 开头                         |
| **生产环境 Key**      | `sk_live_` 开头                         | `pk_live_` 开头                         |

---

### **安全建议**
1. **Secret Key 必须保护**：
   - 不要提交到代码仓库（加入 `.gitignore`）。
   - 使用环境变量（如 `process.env.STRIPE_SECRET_KEY`）注入后端代码。
   - 避免在浏览器控制台、日志中打印。
2. **Publishable Key 的防护**：
   - 在 Stripe Dashboard 中设置**允许的域名**（Whitelist），防止恶意网站盗用。
   - 使用测试 Key（`pk_test_`）进行开发，上线时替换为生产 Key（`pk_live_`）。
3. **最小权限原则**：
   - 如果需要更细粒度的权限控制，可使用 Stripe 的 [Restricted Keys](https://stripe.com/docs/keys#limit-access) 功能。

---

### **典型工作流**
1. 前端用 **Publishable Key** 初始化 Stripe SDK，收集用户支付信息。
2. 前端将支付信息（如信用卡号）通过 Stripe SDK 提交，生成一次性 Token。
3. 前端将 Token 发送到后端。
4. 后端用 **Secret Key** 调用 Stripe API，完成支付（如创建 PaymentIntent）。

通过这种设计，敏感操作始终由后端控制，前端仅负责信息收集，从而保障安全性。