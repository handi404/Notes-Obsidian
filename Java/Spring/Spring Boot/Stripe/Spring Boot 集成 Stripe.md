一套基于 Spring Boot 的 Stripe 后端集成方案，包括如何使用 Secret Key 实现收费（Charge）、退款（Refund）、订阅管理（Subscriptions）以及访问部分敏感信息（如卡号后四位）。
[[完整示例]]
# Spring Boot 集成 Stripe 后端方案

首先，在 Spring Boot 项目中使用 Stripe，需要引入 Stripe 的 Java SDK 依赖。建议在 **pom.xml** 中添加 Stripe-Java 依赖（下例使用 22.x 最新稳定版本）：

```xml
<dependency>
    <groupId>com.stripe</groupId>
    <artifactId>stripe-java</artifactId>
    <version>22.29.0</version>  <!-- 或根据需要使用最新版本 -->
</dependency>
```

接着，需要配置 Stripe 的 **Secret Key**（私钥）。在 Spring Boot 的 `application.properties` 或 `application.yml` 中添加配置，例如：

```properties
stripe.secret-key=sk_test_yourSecretKeyHere
```

然后，在代码中通过 `@Value` 注入该配置并设置给 `Stripe.apiKey`。例如：

```java
@Service
public class StripeConfig {
    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // 初始化 Stripe API，设置 Secret Key
        Stripe.apiKey = secretKey;
    }
}
```

这样 Spring 启动时会自动为 Stripe SDK 设置好私钥。务必**不要**将私钥硬编码在代码中，也不要提交到版本库；生产环境中更推荐使用环境变量或机密管理服务存储 API 密钥。

## 创建一次性收费（Charge）

使用 Stripe 的 Charges API 可以对客户进行一次性收费。示例代码如下：

```java
// 构建收费参数
ChargeCreateParams params = ChargeCreateParams.builder()
    .setAmount(1099L)          // 费用金额（单位为最小货币单位，如美元的分）
    .setCurrency("usd")        // 币种
    .setSource("tok_visa")     // 支付源（可以是 Stripe Token、PaymentMethod 或已有的来源 ID）
    .setDescription("测试收费") // 可选描述
    .build();

// 发起收费
Charge charge = Charge.create(params);
```

以上代码通过 `Charge.create(params)` 向 Stripe 发起收费请求。注意 **private key** 必须在调用前已设置，否则请求会失败。调用成功后，`charge` 对象包含交易详情。对于新型支付场景，Stripe 推荐使用 PaymentIntent 方式处理 3D Secure 验证和 SCA，但这里示例使用了传统的 Charges API。

## 发起退款（Refund）

要对已完成的收费进行退款，可使用 Stripe 的 Refunds API。示例代码：

```java
// 指定要退款的 Charge ID
String chargeId = "ch_ABC123xyz";

// 构建退款参数（可选指定部分金额退款）
RefundCreateParams params = RefundCreateParams.builder()
    .setCharge(chargeId)
    // .setAmount(500L)  // 如果需要部分退款，可设置金额（单位与收费时一致）
    .build();

// 创建退款
Refund refund = Refund.create(params);
```

以上代码通过 `Refund.create(params)` 向 Stripe 发起退款请求。如果未指定金额，会对整个交易进行全额退款；可以多次调用对不同金额退款，直到剩余金额为零。创建退款后，`refund` 对象会包含退款状态和详情。

## 订阅管理（Subscriptions）

### 创建订阅（Create Subscription）

Stripe 订阅依赖于事先在 Stripe 控制台创建的产品（Product）和价格计划（Price）。示例代码：

```java
public Subscription createSubscription(String customerId, String priceId) throws StripeException {
    SubscriptionCreateParams params = SubscriptionCreateParams.builder()
        .setCustomer(customerId)
        .addItem(SubscriptionCreateParams.Item.builder()
            .setPrice(priceId)  // 使用已有的价格计划 ID
            .build())
        .build();
    // 发起订阅创建请求
    return Subscription.create(params);
}
```

此代码将指定的客户（customerId）按给定价格（priceId）创建订阅。返回的 `Subscription` 对象包含订阅详情（例如状态、账单周期等）。

### 取消订阅（Cancel Subscription）

取消订阅可以调用 Stripe SDK 提供的方法：

```java
public Subscription cancelSubscription(String subscriptionId) throws StripeException {
    Subscription subscription = Subscription.retrieve(subscriptionId);
    return subscription.cancel();
}
```

以上通过 `Subscription.retrieve()` 获取订阅对象，然后调用 `cancel()` 方法取消订阅。取消后订阅状态将更新为已取消，并且在当前账单周期结束后停止计费。

