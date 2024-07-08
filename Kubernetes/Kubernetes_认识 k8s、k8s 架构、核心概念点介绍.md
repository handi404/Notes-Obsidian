![](https://img-blog.csdnimg.cn/9d373847b945456aa8eb92bd0b2ab37b.jpeg)

**目录**

[一、Kubernetes 简介](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#%E4%B8%80%E3%80%81Kubernetes%20%E7%AE%80%E4%BB%8B)

[二、Kubernetes 架构](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t0)

[三、Kunbernetes 有哪些核心概念？](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t1)

[1\. 集群 Cluster](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t2)

[2\. 容器 Container](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t3)

[3\. POD](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t4)

[4\. 副本集 ReplicaSet](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t5)

[5\. 服务 service](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t6)

[6\. 发布 Deployment](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t7)

[7\. ConfigMap/Secret](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t8)

[8\. DaemonSet](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t9)

[9\. 核心概念总结](https://blog.csdn.net/weixin_53072519/article/details/125228115?spm=1001.2014.3001.5506#t10)

___

### 一、Kubernetes 简介

Kubernetes 简称 [k8s](https://so.csdn.net/so/search?q=k8s&spm=1001.2101.3001.7020)，是支持云原生部署的一个平台，起源于谷歌。谷歌早在十几年之前就对其应用，通过容器方式进行部署。

k8s 本质上就是用来简化微服务的开发和部署的，关注点包括自愈和自动伸缩、调度和发布、调用链监控、配置管理、Metrics 监控、日志监控、弹性和容错、API 管理、服务安全等，k8s 将这些微服务的公共关注点以组件形式封装打包到 k8s 这个大平台中，让开发人员在开发微服务时专注于业务逻辑的实现，而不需要去特别关系微服务底层的这些公共关注点，大大简化了微服务应用的开发和部署，提高了开发效率。

### 二、Kubernetes 架构

k8s 总体架构采用了经典的 master slave 架构模式，分 master 节点和 worker 节点，节点可以是虚拟机也可以是物理机。

![](https://img-blog.csdnimg.cn/79673d9dc3df46a286be1aa65923be0e.png)

master 节点由以下组件组成；

> -   **etcd**，一种的分布式存储机制，底层采用 Raft 协议，k8s 集群的状态数据包括配置、节点等都存储于 etcd 中，它保存了整个集群的状态。
> -   **API server**，对外提供操作和获取 k8s 集群资源的的 API，是唯一操作 etcd 的组件，其他的组件包括管理员操作都是通过 API server 进行交互的，可以将它理解成 etcd 的 “代理人”。
> -   **Scheduler**，在 k8s 集群中做调动决策，负责资源的调度，按照预定的调度策略将 Pod 调度到相应的机器上。
> -   **Controller Manager**，相当于集群状态的协调者，观察着集群的实际状态，与 etcd 中的预期状态进行对比，如果不一致则对资源进行协调操作让实际状态和预期状态达到最终的一致，维护集群的状态，比如故障检测、自动扩展、滚动更新等。

worker 节点由以下组件组成：

> -   **Controller Runtime**，下载镜像和运行容器的组件，负责镜像管理以及 Pod 和容器的真正运行（CRI）。
> -   **Pod**，k8s 中特有的一个概念，可以理解为对容器的包装，是 k8s 的基本调度单位，实际的容器时运行在 Pod 中的，一个节点可以启动一个或多个 Pod。
> -   **kubelet**，负责管理 worker 节点上的组件，与 master 节点上的 API server 节点进行交互，接受指令执行操作。
> -   **kube-proxy**，负责对 Pod 进行寻址和负载均衡

用户操作 k8s 集群一般是通过 [kubectl](https://so.csdn.net/so/search?q=kubectl&spm=1001.2101.3001.7020) 命令行工具或者 dashboard；Pod 之间进行通讯是通过集群内部的覆盖网络 Overlay Network，外部流量想要进入集群访问 Pod 则是通过负载均衡 Load Balander 设备进行。

### 三、Kunbernetes 有哪些核心概念？

#### 1\. 集群 Cluster

集群有多个节点组成且可以按需添加节点（物理机/虚拟机），每一个节点都包含一定数量的 CPU 和内存 RAM。

![](https://img-blog.csdnimg.cn/61ecf0812cd042e0ace62ea1ef6ba535.png)

#### 2\. 容器 Container

k8s 本身是一个容器调度平台，从宿主机操作系统来看，容器就是一个一个的进程。从容器内部来看容器就是一个操作系统，它有着自己的网络、CPU、文件系统等资源。

![](https://img-blog.csdnimg.cn/933e6007c8d74de396365797ddea7a5d.png)

#### 3\. POD

k8s 也不是直接调度容器的，而是将其封装成了一个个 POD，POD 才是 k8s 的基本调度单位。每个 POD 中可以运行一个或多个容器，共享 POD 的文件系统、IP 和网络等资源，每一个 POD 只有一个 IP。

![](https://img-blog.csdnimg.cn/a4033f24276342598144e15e7bc4bb5d.png)

#### 4\. 副本集 ReplicaSet

一个应用发布时会发布多个 POD 实例，副本集可对应一个应用的一组 POD，它可以通过模板来规范某个应用的容器镜像、端口，副本数量等。运行时副本集会监控和维护 POD 的数量，数量过多则会下线 POD，过少则启动 POD。

![](https://img-blog.csdnimg.cn/a6119e5fa2504932a6ada96e1aebe2a2.png)

#### 5\. 服务 service

POD 在 k8s 中是不固定的，可能会挂起或者重启，且挂起重启都是不可预期的，那么这就会导致服务的 IP 也随着不停的变化，给用户的寻址造成一定的困难。而 service 就是用来解决这个问题的，它屏蔽了应用的 IP 寻址和负载均衡，消费方可直接通过服务名来访问目标服务，寻址和负载均衡均由 service 底层进行。

#### 6\. 发布 Deployment

副本集就是一种基本的发布机制，可以实现基本的或者高级的应用发布，但操作较为繁琐。未来简化这些操作，k8s 引入了 Deployment 来管理 ReplicaSet，实现一些高级发布机制。

![](https://img-blog.csdnimg.cn/ae7eef7214784c40a5a741c74d459fc0.png)

#### 7\. ConfigMap/Secret

微服务在上线时需要设置一些可变配置，环境不同则配置值不同，有些配置如数据库的连接字符串在启动时就应该配好，有些配置则可以在运行中动态调整。为了实现针对不同环境灵活实现动态配置，微服务就需要 ConfigMap 的支持。

k8s 平台内置支持微服务的配置（ConfigMap），开发人员将配置填写在 ConfigMap 中，k8s 再 将 ConfigMap 中的配置以环境变量的形式注入 POD，这样 POD 中的应用就可以访问这些配置。

Secret 是一种特殊的 ConfigMap，提供更加安全的存储和访问配置机制。

#### 8\. DaemonSet

在微服务中，每个节点需要配置一个常驻守护进程。DaemonSet 可支持在每一个 worker 节点上面配置一个守护进程 POD 并且保证每一个节点上有且仅有一个 POD。

#### 9\. 核心概念总结

| 概念 | 作用 |
| --- | --- |
| cluster | 超大计算机抽象，由节点组成 |
| Container | 应用居住和运行在容器中 |
| Pod | Kubernetes 基本调度单位 |
| Service | 应用Pods的访问点，屏蔽IP寻址和负载均衡 |
| 
Deployment

 | 管理ReplicaSet，支持滚动等高级发布机制 |
| ConfigMap/Secrets | 应用配置，secret敏感数据配置 |
| DaemonSet | 保证每个节点有且仅有一个Pod，常见于监控 |

_以上概念点为 Kubernetes 最基本和最重要的概念总结，掌握后可适用于绝大部分云原生场景，还有部分概念本文未作介绍。_