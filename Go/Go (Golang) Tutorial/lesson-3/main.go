package main

import "fmt"

func main() {

	// string variables
	var nameOne string = "mario"
	var nameTwo = "luigi"
	var nameThree string

	fmt.Print(nameOne, nameTwo, nameThree)

	nameOne = "peach"
	nameThree = "bowser"

	fmt.Print(nameOne, nameTwo, nameThree)

	// 以下内容仅在函数中允许
	fmt.Print(nameFour)

	// int variables
	var ageOne int = 20
	var ageTwo = 30
	ageThree := 40

	fmt.Print(ageOne, ageTwo, ageThree)

	// bits & memory
	// var numOne int8 = 25
	// var numTwo int8 = 128 // 对于 8 位来说，这个数字太大了
	// var numTwo uint = -25 无符号整数不能为负数

	var scoreOne float32 = 25.98
	var scoreTwo float64 = 1965385877.5
	var scoreThree = 1.5 // inferred as(推断为) float64

	//最好用float64，int64

	fmt.Print(scoreOne, scoreTwo, scoreThree)
}
