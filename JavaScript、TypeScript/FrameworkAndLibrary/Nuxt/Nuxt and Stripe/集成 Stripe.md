整理一个完整的 Nuxt 3 + TypeScript 集成 Stripe 支付流程，包括如何使用 Publishable Key 结合 Stripe Elements 和 Checkout 收集卡片信息、创建 PaymentMethod，以及如何调用你已有的 Spring Boot 后端接口（如 /charge、/subscribe）。

# Stripe 支付流程前端实现（Nuxt 3 + TypeScript）

本方案演示如何在 Nuxt 3 前端中集成 Stripe 支付，包括 Stripe.js 初始化、使用 Stripe Elements 创建信用卡表单、生成支付凭证并调用后端 API（如 `/charge` 或 `/subscribe`），以及可选的 Stripe Checkout 重定向支付。以下内容假设您已在 Nuxt 项目中安装了 `@stripe/stripe-js` 库，并在 `.env` 中配置了 Stripe 公钥（例如 `NUXT_STRIPE_PUBLIC_KEY=pk_test_...`）。

## 1. 集成 Stripe.js（通过 Nuxt 插件）

- **配置公钥环境变量**：在 `nuxt.config.ts` 中加入运行时配置（或使用 `.env`），例如：
    
    ```ts
    // nuxt.config.ts
    export default defineNuxtConfig({
      runtimeConfig: {
        public: {
          stripePublishableKey: process.env.STRIPE_PUBLIC_KEY  // Stripe 公钥
        }
      }
    })
    ```
    
- **创建 Nuxt 插件**：在 `plugins/stripe.client.ts`（`.client.ts` 表示仅在客户端加载）中使用 `@stripe/stripe-js` 提供的 `loadStripe` 方法初始化 Stripe 实例。示例如下：
    
    ```ts
    // plugins/stripe.client.ts
    import { defineNuxtPlugin, useRuntimeConfig } from '#app'
    import { loadStripe } from '@stripe/stripe-js'
    import type { Stripe } from '@stripe/stripe-js'
    
    export default defineNuxtPlugin(async (nuxtApp) => {
      const config = useRuntimeConfig()
      const stripeKey = config.public.stripePublishableKey as string
      if (!stripeKey) {
        throw new Error('Missing Stripe publishable key')
      }
      // 异步加载 Stripe.js 并初始化 Stripe 实例
      const stripe = await loadStripe(stripeKey)
      if (!stripe) {
        throw new Error('Failed to initialize Stripe')
      }
      // 将 Stripe 实例注入 Nuxt 应用（可在组件中通过 useNuxtApp().$stripe 访问）
      nuxtApp.provide('stripe', stripe)
    })
    ```
    
