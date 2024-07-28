// 递归 = 当一个事物根据其自身来定义时。 - 维基百科 
// 将过程的结果应用于过程。 
// 递归方法调用自身。可以替代迭代。 
// 将问题划分为与原始问题类型相同的子问题。 
// 常用于高级排序算法和导航树 

// 优点 
// ---------- 
// 更容易读/写 
// 更容易调试 

// 缺点 
// ---------- 
// 有时会慢一些 
// 使用更多内存

---

```java
public class Main{

	public static void main(String[] args) {
		
		walk(5);
		System.out.println(factorial(7));
		System.out.println(power(2, 8));
	}

	private static void walk(int steps) {
		
		if(steps < 1) return; //基本情况
		System.out.println("You take a step!");
		walk(steps - 1); //递归情况
	}
	private static int factorial(int num) {
		
	    if (num < 1) return 1; //base case
	    return num * factorial(num - 1); //recursive case
	}

	private static int power(int base, int power) {
		
	    if (power < 1) return 1; //base case
	    return base * power(base, power - 1); //recursive case
	}
}
```