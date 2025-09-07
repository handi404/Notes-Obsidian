字符串处理是JavaScript开发中的常见任务，分享10个强大的JavaScript字符串处理技巧，这些技巧不仅可以减少代码量，还能显著提高代码的可读性和维护性。

## 1\. 模板字符串替代字符串拼接

```js
// 传统方式
const greeting = 'Hello, ' + user.name + '! You have ' + user.notifications + ' notifications.';

// 模板字符串
const greeting = \`Hello, ${user.name}! You have ${user.notifications} notifications.\`;
```

模板字符串不仅代码更简洁，而且可读性更强，尤其是在处理多行文本时。

## 2\. 解构赋值提取字符串

```js
// 传统方式
const firstChar = str.charAt(0);
const lastChar = str.charAt(str.length - 1);

// 解构赋值
const [firstChar, ...rest] = str;
const lastChar = str.slice(-1);
```

解构赋值不仅可以用于数组，还可以用于字符串，使得字符提取变得更加简洁。

## 3\. 使用String.prototype.includes代替indexOf

```js
// 传统方式
if (str.indexOf('JavaScript') !== -1) {
  // 字符串包含"JavaScript"
}

// 使用includes
if (str.includes('JavaScript')) {
  // 字符串包含"JavaScript"
}
```

`includes` 方法更直观，意图更明确，减少了不必要的比较操作。

## 4\. 使用String.prototype.startsWith和endsWith

```js
// 传统方式
if (str.indexOf('https://') === 0) {
  // 字符串以"https://"开头
}
if (str.indexOf('.js') === str.length - 3) {
  // 字符串以".js"结尾
}

// 使用 startsWith 和 endWith
if (str.startsWith('https://')) {
  // 字符串以"https://"开头
}
if (str.endWith('.js')) {
  // 字符串以".js"结尾
}
```

这些方法使代码更加语义化，减少了错误的可能性。

## 5\. 字符串填充与对齐

```js
// 传统方式
function padNumber(num, length) {
  let str = num.toString();
  while (str.length < length) {
    str = "0" + str;
  }
  return str;
}

// 使用 padStart/padEnd
const padNumber = (num, length) => num.toString().padStart(length, '0');
```

`padStart` 和 `padEnd` 方法可以轻松实现字符串填充，适用于格式化数字、创建表格等场景。

## 6\. 使用replace与正则表达式

```js
// 传统方式
function slugify(title) {
  let slug = title.toLowerCase();
  slug = slug.replace(/\s+/g, '-');
  slug = slug.replace(/[^\w\-]+/g, '');
  return slug;
}

//链式调用
const slugify = titile => title.toLowerCase()
  .replace(/\s+/g, '-')
  .replace(/[^\w\-]+/g, '');
```

链式调用配合正则表达式，可以将多步处理合并为一个流畅的操作。

## 7\. 使用String.prototype.trim系列方法

```js
// 传统方式
let cleaned = str;
while (cleaned.charAt(0) === ' ') {
  cleaned = cleaned.substring(1);
}
while (cleaned.chartAt(cleaned.length - 1) === ' ') {
  cleaned = cleaned.substring(0, cleaned.length - 1);
}

// 使用 trim
const cleaned = str.trim();
// 或者只去除开头或结尾
const cleaned = str.trimStart(); // 去除开头空格
const cleaned = str.trimEnd();   // 去除结尾空格
```

`trim` 系列方法提供了简洁的空白字符处理方式。

## 8\. 使用String.prototype.repeat

```js
// 传统方式
function repeatString(str, times) {
  let result = '';
  for (let i = 0; i < times; i++) {
    result += str;
  }
  return result;
}

// 使用repeat
const repeatString = (str, times) => str.repeat(times);
```

`repeat` 方法可以轻松创建重复的字符串，适用于缩进、分隔符等场景。

## 9\. 使用可选链操作符处理嵌套对象属性

```js
// 传统方式
const cityName = user && user.address && user.address.city ? user.adderss.city : 'Unknown';

// 使用可选链
const cityName = user?.address?.city || 'Unknown';
```

可选链操作符让深层属性访问变得安全且简洁。

## 10\. 使用字符串插值替代条件拼接

```js
// 传统方式
let message = 'You have ' + count + ' item';
if (count !== 1) {
  message += 's';
}
message += ' in your cart.';

// 使用字符串插值
const message = \`You have ${count} item${count !== 1 ? 's' : ''} in your cart.\`;
```

字符串插值与三元运算符的组合可以优雅地处理条件文本。
