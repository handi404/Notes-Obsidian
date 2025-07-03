**`Set<E>` 接口** 以及它的主要实现类：`HashSet<E>`, `LinkedHashSet<E>`, 和 `TreeSet<E>`。

`Set` 的核心特性是它**不包含重复元素**，这使得它在需要存储唯一项的场景中非常有用。

---

### `Set<E>` 接口：无序 (通常)、不可重复

*   **定义与特性：**
    *   `java.util.Set<E>` 接口继承自 `Collection<E>` 接口。
    *   它代表一个**不包含重复元素的集合**。更准确地说，`Set` 中最多只能包含一个 `null` 元素（如果其实现允许 `null` 的话）。
    *   **不可重复性 (Uniqueness)：** 当你尝试向 `Set` 中添加一个元素时，如果该元素已经存在（根据 `e.equals(oldElement)` 判断），则 `add(E e)` 方法会返回 `false`，并且 `Set` 的内容不会改变。
    *   **顺序性 (Ordering)：**
        *   `Set` 接口本身**不保证**元素的顺序。一些 `Set` 实现（如 `HashSet`）是无序的，元素的迭代顺序可能与插入顺序不同，甚至可能随时间变化。
        *   另一些 `Set` 实现（如 `LinkedHashSet`）会保持元素的插入顺序。
        *   还有一些 `Set` 实现（如 `TreeSet`）会根据元素的自然顺序或指定的 `Comparator` 对元素进行排序。
    *   **`equals()` 和 `hashCode()` 的约定：**
        *   `Set` 接口对其 `equals()` 方法有严格的约定：两个 `Set` 相等，当且仅当它们大小相同，并且第一个 `Set` 中的每个元素都包含在第二个 `Set` 中（顺序无关）。
        *   `hashCode()` 的计算也必须基于 `Set` 中所有元素的 `hashCode()` 之和（顺序无关）。

*   **`Set<E>` 接口主要依赖于从 `Collection<E>` 继承的方法。** 它并没有在 `Collection` 的基础上添加很多新的方法，其核心行为（如不重复性）主要通过对 `add()`、`addAll()` 等方法的行为约束来体现。

*   **常用操作：**
    *   `add(E e)`: 添加元素，如果元素已存在则不添加，返回 `false`。
    *   `remove(Object o)`: 移除元素。
    *   `contains(Object o)`: 判断是否包含元素。
    *   `size()`, `isEmpty()`, `clear()`, `iterator()`.
    *   批量操作: `containsAll()`, `addAll()`, `removeAll()`, `retainAll()`.

---

### `HashSet<E>`: 基于哈希表，无序，性能高

*   **底层数据结构：**
    *   `HashSet<E>` 的核心是**内部使用了一个 `HashMap<E, Object>` 实例**来存储元素。
    *   `Set` 中的元素作为 `HashMap` 的**键 (key)**。
    *   `HashMap` 的**值 (value)** 存储的是一个固定的、私有的静态 `Object` 对象，名为 `PRESENT`。
        ```java
        // HashSet 源码片段 (简化示意)
        // private transient HashMap<E,Object> map;
        // // Dummy value to associate with an Object in the backing Map
        // private static final Object PRESENT = new Object();

        // public HashSet() {
        //     map = new HashMap<>();
        // }

        // public boolean add(E e) {
        //     return map.put(e, PRESENT) == null; // 如果key不存在，put返回null；如果key已存在，put返回旧value (PRESENT)
        // }

        // public boolean contains(Object o) {
        //     return map.containsKey(o);
        // }

        // public boolean remove(Object o) {
        //     return map.remove(o) == PRESENT; // 如果key存在并被移除，remove返回其value (PRESENT)
        // }
        // ... 其他方法也委托给 map
        ```

*   **核心特性与性能：**
    *   **无序 (Unordered)：** `HashSet` 不保证元素的迭代顺序，这个顺序可能会随着元素的添加和删除而改变，也可能与插入顺序完全不同。它依赖于元素的 `hashCode()` 来确定存储位置。
    *   **高性能 (通常 O(1))：** 对于 `add()`, `remove()`, `contains()` 操作，只要哈希函数设计良好且哈希冲突不严重，平均时间复杂度是 **O(1)**。最坏情况下 (所有元素哈希到同一个桶)，时间复杂度会退化到 O(n)。
    *   **依赖 `hashCode()` 和 `equals()`：**
        *   为了正确地存储和检索元素，存入 `HashSet` 的对象**必须正确地实现 `hashCode()` 和 `equals()` 方法**，并遵守它们的约定（`equals` 相等则 `hashCode` 必须相等）。
        *   如果对象的 `hashCode()` 在对象被添加到 `HashSet` 后发生了改变（通常是因为参与 `hashCode` 计算的字段是可变的并且被修改了），那么 `HashSet` 可能无法再正确地找到这个对象。因此，**强烈推荐使用不可变对象或者至少确保哈希码相关的字段在对象存入 `Set` 后不被修改。**
    *   **允许 `null` 元素：** `HashSet` 最多允许一个 `null` 元素 (因为 `HashMap` 允许一个 `null` key)。
    *   **线程不安全：** `HashSet` 不是线程安全的。

