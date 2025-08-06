在 React 中，`props`（是 "properties" 的缩写）是组件之间传递数据的一种机制。`props` 是组件的输入参数，用于将数据从父组件传递到子组件。它们是只读的，子组件不能修改从父组件接收到的 `props`。

### 1. **基本概念**

- **传递数据：** 父组件可以通过 `props` 将数据传递给子组件。
- **只读属性：** 子组件只能读取 `props` 的值，而不能对其进行修改。
- **函数参数：** `props` 类似于函数的参数，React 组件会使用这些参数来渲染内容。

### 2. **如何使用 `props`**

#### 1. **传递 `props`**

在父组件中，你可以通过 JSX 语法将数据传递给子组件。例如：

```jsx
function ParentComponent() {
  const message = "Hello from Parent!";
  
  return <ChildComponent message={message} />;
}
```

在上面的代码中，`ParentComponent` 通过 `message` 属性将数据传递给 `ChildComponent`。

#### 2. **在子组件中接收 `props`**

子组件可以通过函数参数来接收 `props`，并使用它们：

```jsx
function ChildComponent(props) {
  return <h1>{props.message}</h1>;
}
```

在上面的代码中，`ChildComponent` 接收 `props` 对象，并使用 `props.message` 渲染传递过来的消息。

### 3. **解构 `props`**

为了更简洁，通常会对 `props` 进行解构：

```jsx
function ChildComponent({ message }) {
  return <h1>{message}</h1>;
}
```

通过解构，你可以直接访问 `props` 中的属性，而不需要每次都使用 `props.` 语法。

### 4. **传递多个 `props`**

你可以传递多个 `props`，例如：

```jsx
function ParentComponent() {
  const message = "Hello from Parent!";
  const author = "John Doe";
  
  return <ChildComponent message={message} author={author} />;
}
```

子组件接收这些 `props` 并使用它们：

```jsx
function ChildComponent({ message, author }) {
  return (
    <div>
      <h1>{message}</h1>
      <p>From: {author}</p>
    </div>
  );
}
```

### 5. **默认 `props`**

你可以为组件定义默认 `props`，以防止父组件没有传递某些 `props` 时出错：

```jsx
function ChildComponent({ message, author }) {
  return (
    <div>
      <h1>{message}</h1>
      <p>From: {author}</p>
    </div>
  );
}

ChildComponent.defaultProps = {
  author: "Unknown Author",
};
```

如果父组件没有传递 `author`，则会使用默认值 `"Unknown Author"`。

### 6. **PropTypes（类型检查）**

React 提供了一种类型检查机制，你可以使用 `PropTypes` 来确保组件接收到的 `props` 类型正确。需要先安装 `prop-types` 库：

```bash
npm install prop-types
```

然后在组件中使用：

```jsx
import PropTypes from 'prop-types';

function ChildComponent({ message, author }) {
  return (
    <div>
      <h1>{message}</h1>
      <p>From: {author}</p>
    </div>
  );
}

ChildComponent.propTypes = {
  message: PropTypes.string.isRequired,
  author: PropTypes.string,
};
```

在上面的例子中，`message` 必须是一个字符串，并且是必需的，而 `author` 是可选的字符串。

### 7. **总结**

- `props` 是 React 组件之间传递数据的机制。
- 父组件通过 `props` 向子组件传递数据，子组件通过 `props` 读取数据。
- `props` 是不可变的，即子组件不能修改从父组件接收到的 `props`。
- 使用 `props` 使得组件更加灵活和可重用。