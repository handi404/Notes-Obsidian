
### IDEA中[web项目](https://so.csdn.net/so/search?q=web%E9%A1%B9%E7%9B%AE&spm=1001.2101.3001.7020)c3p0-config.xml文件的配置及存放目录

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/92a1c733bceb277882c98e8714c229d2.png)

> 今天在IDEA上折腾了很长一段时间,始终连接不上数据库,日志总是说找不到mysql.这是我的测试代码

```java
@Test
public void fun2() throws SQLException {
    ComboPooledDataSource ds=new ComboPooledDataSource("mysql");
    Connection con= ds.getConnection();
    System.out.println(con);
    con.close();
}
```

**最终确定下来是文件存放问题**

### c3p0-config.xml配置介绍(文件名称固定)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<c3p0-config>
    <!--默认配置-->
    <default-config>
        <property name="driverClass">com.mysql.cj.jdbc.Driver</property>
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/mydb1?characterEncoding=utf-8&amp;serverTimezone=UTC</property>
        <property name="user">root</property>
        <property name="password">123</property>
        <!-- initialPoolSize：初始化时获取三个连接，
        取值应在minPoolSize与maxPoolSize之间。 -->
        <property name="initialPoolSize">3</property>
        <!-- maxIdleTime：最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃。-->
        <property name="maxIdleTime">60</property>
        <!-- maxPoolSize：连接池中保留的最大连接数 -->
        <property name="maxPoolSize">100</property>
        <!-- minPoolSize: 连接池中保留的最小连接数 -->
        <property name="minPoolSize">10</property>
    </default-config>
    <!--配置连接池mysql-->
    <named-config name="mysql">
        <property name="driverClass">com.mysql.cj.jdbc.Driver</property>
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/mydb1?characterEncoding=utf-8&amp;serverTimezone=UTC</property>
        <property name="user">root</property>
        <property name="password">123</property>
        <property name="initialPoolSize">10</property>
        <property name="maxIdleTime">30</property>
        <property name="maxPoolSize">100</property>
        <property name="minPoolSize">10</property>
    </named-config>
    <!--配置连接池2,可以配置多个-->
</c3p0-config>
```

### 解决

> **原来必须放在resources目录下**
> 
> > 标注部分是所需的jar包

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ad819670e5871e8b61403831e3d63dfc.png)

> **网上找了半天没找到,不过通过查看日志,最终自己还是解决了问题**