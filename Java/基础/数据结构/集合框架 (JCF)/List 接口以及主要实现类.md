核心部分——**`List<E>` 接口**以及它的两个主要实现类：`ArrayList<E>` 和 `LinkedList<E>`。

`List` 是我们日常编程中使用频率最高的集合类型之一。

---

### `List<E>` 接口：有序、可重复

*   **定义与特性：**
    *   `java.util.List<E>` 接口继承自 `Collection<E>` 接口。
    *   它代表一个**有序的 (ordered)** 集合，也称为**序列 (sequence)**。
    *   **有序：** 元素在 `List` 中有明确的插入顺序，并且这个顺序会被保持。当你迭代 `List` 时，元素会按照它们被插入的顺序出现（除非 `List` 被显式地重新排序）。
    *   **可重复：** `List` 允许存储重复的元素。你可以向一个 `List` 中添加多个内容相同（根据 `equals()` 判断）的对象。
    *   **基于索引的访问：** `List` 接口的核心特性是它允许通过**整数索引 (位置)** 来访问和操作元素，这与数组非常相似。索引从 `0` 开始。

*   **`List<E>` 接口在 `Collection<E>` 基础上新增/覆盖的核心方法：**

    1.  **位置访问 (Positional Access)：**
        *   `E get(int index)`: 返回列表中指定位置的元素。
        *   `E set(int index, E element)`: 用指定的元素替换列表中指定位置的元素，并返回被替换的旧元素。
        *   `void add(int index, E element)`: 在列表的指定位置插入指定的元素，后续元素向后移动。
        *   `E remove(int index)`: 移除列表中指定位置的元素，并返回被移除的元素，后续元素向前移动。

    2.  **搜索操作 (Search Operations)：**
        *   `int indexOf(Object o)`: 返回此列表中第一次出现的指定元素的索引；如果列表不包含此元素，则返回 -1。比较时使用 `o.equals(element)`。
        *   `int lastIndexOf(Object o)`: 返回此列表中最后一次出现的指定元素的索引；如果列表不包含此元素，则返回 -1。

    3.  **列表迭代器 (List Iterator)：**
        *   `ListIterator<E> listIterator()`: 返回此列表元素的列表迭代器 (从列表的开头开始)。
        *   `ListIterator<E> listIterator(int index)`: 返回此列表元素的列表迭代器 (从列表的指定位置开始)。
        *   **`ListIterator<E>` 接口：**
            *   继承自 `Iterator<E>`。
            *   除了 `Iterator` 的 `hasNext()`, `next()`, `remove()` 方法外，还增加了：
                *   `boolean hasPrevious()`: 如果反向迭代列表，列表迭代器有多个元素，则返回 `true`。
                *   `E previous()`: 返回列表中的前一个元素，并将光标位置向后移动。
                *   `int nextIndex()`: 返回后续调用 `next()` 所返回元素的索引。
                *   `int previousIndex()`: 返回后续调用 `previous()` 所返回元素的索引。
                *   `void set(E e)`: 用指定元素替换 `next()` 或 `previous()` 最后返回的元素 (可选操作)。
                *   `void add(E e)`: 将指定的元素插入列表，插入位置在 `next()` 将返回的元素之前，或者 `previous()` 将返回的元素之后 (可选操作)。
            *   `ListIterator` 允许双向遍历列表，并且可以在迭代期间修改列表 (添加、移除、设置元素)。

    4.  **子列表视图 (Sublist View)：**
        *   `List<E> subList(int fromIndex, int toIndex)`: 返回列表中指定的 `fromIndex` (包含)和 `toIndex` (不包含)之间的部分视图。
        *   **重要特性：** 返回的 `List` 是**原始列表的视图 (view)，而不是一个独立的副本**。对子列表的任何结构性修改 (如添加、删除元素) 都会反映在原始列表中，反之亦然。对子列表的非结构性修改 (如 `set()`) 也会影响原始列表。
        *   如果在获取子列表后，原始列表发生了结构性修改（通过原始列表自身的方法，而不是通过子列表），那么对子列表的后续操作行为是未定义的 (通常会导致 `ConcurrentModificationException`)。

    5.  **`equals()` 和 `hashCode()` 的约定：**
        *   `List` 接口对其 `equals()` 和 `hashCode()` 方法有严格的约定：
            *   如果两个列表包含相同顺序的相同元素（根据元素的 `equals()` 方法判断），则它们是 `equals()` 的。
            *   `hashCode()` 的计算也必须基于列表中元素的顺序和它们的 `hashCode()`。

    6.  **Java 8+ `default void sort(Comparator<? super E> c)`:**
        *   使用指定的 `Comparator` 对此列表进行排序。如果 `c` 为 `null`，则使用元素的自然顺序 (元素必须实现 `Comparable`)。
        *   这个方法通常会直接修改列表本身。

    7.  **Java 9+ 静态工厂方法 `List.of(...)`:**
        *   `static <E> List<E> of()`
        *   `static <E> List<E> of(E e1)`
        *   `static <E> List<E> of(E e1, E e2, ... E e10)` (多个重载版本，最多 10 个元素)
        *   `static <E> List<E> of(E... elements)` (可变参数版本)
        *   这些方法用于创建**不可变的 (immutable)** `List` 实例。
        *   **特性：**
            *   返回的列表是不可变的：不能添加、删除、替换元素 (会抛 `UnsupportedOperationException`)。
            *   不允许 `null` 元素 (会抛 `NullPointerException`)。
            *   通常比创建可变列表然后用 `Collections.unmodifiableList()` 包装更高效，内存占用也可能更少。
            *   返回的实际类型是 JDK 内部的特定不可变实现，不是 `ArrayList` 或 `LinkedList`。

