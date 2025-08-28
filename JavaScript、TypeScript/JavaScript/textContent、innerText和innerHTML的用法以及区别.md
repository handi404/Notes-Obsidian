# 目录
- [[#0.码仙励志|0.码仙励志]]
- [[#1.textContent的用法|1.textContent的用法]]
	- [[#1.textContent的用法#1.设置标签中的文本内容|1.设置标签中的文本内容]]
	- [[#1.textContent的用法#2.获取标签中的文本内容|2.获取标签中的文本内容]]
- [[#2.innerText的用法|2.innerText的用法]]
	- [[#2.innerText的用法#1.设置标签中的文本内容|1.设置标签中的文本内容]]
	- [[#2.innerText的用法#2.获取标签中的文本内容|2.获取标签中的文本内容]]
- [[#3.innerHTML的用法|3.innerHTML的用法]]
	- [[#3.innerHTML的用法#1.设置标签中的文本内容|1.设置标签中的文本内容]]
	- [[#3.innerHTML的用法#2.获取标签中的文本内容|2.获取标签中的文本内容]]
- [[#4.innerText和textContent的区别|4.innerText和textContent的区别]]
	- [[#4.innerText和textContent的区别#1.兼容代码设置任意的标签中间的任意文本内容|1.兼容代码设置任意的标签中间的任意文本内容]]
	- [[#4.innerText和textContent的区别#2.兼容代码获取任意标签中间的文本内容|2.兼容代码获取任意标签中间的文本内容]]
- [[#5.textContent、innerText和innerHTML的区别|5.textContent、innerText和innerHTML的区别]]
	- [[#5.textContent、innerText和innerHTML的区别#1.设置标签中的文本内容|1.设置标签中的文本内容]]
	- [[#5.textContent、innerText和innerHTML的区别#2.获取标签中的文本内容|2.获取标签中的文本内容]]
	- [[#5.textContent、innerText和innerHTML的区别#3.总结|3.总结]]

___

### 0.码仙励志

**不要随便说自己没时间，时间都是挤出来的**

### 1.textContent的用法

#### 1.设置标签中的文本内容

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        document.getElementById("dv").textContent = "改变了";
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915094417100?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![](https://img-blog.csdn.net/20180915094434222?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

####  2.获取标签中的文本内容

```js
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        var text = document.getElementById("dv").textContent;
        console.log(text);
    };
</script>
</body>
```
这是一个 div

### 2.innerText的用法

#### 1.设置标签中的文本内容

```js
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        document.getElementById("dv").innerText = "改变了";
    };
</script>
</body>
```


![](https://img-blog.csdn.net/20180915095523874?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![](https://img-blog.csdn.net/20180915095545294?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 2.获取标签中的文本内容

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        var text = document.getElementById("dv").innerText;
        console.log(text);
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915095709741?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

### 3.innerHTML的用法

#### 1.设置标签中的文本内容

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        document.getElementById("dv").innerHTML = "改变了";
    };
</script>
</body>
```

![](https://img-blog.csdn.net/2018091510001734?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 ![](https://img-blog.csdn.net/20180915100042424?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 2.获取标签中的文本内容

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    document.getElementById("btn").onclick = function () {
        var text = document.getElementById("dv").innerHTML;
        console.log(text);
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915100242287?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

### 4.innerText和textContent的区别

> 1.  设置标签中的文本内容,应该使用textContent属性,谷歌,火狐支持,IE8不支持
> 2.  设置标签中的文本内容,应该使用innerText属性,谷歌,火狐,IE8都支持
> 3.  如果这个属性在浏览器中不支持,那么这个属性的类型是undefined
> 4.  判断这个属性的类型 是不是undefined,就知道浏览器是否支持

#### 1.兼容代码设置任意的标签中间的任意文本内容

```javascript
<script>
    function setInnerText(element, text) {
        //判断浏览器是否支持这个属性
        if (typeof element.textContent == "undefined") {//不支持
            element.innerText = text;
        } else {//支持这个属性
            element.textContent = text;
        }
    };
</script>
```

#### 2.兼容代码获取任意标签中间的文本内容

```javascript
<script>
    function getInnerText(element) {
        if (typeof element.textContent == "undefined") {
            return element.innerText;
        } else {
            return element.textContent;
        }
    };
</script>
```

### 5.textContent、innerText和innerHTML的区别

**textContent、innerText的效果是一样的，所以这里我只用innerText举例**

#### 1.设置标签中的文本内容

**innerText的效果**

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    function my$(id) {
        return document.getElementById(id);
    };
</script>
<script>
    my$("btn").onclick = function () {
        my$("dv").innerText = "哈哈";//设置文本
        my$("dv").innerText = "<p>这是一个p</p>";//设置html标签的代码
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915101607286?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 **innerHTML的效果**

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">这是一个div</div>
<script>
    function my$(id) {
        return document.getElementById(id);
    };
</script>
<script>
    my$("btn").onclick = function () {
        my$("dv").innerHTML = "哈哈";
        my$("dv").innerHTML = "<p>这是一个p</p>";//设置Html标签的
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915101813790?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 2.获取标签中的文本内容 

**innerText的效果**

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">
    <p>这是一个p</p>
</div>
<script>
    document.getElementById("btn").onclick = function () {
        //可以获取标签中的文本内容
        console.log(document.getElementById("dv").innerText);
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915102438277?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

  **innerHTML的效果**

```javascript
<body>
<input type="button" value="按钮" id="btn">
<div id="dv">
    <p>这是一个p</p>
</div>
<script>
    document.getElementById("btn").onclick = function () {
        //可以获取标签中的文本内容
        console.log(document.getElementById("dv").innerHTML);
    };
</script>
</body>
```

![](https://img-blog.csdn.net/20180915102534175?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Rzd2NfYnl5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 3.总结

> 1.  如果使用innerText主要是设置文本的,设置标签内容,是没有标签的效果的
> 2.  innerHTML是可以设置文本内容
> 3.  innerHTML主要的作用是在标签中设置新的html标签内容,是有标签效果的
> 4.  想要设置标签内容,使用innerHTML,想要设置文本内容,innerText或者textContent,或者innerHTML,推荐用innerHTML
> 5.  innerText可以获取标签中间的文本内容,但是标签中如果还有标签,那么最里面的标签的文本内容也能获取.---获取不到标签的,文本可以获取
> 6.  innerHTML才是真正的获取标签中间的所有内容

本篇博客来自于传智播客视频教程的总结以及笔记的整理，仅供学习交流，切勿用于商业用途，如有侵权，请联系博主删除，博主QQ：194760901