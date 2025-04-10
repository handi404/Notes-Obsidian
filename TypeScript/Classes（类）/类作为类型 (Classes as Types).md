一个 TypeScript 中非常基础且重要的概念：**类作为类型 (Classes as Types)**。

当你使用 `class` 关键字在 TypeScript 中定义一个类时，你实际上同时做了两件事：

1.  **创建了一个值 (Value):** 这个值就是类本身，也就是 **构造函数**。你可以用 `new` 关键字来调用它，创建类的实例（对象）。
2.  **创建了一个类型 (Type):** 这个类型与类同名，它代表了由该类创建的 **实例的形状 (shape)**。

这听起来有点抽象，我们通过例子来理解：

```typescript
class Point {
  x: number;
  y: number;

  // 构造函数 (这是“值”的部分，用于 new Point(...))
  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }

  // 方法
  distanceFromOrigin(): number {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }
}

// 1. 使用类名 'Point' 作为类型注解
// 这表示变量 `p1` 必须持有 Point 类的实例，
// 或者一个具有相同公共成员（属性 x, y 和方法 distanceFromOrigin）的对象。
let p1: Point;

// 2. 使用类的构造函数（“值”的部分）创建实例
p1 = new Point(5, 12);

// 3. 在需要该“类型”的地方使用类名
// 这个函数期望接收一个符合 Point 实例形状的对象
function logPoint(p: Point): void {
  console.log(`Coordinates: (${p.x}, ${p.y})`);
  // 可以安全地访问 Point 实例类型定义的公共成员
  console.log(`Distance from origin: ${p.distanceFromOrigin()}`);
}

logPoint(p1); // 传递 Point 的实例，完全符合类型要求

// --- 结构化类型系统（重要！）---

// 这个对象字面量恰好具有 Point 实例所需的所有公共成员
const p2 = {
  x: 3,
  y: 4,
  distanceFromOrigin: function() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }
};

logPoint(p2); // 也能工作！因为 TypeScript 是结构化类型系统
             // 只要形状匹配（有 x, y 属性和 distanceFromOrigin 方法，且类型兼容），
             // 它就被认为是 Point 类型的兼容形式。

// --- 私有/受保护成员的影响 ---

class SecurePoint {
  public x: number;
  public y: number;
  private secret: string; // 添加一个私有成员

  constructor(x: number, y: number, secret: string) {
    this.x = x;
    this.y = y;
    this.secret = secret;
  }

  distanceFromOrigin(): number {
      return Math.sqrt(this.x * this.x + this.y * this.y);
  }
}

const sp1 = new SecurePoint(1, 1, "mysecret");
let spRef: SecurePoint = sp1; // OK

const sp2 = { // 形状相同，但没有 SecurePoint 的 '血统'
    x: 2,
    y: 2,
    secret: "anothersecret",
    distanceFromOrigin: function() { /*...*/ return 0; }
};

// logPoint(sp1); // 这仍然可以工作，因为它只关心公共部分 Point 的形状

// 但是，不能将 sp2 赋值给 SecurePoint 类型
// spRef = sp2; // Error! Type '{...}' is not assignable to type 'SecurePoint'.
              // Property 'secret' is private in type 'SecurePoint' but not in type '{...}'
              // 私有(或受保护)成员的存在，使得类型检查不仅仅看结构，
              // 还要看它们是否源自同一个类声明 (引入了名义化类型的特点)。
```

**关键点总结:**

1.  **实例类型 (Instance Type):** 当你把类名用作类型时（例如 `let p: Point;` 或 `function log(p: Point)`），你指的是由该类创建的**实例**应该具有的**形状**。这个形状主要由类的**公共 (public)** 成员（属性和方法）决定。
2.  **结构化类型系统:** TypeScript 主要基于结构进行类型比较（“鸭子类型” - 如果它走路像鸭子，叫声像鸭子，那么它就是鸭子）。如果一个对象拥有 `Point` 类实例类型所要求的所有公共成员，并且类型兼容，那么即使它不是通过 `new Point()` 创建的，也可以在需要 `Point` 类型的地方使用。
3.  **`private` 和 `protected` 的影响:** 如果一个类包含 `private` 或 `protected` 成员，那么类型兼容性就不再是纯粹的结构化了。只有该类**本身**的实例或其**子类**（对于 `protected`）的实例才被认为是兼容的。这为 TypeScript 的结构化类型系统增加了一层**名义化类型 (Nominal Typing)** 的特征，因为成员的来源（哪个类定义的）变得重要了。
4.  **类与接口的关系:** 类定义的实例类型在很多方面可以像接口（Interface）一样使用。一个类甚至可以 `implements` 一个接口，来保证其实例满足接口定义的契约。反过来，如果一个类的实例结构恰好满足某个接口，它也可以被用在需要该接口的地方。

**简单来说：**

*   `class MyClass { ... }` 定义了一个**构造器 `MyClass`** 和一个**实例类型 `MyClass`**。
*   类型 `MyClass` 代表了 `new MyClass()` 会产生的对象的**公开**结构。
*   你可以把 `MyClass` 这个名字用在类型注解、函数参数、返回值等任何需要类型的地方，用来约束变量必须持有 `MyClass` 的实例或结构兼容的对象（除非有 `private` / `protected` 成员）。

理解类既是值（构造器）又是类型（实例形状）是掌握 TypeScript 面向对象编程和其类型系统交互方式的基础。