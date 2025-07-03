Java 集合框架的另一大核心分支：**`Map<K, V>` 接口**以及它的主要实现类：`HashMap<K, V>`, `LinkedHashMap<K, V>`, 和 `TreeMap<K, V>`。

`Map` 用于存储**键值对 (key-value pairs)**，其中每个键都是唯一的，并且映射到一个特定的值。它在需要通过键来快速查找、更新或删除值的场景中非常有用。

---

### `Map<K, V>` 接口：键值对存储，键唯一

*   **定义与特性：**
    *   `java.util.Map<K, V>` 接口代表一个将键映射到值的对象。
    *   **键唯一 (Unique Keys)：** 一个 `Map` 不能包含重复的键。每个键最多只能映射到一个值。如果你尝试用一个已存在的键插入新的键值对，新的值会覆盖旧的值，并且 `put()` 方法通常会返回旧的值。
    *   **值可重复 (Values can be duplicate)：** 不同的键可以映射到相同的值。
    *   **`null` 键和 `null` 值：**
        *   某些 `Map` 实现（如 `HashMap`, `LinkedHashMap`）允许一个 `null` 键和多个 `null` 值。
        *   某些 `Map` 实现（如 `TreeMap`）默认不允许 `null` 键（如果使用自然排序），是否允许 `null` 值取决于具体实现和配置。
        *   `ConcurrentHashMap` 通常不允许 `null` 键和 `null` 值。
    *   **顺序性 (Ordering)：**
        *   `Map` 接口本身不保证映射的顺序。
        *   一些 `Map` 实现（如 `HashMap`）是无序的。
        *   另一些 `Map` 实现（如 `LinkedHashMap`）会保持键的插入顺序或访问顺序。
        *   还有一些 `Map` 实现（如 `TreeMap`）会根据键的自然顺序或指定的 `Comparator` 对键进行排序。
    *   **不是 `Collection` 的子接口：** `Map` 接口与 `Collection` 接口是独立的，它们构成了 Java 集合框架的两大分支。尽管如此，`Map` 提供了三种“集合视图 (collection views)”方法，允许将 `Map` 的内容视为 `Set` 或 `Collection`。

