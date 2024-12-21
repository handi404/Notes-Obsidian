在 React 中添加 CSS 样式有多种方式，下面是几种常见的方式：

### 1. **使用外部 CSS 文件**
这是最常见的方法，类似于在普通 HTML 中使用 CSS。

- 创建一个 CSS 文件（例如 `App.css`）。
- 在 React 组件中引入这个 CSS 文件。

**步骤：**

1. 创建 `App.css` 文件，并在其中编写样式：
   ```css
   /* App.css */
   .container {
     background-color: lightblue;
     padding: 20px;
     text-align: center;
   }
   ```

2. 在你的 React 组件中导入这个 CSS 文件：
   ```jsx
   import React from 'react';
   import './App.css';

   function App() {
     return (
       <div className="container">
         <h1>Hello, World!</h1>
       </div>
     );
   }

   export default App;
   ```

### 2. **使用内联样式**
React 中的内联样式使用对象语法，并且属性名采用 camelCase 风格，而不是传统的 CSS 属性名称。

**示例：**

```jsx
function App() {
  const containerStyle = {
    backgroundColor: 'lightblue',
    padding: '20px',
    textAlign: 'center',
  };

  return (
    <div style={containerStyle}>
      <h1>Hello, World!</h1>
    </div>
  );
}

export default App;
```

### 3. **使用 CSS Modules**
CSS Modules 允许你将样式限定在特定组件中，避免全局污染。

**步骤：**

1. 创建一个 CSS 模块文件，文件名采用 `ComponentName.module.css` 的形式，比如 `App.module.css`。
2. 在组件中导入这个模块，并将样式作为对象属性使用。

**示例：**

1. 创建 `App.module.css`：
   ```css
   /* App.module.css */
   .container {
     background-color: lightblue;
     padding: 20px;
     text-align: center;
   }
   ```

2. 在组件中使用：
   ```jsx
   import React from 'react';
   import styles from './App.module.css';

   function App() {
     return (
       <div className={styles.container}>
         <h1>Hello, World!</h1>
       </div>
     );
   }

   export default App;
   ```

### 4. **使用 Styled Components**
Styled Components 是一种流行的 CSS-in-JS 库，允许你将样式直接写在 JavaScript 文件中。

**安装：**

```bash
npm install styled-components
```

**使用：**

```jsx
import React from 'react';
import styled from 'styled-components';

const Container = styled.div`
  background-color: lightblue;
  padding: 20px;
  text-align: center;
`;

function App() {
  return (
    <Container>
      <h1>Hello, World!</h1>
    </Container>
  );
}

export default App;
```

### 5. **使用 CSS-in-JS 库（例如 Emotion 或 Styled-JSX）**
除了 Styled Components，你还可以使用其他 CSS-in-JS 库，比如 [Emotion](https://emotion.sh/) 或 [Styled-JSX](https://github.com/vercel/styled-jsx)，这些库都支持将 CSS 样式直接写在 JavaScript 文件中。

每种方法都有其特定的使用场景，你可以根据项目需求选择合适的方式。