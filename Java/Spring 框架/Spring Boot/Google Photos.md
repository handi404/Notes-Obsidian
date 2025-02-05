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

### 下载图片
在 Photo 中添加 contentType 属性：`private String contentType`，后对 PostMapping 进行完善：
```java
@PostMapping("/photos")  
public Photo create(@RequestPart("data") MultipartFile file) throws IOException {  
    return photosService.save(file.getOriginalFilename(),file.getContentType(), file.getBytes());  
}
```
之后完善 download：
```java
@GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        Photo photo = photosService.get(id);
        if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        byte[] data = photo.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(photo.getContentType()));
        ContentDisposition build = ContentDisposition
                .builder("attachment")
                .filename(photo.getFileName())
                .build();
        headers.setContentDisposition(build);
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
```
之后在 upload 上传图片后得到 id，可通过 id 到 download 下载图片。
### 推荐的项目结构
目前这些类都位于一个文件夹或一个包中，我希望重构项目。
引入几个包，例如，有很多不同的，有 web package，service package，model package。
现在可以做的是，将 Controllers 移动到 web package 中，将实体类移动到 model 中，将 services 移动到 service 中。

### H2 Database - Intro
开始使用真正的数据库前，现在查看 PhotosService 时，可以看到它有一个 map，它将你的照片存储在内存中，在一个普通的哈希映射中，我们想要访问一个数据库一
我们添加了一个 h2 数据库，有趣的是，h2 数据库只是一个java 库，一旦你编辑了你的项目，spring boot 就会自动启动它，就会有一个内存数据库，你看不到任何文件或其他东西，但实际上可以像使用postgres、mysql、oracle、microsoft sql server 一样使用它。默认情况下它只在内存中可用，但也可以告诉 h2 或 spring 将数据库内容保存到文件中，这样就可以实际看到它，并且知道有些东西正在被保存。
这样做的方式是在 `application.properties` 中属性有一个新的属性 `datasource.url`，你需要告诉springboot在哪里保存你的数据库文件：
```properties
spring.datasource.url=jdbc:h2:file:~/springboot;AUTO_SERVER=TRUE;
```
启动项目后就能在根目录找到，然后可以复制到任何服务器，有效地重复使用它。

### 创建数据库模式
让我们想想如何把照片存储在数据库中，这意味着需要有一些表。spring boot有各种各样的方法，例如当你启动 springboot 时，它会检查数据库中是否已经有一些表，若没有会根据选项创建。
进入资源文件夹，创建一个新文件，schema.sql：
```sql
create table if not exists photos (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_name varchar(255),
    content_type varchar(255),
    data binary large object
);
```
现在再次需要知道最后一个属性，用于我们的项目，它被称为 `spring.sql.init.mode` 等于 always，即希望总是在我们启动时执行模式 sql。
重新运行应用程序，然后我们收到一条错误消息，另一条消息是创建表格不存在的 photos，似乎存在预期标识符的问题。收到了两条消息，说 sql dialect is not configured，实际上可以告诉 intellij 我的文件是一个 h2 文件 sql dialect。
希望能够访问数据库，你可以在 idea 中内置非常好的数据库访问，实际上 h2 数据库带有一个**web 界面**，通过添加新属性 `spring.h2.console.enabled=true` 来启用它。
先将 sql 文件名改为 bacschema(**备份模式**，这样 spring 就不会执行此文件)，重新启动进入 `/h2-console` 页面，就是 h2 数据库的 web 页面：JDBC URL 改为与 spring.datasource.url 一致，没有用户名密码删除即可。

