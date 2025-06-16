详解 MyBatis-Plus 的这些核心功能。这部分内容非常关键，掌握了它们，你就能充分利用 MP 的强大能力。

---

### 3. 核心功能详解

#### 3.1 通用 CRUD (BaseMapper & IService)

`BaseMapper<T>` 是 MP 提供的一个基础 Mapper 接口，你的自定义 Mapper 接口继承它之后，就自动拥有了丰富的 CRUD 方法，无需编写任何 SQL。
`IService<T>` 是 MP 提供的 Service 层基础接口，通常由 `ServiceImpl<M extends BaseMapper<T>, T>` 实现，它在 `BaseMapper` 的基础上封装了更多常用的业务方法，并提供了一些便利的批量操作和链式调用。

**以下是 `BaseMapper<T>` 中一些核心的通用方法 ( `IService<T>` 也封装了这些方法，并可能提供更便捷的调用方式)：**

**插入操作：**

*   **`int insert(T entity)`**:
    *   插入一条记录。
    *   **行为**：会插入实体中**所有非 null** 的字段。如果字段为 null，则数据库对应列会使用其默认值 (如果定义了) 或 null。
    *   **主键**：如果主键策略是 `IdType.AUTO` (数据库自增) 或 `IdType.ASSIGN_ID` / `ASSIGN_UUID` (MP生成)，MP 会在插入后将生成的主键值回填到 `entity` 对象的对应主键属性上。
    *   **返回值**：受影响的行数 (通常是 1)。

**删除操作：**

*   **`int deleteById(Serializable id)`**:
    *   根据主键 ID 删除一条记录。
    *   **参数**：主键值。
    *   **返回值**：受影响的行数。
*   **`int deleteById(T entity)`**: (MP 3.1.0+ 新增)
    *   根据实体 `entity` 的主键进行删除，主键值从 `entity` 中获取。
*   **`int deleteByMap(Map<String, Object> columnMap)`**:
    *   根据 `columnMap` 构建的 `WHERE` 条件删除记录 (条件之间是 `AND` 连接)。
    *   **参数**：`key` 是数据库列名 (不是实体属性名)，`value` 是列对应的值。
    *   **返回值**：受影响的行数。
    *   **示例**：`map.put("user_name", "John"); map.put("age", 30);` 会删除 `user_name = 'John' AND age = 30` 的记录。
*   **`int deleteBatchIds(Collection<? extends Serializable> idList)`**:
    *   根据主键 ID 列表批量删除记录。
    *   **参数**：主键 ID 的集合。
    *   **返回值**：受影响的行数。
*   **`int delete(Wrapper<T> wrapper)`**:
    *   根据 `Wrapper` (条件构造器) 构建的 `WHERE` 条件删除记录。
    *   这是进行复杂条件删除的主要方式。
    *   **返回值**：受影响的行数。
    *   **注意安全**：如果没有条件 (wrapper 为空或 `Wrappers.emptyWrapper()`)，可能会删除全表数据！(MP 有防止全表更新/删除插件)

**更新操作：**

*   **`int updateById(T entity)`**:
    *   根据主键 ID 更新一条记录。
    *   **行为**：只会更新 `entity` 中**非 null** 的字段。如果想将某个字段更新为 `null`，此方法做不到。
    *   **WHERE 条件**：`WHERE id = #{entity.id}`。
    *   **返回值**：受影响的行数。
*   **`int update(T entity, Wrapper<T> updateWrapper)`**:
    *   根据 `updateWrapper` 构建的 `WHERE` 条件更新记录。
    *   `entity` 参数用于提供要更新的字段值 (SET 部分)，**仍然是只更新 `entity` 中非 null 的字段**。
    *   如果你想将字段更新为 `null`，或者只更新特定字段而不受 `entity` 中 null 值的影响，应该使用 `UpdateWrapper` 的 `set` 或 `setSql` 方法。
    *   **返回值**：受影响的行数。
    *   **示例 (将字段更新为 null)**:
        ```java
        // 错误方式：userToUpdate.setEmail(null); userMapper.update(userToUpdate, wrapper); // email 不会被更新为 null
        // 正确方式：
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("id", userId).set("email", null); // 直接在 UpdateWrapper 中设置要更新的字段和值
        userMapper.update(null, uw); // 第一个参数 entity 可以传 null，因为更新的字段和值已在 uw 中定义
        ```

