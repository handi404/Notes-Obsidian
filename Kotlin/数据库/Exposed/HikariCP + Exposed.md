使用 **HikariCP** 作为连接池与 **Exposed** 结合，可以大大提升数据库操作性能。HikariCP 是目前业界非常流行的高性能连接池，与 Exposed 集成也非常方便，适合生产环境。

下面是如何使用 **HikariCP** 配合 **Exposed** 的详解：

---

### **1. 添加依赖**

在 Kotlin 项目中引入所需的依赖，包括 Exposed 和 HikariCP。

**Gradle 配置：**

```kotlin
dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("com.zaxxer:HikariCP:5.0.1") // HikariCP 连接池
    implementation("mysql:mysql-connector-java:8.0.33") // MySQL 驱动
}
```

---

### **2. 配置 HikariCP 数据源**

创建一个 `HikariConfig` 并设置数据库连接池的相关参数。

```kotlin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun initDatabase() {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://localhost:3306/your_database" // 数据库地址
        driverClassName = "com.mysql.cj.jdbc.Driver" // JDBC 驱动
        username = "your_username" // 数据库用户名
        password = "your_password" // 数据库密码
        maximumPoolSize = 10 // 最大连接数
        isAutoCommit = false // 禁用自动提交，配合 Exposed 的事务管理
        transactionIsolation = "TRANSACTION_REPEATABLE_READ" // 事务隔离级别
    }

    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource) // 使用 HikariCP 数据源连接到 Exposed
}
```

---

### **3. 定义表结构**

使用 Exposed 的 `Table` 类定义表结构。

```kotlin
import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}
```

---

### **4. 创建表**

在项目启动时，调用 Exposed 提供的工具类自动创建表。

```kotlin
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    transaction {
        SchemaUtils.create(Users) // 自动创建表
    }
}
```

---

### **5. 数据库操作（DSL 模式示例）**

#### **5.1 插入数据：**

```kotlin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime

fun insertUser() {
    transaction {
        Users.insert {
            it[name] = "Alice"
            it[email] = "alice@example.com"
            it[createdAt] = CurrentDateTime()
        }
    }
}
```

#### **5.2 查询数据：**

```kotlin
import org.jetbrains.exposed.sql.selectAll

fun fetchUsers() {
    transaction {
        Users.selectAll().forEach {
            println("ID: ${it[Users.id]}, Name: ${it[Users.name]}, Email: ${it[Users.email]}")
        }
    }
}
```

#### **5.3 更新数据：**

```kotlin
import org.jetbrains.exposed.sql.update

fun updateUser(userId: Int, newName: String) {
    transaction {
        Users.update({ Users.id eq userId }) {
            it[name] = newName
        }
    }
}
```

#### **5.4 删除数据：**

```kotlin
import org.jetbrains.exposed.sql.deleteWhere

fun deleteUser(userId: Int) {
    transaction {
        Users.deleteWhere { Users.id eq userId }
    }
}
```

---

### **6. HikariCP 配置文件方式**

可以将 HikariCP 的配置抽离到 `application.properties` 或 `application.conf` 中，提升可维护性。

**`application.conf` 示例：**

```hocon
hikari {
  jdbcUrl = "jdbc:mysql://localhost:3306/your_database"
  driverClassName = "com.mysql.cj.jdbc.Driver"
  username = "your_username"
  password = "your_password"
  maximumPoolSize = 10
  isAutoCommit = false
  transactionIsolation = "TRANSACTION_REPEATABLE_READ"
}
```

**通过配置文件加载 HikariCP：**

```kotlin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun initDatabaseWithConfig() {
    val config = HikariConfig("/application.conf")
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}
```

---

### **7. 注意事项**

1. **事务管理：**
    
    - Exposed 的事务默认会自动提交，除非显式调用 `commit()` 或 `rollback()`。
    - 结合 HikariCP 时，建议将 `isAutoCommit` 设置为 `false`，完全交给 Exposed 管理事务。
2. **连接池配置优化：**
    
    - 根据应用程序的并发量调整 `maximumPoolSize` 参数。
    - 合理设置连接超时时间、空闲连接最大存活时间等参数。
3. **SQL 调试：**
    
    - 启用 SQL 日志打印，方便调试。
    
    ```kotlin
    import org.jetbrains.exposed.sql.Slf4jSqlLogger
    import org.jetbrains.exposed.sql.addLogger
    import org.jetbrains.exposed.sql.transactions.transaction
    
    transaction {
        addLogger(Slf4jSqlLogger) // 使用 SLF4J 打印 SQL 日志
        // 数据库操作代码
    }
    ```
    
4. **数据库驱动版本匹配：**
    
    - 确保数据库驱动版本与数据库服务器兼容，避免因版本不匹配导致连接问题。

---

### **8. 整体代码示例**

```kotlin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}

fun initDatabase() {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://localhost:3306/your_database"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = "your_username"
        password = "your_password"
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)
}

fun createTables() {
    transaction {
        SchemaUtils.create(Users)
    }
}

fun insertUser(name: String, email: String) {
    transaction {
        Users.insert {
            it[Users.name] = name
            it[Users.email] = email
            it[Users.createdAt] = CurrentDateTime()
        }
    }
}

fun fetchUsers() {
    transaction {
        Users.selectAll().forEach {
            println("ID: ${it[Users.id]}, Name: ${it[Users.name]}, Email: ${it[Users.email]}")
        }
    }
}

fun main() {
    initDatabase()
    createTables()
    insertUser("Alice", "alice@example.com")
    fetchUsers()
}
```

---

### **总结**

使用 **HikariCP** 结合 **Exposed** 是构建高性能 Kotlin 数据库应用的最佳实践之一。通过合理的连接池配置和类型安全的 DSL 查询，可以在保证性能的同时，提高代码的简洁性和可维护性。