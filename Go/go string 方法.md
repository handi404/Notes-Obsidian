> 耐心和持久胜过激烈和狂热。

哈喽大家好，我是陈明勇，今天分享的知识是 Go 标准库 —— `strings` 常用函数和方法。如果本文对你有帮助，不妨点个赞，如果你是 Go 语言初学者，不妨点个关注，一起成长一起进步，如果本文有错误的地方，欢迎指出！

## strings 库

`strings` 库包含了许多高效的字符串常用操作的函数和方法，巧用这些函数与方法，能极大的提高我们程序的性能。下面介绍一些常用的函数和方法。

## 高效[拼接字符串](https://so.csdn.net/so/search?q=%E6%8B%BC%E6%8E%A5%E5%AD%97%E7%AC%A6%E4%B8%B2&spm=1001.2101.3001.7020)

使用 `strings` 库里的 `Builder` 变量，结合其写入方法如 `WriteString` 方法，可以进行高效的拼接字符串。

```go
import ( "strings" ) func main() { var builder strings.Builder builder.WriteString("hello") builder.WriteString(" ") builder.WriteString("world") builder.WriteString("!") s := builder.String() println(s) // hello world! }
```

`strings.Builder` 底层是通过其内部的 `slice` 来储存内容的。当调用其相关的写入方法(如 `WriteString` )的时，新的字节数据就会被追加到 `slice` 上。相比普通字符串的多次拼接，减少了拼接时需要创建新字符串的内存开销。

## 大小写转换

-   **小写转大写**  
    `ToUpper(s string) string`：将一个字符串里的小写字符转成大写，因为字符串不可变的特点，该函数会返回一个新的字符串。
    
    ```go
    import "strings" func main() { s1 := "hello" s2 := strings.ToUpper(s1) println(s2) // HELLO }
    ```
    
    `ToUpper(s string) string` 函数的作用是，
-   **大写转小写**  
    `ToLower(s string) string`：将一个字符串里的大写字符转成小写，因为字符串不可变的特点，该函数会返回一个新的字符串。
    
    ```go
    import "strings" func main() { s1 := "HELLO" s2 := strings.ToLower(s1) println(s2) // hello }
    ```
    

## 比较两个字符串

-   区分大小写比较
    
    ```go
    func main() { s1 := "hello" s2 := "hello" s3 := "HELLO" println(s1 == s2) // true println(s1 == s3) // false }
    ```
    
    直接通过 `==` 操作符进行区分大小写的字符串比较即可。
-   不区分大小写比较  
    使用 `EqualFold(s, t string) bool` 函数进行比较，两个参数为需要比较的两个字符串，返回值为布尔值，如果是 `true` 说明字符串相等，反之 `false` 则字符串不相等。
    
    ```go
    func main() { s1 := "hello" s2 := "hello" s3 := "HELLO" isEual := strings.EqualFold(s1, s2) println(isEual) // true isEual2 := strings.EqualFold(s1, s3) println(isEual2) // true }
    ```
    

## 字符串的替换

