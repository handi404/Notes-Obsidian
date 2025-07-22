`@MappedSuperclass` 这个注解。这是一个在项目初期进行良好分层和代码复用时，非常有用的工具。

---

### 一、是什么 (What): `@MappedSuperclass` 的核心定义

`@MappedSuperclass` 注解用于**标记一个类，该类将它自己的持久化属性（字段和方法）贡献给所有继承它的子类实体（`@Entity`）**。

一句话概括：**它是一个“实体属性模板”，而不是一个真正的实体。**

**核心特点：**

1.  **它不是实体 (`@Entity`)**：被 `@MappedSuperclass` 标注的类本身不会映射到数据库的任何表。
2.  **不能被查询**：你不能对一个 `@MappedSuperclass` 类创建 `Repository`，也不能在 JPQL 中直接查询它（例如 `FROM BaseEntity` 会报错）。
3.  **属性继承**：所有继承这个类的实体，会自动包含父类中定义的字段，并且这些字段会被正确地映射到子类对应的数据库表中。

**一个形象的比喻：**

想象一下你在盖很多栋不同的房子（`Product` 实体、`User` 实体）。虽然房子功能各异，但它们都有一些共同的基础设施，比如地基（`id`）、水电线路铺设日期（`createdAt`）、最后翻新日期（`updatedAt`）。

`@MappedSuperclass` 就是这份 **“标准基础设施蓝图”**。你不会直接去住这份蓝图，但所有按照这份蓝图盖出来的房子，都天然具备了这些标准设施。

---

### 二、怎么用 (How): 核心用法与代码示例

最常见的用法是创建一个包含通用字段（如主键、创建/更新时间等）的基类。

**步骤 1: 创建映射的父类 (蓝图)**

我们创建一个 `BaseEntity`，它包含了所有实体都应该有的 `id`, `createdAt` 和 `updatedAt` 字段。

```java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // 关键注解：声明这是一个映射的父类
public abstract class BaseEntity { // 推荐设为 abstract，防止被错误地实例化

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
    private Long id;

    @Column(name = "created_at", updatable = false) // 不允许更新创建时间
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA 生命周期回调，在持久化之前自动设置时间
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // JPA 生命周期回调，在更新之前自动设置时间
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```
*   `@MappedSuperclass`：告诉 JPA，这是一个模板，请将它的映射信息应用到子类。
*   `abstract`：这是一个最佳实践，明确表示这个类不能被单独实例化。
*   `@PrePersist` / `@PreUpdate`：JPA 自带的生命周期回调注解，可以在实体被保存或更新时自动执行方法。稍后我们会看到 Spring Data JPA 提供了更优雅的实现。

**步骤 2: 创建继承的实体类 (具体的房子)**

