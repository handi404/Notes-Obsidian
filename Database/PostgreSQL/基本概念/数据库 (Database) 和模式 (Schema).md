聊聊 PostgreSQL 中的数据库 (Database) 和模式 (Schema)。这是理解 PostgreSQL 逻辑结构和数据组织方式的基础。

---

### 基本概念

想象一个办公大楼（这是你的 PostgreSQL **实例**或**集群**）：

1.  **数据库 (Database)**:
    *   **是什么**: 数据库是 PostgreSQL 实例中一个独立的、隔离的对象集合。你可以把它想象成大楼里的一个**独立部门**或**公司**，比如“财务部”、“人力资源部”或“客户 A 公司”、“客户 B 公司”。
    *   **隔离性**: 不同数据库之间的数据是严格隔离的。默认情况下，一个数据库连接不能直接访问另一个数据库中的数据（除非使用像 `dblink` 或 `postgres_fdw` 这样的扩展）。每个数据库有自己独立的系统目录、用户权限（大部分全局角色除外）、设置等。
    *   **用途**:
        *   **多租户架构**: 为不同的客户或应用提供完全隔离的环境。
        *   **开发/测试/生产环境分离**: 在同一个 PostgreSQL 实例中，可以用不同的数据库来承载不同阶段的环境。
        *   **逻辑应用分离**: 如果多个应用之间完全没有数据共享需求，可以放在不同数据库。

2.  **模式 (Schema)**:
    *   **是什么**: 模式是数据库内部的一个**命名空间**。你可以把它想象成部门里的一个个**文件夹**或**文件柜抽屉**，用来组织相关的对象，如表、视图、函数、索引、序列等。
    *   **组织性**: 在一个数据库内，你可以创建多个模式来更好地组织你的数据对象。例如，在一个名为 `erp_system` 的数据库中，你可能有 `sales` 模式、`inventory` 模式、`hr` 模式。
    *   **默认模式**: 每个新数据库都会自动包含一个名为 `public` 的模式。如果你在创建对象时没有指定模式，对象通常会创建在 `public` 模式下（取决于 `search_path`）。
    *   **`search_path`**: 这是一个非常重要的概念。它是一个模式名称的列表，当你不带模式限定引用一个对象时（例如 `SELECT * FROM my_table;` 而不是 `SELECT * FROM myschema.my_table;`），PostgreSQL 会按 `search_path` 中列出的顺序查找这个对象。
    *   **用途**:
        *   **逻辑分组**: 将相关的表、函数等组织在一起，使数据库结构更清晰。
        *   **避免命名冲突**: 不同的模式可以有同名的表（例如 `sales.customers` 和 `support.customers`）。
        *   **权限管理**: 可以基于模式来设置权限，控制不同用户组对不同数据子集的访问。
        *   **多租户 (轻量级)**: 在单个数据库内，通过为每个租户分配一个或多个 schema 来实现一定程度的隔离（数据不共享，但共享数据库级资源）。

**关系图示：**

```
PostgreSQL Instance (服务器集群)
  |
  +-- Database 1 (e.g., 'my_app_prod')
  |     |
  |     +-- Schema 'public' (default)
  |     |     |-- Table 'users'
  |     |     `-- Function 'get_user_count()'
  |     |
  |     +-- Schema 'reporting'
  |     |     |-- View 'monthly_sales_summary'
  |     |     `-- Table 'archived_orders'
  |     |
  |     `-- Schema 'audit'
  |           `-- Table 'activity_log'
  |
  +-- Database 2 (e.g., 'my_app_dev')
  |     |
  |     +-- Schema 'public'
  |     |     `-- ... (different set of objects)
  |     `-- ...
  |
  `-- Global Objects (e.g., Roles, Tablespaces)
