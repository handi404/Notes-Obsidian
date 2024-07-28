//插值搜索=对二分搜索的改进，最适用于“均匀”分布的数据 
// 根据计算的探测结果“猜测”某个值可能在哪里 
// 如果探测不正确，则缩小搜索范围，并计算新的探测 
// 平均情况：O(log(log(n))) 
// 最坏情况：O(n) [值呈指数增长]

---

```java
public class Main{
			
    public static void main(String args[]){   
 
    	int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    	
    	int index = interpolationSearch(array, 8);
    	
    	if(index != -1) {
    		System.out.println("Element found at index: "+ index);
    	}
    	else {
    		System.out.println("Element not found");
    	}
    }

	private static int interpolationSearch(int[] array, int value) {
		
		int high = array.length - 1;
		int low = 0;
		
		while(value >= array[low] && value <= array[high] && low <= high) {
			
			int probe = low + (high - low) * (value - array[low]) / 
					    (array[high] - array[low]);
			
			
			//System.out.println("probe: " + probe);
			
			if(array[probe] == value) {
				return probe;
			}
			else if(array[probe] < value) {
				low = probe + 1;
			}
			else {
				high = probe -1;
			}
		}
		
		return -1;
	}
}
```

## 插值搜索：比二分查找更智能的搜索算法

### 什么是插值搜索？

插值搜索（Interpolation Search）是一种基于插值思想的查找算法，它在有序数组中查找元素时，比二分查找更能利用数组中元素分布的信息。**二分查找**每次都取中间位置进行比较，而**插值搜索**则会根据要查找的元素与数组两端元素的关系，计算出一个更接近目标元素的索引位置进行比较。

### 插值搜索的原理

假设我们要在有序数组 nums 中查找目标值 target。插值搜索的思路如下：

1. **计算中间索引 mid：**
    
    ```cpp
	mid = left + ((target - nums[left]) * (right - left)) / (nums[right] - nums[left])
    ```
    
    其中：
    - left：搜索范围的左边界
    - right：搜索范围的右边界
    - nums[left]：左边界元素的值
    - nums[right]：右边界元素的值
2. **比较 mid 处的元素与 target：**
    - 如果 nums[mid] == target，则找到目标元素。
    - 如果 nums[mid] < target，则目标元素在 mid 的右侧。
    - 如果 nums[mid] > target，则目标元素在 mid 的左侧。
3. **调整搜索范围：** 根据比较结果，调整 left 或 right，缩小搜索范围，重复步骤 1 和 2。

### 插值搜索与二分查找的区别

|特点|二分查找|插值搜索|
|---|---|---|
|**中间位置计算**|始终取中间位置|根据目标值与左右边界元素的关系，计算一个更接近目标元素的位置|
|**性能**|平均情况下比插值搜索慢|当数据分布均匀时，通常比二分查找快；但当数据分布不均匀时，性能可能退化为线性搜索|
|**适用场景**|适用于任何有序数组|更适合数据分布均匀的有序数组|

### 插值搜索的代码示例（C++）

```C++
int interpolationSearch(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;

    while (left <= right && target >= nums[left] && target <= nums[right]) {
        int mid = left + ((target - nums[left]) * (right - left)) / (nums[right] - nums[left]);

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return -1;
}
```

### 插值搜索的优缺点

- **优点：**
    - 当数据分布均匀时，插值搜索的性能通常优于二分查找。
    - 充分利用了数据分布的信息，提高了查找效率。
- **缺点：**
    - 当数据分布不均匀时，插值搜索的性能可能退化为线性搜索。
    - 实现相对复杂。

### 总结

插值搜索是一种比二分查找更智能的搜索算法，但在实际应用中，需要根据数据的分布情况来选择合适的算法。如果数据分布较为均匀，插值搜索是一个不错的选择；如果数据分布不均匀或者对算法的稳定性要求较高，二分查找可能更适合。

**什么时候使用插值搜索？**

- 数据分布较为均匀的有序数组
- 需要更高效的查找算法
- 对算法的实现复杂度不敏感

**什么时候使用二分查找？**

- 对算法的稳定性要求较高
- 数据分布不均匀
- 算法的实现简单易懂