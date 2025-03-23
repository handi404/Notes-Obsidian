​     

# 【Linux】Linux常用命令60条（含完整命令语句）

Linux是一个强大的操作系统，它提供了许多常用的命令行工具，可以帮助我们用于管理文件、目录、进程、网络和系统配置等。以下是一些常用的[Linux命令](https://so.csdn.net/so/search?q=Linux%E5%91%BD%E4%BB%A4&spm=1001.2101.3001.7020)：

#### 1\. ls：列出当前目录中的文件和子目录

```
ls
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/dee2f6696bbde456168a66e0fb4bcba3.png)

#### 2\. pwd：显示当前工作目录的路径

```
pwd
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/32c533e31b39faa792fe7d15780f233e.png)

#### 3\. cd：切换工作目录

```
cd /path/to/directory
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2a2c57790ee0a81367bb5823d9444372.png)

#### 4\. mkdir：创建新目录

```
mkdir directory_name
```

#### 5\. rmdir：删除空目录

```
rmdir directory_name
```

#### 6\. rm：删除文件或目录

```
rm file_name
rm -r directory_name  # 递归删除目录及其内容
```

#### 7\. cp：复制文件或目录

```
cp source_file destination
cp -r source_directory destination  # 递归复制目录及其内容
```

#### 8\. mv：移动或重命名文件或目录

```
mv old_name new_name
```

#### 9\. touch：创建空文件或更新文件的时间戳

```
touch file_name
```

#### 10\. cat：连接和显示文件内容

```
cat file_name
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/cce3ed025084d117547774e9b58997ca.png)

#### 11\. more/less：逐页显示文本文件内容

```
more file_name
less file_name
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7f604ab85471abac96350be671f8ef9b.png)

#### 12\. head/tail：显示文件的前几行或后几行

```
head -n 10 file_name  # 显示文件的前10行
tail -n 20 file_name  # 显示文件的后20行
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a2a9aa656363f373f53d7643bf8e35fc.png)

#### 13\. grep：在文件中搜索指定文本

```
grep search_term file_name
```

#### 14\. ps：显示当前运行的进程

```
ps aux
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8e981cda24c114aa8cfc5d133c0ddbad.png)

#### 15\. kill：终止进程

```
kill process_id
```

#### 16\. ifconfig/ip：查看和配置网络接口信息

```
ifconfig
ip addr show
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3f326946d5263ba1b81d69f7ed25bd2d.png)

#### 17\. ping：测试与主机的连通性

```
ping host_name_or_ip
```

#### 18\. wget/curl：从网络下载文件

```
wget URL
curl -O URL
```

#### 19\. chmod：修改文件或目录的权限

```
chmod permissions file_name
```

#### 20\. chown：修改文件或目录的所有者

```
chown owner:group file_name
```

#### 21\. tar：用于压缩和解压文件和目录

```
tar -czvf archive.tar.gz directory_name  # 压缩目录
tar -xzvf archive.tar.gz  # 解压文件
```

#### 22\. df/du：显示磁盘使用情况

```
df -h  # 显示磁盘空间使用情况
du -h directory_name  # 显示目录的磁盘使用情况
```

输出结果为：

```
(wzk_base) wangzhenkuan@pc-System-Product-Name:~$ df -h
文件系统        大小  已用  可用 已用% 挂载点
tmpfs           6.2G  2.9M  6.2G    1% /run
/dev/nvme0n1p3  861G  288G  530G   36% /
tmpfs            31G     0   31G    0% /dev/shm
tmpfs           5.0M  4.0K  5.0M    1% /run/lock
/dev/nvme0n1p1  511M  6.1M  505M    2% /boot/efi
/dev/sda        1.8T  1.2T  521G   71% /home
tmpfs           6.2G  112K  6.2G    1% /run/user/1000
tmpfs           6.2G   68K  6.2G    1% /run/user/1001
tmpfs           6.2G   68K  6.2G    1% /run/user/1003
tmpfs           6.2G   68K  6.2G    1% /run/user/1008
tmpfs           6.2G   68K  6.2G    1% /run/user/1006
tmpfs           6.2G   72K  6.2G    1% /run/user/1005
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b9c88ea889d69fdc68f0eae6b0ef68d2.png)

#### 23\. mount/umount：挂载和卸载文件系统

```
mount /dev/sdX1 /mnt  # 挂载分区到指定目录
umount /mnt  # 卸载挂载的文件系统
```

#### 24\. psql/mysql：用于与PostgreSQL或MySQL数据库交互的命令行工具

```
psql -U username -d database_name  # 连接到PostgreSQL数据库
mysql -u username -p  # 连接到MySQL数据库
```

#### 25\. top/htop：显示系统资源的实时使用情况和进程信息

```
top
htop
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9fe8c43e80a51266415c03c3b74b9332.png)

#### 26\. ssh：远程登录到其他计算机

```
ssh username@remote_host
```

