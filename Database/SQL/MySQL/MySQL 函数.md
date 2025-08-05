MySQL 官方文档中列出的函数有数百个之多。为了做到 **“完美无遗漏且简洁”**，我将采用一种“金字塔”结构：

1.  **宏观分类：** 首先对所有函数进行高层次的分类，让您有一个清晰的全局视图。
2.  **核心函数详解：** 在每个分类下，我会精选出**最常用、最重要**的函数，组成一个速查表，并对其中 1-2 个最具代表性或最容易混淆的函数进行深入剖析（遵循我们之前约定的讲解结构）。
3.  **提及扩展：** 对于不那么常用但特定场景下有奇效的函数，我会简要提及。

这样既能覆盖全貌，又能突出重点，避免陷入冗长的函数手册。

---

### MySQL 函数全景图

MySQL 的内置函数可以主要划分为以下 **七大类**，这基本上涵盖了日常开发与数据分析的 99% 的场景：

1.  **聚合函数 (Aggregate Functions):** 对一组值进行计算，并返回单个值。是 SQL 的核心。
2.  **字符串函数 (String Functions):** 用于处理和操作字符串数据。
3.  **数值函数 (Numeric Functions):** 用于执行数学运算。
4.  **日期与时间函数 (Date and Time Functions):** 用于处理 `DATE`, `DATETIME`, `TIMESTAMP` 等类型的数据。
5.  **流程控制函数 (Control Flow Functions):** 在 SQL 语句中实现条件逻辑。
6.  **窗口函数 (Window Functions):** (MySQL 8.0+ 的明星特性) 对一组与当前行相关的行进行计算。
7.  **JSON 函数 (JSON Functions):** (MySQL 5.7+ 的重要特性) 用于创建、解析和查询 JSON 数据。

下面，我们逐一深入讲解。

---

### 1. 聚合函数 (Aggregate Functions)

它们是 `GROUP BY` 子句的黄金搭档，用于数据汇总。

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`COUNT(expr)`** | 计算行数。`COUNT(*)` 计算所有行，`COUNT(column)` 计算该列非 NULL 的行。 | `SELECT COUNT(*) FROM orders;` |
| **`SUM(expr)`** | 计算数值列的总和。自动忽略 NULL 值。 | `SELECT SUM(amount) FROM orders;` |
| **`AVG(expr)`** | 计算数值列的平均值。自动忽略 NULL 值。 | `SELECT AVG(score) FROM results;` |
| **`MAX(expr)`** | 找出列中的最大值。可用于数值、字符串、日期。 | `SELECT MAX(login_time) FROM logs;` |
| **`MIN(expr)`** | 找出列中的最小值。 | `SELECT MIN(price) FROM products;` |
| **`GROUP_CONCAT(expr)`** | 将分组中的多个字符串值连接成一个单一的字符串。 | `SELECT GROUP_CONCAT(tag) FROM tags WHERE post_id = 1;` |

#### 深度剖析: `GROUP_CONCAT()`

-   **核心概念：** 它是 `GROUP BY` 的“逆向操作”。`GROUP BY` 将多行聚合成一行，而 `GROUP_CONCAT` 则将多行中某一列的值聚合到一个字符串中。
-   **实际应用场景：**
    -   **文章标签系统：** 一篇文章有多个标签，查询文章列表时，希望将所有标签显示在一列。
        ```sql
        SELECT p.title, GROUP_CONCAT(t.name ORDER BY t.name SEPARATOR ', ') AS tags
        FROM posts p
        JOIN post_tags pt ON p.id = pt.post_id
        JOIN tags t ON pt.tag_id = t.id
        GROUP BY p.id;
        ```
    -   **报表生成：** 汇总一个部门下所有员工的姓名。
-   **要点与注意事项：**
    -   **长度限制：** `GROUP_CONCAT` 的结果有最大长度限制，由系统变量 `group_concat_max_len` 控制（默认 1024 字节）。如果拼接内容超长，会被截断。可以动态调整它：`SET SESSION group_concat_max_len = 102400;`
    -   **自定义分隔符：** 默认分隔符是逗号 `,`，可以用 `SEPARATOR` 关键字自定义。
    -   **去重与排序：** 可以在函数内部使用 `DISTINCT` 和 `ORDER BY`。

---

