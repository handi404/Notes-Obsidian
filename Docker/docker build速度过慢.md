
#### docker速度过慢的原因在于，docker的默认源主要是在国外，因此会慢，解决办法是替换掉它.

##### 我这里使用的是阿里的源：

```
# 更换阿里源 sed修改文件中的内容 sed -i "s/原字符串/新字符串/g"
RUN sed -i "s/archive.ubuntu./mirrors.aliyun./g" /etc/apt/sources.list
RUN sed -i "s/deb.debian.org/mirrors.aliyun.com/g" /etc/apt/sources.list
RUN sed -i "s/security.debian.org/mirrors.aliyun.com\/debian-security/g" /etc/apt/sources.list
RUN sed -i "s/httpredir.debian.org/mirrors.aliyun.com\/debian-security/g" /etc/apt/sources.list
```

##### python 的pip也替换成国内的阿里源

```
RUN pip install -U pip
RUN pip config set global.index-url http://mirrors.aliyun.com/pypi/simple
RUN pip config set install.trusted-host mirrors.aliyun.com
```

##### 以上命令均写在 Dockerfile 中.
