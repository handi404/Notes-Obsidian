[Structured Output Converter](https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html#_using_converters) (结构化输出转换器)：
## 可用的转换器
AbstractConversionServiceOutputConverter
AbstractMessageOutputConverter
BeanOutputConverter
ListOutputConverter
MapOutputConverter

## 使用转换器
如何使用可用的转换器生成结构化输出

## 内置 JSON 模式
一些 AI 模型提供专用的配置选项来生成结构化（通常是 JSON）输出

---

当与 LLM 交互时，它们默认输出的是非结构化文本。但在应用程序中，我们通常需要将这些文本转换为 Java 对象（POJOs）、列表（Lists）或映射（Maps）等结构化数据，以便于后续处理、存储或展示。Spring AI 的 `OutputParser` 接口及其实现类就是为此而生的。

## 核心概念：`OutputParser<T>`

`OutputParser<T>` 是一个泛型接口，它的核心职责是将 LLM 返回的 `String` 类型的响应内容转换为指定的 Java 类型 `T`。

它通常有两个关键方法：

1.  `getFormat()`: 返回一个字符串，描述了期望 LLM 输出的格式。这个格式描述通常会作为指令添加到你的提示（Prompt）中，引导 LLM 生成符合要求的输出。
2.  `parse(String text)`: 将 LLM 返回的文本内容解析为你期望的 Java 对象 `T`。

## 可用的转换器 (Parsers)

你列出的转换器中，我们主要关注实际应用中最常用的几个具体实现：

1.  **`BeanOutputParser<T>`**:
    *   **用途**: 将 LLM 的输出直接转换为一个 Java Bean (POJO) 对象。
    *   **工作原理**:
        *   它通过反射分析你提供的 Java Bean 类的结构（字段名、类型）。
        *   `getFormat()` 方法会生成一段指令，告诉 LLM 应该以怎样的 JSON 格式返回数据，这个 JSON 的键应该对应 Bean 的属性名。
        *   `parse()` 方法使用 Jackson (默认) 或其他 JSON 库将 LLM 返回的 JSON 字符串反序列化为你的 Bean 对象。
    *   **特别注意**: 如果你期望得到一个 Bean 的列表，例如 `List<MyBean>`，你可以通过构造 `BeanOutputParser` 时传入 `ParameterizedTypeReference<List<MyBean>>` 来实现。LLM 需要被指示返回一个 JSON 数组，其中每个元素都符合 `MyBean` 的结构。

2.  **`ListOutputParser`**:
    *   **用途**: 将 LLM 的输出转换为一个 `List<String>`。每一项通常是 LLM 生成的列表中的一个元素。
    *   **工作原理**:
        *   `getFormat()` 方法会生成简单的指令，例如告诉 LLM 以逗号分隔列表（或每行一个项，具体看其内部实现和版本）。
        *   `parse()` 方法根据预期的分隔符（默认是 `\n` 换行符，可以通过 `ListOutputParser(Converter<String, List<String>> converter)` 构造函数传入自定义的 `Converter` 来改变分割逻辑）将文本分割成字符串列表。
    *   **注意**: 它主要用于简单的字符串列表。如果你需要 `List<Integer>` 或其他类型，你可能需要在此基础上进行二次转换，或者确保 LLM 输出的字符串能被正确转换。

3.  **`MapOutputParser`**:
    *   **用途**: 将 LLM 的输出转换为一个 `Map<String, Object>`。
    *   **工作原理**:
        *   `getFormat()` 方法会指示 LLM 输出一个 JSON 对象。
        *   `parse()` 方法将 LLM 返回的 JSON 字符串反序列化为一个 Map。
    *   **注意**: 对于嵌套的 JSON 对象，值可能是另一个 Map 或 List。

至于 `AbstractConversionServiceOutputConverter` 和 `AbstractMessageOutputConverter`，它们是更底层的抽象类，开发者通常不直接使用它们，而是使用上述更具体的实现。

“List BeanOutputConverter” 并不是一个单一的类名。它描述的是一种需求：获取一个 Bean 对象的列表。这通常通过以下方式实现：
*   使用 `BeanOutputParser<List<MyBean>>` (通过 `ParameterizedTypeReference`)，并指示 LLM 返回一个 JSON 数组，数组中的每个元素都符合 `MyBean` 的结构。
*   或者，如果你的 Bean 结构非常简单，且可以被 `ListOutputParser` 处理后，再手动映射到 Bean 列表（但这不常见，也不如前者直接）。

## 如何使用可用的转换器生成结构化输出

使用转换器的基本流程如下：

1.  **选择并实例化一个 `OutputParser`**: 根据你期望的输出类型选择合适的 Parser。
2.  **获取格式指令**: 调用 `parser.getFormat()` 获取格式说明。
3.  **构建提示 (Prompt)**:
    *   将你的原始问题或指令与 `parser.getFormat()` 返回的格式说明结合起来。这通常通过 `PromptTemplate` 完成。
    *   明确指示 LLM 遵循提供的格式。
4.  **发送请求给 `ChatClient`**: 将构建好的 `Prompt` 发送给 `ChatClient`。
5.  **解析响应**: 从 `ChatResponse` 中获取 `Generation`，然后调用 `parser.parse(generation.getOutput().getContent())` 来得到结构化的 Java 对象。

**示例：使用 `BeanOutputParser`**

假设我们有一个 POJO：

```java
// Actor.java
public class Actor {
    private String actorName;
    private int moviesCount;

    // Getters and Setters
    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }
    public int getMoviesCount() { return moviesCount; }
    public void setMoviesCount(int moviesCount) { this.moviesCount = moviesCount; }

    @Override
    public String toString() {
        return "Actor{" +
               "actorName='" + actorName + '\'' +
               ", moviesCount=" + moviesCount +
               '}';
    }
}
```

使用 `BeanOutputParser`：

```java
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.ListOutputParser;
import org.springframework.ai.parser.MapOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Map;

@ShellComponent
public class StructuredOutputCommands {

    private final ChatClient chatClient;

    @Autowired
    public StructuredOutputCommands(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @ShellMethod("Get actor details as a Bean")
    public Actor getActorDetails(String actorFullName) {
        BeanOutputParser<Actor> parser = new BeanOutputParser<>(Actor.class);

        String format = parser.getFormat();
        String userMessage = """
                Generate details for the actor {actor}.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage);
        Prompt prompt = promptTemplate.create(Map.of("actor", actorFullName, "format", format));

        ChatResponse response = chatClient.call(prompt);
        String rawContent = response.getResult().getOutput().getContent();
        System.out.println("Raw LLM Output:\n" + rawContent); // 打印原始输出以供调试

        return parser.parse(rawContent);
    }

    @ShellMethod("Get a list of famous actors for a movie as List<String>")
    public List<String> getActorsForMovie(String movie) {
        // ListOutputParser 默认使用 DefaultConversionService，它能处理 String 到 List<String>
        // 对于更复杂的分割，可以自定义 Converter
        ListOutputParser parser = new ListOutputParser(new DefaultConversionService());


        String format = parser.getFormat(); // 通常会提示LLM用逗号或换行符分隔
        String userMessage = """
                What actors are in the movie {movie}?
                {format}
                """;
        // 你也可以直接在提示中强化格式要求，例如：
        // "Return your answer as a comma separated list."
        // "Return your answer as a list, with each item on a new line."


        PromptTemplate promptTemplate = new PromptTemplate(userMessage);
        Prompt prompt = promptTemplate.create(Map.of("movie", movie, "format", format));
        // 有些模型对于ListOutputParser的format指令理解不佳，直接在prompt中指明格式可能更有效
        // Prompt prompt = new Prompt("What actors are in the movie " + movie + "? Your answer should be a comma separated list.");

        ChatResponse response = chatClient.call(prompt);
        String rawContent = response.getResult().getOutput().getContent();
        System.out.println("Raw LLM Output:\n" + rawContent);

        return parser.parse(rawContent);
    }

    @ShellMethod("Get movie details as a Map")
    public Map<String, Object> getMovieDetailsAsMap(String movie) {
        MapOutputParser parser = new MapOutputParser();

        String format = parser.getFormat();
        String userMessage = """
                Provide details for the movie {movie} including director, year, and genre.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage);
        Prompt prompt = promptTemplate.create(Map.of("movie", movie, "format", format));

        ChatResponse response = chatClient.call(prompt);
        String rawContent = response.getResult().getOutput().getContent();
        System.out.println("Raw LLM Output:\n" + rawContent);

        return parser.parse(rawContent);
    }

    @ShellMethod("Get a list of actors (beans) for a movie")
    public List<ActorShortInfo> getActorListForMovie(String movie) {
        // 使用 ParameterizedTypeReference 来指定 List<ActorShortInfo>
        var parser = new BeanOutputParser<>(new org.springframework.core.ParameterizedTypeReference<List<ActorShortInfo>>() {});

        String format = parser.getFormat();
        // 确保提示LLM返回一个JSON数组，每个元素符合ActorShortInfo结构
        String userMessage = """
                Generate a list of main actors for the movie {movie}.
                Provide only their names.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage);
        Prompt prompt = promptTemplate.create(Map.of("movie", movie, "format", format));

        ChatResponse response = chatClient.call(prompt);
        String rawContent = response.getResult().getOutput().getContent();
        System.out.println("Raw LLM Output for List<Bean>:\n" + rawContent);

        return parser.parse(rawContent);
    }
}

// ActorShortInfo.java (用于 List<Bean> 示例)
class ActorShortInfo {
    private String actorName;

    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }

    @Override
    public String toString() {
        return "ActorShortInfo{actorName='" + actorName + "'}";
    }
}
```

**关键点**：

*   **提示工程 (Prompt Engineering)**: 即使使用了 `OutputParser`，你仍然需要在提示中清晰地指示 LLM。`parser.getFormat()` 提供了基础，但你可能需要进一步细化指令。
*   **模型能力**: 不同模型遵循格式指令的能力不同。有些模型可能需要更明确或重复的指令。
*   **调试**: 打印 LLM 的原始输出 (`generation.getOutput().getContent()`) 对于调试解析问题非常有用。你可以看到 LLM 到底返回了什么，以及为什么解析器可能失败。

## 内置 JSON 模式 (Built-in JSON Mode)

一些先进的 AI 模型（如 OpenAI 的 GPT-4-turbo, GPT-3.5-turbo-1106 及更新版本）提供了**内置的 JSON 输出模式**。

*   **是什么**: 当启用此模式时，模型会受到约束，其输出**必须**是语法上有效的 JSON 对象。这通常比仅仅通过提示词引导模型生成 JSON 更可靠。
*   **如何使用**: 你可以在调用 `ChatClient` 时，通过特定于模型的 `ChatOptions` 来启用它。
    *   对于 OpenAI，你可以使用 `OpenAiChatOptions`：

    ```java
    import org.springframework.ai.openai.OpenAiChatClient;
    import org.springframework.ai.openai.OpenAiChatOptions;
    import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
    // ...

    // 注入 OpenAiChatClient 而不是通用的 ChatClient
    // @Autowired
    // private OpenAiChatClient openAiChatClient;

    public Actor getActorDetailsWithJsonMode(String actorFullName, OpenAiChatClient openAiChatClient) { // 假设 openAiChatClient 已注入
        BeanOutputParser<Actor> parser = new BeanOutputParser<>(Actor.class);

        // 提示中仍然需要格式说明，JSON mode 保证了输出是 JSON，但内容结构还是需要引导
        String promptInstruction = """
                Generate details for the actor {actor}.
                Ensure the output strictly follows this JSON schema:
                {format}
                Provide only the JSON object.
                """;
        PromptTemplate promptTemplate = new PromptTemplate(promptInstruction);
        Prompt prompt = promptTemplate.create(Map.of(
                "actor", actorFullName,
                "format", parser.getFormat() // parser.getFormat() 仍然有用，它描述了JSON的结构
        ));

        // 启用 JSON Mode
        ChatResponse response = openAiChatClient.call(new Prompt(
            prompt.getContents(), // 获取 Prompt 实例中的内容
            OpenAiChatOptions.builder()
                .withModel("gpt-4-turbo-preview") // 或其他支持 JSON mode 的模型
                .withResponseFormat(new ResponseFormat("json_object")) // 关键！
                .build()
        ));

        String rawContent = response.getResult().getOutput().getContent();
        System.out.println("Raw LLM Output (JSON Mode):\n" + rawContent);

        return parser.parse(rawContent); // 仍然使用 parser 来将 JSON 字符串转换为 Bean
    }
    ```

*   **优点**:
    *   **可靠性更高**: 大大降低了 LLM 输出无效 JSON 的风险。
    *   **简化提示**: 你可能不需要在提示中花费那么多精力来强调 JSON 格式的正确性（但仍需描述 JSON 的*结构*）。
*   **与 `OutputParser` 的关系**:
    *   即使使用了模型的 JSON 模式，`OutputParser`（尤其是 `BeanOutputParser`）**仍然非常有用**。
    *   JSON 模式确保 LLM 返回的是一个有效的 JSON *字符串*。
    *   `OutputParser` 负责将这个 JSON *字符串*反序列化为你的 Java 对象。
    *   `parser.getFormat()` 依然重要，因为它告诉模型期望的 JSON *结构*是什么样的（比如哪些键，什么类型）。

**总结与扩展应用：**

1.  **优先选择**: 如果模型支持可靠的 JSON 模式，并且你的目标是 JSON 输出，那么**组合使用模型的 JSON 模式和 Spring AI 的 `BeanOutputParser` (或其他 JSON 解析器)** 是目前最稳健的方法。
2.  **提示是关键**: 无论哪种方式，清晰、明确的提示对于获得期望的结构化输出至关重要。`OutputParser.getFormat()` 为你提供了一个起点。
3.  **错误处理**: 实际应用中，即使有 JSON 模式，解析也可能因为内容不符合预期 Bean 结构等原因失败。你需要准备好处理 `OutputParser` 可能抛出的异常（例如 `JsonParseException`）。
4.  **Function Calling/Tool Usage**: 这是另一种获取结构化输出的强大方式，尤其当你需要 LLM 返回符合特定函数签名的参数时。模型可以直接输出一个 JSON 对象，其结构匹配你定义的 Java 方法的参数。Spring AI 对此也有很好的支持。
5.  **迭代开发**: 获取完美的结构化输出往往需要多次迭代调整你的提示和所使用的 `OutputParser` 或模型配置。

通过这些结构化输出转换器和模型特性，Spring AI 使得从 LLM 获取并在 Java 应用中使用结构化数据变得更加简单和标准化。