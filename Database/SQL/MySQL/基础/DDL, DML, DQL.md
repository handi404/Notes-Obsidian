从 SQL 的基石——DDL, DML, DQL 开始。这三者构成了我们与数据库交互的核心语言。我会逐一讲解，并穿插一些最佳实践和注意事项。

**SQL 语言分类**

SQL (Structured Query Language) 是一种用于管理关系数据库管理系统 (RDBMS) 的标准化语言。通常，我们会将其命令按功能划分为几个主要类别：

1.  **DDL (Data Definition Language - 数据定义语言)**：用于定义和管理数据库对象（如数据库本身、表、索引、视图等）的结构。它不直接操作数据，而是操作数据的“骨架”。
2.  **DML (Data Manipulation Language - 数据操作语言)**：用于检索、插入、更新和删除数据库中的数据。这是我们与数据内容打交道最频繁的部分。
3.  **DQL (Data Query Language - 数据查询语言)**：严格来说，DQL 是 DML 的一个子集，专门用于从数据库中检索数据。由于 `SELECT` 语句的复杂性和重要性，经常被单独拎出来强调。
4.  **DCL (Data Control Language - 数据控制语言)**：用于控制对数据库对象的访问权限，如 `GRANT` (授权) 和 `REVOKE` (撤销权限)。
5.  **TCL (Transaction Control Language - 事务控制语言)**：用于管理数据库事务，如 `COMMIT` (提交事务) 和 `ROLLBACK` (回滚事务)。

我们今天重点关注前三者：DDL, DML, DQL。

---

### 一、 DDL (Data Definition Language - 数据定义语言)

DDL 负责创建、修改和删除数据库的结构，而不是数据本身。可以把它想象成设计房子的蓝图。

**核心命令：**

1.  **`CREATE DATABASE database_name;`**
    *   **作用：** 创建一个新的数据库。
    *   **示例：** `CREATE DATABASE my_store;`
    *   **扩展与应用：**
        *   可以指定字符集和排序规则：`CREATE DATABASE my_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;` (推荐使用 `utf8mb4` 以支持包括 emoji 在内的所有 Unicode 字符)。
        *   在创建数据库前，最好检查数据库是否已存在：`CREATE DATABASE IF NOT EXISTS my_store;`

2.  **`CREATE TABLE table_name ( column1_name data_type [constraints], column2_name data_type [constraints], ... [table_constraints] );`**
    *   **作用：** 在当前选定的数据库中创建一个新表。
    *   **示例：**
        ```sql
        USE my_store; -- 先选择数据库
        CREATE TABLE products (
            id INT AUTO_INCREMENT PRIMARY KEY,  -- id 列，整数，自增，主键
            name VARCHAR(255) NOT NULL,        -- 产品名称，可变长度字符串，不能为空
            price DECIMAL(10, 2) DEFAULT 0.00, -- 价格，固定精度和小数位数，默认值为 0.00
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 创建时间，时间戳，默认为当前时间
        );
        ```
    *   **扩展与应用：**
        *   **约束 (Constraints)：**
            *   `PRIMARY KEY`: 主键，唯一标识表中的每一行，不能为空。
            *   `FOREIGN KEY`: 外键，用于建立表与表之间的关联。
            *   `UNIQUE`: 唯一约束，确保列中的所有值都是唯一的。
            *   `NOT NULL`: 非空约束，确保列不能有 NULL 值。
            *   `DEFAULT value`: 为列设置默认值。
            *   `CHECK (condition)`: (MySQL 8.0.16+ 支持) 检查约束，确保列值满足特定条件。
            *   `AUTO_INCREMENT`: 自动递增，通常用于主键。
        *   **存储引擎选择：** 可以在 `CREATE TABLE` 语句末尾指定存储引擎，如 `ENGINE=InnoDB;` (InnoDB 是 MySQL 8.0 的默认引擎，推荐使用)。
        *   **字符集与排序规则：** 可以为表或列指定字符集和排序规则。

