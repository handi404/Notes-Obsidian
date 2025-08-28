###### // console.Time () = 允许您测量所需时间的工具
###### // 用于执行一段代码或进程
###### // 非常适合识别性能“瓶颈”

---

```js
function loadData(){

    console.time("loadData");

    for(let i = 0; i < 1000000000; i++){
        //pretend to load some data
    }

    console.timeEnd("loadData");
}

function processData(){

    console.time("processData");

    for(let i = 0; i < 1000000; i++){
        //pretend to process some data
    }
    
    console.timeEnd("processData");
}

loadData();
processData();

```