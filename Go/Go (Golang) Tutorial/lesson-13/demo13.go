package main

import (
	"fmt"
)
func updateName(n string) string{
	n = "yoshi"
	return n
}
func updateMen(m map[string]float64){
	m["origin"] = 4.2
}
func main(){
	name := "joke"
	// updateName(name)
	name = updateName(name)
	fmt.Println(name)

	men := map[string]float64{
		"coffee": 9.99,
		"ice cream": 4.99,
		"apple": 2.3,
	}
	updateMen(men)
	for c,v := range men{
		fmt.Println(c,v)
	}
}