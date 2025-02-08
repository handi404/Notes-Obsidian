从获取正确的项目依赖项开始，使用 JPA 注解标注我们的类，并设置 SessionFactory。这使我们能够执行基本的 CRUD 操作、HQL 和 Criteria 查询。之后，我们可以简单地 JPA 化我们的代码，替换几个类，你就会理解 Hibernate / JPA 与 Spring Boot 是如何协同工作的。到那时，你将对其使用有一个良好的（初步）了解，以及你的未来 Hibernate / JPA 学习旅程包括什么。

00:00 简介 - 需要了解的 Hibernate 相关信息
它解决了什么问题
项目依赖项
02:41 Mapping Annotations(映射注解)
05:53 SessionFactory(会话工厂)
Save / Update / Delete - 基本 CRUD
10:33 HQL 查询
13:33 JPA
17:37 Criteria API
22:22 Hibernate /JPA & Spring Boot
24:11 下一步要学什么

### 需要了解的 Hibernate 相关信息
hibernate jpa jql sql 什么鬼，学习 hibernate 和 jpa，不是真的，因为那将是一个很大的承诺事实上你需要花费数月甚至数年的时间刻意练习 hibernate 和sql 才能完全理解。
展示几个非常重要的一般概念，在未来的学习过程中提供一些建议。

### Hibernate 解决了什么问题
想象你有一个好的旧关系数据库不管它是 postgres mysql 还是你现在有什么，想象我有一个用户的数据库表该表带有一个 id 列 name 列 birthdate 列，两行里面有两个用户一个用户叫 marco 另一个用户叫 ocram，现在我的 java 项目中有一个 User 类有与表对应的属性。问题是，**如何轻松地从数据库表中获取数据并将其放入 User 对象中，并且，如果在 java 端有一个 User 对象，如何轻松地将其保存到名为 users 的数据库表中，而无需进行疯狂的字符串连接，这本质上是 hibernate 在 20000 个其他问题中解决的核心问题**。

### 项目依赖项
hibernate 启动和运行的依赖项为什么这如此重要，因为现在人们使用例如 spring boot 包括 spring data jpa，其中包括很多个库，其中包括 hibernate，他们认为 hibernate 是一个巨大而复杂的库堆栈，需要包含它。并不是。如果使用的是 maven，则需要 pom.xml 文件；如果您使用的是 gradle，则需要 build.gradle 文件。然后粘贴一个微小的依赖项：
```xml
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>6.1.2.Final</version>
</dependency>
```


### Mapping Annotations(映射注解)
已经设置了依赖项，现在的问题是，如何告诉 hibernate 将 User 类映射到名为 users 的数据库表，以及如何告诉 hibernate id name 和 birthdate 应映射到数据库中的相应列 ID​​ NAME BIRTH_DATE。实际上只需使用 hibernate 特定的注解对类进行注释，第一个是添加实体注释 `@Entity`：
```java
@Entity
public class User {
//...
}
```
entiry 只是一个标记注解，它告诉 hibernate 这个类很特别，需将它映射到数据库表，然后默认情况下，hibernate 会采取一个 User 类并尝试映射将其添加到名为 user 的数据库表中，但我们的表名为 users，因此我们必须使用另一个注解 `@Tahle(name = "")`，需在此处指定名称：
```java
@Entity
@Table(name = "USERS")
public class User {
//...
}
```
然后继续 intellij 已经显示，User 应该有一个 primary key(主键)，对 hibernate 来说，知道哪个字段是主键很重要，还有两个注解 `@Id @GeneratedValue`，我们有一个数据库列 id，它也会递增，本质上是一个标识列，我们还必须告诉 hibernate，属性 id 是一个标识列，请不要自己生成 id：
```Java
@Entity
@Table(name = "USERS")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthDate;
//...
}
```
然后 hibernate 足够聪明，可以很好地理解 id name 应该映射到 ID NAME。只有出生日期，在数据库中称为 BIRTH_DATE，所以我们可以使用注解 `@Column(name = "")` 让其正确的映射：
```Java
@Entity
@Table(name = "USERS")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    public User() {
    }
//...
}
```
然后通常你只需要确保类有一个空的构造函数，特别是如果没有任何辅助构造函数，实际上并不需要它，但是一旦有一个辅助构造函数，你至少需要有一个空的构造函数，实际上这样 hibernate 可以在**从数据库表中获取数据并填充这些数据时构造空对象**，如果只是省略了空构造函数，它是行不通的，因为 hibernate 不知道默认情况下如何构造一个用户对象。

有一本很棒的书，推荐阅读：==《JAVA PERSISTENCE WITH HIBERNATE》==

