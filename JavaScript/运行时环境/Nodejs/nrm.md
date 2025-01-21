
### 1，什么是nrm

nrm 是一个 npm 源管理器，允许你快速地在 npm源间切换。

什么意思呢，npm默认情况下是使用npm官方源（使用npm config ls命令可以查看），在国内用这个源肯定是不靠谱的，一般我们都会用淘宝npm源：https://registry.npm.taobao.org/，修改源的方式也很简单，在终端输入：

```typescript
npm set registry https://registry.npm.taobao.org/
```

再npm config ls查看，已经切换成功。

那么，问题来了，如果哪天你又跑去国外了，淘宝源肯定是用不了的，又要切换回官网源，或者哪天你们公司有自己的私有npm源了，又需要切换成公司的源，这样岂不很麻烦？于是有了nrm。

### 2，nrm安装

```typescript
npm install -g nrm
```

### 3，nrm使用

**3.1查看可选源 星号代表当前使用源**

```typescript
nrm ls
```

  
**3.1查看当前源**

```typescript
nrm current
```

  
**3.2 切换源**

```typescript
nrm use <registry>
```

其中，registry为源名。

比如：切换为taobao源

```typescript
nrm use taobao
```

### 4，添加源

```typescript
nrm add <registry> <url>
```

其中，registry为源名，url为源地址。

比如：添加一个公司私有的npm源，源地址为：http://192.168.22.11:8888/repository/npm-public/，源名为cpm（随意取）。

```typescript
nrm add cpm http://192.168.22.11:8888/repository/npm-public/
```

然后，查看是否添加成功

### 5，删除源

```typescript
nrm del <registry>
```

其中，registry为源名。

比如：删除刚才添加的cpm源

```typescript
nrm del cpm
```

### 6，测试源速度

nrm test  
其中，registry为源名。

比如：测试官方源和淘宝源的响应时间

```typescript
nrm test npm
```

```typescript
nrm test taobao
```