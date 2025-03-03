你可以利用 Java 8 的 Stream API 来对 List 中的每个实体对象调用 mappingEntityToDto 方法，然后收集成一个 DTO 列表。下面给出两种常见的写法：

---

### **1. 使用 Stream 的方式**

```java
@Service
public class YourService {
    
    @Autowired
    private YourRepository repository;

    public List<YourDto> getAllDtos() {
        List<YourEntity> entities = repository.findAll();
        return entities.stream()
                .map(this::mappingEntityToDto)
                .collect(Collectors.toList());
        //return entities.stream()
        //		.map(entity -> mappingEntityToDto(entity))
        //		.collect(Collectors.toList)
    }

    // 假设已实现的实体到 DTO 的映射方法
    private YourDto mappingEntityToDto(YourEntity entity) {
        YourDto dto = new YourDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        // 其它属性映射...
        return dto;
    }
}
```

在这段代码中，我们先通过 `repository.findAll()` 得到所有实体对象，然后利用 `stream()` 创建流，调用 `map(this::mappingEntityToDto)` 对每个对象进行转换，最后用 `collect(Collectors.toList())` 收集成一个 DTO 列表。

---

### **2. 使用传统 for 循环**

```java
@Service
public class YourService {
    
    @Autowired
    private YourRepository repository;

    public List<YourDto> getAllDtos() {
        List<YourEntity> entities = repository.findAll();
        List<YourDto> dtos = new ArrayList<>();
        for (YourEntity entity : entities) {
            dtos.add(mappingEntityToDto(entity));
        }
        return dtos;
    }

    private YourDto mappingEntityToDto(YourEntity entity) {
        YourDto dto = new YourDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        // 其它属性映射...
        return dto;
    }
}
```

这种方式也是常用的，它通过遍历 List，将每个实体对象转换后添加到一个新的 List 中。

---

两种方式都能达到将实体对象转换为 DTO 的效果，具体选择哪种方式可以根据团队的编码习惯或项目需要来定。