#### 文章目录

-   [协程（goroutine）](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#goroutine_1)
-   -   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#_2)
    -   [GMP模型](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#GMP_54)
    -   [协程间共享变量](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#_212)
-   [通道（channel）](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_267)
-   -   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#_268)
    -   [channel的定义方式](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_279)
    -   [channel的读写](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_309)
    -   [channel的关闭](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_362)
    -   [channel的遍历方式](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_398)
    -   [只读/只写channel](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_433)
    -   [channel最佳案例](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#channel_478)
    -   [select语句](https://blog.csdn.net/chenlong_cxy/article/details/137794384?spm=1001.2014.3001.5506#select_569)

## 协程（goroutine）

### 基本介绍

> 基本介绍

进程、线程与协程：

-   进程（Process）是计算机中正在运行的程序的实例，是操作系统进行资源分配和调度的基本单位。每个进程都有自己独立的地址空间、代码、数据和文件资源。进程之间相互独立，通过进程间通信机制进行数据交换和协作。进程的创建、销毁以及切换都由操作系统自动完成，开销较大。
-   线程（Thread）是操作系统调度的最小执行单元，是进程内的一个执行路径。线程与进程共享同一地址空间和大部分资源，包括代码段、数据段和打开的文件等。线程之间通常借助互斥锁、条件变量以及信号量等进行数据交换。线程的创建、销毁以及切换的开销较小，但需要注意线程间的同步和共享资源的管理。
-   协程（Coroutine）协程是一种轻量级的并发执行单元，通常由编程语言本身的运行时系统进行调度和管理。协程通常在一个线程内执行，共享相同的地址空间和资源。协程间通常通过通道（Channel）实现数据交换和协作。协程的创建、销毁以及切换都由运行时系统自动完成，开销非常小，可以创建成千上万个协程而不会导致系统负载过高。

并发与并行：

-   并发（Concurrency）指的是在单个处理器上以时间片轮转的方式交替执行多个任务，使得在一段时间内，这多个任务都得以推进，但实际在一个时间点只有一个任务在执行。
-   并行（Parallelism）指的是多个任务同时在不同的处理器上执行，使得这多个任务同时得以推进，并且在一个时间点来看，也是多个任务在同时执行。

在Go中，通过在函数或方法的调用前加上go关键字即可创建一个go协程，并让其运行对应的函数或方法。如下：

```go
package main

import (
	"fmt"
	"time"
)

func Print() bool {
	for i := 0; i < 10; i++ {
		fmt.Printf("Print: hello goroutine...%d\n", i+1)
		time.Sleep(time.Second)
	}
	return true
}

func main() {
	go Print() // 创建go协程

	for i := 0; i < 5; i++ {
		fmt.Printf("main: hello goroutine...%d\n", i+1)
		time.Sleep(time.Second)
	}
}

```

在上述代码中，主协程创建了一个新协程用于执行Print函数，主协程进行5次打印后退出，新协程进行10次打印后退出。运行结果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/d726e4555b6d49cd9ee7f6c8f7085f03.png)

**说明一下：**

-   在Go中，当程序启动时会自动创建一个主协程来执行main函数，该协程与其他新创建的协程没有本质的区别，但主协程执行完毕后整个程序会退出，即使其他协程还未执行完毕，也会跟着退出。
-   如果一个协程在执行过程中触发了panic异常，但没有对其进行捕获，那么会导致整个程序崩溃，因此在协程中也需要通过recover函数对panic进行捕获。

### GMP模型

> 常规的协程（Coroutine）

线程是在内核态视角下的最小执行单元，而协程是在线程的基础上，在用户态视角下进行二次开发得到的更小的执行单元。常规的协程（Coroutine）通常是与一个线程强绑定的，而一个线程可以绑定多个协程。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/f18077786cea483eb9a4c4b288a8c318.png)

**说明一下：**

-   由于常规的协程是与一个线程强绑定的，因此绑定于同一线程的多个协程只能做到并发，无法做到并行。
-   当一个协程因为某些原因陷入阻塞，那么这个阻塞会直接上升到对应的线程，最终导致整个协程组陷入阻塞。

> Go中的协程（Goroutine）

Go语言中的协程（Goroutine）与常规的协程（Coroutine）的实现方式有所不同，Go中的协程不是与一个线程强绑定的，而是由Go调度器动态的将协程绑定到可用的线程上执行。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/2b1fa1d781c24f41b4fdd972610382c7.png)

**说明一下：**

-   由于Go协程与线程之间的绑定是动态的，因此各个协程之间既能做到并发，也能做到并行。
-   当一个Go协程因为某些原因陷入阻塞，那么Go调度器会将该协程与其绑定的线程进行解绑，将线程的资源释放出来，使得线程可以与其他可调度的协程进行绑定。

> GMP模型

GMP（Goroutine-Machine-Processor）模型是Go运行时系统中用于实现并发执行的模型，负责管理和调度协程的执行。G、M和P的含义分别如下：

-   G（Goroutine）：代表Go中的协程，每个G都有自己的运行栈、状态以及执行的任务函数。
-   M（Machine）：代表Go中的线程，M不直接执行G，而是先和P绑定，由P来指定M所需执行的G。
-   P（Processor）：代表Go中的调度器，P实现G和M之间的动态有机结合。对于G而言，P就是其CPU，G只有被P调度才得以执行；对于M而言，P是其执行代理，为其指定可执行的G。

GMP模型示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/64aed09466884312b666eb85eccb0a29.png)

**上图说明：**

-   全局有多个M和多个P，但M和P的数量不一定是相同的。每个M在调度G之前，需要先和P进行绑定（不是强绑定），每个M调度的G由其对应的P指定。M无需记录所调度的G的状态信息，因此G在全生命周期中可以实现跨M执行。
-   在GMP模型中有三种队列来存放G，分别是全局队列、P的本地队列和wait队列（用于存放io阻塞就绪态的G，图中未展示）。
-   每个P都有一个对应本地队列，访问本地队列时可以接近无锁化。当P为M获取可调度的G时，会优先从自己的本地队列中进行获取，其次从全局队列中获取，最后从wait队列中获取。
-   如果一个G在调度过程中新创建了一个G，那么这个新G会优先投递到当前P的本地队列中，如果本地队列已满则投递到全局队列中。

调度器P获取可调度的G的流程如下：

1.  优先尝试从当前P的本地队列获取可调度的G。
2.  尝试从全局队列获取可调度的G。
3.  尝试从wait队列获取io阻塞就绪的G。
4.  尝试从其他P的本地队列窃取一半的G补充到当前P的本地队列，防止不同P的闲忙差异过大（work-stealing机制）。

**说明一下：**

-   由于存在work-stealing机制，因此P的本地队列的访问也不是完全无锁的，只能说接近无锁化。
-   上述说到的只是获取可调度的G的主要流程，实际实现时还有更多的细节。比如P每进行61次调度后，会先尝试从全局队列中获取一个G进行调度，避免造成全局队列中的G的饥饿问题。

> GOMAXPROCS

在GMP模型中，G只有被P调度才得以执行，因此P的数量决定了G的最大并行数量。通过runtime包中的GOMAXPROCS函数可以获取和设置P的数量。如下：

```go
package main

import (
	"fmt"
	"runtime"
)

func main() {
	cpuNum := runtime.NumCPU()          // 获取本地机器的逻辑CPU数
	fmt.Printf("cpuNum = %d\n", cpuNum) // cpuNum = 6

	runtime.GOMAXPROCS(4)         // 设置可同时执行的最大CPU数
	num := runtime.GOMAXPROCS(0)  // 获取可同时执行的最大CPU数
	fmt.Printf("num = %d\n", num) // num = 4
}
```

**说明一下：**

-   runtime包中的NumCPU函数，用于获取本地机器的逻辑CPU数。
-   runtime包中的GOMAXPROCS函数，用于设置可同时执行的最大CPU数，并返回先前的设置。如果设置的值小于1，则不会更改当前的值，设置的值超过CPU核数无意义。
-   从Go1.5开始，GOMAXPROCS默认设置为CPU的核数，并且可以根据需要自动调整并发执行的并行度，无需再手动设置。

> 协程的生命周期

Go中协程的生命周期大致由如下几种状态组成：

-   \_Gidle：表示该协程刚刚创建，但还未进行初始化。
-   \_Gdead：表示该协程已经完成初始化，但还未被使用。
-   \_Grunnable：表示该协程已经被放入运行队列，但还未被调度。
-   \_Grunning：表示该协程正在被调度。
-   \_Gsyscall：表示该协程正在执行系统调用。
-   \_Gwaiting：表示该协程处于挂起状态，需要等待被唤醒。
-   \_Gdead：表示该协程刚刚执行完毕。

状态转换如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/3bfd5225738a444688be76c751acfac9.png)

