在 TypeScript 中创建一个 **数据资源类 (Data Resource Class)** 来封装 **获取数据 (Fetching Data)** 的逻辑。

这是一种常见的模式，尤其是在构建需要与后端 API 交互的前端或 Node.js 应用时。目标是创建一个**可重用、类型安全且易于管理**的方式来处理特定类型数据（如用户、产品、文章等）的 CRUD (创建、读取、更新、删除) 操作或其他数据请求。

**核心思想：**

1.  **封装 (Encapsulation):** 将与特定数据资源（例如 `/api/users`）相关的所有 API 请求逻辑（URL 构建、HTTP 方法、请求头、请求体处理、响应解析、错误处理）都集中到一个类中。
2.  **可重用性 (Reusability):** 定义一次类，就可以在应用的多个地方实例化和使用它来操作该资源。
3.  **类型安全 (Type Safety):** 利用 TypeScript 的泛型和接口来确保请求参数、请求体和响应数据的类型正确，减少运行时错误。
4.  **抽象 (Abstraction):** 对类的使用者隐藏底层的 `fetch` API 或 HTTP 客户端库（如 `axios`）的实现细节。

**基本步骤与示例：**

假设我们要创建一个用于管理 `User` 资源的类，`User` 的数据结构如下：

```typescript
// 1. 定义数据结构的接口
interface User {
  id: number;
  name: string;
  email: string;
  isActive: boolean;
}
```

现在，我们创建一个 `UserResource` 类：

```typescript
// 2. 创建数据资源类
class UserResource {
  // 通常需要一个基础 URL
  private baseUrl: string;

  constructor(baseUrl: string = '/api') { // 可以提供默认值或从配置注入
    this.baseUrl = baseUrl;
  }

  // --- 读取操作 ---

  /**
   * 获取所有用户
   * @returns Promise<User[]> 用户数组
   */
  async getAll(): Promise<User[]> {
    try {
      const response = await fetch(`${this.baseUrl}/users`);
      if (!response.ok) {
        // 更精细的错误处理可以解析 response.status 等
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data: User[] = await response.json(); // 明确指定响应数据的类型
      return data;
    } catch (error) {
      console.error('Failed to fetch users:', error);
      // 可以选择重新抛出错误，或返回空数组/特定错误对象
      throw error; // 或者 return [];
    }
  }

  /**
   * 根据 ID 获取单个用户
   * @param id 用户 ID
   * @returns Promise<User | null> 用户对象或 null (如果未找到)
   */
  async getById(id: number): Promise<User | null> {
    try {
      const response = await fetch(`${this.baseUrl}/users/${id}`);
      if (response.status === 404) {
        return null; // 明确处理 404 Not Found
      }
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data: User = await response.json();
      return data;
    } catch (error) {
      console.error(`Failed to fetch user with id ${id}:`, error);
      throw error; // 或者 return null;
    }
  }

  // --- 创建操作 ---

  /**
   * 创建一个新用户
   * @param userData 要创建的用户数据 (通常不包含 id)
   * @returns Promise<User> 创建成功后的用户对象 (通常包含新生成的 id)
   */
  async create(userData: Omit<User, 'id'>): Promise<User> {
    // Omit<User, 'id'> 表示 User 类型中除了 'id' 之外的所有属性
    try {
      const response = await fetch(`${this.baseUrl}/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // 可能还需要认证头等
          // 'Authorization': 'Bearer YOUR_TOKEN'
        },
        body: JSON.stringify(userData),
      });
      if (!response.ok) { // 通常 201 Created 是成功，但也可能是 200 OK
         throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const createdUser: User = await response.json();
      return createdUser;
    } catch (error) {
      console.error('Failed to create user:', error);
      throw error;
    }
  }

  // --- 更新操作 ---

  /**
   * 更新一个现有用户
   * @param id 要更新的用户 ID
   * @param updates 要更新的字段 (Partial<User> 表示 User 的部分属性)
   * @returns Promise<User> 更新后的用户对象
   */
  async update(id: number, updates: Partial<Omit<User, 'id'>>): Promise<User> {
    try {
      const response = await fetch(`${this.baseUrl}/users/${id}`, {
        method: 'PATCH', // 或者 'PUT'，取决于 API 设计
        headers: {
          'Content-Type': 'application/json',
          // 'Authorization': 'Bearer YOUR_TOKEN'
        },
        body: JSON.stringify(updates),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const updatedUser: User = await response.json();
      return updatedUser;
    } catch (error) {
      console.error(`Failed to update user ${id}:`, error);
      throw error;
    }
  }

  // --- 删除操作 ---

  /**
   * 删除一个用户
   * @param id 要删除的用户 ID
   * @returns Promise<void> 操作成功则 resolve，失败则 reject
   */
  async delete(id: number): Promise<void> {
    try {
      const response = await fetch(`${this.baseUrl}/users/${id}`, {
        method: 'DELETE',
        headers: {
          // 'Authorization': 'Bearer YOUR_TOKEN'
        },
      });
      // 对于 DELETE，通常 204 No Content 或 200 OK 表示成功
      if (!response.ok && response.status !== 204) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      // DELETE 请求通常没有响应体，或响应体不重要
    } catch (error) {
      console.error(`Failed to delete user ${id}:`, error);
      throw error;
    }
  }
}

// --- 如何使用 ---
// 3. 实例化并使用类
const userApi = new UserResource('https://my-api.com/v1'); // 可以传入不同的 API 基础 URL

async function manageUsers() {
  try {
    // 获取所有用户
    const users = await userApi.getAll();
    console.log('All users:', users);

    // 创建新用户
    const newUser = await userApi.create({
      name: 'Alice',
      email: 'alice@example.com',
      isActive: true,
    });
    console.log('Created user:', newUser);

    // 获取刚创建的用户
    const fetchedUser = await userApi.getById(newUser.id);
    console.log('Fetched user:', fetchedUser);

    // 更新用户
    if (fetchedUser) {
      const updatedUser = await userApi.update(fetchedUser.id, { isActive: false });
      console.log('Updated user:', updatedUser);
    }

    // 删除用户
    await userApi.delete(newUser.id);
    console.log(`User ${newUser.id} deleted.`);

  } catch (error) {
    console.error('An error occurred during user management:', error);
  }
}

manageUsers();
```

**进阶：使用泛型创建更通用的资源类**

上面的 `UserResource` 只能处理 `User`。我们可以创建一个**泛型**的 `ApiResource<T>` 类，让它可以处理任何类型的数据资源，只需要在实例化时指定类型 `T` 和资源路径。

```typescript
// 1. 定义一个基础接口，约束资源必须有 ID (根据需要调整)
interface Resource {
  id: number | string; // ID 类型可能是 number 或 string
}

// 2. 创建泛型资源类
class ApiResource<T extends Resource> { // 约束 T 必须符合 Resource 接口
  private baseUrl: string;
  private resourcePath: string; // 例如 'users', 'products'

  constructor(resourcePath: string, baseUrl: string = '/api') {
    this.resourcePath = resourcePath;
    this.baseUrl = baseUrl;
  }

  private getEndpoint(id?: T['id']): string { // T['id'] 获取 id 属性的类型
    return id !== undefined
      ? `${this.baseUrl}/${this.resourcePath}/${id}`
      : `${this.baseUrl}/${this.resourcePath}`;
  }

  // --- CRUD 方法 (使用泛型 T) ---

  async getAll(): Promise<T[]> {
    // ... fetch logic using this.getEndpoint() ...
    // const data: T[] = await response.json();
    // return data;
    // (实现细节类似上面，只是将 User 换成 T)
    const response = await fetch(this.getEndpoint());
    if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    return await response.json() as T[];
  }

  async getById(id: T['id']): Promise<T | null> {
    // ... fetch logic using this.getEndpoint(id) ...
    const response = await fetch(this.getEndpoint(id));
    if (response.status === 404) return null;
    if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    return await response.json() as T;
  }

  // 使用 Omit<T, 'id'> 或定义专门的 CreateDto<T> 类型
  async create(itemData: Omit<T, 'id'>): Promise<T> {
    // ... fetch POST logic ...
    const response = await fetch(this.getEndpoint(), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(itemData),
    });
    if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    return await response.json() as T;
  }

  async update(id: T['id'], updates: Partial<Omit<T, 'id'>>): Promise<T> {
     // ... fetch PATCH/PUT logic ...
    const response = await fetch(this.getEndpoint(id), {
        method: 'PATCH', // or PUT
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updates),
    });
    if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
    return await response.json() as T;
  }

  async delete(id: T['id']): Promise<void> {
    // ... fetch DELETE logic ...
    const response = await fetch(this.getEndpoint(id), { method: 'DELETE' });
     if (!response.ok && response.status !== 204) {
        throw new Error(`HTTP error! Status: ${response.status}`);
     }
  }
}

