`useRef` 是 React 提供的一个 Hook，主要用于在函数组件中创建可变的引用（ref）。它可以持有一个可变的值，该值在组件的整个生命周期内保持不变，即使组件重新渲染也不会改变。`useRef` 常被用于访问 DOM 元素或在组件之间保持某个状态的持久性。

### 1. 基本语法

```javascript
const refContainer = useRef(initialValue);
```

- `initialValue` 是 `ref` 的初始值，可以是任何类型的值。
- `useRef` 返回一个对象，这个对象的 `current` 属性持有传递的初始值，并且可以读写。

### 2. 使用场景

#### 2.1 访问 DOM 元素

在类组件中，我们通常使用 `React.createRef` 来访问 DOM 元素。在函数组件中，`useRef` 提供了类似的功能。

**示例：通过 `useRef` 获取一个 DOM 元素的引用并操作它**

```javascript
import React, { useRef, useEffect } from 'react';

function FocusInput() {
  const inputRef = useRef(null);

  useEffect(() => {
    // 组件挂载后让 input 元素自动获取焦点
    inputRef.current.focus();
  }, []);

  return <input ref={inputRef} type="text" />;
}
```

在这个例子中：
- `useRef(null)` 创建了一个 `ref` 对象，并将其赋值给 `inputRef`。
- `inputRef` 的 `current` 属性被设置为 `<input>` 元素的 DOM 对象。
- 在 `useEffect` 中，使用 `inputRef.current.focus()` 使输入框在组件挂载后自动获取焦点。

#### 2.2 保持跨渲染的可变值

`useRef` 还可以用于存储跨渲染周期保持不变的可变值。与 `useState` 不同，`useRef` 的值发生变化时不会触发组件重新渲染。

**示例：保持组件间状态**

```javascript
import React, { useRef } from 'react';

function Timer() {
  const countRef = useRef(0);

  const increment = () => {
    countRef.current += 1;
    console.log(countRef.current);
  };

  return <button onClick={increment}>Increment Count</button>;
}
```

在这个例子中：
- `countRef.current` 持有一个可变的计数值。
- 每次点击按钮时，`countRef.current` 都会增加，但组件不会重新渲染。

#### 2.3 保存上一次渲染的值

你可以使用 `useRef` 来保存组件中某个状态的前一次值。

**示例：获取前一个渲染周期的值**

```javascript
import React, { useState, useEffect, useRef } from 'react';

function PreviousValue() {
  const [count, setCount] = useState(0);
  const prevCountRef = useRef();

  useEffect(() => {
    prevCountRef.current = count;
  }, [count]);

  return (
    <div>
      <h1>Now: {count}</h1>
      <h2>Before: {prevCountRef.current}</h2>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

在这个例子中：
- `prevCountRef` 被用来保存 `count` 的前一个值。
- 每次 `count` 变化时，`useEffect` 将 `count` 的当前值保存到 `prevCountRef.current` 中。
- `prevCountRef.current` 可以在任何时候访问到上一次的 `count` 值。

### 3. 使用注意事项

- **不引发重新渲染**：使用 `useRef` 改变 `current` 属性不会触发组件重新渲染，这与 `useState` 的行为不同。`useRef` 更适合那些需要在渲染周期之间保留的值，但又不会影响组件渲染的场景。

- **与 `useEffect` 结合使用**：`useRef` 常与 `useEffect` 一起使用，特别是在需要处理副作用或 DOM 操作时。例如，获取元素的宽高、处理外部库的实例等。

- **避免过度使用**：虽然 `useRef` 可以用来保存任何可变的值，但应避免将它当作“万能存储”。在需要组件重新渲染的场景下，应该使用 `useState`。如果不需要保存跨渲染的状态，则可以直接使用普通的局部变量。

### 4. 总结

`useRef` 是一个强大的 Hook，适用于需要跨渲染保持不变的引用或值的场景。它在函数组件中替代了类组件的 `createRef`，用于访问 DOM 元素，同时也可以用来保存前一次渲染的状态或其他不需要引发重新渲染的可变数据。在使用时，理解其与 `useState` 和 `useEffect` 的不同之处，能帮助你更有效地管理组件的状态和副作用。