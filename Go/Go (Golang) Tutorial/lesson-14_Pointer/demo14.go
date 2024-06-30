package main
import(
	"fmt"
)
func updateName(x *string){
	*x = "handi"
}
func main(){
	// pointer
	name := "joker"
	//name的内存地址
	fmt.Println("memory address of name is:",&name)
	fmt.Println("name is:",name)
	//n存储name的内存地址，n是指针,n有自己的memory address
	n := &name
	fmt.Println("memory address of n is:",&n)
	fmt.Println("n is:",n)
	//*n获取name的值
	fmt.Println("value at memory address of n is:",*n)
	//利用n改变name的值
	updateName(n)
	fmt.Println("name is:",name)
}