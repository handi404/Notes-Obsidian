在 JavaScript 的 ES 模块系统中， `export default` 曾被许多开发者视为一种优雅的语法，它允许一个模块导出一个“默认”的主要功能或值。我们都写过这样的代码：

```
// MyComponent.js
export default function MyComponent() {
  // ...
}

// App.js
import MyAwesomeComponent from './MyComponent.js'; // 导入时可以任意命名
```

这种写法看起来简洁直观。然而，随着前端工程化的演进和项目规模的急剧扩张，越来越多的顶级前端团队和开源项目（如 Google、Airbnb、Vant 等）开始在其代码规范中明确禁止或不鼓励使用 `export default` 。

这并非无病呻吟的语法之争，而是基于长期项目维护、团队协作和工程效率的深思熟虑。

### 命名一致性：混乱的根源

`export default` 的最大问题在于它允许导入时随意命名。

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/btsCOHx9LAO8zDmJicD3ticfB95X5NiclKWINFiaqcnTxjuo4o8icn63A2n0vjbXEvic6qK0Cm4tnjOVbJ0zP21hpiaqA/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

在一个大型项目中，同一个组件或函数在不同文件里有了五花八门的名字。

相比之下，具名导出从根本上解决了这个问题。

```
import { MyComponent } from './MyComponent.js'; // 名字是固定的
import { MyComponent as MyAwesomeComponent } from './MyComponent.js'; // 可以重命名，但是是有意为之，而非无意之举
```

使用具名导出时，导入的名称必须与导出的名称完全匹配，这确保了在整个代码库中， `MyComponent` 始终是 `MyComponent` 。这种强制的一致性对于团队协作至关重要。

### 更好的 Tree-shaking 支持

Tree-shaking是现代前端打包工具用来移除未被使用代码以减小打包体积的关键技术。

虽然现代打包工具对 `export default` 的 Tree-shaking 处理已经相当智能，但具名导出在静态分析上具有天然优势。因为具名导出的依赖关系更加明确和静态，工具能更容易地分析出哪些代码是 dead code，从而进行更可靠的优化。

### 简化模块的再导出

在构建组件库或工具库时，我们经常使用一个 `index.js` 文件来统一导出所有子模块，这被称为“Barrel File”模式。

使用具名导出，这项工作非常简单：

```
export * from './Button';
export * from './Card';
```

但如果 `Button`, `Card` 等模块使用的是 `export default` ，事情就变得非常麻烦：

```
export { default as Button } from './Button';
export { default as Card } from './Card';
```

这种写法既冗长又笨拙，完全失去了简洁性。

如果一个文件真的、真的只有一个导出，并且其功能就是这个文件的全部意义所在（例如，一个配置文件），使用 `export default` 也是可以接受的。

技术选择本质上是一种权衡。 `export default` 提供了表面的便利，但在大型、长期的项目中，这种便利性被其带来的命名不一致、重构困难、API 不清晰等问题所抵消。

拥抱具名导出，构建出一个更健壮、更清晰、对开发者和工具都更友好的代码生态系统。