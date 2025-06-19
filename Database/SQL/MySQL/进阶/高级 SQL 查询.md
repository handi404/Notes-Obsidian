高级 SQL 查询是发挥关系型数据库强大能力的关键。掌握了这些，你就能从复杂的数据中提取有价值的信息。我们来逐一深入探讨这些高级查询技巧。

---

### 一、多表连接 (JOINs)

JOINs 用于根据表之间相关的列将不同表中的行组合起来。这是关系型数据库的核心操作之一。

**假设我们有以下两张表作为示例：**

`employees` 表:

| id  | name      | department_id |
| --- | --------- | ------------- |
| 1   | Alice     | 101           |
| 2   | Bob       | 102           |
| 3   | Charlie   | 101           |
| 4   | David     | NULL          |

`departments` 表:

| id  | name          |
| --- | ------------- |
| 101 | Engineering   |
| 102 | Marketing     |
| 103 | Sales         |

1.  **`INNER JOIN` (或简写为 `JOIN`) - 内连接**
    *   **作用：** 返回两个表中连接条件匹配的行。如果某行在另一个表中没有匹配的行，则该行不会出现在结果中。
    *   **语法：**
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        INNER JOIN departments d ON e.department_id = d.id;
        ```
    *   **结果：**
        | employee_name | department_name |
        | ------------- | --------------- |
        | Alice         | Engineering     |
        | Bob           | Marketing       |
        | Charlie       | Engineering     |
        *(David 和 Sales 部门不会出现，因为 David 没有部门 ID 匹配，Sales 部门没有员工匹配)*
    *   **何时使用：** 当你只需要那些在两个表中都有对应关系的记录时。

2.  **`LEFT JOIN` (或 `LEFT OUTER JOIN`) - 左连接**
    *   **作用：** 返回左表 (FROM 子句中第一个表) 的所有行，以及右表中匹配的行。如果右表中没有匹配的行，则右表的列将显示为 `NULL`。
    *   **语法：**
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        LEFT JOIN departments d ON e.department_id = d.id;
        ```
    *   **结果：**
        | employee_name | department_name |
        | ------------- | --------------- |
        | Alice         | Engineering     |
        | Bob           | Marketing       |
        | Charlie       | Engineering     |
        | David         | NULL            |
        *(David 出现在结果中，因为他是左表的记录，即使他的 department_id 是 NULL 或没有匹配的部门)*
    *   **何时使用：** 当你需要左表的所有记录，即使它们在右表中没有匹配项时。例如，查找所有员工及其部门，即使某些员工尚未分配部门。

