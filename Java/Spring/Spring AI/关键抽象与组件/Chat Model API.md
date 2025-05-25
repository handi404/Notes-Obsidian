Spring AI 中的 `ChatModel` API。这是与大语言模型（LLM）进行**对话式交互**的核心接口。

`ChatModel` API 设计的目标是提供一个统一、简洁的方式来与各种底层聊天模型（如 OpenAI 的 GPT 系列、Azure OpenAI、Google Vertex AI Gemini、Ollama 本地模型、Anthropic Claude 等）进行交互，而无需关心它们各自 SDK 的复杂性。

### 核心概念 (Core Concept)

`ChatModel` 是 Spring AI 中用于**生成基于聊天完成（Chat Completion）的文本响应**的抽象。与简单的文本补全（text completion）不同，聊天完成通常涉及到多轮对话历史（context），使得模型能够理解上下文并给出更连贯和相关的回答。

你可以把它想象成一个**通用的对话引擎接口**。

### 主要接口方法 (Key API Methods)

`ChatModel` 接口本身非常简洁，主要定义了以下几个核心方法（位于 `org.springframework.ai.chat.model.ChatModel`）：

1.  **`ChatResponse call(Prompt prompt)`**:
    *   **功能**: 这是最常用的方法，发送一个**单个提示（Prompt）**给聊天模型，并同步获取一个**聊天响应（ChatResponse）**。
    *   **输入 (`Prompt`):**
        *   包含一个 `List<Message>` 对象，代表了当前的对话历史和用户最新的输入。
        *   可以包含 `PromptOptions`，用于传递特定于模型的配置参数（如 `temperature`, `topP`, `maxTokens`, `model` 等）。
    *   **输出 (`ChatResponse`):**
        *   包含一个 `List<Generation>` 对象，每个 `Generation` 代表模型生成的一个候选回复。通常情况下，这个列表只有一个元素，但如果请求多个候选回复（例如通过设置 `n` 参数），则可能包含多个。
        *   每个 `Generation` 内部包含一个 `Message` 对象（通常是 `AssistantMessage` 类型，代表模型的回复）和可选的元数据（如 `finishReason`）。
        *   `ChatResponse` 还可能包含全局的元数据（`ChatResponseMetadata`），如 token 使用情况、请求 ID 等。
    *   **使用场景**: 单次请求-响应交互，或者当你需要完整响应后才能进行下一步操作时。

2.  **`Flux<ChatResponse> stream(Prompt prompt)`**:
    *   **功能**: 发送一个提示给聊天模型，并以**流式（Streaming）**的方式异步接收响应。这意味着模型的响应会以小块（chunks）的形式逐步返回，而不是等待整个响应生成完毕。
    *   **输入 (`Prompt`):** 与 `call` 方法相同。
    *   **输出 (`Flux<ChatResponse>`):** 这是一个 Project Reactor 的 `Flux` 对象。每个发出的 `ChatResponse` 对象通常包含部分生成的文本内容。你需要聚合这些 `ChatResponse` 中的内容来获得完整的回复。流的最后一个 `ChatResponse` 通常包含完整的元数据（如 token 使用情况）。
    *   **使用场景**:
        *   需要实时显示模型生成过程的场景，如聊天机器人界面。
        *   处理可能非常长的响应，避免长时间等待。
        *   提高用户体验，让用户感觉交互更即时。

3.  **`default ChatResponse call(String text)`**: (便利方法)
    *   **功能**: 这是一个基于 `call(Prompt prompt)` 的便利方法，允许你快速发送一个简单的字符串作为用户消息。
    *   **输入 (`String text`):** 用户的单轮输入文本。
    *   **内部实现**: 它会内部将这个 `text` 包装成一个 `UserMessage`，然后构建一个 `Prompt` 对象，再调用 `call(Prompt prompt)`。
    *   **使用场景**: 快速测试或非常简单的单轮对话。

### 核心数据结构 (Core Data Structures)

理解 `ChatModel` API 离不开以下几个核心数据结构：

1.  **`Message<T>`**: (位于 `org.springframework.messaging.Message`，Spring Framework 核心组件)
    *   Spring AI 利用了 Spring Messaging 的 `Message` 抽象。
    *   `T` 是消息的载荷 (payload)。
    *   **在 Spring AI 中，`Message` 的 payload 通常是 `String` 类型的文本内容。**
    *   它还包含 `MessageHeaders`，可以存储元数据。

