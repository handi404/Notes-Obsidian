用 Kotlin 编写一个 Spring Boot 应用程序。将从一个空的项目骨架开始，然后构建一个微型的 CRUD REST 服务。在这个过程中，将涵盖所有重要的 Kotlin 语言基础和概念，以及更高级的主题，例如 Kotlin 与 JPA 支持。

Spring Initializr
03:44 Gradle & Kotlin
05:13 Project Structure 项目结构
05:47 主类与基本 Kotlin 概念
09:45 Data Classes 数据类
10:46 Var & Val
10:59 Null Safety 空安全
11:29 Default Parameter Values 默认参数值
12:47 Extension Functions 扩展函数
14:47 RestControllers
16:38 Named Parameters 命名参数
17:26 Method Return Syntax
19:19 CRUD
20:18 Ternary Operator 三元运算符
21:43 更多 Kotlin 概念 (== 与 equals，迭代器等)
25:26 JPA
31:37 进一步阅读

### Spring Initializr
[Spring Initializr](https://start.spring.io/)
选择 Gradle-Kotlin 作为构建工具。添加 Spring Web 依赖；访问数据库，因此添加 spring data jpa；使用 H2 作为数据库；还会讨论一下 Validation (验证)，添加 Validation。

### Gradle & Kotlin
有一个build.gradle.kts 文件，它是Gradle 的 kotlin 脚本文件，只需快速浏览一下，实际上花费几个小时、几天或几周来熟悉Gradle，但现在忽略这里的零碎，只需要关注依赖项 `dependencies` 部分：
```kotlin
dependencies {  
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")  
    implementation("org.springframework.boot:spring-boot-starter-validation")  
    implementation("org.springframework.boot:spring-boot-starter-web")  
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")  
    implementation("org.jetbrains.kotlin:kotlin-reflect")  
    runtimeOnly("com.h2database:h2")  
    testImplementation("org.springframework.boot:spring-boot-starter-test")  
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")  
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")  
}
```
有 spring boot starter data jpa，添加了 validation，添加了 web 编辑。还有 Jackson 模块用于 kotlin，因为 Jackson 序列化和反序列化 Json 需要进行一些调整才能与 kotlin 和 kotlin-reflect 反射库正常工作。

### Project Structure 项目结构
与 Java Spring Boot 项目结构几乎相同。

### 主类与基本 Kotlin 概念
对于 java 它可能看起来有点不同甚至对比 Java 看起来有点奇怪，比如 `class SpringbootKotlinApplication`，没有花括号。事实上可以有花括号，但是如果有一个没有方法或什么都没有的空类，可以省略 `{}`。
这是我们的 main 函数：
```Kotlin
@SpringBootApplication  
class SpringbootKotlinApplication

fun main(args: Array<String>) {  
    runApplication<SpringbootKotlinApplication>(*args)  
}
```
就是将要用来运行 springboot 项目的函数使用时髦的语法调用另一个名为 runApplication 的函数。
启动项目，然后进行像 Java Spring Boot 项目编写。

### Data Classes 数据类
[[2.数据类和解构声明]]
### Null Safety 空安全
[[1.空安全（Nullable）与 Elvis 操作符]]
### Default Parameter Values 默认参数值
建立一个很小很无聊的博客。
从一个叫做 article 的数据类开始：
```Kotlin
data class Article (  
    val title: String,  
    val content: String?,  
    val createdAt: LocalDateTime = LocalDateTime.now()  
)
```
kotlin有默认参数值，所以默认情况下，如果用户不传递它，就把它变成一个本地日期时间点。
希望每篇文章都有一个slug，slug 是类似于将 My Title 变为 my-title，搜索特定博客需要 `https://blogs/my-first-title`，所以增加 slug：
```Kotlin
data class Article (  
    val title: String,  
    val content: String?,  
    val createdAt: LocalDateTime = LocalDateTime.now(),  
    val slug: String = title.toSlug(),  
)
```
但是我们没有 toSlug()这个方法，这个时候就需要用到扩展函数。
### Extension Functions 扩展函数
[[1.扩展函数和扩展属性]]
新建 `Extensions.kt`，编写：
```Kotlin
fun String.toSlug() = lowercase(Locale.getDefault())  
    .replace("\n", "_")  
    .replace("[^a-z\\d\\s]".toRegex(), " ")  
    .split(" ")  
    .joinToString("-")  
    .replace("-+".toRegex(), "-")
```

### RestControllers
创建第一个 RestController，新建 RestControllers.kt：
```Kotlin
@RestController  
@RequestMapping("/api/v1/articles")  
class ArticleController {  
    val articles = mutableListOf(Article(title = "my title", content = "my content"))  
    @GetMapping  
    fun getArticles() = articles
}
```
首先对于调用构造函数时不必用 `new`，其次参数赋值可以按顺序，也可以像这里的不按顺序指定赋值。
### Method Return Syntax 方法返回语法
对于 `fun getArticles() = articles` 没有指定返回类型，但是如果只是 `=`，那么 kotlin 编译器将自动正确推断返回类型，也可以将其转换为主体块：
```Kotlin
fun getArticles(): MutableList<Article> {  
    return articles  
}
```
对于主体块必须带有返回类型。

### CRUD
添加所有其他 crud 方法来检索一篇特定的文章，删除一篇文章，更新一篇文章，发布一篇文章：
```Kotlin 
@RestController  
@RequestMapping("/api/v1/articles")  
class ArticleController {  
    val articles = mutableListOf(Article(title = "my title", content = "my content"))  
  
    @GetMapping    
    fun articles() = articles  
  
    @GetMapping("/{slug}")  
    fun article(@PathVariable slug: String) = articles.find { it.slug == slug } ?: throw ResponseStatusException(  
        HttpStatus.NOT_FOUND)  
  
    @PostMapping    
    fun newArticle(@RequestBody article: Article): Article {  
        articles.add(article)  
        return article  
    }  
  
    @PutMapping("/{slug}")  
    fun updateArticle(@RequestBody article: Article, @PathVariable slug: String): Article {  
        val existingArticle = articles.find { it.slug == slug } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)  
        existingArticle.content = article.content  
        return article  
    }  
  
    @DeleteMapping("/{slug}")  
    fun deleteArticle(@PathVariable slug: String) {  
//        articles.remove(articles.find { it.slug == slug })  
        articles.removeIf({ it.slug == slug })  
    }  
}
```
只候在 Http Client 进行测试。

### JPA
创建一个新文件，称之为 entities，因为首先需要 entities 来与 jpa 一起工作，现在我们的数据类不太适合成为实体，所以我要做的就是快速删除 Articles，将代码粘贴到 entities 中。首先，需要 `@Entity` 注解：
```Kotlin
@Entity  
data class Article (  
    var title: String,  
    var content: String,  
    var createdAt: LocalDateTime = LocalDateTime.now(),  
    var slug: String = title.toSlug(),  
)
```
你可能不希望数据类成为实体，比如它覆盖了equals 和 hash code，这种方式不太适合 jpa，所以你知道写一个普通的类。对于已经习惯使用 hibernate 的人来说，另一件事是总是需要一个无参数的构造函数作为默认值，而在 kotlin 版本中没有这个，有趣的是，当进入 `build.gradle.kts` 文件中，将看到 `plugin.jap`，它实际上将在后台为我们创建默认的无参构造函数，因此，即使没有无参构造函数，也不会在启动应用程序时遇到问题。
缺少一个 ID，它没有主键：
```Kotlin
@Entity
data class Article (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    var title: String,
    var content: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var slug: String = title.toSlug(),
)
```

现在让我们创建另一个文件，将其称为 `repositories`，因为使用 spring data，通常会为每个实体编写一个存储库。
```Kotlin
interface ArticleRepository: JpaRepository<Article, Long> {
    fun findAllByOrderByCreatedAtDesc(): Iterable<Article>
}
```

剩下要做的就是摆脱可变列表。
在构造函数中传入了 ArticleRepository ，就是在 Spring 中使用普通的**构造注入**，然后用 repository 的方法替换了所有列表方法。
```Kotlin
@RestController
@RequestMapping("/api/v1/articles")
class ArticleController(val articleRepository: ArticleRepository) {

    @GetMapping
    fun articles() = articleRepository.findAllByOrderByCreatedAtDesc()

    @GetMapping("/{slug}")
    fun article(@PathVariable slug: String) = articleRepository.findBySlug(slug)

    @PostMapping
    fun newArticle(@RequestBody article: Article): Article {
        article.id = null
        articleRepository.save(article)
        return article
    }

    @PutMapping("/{slug}")
    fun updateArticle(@RequestBody article: Article, @PathVariable slug: String): Article {
        val existingArticle = articleRepository.findBySlug(slug)
        existingArticle.content = article.content
        articleRepository.save(article)
        return article
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(@PathVariable slug: String) {
        articleRepository.deleteBySlug(slug)
    }
}
```

启动应用程序时会自动创建 H2 数据库，但需要做的是，将 `spring.jpa.generate.ddl` 设置为 true，这样 hibernate 就会创建 H2 数据库所需的所有表，现在不必担心 SQL 和所有这些东西，所以一切都会开箱即用。
```properties
spring.jpa.generate.ddl = true
```