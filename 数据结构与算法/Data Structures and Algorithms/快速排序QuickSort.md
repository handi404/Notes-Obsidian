// 快速排序 = 将较小的元素移动到枢轴的左侧。
// 递归地将数组分为 2 个分区

// 运行时复杂度 = 最佳情况 O (n log (n))
// 平均情况 O (n log (n))
// 如果已经排序，最坏情况 O (n^2)

// 由于递归，空间复杂度 = O (log (n))

---
==以数组 end 为枢轴，i 为-1 j 为 0。j 向右走，若 arry[j]小于枢轴，则 i++并将 arr[j]与 arr[i]交换==
==当 j走到 end 时依旧 i++并将 arr[j]与 arr[i]交换。此时 i 为数轴位置。后将枢轴左右分别进行如此排序，再往后就以此类推......==

```java
public class Main{
	
    public static void main(String args[])
    {
        int[] array = {8, 2, 5, 3, 9, 4, 7, 6, 1};
        
        quickSort(array, 0, array.length - 1);
        
        for(int i : array){
            System.out.print(i + " ");
        }
    }

	private static void quickSort(int[] array, int start, int end) {
		
		if(end <= start) return; //base case
		
		int pivot = partition(array, start, end);
		quickSort(array, start, pivot - 1);
		quickSort(array, pivot + 1, end);		
	}
	private static int partition(int[] array, int start, int end) {
		
		int pivot = array[end]; //枢轴
		int i = start - 1;
		
		for(int j = start; j <= end; j++) {
			if(array[j] < pivot) {
				i++;
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
		i++;
		int temp = array[i];
		array[i] = array[end];
		array[end] = temp;
		
		return i;
	}
}
```