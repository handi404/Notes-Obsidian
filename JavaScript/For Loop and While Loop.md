
```js
let names = ["ed", "john", "maria", "eliza", "burrito", "harry", "potter"];

  

for (name of names) {

  console.log(name);

  if (name === "harry") {

    console.log("found harry");

    break;

  }

}

  

let loading = 0;

  

while (loading < 100) {

  console.log("website is still loading");

  loading++;

}
```