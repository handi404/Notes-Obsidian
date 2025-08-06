
### [toFixed](https://so.csdn.net/so/search?q=toFixed&spm=1001.2101.3001.7020)() 方法

toFixed(n)中n指小数点后的位数

1、整数[保留一位小数]
```javascript
var num = 1
num = num.toFixed(1)   
console.log(num);   //1.0
```

2、小数保留一位  
注意：四舍六入五取偶 - 五后非零就进一，五后为零看奇偶，五前为偶应舍去，五前为奇要进一

```javascript
var num = 1.55
var num1 = 1.45          //5前为偶舍去
num1 = num1.toFixed(1)
num = num.toFixed(1)   
console.log(num);   //1.6
console.log(num1);  //1.4

```

### 二、自定义转换方法 - 四舍五入，转换后仍为数字类型

-   第一个参数为待转换的数字，是小数，整数不能实现
-   第二个参数为保留的小数个数

```javascript
function roundFun(value, n) {
  return Math.round(value*Math.pow(10,n))/Math.pow(10,n);
}
console.log(roundFun(2.853，2));  //2.85
console.log(roundFun(5/3, 2)     //1.67
console.log(roundFun(2, 2)       //2      
```

```javascript
//输入小数value，并保留小数点后一位，如果想保留两位，将10改为100
function roundFun(value) {
    return Math.round(value * 10) / 10;
}
console.log(roundFun(2.853));      //2.9

```

### 三、自定义格式化输出方法 - 对上面自定义转换方法的补充

-   小数位数进行四舍五入，返回的是格式化后的字符串，不是数字，小数点后不足的位数会自动补0，如4会变成4.0返回

```javascript
//保留n位小数并格式化输出（不足的部分补0）
    function fomatFloat(value, n) {
      var f = Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
      var s = f.toString();
      var rs = s.indexOf(".");
      if (rs < 0) {
        s += ".";
      }
      for (var i = s.length - s.indexOf("."); i <= n; i++) {
        s += "0";
      }
      return s;
    };
    console.log(fomatFloat(1,2));    //1.00

```