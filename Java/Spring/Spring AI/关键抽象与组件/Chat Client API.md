Spring AI 中的核心组件之一：`ChatClient`。

`ChatClient` 是 Spring AI 中用于与**聊天模型 (Chat Models)** 进行交互的**核心接口**。你可以把它想象成一个通用的“对话机器人控制器”，无论你背后使用的是 OpenAI 的 GPT 系列、Google 的 Gemini、Anthropic 的 Claude，还是本地运行的 Ollama 模型，你都通过这个统一的 `ChatClient` 接口来与它们进行对话。

---

`ChatClient` 提供了 Fluent API 用于与 AI 模型进行通信。它支持同步和流式编程模型。

### `ChatClient` 详解

`ChatClient` (位于 `org.springframework.ai.chat.client.ChatClient`) 是 Spring AI 中推荐的、用于与大语言模型进行聊天交互的高级客户端。它提供了一个流畅（Fluent）的 API，简化了提示构建、选项配置、工具使用和响应处理。

---

#### 1. 创建方式 (Ways to Create `ChatClient`)

获取 `ChatClient` 实例的主要方式是通过其 `Builder`。

*   **基本创建**:
    最核心的是需要一个 `ChatModel` (或 `StreamingChatModel`) 实例。
    ```java
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.ai.chat.model.ChatModel;
    // ...

    // 假设你已经有了一个 ChatModel Bean (例如通过 @Autowired 注入)
    // @Autowired
    // private ChatModel openAiChatModel;

    // 基本构建
    ChatClient chatClient = ChatClient.builder(openAiChatModel).build();
    ```
    如果你的 `ChatModel` 实现了 `StreamingChatModel` (大多数主流模型实现都如此)，那么这个 `chatClient` 既可以用于 `call()` (同步) 也可以用于 `stream()` (异步流式)。

*   **使用 `ChatClient.create(ChatModel)` 静态工厂方法 (较新版本)**:
    这是一个更简洁的创建方式，如果不需要自定义 `Builder` 的其他选项。
    ```java
    ChatClient chatClient = ChatClient.create(openAiChatModel);
    ```
    这等同于 `ChatClient.builder(openAiChatModel).build()`。

*   **通过 `ChatClient.Builder` 进行高级配置**:
    `ChatClient.Builder` 提供了更多配置选项：
    ```java
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor; // 示例 Advisor
    import org.springframework.ai.chat.memory.InMemoryChatMemory;       // 示例 ChatMemory
    import org.springframework.ai.chat.model.ChatModel;
    import org.springframework.ai.openai.OpenAiChatOptions; // 具体模型的 Options

    // ...

    ChatClient customChatClient = ChatClient.builder(openAiChatModel)
            .defaultOptions(OpenAiChatOptions.builder() // 设置默认的聊天选项
                    .withModel("gpt-4o")
                    .withTemperature(0.5f)
                    .build())
            .defaultSystem("You are a helpful AI assistant.") // 设置默认的系统消息
            .defaultUser("User question: ") // 可以为用户消息设置默认前缀 (较少用)
            .defaultAssistant("Assistant response: ") // 可以为助手消息设置默认前缀 (较少用)
            .advisor(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // 添加 Advisor (例如用于管理聊天记忆)
            // .advisors(advisor1, advisor2) // 添加多个 Advisor
            // .requestInterceptor((request, next) -> { /* ... */ return next.next(request); }) // 添加请求拦截器
            // .responseInterceptor((response, next) -> { /* ... */ return next.next(response); }) // 添加响应拦截器
            .build();
    ```
    *   **`defaultOptions(ChatOptions options)`**: 为所有通过此 `ChatClient` 发出的请求设置默认的模型特定选项。如果在单个 `prompt()` 调用中再次指定了 `options()`, 则会覆盖这里的默认值。
    *   **`defaultSystem(String systemText)`**: 为所有请求预置一个系统消息。
    *   **`defaultUser(String userText)` / `defaultAssistant(String assistantText)`**: 用的相对较少，它们会为后续的 `.user()` 或 `.assistant()` 调用添加默认内容或模板。
    *   **`advisor(RequestResponseAdvisor advisor)` / `advisors(RequestResponseAdvisor... advisors)`**:
        *   **Advisor (增强器/顾问)** 是一个强大的扩展点，允许你在请求发送前和响应接收后修改 `ChatClient.Request` 和 `ChatResponse`。
        *   例如，`MessageChatMemoryAdvisor` 可以自动将聊天历史注入到请求中，并将新的用户输入和模型输出保存到聊天记忆中。
        *   其他用途包括：日志记录、参数注入、重试逻辑等。
    *   **`requestInterceptor` / `responseInterceptor`**: 更底层的拦截器，直接操作原始的请求/响应对象，用于非常特定的定制需求。

