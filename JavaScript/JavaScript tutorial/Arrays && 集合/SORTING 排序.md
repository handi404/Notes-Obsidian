
###### // sort() = 用于对数组元素进行就地排序的方法。 
###### // 按字典顺序（而不是字母顺序）将元素作为字符串排序
###### // 字典顺序=（字母+数字+符号）作为字符串

---

```js
// ---------- EXAMPLE 1 ---------- 
const numbers = [1, 10, 2, 9, 3, 8, 4, 7, 5, 6]; 
numbers.sort((a, b) => a - b); //前进 
numbers.sort((a, b) => b - a); //逆转 
console.log(numbers);

// ---------- EXAMPLE 2 ---------- 
const people = [{name: "Spongebob", age: 30, gpa: 3.0}, 
				{name: "Patrick", age: 37, gpa: 1.5}, 
				{name: "Squidward", age: 51, gpa: 2.5}, 
				{name: "Sandy", age: 27, gpa: 4.0}]
people.sort((a, b) => a.age - b.age); //前进 
people.sort((a, b) => b.age - a.age); //逆转 

people.sort((a, b) => a.gpa - b.gpa); //前进 
people.sort((a, b) => b.gpa - a.gpa); //逆转 

people.sort((a, b) => a.name.localeCompare(b.name)); //前进 
people.sort((a, b) => b.name.localeCompare(a.name)); //逆转 
console.log(people);


```