// 二分查找 = 找到位置的搜索算法 
//                    排序数组中的目标值。 
//                    在每个“步骤”期间，数组的一半被消除

---

```java
import java.util.Arrays;

public class Main{
	public static void main(String[] args) {
		
		int array[] = new int[1000000];
		int target = 777777;
		
		for(int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		//内置的二分查找
		//int index = Arrays.binarySearch(array, target);
		int index = binarySearch(array, target);
		
		if(index == -1) {
			System.out.println(target + " not found");
		}
		else {
			System.out.println("Element found at: " + index);
		}
		
	}

	private static int binarySearch(int[] array, int target) {
		
		int low = 0;
		int high = array.length - 1;
		
		while(low <= high) {
			
			int middle = low + (high - low) / 2;
			int value = array[middle];
			
			System.out.println("middle: " + value);
			
			if(value < target) low = middle + 1;
			else if(value > target) high = middle - 1;
			else return middle; //target found
		}
		
		return -1;
	}
}
```

## 二分查找算法详解

### 什么是二分查找？

二分查找（Binary Search）是一种高效的查找算法，它只适用于 **有序** 的数组。其核心思想是每次将搜索范围缩小一半，直到找到目标元素或搜索范围为空。

### 二分查找的步骤

1. **确定搜索范围：** 初始时，搜索范围是整个数组。
2. **找到中间元素：** 计算数组的中间索引，获取中间元素。
3. **比较中间元素与目标值：**
    - 如果中间元素等于目标值，则查找成功，返回索引。
    - 如果中间元素大于目标值，则目标元素（如果存在）一定在左半部分，将搜索范围缩小为左半部分。
    - 如果中间元素小于目标值，则目标元素（如果存在）一定在右半部分，将搜索范围缩小为右半部分。
4. **重复步骤 2 和 3：** 继续在缩小的范围内重复上述步骤，直到找到目标元素或搜索范围为空。

### 代码示例（C++）

```C++
#include <vector>

int binarySearch(std::vector<int>& nums, int target) {
    int left = 0;
    int right = nums.size() - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2; //防止整数溢出

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return -1; // 未找到
}
```

### 关键点

- **数组必须有序：** 二分查找的前提是数组是有序的。
- **中间元素的计算：** 使用 `mid = left + (right - left) / 2` 的方式可以防止整数溢出。
- **边界条件：** 注意 `left` 和 `right` 的初始值和循环条件，以避免数组越界。
- **等号的位置：** `left <= right` 的循环条件保证了当 `left == right` 时，还会进行一次比较，确保不会漏掉目标元素。

### 二分查找的应用场景

- 在有序数组中查找元素
- 查找一个数的左侧边界或右侧边界
- 寻找峰值
- 判定一个数是否存在

### 二分查找的变形

- **寻找左侧边界：** 当找到目标值时，继续向左查找，直到找到第一个等于目标值的元素。
- **寻找右侧边界：** 当找到目标值时，继续向右查找，直到找到第一个大于目标值的元素。

### 注意事项

- 二分查找的时间复杂度为 O(log n)，效率很高。
- 二分查找的实现细节有很多，不同的实现方式可能略有差异。
- 在实际应用中，需要根据具体情况选择合适的二分查找变种。

### 总结

二分查找是一种非常重要的算法，广泛应用于各种编程问题中。掌握二分查找的原理和实现细节，对于提高算法能力非常有帮助。