**说明一下：**

-   当协程在调度过程中执行到系统调用代码时，其状态就会由\_Grunning切换为\_Gsyscall，并在系统调用结束后根据实际情况恢复为\_Grunning或\_Grunnable状态。
-   协程在调度过程中，可能因为某些原因而陷入阻塞，比如等待锁资源就绪或等待channel条件就绪等，这是协程的状态会由\_Grunning切换为\_Gwaiting，并在协程被唤醒后恢复为\_Grunnable状态。
-   除了上述常见的协程状态外，协程还有一些其他的状态，比如\_Gcopystack表示该协程正处于栈扩容流程中（Go协程的栈空间大小可动态扩缩），\_Greempted表示协程被抢占后的状态。

> 协程的调度流程

GMP模型中存在三种类型的协程：

-   普通的g：用户通过go关键字创建的协程，也就是GMP模型中需要被调度的G。
-   g0：特殊的调度协程，每个M都有一个g0，其主要负责对普通的g进行运行调度。
-   monitor g：全局监控协程，monitor g会越过P直接与一个M进行绑定，不断轮询对所有P的执行状况进行监控，如果发现满足抢占调度的条件，则会从第三方的角度出手干预，主动发起抢占调度。

在创建M时，Go运行时系统会为每个M初始化一个g0，g0的调度流程如下：