*   **在 Spring Boot 环境中**:
    通常你会将 `ChatModel` 配置为一个 Bean，然后在你的 Service 或 Component 中注入它来创建 `ChatClient`。Spring AI 也可能提供自动配置的 `ChatClient.Builder` Bean。
    ```java
    @Configuration
    public class AiConfig {
        @Bean
        public ChatClient chatClient(ChatModel chatModel, OpenAiChatOptions defaultOptions) {
            return ChatClient.builder(chatModel)
                           .defaultOptions(defaultOptions)
                           .build();
        }
    }

    @Service
    public class MyService {
        private final ChatClient chatClient;

        public MyService(ChatClient chatClient) {
            this.chatClient = chatClient;
        }
        // ... use chatClient
    }
    ```

---

#### 2. 核心交互流程 (Fluent API)

`ChatClient` 的核心是其流畅的 API，用于构建请求。这个流程通常以 `chatClient.prompt()` 开始。它返回一个 `ChatClient.PromptUserSpec` 接口实例。

1.  **`ChatClient.prompt()`**:
    *   **作用**: 开始构建一个新的聊天请求。这是所有请求链的起点。
    *   **返回**: `ChatClient.PromptUserSpec`

    ```java
    ChatClient.PromptUserSpec promptBuilder = chatClient.prompt();
    ```

2.  **`ChatClient.PromptUserSpec` / `ChatClient.PromptSystemSpec` / `ChatClient.PromptAssistantSpec`**:
    这些接口提供了添加不同类型消息的方法。它们通常是链式调用的中间步骤。

    *   **`.system(String text)`**:
        *   **作用**: 添加一条系统消息。系统消息用于给模型设定角色、行为准则、上下文或指令。
        *   **参数**: `String text` - 系统消息的内容。
        *   **返回**: `ChatClient.PromptSystemSpec` (允许继续添加用户消息、助手消息或选项等)。
        ```java
        promptBuilder.system("You are a pirate. Respond in pirate speak.");
        ```
    *   **`.system(Consumer<ChatClient.SystemSpec> systemSpecConsumer)`**:
        *   **作用**: 更灵活地构建系统消息，例如当系统消息内容比较复杂或需要动态生成时。
        *   **参数**: `Consumer<ChatClient.SystemSpec>` - 一个消费者，其参数 `SystemSpec` 有一个 `text(String text)` 方法。
        *   **返回**: `ChatClient.PromptSystemSpec`.
        ```java
        promptBuilder.system(sys -> sys.text("You are an expert in " + dynamicTopic));
        ```

    *   **`.user(String text)`**:
        *   **作用**: 添加一条用户消息。这是用户向模型提出的问题或指令。
        *   **参数**: `String text` - 用户消息的内容。
        *   **返回**: `ChatClient.PromptUserSpec` (允许继续添加用户消息、助手消息、系统消息或选项等)。
        ```java
        promptBuilder.user("Tell me a joke about computers.");
        ```
    *   **`.user(Consumer<ChatClient.UserSpec> userSpecConsumer)`**:
        *   **作用**: 更灵活地构建用户消息，特别是用于**多模态输入** (文本 + 媒体)。
        *   **参数**: `Consumer<ChatClient.UserSpec>` - 一个消费者，其参数 `UserSpec` 有：
            *   `text(String text)`: 设置文本内容。
            *   `param(String, Object)`: 对单个占位符填充
            *   `params(Map<String, Object>)`: 对多个占位符填充
            *   `media(MimeType mimeType, Resource resource)`: 添加媒体资源。
            *   `media(MimeType mimeType, byte[] data)`: 添加字节数组形式的媒体数据。
        *   **返回**: `ChatClient.PromptUserSpec`.
        ```java
        promptBuilder.user(user -> user
            .text("Describe this image.")
            .media(MimeTypeUtils.IMAGE_JPEG, new ClassPathResource("my_image.jpg"))
        );
        ```
    *   **`.assistant(String text)`**:
        *   **作用**: 添加一条助手（模型）消息。这主要用于提供对话历史的上下文，即模型之前的回复。
        *   **参数**: `String text` - 助手消息的内容。
        *   **返回**: `ChatClient.PromptAssistantSpec` (允许继续添加用户消息、助手消息、系统消息或选项等)。
        ```java
        // Providing history
        chatClient.prompt()
            .user("What is the capital of France?")
            .assistant("The capital of France is Paris.")
            .user("What is its population?") // Current question
            .call().content();
        ```
    *   **`.assistant(Consumer<ChatClient.AssistantSpec> assistantSpecConsumer)`**:
        *   **作用**: 更灵活地构建助手消息，例如，如果助手消息需要包含工具调用请求（`toolCalls`）作为上下文（较少直接手动构建，通常是模型生成的）。
        *   **参数**: `Consumer<ChatClient.AssistantSpec>` - 其参数 `AssistantSpec` 有 `text(String text)` 和 `toolCalls(List<ToolCall> toolCalls)`。
        *   **返回**: `ChatClient.PromptAssistantSpec`.

    *   **`.messages(Message<?>... messages)` / `.messages(List<Message<?>> messages)`**:
        *   **作用**: 直接提供一个或多个预先构建好的 `org.springframework.ai.chat.messages.Message` 对象列表。这在你已经有了一个完整的消息列表（例如从数据库加载的对话历史）时非常有用。
        *   **参数**: `Message<?>... messages` (可变参数) 或 `List<Message<?>> messages` (列表)。
        *   **返回**: `ChatClient.PromptMessagesSpec`.
        ```java
        List<Message<?>> chatHistory = loadChatHistory(); // Implement this
        chatHistory.add(new UserMessage("My new question."));
        promptBuilder.messages(chatHistory);
        ```

