###### // Promise = 管理异步操作的对象。
###### // 将 Promise 对象包装在 {异步代码} 周围
###### //“我保证返回一个值”
###### // 待处理 -> 已解决或已拒绝
###### // new Promise ((resolve, reject) => {异步代码})

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

walkDog().then(value => {console.log(value); return cleanKitchen()})
                  .then(value => {console.log(value); return takeOutTrash()})
                  .then(value => {console.log(value); console.log("You finished all the chores!")})
                  .catch(error => console.error(error));
```