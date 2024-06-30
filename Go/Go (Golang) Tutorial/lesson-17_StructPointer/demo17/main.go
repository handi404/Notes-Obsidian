package main
import(
	"fmt"
)
func main(){
	myBill := updateBill("myBill")
	item := map[string]float64{
		"apple": 1.5,
		"banana": 2.5,
		"orange": 3.5,
	}
	for c,v := range item{
		myBill.updateItem(c,v)
	}
	myBill.updateTip(10)
	fmt.Println(myBill)
	fmt.Println(myBill.format())
}