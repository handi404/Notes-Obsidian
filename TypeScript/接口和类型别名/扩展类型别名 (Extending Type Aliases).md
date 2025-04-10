**扩展类型别名 (Extending Type Aliases)**。

这是一个有趣的话题，因为和接口（Interface）不同，**类型别名 (`type`) 本身不直接支持 `extends` 关键字**。你不能像写 `interface B extends A {}` 那样写 `type B extends A = ...`。

但是，我们仍然有非常强大的方法来**实现类似“扩展”的效果**，即基于一个已有的类型别名创建一个新的、包含更多属性或约束的类型别名。最核心的方式就是使用 **交叉类型 (Intersection Types)**，也就是 `&` 操作符。

**核心方法：交叉类型 (`&`)**

交叉类型 `&` 将多个类型合并为一个新类型。这个新类型**同时拥有**所有组成类型的成员。这与接口的 `extends` 在结果上非常相似，都是为了组合类型。

```typescript
// 1. 基础类型别名
type Person = {
  name: string;
  age: number;
};

// 2. "扩展" Person 来创建 Employee 类型别名
// 使用交叉类型 (&) 将 Person 的所有属性与新定义的属性合并
type Employee = Person & { // 注意这里的 &
  employeeId: string;
  department: string;
  // 可以添加方法签名
  report: () => void;
};

// 3. 使用 "扩展" 后的类型别名
const employee: Employee = {
  name: "Alice",
  age: 28,
  employeeId: "E67890",
  department: "Marketing",
  report: () => {
    console.log(`${employee.name} reporting from ${employee.department}`);
  },
};

employee.report(); // 调用方法

// Employee 类型也兼容 Person 类型
function processPerson(person: Person): void {
  console.log(`Processing person: ${person.name}`);
}

processPerson(employee); // OK! 因为 Employee 包含了 Person 的所有属性
```

**关键点：**

*   `Employee` 类型现在包含了 `Person` 的 `name` 和 `age` 属性，以及新增的 `employeeId`、`department` 和 `report` 方法。
*   `&` 操作符是这里的关键，它将 `Person` 类型和 `{ employeeId: string; department: string; report: () => void; }` 这个匿名对象类型合并了。

**与接口 `extends` 的对比和选择：**

虽然 `&` 实现了类似扩展的功能，但与 `interface extends` 相比，还是有一些关键区别：

1.  **语法:** `type Alias = TypeA & TypeB;` vs `interface InterfaceB extends InterfaceA {}`
2.  **扩展对象:**
    *   `interface extends` 只能扩展接口（或者类，在类继承的语境下）。
    *   `&` 可以组合几乎**任何类型**，包括：
        *   类型别名定义的对象类型
        *   接口定义的对象类型
        *   原始类型（虽然 `string & number` 会得到 `never`，但理论上可以写）
        *   联合类型 (`(A | B) & { extra: string }`)
        *   函数类型等
3.  **声明合并 (Declaration Merging):**
    *   **接口支持声明合并**。你可以多次声明同一个接口，TypeScript 会将它们的成员合并起来。
    *   **类型别名不支持声明合并**。你不能多次声明同名的 `type`。
    *   *示例:*
        ```typescript
        // 接口可以合并
        interface Box { width: number; }
        interface Box { height: number; }
        const box: Box = { width: 10, height: 20 }; // OK

        // 类型别名不可以合并
        // type BoxType = { width: number; }
        // type BoxType = { height: number; } // Error: Duplicate identifier 'BoxType'.
        ```
4.  **错误信息:** 有时，当交叉类型变得非常复杂或产生冲突时，TypeScript 的错误信息可能不如接口继承的错误信息那么直观（尽管 TS 编译器在这方面一直在改进）。
5.  **递归类型:** 在定义某些复杂的递归类型时，类型别名有时比接口更灵活。
6.  **映射类型和条件类型:** `type` 可以直接用来定义复杂的映射类型 (Mapped Types) 和条件类型 (Conditional Types)，而 `interface` 主要用于定义对象结构。

**何时使用 `&` 来 "扩展" 类型别名？**

*   当你主要使用 `type` 来定义你的类型时。
*   当你需要组合的类型不仅仅是对象形状，或者来源多样（比如混合类型别名和接口）。
*   当你不需要或不希望利用接口的声明合并特性时。
*   当你正在使用映射类型或条件类型，并想在其结果的基础上添加属性时。

**示例：组合接口和类型别名**

```typescript
interface Clickable {
  onClick: () => void;
}

type Position = {
  x: number;
  y: number;
};

// 使用交叉类型组合接口和类型别名
type ClickablePosition = Clickable & Position & {
  id: string; // 添加额外属性
};

const point: ClickablePosition = {
  x: 100,
  y: 200,
  id: "point-1",
  onClick: () => console.log("Position clicked!"),
};
```

**总结:**

虽然类型别名 (`type`) 没有 `extends` 关键字，但你可以通过 **交叉类型 (`&`)** 非常有效地实现类型的“扩展”和组合。`&` 操作符允许你将一个或多个现有类型（无论是类型别名、接口还是其他类型）的成员合并到一个新的类型别名中。

理解 `&` 和 `interface extends` 之间的差异（尤其是在声明合并和适用范围上）有助于你在具体场景下选择最合适的方式来组织和复用你的 TypeScript 类型。