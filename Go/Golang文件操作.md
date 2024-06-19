#### 文章目录

-   [文件操作](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#_1)
-   -   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#_2)
    -   [普通的文件操作方式（os包）](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#os_13)
    -   [带缓冲的文件操作方式（bufio包）](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#bufio_400)
    -   [文件拷贝操作（io包）](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#io_621)
-   [命令行参数](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#_693)
-   -   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#_695)
    -   [解析命令行参数（flag包）](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#flag_724)
-   [JSON](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#JSON_786)
-   -   [基本介绍](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#_787)
    -   [JSON序列化](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#JSON_819)
    -   [JSON反序列化](https://blog.csdn.net/chenlong_cxy/article/details/137794299?spm=1001.2014.3001.5506#JSON_881)

## 文件操作

### 基本介绍

> 基本介绍

-   文件操作是指对计算机文件进行读取、写入、修改、删除和移动等操作的过程，它可以用于读取配置文件、存储日志、处理用户上传的文件等，Go中主要通过os和bufio包提供文件操作功能。
-   文件在程序中是以流的形式进行操作的，我们把数据在数据源（文件）和程序（内存）之间经历的路径叫做流。其中数据从数据源到程序的路径叫做输入流，数据从程序到数据源的路径叫做输出流。

文件流示意图如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7e9f8802e7fa4256845ee2074d9bab79.png)

### 普通的文件操作方式（os包）

> os包介绍

在os包中，File类型代表一个打开的文件，其封装了与文件相关的操作和属性。File结构体的定义如下：

```go
type File struct { 
	*file // os specific 
}
```

File结构体中以`*type`的方式嵌套了file类型的匿名结构体指针，实际文件的属性信息都存储在file结构体中。file结构体的定义如下：

```go
type file struct {
	pfd         poll.FD
	name        string
	dirinfo     atomic.Pointer[dirInfo] // nil unless directory being read
	nonblock    bool                    // whether we set nonblocking mode
	stdoutOrErr bool                    // whether this is stdout or stderr
	appendMode  bool                    // whether file is opened for appending
}
```

file结构体各字段说明：

-   pfd：用于与底层的操作系统文件描述符进行交互。
-   name：表示文件的名称（包括路径）。
-   dirinfo：用于在读取目录时缓存目录的信息（打开的文件是目录时被使用）。
-   nonblock：表示文件是否设置为非阻塞模式。
-   stdoutOrErr：表示文件是否是标准输出或标准错误。
-   appendMode：表示文件是否以追加模式打开。

每一个打开的文件都对应一个文件描述符，file结构体中的pfd是poll.FD类型的，实际文件对应的文件描述符就存储在poll.FD结构体的Sysfd字段中。FD结构体的定义如下：

```go
type FD struct {
	// Lock sysfd and serialize access to Read and Write methods.
	fdmu fdMutex

	// System file descriptor. Immutable until Close.
	Sysfd int

	// Platform dependent state of the file descriptor.
	SysFile

	// I/O poller.
	pd pollDesc

	// Semaphore signaled when file is closed.
	csema uint32

	// Non-zero if this file has been set to blocking mode.
	isBlocking uint32

	// Whether this is a streaming descriptor, as opposed to a
	// packet-based descriptor like a UDP socket. Immutable.
	IsStream bool

	// Whether a zero byte read indicates EOF. This is false for a
	// message based socket connection.
	ZeroReadIsEOF bool

	// Whether this is a file rather than a network socket.
	isFile bool
}
```

> 打开文件

在os包中，使用OpenFile函数打开文件，该函数的函数原型如下：

```go
func OpenFile(name string, flag int, perm FileMode) (file *File, err error)
```

参数说明：

-   name：表示需要打开的文件名称（包括路径）。
-   flag：表示打开文件的方式。
-   perm：表示新创建文件的权限，通常设置为0666（表示任何人都可读写，不可执行）。

返回值说明：

-   file：如果打开文件成功，将返回文件对应的File结构体。
-   err：如果打开文件过程中出错，将返回非nil的错误值。

打开文件的方式可以使用以下标注之一或它们的组合。如下：

| 参数选项 | 含义 |
| --- | --- |
| O\_RDONLY | 以只读方式打开文件 |
| O\_WRONLY | 以只写方式打开文件 |
| O\_RDWR | 以读写方式打开文件 |
| O\_APPEND | 以追加的方式打开文件 |
| O\_CREATE | 如果文件不存在，则创建文件 |
| O\_EXCL | 与O\_CREATE一起使用，确保创建新文件时不会覆盖现有文件 |
| O\_SYNC | 在每次写入操作后同步文件内容到磁盘 |
| O\_TRUNC | 如果文件已存在，将截断文件为零长度 |

打开文件示例如下：

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\NormalOperation\data.txt`
	// 打开文件
	file, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	fmt.Printf("open file success, fd = %v\n", file.Fd()) // open file success, fd = 420
}
```

**说明一下：**

-   通过File结构体的Fd方法，可以获取该文件对应的文件描述符。文件的文件描述符由底层操作系统分配，每次打开文件时分配的文件描述符可能不同。

> 关闭文件

在os包中，使用File结构体的Close方法关闭文件，该方法的原型如下：

```go
func (f *File) Close() error
```

返回值说明：

-   关闭文件成功返回nil，否则返回非nil的错误值。

关闭文件示例如下：

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\NormalOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
}

```

**说明一下：**

-   文件操作完毕后需要及时调用Close方法对文件进行关闭，避免造成文件描述符泄露。
-   通常利用defer机制对文件进行延迟关闭，在defer语句之后仍然可以操作文件，文件将会在函数执行完毕后自动关闭。

> 获取文件属性信息

在os包中，使用File结构体的Stat方法获取文件的属性信息，该方法的原型如下：

```go
func (f *File) Stat() (fi FileInfo, err error)
```

返回值说明：

-   fi：如果方法调用成功，将返回文件的属性信息。
-   err：如果方法调用过程中出错，将返回非nil的错误值。

Stat方法获取到的文件信息FileInfo是一个接口类型，该类型的定义如下：

```go
type FileInfo interface {
	Name() string       // base name of the file
	Size() int64        // length in bytes for regular files; system-dependent for others
	Mode() FileMode     // file mode bits
	ModTime() time.Time // modification time
	IsDir() bool        // abbreviation for Mode().IsDir()
	Sys() any           // underlying data source (can return nil)
}

```

FileInfo接口中各方法说明：

-   Name方法：返回文件的基本名称（不包含路径）。
-   Size方法：返回文件的大小。
-   Mode方法：返回文件的权限和模式位信息。
-   ModTime方法：返回文件最后一次修改时间。
-   IsDir方法：判断文件是否是一个目录。
-   Sys方法：返回底层数据源，通常是操作系统特定的文件信息（可能返回nil）。

获取文件属性信息示例如下：

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\NormalOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_RDONLY, 0666)
	if err != nil {
		if os.IsNotExist(err) {
			fmt.Println("warning: file not exists...")
		} else {
			fmt.Printf("open file error, err = %v\n", err)
		}
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
	// 3、获取文件属性信息
	fi, err := file.Stat()
	if err != nil {
		fmt.Printf("get file info error, err = %v\n", err)
		return
	}
	fmt.Printf("file name = %v\n", fi.Name())       // file name = data.txt
	fmt.Printf("file size = %v\n", fi.Size())       // file size = 240
	fmt.Printf("file mode = %v\n", fi.Mode())       // file mode = -rw-rw-rw-
	fmt.Printf("file modTime = %v\n", fi.ModTime()) // file modTime = 2024-05-09 17:27:33.1530703 +0800 CST
	fmt.Printf("file isDir = %v\n", fi.IsDir())     // file isDir = false
	fmt.Printf("file sys = %v\n", fi.Sys())         // file sys = &{32 {1262805282 31105508} {690243707 31105523} {521554895 31105523} 0 240}
}
```

**说明一下：**

-   os包中的IsNotExist函数返回一个bool值，表示传入的错误值是否表示文件或目录不存在。
-   os包中也提供了单独的Stat函数用于获取文件的属性信息，调用时传入文件名称（包括路径）即可。

> 写文件

在os包中，使用File结构体的Write方法将指定内容写入到文件中，该方法的原型如下：

```go
func (f *File) Write(b []byte) (n int, err error)
```

参数说明：

-   b：表示要写入文件的数据。

返回值说明：

-   n：表示成功写入文件的字节数。
-   err：如果写入过程中出错，将返回非nil的错误值。

写文件示例如下：

```go
package main

import (
	"fmt"
	"os"
	"strconv"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\NormalOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
	// 3、写文件
	var str string
	for i := 0; i < 10; i++ {
		str += "Hello File(os package)" + strconv.Itoa(i) + "\n"
	}
	count, err := file.Write([]byte(str))
	if err != nil {
		fmt.Printf("write file error, err = %v\n", err)
		return
	}
	fmt.Printf("write %d bytes data to data.txt\n", count) // write 240 bytes data to data.txt
}
```

运行程序后可以看到数据被成功写入文件。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/e8743fc3bf244b448880415853be0b34.png)

> 读文件

在os包中，使用File结构体的Read方法读取文件中的内容，该方法的原型如下：

```go
func (f *File) Read(b []byte) (n int, err error)
```

参数说明：

-   b：输出型参数，用于存储从文件中读取到的数据。

返回值说明：

-   n：表示成功读取到的字节数。
-   err：如果读取过程中出错，将返回非nil的错误值。

读文件示例如下：

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\NormalOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_RDONLY, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
	// 3、获取文件大小
	fileInfo, err := file.Stat()
	if err != nil {
		fmt.Printf("get file info error, err = %v\n", err)
		return
	}
	fileSize := fileInfo.Size()
	// 4、读文件
	buffer := make([]byte, fileSize)
	count, err := file.Read(buffer)
	if err != nil {
		fmt.Printf("read file error, err = %v\n", err)
		return
	}
	fmt.Printf("file size = %d, read count = %d\n", fileSize, count)
	fmt.Printf("file content:\n%s\n", string(buffer))
}
```

运行程序后可以看到文件中的数据被成功读取出来。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/def6ba63e9a549ee9838ce6864be389f.png)

> 其他函数和方法

在os包中，常用的打开文件获取File对象的函数如下：

| 函数名 | 功能 |
| --- | --- |
| OpenFile | 打开文件，可以指明打开文件的方式和新创建文件的权限 |
| Open | 以只读模式打开文件 |
| Create | 以0666权限创建并打开文件，如果文件已存在会将其截断 |

在os包中，常用的File结构体提供的文件操作方法如下：

| 方法名 | 功能 |
| --- | --- |
| Name | 获取文件名称（包括路径） |
| Stat | 获取文件属性信息 |
| Fd | 获取文件对应的文件描述符 |
| Read | 从当前位置开始读取文件数据 |
| ReadAt | 从指定位置开始读取文件数据 |
| Write | 从当前位置开始向文件写入数据 |
| WriteString | 从当前位置开始向文件写入一个字符串 |
| WriteAt | 从指定位置开始向文件写入数据 |
| Seek | 设置下一次文件的读写位置 |
| Close | 关闭文件 |

### 带缓冲的文件操作方式（bufio包）

> bufio包介绍

-   bufio包中的Reader和Writer类型提供了带缓冲的读写功能，可以搭配os包一起使用来实现文件的带缓冲读写。

Reader结构体的定义如下：

```go
type Reader struct {
	buf          []byte
	rd           io.Reader // reader provided by the client
	r, w         int       // buf read and write positions
	err          error
	lastByte     int // last byte read for UnreadByte; -1 means invalid
	lastRuneSize int // size of last rune read for UnreadRune; -1 means invalid
}
```

Reader结构体各字段说明：

-   buf：内部缓冲区，用于临时存储从底层io.Reader读取到的数据。
-   rd：创建Reader对象时提供的io.Reader对象，用于读取数据。
-   r：缓冲区中的读取位置，表示下一个要读取的字节位置。
-   w：缓冲区中的写入位置，表示下一个要写入的字节位置。
-   err：存储读取数据过程中发生的错误。
-   lastByte：上一次读取的字节，用于UnreadByte方法，-1表示无效。
-   lastRuneSize：上一次读取的Unicode字符大小，用于UnreadRune方法，-1表示无效。

Reader结构体常用的方法如下：

| 方法名 | 功能 |
| --- | --- |
| Read | 从缓冲区中读取数据到指定的字节切片中 |
| ReadString | 从缓冲区中读取数据到字符串中，直到遇到指定的分隔符 |
| ReadLine | 从缓冲区读取一行数据，返回的字节切片包含行尾的换行符 |

Writer结构体的定义如下：

```go
type Writer struct {
	err error
	buf []byte
	n   int
	wr  io.Writer
}
```

Writer结构体各字段说明：

-   err：存储写入数据过程中发生的错误。
-   buf：内部缓冲区，用于临时存储需要写入到底层io.Reader的数据。
-   n：缓冲区中的有效数据长度，表示待写入的字节数。
-   wr：创建Writer对象时提供的io.Writer对象，用于写入数据。

Writer结构体常用的方法如下：

| 方法名 | 功能 |
| --- | --- |
| Write | 将字节切片中的数据写入缓冲区 |
| WriteString | 将字符串中的数据写入缓冲区 |
| Flush | 将缓冲区中的数据刷新到底层的io.Writer |

**说明一下：**

-   Reader结构体中的lastByte字段，用于支持UnreadByte方法，该方法用于将最后读取的字节放回缓冲区。lastRuneSize字段用于支持UnreadRune方法，该方法用于将最后读取的Unicode字符放回缓冲区。
-   Writer结构体中不需要记录下一次缓冲区的读取和写入位置，因为Writer缓冲区的写入操作是顺序进行的，不需要回退或随机访问缓冲区中的数据，而只有当缓冲区满或调用Flush方法时才会读取缓冲区中的数据，将其刷新到底层的io.Writer中，每次刷新都会读取整个缓冲区中的数据。

> 写文件

要使用bufio包对文件进行写入操作，首先需要创建出Writer对象，bufio包中提供了两个用于创建Writer对象的函数，它们的函数原型如下：

```go
func NewWriter(w io.Writer) *Writer 
func NewWriterSize(w io.Writer, size int) *Writer
```

参数说明：

-   w：创建Writer对象时提供的io.Writer对象，用于写入数据。
-   size：表示创建的Writer对象的缓冲区大小，以字节为单位。

返回值说明：

-   返回所创建的Writer对象的指针，该对象具有指定大小的缓冲区，如果使用NewWriter函数创建Writer对象，那么缓冲区大小默认为4096字节。

使用NewWriter或NewWriterSize函数创建Writer对象时，都需要提供一个io.Writer类型的对象。io.Writer本质是io包中的一个接口类型，其定义如下：

```go
type Writer interface {
	Write(p []byte) (n int, err error)
}
```

io.Writer接口中只定义了一个Write方法，因此所有实现了该Write方法的类型都可以传递给io.Writer接口。而os包中的File结构体提供的Write方法，恰好与io.Writer接口中的Write方法签名一致，因此File对象可以传递给io.Writer接口。File结构体提供的Write方法如下：

```go
func (f *File) Write(b []byte) (n int, err error)
```

当bufio.Writer刷新底层缓冲区时，会回调File对象的Write方法，将缓冲区中的数据写入到文件中，完成文件的写入操作。案例如下：

```go
package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\BufferedOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
	// 3、写文件
	var str string
	for i := 0; i < 10; i++ {
		str += "Hello File(bufio package)" + strconv.Itoa(i) + "\n"
	}
	writer := bufio.NewWriter(file) // 创建Writer
	count, err := writer.Write([]byte(str))
	if err != nil {
		fmt.Printf("write file error, err = %v\n", err)
		return
	}
	writer.Flush() // 刷新缓冲区
	fmt.Printf("write %d bytes data to data.txt\n", count) // write 270 bytes data to data.txt
}
```

运行程序后可以看到数据被成功写入文件。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/99d5181a153e4ae2a81d747643a25abd.png)

**注意：** 只有当Writer缓冲区满或调用Flush方法时，才会将缓冲区中的数据刷新到底层的io.Writer中，因此在数据写入Writer后需要Flush方法刷新缓冲区中的数据。

> 读文件

要使用bufio包对文件进行读取操作，首先需要创建出Reader对象，bufio包中提供了两个用于创建Reader对象的函数，它们的函数原型如下：

```go
func NewReader(rd io.Reader) *Reader 
func NewReaderSize(rd io.Reader, size int) *Reader
```

参数说明：

-   rd：创建Reader对象时提供的io.Reader对象，用于读取数据。
-   size：表示创建的Reader对象的缓冲区大小，以字节为单位。

返回值说明：

-   返回所创建的Reader对象的指针，该对象具有指定大小的缓冲区，如果使用NewReader函数创建Reader对象，那么缓冲区大小默认为4096字节。

使用NewReader或NewReaderSize函数创建Reader对象时，都需要提供一个io.Reader类型的对象。io.Reader本质是io包中的一个接口类型，其定义如下：

```go
type Reader interface {
	Read(p []byte) (n int, err error)
}
```

io.Reader接口中只定义了一个Read方法，因此所有实现了该Read方法的类型都可以传递给io.Reader接口。而os包中的File结构体提供的Read方法，恰好与io.Reader接口中的Read方法签名一致，因此File对象可以传递给io.Reader接口。File结构体提供的Read方法如下：

```go
func (f *File) Read(b []byte) (n int, err error)
```

当bufio.Reader读取文件时，会回调File对象的Read方法，将文件中的数据读取到缓冲区中，完成文件的读取操作。案例如下：

```go
package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	name := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\BufferedOperation\data.txt`
	// 1、打开文件
	file, err := os.OpenFile(name, os.O_RDONLY, 0666)
	if err != nil {
		fmt.Printf("open file error, err = %v\n", err)
		return
	}
	// 2、延迟关闭文件
	defer file.Close()
	// 3、获取文件大小
	fileInfo, err := file.Stat()
	if err != nil {
		fmt.Printf("get file info error, err = %v\n", err)
		return
	}
	fileSize := fileInfo.Size()
	// 4、读文件
	reader := bufio.NewReader(file) // 创建Reader
	buffer := make([]byte, fileSize)
	count, err := reader.Read(buffer)
	if err != nil {
		fmt.Printf("read file error, err = %v\n", err)
		return
	}
	fmt.Printf("file size = %d, read count = %d\n", fileSize, count)
	fmt.Printf("file content:\n%s\n", string(buffer))
}
```

运行程序后可以看到文件中的数据被成功读取出来。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/3d45fef211a942c6b9d8c20bdd344b3a.png)

### 文件拷贝操作（io包）

> io包介绍

在io包中，使用Copy函数能够将数据从一个io.Reader拷贝到一个io.Writer中，该函数的函数原型如下：

```go
func Copy(dst Writer, src Reader) (written int64, err error)
```

参数说明：

-   dst：表示将拷贝到的数据写入到该io.Writer中。
-   src：表示从该io.Reader中拷贝数据。

返回值说明：

-   written：返回成功拷贝的字节数。
-   err：如果拷贝过程中出错，将返回非nil的错误值。

> 拷贝文件

前面已经介绍过，io.Reader和io.Writer本质都是io包中的接口类型，os包中的File结构体对这两个接口进行了实现，因此在使用io.Copy函数拷贝文件时，只需传入src文件和dst文件的File对象即可。案例如下：

```go
package main

import (
	"fmt"
	"io"
	"os"
)

func CopyFile(dstName string, srcName string) (err error) {
	// 1、以读方式打开源文件
	srcFile, err := os.OpenFile(srcName, os.O_RDONLY, 0666)
	if err != nil {
		fmt.Printf("open src file error, err = %v\n", err)
		return
	}
	defer srcFile.Close()
	// 2、以写方式打开目标文件
	dstFile, err := os.OpenFile(dstName, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Printf("open dst file error, err = %v\n", err)
		return
	}
	defer dstFile.Close()
	// 3、进行文件拷贝
	count, err := io.Copy(dstFile, srcFile)
	if err != nil {
		fmt.Printf("copy file error, err = %v\n", err)
		return
	}
	fmt.Printf("copy file success, copy size = %d\n", count) // copy file success, copy size = 250
	return
}

func main() {
	srcName := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\CopyOperation\src.txt`
	dstName := `D:\github\Golang-topics\src\go_code\FileOperation\FileOperation\CopyOperation\dst.txt`
	err := CopyFile(dstName, srcName)
	if err != nil {
		fmt.Printf("copy file error, err = %v\n", err)
	}
}
```

运行程序后可以看到成功完成了文件的拷贝操作。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/8d68f1972e3948a1b8c727c8debe76fb.png)

## 命令行参数

### 基本介绍

> 基本介绍

-   命令行参数是在运行程序时通过命令行传递给程序的参数，在Go中可以使用os包的Args变量来访问命令行参数。
-   os.Args是一个字符串切片，其中第一个元素是程序本身的名称，随后的元素依次是传递给程序的命令行参数。

使用案例如下：

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	fmt.Printf("os.Args type = %T\n", os.Args) // os.Args type = []string
	for index, value := range os.Args {
		fmt.Printf("args[%d] = %v\n", index, value)
	}
}
```

运行上述程序时指明命令行参数，可以看到命令行参数被逐一输出。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/6ab96a823833436da4885b3df17b93fd.png)

### 解析命令行参数（flag包）

> flag包介绍

-   通过os包的Args变量虽然可以访问命令行参数，但对参数的解析不是特别方便，比如各个参数的含义定义后要求严格的输入顺序。
-   flag包是Go标准库中的一个包，通过flag包可以轻松定义和解析命令行参数，并且参数的输入顺序可以随意。

flag包中常用的函数如下：

| 函数名 | 功能 |
| --- | --- |
| StringVar | 用于定义一个string类型的命令行参数 |
| IntVar | 用于定义一个int类型的命令行参数 |
| BoolVar | 用于定义一个bool类型的命令行参数 |
| Parse | 用于解析命令行参数，将命令行参数的值赋给相应的变量 |

其中StringVar、IntVar和BoolVar函数都有四个参数，没有返回值。各个参数的含义如下：

-   第一个参数：指向对应类型变量的指针，用于存储命令行参数的值。
-   第二个参数：命令行参数的名称。
-   第三个参数：命令行参数的默认值，用户未输入该命令行参数时采用。
-   第四个参数：命令行参数的描述信息。

> 解析命令行参数

例如在运行mysql命令连接MySQL服务器时，需要通过命令行参数指明用户名、密码、服务端IP地址和端口号等。借助flag包可以按如下方式实现：

```go
package main

import (
	"flag"
	"fmt"
)

func main() {
	var user, psw, ip string
	var port int
	flag.StringVar(&user, "u", "root", "用户名")
	flag.StringVar(&psw, "p", "000000", "密码")
	flag.StringVar(&ip, "h", "localhost", "IP地址")
	flag.IntVar(&port, "P", 3306, "端口号")

	flag.Parse() // 解析命令行参数

	fmt.Printf("user = %v\n", user)
	fmt.Printf("psw = %v\n", psw)
	fmt.Printf("ip = %v\n", ip)
	fmt.Printf("port = %v\n", port)
}
```

在运行程序时，需要通过`-命令行参数名称 命令行参数`的形式，依次指明各个命令行参数，如果输入的命令行参数名称未定义，则会输出一个简单的使用手册。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/5e03e42373284327b3cb24135a8fe79b.png)

只有按照正确的格式指明命令行参数后程序才能正常运行，但各个命令行参数的指明顺序可以随意。对于未指明的命令行参数，将采用定义命令行参数时给定的默认值。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/9a3c5b9f44664cc9b43bb99f843bc0d0.png)

