TypeScript 中的 **可重用接口 (Reusable Interfaces)**。

简单来说，**接口（Interface）** 在 TypeScript 中就像一个 **“契约”** 或 **“蓝图”**，它定义了一个对象**应该**长什么样（即包含哪些属性和方法，以及它们的类型）。

而 **“可重用”** 的核心思想就是：**定义一次，多处使用**。这能极大地提高代码的 **一致性、可维护性和可读性**。

想象一下，如果你要在代码的很多地方都用到表示“用户”的数据结构，每次都手动写一遍 `{ id: number; name: string; email?: string }` 会非常繁琐且容易出错。一旦需要修改（比如给用户增加 `age` 属性），你就得找到所有用到这个结构的地方去改，简直是噩梦！

可重用接口就是解决这个问题的利器。

以下是实现接口可重用的几种核心方式：

**1. 基础：定义与使用**

最基本的方式就是定义一个接口，然后在需要的地方引用它来约束变量、函数参数或函数返回值。

```typescript
// 1. 定义接口 (定义一次)
interface User {
  id: number;
  name: string;
  email?: string; // 可选属性
  registerDate: Date;
}

// 2. 多处使用
function processUser(user: User): void {
  console.log(`Processing user: ${user.name}, registered on ${user.registerDate.toLocaleDateString()}`);
  if (user.email) {
    console.log(`Email: ${user.email}`);
  }
}

const newUser: User = {
  id: 1,
  name: "Alice",
  registerDate: new Date(),
  // email: "alice@example.com" // email 是可选的
};

const oldUser: User = {
    id: 2,
    name: "Bob",
    email: "bob@example.com",
    registerDate: new Date("2022-01-01"),
}

processUser(newUser);
processUser(oldUser);

// 函数返回值也可以使用接口
function fetchUser(id: number): User {
    // ... 实际的获取逻辑
    return { id, name: `User ${id}`, registerDate: new Date() };
}
```

**关键点：** `User` 接口定义了一次，`processUser` 函数参数、`newUser` 变量、`oldUser` 变量以及 `fetchUser` 函数的返回值都重用了这个“契约”。

**2. 接口继承 (`extends`)**

当一个接口需要包含另一个接口的所有成员，并可能添加额外成员时，可以使用 `extends` 关键字。这就像类的继承，但用于类型契约。

```typescript
interface Person {
  name: string;
  age: number;
}

// Admin 继承了 Person 的所有属性，并添加了 role
interface Admin extends Person {
  role: 'admin' | 'superadmin';
  permissions: string[];
}

const adminUser: Admin = {
  name: "Charlie",
  age: 30,
  role: "admin",
  permissions: ["read", "write"],
};

function greet(person: Person) { // 可以接受 Person 或 Admin 类型
    console.log(`Hello, ${person.name}`);
}

greet(adminUser); // OK, 因为 Admin "is a" Person
```

**关键点：** `Admin` 重用了 `Person` 的定义，避免了重复声明 `name` 和 `age`。修改 `Person` 会自动影响 `Admin`。

**3. 接口组合（作为属性类型）**

一个接口的属性类型可以是另一个接口。这表示一种“拥有”或“包含”的关系。

```typescript
interface Address {
  street: string;
  city: string;
  zipCode: string;
}

interface Company {
  name: string;
  legalAddress: Address; // 公司 "拥有一个" 地址
  staffCount: number;
}

const myCompany: Company = {
  name: "Tech Corp",
  legalAddress: { // 这个对象必须符合 Address 接口
    street: "123 Main St",
    city: "Anytown",
    zipCode: "12345",
  },
  staffCount: 100,
};
```

**关键点：** `Company` 接口通过 `legalAddress` 属性重用了 `Address` 接口，使得地址结构标准化。

**4. 泛型接口 (`<T>`)**

这是实现高级别可重用性的强大武器。泛型接口允许你在定义接口时不指定某些内部类型，而在使用接口时再具体指定。

```typescript
// 定义一个通用的 API 响应结构
interface ApiResponse<T> { // T 是一个类型参数 (占位符)
  success: boolean;
  data: T; // data 的类型由使用时决定
  message?: string;
}

// 使用时指定具体的 data 类型
interface Product {
  id: number;
  name: string;
  price: number;
}

// 获取产品列表的响应，data 是 Product 数组
const productResponse: ApiResponse<Product[]> = {
  success: true,
  data: [
    { id: 1, name: "Laptop", price: 1200 },
    { id: 2, name: "Mouse", price: 25 },
  ],
};

// 获取用户信息的响应，data 是 User 对象 (假设 User 接口已定义)
const userResponse: ApiResponse<User> = {
    success: true,
    data: {
        id: 101,
        name: "Eve",
        registerDate: new Date()
    }
}
```

**关键点：** `ApiResponse<T>` 这个接口骨架是可重用的。无论 API 返回的是产品列表、用户信息还是其他任何数据结构，都可以用 `ApiResponse` 来包裹，只需在 `T` 的位置传入具体的类型即可。

**总结:**

可重用接口是 TypeScript 中构建健壮、可维护应用程序的基础。通过 **基础定义、继承 (`extends`)、组合（属性类型）和泛型 (`<T>`)**，你可以创建出灵活、一致且易于理解的类型契约，从而：

*   **减少代码重复 (DRY)**
*   **提高类型安全性**
*   **增强代码可读性**
*   **简化重构和维护**

在现代 TypeScript 开发中，熟练运用这些技巧来创建可重用接口是必备的核心技能。