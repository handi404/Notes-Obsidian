	    // 哈希表 = 一种存储唯一键值的数据结构，例如：<整数，字符串>。
	    // 每个键/值对称为一个条目
	    // 快速插入、查找和删除键/值对
    	// 不适合小型数据集，但适合大型数据集
    	
    	// 散列 = 获取一个键并计算一个整数（公式会根据键和数据类型而变化）
    	// 在哈希表中，我们使用哈希%容量来计算索引号 
    	
    	// key.hashCode() % capacity = index  
    	
    	// 桶 = 一个或多个条目的索引存储位置
    	// 在发生碰撞的情况下，可以存储多个条目（链接方式类似于链接列表）
    	
    	// 碰撞 = 哈希函数为一个以上的密钥生成相同的索引
    	// 减少碰撞 = 提高效率
    	
    	// 运行时复杂度： 最佳情况 O(1)
    	// 最差情况 O(n)

---

```java
import java.util.*;

public class Main{
	
    public static void main(String args[]) {    	

    	Hashtable<Integer, String> table = new Hashtable<>(10);
    	
    	table.put(100, "Spongebob");
    	table.put(123, "Patrick");
    	table.put(321, "Sandy");
    	table.put(555, "Squidward");
    	table.put(777, "Gary");
    	  	
    	for(Integer key : table.keySet()) {
    		System.out.println(key.hashCode() % 10 + "\t" + key + "\t" + table.get(key));
    	}
    }
}
```