3.  **`RIGHT JOIN` (或 `RIGHT OUTER JOIN`) - 右连接**
    *   **作用：** 返回右表 (JOIN 子句中第二个表) 的所有行，以及左表中匹配的行。如果左表中没有匹配的行，则左表的列将显示为 `NULL`。
    *   **语法：**
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        RIGHT JOIN departments d ON e.department_id = d.id;
        ```
    *   **结果：**
        | employee_name | department_name |
        | ------------- | --------------- |
        | Alice         | Engineering     |
        | Bob           | Marketing       |
        | Charlie       | Engineering     |
        | NULL          | Sales           |
        *(Sales 部门出现在结果中，即使没有员工属于该部门)*
    *   **何时使用：** 当你需要右表的所有记录，即使它们在左表中没有匹配项时。例如，查找所有部门及其员工，即使某些部门没有员工。实际上，`RIGHT JOIN` 总是可以改写为 `LEFT JOIN` (通过交换表的位置)，所以有些人倾向于只使用 `LEFT JOIN` 以保持一致性。

4.  **`FULL OUTER JOIN` (或 `FULL JOIN`) - 全外连接**
    *   **作用：** 返回左表和右表中的所有行。如果某行在一个表中有匹配，则显示匹配的行；如果在一个表中没有匹配，则另一个表的列显示为 `NULL`。
    *   **MySQL 的模拟方式：** MySQL 本身不直接支持 `FULL OUTER JOIN` 语法。可以通过 `LEFT JOIN` 和 `RIGHT JOIN` 的结果使用 `UNION` (或 `UNION ALL`，取决于是否需要去重) 来模拟。
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        LEFT JOIN departments d ON e.department_id = d.id
        UNION -- 使用 UNION 会去除完全重复的行 (如果 Alice 同时被左右连接匹配到)
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        RIGHT JOIN departments d ON e.department_id = d.id
        WHERE e.id IS NULL; -- 关键：只取右连接中左表不匹配的部分，避免与左连接结果重复
        ```
        更简洁的模拟 (通常使用 `UNION` 会自动处理重复，但为了逻辑清晰可以这样理解):
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        LEFT JOIN departments d ON e.department_id = d.id
        UNION ALL -- 如果确定左右两部分没有交集，或想保留所有行，用 ALL
        SELECT NULL, d.name -- 或者 e.name
        FROM departments d
        WHERE NOT EXISTS (SELECT 1 FROM employees e2 WHERE e2.department_id = d.id);
        ```
        或者标准写法(使用 `UNION` 天然去重)
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        LEFT JOIN departments d ON e.department_id = d.id
        UNION
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        RIGHT JOIN departments d ON e.department_id = d.id;
        ```
    *   **结果 (模拟后)：**
        | employee_name | department_name |
        | ------------- | --------------- |
        | Alice         | Engineering     |
        | Bob           | Marketing       |
        | Charlie       | Engineering     |
        | David         | NULL            |
        | NULL          | Sales           |
    *   **何时使用：** 当你需要两个表中的所有记录，并显示它们之间的匹配关系（或不匹配关系）时。

5.  **`CROSS JOIN` (或笛卡尔积 `CARTESIAN JOIN`)**
    *   **作用：** 返回第一个表的每一行与第二个表的每一行的所有可能组合。结果集的行数是两个表行数的乘积。
    *   **语法：**
        ```sql
        SELECT e.name AS employee_name, d.name AS department_name
        FROM employees e
        CROSS JOIN departments d;
        -- 或者使用旧式逗号分隔 (不推荐，容易与忘记写 JOIN 条件的 INNER JOIN 混淆)
        -- SELECT e.name, d.name FROM employees e, departments d;
        ```
    *   **结果：** (4 行员工 * 3 行部门 = 12 行)
        | employee_name | department_name |
        | ------------- | --------------- |
        | Alice         | Engineering     |
        | Alice         | Marketing       |
        | Alice         | Sales           |
        | Bob           | Engineering     |
        | ...           | ...             |
    *   **何时使用：** 通常情况下应避免使用 `CROSS JOIN`，因为它可能产生非常大的结果集。但在某些特定场景下有用，例如生成测试数据、所有可能的组合等。如果 `INNER JOIN` 忘记写 `ON` 条件，其效果就等同于 `CROSS JOIN` (然后被 `WHERE` 过滤)，这是常见的错误。

6.  **自连接 (Self Join)**
    *   **作用：** 表与自身进行连接。这在需要比较同一表内不同行的数据时非常有用。
    *   **语法：** 必须为表使用别名。
        ```sql
        -- 假设 employees 表有 manager_id 列，引用同一个表的 id 列
        -- 查询每个员工及其经理的姓名
        CREATE TABLE employees_with_manager (
            id INT PRIMARY KEY,
            name VARCHAR(50),
            manager_id INT,
            FOREIGN KEY (manager_id) REFERENCES employees_with_manager(id)
        );
        INSERT INTO employees_with_manager VALUES
        (1, 'Alice', NULL), (2, 'Bob', 1), (3, 'Charlie', 1);

        SELECT e1.name AS employee_name, e2.name AS manager_name
        FROM employees_with_manager e1
        LEFT JOIN employees_with_manager e2 ON e1.manager_id = e2.id;
        ```
    *   **结果：**
        | employee_name | manager_name |
        | ------------- | ------------ |
        | Alice         | NULL         |
        | Bob           | Alice        |
        | Charlie       | Alice        |
    *   **何时使用：** 处理具有层级关系的数据（如员工与经理、类别与子类别）、查找相邻记录等。

