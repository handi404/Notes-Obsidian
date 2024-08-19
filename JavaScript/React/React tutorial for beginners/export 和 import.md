是的，在 JSX（JavaScript XML）中，`export` 和 `import` 的使用与普通 JavaScript 中的用法基本相同。它们在 React 中的组件开发中非常常见，因为 React 组件通常被定义在单独的模块中，然后通过 `export` 和 `import` 在不同的文件中复用这些组件。

### 1. `export` 在 JSX 中的用法

#### 命名导出（Named Export）
通常在一个模块中导出多个组件或其他导出项。

```jsx
// Button.jsx
import React from 'react';

export function Button(props) {
  return <button>{props.label}</button>;
}

export const buttonStyles = {
  primary: 'btn-primary',
  secondary: 'btn-secondary',
};
```

#### 默认导出（Default Export）
在模块中导出一个默认组件。

```jsx
// Header.jsx
import React from 'react';

export default function Header() {
  return <h1>This is the header</h1>;
}
```

### 2. `import` 在 JSX 中的用法

#### 导入命名导出
从其他模块中导入特定的组件或变量。

```jsx
// App.jsx
import React from 'react';
import { Button, buttonStyles } from './Button';

function App() {
  return (
    <div>
      <Button label="Click Me" />
      <p className={buttonStyles.primary}>This is a primary button style</p>
    </div>
  );
}

export default App;
```

#### 导入默认导出
从其他模块中导入默认导出的组件或函数。

```jsx
// App.jsx
import React from 'react';
import Header from './Header';

function App() {
  return (
    <div>
      <Header />
      <p>Welcome to my app!</p>
    </div>
  );
}

export default App;
```

#### 导入整个模块
使用 `* as` 语法导入整个模块的所有导出内容。

```jsx
// App.jsx
import React from 'react';
import * as ButtonModule from './Button';

function App() {
  return (
    <div>
      <ButtonModule.Button label="Click Me" />
      <p className={ButtonModule.buttonStyles.primary}>Styled with imported styles</p>
    </div>
  );
}

export default App;
```

### 在 React 项目中的常见用法
在 React 中，组件通常分布在不同的文件中，通过 `export` 导出组件，然后在需要使用这些组件的地方通过 `import` 导入。这种模块化的做法可以让代码更加结构化和易于维护。

例如，一个典型的 React 项目结构可能如下：

```
src/
│
├── components/
│   ├── Header.jsx
│   ├── Footer.jsx
│   └── Button.jsx
│
├── App.jsx
└── index.js
```

在 `App.jsx` 中，你可以通过 `import` 导入 `Header`、`Footer` 和 `Button` 组件来组合页面：

```jsx
// App.jsx
import React from 'react';
import Header from './components/Header';
import Footer from './components/Footer';
import { Button } from './components/Button';

function App() {
  return (
    <div>
      <Header />
      <Button label="Click Me" />
      <Footer />
    </div>
  );
}

export default App;
```

### 结论
在 JSX 中，`export` 和 `import` 的用法与普通 JavaScript 一致。通过这两个关键字，React 组件可以在不同的文件间轻松共享和复用，使代码更加模块化和维护良好。