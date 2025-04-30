## 前言

记录这篇文章的缘由，主要是涉及一个[格式转换](https://so.csdn.net/so/search?q=%E6%A0%BC%E5%BC%8F%E8%BD%AC%E6%8D%A2&spm=1001.2101.3001.7020)，对此深挖了这个类

在Java中，Date类是用于表示日期和时间的类。

位于java.util包中，是[Java平台](https://so.csdn.net/so/search?q=Java%E5%B9%B3%E5%8F%B0&spm=1001.2101.3001.7020)中处理日期和时间的基本类之一。然而，需要注意的是，Date类存在一些问题：

其中之一是它不支持国际化，而且在Java 8及以后版本中，推荐使用java.time包中的新日期和时间API（如LocalDate、LocalTime、LocalDateTime等）来替代Date类。

## 1\. 基本知识

**一、 以下是Date类的一些基本概念和作用：**

-   **概念**： Date类用于表示一个特定的时间点，精确到毫秒级别。  
    存储的是从1970年1月1日午夜开始的毫秒数，这被称为“Unix时间”或“Epoch时间”。
    
-   **作用**： Date类主要用于获取、表示和操作日期和时间。  
    用于记录事件发生的时间、计算时间间隔等。
    

**二、常用的Date类API包括：**

| 构造方法 | 获取时间 | 设置时间 | 时间比较 | 格式转换 |
| --- | --- | --- | --- | --- |
| `Date()`: 创建一个表示当前时间的Date对象。
`Date(long date)`: 根据指定的毫秒数创建一个Date对象。

 | `getTime()`: 返回从1970年1月1日00:00:00以来的毫秒数。 | `setTime(long time)`: 设置Date对象的时间。 | `before(Date when):` 判断当前日期是否在指定日期之前。

`after(Date when)`: 判断当前日期是否在指定日期之后。

`equals(Object obj)`: 判断两个日期是否相等。

 | `toString()`: 将日期对象转换为字符串。

`toLocaleString()`: 将日期对象转换为本地化的字符串。

`toGMTString()`: 将日期对象转换为GMT时间字符串。

 |

然而，由于Date类的一些缺陷，建议在新的应用中使用`java.time`包中的日期和时间API，以便更好地处理日期和时间的操作。

## 2\. 格式化输出

一般Date格式化输出，会配合`SimpleDateFormat`这个类！

主要的参数如下：

| 字母 | 含义 |
| --- | --- |
| y | 年 |
| M | 年中的月份 |
| w | 年中的周数 |
| W | 月份中的周数 |
| D | 年中的天数 |
| d | 月份中的天数 |
| F | 月份中的星期 |
| E | 星期中的天数 |
| a | Am/pm 标记 |
| H | 一天中的小时数（0-23） |
| k | 一天中的小时数（1-24） |
| K | am/pm 中的小时数（0-11） |
| h | am/pm 中的小时数（1-12） |
| m | 小时中的分钟数 |
| s | 分钟中的秒数 |
| S | 毫秒数 |
| z | 时区 |

例子如下：

```java
import java.text.SimpleDateFormat;
import java.util.Date;

public class datetest {
    public static void main(String[] args) {
        Date now = new Date();
        SimpleDateFormat sd=new SimpleDateFormat("现在时间： "+"yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒");
        System.out.println(sd.format(now));
        sd = new SimpleDateFormat("一年中的第 D 天，一个月中的第 F 个星期 ，一年中的第 w 个星期，一个月中的第 W 个星期");
        System.out.println(sd.format(now));
    }
}

```

截图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/9207e1e23ba842dc9701b6b03c3a3653.png)

特别注意到是12小时和24小时的规则不大一样：

```java
import java.text.SimpleDateFormat;
import java.util.Date;

public class datetest {
    public static void main(String[] args) {
        Date now = new Date();
        // yyyy-MM-dd hh:mm:ss 12小时
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //小写是12小时
        System.out.println(sdf.format(now));

        // yyy-MM-dd HH:mm:ss  24小时
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//大写是24小时的
        System.out.println(sdf.format(now));

    }
}

```

截图如下：（由于时间点还没过下午，将就下~）

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/6d603f57f1bd427ea94e5e1d1b6ad95e.png)

## 3\. 格式转换

常用的转换主要是Date转换为String，或者String转换为时间格式！

**一、Date转换为String：**

```java
public class datetest {
    public static void main(String[] args) {
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sd.format(now));

        // 自定义格式
        sd = new SimpleDateFormat("M-dd");
        System.out.println(sd.format(now));
    }
}

```

截图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/e47723296789416ca0b24b9b32a02522.png)

**二、String转换为Date：**

```java
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class datetest {
    public static void main(String[] args) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String now = "2024-01-21";

        try {
            // 转换为Date类型
            Date date = sd.parse(now);
            System.out.println(date);
            
            // 输出long的格式
            long time = date.getTime();
            System.out.println(time);
            
            // long格式之后转换为String来表示
            System.out.println(sd.format(time));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}

```

截图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/29c657bac55b4815baf832d13e691c5f.png)