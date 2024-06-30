package main

import (
	"fmt"
	"math"
)

func sayHello(n string){
	fmt.Println("hello", n)
}
func sayBye(n string){
	fmt.Println("goodbye",n)
}

func circleArea(r float64) (float64){
	return math.Pi*r*r
}

func manySay(n []string, f func(string)){	//函数里调用函数
	for _, v := range n {
		f(v)
	}
}

func main(){
	sayHello("handi")
	sayHello("luigi")
	sayBye("handi")
	sayBye("luigi")

	manySay([]string{"laowang", "yuan", "hou", "cao","tian"}, sayHello)
	manySay([]string{"laowang", "yuan", "hou", "cao","tian"}, sayBye)


	c1 := circleArea(13.5)
	c2 := circleArea(15)
	fmt.Println(c1,c2)
	fmt.Printf("the c1 area is %0.3f ,the c2 area is %09.3f\n",c1,c2)
}