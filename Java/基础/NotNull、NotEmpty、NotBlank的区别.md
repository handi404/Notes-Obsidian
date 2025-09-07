下面这几个词在平日开发种大家都见过，而且是不止一次的见过：
NotNull、NotEmpty、NotBlank
比如在String的工具类里，又或者在List的工具类里，那么大家平时真的注意到这里面的区别了吗？

## NotNull

NotNull 字面意思不为 null。

对就是字面意思，这个比较简单。

比如说

**String  str!=null**
**List  list != null**
**Map   map != null**
**Integer  a != null**

等等，所有的类型都可以判断不等于null。  

用于：String、List、Map、所有对象。

## NotEmpty

NotEmpty 字面意思不为空，注意不是不为 null。  

比如

**String  str != null  且  str 不等于 ""**
**List  list != null 且  list.size 不等于0**

用于：String、List、Map等。  

## NotBlank

NotBlank 则作用在字符串 String 上，不能为空串或空白(空格或制表符)字符串。

**String  str != null**  **且  str 不等于 ""** **且 str 不等于 "  "**

用于：字符串String。  