2.  **Spring AI 特有的 `Message` 类型**: (位于 `org.springframework.ai.chat.messages`)
    这些类继承自 `AbstractMessage`，而 `AbstractMessage` 实现了 Spring Framework 的 `Message<String>`。
    *   **`UserMessage(String content)`**: 代表用户的输入。
    *   **`UserMessage(String text, List<Media> media)`**: (最新特性) 代表用户的多模态输入，可以包含文本和媒体数据（如图片）。
        *   `Media(MimeType mimeType, Object data)`: `data` 可以是 `Resource` 或 `byte[]`。
    *   **`AssistantMessage(String content)`**: 代表 AI 助手的回复。
    *   **`AssistantMessage(String content, Map<String, Object> properties, List<ToolCall> toolCalls)`**: (最新特性) 代表 AI 助手的回复，可以包含工具调用请求（Function Calling）。
    *   **`SystemMessage(String content)`**: 用于给模型设定行为、角色或提供全局指令。通常放在对话列表的开头。
    *   **`ToolResponseMessage(List<ToolResponse> responses)`**: (最新特性) 用于将工具（函数）执行的结果返回给模型。
        *   `ToolResponse(String id, String name, String responseData)`

3.  **`Prompt`**: (位于 `org.springframework.ai.chat.prompt.Prompt`)
    *   **`List<Message> getInstructions()`**: 获取消息列表，即对话历史和当前用户输入。
    *   **`PromptOptions getOptions()`**: 获取特定于模型的选项。

4.  **`PromptOptions`**: (位于 `org.springframework.ai.chat.prompt.PromptOptions`)
    *   这是一个标记接口，具体的实现由各个模型提供者定义，例如：
        *   `OpenAiChatOptions`: 包含 `model`, `temperature`, `topP`, `maxTokens`, `presencePenalty`, `frequencyPenalty`, `stop`, `functions` (用于 Function Calling), `tools`, `toolChoice` 等。
        *   `AzureOpenAiChatOptions`: 类似 OpenAI。
        *   `OllamaOptions`: 包含 `model`, `temperature` 等。
    *   通常通过 `builder` 模式创建，例如 `OpenAiChatOptions.builder().withModel("gpt-4o").withTemperature(0.7f).build()`。

5.  **`ChatResponse`**: (位于 `org.springframework.ai.chat.model.ChatResponse`)
    *   **`List<Generation> getResults()`**: 获取模型生成的多个结果/候选。
    *   **`ChatResponseMetadata getMetadata()`**: 获取响应的元数据，如 `Usage` (token 统计), `RateLimit` 等。

6.  **`Generation`**: (位于 `org.springframework.ai.chat.model.Generation`)
    *   **`Message getOutput()`**: 获取模型生成的具体消息内容（通常是一个 `AssistantMessage`）。
    *   **`GenerationMetadata getMetadata()`**: 获取此 `Generation` 的元数据，如 `finishReason`。

### 如何使用 (How to Use - 简要示例)

假设你已经配置好了 OpenAI 的 `ChatModel` Bean。

```java
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions; // 具体模型的 Options
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyChatService {

    private final ChatModel chatModel;

    public MyChatService(ChatModel chatModel) { // 依赖注入 ChatModel
        this.chatModel = chatModel;
    }

    // 简单单轮对话
    public String simpleChat(String userInput) {
        // 使用便利方法
        // ChatResponse response = chatModel.call(userInput);
        // return response.getResult().getOutput().getContent();

        // 或者标准方式
        UserMessage userMessage = new UserMessage(userInput);
        Prompt prompt = new Prompt(userMessage);
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    // 带上下文的多轮对话
    public String multiTurnChat(List<Message> conversationHistory, String newUserInput) {
        List<Message> messages = new ArrayList<>(conversationHistory);
        messages.add(new UserMessage(newUserInput));

        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);

        // 更新对话历史
        // conversationHistory.add(new UserMessage(newUserInput)); // 实际应用中，这里的 conversationHistory 应由调用者管理
        // conversationHistory.add(response.getResult().getOutput());

        return response.getResult().getOutput().getContent();
    }

    // 使用特定模型选项 (如温度)
    public String chatWithOptions(String userInput) {
        UserMessage userMessage = new UserMessage(userInput);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel("gpt-4o") // 可以指定模型，如果默认配置不是这个
                .withTemperature(0.7f)
                .withMaxTokens(150)
                .build();

        Prompt prompt = new Prompt(userMessage, options);
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    // 流式响应
    public void streamChat(String userInput) {
        UserMessage userMessage = new UserMessage(userInput);
        Prompt prompt = new Prompt(userMessage);

        chatModel.stream(prompt)
            .flatMap(chatResponse -> Flux.fromIterable(chatResponse.getResults()))
            .map(generation -> generation.getOutput().getContent())
            .doOnNext(System.out::print) // 实时打印每个 token/chunk
            .doOnError(error -> System.err.println("Error streaming: " + error.getMessage()))
            .doOnComplete(() -> System.out.println("\nStreaming complete."))
            .subscribe(); // 实际应用中会传递给客户端，例如通过 SSE
    }

    // 多模态示例 (需要模型支持，如 gpt-4o)
    public String describeImage(String textPrompt, org.springframework.core.io.Resource imageResource) {
        var userMessage = new UserMessage(
            textPrompt,
            List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageResource)) // 假设是 PNG 图片
        );
        Prompt prompt = new Prompt(List.of(userMessage), OpenAiChatOptions.builder()
            .withModel("gpt-4o") // 确保模型支持图像
            .build());
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getContent();
    }
}
```

