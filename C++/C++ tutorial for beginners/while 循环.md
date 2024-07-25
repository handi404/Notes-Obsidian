[[break & continue]]
[[C++/C++ tutorial for beginners/嵌套循环|嵌套循环]]
```c++
#include <iostream>

int main() 
{
   std::string name;
   
   while(name.empty()){
      std::cout << "Enter your name: ";
      std::getline(std::cin, name);
   }
   
   std::cout << "Hello " << name;

   return 0;
}
```

```c++
#include <iostream>
 
int main()
{
   int number;

   do{
      std::cout << "Enter a positive #: ";
      std::cin >> number;
   }while(number < 0);

   std::cout << "The # is: " << number;

   return 0;
}
```