*   **`Map<K, V>` 接口的核心方法：**

    1.  **基本操作 (Basic Operations)：**
        *   `V put(K key, V value)`: 将指定的键值对关联到此映射中。如果映射先前包含一个该键的映射关系，则旧值被替换并返回。如果键是新的，返回 `null`。
        *   `V get(Object key)`: 返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 `null`。注意参数是 `Object`，因为要能用任何对象去查找。
        *   `V remove(Object key)`: 如果存在一个键的映射关系，则将其从此映射中移除，并返回与该键关联的旧值；否则返回 `null`。
        *   `boolean containsKey(Object key)`: 如果此映射包含指定键的映射关系，则返回 `true`。
        *   `boolean containsValue(Object value)`: 如果此映射将一个或多个键映射到指定值，则返回 `true`。这个操作通常效率较低 (O(n))，因为它可能需要遍历所有值。
        *   `int size()`: 返回此映射中的键值对数量。
        *   `boolean isEmpty()`: 如果此映射不包含键值对，则返回 `true`。
        *   `void clear()`: 从此映射中移除所有映射关系。

    2.  **批量操作 (Bulk Operations)：**
        *   `void putAll(Map<? extends K, ? extends V> m)`: 将指定映射中的所有映射关系复制到此映射中。

    3.  **集合视图 (Collection Views)：**
        *   `Set<K> keySet()`: 返回此映射中包含的键的 `Set` 视图。对这个 `Set` 的修改会反映到原始 `Map` 中（例如，通过迭代器从 `keySet` 中移除一个键，会同时移除 `Map` 中的对应条目），反之亦然 (但 `keySet` 不支持 `add` 或 `addAll` 操作)。
        *   `Collection<V> values()`: 返回此映射中包含的值的 `Collection` 视图。对这个 `Collection` 的修改也会反映到原始 `Map` 中（例如，通过迭代器移除一个值，会移除 `Map` 中映射到该值的第一个条目），反之亦然 (但 `values` 不支持 `add` 或 `addAll` 操作)。
        *   `Set<Map.Entry<K, V>> entrySet()`: 返回此映射中包含的映射关系 (键值对条目) 的 `Set` 视图。每个元素都是一个 `Map.Entry<K, V>` 对象。这是**遍历 `Map` 中所有键值对的最常用和最高效的方式**。对这个 `Set` 或其元素的 `Map.Entry` (通过 `setValue()`) 的修改会反映到原始 `Map` 中。

    4.  **`Map.Entry<K, V>` 接口 (嵌套接口)：**
        *   代表 `Map` 中的一个键值对条目。
        *   核心方法：
            *   `K getKey()`: 返回此条目的键。
            *   `V getValue()`: 返回此条目的值。
            *   `V setValue(V value)`: 用指定的值替换此条目对应的值，并返回旧值。**这个修改会直接反映在底层的 `Map` 中。**
            *   `boolean equals(Object o)`
            *   `int hashCode()`

    5.  **`equals()` 和 `hashCode()` 的约定：**
        *   两个 `Map` 相等，当且仅当它们包含相同的键值对映射关系（即它们的 `entrySet()` 返回的 `Set` 是 `equals` 的）。键和值的比较都使用它们各自的 `equals()` 方法。
        *   `hashCode()` 的计算也必须基于其 `entrySet()` 的 `hashCode()`。

    6.  **Java 8+ `default` 方法 (非常重要和实用)：**
        *   `V getOrDefault(Object key, V defaultValue)`: 获取指定键的值，如果键不存在，则返回 `defaultValue`。
        *   `void forEach(BiConsumer<? super K, ? super V> action)`: 对映射中的每个条目执行给定的操作。
        *   `void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)`: 用将函数应用于每个条目的结果替换每个条目的值。
        *   `V putIfAbsent(K key, V value)`: 如果指定的键尚未与某个值关联（或映射到 `null`），则将其与给定值关联并返回 `null`，否则返回当前值。
        *   `boolean remove(Object key, Object value)`: 仅当指定的键当前映射到指定的值时，才移除该键的条目。
        *   `boolean replace(K key, V oldValue, V newValue)`: 仅当指定的键当前映射到指定旧值时，才用新值替换该键的条目。
        *   `V replace(K key, V value)`: 仅当指定的键当前映射到某个值时，才用新值替换该键的条目。
        *   `V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)`: 如果指定的键尚未与值关联或与 `null` 关联，则尝试使用给定的映射函数计算其值，并将其输入此映射，除非 `null`。
        *   `V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)`: 如果指定键的值存在且非 `null`，则尝试在给定键及其当前映射值的情况下计算新映射。
        *   `V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)`: 尝试在给定键及其当前映射值（如果不存在则为 `null`）的情况下计算映射。
        *   `V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)`: 如果指定的键尚未与值关联或与 `null` 关联，则将其与给定的非 `null` 值关联。否则，将关联值替换为给定重映射函数的结果，如果结果为 `null`，则删除映射。这个方法对于实现原子性的“读取-修改-写入”操作非常有用，例如计数器。

    7.  **Java 9+ 静态工厂方法 `Map.of(...)` 和 `Map.ofEntries(...)`:**
        *   `static <K,V> Map<K,V> of()`
        *   `static <K,V> Map<K,V> of(K k1, V v1, ... K k10, V v10)` (多个重载，最多 10 个键值对)
        *   `static <K,V> Map<K,V> ofEntries(Map.Entry<? extends K, ? extends V>... entries)`
        *   这些方法用于创建**不可变的 (immutable)** `Map` 实例。
        *   **特性：**
            *   返回的映射是不可变的。
            *   不允许 `null` 键或 `null` 值 (会抛 `NullPointerException`)。
            *   不允许重复的键 (会抛 `IllegalArgumentException`)。
            *   通常比创建可变映射然后包装更高效。

---

### `HashMap<K, V>`: 基于哈希表，无序，性能高