**JOINs 的最佳实践与注意事项：**

*   **明确 `ON` 条件：** `ON` 子句中的连接条件非常关键，确保它是正确的，并且相关的列已经建立了索引以提高性能。
*   **使用表别名：** 当连接多个表或自连接时，使用简短且有意义的表别名可以使查询更易读。
*   **选择正确的 JOIN 类型：** 理解每种 JOIN 类型的含义，根据需求选择最合适的。
*   **性能：** JOIN 操作，特别是涉及大表时，可能会消耗大量资源。确保连接键上有索引。`EXPLAIN` 是分析 JOIN 性能的好工具。
*   **`USING` 子句：** 如果连接的列在两个表中名称相同，可以使用 `USING(column_name)` 代替 `ON t1.column_name = t2.column_name`。例如：`FROM employees e JOIN departments d USING(department_id)`。

---

### 二、子查询 (Subqueries)

子查询是嵌套在另一个 SQL 查询 (外部查询) 中的查询。子查询的结果可以被外部查询使用。

**类型：**

1.  **标量子查询 (Scalar Subquery)**
    *   **作用：** 返回单个值（一行一列）。
    *   **位置：** 可以用在 `SELECT` 列表、`WHERE` 子句、`HAVING` 子句中，以及任何期望单个值的地方。
    *   **示例：** 查找工资高于平均工资的员工。
        ```sql
        SELECT name, salary
        FROM employees
        WHERE salary > (SELECT AVG(salary) FROM employees);
        ```
    *   **注意：** 如果标量子查询返回多于一行或一列，通常会导致错误。

2.  **行子查询 (Row Subquery)**
    *   **作用：** 返回单行，但可能包含多列。
    *   **位置：** 主要用在 `WHERE` 或 `HAVING` 子句中与行构造器进行比较。
    *   **示例：** 查找与某个特定员工部门和职位都相同的其他员工 (假设有 `job_title` 列)。
        ```sql
        SELECT name
        FROM employees
        WHERE (department_id, job_title) = (SELECT department_id, job_title FROM employees WHERE name = 'Alice')
          AND name <> 'Alice';
        ```

3.  **列子查询 (Column Subquery)**
    *   **作用：** 返回单列，但可能包含多行。
    *   **位置：** 通常与 `IN`, `NOT IN`, `ANY`, `ALL` 等操作符一起用在 `WHERE` 子句中。
    *   **示例：** 查找在 'Engineering' 或 'Marketing' 部门的员工。
        ```sql
        SELECT name
        FROM employees
        WHERE department_id IN (SELECT id FROM departments WHERE name IN ('Engineering', 'Marketing'));
        ```

4.  **表子查询 (Table Subquery / Derived Table)**
    *   **作用：** 返回一个结果集（多行多列），就像一个临时表。
    *   **位置：** 主要用在 `FROM` 子句中，必须为其指定一个别名。
    *   **示例：** 查询每个部门的平均工资，然后再找出平均工资大于 50000 的部门。
        ```sql
        SELECT dept_avg_salary.department_name, dept_avg_salary.avg_sal
        FROM (
            SELECT d.name AS department_name, AVG(e.salary) AS avg_sal
            FROM employees e
            JOIN departments d ON e.department_id = d.id
            GROUP BY d.name
        ) AS dept_avg_salary -- 必须有别名
        WHERE dept_avg_salary.avg_sal > 50000;
        ```
        (这个例子也可以用 `HAVING` 实现，但表子查询展示了其用法)

**`EXISTS` 和 `IN` 的使用与区别：**

