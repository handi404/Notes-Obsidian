当表单以 `multipart/form-data` 编码类型提交时，这意味着表单中可能包含了文件上传字段。对于这种类型的请求，标准的 `request.getParameter()` 方法将不再适用于获取表单数据，因为这些方法是为简单键值对形式的数据设计的（例如 `application/x-www-form-urlencoded` 或 `multipart/form-data` 中的非文件字段）。对于文件上传和其他 multipart 数据，你需要使用专门的库或 API 来解析和处理。

### 处理 `multipart/form-data` 请求

#### 使用 Apache Commons FileUpload 库

Apache Commons FileUpload 是一个广泛使用的库，用于处理 `multipart/form-data` 类型的 HTTP 请求。它能够轻松地解析出文件和其他表单字段，并且可以处理大文件上传。下面是如何使用它的简要说明：

1. **添加依赖**：如果你使用的是 Maven 项目，可以在 `pom.xml` 文件中添加以下依赖：
   ```xml
   <dependency>
       <groupId>commons-fileupload</groupId>
       <artifactId>commons-fileupload</artifactId>
       <version>1.4</version>
   </dependency>
   ```

2. **导入必要的包**：确保在你的 Servlet 中导入了相关的类。
   ```java
   import org.apache.commons.fileupload.FileItem;
   import org.apache.commons.fileupload.disk.DiskFileItemFactory;
   import org.apache.commons.fileupload.servlet.ServletFileUpload;
   import java.util.List;
   ```

3. **检查请求是否为多部分内容**：
   ```java
   if (ServletFileUpload.isMultipartContent(request)) {
       // 处理 multipart/form-data 请求
   }
   ```

4. **创建 DiskFileItemFactory 和 ServletFileUpload 对象**：
   ```java
   DiskFileItemFactory factory = new DiskFileItemFactory();
   ServletFileUpload upload = new ServletFileUpload(factory);
   ```

5. **解析请求并处理文件项和其他表单字段**：
   ```java
   List<FileItem> items = upload.parseRequest(request);
   for (FileItem item : items) {
       if (item.isFormField()) {
           // 处理普通表单字段
           String name = item.getFieldName();
           String value = item.getString();
           // Do something with the field name and value
       } else {
           // 处理文件上传字段
           String fieldName = item.getFieldName();
           String fileName = item.getName();
           InputStream fileContent = item.getInputStream();
           // Do something with the uploaded file
       }
   }
   ```

6. **处理文件保存**：根据需要保存文件到服务器上的指定位置，或者直接在内存中处理文件内容。

7. **清理资源**：如果适用，记得关闭输入流或其他需要关闭的资源。

#### 使用 Servlet 3.0+ 内置支持

从 Servlet 3.0 开始，Java EE 提供了内置的支持来处理文件上传，无需额外的库。你可以通过 `@MultipartConfig` 注解配置 Servlet，并使用 `Part` 接口来处理上传的文件。

```java
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file"); // 获取名为 'file' 的文件上传部分
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // 获取上传文件的名称
        
        // 将文件保存到某个位置
        String uploadPath = "path/to/upload/directory";
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        
        // 处理其他表单字段
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            // Do something with the parameter name and value
        }
    }
}
```

这种方式简化了文件上传的处理过程，同时仍然提供了足够的灵活性来满足大多数应用场景的需求。

### 总结

当处理 `multipart/form-data` 请求时，关键是认识到不能直接用 `request.getParameter()` 来获取所有表单数据。而是应该使用适当的工具或 API 来解析 multipart 请求的内容。无论是选择 Apache Commons FileUpload 还是利用 Servlet 3.0+ 的内置功能，都可以有效地处理包含文件上传在内的复杂表单提交。