*   **底层数据结构：**
    *   `HashMap<K, V>` 使用**哈希表 (hash table)** 来存储键值对。
    *   内部结构是一个**节点数组 (array of nodes)**，每个节点 `Node<K,V>` (或 JDK 8+ 的 `TreeNode<K,V>`) 存储一个键值对、哈希值以及指向下一个节点的引用 (用于处理哈希冲突的链表)。
    *   **JDK 8+ 优化：** 当哈希桶中的链表长度超过一定阈值 (默认为 `TREEIFY_THRESHOLD = 8`) 并且哈希表的大小也达到一定阈值 (默认为 `MIN_TREEIFY_CAPACITY = 64`) 时，该链表会**转换为红黑树 (red-black tree)**，以提高在哈希冲突严重情况下的查找性能 (从 O(n) 降到 O(log n))。当红黑树的节点数减少到一定阈值 (默认为 `UNTREEIFY_THRESHOLD = 6`) 时，又会转换回链表。

*   **核心特性与性能：**
    *   **无序 (Unordered)：** `HashMap` 不保证键值对的迭代顺序，这个顺序可能会随着元素的添加和删除而改变。
    *   **高性能 (通常 O(1))：** 对于 `put()`, `get()`, `remove()` 操作，只要哈希函数设计良好且哈希冲突不严重，平均时间复杂度是 **O(1)**。最坏情况下 (所有键哈希到同一个桶且未树化或树化后)，时间复杂度会退化到 O(n) 或 O(log n)。
    *   **依赖键的 `hashCode()` 和 `equals()`：**
        *   为了正确地存储和检索键值对，键对象**必须正确地实现 `hashCode()` 和 `equals()` 方法**，并遵守它们的约定。
        *   `HashMap` 首先使用键的 `hashCode()` 计算出它在内部数组中的索引 (桶位置)。然后，如果桶中已有元素（哈希冲突），则通过 `equals()` 方法比较键，以找到正确的条目或确定键是否已存在。
        *   **键的不可变性：** 与 `HashSet` 类似，如果键的 `hashCode()` 在键被添加到 `HashMap` 后发生了改变，`HashMap` 可能无法再正确地找到这个键对应的条目。因此，**强烈推荐使用不可变对象作为 `HashMap` 的键。**
    *   **允许一个 `null` 键和多个 `null` 值。** `null` 键总是被映射到哈希表的索引 0 处。
    *   **线程不安全：** `HashMap` 不是线程安全的。如果需要线程安全的 `Map`，可以使用 `Collections.synchronizedMap(new HashMap<>())` 或更推荐的 `java.util.concurrent.ConcurrentHashMap`。

*   **构造函数、初始容量、加载因子、扩容 (rehash)：**
    *   与 `HashSet` 非常类似，因为 `HashSet` 内部就是用的 `HashMap`。
    *   可以指定初始容量 (默认 16) 和加载因子 (默认 0.75)。
    *   当 `size >= capacity * loadFactor` 时，会进行扩容 (rehash)，容量通常会加倍，并将所有现有条目重新分配到新的桶中。这是一个耗时的操作。
    *   合理设置初始容量对于避免不必要的 rehash 非常重要。如果能预估键值对的数量 `n`，可以将初始容量设置为 `(int)(n / loadFactor) + 1` 或更大的一个 2 的幂。

*   **适用场景：**
    *   当你需要一个**不保证顺序**但要求**快速存取键值对**的映射时。
    *   这是最常用的 `Map` 实现。

---

### `LinkedHashMap<K, V>`: 基于哈希表和链表，保持插入顺序或访问顺序

*   **底层数据结构：**
    *   `LinkedHashMap<K, V>` 继承自 `HashMap<K, V>`。
    *   它在 `HashMap` 的基础上，额外维护了一个**贯穿所有条目的双向链表**。
    *   这个链表定义了迭代顺序，可以是**插入顺序 (insertion-order)** 或**访问顺序 (access-order)**。

*   **核心特性与性能：**
    *   **顺序性：**
        *   **插入顺序 (默认)：** 迭代时，键值对会按照它们被插入到映射中的顺序出现。
        *   **访问顺序：** 如果在构造 `LinkedHashMap` 时将 `accessOrder` 参数设置为 `true`，那么每次调用 `get()`、`put()` (如果键已存在并更新值) 或 `compute*()` 等访问操作时，被访问的条目会被移动到双向链表的**尾部**。这使得最近访问的条目总是在链表尾部，最久未访问的条目在链表头部。此特性非常适合用来实现 **LRU (Least Recently Used) 缓存**。
    *   **性能：**
        *   `put()`, `get()`, `remove()` 操作的平均时间复杂度仍然是 **O(1)**，与 `HashMap` 类似。
        *   由于需要维护额外的链表，`LinkedHashMap` 的性能开销会比 `HashMap` **略高**一点点，内存占用也会稍大一些。
    *   **依赖键的 `hashCode()` 和 `equals()`：** 与 `HashMap` 相同。
    *   **允许一个 `null` 键和多个 `null` 值。**
    *   **线程不安全。**