### 关键特性与扩展 (Key Features and Extensions)

1.  **统一抽象**: 开发者使用一套 API 与不同 LLM 服务商交互。更换底层模型通常只需要修改配置和少量特定选项代码。
2.  **配置驱动**:
    *   通过 `application.properties` 或 `application.yml` 配置 API Key、模型名称、默认选项等。
    *   例如:
        ```yaml
        spring:
          ai:
            openai:
              api-key: ${OPENAI_API_KEY}
              chat:
                options:
                  model: gpt-3.5-turbo
                  temperature: 0.7
        ```
3.  **Prompt Templating**: 虽然不是 `ChatModel` API 的一部分，但 `PromptTemplate` (位于 `org.springframework.ai.chat.prompt.PromptTemplate`) 是构建 `Prompt` 对象的重要辅助工具，它允许你使用占位符动态生成消息内容。
    ```java
    String templateString = "告诉我关于{topic}的三个笑话。";
    PromptTemplate promptTemplate = new PromptTemplate(templateString);
    Prompt prompt = promptTemplate.create(Map.of("topic", "猫"));
    // 然后 chatModel.call(prompt);
    ```
4.  **Output Parsers**: (例如 `BeanOutputParser`, `MapOutputParser`, `ListOutputParser`) 用于将模型的文本输出转换为结构化的 Java 对象，使得处理 LLM 的响应更加方便。通常与 `PromptTemplate` 结合使用。
5.  **Function Calling / Tool Usage**: (最新且重要)
    *   允许模型请求调用外部工具或函数来获取额外信息或执行操作。
    *   **流程**:
        1.  在 `PromptOptions` (如 `OpenAiChatOptions`) 中定义可用的函数/工具（名称、描述、参数模式）。
        2.  模型如果认为需要调用函数，会在 `AssistantMessage` 的 `toolCalls` 字段中返回一个或多个 `ToolCall` 请求。
        3.  你的应用代码检查 `toolCalls`，如果存在：
            a.  根据 `ToolCall` 中的函数名和参数，执行相应的 Java 方法。
            b.  将执行结果构造成 `ToolResponseMessage`。
            c.  将这个 `ToolResponseMessage`（连同之前的对话历史和模型的函数调用请求消息）再次发送给模型。
        4.  模型利用函数返回的结果生成最终的用户可见的回复。
    *   Spring AI 提供了 `@Bean` 声明的函数自动注册和调用的机制，简化了这一流程。通常使用 `ChatClient.builder(chatModel).build().prompt().tools(...)` 等方式进行更高级的封装。
6.  **多模态输入**: (最新进展)
    *   如上例所示，`UserMessage` 现在可以包含 `List<Media>`，允许发送图像等多媒体数据给支持多模态输入的模型（如 `gpt-4o`, `gemini-1.5-pro`）。
    *   Spring AI 负责将 `Resource` 或 `byte[]` 形式的媒体数据正确编码并传递给模型。

### 应用场景 (Application Scenarios)

`ChatModel` API 是构建以下类型应用的基础：

*   **智能聊天机器人 (Chatbots)**: 提供客服、问答、闲聊等功能。
*   **内容生成 (Content Generation)**: 撰写文章、邮件、代码、广告词等。
*   **文本摘要 (Summarization)**: 从长文本中提取核心内容。
*   **问答系统 (Q&A Systems)**: 基于提供的上下文或通用知识回答问题。
*   **代码助手 (Coding Assistant)**: 生成代码、解释代码、调试建议。
*   **需要与外部系统交互的智能体 (AI Agents)**: 通过 Function Calling/Tool Usage 与 API、数据库等交互。

### 总结 (Summary)

Spring AI 的 `ChatModel` API 提供了一个强大而灵活的抽象层，用于与各种大语言模型进行对话式交互。它的核心优势在于**统一性、简洁性、可扩展性以及与 Spring 生态的无缝集成**。通过 `Prompt`、`Message` 等核心数据结构，以及 `call` 和 `stream` 方法，开发者可以轻松实现从简单问答到复杂的多轮对话、流式响应乃至高级的 Function Calling 和多模态交互。

随着 Spring AI 的不断发展，`ChatModel` API 及其周边生态（如 Prompt Templating, Output Parsers, Function Calling 支持）会持续增强，为 Java 开发者构建下一代 AI 应用提供坚实的基础。始终关注 Spring AI 的官方文档和 GitHub 仓库以获取最新信息。