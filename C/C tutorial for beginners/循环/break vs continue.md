// continue = 跳过其余代码并强制进行下一次循环迭代
//break=退出循环/switch

---

```c
#include <stdio.h>

int main()
{
   for(int i = 1; i <= 20; i++)
   {
      if(i == 13)
      {
         continue;
         //break;
      }
      printf("%d\n", i);
   }
  
   return 0;
}
```