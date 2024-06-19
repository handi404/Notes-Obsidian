#### 文章目录

-   [网络编程介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_1)
-   [TCP网络编程](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#TCP_9)
-   -   [服务器监听](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_10)
    -   [客户端连接服务器](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_121)
    -   [服务端获取连接](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_234)
    -   [向连接中写入数据](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_297)
    -   [从连接中读取数据](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_317)
    -   [关闭连接/监听器](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_336)
-   [简易的TCP回声服务器](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#TCP_351)
-   -   [效果展示](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_353)
    -   [服务端处理逻辑](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_366)
    -   [客户端处理逻辑](https://blog.csdn.net/chenlong_cxy/article/details/137794458?spm=1001.2014.3001.5506#_442)

## 网络编程介绍

> 网络编程介绍

-   网络编程是指通过计算机网络实现程序间通信的一种编程技术，涉及到在不同计算机之间建立连接、传输数据和协议解析等操作。
-   套接字（Socket）编程是网络编程的一种实现方式，其提供了一种机制，使得应用程序能够通过网络进行数据传输和通信。
-   Go中的net包是标准库中提供的网络编程包，是基于套接字编程的一种实现方式，提供了对TCP、UDP、IP、ICMP、Unix域套接字等常见网络协议的支持，通过net包可以完成创建套接字、建立连接、发送和接收数据等操作，实现网络通信。

## TCP网络编程

### 服务器监听

> 服务器监听

在Go的net包中，Listen函数用于创建并返回一个网络监听器（Listener），以监听指定网络地址和端口上的连接请求。该函数的函数原型如下：

```go
func Listen(network, address string) (Listener, error)
```

**参数说明：**

-   network：用于指定网络类型，其值必须是"tcp", “tcp4”, “tcp6”, “unix"或"unixpacket”。
-   address：用于指定需要被监听的IP地址和端口号，格式为"host:port"。

**返回值说明：**

-   第一个返回值：表示创建的网络监听器。
-   第二个返回值：如果创建网络监听器过程中出错，将返回非nil的错误值。

通过Listen函数创建得到的网络监听器是Listener类型的，该类型是一个接口类型，其定义如下：

```go
type Listener interface {
	// Accept waits for and returns the next connection to the listener.
	Accept() (Conn, error)

	// Close closes the listener.
	// Any blocked Accept operations will be unblocked and return errors.
	Close() error

	// Addr returns the listener's network address.
	Addr() Addr
}

```

Listener接口中各方法说明：

-   Accept方法：从底层获取下一个已经建立好的连接给监听器。
-   Close方法：关闭监听器。
-   Addr方法：返回监听器对应的网络地址（由IP地址和端口号组成）。

当使用Listen函数创建TCP类型的监听器时，其返回的监听器底层具体的类型是TCPListener，其定义如下：

```go
type TCPListener struct {
	fd *netFD
	lc ListenConfig
}
```

TCPListener结构体各字段说明：

-   fd：对底层网络文件描述符的封装，提供了对网络连接的读写和控制操作。
-   lc：用于配置监听器创建的行为，比如设置监听地址、控制网络参数等。

TCPListener结构体中的fd字段是netFD类型的，其定义如下：

```go
type netFD struct {
	pfd poll.FD

	// immutable until Close
	family      int
	sotype      int
	isConnected bool // handshake completed or use of association with peer
	net         string
	laddr       Addr
	raddr       Addr
}

```

netFD结构体各字段说明：

-   pfd：用于与底层的操作系统文件描述符进行交互。
-   family：表示套接字的协议家族，比如IPv4或IPv6。
-   sotype：表示套接字的类型，比如TCP或UDP
-   isConnected：表示连接是否已完成握手或与对方建立关联。
-   net：表示网络协议，比如"tcp"或"udp"。
-   laddr：表示本地网络连接的地址。
-   raddr：表示远程网络连接的地址。

> 服务器监听示例

服务器监听示例如下：

```go
package main

import (
	"fmt"
	"net"
)

func main() {
	// 服务器监听
	listen, err := net.Listen("tcp", "0.0.0.0:8081")
	if err != nil {
		fmt.Printf("listen error, err = %v\n", err)
		return
	}
	defer listen.Close()
	fmt.Println("listen success...")
}
```

**说明一下：**

-   服务器在创建监听套接字时，将其绑定到`0.0.0.0`（通常表示为INADDR\_ANY）地址，这样服务器就可以同时监听和接受来自不同网络接口的连接请求，而不需要为每个接口分别创建监听套接字。
-   为了避免网络文件描述符资源泄露，在创建监听器后及时利用defer机制关闭监听器。监听器被关闭后会停止监听新的连接请求，并且任何被阻塞的Accept操作都会被解除阻塞并返回错误。

### 客户端连接服务器

> 客户端连接服务器

在Go的net包中，Dial函数用于客户端应用程序与远程服务器建立连接。该函数的函数原型如下：

```go
func Dial(network, address string) (Conn, error)
```

**参数说明：**

-   network：用于指定网络协议，比如"tcp", "udp"等。
-   address：用于指定连接的目标地址。

**返回值说明：**

-   第一个返回值：表示建立的网络连接。
-   第二个返回值：如果建立网络连接过程中出错，将返回非nil的错误值。

通过Dial函数建立得到的连接Conn类型的，该类型是一个接口类型，其定义如下：

```go
type Conn interface {
	// Read reads data from the connection.
	Read(b []byte) (n int, err error)

	// Write writes data to the connection.
	Write(b []byte) (n int, err error)

	// Close closes the connection.
	// Any blocked Read or Write operations will be unblocked and return errors.
	Close() error

	// LocalAddr returns the local network address, if known.
	LocalAddr() Addr

	// RemoteAddr returns the remote network address, if known.
	RemoteAddr() Addr

	// SetDeadline sets the read and write deadlines associated
	// with the connection. It is equivalent to calling both
	// SetReadDeadline and SetWriteDeadline.
	SetDeadline(t time.Time) error

	// SetReadDeadline sets the deadline for future Read calls
	// and any currently-blocked Read call.
	SetReadDeadline(t time.Time) error

	// SetWriteDeadline sets the deadline for future Write calls
	// and any currently-blocked Write call.
	SetWriteDeadline(t time.Time) error
}

```

Conn接口中各方法说明：

-   Read方法：从连接中读取数据。
-   Write方法：向连接中写入数据。
-   Close方法：关闭连接。
-   LocalAddr方法：返回连接对应的本地网络地址（由IP地址和端口号组成）。
-   RemoteAddr方法：返回连接对应的远程网络地址（由IP地址和端口号组成）。
-   SetDeadline方法：设置连接读取和写入的截止时间。
-   SetReadDeadline方法：设置连接读取的截止时间。
-   SetWriteDeadline方法：设置连接写入的截止时间。

当使用Dial函数与TCP服务器建立连接时，其返回的网络连接底层具体的类型是TCPConn，其定义如下：

```go
type TCPConn struct { 
	conn
}
```

TCPConn结构体中仅嵌套了一个conn类型的匿名结构体，其定义如下：

```go
type conn struct { 
	fd *netFD 
}
```

可以看到，conn结构体中的fd字段与TCPListener结构体中的fd字段的类型相同，它们都是对底层网络文件描述符的封装，提供了对网络连接的读写和控制操作。

> 客户端连接服务器示例

客户端连接服务器示例如下：

```go
package main

import (
	"fmt"
	"net"
)

func main() {
	// 客户端连接服务器
	conn, err := net.Dial("tcp", "127.0.0.1:8081")
	if err != nil {
		fmt.Printf("connect server error, err = %v\n", err)
		return
	}
	defer conn.Close()
	fmt.Println("connect server success...")
}

```

**说明一下：**

-   当客户端连接TCP服务器时，服务器必须处于监听状态。
-   为了避免网络文件描述符资源泄露，客户端在与服务器建立连接后及时利用defer机制关闭连接。

### 服务端获取连接

> 服务端获取连接

在创建TCP网络监听器后，调用Listener接口的Accept方法，本质调用的是TCPListener的Accept方法，该方法用于从底层获取下一个已经建立好的连接给监听器，如果底层没有建立好的连接则会进行阻塞等待。该方法的原型如下：

```go
func (l *TCPListener) Accept() (Conn, error)
```

**返回值说明：**

-   第一个返回值：表示获取到的与客户端建立好的连接。
-   第二个返回值：如果在获取连接过程中出错，将返回非nil的错误值。

> 服务端获取连接示例

服务端获取连接示例如下：

```go
package main

import (
	"fmt"
	"net"
)

func process(conn net.Conn) {
	defer conn.Close()

	fmt.Printf("handle a link %v...\n", conn.RemoteAddr())
}

func main() {
	// 服务器监听
	listen, err := net.Listen("tcp", "0.0.0.0:8081")
	if err != nil {
		fmt.Printf("listen error, err = %v\n", err)
		return
	}
	defer listen.Close()
	fmt.Println("listen success...")

	for {
		fmt.Println("waiting client connect...")
		// 服务端获取连接
		conn, err := listen.Accept()
		if err != nil {
			fmt.Printf("accept error, err = %v\n", err)
			continue
		}
		fmt.Printf("get a link from %v...\n", conn.RemoteAddr())
		// 开启新协程为客户端提供服务
		go process(conn)
	}
}

```

**说明一下：**

-   网络监听器的任务就是不断调用Accept方法，从底层获取已经建立好的连接并为其提供服务，通常在获取到一个连接后会开启一个新协程为其提供服务，而主协程则继续调用Accept方法获取新的连接。
-   为了让新协程能够获取需要被处理的连接，需要将对应的连接通过参数传递的方式，传递给协程对应的处理函数。此外，为了避免网络文件描述符资源泄露，需要在处理函数中利用defer机制关闭连接，保证连接处理完毕后能够及时关闭连接。

### 向连接中写入数据

> 向连接中写入数据

在创建TCP连接后，调用Conn接口的Write方法，本质调用的是TCPConn的Write方法，该方法用于向连接中写入数据。该方法的原型如下：

```go
func (c *TCPConn) Write(b []byte) (int, error)
```

**参数说明：**

-   b：表示要写入连接的数据。

**返回值说明：**

-   第一个返回值：表示实际写入的字节数。
-   第二个返回值：如果在写入数据过程中出错，将返回非nil的错误值。

### 从连接中读取数据

> 从连接中读取数据

在创建TCP连接后，调用Conn接口的Read方法，本质调用的是TCPConn的Read方法，该方法用于从连接中读取数据。该方法的原型如下：

```go
func (c *TCPConn) Read(b []byte) (int, error)
```

**参数说明：**

-   b：输出型参数，用于存储读取到的数据。

**返回值说明：**

-   第一个返回值：表示实际读取的字节数。
-   第二个返回值：如果在读取数据过程中出错，将返回非nil的错误值。

### 关闭连接/监听器

> 关闭连接/监听器

为了避免网络文件描述符泄露，TCP网络监听器和TCP连接在使用完毕后都需要及时将其关闭，对应调用的分别是TCPListener和TCPConn的Close方法，这两个方法的原型如下：

```go
func (l *TCPListener) Close() error 
func (c *TCPConn) Close() error
```

**返回值说明：**

-   如果在关闭监听器或关闭连接过程中出错，将返回非nil的错误值。

## 简易的TCP回声服务器

### 效果展示

> 效果展示

为了演示使用net包实现网络通信，下面实现了一个简易的TCP回声服务器，其功能如下：

-   服务端能够同时处理多个客户端的连接请求，在为每个客户端提供服务时，能够将各个客户端发来的数据显示在服务端，同时将客户端发来的数据再发回给客户端。
-   客户端在连接到服务器后，能够不断从控制台读取用户输入的数据发送给服务端，并将服务端发来的数据显示在客户端，用户在控制台输入exit能够退出客户端。

最终效果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1b677eb6287e42eca0cdb8eb465e16ba.gif#pic_center)

### 服务端处理逻辑

> 服务端处理逻辑

服务端处理逻辑如下：

-   主协程调用Listen函数完成服务器的监听后，通过监听器不断调用Accept方法从底层获取已经建立好的连接，并为每一个获取到的连接创建一个新协程为其提供服务，而主协程则继续获取新的连接。
-   每个新协程在为其对应的连接提供服务时，通过调用连接的Read方法不断读取客户端发来的数据，将数据其显示在服务端，同时通过调用连接的Write方法将客户端发来的数据再发回给客户端。
-   每个新协程在为其对应的连接提供服务的过程中，如果从连接中读取数据或向连接中写入数据时出错，或是客户端退出，则通过调用连接的Close方法将对应的连接关闭。

服务端代码如下：

```go
package main

import (
	"fmt"
	"io"
	"net"
)

func process(conn net.Conn) {
	defer conn.Close()

	data := make([]byte, 1024)
	for {
		// 1、读取客户端发来的数据
		n, err := conn.Read(data)
		if err != nil {
			if err == io.EOF {
				fmt.Printf("client %v quit\n", conn.RemoteAddr())
			} else {
				fmt.Printf("read client message error, err = %v\n", err)
			}
			return
		}
		fmt.Printf("client message[%v]: %v\n", conn.RemoteAddr(), string(data[:n]))

		// 2、发送数据给客户端
		len, err := conn.Write(data[:n])
		if err != nil || len != n {
			fmt.Printf("send back message error, err = %v\n", err)
			return
		}
	}
}

func main() {
	// 服务器监听
	listen, err := net.Listen("tcp", "0.0.0.0:8081")
	if err != nil {
		fmt.Printf("listen error, err = %v\n", err)
		return
	}
	defer listen.Close()
	fmt.Println("listen success...")

	for {
		fmt.Println("waiting client connect...")
		// 服务端获取连接
		conn, err := listen.Accept()
		if err != nil {
			fmt.Printf("accept error, err = %v\n", err)
			continue
		}
		fmt.Printf("get a link from %v...\n", conn.RemoteAddr())
		// 开启新协程为客户端提供服务
		go process(conn)
	}
}

```

**说明一下：**

-   当服务端从某个连接中读取数据时，如果该连接对应的客户端已经将连接关闭，那么服务端的读操作将会返回io.EOF错误。

### 客户端处理逻辑

> 客户端处理逻辑

客户端处理逻辑如下：

-   客户端在调用Dial函数与服务端建立连接后，不断读取用户的输入，通过调用连接Write方法将用户输入的数据发送给服务端，然后通过调用连接的Read方法读取服务端发来的数据并显示在客户端，如果用户输入exit则调用连接的Close方法将连接关闭。

客户端代码如下：

```go
package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
)

func main() {
	// 客户端连接服务器
	conn, err := net.Dial("tcp", "127.0.0.1:8081")
	if err != nil {
		fmt.Printf("connect server error, err = %v\n", err)
		return
	}
	defer conn.Close()
	fmt.Println("connect server success...")

	reader := bufio.NewReader(os.Stdin)
	data := make([]byte, 1024)
	for {
		// 1、读取用户输入
		str, err := reader.ReadString('\n')
		if err != nil {
			fmt.Printf("read input error, err = %v\n", err)
			continue
		}
		str = str[:len(str)-2] // 去掉\r\n
		if str == "exit" {
			fmt.Printf("exit success...")
			break
		}

		// 2、发送数据给服务端
		n, err := conn.Write([]byte(str))
		if err != nil || n != len(str) {
			fmt.Printf("send message error, err = %v\n", err)
			continue
		}
		fmt.Printf("send %d byte message to server...\n", n)

		// 3、读取服务端发来的数据
		n, err = conn.Read(data)
		if err != nil {
			fmt.Printf("read message error, err = %v\n", err)
			continue
		}
		fmt.Printf("server message: %v\n", string(data[:n]))
	}
}

```

**说明一下：**

-   Stdin、Stdout和Stderr是os包中的全局变量，分别表示标准输入流、标准输出流和标准错误流。
-   客户端在读取用户输入时，通过bufio包中的Reader，以带缓冲的方式每次从标准输入流中读取一行数据。
-   Windows系统中通常使用`\r\n`作为换行符，因此客户端在每次读取一行用户输入的数据后需要将末尾的两个字符去掉。