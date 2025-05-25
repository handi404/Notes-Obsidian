ChatClient 的流式 API 所采用的链式调用中每个方法的详解：
```
ChatClient.prompt() 
    → PromptSpec (配置提示)
    → CallSpec (执行调用)
    → ResponseSpec (处理响应)
```