### SessionFactory(会话工厂)
有了依赖我们有了映射注释，现在编写一些代码，准备一个小型测试类。
查阅 Hibernate 的入门手册，复制并粘贴了一些代码并把它放在这里，你最终无法自己想出这些代码：
```Java
public class HibernateFullTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    protected void setUp() throws Exception {
	   // A SessionFactory is set up once for an application!
	   final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			 .configure() // configures settings from hibernate.cfg.xml
			 .build();
	   try {
		  sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
	   }
	   catch (Exception e) {
		  // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		  // so destroy it manually.
		  StandardServiceRegistryBuilder.destroy( registry );
	   }
    }

    @AfterEach
    protected void tearDown() throws Exception {
	   if ( sessionFactory != null ) {
		  sessionFactory.close();
	   }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBasicUsage() {
	   // create a couple of events...
	   Session session = sessionFactory.openSession();
	   session.beginTransaction();
	   session.remove(new User("Marco's Friend", LocalDate.now()));
	   session.getTransaction().commit();
	   session.close();

	   session = sessionFactory.openSession();
	   session.beginTransaction();
	   List<User> result = session.createQuery( "select u from User u" , User.class).list();
	   for ( User user : result) {
		  System.out.println( "User (" + user.getName() + ") : " + user.getBirthDate() );
	   }
	   session.getTransaction().commit();
	   session.close();
    }

    @Test
    public void marco_is_in_the_house() {
	   assertThat(1).isGreaterThanOrEqualTo(0);
    }
}
```
你想要的是一个 SessionFactory，SessionFactory 本质上是 Hibernate 所以它的全部内容都是关于构建一个 SessionFactory。现在为此这里有一些时髦的代码带有一个标准服务注册表配置构建新元数据源注册表构建元数据构建会话，无论什么你需要知道的是，这段代码**本质上是查看一个配置文件来配置 hibernate，它被称为 `hibernate.config.xml`**，查看它：
```XML
<?xml version='1.0' encoding='utf-8'?>
<!--
  ~ Hibernate, Relational Persistence for Idiomatic Java
  ~
  ~ License: GNU Lesser General Public License (LGPL), version 2.1 or later.
  ~ See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
  -->
<!DOCTYPE hibernate-configuration PUBLIC
	   "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	   "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

    <session-factory>

	   <!-- Database connection settings -->
	   <property name="connection.driver_class">org.h2.Driver</property>
	   <property name="connection.url">jdbc:h2:~/database;AUTO_SERVER=TRUE</property>
	   <property name="connection.username">sa</property>
	   <property name="connection.password"></property>

	   <!-- SQL dialect -->
	   <property name="dialect">org.hibernate.dialect.H2Dialect</property>

	   <!-- Echo all executed SQL to stdout -->
	   <property name="show_sql">true</property>

	   <!-- Names the annotated entity class -->
	   <mapping class="org.example.User"/>

    </session-factory>

</hibernate-configuration>
```
可以看到似乎有一个 session-factory 标记，可以设置几个属性，例如，我可以告诉会话工厂==连接到什么数据库==，我正在连接到一个 h2 数据库，所以只需将你的数据库 url 放在这里，还有==正确的驱动程序类和用户名密码==。需要告诉 hibernate当它创建 sql 语句时它应该使用什么 ==sql 方言==，在这种情况下是 h2。可以告诉 hibernate 一些事情，比==打印出正在执行的所有 sql 语句==，但**最重要的是需要告诉 hibernate 哪些特殊的类需要将我们需要的映射标记交给它**，比如 User 类。

### Save / Update / Delete - 基本 CRUD
从简单的事情开始，尝试将 User 对象保存到数据库表中。
显然它从 SessionFactory 开始，我想要做的是，现在创建一个新的会话 sessino，可以将它视为一个数据库连接，没有其他东西，实际上并不完全正确，但现在完全没问题：
```Java
@Test
void save_my_first_object_to_the_db(){
	User user = new User("Lisa", LocalDate.now());
	
	try (Session session = sessionFactory.openSession()) {
		session.beginTransaction();
		
		session.persist(user);
		
		session.getTransaction().commit();
	}
}
```

### HQL 查询
尝试将用户从数据库中取出，将在 hql 的帮助下做到这一点，hql是 hibernate 的查询语言，它类似于 sql，尽管 sql 适用于数据库表，而 hql 适用于类层次结构。所以编写 `session.createQuery()`，因为想要编写一个 hql 查询，第一个参数是你的查询，第二个参数是结果对象的类型。现在问题是查询要查找什么，非常类似于 sql，所以：
```Java
@Test
void hql_fetch_users(){
	try (Session session = sessionFactory.openSession()) {
		session.beginTransaction();
		List<User> users = session.createQuery("select u from User u", User.class).list();
		users.forEach(System.out::println)
		
		session.getTransaction().commit();
	}
}
```
这意味着实际上来说，获取特定用户应该不是什么大问题，必须找出如何在 hql 查询中指定参数设置这些参数，然后只返回一个用户。

