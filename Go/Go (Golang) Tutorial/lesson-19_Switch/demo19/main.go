package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func getInput(print string,read *bufio.Reader)(string, error){
	fmt.Println(print)
	name,error := read.ReadString("\n")
	return name,error
}
func createBill() bill{
	read := bufio.NewReader(os.Stdin)
	name,_ := getInput("请输入账单名称：",read)
	bill := newBill(name)
	return bill
}
func choice(b bill){
	read := bufio.NewReader(os.Stdin)
	choice,_ := getInput("选择选项（a - 添加项目，s - 保存账单，t - 添加提示）",read)
	switch choice{
	case "a":
		name,_ := getInput("name:",read)
		price,_ := getInput("price:",read)
		priceFloat, _ := strconv.ParseFloat(price,64)
		b.updateItem(name,priceFloat)
	case "s":
		fmt.Println("you chose to save the bill")
	case "t":
		tip,_ := getInput("tip $",read)
		tipFloat,_ := strconv.ParseFloat(tip,64)
		b.updateTip(tipFloat)
	default:
		fmt.Println("not choice")
	}
}
func main(){
	bill := createBill()
	choice(bill)
}