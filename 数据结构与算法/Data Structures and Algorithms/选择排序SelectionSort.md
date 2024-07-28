// 选择排序 = 在数组中搜索，并在每次迭代过程中跟踪最小值。
// 每次迭代。在每次迭代结束时，我们交换值。
	
// 二次方时间 O(n^2)
// 小数据集 = OK
// 大数据集 = 差

---

```java
public class Main{

	public static void main(String[] args) {
		
		int array[] = {8, 7, 9, 2, 3, 1, 5, 4, 6};
		
		selectionSort(array);
		
		for(int i : array) {
			System.out.print(i);
		}	
	}

	private static void selectionSort(int[] array) {
		
		for(int i = 0; i < array.length - 1; i++) {
			int min = i;
			for(int j = i + 1; j < array.length; j++) {
				if(array[min] > array[j]) {
					min = j;
				}
			}
			
			int temp = array[i];
			array[i] = array[min];
			array[min] = temp;
		}
		
	}
}
```