3.  **`ALTER TABLE table_name action;`**
    *   **作用：** 修改已存在的表结构。
    *   **常见的 `action`：**
        *   `ADD column_name data_type [constraints];` (添加列)
        *   `DROP COLUMN column_name;` (删除列)
        *   `MODIFY COLUMN column_name new_data_type [constraints];` (修改列的数据类型或约束)
        *   `CHANGE COLUMN old_column_name new_column_name new_data_type [constraints];` (修改列名和定义)
        *   `ADD CONSTRAINT constraint_name constraint_definition;` (添加约束，如外键)
        *   `DROP CONSTRAINT constraint_name;` (删除约束)
        *   `RENAME TO new_table_name;` (重命名表，MySQL 也支持 `RENAME TABLE old_name TO new_name;`)
    *   **示例：**
        ```sql
        ALTER TABLE products ADD COLUMN description TEXT; -- 添加描述列
        ALTER TABLE products MODIFY COLUMN name VARCHAR(300) NOT NULL; -- 修改 name 列长度
        ALTER TABLE products DROP COLUMN created_at; -- 删除创建时间列
        ```
    *   **扩展与应用：**
        *   `ALTER TABLE` 操作可能会很耗时，尤其是在大表上。某些操作会导致表锁定，影响线上服务。
        *   对于大表的结构变更，推荐使用如 `pt-online-schema-change` (Percona Toolkit) 或 `gh-ost` (GitHub) 这样的在线 DDL 工具，它们可以在不锁表或极短锁表时间的情况下完成操作。
        *   MySQL 8.0 引入了原子 DDL (Atomic DDL)，使得一些 DDL 操作（如 `DROP TABLE`, `RENAME TABLE`）具备原子性，要么完全成功，要么完全回滚，减少了部分 DDL 失败导致数据不一致的风险。

4.  **`DROP DATABASE database_name;`**
    *   **作用：** 删除一个数据库及其包含的所有表和数据。**这是一个非常危险的操作，请谨慎使用！**
    *   **示例：** `DROP DATABASE my_store;`
    *   **扩展与应用：**
        *   最好先备份再删除。
        *   可以使用 `DROP DATABASE IF EXISTS my_store;` 来避免数据库不存在时报错。

5.  **`DROP TABLE table_name;`**
    *   **作用：** 删除一个表及其所有数据和索引。**同样危险，谨慎操作！**
    *   **示例：** `DROP TABLE products;`
    *   **扩展与应用：**
        *   可以使用 `DROP TABLE IF EXISTS products;`
        *   删除有外键关联的表时，需要先解除外键约束或删除依赖它的表。

6.  **`TRUNCATE TABLE table_name;`**
    *   **作用：** 快速删除表中的所有行，但保留表结构 (包括列、索引、约束等)。
    *   **与 `DELETE FROM table_name;` (不带 `WHERE` 条件) 的区别：**
        *   **速度：** `TRUNCATE` 通常比 `DELETE` 快得多，因为它不记录每条删除的行，而是直接释放数据页。
        *   **事务：** `TRUNCATE` 是 DDL 操作，在某些数据库中（包括 MySQL 的大部分情况，特别是对于 InnoDB 表）会隐式提交当前事务。而 `DELETE` 是 DML 操作，可以回滚。
        *   **触发器：** `TRUNCATE` 通常不会触发与 `DELETE` 相关的触发器。
        *   **自增计数器：** `TRUNCATE` 通常会重置自增列的计数器，而 `DELETE` 不会。
    *   **示例：** `TRUNCATE TABLE products;`
    *   **应用场景：** 当需要快速清空一个大表并且不需要回滚时，`TRUNCATE` 是个好选择。

**DDL 的注意事项：**

*   **原子性：** 并非所有 DDL 操作在所有 MySQL 版本和存储引擎中都是完全原子的。MySQL 8.0 在这方面有很大改进。
*   **性能影响：** 许多 DDL 操作（尤其是 `ALTER TABLE`）在繁忙的生产系统上执行时需要特别小心，因为它们可能导致长时间的表锁定，影响应用程序的可用性。
*   **元数据锁 (Metadata Locks, MDL)：** MySQL 使用 MDL 来保护数据库对象的定义，防止在 DDL 操作期间对象被并发的 DML 或其他 DDL 修改。不当的 DDL 或长时间运行的查询可能导致 MDL 等待，从而阻塞其他操作。