```

---

### 创建数据库 (Creating Databases)

使用 `CREATE DATABASE` 命令。

**基本语法：**
```sql
CREATE DATABASE database_name;
```

**常用选项：**

*   `OWNER role_name`: 指定数据库的所有者。默认是执行命令的角色。
    ```sql
    CREATE DATABASE my_new_db OWNER db_owner_role;
    ```
*   `TEMPLATE template_name`: 指定创建新数据库所依据的模板。
    *   `template1` (默认): PostgreSQL 的标准系统数据库。如果你在 `template1` 中添加了对象，那么之后所有基于 `template1` 创建的新数据库都会包含这些对象。
    *   `template0`: 一个“纯净”的模板，不包含任何本地（在 `template1` 中）添加的对象。当你想创建一个完全干净的数据库，或者需要指定与 `template1` 不同的编码或区域设置时，通常使用 `template0`。
    ```sql
    CREATE DATABASE clean_db TEMPLATE template0;
    ```
*   `ENCODING encoding_name`: 指定数据库的字符集编码。**强烈推荐使用 `UTF8`。** 这个设置一旦创建就很难更改。
    ```sql
    CREATE DATABASE international_db ENCODING 'UTF8';
    ```
*   `LC_COLLATE collation` 和 `LC_CTYPE ctype`: 指定数据库的区域设置，影响字符串排序规则 (`LC_COLLATE`) 和字符分类 (`LC_CTYPE`，例如什么是字母、大小写如何转换)。这些也非常重要，且创建后很难更改。通常建议使用操作系统环境提供的区域设置，或者显式指定，如 `en_US.UTF-8`。
    ```sql
    CREATE DATABASE localized_db LC_COLLATE 'en_US.UTF-8' LC_CTYPE 'en_US.UTF-8' TEMPLATE template0;
    ```
*   `TABLESPACE tablespace_name`: 指定数据库的默认表空间。
*   `CONNECTION LIMIT limit`: 限制可以连接到此数据库的并发连接数 (`-1` 为无限制)。
*   `ALLOW_CONNECTIONS boolean` (PG12+): 是否允许连接到此数据库。可以设置为 `false` 来创建一个不能直接连接的模板数据库。

**谁可以创建？**
超级用户，或者拥有 `CREATEDB` 属性的角色。

**重要提示**：`CREATE DATABASE` 不能在事务块 (BEGIN/COMMIT) 中执行。

---

### 修改数据库 (Altering Databases)

使用 `ALTER DATABASE` 命令。

**常用操作：**

*   **重命名数据库：**
    ```sql
    ALTER DATABASE old_db_name RENAME TO new_db_name;
    ```
    *注意：你不能连接到正在被重命名的数据库。连接到 `postgres` 或其他数据库执行此操作。活动的连接也可能阻止重命名。*
*   **修改所有者：**
    ```sql
    ALTER DATABASE db_name OWNER TO new_owner_role;
    ```
*   **设置默认表空间：**
    ```sql
    ALTER DATABASE db_name SET TABLESPACE new_tablespace;
    ```
    *这只会影响未来在该数据库中创建且未显式指定表空间的对象，不会移动现有对象。*
*   **修改连接限制：**
    ```sql
    ALTER DATABASE db_name CONNECTION LIMIT 20;
    ```
*   **修改运行时配置参数 (GUCs) 的数据库级别默认值：**
    ```sql
    ALTER DATABASE db_name SET search_path TO my_schema, public;
    ALTER DATABASE db_name SET timezone TO 'UTC';
    ```
    *这些设置会在连接到该数据库时自动生效，除非被角色或会话级别设置覆盖。*
*   **修改 `ALLOW_CONNECTIONS` (PG 12+)：**
    ```sql
    ALTER DATABASE template_db ALLOW_CONNECTIONS false;
    ```

**不可修改的属性：**
通常，数据库的 `ENCODING`, `LC_COLLATE`, `LC_CTYPE` 在创建后**不能通过 `ALTER DATABASE` 直接修改**。要更改这些，通常需要：
1.  创建一个具有期望设置的新数据库。
2.  使用 `pg_dump` 从旧数据库导出数据。
3.  将数据导入到新数据库。
4.  删除旧数据库。

---

### 删除数据库 (Deleting Databases)

使用 `DROP DATABASE` 命令。

**基本语法：**
```sql
DROP DATABASE database_name;
```

**选项：**

*   `IF EXISTS`: 如果数据库不存在，则不报错。
    ```sql
    DROP DATABASE IF EXISTS old_db;
    ```
*   `WITH (FORCE)` (PG13+): 这是一个**危险**的选项，它会尝试终止连接到目标数据库的所有现有连接，然后删除数据库。在正常情况下，如果存在活动连接，`DROP DATABASE` 会失败。**请谨慎使用 `FORCE`，因为它会中断正在进行的操作。**
    ```sql
    DROP DATABASE active_db WITH (FORCE);
    ```

**重要限制：**
*   你**不能删除你当前连接到的数据库**。你需要连接到另一个数据库 (如 `postgres` 或 `template1`) 来执行 `DROP DATABASE` 命令。
*   默认情况下，如果数据库仍有活动连接，命令会失败。

---

### 管理模式 (Managing Schemas)

#### 1. 创建模式 (Creating Schemas)

使用 `CREATE SCHEMA` 命令。

**基本语法：**
```sql
CREATE SCHEMA schema_name;
```

**选项：**

*   `AUTHORIZATION role_name`: 指定模式的所有者。
    ```sql
    CREATE SCHEMA hr_data AUTHORIZATION hr_manager_role;
    ```
    如果省略，则执行命令的角色成为所有者。
*   `IF NOT EXISTS`: 如果模式已存在，则不报错。
    ```sql
    CREATE SCHEMA IF NOT EXISTS staging_area;
    ```
*   在创建模式的同时创建对象：
    ```sql
    CREATE SCHEMA sales_app
        CREATE TABLE invoices (id INT PRIMARY KEY, amount DECIMAL)
        CREATE VIEW open_invoices AS SELECT * FROM invoices WHERE status = 'open';
    ```

#### 2. 修改模式 (Altering Schemas)

使用 `ALTER SCHEMA` 命令。

*   **重命名模式：**
    ```sql
    ALTER SCHEMA old_schema_name RENAME TO new_schema_name;
    ```
*   **修改所有者：**
    ```sql
    ALTER SCHEMA schema_name OWNER TO new_owner_role;
    ```

#### 3. 删除模式 (Dropping Schemas)

使用 `DROP SCHEMA` 命令。

**基本语法：**
```sql
DROP SCHEMA schema_name;
```

**选项：**

*   `IF EXISTS`: 如果模式不存在，则不报错.
*   `CASCADE`: **删除模式及其包含的所有对象** (表、视图、函数等)。**这是非常危险的操作，请务必小心！**
    ```sql
    DROP SCHEMA old_app_schema CASCADE;
    ```
*   `RESTRICT` (默认): 如果模式包含任何对象，则拒绝删除操作。这是更安全的选择。
    ```sql
    DROP SCHEMA empty_schema RESTRICT; -- 如果 empty_schema 为空则成功
    ```

#### 4. `search_path` 的重要性

`search_path` 是一个会话级别的参数，它决定了 PostgreSQL 在查找没有明确模式限定的对象（如表、函数）时，会按什么顺序搜索哪些模式。

*   查看当前的 `search_path`:
    ```sql
    SHOW search_path;
    -- 可能的输出: "$user", public
    -- "$user" 表示与当前用户名同名的模式（如果存在）
    ```
*   修改当前会话的 `search_path`:
    ```sql
    SET search_path TO myschema, public;
    ```
    现在，如果你执行 `SELECT * FROM mytable;`，PostgreSQL 会先在 `myschema` 中找 `mytable`，如果找不到，再在 `public` 模式中找。
*   为角色设置默认 `search_path`:
    ```sql
    ALTER ROLE myuser SET search_path = app_schema, extensions_schema, public;
    ```
*   为数据库设置默认 `search_path`:
    ```sql
    ALTER DATABASE mydb SET search_path = app_schema, public;
    ```

#### 5. 模式权限

模式本身也有权限，主要的是：

*   `USAGE`: 允许角色“看到”模式并访问其中的对象（前提是角色对这些具体对象也有权限）。这是访问模式内任何对象的基础权限。
*   `CREATE`: 允许角色在模式中创建新对象（表、视图、函数等）。

**授予权限示例：**
```sql
-- 允许 web_user 访问 app_schema 中的对象 (但不能创建)
GRANT USAGE ON SCHEMA app_schema TO web_user;

