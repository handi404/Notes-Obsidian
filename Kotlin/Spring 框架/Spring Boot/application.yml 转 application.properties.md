将 `application.yml` 文件转换为 `application.properties` 文件，主要是将每个 YAML 层级的键值对转换为 `key=value` 格式。下面是 `application.yml` 文件转换为 `application.properties` 的结果：

### **application.yml 内容**

```yaml
logging:
  file:
    name: app.log
server:
  error:
    include-binding-errors: always
    include-message: always
    include-stacktrace: on_param
  port: ${SERVER_PORT:8080}
spring:
  datasource:
    username: ${DATASOURCE_USERNAME:sa}
    password: ${DATASOURCE_PASSWORD:''}
    url: ${DATASOURCE_URL:jdbc:h2:mem:testdb}
    driver-class-name: ${DATASOURCE_DRIVER_CLASS_NAME:org.h2.Driver}
  jpa:
    database: ${JPA_DATABASE:h2}
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
    show-sql: true
```

### **转换为 application.properties**

```properties
# Logging configuration
logging.file.name=app.log

# Server configuration
server.error.include-binding-errors=always
server.error.include-message=always
server.error.include-stacktrace=on_param
server.port=${SERVER_PORT:8080}

# Spring datasource configuration
spring.datasource.username=${DATASOURCE_USERNAME:sa}
spring.datasource.password=${DATASOURCE_PASSWORD:''}
spring.datasource.url=${DATASOURCE_URL:jdbc:h2:mem:testdb}
spring.datasource.driver-class-name=${DATASOURCE_DRIVER_CLASS_NAME:org.h2.Driver}

# Spring JPA configuration
spring.jpa.database=${JPA_DATABASE:h2}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.show-sql=true
```

### **详细说明：**

- **日志配置**：
    
    - `logging.file.name=app.log`：定义了日志文件的名称为 `app.log`。
- **服务器配置**：
    
    - `server.error.include-binding-errors=always`：控制是否在错误响应中总是包括绑定错误信息。
    - `server.error.include-message=always`：控制是否在错误响应中总是包括错误消息。
    - `server.error.include-stacktrace=on_param`：控制是否在错误响应中包含堆栈跟踪信息，只有在请求中带有 `stacktrace` 参数时才返回堆栈信息。
    - `server.port=${SERVER_PORT:8080}`：配置服务器的端口，默认是 `8080`，可以通过环境变量 `SERVER_PORT` 覆盖。
- **数据源配置**：
    
    - `spring.datasource.username=${DATASOURCE_USERNAME:sa}`：数据库连接的用户名，默认值是 `sa`。
    - `spring.datasource.password=${DATASOURCE_PASSWORD:''}`：数据库连接的密码，默认值为空字符串。
    - `spring.datasource.url=${DATASOURCE_URL:jdbc:h2:mem:testdb}`：数据库连接 URL，默认是 `jdbc:h2:mem:testdb`。
    - `spring.datasource.driver-class-name=${DATASOURCE_DRIVER_CLASS_NAME:org.h2.Driver}`：数据库驱动类，默认使用 H2 数据库驱动。
- **JPA 配置**：
    
    - `spring.jpa.database=${JPA_DATABASE:h2}`：配置 JPA 数据库类型，默认是 `h2`。
    - `spring.jpa.hibernate.ddl-auto=update`：设置 Hibernate 的数据库自动模式为 `update`，即在应用启动时自动更新数据库结构。
    - `spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true`：配置 Hibernate 对 LOB 类型数据的处理方式。
    - `spring.jpa.show-sql=true`：启用 Hibernate 的 SQL 日志输出，便于调试。

### **总结**

通过这种转换方式，我们将 `application.yml` 中的层级结构转换为 `application.properties` 文件中的 `key=value` 键值对格式。两者的核心配置相同，只是语法不同，`application.properties` 更加平铺，适合简单配置，但不如 `application.yml` 那样直观。