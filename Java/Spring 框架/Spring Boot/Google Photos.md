构建一个类似 Google Photos 的小型克隆应用，使用 Spring Boot。此外，在过程中，你将学到很多关于 Spring 的依赖注入和 MVC 框架、验证和文件处理、Spring Data 和数据库的知识。
依赖项：**Spring Web**， **Spring Data JDBC**， **H2 Database**

### 首次启动项目
localhost:8080 我们收到一条错误消息 404，表示未找到任何内容，这实际上是我们所期望的，因为我们的 spring 应用程序无法执行任何操作，没有任何东西可以接受 json 消息或将其发送回前端，所以 404 非常好，因为这意味着我们的应用程序实际上已经启动了，现在我们需要通过构建我们的 rest api 来改变这种情况。

### Hello World @RestController
现在我们不再需要显示错误消息，而是显示成功消息，因此我们需要添加一些 hello world 之类的东西。
基础包中创建一个新类：
```java
package com.jetbrains.handi.photo.clone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class photozController {
    @GetMapping("/")
    public String hello() {
        return "hello world";
    }
}
```
默认情况下，当运行应用程序时，spring 不会自动重新加载并注册这些新方法，所以我们要做的就是重新运行。

### REST API 设计
照片克隆本质上我们想要的是我们希望前端能够做的是拍摄一些照片并将它们上传到服务器，这意味着我们最终会得到很多 Java 商业应用程序最终会得到的东西，这意味着我们希望能够创建照片，我们希望能够读取照片。
比如获取它们并看到你知道特定的照片，我们希望能够更新它们，能够删除它们，这意味着我们正在构建一个 crud rest api。

### @GetMapping
得到 photo 的 rest api
创建一个所谓的照片模型类：
```java
package com.jetbrains.handi.photo.clone;  
  
public class Photo {  
    private String id;  
    private String fileName;  
      
    //get and set constructor   
}
```
```java

@RestController
public class PhotozController {
    private List<Photo> db = List.of(new Photo("1", "hello.jpg"));
    //...
    @GetMapping("/photoz")
    public List<Photo> get(){
        return db;
    }
}
```
还想做的是，我希望能够获取一张特定的照片：
```java

@RestController
public class PhotozController {
    private Map<String, Photo> db = new HashMap<>() {{
        put("1", new Photo("1", "hello.jpg"));
    }};

    @GetMapping("/photoz")
    public Collection<Photo> get(){
        return db.values();
    }

    @GetMapping("/photoz/{id}")
    public Photo get(@PathVariable String id){
        Photo photo = db.get(id);
        if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return photo;
    }
}
```

### @DeleteMapping
```java
@DeleteMapping("/photoz/{id}")  
public void delete(@PathVariable String id){  
    Photo photo = db.remove(id);  
    if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);  
}
```
### Browser - HTTP Delete
测试需要在浏览器 console 输入：
```js
(async function deletePhoto(id) {
          await fetch("http://localhost:8080/photoz/" + id, {
                    method: "DELETE"
          })
})("5")
```

### @PostMapping
前端发送一些 json，然后 spring boot应该将该 json 转换为照片对象，然后我们就可以直接处理照片对象了。
有趣的是前端不应该生成 id 我们应该生成 id
```java
@PostMapping("/photoz")
    public Photo create(Photo photo){
        photo.setId(UUID.randomUUID().toString());
        db.put(photo.getId(), photo);
        return photo;
    }
```
### Browser - HTTP Post
```js
(async function createPhoto() {
          let photo = {"fileName": "hello.jpg"};
          await fetch("http://localhost:8080/photoz", {
                    method: "POST",
                    headers: {
                              "Accept": "application/json",
                              "Content-Type": "application/json"
                    },
                    body: JSON.stringify(photo)
                    })
                    .then(result => result.text())
                    .then(text => alert(text));
})()
```

### @RequestBody - 接收 JSON
文件名等于 null 我们需要找出原因，其中一个错误，让很多初学者都经历它。
需要一个标记注释 `@RequestBody`，以便来告诉 spring boot 嘿，请获取整个 json 并将其转换为我的 Photo 对象。
```java
@PostMapping("/photos")  
public Photo create(@RequestBody Photo photo){  
    photo.setId(UUID.randomUUID().toString());  
    db.put(photo.getId(), photo);  
    return photo;  
}
```