**查询操作 (返回实体对象列表或单个实体)：**

*   **`T selectById(Serializable id)`**:
    *   根据主键 ID 查询一条记录。
    *   **返回值**：实体对象，如果不存在则返回 `null`。
*   **`List<T> selectBatchIds(Collection<? extends Serializable> idList)`**:
    *   根据主键 ID 列表查询多条记录。
    *   **返回值**：实体对象列表。
*   **`List<T> selectByMap(Map<String, Object> columnMap)`**:
    *   根据 `columnMap` 构建的 `WHERE` 条件查询记录 (条件之间是 `AND` 连接)。
    *   **参数**：`key` 是数据库列名，`value` 是值。
    *   **返回值**：实体对象列表。
*   **`T selectOne(Wrapper<T> queryWrapper)`**:
    *   根据 `queryWrapper` 构建的 `WHERE` 条件查询一条记录。
    *   **行为**：如果查询结果有多条，会抛出 `TooManyResultsException` 异常。如果查询结果没有，返回 `null`。
    *   常用于查询唯一性记录或期望只返回一条记录的场景。
    *   **返回值**：单个实体对象或 `null`。
*   **`List<T> selectList(Wrapper<T> queryWrapper)`**:
    *   根据 `queryWrapper` 构建的 `WHERE` 条件查询多条记录。
    *   如果 `queryWrapper` 为 `null` 或 `Wrappers.emptyWrapper()`，则查询所有记录 (全表扫描，慎用)。
    *   **返回值**：实体对象列表。

**查询操作 (返回统计结果或特定投影)：**

*   **`Long selectCount(Wrapper<T> queryWrapper)`**: (MP 3.4.0 版本之前返回 `Integer`)
    *   根据 `queryWrapper` 构建的 `WHERE` 条件查询记录总数。
    *   **返回值**：记录总数。
*   **`List<Map<String, Object>> selectMaps(Wrapper<T> queryWrapper)`**:
    *   根据 `queryWrapper` 构建的 `WHERE` 条件查询记录，每条记录封装成一个 `Map<String, Object>`。
    *   `Map` 的 `key` 是数据库列名 (如果开启了 `map-underscore-to-camel-case` 且 Wrapper 中没有显式 `select` 列名，则可能是驼峰属性名，具体行为需验证)。通常是列名。
    *   可以通过 `queryWrapper.select("col1", "col2")` 指定查询的列。
    *   **返回值**：`Map` 对象列表。
*   **`List<Object> selectObjs(Wrapper<T> queryWrapper)`**:
    *   根据 `queryWrapper` 构建的 `WHERE` 条件查询记录。
    *   **行为**：只返回结果集中的**第一列**的值。
    *   常用于查询单个字段的列表，如 `List<Long> userIds = userMapper.selectObjs(Wrappers.<User>lambdaQuery().select(User::getId));`
    *   **返回值**：对象列表，每个对象是第一列的值。

**分页查询操作：**

*   **`IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper)`**:
    *   根据 `queryWrapper` 构建的 `WHERE` 条件进行分页查询。
    *   需要配置分页插件 (`PaginationInnerInterceptor`)。
    *   **参数**：
        *   `page`: 实现了 `IPage` 接口的分页参数对象 (通常是 `new Page<>(pageNum, pageSize)`)。
        *   `queryWrapper`: 查询条件。
    *   **返回值**：`IPage<T>` 对象，其中包含了分页信息 (总记录数、总页数、当前页数据列表等)。
