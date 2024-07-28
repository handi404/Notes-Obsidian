// 插入排序 = 将元素向左比较后、
// 将元素向右移动，以腾出空间插入数值
	
// 二次方时间 O(n^2)
// 小数据集 = 体面
// 大数据集 = 糟糕
	
// 比气泡排序的步骤少
// 最佳情况下为 O(n)，而选择排序为 O(n^2)

---

```java
public class Main{

	public static void main(String[] args) {
		
		int array[] = {9, 1, 8, 2, 7, 3, 6, 5, 4};
		
		insertionSort(array);
		
		for(int i : array) {
			System.out.print(i + " ");
		}
	}

	private static void insertionSort(int[] array) {
		
		for(int i = 1; i < array.length; i++) {
			int temp = array[i];
			int j = i - 1;
			
			while(j >= 0 && array[j] > temp) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = temp;
		}
	}
}
```