//伪随机数 = 统计上随机的一组值或元素
//（不要将它们用于任何形式的加密安全）

//使用当前时间作为种子
   Srand (time (0));
   
//生成一个介于 MIN 和 MAX 之间的随机数
   Answer = (rand () % MAX) + MIN;

---
[[C/C tutorial for beginners/猜数字游戏|猜数字游戏]]

```c
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main()
{
   // 使用当前时间作为随机生成器的种子
   srand(time(0));
 
   // rand() 生成 0 - 32,767 之间的伪随机数
   int number1 = (rand() % 6) + 1;
   int number2 = (rand() % 6) + 1;
   int number3 = (rand() % 6) + 1;
 
   printf("%d\n", number1);
   printf("%d\n", number2);
   printf("%d\n", number3);
 
   return 0;
}
```