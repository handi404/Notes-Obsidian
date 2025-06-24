逻辑删除（Soft Delete）是一个非常实用的模式！

想象一下，你不是真的把数据从数据库里“扔掉”，而是给它贴上一个“已作废”的标签。这样，数据还在，但对于正常的业务操作来说，它就像消失了一样。

**一、什么是逻辑删除？**

逻辑删除是指在删除数据时，并不是真的从数据库中物理移除该记录，而是通过一个**状态字段**（例如 `is_deleted`, `deleted_at`, `status` 等）来标记该记录为“已删除”状态。

**这样做的好处：**

1.  **数据可恢复**：万一误删，或者将来需要找回数据，非常容易。
2.  **数据审计与追踪**：可以保留完整的操作历史，知道数据是什么时候被“删除”的。
3.  **维持数据完整性**：如果其他表有外键关联到这条记录，物理删除可能会导致问题或需要级联删除。逻辑删除可以避免这种情况。
4.  **业务需求**：某些业务场景下，数据不能被真正删除，只是在特定条件下不再展示。

**二、Spring Data JPA 实现逻辑删除**

Spring Data JPA (实际上是其底层的 JPA 实现，如 Hibernate) 提供了非常优雅的方式来实现逻辑删除。主要依赖两个 Hibernate 特有的注解：

1.  **`@SQLDelete(sql = "...")`**：
    *   当调用 JPA 的 `delete()` 或 `deleteById()` 等删除方法时，Hibernate 会执行这个注解中定义的 SQL 语句，而不是标准的 `DELETE FROM table ...`。
    *   我们可以在这里将 SQL 定义为更新“删除标记”字段。

2.  **`@Where(clause = "...")`**：
    *   这个注解会自动给实体相关的**所有查询语句**（SELECT、UPDATE、甚至 DELETE，虽然 DELETE 通常被 `@SQLDelete` 覆盖了）追加一个 `WHERE` 条件。
    *   这样，在执行 `findAll()`, `findById()`, 甚至通过方法名约定的查询时，都会自动过滤掉那些被标记为“已删除”的记录。

**三、实现步骤与示例**

假设我们有一个 `Product` 实体，我们想对它实现逻辑删除。

1.  **修改实体类 (`Product.java`)**：

    ```java
    import jakarta.persistence.*;
    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.Where;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "products")
    @SQLDelete(sql = "UPDATE products SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?") // 当调用delete方法时，执行该SQL
    @Where(clause = "deleted = false") // 全局查询过滤条件，只查询未被逻辑删除的记录
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private double price;

        // 逻辑删除标记字段
        private boolean deleted = false; // 默认为 false，表示未删除

        // 可选：记录删除时间
        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        // Constructors, Getters, Setters...

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public LocalDateTime getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
        }

        @Override
        public String toString() {
            return "Product{" +
                   "id=" + id +
                   ", name='" + name + '\'' +
                   ", price=" + price +
                   ", deleted=" + deleted +
                   ", deletedAt=" + deletedAt +
                   '}';
        }
    }
    ```
    *   **`private boolean deleted = false;`**: 这是我们的逻辑删除标记。`false` 表示有效，`true` 表示已逻辑删除。
    *   **`@SQLDelete(sql = "UPDATE products SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")`**:
        *   当调用 `productRepository.delete(product)` 或 `productRepository.deleteById(1L)` 时，Hibernate 不会执行物理删除。
        *   而是执行 `UPDATE products SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = <product_id>`。
        *   `?` 是一个占位符，Hibernate 会用实体 ID 替换它。`CURRENT_TIMESTAMP` 是数据库函数，用于记录删除时间。
    *   **`@Where(clause = "deleted = false")`**:
        *   这个注解是关键，它会影响所有针对 `Product` 实体的查询。
        *   例如，`productRepository.findAll()` 会自动变成类似 `SELECT * FROM products WHERE deleted = false`。
        *   `productRepository.findById(1L)` 会变成 `SELECT * FROM products WHERE id = 1 AND deleted = false`。
        *   这确保了应用程序在正常操作中感知不到那些“已删除”的数据。

2.  **Repository 接口 (`ProductRepository.java`)**：
    不需要做任何特殊改动，和平时一样使用 `JpaRepository` 即可。

    ```java
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface ProductRepository extends JpaRepository<Product, Long> {
        // 你可以像往常一样定义其他查询方法
        // List<Product> findByName(String name); // 这个查询也会自动带上 "deleted = false"
    }
    ```

