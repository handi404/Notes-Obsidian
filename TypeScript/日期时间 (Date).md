TypeScript 中的**日期时间 (Date)** 处理。

在 TypeScript (以及 JavaScript) 中，处理日期和时间的主要方式是使用内置的 `Date` 对象。需要注意的是，TypeScript 本身并没有为 `Date` 对象添加额外的类型功能，它直接使用了 JavaScript 环境（浏览器或 Node.js）提供的 `Date` 对象。TypeScript 的作用在于为这些对象的使用提供了类型安全。

**1. `Date` 对象的基本使用**

`Date` 对象用于处理日期和时间。它可以表示从 1970 年 1 月 1 日 00:00:00 UTC (Unix 纪元) 开始计算的一个特定时间点，单位是毫秒。

**创建 `Date` 对象：**

有以下几种主要方式创建 `Date` 实例：

*   **无参数 - 当前日期和时间：**
    ```typescript
    const now: Date = new Date();
    console.log(now); // 输出类似: 2023-10-27T08:30:00.123Z (ISO 8601 格式, UTC)
    ```

*   **时间戳 (毫秒数)：**
    ```typescript
    const epochTime: Date = new Date(0); // Unix 纪元开始时间
    console.log(epochTime); // 1970-01-01T00:00:00.000Z

    const specificTime: Date = new Date(1678886400000); // 2023-03-15T12:00:00.000Z
    console.log(specificTime);
    ```

*   **日期字符串 (Date String)：**
    这种方式的解析行为可能因浏览器/环境而异，**需要特别小心**。推荐使用 ISO 8601 格式 (`YYYY-MM-DDTHH:mm:ss.sssZ`) 以获得最佳兼容性。
    ```typescript
    const fromISOString: Date = new Date("2024-01-15T10:00:00.000Z"); // UTC 时间
    console.log(fromISOString);

    const fromShortDate: Date = new Date("2024-01-15"); // 通常解析为当地时间的午夜
    console.log(fromShortDate); // 时区取决于你的环境

    // 不推荐的格式，可能导致解析不一致
    // const fromAmbiguousString: Date = new Date("01/15/2024");
    ```
    **注意：** 解析非 ISO 8601 格式的日期字符串时，行为可能不可靠，强烈建议避免或使用专门的日期库。

*   **年、月、日等分量 (本地时间)：**
    注意月份是从 `0` (一月) 到 `11` (十二月) 的。
    ```typescript
    // new Date(year, monthIndex, day?, hours?, minutes?, seconds?, milliseconds?)
    const specificDate: Date = new Date(2024, 0, 15); // 2024年1月15日 (0 代表一月)
    console.log(specificDate);

    const specificDateTime: Date = new Date(2024, 0, 15, 14, 30, 0); // 2024年1月15日 14:30:00
    console.log(specificDateTime);
    ```

**2. 获取日期和时间的分量**

`Date` 对象提供了多种方法来获取其各个部分：

*   `getFullYear()`: 获取四位数的年份 (例如 `2024`)。
*   `getMonth()`: 获取月份 (0-11，0 代表一月)。
*   `getDate()`: 获取月份中的第几天 (1-31)。
*   `getDay()`: 获取星期几 (0-6，0 代表星期日)。
*   `getHours()`: 获取小时 (0-23)。
*   `getMinutes()`: 获取分钟 (0-59)。
*   `getSeconds()`: 获取秒数 (0-59)。
*   `getMilliseconds()`: 获取毫秒数 (0-999)。
*   `getTime()`: 获取从 Unix 纪元开始至今的毫秒数 (时间戳)。
*   `getTimezoneOffset()`: 获取本地时间与 UTC 时间之间的时差，单位是分钟。

```typescript
const today: Date = new Date();

console.log(`Year: ${today.getFullYear()}`);
console.log(`Month: ${today.getMonth() + 1}`); // 注意 +1
console.log(`Day of month: ${today.getDate()}`);
console.log(`Day of week: ${today.getDay()}`); // 0 (Sun) - 6 (Sat)
console.log(`Hours: ${today.getHours()}`);
console.log(`Minutes: ${today.getMinutes()}`);
console.log(`Seconds: ${today.getSeconds()}`);
console.log(`Milliseconds: ${today.getMilliseconds()}`);
console.log(`Timestamp: ${today.getTime()}`);
```

**3. 设置日期和时间的分量**

类似地，也有相应的 `set` 方法，它们会**直接修改原始的 `Date` 对象** (mutable)。

*   `setFullYear(year, month?, day?)`
*   `setMonth(monthIndex, day?)`
*   `setDate(day)`
*   `setHours(hours, minutes?, seconds?, ms?)`
*   `setMinutes(minutes, seconds?, ms?)`
*   `setSeconds(seconds, ms?)`
*   `setMilliseconds(ms)`
*   `setTime(milliseconds)`

```typescript
const eventDate: Date = new Date();
console.log("Original:", eventDate.toString()); // 显示当前时间

eventDate.setFullYear(2025);
eventDate.setMonth(11); // 11 代表十二月
eventDate.setDate(25);
eventDate.setHours(10, 0, 0, 0); // 设置为 10:00:00.000

console.log("Modified:", eventDate.toString()); // 显示 2025-12-25 10:00:00 (本地时间)
```

