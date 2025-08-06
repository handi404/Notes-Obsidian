在 React 中，可以通过多种方式返回多个 JSX 片段。以下是几种常见的实现方法：

### 1. 使用 `Fragment`
React 提供了一个 `Fragment` 组件，用于返回多个 JSX 元素而不需要额外的 DOM 元素包裹。

```jsx
import React from 'react';

function MyComponent() {
  return (
    <React.Fragment>
      <h1>Hello, World!</h1>
      <p>This is a paragraph.</p>
    </React.Fragment>
  );
}

export default MyComponent;
```

或者你可以使用简化的语法：

```jsx
function MyComponent() {
  return (
    <>
      <h1>Hello, World!</h1>
      <p>This is a paragraph.</p>
    </>
  );
}
```

### 2. 返回一个包含多个元素的数组
你还可以返回一个包含多个 JSX 元素的数组。在这种情况下，需要确保每个元素都有一个唯一的 `key` 属性：

```jsx
function MyComponent() {
  return [
    <h1 key="1">Hello, World!</h1>,
    <p key="2">This is a paragraph.</p>
  ];
}
```

### 3. 使用一个包裹元素
虽然不如前两种方法优雅，但你也可以简单地使用一个包裹元素（如 `div`）来返回多个元素：

```jsx
function MyComponent() {
  return (
    <div>
      <h1>Hello, World!</h1>
      <p>This is a paragraph.</p>
    </div>
  );
}
```

### 4. 使用自定义组件作为容器
你也可以创建一个自定义的容器组件来包裹多个 JSX 元素：

```jsx
function Container({ children }) {
  return <div>{children}</div>;
}

function MyComponent() {
  return (
    <Container>
      <h1>Hello, World!</h1>
      <p>This is a paragraph.</p>
    </Container>
  );
}
```

### 结论
最常用的方式是使用 `Fragment` 或空标签（`<>`）。这两种方式不会在最终的 DOM 中产生额外的元素，所以对于布局来说更加简洁。