### 2. 字符串函数 (String Functions)

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`CONCAT(s1, s2, ...)`** | 连接多个字符串。任何参数为 NULL，结果即为 NULL。 | `CONCAT('My', 'S', 'QL');` -> 'MySQL' |
| **`CONCAT_WS(sep, s1, s2, ...)`** | 使用指定分隔符连接字符串，自动忽略 NULL 参数。 | `CONCAT_WS('-', 'A', 'B', NULL, 'C');` -> 'A-B-C' |
| **`LENGTH(str)`** | 返回字符串的**字节**长度。 | `LENGTH('你好');` (UTF 8) -> 6 |
| **`CHAR_LENGTH(str)`** | 返回字符串的**字符**长度。 | `CHAR_LENGTH('你好');` -> 2 |
| **`SUBSTRING(str, pos, len)`** | 截取子字符串。 | `SUBSTRING('MySQL', 1, 2);` -> 'My' |
| **`LOCATE(substr, str)`** | 查找子串在字符串中第一次出现的位置。 | `LOCATE('S', 'MySQL');` -> 3 |
| **`REPLACE(str, from_str, to_str)`**| 替换字符串中的所有子串。 | `REPLACE('abab', 'a', 'c');` -> 'cbcb' |
| **`TRIM([remstr FROM] str)`** | 去除字符串两端或指定的字符。 | `TRIM('  hello  ');` -> 'hello' |
| **`FIND_IN_SET(str, strlist)`**| 在逗号分隔的字符串列表 `strlist` 中查找 `str`。 | `FIND_IN_SET('b', 'a,b,c');` -> 2 |
| **`UPPER(str)` / `LOWER(str)`** | 转换为大/小写。 | `UPPER('mysql');` -> 'MYSQL' |

#### 深度剖析: `FIND_IN_SET()` vs `LIKE`

-   **核心概念：** `FIND_IN_SET('b', 'a,b,c')` 用于精确匹配逗号分隔列表中的一个完整单元。`LIKE '%,b,%'` 是模式匹配，更模糊。
-   **实际应用场景：**
    -   `FIND_IN_SET` 用于存储如 `'1,2,7'` 这样的简单 ID 集合的字段。
-   **要点与注意事项 (非常重要！)：**
    -   **性能陷阱：** `FIND_IN_SET` 和 `LIKE '%,value,%'` 都**无法有效利用索引**，当数据量大时，会导致全表扫描，性能极差。
    -   **反模式设计：** 在字段中用逗号分隔存储多个值，本身是**违反数据库第一范式 (1 NF)** 的。它让更新、查询和数据完整性维护变得异常困难。
    -   **最佳实践：** 强烈建议使用**中间表/关联表 (Junction Table)** 来代替这种设计。例如，`post_tags` 表来关联 `posts` 表和 `tags` 表，这才是关系型数据库的正道，性能和扩展性都最佳。

---

### 3. 日期与时间函数 (Date and Time Functions)

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`NOW()` / `CURRENT_TIMESTAMP()`**| 返回当前日期和时间 (YYYY-MM-DD HH:MM:SS)。 | `SELECT NOW();` |
| **`CURDATE()` / `CURRENT_DATE()`**| 返回当前日期 (YYYY-MM-DD)。 | `SELECT CURDATE();` |
| **`CURTIME()` / `CURRENT_TIME()`**| 返回当前时间 (HH:MM:SS)。 | `SELECT CURTIME();` |
| **`DATE_FORMAT(date, format)`** | 将日期按指定格式格式化为字符串。 | `DATE_FORMAT(NOW(), '%Y年%m月%d日');` |
| **`STR_TO_DATE(str, format)`** | 将字符串按指定格式解析为日期。 | `STR_TO_DATE('2023-01-01', '%Y-%m-%d');` |
| **`DATEDIFF(date1, date2)`** | 返回两个日期之间的天数差 (`date1` - `date2`)。 | `DATEDIFF('2023-01-05', '2023-01-01');` -> 4 |
| **`DATE_ADD(date, INTERVAL expr unit)`**| 给日期增加一个时间间隔。 | `DATE_ADD(NOW(), INTERVAL 1 DAY);` |
| **`DATE_SUB(date, INTERVAL expr unit)`**| 给日期减去一个时间间隔。 | `DATE_SUB(CURDATE(), INTERVAL 1 YEAR);` |
| **`UNIX_TIMESTAMP([date])`** | 返回日期的 Unix 时间戳。 | `SELECT UNIX_TIMESTAMP();` |
| **`FROM_UNIXTIME(ts, [format])`**| 将 Unix 时间戳转换为日期。 | `FROM_UNIXTIME(1672531200);` |

#### 深度剖析: `DATE_FORMAT()` 与索引

-   **核心概念：** `DATE_FORMAT` 是一个强大的格式化工具，但它也是一个**“索引杀手”**。
-   **实际应用场景：**
    -   **错误用法 (导致索引失效)：** 查询某个月份的数据。
        ```sql
        -- 错误！对列使用函数，索引无法生效！
        SELECT * FROM orders WHERE DATE_FORMAT(create_time, '%Y-%m') = '2023-01';
        ```
    -   **正确用法 (保证索引生效)：**
        ```sql
        -- 正确！让列保持原始状态，对常量进行计算。
        SELECT * FROM orders WHERE create_time >= '2023-01-01' AND create_time < '2023-02-01';
        ```
