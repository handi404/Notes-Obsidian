package main

// struct
type bill struct{
	name string
	item map[string]float64
	tip float64
}

func updateBill(n string) bill{
	Bill := bill{
		name: n,
		item: map[string]float64{},
		tip: 0,
	}
	return Bill
}