*   **构造函数：**
    *   `LinkedHashMap()`: 默认初始容量、加载因子，插入顺序。
    *   `LinkedHashMap(int initialCapacity)`
    *   `LinkedHashMap(int initialCapacity, float loadFactor)`
    *   `LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)`: **可以指定 `accessOrder` 为 `true` 来启用访问顺序。**
    *   `LinkedHashMap(Map<? extends K, ? extends V> m)`

*   **实现 LRU 缓存：**
    *   可以通过继承 `LinkedHashMap` 并重写 `removeEldestEntry(Map.Entry<K,V> eldest)` 方法来轻松实现一个固定大小的 LRU 缓存。当 `put` 或 `putAll` 后，如果此方法返回 `true`，则最老的条目 (链表头部的条目，在访问顺序模式下即最久未访问的条目) 会被移除。
    ```java
    class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRUCache(int capacity) {
            // true for access-order
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            // Remove eldest if size exceeds capacity
            return size() > capacity;
        }

        public static void main(String[] args) {
            LRUCache<Integer, String> cache = new LRUCache<>(2);
            cache.put(1, "A");
            cache.put(2, "B");
            System.out.println(cache); // {1=A, 2=B} (顺序可能取决于初始添加)
            cache.get(1); // Access 1
            System.out.println(cache); // {2=B, 1=A} (1 变为最近访问)
            cache.put(3, "C"); // Add 3, triggers removal of eldest (2)
            System.out.println(cache); // {1=A, 3=C} (2 被移除)
        }
    }
    ```

*   **适用场景：**
    *   当你需要一个**保持键插入顺序**或**访问顺序**并且能够**快速存取键值对**的映射时。
    *   实现 LRU 缓存。

---

### `TreeMap<K, V>`: 基于红黑树，键有序 (自然顺序或自定义顺序)

*   **底层数据结构：**
    *   `TreeMap<K, V>` 使用**红黑树 (Red-Black Tree)** 来存储键值对，并根据键进行排序。
    *   它实现了 `NavigableMap<K, V>` 接口 (继承自 `SortedMap<K, V>`)。

*   **核心特性与性能：**
    *   **键有序 (Keys are Sorted)：** `TreeMap` 中的键值对总是根据键的顺序进行排序。
        *   **排序方式：**
            1.  **自然排序：** 如果键实现了 `java.lang.Comparable<K>` 接口，并且在构造 `TreeMap` 时没有提供 `Comparator`。
            2.  **自定义排序：** 如果在构造 `TreeMap` 时传入一个 `java.util.Comparator<K>` 实例。
        *   **键“相等性”判断：** `TreeMap` 使用键的 `compareTo()` (或 `Comparator.compare()`) 方法来判断键是否“相等”以保证唯一性。如果 `key1.compareTo(key2) == 0`，`TreeMap` 会认为这两个键是相同的，即使它们的 `key1.equals(key2)` 可能返回 `false`。
    *   **性能：**
        *   `put()`, `get()`, `remove()`, `containsKey()` 操作的时间复杂度是 **O(log n)**。
    *   **不允许 `null` 键 (默认情况下)：**
        *   如果使用自然排序，尝试使用 `null` 键会导致 `NullPointerException`。
        *   如果使用自定义 `Comparator`，是否允许 `null` 键取决于比较器的实现。
        *   `TreeMap` **允许 `null` 值**。
    *   **线程不安全。**

