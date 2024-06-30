package main
import(
	"fmt"
)
func main(){
	age := 12
	updateAge(&age)
	fmt.Println(age)
}
func updateAge(age *int){
	*age = 13
}

