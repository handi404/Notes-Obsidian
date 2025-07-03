这次我们将聚焦于 **`Queue<E>` 与 `Deque<E>` 接口** 以及它们的一些重要实现类。

队列是一种非常重要的数据结构，常用于任务调度、消息传递、缓冲区管理等场景。

---

### `Queue<E>` 接口：先进先出 (FIFO)

*   **定义与特性：**
    *   `java.util.Queue<E>` 接口继承自 `Collection<E>` 接口。
    *   它代表一个用于在处理之前持有元素的集合，通常（但不一定）以 **FIFO (First-In, First-Out 先进先出)** 的方式对元素进行排序。这意味着元素通常从队列的“尾部”添加，并从队列的“头部”移除。
    *   优先级队列 (`PriorityQueue`) 是一个例外，它根据元素的自然顺序或指定的 `Comparator` 来排序元素，而不是严格的 FIFO。
    *   `Queue` 接口提供了额外的插入、提取和检查操作。

*   **`Queue<E>` 接口的核心方法 (每种操作都有两种形式)：**

| 操作类型       | 抛出异常 | 返回特殊值 | 描述                                                                 |
| -------------- | ------------------------ | --------------------------- | -------------------------------------------------------------------- |
| **插入 (Insert)** | `add(E e)`               | `boolean offer(E e)`        | 将指定元素插入此队列（如果立即可行且不违反容量限制）。`offer` 在有界队列满时返回 `false`，而 `add` 抛 `IllegalStateException`。 |
| **移除 (Remove)** | `E remove()`             | `E poll()`                  | 检索并移除此队列的头部。如果队列为空，`poll` 返回 `null`，而 `remove` 抛 `NoSuchElementException`。 |
| **检查 (Examine)** | `E element()`            | `E peek()`                  | 检索但不移除此队列的头部。如果队列为空，`peek` 返回 `null`，而 `element` 抛 `NoSuchElementException`。 |

*   **选择哪种形式？**
	*   对于**有界队列 (bounded queue)**，即容量有限的队列，推荐使用 `offer()`、`poll()`、`peek()` 这一组方法。因为它们通过返回特殊值（`false` 或 `null`）来优雅地处理队列满或队列空的情况，避免了需要捕获异常的麻烦。
	*   对于**无界队列 (unbounded queue)**，或者当你明确知道操作不会失败时，可以使用 `add()`、`remove()`、`element()`。

*   **常见实现类：**
    *   **`LinkedList<E>`:** (我们之前讨论过) 它实现了 `Deque` 接口，而 `Deque` 继承了 `Queue`，所以 `LinkedList` 也可以用作 FIFO 队列。
        *   `add(e)` / `offer(e)` -> 实际调用 `addLast(e)` / `offerLast(e)`
        *   `remove()` / `poll()` -> 实际调用 `removeFirst()` / `pollFirst()`
        *   `element()` / `peek()` -> 实际调用 `getFirst()` / `peekFirst()`
    *   **`ArrayDeque<E>`:** (稍后会详细介绍) 一个基于可调整大小的数组实现的双端队列，通常比 `LinkedList` 作为队列使用时更高效。
    *   **`PriorityQueue<E>`:** (稍后会详细介绍) 一个基于优先级堆的无界优先级队列。元素按其自然顺序或构造时提供的 `Comparator` 进行排序。
    *   **各种阻塞队列 (Blocking Queues) 实现 (位于 `java.util.concurrent` 包)：** 如 `ArrayBlockingQueue`, `LinkedBlockingQueue`, `SynchronousQueue` 等。它们是线程安全的，并且提供了在队列为空时获取元素或队列满时插入元素时阻塞调用线程的功能。我们会在并发集合部分详细讨论它们。

---

### `Deque<E>` 接口：双端队列 (Double-Ended Queue)

*   **定义与特性：**
    *   `java.util.Deque<E>` (发音通常是 "deck") 接口继承自 `Queue<E>` 接口。
    *   它代表一个**双端队列**，支持在队列的**两端**（头部和尾部）进行元素的插入和移除。
    *   因此，`Deque` 既可以作为传统的 FIFO 队列使用，也可以作为 **LIFO (Last-In, First-Out 后进先出) 栈 (Stack)** 使用。
    *   当用作队列时，行为与 `Queue` 类似 (FIFO)。
    *   当用作栈时，元素从双端队列的“头部”压入 (push) 和弹出 (pop)。

*   **`Deque<E>` 接口的核心方法 (每种操作都有针对头部和尾部的两种形式，且每种形式又有抛异常和返回特殊值两种版本)：**

