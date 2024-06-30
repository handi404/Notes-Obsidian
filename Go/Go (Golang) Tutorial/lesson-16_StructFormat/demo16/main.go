package main
import(
	"fmt"
)
func main(){
	myBill := updateBill("myBill")
	fmt.Println(myBill)
	fmt.Println(myBill.format())
}