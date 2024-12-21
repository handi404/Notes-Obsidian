## 一、简介

### 1.1. npm、pnpm、[yarn](https://so.csdn.net/so/search?q=yarn&spm=1001.2101.3001.7020)包管理工具介绍

[npm、pnpm、yarn包管理工具介绍](https://blog.csdn.net/qq_44741577/article/details/136811200?spm=1001.2014.3001.5501)

### 1.2. 为什么下包速度慢？

> 在下包的时候，默认从国外的 `https:/registry.npmjs.org/` 服务器进行下载，此时，网络数据的传输需要经过漫长的海底光缆，因此下包速度会很慢。

### 1.3. 淘宝镜像服务器介绍

淘宝在国内搭建了一个服务器，专门把国外官方服务器上的包同步到国内的服务器，然后在国内提供下包的服务从而极大的提高了下包的速度  
![image.png](https://i-blog.csdnimg.cn/blog_migrate/4af9993b79eab10f9e28a843ec3dbfee.png)

### 1.4. 淘宝镜像域名更换说明

> **原域名：** `https://registry.npm.taobao.org/` 在 `2022.06.30` 号正式下线和停止 DNS 解析  
> **新域名：** `https://registry.npmmirror.com/`

## 二、npm包下载慢的三种解决方案

### 2.1. 解决方案一：安装[cnpm](https://so.csdn.net/so/search?q=cnpm&spm=1001.2101.3001.7020)

```bash
npm install -g cnpm --registry=https://registry.npmmirror.com/ 
// 查看版本 
cnpm -v // 之后下载东西就用cnpm代替npm下载
```

### 2.2. 解决方案二：改变npm默认下载地址

#### 2.2.1. 方式一：通过命令修改

```bash
// 查看当前下载地址 
npm config get registry 

// 设置淘宝镜像的地址 
npm config set registry https://registry.npmmirror.com/ 

// 查看当前的下载地址 
npm config get registry
```

> 相比较方案一，不用下载`cnpm`，安装命令依旧是`npm`安装

#### 2.2.2. 方式二：直接修改.npmrc配置文件

-   本地电脑中（对应npm的全局配置）

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/e0efb6c2c9f9577e7aa8c103726599e7.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/61e0492bf7da498e752e973e12daeed7.png)

-   项目中（对应项目中的npm配置）  
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/5863235c0aa9e7269e469ece83cd4d20.png)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8a22c857d3142b49134b24df1c7775dd.png)

### 2.3. .npmrc配置优先级说明

> .npmrc配置优先级  
> 当我们在多个配置文件中定义相同的键时，npm将按照以下顺序查找和应用配置：
> 
> -   项目根目录下的.npmrc文件(最高优先级)
> -   用户电脑中的.npmrc文件
> -   npm内置的默认配置

### 2.3. 解决方案三：使用nrm地址管理源管理

> 随时切换下载地址

```bash
// nrm安装 npm install -g nrm // 查看可选源 星号代表当前使用源 nrm ls // 切换为taobao源 nrm use taobao // 查看当前正在使用的镜像源 nrm current
```

![image.png](https://i-blog.csdnimg.cn/blog_migrate/49bfb9c120fe70adfd5b62204834ee0e.png)

### 2.4. 发布npm包注意还原默认源

```bash
// 设置官方服务器镜像的地址 
npm config set registry https:/registry.npmjs.org/ 

// 查看当前的下载地址 
npm config get registry
```

## 三、yarn切换镜像源

### 3.1. 查看当前的镜像源。

```bash
yarn config get registry
```

### 3.2. 设置为淘宝源

```bash
yarn config set registry https://registry.npmmirror.com/
```

### 3.3. 还原默认源

```bash
yarn config set registry https://registry.yarnpkg.com
```

## 四、pnpm切换镜像源

### 4.1. 查看当前镜像源

```
pnpm config get registry
```

### 4.2. 设置为淘宝镜像源

```
pnpm config set registry https://registry.npmmirror.com/
```

### 4.3. 切回原镜像源

```
pnpm config set registry https://registry.npmjs.org
```