// --- 如何使用泛型类 ---
// 3. 定义 Product 接口
interface Product extends Resource { // 确保 Product 符合 Resource 约束
  id: string; // Product ID 是 string
  name: string;
  price: number;
}

// 4. 实例化泛型类来处理不同资源
const userApiGeneric = new ApiResource<User>('users', 'https://my-api.com/v1');
const productApiGeneric = new ApiResource<Product>('products', 'https://my-api.com/v1');

async function manageProducts() {
  try {
    const products = await productApiGeneric.getAll();
    console.log('All products:', products);

    const newProduct = await productApiGeneric.create({ name: "Laptop", price: 1200 });
    console.log('Created product:', newProduct);

    await productApiGeneric.delete(newProduct.id);
    console.log('Product deleted.');

  } catch (error) {
    console.error('Product management error:', error);
  }
}

manageProducts();
// 现在可以用 userApiGeneric 操作用户，用 productApiGeneric 操作产品
```

**总结:**

创建数据资源类（无论是特定资源的还是泛型的）是组织 TypeScript 应用中 API 交互逻辑的有效方式。它利用类的封装性、TypeScript 的类型系统（接口、泛型）以及 `async/await` 来提供：

*   **结构清晰:** 每个资源的管理逻辑都在一个地方。
*   **类型安全:** 减少因类型错误导致的 bug。
*   **代码复用:** 避免在各处重复编写 `fetch` 调用和处理逻辑。
*   **可维护性:** 修改 API 端点或处理逻辑时，只需修改对应的资源类。

泛型版本提供了更高的抽象和代码复用度，特别适合需要处理多种相似数据资源的应用。