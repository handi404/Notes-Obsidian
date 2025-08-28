学习了很多Typescript 高级类型， 但是看到一堆的尖括号、问号、冒号、 `infer` ……，就无从下手， 默默的写上了 `any`...

还感叹一句， 还是 any 大法好！

还记得当初学习Typescript时， 看到这样的代码：

```ts
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : never;
```

`infer R` 是啥？为什么看起来像有人不小心敲了一堆标点符号？

如果你也被高级类型吓过——放心，你不是一个人。

甚至很多老手，第一次接触 条件类型（Conditional Types）和 `infer` 时，也是满脸问号。

但好消息是：

高级类型并不是玄学，它们只是 **用几条固定套路在玩** 。

3 条规则 + 真实案例，来读懂它们。

## 规则 1：条件类型 = 类型层面的 if…else

如果你会写 JavaScript 的 if…else，你就能理解 TypeScript 的条件类型。

语法长这样：

```ts
type MyType<T> = T extends SomeCondition ? TrueType : FalseType;
```

比如我们要判断一个类型是不是字符串：

```ts
type IsString<T> = T extends string ? true : false;

type A = IsString<'hello'>; // true
type B = IsString<123>;     // false
```

📌 理解要点：

左边的类型能赋值给右边，就走 `?` 后面的分支，否则走 `:` 后面的分支。

有点像 JavaScript 里：

```ts
if (typeof x === 'string') { ... }
```

实际开发里，这能做非常实用的类型约束，比如我们做一个 `Button` 组件， `loading` 状态下禁止点击, 根据 `loading` 状态切换组件属性：

```ts
type ButtonProps<T extends boolean> = {
  loading: T;
} & (T extends true
  ? { onClick?: never; disabled: true }
  : { onClick: () => void; disabled?: boolean }
);
```

这样：

- `loading` 为 `true` 时， `onClick` 会被禁止
- `loading` 为 `false` 时， `onClick` 必须提供

这就是类型层面的“动态规则”

## 规则 2：分布式条件类型 = 自动“循环”联合类型

当条件类型的参数是裸类型（naked type）时，TypeScript 会对联合类型逐个分发处理。

比如：

```ts
type ToArray<T> = T extends any ? T[] : never;

type Result = ToArray<'a' | 'b' | 'c'>;
// 'a'[] | 'b'[] | 'c'[]
```

它的运行逻辑其实是：

- 先拿 `'a'` 判断 → `'a'[]`
- 再拿 `'b'` 判断 → `'b'[]`
- 再拿 `'c'` 判断 → `'c'[]`
- 最后把结果合并成 `'a'[] | 'b'[] | 'c'[]`

如果你不想分发，可以用 `[]` 包起来阻止它：

```ts
type NoDistribute<T> = [T] extends [any] ? T[] : never;

type R2 = NoDistribute<'a' | 'b' | 'c'>;
// ('a' | 'b' | 'c')[]
```

一个很典型的应用是过滤联合类型,过滤掉 `null / undefined`:

```ts
type NonNullable<T> = T extends null | undefined ? never : T;

type Clean = NonNullable<string | null | number | undefined>;
// string | number
```

这也是内置工具类型 `Exclude` 、 `Extract` 背后的原理。

## 规则 3：infer = 类型模式匹配 + 提取

`infer` 的意思是：

「我不知道这个类型具体是什么，但 TypeScript 你帮我推断出来，并把它存到一个变量里。」

比如我们要获取一个函数的返回值类型：

```ts
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : never;

function getName(): string { return "John"; }

type NameType = ReturnType<typeof getName>; // string
```

又比如提取数组元素类型：

```ts
type ArrayElement<T> = T extends (infer U)[] ? U : never;

type Str = ArrayElement<string[]>; // string
type Num = ArrayElement<number[]>; // number
type NotArray = ArrayElement<boolean>; // never
```

甚至可以提取 React 组件的 props：

```ts
type PropsOf<T> = T extends React.ComponentType<infer P> ? P : never;
```

## 进阶：映射类型（Mapped Types）

映射类型就像 `for...in` 循环，但用在类型属性上：

```ts
type Optional<T> = { [K in keyof T]?: T[K] };
```

还能结合条件类型做更强的类型变换，比如过滤掉方法：

```ts
type NonFunctionPropertyNames<T> = {
  [K in keyof T]: T[K] extends Function ? never : K
}[keyof T];

type NonFunctionProperties<T> = Pick<T, NonFunctionPropertyNames<T>>;
```

## 总结：高级类型的核心思维

记住这 3 条规则，高级类型就不再可怕：

- 条件类型 = 类型版 `if…else`
- 分布式条件类型 = 对联合类型逐个处理（naked type 时才会发生）
- `infer` \= 类型模式匹配并提取

掌握它们，你就能看懂 TypeScript 内置的几乎所有工具类型： `Partial` 、 `Exclude` 、 `ReturnType` 、 `Parameters` ……
