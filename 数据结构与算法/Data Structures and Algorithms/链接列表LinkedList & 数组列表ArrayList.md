```java
import java.util.ArrayList;
import java.util.LinkedList;

public class Main{
	
	public static void main(String[] args) {

		LinkedList<Integer> linkedList = new LinkedList<Integer>();
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		long startTime;
		long endTime;
		long elapsedTime;
		
		for(int i = 0; i < 1000000; i++){
			linkedList.add(i);
			arrayList.add(i);
		}
					
		// ****************LinkedList****************
		startTime = System.nanoTime();
		
		linkedList.get(0);
		//linkedList.get(500000);
		//linkedList.get(999999);
		//linkedList.remove(0);
		//linkedList.remove(500000);
		//linkedList.remove(999999);
		
		endTime = System.nanoTime();
		
		elapsedTime = endTime - startTime;
		
		System.out.println("LinkedList:\t" + elapsedTime +" ns");
		
		// ****************ArrayList****************	
		
		startTime = System.nanoTime();
		
		arrayList.get(0);
		//arrayList.get(500000);
		//arrayList.get(999999);
		//arrayList.remove(0);
		//arrayList.remove(500000);
		//arrayList.remove(999999);
		
		endTime = System.nanoTime();
		
		elapsedTime = endTime - startTime;
		
		System.out.println("ArrayList:\t" + elapsedTime +" ns");
				
	}
}
```

详细对比一下 LinkedList 和 ArrayList 这两种常用的 Java 集合类：
### LinkedList 与 ArrayList 的对比

|特点|LinkedList|ArrayList|
|---|---|---|
|**底层实现**|双向链表|动态数组|
|**随机访问**|慢|快|
|**插入删除**|快|慢（尤其是中间位置）|
|**内存占用**|每个元素都存储前驱和后继节点的引用，开销较大|数组连续存储，内存利用率高|
|**扩容**|插入时动态增加节点，没有固定的扩容机制|数组容量不够时，会创建一个新的数组，并将原数组中的元素复制到新数组中|
|**线程安全**|非线程安全|非线程安全|

### 总结

- **LinkedList** 更适合频繁的插入、删除操作，以及需要按照顺序遍历元素的场景。例如：栈、队列、链表等数据结构的实现。
- **ArrayList** 更适合随机访问元素，以及元素数量相对稳定、不需要频繁插入删除的场景。例如：作为普通的列表存储数据。

### 具体选择建议

- **频繁插入删除操作**：选择 LinkedList。
- **随机访问元素**：选择 ArrayList。
- **元素数量变化大**：选择 LinkedList，因为其插入删除操作开销较小。
- **内存空间有限**：选择 ArrayList，因为其内存利用率较高。
- **线程安全**：如果需要线程安全，可以考虑使用它们的线程安全版本：LinkedList 和 CopyOnWriteArrayList。

### 示例代码

```Java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        Li   1. zkread.com zkread.comst<Integer> linkedList = new LinkedList<>();
        List<Integer> arrayList = new ArrayList   1. github.com github.com<>();

        // 向列表中添加元素
        for (int i = 0; i < 10; i++) {
            linkedList.add(i);
            arrayList.add(i);
        }

        // 获取元素
        int index = 5;
        System.out.println("LinkedList中第" + index + "个元素：" + linkedList.get(index));
        System.out.println("ArrayList中第" + index + "个元素：" + arrayList.get(index));

        // 插入元素
        linkedList.add(2, 100);
        arrayList.add(2, 100);

        // 删除元素
        linkedList.remove(2);
        arrayList.remove(2);
    }
}
```
### 总结

选择合适的 List 实现类对于提高程序性能和可读性非常重要。在实际开发中，需要根据具体的业务场景和性能要求来进行选择。