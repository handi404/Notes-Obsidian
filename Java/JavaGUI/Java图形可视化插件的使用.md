# JFormDesigner

JFormDesigner 是一款用于设计和创建图形用户界面（GUI）的插件，它允许开发者使用可视化界面设计器轻松创建 Swing 和 AWT 界面。
本文将介绍在 IntelliJ IDEA 中使用 JFormDesigner 的简单步骤以及使用教程，同时用一个简单的登录界面案例展示IDEA中JFormDesigner的使用。

## 1 安装 JFormDesigner 插件

1. 打开 IntelliJ IDEA ，点击File→Settings，打开软件设置：

![image-20240606182142266](C:\Users\86166\Desktop\Obsidian Vault\Java\JavaGUI\img\image-20240606182142266.png)

2. 在Settings中选择Plugins，在右侧的搜索栏输入插件名JFormDesigner进行搜索，选择第一个插件，点击install进行下载安装，下载完成后会提示需重启IDEA：

![image-20240606182236416](D:\云和文档\实训项目\img\image-20240606182236416.png)

3. 重启IDEA：

![image-20240606182302678](D:\云和文档\实训项目\img\image-20240606182302678.png)

4. 重启IDEA后，点击Filey→Manage IDE Settings→Import Settings，复制本地IDEA的下载路径并在文件管理器中打开：

![image-20240606182330023](D:\云和文档\实训项目\img\image-20240606182330023.png)

![image-20240606182350097](D:\云和文档\实训项目\img\image-20240606182350097.png)

5. 在文件管理器中进入上面的导入设置路径后，依次点击plugins→JFormDesigner→lib：

![image-20240606182416015](D:\云和文档\实训项目\img\image-20240606182416015.png)

找到JFormDesigner-Idea.jar，此处需复制JFormDesigner-Idea.jar所在路径（注册机需要用到）：

![image-20240606182443754](D:\云和文档\实训项目\img\image-20240606182443754.png)

6. !!关闭IDEA!!
   准备开始注册JFormDesigner插件。

7. 下载注册机：注册机下载地址

   链接：https://pan.baidu.com/s/1Rb1EMva5HIYbyBcYgCxIsw
   提取码：6666

8. （退出电脑杀毒软件）双击注册机打开，选择产品名称，点击Patch。

   ![image-20240606182729253](D:\云和文档\实训项目\img\image-20240606182729253.png)

9. 选择第5步复制的JFormDesigner-Idea.jar所在路径，点击打开：

![image-20240606182803626](D:\云和文档\实训项目\img\image-20240606182803626.png)

注册机提示匹配成功：

![image-20240606182832399](D:\云和文档\实训项目\img\image-20240606182832399.png)

在注册机中点击Generate，生成密钥：

![image-20240606182900031](D:\云和文档\实训项目\img\image-20240606182900031.png)

保存该密钥到任意位置：

![image-20240606182920122](D:\云和文档\实训项目\img\image-20240606182920122.png)

点击Exit退出虚拟机，打开IDEA，点击File→：Settings，找到JFormDesigner，点击Register进行注册：

![image-20240606182939663](D:\云和文档\实训项目\img\image-20240606182939663.png)

点击License File，并找到之前生成的密钥文件（或者打开密钥文件复制到文本框中）：

![image-20240606183011753](D:\云和文档\实训项目\img\image-20240606183011753.png)

![image-20240606183033864](D:\云和文档\实训项目\img\image-20240606183033864.png)

12. 接下来就可以畅快地使用JFormDesigner设计界面了~

    ![image-20240606183109663](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20240606183109663.png)

## 2 JFormDesigner 使用教程

### 2.1 新建JFormDesigner Form时的选项

当在IDEA中新建一个项目后，选择src新建JFormDesigner Form时，有多个选项可供选择，如下图所示：

![image-20240606183248513](D:\云和文档\实训项目\img\image-20240606183248513.png)

#### 1. **Name**（名称）：该Form的名称。

#### 2.**SuperClass**（超类）：该 Form 继承的类。

​	**JPanel**： 一个轻量级容器，通常用于包含和组织其他 GUI 组件。它没有标题栏或边框，常用于将组件分组或创建复杂的界面。
​	**JDialog**：一个弹出式对话框窗口，通常用于显示与主窗口相关的临时信息、接收用户输入或执行特定任务。它可以有标题栏和可关闭按钮。
​	**JFrame**：顶层窗口，用于创建主应用程序窗口。它具有标题栏、最大化和最小化按钮，通常包含应用程序的主要界面。
​	**other**：允许指定任何其他的超类。

#### 3.**Button bar**（按钮栏）：