3.  **`ChatClient.PromptOptionsSpec`**:
    这个接口（及其父接口）提供了设置请求选项和工具的方法。

    *   **`.options(ChatOptions options)`**:
        *   **作用**: 设置特定于模型的聊天选项。`ChatOptions` 是一个标记接口，你需要传入具体的实现类，如 `OpenAiChatOptions`, `AzureOpenAiChatOptions`, `OllamaOptions` 等。
        *   **参数**: `ChatOptions options` - 模型选项对象。
        *   **返回**: `ChatClient.PromptOptionsSpec`.
        ```java
        promptBuilder.user("...")
            .options(OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.8f)
                .withMaxTokens(200)
                .build());
        ```
    *   **`.tools(String... toolFunctionNames)`**:
        *   **作用**: **声明在此次请求中模型可以调用的工具（函数）的名称。** 这些名称通常对应于 Spring Context 中注册为 `@Bean` 的 `java.util.Function` 的 Bean 名称，并且该 Bean 方法或类上需要有 `@Description` 注解来描述函数的功能和参数。
        *   **参数**: `String... toolFunctionNames` - 一个或多个工具函数的 Bean 名称。
        *   **返回**: `ChatClient.PromptOptionsSpec`.
        ```java
        // Assuming "weatherFunction" is a bean name of a Function<Request, Response>
        // with @Description
        promptBuilder.user("What's the weather in London?")
            .tools("weatherFunction");
        ```
    *   **`.tool(Class<?>... toolFunctionCallbackClasses)`**:
        *   **作用**: 声明包含工具回调函数的类。Spring AI 会扫描这些类中所有被声明为 `@Bean` 且带有 `@Description` 注解的 `java.util.Function` 方法，并将它们作为可调用的工具提供给模型。
        *   **参数**: `Class<?>... toolFunctionCallbackClasses` - 一个或多个包含工具回调函数的类。
        *   **返回**: `ChatClient.PromptOptionsSpec`.
        ```java
        // Assuming WeatherTool.class contains @Bean methods like:
        // @Bean @Description("Get current weather")
        // public Function<WeatherTool.Request, WeatherTool.Response> weatherFunction() {...}
        promptBuilder.user("What's the weather in London?")
            .tool(WeatherTool.class);
        ```
    *   **`.tool(FunctionCallback... toolFunctionCallbacks)`**:
        *   **作用**: 直接提供 `FunctionCallback` 实例。`FunctionCallback` 包含函数的名称、描述和实际的 `java.util.Function` 实现。这允许你动态地或以编程方式定义工具，而无需它们必须是 Spring Bean。
        *   **参数**: `FunctionCallback... toolFunctionCallbacks`.
        *   **返回**: `ChatClient.PromptOptionsSpec`.