---

### `ArrayList<E>`: 基于动态数组的实现

*   **底层数据结构：** `ArrayList` 内部使用一个**动态数组 (Object[])** 来存储元素。这个数组的名称通常是 `elementData`。

*   **核心特性与性能：**
    *   **随机访问高效 (O(1))：** 由于底层是数组，通过索引 `get(index)` 和 `set(index, element)` 操作非常快，时间复杂度是 O(1)。
    *   **尾部添加/删除摊销 O(1)：**
        *   `add(E element)` (添加到末尾): 如果数组还有空间，时间复杂度是 O(1)。如果数组已满，需要进行**扩容 (grow)**，这涉及到创建一个更大的新数组并将旧数组元素复制到新数组，这个操作是 O(n) 的。但是，由于扩容不是每次添加都发生，经过**摊销分析 (amortized analysis)**，平均下来尾部添加的复杂度是 O(1)。
    *   **中间插入/删除低效 (O(n))：**
        *   `add(int index, E element)` (在中间插入) 或 `remove(int index)` (从中间移除)：这些操作需要移动索引之后的所有元素，平均时间复杂度是 O(n)。
    *   **空间占用：**
        *   `ArrayList` 会预留一些额外的空间以备后续添加，所以实际占用的内存可能比存储的元素多。
        *   可以通过构造函数指定初始容量 `initialCapacity`，或者使用 `trimToSize()` 方法来减少底层数组到实际元素数量的大小。
    *   **线程不安全：** `ArrayList` 不是线程安全的。如果在多线程环境中使用，必须进行外部同步（例如使用 `Collections.synchronizedList(new ArrayList<>())`）或者使用并发集合如 `CopyOnWriteArrayList`。

*   **扩容机制 (Grow Mechanism)：**
    *   当调用 `add()` 方法且当前 `elementData` 数组已满时，`ArrayList` 会进行扩容。
    *   JDK 8 及以后的典型扩容策略是：**新容量 = 旧容量 + (旧容量 >> 1)**，即新容量大约是旧容量的 1.5 倍。 (例如，10 -> 15, 15 -> 22, 22 -> 33)
    *   如果计算出的新容量仍然不足以容纳添加的元素（例如，批量添加 `addAll`），则新容量会调整为至少满足需求的最小容量。
    *   扩容是一个相对耗时的操作，因为它涉及内存分配和数组复制。合理设置初始容量可以减少不必要的扩容次数。
        ```java
        // ArrayList 源码片段 (简化示意)
        // private Object[] elementData;
        // private int size;
        // private static final int DEFAULT_CAPACITY = 10;

        // private void grow(int minCapacity) {
        //     int oldCapacity = elementData.length;
        //     int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5倍
        //     if (newCapacity - minCapacity < 0)
        //         newCapacity = minCapacity;
        //     if (newCapacity - MAX_ARRAY_SIZE > 0) // MAX_ARRAY_SIZE 是 Integer.MAX_VALUE - 8 左右
        //         newCapacity = hugeCapacity(minCapacity);
        //     elementData = Arrays.copyOf(elementData, newCapacity);
        // }
        ```

