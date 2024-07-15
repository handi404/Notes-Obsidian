**目录**
- [[#1 length 方法用于获取字符串的长度|1 length 方法用于获取字符串的长度]]
- [[#2 split 方法用于将字符串分割为子字符串数组|2 split 方法用于将字符串分割为子字符串数组]]
- [[#3  toLowerCase 方法用于将字符串转换为小写|3  toLowerCase 方法用于将字符串转换为小写]]
- [[#4 toUpperCase 方法用于将字符串转换为大写|4 toUpperCase 方法用于将字符串转换为大写]]
- [[#5 trim 方法用于去除字符串两端的空格|5 trim 方法用于去除字符串两端的空格]]
- [[#6 indexOf 方法用于查找字符串中某个字符或子字符串的索引位置|6 indexOf 方法用于查找字符串中某个字符或子字符串的索引位置]]
- [[#7 lastIndexOf 方法用于查找字符串中某个字符或子字符串的最后一次出现的索引位置|7 lastIndexOf 方法用于查找字符串中某个字符或子字符串的最后一次出现的索引位置]]
- [[#8 includes 方法用于检查字符串是否包含指定的子字符串|8 includes 方法用于检查字符串是否包含指定的子字符串]]
- [[#9 startsWith 方法用于检查字符串是否以指定的子字符串开头|9 startsWith 方法用于检查字符串是否以指定的子字符串开头]]
- [[#10 endsWith 方法用于检查字符串是否以指定的子字符串结尾|10 endsWith 方法用于检查字符串是否以指定的子字符串结尾]]
- [[#11 repeat 方法用于将字符串重复指定的次数|11 repeat 方法用于将字符串重复指定的次数]]
- [[#12 match 方法用于在字符串中查找匹配正则表达式的结果。返回一个数组，其中包含所有匹配的结果。如果没有匹配的结果，则返回null|12 match 方法用于在字符串中查找匹配正则表达式的结果。返回一个数组，其中包含所有匹配的结果。如果没有匹配的结果，则返回null]]
- [[#13 replace 方法用于在字符串中替换指定子字符串。返回替换后的字符串|13 replace 方法用于在字符串中替换指定子字符串。返回替换后的字符串]]
- [[#14 substring 方法用于从字符串中提取子字符串。返回被提取的子字符串|14 substring 方法用于从字符串中提取子字符串。返回被提取的子字符串]]
- [[#15 charAt 方法用于获取指定索引位置的字符。返回该索引位置的字符|15 charAt 方法用于获取指定索引位置的字符。返回该索引位置的字符]]
- [[#16 concat 方法用于连接两个或多个字符串。返回连接后的字符串|16 concat 方法用于连接两个或多个字符串。返回连接后的字符串]]

___

## 1 length 方法用于获取字符串的长度

```javascript
const str = 'hello'

console.log(str.length)// 输出：5
```

___

![](https://img-blog.csdnimg.cn/direct/bf52dd50491c48878da4f73ef32e22b2.png)

## 2 split 方法用于将字符串分割为子字符串数组

```javascript
const str2 = 'apple,banana,orange'

console.log(str2.split(','))// 输出：["apple", "banana", "orange"]
```

___

![](https://img-blog.csdnimg.cn/direct/6f44fd10ab1d496dbfcbbbc33dce7410.png)

## 3  toLowerCase 方法用于将字符串转换为小写

```javascript
const str3 = 'Hello World'

console.log(str3.toLowerCase())// 输出：hello world
```

___

![](https://img-blog.csdnimg.cn/direct/b4f5a8746ad640b99ef057531be5dc53.png)

## 4 toUpperCase 方法用于将字符串转换为大写

```javascript
const str4 = 'hello world'

console.log(str4.toUpperCase())// 输出：HELLO WORLD
```

___

![](https://img-blog.csdnimg.cn/direct/64441d767b7b4fdb9c797ebbb80527a3.png)

## 5 trim 方法用于去除字符串两端的空格

```javascript
const str5 = ' hello world '

console.log(str5.trim())// 输出：hello world
```

___

![](https://img-blog.csdnimg.cn/direct/97da28ee149e4b17a0f7ebe222df7c74.png)

## 6 indexOf 方法用于查找字符串中某个字符或子字符串的索引位置

```javascript
1. // 语法：
    
2. // indexOf(searchElement)
    
3. // indexOf(searchElement, fromIndex)
const str6 = 'hello world'

console.log(str6.indexOf('o'))// 输出：4

console.log(str6.indexOf('o', 6))// 输出：7

console.log(str6.indexOf('o', 10))// 输出：-1
```

___

![](https://img-blog.csdnimg.cn/direct/aff084015b26417d9e69c465d66fc427.png)

## 7 lastIndexOf 方法用于查找字符串中某个字符或子字符串的最后一次出现的索引位置

> 该方法**从 `fromIndex` 开始**向前（左）搜索数组。**但是数组中元素的位置先后还是按照从左往右的。**

```javascript
1. // 语法：
    
2. // lastIndexOf(searchElement)
    
3. // lastIndexOf(searchElement, fromIndex)
const str7 = 'hello oorld'

console.log(str7.lastIndexOf('o'))// 输出：7

console.log(str7.lastIndexOf('o', 6))// 输出：6

console.log(str7.lastIndexOf('o', 10))// 输出：7
```

___

![](https://img-blog.csdnimg.cn/direct/43dc9db98bfd4448b201d2161eb7f2fb.png)

## 8 includes 方法用于检查字符串是否包含指定的子字符串

```javascript
const str8 = 'hello world'

console.log(str8.includes('world'))// 输出：true

console.log(str8.includes('world', 6))// 输出：false
```

___

![](https://img-blog.csdnimg.cn/direct/92750de3554849cf992d08e352872397.png)

## 9 startsWith 方法用于检查字符串是否以指定的子字符串开头

```javascript
const str9 = 'hello world'

console.log(str9.startsWith('hello'))// 输出：true

console.log(str9.startsWith('world'))// 输出：false
```

___

![](https://img-blog.csdnimg.cn/direct/577f9f3a050e4a3bb6c74cef1004c6a1.png)

## 10 endsWith 方法用于检查字符串是否以指定的子字符串结尾

```javascript
const str10 = 'hello world'

console.log(str10.endsWith('world'))// 输出：true
```

___

![](https://img-blog.csdnimg.cn/direct/0887f25f152c4a37ab2fc1ba8fec90ca.png)

## 11 repeat 方法用于将字符串重复指定的次数

```javascript
const str11 = 'hello'

console.log(str11.repeat(3))// 输出：hellohellohello
```

___

![](https://img-blog.csdnimg.cn/direct/2ca6dc9ec3664918a5c2f431587c6af4.png)

## 12 match 方法用于在字符串中查找匹配正则表达式的结果。返回一个数组，其中包含所有匹配的结果。如果没有匹配的结果，则返回null

```javascript
const str12 = 'hello world'

console.log(str12.match(/o/g))// 输出：["o", "o"]

console.log(str12.match(/a/g))// 输出：null
```

___

![](https://img-blog.csdnimg.cn/direct/357f14e84c5e4c669d956ad526dff060.png)

## 13 replace 方法用于在字符串中替换指定子字符串。返回替换后的字符串

```javascript
const str13 = 'hello world'

console.log(str13.replace('world', 'universe'))// 输出：hello universe
```

___

![](https://img-blog.csdnimg.cn/direct/80124b332fb441b3805644c7c905da44.png) 

## 14 substring 方法用于从字符串中提取子字符串。返回被提取的子字符串

```javascript
// 语法：
// substring(第一个参数是起始索引 [, 第二个参数是结束索引（不包含）]) 第二个参数省略，默认截取到最后
const str14 = 'hello world'

console.log(str14.substring(0, 4))// 输出：hell

console.log(str14.substring(1, 3))// 输出：el
```

___

![](https://img-blog.csdnimg.cn/direct/03a62603218d4ce1aed3f57e88122def.png)

## 15 charAt 方法用于获取指定索引位置的字符。返回该索引位置的字符

```javascript
const str15 = 'hello world'

console.log(str15.charAt(7))// 输出：o
```

___

![](https://img-blog.csdnimg.cn/direct/e56cd4407c53430384de120985ab42b7.png)

## 16 concat 方法用于连接两个或多个字符串。返回连接后的字符串

```javascript
const str16 = 'hello'

const str17 = 'world'

console.log(str16.concat(' ', str17))// 输出：hello world

console.log(str16.concat('+', str17))// 输出：hello+world
// concat中的第一个参数是连接符
```

___

![](https://img-blog.csdnimg.cn/direct/fa15e22f22c04bebbc41f8f7204dee5f.png)