package main

import (
	"fmt"
)

type bill struct{
	name string
	item map[string]float64
	tip float64
}

func newBill(name string) bill{
	Bill := bill{
		name: name,
		item: map[string]float64{},
		tip: 0,
	}
	return Bill
}

func (b *bill) format() string{
	zd := "账单明细\n"
	var total float64 = 0
	for c,v := range b.item{
		zd += fmt.Sprintf("%-25v...%.2f\n",c,v)
		total += v
	}
	zd += fmt.Sprintf("%-25v...%.2f\n","tip:",b.tip)
	zd += fmt.Sprintf("%-25v...%.2f\n","总:",total)
	return zd
}

func (b *bill) updateItem(name string ,price float64){
	b.item[name] = price
}

func (b *bill) updateTip(tip float64){
	b.tip = tip
}