1.  找到一个可被调度执行的G。
2.  将这个G的状态切换为\_Grunning，并通过调用gogo函数将执行权交给G。
3.  执行G的代码逻辑，直到某些条件达成使得调度结束。
4.  G调度结束后，通过调用mcall函数将执行权交还给g0，并更新G的状态。

示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/ec558dede93241fcbc187380f5928ac4.png)

> 调度类型

GMP模型中的调度类型大致可分为如下四类：

-   主动调度：用户通过调用runtime包中的Gosched函数，可以让当前G主动让出执行权，并将其投递到全局队列中等待下一次调度。
-   被动调度：G在调度过程中，因为某些原因而陷入阻塞而导致调度终止，比如等待锁资源就绪或等待channel条件就绪等。
-   正常调度：G的代码逻辑被正常执行完毕，调度终止。
-   抢占调度：在G执行系统调用的情况下，如果满足了抢占调度的条件，那么monitor g会强行将当前的P和M进行解绑，让解绑后的P重新寻找一个空闲的M进行绑定，进而可以继续调度其他的G，而解绑后M则继续执行系统调用。

触发前三种调度类型中的任意一种，都会导致当前G的调度终止，此时M的执行权将由普通的g交还给g0。示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/c7e325862d954f499061ade510d8bbc0.png)

**上图说明：**

-   g0在调度普通的g时，会先通过findRunnable函数找到一个可被调度的G，然后通过execute函数更新对应G和P的状态信息，最后通过gogo函数将执行权交给G，进行G的调度。
-   G在调度过程中，如果因为主动调度、被动调度或正常调度导致调度终止，那么会先调用mcall函数将执行权交还给g0，然后通过调用对应的函数更新G的状态信息，并完成G和M解绑等操作，然后开启新一轮的调度。
-   gosched\_m函数对应的是主动调度，该函数会先将G的状态由\_Grunning切换为\_Grunnable，然后将G和M解绑并将其投递到全局队列中，最后开启新一轮的调度。
-   park\_m函数对应的是被动调度，该函数会先将G的状态由\_Grunning切换为\_Gwaiting，然后将G和M解绑，最后开启新一轮的调度。
-   goexit0函数对应的是正常调度，该函数会先将G的状态由\_Grunning切换为\_Gdead，然后将G和M解绑，最后开启新一轮的调度。

**关于被动调度：**

-   当因被动调度陷入阻塞的G对应的条件就绪时，会由导致条件就绪的G执行goready函数将其唤醒，唤醒时会先将G的状态由\_Gwaiting切换为\_Grunnable，然后将其添加到唤醒者的P的本地队列中。
-   比如某个G在申请锁时由于锁资源不就绪而陷入阻塞，此时这个G会被放在锁对应的资源等待队列中，当另一个持有锁的G在被调度的过程中执行释放锁操作时，就会执行goready函数唤醒该锁对应的资源等待队列中的G，并将其添加到自己的P的本地队列中。
-   在调度唤醒者时M的执行权在普通的g手中，而被唤醒者的状态切换操作以及G的投递操作需要由g0执行，因此在goready函数中会先将执行权交还给g0，并在执行唤醒操作后再重新获得执行权，这里的执行权交接是通过systemstack函数完成的。
-   goready函数在将唤醒的G添加到唤醒者的P的本地队列中时，如果P的本地队列已满，则会将唤醒的G以及P的本地队列中一半的G放回到全局队列中，帮助当前的P缓解执行压力。