*   **`IN (subquery)`:**
    *   **作用：** 判断外部查询的某个表达式的值是否存在于子查询返回的结果集中。
    *   **执行方式：** 通常情况下，数据库会先执行子查询，将其结果集物化（存储起来），然后外部查询的每一行再去这个结果集中查找匹配。
    *   **示例：** `SELECT * FROM employees WHERE department_id IN (SELECT id FROM departments WHERE location = 'New York');`
    *   **对 `NULL` 的处理：** 如果子查询结果集中包含 `NULL`，并且外部查询的列值也是 `NULL`，`IN` 的判断会比较复杂 (`NULL IN (1, NULL)` 结果是 `NULL`，不是 `TRUE`)。

*   **`EXISTS (subquery)`:**
    *   **作用：** 判断子查询是否返回任何行。如果子查询返回至少一行，`EXISTS` 为 `TRUE`；如果子查询不返回任何行，`EXISTS` 为 `FALSE`。
    *   **执行方式 (相关子查询 - Correlated Subquery)：** 子查询通常会依赖于外部查询的当前行值。对于外部查询的每一行，都会执行一次子查询。子查询的目标不是返回数据给外部查询，而是告诉外部查询“是否存在这样的记录”。
    *   **示例 (查找有员工的部门)：**
        ```sql
        SELECT d.name
        FROM departments d
        WHERE EXISTS (SELECT 1 FROM employees e WHERE e.department_id = d.id);
                                     -- 子查询中的 d.id 来自外部查询的当前行
                                     -- SELECT 1 是惯用法，表示我们不关心具体值，只关心是否存在
        ```
    *   **对 `NULL` 的处理：** `EXISTS` 不太受 `NULL` 值影响，因为它只关心行是否存在。

*   **`NOT IN (subquery)` vs. `NOT EXISTS (subquery)` (重要区别！)：**
    *   **`NOT IN`：** 如果子查询返回的结果集中包含任何 `NULL` 值，则 `column NOT IN (subquery_results_with_null)` 的结果总是 `FALSE` 或 `NULL` (永远不会是 `TRUE`)，这通常不是我们期望的。**因此，使用 `NOT IN` 时要非常小心子查询结果中的 `NULL`。**
        *   例如：`'A' NOT IN ('B', NULL)` 结果是 `NULL`。
    *   **`NOT EXISTS`：** 通常是更安全、更推荐的替代 `NOT IN` 的方式，尤其当子查询可能返回 `NULL` 时。
        *   **示例 (查找没有员工的部门)：**
            ```sql
            SELECT d.name
            FROM departments d
            WHERE NOT EXISTS (SELECT 1 FROM employees e WHERE e.department_id = d.id);
            ```

*   **性能比较：**
    *   传统观点认为，如果子查询结果集小，`IN` 可能更快；如果外部查询结果集小（对于相关子查询），`EXISTS` 可能更快。
    *   现代数据库优化器越来越智能，很多情况下它们能够将 `IN` 和 `EXISTS` 转换为相似的执行计划 (例如，转换为 JOIN)。
    *   **最佳实践是：根据语义选择最清晰的表达方式，然后通过 `EXPLAIN` 分析性能。如果 `NOT IN` 遇到 `NULL` 问题，优先考虑 `NOT EXISTS` 或确保子查询不返回 `NULL`。**

**子查询的注意事项：**

*   **性能：** 滥用子查询，特别是相关子查询，可能导致性能问题，因为子查询可能对外部查询的每一行都执行一次。
*   **可读性：** 过多嵌套的子查询会降低 SQL 的可读性。可以考虑使用 JOINs、CTEs (见下文) 来重构复杂的子查询。
*   **限制：** 某些数据库对子查询的嵌套层数有限制。

---

### 三、聚合函数 (Aggregate Functions) 深入

聚合函数对一组值进行计算，并返回单个汇总值。它们通常与 `GROUP BY` 子句一起使用。