*   **`IPage<Map<String, Object>> selectMapsPage(IPage<T> page, Wrapper<T> queryWrapper)`**:
    *   类似 `selectPage`，但返回的是 `Map` 对象的列表。

**`IService<T>` 接口提供的额外便利方法 (部分示例)：**

*   `boolean save(T entity)`: 对应 `BaseMapper.insert`。
*   `boolean saveBatch(Collection<T> entityList)`: 批量插入。
*   `boolean saveOrUpdate(T entity)`: 如果实体主键存在则更新，否则插入。
*   `boolean saveOrUpdateBatch(Collection<T> entityList)`: 批量保存或更新。
*   `boolean removeById(Serializable id)`: 对应 `BaseMapper.deleteById`。
*   `boolean removeByIds(Collection<? extends Serializable> idList)`: 对应 `BaseMapper.deleteBatchIds`。
*   `boolean remove(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.delete`。
*   `boolean updateById(T entity)`: P对应 `BaseMapper.updateById`。
*   `boolean update(Wrapper<T> updateWrapper)`: 只使用 Wrapper 进行更新 (如 `set` 子句在 Wrapper 中定义)。
*   `boolean update(T entity, Wrapper<T> updateWrapper)`: 对应 `BaseMapper.update`。
*   `boolean updateBatchById(Collection<T> entityList)`: 批量根据 ID 更新。
*   `T getById(Serializable id)`: 对应 `BaseMapper.selectById`。
*   `List<T> listByIds(Collection<? extends Serializable> idList)`: 对应 `BaseMapper.selectBatchIds`。
*   `T getOne(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectOne` (如果结果不唯一，默认不抛异常，返回第一条，可通过第二个布尔参数控制是否抛异常)。
*   `long count()`: 查询总记录数 (无条件)。
*   `long count(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectCount`。
*   `List<T> list()`: 查询所有记录 (无条件)。
*   `List<T> list(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectList`。
*   `List<Map<String, Object>> listMaps()`: 查询所有记录为 Map 列表。
*   `List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectMaps`。
*   `List<Object> listObjs()`: 查询所有记录的第一列。
*   `List<Object> listObjs(Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectObjs`。
*   `IPage<T> page(IPage<T> page)`: 分页查询所有。
*   `IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectPage`。
*   `IPage<Map<String, Object>> pageMaps(IPage<Map<String, Object>> page)`: 分页查询所有为 Map 列表。
*   `IPage<Map<String, Object>> pageMaps(IPage<Map<String, Object>> page, Wrapper<T> queryWrapper)`: 对应 `BaseMapper.selectMapsPage`。
*   `QueryChainWrapper<T> query()`: 返回一个 `QueryChainWrapper`，可以进行链式查询，最后通过 `.one()`, `.list()`, `.count()` 等方法获取结果。
*   `LambdaQueryChainWrapper<T> lambdaQuery()`: 返回 Lambda 版本的链式查询器。
*   `UpdateChainWrapper<T> update()`: 返回一个 `UpdateChainWrapper`，可以进行链式更新。
*   `LambdaUpdateChainWrapper<T> lambdaUpdate()`: 返回 Lambda 版本的链式更新器。

**链式调用示例 (IService):**
```java
// 使用 IService 的 LambdaQueryChainWrapper
List<User> users = userService.lambdaQuery()
                              .like(User::getName, "Admin")
                              .gt(User::getAge, 20)
                              .orderByAsc(User::getCreateTime)
                              .list();

boolean result = userService.lambdaUpdate()
                            .eq(User::getStatus, "INACTIVE")
                            .set(User::getStatus, "ARCHIVED")
                            .update();
```

---

#### 3.2 强大的条件构造器 (Wrapper)

Wrapper 是 MP 的精髓之一，它允许你以编程方式、面向对象地构建 SQL 的 `WHERE` 条件、`ORDER BY`、`SELECT` 列等，而无需手写 XML 中的动态 SQL。

