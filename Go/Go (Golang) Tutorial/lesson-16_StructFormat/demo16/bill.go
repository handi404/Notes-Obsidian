package main

import (
	"fmt"
)
type bill struct{
	name string
	item map[string]float64
	tip float64
}
func updateBill(n string) bill{
	Bill := bill{
		name: n,
		item: map[string]float64{"coffee":3.99,"apple":2.99,"orange":1.99},
		tip: 0,
	}
	return Bill
}
// 格式化账单
func (b bill) format() string{
	qw := "bill breakdown:\n"
	var total float64 = 0
	for c,v := range b.item{
		qw += fmt.Sprintf("%-25v...$%.2f\n",c+":",v)
		total += v
	}
	qw += fmt.Sprintf("%-25v...$%.2f","total:",total)
	return qw
}