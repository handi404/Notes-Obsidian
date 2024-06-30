package main

import (
	"fmt"
)
type bill struct{
	name string
	item map[string]float64
	tip float64
}
func newBill(n string) bill{
	Bill := bill{
		name: n,
		item: map[string]float64{},
		tip: 0,
	}
	return Bill
}
// 格式化账单
func (b *bill) format() string{
	qw := "bill breakdown:\n"
	var total float64 = 0
	for c,v := range b.item{
		qw += fmt.Sprintf("%-25v...$%.2f\n",c+":",v)
		total += v
	}

	qw += fmt.Sprintf("%-25v...$%.2f\n","tip:",b.tip)

	qw += fmt.Sprintf("%-25v...$%.2f\n","total:",total)

	return qw
}

// 将项目添加到账单
func (b *bill) updateItem(name string, price float64){
	// go特性对于struct内部不用再*
	// (*b).item = price
	b.item[name] = price
}
// 更新提示
func (b *bill) updateTip(tip float64){ //类型要一致
	b.tip = tip
}