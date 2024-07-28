// switch = 使用许多“else if”语句的更有效替代方案
// 允许在多种情况下测试一个值是否相等

---

```c
#include <stdio.h>

int main(){
   char grade;

   printf("\nEnter a letter grade: ");
   scanf("%c", &grade);

   switch(grade){
      case 'A':
         printf("perfect!\n");
         break;
      case 'B':
         printf("You did good!\n");
         break;
      case 'C':
         printf("You did okay!\n");
         break;
      case 'D':
         printf("At least it's not an F!\n");
         break;
      case 'F':
         printf("YOU FAILED!\n");
         break;
      default:
         printf("Please enter only valid grades");
   }

    return 0;
}
```