# 发布配置

# 获取配置

若使用的是 Spring Boot 2.4+ / Spring Cloud 2020.0.0+ / Spring Cloud Alibaba 2021.1+，**推荐使用 `spring.config.import` 来加载多个配置文件**。

## spring.cofig.import

优先级

**后加载的配置会覆盖先加载的配置**。

⚠️ `spring.config.import` 中配置的顺序 **决定优先级**：**越靠后，优先级越高**。 

⚠️ 注意：**`spring.config.import` 加载的配置，优先级高于打包内的 `application.yml`，但低于命令行参数。**

参数
与 url 相同，?后跟，&添加

动态刷新（refresh=true）如何生效

```java
@RestController
@RefreshScope  // ⬅️ 关键！
public class ConfigController {

    @Value("${my.config.value}")
    private String configValue;

    @GetMapping("/config")
    public String getConfig() {
        return configValue;
    }
}
```



## spring.cloud.nacos.config

### spring.cloud.nacos.config.extension-configs

## 命名空间

这是一个非常关键且容易混淆的问题！

---

## ✅ 简短回答：

> **Spring Boot 不会“自动”去服务注册的命名空间查找配置。**
>
> 如果你写的是：
> ```yaml
> optional:nacos:spring-boot-http.yaml?group=COMMON_GROUP&refresh=true
> ```
>
> 那么 Spring Boot 会去 Nacos 的 **`public` 命名空间（默认命名空间） + `COMMON_GROUP` 分组** 中查找该配置文件。
>
> **除非你显式指定 `namespace` 参数，否则不会使用服务注册时的命名空间。**

---

## 📚 详细解释

### 🔹 1. Nacos 中的两个“空间”概念

| 类型                      | 说明                                               | 默认值                              |
| ------------------------- | -------------------------------------------------- | ----------------------------------- |
| **Namespace（命名空间）** | 用于环境隔离（如 dev/test/prod），是最高级隔离单位 | `public`（空字符串 "" 或 "public"） |
| **Group（分组）**         | 用于逻辑分组（如 COMMON_GROUP、APP_GROUP）         | `DEFAULT_GROUP`                     |

---

### 🔹 2. 服务注册 vs 配置读取 —— **命名空间是独立配置的！**

你可能配置了服务注册到某个命名空间：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: dev-ns-id  # 👈 注册到 dev 命名空间
```

但！配置中心是**另一套独立配置**：

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        namespace: dev-ns-id  # 👈 配置也要显式指定命名空间，否则默认 public
```

⚠️ 即使你用了 `spring.config.import`，**也不会自动继承 `discovery.namespace` 的值！**

---

### 🔹 3. `spring.config.import` 中如何指定命名空间？

在 URI 中使用 `namespace` 参数：

```yaml
optional:nacos:spring-boot-http.yaml?group=COMMON_GROUP&refresh=true&namespace=dev-ns-id
```

📌 **`namespace` 参数值必须是命名空间 ID（不是名称）**，在 Nacos 控制台 → “命名空间” 页面查看：

```
命名空间ID：dev-ns-id
命名空间名称：开发环境
描述：dev namespace
```

✅ 你要用的是：`dev-ns-id`

---

## ✅ 正确配置示例

假设：

- 你的服务注册在命名空间 `dev-ns-id`
- 你想加载的配置文件 `spring-boot-http.yaml` 也在同一个命名空间中
- 分组是 `COMMON_GROUP`

### ✅ 推荐写法：

```yaml
spring:
  application:
    name: your-service

  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: dev-ns-id  # 服务注册命名空间

      config:
        server-addr: 127.0.0.1:8848
        namespace: dev-ns-id  # ⚠️ 配置中心命名空间（必须显式指定）
        file-extension: yaml

  config:
    import:
      - optional:nacos:${spring.application.name}.yaml?refresh=true
      - optional:nacos:spring-boot-http.yaml?group=COMMON_GROUP&refresh=true
        # ⚠️ 注意：这里没有指定 namespace，会使用 spring.cloud.nacos.config.namespace 的值
```

