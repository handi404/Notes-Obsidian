---
title: "JWT.IO - JSON Web Tokens Introduction"
source: "https://jwt.io/introduction"
author:
  - "[[auth0.com]]"
published:
created: 2025-04-19
description: "Learn about JSON Web Tokens, what are they, how they work, when and why you should use them."
tags:
  - "clippings"
---
## 什么是 JSON Web Token？

JSON Web Token (JWT) 是一个开放标准 ( [RFC 7519](https://tools.ietf.org/html/rfc7519) )，它定义了一种紧凑且自包含的方式，用于在各方之间安全地以 JSON 对象的形式传输信息。由于这些信息经过数字签名，因此可以被验证和信任。JWT 可以使用密钥（采用 **HMAC** 算法）或使用 **RSA** 或 **ECDSA 的** 公钥/私钥对进行签名。

虽然 JWT 可以通过加密来保障各方之间的隐私，但我们将重点介绍 *signed(签名)* 令牌。签名令牌可以验证其所含声明的 *完整性* ，而加密令牌则会向其他方 *隐藏* 这些声明。使用公钥/私钥对对令牌进行签名时，签名还能证明只有持有私钥的一方才是签名者。

## 何时应该使用 JSON Web Tokens？

以下是 JSON Web Tokens 有用的一些场景：

- **Authorization(授权)** ：这是使用 JWT 最常见的场景。用户登录后，每个后续请求都将包含 JWT，允许用户访问该令牌允许的路由、服务和资源。单点登录是如今 JWT 广泛使用的一项功能，因为它开销小，并且易于跨域使用。
- **Information Exchange(信息交换)** ：JSON Web Token 是各方之间安全传输信息的有效方式。由于 JWT 可以签名（例如，使用公钥/私钥对），因此您可以确保发送者的身份与其声明相符。此外，由于签名是使用标头和有效负载计算得出的，因此您还可以验证内容未被篡改。

## JSON Web Token 结构是什么？

在其紧凑形式中，JSON Web Tokens 由三部分组成，用点（ `.` ）分隔，它们是：

- Header 标头
- Payload 有效载荷
- Signature 签名

因此，JWT 通常如下所示。

`xxxxx.yyyyy.zzzzz`

让我们分解一下不同的部分。

### Header 标头

标头 *通常* 由两部分组成：令牌的类型（即 JWT）和正在使用的签名算法（例如 HMAC SHA256 或 RSA）。

例如：

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

然后，将此 JSON 经过 **Base64Url** 编码，形成 JWT 的第一部分。

### Payload 有效载荷

令牌的第二部分是有效负载，其中包含声明。声明是关于实体（通常是用户）及其附加数据的声明。声明分为三种类型： *已注册声明* 、 *公共声明* 和 *私有* 声明。

- [**Registered claims(注册声明)**](https://tools.ietf.org/html/rfc7519#section-4.1) ：这些是一组预定义的声明，虽然不是强制性的，但建议使用，以提供一组有用且可互操作的声明。其中包括： **iss** （issuer(颁发者)）、 **exp** （expiration time(到期时间)）、 **sub** （subject(主题)）、 **aud** （audience(受众)） [等](https://tools.ietf.org/html/rfc7519#section-4.1) 。
	
	> 请注意，由于 JWT 要求紧凑，因此声明名称只有三个字符。
- [**Public claims(公开声明)**](https://tools.ietf.org/html/rfc7519#section-4.2) ：JWT 用户可随意定义这些声明。但为了避免冲突，应在 [IANA JSON Web Token 注册表](https://www.iana.org/assignments/jwt/jwt.xhtml) 中定义，或将其定义为包含抗冲突命名空间的 URI。
- [**Private claims(私人声明)**](https://tools.ietf.org/html/rfc7519#section-4.3) ：这些是为在同意使用它们的各方之间共享信息而创建的自定义声明，既不是 *注册* 声明也不是 *公开* 声明。

payload 示例如下：

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

The payload is then **Base64Url** encoded to form the second part of the JSON Web Token.  
然后对 payload 进行 **Base64Url** 编码以形成 JSON Web Token 的第二部分。

> 请注意，对于签名令牌，这些信息虽然受到保护以防止篡改，但任何人都可以读取。除非已加密，否则请勿将机密信息放入 JWT 的有效负载或标头元素中。

### Signature 签名

要创建签名部分，您必须获取编码的标头、编码的有效负载、secret(密钥)、标头中指定的算法，然后对其进行签名。

例如，如果您想使用 HMAC SHA256 算法，则签名将以以下方式创建：

```javascript
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```

签名用于验证消息在传输过程中未被更改，并且在使用私钥签名的令牌的情况下，它还可以验证 JWT 的发件人是否是它所声称的身份。

### 整合

输出是三个由点分隔的 Base64-URL 字符串，可以在 HTML 和 HTTP 环境中轻松传递，同时与基于 XML 的标准（如 SAML）相比更加紧凑。

The following shows a JWT that has the previous header and payload encoded, and it is signed with a secret.  
下面显示了一个 JWT，该 JWT 对之前的标头和有效负载进行了编码，并使用密钥进行了签名。 ![Encoded JWT](https://cdn.auth0.com/content/jwt/encoded-jwt3.png)

如果您想使用 JWT 并将这些概念付诸实践，您可以使用 [jwt.io Debugger](https://jwt.io/#debugger-io) 来解码、验证和生成 JWT。

![JWT.io Debugger](https://cdn.auth0.com/website/jwt/introduction/debugger.png)

## JSON Web Tokens 如何工作？

在身份验证中，当用户使用其凭证成功登录时，将返回一个 JSON Web Token。由于 token 是凭证，因此必须格外小心，以防止出现安全问题。通常情况下，您不应将 token 保留超过规定时间。

[由于缺乏安全性，您也不应将敏感的会话数据存储在浏览器 storage(存储) 中](https://cheatsheetseries.owasp.org/cheatsheets/HTML5_Security_Cheat_Sheet.html#local-storage) 。

每当用户想要访问受保护的路由或资源时，用户代理都应该发送 JWT，通常在 **Authorization** 标头中使用 **Bearer** 模式。标头内容应如下所示：

```
Authorization: Bearer <token>
```

在某些情况下，这可以是一种无状态授权机制。服务器的受保护路由将检查 `Authorization` 标头中是否存在有效的 JWT，如果存在，则允许用户访问受保护的资源。如果 JWT 包含必要的数据，则某些操作查询数据库的需要可能会减少，尽管情况并非总是如此。

请注意，如果您通过 HTTP 标头发送 JWT 令牌，则应尽量避免其过大。某些服务器不接受超过 8 KB 的标头。如果您尝试在 JWT 令牌中嵌入过多信息（例如包含所有用户权限），则可能需要其他解决方案，例如 [Auth0 Fine-Grained Authorization](https://fga.dev/)。

如果在 `Authorization` 标头中发送令牌，则跨域资源共享（CORS）不会成为问题，因为它不使用 cookie。

下图展示了如何获取 JWT 以及如何使用它来访问 API 或资源：

![How does a JSON Web Token work](https://cdn.auth0.com/website/jwt/introduction/client-credentials-grant.png)

1. 应用程序或客户端向授权服务器请求授权。这通过不同的授权流程之一执行。例如，一个典型的符合 [OpenID Connect](http://openid.net/connect/) 标准的 Web 应用程序将使用 [授权码流程](http://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) 通过 `/oauth/authorize` 端点。
2. 当授权被授予时，授权服务器会向应用程序返回 access token(访问令牌)。
3. 应用程序使用访问令牌来访问受保护的资源（如 API）。

请注意，使用签名令牌时，令牌中包含的所有信息都会暴露给用户或其他方，即使他们无法更改这些信息。这意味着您不应在令牌中存放机密信息。

## 为什么我们应该使用 JSON Web Tokens？

让我们来谈谈 **JSON Web Tokens（JWT）** 与 **Simple Web Tokens 简单 Web 令牌（SWT）** 和 **Security Assertion Markup Language Tokens 安全断言标记语言令牌（SAML）** 相比的优势。

由于 JSON 比 XML 简洁，编码后的大小也更小，使得 JWT 比 SAML 更紧凑。这使得 JWT 成为在 HTML 和 HTTP 环境中传递的理想选择。

安全方面，SWT 只能使用 HMAC 算法通过共享密钥进行对称签名。然而，JWT 和 SAML 令牌可以使用 X.509 证书形式的公钥/私钥对进行签名。与对 JSON 进行签名的简单性相比，使用 XML 数字签名对 XML 进行签名而不引入模糊的安全漏洞是非常困难的。

JSON 解析器在大多数编程语言中很常见，因为它们直接映射到对象。相反，XML 没有自然的文档到对象的映射。这使得使用 JWT 比使用 SAML 断言更容易。

就使用范围而言，JWT 已在互联网规模上得到广泛应用。这凸显了 JSON Web Token 在多个平台（尤其是移动平台）上客户端处理的便捷性。

![Comparing the length of an encoded JWT and an encoded SAML](https://cdn.auth0.com/content/jwt/comparing-jwt-vs-saml2.png) *编码后的 JWT 和编码后的 SAML 的长度比较*

如果您想了解有关 JSON Web Tokens 的更多信息，甚至开始使用它们在您自己的应用程序中执行身份验证，请浏览 Auth0 上的 [JSON Web Token landing page](http://auth0.com/learn/json-web-tokens) 。