1.  **`COUNT(expression | *)`**
    *   `COUNT(*)`: 返回表中的总行数（如果和 `GROUP BY` 连用，则是每个组的总行数）。
    *   `COUNT(column_name)`: 返回指定列中非 `NULL` 值的数量。
    *   `COUNT(DISTINCT column_name)`: 返回指定列中非 `NULL` 且唯一的值的数量。
    *   **示例：**
        ```sql
        SELECT COUNT(*) AS total_employees FROM employees;
        SELECT department_id, COUNT(id) AS num_employees -- COUNT(id) 或 COUNT(*) 在这里效果类似
        FROM employees
        GROUP BY department_id;
        SELECT COUNT(DISTINCT department_id) AS num_departments_with_employees
        FROM employees;
        ```

2.  **`SUM(expression)`**
    *   返回表达式中所有非 `NULL` 值的总和。只适用于数字类型。
    *   `SUM(DISTINCT column_name)`: 返回唯一值的总和。
    *   **示例：**
        ```sql
        SELECT SUM(salary) AS total_salary_payout FROM employees;
        SELECT department_id, SUM(salary) AS department_total_salary
        FROM employees
        GROUP BY department_id;
        ```

3.  **`AVG(expression)`**
    *   返回表达式中所有非 `NULL` 值的平均值。只适用于数字类型。
    *   `AVG(DISTINCT column_name)`: 返回唯一值的平均值。
    *   **示例：**
        ```sql
        SELECT AVG(salary) AS average_salary FROM employees;
        ```

4.  **`MIN(expression)`**
    *   返回表达式中的最小值 (`NULL` 值被忽略)。适用于数字、字符串、日期等可比较类型。
    *   **示例：**
        ```sql
        SELECT MIN(salary) AS lowest_salary FROM employees;
        ```

5.  **`MAX(expression)`**
    *   返回表达式中的最大值 (`NULL` 值被忽略)。适用于数字、字符串、日期等可比较类型。
    *   **示例：**
        ```sql
        SELECT MAX(salary) AS highest_salary FROM employees;
        ```

6.  **`GROUP_CONCAT([DISTINCT] expression_list [ORDER BY column_list] [SEPARATOR str_val])` (MySQL 特有)**
    *   **作用：** 将一个组内多行的某个列（或表达式）的值连接成一个字符串，用指定的分隔符（默认为逗号 `,`）隔开。
    *   **参数：**
        *   `DISTINCT`: 可选，只连接唯一的值。
        *   `expression_list`: 要连接的列或表达式。
        *   `ORDER BY`: 可选，指定连接顺序。
        *   `SEPARATOR`: 可选，指定分隔符字符串。
    *   **示例：** 列出每个部门的所有员工姓名。
        ```sql
        SELECT
            d.name AS department_name,
            GROUP_CONCAT(e.name ORDER BY e.name ASC SEPARATOR '; ') AS employee_list
        FROM departments d
        LEFT JOIN employees e ON d.id = e.department_id
        GROUP BY d.id, d.name;
        ```
    *   **结果可能如下：**
        | department_name | employee_list               |
        | --------------- | --------------------------- |
        | Engineering     | Alice; Charlie              |
        | Marketing       | Bob                         |
        | Sales           | NULL (或空字符串，取决于配置) |
    *   **注意：** `GROUP_CONCAT` 返回的字符串长度受 `group_concat_max_len` 系统变量限制 (默认 1024 字节)，如果结果超长会被截断。可以调整此变量。

**聚合函数与 `GROUP BY`：**

*   当 `SELECT` 列表中同时包含聚合函数和非聚合列时，所有非聚合列必须出现在 `GROUP BY` 子句中 (除非它们函数依赖于 `GROUP BY` 列，这是 `ONLY_FULL_GROUP_BY` SQL mode 的要求)。
*   `HAVING` 子句用于在 `GROUP BY` 分组后过滤组，`HAVING` 中可以使用聚合函数。

---

### 四、窗口函数 (Window Functions) (MySQL 8.0+ 重点！)

