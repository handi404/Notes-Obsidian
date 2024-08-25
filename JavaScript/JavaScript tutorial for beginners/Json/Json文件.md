#### // JSON =（JavaScript 对象表示法）数据交换格式 
#### // 用于在服务器和 Web 应用程序之间交换数据
#### // JSON 文件 {key: value} OR [value 1, value 2, value 3]

#### // JSON.Stringify () = 将 JS 对象转换为 JSON 字符串。
#### // JSON.Parse () = 将 JSON 字符串转换为 JS 对象

---

```js
// ---------- JSON.stringify() ----------

const names = ["Spongebob", "Patrick", "Squidward", "Sandy"];
const person = {
    "name": "Spongebob",
    "age": 30,
    "isEmployed": true,
    "hobbies": ["Jellyfishing", "Karate", "Cooking"]
};
const people = [{
    "name": "Spongebob",
    "age": 30,
    "isEmployed": true
},
{
    "name": "Patrick",
    "age": 34,
    "isEmployed": false
},
{
    "name": "Squidward",
    "age": 50,
    "isEmployed": true
},
{
    "name": "Sandy",
    "age": 27,
    "isEmployed": false
}];

const jsonString = JSON.stringify(people);

console.log(jsonString);

// ---------- JSON.parse() ----------

const jsonNames = `["Spongebob", "Patrick", "Squidward", "Sandy"]`;
const jsonPerson = `{"name": "Spongebob", "age": 30, "isEmployed": true, "hobbies": ["Jellyfishing", "Karate", "Cooking"]}`;
const jsonPeople = `[{"name": "Spongebob","age": 30,"isEmployed": true},
                     {"name": "Patrick","age": 34,"isEmployed": false},
                     {"name": "Squidward","age": 50,"isEmployed": true},
                     {"name": "Sandy","age": 27,"isEmployed": false}]`;

const parsedData = JSON.parse(jsonPeople);

console.log(parsedData);

// ---------- fetch() ----------
fetch("people.json")
    .then(response => response.json())
    .then(values => values.forEach(value => console.log(value)))
    .catch(error => console.error(error));
```


```json
//name.json		数组
["Spongebob", "Patrick", "Squidward", "Sandy"]
```

```json
//peron.json	对象
{
    "name": "Spongebob",
    "age": 30,
    "isEmployed": true,
    "hobbies": ["Jellyfishing", "Karate", "Cooking"]
}
```

```json
//people.json	对象数组
[
  {
    "name": "Spongebob",
    "age": 30,
    "isEmployed": true
  },
  {
    "name": "Patrick",
    "age": 34,
    "isEmployed": false
  },
  {
    "name": "Squidward",
    "age": 50,
    "isEmployed": true
  },
  {
    "name": "Sandy",
    "age": 27,
    "isEmployed": false
  }
]
```