现在，我们创建一个 `Product` 实体，让它继承 `BaseEntity`。

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity { // 继承 BaseEntity

    private String name;

    private BigDecimal price;

    // 无需再定义 id, createdAt, updatedAt 字段，它们已经从 BaseEntity 继承了
}
```

**结果：**

当 JPA (Hibernate) 创建 `products` 表时，其 DDL 语句会是这样的：

```sql
CREATE TABLE products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    name VARCHAR(255),
    price DECIMAL(19,2),
    PRIMARY KEY (id)
);
```
如你所见，`Product` 实体虽然没有显式定义 `id`, `created_at`, `updated_at`，但这些字段已经成功地映射到了 `products` 表中。

---

### 三、为什么 (Why): 设计哲学与适用场景

使用 `@MappedSuperclass` 的主要动机是 **代码复用（Don't Repeat Yourself - DRY）** 和 **维护统一性**。

**适用场景：**

1.  **通用审计字段**：这是最经典、最广泛的应用场景。`id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy` 这些字段几乎是每个业务表的标配。
2.  **通用乐观锁字段**：当需要使用乐观锁时，可以在基类中定义 `@Version` 字段。
3.  **通用软删除标记**：例如，一个 `deleted` 布尔型字段或 `deletedAt` 时间戳字段。

它将这些横切关注点（Cross-Cutting Concerns）从业务实体中抽离出来，让实体类本身更专注于自身的业务属性。

---

### 四、最佳实践与陷阱 (Best Practices & Pitfalls)

#### 最佳实践

1.  **设为抽象类 (`abstract`)**：如前所述，这能防止它被意外实例化，并清晰地表达其作为模板的意图。
2.  **结合 Spring Data JPA 审计功能**：使用 `@PrePersist` 和 `@PreUpdate` 是 JPA 的原生方式，但 Spring Data JPA 提供了更强大、更解耦的自动化审计功能。这是现代 Spring 开发的**首选方案**（详见下一节“扩展应用”）。
3.  **保持简洁**：`@MappedSuperclass` 中只应包含普遍适用的、与具体业务无关的字段。不要把某个特定模块的通用字段放进去。

#### 陷阱 (常见误区)

1.  **它不是实体**：再次强调，你不能为 `BaseEntity` 创建 `JpaRepository<BaseEntity, Long>`，也不能写 `em.createQuery("from BaseEntity", BaseEntity.class)`。
2.  **与实体继承 (`@Inheritance`) 的混淆**：
    *   `@MappedSuperclass`：是**代码层面**的继承，用于共享映射信息，数据库层面没有继承关系，每个子类对应独立的表。它不实现多态查询。
    *   `@Inheritance`：是**数据模型层面**的继承，用于建立“is-a”关系（例如 `Cat` is-a `Animal`），数据库层面会形成继承关系（如单表、连接表等策略），并且支持多态查询（`SELECT a FROM Animal a` 可以查出 `Cat` 和 `Dog`）。
    *   **简单区分**：如果你的基类代表一个抽象的、不应独立存在的“概念模板”，用 `@MappedSuperclass`。如果你的基类代表一个可以被独立查询的、有具体子类实现的“父概念”，用 `@Inheritance`。

3.  **关联关系的限制**：在 `@MappedSuperclass` 中定义关联关系（如 `@ManyToOne`）要特别小心。如果所有子类都关联到同一个实体，这是可行的。但如果子类需要关联到不同的实体，这种设计就行不通了。通常不建议在映射父类中定义复杂的关联关系。

---

### 五、扩展与应用 (Advanced Usage): 自动化审计

现在，我们用 Spring Data JPA 的审计功能来升级之前的 `BaseEntity`，这是目前业界最主流、最优雅的实现方式。

**步骤 1: 启用 JPA 审计**

在你的 Spring Boot 主启动类上添加 `@EnableJpaAuditing` 注解。

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 启用 JPA 审计功能
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

**步骤 2: 改造 `BaseEntity`**

使用 Spring Data 提供的专用注解，并添加 `@EntityListeners`。

```java
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 引入 Spring Data JPA 的审计监听器
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate // 标记为创建日期，由 AuditingEntityListener 自动填充
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 标记为最后修改日期，由 AuditingEntityListener 自动填充
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 不再需要 @PrePersist 和 @PreUpdate 方法了！
}
```
`@EntityListeners(AuditingEntityListener.class)` 会监听实体的持久化和更新事件，并根据 `@CreatedDate` 和 `@LastModifiedDate` 注解自动填充相应字段。代码变得更加干净和声明式。

**步骤 3 (可选但推荐): 记录创建者和修改者**

如果你的项目集成了 Spring Security，还可以自动记录操作人。

首先，改造 `BaseEntity`，添加 `createdBy` 和 `updatedBy` 字段。

```java
// ... 在 BaseEntity 中添加 ...
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@CreatedBy
@Column(name = "created_by", updatable = false)
private String createdBy;

@LastModifiedBy
@Column(name = "updated_by")
private String updatedBy;
```

然后，提供一个 `AuditorAware` 的 Bean，告诉 Spring Data JPA 如何获取当前用户名。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User; // 假设使用 Spring Security 的 User
import java.util.Optional;

@Configuration
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 返回一个 AuditorAware 的实现
        return () -> {
            // 从 Spring Security 上下文中获取当前登录的用户名
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.empty(); // 如果未登录，则返回空
            }
            // 这里可以根据你的 UserDetails 实现来获取用户名
            return Optional.of(authentication.getName());
        };
    }
}
```

现在，当你保存或更新任何继承自 `BaseEntity` 的实体时，`createdAt`, `updatedAt`, `createdBy`, `updatedBy` 都会被**全自动、透明地填充**。这极大地提升了开发效率和代码整洁度。