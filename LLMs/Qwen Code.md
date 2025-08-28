# 掌握 Qwen Code：用户指南

Qwen Code 是一个强大的 CLI 代理，旨在帮助您高效、安全地完成软件工程任务。本指南将详细介绍如何使用 Qwen Code 的各种功能。

## 核心功能

1.  **代码编辑与生成**：可以修改现有代码或创建新文件。
2.  **文件操作**：读取、写入和搜索文件。
3.  **命令执行**：在 shell 中运行命令。
4.  **项目理解**：分析代码库结构和内容。
5.  **应用开发**：从头开始构建应用程序原型。

## 基本交互方式

与 Qwen Code 的交互主要通过自然语言进行。您可以描述您想要完成的任务，Qwen Code 会自动执行必要的步骤。

### 示例交互

- **编辑代码**：

  > "修复 `src/auth.py` 中的认证逻辑，使用 `requests` 库代替 `urllib`。"

- **生成代码**：

  > "为 `someFile.ts` 编写测试。"

- **查找文件**：

  > "找出项目中所有的 `app.config` 文件。"

- **运行命令**：

  > "启动 `server.js` 中实现的服务器。"

## 工具详解

Qwen Code 在后台使用一系列工具来执行任务。以下是一些关键工具及其用途：

### `list_directory`

列出指定目录中的文件和子目录。

-   **用途**：了解项目结构。
-   **示例**：列出 `src` 目录的内容。

### `read_file`

读取指定文件的内容。

-   **用途**：查看文件内容，理解代码逻辑。
-   **示例**：读取 `package.json` 文件。

### `search_file_content`

在文件内容中搜索正则表达式模式。

-   **用途**：快速定位特定代码或配置。
-   **示例**：搜索所有包含 `function myFunction` 的文件。

### `glob`

根据 glob 模式查找文件。

-   **用途**：批量查找特定类型的文件。
-   **示例**：查找所有 `.ts` 文件。

### `replace`

替换文件中的文本。

-   **用途**：修改代码或配置文件。
-   **示例**：将 `oldFunction` 替换为 `newFunction`。

### `write_file`

将内容写入指定文件。

-   **用途**：创建新文件或覆盖现有文件。
-   **示例**：创建一个新的 `README.md` 文件。

### `run_shell_command`

执行 shell 命令。

-   **用途**：运行构建、测试或部署命令。
-   **示例**：运行 `npm run build`。

### `web_fetch`

从 URL 获取内容并处理。

-   **用途**：获取和分析网页内容。
-   **示例**：获取 GitHub 仓库的 README。

## 最佳实践

1.  **明确任务**：尽量清晰地描述您想要完成的任务。
2.  **提供上下文**：如果任务涉及特定文件或代码，请提供相关信息。
3.  **检查结果**：在 Qwen Code 完成任务后，检查结果是否符合预期。
4.  **安全意识**：Qwen Code 会解释将要执行的命令，确保您理解其影响。

## 高级功能

### 项目分析

Qwen Code 可以自动分析项目结构，理解依赖关系和代码逻辑，从而提供更精准的帮助。

### 自动化测试

在修改代码后，Qwen Code 可以自动运行项目的测试命令，确保更改不会引入新的错误。

### 应用开发

Qwen Code 可以帮助您从零开始构建完整的应用程序，包括前端、后端和数据库。

## 总结

Qwen Code 是一个功能强大的工具，可以帮助您高效地完成各种软件工程任务。通过自然语言交互和强大的工具集，您可以专注于创造性的工作，而将重复性任务交给 Qwen Code。

# Commands & Shortcuts
### Session Commands  会话命令

- `/help` - 显示可用命令
- `/clear` - 清除对话历史记录
- `/compress` - 压缩历史记录以保存令牌
- `/stats` - 显示当前会话信息
- `/exit` 或 `/quit` - 退出 Qwen 代码

| /+         | 作用                                            |
| ---------- | --------------------------------------------- |
| about      | 显示版本信息                                        |
| auth       | 更改身份验证方法                                      |
| bug        | 提交一个错误报告                                      |
| chat       | 管理对话历史记录。                                     |
| clear      | 清除屏幕和对话记录                                     |
| compress   | 通过用摘要替换上下文来压缩上下文。                             |
| copy       | 将最后一个结果或代码片段复制到剪贴板                            |
| corgi      | Toggles corgi mode.                           |
| docs       | 在您的浏览器中打开完整的Qwen Code文档                       |
| directory  | 管理工作区目录                                       |
| editor     | 设置外部编辑器首选项                                    |
| extensions | 列出已启用的扩展程序                                    |
| help       | 关于Qwen Code的帮助                                |
| init       | 分析项目并生成定制化的QWEN.md文件。                         |
| mcp        | 列出已配置的MCP服务器和工具，或通过支持OAuth的服务器进行身份验证。         |
| memory     | 与内存交互的命令。                                     |
| privacy    | 显示隐私声明                                        |
| quit       | 退出命令行界面                                       |
| stats      | 检查会话统计信息。 Usage: /stats<br>`[model \| tools]` |
| theme      | 更改主题                                          |
| tools      | 列出可用的Qwen Codetools                           |
| vim        | 切换 Vim 模式的开启/关闭状态                             |

### Keyboard Shortcuts  键盘快捷键

- `Ctrl+C` - 取消当前操作
- `Ctrl+D` - Exit (on empty line)  
- `Up/Down` - 导航命令历史记录  






| about      | show version info                                                                    |
| ---------- | ------------------------------------------------------------------------------------ |
| auth       | change the auth method                                                               |
| bug        | submit a bug report                                                                  |
| chat       | Manage conversation history.                                                         |
| clear      | clear the screen and conversation history                                            |
| compress   | Compresses the context by replacing it with a summary.                               |
| copy       | Copy the last result or code snippet to<br>clipboard                                 |
| corgi      | Toggles corgi mode.                                                                  |
| docs       | open full Qwen Code documentation in your<br>browser                                 |
| directory  | Manage workspace directories                                                         |
| editor     | set external editor preference                                                       |
| extensions | list active extensions                                                               |
| help       | for help on Qwen Code                                                                |
| init       | Analyzes the project and creates a<br>tailored QWEN.md file.                         |
| mcp        | list configured MCP servers and tools, or<br>authenticate with OAuth-enabled servers |
| memory     | Commands for interacting with memory.                                                |
| privacy    | display the privacy notice                                                           |
| quit       | exit the cli                                                                         |
| stats      | check session stats. Usage: /stats<br>`[model \| tools]`                             |
| theme      | change the theme                                                                     |
| tools      | list available Qwen Codetools                                                        |
| vim        | toggle vim mode on/off                                                               |