3.  **使用**：

    ```java
    // 在你的 Service 或 Test 中
    @Autowired
    private ProductRepository productRepository;

    public void manageProduct() {
        Product p1 = new Product();
        p1.setName("Laptop");
        p1.setPrice(1200.00);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Mouse");
        p2.setPrice(25.00);
        productRepository.save(p2);

        System.out.println("All active products: " + productRepository.findAll());

        // 逻辑删除 Laptop
        productRepository.deleteById(p1.getId()); // 或者 productRepository.delete(p1);

        System.out.println("Active products after delete: " + productRepository.findAll()); // Laptop 不会出现在这里

        Optional<Product> deletedProduct = productRepository.findById(p1.getId());
        System.out.println("Try to find deleted product by ID: " + deletedProduct.isPresent()); // false
    }
    ```

**四、如何查询包括逻辑删除的数据？**

`@Where` 注解非常方便，但也意味着默认情况下你无法直接通过标准 Repository 方法查到被逻辑删除的数据（例如，管理员可能需要查看回收站）。

如果你需要查询包括（或仅查询）逻辑删除的数据，你有以下几种方式：

1.  **使用原生 SQL 或 JPQL 配合 `@Query` 并忽略 `@Where` 的效果**：
    `@Where` 注解对 `@Query` 中自定义的 JPQL 或原生 SQL **不起作用**。

    ```java
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import java.util.List;
    import java.util.Optional;

    public interface ProductRepository extends JpaRepository<Product, Long> {

        // 查询所有产品，包括已逻辑删除的 (原生SQL)
        @Query(value = "SELECT * FROM products", nativeQuery = true)
        List<Product> findAllIncludingDeletedNative();

        // 查询所有产品，包括已逻辑删除的 (JPQL)
        @Query("SELECT p FROM Product p")
        List<Product> findAllIncludingDeletedJpql();

        // 只查询已逻辑删除的产品 (JPQL)
        @Query("SELECT p FROM Product p WHERE p.deleted = true")
        List<Product> findAllDeletedOnly();

        // 根据ID查找产品，无论是否已逻辑删除 (JPQL)
        @Query("SELECT p FROM Product p WHERE p.id = :id")
        Optional<Product> findByIdIncludeDeleted(@Param("id") Long id);
    }
    ```

2.  **使用 Hibernate 的 `@Filter` (更高级，更灵活但更复杂)**：
    Hibernate Filter 提供了动态启用/禁用过滤条件的能力。这比静态的 `@Where` 更灵活，但配置也更复杂。通常，对于简单的逻辑删除场景，`@Query` 已经足够。

**五、注意事项与最佳实践**

1.  **索引**：为你的逻辑删除标记字段（如 `deleted`）创建数据库索引，特别是当这个字段经常作为查询条件时。
2.  **唯一约束**：
    *   如果你有一个字段（如 `email`）需要保持唯一性，逻辑删除可能会带来问题。因为 `("test@example.com", false)` 和 `("test@example.com", true)` 在数据库层面是两条不同的记录，但业务上可能只允许一个活跃的 `test@example.com`。
    *   **解决方案**：
        *   **数据库层面**：如果数据库支持，可以使用部分索引/筛选索引 (Filtered Index)，例如 `CREATE UNIQUE INDEX unique_active_email ON users (email) WHERE deleted = false;` (PostgreSQL 示例)。
        *   **应用层面**：在保存或更新前，先查询是否存在具有相同唯一键且 `deleted = false` 的记录。
        *   **使用可空时间戳**：将 `deleted` 字段改为 `deletedAt` (类型为 `LocalDateTime` 或 `Timestamp`)，未删除时为 `NULL`。很多数据库允许唯一约束的列中包含多个 `NULL` 值。此时 `@Where` 改为 `deleted_at IS NULL`，`@SQLDelete` 改为 `SET deleted_at = CURRENT_TIMESTAMP`。
3.  **级联操作**：
    *   如果实体间存在级联关系 (e.g., `CascadeType.REMOVE` 或 `orphanRemoval=true`)，当父实体被逻辑删除时（通过 `@SQLDelete`），级联操作会正确地作用于子实体，子实体也会被逻辑删除（如果它们也配置了 `@SQLDelete` 和 `@Where`）。
4.  **数据清理**：逻辑删除的数据仍然占用数据库空间。需要根据业务需求制定策略，定期归档或物理删除那些不再需要的、已被逻辑删除很久的数据。
5.  **字段命名**：`isDeleted`, `deleted`, `active` (反向逻辑), `status` (用特定值表示删除), `deletedAt` (时间戳) 都是常见的逻辑删除标记字段名。选择一个清晰并在团队内保持一致。使用 `deletedAt` 时间戳可以额外记录删除时间，非常有用。

**总结：**
通过 `@SQLDelete` 和 `@Where` 注解，Spring Data JPA (Hibernate) 为逻辑删除提供了一个非常强大且透明的实现方式。开发者在大多数情况下可以像操作普通数据一样操作配置了逻辑删除的实体，而框架会自动处理“删除”标记和查询过滤。当需要访问被隐藏的数据时，可以通过自定义 `@Query` 来实现。