*   **构造函数：**
    *   `HashSet()`: 创建一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 `HashSet` (实际上是其内部 `HashMap` 的默认值)。
    *   `HashSet(int initialCapacity)`: 创建一个具有指定初始容量和默认加载因子 (0.75) 的空 `HashSet`。
    *   `HashSet(int initialCapacity, float loadFactor)`: 创建一个具有指定初始容量和加载因子的空 `HashSet`。
        *   **初始容量 (Initial Capacity)：** 哈希表中桶 (bucket) 的数量。
        *   **加载因子 (Load Factor)：** 一个介于 0.0 和 1.0 之间的数字，它决定了哈希表在其容量自动增加之前可以达到多满。当哈希表中的条目数超出了加载因子与当前容量的乘积时，哈希表将进行 **rehash** (重建内部数据结构，通常是容量加倍)。
        *   设置过高的加载因子会减少空间开销但增加查找成本。合理设置初始容量和加载因子对于性能很重要，特别是对于包含大量元素的 `HashSet`。
    *   `HashSet(Collection<? extends E> c)`: 创建一个包含指定集合元素的新 `HashSet`。容量会被设置为足以容纳 `c` 中元素的最小值（通常是 `max(16, c.size() / loadFactor + 1)`）。

*   **适用场景：**
    *   当你需要一个**不保证顺序**但要求**快速查找、添加、删除唯一元素**的集合时。
    *   例如：元素去重、判断某个元素是否存在于一组数据中。

---

### `LinkedHashSet<E>`: 基于哈希表和链表，保持插入顺序

*   **底层数据结构：**
    *   `LinkedHashSet<E>` 继承自 `HashSet<E>`。
    *   它内部仍然使用一个 `HashMap` (实际上是 `LinkedHashMap<E, Object>`) 来存储元素，以保证 O(1) 的平均性能。
    *   与 `HashSet` 不同的是，`LinkedHashSet` 还额外维护了一个**双向链表**来记录元素的**插入顺序**。

*   **核心特性与性能：**
    *   **保持插入顺序 (Insertion-ordered)：** 当你迭代 `LinkedHashSet` 时，元素会按照它们被插入到集合中的顺序出现。
    *   **性能：**
        *   `add()`, `remove()`, `contains()` 操作的平均时间复杂度仍然是 **O(1)**，与 `HashSet` 类似，因为它们依赖于底层的哈希表结构。
        *   由于需要维护额外的链表，`LinkedHashSet` 的性能开销会比 `HashSet` **略高**一点点 (常量级别)，内存占用也会稍大一些。
    *   **依赖 `hashCode()` 和 `equals()`：** 与 `HashSet` 相同。
    *   **允许 `null` 元素：** 最多一个 `null` 元素。
    *   **线程不安全：** `LinkedHashSet` 不是线程安全的。

*   **构造函数：**
    *   与 `HashSet` 类似，可以指定初始容量和加载因子。
    *   `LinkedHashSet()`
    *   `LinkedHashSet(int initialCapacity)`
    *   `LinkedHashSet(int initialCapacity, float loadFactor)`
    *   `LinkedHashSet(Collection<? extends E> c)`

*   **适用场景：**
    *   当你需要一个**保持元素插入顺序**并且能够**快速查找、添加、删除唯一元素**的集合时。
    *   例如：需要去重，并且后续处理依赖于原始数据的输入顺序。
    *   `LinkedHashMap` (其底层支持) 还可以配置为维护**访问顺序 (access-order)**，这可以用于实现 LRU (Least Recently Used) 缓存。但 `LinkedHashSet` 本身不直接暴露这个功能，它固定使用插入顺序。

---

### `TreeSet<E>`: 基于红黑树，有序 (自然顺序或自定义顺序)

*   **底层数据结构：**
    *   `TreeSet<E>` 内部使用一个**平衡二叉搜索树 (Balanced Binary Search Tree)** 来存储元素，具体来说是**红黑树 (Red-Black Tree)**。
    *   实际上，`TreeSet` 是通过一个 `NavigableMap<E, Object>` (通常是 `TreeMap<E, Object>`) 来实现的，元素作为 map 的键，值是固定的 `PRESENT` 对象，与 `HashSet` 类似。