-   **要点与注意事项：**
    -   **SARGable 原则：** 这是数据库查询优化的一个重要原则，意为“Search Argument Able”。要保证 `WHERE` 子句中的条件可以利用索引，就必须避免对**被索引的列**使用函数。始终将函数应用在等号的右侧（常量值）上。

---

### 4. 数值函数 (Numeric Functions)

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`ROUND(X, [D])`** | 四舍五入到指定小数位 D。 | `ROUND(123.456, 2);` -> 123.46 |
| **`CEIL(X)` / `CEILING(X)`**| 向上取整，返回不小于 X 的最小整数。 | `CEIL(1.23);` -> 2 |
| **`FLOOR(X)`** | 向下取整，返回不大于 X 的最大整数。 | `FLOOR(1.89);` -> 1 |
| **`ABS(X)`** | 返回 X 的绝对值。 | `ABS(-10);` -> 10 |
| **`RAND()`** | 返回一个 0 到 1 之间的随机浮点数。 | `SELECT RAND();` |
| **`MOD(N, M)` / `N % M`**| 取模，返回 N 除以 M 的余数。 | `MOD(10, 3);` -> 1 |

#### 要点与注意事项：
- **精度问题：** 对于金融、货币等需要高精度的计算，不要使用 `FLOAT` 或 `DOUBLE` 类型，应使用 `DECIMAL` 类型，以避免浮点数精度丢失问题。
- **随机排序：** `ORDER BY RAND()` 在小表上可以实现随机排序，但在大表上性能极差，因为它需要为每一行生成一个随机数并进行排序。大表随机取样有更高效的方案。

---

### 5. 流程控制函数 (Control Flow Functions)

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`IF(expr1, expr2, expr3)`**| 如果 expr1 为真，返回 expr2，否则返回 expr3。 | `SELECT IF(score >= 60, '及格', '不及格') FROM results;` |
| **`IFNULL(expr1, expr2)`** | 如果 expr1 不为 NULL，返回 expr1，否则返回 expr2。 | `SELECT IFNULL(address, '地址未填写') FROM users;` |
| **`NULLIF(expr1, expr2)`**| 如果 expr1 = expr2，返回 NULL，否则返回 expr1。 | `NULLIF(1, 1);` -> NULL, `NULLIF(1, 0);` -> 1 |
| **`CASE ... END`** | 标准的 SQL 条件分支语句，比 `IF` 更强大灵活。 | 见下方深度剖析。 |

#### 深度剖析: `CASE` 语句

-   **核心概念：** 提供了两种形式，用于在查询中实现 `if-then-else` 逻辑。
-   **实际应用场景：**
    -   **简单 CASE (值匹配):**
        ```sql
        SELECT CASE status
                   WHEN 1 THEN '待支付'
                   WHEN 2 THEN '已支付'
                   WHEN 3 THEN '已发货'
                   ELSE '未知状态'
               END AS status_text
        FROM orders;
        ```
    -   **搜索 CASE (条件匹配，更常用):**
        ```sql
        -- 按年龄段给用户打标
        SELECT name, CASE
                         WHEN age < 18 THEN '少年'
                         WHEN age BETWEEN 18 AND 40 THEN '青年'
                         WHEN age > 40 THEN '中老年'
                     END AS age_group
        FROM users;
        ```
-   **要点与注意事项：**
    - `CASE` 是一个表达式，不是一个语句。它可以用在 `SELECT`, `WHERE`, `ORDER BY` 等任何可以出现表达式的地方。
    - `CASE` 语句比多个 `IF` 嵌套更具可读性。

---

### 6. 窗口函数 (Window Functions) (MySQL 8.0+)

这是 MySQL 8.0 引入的革命性功能，极大地增强了分析能力。

| 函数 | 说明 | 示例 (均需配合 `OVER (...)` 子句) |
| :--- | :--- | :--- |
| **`ROW_NUMBER()`**| 为分区中的每一行分配一个从 1 开始的唯一序号。 | `ROW_NUMBER() OVER (PARTITION BY class_id ORDER BY score DESC)` |
| **`RANK()`** | 计算排名。值相同时排名相同，但会跳过后续排名 (1, 2, 2, 4)。 | `RANK() OVER (ORDER BY salary DESC)` |
| **`DENSE_RANK()`**| 计算排名。值相同时排名相同，且不跳过后续排名 (1, 2, 2, 3)。 | `DENSE_RANK() OVER (ORDER BY salary DESC)` |
| **`LEAD(expr, N, default)`** | 获取当前行之后第 N 行的 `expr` 值。 | `LEAD(amount, 1, 0) OVER (ORDER BY month)` |
| **`LAG(expr, N, default)`**| 获取当前行之前第 N 行的 `expr` 值。 | `LAG(amount, 1, 0) OVER (ORDER BY month)` |
| **聚合函数 `OVER()`**| `SUM`, `AVG`, `COUNT` 等聚合函数也可以用作窗口函数。| `SUM(amount) OVER (PARTITION BY user_id)` |

