
# 常用的 Lambda 表达式案例解析

__ _ _ _ _

在日常工作中，Lambda 表达式的使用非常频繁，尤其是在集合类的流操作中。通过几行代码，Lambda
就可以帮助我们实现复杂的逻辑。下面我们通过一些常见的例子来解析 Lambda 表达式的常用方法。

## 1\. 集合遍历： ` forEach ` 方法

  *   *   *   *   *   *   *   * 
```java
public void testForEach(){
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.forEach(s -> System.out.println(s));
}
```

## 2\. 转换对象： ` collect ` 方法

将操作后的对象转化为新的对象：

  *   *   *   *   *   *   *   *   *   * 
```java
public void testCollect(){
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("2");
    }};
    List<Integer> newList = list.stream()
                                .map(s -> Integer.valueOf(s))
                                .collect(Collectors.toList());
}
```

## 3\. 过滤数据： ` filter ` 方法

` filter ` 用于过滤不满足条件的元素：

  *   *   *   *   *   *   *   *   *   *   * 
```java
public void testFilter() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};        
    list.stream()
        .filter(str -> "1".equals(str))
        .collect(Collectors.toList());
}
// 结果 2,3
```
## 4\. 流转换： ` map ` 方法

` map ` 方法可以对流中的元素进行转换：

  *   *   *   *   *   *   *   *   *   * 
```java
public void testMap() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    List<String> strLowerList = list.stream()
                                    .map(str -> str.toLowerCase())
                                    .collect(Collectors.toList());
}
```

## 5\. 数字流： ` mapToInt ` 方法

` mapToInt ` 返回 ` int ` 类型的流：

  *   *   *   *   *   *   *   *   *   *   *   *   *   *   * 
```java
public void testMapToInt() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.stream()
        .mapToInt(s -> Integer.valueOf(s))
        .mapToObj(s -> s)
        .collect(Collectors.toList());
    
    list.stream()
        .mapToDouble(s -> Double.valueOf(s))
        .sum();
}
```

## 6\. 去重： ` distinct ` 方法

` distinct ` 方法可以对流中的元素进行去重：

  *   *   *   *   *   *   *   *   *   *   * 
```java
public void testDistinct() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("2");
    }};
    list.stream()
        .map(s -> Integer.valueOf(s))
        .distinct()
        .collect(Collectors.toList());
}
```

## 7\. 排序： ` sorted ` 方法

` sorted ` 提供自然排序和自定义排序：

  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *  
```java
public void testSorted() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.stream()
        .map(s -> Integer.valueOf(s))
        .sorted()
        .collect(Collectors.toList());

    list.stream()
        .map(s -> Integer.valueOf(s))
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
}
```

## 8\. 分组： ` groupingBy ` 方法

` groupingBy ` 可以对流进行分组：

  *   *   *   *   *   *   *   *   * 
```java
public void testGroupBy() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("2");
    }};
    Map<String, List<String>> strList = list.stream()
                                            .collect(Collectors.groupingBy(s -> "2".equals(s) ? "2" : "1"));
}
```

## 9\. 获取第一个匹配值： ` findFirst ` 方法

` findFirst ` 方法返回第一个满足条件的元素：

  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   * 
```java
public void testFindFirst() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("2");
    }};
    list.stream()
        .filter(s -> "2".equals(s))
        .findFirst()
        .get();

    list.stream()
        .filter(s -> "2".equals(s))
        .findFirst()
        .orElse("3");

    Optional<String> str = list.stream()
                               .filter(s -> "2".equals(s))
                               .findFirst();
    if (str.isPresent()) {
        return;
    }
}
```

## 10\. 累加计算： ` reduce ` 方法

` reduce ` 方法可以对流中的元素进行累加或叠加：

  *   *   *   *   *   *   *   *   *   *   *   *   *   *   * 
```java
public void testReduce() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.stream()
        .map(s -> Integer.valueOf(s))
        .reduce((s1, s2) -> s1 + s2)
        .orElse(0);

    list.stream()
        .map(s -> Integer.valueOf(s))
        .reduce(100, (s1, s2) -> s1 + s2);
}
```

## 11\. 中间操作： ` peek ` 方法

` peek ` 方法允许在流处理过程中插入额外的操作，如日志记录：

  *   *   *   *   *   *   *   *   *   *   * 
```java
public void testPeek() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.stream()
        .map(s -> Integer.valueOf(s))
        .peek(s -> System.out.println(s))
        .collect(Collectors.toList());
}
```
## 12\. 限制输出： ` limit ` 方法

` limit ` 方法限制流的输出数量：

  *   *   *   *   *   *   *   *   *   *   * 
```java
public void testLimit() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
    }};
    list.stream()
        .map(s -> Integer.valueOf(s))
        .limit(2L)
        .collect(Collectors.toList());
}
```

## 13\. 获取最大/最小值： ` max ` 和 ` min ` 方法

` max ` 和 ` min ` 方法用于获取流中的最大值或最小值：

  *   *   *   *   *   *   *   *   *   *   *   *   *   * 
```java
public void testMaxMin() {
    List<String> list = new ArrayList<String>() {{
        add("1");
        add("2");
        add("2");
    }};
    list.stream()
        .max(Comparator.comparing(s -> Integer.valueOf(s)))
        .get();

    list.stream()
        .min(Comparator.comparing(s -> Integer.valueOf(s)))
        .get();
}
```