*   **构造函数：**
    *   `ArrayList()`: 创建一个初始容量为 10 的空列表。
    *   `ArrayList(int initialCapacity)`: 创建一个具有指定初始容量的空列表。
        *   如果 `initialCapacity` 为 0，会使用一个共享的空数组实例 `EMPTY_ELEMENTDATA`。第一次添加元素时会扩容到默认容量 (通常是 10，除非第一次添加的是一个集合)。
    *   `ArrayList(Collection<? extends E> c)`: 创建一个包含指定集合元素的列表，元素的顺序由集合的迭代器返回的顺序决定。初始容量是 `c.size()` 的 110% (JDK 8) 或略大于 `c.size()`。

*   **适用场景：**
    *   **读多写少**的场景，特别是需要频繁进行随机访问 (通过索引 `get()`) 的情况。
    *   当元素数量相对稳定，或者可以预估元素数量以设置合适的初始容量时。
    *   作为方法返回值或局部变量存储一组数据时非常常用。

*   **`subList()` 的特殊性：**
    *   `ArrayList` 的 `subList()` 方法返回的是 `ArrayList` 内部的一个名为 `SubList` 的私有类实例。
    *   这个 `SubList` 实例**直接操作原始 `ArrayList` 的 `elementData` 数组**，通过偏移量和大小来界定子列表范围。
    *   因此，对 `SubList` 的修改会直接影响原始 `ArrayList`。
    *   **`ConcurrentModificationException` 风险：** 如果在创建 `SubList` 后，原始 `ArrayList` 通过其自身的方法（而不是通过 `SubList`）进行了结构性修改 (增删元素导致 `modCount` 变化)，那么对这个 `SubList` 的任何后续操作（如 `get`, `size`, `add`）都会抛出 `ConcurrentModificationException`。这是因为 `SubList` 内部也会检查 `modCount`。

    ```java
    ArrayList<String> mainList = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
    List<String> sub = mainList.subList(1, 3); // sub 包含 "b", "c"

    System.out.println("MainList: " + mainList); // [a, b, c, d, e]
    System.out.println("SubList: " + sub);     // [b, c]

    sub.set(0, "X"); // 修改 sub，会影响 mainList
    System.out.println("MainList after sub.set: " + mainList); // [a, X, c, d, e]
    System.out.println("SubList after sub.set: " + sub);     // [X, c]

    sub.add("Y"); // 通过 sub 添加，也会影响 mainList
    System.out.println("MainList after sub.add: " + mainList); // [a, X, Y, c, d, e]
    System.out.println("SubList after sub.add: " + sub);     // [X, Y, c] (sub 范围也变了)

    // !! 危险操作 !!
    // mainList.add("Z"); // 通过 mainList 进行结构性修改
    // System.out.println(sub.get(0)); // 此时会抛出 ConcurrentModificationException
    ```

---

### `LinkedList<E>`: 基于双向链表的实现

*   **底层数据结构：** `LinkedList` 内部使用一个**双向链表 (doubly linked list)** 来存储元素。每个节点 (Node) 包含元素本身、一个指向前一个节点的引用 (prev) 和一个指向后一个节点的引用 (next)。它还会维护 `first` 和 `last` 节点引用以及 `size`。

    ```java
    // LinkedList.Node 内部类简化示意
    // private static class Node<E> {
    //     E item;
    //     Node<E> next;
    //     Node<E> prev;
    //     Node(Node<E> prev, E element, Node<E> next) { ... }
    // }
    // transient Node<E> first;
    // transient Node<E> last;
    // transient int size = 0;
    ```

*   **核心特性与性能：**
    *   **插入和删除高效 (O(1) 如果有引用，否则 O(n) 查找 + O(1) 操作)：**
        *   在链表的头部 (`addFirst`, `removeFirst`) 或尾部 (`addLast`, `removeLast`) 进行插入和删除操作的时间复杂度是 O(1)，因为可以直接操作 `first` 或 `last` 节点。
        *   `ListIterator` 的 `add()` 和 `remove()` 方法也是 O(1) 的，因为它持有当前节点的引用。
        *   通过索引 `add(int index, E element)` 或 `remove(int index)`，首先需要遍历链表找到指定索引的节点，这个查找过程平均是 O(n) (最坏 O(n)，`LinkedList` 会从头或尾开始查找，取决于索引离哪端更近)，然后实际的节点插入/删除是 O(1)。所以整体是 O(n)。
    *   **随机访问低效 (O(n))：**
        *   `get(int index)` 和 `set(int index, E element)` 操作需要从头或尾开始遍历链表直到找到指定索引的节点，平均时间复杂度是 O(n)。
    *   **空间占用：**
        *   除了存储元素本身，每个元素还需要额外的空间来存储前后节点的引用，所以 `LinkedList` 的内存开销通常比 `ArrayList` 大。
    *   **线程不安全：** `LinkedList` 也不是线程安全的。