[字符串替换](https://so.csdn.net/so/search?q=%E5%AD%97%E7%AC%A6%E4%B8%B2%E6%9B%BF%E6%8D%A2&spm=1001.2101.3001.7020)的函数：`Replace(s, old, new string, n int) string`

-   第一个参数 `s` 为原字符串。
-   第二个参数 `old` 为需要替换的字符串。
-   第三个参数 `new` 为替换后的字符串。
-   第四个参数 `n` 为指定替换几个 `old` ，如果 `n` 小于 0，则替换全部。
-   返回值为替换后的新字符串。

案例：实现对[敏感词](https://so.csdn.net/so/search?q=%E6%95%8F%E6%84%9F%E8%AF%8D&spm=1001.2101.3001.7020)的替换

```go
func main() { s1 := "我靠啊靠" s2 := strings.Replace(s1, "靠", "*", 1) println(s2) // true 我*啊靠 s3 := "我靠啊靠" s4 := strings.Replace(s3, "靠", "*", -1) println(s4) // true 我*啊* }
```

第一次替换时，`n` 指定为 `1`，因此只替换了一个敏感词。  
第二次替换时，`n` 指定为 `-1`，小于 `0`，因此将所有敏感词都替换了。

## 按照某个分割标识分割字符串

分割字符串的函数：`Split(s, sep string) []string`

-   第一个参数 `s` 为需要分割的字符串。
-   第二个参数 `sep` 为分割的标识。
-   返回值为字符串切片，保存被分割出来的子字符串。

```go
import ( "fmt" "strings" ) func main() { s1 := "golang-is-awesome" strSlice := strings.Split(s1, "-") fmt.Println(strSlice) // [golang is awesome] }
```

## 去掉字符串左右两边的空格

函数：`TrimSpace(s string) string`

-   第一个参数 `s` 为需要去除空格的字符串。
-   返回值为去除空格后的新字符串。

```go
import ( "strings" ) func main() { s1 := " golang is awesome " s2 := strings.TrimSpace(s1) println(s2) // "golang is awesome" }
```

## 将字符串\[左边\]或\[右边\]或\[左右两边\]所指定的字符（串）去掉

-   将字符串\[左右两边\]所指定的字符（串）去掉  
    函数：`Trim(s, cutset string) string`
    
    -   第一个参数 `s` 为需要去除指定字符的字符串。
    -   第二个参数为指定的字符（串）。
    
    ```go
    import ( "strings" ) func main() { s1 := "-golang is awesome-" s2 := strings.Trim(s1, "-") println(s2) // "golang is awesome" }
    ```
    
-   将字符串\[左边\]所指定的字符（串）去掉  
    函数：`TrimLeft(s, cutset string) string`
    
    -   第一个参数 `s` 为需要去除指定字符的字符串。
    -   第二个参数为指定的字符（串）。
    
    ```go
    import ( "strings" ) func main() { s1 := "-golang is awesome" s2 := strings.TrimLeft(s1, "-") println(s2) // "golang is awesome" }
    ```
    
-   将字符串\[右边\]所指定的字符（串）去掉  
    函数：`TrimRight(s, cutset string) string`
    
    -   第一个参数 `s` 为需要去除指定字符的字符串。
    -   第二个参数为指定的字符（串）。
    
    ```go
    import ( "strings" ) func main() { s1 := "golang is awesome-" s2 := strings.TrimRight(s1, "-") println(s2) // "golang is awesome" }
    ```
    

## 判断字符串是否以指定的字符串开头

函数：`HasPrefix(s, prefix string) bool`  
\- 第一个参数 `s` 为被判断字符串。  
\- 第二个参数 `prefix` 为指定的字符串。

```go
import ( "strings" ) func main() { s1 := "hello world!" flag := strings.HasPrefix(s1, "hello") println(flag) // true }
```

## 判断字符串是否以指定的字符串结束

函数：`HasPrefix(s, prefix string) bool`  
\- 第一个参数 `s` 为被判断字符串。  
\- 第二个参数 `prefix` 为指定的字符串。

```go
import ( "strings" ) func main() { s1 := "hello world!" flag := strings.HasSuffix(s1, "!") println(flag) // true }
```

## 将字符串切片中的元素以指定字符串连接生成新字符串

函数：`Join(elems []string, sep string) string`

-   第一个参数 `elems` 为字符串切片。
-   第二个参数 `sep` 为连接符。
-   返回值为新的字符串。

```go
import ( "strings" ) func main() { strSlice := []string{"golang", "is", "awesome"} s := strings.Join(strSlice, "-") println(s) // golang-is-awesome }
```

## 查找子串是否存在于指定的字符串中

函数： `Contains(s, substr string) bool`

-   第一个参数 `s` 为指定的字符串。
-   第二个参数 `substr` 为子串。
-   返回值为布尔值，如果是 `true` 说明存在，反之 `false` 则不存在。

```go
import ( "strings" ) func main() { s := "golang is awesome" isContains := strings.Contains(s, "golang") println(isContains) // true }
```

## 小结

本文先对 strings 标准库里的 `Builder` 变量进行介绍，使用其写入方法可以高效地拼接字符串，然后对 `ToUpper`、`ToLower`、`Replace` 等常用函数的参数和返回值以及用法进行介绍。