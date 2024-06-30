package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)
func getInput(promt string,r *bufio.Reader) (string,error){
	fmt.Print(promt)
	name,error:= r.ReadString('\n')
	return strings.TrimSpace(name),error
}
func createBill() bill{
	read := bufio.NewReader(os.Stdin)
	// fmt.Printf("请输入账单名称：")
	// name,_ := read.ReadString('\n') //以\n结束
	// name = strings.TrimSpace(name) //去除空格
	name,_ := getInput("请输入账单名称：", read)
	b := newBill(name)
	return b
}
func promptOptions(b bill){
	read := bufio.NewReader(os.Stdin)
	opt,_ := getInput("选择选项（a - 添加项目，s - 保存账单，t - 添加提示）",read)
	fmt.Println(opt)
}
func main(){
	myBill := createBill()
	promptOptions(myBill)
	fmt.Println(myBill)
	fmt.Println(myBill.format())
}