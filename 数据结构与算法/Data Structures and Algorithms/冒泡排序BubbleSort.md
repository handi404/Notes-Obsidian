// 气泡排序 = 相邻元素对进行比较，如果不按顺序排列，则对调元素。
// 如果不按顺序排列，则对调。
	
// 二次方时间 O(n^2)
// 小数据集 = 还行
// 大数据集 = 糟糕（请不要这样做）

---

```java
public class Main{

	public static void main(String[] args) {
		
		int array[] =  {9, 1, 8, 2, 7, 3, 6, 4, 5};
		
		bubbleSort(array);
		
		for(int i : array) {
			System.out.print(i);
		}
	}
	
	public static void bubbleSort(int array[]) {
		
		for(int i = 0; i < array.length - 1; i++) {
			for(int j = 0; j < array.length - i - 1; j++) {
				if(array[j] > array[j+1]) {
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
	}
}
```