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