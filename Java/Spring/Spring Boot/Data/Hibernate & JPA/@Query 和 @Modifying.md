`@Query` 和 `@Modifying` 这两个在日常开发中极为常用的注解。

---

### 序言：为什么需要 `@Query`？

我们已经了解了两种查询方式：

1.  **派生查询 (Derived Queries):** `findByUsername(String username)`，简单直观，但无法处理复杂逻辑。
2.  **规格查询 (Specifications):** 用于构建动态的、可组合的查询，非常灵活，但代码稍显繁琐。

`@Query` 则填补了这两者之间的巨大空白。它适用于那些**逻辑比派生查询复杂，但又不像规格查询那样需要动态组合**的场景。比如，一个需要 `JOIN`、`GROUP BY` 或者自定义返回结果的固定查询。

> **一句话概PI括：** `@Query` 允许你在 Repository 方法上直接编写 JPQL 或原生 SQL，将查询的控制权完全掌握在自己手中。

---

### 一、`@Query`：自定义查询的利器

#### 1. 核心主角：JPQL (Java Persistence Query Language)

这是理解 `@Query` 的关键。请忘掉你对 SQL 的固有印象，用**面向对象**的思维来理解 JPQL。

*   **SQL 面向的是“表”和“列” (Table & Column)。**
*   **JPQL 面向的是“实体”和“属性” (Entity & Attribute)。**

**关键区别与语法要点：**

*   **查询对象：** JPQL 查询的是实体类名（区分大小写），而不是数据库表名。
    *   SQL: `SELECT * FROM user_table`
    *   JPQL: `SELECT u FROM User u` (这里的 `User` 是你的实体类名, `u` 是别名)
*   **查询字段：** JPQL 查询的是实体类的属性名，而不是表的列名。
    *   SQL: `WHERE user_name = ?`
    *   JPQL: `WHERE u.username = :name` (这里的 `username` 是 `User` 类里的属性)
*   **`SELECT` 子句：** 在 JPQL 中，`SELECT u` 返回的是整个 `User` 实体对象，JPA 会自动映射。而在 SQL 中 `SELECT *` 返回的是一堆列。
*   **大小写敏感：** 实体名和属性名是大小写敏感的。

**现在，让我们看实例来学习。**

#### 2. JPQL 的使用方法与技巧

##### (1) 基本查询与参数绑定

我们有两种参数绑定方式：**位置参数**和**命名参数**。**强烈推荐使用命名参数**，因为它可读性更高，且不易因参数位置调换而出错。

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // 👎 位置参数 (?1, ?2, ...) -- 不推荐
    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.email = ?2")
    Optional<User> findByUsernameAndEmailWithPositionalParams(String username, String email);

    // 👍 命名参数 (:name, :email) -- 推荐！
    // 参数名通过 @Param("name") 绑定
    @Query("SELECT u FROM User u WHERE u.username = :name AND u.email = :email")
    Optional<User> findByUsernameAndEmailWithNamedParams(@Param("name") String username, @Param("email") String userEmail);
}
```
> **注意：** `@Param` 注解的值需要和 JPQL 中的命名参数（如 `:name`）完全对应。方法参数名 (`username`, `userEmail`) 则可以任意。

##### (2) 投影 (Projection)：只查询部分字段（性能优化关键）

很多时候我们不需要整个实体，只需要几个字段。直接返回一个自定义的 DTO (Data Transfer Object) 是最高效的方式。这可以通过 JPQL 的**构造器表达式**实现。

假设我们有一个 `UserSummaryDTO`：

```java
public class UserSummaryDTO {
    private String username;
    private String email;

    // 关键：必须有一个与 JPQL 中 new 表达式参数列表匹配的构造函数！
    public UserSummaryDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
    // getters...
}
```

在 Repository 中使用它：

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // 使用 "new" 关键字和类的全限定名来创建 DTO
    @Query("SELECT new com.example.dto.UserSummaryDTO(u.username, u.email) FROM User u WHERE u.id = :id")
    Optional<UserSummaryDTO> findUserSummaryById(@Param("id") Long id);
}
```
**这样做的好处巨大：**
*   **性能提升：** Hibernate 只会生成 `SELECT username, email FROM ...` 的 SQL，而不是 `SELECT *`，减少了数据传输量和内存占用。
*   **清晰的契约：** Service 层得到的是一个干净的 DTO，而不是一个可能被意外修改的持久化实体。

##### (3) 关联查询 (JOIN)

JPQL 的 `JOIN` 语法也非常面向对象。

假设 `User` 有一个 `one-to-many` 的关系到 `Post` 实体 (`private List<Post> posts;`)。

```java
// 查找发布过标题包含特定关键字的帖子的所有用户
@Query("SELECT DISTINCT u FROM User u JOIN u.posts p WHERE p.title LIKE %:keyword%")
List<User> findUsersWhoPostedWithTitleKeyword(@Param("keyword") String keyword);
```
*   `JOIN u.posts p`: 直接在用户的 `posts` 属性上进行 `JOIN`，JPA 会自动转换成正确的 SQL JOIN。`p` 是 `Post` 实体的别名。
*   `DISTINCT`: 防止因为一个用户有多篇匹配的帖子而返回重复的用户。

##### (4) 利用 `Pageable` 和 `Sort`

Spring Data JPA 的一大魔力在于，即使你用了 `@Query`，`Pageable` 和 `Sort` 参数依然可以无缝工作！Spring Data 会自动将分页和排序的 SQL 片段追加到你的查询后面。