---

### 二、 DML (Data Manipulation Language - 数据操作语言)

DML 用于管理数据库中的实际数据。

**核心命令：**

1.  **`INSERT INTO table_name (column1, column2, ...) VALUES (value1, value2, ...);`**
    *   **作用：** 向表中插入一行或多行数据。
    *   **示例 (单行插入)：**
        ```sql
        INSERT INTO products (name, price) VALUES ('Laptop X1', 1200.50);
        ```
    *   **示例 (多行插入)：**
        ```sql
        INSERT INTO products (name, price) VALUES
        ('Mouse M5', 25.00),
        ('Keyboard K100', 75.99);
        ```
    *   **扩展与应用：**
        *   如果插入的值与列的顺序一致，可以省略列名列表：`INSERT INTO products VALUES (NULL, 'Monitor Z27', 350.00, NOW());` (假设 id 是自增的，最后一个是 created_at)。但不推荐这样做，因为表结构变化时容易出错。
        *   **`INSERT ... SELECT ...`：** 从其他表查询数据并插入到当前表。
            ```sql
            INSERT INTO old_products (name, price)
            SELECT name, price FROM products WHERE price < 100;
            ```
        *   **`INSERT IGNORE ...`：** 如果插入会导致唯一键冲突，则忽略该行，不报错。
        *   **`INSERT ... ON DUPLICATE KEY UPDATE ...`：** 如果插入会导致唯一键 (包括主键) 冲突，则执行 `UPDATE` 子句中的操作，而不是报错。非常有用！
            ```sql
            INSERT INTO products (id, name, price) VALUES (1, 'Laptop X1 Pro', 1250.00)
            ON DUPLICATE KEY UPDATE price = VALUES(price), name = VALUES(name);
            -- VALUES(column_name) 用于引用 INSERT 部分试图插入的值
            ```
        *   **`REPLACE INTO ...`：** 行为类似 `INSERT`，但如果存在唯一键冲突，它会先删除旧行，再插入新行。这与 `ON DUPLICATE KEY UPDATE` 的区别在于 `REPLACE` 会导致行的物理删除和重新插入，可能会触发 `DELETE` 和 `INSERT` 触发器，并且自增 ID 可能会改变。

2.  **`UPDATE table_name SET column1 = value1, column2 = value2, ... WHERE condition;`**
    *   **作用：** 修改表中已存在的数据行。
    *   **示例：**
        ```sql
        UPDATE products SET price = 1150.00 WHERE name = 'Laptop X1';
        ```
    *   **扩展与应用：**
        *   **`WHERE` 子句至关重要！** 如果省略 `WHERE` 子句，将会更新表中的所有行！务必小心。
        *   可以使用子查询在 `SET` 或 `WHERE` 子句中。
        *   **多表更新 (Multi-table Update)：** MySQL 支持在一个 `UPDATE` 语句中更新多个表的数据 (通常通过 JOIN)。
            ```sql
            UPDATE orders o
            JOIN customers c ON o.customer_id = c.id
            SET o.customer_name = c.name
            WHERE c.city = 'New York';
            ```
        *   `LIMIT` 子句可以与 `UPDATE` (和 `DELETE`) 结合使用，但通常需要配合 `ORDER BY` 来确保确定性，以更新特定数量的行。

3.  **`DELETE FROM table_name WHERE condition;`**
    *   **作用：** 从表中删除满足条件的行。
    *   **示例：**
        ```sql
        DELETE FROM products WHERE price < 10;
        ```
    *   **扩展与应用：**
        *   **`WHERE` 子句至关重要！** 如果省略 `WHERE` 子句，将会删除表中的所有行！(与 `TRUNCATE` 不同，`DELETE` 是逐行删除，记录日志，可以回滚，速度较慢)。
        *   **多表删除 (Multi-table Delete)：** MySQL 支持在一个 `DELETE` 语句中从多个表中删除行 (通常通过 JOIN)。
            ```sql
            DELETE p
            FROM products p
            JOIN categories c ON p.category_id = c.id
            WHERE c.name = 'Obsolete';
            ```
        *   `LIMIT` 子句可以与 `DELETE` 结合使用，但同样需要注意确定性。

