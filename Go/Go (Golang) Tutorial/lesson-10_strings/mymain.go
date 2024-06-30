package main

import (
	"fmt"
	"strings"
)
/*
append 内置函数将元素追加到切片的末尾。
slice = append(slice, elem1, elem2)
slice = append(slice, anotherSlice...)
作为一种特殊情况，将字符串附加到字节片是合法的，如下所示：
slice = append([]byte("hello "), "world"...)
*/

func getInitials(n string) (string, string){	//get大写首字母.返回两个值
	s := strings.ToUpper(n)
	names := strings.Split(s, " ")
	var initials []string
	for _, v := range names{
		initials = append(initials, v[:1])
	}
	if len(initials) > 1 {
		return initials[0], initials[1]
	}
	return initials[0], "_"
}

func main(){
	n1, s1 := getInitials("Han Di")
	fmt.Println(n1, s1)
	n2, s2 := getInitials("zhang san")
	fmt.Println(n2,s2)
	n3, s3 := getInitials("wang")
	fmt.Println(n3,s3)

	names := []string{"hai", "dai","gong","shang"}
	var copy []string
	for _,v := range names{
		copy = append(copy, v)
	}
	fmt.Println(names)
	fmt.Println(copy)
}