**4. 格式化日期**

内置的 `Date` 对象格式化能力有限，但有一些基本方法：

*   `toString()`: 返回一个表示该日期的字符串 (格式因实现而异，通常是本地时区)。
*   `toDateString()`: 返回日期部分的字符串 (例如 "Fri Oct 27 2023")。
*   `toTimeString()`: 返回时间部分的字符串 (例如 "16:30:00 GMT+0800 (China Standard Time)")。
*   `toLocaleDateString(locales?, options?)`: 返回日期部分的本地化字符串。
*   `toLocaleTimeString(locales?, options?)`: 返回时间部分的本地化字符串。
*   `toLocaleString(locales?, options?)`: 返回日期和时间部分的本地化字符串。
*   `toISOString()`: 返回 ISO 8601 格式的字符串 (例如 "2023-10-27T08:30:00.123 Z")，总是 UTC 时间。**这是在不同系统间交换日期的推荐格式。**
*   `toUTCString()`: 返回一个根据世界时约定的字符串格式。
*   `toJSON()`: 与 `toISOString()` 行为类似，常用于 `JSON.stringify()`。

```typescript
const d: Date = new Date(2024, 0, 20, 15, 30, 0); // 2024-01-20 15:30:00

console.log(d.toString());           // Sat Jan 20 2024 15:30:00 GMT+0800 (China Standard Time) (示例)
console.log(d.toDateString());       // Sat Jan 20 2024 (示例)
console.log(d.toISOString());        // 2024-01-20T07:30:00.000Z (UTC)
console.log(d.toLocaleDateString('zh-CN')); // 2024/1/20 (示例)
console.log(d.toLocaleTimeString('en-US', { hour12: true })); // 3:30:00 PM (示例)
```

**5. TypeScript 中的类型安全**

TypeScript 确保当你声明一个变量、函数参数或返回值为 `Date` 类型时，你实际上传递或返回的是一个 `Date` 对象实例。

```typescript
function displayFormattedDate(date: Date): string {
  // TS 知道 date 是一个 Date 对象，所以 date.getFullYear 等方法可用
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

const myDate: Date = new Date();
console.log(displayFormattedDate(myDate));

// const wrongType = "2023-10-27";
// console.log(displayFormattedDate(wrongType)); // TypeScript 错误: Argument of type 'string' is not assignable to parameter of type 'Date'.
```

**6. 日期比较与计算**

*   **比较：**
    可以直接使用比较运算符 (`>`, `<`, `>=`, `<=`)，它们会隐式调用 `valueOf()` 方法，该方法返回时间戳。或者显式使用 `getTime()`。
    ```typescript
    const date1: Date = new Date(2023, 9, 27);
    const date2: Date = new Date(2023, 9, 28);

    console.log(date1 < date2); // true
    console.log(date1.getTime() < date2.getTime()); // true
    ```

*   **计算：**
    日期计算通常通过操作时间戳 (毫秒数) 来完成。
    ```typescript
    const now: Date = new Date();
    const oneDayInMilliseconds: number = 24 * 60 * 60 * 1000;

    const tomorrow: Date = new Date(now.getTime() + oneDayInMilliseconds);
    console.log("Tomorrow:", tomorrow.toLocaleDateString());

    const yesterday: Date = new Date(now.getTime() - oneDayInMilliseconds);
    console.log("Yesterday:", yesterday.toLocaleDateString());
    ```
    **注意：** 对于更复杂的日期计算（如“下个月的今天”，考虑到不同月份天数、闰年等），直接操作毫秒数会非常复杂且容易出错。

**7. 常见陷阱与注意事项 (非常重要！)**

*   **月份索引 (0-11)**：`getMonth()` 返回 0-11，`new Date(year, monthIndex, ...)` 的 `monthIndex` 也是 0-11。这是最常见的错误来源之一。
*   **时区 (Timezones)**：`Date` 对象内部存储的是 UTC 时间戳。但大多数方法（如 `getFullYear()`, `getHours()`, `toString()`）默认使用**用户本地时区**进行转换和显示。`toISOString()` 和 `toUTCString()` 是显式使用 UTC 的。时区处理是日期操作中最复杂的部分，如果你的应用需要处理不同时区，原生 `Date` 对象能力非常有限。
*   **可变性 (Mutability)**：`Date` 对象是可变的。`setDate()`, `setHours()` 等方法会直接修改原始对象。这可能导致意外的副作用，尤其是在将 Date 对象传来传去时。
    ```typescript
    const d1 = new Date();
    const d2 = d1; // d2 引用的是与 d1 相同的对象
    d2.setFullYear(2000);
    console.log(d1.getFullYear()); // 输出 2000，因为 d1 也被修改了！
    ```
    若要避免，可以先创建一个副本：`const d2 = new Date(d1.getTime());`
