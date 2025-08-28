多年来，我们开发者在处理复杂的条件逻辑中，一直依赖着 `if-else` 的层层嵌套和功能有限的 `switch` 语句。

我们写过“if-else 地狱”，也吐槽过 `switch` 无法穿透数据结构的窘境。

但现在，这一切可能要终结了。

TC39 技术委员会已将“模式匹配”提案推进到了 Stage 3 阶段，这意味着该特性已经设计完毕，只待各大浏览器厂商实现，就将正式成为 ECMAScript 2025 标准的一部分。

这不仅仅是一个语法糖，它是一种全新的思维方式，将彻底改变我们编写条件逻辑的习惯。

### 什么是模式匹配？

如果你接触过 Rust、Scala、Elixir 或 F# 等函数式编程语言，一定会对模式匹配不陌生。它是一种强大的控制流结构，允许我们将一个值与一系列“模式”进行比较，并根据匹配成功的模式执行相应的代码。

### 告别冗长：全新的 match 语法

模式匹配引入了新的关键字 `match` ，它作为一个表达式而非语句存在，这意味着它会返回一个值。

基本语法如下：

```js
const result = match (someValue) {
  when (pattern1) { /* ... do something ... */ }
  when (pattern2) { /* ... do something ... */ }
  // ... more patterns
  when (_) { /* 相当于 default */ }
}
```

现在，让我们通过几个例子，看看它如何干掉 `if-else` 和 `switch` 。

#### 取代简单的 switch

传统的 `switch` 写法，繁琐的 `break` 是永远的痛。

```js
// 旧方法: switch
function getDayOfWeek(day) {
  let dayName;
  switch (day) {
    case 0:
      dayName = 'Sunday';
      break;
    case 6:
      dayName = 'Saturday';
      break;
    default:
      dayName = 'Weekday';
      break;
  }
  return dayName;
}
```

使用模式匹配，代码更简洁、更具声明性，且没有 `break` 的烦恼。

```js
// 新方法：match
function getDayOfWeek(day) {
  return match (day) {
    when (0) { 'Sunday' }
    when (6) { 'Saturday' }
    when (_) { 'Weekday' } // \`_\` 是通配符，相当于 default
  };
}
```

#### 秒杀 if-else 地狱

想象一下处理一个 API 返回的数据，我们需要根据不同的 `status` 和 `data` 内容来做不同的事。

```js
// 旧方法: if-else 地狱
function handleResponse(response) {
  if (response.status === 200) {
    if (response.data && response.data.id) {
      console.log(`Success! Received data for user: ${response.data.id}`);
    } else {
      console.log('Sucess, but no data.');
    }
  } else if (response.status >= 500) {
    console.error(`Server Error: ${response.error.message}`);
  } else {
    console.log('Unknown status.');
  }
}
```

这段代码嵌套、冗长，可读性极差，现在看看模式匹配如何将其夷为平地：

```js
// 新方法：match
function handleResponse(response) {
 match (response) {
    // 匹配结构并解构出 data.id
    when ({ status: 200, data: { id } }) {
      console.log(\`Success! Received data for user: ${id}\`);
    }
    when ({ status: 200 }) {
      console.log('Success, but no data.');
    }
    when ({ status: s } when s >= 500) {
      console.error(\`Server Error: ${response.error.message}\`);
    }
    when (_) {
      console.log('Unknown status.');
    }
  }
}
```

对于简单的布尔判断， `if (user.isLoggedIn) { ... }` 依然是最清晰的选择，对于简单的原始值判断， `switch` 也许还能一用。

但是，对于处理复杂数据结构、多重条件和状态机的场景，模式匹配将是毫无疑问的王者。它将消灭那些超过两层嵌套的 `if-else` ，让我们的代码库变得更加健壮、优雅和易于维护。

模式匹配不是一个简单的补充，它是 JavaScript 语言进化的一大步。它鼓励我们用更声明式、更函数式的方式去思考问题，将我们从繁琐的命令式条件判断中解放出来。