### JPA
到了 jpa 的话题，java persistence api(java 持久性 api)。什么是 jpa，事情已经告诉过了有多个工具，例如 hibernate eclipselink openJPA datanucleus，其他几个和商品在很久以前的某个时候聚在一起，认为很高兴有某种基础接口 jpa，所有这些工具都需要遵循该接口，并且它们可以添加自己的功能。
如果代码将确保不使用任何特定于 hibernate 的类，而只使用特定于 jpa 的类，我可以在最后插入 hibernate，让 hibernate 进行保存。这在实践中意味着什么，在实践中我可以说，明天我将用 eclipse link 替换 hibernate，然后在后天用 data nucleus 替换 hibernate，尽管 reddit 上总有一些评论声称他们基本上每天都这样做，但基本上针对 jpa 注解进行编码仍然是一种很好的做法。每当需要 hibernate 中的特定功能时，只需使用它们即可。如果使用的是 spring data 之类的框架，那么选择已经为你做出。

### Criteria API & dynamic sql
到了 hibernate 的 Criteria(标准) API 和 dynamic(动态) sql。
这很重要，因为想象一下你有一个复杂的表单，它甚至不必是一个复杂的表单，根据下拉菜单中的字段，需要在后端生成不同的 sql 语句，可以通过使用 hql 查询进行字符串连接来实现这一点，有些人会这样做，第二个是构建自己的查询生成器框架，第三个可以使用 hibernate 的 Criteria API，它使用起来有点古怪，但它现在可以完成工作。
```Java
@Test
public void criteria_api() {
	EntityManager em = emf.createEntityManager();
	CriteriaBuilder cb = em.getCriaBuilder();
	
	CriteriaQuery<User> cirteriaQuery = cb.createQuery(User.class);
	Root<User> root = criteriaQuery.from(User.class);
	cirteriaQuery.select(root).where(cb.equal(root.get(User_.NAME), "marco"));
	
	TypedQuery<User> query = em.createQuery(criteriaQuery);
	List<User> results = query.getResultList();
	results.forEach(System.out::println);
	
	em.close();
}
```
我们需要一个实体管理器，我们需要一个标准生成器，因为我们想要现在创建一个条件查询条件生成器请让我创建一个查询，从中获取用户 user.class 是查询的结果类型，然后构造查询条件查询，就好像它是一个 sql 查询，但它看起来有点复杂，所以从我的用户表中进行条件查询，本质上是用户表，我想选择 User_.NAME 等于 marco。显然，如果你在这里有不同的条件，你可以把它们放进去。User_ 来自哪里，让它工作需要向 pom.xml 添加：
```XML
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-jpamodelgen</artifactId>
	<version>6.1.1.Final</version>
</dependency>
```
需要 jsp 运行时才能使它工作，现在每次编译项目时会发生什么，编译器插件都会查看 User类，然后在 target 生成的源上自动生成一个 User_ 辅助类：
```Java
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodeL(User.class)
public abstract class User_ {
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User,·Long> id;
	public static volatile SingularAttribute<User, LocalDate> birthDate;
	
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String BIRTH_DATE = "birthDate";	
}
```

推荐插件：jap buddy

### Hibernate /JPA & Spring Boot
最后在 spring boost 或 spring data 等框架，hibernation jpa它们在幕后的作用是什么。它们还必须使用实体管理器工厂/会话工厂，因此 spring boot 可以为你做什么：
首先是你不必使用 `hibernate.config.xml` 文件或 java 持久性配置文件，你只需在 `application.properties` 中放入几个属性，即可自动创建实体管理器，最后 spring boot 将为您创建一个实体管理器，这是第一，所以这是基础设施管道。

第二，当使用 spring 时，不用编程事务代码，例如开始和提交事务这里，你要做的是使用 `@Transactional` 注解，spring将确保事务打开和关闭，这是整个事务管理主题中的第二大主题。

第三大主题是 `repository`，因为现在我们必须在这里写入这些 hql 查询字符串等等，而 spring data 具体为你提供的就是这些 repository，所以你只需编写接口 UserRepository，并让方法按名称查找，甚至可以给它一个名称参数，然后你将获得一个用户列表，现在 spring data 所做的是它会自动将所有这些转换为适当的 hibernate/jpa 代码，你不必担心编写 sql 之类的东西。

示例：[[示例]] [[示例2]]