**DML 的注意事项：**

*   **`WHERE` 子句的精确性：** 在 `UPDATE` 和 `DELETE` 中，`WHERE` 子句是防止误操作的关键。执行前最好先用 `SELECT` 语句配合相同的 `WHERE` 子句检查将要影响的行。
*   **事务：** DML 操作通常在事务中执行，以确保数据的一致性。如果发生错误，可以回滚。
*   **性能：** 大量的 `INSERT`, `UPDATE`, `DELETE` 操作会对性能产生影响，特别是涉及到索引更新和日志写入。批量操作通常比单条操作更高效。

---

### 三、 DQL (Data Query Language - 数据查询语言)

DQL 的核心就是 `SELECT` 语句，它是我们从数据库中获取信息的方式。

**核心命令及子句 (按典型执行顺序大致排列，但逻辑顺序不同)：**

1.  **`SELECT column1, column2, ... | *`**
    *   **作用：** 指定要检索的列。`*` 表示选择所有列 (生产环境中应尽量避免使用 `*`，明确指定列名以提高效率和可读性)。
    *   可以对列使用表达式、函数、别名 (`AS`)。
    *   **示例：** `SELECT id, name, price * 0.8 AS discounted_price FROM products;`

2.  **`FROM table_name [AS alias]`**
    *   **作用：** 指定数据来源的表。可以为表指定别名，在多表连接时非常有用。
    *   **示例：** `SELECT p.name FROM products AS p;`

3.  **`JOIN table_name ON condition` (多种 JOIN 类型)**
    *   **作用：** 用于根据某些条件将多个表中的行组合起来。
    *   **类型：** `INNER JOIN` (内连接), `LEFT JOIN` (左连接), `RIGHT JOIN` (右连接), `FULL OUTER JOIN` (MySQL 通过 `LEFT JOIN ... UNION ... RIGHT JOIN` 模拟), `CROSS JOIN` (笛卡尔积)。
    *   **示例 (内连接)：**
        ```sql
        SELECT o.id AS order_id, p.name AS product_name
        FROM orders o
        INNER JOIN products p ON o.product_id = p.id;
        ```

4.  **`WHERE condition`**
    *   **作用：** 过滤行，只选择满足指定条件的行。
    *   使用比较运算符 (`=`, `>`, `<`, `>=`, `<=`, `<>`, `!=`)、逻辑运算符 (`AND`, `OR`, `NOT`)、范围 (`BETWEEN`), 列表 (`IN`, `NOT IN`), 模式匹配 (`LIKE`, `RLIKE` / `REGEXP`)、空值判断 (`IS NULL`, `IS NOT NULL`)。
    *   **示例：** `SELECT name, price FROM products WHERE price > 100 AND name LIKE 'Laptop%';`

5.  **`GROUP BY column1, column2, ...`**
    *   **作用：** 将结果集中的行按照一个或多个列进行分组，通常与聚合函数 (如 `COUNT()`, `SUM()`, `AVG()`) 配合使用，对每个组进行计算。
    *   **示例：**
        ```sql
        SELECT category_id, COUNT(*) AS product_count, AVG(price) AS average_price
        FROM products
        GROUP BY category_id;
        ```
    *   **MySQL 扩展：`WITH ROLLUP`** 可以为 `GROUP BY` 的结果添加一个汇总行。
    *   **`ONLY_FULL_GROUP_BY` SQL Mode:** MySQL 默认启用此模式 (自 5.7.5 起)。它要求 `SELECT` 列表、`HAVING` 条件或 `ORDER BY` 列表中的非聚合列必须出现在 `GROUP BY` 子句中，或者函数依赖于 `GROUP BY` 列。这是符合 SQL 标准的行为，有助于避免不确定的查询结果。