窗口函数对与当前行相关的“窗口”（一组表行）执行计算。与聚合函数不同，窗口函数不将多行聚合成单行，而是为结果集中的每一行都返回一个值。

**基本语法：**
`function_name() OVER ( [PARTITION BY partition_expression, ...] [ORDER BY sort_expression [ASC|DESC], ...] [frame_clause] )`

*   `function_name()`: 窗口函数名 (如 `ROW_NUMBER()`, `SUM()`, `AVG()`)。
*   `OVER()`: 关键字，指示这是一个窗口函数。
*   `PARTITION BY partition_expression, ...`: 可选。将行分成多个分区（组），窗口函数在每个分区内独立计算。如果省略，整个结果集视为一个分区。
*   `ORDER BY sort_expression [ASC|DESC], ...`: 可选。定义分区内行的顺序。对于排名函数和某些聚合窗口函数是必需的。
*   `frame_clause`: 可选 (更高级)。定义当前行相关的窗口范围（如“当前行及其前两行”）。默认行为通常是 `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` 或类似，具体取决于函数和 `ORDER BY`。

**常用窗口函数：**

1.  **排名函数 (Ranking Functions)：**
    *   **`ROW_NUMBER() OVER (ORDER BY ...)`:** 为结果集中的每一行（或分区内的每一行）分配一个从 1 开始的连续唯一排名。即使值相同，排名也不同。
        ```sql
        SELECT name, salary,
               ROW_NUMBER() OVER (ORDER BY salary DESC) AS salary_rank_row_number
        FROM employees;
        ```
    *   **`RANK() OVER (ORDER BY ...)`:** 为每一行分配排名。如果有多行具有相同的值，它们将获得相同的排名，但下一个排名将被跳过。例如：1, 2, 2, 4。
        ```sql
        SELECT name, salary,
               RANK() OVER (ORDER BY salary DESC) AS salary_rank_rank
        FROM employees;
        ```
    *   **`DENSE_RANK() OVER (ORDER BY ...)`:** 为每一行分配排名。如果有多行具有相同的值，它们将获得相同的排名，且下一个排名是连续的。例如：1, 2, 2, 3。
        ```sql
        SELECT name, salary,
               DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank_dense
        FROM employees;
        ```
    *   **`NTILE(n) OVER (ORDER BY ...)`:** 将有序的分区划分为 `n` 个大致相等的组（桶），并为每一行分配其所在的桶号 (从 1 到 `n`)。
        ```sql
        -- 将员工按工资分为 4 个等级
        SELECT name, salary,
               NTILE(4) OVER (ORDER BY salary DESC) AS salary_quartile
        FROM employees;
        ```
    *   **分区使用：** 所有排名函数都可以配合 `PARTITION BY` 在每个分区内独立排名。
        ```sql
        -- 按部门对员工工资进行排名 (dense_rank)
        SELECT name, department_id, salary,
               DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rank_in_department
        FROM employees;
        ```

2.  **值窗口函数 (Value Window Functions) / 偏移函数 (Offset Functions)：**
    *   **`LAG(expression [, offset [, default_value]]) OVER (ORDER BY ...)`:** 访问当前行之前的第 `offset` 行 (默认为 1) 的 `expression` 值。如果不存在，则返回 `default_value` (默认为 `NULL`)。
        ```sql
        -- 查询每个员工的工资以及其同部门工资排名前一位员工的工资
        SELECT name, department_id, salary,
               LAG(salary, 1, 0) OVER (PARTITION BY department_id ORDER BY salary ASC) AS previous_salary
        FROM employees;
        ```
    *   **`LEAD(expression [, offset [, default_value]]) OVER (ORDER BY ...)`:** 访问当前行之后的第 `offset` 行 (默认为 1) 的 `expression` 值。
        ```sql
        -- 查询每个月销售额以及下个月的销售额
        SELECT month, sales,
               LEAD(sales, 1, 0) OVER (ORDER BY month ASC) AS next_month_sales
        FROM monthly_sales;
        ```
    *   **`FIRST_VALUE(expression) OVER (... ORDER BY ... [frame_clause])`:** 返回窗口框架内第一行的 `expression` 值。
    *   **`LAST_VALUE(expression) OVER (... ORDER BY ... [frame_clause])`:** 返回窗口框架内最后一行的 `expression` 值。 (注意默认的 `frame_clause` 可能不是你想要的，通常需要显式指定如 `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` 才能获取整个分区的最后一个值)。
    *   **`NTH_VALUE(expression, n) OVER (... ORDER BY ... [frame_clause])`:** 返回窗口框架内第 `n` 行的 `expression` 值。