*   **`NavigableMap<K, V>` 接口：**
    *   提供了许多额外的导航和范围查询方法，与 `NavigableSet` 类似，但操作的是键：
        *   `Map.Entry<K,V> lowerEntry(K key)`, `K lowerKey(K key)`
        *   `Map.Entry<K,V> floorEntry(K key)`, `K floorKey(K key)`
        *   `Map.Entry<K,V> ceilingEntry(K key)`, `K ceilingKey(K key)`
        *   `Map.Entry<K,V> higherEntry(K key)`, `K higherKey(K key)`
        *   `Map.Entry<K,V> firstEntry()`, `K firstKey()`
        *   `Map.Entry<K,V> lastEntry()`, `K lastKey()`
        *   `Map.Entry<K,V> pollFirstEntry()`, `Map.Entry<K,V> pollLastEntry()`
        *   `NavigableMap<K,V> descendingMap()`: 返回此 map 的逆序视图。
        *   `NavigableSet<K> navigableKeySet()`: 返回键的 `NavigableSet` 视图。
        *   `NavigableSet<K> descendingKeySet()`
        *   `NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)`
        *   `NavigableMap<K,V> headMap(K toKey, boolean inclusive)`
        *   `NavigableMap<K,V> tailMap(K fromKey, boolean inclusive)`

*   **构造函数：**
    *   `TreeMap()`: 按键的自然顺序。
    *   `TreeMap(Comparator<? super K> comparator)`: 按指定比较器。
    *   `TreeMap(Map<? extends K, ? extends V> m)`: 从已有 `Map` 创建，按键自然顺序。
    *   `TreeMap(SortedMap<K, ? extends V> m)`: 从已有 `SortedMap` 创建，使用其排序方式。

*   **适用场景：**
    *   当你需要一个**根据键自动保持排序**并且能够**快速存取键值对**的映射时。
    *   当需要频繁进行基于键的范围查询或获取最大/最小键对应的条目时。

---

### `Map` 实现类总结对比

| 特性                | `HashMap<K,V>`                            | `LinkedHashMap<K,V>`                         | `TreeMap<K,V>`                                          |
| ------------------- | ----------------------------------------- | ---------------------------------------------- | ------------------------------------------------------- |
| **底层结构**        | 哈希表 (数组+链表/红黑树)                  | 哈希表 + 双向链表                             | 红黑树                                                  |
| **顺序性 (键)**     | 无序                                      | 保持插入顺序或访问顺序                       | 有序 (自然顺序或自定义顺序)                             |
| **性能 (put,get,remove)**| 平均 O(1)                                 | 平均 O(1) (略慢于 `HashMap`)                 | O(log n)                                                |
| **`null` 键**       | 允许一个                                  | 允许一个                                       | 默认不允许 (取决于 `Comparator`)                         |
| **`null` 值**       | 允许多个                                  | 允许多个                                       | 允许多个                                                |
| **键唯一性依据**    | `equals()` 和 `hashCode()`                  | `equals()` 和 `hashCode()`                       | `compareTo()` 或 `Comparator.compare()` (返回 0 则视为重复) |
| **额外功能**        | Java 8+ default methods                  | 可配置访问顺序 (LRU)                           | `NavigableMap` 方法 (范围查询、导航)                    |
| **线程安全**        | 否                                        | 否                                             | 否                                                      |

**选择建议：**

*   **如果只需要快速存取键值对，不关心顺序，且追求最高性能：** 选择 `HashMap`。这是最通用的选择。
*   **如果需要保持键的插入顺序或访问顺序 (如实现 LRU 缓存)：** 选择 `LinkedHashMap`。
*   **如果需要键始终处于排序状态，或者需要执行基于键的范围查询：** 选择 `TreeMap`。但要注意其对键类型和唯一性判断方式的要求。

**遍历 `Map` 的推荐方式：**

使用 `entrySet()` 结合增强 `for` 循环或 `forEach` 方法：

```java
Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

// 1. 使用 entrySet() 和增强 for 循环 (推荐)
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    String key = entry.getKey();
    Integer value = entry.getValue();
    System.out.println("Key: " + key + ", Value: " + value);
    // entry.setValue(value * 10); // 可以修改值
}

// 2. 使用 forEach() 方法 (Java 8+)
map.forEach((key, value) -> {
    System.out.println("Key: " + key + ", Value: " + value);
});

// 3. 仅遍历键 (使用 keySet())
// for (String key : map.keySet()) {
//     Integer value = map.get(key); // 需要再次查找value，效率略低
//     System.out.println("Key: " + key + ", Value: " + value);
// }

// 4. 仅遍历值 (使用 values())
// for (Integer value : map.values()) {
//     System.out.println("Value: " + value);
// }
```

---

`Map` 是编程中用于关联数据和快速查找的核心工具。