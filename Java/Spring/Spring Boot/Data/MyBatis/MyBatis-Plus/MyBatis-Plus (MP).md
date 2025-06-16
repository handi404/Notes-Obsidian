MyBatis-Plus (MP) 是 MyBatis 生态中非常重要且流行的增强工具，值得我们重点全面深入地讲解。它极大地简化了基于 MyBatis 的开发，特别是对于 CRUD 操作和复杂条件的构建。

我将按照以下结构来为你详细阐述 MyBatis-Plus：

1.  **MyBatis-Plus 核心理念与优势回顾** (为什么选择 MP)
2.  **快速上手与 Spring Boot 集成**
    *   依赖引入 (`mybatis-plus-boot-starter`)
    *   核心配置 (`application.yml`)
    *   与 MyBatis 原生配置的协同
3.  **核心功能详解**
    *   **通用 CRUD (BaseMapper & IService)**
        *   `insert`, `deleteById`, `deleteByMap`, `deleteBatchIds`, `delete(Wrapper)`
        *   `updateById`, `update(entity, wrapper)`
        *   `selectById`, `selectBatchIds`, `selectByMap`
        *   `selectOne(wrapper)`
        *   `selectCount(wrapper)`
        *   `selectList(wrapper)`
        *   `selectMaps(wrapper)`
        *   `selectObjs(wrapper)`
        *   `selectPage(page, wrapper)`, `selectMapsPage(page, wrapper)`
    *   **强大的条件构造器 (Wrapper)**
        *   `QueryWrapper` / `LambdaQueryWrapper`: 构建查询条件
        *   `UpdateWrapper` / `LambdaUpdateWrapper`: 构建更新条件
        *   常用方法：`eq`, `ne`, `gt`, `ge`, `lt`, `le`, `like`, `notLike`, `likeLeft`, `likeRight`, `in`, `notIn`, `isNull`, `isNotNull`, `orderBy`, `groupBy`, `having`, `or`, `and`, `apply`, `last`, `exists`, `notExists`, `between`, `notBetween`, `select` (指定查询字段), `set` (用于 `UpdateWrapper`)
        *   LambdaQueryWrapper 的优势 (类型安全，重构友好)
    *   **实体类注解详解**
        *   `@TableName`: 表名映射
        *   `@TableId`: 主键策略 (`IdType.AUTO`, `ASSIGN_ID` (雪花), `ASSIGN_UUID`, `INPUT`, `NONE`)
        *   `@TableField`: 字段映射 (`value`, `exist`, `select`, `fill`, `numericScale`)
        *   `@Version`: 乐观锁版本号
        *   `@TableLogic`: 逻辑删除
        *   `@EnumValue`: 枚举处理
    *   **主键策略**
        *   全局配置与局部注解配置
        *   雪花算法 (Snowflake) 详解与配置
    *   **自动填充字段 (MetaObjectHandler)**
        *   实现 `MetaObjectHandler` 接口
        *   配置 `insertFill` 和 `updateFill`
    *   **分页插件 (PaginationInnerInterceptor)**
        *   配置与使用
        *   `IPage` 和 `Page` 对象
    *   **乐观锁插件 (OptimisticLockerInnerInterceptor)**
        *   配置与使用 (`@Version` 注解)
    *   **逻辑删除**
        *   全局配置与 `@TableLogic` 注解
        *   如何自定义逻辑删除值
    *   **性能分析插件 (IllegalSqlInnerInterceptor / BlockAttackInnerInterceptor)**
        *   用于开发和测试阶段，防止全表更新/删除
    *   **多租户插件 (TenantLineInnerInterceptor)**
        *   基本原理与配置
4.  **MyBatis-Plus 代码生成器 (MyBatis-Plus Generator)**
    *   依赖与配置
    *   生成 Entity, Mapper, Service, Controller
    *   自定义模板与策略
5.  **进阶使用与技巧**
    *   自定义 SQL 与 MP 通用方法结合
    *   `IService` 的常用方法与自定义扩展
    *   ActiveRecord 模式 (可选，非主流)
    *   MP 的动态表名处理
    *   枚举处理的最佳实践
6.  **与 MyBatis 原生功能的兼容性与选择**
7.  **常见问题与注意事项**