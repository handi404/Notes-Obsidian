// 堆栈 = LIFO 数据结构。后进先出 
// 将对象存储到一种“垂直塔”中 
// push() 将对象添加到顶部 
// pop() 从顶部删除对象 

// 栈的使用？ 
// 1. 文本编辑器中的撤消/重做功能 
// 2. 在浏览器历史记录中后退/前进 
// 3.回溯算法（迷宫、文件目录） 
// 4. 调用函数（调用栈）

---

```java
import java.util.Stack;

public class Main{
	
	public static void main(String[] args) {
	
		Stack<String> stack = new Stack<String>();
		
		//System.out.println(stack.empty()); //是否为空
		
		stack.push("Minecraft");
		stack.push("Skyrim");
		stack.push("DOOM");
		stack.push("Borderlands");
		stack.push("FFVII");
		
		//String myFavGame = stack.pop();
		//System.out.println(stack.peek());	//返回顶部
		//System.out.println(stack.search("Fallout76")); //查
		System.out.println(stack);
		
	}
}
```