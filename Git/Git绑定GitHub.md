> GitHub是个代码托管平台，可以利用git进行项目版本维护，如果使用GitHub管理项目，有必要使用git和GitHub关联起来。

##### 上车准备

-   有GitHub账号  
    没有账号需要注册，github注册只需要一个有效邮箱就可以了 [地址](https://github.com/)
-   安装git  
    [安装教程](https://www.runoob.com/git/git-install-setup.html)

##### 出发

###### 1、 打开gitBash

> 鼠标右键会有两个git选项，只能选择 **Git Bash Here**才能打开[git命令](https://so.csdn.net/so/search?q=git%E5%91%BD%E4%BB%A4&spm=1001.2101.3001.7020)窗口

![gitBash](https://img-blog.csdnimg.cn/20210419162558819.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

###### 2、输入ssh-keygen -t rsa -C 生成秘钥id\_rsa.pub

```shell
ssh-keygen -t rsa -C '替换成自己的GitHub邮箱账号'
```

> 输入命令后 只有overwrite 选择需要输入Y以外其它直接按回车就行

![生成秘钥](https://img-blog.csdnimg.cn/20210419163321206.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

###### 3、获取生成秘钥id\_rsa.pub

> 从提示的路径找到对应的.ssh文件，打开id\_rsa.pub文件，复制里面的数据

![生成的秘钥目录文件](https://img-blog.csdnimg.cn/20210419164441579.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

###### 4、将复制的秘钥添加到github上

> 步骤1：点击头像打开settings；

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210419174848780.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

> 步骤2：打开页面左边的SSH and GPG keys选项，点击 New SSH key；

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210419174911703.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

> 最后：将秘钥粘贴到Key框内点击Add SSH key即。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210419174957138.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

###### 5、测试是否绑定成功

> 在git工具输入：ssh -T git@github.com，显示 You\`ve successfully authenticated 表示已经绑定

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210419183243138.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1OTYyNzQw,size_16,color_FFFFFF,t_70)

**over**