6.  **`HAVING condition`**
    *   **作用：** 在 `GROUP BY` 分组之后，对分组结果进行过滤。`WHERE` 过滤的是行，`HAVING` 过滤的是组。
    *   `HAVING` 子句中可以使用聚合函数，而 `WHERE` 子句中通常不能 (除非在子查询中)。
    *   **示例：**
        ```sql
        SELECT category_id, AVG(price) AS average_price
        FROM products
        GROUP BY category_id
        HAVING AVG(price) > 500; -- 只显示平均价格大于 500 的类别
        ```

7.  **`ORDER BY column1 [ASC|DESC], column2 [ASC|DESC], ...`**
    *   **作用：** 对结果集进行排序。`ASC` (升序，默认)，`DESC` (降序)。
    *   可以按多个列排序。
    *   **示例：** `SELECT name, price FROM products ORDER BY price DESC, name ASC;` (先按价格降序，价格相同时按名称升序)

8.  **`LIMIT [offset,] row_count`**
    *   **作用：** 限制返回的行数。常用于分页。
    *   `row_count`: 要返回的行数。
    *   `offset`: 可选参数，表示从结果集的第几行开始返回 (第一行为 0)。
    *   **示例 (取前 10 条)：** `SELECT * FROM products LIMIT 10;`
    *   **示例 (分页，第 2 页，每页 10 条)：** `SELECT * FROM products LIMIT 10, 10;` (跳过前 10 条，取接下来的 10 条)
    *   **注意：** 当使用 `LIMIT` 时，为了保证结果的确定性，通常需要配合 `ORDER BY`。否则，返回的行可能是随机的 (取决于内部执行计划)。

**DQL (SELECT 语句) 的逻辑执行顺序 (重要！与书写顺序不同)：**

理解这个顺序有助于编写更高效、更正确的 SQL：

1.  `FROM` (包括 `JOIN` s)
2.  `WHERE`
3.  `GROUP BY`
4.  `HAVING`
5.  `SELECT` (包括窗口函数，窗口函数在 `SELECT` 阶段，但其计算依赖于前面的步骤)
6.  `DISTINCT` (如果使用)
7.  `ORDER BY`
8.  `LIMIT`

**DQL 的应用与扩展：**

*   **子查询 (Subqueries)：** 嵌套在其他 SQL 语句中的查询。
*   **公用表表达式 (CTEs - Common Table Expressions)：** 使用 `WITH` 子句定义临时的、命名的结果集，使复杂查询更易读和维护 (MySQL 8.0+)。
*   **窗口函数 (Window Functions)：** 对与当前行相关的行集执行计算，但不像 `GROUP BY` 那样将行折叠 (MySQL 8.0+)。
*   **`UNION` 和 `UNION ALL`：** 合并多个 `SELECT` 语句的结果集。`UNION` 会去除重复行，`UNION ALL` 不会。

---

### 四、常用数据类型选择及其适用场景

选择正确的数据类型对于数据库的性能、存储效率和数据完整性至关重要。

1.  **整数类型 (Integer Types)：**
    *   `TINYINT`: 1 字节，范围 `-128` 到 `127` (有符号) 或 `0` 到 `255` (无符号 `UNSIGNED`)。适用于存储年龄、小的计数等。
    *   `SMALLINT`: 2 字节。
    *   `MEDIUMINT`: 3 字节。
    *   `INT` 或 `INTEGER`: 4 字节。最常用，适用于大多数 ID、计数等。
    *   `BIGINT`: 8 字节。适用于需要非常大范围的整数，如分布式系统中的雪花 ID。
    *   **选择建议：** 选择能覆盖数据范围的最小类型。如果确定是非负数，使用 `UNSIGNED` 可以扩大正数范围一倍。`AUTO_INCREMENT` 通常与 `INT UNSIGNED` 或 `BIGINT UNSIGNED` 配合。

