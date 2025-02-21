
```java
/**
 * JSON工具类，用于处理JSON数据的序列化和反序列化
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串并写入响应
     *
     * @param response HttpServletResponse对象
     * @param obj 要转换的对象
     * @throws IOException 如果写入响应时发生IO错误
     */
    public void writeJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String jsonStr = objectMapper.writeValueAsString(obj);
        PrintWriter out = response.getWriter();
        out.write(jsonStr);
        out.flush();
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param jsonStr JSON字符串
     * @param valueType 目标类型的Class对象
     * @param <T> 目标类型
     * @return 转换后的对象
     * @throws IOException 如果解析JSON时发生错误
     */
    public <T> T parseJson(String jsonStr, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonStr, valueType);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串
     * @throws IOException 如果转换过程中发生错误
     */
    public String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }
}
```