-- 允许 developer_role 在 app_schema 中创建对象
GRANT CREATE ON SCHEMA app_schema TO developer_role;

-- 授予所有权限 (包括 USAGE 和 CREATE)
GRANT ALL ON SCHEMA app_schema TO schema_admin;
```

**关于 `public` 模式的特别说明：**
默认情况下，`PUBLIC` 角色（代表所有用户）对 `public` 模式拥有 `CREATE` 和 `USAGE` 权限。这意味着任何用户都可以在 `public` 模式下创建对象。
**最佳实践**：通常建议撤销 `PUBLIC` 角色在 `public` 模式下的 `CREATE` 权限，以避免意外的对象创建和混乱。
```sql
REVOKE CREATE ON SCHEMA public FROM PUBLIC;
-- 然后根据需要，显式地将 CREATE 权限授予特定的角色/组
GRANT CREATE ON SCHEMA public TO application_creators;
GRANT USAGE ON SCHEMA public TO PUBLIC; -- USAGE 通常可以保留给 PUBLIC
```

---

### 扩展与应用

*   **数据库 vs. 模式的选择**：
    *   **需要强隔离、不同所有者、不同字符集/排序规则、不同备份策略、不同租户共享完全不同的数据集**：使用多个**数据库**。
    *   **逻辑组织、共享数据库级资源 (如连接池、某些扩展)、避免命名冲突、细粒度权限控制、轻量级多租户 (数据不交叉但逻辑分离)**：在单个数据库内使用多个**模式**。
    *   在微服务架构中，每个服务通常对应自己的数据库或一组专用模式。

*   **模板数据库的妙用**：
    *   可以创建一个自定义的 `template_app` 数据库，预装好所有应用需要的扩展、通用函数、用户角色、模式结构等。
    *   然后 `CREATE DATABASE new_customer_db TEMPLATE template_app;` 就能快速复制一个完整的应用环境。
    *   记得 `ALTER DATABASE template_app ALLOW_CONNECTIONS false;` 以防止直接修改模板。

*   **模式在应用升级中的作用**：
    *   可以使用模式来实现蓝绿部署或版本化。例如，`app_v1` 模式和 `app_v2` 模式。通过修改应用的 `search_path` 或直接限定对象名来切换版本。

*   **`ALTER DEFAULT PRIVILEGES` 与模式**：
    与角色部分提到的 `ALTER DEFAULT PRIVILEGES` 结合使用模式非常强大。
    ```sql
    -- app_owner 创建的表，默认授予 app_user SELECT 权限
    ALTER DEFAULT PRIVILEGES FOR ROLE app_owner IN SCHEMA app_specific_schema
       GRANT SELECT ON TABLES TO app_user_group;
    ```
    这确保了在特定模式内由特定角色创建的新对象自动拥有预设的权限。

---

通过理解数据库和模式的层级关系和各自的用途，你可以更好地设计和管理你的 PostgreSQL 环境，使其既安全又易于维护。