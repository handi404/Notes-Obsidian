
### **一、`Prompt` (提示)**

1.  **是什么？**
    *   `Prompt` 在 Spring AI 中是一个对象，它封装了发送给 AI 模型的**完整输入**。
    *   你可以把它想象成你向 AI 提出的一个**具体问题、指令或者一段需要处理的文本**。
    *   最简单的 `Prompt` 就是一个包含你想要发送给模型的字符串。

2.  **为什么需要一个对象，而不仅仅是字符串？**
    *   **结构化**：虽然最基本的是文本指令，但一个 `Prompt` 对象可以携带更多结构化信息。
    *   **模型选项 (Model Options)**：除了指令文本，`Prompt` 还可以携带特定于模型的配置选项，例如 OpenAI 的 `temperature`（温度，控制创造性）、`topP`（核心采样），或者其他模型的类似参数。这些选项会影响模型的响应方式。
    *   **指令与内容分离 (未来或特定场景)**：在更复杂的场景下，一个 Prompt 可能包含多个部分，例如系统指令、用户输入、少量示例（few-shot examples）等。`Prompt` 对象为这种复杂性提供了容器。目前，对于聊天模型，这通常通过 `List<Message>` 来实现，而 `Prompt` 对象则会包装这个消息列表。

3.  **核心组成（以 `org.springframework.ai.chat.prompt.Prompt` 为例）：**
    *   **`instructions` (指令/内容)**：
        *   对于简单的文本输入模型，这通常是一个 `String`。
        *   对于聊天模型（`ChatClient`），`instructions` 通常是一个 `Message` 对象或 `List<Message>` 对象。`Message` 可以是 `SystemMessage`（系统指令）、`UserMessage`（用户输入）或 `AssistantMessage`（AI 的先前回复，用于上下文）。
    *   **`options` (选项)**：
        *   这是一个 `ChatOptions` 接口的实例，允许你设置特定于模型的参数。例如 `OpenAiChatOptions`, `AzureOpenAiChatOptions`, `OllamaOptions` 等。
        *   如果你不指定，会使用模型的默认选项。

4.  **简单示例：**

    ```java
    import org.springframework.ai.chat.prompt.Prompt;
    import org.springframework.ai.openai.OpenAiChatOptions; // 假设使用OpenAI

    // 1. 最简单的 Prompt，只包含一个字符串指令
    Prompt simpleTextPrompt = new Prompt("请给我讲一个关于程序员的笑话。");

    // 2. 带模型选项的 Prompt
    OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
            .withTemperature(0.7f) // 设置温度，增加一点创造性
            .withModel("gpt-4o")     // 指定模型 (通常在客户端级别配置，但也可在Prompt级别覆盖)
            .build();
    Prompt promptWithOptions = new Prompt("请给我讲一个关于程序员的笑话。", chatOptions);

    // 3. 用于聊天模型的 Prompt (更常见)
    import org.springframework.ai.chat.messages.UserMessage;
    import java.util.List;

    UserMessage userMessage = new UserMessage("你好，你是谁？");
    Prompt chatPrompt = new Prompt(userMessage); // 单个消息
    // 或者
    Prompt multiMessageChatPrompt = new Prompt(List.of(userMessage), chatOptions); // 消息列表和选项
    ```

#### **如何在 `Prompt` 中使用 `ChatOptions`？**

你可以在创建 `Prompt` 对象时，将其作为构造函数的第二个参数传入。

```java
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatOptions; // 示例：使用OpenAI

UserMessage userMessage = new UserMessage("给我写一首关于春天的诗。");

OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
        .withModel("gpt-4o")      // 指定模型 (也可以在ChatClient级别配置)
        .withTemperature(0.8f)  // 设置温度
        .withMaxTokens(150)     // 设置最大输出token数
        .build();

Prompt promptWithOptions = new Prompt(userMessage, chatOptions);

// 后续将此 promptWithOptions 传递给 chatClient.call() 或 chatClient.stream()
```

**常见的 `ChatOptions` 及其作用：**

不同的模型提供商会有其独特的选项，但以下是一些跨模型比较常见的概念（具体名称和可用性可能因 `ChatOptions` 的具体实现而异）：

1.  **`model` (String):**
    *   **作用**: 指定要使用的具体模型 ID 或名称（例如 "gpt-4 o", "gpt-3.5-turbo", "gemini-1.5-pro", "llama 3"）。
    *   **使用**: 虽然通常在 `ChatClient` 级别进行全局配置，但 `Prompt` 级别的 `model` 设置可以覆盖全局设置，为特定请求使用不同模型。
    *   **示例**: `OpenAiChatOptions.builder().withModel("gpt-4o-mini").build()`

