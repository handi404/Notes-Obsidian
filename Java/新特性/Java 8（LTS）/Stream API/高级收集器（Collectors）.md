Java Stream 的 **高级收集器（Collectors）** 是处理流元素聚合操作的强大工具。以下是通俗易懂的讲解和示例：

---

### 一、核心概念
收集器（`Collectors`）是 `Stream` 终止操作的一种工具，用于将流元素**收集为集合、统计结果、分组数据**等形式。常见操作包括：
- **分组**（Grouping）
- **分区**（Partitioning）
- **统计**（Summarizing）
- **字符串拼接**（Joining）

---

### 二、常用高级收集器及示例

#### 1. **分组（Grouping）**
将元素按某个属性分类，返回 `Map<K, List<T>>`。

```java
// 按员工部门分组
Map<String, List<Employee>> grouped = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// 按部门分组后，统计每个部门人数
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
```

#### 2. **分区（Partitioning）**
根据条件将元素分为 `true` 和 `false` 两组。

```java
// 按是否及格分区
Map<Boolean, List<Student>> partitioned = students.stream()
    .collect(Collectors.partitioningBy(s -> s.getScore() >= 60));
```

#### 3. **统计（Summarizing）**
生成元素的统计信息（如总和、平均值、最大值等）。

```java
// 统计员工工资：总数、平均、最大值等
IntSummaryStatistics stats = employees.stream()
    .collect(Collectors.summarizingInt(Employee::getSalary));
System.out.println(stats.getAverage()); // 输出平均工资
```

#### 4. **字符串拼接（Joining）**
将元素拼接为字符串。

```java
// 拼接所有员工姓名，用逗号分隔
String names = employees.stream()
    .map(Employee::getName)
    .collect(Collectors.joining(", "));
```

#### 5. **组合收集器（下游收集器）**
通过嵌套 `Collectors` 实现复杂逻辑。

```java
// 按部门分组，再按工资排序取最高工资员工
Map<String, Optional<Employee>> topInDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.maxBy(Comparator.comparing(Employee::getSalary))
    ));
```

#### 6. **映射收集器（Mapping）**
对分组后的元素做转换处理。

```java
// 按部门分组，收集员工姓名列表
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(Employee::getName, Collectors.toList())
    ));
```

#### 7. **自定义收集器（Reducing）**
自定义聚合逻辑（如累加、合并）。

```java
// 计算所有员工工资总和
int total = employees.stream()
    .collect(Collectors.reducing(0, Employee::getSalary, Integer::sum));
```

---

### 三、使用场景总结
| 收集器 | 用途 | 典型场景 |
|--------|------|----------|
| `groupingBy` | 分组统计 | 按类别、部门等分类 |
| `partitioningBy` | 条件二分 | 过滤合格/不合格数据 |
| `summarizingInt/Long/Double` | 数值统计 | 计算平均值、最大值等 |
| `joining` | 字符串拼接 | 生成日志、列表字符串 |
| `mapping` | 分组后转换 | 提取分组内的特定字段 |
| `reducing` | 自定义聚合 | 复杂合并逻辑 |

---

### 四、注意事项
1. **并发处理**：使用 `parallelStream()` 时需确保收集器线程安全。
2. **下游收集器**：`groupingBy` 等支持链式调用（如 `groupingBy(..., counting())`）。
3. **空值处理**：流中存在 `null` 时需谨慎（如 `groupingBy` 会忽略 `null` 键）。

---

通过以上方法，可以高效处理复杂的数据聚合需求，替代传统的循环嵌套逻辑。