### Spring Data JDBC
关于数据库最简单的入门方式，正如一开始提到的，spring data jdbc是 jdbc 是将数据库语句从 java 发送到数据库的低级 api，而 spring data 可以说是 jdbc api 的一个小型舒适包装器。
快速入门 springdata 创建 `repository存储库`。所谓的 repository，我们只需创建一个类，我们将在 repository 包中调用该类。
在 repository 包中创建 PhotosRepository 接口，它**扩展一个来自 spring data 的已经存在的接口，称为 `CrudRepository`**，CrudRepository 为你提供了大量的方法，比如 find delete save into database 中。显然可以编写自己的sql语句，但现在只想开始使用 CrudRepository 并获得基本的最终按id查找，更新语句的工作。
```java 
public interface PhotosRepository extends CrudRepository<Integer, Photo> {  
}
```
快速了解，简而言之，当spring启动时扫描 repository 发现有 CrudRepository，它会为你生成那些sql语句，这样你的数据库调用就可以工作了，这是一部分；第二部分是标记注解，我们需要做的是将我们的 java 类映射到我们的数据库表：
```java  
//h2 默认将每个表转换为大写
@Table("PHOTOS")  
public class Photo {  
    @Id    
    private Integer id;  
    @NotEmpty  
    private String fileName;  
  
    private String contentType;  
  
    @JsonIgnore  
    private byte[] data;  
 //...
}
```
然后是更改我们的 PhotosService：
```java
@Service  
public class PhotosService {  
    private final PhotosRepository photosRepository;  
  
    public PhotosService(PhotosRepository photosRepository) {  
        this.photosRepository = photosRepository;  
    }  
  
    public Iterable<Photo> getPhotos() {  
        return photosRepository.findAll();  
    }  
    public Photo get(Integer id) {  
        return photosRepository.findById(id).orElse(null);  
    }  
  
    public void remove(Integer id) {  
        photosRepository.deleteById(id);  
    }  
  
    public Photo save(String fileName,String contentType, byte[] data) {  
        Photo photo = new Photo();  
        photo.setFileName(fileName);  
        photo.setContentType(contentType);  
        photo.setData(data);  
        photosRepository.save(photo);  
        return photo;  
    }  
}
```
更改 PhotosController：
```java
@RestController  
public class PhotosController {  
    private final PhotosService photosService;  
  
    public PhotosController(PhotosService photosService) {  
        this.photosService = photosService;  
    }  
  
    @GetMapping("/")  
    public String hello() {  
        return "hello world";  
    }  
  
    @GetMapping("/photos")  
    public Iterable<Photo> get(){  
        return photosService.getPhotos();  
    }  
  
    @GetMapping("/photos/{id}")  
    public Photo get(@PathVariable Integer id){  
        Photo photo = photosService.get(id);  
        if (photo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);  
        return photo;  
    }  
  
    @DeleteMapping("/photos/{id}")  
    public void delete(@PathVariable Integer id){  
        photosService.remove(id);  
    }  
  
    @PostMapping("/photos")  
    public Photo create(@RequestPart("data") MultipartFile file) throws IOException {  
        return photosService.save(file.getOriginalFilename(),file.getContentType(), file.getBytes());  
    }  
}
```

快速总结一下，在部署应用程序之前，我们忽略了很多东西，我们必须确保我们有验证，必须确保我们清理了我们的数据，必须确保正确地限制我们的数据。

### 打包与部署
部署是什么意思，现在打开一个终端窗口，事实上，如果使用的是 intellij，也可以使用快捷方式，而不是打开终端窗口，用右侧 maven 工具箱窗口。如果你不使用intellij，只是想看看它在命令行中完成的操作，输入 `./mvnw clean package`，记住在项目 maven clean package 的最开始，请确保你的 java home 环境变量设置为与你构建项目时使用的 java 版本完全相同。
构建成功，现在如果进入 target 文件夹，这是一个标准的 maven构建文件夹，进去后，你会看到你有一个几个字节的 jar 文件。可以简单地把这个 jar 文件放到服务器上，放到任何我想要的地方，然后运行它，这就是部署。
如何运行这个文件，`java -jar jar文件` 只需这一句，然后你的应用程序启动，它现在**没有在 ide 中启动，只是在终端窗口中。再次打开浏览器时，可以看到应用程序，看看 photos，它们仍然在数据库中，因为我正在我文件系统上的主目录中访问同一个数据库。**

### 结束与下一步
我们没有涵盖大量主题，例如测试与测试有关的一切，进一步的数据库访问等等。
