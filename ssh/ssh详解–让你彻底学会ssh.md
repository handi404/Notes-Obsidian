## [ssh](https://so.csdn.net/so/search?q=ssh&spm=1001.2101.3001.7020)详解–让你彻底学会ssh

### 概念

SSH全称secure shell，安全外壳[协议](https://so.csdn.net/so/search?q=%E5%8D%8F%E8%AE%AE&spm=1001.2101.3001.7020)（安全的shell），是一个计算机网络协议（默认端口号为22）。通过ssh协议可以在客户端**安全**（提供身份认证、信息加密）的**远程连接**LInux服务器或其他设备。

使用广泛的[Xshell软件](https://so.csdn.net/so/search?q=Xshell%E8%BD%AF%E4%BB%B6&spm=1001.2101.3001.7020)就是基于SSH协议远程连接。

SSH远程连接之后能干什么？

SSH远程连接之后，就可以像操作本地的机器一样操作远程机器。当需要操控的机器不在本地时就可以使用ssh协议远程连接操控。

### 实现

#### OpenSSH

SSH协议有诸多的实现软件，广泛使用的SSH实现软件是OpenSSH。OpenSSH是SSH协议的一种开源实现，现在已经成为Linux、Unix等操作系统的SSH协议默认实现。

OpenSSH官网：[点这里](https://www.openssh.com/)

官网的介绍：

OpenSSH is the premier connectivity tool for remote login with the SSH protocol. It encrypts all traffic to eliminate eavesdropping, connection hijacking, and other attacks. In addition, OpenSSH provides a large suite of secure tunneling capabilities, several authentication methods, and sophisticated configuration options.

OpenSSH软件分为两个部分：client端和server端

OpenSSH 服务端主要提供远程登录和文件传输等功能，可以被其他用户或系统通过 SSH 协议连接并访问。通过 SSH 连接到 OpenSSH 服务端后，用户可以在服务器上执行命令、上传或下载文件等操作。

OpenSSH 客户端则是用来连接远程服务端的工具。用户可以使用命令行或图形界面工具来连接到 OpenSSH 服务端，并通过 SSH 协议进行通信。OpenSSH 客户端可以通过用户名和密码、公钥验证等方式进行身份验证，一旦连接成功，用户就可以在远程服务器上执行命令、上传或下载文件等操作。

使用OpenSSH控制远程机器，需要对方安装了OpenSSH服务端。

#### 安装

OpenSSH基本不需要大家自己安装，大部分的Linux发行版，比如Debian、Ubuntu、CentOS等都默认安装好OpenSSH，相当于系统软件，当然如果因为某些原因没有安装OpenSSH，也可以使用下面的命令安装。

在 CentOS 或 Red Hat Enterprise Linux 中可以使用以下命令安装：

```shell
sudo yum install openssh-clients openssh-server
```

在 Ubuntu 或 Debian 中可以使用以下命令安装：

```shell
sudo apt install openssh-client openssh-server
```

查看安装版本（可以验证是否安装）

```shell
ssh -V
```

确认SSH服务正在运行

```shell
systemctl status sshd
```

#### 使用

确认安装好OpenSSH之后，就可以使用`ssh`远程控制其他机器了，当然被控制的机器也需要安装OpenSSH。在Linux中使用OpenSSH与其他软件相同都是通过命令的形式使用，其中进行连接的命令为`ssh`。

OpenSSH客户端连接服务端时需要进行身份认证，常用的**认证方式有两种**：密码认证和密钥认证。这里将用密码认证的方式进行基本的使用。

`ssh`命令进行远程连接有三种命令格式：

```shell
ssh 用户名@hostname ssh -l [用户名] hostname ssh hostname
```

**加深对ssh的理解**：使用Linux操作系统的时候，开机完成之后的第一件事就是登录用户，输入用户名然后输入密码，这个用户是Linux中已经创建的，然后密码存储在/etc/shadow中，这是本地登录的时候的过程。

而**SSH远程登录**也是差不多的，只是从本地换成远程。在**前两个命令**中，都可以传入用户名，这个用户名要求远程机器中已经创建，命令本质就是使用这个用户名去登录远程机器，然后**第三个命令**形式没有用户名，它默认使用的是你当前本地机器登录的用户名去登录远程机器（第二个命令不指定效果相同），比如你当前本地机器是root用户，使用第三个命令去远程连接，则会使用root用户去登录。**hostname**一般为远程机器的IP地址，也可使用主机域名。

连接完成之后就可以远程控制主机。

**示例**：使用`ssh 用户名@hostname`的形式连接远程机器。

host1模拟本地机器，IP地址：`192.168.94.142`；host2模拟远程服务器，IP地址：`192.168.94.143`，连接成功之后会发现第一提示符已经变为host2，这时就可以远程操纵host2了。连接过程截图如下所示：

![请添加图片描述](https://img-blog.csdnimg.cn/aee0fa17726b42a5a77feb06a30e1415.png)

-   ssh命令运行过程：
    1.  当第一次连接时会提醒`The autheticity of host '192.168.94.143' can't be establised`，这是由于第一次连接，`~/.ssh`下的konwn\_hosts文件无相关的主机信息（甚至这个文件都没有），无法信任对方主机，所以后续还会询问你是否进行连接，需要输入yes
    2.  当输入yes之后，就会将该主机的信息放入known\_hosts文件中（没有文件会自动生成），然后提示输入密码。

##### ~/.ssh文件路径

`~/.ssh`是存放SSH客户端相关配置和密钥文件的目录，但是第一次使用ssh的时候这个目录一般是空的，上述的konwn\_hosts只有使用了ssh之后才会自动创建，然后其他主机发送公钥至本机之后，本机会生成`authorized_keys`文件用于存放相关的主机和密钥信息。如果想要对客户端的配置进行修改，也可以在该目录下创建config文件。

### 进阶

如果你只是想在本地控制远程机器，上述的`ssh`命令已经能够满足你，但是关于`ssh`还有很多拓展。

#### 配置文件

一般我们想要更深刻的了解Linux的软件，基本都是从它的配置文件入手。

OpenSSH分为服务端和客户端，它的配置文件也有两个，sshd\_config（server配置文件）和ssh\_config（client配置文件）。

-   会发现一般Linux系统一些软件进程是name加一个d，这个d的含义是守护进程（daemon）。

##### ssh\_config

ssh\_config是OpenSSH客户端的配置文件，这是一个全局的配置文件，文件内容大多为远程连接时使用的参数，文件的路径`/etc/ssh/ssh_config`。虽然这个文件是客户端的全局配置文件，但是一般不会对这个配置文件进行修改。在配置文件中可以查看到下列内容。

![请添加图片描述](https://img-blog.csdnimg.cn/29050cd13c2445c1aed35ff12c37aae2.png)

由上图可知：ssh客户端的全局配置文件的优先级是**最低**的，**优先级最高**是使用命令时指定的参数，**其次**为单个用户的配置文件（路径设置`~/.ssh/config`，这个文件默认不存在，需要用户进行创建）。

基于以上的规则，修改的客户端全局配置文件并不是一个很好的选择，当需要对某一参数进行改变时，直接在使用`ssh`命令连接时指定相对应的参数更加方便灵活。

##### sshd\_config

sshd\_config是OpenSSH服务端的配置文件，路径`etc/ssh/sshd_config`，该文件包括服务端的安全配置等。ssh服务器的连接配置都由这个文件决定，包括SSH协议连接端口号等。

_常规sshd\_config文件内容：_

```
 1#$OpenBSD: sshd_config,v 1.100 2016/08/15 12:32:04 naddy Exp $
     2
     3# This is the sshd server system-wide configuration file.  See
     4# sshd_config(5) for more information.
     5
     6# This sshd was compiled with PATH=/usr/local/bin:/usr/bin
     7
     8# The strategy used for options in the default sshd_config shipped with
     9# OpenSSH is to specify options with their default value where
    10# possible, but leave them commented.  Uncommented options override the
    11# default value.
    12
    13# If you want to change the port on a SELinux system, you have to tell
    14# SELinux about this change.
    15# semanage port -a -t ssh_port_t -p tcp #PORTNUMBER
    16#
    17#Port 22
    18#AddressFamily any
    19#ListenAddress 0.0.0.0
    20#ListenAddress ::
    21
    22HostKey /etc/ssh/ssh_host_rsa_key
    23#HostKey /etc/ssh/ssh_host_dsa_key
    24HostKey /etc/ssh/ssh_host_ecdsa_key
    25HostKey /etc/ssh/ssh_host_ed25519_key
    26
    27# Ciphers and keying
    28#RekeyLimit default none
    29
    30# Logging
    31#SyslogFacility AUTH
    32
    33#LogLevel INFO
    34
    35# Authentication:
    36
    37#LoginGraceTime 2m
    38#PermitRootLogin yes
    39#StrictModes yes
    40#MaxAuthTries 6
    41#MaxSessions 10
    42
    43#PubkeyAuthentication yes
    44
    45# The default is to check both .ssh/authorized_keys and .ssh/authorized_keys2
    46# but this is overridden so installations will only check .ssh/authorized_keys
    47AuthorizedKeysFile.ssh/authorized_keys
    48
    49#AuthorizedPrincipalsFile none
    50
    51#AuthorizedKeysCommand none
    52#AuthorizedKeysCommandUser nobody
    53
    54# For this to work you will also need host keys in /etc/ssh/ssh_known_hosts
    55#HostbasedAuthentication no
    56# Change to yes if you don't trust ~/.ssh/known_hosts for
    57# HostbasedAuthentication
    58#IgnoreUserKnownHosts no
    59# Don't read the user's ~/.rhosts and ~/.shosts files
    60#IgnoreRhosts yes
    61
    62# To disable tunneled clear text passwords, change to no here!
    63#PermitEmptyPasswords no
    64
    65
    66# Change to no to disable s/key passwords
    67#ChallengeResponseAuthentication yes
    68ChallengeResponseAuthentication no
    69
    70# Kerberos options
    71#KerberosAuthentication no
    72#KerberosOrLocalPasswd yes
    73#KerberosTicketCleanup yes
    74#KerberosGetAFSToken no
    75#KerberosUseKuserok yes
    76
    77# GSSAPI options
    78GSSAPIAuthentication yes
    79GSSAPICleanupCredentials no
    80#GSSAPIStrictAcceptorCheck yes
    81#GSSAPIKeyExchange no
    82#GSSAPIEnablek5users no
    83
    84# Set this to 'yes' to enable PAM authentication, account processing,
    85# and session processing. If this is enabled, PAM authentication will
    86# be allowed through the ChallengeResponseAuthentication and
    87# PAM authentication via ChallengeResponseAuthentication may bypass
    88# the setting of "PermitRootLogin without-password".
    89# If you just want the PAM account and session checks to run without
    90# and ChallengeResponseAuthentication to 'no'.
    91# WARNING: 'UsePAM no' is not supported in Red Hat Enterprise Linux and may cause several
    92# problems.
    93UsePAM yes
    94
    95#AllowAgentForwarding yes
    96#AllowTcpForwarding yes
    97#GatewayPorts no
    98X11Forwarding yes
    99#X11DisplayOffset 10
   100#X11UseLocalhost yes
   101#PermitTTY yes
   102#PrintMotd yes
   103#PrintLastLog yes
   104#TCPKeepAlive yes
   105#UseLogin no
   106#UsePrivilegeSeparation sandbox
   107#PermitUserEnvironment no
   108#Compression delayed
   109#ClientAliveInterval 0
   110#ClientAliveCountMax 3
   111#ShowPatchLevel no
   112#UseDNS yes
   113#PidFile /var/run/sshd.pid
   114#MaxStartups 10:30:100
   115#PermitTunnel no
   116#ChrootDirectory none
   117#VersionAddendum none
   118
   119# no default banner path
   120#Banner none
   121
   122# Accept locale-related environment variables
   123AcceptEnv LANG LC_CTYPE LC_NUMERIC LC_TIME LC_COLLATE LC_MONETARY LC_MESSAGES
   124AcceptEnv LC_PAPER LC_NAME LC_ADDRESS LC_TELEPHONE LC_MEASUREMENT
   125AcceptEnv LC_IDENTIFICATION LC_ALL LANGUAGE
   126AcceptEnv XMODIFIERS
   127
   128# override default of no subsystems
   129Subsystemsftp/usr/libexec/openssh/sftp-server
   130
   131# Example of overriding settings on a per-user basis
   132#Match User anoncvs
   133#X11Forwarding no
   134#AllowTcpForwarding no
   135#PermitTTY no
   136#ForceCommand cvs server
   137
   138UseDNS no
   139AddressFamily inet
   140SyslogFacility AUTHPRIV
   141PermitRootLogin yes
   142PasswordAuthentication yes
```

1.  修改端口号，修改17行内容，取消注释，将22改成其他值，然后重启刷新服务（守护进程都需要重新启动服务才能使用新的配置），这时默认端口号将会改成其他值。  
    例：将端口号改为23后，如何使用`ssh`命令连接。  
    这个时候连接该机器则需要使用23号端口，不然会连接不上，使用`ssh`命令指定相应的23端口如下所示，三种格式都是可以使用-p参数的。
    
    ```shell
    # 使用-p参数指定接口 ssh 用户名@hostname -p 23
    ```
    
2.  19行和20行，ListenAddress，指定SSH监听的IP地址，19行是ipv4地址，20行是ipv6地址，通过更改设置，可以指定哪些IP地址的机器通过SSH连接本机（0.0.0.0和::代表监听所有IP地址）。
    
3.  31行，`#SyslogFacility AUTH`这个是日志相关的配置，指定记录日志facility，与日志记录相关，在日志学习中可以学到。
    
4.  38行，`#PermitRootLogin yes`设置是否允许使用root用户进行远程登录。默认为yes表示允许，去掉注释改为no则不再允许使用root用户远程登录。
    
5.  43行，`#PubkeyAuthentication yes`设置是否允许使用密钥认证，上文提到ssh有两种认证方式：密码认证和密钥认证。设置为no禁止使用密钥认证。
    
6.  142行，`#PasswordAuthentication yes`设置是否允许使用密码认证，设置为no则禁止密码认证。
    
7.  123行，`#Banner none`设置欢迎横幅，就是远程登录之后，出现的文字。
    

___

其他基本不用的配置：

1.  23行，`HostKey`指定ssh主机密钥路径，每个加密方法一个加密路径。
2.  69行，`ChallengeResponseAuthentication no`设置挑战-响应式身份认证
3.  93行，`UsePAM yes`设置使用PAM认证（可以理解为一个认证机制，密码认证是PAM的认证模块之一）

___

-   _可能每台机器配置文件的行数会不相同，这里的行数只是上面示例配置文件的行数_

 以上是常见的关于sshd的配置。那么修改这些配置的原因是什么？直接使用默认配置就已经能够正常使用ssh协议，修改配置的意义涉及另一个方面：安全。

 每台Linux系统的ssh默认配置是相同的，这样所有人都知道ssh协议默认是22号端口，那么这个时候IP地址再泄露，可以通过猜密码的方式去恶意的远程控制主机。而修改这些默认设置，比如修改端口号，其他人就很难知道该主机的ssh端口号，再进一步比如禁止root用户登录，降低远程登录的权限，通过一步步的加固，可以让Linux服务器更加安全。

#### 命令进阶

上文提到了`ssh`命令进行远程连接，这部分对`ssh`命令的参数进行详细的讲解。

##### ssh 命令

1.  远程登录，前面说过了，不再赘述（包括使用端口号登录）
    
2.  登录同时执行命令
    
    ```shell
    # 执行command格式、 # 命令执行完自动断开远程连接 ssh 用户名@hostname 'command' # 登录同时执行ls命令 ssh user@hostname 'ls' # 执行多条command，使用;分隔 ssh 用户名@hostname 'command1;command2;command3' # 例 ssh user@hostname 'ls;pwd;whoami'
    ```
    
3.  `-t`选项：提供互动式的shell，当执行命令需要一个互动的shell时使用，例如vim命令
    
    ```shell
    # 不加-t选项时，下面命令报错 ssh user@hostname 'vim' # 添加-t不报错 ssh -t user@hostname 'vim'
    ```
    
4.  `-f`和`-F`
    
    -   \-f：后台运行ssh连接
    -   \-F：指定参数配置文件，后接文件路径（path/ssh\_config）
5.  `-v`（小写）：显示命令的详细执行过程
    
6.  `-q`（quiet）：静默模式，不会输出警告信息（用了参数输错密码也不会提示，如下图所示）
    

![请添加图片描述](https://img-blog.csdnimg.cn/bfba84eca68c40048463787848eaf0c6.png)

7.  `ssh`其他参数大多不常用。

-   `ssh`还有一个端口转发的功能，但是大多时候用不上，类似端口转发的场景基本也不是用ssh去实现，这里就不作讲解了。

##### scp 命令

当需要在本地和远程进行文件传输时就可以使用scp命令，`scp`和`cp`命令效果完全相同（拷贝文件，包括拷贝多个文件），只是`scp`是远程拷贝，而`cp`是本地拷贝。`scp`分为将远程文件拷贝到本地和将本地文件拷贝到远端。

命令格式：

1.  将本地文件拷贝至远程机器
    
    ```shell
    # 将本地机器中的文件复制到远程机器中 scp /path/local_file remote_username@remote_ip:/path/target_file # 拷贝多个文件 scp file1.txt file2.txt file3.txt username@hostname:/remote/directory/ # 添加-r参数，递归拷贝目录 scp -r /path/local_directory remote_username@remote_ip:/path/target_directory
    ```
    
    -   当不指定target\_file的时候，会在远程机器上产生local\_file同名文件
2.  将远程机器文件拷贝至本地
    
    ```shell
    # 将远程机器中的文件复制到本地机器中 # 远程拷贝多个文件的命令形式比较繁琐，就不写了 scp remote_username@remote_ip:/path/source_file /path/target_file
    ```
    
    -   也可使用`-r`参数

**其他参数**

1.  `-v`：输出更详细
    
2.  `-P`（大写P）：指定ssh协议端口号
    
    ```shell
    # 指定使用23号端口 scp -P　23 /path/local_file remote_username@remote_ip:/path/target_file
    ```
    
3.  `-l`：限制传输速率（单位：kbit/s）
    

##### sftp 命令

sftp是一种通过安全通道进行文件传输的协议。与scp命令相同，sftp命令也能够进行本地和远端的文件传输。

sftp与scp的区别：scp在拷贝完文件之后会立即退出远程控制，不会提供交互式的shell，而sftp是交互式的，不需要一次完成，并且sftp有专门的命令语法对远程和本地机器的文件拷贝进行控制，功能更强大。

命令格式：

```shell
sftp remote_username@remote_ip
```

sftp是先使用ssh协议连接到远程主机，然后提供一个交换式shell，通过这个交互式的shell运行自带的命令实现文件传输。

在`sftp`成功之后，会发现第一标识符变为`sftp>`，这时已经切换到交互式shell：

![请添加图片描述](https://img-blog.csdnimg.cn/9cc8610c10f04ee89042822bc263e03a.png)

当第一标识符变为`sftp`之后，就可使用sftp自带的命令进行文件传输。

###### sftp自带命令

sftp自带命令与平常的Linux命令十分相似，然后控制本地主机和远程主机命令是一致的，唯一的区别：控制本地机器时需要在命令之前加`!`，不加`!`控制远程机器。

命令如下所示：

```shell
# 与Linux中命令完全相同 pwd ls cd mkdir rmdir：删除命令 # 不同指令，方括号中的参数可选，不指定本地或远程目录时，默认放在当前目录 put local_file [remotefile]：将本地文件传输到远程主机 get remotefile [local_file]：将远程文件传输到本机 exit：退出sftp
```

部分操作演示：

在host1`/root`创建文件test，然后使用sftp远程连接host2，host2`/root`不创建test目录

1.  host1sftp连接host2之后直接使用ls命令查看的是host2的`/root`，加上`!`则是查看host1的`/root`

![请添加图片描述](https://img-blog.csdnimg.cn/87389d9b8c1841d8ae73a649da41ce20.png)

2.  将host1的test文件传输至host2
    
    ![请添加图片描述](https://img-blog.csdnimg.cn/cf8e4c69c9174233a29aaeb1e59f6834.png)
    

#### ssh 密钥（配置免密通道）

最开始介绍了ssh的密码认证，ssh还有另一种更方便更安全的常用认证方式：密钥认证。在进行相关配置之后，连接比密码认证更便捷，只需配置一次，之后就不需要像密码认证一般每次连接输入密码（免密通道），而密码认证，每次都需要输入密码进行认证，有泄露密码的风险。

ssh密钥认证算法原理使用了非对称加密算法原理，通过使用公钥和私钥来实现。

##### 非对称加密

非对称加密是一种使用不同的密钥进行加密和解密的加密算法，另外还有使用相同密钥加密解密的算法——对称加密算法。在非对称加密中，有两个密钥：公钥（Public Key）和私钥（Private Key）。公钥用于加密数据，而私钥用于解密数据，使用不同的密钥进行加密解密安全性更高。

非对称加密通信过程：

在两台主机进行数据通信时，接受方将生成的公钥传送给发送方，发送方使用接收方的公钥对数据加密传输给接收方，接收方使用私钥进行解密。使用非对称加密时，只将公钥传输给对方，私钥不发送，并且只有私钥能够对信息进行解密，对比对称加密，安全性更高。

常用的非对称加密算法：rsa，dsa等

##### ssh 密钥认证原理

虽然ssh密钥认证使用非对称加密的原理，但是ssh密钥认证过程并不是与非对称加密通信过程完全相同，而是和数字签名的过程的比较相似，数字签名也使用了非对称加密，但是它是私钥加密，公钥解密，用于识别发送方的身份和验证数据的完整性。 ssh使用数字签名相似的原理进行了身份验证。

**在SSH密钥认证中**，客户端会生成一对密钥：私钥和公钥。私钥保存在客户端本地，而公钥则传输到服务器上。当客户端尝试连接到服务器时，客户端会使用私钥对一个挑战（challenge）进行加密，并将加密结果发送给服务器。服务器端会使用之前保存的公钥来验证客户端发送的数据。如果验证成功，服务器就确认客户端的身份，允许其登录。

##### ssh 配置免密通道实操

使用ssh公钥认证时请确保ssh服务器开启了公钥认证权限配置，下面是实现的具体步骤：

host1模拟本地机器，host2模拟远程机器

1.  首先在ssh客户端生成公钥和私钥对
    
    ```shell
    # 使用ssh-keygen命令生成密钥对，不带参数时默认使用rsa算法 ssh-keygen # 使用-t参数指定非对称加密算法 ssh-keygen -t dsa
    ```
    
    -   使用上述命令时，会有两次输入请求：
        
        ![请添加图片描述](https://img-blog.csdnimg.cn/41b18301a07d4761906e6908c21001c7.png)
        
        1.  询问你将密钥对放置位置，默认放置在~/.ssh路径下。不设置直接回车即可
        2.  询问你是否对密钥对设置密码（一般不设置，更方便，不然每次连接还是需要输入密码）。不设置直接回车即可， 这里需要回车两次，第二次是确认密码
    -   当生成完毕之后，可以在`~/.ssh`路径下可以看到新生成的密钥对（会多出两个文件），在不指定文件名称时，文件名称为加密算法名称，**其中.pub结尾的为公钥**：
        
        ![请添加图片描述](https://img-blog.csdnimg.cn/11ba561d2321418b817262ba746305ba.png)
        
2.  生成完毕之后，将公钥发送给需要远程连接的机器
    
    ```shell
    # 可以使用-i参数指定传送的公钥文件，如不指定自动在~/.ssh中进行查找 # 直接使用ssh-copy-id命令发送公钥 ssh-copy-id username@hostname # 使用-i参数指定公钥文件 ssh-copy-id -i key_file username@hostname
    ```
    
    -   传输公钥时，会要求输入服务器端的用户密码
        
    -   传输完成后，在host2的`~/.ssh`下可以看到多了个文件authorized\_keys，这个文件储存有host1的公钥，如下图，图二为文件内容：
        
        ![请添加图片描述](https://img-blog.csdnimg.cn/bdc7331b70a041d0b1e2fc9c8c68a035.png)
        

![请添加图片描述](https://img-blog.csdnimg.cn/3427a0b3570c4ce89f93de2142fc3076.png)

3.  将公钥发送给服务器之后即可进行连接
    
    ```shell
    # 使用ssh进行连接，会发现不再提醒你输入密码，直接连接成功 # 只要在生成密钥时未设置密码，之后的ssh连接都不再需要输入密码 ssh 用户名@hostname
    ```
    

### 其他

这部分是一些和ssh协议有关系的操作或者命令的简单介绍

#### rsync命令

rsync命令是一个文件同步备份的命令，除了本地同步，还支持远程同步，可以将其理解为remote sync（远程同步）。除了支持远程同步备份与cp，tar等命令不同之外，rsync是一个增量同步的命令，只有数据存在差异时，才会覆盖不同的部分。

rsync命令十分好用，这个命令底层有两个实现协议：ssh协议和rsync协议。因为它也用到了ssh协议这里顺带讲一下。（只是简单提一下）

rsync和scp很相像，但是rsync的性能比scp好，还支持增量同步，而且rsync支持更复杂的参数，有更多的控制选项，rsync相当于scp的上位命令。

使用rsync命令需要两台机器都安装rsync命令。

```shell
# 安装rsync yum install rsync -y sudo apt install rsync
```

rsync命令有几种格式：

```shell
# 用于本地备份 rsync SRC DEST # 将本地数据备份到远程机器上 rsync SRC [USER@]HOST:DEST # 将远程数据备份到本地 rsync [USER@]HOST:SRC DEST # 剩下的两个格式和上面两个是相同的，只是用来两个`:`，这代表使用rsync协议，而上面的命令则是使用ssh协议 rsync [USER@]HOST::SRC DEST rsync SRC [USER@]HOST::DEST
```

可用参数：

1.  `-a, --archive`: 归档模式，递归复制文件和文件夹，并保留所有属性、权限和时间信息。
2.  `-v, --verbose`: 显示详细输出，包括复制的文件列表和进度信息。
3.  `-r, --recursive`: 递归复制目录及其内容。
4.  `-u, --update`: 仅复制源文件中更新的部分。
5.  `-z, --compress`: 在传输过程中压缩文件，可以加快传输速度。
6.  `-P, --partial --progress`: 显示传输过程的进度信息，并保留未完成的文件。
7.  `--delete`: 删除目标路径上多余的文件。

rsync相较于使用ssh协议，使用更多的是rsync协议，一般称为`rsync-daemon`模式，这个模式更稳定，更安全，只是没有但用rsync命令那么灵活，需要在被传输机器上进行相关配置，这里就不讲解了。