2.  **浮点数与定点数类型 (Floating-Point and Fixed-Point Types)：**
    *   `FLOAT(M,D)`: 单精度浮点数，4 字节。`M` 是总位数，`D` 是小数点后的位数。存在精度问题，不适用于精确计算（如货币）。
    *   `DOUBLE(M,D)` 或 `REAL(M,D)`: 双精度浮点数，8 字节。精度比 `FLOAT` 高，但同样存在精度问题。
    *   **`DECIMAL(M,D)` 或 `NUMERIC(M,D)` (推荐用于精确计算)：** 定点数。以字符串形式存储，用于精确计算，如货币、金融数据。`M` 是最大总位数 (精度)，`D` 是小数点后的位数 (标度)。
        *   **示例：** `DECIMAL(10, 2)` 可以存储最多 10 位数字，其中 2 位在小数点后 (例如 `12345678.90`)。
    *   **选择建议：** **对于货币或任何需要精确计算的场景，请务必使用 `DECIMAL`。** 避免使用 `FLOAT` 和 `DOUBLE` 进行此类计算，因为它们可能导致舍入误差。

3.  **字符串类型 (String Types)：**
    *   **`CHAR(M)`:** 固定长度字符串。长度范围 0 到 255。如果存储的字符串短于 `M`，右边会用空格填充 (但在检索时，如果 `PAD_CHAR_TO_FULL_LENGTH` SQL mode 未启用，尾部空格通常会被移除)。适用于长度基本固定的值，如邮政编码、性别 (M/F)、国家代码。
    *   **`VARCHAR(M)`:** 可变长度字符串。长度范围 0 到 65535 (实际最大长度受行大小限制和字符集影响)。只存储实际字符内容加 1 或 2 个字节的长度前缀。是**最常用的字符串类型**。
    *   **`TINYTEXT`:** 最多 255 字节。
    *   **`TEXT`:** 最多 65,535 (2^16 - 1) 字节。
    *   **`MEDIUMTEXT`:** 最多 16,777,215 (2^24 - 1) 字节。
    *   **`LONGTEXT`:** 最多 4,294,967,295 (2^32 - 1) 字节 (4 GB)。
    *   **`BINARY(M)` 和 `VARBINARY(M)`:** 类似于 `CHAR` 和 `VARCHAR`，但存储的是二进制字节串，区分大小写，没有字符集概念。
    *   **`BLOB` 类型 (Binary Large Object)：** `TINYBLOB`, `BLOB`, `MEDIUMBLOB`, `LONGBLOB`。用于存储二进制数据，如图片、音频、文件。通常不建议在数据库中存储大型二进制对象，更推荐存储文件路径，将文件存放在文件系统或对象存储中。
    *   **选择建议：**
        *   大部分短文本使用 `VARCHAR`。根据预估的最大长度选择合适的 `M` 值。
        *   非常长的文本内容使用 `TEXT` 系列。`TEXT` 和 `BLOB` 列在某些情况下存储和处理方式与 `VARCHAR` 不同 (例如，它们可能存储在行外，影响查询性能)。
        *   如果字符串长度几乎总是固定的，可以考虑 `CHAR`，但 `VARCHAR` 更灵活且通常更节省空间 (除非字符串长度都接近 `M`)。
        *   **字符集 (Character Set) 和排序规则 (Collation)** 对字符串类型非常重要。推荐使用 `utf8mb4` 字符集以支持所有 Unicode 字符。

4.  **日期和时间类型 (Date and Time Types)：**
    *   **`DATE`:** 存储日期，格式 'YYYY-MM-DD'。范围 '1000-01-01' 到 '9999-12-31'。
    *   **`TIME`:** _不推荐用于时间点_。存储时间或时间间隔，格式 'HH:MM:SS'。范围 '-838:59:59' 到 '838:59:59'。
    *   **`DATETIME(fsp)`:** 存储日期和时间，格式 'YYYY-MM-DD HH:MM:SS[.fraction]'。范围 '1000-01-01 00:00:00.000000' 到 '9999-12-31 23:59:59.999999'。`fsp` 是可选的，表示秒的小数部分的精度 (0-6)。**不存储时区信息，存储的是字面值。**
    *   **`TIMESTAMP(fsp)` (推荐用于记录事件发生时间)：** 存储日期和时间，也带可选的毫秒精度。范围 '1970-01-01 00:00:01.000000' UTC 到 '2038-01-19 03:14:07.999999' UTC。**`TIMESTAMP` 的值在存储时会从当前连接的时区转换为 UTC，在检索时会从 UTC 转换回当前连接的时区。** 这使得它非常适合记录具有全局一致性的时间点。
        *   在 MySQL 8.0.28 之前，`TIMESTAMP` 默认行为是 `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`，这可能不是期望的。之后版本，这些默认行为不再自动应用，除非显式指定。
    *   **`YEAR`:** 存储年份，1 字节 (`YEAR(2)` 已废弃) 或 2 字节 (`YEAR(4)` 格式 'YYYY')。
    *   **选择建议：**
        *   仅日期用 `DATE`。
        *   **记录事件发生时间、创建时间、修改时间等，强烈推荐使用 `TIMESTAMP`**，因为它会自动处理时区转换，确保时间的一致性。
        *   如果需要存储不带时区信息的特定日期和时间（例如，用户的生日，不应随时区改变），可以使用 `DATETIME`。
        *   使用 `NOW()` 或 `CURRENT_TIMESTAMP` 获取当前日期时间。

