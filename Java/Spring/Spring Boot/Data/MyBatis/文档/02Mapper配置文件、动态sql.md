## Mapper配置文件

MyBatis 的真正强大在于它的映射语句，也是它的魔力所在。由于它的异常强大，映射器的 XML 文件就显得相对简单。如果拿它跟具有相同功能的 JDBC 代码进行对比，你会立即发现省掉了将近 95% 的代码。MyBatis 就是针对 SQL 构建的，并且比普通的方法做的更好。

SQL 映射文件有很少的几个顶级元素（按照它们应该被定义的顺序）： ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f02ffd0ecb0bb2251e13d7e55ae26363.png)

**mapper**：mapper配置文件根目录，拥有namespace属性

在MyBatis中，Mapper中的namespace用于绑定Dao接口的，即面向接口编程。 它的好处在于当使用了namespace之后就可以不用写接口实现类，业务逻辑会直接通过这个绑定寻找到相对应的SQL语句进行对应的数据处理

值拥有两种书写方法：

1) 短名称（比如“selectAllThings”）如果全局唯一也可以作为一个单独的引用

```XML
<mapper namespace="user"><mapper/>
```

2) 接口完全限定名

```XML
<!--配置在核心配置文件中-->
<typeAliases>
    <typeAlias alias="user" type="com.dao.UserDao"/>
</typeAliases>
<!--mapper配置文件引用别名 user代表接口名-->
<mapper namespace="user"><mapper/>
<!--也可以不使用别名直接填写接口名-->
<mapper namespace="com.dao.UserDao"><mapper/>
```

**select**：书写在mapper标签中的标签，拥有如下属性

| 属性            | 描述                                                                                                                           |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| id            | 在命名空间中唯一的标识符，可以被用来引用这条语句。                                                                                                    |
| parameterType | 将会传入这条语句的参数类的完全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset。                                          |
| resultType    | 从这条语句中返回的期望类型的类的完全限定名或别名。注意如果是集合情形，那应该是集合可以包含的类型，而不能是集合本身。使用 resultType 或 resultMap，但不能同时使用。                                 |
| resultMap     | 外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，对其有一个很好的理解的话，许多复杂映射的情形都能迎刃而解。使用 resultMap 或 resultType，但不能同时使用。                    |
| flushCache    | 将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：false。                                                                         |
| useCache      | 将其设置为 true，将会导致本条语句的结果被二级缓存，默认值：对 select 元素为 true。                                                                           |
| timeout       | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为 unset（依赖驱动）。                                                                            |
| fetchSize     | 这是尝试影响驱动程序每次批量返回的结果行数和这个设置值相等。默认值为 unset（依赖驱动）。                                                                              |
| statementType | STATEMENT，PREPARED 或 CALLABLE 的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。             |
| resultSetType | FORWARD_ONLY，SCROLL_SENSITIVE 或 SCROLL_INSENSITIVE 中的一个，默认值为 unset （依赖驱动）。                                                   |
| databaseId    | 如果配置了 databaseIdProvider，MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。                             |
| resultOrdered | 这个设置仅针对嵌套结果 select 语句适用：如果为 true，就是假设包含了嵌套结果集或是分组了，这样的话当返回一个主结果行的时候，就不会发生有对前面结果集的引用的情况。这就使得在获取嵌套的结果集的时候不至于导致内存不够用。默认值：false。 |
| resultSets    | 这个设置仅对多结果集的情况适用，它将列出语句执行后返回的结果集并每个结果集给一个名称，名称是逗号分隔的。                                                                         |

**Insert, Update, Delete**：书写在mapper标签中的标签，拥有如下属性

| 属性               | 描述                                                                                                                                          |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| id               | 命名空间中的唯一标识符，可被用来代表这条语句。                                                                                                                     |
| parameterType    | 将要传入语句的参数的完全限定类名或别名。这个属性是可选的，因为 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset。                                                           |
| flushCache       | 将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true（对应插入、更新和删除语句）。                                                                           |
| timeout          | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为 unset（依赖驱动）。                                                                                           |
| statementType    | STATEMENT，PREPARED 或 CALLABLE 的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。                            |
| useGeneratedKeys | （仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段），默认值：false。      |
| keyProperty      | （仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 子元素设置它的键值，默认：unset。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| keyColumn        | （仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。                       |
| databaseId       | 如果配置了 databaseIdProvider，MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。                                            |

**cache:** 二级缓存标签,书写在mapper标签中 无属性,需要在mybatis.xml中配置开启二级缓存(默认开启)

```XML
<settings>
    <setting name="cacheEnabled" value="true"/>
</settings>
```

