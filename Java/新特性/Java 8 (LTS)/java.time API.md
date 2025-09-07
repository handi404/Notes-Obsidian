详细讲解 Java 8 中引入的，也是目前 Java 开发中处理日期时间的**唯一标准**—— `java.time` API。这个 API 彻底解决了旧版 `java.util.Date` 和 `java.util.Calendar` 的所有痛点。

---

### 新的日期与时间 API (java.time)

#### 1. 核心概念 (Core Concept)

`java.time` API 的设计哲学基于 **清晰、流畅、不可变和领域驱动**。它将人类的时间概念和机器的时间概念清晰地分离开来。

*   **人类时间 (Human Time):** 关注年月日、时分秒，与时区、日历系统相关。
    *   `LocalDate`: 只包含日期，如 `2024-05-21`。
    *   `LocalTime`: 只包含时间，如 `10:30:00`。
    *   `LocalDateTime`: 包含日期和时间，如 `2024-05-21T10:30:00`。它是最常用的类。
    *   `ZonedDateTime`: **带时区**的 `LocalDateTime`，能处理夏令时等复杂情况，如 `2024-05-21T10:30:00+08:00[Asia/Shanghai]`。

*   **机器时间 (Machine Time):** 关注从一个固定时间点（1970-01-01T00:00:00Z）开始经过的秒数或纳秒数，是时间线上的一个瞬时点，与时区无关。
    *   `Instant`: 时间戳，内部表示为纳秒。它就是机器视角的时间。

*   **时间段 (Duration & Period):**
    *   `Duration`: 用于计算两个**时间**之间（`LocalTime`, `LocalDateTime`, `Instant`）的间隔，精确到纳秒。例如，“3小时20分钟”。
    *   `Period`: 用于计算两个**日期**之间（`LocalDate`）的间隔，单位是年月日。例如，“2年3个月5天”。

**核心特性：**
*   **不可变性 (Immutability):** 所有 `java.time` 包下的类都是不可变的（类似于 `String`）。任何修改操作（如 `plusDays`）都会返回一个**新的实例**，而不是修改原始对象。这使得它们在多线程环境下是**线程安全**的。
*   **清晰的 API:** 方法名非常直观，如 `plus()`, `minus()`, `with()`, `of()`。
*   **时区处理:** `ZonedDateTime` 和 `ZoneId` 提供了对时区的全面支持。

#### 2. 演进与现代化 (Evolution & Modernization)

*   **Java 8 之前 (The Pain Point):**
    *   `java.util.Date`: 设计混乱，它既表示一个瞬时点，又被用于表示年月日，其 `toString()` 方法还依赖 JVM 默认时区。更糟糕的是，它是**可变的 (mutable)**，在多线程环境下非常危险。很多方法（如 `getYear`）都已被废弃。
    *   `java.util.Calendar`: 设计笨重，API 复杂难用，同样是可变的。月份从 0 开始计数（`0` 代表一月），极易出错。
    *   `java.text.SimpleDateFormat`: 线程不安全，是并发 bug 的常见来源。必须为每个线程创建单独的实例或使用 `ThreadLocal` 包装。

*   **Java 8 之后 (The Solution):**
    `java.time` API (又称 JSR-310) 全面取代了上述旧 API。它借鉴了著名的 Joda-Time 库的设计思想，并将其作为 Java 的标准。

*   **现代化使用：**
    *   **数据库交互：** 现代 JDBC 驱动 (4.2+) 可以直接将 `LocalDate`, `LocalDateTime` 等类型映射到数据库的 `DATE`, `TIMESTAMP` 类型，无需手动转换。
    *   **JSON 序列化：** 主流框架如 Jackson 和 Fastjson 都内置了对 `java.time` 类型的支持，可以自动序列化和反序列化为 ISO 8601 标准格式的字符串（如 `"2024-05-21T10:30:00"`）。
    *   **与旧 API 转换：** 如果需要兼容老代码，`java.time` 提供了方便的转换方法，如 `Date.toInstant()` 和 `Timestamp.valueOf(LocalDateTime)`。

#### 3. 代码示例 (Code Example)

