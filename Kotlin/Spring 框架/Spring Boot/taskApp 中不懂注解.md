```Kotlin
@Entity  
@Table(name = "task", uniqueConstraints = [UniqueConstraint(name = "uk_task_description", columnNames = ["description"])])  
class Task {  
    @Id    @GeneratedValue(generator = "task_sequence", strategy = GenerationType.SEQUENCE)  
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_sequence", allocationSize = 1)  
    val id: Long = 0  
    @NotBlank  
    @Column(name = "description", unique = true, nullable = false)  
    var description: String = ""  
  
    @Column(name = "is_reminder_set", nullable = false)  
    var isReminderSet: Boolean = false  
  
    @Column(name = "is_task_open", nullable = false)  
    var isTaskOpen: Boolean = true  
  
    @Column(name = "created_on", nullable = false)  
    val createdOn: LocalDateTime = LocalDateTime.now()  
  
    @NotNull    @Enumerated(EnumType.STRING)  
    var priority: Priority = Priority.LOW  
}
```


```Kotlin
@Repository  
interface TaskRepository: JpaRepository<Task, Long> {  
    fun findTaskById(id: Long): Task  
  
    @Query(value = "SELECT * FROM task WHERE is_task_open = TRUE", nativeQuery = true)  
    fun queryAllOpenTasks(): List<Task>  
    @Query(value = "SELECT * FROM task WHERE is_task_open = FALSE", nativeQuery = true)  
    fun queryAllClosedTasks(): List<Task>  
  
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Task t WHERE t.description = ?1")  
    fun doesDescriptionExist(description: String): Boolean  
}
```