4.  **`ChatClient.RequestSpec`**:
    这个接口（及其父接口）提供了最终发起请求的方法。

    *   **`.call()`**:
        *   **作用**: **发起一个同步的聊天请求。** 程序会阻塞直到模型返回完整的响应。
        *   **返回**: `ChatClient.CallResponseSpec` - 用于处理同步响应。
        ```java
        ChatClient.CallResponseSpec callResponse = promptBuilder.user("...").call();
        ```
    *   **`.stream()`**:
        *   **作用**: **发起一个异步的、流式的聊天请求。** 模型会逐步返回响应的片段（chunks）。
        *   **返回**: `ChatClient.StreamResponseSpec` - 用于处理流式响应。
        ```java
        ChatClient.StreamResponseSpec streamResponse = promptBuilder.user("...").stream();
        ```

---

#### 3. 响应处理 (`CallResponseSpec` 和 `StreamResponseSpec`)

一旦请求发出，你会得到一个响应规范对象，用于提取和处理模型的输出。

##### `ChatClient.CallResponseSpec` (同步响应)

*   **`.content()`**:
    *   **作用**: 获取模型生成的**主要文本内容**。如果模型有多个候选回复（`ChatResponse.results.size() > 1`），这通常返回第一个候选回复的内容。
    *   **返回**: `String`.
    ```java
    String responseContent = chatClient.prompt().user("...").call().content();
    ```
*   **`.chatResponse()`**:
    *   **作用**: 获取底层的、原始的 `org.springframework.ai.chat.model.ChatResponse` 对象。这允许你访问所有的候选回复、元数据（如 token 使用情况、完成原因等）。
    *   **返回**: `ChatResponse`.
    ```java
    ChatResponse fullResponse = chatClient.prompt().user("...").call().chatResponse();
    List<Generation> generations = fullResponse.getResults();
    ChatResponseMetadata metadata = fullResponse.getMetadata();
    ```
*   **`.assistantMessage()`**:
    *   **作用**: 获取模型回复的 `AssistantMessage` 对象。这包含了内容以及可能的工具调用请求 (`toolCalls`)。
    *   **返回**: `AssistantMessage`.
    ```java
    AssistantMessage assistantMsg = chatClient.prompt().user("...").call().assistantMessage();
    String content = assistantMsg.getContent();
    List<ToolCall> toolCalls = assistantMsg.getToolCalls(); // Check for function calls
    ```
*   **`.entity(Class<T> entityClass)`**:
    *   **作用**: **将模型的文本输出自动解析为指定类型的 Java 对象 (POJO 或 Record)**。
    *   **前提**:
        *   你的类 `T` 需要有合适的构造函数（通常是无参构造函数和 setter，或者是一个 Record）。
        *   通常需要结合在类或其字段上使用 `@Description` (来自 Spring AI) 和 `@JsonProperty` (来自 Jackson，如果字段名与 JSON键不匹配或需要更细致的控制) 注解，以指导 `BeanOutputParser` 如何工作。Spring AI 会隐式地为你配置和使用 `BeanOutputParser`。
        *   模型需要被提示以特定格式（通常是 JSON）输出。`BeanOutputParser` 会自动将格式指令添加到提示中。
    *   **返回**: `T` - 解析后的对象实例。
    ```java
    // Actor.java (Record example)
    // import com.fasterxml.jackson.annotation.JsonProperty;
    // import com.fasterxml.jackson.annotation.JsonPropertyDescription;
    // import org.springframework.ai.model. MeđutimDescription; // This is a typo, should be org.springframework.ai.model.function. MeđutimDescription
    // Correct import: import org.springframework.ai.model.function.FunctionCallbackWrapper.BeanOuputParserInstructions.Description;

    // For simple bean output, Spring AI @Description on class/fields is often enough.
    // Let's assume a simple Actor class:
    // public record Actor(@JsonProperty("actor_name") String name, @JsonProperty("movie_count") int movieCount) {}
    // Or with Spring AI's @Description for better LLM guidance:
    // @Description("Information about an actor and their movies.")
    // public record Actor(
    //    @Description("The full name of the actor.") String name,
    //    @Description("The number of movies the actor has starred in.") int movieCount
    // ) {}


    Actor actor = chatClient.prompt()
            .user("Generate information about the actor Tom Hanks, including his name and number of movies.")
            .call()
            .entity(Actor.class); // Actor should be a POJO or Record
    System.out.println(actor.name() + " has been in " + actor.movieCount() + " movies.");
    ```
