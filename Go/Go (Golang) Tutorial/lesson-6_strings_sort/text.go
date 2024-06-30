package main

import (
	"fmt"
	"strings"
	"sort"
)

func main(){
		greeting := "hello my friends! ll"
		fmt.Println(strings.Contains(greeting, "hello"))
		fmt.Println(strings.Index(greeting," "))
		fmt.Println(strings.ToUpper(greeting))
		fmt.Println(strings.Split(greeting , " "))
		fmt.Println(strings.ReplaceAll(greeting, "hello", "hi"))
		fmt.Println(strings.Replace(greeting ,"ll", "o",2))
		fmt.Println("the string = ", greeting)

		ages := []int{10 ,47 ,30 ,40 ,50}
		fmt.Println(ages[:1])
		sort.Ints(ages)
		fmt.Println(ages)

		index := sort.SearchInts(ages ,40)
		fmt.Println(index)

		names := []string{"handi", "wang", "cao", "tian", "yuan" ,"hou"}
		sort.Strings(names)
		fmt.Println(names)
		for _,v := range names{
			fmt.Println(v[:1])
		}
}