5.  **枚举和集合类型 (Enum and Set Types)：**
    *   **`ENUM('value1', 'value2', ...)`:** 单选列表。列值只能是预定义列表中的一个值 (或 `NULL`，如果允许)。在内部，MySQL 将其存储为整数 (每个值的索引)。
        *   **示例：** `status ENUM('pending', 'processing', 'completed', 'failed')`
    *   **`SET('value1', 'value2', ...)`:** 多选列表。列值可以是预定义列表中的零个、一个或多个值的组合。内部存储为位图。
        *   **示例：** `permissions SET('read', 'write', 'execute')`
    *   **选择建议：**
        *   `ENUM` 和 `SET` 在某些情况下可以节省空间，并且提供了一定的数据校验。
        *   **缺点：** 修改 `ENUM` 或 `SET` 的定义 (如添加新值) 通常需要 `ALTER TABLE`，这可能是个昂贵的操作。它们也降低了数据库的可移植性，因为不是所有数据库都支持。
        *   **替代方案：** 可以使用关联表 (查找表) 和外键来代替 `ENUM`，这更符合关系数据库设计原则，也更灵活。对于 `SET`，可以考虑使用多个 `BOOLEAN` / `TINYINT(1)` 列，或者（在支持良好的情况下）JSON 数组。

6.  **布尔类型 (Boolean Type)：**
    *   MySQL 没有真正的 `BOOLEAN` 或 `BOOL` 类型。它们是 `TINYINT(1)` 的同义词。`TRUE` 被视为 `1`，`FALSE` 被视为 `0`。
    *   **选择建议：** 使用 `TINYINT(1)` 并用 `0` 和 `1` 来表示 `false` 和 `true` 是常见做法。

7.  **JSON 类型 (JSON Type) (MySQL 5.7.8+)：**
    *   用于存储 JSON (JavaScript Object Notation) 文档。
    *   MySQL 会验证 JSON 文档的有效性，并以优化的二进制格式存储，以便快速访问其元素。
    *   提供了丰富的 JSON 函数来操作和查询 JSON 数据。
    *   **选择建议：** 当需要存储半结构化数据，或者数据结构可能经常变化时，JSON 类型非常有用。避免滥用，如果数据是高度结构化的，传统的关系模型通常更好。

**数据类型选择的通用原则：**

*   **选择最小的能正确存储数据的类型：** 节省存储空间，提高查询效率。
*   **简单性：** 尽量选择简单的数据类型。例如，如果只需要存储 'Y'/'N'，`CHAR(1)` 比 `VARCHAR(1)` 可能更好（尽管差异微小）。
*   **避免 `NULL` (如果可能)：** `NULL` 值需要额外的存储空间，并且在查询中处理起来比较复杂 (需要 `IS NULL` 或 `IS NOT NULL`)。如果业务允许，可以为列设置 `NOT NULL` 并提供一个有意义的默认值。
*   **一致性：** 在相关联的表和列中，使用相同或兼容的数据类型。
*   **考虑查询需求：** 数据类型会影响索引的效率和查询的性能。

---

以上就是关于 SQL 基础 DDL, DML, DQL 以及常用数据类型的详细讲解。这部分内容是 MySQL 学习的绝对核心，务必牢固掌握。