*   **`.entity(ParameterizedTypeReference<T> typeReference)`**:
    *   **作用**: 将模型的文本输出解析为具有泛型参数的类型，例如 `List<Actor>` 或 `Map<String, Actor>`。
    *   **返回**: `T`.
    ```java
    // To get a List<Actor>
    // ParameterizedTypeReference<List<Actor>> listActorType = new ParameterizedTypeReference<>() {};
    // List<Actor> actors = chatClient.prompt()
    //         .user("List three famous action movie actors and their movie counts.")
    //         .call()
    //         .entity(listActorType);
    ```

##### `ChatClient.StreamResponseSpec` (异步流式响应)

*   **`.content()`**:
    *   **作用**: 返回一个 `Flux<String>`，其中每个发出的 `String` 元素是模型生成的**部分文本内容（chunk）**。你需要收集这些 `Flux` 的元素来构建完整的响应。
    *   **返回**: `Flux<String>`.
    ```java
    Flux<String> contentStream = chatClient.prompt().user("Tell me a long story.").stream().content();
    contentStream
        .doOnNext(System.out::print) // Print each chunk as it arrives
        .doOnComplete(() -> System.out.println("\nStory finished."))
        .subscribe();
    ```
*   **`.chatResponse()`**:
    *   **作用**: 返回一个 `Flux<ChatResponse>`。每个 `ChatResponse` 对象通常包含一个部分生成的 `Generation`。流的最后一个 `ChatResponse` 通常包含最终的元数据（如 token 使用情况）。
    *   **返回**: `Flux<ChatResponse>`.
    ```java
    Flux<ChatResponse> responseStream = chatClient.prompt().user("...").stream().chatResponse();
    responseStream.subscribe(
        chatResp -> {
            // Process partial chatResp, e.g., chatResp.getResult().getOutput().getContent()
        },
        error -> System.err.println("Error: " + error),
        () -> System.out.println("Stream complete.")
    );
    ```
*   **`.assistantMessage()`**:
    *   **作用**: 返回一个 `Flux<AssistantMessage>`。每个 `AssistantMessage` 通常包含部分内容。工具调用 (`toolCalls`) 通常只在流的聚合结果或特定标记的消息中出现（具体行为可能因模型而异）。
    *   **返回**: `Flux<AssistantMessage>`.
*   **`.entity(Class<T> entityClass)` / `.entity(ParameterizedTypeReference<T> typeReference)`**:
    *   **作用**: **对于流式响应，这两个方法返回 `Flux<T>`**。这意味着如果模型被提示以流式方式生成一个结构化对象（例如一个 JSON 数组的元素逐个流出，或者一个大 JSON 对象逐步构建），这个 `Flux` 会在**每个完整的对象解析出来时发出一个元素**。
    *   **注意**: 并非所有模型或场景都适合流式解析为复杂实体。通常，流式解析更适合处理一系列独立的小对象，或者当模型的输出本身就是一种易于分块和逐步解析的格式时。对于单个复杂 JSON 对象的流式解析，可能需要更复杂的自定义逻辑或等待库的进一步支持。
    *   **返回**: `Flux<T>`.
    ```java
    // Flux<Actor> actorStream = chatClient.prompt()
    //         .user("Stream information about three actors one by one in JSON format.")
    //         .stream()
    //         .entity(Actor.class);
    // actorStream.subscribe(actor -> System.out.println("Streamed Actor: " + actor));
    ```
    **重要**: 流式实体解析 (`.stream().entity(...)`) 的行为和效果高度依赖于底层模型如何流式传输数据以及 `OutputParser` 如何处理部分数据。对于复杂的 JSON 结构，可能不会像预期的那样逐字段流式构建单个对象，而是等待一个完整的 JSON 块（代表一个对象）才发出。