​		是否在 Form 的底部自动生成按钮栏，用于执行特定的操作或响应用户的输入。
​	**OK / Cancel**（确定/取消）：同时生成OK和Cancel两个按钮。Cancel用于取消当前的操作或关闭对话框而不保存任何更改。如果用户	不希望应用先前所做的更改，可以点击"Cancel"按钮。这通常用于撤销用户可能已经进行的修改。
​	**OK**（确定）： 通常用于确认用户的选择或输入。当用户完成了某个操作或在对话框中进行了设置，并希望将其应用时，可以点"OK"按	钮。这会触发确认操作，将用户所做的更改保存并关闭对话框。
​	**none**（无）：一个不执行任何操作的按钮，或者表示不选择任何选项。在某些上下文中，可能会使用"None"作为某个设置的默认选项，表示用户选择不设置任何特定的值。
​	Help（帮助）：提供用户帮助和支持的按钮。点击"Help"按钮通常会打开相关的帮助文档或显示有关当前上下文的信息，以便用户了解如何使用应用程序或解决问题。

#### **4.Layout manager**（布局管理器）：

BorderLayout： 将组件放置在边界（North、South、East、West、Center）位置，适用于简单的布局需求。
BoxLayout：水平或垂直排列组件，可以是盒式布局（水平或垂直排列）或流式布局（按添加顺序排列）。
CardLayout：允许在同一容器中切换多个组件，只显示其中一个。适用于需要在不同视图间切换的场景，如向导式界面。
FlowLayout：按照添加顺序在行或列上排列组件，适用于简单的流式布局。
FormLayout（JGoodies）：提供了更高级的表单布局，支持在表格中精确控制组件的位置和大小。
GridBagLayout：强大的布局管理器，可以在网格中精确控制组件的位置和大小。
GridLayout： 将组件放置在矩形的网格中，所有的单元格大小相等。适用于简单的网格布局。
GroupLayout（Free Design）： GroupLayout 是 GroupLayout 提供的一种布局方式，可实现复杂的布局。
HorizontalLayout（SwingX）：SwingX 库提供的水平布局，用于水平排列组件。
IntelliJ IDEA GridLayout：IntelliJ IDEA 特有的网格布局，与标准 GridLayout 有一些差异。
MigLayout： 强大而灵活的布局管理器，可以应对各种复杂的布局需求。
null Layout：不使用任何布局管理器，组件的位置和大小需要手动设置。
TableLayout：使用表格形式排列组件，支持相对和绝对定位。
VerticalLayout：SwingX 库提供的垂直布局，用于垂直排列组件。

#### 5.Store strings in resource bundle (properties file)：

用于启用或禁用本地化支持。启用本地化支持后，JFormDesigner 将为每个组件生成一个 .properties 文件，用于存储本地化文本。这对于多语言应用程序是非常有用的，因为它允许你轻松地将应用程序的文本翻译成不同的语言。

Resource Bundle Name（资源包名称）： 资源包是包含本地化信息的文件，其中包括文本字符串、图像和其他本地化资源。资源包名称是指用于标识和加载这些资源包的名称。在 Java 中，这通常是一个属性文件，例如 messages.properties。通过使用不同的资源包名称，开发者可以为不同的语言或地区提供相应的本地化资源。
例如，在 Java 中，可以使用 ResourceBundle.getBundle("messages", locale) 来获取与特定区域设置（locale）相关的资源包。这里的 “messages” 就是资源包名称。
Prefix for Generated Keys（生成的键的前缀）： 在本地化资源文件中，每个本地化字符串都与一个唯一的键相关联。生成这些键时，有时会使用前缀来提供更多的上下文信息或防止冲突。
例如，如果有一个按钮的标签需要本地化，可以使用键值对，其中键可能是 “button.ok” 或 “button.cancel”。在这里，“button” 就是生成的键的前缀，有助于组织和区分不同部分的本地化字符串。
Auto-externalize strings：是否自动将应用程序中的字符串提取出来，以便更容易进行本地化。

### 2.2 JFormDesigner Form界面布局

### 2.3 JFormDesigner 组件

![image-20240606183737515](D:\云和文档\实训项目\img\image-20240606183737515.png)

#### 2.3.1 Components基本组件