**注意：** 在命令行参数定义完毕后，需要调用Parse函数解析命令行参数，这样才能将命令行参数的值赋给相应的变量。

## JSON

### 基本介绍

> 基本介绍

-   JSON（JavaScript Object Notation）是一种轻量级的数据交换格式，常用于在网络上传输数据，它最初是基于JavaScript语言的一个子集，但目前已经成为一种独立于编程语言的数据格式。
-   不同的系统和平台通过JSON可以方便地交换和共享数据，无论其使用的是哪种编程语言，这使得JSON成为一种常用的数据交换格式，在Web开发、API设计和数据存储中得到广泛应用。
-   JSON序列化指的是将数据转换为JSON格式的过程，JSON反序列化指的是将JSON格式的数据转换为原始数据的过程。

JSON使用人类可读的文本来表示结构化数据，采用键值对的方式存储数据，其由以下几种数据类型组成：

-   对象：由一组无序的键值对组成，使用大括号{}表示。键是字符串，值可以是字符串、数字、布尔值、对象、数组或空值。
-   数组：由一组有序的值组成，使用中括号\[\]表示。值可以是字符串、数字、布尔值、对象、数组或空值。
-   字符串：由双引号包围的Unicode字符序列。
-   数字：整数或浮点数。
-   布尔值：true或false。
-   空值：null。