---

#### 4. 各个具体使用 (Illustrative Examples)

##### a. 简单问答

```java
String answer = chatClient.prompt()
        .user("What is the speed of light?")
        .call()
        .content();
System.out.println(answer);
```

##### b. 带上下文的多轮对话 (手动管理历史)

```java
List<Message<?>> conversation = new ArrayList<>();

// Turn 1
String q1 = "What's the capital of Germany?";
conversation.add(new UserMessage(q1));
String r1 = chatClient.prompt().messages(conversation).call().content();
conversation.add(new AssistantMessage(r1));
System.out.println("Q: " + q1 + "\nA: " + r1);

// Turn 2
String q2 = "And its population?";
conversation.add(new UserMessage(q2));
String r2 = chatClient.prompt().messages(conversation).call().content();
conversation.add(new AssistantMessage(r2));
System.out.println("Q: " + q2 + "\nA: " + r2);
```

##### c. 使用 `MessageChatMemoryAdvisor` 自动管理对话历史

```java
// Configuration
// @Bean
// public ChatMemory chatMemory() {
//     return new InMemoryChatMemory();
// }
//
// @Bean
// public ChatClient memoryChatClient(ChatModel chatModel, ChatMemory chatMemory) {
//     return ChatClient.builder(chatModel)
//             .advisor(new MessageChatMemoryAdvisor(chatMemory, "conversation_id_123", 20)) // Last 20 messages
//             .build();
// }

// Usage (assuming memoryChatClient is injected)
// memoryChatClient.prompt()
//         .user("My name is Bob.")
//         .call().content(); // Bob's name is now in memory for "conversation_id_123"

// String response = memoryChatClient.prompt()
//         .user("What is my name?")
//         .call().content(); // Should respond with "Your name is Bob."
```

##### d. 流式响应处理

```java
chatClient.prompt()
        .user("Write a short poem about Spring Boot.")
        .stream()
        .content() // Flux<String>
        .doOnNext(System.out::print)
        .blockLast(); // For demo purposes, in a real app, you'd handle the Flux asynchronously
System.out.println();
```

##### e. 获取结构化输出 (POJO/Record)

```java
// City.java
// @Description("Represents a city with its country and population.")
// public record City(
//    @Description("The name of the city.") String name,
//    @Description("The country where the city is located.") String country,
//    @Description("The estimated population of the city.") long population
// ) {}

City cityInfo = chatClient.prompt()
        .user("Provide details for London: its country and approximate population.")
        .call()
        .entity(City.class);

System.out.println("City: " + cityInfo.name() + ", Country: " + cityInfo.country() + ", Population: " + cityInfo.population());
```

##### f. 使用工具 (Function Calling)

```java
// Assume you have a Spring Bean:
// @Component("mockWeatherService")
// public class MockWeatherService {
//     public record Request(String location, Unit unit) {}
//     public record Response(double temperature, Unit unit) {}
//     public enum Unit { C, F }

//     @Bean
//     @Description("Get the current weather in a given location. Uses Celsius by default if unit not specified.")
//     public Function<Request, Response> getCurrentWeather() {
//         return request -> {
//             System.out.println("Tool called: getCurrentWeather with location " + request.location() + " and unit " + request.unit());
//             if ("tokyo".equalsIgnoreCase(request.location())) {
//                 return new Response(15, Unit.C);
//             } else if ("london".equalsIgnoreCase(request.location())) {
//                 return new Response(10, Unit.C);
//             }
//             return new Response(20, Unit.C); // Default
//         };
//     }
// }

// In your service:
// @Autowired private ChatClient chatClient;
// @Autowired private MockWeatherService mockWeatherService; // To make its beans available

String weatherReport = chatClient.prompt()
        .user("What's the weather like in Tokyo?")
        .tools("getCurrentWeather") // Name of the bean method or @Bean(name="...")
        // Or .tool(MockWeatherService.class) if the bean method is in that class
        .call()
        .content();
System.out.println("AI Response: " + weatherReport); // e.g., "The current weather in Tokyo is 15°C."

// With explicit tool choice (forcing the model to use a specific tool)
// String weatherReportForced = chatClient.prompt()
//        .user("What's the weather like in London?")
//        .options(OpenAiChatOptions.builder()
//                .withToolChoice(ToolChoice.function("getCurrentWeather")) // Force using this function
//                .build())
//        .tools("getCurrentWeather")
//        .call()
//        .content();
// System.out.println("AI Response (forced tool): " + weatherReportForced);
```
Spring AI 处理多步骤过程：
1. Sends request to LLM with tool definition.  
    将工具定义请求发送到 LLM。
