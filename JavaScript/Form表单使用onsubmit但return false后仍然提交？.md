

**我们可能会是如下使用onsubmit**

```html
<form action="" method="post" class="form-horizontal" onsubmit="checkUserProduct()">
```

**我们试图在checkUserProduct()中return false来阻止表单的提交。实际上的效果是即使return false ，表单还是会提交。但是如果改成 οnsubmit=”return checkUserProduct()”就没有问题了。**  
原来onsubmit属性就像是这个html对象的一个方法名，其值（[字符串](https://so.csdn.net/so/search?q=%E5%AD%97%E7%AC%A6%E4%B8%B2&spm=1001.2101.3001.7020)）就是其方法体，默认返回true；如果你没有改变他的返回值，那么他将默认返回true。

### 类似java里面如下二图

onsubmit（）方法调用checkUserProduct（）方法，虽然checkUserProduct（）方法return false，但是onsubmit（）不受影响，本身还是返回[默认值](https://so.csdn.net/so/search?q=%E9%BB%98%E8%AE%A4%E5%80%BC&spm=1001.2101.3001.7020)。我们只执行了checkUserProduct（）方法，没有对onsubmit（）结果进行任何处理。  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b7be0d1348637cd28521b5ca0aeff59d.png)

### 修改后

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/bf670df8ef1d0391ec6525c20eb93374.png)