```java
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeExample {
    public static void main(String[] args) {
        // --- 1. 创建日期和时间 ---
        System.out.println("--- Creation ---");
        LocalDate today = LocalDate.now();                     // 获取当前日期: 2024-05-21
        LocalTime now = LocalTime.now();                       // 获取当前时间: 10:30:00.123
        LocalDateTime currentDateTime = LocalDateTime.now();   // 获取当前日期时间
        
        // 使用 of() 方法指定创建
        LocalDate specificDate = LocalDate.of(2025, Month.JANUARY, 1);
        LocalDateTime specificDateTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);

        System.out.println("Today: " + today);
        System.out.println("Now: " + now);
        System.out.println("Specific Date: " + specificDate);

        // --- 2. 修改与计算 (不可变性) ---
        System.out.println("\n--- Modification (Immutable) ---");
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);  // 增加一周
        LocalDate nextMonth = today.plusMonths(1);             // 增加一个月
        LocalDateTime tenHoursLater = currentDateTime.plusHours(10); // 增加10小时

        // plus/minus 返回的是新对象，today 自身并未改变
        System.out.println("Date after one week: " + nextWeek);
        System.out.println("Original today's date: " + today);

        // --- 3. 格式化与解析 (线程安全) ---
        System.out.println("\n--- Formatting & Parsing ---");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("Formatted: " + formattedDateTime);

        String dateTimeStr = "2023/01/05 15:30:00";
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeStr, formatter);
        System.out.println("Parsed: " + parsedDateTime);

        // --- 4. 时区处理 (ZonedDateTime) ---
        System.out.println("\n--- Time Zone Handling ---");
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        ZonedDateTime shanghaiTime = ZonedDateTime.now(shanghaiZone);
        System.out.println("Time in Shanghai: " + shanghaiTime);

        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime tokyoTime = shanghaiTime.withZoneSameInstant(tokyoZone);
        System.out.println("Same instant in Tokyo: " + tokyoTime); // 时间会变化，因为时区不同

        // --- 5. 时间戳 (Instant) ---
        System.out.println("\n--- Instant (Machine Time) ---");
        Instant timestamp = Instant.now();
        System.out.println("Current timestamp: " + timestamp); // UTC时间
        long epochSecond = timestamp.getEpochSecond(); // 获取秒数
        System.out.println("Seconds from epoch: " + epochSecond);

        // --- 6. 计算时间间隔 (Duration & Period) ---
        System.out.println("\n--- Duration & Period ---");
        LocalDateTime start = LocalDateTime.of(2024, 5, 21, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 5, 21, 12, 30);
        Duration duration = Duration.between(start, end);
        System.out.println("Duration: " + duration.toHours() + " hours and " + duration.toMinutesPart() + " minutes.");

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 15);
        Period period = Period.between(startDate, endDate);
        System.out.println("Period: " + period.getYears() + " years, " + period.getMonths() + " months, and " + period.getDays() + " days.");
    }
}
```

#### 4. 扩展与应用 (Extension & Application)

*   **业务逻辑:** 计算年龄、判断活动是否在有效期内、调度任务执行时间等。
*   **日志记录:** 精确记录事件发生的时间点，`Instant` 是最佳选择。
*   **API 设计:** 在 RESTful API 中，使用 ISO 8601 格式的字符串（`java.time` 默认 `toString()` 格式）作为日期时间的标准交换格式。
*   **数据库持久化 (JPA/MyBatis):**
    *   `LocalDate` -> `DATE`
    *   `LocalTime` -> `TIME`
    *   `LocalDateTime` -> `TIMESTAMP`
    *   `ZonedDateTime`/`Instant` -> `TIMESTAMP WITH TIME ZONE`

#### 5. 要点与注意事项 (Key Points & Cautions)

1.  **分清场景，选用正确的类：**
    *   只关心日期 -> `LocalDate`
    *   只关心时间 -> `LocalTime`
    *   不涉及时区 -> `LocalDateTime` (最常用)
    *   需要处理跨时区或夏令时 -> `ZonedDateTime`
    *   与机器交互，记录日志时间戳 -> `Instant`

2.  **不可变性是核心：**
    永远记住，`plus()`, `minus()`, `with()` 等方法会返回一个**新对象**。如果这样写，代码将不会有任何效果：
    ```java
    LocalDateTime myTime = LocalDateTime.now();
    myTime.plusDays(1); // 错误！这个返回值被丢弃了
    // 正确写法：
    LocalDateTime tomorrow = myTime.plusDays(1);
    ```

3.  **`DateTimeFormatter` 是线程安全的：**
    可以放心地将其定义为静态常量（`private static final`）供全局共享，不必每次都创建。

4.  **`Duration` vs. `Period`：**
    不要混淆。`Duration` 基于时间，`Period` 基于日期。对 `LocalDate` 使用 `Duration.between()` 会抛出异常，反之亦然。

5.  **数据库时区问题：**
    当使用 `LocalDateTime` 时，数据库驱动和应用服务器的默认时区可能会影响存储和读取的时间。最佳实践是：服务器和数据库都统一设置为 UTC 时区，业务代码中使用 `ZonedDateTime` 或在存储/读取时手动转换时区，以避免歧义。如果应用不涉及时区，统一配置即可。

`java.time` API 的引入是 Java 平台的一次巨大飞跃，它让日期时间处理变得前所未有的简单、安全和可靠。掌握它是每一位现代 Java 开发者的基本功。