### 更新订阅（Update Subscription）

更新订阅最常见的是更换价格计划（proration 默认开启）或添加/移除优惠券等。示例代码（更换价格计划）：

```java
public Subscription updateSubscription(String subscriptionId, String newPriceId) throws StripeException {
    Subscription subscription = Subscription.retrieve(subscriptionId);
    SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
        .addItem(SubscriptionUpdateParams.Item.builder()
            .setId(subscription.getItems().getData().get(0).getId()) // 当前订阅项 ID
            .setPrice(newPriceId)    // 更新为新价格 ID
            .build())
        .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.CREATE_INVOICE) // 按规则 proration
        .build();
    return subscription.update(params);
}
```

以上代码会将订阅中的第一项价格更新为 `newPriceId`，并生成新的账单差额。`SubscriptionUpdateParams` 支持更复杂的设置，如修改收费周期、应用折扣等。

## 获取卡片后四位等敏感信息

Stripe 允许您获取卡的 **后四位**等非敏感信息，但**不可**获取完整卡号、CVC 等敏感数据。获取卡片信息的方式取决于卡片是如何与客户关联的：

- 如果在付款时使用了 PaymentMethod：可以调用 `PaymentMethod.retrieve(paymentMethodId)`，然后使用返回的 `PaymentMethod` 对象访问卡片信息。例如：
    
    ```java
    PaymentMethod pm = PaymentMethod.retrieve("pm_xxx");
    String last4 = pm.getCard().getLast4();
    ```
    
- 如果要从已有的 Charge 中获取卡后四位，也可先 `Charge.retrieve(chargeId)`，再通过 `charge.getPaymentMethodDetails().getCard().getLast4()`（或类似方法）取得。但 **前提**是支付时产生的 Charge 对象包含卡详情。
    

以上操作均只会返回卡片的部分信息（如后四位、到期月份等），符合 PCI 合规要求。无论哪种方法，您都无需也不应直接接触到完整的卡号或 CVC，所有敏感数据均由 Stripe 安全存储。

## 关键注意事项与安全实践

- **密钥管理**：绝不可将 Stripe Secret Key 硬编码在代码或提交到版本库。推荐使用环境变量、机密管理服务或 Spring Cloud Config 等安全存储机制。定期轮换密钥，并仅在后端安全环境使用 Secret Key。前端页面只能使用 Publishable Key，不要泄露 Secret Key。
    
- **测试模式**：开发时使用 Stripe 提供的测试密钥（以 `sk_test_` 开头），并使用 Stripe 提供的测试卡号进行测试，以避免真实扣款。切勿在生产环境误用测试密钥，反之亦然。
    
- **PCI 合规**：后端使用 Stripe SDK 并不直接处理或存储信用卡完整信息，只处理 Token 或 PaymentMethod ID，符合 PCI 要求。尽量使用 Stripe Elements 或 Checkout 等前端组件收集卡信息，这样卡信息不会经过您的服务器。
    
- **Webhooks 验证**：对订阅等事件，建议配置 Stripe Webhooks，以便实时获知支付成功、订阅到期、欠费等事件，并在服务器端做相应处理（如更新数据库状态）。
    
- **并发与幂等性**：对可能重复请求的接口（如收费接口），应使用 Stripe 的幂等请求功能（Idempotency Keys），避免因重试导致重复扣款。
    
- **安全最佳实践**：切记按照最小权限原则操作。不要返回或日志记录敏感字段。参考 Stripe 官方及安全社区建议，避免泄露任何敏感信息。
    

通过以上步骤，您可以在 Spring Boot 后端中完整集成 Stripe 支付功能，安全地对接一次性收费、退款以及订阅服务等操作。

**参考资料：** Stripe 官方文档、Kinsta 与 OMI 博客等[docs.stripe.com](https://docs.stripe.com/api/charges/create?lang=java#:~:text=ChargeCreateParams%20params%20%3D)[docs.stripe.com](https://docs.stripe.com/api/refunds/create?lang=java#:~:text=RefundCreateParams%20params%20%3D)[omi.me](https://www.omi.me/blogs/api-guides/how-to-use-stripe-billing-api-for-subscription-management-in-java#:~:text=public%20class%20SubscriptionService%20)[omi.me](https://www.omi.me/blogs/api-guides/how-to-use-stripe-billing-api-for-subscription-management-in-java#:~:text=SubscriptionUpdateParams%20params%20%3D%20SubscriptionUpdateParams)[gitguardian.com](https://www.gitguardian.com/remediation/stripe-secret-key#:~:text=,term%20exposure)。