> ✅ 在这种写法下，`spring-boot-http.yaml` 会从 `dev-ns-id` + `COMMON_GROUP` 中加载，因为继承了 `spring.cloud.nacos.config.namespace` 的上下文。

---

## 🆚 对比：显式指定 vs 继承 config.namespace

### 方式一：继承（推荐）

```yaml
spring:
  cloud:
    nacos:
      config:
        namespace: dev-ns-id

  config:
    import:
      - optional:nacos:spring-boot-http.yaml?group=COMMON_GROUP&refresh=true
```

→ 使用 `dev-ns-id` 命名空间

### 方式二：显式指定（覆盖）

```yaml
spring:
  cloud:
    nacos:
      config:
        namespace: prod-ns-id

  config:
    import:
      - optional:nacos:spring-boot-http.yaml?group=COMMON_GROUP&refresh=true&namespace=dev-ns-id
```

→ 使用 `dev-ns-id`（显式指定优先）

---

## ✅ 最佳实践建议

1. **统一命名空间配置**：

   ```yaml
   spring:
     cloud:
       nacos:
         discovery:
           namespace: ${nacos.namespace:}
         config:
           namespace: ${nacos.namespace:}
   ```

   然后在 `application.yml` 或环境变量中统一设置：

   ```yaml
   nacos:
     namespace: dev-ns-id
   ```

2. **在 `spring.config.import` 中省略 `namespace`**，让它继承 `spring.cloud.nacos.config.namespace`，便于统一管理。

3. **在 Nacos 控制台中，确保配置文件 Data ID、Group、Namespace 三者完全匹配导入语句。**

---

## ✅ 如何验证配置是从哪个命名空间加载的？

### 方法 1：启动日志

搜索：

```
Loading config from Nacos with dataId=spring-boot-http.yaml, group=COMMON_GROUP, namespace=dev-ns-id
```

### 方法 2：Actuator `/env`

```bash
curl http://localhost:8080/actuator/env/your.property.name
```

返回中会显示：

```json
{
  "property": {
    "source": "NacosPropertySource: spring-boot-http.yaml (namespace=dev-ns-id, group=COMMON_GROUP)",
    "value": "your-value"
  }
}
```

---

## ❗ 常见踩坑

| 问题                                             | 原因                                           | 解决方案                         |
| ------------------------------------------------ | ---------------------------------------------- | -------------------------------- |
| 配置找不到                                       | 写了 `namespace=dev`，但实际 ID 是 `dev-ns-id` | 用命名空间 ID，不是名称          |
| 读到 public 空间的配置                           | 没有配置 `spring.cloud.nacos.config.namespace` | 显式配置命名空间                 |
| 服务注册和配置空间不一致                         | 以为 discovery.namespace 会自动用于 config     | 必须为 config 单独配置 namespace |
| import 中没写 namespace，也没配 config.namespace | 默认去 public 空间找                           | 显式配置或指定参数               |

---

## ✅ 总结

| 场景                                        | 行为                                                         |
| ------------------------------------------- | ------------------------------------------------------------ |
| `spring.config.import` 中未指定 `namespace` | 使用 `spring.cloud.nacos.config.namespace` 的值（默认是 `public`） |
| `spring.config.import` 中指定了 `namespace` | 使用指定值，覆盖全局配置                                     |
| 服务注册命名空间（discovery.namespace）     | **不会影响配置读取命名空间**，两者独立！                     |
| Nacos 控制台中 Data ID 位置                 | 必须与 `namespace + group + dataId` 完全匹配                 |

---

✅ **结论：服务注册到哪个空间，跟配置读取空间无关 —— 你必须显式为配置中心指定命名空间，或在 import URI 中指定，否则默认去 `public` 空间查找。**

---

如你配置后仍找不到配置，欢迎贴出：

- 你的完整 `application.yml`
- Nacos 控制台截图（命名空间、Data ID、Group）
- 启动日志片段

我可以帮你精准定位！