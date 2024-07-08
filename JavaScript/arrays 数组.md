

```js
const arrays = ["hello", "oid age", "happen", "see the video"];

  
console.log(arrays);

  
console.log(arrays.length);

  
arrays.push("new"); //从尾部添加元素

console.log(arrays);

  

arrays.unshift("new"); //从头部添加元素

console.log(arrays);

  

arrays.pop(); //从尾部删除元素

console.log(arrays);

  

arrays.shift(); //从头部删除元素

console.log(arrays);

  

console.log(arrays.indexOf("happen")); //查找元素

  

const index = arrays.indexOf("happen");

console.log(arrays[index]);

arrays.splice(index, 1); //删除元素 1 表示删除一个元素

console.log(arrays);
```