*   **`Deque<E>` 接口的实现：**
    *   `LinkedList` 同时实现了 `List<E>` 和 `Deque<E>` (双端队列) 接口。
    *   因此，`LinkedList` 不仅可以用作列表，还可以高效地用作**栈 (Stack)** 或**队列 (Queue)**。
    *   作为栈：`push()` (等同于 `addFirst()`), `pop()` (等同于 `removeFirst()`), `peek()` (等同于 `peekFirst()`)。
    *   作为队列：`offer()` (等同于 `offerLast()` / `addLast()`), `poll()` (等同于 `pollFirst()` / `removeFirst()`), `peek()` (等同于 `peekFirst()`)。
    *   **推荐使用 `ArrayDeque` 作为栈和队列**，如果不需要 `List` 的功能，因为 `ArrayDeque` 通常性能更好（基于循环数组）。但如果需要在列表操作和队列/栈操作之间切换，`LinkedList` 是一个方便的选择。

*   **构造函数：**
    *   `LinkedList()`: 创建一个空列表。
    *   `LinkedList(Collection<? extends E> c)`: 创建一个包含指定集合元素的列表。

*   **适用场景：**
    *   **写多读少**的场景，特别是需要频繁在列表的**头部或尾部**进行插入和删除操作。
    *   当需要同时具备 `List` 和 `Deque` (栈或队列) 的功能时。
    *   当对内存占用不是非常敏感，且随机访问性能不是主要瓶颈时。
    *   `ListIterator` 的 `add()` 和 `remove()` 操作非常高效。

---

### `ArrayList` vs `LinkedList` 总结对比

| 特性             | `ArrayList<E>`                                    | `LinkedList<E>`                                      |
| ---------------- | ------------------------------------------------- | ----------------------------------------------------- |
| **底层结构**     | 动态数组                                          | 双向链表                                              |
| **随机访问 (get/set)** | **O(1)**                                       | O(n)                                                  |
| **头部插入/删除**  | O(n)                                              | **O(1)**                                           |
| **尾部插入/删除**  | 摊销 O(1) (无扩容 O(1), 扩容 O(n))                 | **O(1)**                                           |
| **中间插入/删除**  | O(n)                                              | O(n) (查找 O(n) + 操作 O(1))                          |
| **`ListIterator` 增删** | O(n) (因元素移动)                            | **O(1)**                                           |
| **内存占用**     | 相对较小，但可能有预留空间                        | 相对较大 (每个节点有额外指针开销)                     |
| **实现接口**     | `List`, `RandomAccess`, `Cloneable`, `Serializable` | `List`, `Deque`, `Cloneable`, `Serializable`          |
| **`RandomAccess`** | 是 (标记接口，表示支持快速随机访问)                | 否                                                    |
| **适用场景**     | 读多写少，频繁随机访问                            | 写多读少，频繁头尾操作，或用作栈/队列                 |
| **线程安全**     | 否                                                | 否                                                    |

**`RandomAccess` 接口：**

*   这是一个**标记接口 (marker interface)**，没有方法。
*   如果一个 `List` 实现类实现了 `RandomAccess`，它表明该列表支持快速（通常是 O(1) 时间）的随机访问。
*   `ArrayList` 实现了 `RandomAccess`，而 `LinkedList` 没有。
*   `Collections` 类的某些算法（如 `binarySearch`，或某些内部循环）会检查列表是否 `instanceof RandomAccess`，如果是，则使用基于索引的循环；否则，使用迭代器进行遍历，以获得更好的性能。

**选择建议：**

*   **默认情况下，优先考虑 `ArrayList`。** 因为它的随机访问性能好，并且 CPU 缓存友好性通常也更好（由于数组的连续内存）。大多数场景下，读操作比写操作更频繁。
*   **只有当你有非常明确的理由**（例如，大量的头尾增删，或者需要 `Deque` 功能且与 `List` 功能混合使用），才选择 `LinkedList`。
*   **不要过早优化。** 如果不确定，从 `ArrayList` 开始，如果性能分析显示瓶颈在列表操作上，再考虑是否更换为 `LinkedList` 或其他数据结构。
*   对于**栈或队列**的需求，如果不需要 `List` 的方法，**`ArrayDeque` 通常是比 `LinkedList` 更好的选择**，因为它基于循环数组，性能和内存效率往往更高。