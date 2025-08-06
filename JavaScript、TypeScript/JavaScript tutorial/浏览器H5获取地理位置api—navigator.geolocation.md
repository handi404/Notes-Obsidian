

地理位置 [API]（Geolocation API）允许用户向 web 应用程序提供他们的位置。出于隐私考虑，报告地理位置前会先请求用户许可。

_**PS**_：出于安全考虑，当网页请求获取用户位置信息时，用户会被提示进行授权。注意不同浏览器在请求权限时有不同的策略和方式。Windows 10 在未开启定位的情况下无法获取位置。

-   Geolocation.getCurrentPosition()：检索设备的当前位置。
-   Geolocation.watchPosition()：注册一个处理函数，在设备位置发生改变时都会自动调用，并返回改变后的位置信息。

对于上述的几种方法，其[回调]函数最多有三个参数：

-   一个必须的成功的回调函数：如果位置检索成功，则调用该回调函数，并以 GeolocationPosition 对象（用于提供位置数据）作为其唯一的参数。
-   一个可选的错误回调函数：如果位置检索失败，则调用该回调函数，并以 GeolocationPositionError 对象（用于提供访问出错的信息）作为其唯一的参数。
-   一个可选的对象：用于提供检索位置数据的选项。

_**获取当前位置**_  
你可以调用 getCurrentPosition() 函数获取用户当前定位位置。这会异步地请求获取用户位置，并查询定位硬件来获取最新信息。当定位被确定后，定义的回调函数就会被执行。你可以选择性地提供第二个回调函数，当有错误时会被执行。第三个参数也是可选的，你可以通过该对象参数设定最长可接受的定位返回时间、等待请求的时间和是否获取高[精度]定位。

```javascript
navigator.geolocation.getCurrentPosition((position) => {
  doSomething(position.coords.latitude, position.coords.longitude);
});
```

_**监视定位**_  
你可以设定一个回调函数来响应定位数据发生的变更（设备发生了移动，或获取到了更高精度的地理位置信息）。你可以通过 watchPosition() 函数实现该功能。它与 getCurrentPosition() 接受相同的参数，但回调函数会被调用多次。错误回调函数与 getCurrentPosition() 中一样是可选的，也会被多次调用。

```javascript
const watchID = navigator.geolocation.watchPosition((position) => {
  doSomething(position.coords.latitude, position.coords.longitude);
});
```

```javascript
navigator.geolocation.clearWatch(watchID);
```

_**调整返回结果**_

```javascript
function success(position) {
  const latitude  = position.coords.latitude; //十进制数的纬度
  const longitude = position.coords.longitude; //十进制数的经度
  const accuracy = position.coords.accuracy; //所提供的以米为单位的经度和纬度估计的精确度
  const altitude = position.coords.altitude; //海拔，海平面以上以米计
  const altitudeAccuracy = position.coords.altitudeAccuracy; //所提供的以米为单位的高度估计的精确度
  const heading = position.coords.heading; //宿主设备当前移动的角度方向，相对于正北方向顺时针计算
  const heading = position.coords.speed  以米每秒为单位的设备的当前对地速度
  // 使用 latitude 和 longitude 做些什么
}

function errorCallback(error) {
  switch (error.code) {
    case 1: log('用户拒绝对获取地理位置的请求'); break;
    case 2: log('位置信息是不可用的'); break;
    case 3: log('请求用户地理位置超时'); break;
    default: log('other Error')
  }
}

const options = {
  enableHighAccuracy: true, //启用高精确度模式，这个参数通知浏览器启用HTML5
  maximumAge: 30000, //表示浏览器重新计算位置的时间间隔，单位为ms，默认值为零，这意味着浏览器每次请求都必须重新计算位置
  timeout: 27000, //超时限制（单位为毫秒）。如果在该时间内为获取到地理位置信息，则返回错误
  Geolocation: true //服务的高精确度模式，默认值为false
};

const watchID = navigator.geolocation.watchPosition(success, errorCallback, options);
```


## 例：

```js
window.addEventListener("load", () => {

  // 坐标 经度纬度

  let long;

  let lat;

  if (navigator.geolocation) {

    navigator.geolocation.getCurrentPosition((position) => {

      //   console.log(position);

      long = position.coords.longitude; //经度

      lat = position.coords.latitude; //纬度

      //   console.log(`精度:${long},纬度:${lat}`);
   }
});
```