#### 27\. scp：安全地将文件从本地复制到远程主机，或从远程主机复制到本地

```
scp local_file remote_user@remote_host:/remote/directory
```

#### 28\. find：在文件系统中查找文件和目录

```
find /path/to/search -name "file_pattern"
```

#### 29\. grep：在文本中搜索匹配的行，并可以使用正则表达式进行高级搜索

```
grep -r "pattern" /path/to/search
```

#### 30\. sed：流编辑器，用于文本处理和替换

```
sed 's/old_text/new_text/' file_name
```

#### 31\. awk：用于文本处理和数据提取的文本处理工具

```
awk '{print $1}' file_name  # 提取文件中的第一列数据
```

#### 32\. ssh-keygen：生成SSH密钥对，用于身份验证远程服务器

```
ssh-keygen -t rsa
```

#### 33\. date：显示或设置系统日期和时间

```
date
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c6fa394ff831201437fe89ca991a5e55.png)

#### 34\. echo：将文本输出到标准输出

```
echo "Hello, World!"
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b2cd180d63faffb7a70f8a7f9d2dbfc8.png)

#### 35\. ln：创建硬链接或符号链接

```
ln source_file link_name  # 创建硬链接
ln -s source_file link_name  # 创建符号链接
```

#### 36\. uname：显示系统信息

```
uname -a
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7806e707d46e73fdfa2ca3fffd9fe212.png)

#### 37\. shutdown/reboot：关闭或重新启动系统

```
shutdown -h now  # 立即关闭系统
reboot  # 重新启动系统
```

#### 38\. who/w：显示当前登录的用户信息

```
who
w
```

#### 39\. curl：用于与网络资源进行交互，支持各种协议

```
curl -X GET http://example.com
```

#### 40\. zip/unzip：用于压缩和解压ZIP文件

```
zip archive.zip file1 file2  # 压缩文件
unzip archive.zip  # 解压ZIP文件
```

#### 41\. chmod/chown：修改文件或目录的权限和所有者

```
chmod permissions file_name  # 修改文件权限
chown owner:group file_name  # 修改文件所有者
```

#### 42\. useradd/userdel：用于添加和删除用户账户

```
useradd new_user  # 添加用户
userdel username  # 删除用户
```

#### 43\. passwd：更改用户密码

```
passwd username
```

#### 44\. cron：定时任务管理器，用于自动执行计划任务

```
crontab -e  # 编辑用户的定时任务
```

#### 45\. uptime：显示系统的运行时间和负载情况

```
uptime
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/5e17da8b304d0781d3e5d7a02999ee19.png)

#### 46\. hostname：显示或设置计算机的主机名

```
hostname  # 显示主机名
```

#### 47\. iptables/ufw：用于配置防火墙规则

```
iptables -A INPUT -p tcp --dport 80 -j ACCEPT  # 允许HTTP流量
ufw enable  # 启用Uncomplicated Firewall
```

#### 48\. netstat/ss：显示网络连接信息

```
netstat -tuln  # 显示所有TCP和UDP端口
ss -tuln  # 使用Socket Stat查看网络连接
```

#### 49\. ps/top/htop：显示进程信息和系统资源使用情况

```
ps aux  # 显示所有进程
top  # 实时监视系统资源
htop  # 更友好的进程监视器
```

#### 50\. history：查看命令历史记录

```
history
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d3ba7864e4174ba1d7e50a4a59ddb2fd.png)

#### 51\. free：显示系统内存使用情况

```
free -m  # 以MB为单位显示内存使用情况
```

#### 52\. lsblk/fdisk：查看磁盘分区信息和管理磁盘

```
lsblk  # 显示块设备信息
fdisk /dev/sdX  # 打开磁盘分区工具
```

#### 53\. nc：用于网络连接测试和数据传输

```
nc -vz host_name_or_ip port  # 测试主机的端口是否可达
```

#### 54\. stat：显示文件或目录的详细信息

```
stat file_or_directory
```

#### 55\. nmcli：用于管理网络连接的命令行工具

```
nmcli connection show  # 显示网络连接信息
```

#### 56\. tailf：实时追踪文件的末尾，类似于tail -f

```
tailf file_name
```

#### 57\. scp：安全地将文件从本地复制到远程主机，或从远程主机复制到本地

```
scp local_file remote_user@remote_host:/remote/directory  # 从本地到远程
scp remote_user@remote_host:/remote/file local_directory  # 从远程到本地
```

#### 58\. rsync：用于在本地和远程系统之间同步文件和目录

```
rsync -avz source_directory/ remote_user@remote_host:/remote/directory/
```

#### 59\. dd：用于复制和转换文件

```
dd if=input_file of=output_file bs=block_size
```

#### 60\. sudo：以超级用户权限运行命令

```
sudo command_to_run_as_superuser
```

sudo命令允许普通用户执行需要超级用户权限的命令，前提是他们在sudoers文件中有相应的权限。这是确保系统安全性的关键工具，要谨慎使用。
