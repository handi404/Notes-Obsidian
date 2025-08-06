#### // Async/Await => Async = 使函数返回一个 Promise
#### //                            Await = 让异步函数等待 Promise

#### // 允许您以同步方式编写异步代码
#### // Async 没有 resolve 或 reject 参数
#### // Await 之后的所有内容都被放入事件队列中

---

```js
// 按顺序做这些杂务

// 1. 遛狗
// 2. 清洁厨房
// 3. 倒掉垃圾
function walkDog(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {

            const dogWalked = false;

            if(dogWalked){
                resolve("You walk the dog 🐶");
            }
            else{
                reject("You DIDN'T walk the dog");
            }
        }, 1500);
    });
}

function cleanKitchen(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            
            const kitchenCleaned = true;

            if(kitchenCleaned){
                resolve("You clean the kitchen 🧹");
            }
            else{
                reject("You DIDN'T clean the kitchen");
            }
        }, 2500);
    });
}

function takeOutTrash(){
    return new Promise((resolve, reject) => {
        setTimeout(() => {

            const trashTakenOut = true;

            if(trashTakenOut){
                resolve("You take out the trash 🚮");
            }
            else{
                reject("You DIDN'T take out the trash");
            }

        }, 500);
    });
}

async function doChores(){

    try{
        const walkDogResult = await walkDog();
        console.log(walkDogResult);
    
        const cleanKitchenResult = await cleanKitchen();
        console.log(cleanKitchenResult);
    
        const takeOutTrashResult = await takeOutTrash();
        console.log(takeOutTrashResult);
        
        console.log("You finsihed all the chores!");
    }
    catch(error){
        console.error(error);
    }
}
```