**主要类型：**

*   **`AbstractWrapper` (抽象父类)**
    *   `QueryWrapper<T>`: 用于构建查询条件的 Wrapper。
    *   `UpdateWrapper<T>`: 用于构建更新操作的 Wrapper (可以定义 `SET` 和 `WHERE` 部分)。
*   **`AbstractLambdaWrapper` (支持 Lambda 的抽象父类)**
    *   `LambdaQueryWrapper<T>`: `QueryWrapper` 的 Lambda 版本，可以使用方法引用 (如 `User::getName`) 来指定列，具有类型安全和重构友好性。**强烈推荐使用**。
    *   `LambdaUpdateWrapper<T>`: `UpdateWrapper` 的 Lambda 版本。**强烈推荐使用**。

**创建 Wrapper 实例：**

```java
// QueryWrapper
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
// 或者使用工具类 Wrappers (更简洁)
QueryWrapper<User> qw = Wrappers.query();
QueryWrapper<User> qwEntity = Wrappers.query(new User().setName("Tom")); // 根据实体非空属性构建条件

// LambdaQueryWrapper
LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
// 或者使用工具类 Wrappers
LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
LambdaQueryWrapper<User> lqwEntity = Wrappers.lambdaQuery(new User().setName("Tom"));

// UpdateWrapper
UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
UpdateWrapper<User> uw = Wrappers.update();

// LambdaUpdateWrapper
LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
LambdaUpdateWrapper<User> luw = Wrappers.lambdaUpdate();
```

**常用方法 (以 `LambdaQueryWrapper` 为例，`QueryWrapper` 方法名类似，只是参数为字符串列名)：**

*   **等值比较：**
    *   `eq(R column, Object val)`: 等于 (`=`)。`User::getName`, "John" -> `name = 'John'`
    *   `ne(R column, Object val)`: 不等于 (`!=` 或 `<>`)。
*   **范围比较：**
    *   `gt(R column, Object val)`: 大于 (`>`)。
    *   `ge(R column, Object val)`: 大于等于 (`>=`)。
    *   `lt(R column, Object val)`: 小于 (`<`)。
    *   `le(R column, Object val)`: 小于等于 (`<=`)。
    *   `between(R column, Object val1, Object val2)`: `BETWEEN val1 AND val2`。
    *   `notBetween(R column, Object val1, Object val2)`: `NOT BETWEEN val1 AND val2`。
*   **模糊匹配：**
    *   `like(R column, Object val)`: `LIKE '%val%'`。
    *   `notLike(R column, Object val)`: `NOT LIKE '%val%'`。
    *   `likeLeft(R column, Object val)`: `LIKE '%val'` (左模糊)。
    *   `likeRight(R column, Object val)`: `LIKE 'val%'` (右模糊)。
*   **IN / NULL 判断：**
    *   `in(R column, Collection<?> coll)`: `IN (...)`。
    *   `in(R column, Object... values)`: `IN (...)`。
    *   `notIn(R column, Collection<?> coll)`: `NOT IN (...)`。
    *   `notIn(R column, Object... values)`: `NOT IN (...)`。
    *   `isNull(R column)`: `IS NULL`。
    *   `isNotNull(R column)`: `IS NOT NULL`。
*   **排序：**
    *   `orderByAsc(R... columns)`: 升序排序。
    *   `orderByDesc(R... columns)`: 降序排序。
    *   `orderBy(boolean condition, boolean isAsc, R... columns)`: 条件排序。
*   **分组与聚合：**
    *   `groupBy(R... columns)`: `GROUP BY ...`。
    *   `having(String sqlHaving, Object... params)`: `HAVING ...` (可以包含占位符 `?` 或 `${i}`，对应 params)。
