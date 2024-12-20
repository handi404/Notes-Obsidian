`ng` 是 Angular CLI（命令行界面）的命令前缀，用于执行各种与 Angular 项目相关的任务。Angular CLI 是一个官方提供的工具，它简化了 Angular 应用程序的创建、管理和开发过程。通过使用 `ng` 命令，开发者可以快速搭建项目结构、生成代码、运行开发服务器、构建应用程序以及执行其他实用操作。

以下是一些常用的 `ng` 命令及其功能：

- **`ng new <project-name>`**：创建一个新的 Angular 项目。这将设置好所有必要的配置文件和初始项目结构。
  
- **`ng serve` 或 `ng serve --open`**：启动开发服务器，并在浏览器中打开应用程序。`--open` 参数会自动打开默认浏览器。
  
- **`ng build`**：编译 Angular 应用程序到输出（dist/）目录。你可以添加 `--prod` 标志来优化生产环境下的构建。
  
- **`ng test`**：运行单元测试。这通常使用 Karma 测试运行器来执行。
  
- **`ng e2e`**：运行端到端测试。这通常使用 Protractor 来执行。
  
- **`ng generate <schematic>` 或 `ng g <schematic>`**：根据指定的 schematic 模板生成或修改文件。例如：
  - `ng generate component my-new-component` 或 `ng g c my-new-component`：生成一个新的组件。
  - `ng generate service my-new-service` 或 `ng g s my-new-service`：生成一个新的服务。
  - `ng generate module my-new-module` 或 `ng g m my-new-module`：生成一个新的模块。
  
- **`ng add <package>`**：安装并集成新的库到现有项目中，同时应用任何必要的架构更改。
  
- **`ng update`**：更新 Angular CLI 和你的项目依赖项。它可以智能地处理版本兼容性问题。

这些命令可以帮助你更高效地开发和维护 Angular 应用程序。如果你刚开始接触 Angular，熟悉这些命令将大大加快你的开发速度并提高工作效率。