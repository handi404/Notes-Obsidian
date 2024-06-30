package main

import(
	"fmt"
)

func main(){
	maps := map[string]float64{
		"pi": 3.14,
		"e": 2.718,
		"score": 90.5,
		"soup": 4.99,
	}
	fmt.Println(maps)
	for k,v := range maps{
		fmt.Println(k,"-",v)
	}
	phonebook := map[string]int64{
		"han": 724566784,
		"lao": 7824689838,
		"tao": 67235487,
	}
	for k,v := range phonebook{
		fmt.Println(k,"-",v)
	}
	fmt.Println(phonebook)
	//update
	phonebook["han"] = 724566785
	fmt.Println(phonebook)
	delete(phonebook, "tao")
	fmt.Println(phonebook)
}