例如下面是一个简单的JSON字符串：

```javascript
{
	"name": "Alice",
	"age": 12,
	"gender": "女",
	"scores": [
		105,
		128,
		115
	]
}
```
### JSON 序列化

> JSON 序列化

在 Go 中，使用 encoding/json 包中的 Marshal 函数能够对数据进行 JSON 序列化，该函数的函数原型如下：

```go
func Marshal(v interface{}) ([]byte, error)
```

参数说明：

-   v：需要进行 JSON 序列化的任意类型的数据。

返回值说明：

-   第一个返回值：表示 JSON 序列化成功后得到的 JSON 字符串。
-   第二个返回值：如果序列化过程中出错，将返回非 nil 的错误值。

使用案例如下：
```go
package main

import (
	"encoding/json"
	"fmt"
)

type Student struct {
	Name   string         `json:"name"`
	Age    int            `json:"age"`
	Gender string         `json:"gender"`
	Scores map[string]int `json:"scores"`
}

func main() {
	var stu = Student{
		Name:   "Alice",
		Age:    12,
		Gender: "女",
		Scores: map[string]int{
			"语文": 105,
			"数学": 128,
			"英语": 115,
		},
	}
	data, err := json.Marshal(stu)
	if err != nil {
		fmt.Printf("json serialize error, err = %v\n", err)
		return
	}
	fmt.Printf("json str = %s\n", string(data))
}
```