3.  **聚合窗口函数 (Aggregate Window Functions)：**
    *   可以使用标准的聚合函数 (如 `SUM()`, `AVG()`, `COUNT()`, `MIN()`, `MAX()`) 作为窗口函数。
    *   它们在定义的窗口框架内计算聚合值，但为每一行都返回结果。
    *   **示例：** 计算每个员工的工资以及其所在部门的总工资和平均工资。
        ```sql
        SELECT name, department_id, salary,
               SUM(salary) OVER (PARTITION BY department_id) AS department_total_salary,
               AVG(salary) OVER (PARTITION BY department_id) AS department_average_salary
        FROM employees;
        ```
    *   **示例：** 计算每个员工的工资以及截至当前员工（按工资排序）的累计工资（Running Total）。
        ```sql
        SELECT name, salary,
               SUM(salary) OVER (ORDER BY salary ASC, id ASC ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running_total_salary
        FROM employees;
        -- ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW 是默认的frame，通常可以省略。
        -- 但对于某些场景，如 LAST_VALUE，或需要精确控制范围时，frame_clause 很重要。
        ```

**窗口函数的优势：**

*   **简洁性：** 可以用更简洁的 SQL 实现复杂的分析查询，而不需要使用复杂的自连接或相关子查询。
*   **性能：** 通常比等效的子查询或自连接更高效，因为数据库优化器可以更好地处理它们。
*   **可读性：** 提高了复杂分析查询的可读性。

---

### 五、公用表表达式 (CTEs - Common Table Expressions) (MySQL 8.0+ 重点！)

CTE 是一个临时的、命名的结果集，你可以在单个 SQL 语句（如 `SELECT`, `INSERT`, `UPDATE`, `DELETE` 或 `CREATE VIEW`）中引用它。CTE 使查询更易读、更模块化。

**基本语法 (非递归 CTE)：**
`WITH cte_name [(column_name_list)] AS ( subquery_defining_the_CTE ) SELECT ... FROM cte_name ...;`

*   `WITH`: 关键字，引入一个或多个 CTE。
*   `cte_name`: 你为 CTE 指定的名称。
*   `(column_name_list)`: 可选，为 CTE 的列指定名称。如果省略，则使用子查询的列名。
*   `AS (subquery_defining_the_CTE)`: 定义 CTE 的子查询。
*   可以在一个 `WITH` 子句中定义多个 CTE，用逗号分隔。后续的 CTE 可以引用前面定义的 CTE。

**示例 (非递归 CTE)：**
假设我们要找到每个部门中工资最高的员工。

```sql
WITH DepartmentMaxSalaries AS ( -- CTE 1: 计算每个部门的最高工资
    SELECT department_id, MAX(salary) AS max_salary
    FROM employees
    GROUP BY department_id
),
RankedEmployees AS ( -- CTE 2: (可选) 为员工按部门和工资排名
    SELECT e.name, e.salary, e.department_id,
           DENSE_RANK() OVER (PARTITION BY e.department_id ORDER BY e.salary DESC) as rn
    FROM employees e
)
SELECT e.name, e.salary, d.name AS department_name
FROM employees e
JOIN departments d ON e.department_id = d.id
JOIN DepartmentMaxSalaries dms ON e.department_id = dms.department_id AND e.salary = dms.max_salary;

-- 或者使用第二个 CTE (RankedEmployees)
-- SELECT re.name, re.salary, d.name AS department_name
-- FROM RankedEmployees re
-- JOIN departments d ON re.department_id = d.id
-- WHERE re.rn = 1;
```