2.  **`temperature` (Float/Double):**
    *   **作用**: 控制输出的随机性或“创造性”。值通常在 0.0 到 2.0 之间。
        *   **较低的值 (如 0.2)**: 使输出更具确定性、更集中、更少随机性。适合需要事实性、精确回答的场景。
        *   **较高的值 (如 0.8-1.0)**: 使输出更随机、更有创意、更多样化。适合创意写作、头脑风暴等场景。
    *   **使用**: 根据你的需求调整。
    *   **示例**: `OpenAiChatOptions.builder().withTemperature(0.7f).build()`

3.  **`topP` (Float/Double, Nucleus Sampling):**
    *   **作用**: 另一种控制输出随机性的方法，与 `temperature` 类似，但工作方式不同。模型会考虑概率总和达到 `topP` 值的最小词汇集。例如，`topP = 0.1` 表示模型只考虑构成概率质量前 10%的词汇。
    *   **使用**: 通常建议只使用 `temperature` 或 `topP` 中的一个，而不是两者都用。
    *   **示例**: `OpenAiChatOptions.builder().withTopP(0.9f).build()`

4.  **`maxTokens` (Integer, 或类似名称如 `maxOutputTokens`, `numPredict`):**
    *   **作用**: 限制模型单次响应生成的最大 token 数量（token 大致可以理解为词或子词）。这有助于控制成本和响应时间，并确保响应不会过长。
    *   **使用**: 根据期望的输出长度设置。注意，输入+输出的总 token 数通常有上限。
    *   **示例**: `OpenAiChatOptions.builder().withMaxTokens(200).build()`

5.  **`stopSequences` (`List<String>`, 或 `stop`):**
    *   **作用**: 提供一个或多个字符串序列。当模型生成到这些序列中的任何一个时，它会立即停止生成。
    *   **使用**: 用于确保模型在特定标记处停止，例如在生成列表项后或在特定格式化标记后。
    *   **示例**: `OpenAiChatOptions.builder().withStopSequences(List.of("\n", "---")).build()`

6.  **`frequencyPenalty` (Float/Double, OpenAI 等模型):**
    *   **作用**:  -2.0 到 2.0 之间的数字。正值会根据 token 在文本中已出现的频率对其进行惩罚，从而降低模型逐字重复相同行的可能性。
    *   **示例**: `OpenAiChatOptions.builder().withFrequencyPenalty(0.5 f).build()`

7.  **`presencePenalty` (Float/Double, OpenAI 等模型):**
    *   **作用**: -2.0 到 2.0 之间的数字。正值会根据 token 是否已在文本中出现过对其进行惩罚，从而鼓励模型谈论新的主题。
    *   **示例**: `OpenAiChatOptions.builder().withPresencePenalty(0.3 f).build()`

**特定模型 `ChatOptions` 示例：**

*   **`OpenAiChatOptions`**:
    *   `user` (String): 最终用户的唯一标识符，可帮助 OpenAI 监控和检测滥用行为。
    *   `functions` / `tools` (`List<String>` 或 `List<ChatFunction>`): 用于定义可供模型调用的外部函数/工具。这是实现 Function Calling/Tool Calling 的关键。
    *   `responseFormat` (Object): 例如，`ChatResponseFormat.JSON` 可以请求模型以 JSON 格式输出。`new OpenAIJsonModeResponseFormat()`
    *   `seed` (Integer): 如果模型支持，使用相同的种子和参数可以使输出更具可重复性（通常用于调试）。

*   **`OllamaOptions`**:
    *   `mirostat` (Integer): 启用 Mirostat 采样 (0=禁用, 1=Mirostat, 2=Mirostat 2.0)。
    *   `mirostatEta` (Float): Mirostat 学习率。
    *   `mirostatTau` (Float): Mirostat 目标惊喜度。
    *   `numCtx` (Integer): 上下文窗口大小。
    *   `repeatPenalty` (Float): 控制重复。
    *   `numGpuLayers` (Integer): 要卸载到 GPU 的层数。

**重点**:
*   每个模型提供商的 `ChatOptions` 实现都会有其特定的 `builder()` 方法和 `withXxx()` 方法来设置这些参数。
*   请查阅 Spring AI 文档中对应模型提供商的章节，以获取最准确和最全面的选项列表。

### **二、`PromptTemplate` (提示模板)**

1.  **是什么？**
    *   `PromptTemplate` 是一种**动态生成 `Prompt` 的机制**。
    *   它允许你定义一个包含**占位符 (placeholders)** 的模板字符串，然后在运行时用具体的值替换这些占位符，从而创建出一个具体的 `Prompt` 对象。