```java
@Query("SELECT u FROM User u WHERE u.age > :minAge")
Page<User> findUsersOlderThan(@Param("minAge") int age, Pageable pageable);

// 调用时：
// userRepository.findUsersOlderThan(18, PageRequest.of(0, 10, Sort.by("username").descending()));
// Spring Data 会自动生成类似 ... ORDER BY username DESC LIMIT 10 OFFSET 0 的 SQL。
```
**注意：** 如果你的 `@Query` 包含了复杂的 `GROUP BY`，自动的 `Sort` 可能会失效。这时你需要在查询中自己处理排序。

#### 3. 原生 SQL 的使用

当你需要使用数据库特有的函数（如 Oracle 的 `CONNECT BY`）或者想利用特定数据库的性能优化（如查询提示），就需要使用原生 SQL。

使用原生 SQL 只需增加一个属性：`nativeQuery = true`。

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // 假设我们使用一个数据库特有的函数 a_custom_function()
    @Query(value = "SELECT * FROM user_table u WHERE u.email = :email AND a_custom_function(u.id) = 1",
           nativeQuery = true)
    Optional<User> findByEmailWithNativeQuery(@Param("email") String email);
}
```
**原生 SQL 的注意事项：**
*   **表名和列名：** 必须使用数据库中的真实表名和列名。
*   **失去可移植性：** 这段查询可能在换成另一个数据库后就无法运行了。
*   **DTO 投影：** 原生查询的 DTO 投影需要额外配置 `@SqlResultSetMapping`，比 JPQL 构造器表达式要复杂得多。**除非万不得已，否则优先使用 JPQL**。

---

### 二、`@Modifying`：执行更新和删除

`@Query` 默认只能执行查询（`SELECT`）。如果你想执行 `UPDATE`、`DELETE` 或 `INSERT` 操作，必须组合使用 `@Modifying`。

#### 核心场景：批量操作

想象一下，你要将所有未激活的用户批量删除。如果用传统方法：
1.  `findAll` 查出所有未激活用户（可能成千上万个）。
2.  `deleteAll` 循环删除。

这会造成巨大的内存开销和无数次数据库交互。而使用 `@Modifying` 可以**一条 SQL 直接在数据库层面完成**。

#### 使用方法与最重要注意事项

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional // 1. 关键点：修改操作必须在事务中执行！
    public int deactivateUsersByAge(int age) {
        return userRepository.deactivateUsersOlderThan(age);
    }
}


public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true) // 2. 关键点：声明为修改查询
    @Query("UPDATE User u SET u.active = false WHERE u.age > :age")
    int deactivateUsersOlderThan(@Param("age") int age);
    // 返回值 int 表示受影响的行数
}
```

**必须理解的三个关键点：**

1.  **`@Transactional` 注解：** 所有的 `@Modifying` 查询都必须在事务（`@Transactional`）中调用。没有事务，它会直接抛出异常。这通常加在 Service 方法上。

2.  **`@Modifying` 注解本身：**
    *   它告诉 Spring Data JPA，这个查询不是 `SELECT`，而是一个 DML (Data Manipulation Language) 操作。
    *   **返回值：** 方法的返回值可以是 `int` 或 `void`。`int` 表示这次操作影响的数据库行数。

3.  **`clearAutomatically = true`（极其重要）：**
    *   **问题背景：** `@Modifying` 操作是直接操作数据库的，它会**绕过 JPA 的一级缓存（Persistence Context）**。假设你在一个事务中，先 `findById(1L)` 查出了一个用户（此时它在缓存中是 `active=true`），然后你调用了上面的 `deactivateUsersOlderThan()` 方法，数据库里这个用户已经变成了 `active=false`。但如果你再次 `findById(1L)`，由于一级缓存的存在，你拿到的**还是旧的、`active=true` 的那个实体**！这就造成了数据不一致。
    *   **解决方案：** `clearAutomatically = true` 会在执行完 `@Modifying` 查询后，**自动清空整个一级缓存**。这样，后续的任何查询都会直接从数据库获取最新的数据，从而避免了脏数据问题。**在绝大多数情况下，都应该开启此选项。**
    *   `flushAutomatically = true`: 在执行自定义查询前，先将一级缓存中所有挂起的变更同步到数据库。这也是一个好习惯，可以防止一些意外情况。

---

### 总结与最佳实践

| 特性 | JPQL (`@Query`) | Native SQL (`@Query(nativeQuery=true)`) | `@Modifying` |
| :--- | :--- | :--- | :--- |
| **用途** | 自定义 SELECT 查询 | 数据库特定的 SELECT 查询 | 批量 UPDATE, DELETE, INSERT |
| **查询对象** | 实体 & 属性 | 表 & 列 | 实体 & 属性 (或表 & 列) |
| **可移植性** | **高** (数据库无关) | **低** (数据库特定) | 高 (若用 JPQL) / 低 (若用原生 SQL) |
| **优点** | 面向对象、与 `Pageable` 良好集成、DTO 投影简单 | 可利用数据库全部特性、极致性能优化 | **批量操作性能极高** |
| **缺点** | 无法使用数据库特定功能 | 失去可移植性、DTO 投影复杂 | **必须处理事务和缓存一致性问题** |
| **关键实践** | 使用**命名参数**、用**构造器表达式**做 DTO 投影 | 仅在 JPQL 无法满足时使用 | **必须加 `@Transactional`**、**强烈推荐 `@Modifying(clearAutomatically = true)`** |

现在，你应该对 `@Query` 和 `@Modifying` 有了全面且深入的理解。