// while 循环=重复一段代码可能无限次。
// 当某些条件保持为真时
// while 循环可能根本不执行

// while 循环=检查条件，如果条件为真则执行代码块
// do while 循环=总是执行一次代码块，然后检查条件

---

```c
#include <stdio.h>
#include <string.h>

int main()
{
   char name[25];

   printf("\nWhat's your name?: ");
   fgets(name, 25, stdin);
   name[strlen(name) - 1] = '\0';

   while(strlen(name) == 0)
   {
      printf("\nYou did not enter your name");
      printf("\nWhat's your name?: ");
      fgets(name, 25, stdin);
      name[strlen(name) - 1] = '\0';
   }

   printf("Hello %s", name);

   return 0;
}
```

```c
#include <stdio.h>

int main()
{
   int number = 0;
   int sum = 0;

   do{
      printf("Enter a # above 0: ");
      scanf("%d", &number);
      if(number > 0)
      {
         sum += number;
      }
   }while(number > 0);
   
   printf("sum: %d", sum);
 
   return 0;
}
```