### 验证用户输入（validation）
实际上可以发送 json 没有文件名，得到了随机 id 但这里的 fileName 为 null，显然不是我们想要的。
添加 `spring-boot-starter-validation`：
```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-validation</artifactId>  
</dependency>
```
现在已经拥有了全面的验证功能。标记注解表明您的照片是有效的，因此只需在此处放置 `@valid` 注解。
```java
@PostMapping("/photos")  
public Photo create(@RequestBody @Valid Photo photo){  
    photo.setId(UUID.randomUUID().toString());  
    db.put(photo.getId(), photo);  
    return photo;  
}
```
valid 是什么意思，让我们来看看 Photo 本身，我们目前不关心 ID，但 fileName 不应该为空：
```java
public class Photo {
    //...
    @NotEmpty
    private String fileName;
    //...
}
```
在 ` jakarta.validation.constraints` 中能看到许多不同注解，@NotEmpty 是其中之一。**所以你基本上可以用不同的注解去注解你的字段，然后 springboot 将能够自动验证**。

### 上传照片
让我们上传一张真实的照片，而不仅仅是发送元数据为此，我们将使用浏览器的文件选择器功能。首先让我们在 `resources/static` 中创建一个小的 html 静态页面。
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h2>Photo Upload</h2>
<input id="fileupload" type="file" name="fileupload" />
<button id="upload-button" onclick="uploadFile()"> Upload </button>

<script>
    async function uploadFile() {
        let formData = new FormData();
        formData.append("data", fileupload.files[0]);
        await fetch('http://localhost:8080/photoz', {
            method: "POST",
            body: formData
        }).then(result =>    result.text())
            .then(text => alert(text));
    }
</script>
</body>
</html>
```
需要给 Photo 添加新的属性 `private byte[] data;`
完善 PostMapping 部分：
```java
@PostMapping("/photos")
    public Photo create(@RequestPart("data") MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setId(UUID.randomUUID().toString());
        photo.setFileName(file.getOriginalFilename());
        photo.setData(file.getBytes());
        db.put(photo.getId(), photo);
        return photo;
    }
```


### application.properties
上传图片会遇到限制大小的错误。
spring boot默认将最大允许上传大小为特定数量的字节，你可以做的是可以更改配置。
查看 `application.properties` 文件，在这个应用程序属性配置文件中，你可以将你知道的配置告诉 spring boot。
```properties
spring.servlet.multipart.max-file-size=100MB  
spring.servlet.multipart.max-request-size=100MB
```
上传成功，可以看到生成的字节，或者更确切地说是转换为 base64 字符串编码。

### Jackson & @JsonIgnore
想看到真正漂亮的图像，有两件事首先，当我们获得元数据时，我们只希望spring 不包含数据字段并将其发送回前端，我们可以通过返回到
打开 Photo 类然后为 spring 执行 json 转换操作，需要使用另一个 jackson 特定的标记注释，即 `@JsonIgnore`，只需将它放在 data 字段上。现在，每当我们将照片发送回前端时，数据不会被转换，我们看不到它，这意味着目前我们可以上传照片，但我们无法取回数据，但我们想在浏览器中看到一张真实的照片，这意味着我们需要对我们的应用程序进行大量重构，因为我想做的是，目前有几种方法可以解决这个问题

### Photo Download
假设我们想要另一个额外的控制器，它被称为 DownloadController，
创建来自照片的数据。数据库也需要一些标头，我们使用 spring 框架提供的特定类 http标头
```java
@RestController  
public class DownloadController {  
    @GetMapping("/download/{id}")    public ResponseEntity<byte[]> download(@PathVariable String id) {  
        byte[] data;  
        HttpHeaders heaers = new HttpHeaders();  
        return new ResponseEntity<>(data, headers, HttpStatus.OK);  
    }  
}
```
想要把它发回给客户，现在的问题是，在我们完成这段旅程之前，我们如何获得照片，这是我们第一次将学习 spring 中的依赖注入，依赖注入是什么意思？
### Dependency Injection(依赖注入)
[[Dependency Injection 依赖注入]]