运行程序后可以看到序列化后的JSON字符串。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/999cbf9aa23d49fe8a54f18b30170c29.png)

**说明一下：** 通过结构体字段的Tag标签，可以指定结构体各个字段在JSON序列化时的名称，如果没有指定则默认使用字段名。

### JSON反序列化

> JSON反序列化

在Go中，使用encoding/json包中的Unmarshal函数能够将JSON字符串反序列化为原始数据，该函数的函数原型如下：

```go
func Unmarshal(data []byte, v interface{}) error
```

参数说明：

-   data：需要进行反序列化的JSON字符串。
-   v：指向目标结构体或数据的指针，用于保存反序列化后的结果。

返回值说明：

-   反序列化成功返回nil，否则返回非nil的错误值。

使用案例如下：

```go
package main

import (
	"encoding/json"
	"fmt"
)

type Student struct {
	Name   string `json:"name"`
	Age    int    `json:"age"`
	Gender string `json:"gender"`
	Scores []int  `json:"scores"`
}

func main() {
	var str = `{"name":"Alice","age":12,"gender":"女","scores":[105,128,115]}`
	var stu Student
	err := json.Unmarshal([]byte(str), &stu)
	if err != nil {
		fmt.Printf("json unserialize error, err = %v\n", err)
		return
	}
	fmt.Printf("stu = %v\n", stu)
}

```

运行程序后可以看到反序列化后的Student对象。如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1475b0689ee5408e84f316ee38650c3d.png)

**注意：** 要确保反序列化的数据类型与序列化前的数据类型一致，否则会反序列化失败。