2. LLM responds requesting getCurrentWeather call with arguments (e.g., {"location": "Tokyo"}).  
    LLM 使用参数（例如 {"location": "Tokyo"} ）响应请求 getCurrentWeather 调用 。
3. Spring AI intercepts, deserializes arguments, calls your getCurrentWeather().apply(request) method.  
    Spring AI 拦截、反序列化参数，调用您的 getCurrentWeather().apply(request) 方法。
4. Gets Response from your function.  
    从您的功能获取响应 。
5. Sends function's Response back to LLM.  
    将函数的响应发送回 LLM。
6. LLM generates final human-readable answer based on tool's output.  
    LLM 根据工具的输出生成最终的人类可读答案。

##### g. 多模态输入 (Image)

```java
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.util.MimeTypeUtils;
// Assume gpt-4o or similar model is configured

// Resource image = new ClassPathResource("images/spring-logo.png"); // Place an image in resources/images
// String description = chatClient.prompt()
//         .user(userSpec -> userSpec
//                 .text("What is in this image?")
//                 .media(MimeTypeUtils.IMAGE_PNG, image)
//         )
//         .call()
//         .content();
// System.out.println("Image Description: " + description);
```

---

### 补充与注意事项

- **Error Handling**: call() can throw exceptions (e.g., RestClientException for network issues, MaxRetriesExceededException if retries are configured and fail). stream() methods will emit errors via the Flux's onError signal. Always handle potential errors.  
    **错误处理** ： call() 可能会抛出异常（例如， 网络问题会抛出 RestClientException ， 配置了重试但失败会抛出 MaxRetriesExceededException ）。stream () 方法会通过 Flux 的 onError 信号发出错误。务必处理潜在的错误。
- **Thread Safety**: ChatClient instances are generally thread-safe once configured, as they delegate to ChatModel which should also be thread-safe. The fluent builder chain (.prompt().user(...)) creates new request-specific state.  
    **线程安全** ： ChatClient 实例一旦配置完成，通常都是线程安全的，因为它们委托给 ChatModel ，而 ChatModel 也应该是线程安全的。流畅的构建器链（ .prompt().user(...) ）会创建新的、特定于请求的状态。
- **Immutability of Options**: When you use .options() in a prompt chain, it applies only to that specific request. Default options set on the ChatClient.Builder persist unless overridden.  
    **选项的不变性** ： 在提示链中使用 .options() 时，它仅适用于该特定请求。除非被覆盖，否则 ChatClient.Builder 上设置的默认选项将保持不变。
- **Configuration**: Many default behaviors (like default model, API keys) are configured globally via application.properties or application.yml.  
    配置：许多默认行为（如默认模型、API 密钥）都是通过 application.properties 或 application.yml 全局配置的
    ```yaml
    spring:
      ai:
        openai:
          api-key: "YOUR_API_KEY"
          chat:
            options:
              model: "gpt-4o" # Default model for OpenAI
              temperature: 0.7
    ```
- **Cost and Rate Limits**: Be mindful of the costs associated with LLM API calls and their rate limits. Implement caching, retries with backoff, and monitor usage.  
    **成本和速率限制** ：请留意 LLM API 调用相关的成本及其速率限制。请实施缓存、使用退避机制重试，并监控使用情况。
