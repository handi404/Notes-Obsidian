#### // Error = 为表示发生的问题而创建的对象
#### // 经常在用户输入或建立连接时发生

#### // try { } = 包含可能导致错误的代码
#### // catch { } = 捕获并处理 try { } 抛出的任何错误
#### // finally { } =（可选）始终执行。主要用于清理
#### // ex. close files, close connections, release resources (关闭文件、关闭连接、释放资源)

---

```js
try{
    const dividend = Number(window.prompt("Enter a dividend: "));
    const divisor = Number(window.prompt("Enter a divisor: "));
    
    if(divisor == 0){
        throw new Error("You can't divide by zero!");
    }
    if(isNaN(dividend) || isNaN(divisor)){	//isNaN()检查是否为非数字
        throw new Error("Values must be a number");
    }

    const result = dividend / divisor;
    console.log(result);
}
catch(error){
    console.error(error);
}
finally{
    console.log("This always executes");
}

console.log("You have reached the end!");
```