**关于抢占调度：**

-   在G需要执行系统调用之前，会先调用reentersyscall函数保存当前G的执行环境，并将G和P的状态更新为对应的系统调用状态，最后解除P和当前M之间的绑定，因为M即将进入系统调用而导致短暂不可用。与M解除绑定关系的P会被添加到当前M的oldp容器中，后续M执行完系统调用后会优先寻找该P重新建立绑定关系。
-   在G执行系统调用期间，如果P的本地队列不为空，或者当前没有空闲的M和P，或者G执行系统调用的时间超过10ms，则monitor g会将当前M的oldp容器中的P的状态置为空闲，并让其与其他空闲的M（也可能新创建一个M）进行绑定，进而可以继续调度其他的G，而当前的M仍然继续执行系统调用。
-   当M执行完系统调用后，会通过exitsyscall函数尝试寻找P进行绑定。如果此时M的oldp容器中的P仍然可用，则重新与该P建立绑定关系，并将G的状态重新置为\_Grunning，继续执行后续的代码逻辑。如果原先的P已经不可用，则将G的状态置为\_Grunnable，并解除G和M的绑定关系，尝试从全局P队列中寻找一个可用的P进行绑定，如果找到了则在绑定对应的P后继续调度该G，否则将该G投递到全局队列，并让当前的M陷入沉睡，直到被唤醒后再继续发起调度。

### 协程间共享变量

> 协程间共享变量

-   在协程之间共享变量是常见的需求，以便协程之间能够进行数据交换和协同工作。
-   为了保证共享资源的并发安全，通常需要引入互斥锁对共享资源进行保护。

例如，下面程序中启动了4个协程进行抢票，在抢票过程中需要并发访问全局变量tickets，代码中通过加锁的方式保证了tickets变量的并发安全。如下：

```go
package main

import (
	"fmt"
	"sync"
	"time"
)

var (
	tickets = 1000     // 共享资源
	mtx     sync.Mutex // 互斥锁
)

func ByTicket(id int) {
	for {
		mtx.Lock() // 加锁
		if tickets <= 0 {
			mtx.Unlock() // 解锁
			break
		}
		time.Sleep(time.Microsecond) // 模拟抢票过程的耗时
		tickets--
		fmt.Printf("goroutine %d get a ticket, tickets = %d\n", id, tickets)
		mtx.Unlock() // 解锁
	}
}

func main() {
	// 启动4个协程进行抢票
	for i := 0; i < 4; i++ {
		go ByTicket(i)
	}
	for {
		if tickets <= 0 {
			break
		}
	}
	fmt.Printf("tickets sold out...tickets = %d\n", tickets)
}
```

**说明一下：**

-   Mutex是sync包中的互斥锁类型，用于保护共享资源的并发访问，该类型提供了Lock和Unlock两个方法，分别用于加锁和解锁。

## 通道（channel）

### 基本介绍

> 基本介绍

-   通道（channel）是Go中用于协程间通信和数据交换的机制，其提供了一种安全、同步和高效的方式来传递数据，以实现协程之间的通信和协同工作。
-   channel本质是一个队列，遵守先进先出（FIFO）的原则。channel本身是线程安全的，多协程可以通过channel直接发送和接收数据，显式的加锁解锁操作。

channel的示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/9d68782fc68b4681ae7fffd6cd60e8b4.png)

### channel的定义方式

> channel的定义方式

在定义channel时，通过make创建指定类型以及容量的channel。如下：

```go
package main

import (
	"fmt"
	"unsafe"
)

func main() {
	// make channel
	var intChan = make(chan int, 10)
	fmt.Printf("intChan type = %T\n", intChan)                // intChan type = chan int
	fmt.Printf("intChan len = %d\n", len(intChan))            // intChan len = 0
	fmt.Printf("intChan cap = %d\n", cap(intChan))            // intChan cap = 10
	fmt.Printf("intChan size = %d\n", unsafe.Sizeof(intChan)) // intChan size = 8
}

```

**说明一下：**

-   channel是引用类型，其定义后需要先通过make函数分配内存空间，然后才能使用。在使用make函数为channel分配内存空间时，其第一个参数表示channel的类型，第二个参数表示channel的容量，第二个参数若省略则默认为0。
-   通过len函数可以获取channel中元素的数量，通过cap函数可以获取channel的容量。channel中仅包含一个指向底层队列的指针，属于引用类型，因此channel类型变量的大小为8字节。
-   channel中只能存放对应类型的数据，如果想让channel存放任意类型的数据，可以指定channel中存放的元素类型为interface{}。