*   **核心特性与性能：**
    *   **有序 (Sorted)：** `TreeSet` 中的元素总是处于**排序状态**。
        *   **排序方式：**
            1.  **自然排序 (Natural Ordering)：** 如果存入 `TreeSet` 的元素实现了 `java.lang.Comparable<E>` 接口，并且在构造 `TreeSet` 时没有提供 `Comparator`，那么元素会按照其 `compareTo()` 方法定义的自然顺序进行排序。
            2.  **自定义排序 (Custom Ordering)：** 可以在构造 `TreeSet` 时传入一个 `java.util.Comparator<E>` 实例，元素将按照该比较器定义的顺序进行排序。
        *   **元素“相等性”判断：** `TreeSet` 使用 `compareTo()` (或 `Comparator.compare()`) 方法来判断元素是否“相等”以保证唯一性。如果 `e1.compareTo(e2) == 0` (或 `comparator.compare(e1, e2) == 0`)，`TreeSet` 会认为 `e1` 和 `e2` 是重复元素，即使它们的 `e1.equals(e2)` 可能返回 `false`。**这一点非常重要，也是 `TreeSet` 与依赖 `equals()` 的 `HashSet` 的一个关键区别。**
    *   **性能：**
        *   `add()`, `remove()`, `contains()` 操作的时间复杂度是 **O(log n)**，其中 n 是集合中元素的数量。这是因为红黑树的操作（查找、插入、删除）通常需要对数时间。
        *   相比 `HashSet` 的 O(1)，`TreeSet` 的性能略低，但提供了排序功能。
    *   **不允许 `null` 元素 (默认情况下)：**
        *   如果使用自然排序，尝试添加 `null` 元素会导致 `NullPointerException`，因为 `null` 无法与非 `null` 元素通过 `compareTo()` 比较。
        *   如果使用自定义 `Comparator`，是否允许 `null` 取决于比较器的实现。如果比较器不能处理 `null`，也可能抛异常。
    *   **线程不安全：** `TreeSet` 不是线程安全的。

*   **`NavigableSet<E>` 接口：**
    *   `TreeSet` 实现了 `NavigableSet<E>` 接口 (它继承自 `SortedSet<E>`)，这提供了许多额外的导航和范围查询方法：
        *   `E lower(E e)`: 返回小于 `e` 的最大元素。
        *   `E floor(E e)`: 返回小于或等于 `e` 的最大元素。
        *   `E ceiling(E e)`: 返回大于或等于 `e` 的最小元素。
        *   `E higher(E e)`: 返回大于 `e` 的最小元素。
        *   `E pollFirst()`: 移除并返回第一个 (最小) 元素。
        *   `E pollLast()`: 移除并返回最后一个 (最大) 元素。
        *   `NavigableSet<E> descendingSet()`: 返回此 set 的逆序视图。
        *   `Iterator<E> descendingIterator()`: 返回逆序迭代器。
        *   `NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)`: 返回指定范围的子集视图。
        *   `NavigableSet<E> headSet(E toElement, boolean inclusive)`: 返回小于 (或小于等于) `toElement` 的子集视图。
        *   `NavigableSet<E> tailSet(E fromElement, boolean inclusive)`: 返回大于 (或大于等于) `fromElement` 的子集视图。

*   **构造函数：**
    *   `TreeSet()`: 创建一个空的 `TreeSet`，元素将按自然顺序排序 (元素必须实现 `Comparable`)。
    *   `TreeSet(Comparator<? super E> comparator)`: 创建一个空的 `TreeSet`，元素将按照指定的比较器排序。
    *   `TreeSet(Collection<? extends E> c)`: 创建一个包含指定集合元素的 `TreeSet`，按自然顺序排序。
    *   `TreeSet(SortedSet<E> s)`: 创建一个与指定 `SortedSet` 具有相同元素和相同排序方式的 `TreeSet`。

*   **适用场景：**
    *   当你需要一个**自动保持元素排序**并且不包含重复元素的集合时。
    *   当需要频繁进行范围查询（如查找某个值附近的所有元素）或获取最大/最小元素时。
    *   例如：排行榜、按时间排序的事件列表（去重后）。

---

### `Set` 实现类总结对比

| 特性                | `HashSet<E>`                               | `LinkedHashSet<E>`                             | `TreeSet<E>`                                           |
| ------------------- | ------------------------------------------ | ---------------------------------------------- | ------------------------------------------------------ |
| **底层结构**        | 哈希表 (`HashMap`)                          | 哈希表 + 双向链表 (`LinkedHashMap`)             | 红黑树 (`TreeMap`)                                     |
| **顺序性**          | 无序                                       | 保持插入顺序                                   | 有序 (自然顺序或自定义顺序)                            |
| **性能 (add, remove, contains)** | 平均 O(1)                                  | 平均 O(1) (略慢于 `HashSet`)                  | O(log n)                                               |
| **`null` 元素**     | 允许一个                                   | 允许一个                                       | 默认不允许 (取决于 `Comparator`)                        |
| **唯一性判断依据**  | `equals()` 和 `hashCode()`                   | `equals()` 和 `hashCode()`                       | `compareTo()` 或 `Comparator.compare()` (返回 0 则视为重复) |
| **额外功能**        | 无特殊                                     | 无特殊 (除了顺序)                              | `NavigableSet` 方法 (范围查询、导航)                   |
| **线程安全**        | 否                                         | 否                                             | 否                                                     |

**选择建议：**

*   **如果只需要存储唯一元素，不关心顺序，且追求最高性能：** 选择 `HashSet`。
*   **如果需要存储唯一元素，并希望保持它们的插入顺序：** 选择 `LinkedHashSet`。
*   **如果需要存储唯一元素，并希望它们始终处于排序状态，或者需要执行范围查询：** 选择 `TreeSet`。但要注意其对元素类型（需 `Comparable` 或提供 `Comparator`）和唯一性判断方式的要求。