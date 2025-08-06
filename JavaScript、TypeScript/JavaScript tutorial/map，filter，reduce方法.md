### map
```js
const dates = ["2024-1-10", "2025-2-20", "2026-3-30"]; 
const formattedDates = dates.map(formatDates); 
console.log(formattedDates); 
function formatDates(element){ 
	const parts = element.split("-"); 
	return `${parts[1]}/${parts[2]}/${parts[0]}`; 
}
//["1/10/2024","2/20/2025",3/30/2026]
```


### filter  一种过滤

```js
// ----------- EXAMPLE 1 ----------- 
let numbers = [1, 2, 3, 4, 5, 6, 7]; 
let evenNums = numbers.filter(isEven); 
let oddNums = numbers.filter(isOdd); 
console.log(oddNums); 
function isEven(element){ 
	return element % 2 === 0; 
} 
function isOdd(element){ 
	return element % 2 !== 0; 
} 
// ----------- EXAMPLE 2 ----------- 
let ages = [16, 17, 17, 18, 19, 20, 65]; 
let adults = ages.filter(isAdult); 
let children = ages.filter(isChild); 
console.log(children); 
function isAdult(element){ 
	return element >= 18; 
} 
function isChild(element){ 
	return element < 18; 
} 
// ----------- EXAMPLE 3 ----------- 
const words = ['apple', 'orange', 'kiwi', 'banana', 'pomegranate', 'coconut',]; 
const longWords = words.filter(getLongWords); 
const shortWords = words.filter(getShortWords); 
console.log(shortWords); 
function getShortWords(element){ 
	return element.length <= 6; 
} 
function getLongWords(element){ 
	return element.length > 6; 
}
```

### reduce

```js
// ---------- EXAMPLE 1 ---------- 
const prices = [5, 30, 10, 25, 15, 20]; 
const total = prices.reduce(sum); 
onsole.log(`$${total.toFixed(2)}`); 	//$105.00
function sum(accumulator, element){ 
	return accumulator + element; 
} 

// ---------- EXAMPLE 2 ---------- 
const scores = [75, 50, 90, 80, 65, 95]; 
const maximum = scores.reduce(getMax); 
const minimum = scores.reduce(getMin); 
console.log(maximum);	//95 
console.log(minimum); 	//50
function getMax(accumulator, element){ 
	return Math.max(accumulator, element); 
} 
function getMin(accumulator, element){ 
	return Math.min(accumulator, element); 
}

```