| 操作类型             | 头部操作 (抛异常)           | 头部操作 (特殊值)                | 尾部操作 (抛异常)          | 尾部操作 (特殊值)               | 描述                                                                          |
| ---------------- | -------------------- | ------------------------- | ------------------- | ------------------------ | --------------------------------------------------------------------------- |
| **插入 (Insert)**  | `void addFirst(E e)` | `boolean offerFirst(E e)` | `void addLast(E e)` | `boolean offerLast(E e)` | 在头部/尾部插入元素。对于有界双端队列，`offer*` 满时返回 `false`，`add*` 抛 `IllegalStateException`。 |
| **移除 (Remove)**  | `E removeFirst()`    | `E pollFirst()`           | `E removeLast()`    | `E pollLast()`           | 检索并移除头部/尾部元素。空时 `poll*` 返回 `null`，`remove*` 抛 `NoSuchElementException`。     |
| **检查 (Examine)** | `E getFirst()`       | `E peekFirst()`           | `E getLast()`       | `E peekLast()`           | 检索但不移除头部/尾部元素。空时 `peek*` 返回 `null`，`get*` 抛 `NoSuchElementException`。       |

*   **`Deque` 作为 `Queue` 使用：**
    *   FIFO 行为可以通过以下映射实现：
        *   `Queue.add(e)` / `offer(e)` -> `Deque.addLast(e)` / `offerLast(e)`
        *   `Queue.remove()` / `poll()` -> `Deque.removeFirst()` / `pollFirst()`
        *   `Queue.element()` / `peek()` -> `Deque.getFirst()` / `peekFirst()`

*   **`Deque` 作为 `Stack` 使用：**
    *   LIFO 行为可以通过以下映射实现 (推荐使用 `Deque` 替代过时的 `java.util.Stack` 类)：
        *   `Stack.push(e)` -> `Deque.addFirst(e)` (或 `push(e)`，`Deque` 接口直接提供了 `push` 方法)
        *   `Stack.pop()` -> `Deque.removeFirst()` (或 `pop()`，`Deque` 接口直接提供了 `pop` 方法)
        *   `Stack.peek()` -> `Deque.getFirst()` (或 `peek()`，`Deque` 接口直接提供了 `peek` 方法)
    *   `Deque` 接口还直接提供了 `push(E e)`, `E pop()`, `E peek()` 这三个方法，它们的语义与传统的栈操作完全对应，并且默认操作的是队列的头部。

*   **迭代顺序：**
    *   `Deque` 的迭代器 (`iterator()`) 通常是从**头部到尾部**遍历元素。
    *   `descendingIterator()` 方法返回一个从尾部到头部遍历的迭代器。

---

### `ArrayDeque<E>`: 高效的双端队列/栈/队列实现

*   **底层数据结构：**
    *   `java.util.ArrayDeque<E>` 使用一个**可调整大小的循环数组 (circular array)** 来实现 `Deque` 接口。
    *   它内部维护 `head` 和 `tail` 指针（或索引）来标记队列的有效元素范围。当元素添加到头部或尾部时，这些指针会相应地在循环数组中移动。
    *   当数组满时，会进行扩容（通常是容量加倍）。

*   **核心特性与性能：**
    *   **高效的头尾操作：** `addFirst()`, `addLast()`, `pollFirst()`, `pollLast()`, `peekFirst()`, `peekLast()` 等操作的**摊销时间复杂度是 O(1)**。因为它们只需要修改指针和在数组中存取元素，只有在扩容时才需要 O(n) 的复制成本。
    *   **无容量限制 (逻辑上)：** `ArrayDeque` 会根据需要自动扩容，所以它在逻辑上是无界的 (实际受限于可用内存和 `Integer.MAX_VALUE`)。
    *   **不允许 `null` 元素：** `ArrayDeque` 不允许插入 `null` 元素，尝试这样做会抛出 `NullPointerException`。这是为了避免 `poll()` 或 `peek()` 返回 `null` 时无法区分是队列为空还是存储了一个 `null` 元素。
    *   **线程不安全：** `ArrayDeque` 不是线程安全的。
    *   **通常比 `LinkedList` 更快：** 对于作为栈或队列使用，`ArrayDeque` 通常比 `LinkedList` 性能更好。这是因为数组操作（特别是顺序访问）具有更好的 CPU 缓存局部性 (cache locality)，并且没有 `LinkedList` 中节点对象的额外开销和间接寻址。

*   **构造函数：**
    *   `ArrayDeque()`: 创建一个初始容量为 16 的空双端队列。
    *   `ArrayDeque(int numElements)`: 创建一个初始容量足以容纳 `numElements` 个元素的空双端队列 (通常是大于等于 `numElements` 的最小 2 的幂，但不小于 8)。
    *   `ArrayDeque(Collection<? extends E> c)`: 创建一个包含指定集合元素的双端队列。

*   **推荐用途：**
    *   **首选的栈实现：** Java 官方文档推荐使用 `ArrayDeque` (或其他 `Deque` 实现) 来代替过时的 `java.util.Stack` 类。
    *   **首选的非阻塞 FIFO 队列实现 (单线程)：** 如果不需要阻塞功能，并且在单线程环境中使用，`ArrayDeque` 是比 `LinkedList` 更高效的队列选择。
    *   用于广度优先搜索 (BFS) 等算法中。

