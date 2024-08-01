在Go语言中编写API通常使用 `net/http` 包，或者使用更高级的Web框架如Gin、Echo等。下面是一个使用Go内置的 `net/http` 包编写简单RESTful API的示例，以及使用Gin框架的示例。

### 使用`net/http`包编写API

#### 步骤 1: 创建项目目录

创建一个新的目录来存放你的项目：

```bash
mkdir myapi
cd myapi
```

#### 步骤 2: 编写API代码

创建一个名为`main.go`的文件，并添加以下代码：

```go
package main

import (
    "encoding/json"
    "net/http"
)

type Message struct {
    Message string `json:"message"`
}

func helloHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method == http.MethodGet {
        msg := Message{Message: "Hello, World!"}
        w.Header().Set("Content-Type", "application/json")
        json.NewEncoder(w).Encode(msg)
    } else {
        http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
    }
}

func main() {
    http.HandleFunc("/api/hello", helloHandler)
    http.ListenAndServe(":8080", nil)
}
```

#### 步骤 3: 运行API服务

在终端中运行以下命令启动服务：

```bash
go run main.go
```

#### 步骤 4: 测试API服务

打开浏览器或使用Postman等工具访问`http://localhost:8080/api/hello`，你应该会看到以下JSON响应：

```json
{
    "message": "Hello, World!"
}
```

### 使用Gin框架编写API

#### 步骤 1: 安装Gin框架

在终端中运行以下命令安装Gin：

```bash
go get -u github.com/gin-gonic/gin
```

#### 步骤 2: 编写API代码

创建一个名为`main.go`的文件，并添加以下代码：

```go
package main

import (
    "github.com/gin-gonic/gin"
)

func main() {
    r := gin.Default()
    
    r.GET("/api/hello", func(c *gin.Context) {
        c.JSON(200, gin.H{
            "message": "Hello, World!",
        })
    })
    
    r.Run(":8080")
}
```

#### 步骤 3: 运行API服务

在终端中运行以下命令启动服务：

```bash
go run main.go
```

#### 步骤 4: 测试API服务

打开浏览器或使用Postman等工具访问`http://localhost:8080/api/hello`，你应该会看到以下JSON响应：

```json
{
    "message": "Hello, World!"
}
```

### 总结

这两个示例展示了如何使用Go语言创建简单的RESTful API。使用`net/http`包可以满足基本需求，而Gin框架提供了更强大的功能和更简单的语法，可以帮助你更快地构建复杂的Web应用程序。你可以根据需要选择合适的方式来创建你的API服务。