
==    and   ===  is different

```js
const age = 18;

/*

解释 == 和 === 的简单方法是，当我们使用 == 时，JavaScript 仅检查名称和值是否相等，

但当我们使用 === 时，JavaScript 会检查值和类型，所以简单的是 == 是用于检查值，===用于检查值和类型

*/

if (age == "18") {

  console.log("you are good to go");

} else {

  console.log("you are not old enght");

}
```