---

### `PriorityQueue<E>`: 基于优先级的队列

*   **底层数据结构：**
    *   `java.util.PriorityQueue<E>` 使用**二叉堆 (binary heap)**，具体来说是**最小堆 (min-heap)** 来实现。这意味着队列的头部总是整个队列中“最小”的元素。

*   **核心特性与性能：**
    *   **优先级排序：** 元素不是按照插入顺序排列，而是根据它们的**优先级**排列。
        *   **优先级确定方式：**
            1.  **自然排序：** 如果元素实现了 `Comparable` 接口，则按其自然顺序。
            2.  **自定义排序：** 如果在构造 `PriorityQueue` 时提供了 `Comparator`，则按比较器定义的顺序。
        *   `peek()`, `poll()`, `remove()` 等方法操作的都是队列中**优先级最高**（即“最小”）的元素。
    *   **性能：**
        *   `offer(e)` (添加元素): **O(log n)** (元素需要上浮到堆的正确位置)。
        *   `poll()` (移除头部元素): **O(log n)** (移除堆顶后，最后一个元素放到堆顶，然后下沉)。
        *   `peek()` (查看头部元素): **O(1)** (直接返回堆顶元素)。
        *   `remove(Object o)` (移除任意元素): **O(n)** (需要线性搜索元素，然后进行堆调整)。
        *   `contains(Object o)`: **O(n)** (需要线性搜索)。
    *   **无界队列：** `PriorityQueue` 是无界的 (逻辑上)，但可以指定初始容量。它会自动扩容。
    *   **不允许 `null` 元素 (默认)：** 与 `TreeSet` 类似，如果使用自然排序或比较器不支持 `null`，则不能添加 `null`。
    *   **线程不安全：** `PriorityQueue` 不是线程安全的。如果需要线程安全的优先级队列，可以使用 `java.util.concurrent.PriorityBlockingQueue`。
    *   **迭代顺序不保证：** `PriorityQueue` 的迭代器 (`iterator()`) **不保证**以任何特定顺序（包括优先级顺序）遍历元素。如果你需要按优先级顺序访问所有元素，应该重复调用 `poll()`。

*   **构造函数：**
    *   `PriorityQueue()`: 初始容量 11，按自然顺序。
    *   `PriorityQueue(int initialCapacity)`: 指定初始容量，按自然顺序。
    *   `PriorityQueue(Comparator<? super E> comparator)`: 初始容量 11，按指定比较器。
    *   `PriorityQueue(int initialCapacity, Comparator<? super E> comparator)`
    *   `PriorityQueue(Collection<? extends E> c)`: 从集合创建，如果集合是 `SortedSet` 或 `PriorityQueue`，则使用其顺序，否则按自然顺序。
    *   `PriorityQueue(PriorityQueue<? extends E> c)`
    *   `PriorityQueue(SortedSet<? extends E> c)`

*   **适用场景：**
    *   任务调度（按任务优先级处理）。
    *   事件模拟（按事件发生时间处理）。
    *   图算法（如 Dijkstra 算法、Prim 算法中的优先队列）。
    *   查找第 K 大/小元素（Top K 问题）。

---

**`java.util.Stack<E>` (过时，不推荐使用)**

*   `Stack` 类是 JDK 1.0 时期引入的，它继承自 `Vector<E>` (一个线程安全但性能较低的 `ArrayList` 版本)。
*   **设计缺陷：**
    *   继承 `Vector` 意味着 `Stack` 拥有了 `Vector` 的所有公共方法（如 `get(index)`, `add(index, element)` 等），这破坏了栈的 LIFO 抽象。
    *   `Vector` 的方法都是 `synchronized` 的，导致不必要的性能开销，即使在单线程环境中使用 `Stack`。
*   **替代方案：** 如前所述，**强烈推荐使用任何 `Deque` 实现 (特别是 `ArrayDeque`) 来代替 `Stack`**。
    ```java
    // 不推荐
    // Stack<String> oldStack = new Stack<>();
    // oldStack.push("A");
    // String top = oldStack.pop();
    
    // 推荐
    Deque<String> newStack = new ArrayDeque<>();
    newStack.push("A"); // 等同于 newStack.addFirst("A");
    String topModern = newStack.pop(); // 等同于 newStack.removeFirst();
    ```

**总结与重要性：**

*   `Queue` 定义了 FIFO 集合的基本操作，`Deque` 在此基础上扩展为双端操作，使其既能作队列也能作栈。
*   `ArrayDeque` 是作为栈和非阻塞队列的现代、高效选择，通常优于 `LinkedList` (用于此目的) 和过时的 `Stack`。
*   `PriorityQueue` 提供了基于元素优先级的出队顺序，对于特定算法和应用场景非常有用。
*   理解不同队列实现的底层数据结构和性能特点，有助于在具体场景下做出正确的选择。

---

至此，`Collection` 接口的主要分支 (`List`, `Set`, `Queue/Deque`) 都已覆盖。