#### 深度剖析: `ROW_NUMBER()` 的应用

-   **核心概念：** 窗口函数在一个“窗口”（由 `OVER` 子句定义的一组行）上执行计算，但不会像 `GROUP BY` 那样将行折叠。它为每一行都返回一个结果。
-   **实际应用场景 (Top-N 问题)：**
    -   **获取每个部门工资最高的 3 名员工：**
        ```sql
        WITH RankedEmployees AS (
            SELECT
                name,
                department,
                salary,
                ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rn
            FROM employees
        )
        SELECT name, department, salary
        FROM RankedEmployees
        WHERE rn <= 3;
        ```
-   **要点与注意事项：**
    -   窗口函数的学习关键在于理解 `OVER()` 子句中的 `PARTITION BY` (分组) 和 `ORDER BY` (排序)。
    -   在没有窗口函数之前，实现 Top-N 问题需要复杂的自连接或相关子查询，性能和可读性都很差。窗口函数是现代 SQL 的必备技能。

---

### 7. JSON 函数 (MySQL 5.7+)

| 函数 | 说明 | 示例 |
| :--- | :--- | :--- |
| **`JSON_OBJECT()`** | 创建 JSON 对象。 | `JSON_OBJECT('id', 1, 'name', 'test');` |
| **`JSON_ARRAY()`** | 创建 JSON 数组。 | `JSON_ARRAY(1, "a", TRUE);` |
| **`JSON_EXTRACT(json, path)`**| 从 JSON 文档中提取数据。快捷方式：`->`。 | `JSON_EXTRACT('{"a": 1}', '$.a');` |
| **`JSON_UNQUOTE(expr)`** | 解除 JSON 值的引号。快捷方式：`->>`。 | `JSON_UNQUOTE(JSON_EXTRACT('{"a": "hello"}', '$.a'));` |
| **`JSON_CONTAINS(json, val, [path])`**| 判断 JSON 文档中是否包含指定值。 | `JSON_CONTAINS('[1,2,3]', '2');` -> 1 |
| **`JSON_TABLE(expr, path COLUMNS(...) ...)`** | (MySQL 8.0) 将 JSON 数据转换为关系表格式。 | (用法较复杂，用于 ETL 和报表) |

#### 深度剖析: `->` vs `->>`

-   **核心概念：** 这是 MySQL 5.7.13 后引入的便捷操作符，是 `JSON_EXTRACT` 的简写。
    -   `column->path`: 提取出的结果**保留原始 JSON 类型**。如果提取的是字符串，会带着双引号。
    -   `column->>path`: 相当于 `JSON_UNQUOTE(JSON_EXTRACT(column, path))`，它会**解除引号**，始终返回一个字符串。
-   **实际应用场景：**
    -   假设 `extra` 列是 JSON 类型：`'{"id": 101, "tags": ["mysql", "json"]}'`
    ```sql
    -- 使用 ->
    SELECT extra->'$.id' FROM users;   -- 结果: 101 (JSON number)
    SELECT extra->'$.tags' FROM users; -- 结果: ["mysql", "json"] (JSON array)

    -- 使用 ->>
    SELECT extra->>'$.tags' FROM users; -- 结果: [\"mysql\", \"json\"] (STRING)

    -- 在 WHERE 子句中使用
    WHERE extra->>'$.name' = '张三'; -- 正确，用 ->> 比较字符串
    WHERE extra->'$.id' = 101;       -- 正确，用 -> 比较数字
    ```
-   **要点与注意事项：**
    -   **JSON 索引：** 对 JSON 列中频繁查询的字段，可以创建**虚拟列 (Generated Column)** 并对其建立索引，从而实现对 JSON 内部数据的索引查询，极大提升性能。这是使用 JSON 功能的必备优化技巧。

---

### 总结

以上七类函数构成了 MySQL 查询与开发的核心工具箱。掌握它们，尤其是每个分类下的核心函数和现代特性（窗口函数、JSON 函数），是从“会用 SQL”到“精通 SQL”的关键一步。

对于未详细展开的函数，当您遇到具体需求时（例如位运算、加密解密、GIS 地理信息等），可以根据这个分类框架，快速定位并查阅官方文档。