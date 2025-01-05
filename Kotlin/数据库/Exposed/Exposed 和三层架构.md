### **如何在 Kotlin 项目中使用 Exposed 和三层架构**

#### **1. 数据访问层（Repository）**

在数据访问层，我们使用 **Exposed** 来定义表结构、执行数据库查询等操作。Exposed 提供了一个非常简单和类型安全的 **DSL** 来与数据库交互。

```kotlin
// 数据访问层 - Repository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = integer("id").autoIncrement() // ID 列
    val name = varchar("name", 50) // Name 列
    val email = varchar("email", 100) // Email 列
    override val primaryKey = PrimaryKey(id) // 主键
}

class UserRepository {

    init {
    	//val dataSource = HikariDataSource(HikariConfig("/hikari.properties"))  
		//Database.connect(dataSource)
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            // 自动创建表
            SchemaUtils.create(Users)
        }
    }

    // 插入一个新用户
    fun insertUser(name: String, email: String) {
        transaction {
            Users.insert {
                it[Users.name] = name
                it[Users.email] = email
            }
        }
    }

    // 查询所有用户
    fun getAllUsers(): List<User> {
        return transaction {
            Users.selectAll().map { 
                User(it[Users.id], it[Users.name], it[Users.email]) 
            }
        }
    }

    // 获取单个用户
    fun getUserById(id: Int): User? {
        return transaction {
            Users.select { Users.id eq id }
                .map { User(it[Users.id], it[Users.name], it[Users.email]) }
                .singleOrNull()
        }
    }
}

data class User(val id: Int, val name: String, val email: String)
```

- 在 `UserRepository` 类中，我们使用 Exposed 定义了一个 `Users` 表和相应的 CRUD 方法。
- `transaction` 块中执行的是数据库操作，Exposed 会自动管理数据库连接和事务。

---

#### **2. 业务逻辑层（Service）**

在业务逻辑层，我们将用户的具体操作封装起来，并且调用数据访问层的方法来执行实际的数据库操作。

```kotlin
// 业务逻辑层 - Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(name: String, email: String) {
        userRepository.insertUser(name, email)
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }
}
```

- `UserService` 类是业务逻辑层，它调用了 `UserRepository` 中定义的方法来处理数据操作。`Service` 层通常进行更复杂的业务处理，而 `Repository` 层专注于数据库交互。

---

#### **3. 表示层（Controller）**

在表示层（Controller），我们负责处理用户的请求并返回响应。我们可以使用 **Ktor** 或 **Spring Boot** 来实现控制器。

以 **Ktor** 为例，示例如下：

```kotlin
// 表示层 - Controller
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.kotlinx.json
import io.ktor.request.receive
import io.ktor.application.call

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    install(ContentNegotiation) {
        json() // 支持 JSON 格式的序列化
    }

    routing {
        route("/users") {
            post {
                val user = call.receive<User>()
                userService.createUser(user.name, user.email)
                call.respond(HttpStatusCode.Created, "User created")
            }

            get {
                val users = userService.getAllUsers()
                call.respond(users)
            }
        }
    }
}

// 启动服务器
fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}
```

- 在 `Ktor` 控制器中，我们通过接收 HTTP 请求并调用 `UserService` 来处理用户操作。
- `post` 请求用于创建用户，`get` 请求用于获取所有用户。

---

### **Exposed 在三层架构中的使用总结：**

1. **数据访问层（Repository）**：
    
    - 使用 **Exposed** 提供的 DSL 来操作数据库，包括创建表、查询数据、插入数据等。
    - 需要定义数据库的表结构并封装相应的 CRUD 操作。
2. **业务逻辑层（Service）**：
    
    - 业务层将数据操作的细节从控制器中抽离出来，专注于业务逻辑的处理。
    - 业务逻辑层通过调用数据访问层的接口来与数据库交互。
3. **表示层（Controller）**：
    
    - 通过 **Ktor**（或其他 Web 框架）处理来自用户的 HTTP 请求。
    - 控制器从业务层获取数据，并通过 HTTP 响应返回给客户端。

---

### **Exposed 的优缺点**

#### **优点：**

1. **类型安全：** 使用 Exposed 时，所有的查询和操作都是类型安全的，减少了运行时错误。
2. **轻量：** 与 Hibernate 等 ORM 框架相比，Exposed 更加轻量，灵活性更高。
3. **DSL 支持：** Exposed 提供了强大的 DSL 语法，开发者可以通过更简洁、直观的方式来操作数据库。
4. **原生 SQL 支持：** 除了使用 DSL 语法外，Exposed 也支持原生 SQL，因此对于一些复杂查询或特定的 SQL 操作也能轻松处理。

#### **缺点：**

1. **功能相对简单：** Exposed 没有像 Hibernate 那样提供许多高级特性（如级联操作、懒加载等）。
2. **社区支持较少：** 相比于 Hibernate，Exposed 的使用群体较小，文档和教程相对较少。

---

### **总结：**

- 使用 **Exposed** 可以非常方便地在 Kotlin 中进行数据库操作，它的 **DSL** 和类型安全性使得数据库交互更加直观且易于维护。
- 在 **三层架构** 中，Exposed 主要用于数据访问层，处理数据库操作，而业务逻辑层和表示层则分别处理业务处理和请求响应。
- 对于轻量级的应用或中型项目，Exposed 结合三层架构是一个非常不错的选择，特别是在你不需要复杂的 ORM 特性时。