JLabel（标签）：非可编辑文本组件，用于显示单行文本或图像。它通常用于标识其他组件或提供有关其他组件的信息。
JTextField（文本输入框）：允许用户输入和编辑单行文本。它常用于接受用户输入，如用户名、密码等。
JComboBox（下拉列表）：提供一个下拉菜单，用户可以从中选择一个选项。适用于需要用户在预定义选项中进行选择的场景。
JButton（按钮）：在用户点击时触发动作的按钮。用户可以点击按钮来执行与按钮关联的操作，例如提交表单、保存文件等。
JCheckBox（复选框）：允许用户选择或取消选择一个或多个选项，适用于开启或关闭特定功能或选项。
JRadioButton（单选按钮）：允许用户从一组互斥选项中选择一个，常用于需要用户在几个相关选项中进行单一选择的情况。
JToggleButton（切换按钮）：可以在按下和未按下状态之间切换的按钮，适用于需要用户切换两种状态的场景。
JTextArea（多行文本输入区）：允许用户输入和编辑多行文本，通常用于需要用户输入大段文本的场景。
JFormattedTextField（格式化文本输入框）：限制用户输入特定格式的文本，可用于确保用户按照特定要求输入数据。
JPasswordField （密码输入框）：专为密码输入而设计的文本输入字段，以保护用户输入的密码信息不可见。
JTextPane（样式文本显示）：支持样式文本的文本组件，允许在同一文档中使用不同的样式，适用于需要显示富文本内容的场景。
JEditorPane（富文本编辑器）：支持富文本编辑和HTML显示的文本组件，用于需要用户编辑富文本内容的场合。
JSpinner（数值选择器）：允许用户从范围中选择一个值的组件，适用于需要用户在一定范围内选择数值的场景。
JList（列表）：显示项目列表以供用户选择的组件。用于显示一列项目，用户可以选择一个或多个。
JTable（表格）：表示一个二维表格，用于显示和编辑表格数据。通常用于显示数据库查询结果或其他表格形式的数据。
JTree（树形结构）：以树形结构显示分层数据的组件，常用于呈现层次结构的数据。
JProgressBar（进度条）：表示任务进度的可视组件，用于显示某项任务的完成百分比。
JScrollBar（滚动条）：允许用户在大量内容中滚动的组件。通常与其他组件一起使用，以显示超过可见区域的内容。
JSeparator（分隔符）：用于分组组件的可视分隔符。可用于增强UI的可读性和组织性。
JSlider（滑块）：允许用户从连续范围中选择一个值的组件。用于需要用户在一个范围内选择数值的场景。
Action（动作）：封装动作的对象，可与各种UI组件关联，是用于处理用户操作的通用方式。
Horizontal Spacer/Vertical Spacer（水平和垂直间隔器）：用于布局目的的组件，用于在其他组件之间添加空间。有助于控制组件之间的距离和布局。

#### 2.3.2 Containers中间容器（面板）

JPanel：用于组织和布局其他组件的通用容器。
JTabbedPane：允许用户在多个选项卡之间切换的容器，每个选项卡包含不同的组件。
JScrollPane：提供另一个组件的可滚动视图的容器。
JSplitPane：允许用户水平或垂直调整和分割两个组件的容器。
JToolBar：用于工具栏组件的容器。
JToolBar Separator：用于在工具栏中视觉上分组组件的分隔符。
JDesktopPane：在桌面样式GUI中管理内部框架的容器。
JInternalFrame：存在于另一个框架内部的框架。
JLayeredPane：允许组件层叠在彼此上方的容器。

#### 2.3.3 Windows顶级容器（窗口）

JDialog：表示对话框的顶级容器。
JFrame：表示应用程序主窗口的顶级容器。
JWindow：表示辅助窗口的顶级容器。
Dialog/Frame/Window：表示对话框框架和窗口的通用类。

#### 2.3.4 Menus（菜单）

JMenuBar：用于组织菜单的容器。
JMenu：下拉菜单。
JMenuItem：菜单中的项目。
JCheckBoxMenuItem：带有复选框的菜单项。
JRadioButtonMenuItem：带有单选按钮的菜单项。
Menu Separator：菜单中的可视分隔符。
JPopupMenu：响应右键单击而出现的弹出菜单。

#### 2.3.5 JGoodies

Label：一个标签组件。
Titled Separator：带有标题的分隔符。
Title：用于显示有标题部分的组件。

#### 2.3.6 Binding

List：显示项目列表的组件。
ObservableList/ObservableMap：用于处理可观察列表和映射的类。

## 3 JFormDesigner 使用案例

本文以用户登录为例，介绍JFormDesigner的应用流程。

### 3.1 在IDEA中新建一个项目

IDEA菜单栏新建→项目，按照下图设置：

在该项目的src处右键，新建一个JFormDesigner Form：

### 3.2 设计登录页面

双击jfd文件，使用JLabel、JTextField、JButton、JPasswordField、JSeparator组件设计登录界面如下：


使用jfd中的按钮预览界面效果并生成界面代码：

### 3.3 与数据库交互

使用Navicat新建数据库以及数据表，添加测试数据如下：

注：此处仅为演示案例用，因此并未对用户密码进行加密操作。

在jfd文件中，选中“登录”按钮，在其属性面板中点击Events右侧的“+”号，并选择第一个选项：



这时java文件中就会自动添加这两段代码：
为“登录”按钮添加监听事件：

监听到按钮触发后的动作：


在java代码中添加用户验证处理逻辑，关键代码如下：

@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            // 获取用户输入的账户和密码
            String username = textField.getText();
            String password = new String(passwordField.getPassword());

            // 连接到数据库并验证用户
            if (validateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "登录成功！");
            } else {
                JOptionPane.showMessageDialog(this, "登录失败，请检查用户名和密码！");
            }
        }
    }
    
    private boolean validateUser(String username, String password) {
        try {
            // 连接到 MySQL 数据库
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
    
            // 查询用户表
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
    
                // 执行查询
                ResultSet resultSet = preparedStatement.executeQuery();
    
                // 如果有匹配的记录，则验证通过
                return resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库连接或查询出现问题：" + ex.getMessage());
            return false;
        }
    }

注意替换数据库连接的 URL、用户名和密码。

下载 MySQL Connector/J 驱动程序（JDBC 驱动程序），并将其置于项目lib目录下：

右键点击该 JAR 文件，选择 “Add as Library” 来将其添加到项目中：


编写main函数

public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
    }

点击运行按钮，测试结果如下：