### channel的读写

> channel的读写

channel的读写：

-   通过`channel <- data`的方式向channel中写入数据，在写入数据时，如果channel已满，则写操作会被阻塞，直到channel中有数据被读走，再执行写操作。
-   通过`data := <-channel`的方式从channel中读取数据，在读取数据时，如果channel为空，则读操作会被阻塞，直到有数据写入channel中，再执行读操作。

例如，下面程序中定义了一个容量为5的channel，并启动了一个协程不断向该channel中写入数据，而在主协程中每隔1秒从该channel中读取一次数据。如下：

```go
package main

import (
	"fmt"
	"time"
)

func WriteNum(intChan chan int) {
	num := 0
	for {
		intChan <- num // 向channel中写入数据
		fmt.Printf("write a num: %d\n", num)
		num++
	}
}

func ReadNum(intChan chan int) {
	for {
		time.Sleep(time.Second)
		num := <-intChan // 从channel中读取数据
		fmt.Printf("read a num: %d\n", num)
	}
}

func main() {
	intChan := make(chan int, 5)

	go WriteNum(intChan)

	ReadNum(intChan)
}

```

在上述代码中，由于向channel中写入数据的过程中没有进行任何休眠操作，因此程序运行后channel立马被写满了，此时对channel的写操作将会被阻塞，直到channel中的数据被主协程读走，才能再次执行写操作，因此后续对channel的写操作也被同步为每秒一次。程序运行结果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/9ac9cf736c0847269a231085586e149a.png)

**说明一下：**

-   将channel的容量指定为0，意味着channel中不能存储任何数据，此时该channel将成为一个无缓冲通道。对无缓冲通道的写操作将会被阻塞，直到有另一个协程准备对channel进行读操作，反之亦然，因此无缓冲通道是一种强制同步的机制。

### channel的关闭

> channel的关闭

在Go中，通过内建函数close可以关闭指定的channel，channel关闭后不能再对其进行写操作，否则会触发panic异常，但仍可以从该channel中读取数据。如下：

```go
package main

import "fmt"

func main() {
	charChan := make(chan int, 10)
	for i := 0; i < 10; i++ {
		charChan <- 'a' + i
	}
	close(charChan) // 关闭channel

	for {
		ch, ok := <-charChan
		if !ok {
			break
		}
		fmt.Printf("read a char: %c\n", ch)
	}
}

```

运行程序后可以看到，channel虽然被关闭了，但仍然可以读取channel中的数据。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/6c7e4d732f924bebb1f2b5a65418e2be.png)

**说明一下：**

-   通过`<-channel`的方式读取channel中的数据将会得到两个值，第一个值是从channel中读取到的数据，第二个值表示本次对channel进行的读操作是否成功，如果channel已关闭并且channel中没有数据可读，那么第二个值将会返回false，否则为true。

### channel的遍历方式

> channel的遍历方式

在Go中，可以通过for range循环的方式对channel中的元素进行遍历，其特点如下：

-   for range循环的每次迭代会从channel中读取一个数据，并将该值赋给指定的变量。
-   如果channel中没有数据可读取，for range会阻塞等待，直到有数据可读或channel关闭。
-   channel被关闭后，for range可以继续从channel中读取数据，当所有数据都被读取后会自动结束迭代。

使用案例如下：

```go
package main

import "fmt"

func main() {
	charChan := make(chan int, 10)
	for i := 0; i < 10; i++ {
		charChan <- 'a' + i
	}
	close(charChan) // 关闭channel

	for value := range charChan {
		fmt.Printf("read a char: %c\n", value)
	}
}
```

**说明一下：**

-   在对channel进行读操作时，要确保有协程会对channel进行对应的写操作，否则会造成死锁（deadlock）。
-   如果去掉上述代码中关闭channel的操作，那么for range循环在读取完channel中的数据后不会自动结束迭代，而会继续进行读操作，但此时没有任何协程会再对该channel进行写操作，因此会造成死锁（deadlock）。

### 只读/只写channel

> 只读/只写channel

在Go中，通过`<-chan type`和`chan<- type`的方式，可以将channel声明为只读或只写。如下：