**递归 CTE (Recursive CTEs)：**
递归 CTE 可以引用自身，用于处理具有层级结构或图结构的数据。

**语法：**
`WITH RECURSIVE cte_name [(column_name_list)] AS ( initial_query -- 非递归部分 (锚点成员) UNION ALL recursive_query -- 递归部分，引用 cte_name 自身 ) SELECT ... FROM cte_name ...;`

*   `RECURSIVE`: 关键字，表明这是一个递归 CTE。
*   `initial_query` (锚点成员 Anchor Member): 返回递归的基础结果集，它不引用 CTE 自身。
*   `UNION ALL` (或 `UNION DISTINCT`，但 `UNION ALL` 更常见且通常性能更好，除非确实需要去重且锚点和递归部分可能产生重复)：连接锚点成员和递归成员。
*   `recursive_query` (递归成员 Recursive Member): 引用 CTE 自身，并根据前一次迭代的结果生成新的行，直到递归成员不再返回任何行为止。
*   **终止条件：** 递归必须有一个终止条件，否则会无限循环。终止条件通常是递归成员在某次迭代后不再产生新的行（例如，连接条件不再满足）。MySQL 也有 `cte_max_recursion_depth` 系统变量来防止无限递归 (默认 1000)。

**示例 (递归 CTE)：**
假设我们有一个层级结构的 `employees_with_manager` 表 (如自连接示例中)，我们要查询 Alice 的所有下属 (直接和间接)。

```sql
WITH RECURSIVE EmployeeHierarchy AS (
    -- 锚点成员: 找到起始员工 (Alice)
    SELECT id, name, manager_id, 0 AS level
    FROM employees_with_manager
    WHERE name = 'Alice'

    UNION ALL

    -- 递归成员: 找到上一级别员工的直接下属
    SELECT e.id, e.name, e.manager_id, eh.level + 1
    FROM employees_with_manager e
    JOIN EmployeeHierarchy eh ON e.manager_id = eh.id -- 连接到 CTE 自身
)
SELECT id, name, manager_id, level
FROM EmployeeHierarchy;
```

**CTE 的优势：**

*   **可读性与模块化：** 将复杂查询分解为逻辑上独立的、命名的步骤，使 SQL 更易于理解和维护。
*   **可重用性：** 在单个查询中可以多次引用同一个 CTE (尽管 MySQL 的实现中，如果多次引用，子查询可能会被执行多次，除非优化器能优化)。
*   **递归查询：** 提供了优雅的方式来处理层级数据和图遍历，这是传统 SQL 难以做到的。
*   **替代某些复杂的子查询或临时表：** 使得查询结构更清晰。

**CTE 的注意事项：**

*   **生命周期：** CTE 只在定义它的单个 SQL 语句的执行期间存在。
*   **性能：** 虽然 CTE 提高了可读性，但它们本质上还是子查询。MySQL 优化器对 CTE 的优化程度各不相同。简单的 CTE 通常会被很好地优化 (例如，合并到外部查询或物化)。复杂的或递归的 CTE 需要仔细评估其性能。
*   **非物化：** 默认情况下，MySQL 中的非递归 CTE 通常不会被物化 (即结果不会被存储到临时表中)，而是它们的定义会被“内联”或“合并”到引用它的查询中。递归 CTE 则需要物化中间结果。

---

以上就是关于高级 SQL 查询的详细讲解，包括 JOINs、子查询、聚合函数、窗口函数和 CTEs。这些工具极大地扩展了你从数据库中提取和分析数据的能力。

这些概念中，窗口函数和 CTEs 是 MySQL 8.0 之后非常重要的增强，它们使得很多以前需要复杂 SQL 或应用层逻辑才能完成的任务变得简单和高效。