*   **日期字符串解析**：`new Date(dateString)` 对非 ISO 8601 格式的字符串解析行为在不同 JavaScript 引擎中可能不一致。**强烈建议**后端返回 ISO 8601 格式的日期字符串，前端也尽量使用此格式。
*   **`Date.parse(dateString)`**：返回日期字符串对应的时间戳，同样受解析行为不一致的影响。

**8. 现代日期时间处理：推荐使用库**

鉴于原生 `Date` 对象的上述局限性（尤其是时区处理、不变性、复杂的日期计算和格式化），在大多数生产项目中，**强烈推荐使用专门的日期时间处理库**。这些库提供了更健壮、更易用、功能更丰富的 API。

**流行的现代库 (截至 2023/2024)：**

*   **`date-fns`** (推荐)：
    *   **特性**：轻量、Immutable (不可变)、功能全面、模块化 (可以按需引入，利于 Tree Shaking)、对函数式编程友好、优秀的 TypeScript 支持、良好的国际化 (I 18 N) 支持。
    *   **风格**：提供纯函数操作日期，每次操作返回新的 Date 对象。
    *   **示例**：
        ```typescript
        import { format, addDays, parseISO, isValid } from 'date-fns';
        // 如果在 Node.js CommonJS 环境，可能是:
        // const { format, addDays, parseISO, isValid } = require('date-fns');

        const today = new Date();
        const formattedDate = format(today, 'yyyy-MM-dd HH:mm:ss'); // "2023-10-27 17:30:00"
        console.log(formattedDate);

        const nextWeek = addDays(today, 7);
        console.log(format(nextWeek, 'PPP')); // 'Oct 4th, 2024' (示例)

        const parsed = parseISO('2024-01-15T10:00:00.000Z');
        if (isValid(parsed)) {
            console.log('Parsed date:', format(parsed, 'PPpp'));
        }
        ```

*   **`Day.js`**：
    *   **特性**：极轻量 (2 KB gzip)，API 设计与 Moment.js 非常相似 (方便迁移)，Immutable，支持插件扩展功能，TypeScript 支持。
    *   **风格**：链式调用，类似 Moment.js。
    *   **示例**：
        ```typescript
        import dayjs from 'dayjs';
        // import utc from 'dayjs/plugin/utc'; // 示例：如果需要 UTC 功能
        // import timezone from 'dayjs/plugin/timezone'; // 示例：如果需要时区功能
        // dayjs.extend(utc);
        // dayjs.extend(timezone);

        const now = dayjs();
        console.log(now.format('YYYY-MM-DD HH:mm:ss'));

        const futureDate = now.add(7, 'day').subtract(1, 'month');
        console.log(futureDate.toString());

        // console.log(dayjs.tz("2014-06-01 12:00", "America/New_York").format());
        ```

*   **`Luxon`**：
    *   **特性**：由 Moment.js 团队的成员创建，旨在解决 Moment.js 的一些设计问题。Immutable，强大的时区和国际化支持 (基于 `Intl` API)，清晰的 API。
    *   **风格**：面向对象，但对象是不可变的。
    *   **示例**：
        ```typescript
        import { DateTime } from 'luxon';

        const dt = DateTime.now();
        console.log(dt.toFormat('yyyy-LL-dd HH:mm:ss')); // LL 代表月份补零

        const specificDt = DateTime.local(2024, 1, 15, 10, 30);
        console.log(specificDt.plus({ days: 5 }).toISO());

        const nyTime = DateTime.now().setZone('America/New_York');
        console.log(nyTime.toString()); // 包含时区信息
        ```

*   **`Temporal` (未来标准)**：
    *   JavaScript TC39 委员会正在制定一个新的、更完善的日期时间 API 标准—— `Temporal`。它旨在解决原生 `Date` 的所有痛点，提供不可变对象、完整的时区支持、以及更符合人类直觉的 API。
    *   目前 `Temporal` 仍处于 Stage 3 提案阶段，部分浏览器和 Node.js 版本可能通过 flag 实验性支持，或者可以使用 polyfill。
    *   **一旦 `Temporal` 成为正式标准并得到广泛支持，它将是未来的首选。** 但目前在生产环境中直接使用需谨慎评估其兼容性和 polyfill 的成熟度。

**Moment.js (不推荐新项目使用)**：
曾经非常流行，但现在处于维护状态，官方不推荐用于新项目，主要原因是：
*   体积大。
*   默认是可变的 (mutable)，容易出错。
*   一些设计问题。
其维护者推荐使用 `Luxon`、`Day.js` 或 `date-fns` 等现代库。

**总结：**

1.  TypeScript 使用 JavaScript 内置的 `Date` 对象，并为其提供类型检查。
2.  熟悉 `Date` 对象的创建、获取/设置分量、基本格式化方法。
3.  **务必注意**月份索引 (0-11)、时区问题和对象的可变性。
4.  对于任何需要精确控制格式、处理时区、进行复杂日期计算或追求代码健壮性的项目，**强烈推荐使用 `date-fns`、`Day.js` 或 `Luxon` 等现代日期库**。它们提供了不可变性、更好的 API 设计和更全面的功能。
5.  关注 `Temporal` API 的进展，它可能是未来的标准解决方案。