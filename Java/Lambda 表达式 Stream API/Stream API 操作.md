## 
- [[#1. **创建 Stream**|1. **创建 Stream**]]
- [[#2. **中间操作**|2. **中间操作**]]
	- [[#2. **中间操作**#**2.1. `filter`**|**2.1. `filter`**]]
	- [[#2. **中间操作**#**2.2. `map`**|**2.2. `map`**]]
	- [[#2. **中间操作**#**2.3. `flatMap`**|**2.3. `flatMap`**]]
	- [[#2. **中间操作**#**2.4. `distinct`**|**2.4. `distinct`**]]
	- [[#2. **中间操作**#**2.5. `sorted`**|**2.5. `sorted`**]]
	- [[#2. **中间操作**#**2.6. `peek`**|**2.6. `peek`**]]
	- [[#2. **中间操作**#**2.7. `limit` 和 `skip`**|**2.7. `limit` 和 `skip`**]]
- [[#3. **终止操作**|3. **终止操作**]]
	- [[#3. **终止操作**#**3.1. `forEach`**|**3.1. `forEach`**]]
	- [[#3. **终止操作**#**3.2. `collect`**|**3.2. `collect`**]]
	- [[#3. **终止操作**#**3.3. `count`**|**3.3. `count`**]]
	- [[#3. **终止操作**#**3.4. `reduce`**|**3.4. `reduce`**]]
	- [[#3. **终止操作**#**3.5. `findFirst` 和 `findAny`**|**3.5. `findFirst` 和 `findAny`**]]
	- [[#3. **终止操作**#**3.6. `anyMatch`、`allMatch` 和 `noneMatch`**|**3.6. `anyMatch`、`allMatch` 和 `noneMatch`**]]
	- [[#3. **终止操作**#**3.7. `min` 和 `max`**|**3.7. `min` 和 `max`**]]
	- [[#3. **终止操作**#**3.8. `toArray`**|**3.8. `toArray`**]]
- [[#4. **总结**|4. **总结**]]

Java 的 Stream API 提供了一种高效且声明性的方式来处理集合数据。Stream API 支持一系列操作，这些操作可以链式调用，以实现数据的过滤、转换、排序等功能。Stream API 操作分为中间操作和终止操作。

### 1. **创建 Stream**

**1.1. 从集合创建**

```java
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();
```

**1.2. 从数组创建**

```java
Stream<String> stream = Stream.of("a", "b", "c");
```

**1.3. 从值创建**

```java
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
```

**1.4. 使用 `Stream.generate` 或 `Stream.iterate`**

```java
Stream<Integer> infiniteStream = Stream.generate(() -> 1).limit(10);
Stream<Integer> naturalNumbers = Stream.iterate(1, n -> n + 1).limit(10);
```

### 2. **中间操作**

中间操作是惰性求值的，它们会返回一个新的 Stream。这些操作不会立即执行，只有在遇到终止操作时才会进行计算。

#### **2.1. `filter`**

用于过滤数据，保留符合条件的元素。

```java
List<String> list = Arrays.asList("java", "python", "node");
List<String> filtered = list.stream()
    .filter(s -> s.length() > 4)
    .collect(Collectors.toList());
```

#### **2.2. `map`**

用于将每个元素映射到另一个值，通常用于转换数据。

```java
List<String> list = Arrays.asList("java", "python", "node");
List<Integer> lengths = list.stream()
    .map(String::length)
    .collect(Collectors.toList());
```

#### **2.3. `flatMap`**

用于将每个元素映射到一个 Stream，然后将所有 Stream 合并成一个。

```java
List<List<String>> listOfLists = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d")
);
List<String> flattened = listOfLists.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
```

#### **2.4. `distinct`**

用于去除重复的元素。

```java
List<String> list = Arrays.asList("a", "b", "a", "c", "b");
List<String> distinct = list.stream()
    .distinct()
    .collect(Collectors.toList());
```

#### **2.5. `sorted`**

用于对元素进行排序，可以指定排序方式。

```java
List<String> list = Arrays.asList("java", "python", "node");
List<String> sorted = list.stream()
    .sorted()
    .collect(Collectors.toList());
```

#### **2.6. `peek`**

用于在处理元素时进行调试，通常用于打印流中的元素而不修改流。

```java
List<String> list = Arrays.asList("java", "python", "node");
list.stream()
    .filter(s -> s.length() > 4)
    .peek(System.out::println)  // 调试输出
    .collect(Collectors.toList());
```

#### **2.7. `limit` 和 `skip`**

- `limit(n)`：限制流的元素数量为 n。
- `skip(n)`：跳过流中的前 n 个元素。

```java
List<String> list = Arrays.asList("a", "b", "c", "d", "e");
List<String> limited = list.stream()
    .limit(3)
    .collect(Collectors.toList());  // ["a", "b", "c"]

List<String> skipped = list.stream()
    .skip(2)
    .collect(Collectors.toList());  // ["c", "d", "e"]
```

### 3. **终止操作**

终止操作触发 Stream 的处理，并产生最终结果或副作用。终止操作一旦执行，Stream 就不能再被操作。

#### **3.1. `forEach`**

用于遍历每个元素，并对每个元素执行操作。

```java
List<String> list = Arrays.asList("java", "python", "node");
list.stream().forEach(System.out::println);
```

#### **3.2. `collect`**

用于将 Stream 的元素收集到集合、列表、映射等数据结构中。

```java
List<String> list = Arrays.asList("java", "python", "node");
Set<String> set = list.stream()
    .collect(Collectors.toSet());
```

#### **3.3. `count`**

用于计算 Stream 中元素的数量。

```java
long count = list.stream()
    .count();  // 3
```

#### **3.4. `reduce`**

用于将 Stream 的元素结合成一个单一的值。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);  // 15
```

#### **3.5. `findFirst` 和 `findAny`**

用于查找流中的第一个元素或任何一个元素。

```java
Optional<String> first = list.stream()
    .findFirst();  // Optional["java"]

Optional<String> any = list.stream()
    .findAny();    // Optional["java"] or any other
```

#### **3.6. `anyMatch`、`allMatch` 和 `noneMatch`**

用于判断流中的元素是否满足某个条件。

```java
boolean anyMatch = list.stream()
    .anyMatch(s -> s.startsWith("j"));  // true

boolean allMatch = list.stream()
    .allMatch(s -> s.length() > 2);     // true

boolean noneMatch = list.stream()
    .noneMatch(s -> s.startsWith("z")); // true
```

#### **3.7. `min` 和 `max`**

用于获取 Stream 中的最小值或最大值。

```java
Optional<String> min = list.stream()
    .min(String::compareTo);  // Optional["java"]

Optional<String> max = list.stream()
    .max(String::compareTo);  // Optional["python"]
```

#### **3.8. `toArray`**

用于将 Stream 的元素收集到数组中。

```java
String[] array = list.stream()
    .toArray(String[]::new);
```

### 4. **总结**

Stream API 提供了一种声明式的方式来处理数据，支持多种中间操作和终止操作，使得集合处理变得更加简洁和强大。通过灵活组合这些操作，你可以编写出高效且可读性高的数据处理代码。