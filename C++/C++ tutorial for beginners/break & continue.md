#### //break=跳出循环 
#### // continue = 跳过当前迭代

```c++
#include <iostream>

int main()
{
    for(int i = 1; i <= 20; i++){
        if(i == 13){
            //break;
            //continue;
        }
        std::cout << i << '\n';
    }

    return 0;
}
```