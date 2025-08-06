###### // ES 6 Module = 包含可重用代码的外部文件
###### // 可以导入到其他 JavaScript 文件中
###### // 可以包含变量、类、函数... 等等
###### // 作为 ECMAScript 2015 更新的一部分引入

---

```js
// ---------- index.js ----------

import {PI, getCircumference, getArea, getVolume} from './mathUtil.js';

console.log(PI);
const circumference = getCircumference(10);
const area = getArea(10);
const volume = getVolume(10);

console.log(`${circumference.toFixed(2)}cm`);
console.log(`${area.toFixed(2)}cm^2`);
console.log(`${volume.toFixed(2)}cm^3`);

// ---------- mathutil.js ----------

export const PI = 3.14159;

export function getCircumference(radius){
    return 2 * PI * radius;
}

export function getArea(radius){
    return PI * radius * radius;
}

export function getVolume(radius){
    return 4 /3 * PI * radius * radius * radius;
}
```