package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func getInput(print string,read *bufio.Reader)(string, error){
	fmt.Println(print)
	name,error := read.ReadString('\n')
	return strings.TrimSpace(name),error
}
func createBill() bill{
	read := bufio.NewReader(os.Stdin)
	name,_ := getInput("请输入账单名称：",read)
	bill := newBill(name)
	return bill
}
func choice(b bill){
	read := bufio.NewReader(os.Stdin)
	cho,_ := getInput("选择选项（a - 添加项目，s - 保存账单，t - 添加提示）",read)
	switch cho{
	case "a":
		name,_ := getInput("name:",read)
		price,_ := getInput("price:",read)
		priceFloat, error := strconv.ParseFloat(price,64)
		if error != nil {	//出错
			fmt.Println("价格必须是一个数字...")
			choice(b)
		}
		b.updateItem(name,priceFloat)
		fmt.Println("项目增加 -",name,priceFloat)
		choice(b)
	case "s":
		fmt.Println("you chose to save the bill")
		fmt.Println(b.format())
	case "t":
		tip,_ := getInput("tip $",read)
		tipFloat,error := strconv.ParseFloat(tip,64)
		if error != nil {
			fmt.Println("tip必须是一个数字....")
			choice(b)
		}
		b.updateTip(tipFloat)
		fmt.Println("提示已更新",tipFloat)
		choice(b)
	default:
		fmt.Println("not choice (无效)")
		choice(b)
	}
}
func main(){
	bill := createBill()
	choice(bill)
}