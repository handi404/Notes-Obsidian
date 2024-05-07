## 一、通过ssh连接服务器

1、打开vscode，进入拓展（Ctrl+Shift+X），下载拓展Remote - SSH。

![](https://img-blog.csdnimg.cn/direct/e651df2212804cb58d275ab10f92dcce.png)

2、点击远程资源管理器[选项卡](https://so.csdn.net/so/search?q=%E9%80%89%E9%A1%B9%E5%8D%A1&spm=1001.2101.3001.7020)，选择远程（隧道/SSH）类别。

![](https://img-blog.csdnimg.cn/direct/c485d238e6cc4f2f8904a8d4c4b432ae.png)

3、点击SSH配置。

![](https://img-blog.csdnimg.cn/direct/bea6594037c544dc8d54597f36fc2bd4.png)

4、在中间上部分弹出的配置文件中点击第一个....config。

![](https://img-blog.csdnimg.cn/direct/e54df5ca87994c958f3bd3171f5f2f93.png)

5、在点进的config文件中输入以下内容。

![](https://img-blog.csdnimg.cn/direct/5f115561279d4b159541146df21475b1.png)

ps：

这里的hostname是服务器的ip地址；

port端口号有就写上，没有的话可以不写；

user是服务器上用户的用户名

（例如：Linux中 “用户名”+@+“服务器ip地址” 就是访问服务器上用户的服务器用户访问地址。）

![](https://img-blog.csdnimg.cn/direct/16cfcfb06d3246e1815d6987e0ee157d.png)

6、点击保存后点击刷新按钮。

![](https://img-blog.csdnimg.cn/direct/32c8340699e3406aa118e1c5a107dcdf.png)

7、这时候就可以看到刚刚创建的配置了。

![](https://img-blog.csdnimg.cn/direct/6a23d787b534428da53a1f9be0770f89.png)

8、Ctrl + Shift + P，打开命令窗口，输入ssh connect to host，选择第一个，

![](https://img-blog.csdnimg.cn/direct/b760c592783e4261b6b782994c3850b0.png)

9、选择刚刚创建好的那个配置。

![](https://img-blog.csdnimg.cn/direct/32793267133e4cd198069e46159c7098.png)

10、询问是否保存known\_hosts，选择Continue。

![](https://img-blog.csdnimg.cn/direct/12d4450cac3445038261c76ca8823f38.png)

11、输入服务器上用户的密码

![](https://img-blog.csdnimg.cn/direct/8559c87ce232425ca6c28327bc65c7ee.png)

12、该用户第一次访问该服务器可以看到该提示信息，耐心等待，这时是插件在服务器上面安装需要的依赖，大约会占用服务器150mb左右的空间。

![](https://img-blog.csdnimg.cn/direct/ef2e2811334f4772ba138e3e903ce398.png)

13.如果长时间都一直是该情况，可以使用Ctrl + Shift + P，打开命令窗口，输入reload window来重新加载窗口（会要求你重新手动输入密码）。

![](https://img-blog.csdnimg.cn/direct/62fa9257104a4d459b58711f654ae8ff.png)

14.最终显示如下页面就代表已经连接成功了。

![](https://img-blog.csdnimg.cn/direct/baaec36eace74f38bd930b5151847a4d.png)

## 二、设置免密登录

1、生成ssh使用的公钥/密钥对。（公钥给服务器用，秘钥给自己客户端用）

   \[在[vscode终端](https://so.csdn.net/so/search?q=vscode%E7%BB%88%E7%AB%AF&spm=1001.2101.3001.7020)或者cmd中进行如下代码输入\]

（1）直接使用终端在用户本机生成公钥和私钥。输入命令`ssh-keygen -t rsa`：

```undefined
ssh-keygen -t rsa
```

（2）终端会出现以下提示，可以自己定义密钥名(就是自己定义文件所在位置)，也可以直接跳过，默认在C盘中的C:\\Users\\“用户名”\\.ssh中。

```cobol
Generating public/private rsa key pair.

Enter file in which to save the key (/Users/~your-local-username~/.ssh/id_rsa):
```

生成如下配置文件：

![](https://img-blog.csdnimg.cn/direct/84e0a75a182c4f669dca2de157fb0715.png)

ps：

这里id\_rsa.pub就是公钥，在服务器端使用；

id\_rsa是私钥在用户端使用；

config文件是之前设置的hostname、port、user的那个文件

![](https://img-blog.csdnimg.cn/direct/da9cc3cd9fda4d6c897663c9922796c7.png)

（3）接下来终端会提示输入密码 passphrase，这个密码为生成私钥的密码，将来防止私钥被其他人盗用。这里可以设定，也可以不输入任何密码，直接回车，再次提示输入密码，再次回车。生成新的密钥。

![](https://img-blog.csdnimg.cn/direct/3eb330d3061540798ea2d38126dd6f98.png)

（4）在服务器路径下创建.ssh文件夹

```bash
mkdir ~/.ssh

cd ~/.ssh

touch authorized_keys
```

![](https://img-blog.csdnimg.cn/direct/7a779d0975a746bea32f8fb3fbf40f69.png)

```csharp
# 把公钥文件id_rsa.pub拷贝到需要登录的服务器上（存储路径可以随意，但是之后加入到另一个文件尾部的时候注意路径地址。）
```

![](https://img-blog.csdnimg.cn/direct/057f4f55c7f84ec1adbf3ae0062fcb89.png)

（5）将公钥id\_rsa.pub填充到authorized\_keys尾部

```bash
cat /home/id_rsa.pub >> ~/.ssh/authorized_keys
```

（6）配置 SSH 客户端**（！！！！这里可以不设置。如果之前的步骤已经可以免密连接服务器，那这一步就不设置了！！！！）**

打开你的 SSH 客户端（本机）配置文件（也就是前面生成的config文件，一般在`C:\Users\YourUsername\.ssh\config`），添加配置（`IdentityFile` **私钥**文件路径），以指定使用哪个私钥文件。下图红框为我添加的内容。

![](https://img-blog.csdnimg.cn/direct/485d022a456643b893fcb2fe08311fbb.png)

配置完成后即可免密远程登录其他服务器啦！！！~~~

 参考博客：

[https://blog.csdn.net/savet/article/details/131683156](https://blog.csdn.net/savet/article/details/131683156 "https://blog.csdn.net/savet/article/details/131683156")

[服务器免密登录-CSDN博客](https://blog.csdn.net/weixin_43551076/article/details/131721687?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-131721687-blog-135903171.235%5Ev43%5Epc_blog_bottom_relevance_base1&spm=1001.2101.3001.4242.1&utm_relevant_index=3 "服务器免密登录-CSDN博客")

[VSCode配置 SSH连接远程服务器+免密连接教程 - 知乎](https://zhuanlan.zhihu.com/p/667236864?utm_id=0 "VSCode配置 SSH连接远程服务器+免密连接教程 - 知乎")

[Linux免密登录远程服务器\_id\_rsa.pub linux-CSDN博客](https://blog.csdn.net/qq_45305209/article/details/131242918 "Linux免密登录远程服务器_id_rsa.pub linux-CSDN博客")