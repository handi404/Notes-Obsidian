当处理 `multipart/form-data` 类型的表单提交时，获取普通输入字段（如文本框、下拉列表等）的值与获取文件上传字段的方式有所不同。这里我将分别介绍使用 Apache Commons FileUpload 和 Servlet 3.0+ 内置支持这两种方式来获取普通输入字段的值。

### 使用 Apache Commons FileUpload

当你使用 Apache Commons FileUpload 库解析 multipart 请求时，普通输入字段和文件上传字段都会被解析为 `FileItem` 对象。你需要检查每个 `FileItem` 是否是一个普通的表单字段，然后从中提取值。

```java
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

// ...

if (ServletFileUpload.isMultipartContent(request)) {
    DiskFileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    
    try {
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) { // 检查是否是普通表单字段
                String fieldName = item.getFieldName(); // 获取字段名
                String fieldValue = item.getString();   // 获取字段值
                // 根据需要处理字段名和字段值
                System.out.println("Field Name = " + fieldName + ", Field Value = " + fieldValue);
            }
        }
    } catch (Exception ex) {
        // 处理异常
    }
}
```

### 使用 Servlet 3.0+ 内置支持

如果你使用的是 Servlet 3.0 或更高版本，并且你的应用服务器支持它，你可以直接利用内置的功能来处理 multipart 表单数据。对于非文件类型的表单字段，你可以继续使用 `request.getParameter()` 方法来获取它们的值。

```java
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取文件上传部分
        Part filePart = request.getPart("file");
        
        // 获取其他表单字段的值
        String inputFieldValue = request.getParameter("inputFieldName"); // 替换为实际的input名称
        
        // 打印或进一步处理这些值
        System.out.println("Input Field Value = " + inputFieldValue);
        
        // 继续处理文件上传或其他逻辑...
    }
}
```

### 注意事项

- **字符编码**：确保你设置了正确的字符编码以正确读取非 ASCII 字符。可以在解析请求之前调用 `request.setCharacterEncoding("UTF-8")`。
- **多值参数**：如果一个表单字段允许多个值（例如多个复选框），可以使用 `request.getParameterValues()` 方法来获取所有值。
- **安全性和验证**：始终对用户输入的数据进行适当的验证和清理，以防止潜在的安全风险，如跨站脚本攻击（XSS）或 SQL 注入。

通过上述方法，你可以有效地从 `multipart/form-data` 表单中提取普通输入字段的值，同时处理文件上传。选择哪种方法取决于你的项目要求和技术栈。