2.  **为什么需要 `PromptTemplate`？**
    *   **动态性**：你的应用需要根据用户输入、上下文或其他动态数据来构造提示。例如，你可能想问 AI：“请总结一下关于主题‘{topic}’的最新新闻。” 这里的 `{topic}` 就是一个占位符。
    *   **复用性**：定义一次模板，可以在多处使用，只需要传入不同的参数。
    *   **结构化和一致性**：确保生成的提示遵循特定结构，这对于获得模型一致且高质量的输出非常重要（即所谓的“提示工程” Prompt Engineering 的一部分）。
    *   **可维护性**：将提示的结构与动态数据分离，使得修改提示逻辑更加容易，而不需要在代码中到处拼接字符串。

3.  **核心组成与使用：**
    *   **模板字符串 (Template String)**：一个包含占位符的字符串。占位符通常用大括号 `{}` 包裹，例如 `{variableName}`。
    *   **参数填充 (Parameter Filling)**：你需要提供一个 `Map<String, Object>`，其中键是占位符的名称，值是替换占位符的具体内容。
    *   **`render(Map<String, Object> model)` 方法**：将模板字符串中的占位符替换为 `model` 中提供的值，生成一个最终的提示字符串。
    *   **`create(Map<String, Object> model)` 方法**：更进一步，它不仅渲染模板，还会直接创建一个 `Prompt` 对象。如果模板被设计为生成聊天消息，它也可以创建包含渲染后 `Message` 对象的 `Prompt`。

4.  **简单示例：**

    ```java
    import org.springframework.ai.chat.prompt.Prompt;
    import org.springframework.ai.chat.prompt.PromptTemplate;
    import java.util.Map;

    // 1. 创建一个 PromptTemplate
    String templateString = "请告诉我关于 {subject} 的三个有趣的事实。请确保使用{language}来回答。";
    PromptTemplate promptTemplate = new PromptTemplate(templateString);

    // 2. 准备替换占位符的参数
    Map<String, Object> parameters = Map.of(
            "subject", "太阳系",
            "language", "中文"
    );

    // 3. 使用 PromptTemplate 创建一个 Prompt 对象
    Prompt dynamicPrompt = promptTemplate.create(parameters);

    // dynamicPrompt.getInstructions().getContent() 的内容将会是:
    // "请告诉我关于 太阳系 的三个有趣的事实。请确保使用中文来回答。"
    // (注意：这里的 getInstructions() 返回的是 Message，getContent() 拿到具体字符串)

    // 也可以先渲染成字符串，再手动创建 Prompt
    // String renderedContent = promptTemplate.render(parameters);
    // Prompt manualPrompt = new Prompt(renderedContent);

    // 4. 将 dynamicPrompt 传递给 ChatClient
    // ChatClient chatClient = ...;
    // ChatResponse response = chatClient.call(dynamicPrompt);
    // System.out.println(response.getResult().getOutput().getContent());
    ```

5.  **从外部资源加载模板：**
    *   `PromptTemplate` 非常灵活，它不仅可以接受字符串作为模板，还可以接受 Spring 的 `Resource` 对象。这意味着你可以将模板存储在文件系统、类路径等位置。
    *   这对于管理复杂的或大量的提示模板非常有用。

    ```java
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.core.io.Resource;

    // Resource resource = new FileSystemResource("path/to/my-template.st"); // .st 是 StringTemplate 模板文件后缀约定
    Resource resource = new ClassPathResource("prompts/my-template.st"); // 从类路径加载
    PromptTemplate fileBasedTemplate = new PromptTemplate(resource);

    // 使用方式同上
    Map<String, Object> params = Map.of("name", "Spring AI");
    Prompt promptFromFile = fileBasedTemplate.create(params);
    ```
    默认情况下，Spring AI 的 `PromptTemplate` 使用的是 [StringTemplate 4](https://www.stringtemplate.org/) 引擎。

### **三、`Prompt` 与 `PromptTemplate` 的关系**

*   `PromptTemplate` 是一个**工厂或构建器**，用于创建 `Prompt` 对象。
*   你定义一个 `PromptTemplate` 来描述提示的通用结构和可变部分。
*   在运行时，你向 `PromptTemplate` 提供具体的值，它会为你生成一个可直接发送给 AI 模型的 `Prompt` 实例。

核心关系图

```
PromptTemplate (模板层)
    ↓ create(variables)
Prompt (实例层)  
    ↓ chatClient.call()
ChatResponse (响应层)
```