*   **逻辑连接：**
    *   `and(Consumer<Param> consumer)`: `AND ( ... )`，用于嵌套条件。
        ```java
        lqw.eq(User::getStatus, 1)
           .and(wq -> wq.like(User::getName, "A").or().like(User::getEmail, "example.com"));
        // SQL: status = 1 AND (name LIKE '%A%' OR email LIKE '%example.com%')
        ```
    *   `or()`: `OR`，连接下一个条件。
    *   `or(Consumer<Param> consumer)`: `OR ( ... )`，用于嵌套条件。
    *   `nested(Consumer<Param> consumer)`: `( ... )`，用于更自由的嵌套。
*   **SQL 注入与自定义 SQL 片段：**
    *   `apply(String applySql, Object... params)`: 拼接原生 SQL 到 `WHERE` 子句中，**务必注意 SQL 注入风险**，通常用于数据库函数或复杂表达式。`applySql` 中可以使用 `{i}` 作为占位符对应 params。
        ```java
        // lqw.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2023-01-01");
        // lqw.apply("find_in_set({0}, tags)", tagValue);
        ```
    *   `last(String lastSql)`: 无视优化规则，直接拼接到 SQL 的最后。**只能调用一次，多次调用以最后一次为准，有 SQL 注入风险**。常用于 `LIMIT 1` 等。
        ```java
        // lqw.last("LIMIT 1"); // 不推荐，分页有专门插件
        ```
*   **EXISTS / NOT EXISTS：**
    *   `exists(String existsSql, Object... params)`: `EXISTS ( subquery )`。
    *   `notExists(String existsSql, Object... params)`: `NOT EXISTS ( subquery )`。
*   **选择查询字段 ( Projection )：**
    *   `select(R... columns)`: 指定查询返回的字段。
        ```java
        // lqw.select(User::getId, User::getName, User::getEmail);
        ```
    *   `select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)`: 根据实体字段的元信息动态选择列。
        ```java
        // lqw.select(User.class, field -> !field.getProperty().equals("password")); // 查询除 password 外所有字段
        ```
*   **条件判断 (`condition` 参数)**:
    *   很多 Wrapper 方法都有一个重载版本，第一个参数是 `boolean condition`。只有当 `condition` 为 `true` 时，该条件才会应用到 SQL 中。这使得动态构建条件非常方便，替代了大量 `<if>` 标签。
        ```java
        String nameParam = "Test"; // 可能为 null 或 empty
        Integer ageParam = null;  // 可能为 null

        lqw.like(StringUtils.isNotBlank(nameParam), User::getName, nameParam) // 只有 nameParam 不为空白时才添加 like 条件
           .gt(ageParam != null && ageParam > 0, User::getAge, ageParam); // 只有 ageParam 有效时才添加 gt 条件
        ```

**`UpdateWrapper` / `LambdaUpdateWrapper` 特有方法：**

*   **`set(R column, Object val)`**: 设置要更新的字段和值 (`SET column = val`)。
*   **`setSql(String sql)`**: 设置 `SET` 子句的 SQL 片段 (如 `SET score = score + 1`)。**注意 SQL 注入**。
    ```java
    LambdaUpdateWrapper<User> luw = Wrappers.lambdaUpdate();
    luw.eq(User::getId, 1L)
       .set(User::getAge, 30)
       .set(User::getEmail, null) // 可以将字段更新为 null
       .setSql("login_count = login_count + 1");
    userMapper.update(null, luw); // 或者 userService.update(luw);
    ```

**LambdaQueryWrapper 的优势：**

1.  **类型安全**: 使用方法引用 (如 `User::getName`) 而不是字符串列名 ("name")，编译器可以检查属性是否存在，减少运行时错误。
2.  **重构友好**: 如果实体类的属性名发生变化，IDE 的重构功能可以自动更新 Lambda 表达式中的方法引用。而字符串列名则需要手动查找和修改，容易遗漏。
3.  **代码更简洁易读**: 对于熟悉 Lambda 的开发者，可读性更强。