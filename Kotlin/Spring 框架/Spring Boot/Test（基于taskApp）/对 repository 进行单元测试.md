在 test 中创建对应的 repository package。并且在 test 下创建 resources 目录，新建 test-data.sql 文件资源，准备一些 SQL 指令来正确地测试一些东西：

```SQL
INSERT INTO task  
(`id`, `description`, `is_reminder_set`, `is_task_open`, `created_on`, `priority`)  
VALUES (111, 'first test todo', false, false, CURRENT_TIME(), 'LOW');  
  
INSERT INTO task  
(`id`, `description`, `is_reminder_set`, `is_task_open`, `created_on`, `priority`)  
VALUES (112, 'second test todo', true, false, CURRENT_TIME(), 'MEDIUM');  
  
INSERT INTO task  
(`id`, `description`, `is_reminder_set`, `is_task_open`, `created_on`, `priority`)  
VALUES (113, 'third test todo', true, true, CURRENT_TIME(), 'HIGH');
```
这里的 SQL 指令做操作的数据都是虚拟数据，不影响数据库中的数据。

在 repository 中创建测试类TaskRepositoryEmbeddedTest：
```Kotlin
@DataJpaTest(properties = ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TaskRepositoryEmbeddedTest {
    @Autowired
    private lateinit var objectUnderTest: TaskRepository

    private val numberOfRecordsInTestDataSql = 3
    private val numberOfOpenRecordsInTestDataSql = 1
    private val numberOfClosedRecordsInTestDataSql = 2

    @Test
    @Sql("classpath:test-data.sql")
    fun `when task is saved then check for not null`() {
        //Given
        //When
        val task: Task = objectUnderTest.findTaskById(111)
        //Then
        assertThat(task).isNotNull
    }

    @Sql("classpath:test-data.sql")
    @Test
    fun `when all tasks are fetched then check for the number of records`() {
        //Given
        //When
        val tasks: List<Task> = objectUnderTest.findAll()

        //Then
        assertThat(tasks).isEqualTo(numberOfRecordsInTestDataSql)
    }
    
    @Sql("classpath:test-data.sql")
    @Test
    fun `when task is deleted then check for the size of list`() {
        // Given
        // When
        objectUnderTest.deleteById(113)
        val tasks: List<Task> = objectUnderTest.findAll()

        // Then
        assertThat(tasks.size).isEqualTo(2)
    }
    
    @Test
    @Sql("classpath:test-data.sql")
    fun `when all closed tasks are queried then check for the correct number of closed tasks`() {
        val tasks: List<Task> = objectUnderTest.queryAllClosedTasks()

        assertThat(tasks.size).isEqualTo(numberOfClosedRecordsInTestDataSql)
    }
    
    @Test
    @Sql("classpath:test-data.sql")
    fun `when all open tasks are queried then check for the correct number of open tasks`() {
        val tasks: List<Task> = objectUnderTest.queryAllOpenTasks()

        assertThat(tasks.size).isEqualTo(numberOfOpenRecordsInTestDataSql)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `when description is queried then check if description already exists`() {
        val doesDescriptionExist1 = objectUnderTest.doesDescriptionExist("second test todo")
        val doesDescriptionExist2 = objectUnderTest.doesDescriptionExist("feed the cat")

        assertThat(doesDescriptionExist1).isTrue
        assertThat(doesDescriptionExist2).isFalse
    }

}
```