```go
package main

import (
	"fmt"
	"time"
)

func WriteNum(intChan chan<- int) { // 只写channel
	num := 0
	for {
		intChan <- num // 向channel中写入数据
		fmt.Printf("write a num: %d\n", num)
		num++
	}
}

func ReadNum(intChan <-chan int) { // 只读channel
	for {
		time.Sleep(time.Second)
		num := <-intChan // 从channel中读取数据
		fmt.Printf("read a num: %d\n", num)
	}
}

func main() {
	intChan := make(chan int, 5)

	go WriteNum(intChan)

	ReadNum(intChan)
}
```

**说明一下：**

-   对只读的channel进行写操作，或对只写的channel进行读操作都会产生报错。
-   由于WriteNum函数中只会对intChan进行写操作，而ReadNum函数中只会对intChan进行读操作，这时为了避免误操作，可以分别将WriteNum和ReadNum函数的intChan参数声明为只写和只读的channel。

### channel最佳案例

> 题目要求：统计1-300000中有多少个素数

为了快速统计出素数的个数，使用多个Go协程并发进行素数判断，具体的解决思路如下：

-   启动一个生产者协程，负责将1-300000的数字写入到intChan中，作为数据源。
-   启动多个消费者协程，负责从intChan中读取数据进行素数判断，并将素数写入到primeChan中。
-   主协程负责不断读取primeChan中的数据，统计素数的个数。

为了让主协程能够判断primeChan中的素数是否已经读取完毕，需要借助一个exitChan：

-   生产者协程在生产完数据后关闭intChan，使得各个消费者协程能够判断intChan中的数据是否消费完毕，并在数据消费完毕后写入一个结束标志到exitChan中。
-   启动一个匿名协程，负责从exitChan中读取结束标志，当读取到的结束标志个数等于消费者的个数时，表明所有消费者协程已经退出，这时关闭primeChan和exitChan。
-   当primeChan被关闭，并且primeChan中的数据已经读取完时，则说明所有素数已经统计完毕。

示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/244b0c39aa5a46fca20e4d18579361ff.png)

代码如下：

```go
package main

import "fmt"

func Producer(numChan chan<- int) {
	for num := 1; num <= 300000; num++ {
		numChan <- num
	}
	close(numChan)
}

func IsPrime(num int) bool {
	for i := 2; i <= num-1; i++ {
		if num%i == 0 {
			return false
		}
	}
	return true
}

func Consumer(numChan <-chan int, primeChan chan<- int, exitChan chan<- bool) {
	for {
		num, ok := <-numChan
		if !ok {
			break
		}
		if IsPrime(num) {
			primeChan <- num
		}
	}
	exitChan <- true
}

func main() {
	numChan := make(chan int, 300000)
	primeChan := make(chan int, 300000)
	exitChan := make(chan bool, 6)

	// 生产者协程
	go Producer(numChan)

	// 消费者协程
	for i := 0; i < 6; i++ {
		go Consumer(numChan, primeChan, exitChan)
	}

	// 匿名协程
	go func() {
		for i := 0; i < 6; i++ {
			<-exitChan
		}
		close(primeChan)
		close(exitChan)
	}()

	// 主协程
	count := 0
	for {
		_, ok := <-primeChan
		if !ok {
			break
		}
		count++
	}
	fmt.Printf("prime count = %d\n", count) // prime count = 25998
}

```

### select语句

> select语句

在Go中，select语句用于实现非阻塞的通信。其特点如下：

-   select语句可以同时监听多个channel的操作，它会选择一个已经就绪的操作，并执行相应的分支代码。
-   如果有多个操作就绪，select语句会随机选择其中一个操作执行，如果没有操作就绪，则会执行default分支。
-   需要注意的是，如果没有操作就绪，并且select语句中没有default分支，则select语句会阻塞，直到至少有一个操作就绪。

使用案例如下：

```go
package main

import "fmt"

func main() {
	intChan := make(chan int, 10)
	stringChan := make(chan string, 10)

	for i := 0; i < 10; i++ {
		intChan <- i
		stringChan <- fmt.Sprintf("hello select%d", i)
	}

label:
	for {
		select {
		case num := <-intChan:
			fmt.Printf("read intChan: %d\n", num)
		case str := <-stringChan:
			fmt.Printf("read stringChan: %s\n", str)
		default:
			fmt.Printf("no data now...\n")
			break label
		}
	}
}

```

运行代码后可以看到，当intChan和stringChan中都有数据时，select语句会随机对一个channel进行读操作，并在两个channel中的数据都被读取完后，通过执行default分支中的break语句跳出for循环。运行结果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/31739dacde664c79b2f741dfc94830ed.png)