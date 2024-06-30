package main

import "fmt"

var score = 99.5

// cannot use shorthand outside of functions
// 不能在函数之外使用速记
// scoreTwo := 50

func main() {
	sayHello("mario")
	showScore()

	for _, v := range points {
		fmt.Println(v)
	}
}
