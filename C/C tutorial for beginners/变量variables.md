###### // 变量 = 内存中分配的空间来存储值。
###### // 我们引用变量的名称来访问存储的值。
###### // 该变量现在的行为就像它包含的值一样。
###### // 但是我们需要声明我们存储的数据类型。

---

```c
#include <stdio.h>

int main(){
    int x;            //宣言
    x = 123;       //初始化
    int y = 321; //声明+初始化

    int age = 21;              //整数
    float gpa = 2.05;       //浮点数
    char grade = 'C';        //单个字符
    char name[] = "Bro";  //字符数组
    
    // % = 格式说明符
    printf("Hello %s\n", name);
    printf("You are %d years old\n", age);
    printf("Your average grade is %c\n", grade);
    printf("Your gpa is %f\n", gpa);

    return 0;
}
```