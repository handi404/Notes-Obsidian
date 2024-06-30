package main

import "fmt"

// func updateName(x string) {
// 	x = "yoshi"
// }

func updateName(x string) string {
	x = "wedge"
	return x
}

func updateMenu(y map[string]float64) {
	y["coffee"] = 2.99
}

func main() {
	// group A types -> strings, ints, bools, floats, arrays, structs
	// 组 A 类型 ->字符串、整数、布尔斯、浮点数、数组、结构
	// non-pointer wrapper values
	// 非指针包装器值
	name := "tifa"

	// updateName(name)
	name = updateName(name)

	fmt.Println(name)

	// group B types -> slices, maps, functions
	// B 组类型 - >切片、映射、函数
	// pointer wrapper values
	// 指针包装器值
	menu := map[string]float64{
		"pie":       5.95,
		"ice cream": 3.99,
	}

	updateMenu(menu)
	fmt.Println(menu)
}
