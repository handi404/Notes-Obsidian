以下是对 Spring Boot 中邮件（Mail）功能所有可配置属性的通俗易懂、简洁且完整的讲解。所有属性均可在 `application.properties` 或 `application.yml` 中配置，Spring Boot 会自动将它们注入到底层的 `JavaMailSenderImpl`。

---

## 一、基础连接属性

|属性|说明|示例|
|---|---|---|
|`spring.mail.host`|SMTP 服务器主机地址|`smtp.example.com`|
|`spring.mail.port`|SMTP 服务器端口|`25`、`587`（STARTTLS）、`465`（SSL）|
|`spring.mail.protocol`|通讯协议，默认 `smtp`|`smtp`、`smtps`|
|`spring.mail.username`|用于认证的用户名（邮箱账号）|`user@example.com`|
|`spring.mail.password`|用于认证的密码或授权码|`secret`|
|`spring.mail.default-encoding`|邮件内容默认字符编码|`UTF-8`|
|`spring.mail.test-connection`|启动时是否测试与 SMTP 的连接，默认 `false`|`true`|

```yaml
spring:
  mail:
    host: smtp.example.com
    port: 587
    protocol: smtp
    username: user@example.com
    password: secret
    default-encoding: UTF-8
    test-connection: true
```

---

## 二、JavaMail 原生属性

通过 `spring.mail.properties.mail.*` 前缀，将下列 JavaMail 属性直接传给 `Session`：

|属性|说明|示例|
|---|---|---|
|`mail.smtp.auth`|是否开启 SMTP 身份验证（`true` 则使用 username/password）|`true`|
|`mail.smtp.starttls.enable`|是否启用 STARTTLS（在明文端口上升级到加密通道）|`true`|
|`mail.smtp.starttls.required`|是否强制要求 STARTTLS|`false`|
|`mail.smtp.ssl.enable`|是否启用 SSL（通常对应端口 465）|`false`|
|`mail.smtp.ssl.trust`|信任哪些主机的 SSL 证书；`*` 表示信任所有|`smtp.example.com`|
|`mail.smtp.connectiontimeout`|建立 socket 连接的超时时间（毫秒）|`5000`|
|`mail.smtp.timeout`|读取服务器响应的超时时间（毫秒）|`3000`|
|`mail.smtp.writetimeout`|向服务器发送数据的超时时间（毫秒）|`5000`|
|`mail.smtp.socketFactory.port`|指定 SSL SocketFactory 端口（若需单独指定）|`465`|
|`mail.smtp.socketFactory.class`|指定 SSL SocketFactory 类名（一般无需修改）|`javax.net.ssl.SSLSocketFactory`|
|`mail.debug`|是否输出 JavaMail 调试日志，便于排查问题|`true`|

```yaml
spring:
  mail:
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: false
          ssl:
            enable: false
            trust: "*"
          connectiontimeout: 5000
          timeout: 3000
          writetimeout: 5000
          socketFactory:
            port: 465
            class: javax.net.ssl.SSLSocketFactory
        debug: true
```

---

## 三、使用示例

1. **带 STARTTLS 的普通 SMTP（端口 587）**
    
    ```yaml
    spring:
      mail:
        host: smtp.example.com
        port: 587
        username: user
        password: pass
        properties:
          mail:
            smtp:
              auth: true
              starttls:
                enable: true
    ```
    
2. **SSL 直连（端口 465）**
    
    ```yaml
    spring:
      mail:
        host: smtp.example.com
        port: 465
        protocol: smtps
        username: user
        password: pass
        properties:
          mail:
            smtp:
              auth: true
              ssl:
                enable: true
                trust: smtp.example.com
    ```
    
3. **本地调试（MailHog / FakeSMTP）**
    
    ```yaml
    spring:
      mail:
        host: localhost
        port: 1025
        username: any
        password: any
        properties:
          mail:
            smtp:
              auth: false
    ```
    

---

## 四、小贴士

- **调试连接**：`spring.mail.test-connection=true` 启动时即验证 SMTP 配置是否可用。
- **字符编码**：`default-encoding` 确保中文或特殊字符不乱码。
- **超时设置**：合理配置三阶段超时（连接/读/写），避免网络抖动导致线程长时间挂起。
- **安全策略**：生产环境下建议关闭 `ssl.trust="*"`，改为指定信任的 SMTP 主机。
- **日志追踪**：开启 `mail.debug=true` 可打印底层 SMTP 交互日志，助力排查。

按照以上配置，你即可在 Spring Boot 应用中通过注入 `JavaMailSender`（或使用 `MailSender` 接口）来实现可靠、安全且高效的邮件发送。