mybatis的二级缓存默认在缓存在内存中，不支持分布式服务器的缓存，所以，一般使用第三方的缓存插件，比如ehcache,redis

 ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9ba73ed890e77cffbd8f7242ac1415a0.png)

**useCache**：属性，对于开启二级缓存的查询，每次会先去缓存进行查找之后才会执行，如果不想让当前执行查询使用缓存需要将值设置为false

```XML
<select id="findOrderListResultMap" resultMap="ordersUserMap" useCache="false">
    sql…
</select>
```

**flushCache**： 属性在mapper的同一个namespace中，如果有其它insert、update、delete操作数据后需要刷新缓存，如果不执行刷新缓存会出现脏读。 设置statement配置中的flushCache="true" 属性，默认情况下为true即刷新缓存，如果改成false则不会刷新。使用缓存时如果手动修改数据库表中的查询数据会出现脏读。

```XML
<insert id="insertUser" parameterType="cn.itcast.mybatis.po.User" flushCache="true">
    sql…
</insert>
```

**resultMap** ：用来描述获取数据后与对象的映射（数据库字段与对象属性名），通常情况下使用默认的映射（数据库字段与属性名相同），但有时需要进行不同的映射，这时候就需要使用requestMap进行设置在resultType中使用。具体如下：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6b201dbc36dcd9506460894ee86194a3.png)

**id**：标签，用来标识主键

column：数据库字段属性  property：实体类字段属性 

**result**：标签 ，用于其他列标识

**colleaction**：标签，用来标识集合属性

property：实体类字段属性 

ofType：集合存储数据类型属性

column属性：用于标识查询数据列（会获取对应列数据传入相应的查询语句）

select属性：懒加载执行时调用获取数据的方法(可以调用其他mapper中的方法，通过namespace.id使用)

fetchType：局部懒加载默认fetchType="lazy"深入式  eager侵入式

**association**：标签，用于标识类属性

property：实体类字段属性 

javaType：类类型属性

column属性：用于标识查询数据列（会获取对应列数据传入相应的查询语句）

select属性：懒加载执行时调用获取数据的方法(可以调用其他mapper中的方法，通过namespace.id使用)

fetchType：局部懒加载默认fetchType="lazy"深入式  eager侵入式

**sql**：标签， 可以被用来定义可重用的 SQL 代码段，可以包含在其他语句中。它可以被静态地(在加载参数) 参数化. 不同的属性值通过包含的实例变化. 比如：

```XML
<sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
```

**include**:标签， 可以调用定义的sql标签，可以设置property标签  设置name属性与value属性为sql标签中设置的参数赋值

```XML
<select id="selectUsers" resultType="map">
  select
    <include refid="userColumns"><property name="alias" value="t1"/></include>,
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  from some_table t1
    cross join some_table t2
</select>
```

## 动态sql标签

动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。

使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。

### if 标签

使用动态 SQL 最常见情景是根据条件包含 where 子句的一部分。比如：

```XML
<select id="findUserWhereId" resultType="User">
  SELECT * FROM User
  WHERE state = 0
  <if test="id != null">
    AND id = #{id}
  </if>
</select>
```

### choose、when、otherwise 标签

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句

```XML
<select id="findUser" resultType="User">
  SELECT * FROM User WHERE state = 0
  <choose>
    <when test="id != null">
      AND title like #{id}
    </when>
    <when test="name!= null">
      AND name like '%#{name}$'
    </when>
    <otherwise>
      AND sal= 1
    </otherwise>
  </choose>
</select>
```

**trim、where、set标签**

*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

```XML
<select id="findUser" resultType="User">
  SELECT * FROM User
  <where>
    <if test="id != null">
         state = #{state}
    </if>
    <if test="name!= null">
        AND title like #{title}
    </if>
  </where>
</select>
```

如果 *where* 元素与你期望的不太一样，你也可以通过自定义 trim 元素来定制 *where* 元素的功能。比如，和 *where* 元素等价的自定义 trim 元素

*prefixOverrides* 属性会忽略通过管道符分隔的文本序列（注意此例中的空格是必要的）.上述例子会移除所有prefixOverrides属性中指定的内容，并且插入 *prefix* 属性中指定的内容。

```XML
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

用于动态更新语句的类似解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，忽略其它不更新的列。比如：

```XML
<update id="updateUserIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

### foreach遍历标签

动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）。比如：

```XML
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list" open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

item：遍历取出的数据

index：遍历数据的索引

colleaction：遍历的数据集合

open：开始拼接时添加的字符串

separator：每次遍历中间的字符串

close：结束后最后添加的字符串