> **说明：** `loadStripe` 会异步加载 `https://js.stripe.com/v3/` 并返回一个 `Stripe` 对象，可用于后续调用 [docs.stripe.com](https://docs.stripe.com/payments/accept-a-payment-charges#:~:text=Use%20Stripe%20Elements%2C%20our%20prebuilt,token%20to%20create%20a%20charge)。我们在插件中通过 `nuxtApp.provide('stripe', stripe)` 将其注入为 `$stripe`。同时建议在 TypeScript 项目中通过声明文件扩展 `NuxtApp` 接口，以便正确提示类型，如：
> 
> ```ts
> declare module '#app' {
>   interface NuxtApp {
>     $stripe: Stripe
>   }
> }
> ```

## 2. 使用 Stripe Elements 创建卡片表单

Stripe Elements 是 Stripe 提供的一组预构建支付表单组件，可直接嵌入页面用于安全收集银行卡信息 [docs.stripe.com](https://docs.stripe.com/payments/accept-a-payment-charges#:~:text=Use%20Stripe%20Elements%2C%20our%20prebuilt,token%20to%20create%20a%20charge)。下面以 `pages/checkout.vue` 为例，展示如何使用 Elements 创建卡号、有效期、安全码输入框。

```vue
<template>
  <form @submit.prevent="onSubmit">
    <div>
      <label>信用卡卡号</label>
      <div id="card-number" class="stripe-input"></div>
      <p v-if="cardNumberError" class="error-text">{{ cardNumberError }}</p>
    </div>
    <div>
      <label>有效期</label>
      <div id="card-expiry" class="stripe-input"></div>
      <p v-if="cardExpiryError" class="error-text">{{ cardExpiryError }}</p>
    </div>
    <div>
      <label>CVC</label>
      <div id="card-cvc" class="stripe-input"></div>
      <p v-if="cardCvcError" class="error-text">{{ cardCvcError }}</p>
    </div>
    <button type="submit" :disabled="isProcessing">支付</button>
  </form>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { StripeCardNumberElement, StripeCardExpiryElement, StripeCardCvcElement } from '@stripe/stripe-js'

// 通过 useNuxtApp 获取注入的 Stripe 实例
const nuxtApp = useNuxtApp()
const stripe = nuxtApp.$stripe
if (!stripe) throw new Error('Stripe 未初始化')

// 表单校验信息
const cardNumberError = ref('')
const cardExpiryError = ref('')
const cardCvcError = ref('')
const isProcessing = ref(false)

// 定义 Element 对象
let cardNumberElement: StripeCardNumberElement
let cardExpiryElement: StripeCardExpiryElement
let cardCvcElement: StripeCardCvcElement

onMounted(() => {
  // 创建 Elements 实例并挂载组件
  const elements = stripe.elements({ locale: 'zh' })
  const style = {
    base: {
      color: '#303238',
      fontSize: '16px',
      '::placeholder': { color: '#aab7c4' }
    },
    invalid: { color: '#e5424d' }
  }
  const classes = { base: 'stripe-element', focus: 'stripe-element--focus', invalid: 'stripe-element--invalid' }

  cardNumberElement = elements.create('cardNumber', { style, classes })
  cardNumberElement.mount('#card-number')
  cardNumberElement.on('change', (e) => {
    cardNumberError.value = e.error?.message || ''
  })

  cardExpiryElement = elements.create('cardExpiry', { style, classes })
  cardExpiryElement.mount('#card-expiry')
  cardExpiryElement.on('change', (e) => {
    cardExpiryError.value = e.error?.message || ''
  })

  cardCvcElement = elements.create('cardCvc', { style, classes })
  cardCvcElement.mount('#card-cvc')
  cardCvcElement.on('change', (e) => {
    cardCvcError.value = e.error?.message || ''
  })
})
</script>

<style>
.stripe-input { padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; }
.error-text { color: #e5424d; font-size: 0.9em; }
</style>
```

- **说明：** 在上例中，我们在页面模板中用 `<div id="card-number">` 等占位符加载 Stripe Elements 卡号、有效期和 CVC 输入框。在 `<script setup>` 中，通过 `stripe.elements()` 创建 `elements` 实例，然后调用 `elements.create('cardNumber')` 等方法生成相应元素并挂载到 DOM 上。`on('change')` 事件用于实时校验并显示错误信息。这样就可以在不直接处理敏感卡片数据的前提下收集用户输入。
    

## 3. 生成 PaymentMethod/Token 并提交后端

用户点击「支付」按钮时，前端代码可调用 Stripe 的方法将收集到的卡信息转换为支付凭证，然后通过 Fetch 或 Axios 发送给后端进行收费或订阅。常用的方法有：

- **PaymentMethod 模式（推荐）：** 调用 `stripe.createPaymentMethod({ type: 'card', card: cardElement })` 生成一个 `PaymentMethod` 对象，该对象包含一个 `id`。然后将该 `id` 发送到后端。
    
- **Token 模式（旧版 Charges API）：** 调用 `stripe.createToken(cardElement)` 生成一个用于收费的 Token，然后发送给后端。此处以 PaymentMethod 为例说明。
    

在组件中添加提交逻辑，例如：

```ts
<script setup lang="ts">
// ... 上文的 imports 和 onMounted 内容 ...

async function onSubmit() {
  if (isProcessing.value) return
  isProcessing.value = true
  cardNumberError.value = ''
  // 使用 stripe.createPaymentMethod 将卡信息转换为 PaymentMethod
  const { paymentMethod, error } = await stripe.createPaymentMethod({
    type: 'card',
    card: cardNumberElement
  })
  if (error || !paymentMethod) {
    cardNumberError.value = error?.message || '支付信息无效'
    isProcessing.value = false
    return
  }
  try {
    // 调用后端接口完成支付（以使用 $fetch 为例）
    const res = await $fetch('/api/charge', {
      method: 'POST',
      body: {
        paymentMethodId: paymentMethod.id,
        amount: 1000  // 以分为单位的金额，例如 1000 分 = 10.00 元
      }
    })
    // TODO: 根据 res 结果处理成功逻辑
  } catch (err: any) {
    console.error('支付请求失败：', err)
    // 可以展示错误提示
  } finally {
    isProcessing.value = false
  }
}
</script>
```

- **与后端交互：** 前端通过 `fetch` 或 `axios` 将 `paymentMethod.id`（或 Token）和其他必要参数（如金额、订阅计划等）发送到后端接口。后端根据接收到的 ID 调用 Stripe 服务器端 API 完成收费或创建订阅。示例调用：
    
    ```ts
    // 使用 axios 调用示例
    await axios.post('/charge', { paymentMethodId: paymentMethod.id, amount: 1000 })
    // 或订阅接口
    await axios.post('/subscribe', { paymentMethodId: paymentMethod.id, planId: 'pro_monthly' })
    ```
    
    后端接口（Spring Boot）示例可参考 Stripe 官方文档。总之，前端无需直接处理卡号数据，只需将从 Elements 获取的凭证发送给后端。
    

## 4. 可选：Stripe Checkout 重定向支付

如果使用 **Stripe Checkout**（托管式支付页面），则前端流程会有所不同：前端发起请求让后端创建一个 Checkout Session，得到 Session ID 后调用 `stripe.redirectToCheckout` 实现跳转。例如：

```ts
<script setup lang="ts">
async function onCheckout() {
  // 调用后端创建 Checkout Session，假设后端接口为 /create-checkout-session
  const session = await $fetch('/api/create-checkout-session', {
    method: 'POST',
    body: {
      items: [{ priceId: 'price_123', quantity: 1 }]  // 商品信息
    }
  })
  // 使用 Stripe 实例跳转到 Stripe 托管的结算页面
  const { error } = await stripe.redirectToCheckout({ sessionId: session.id })
  if (error) {
    console.error('跳转 Checkout 失败：', error.message)
  }
}
</script>
```

- **说明：** Stripe Checkout 会在后端创建 Session，并返回一个 `session.id`。前端通过注入的 Stripe 实例调用 `stripe.redirectToCheckout({ sessionId })`，即可重定向到 Stripe 提供的支付页面完成支付。无需在前端创建表单或调用 `createPaymentMethod`，大大简化流程。
    

## 5. Nuxt 3 + TypeScript 组件结构

- **组件结构：** 使用 Nuxt 3 的 `<script setup lang="ts">` 语法编写组件，上例代码即为典型用法。确保安装 `@stripe/stripe-js` 并正确导入类型。
    
- **类型声明：** 可以在项目中添加一个全局声明文件（如 `types/stripe.d.ts`），扩充 NuxtApp 接口，使 `useNuxtApp().$stripe` 拥有类型。例如：
    
    ```ts
    import type { Stripe } from '@stripe/stripe-js'
    declare module '#app' {
      interface NuxtApp {
        $stripe: Stripe
      }
    }
    ```
    
- **错误提示与交互：** 示例中通过绑定响应式变量（如 `cardNumberError`）展示错误提示。也可根据实际需求增加 loading 状态和用户提示。
    
- **代码分块与模块化：** 根据项目需要，可将表单逻辑封装为可复用组件或组合式函数，保持代码整洁。总之，使用 Nuxt 插件注入 Stripe 实例，并结合 Stripe Elements/Checkout API，即可实现完整的前端支付流程。
    

**参考：** Stripe 官方文档介绍了 Elements 的用法及安全性：“Stripe Elements 是预构建的 UI 组件，用于安全收集客户卡片信息，这些信息将被转换为一个令牌（Token）并发送给服务器进行收费”；Nuxt 教程中也建议将 Stripe 公钥作为环境变量配置。以上实现方案综合了这些最佳实践。