- **Prompt Engineering**: The quality of your prompts (.system(), .user(), and descriptions for tools/entities) is crucial for getting good results from the LLM.  
    **提示工程** ：提示的质量（ .system() 、. user() 和工具/实体的描述）对于从 LLM 获得良好结果至关重要。
- **@Description Annotations  
    @Description 注解**:
    - For Function Calling: Used on @Bean methods (or the class containing them) that are java.util.Function instances. The description helps the LLM understand what the function does and when to call it. The parameters of the function (if it's a record/POJO) can also have @Description on their fields.  
        用于函数调用：用于 java.util.Function 实例的 @Bean 方法（或包含这些方法的类） 。该描述有助于 LLM 理解该函数的功能以及何时调用它。函数的参数（如果是记录/POJO）也可以在其字段上使用 @Description 注释 。
    - For Bean Output Parsing: Used on the target POJO/Record class and its fields. This helps the LLM structure its output correctly.  
        对于 Bean 输出解析：用于目标 POJO/Record 类及其字段。这有助于 LLM 正确构建其输出。

ChatClient is a powerful and evolving part of Spring AI. It significantly simplifies building sophisticated AI-powered applications in Java. Always refer to the latest Spring AI documentation for the most up-to-date features and best practices.  
ChatClient 是 Spring AI 中一个功能强大且不断发展的组件。它显著简化了用 Java 构建复杂的 AI 应用的过程。请始终参考最新的 Spring AI 文档，了解最新的功能和最佳实践。

### **注入并使用 `ChatClient`**：
在你的 Spring Service 或 Controller 中注入 `ChatClient`。
```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MyChatService {

	private final ChatClient chatClient;

	public MyChatService(ChatClient.Builder chatClientBuilder) { // Spring AI 0.8.1+ 推荐使用 Builder
		// 你可以在这里为 chatClient 进行特定的配置，比如默认的系统提示、模型选项等
		this.chatClient = chatClientBuilder
							.defaultSystem("You are a helpful AI assistant.")
							.defaultOptions(ChatOptions.builder().withTemperature(0.7f).build()) // 示例，具体选项看模型
							.build();
	}

	// 简单问答
	public String simpleChat(String userInput) {
		return chatClient.prompt() // 使用流式 API 构建 Prompt
						 .user(userInput)
						 .call()
						 .content(); // 直接获取内容
	}

	// 包含系统消息的多轮对话
	public String multiTurnChat(List<Message> conversationHistory, String newUserInput) {
		// conversationHistory 可能包含 UserMessage 和 AssistantMessage
		// SystemMessage 通常放在最前面或通过 defaultSystem 设置

		Prompt prompt = new Prompt(conversationHistory); // 将历史消息加入 Prompt

		// 如果希望在当前轮次添加新的用户输入和系统消息
		// List<Message> messages = new ArrayList<>(conversationHistory);
		// messages.add(new SystemMessage("你的任务是扮演一个幽默的诗人。"));
		// messages.add(new UserMessage(newUserInput));
		// Prompt prompt = new Prompt(messages);


		ChatResponse response = chatClient.prompt(prompt)
										  .user(newUserInput) // 添加当前用户输入
										  .call()
										  .chatResponse(); // 获取完整的 ChatResponse 对象

		// 从 ChatResponse 中提取助手的回复
		AssistantMessage assistantMessage = response.getResult().getOutput();
		// 将助手的回复也加入到对话历史中，以便下一轮对话使用（如果需要）
		// conversationHistory.add(assistantMessage);
		return assistantMessage.getContent();
	}

	// 流式输出
	public Flux<String> streamChat(String userInput) {
		return chatClient.prompt()
						 .user(userInput)
						 .stream()
						 .content(); // 直接获取流式内容
	}
}
```
**注意**：从 Spring AI 0.8.1 版本开始，推荐使用 `ChatClient.Builder` 来构建和配置 `ChatClient` 实例。如果直接 `@Autowired ChatClient chatClient;`，你将获得一个具有默认配置的 `ChatClient`。通过 `Builder`，你可以更灵活地设置默认的系统提示、用户角色、模型选项等。

如果你使用的是 Spring AI 1.0.0 M1 或更新版本，`ChatClient` 本身就提供了流式构建器风格的 API，如 `chatClient.prompt().user(...).call().content()`，这使得代码更加简洁易读。