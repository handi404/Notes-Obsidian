**目录**

[XML简介](https://blog.csdn.net/m0_58859743/article/details/125113744#XML%E7%AE%80%E4%BB%8B)

[一、初识XML](https://blog.csdn.net/m0_58859743/article/details/125113744#t1)

[1.什么是 XML？](https://blog.csdn.net/m0_58859743/article/details/125113744#t2)

[2.XML 和 HTML 之间的差异](https://blog.csdn.net/m0_58859743/article/details/125113744#t3)

[3.XML 不会做任何事情](https://blog.csdn.net/m0_58859743/article/details/125113744#t4)

[4.通过 XML 您可以发明自己的标签](https://blog.csdn.net/m0_58859743/article/details/125113744#t5)

[5.XML 不是对 HTML 的替代](https://blog.csdn.net/m0_58859743/article/details/125113744#t6)

[二、XML 用途](https://blog.csdn.net/m0_58859743/article/details/125113744#t7)

[1.XML 把数据从 HTML 分离](https://blog.csdn.net/m0_58859743/article/details/125113744#t8)

[2.XML 简化数据共享](https://blog.csdn.net/m0_58859743/article/details/125113744#t9)

[3.XML 简化数据传输](https://blog.csdn.net/m0_58859743/article/details/125113744#t10)

[三、XML 树结构](https://blog.csdn.net/m0_58859743/article/details/125113744#t11)

[1.一个 XML 文档实例](https://blog.csdn.net/m0_58859743/article/details/125113744#t12)

[2.XML 文档形成一种树结构](https://blog.csdn.net/m0_58859743/article/details/125113744#t13)

[四、XML 语法规则](https://blog.csdn.net/m0_58859743/article/details/125113744#t14)

[1.XML 文档必须有根元素](https://blog.csdn.net/m0_58859743/article/details/125113744#t15)

[2.XML 声明](https://blog.csdn.net/m0_58859743/article/details/125113744#t16)

[3.所有的 XML 元素都必须有一个关闭标签](https://blog.csdn.net/m0_58859743/article/details/125113744#t17)

[4.XML 标签对大小写敏感](https://blog.csdn.net/m0_58859743/article/details/125113744#t18)

[5.XML 必须正确嵌套](https://blog.csdn.net/m0_58859743/article/details/125113744#t19)

[6.XML 属性值必须加引号](https://blog.csdn.net/m0_58859743/article/details/125113744#t20)

[7.实体引用](https://blog.csdn.net/m0_58859743/article/details/125113744#t21)

[8.XML 中的注释](https://blog.csdn.net/m0_58859743/article/details/125113744#t22)

[9.在 XML 中，空格会被保留](https://blog.csdn.net/m0_58859743/article/details/125113744#t23)

[五、XML 元素](https://blog.csdn.net/m0_58859743/article/details/125113744#t24)

[1.什么是 XML 元素？](https://blog.csdn.net/m0_58859743/article/details/125113744#t25)

[2.XML 命名规则](https://blog.csdn.net/m0_58859743/article/details/125113744#t26)

[3.最佳命名习惯](https://blog.csdn.net/m0_58859743/article/details/125113744#t27)

[4.XML 元素是可扩展的](https://blog.csdn.net/m0_58859743/article/details/125113744#t28)

[六.XML 属性](https://blog.csdn.net/m0_58859743/article/details/125113744#t29)

[1.XML 属性](https://blog.csdn.net/m0_58859743/article/details/125113744#t30)

[2.XML 属性必须加引号](https://blog.csdn.net/m0_58859743/article/details/125113744#t31)

[3.XML 元素 vs. 属性](https://blog.csdn.net/m0_58859743/article/details/125113744#t32)

[4.避免 XML 属性？](https://blog.csdn.net/m0_58859743/article/details/125113744#t33)

[5.针对元数据的 XML 属性](https://blog.csdn.net/m0_58859743/article/details/125113744#t34)

[七、查看 XML 文件](https://blog.csdn.net/m0_58859743/article/details/125113744#t35)

[1.查看 XML 文件](https://blog.csdn.net/m0_58859743/article/details/125113744#t36)

[2.查看无效的 XML 文件](https://blog.csdn.net/m0_58859743/article/details/125113744#t37)

[3.为什么 XML 显示这个样子？](https://blog.csdn.net/m0_58859743/article/details/125113744#t38)

[八、使用 CSS 显示 XML](https://blog.csdn.net/m0_58859743/article/details/125113744#t39)

[1.使用 CSS 显示您的 XML？](https://blog.csdn.net/m0_58859743/article/details/125113744#t40)

[九、使用 XSLT 显示 XML](https://blog.csdn.net/m0_58859743/article/details/125113744#t41)

[1.使用 XSLT 显示 XML](https://blog.csdn.net/m0_58859743/article/details/125113744#t42)

[2.在服务器上通过 XSLT 转换 XML](https://blog.csdn.net/m0_58859743/article/details/125113744#t43)

[十、XML JavaScript](https://blog.csdn.net/m0_58859743/article/details/125113744#t44)

[1.XMLHttpRequest 对象](https://blog.csdn.net/m0_58859743/article/details/125113744#t45)

[2.XML 解析器](https://blog.csdn.net/m0_58859743/article/details/125113744#t46)

[实例](https://blog.csdn.net/m0_58859743/article/details/125113744#t47)

[总结](https://blog.csdn.net/m0_58859743/article/details/125113744#t48)

___

## **XML简介**

-     **`XML 被设计用来传输和存储数据。`** 
-     **`HTML 被设计用来显示数据`**

___

`提示：以下是本篇文章正文内容，下面案例可供参考`

### 一、初识XML

#### 1.什么是 XML？

-   XML 指可扩展标记语言（EXtensible Markup Language）。
-   XML 是一种很像HTML的标记语言。
-   XML 的设计宗旨是传输数据，而不是显示数据。
-   XML 标签没有被预定义。您需要自行定义标签。
-   XML 被设计为具有自我描述性。
-   XML 是 W3C 的推荐标准

#### 2.XML 和 HTML 之间的差异

XML 不是 HTML 的替代。

XML 和 HTML 为不同的目的而设计：

> -   XML 被设计用来传输和存储数据，其焦点是数据的内容。
> -   HTML 被设计用来显示数据，其焦点是数据的外观。

HTML 旨在显示信息，而 XML 旨在传输信息。

#### 3.XML 不会做任何事情

也许这有点难以理解，但是 XML 不会做任何事情。XML 被设计用来结构化、存储以及传输信息。

下面实例是 Jani 写给 Tove 的便签，存储为 XML：

```cobol
<note>

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>
```

上面的这条便签具有自我描述性。它包含了发送者和接受者的信息，同时拥有标题以及消息主体。

但是，这个 XML 文档仍然没有做任何事情。它仅仅是包装在 XML 标签中的纯粹的信息。我们需要编写软件或者程序，才能传送、接收和显示出这个文档。

#### 4.通过 XML 您可以发明自己的标签

上面实例中的标签没有在任何 XML 标准中定义过（比如 <to> 和 <from>）。这些标签是由 XML 文档的创作者发明的。

这是因为 XML 语言没有预定义的标签。

HTML 中使用的标签都是预定义的。HTML 文档只能使用在 HTML 标准中定义过的标签（如 <p>、<h1> 等等）。

XML 允许创作者定义自己的标签和自己的文档结构。

#### 5.XML 不是对 HTML 的替代

**XML 是对 HTML 的补充。**

XML 不会替代 HTML，理解这一点很重要。在大多数 Web 应用程序中，XML 用于传输数据，而 HTML 用于格式化并显示数据。

对 XML 最好的描述是：

**XML 是独立于软件和硬件的信息传输工具。**

___

## 

### 二、XML 用途

XML 应用于 Web 开发的许多方面，常用于**简化数据的存储和共享**。

#### 1.XML 把数据从 HTML 分离

如果您需要在 HTML 文档中显示动态数据，那么每当数据改变时将花费大量的时间来编辑 HTML。

通过 XML，数据能够存储在独立的 XML 文件中。这样您就可以专注于使用 HTML/CSS 进行显示和布局，并**确保修改底层数据不再需要对 HTML 进行任何的改变**。

**通过使用几行 JavaScript 代码，您就可以读取一个外部 XML 文件，并更新您的网页的数据内容。**

#### 2.XML 简化数据共享

在真实的世界中，[计算机系统](https://so.csdn.net/so/search?q=%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%B3%BB%E7%BB%9F&spm=1001.2101.3001.7020)和数据使用不兼容的格式来存储数据。

**XML 数据以纯文本格式进行存储，因此提供了一种独立于软件和硬件的数据存储方法。**

这让创建不同应用程序可以共享的数据变得更加容易。

#### 3.XML 简化数据传输

对开发人员来说，其中一项最费时的挑战一直是在互联网上的不兼容系统之间交换数据。

由于可以通过各种不兼容的应用程序来读取数据，以 XML 交换数据降低了这种复杂性。

___

### 三、XML 树结构

XML 文档形成了一种树结构，它从"根部"开始，然后扩展到"枝叶"。

#### 1.一个 XML 文档实例

XML 文档使用简单的具有自我描述性的语法：

```cobol
<?xml version="1.0" encoding="UTF-8"?>

<note>

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>
```

第一行是 XML 声明。它定义 XML 的版本（1.0）和所使用的编码（UTF-8 : 万国码, 可显示各种语言）。

下一行描述文档的**根元素**（像在说："本文档是一个便签"）：

```cobol
<note>
```

接下来 4 行描述根的 4 个**子元素**（to, from, heading 以及 body）：

```cobol
<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>
```

最后一行定义根元素的结尾：

```cobol
</note>
```

您可以假设，从这个实例中，XML 文档包含了一张 Jani 写给 Tove 的便签。

XML 具有出色的自我描述性，您同意吗？

#### 2.XML 文档形成一种树结构

XML 文档必须包含**根元素**。该元素是所有其他元素的父元素。

XML 文档中的元素形成了一棵文档树。这棵树从根部开始，并扩展到树的最底端。

所有的元素都可以有子元素：

```cobol
<root>

<child>

<subchild>.....</subchild>

</child>

</root>
```

**父、子以及同胞等术语用于描述元素之间的关系。**父元素拥有子元素。相同层级上的子元素成为同胞（兄弟或姐妹）。

**所有的元素都可以有文本内容和属性（类似 HTML 中）。**

**实例：**

![](https://img-blog.csdnimg.cn/img_convert/240b07f736e21981e54edd4f83f6fabf.gif)

上图表示下面的 XML 中的一本书：

**XML 文档实例**

```cobol
<bookstore>

<book category="COOKING">

<title lang="en">Everyday Italian</title>

<author>Giada De Laurentiis</author>

<year>2005</year>

<price>30.00</price>

</book>

<book category="CHILDREN">

<title lang="en">Harry Potter</title>

<author>J K. Rowling</author>

<year>2005</year>

<price>29.99</price>

</book>

<book category="WEB">

<title lang="en">Learning XML</title>

<author>Erik T. Ray</author>

<year>2003</year>

<price>39.95</price>

</book>

</bookstore>
```

实例中的根元素是 <bookstore>。文档中的所有 <book> 元素都被包含在 <bookstore> 中。

<book> 元素有 4 个子元素：<title>、<author>、<year>、<price>。 

___

### 四、XML 语法规则

XML 的语法规则很简单，且很有逻辑。这些规则很容易学习，也很容易使用。

#### 1.XML 文档必须有根元素

XML 必须包含根元素，它是所有其他元素的父元素，比如以下实例中 root 就是根元素：

```cobol
<root>

<child>

<subchild>.....</subchild>

</child>

</root>
```

以下实例中 note 是根元素：

```cobol
<?xml version="1.0" encoding="UTF-8"?>

<note>

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>
```

#### 2.XML 声明

XML 声明文件的可选部分，如果存在需要放在文档的第一行，如下所示：

> ```
> &lt;?xml version="1.0" encoding="utf-8"?&gt;
> ```

以上实例定义 XML 的版本（1.0）和所使用的编码（UTF-8 : 万国码, 可显示各种语言）。

#### 3.所有的 XML 元素都必须有一个关闭标签

在 HTML 中，某些元素不必有一个关闭标签：

```cobol
<p>This is a paragraph.

<br>
```

在 XML 中，省略关闭标签是非法的。所有元素都**必须**有关闭标签：

> ```cobol
> <p>This is a paragraph.</p>
> 
> <br />
> ```

**注释：**从上面的实例中，您也许已经注意到 XML 声明没有关闭标签。这不是错误。**声明不是 XML 文档本身的一部分，它没有关闭标签。**

#### 4.XML 标签对大小写敏感

XML 标签对大小写敏感。标签 <Letter> 与标签 <letter> 是不同的。

必须使用相同的大小写来编写打开标签和关闭标签：

```cobol
<Message>这是错误的</message>

<message>这是正确的</message>
```

**注释：**打开标签和关闭标签通常被称为开始标签和结束标签。不论您喜欢哪种术语，它们的概念都是相同的。

#### 5.XML 必须正确嵌套

在 HTML 中，常会看到没有正确嵌套的元素：

> ```
> &lt;b&gt;&lt;i&gt;This text is bold and italic&lt;/b&gt;&lt;/i&gt;
> ```

在 XML 中，**所有元素都必须彼此正确地嵌套**：

> ```
> &lt;b&gt;&lt;i&gt;This text is bold and italic&lt;/i&gt;&lt;/b&gt;
> ```

在上面的实例中，正确嵌套的意思是：由于 <i> 元素是在 <b> 元素内打开的，那么它必须在 <b> 元素内关闭。

#### 6.XML 属性值必须加引号

-   与 HTML 类似，XML 元素也可拥有属性（名称/值的对）。
-   在 XML 中，XML 的属性值必须加引号。

请研究下面的两个 XML 文档。 第一个是错误的，第二个是正确的：

```cobol
<note date=12/11/2007>

<to>Tove</to>

<from>Jani</from>

</note>

<note date="12/11/2007">

<to>Tove</to>

<from>Jani</from>

</note>
```

在第一个文档中的错误是，note 元素中的 date 属性没有加引号。

#### 7.实体引用

在 XML 中，一些字符拥有特殊的意义。

**如果您把字符 "<" 放在 XML 元素中，会发生错误，这是因为解析器会把它当作新元素的开始。**

这样会产生 XML 错误：

```cobol
<message>if salary < 1000 then</message>
```

为了避免这个错误，请用**实体引用**来代替 "<" 字符：

```cobol
<message>if salary &lt; 1000 then</message>
```

在 XML 中，有 5 个预定义的实体引用：

<table><tbody><tr><td>&amp;lt;</td><td>&lt;</td><td>less than</td></tr><tr><td>&amp;gt;</td><td>&gt;</td><td>greater than</td></tr><tr><td>&amp;amp;</td><td>&amp;</td><td>ampersand</td></tr><tr><td>&amp;apos;</td><td>'</td><td>apostrophe</td></tr><tr><td>&amp;quot;</td><td>"</td><td>quotation mark</td></tr></tbody></table>

**注释：**在 XML 中，只有字符 "<" 和 "&" 确实是非法的。大于号是合法的，但是用实体引用来代替它是一个好习惯。

#### 8.XML 中的注释

在 XML 中编写注释的语法与 HTML 的语法很相似。

```xml
<!-- This is a comment -->
```

#### 9.在 XML 中，空格会被保留

HTML 会把多个连续的空格字符裁减（合并）为一个：

<table><tbody><tr><td>HTML:</td><td><pre data-index="19" name="code">Hello           Tove</pre></td></tr><tr><td>输出结果:</td><td>Hello Tove</td></tr></tbody></table>

在 XML 中，文档中的空格不会被删减。

___

### 五、XML 元素

XML 文档包含 XML 元素。

#### 1.什么是 XML 元素？

XML 元素指的是从（且包括）开始标签直到（且包括）结束标签的部分。

一个元素可以包含：

-   其他元素
-   文本
-   属性
-   或混合以上所有...

```cobol
<bookstore>

<book category="CHILDREN">

<title>Harry Potter</title>

<author>J K. Rowling</author>

<year>2005</year>

<price>29.99</price>

</book>

<book category="WEB">

<title>Learning XML</title>

<author>Erik T. Ray</author>

<year>2003</year>

<price>39.95</price>

</book>

</bookstore>
```

在上面的实例中，<bookstore> 和 <book> 都有 **元素内容**，因为他们包含其他元素。<book> 元素也有**属性**（category="CHILDREN"）。<title>、<author>、<year> 和 <price> 有**文本内容**，因为他们包含文本。

#### 2.XML 命名规则

XML 元素必须遵循以下命名规则：

> -   名称可以包含字母、数字以及其他的字符
> -   名称不能以数字或者标点符号开始
> -   名称不能以字母 xml（或者 XML、Xml 等等）开始
> -   名称不能包含空格

可使用任何名称，没有保留的字词。

#### 3.最佳命名习惯

-   使名称具有描述性。使用下划线的名称也很不错：<first\_name>、<last\_name>。
-   名称应简短和简单，比如：<book\_title>，而不是：<the\_title\_of\_the\_book>。
-   避免 "-" 字符。如果您按照这样的方式进行命名："first-name"，一些软件会认为您想要从 first 里边减去 name。
-   避免 "." 字符。如果您按照这样的方式进行命名："first.name"，一些软件会认为 "name" 是对象 "first" 的属性。
-   避免 ":" 字符。冒号会被转换为命名空间来使用（稍后介绍）。
-   XML 文档经常有一个对应的数据库，其中的字段会对应 XML 文档中的元素。有一个实用的经验，即使用数据库的命名规则来命名 XML 文档中的元素。
-   在 XML 中，éòá 等非英语字母是完全合法的，不过需要留意，您的软件供应商不支持这些字符时可能出现的问题。

#### 4.XML 元素是可扩展的

XML 元素是可扩展，以携带更多的信息。

请看下面的 XML 实例：

```cobol
<note>

<to>Tove</to>

<from>Jani</from>

<body>Don't forget me this weekend!</body>

</note>
```

让我们设想一下，我们创建了一个应用程序，可将 <to>、<from> 以及 <body> 元素从 XML 文档中提取出来，并产生以下的输出：

**MESSAGE**

**To:** Tove  
**From:** Jani

Don't forget me this weekend!

想象一下，XML 文档的作者添加的一些额外信息：

```cobol
<note>

<date>2008-01-10</date>

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>
```

那么这个应用程序会中断或崩溃吗？

不会。这个应用程序仍然可以找到 XML 文档中的 <to>、<from> 以及 <body> 元素，并产生同样的输出。

XML 的优势之一，就是可以在不中断应用程序的情况下进行扩展。

___

### 六.XML 属性

XML元素具有属性，类似 HTML。

**属性（Attribute）提供有关元素的额外信息。**

#### 1.XML 属性

在 HTML 中，属性提供有关元素的额外信息：

```cobol
<img src="computer.gif">

<a href="demo.html">
```

属性通常提供不属于数据组成部分的信息。在下面的实例中，文件类型与数据无关，但是对需要处理这个元素的软件来说却很重要：

```cobol
<file type="gif">computer.gif</file>
```

#### 2.XML 属性必须加引号

属性值必须被引号包围，不过单引号和双引号均可使用。比如一个人的性别，person 元素可以这样写：

```cobol
<person sex="female">
```

或者这样也可以：

```cobol
<person sex='female'>
```

如果属性值本身包含双引号，您可以使用单引号，就像这个实例：

```cobol
<gangster name='George "Shotgun" Ziegler'>
```

或者您可以使用字符实体：

```xml
<gangster name="George &quot;Shotgun&quot; Ziegler">
```

#### 3.XML 元素 vs. 属性

请看这些实例：

```cobol
<person sex="female">

<firstname>Anna</firstname>

<lastname>Smith</lastname>

</person>

<person>

<sex>female</sex>

<firstname>Anna</firstname>

<lastname>Smith</lastname>

</person>
```

在第一个实例中，sex 是一个属性。在第二个实例中，sex 是一个元素。这两个实例都提供相同的信息。

没有什么规矩可以告诉我们什么时候该使用属性，而什么时候该使用元素。我的经验是在 HTML 中，属性用起来很便利，但是在 XML 中，您应该尽量避免使用属性。**如果信息感觉起来很像数据，那么请使用元素吧。**

### 

#### 4.避免 XML 属性？

因使用属性而引起的一些问题：

> -   属性不能包含多个值（元素可以）
> -   属性不能包含树结构（元素可以）
> -   属性不容易扩展（为未来的变化）

属性难以阅读和维护。**请尽量使用元素来描述数据。而仅仅使用属性来提供与数据无关的信息。**

不要做这样的蠢事（这不是 XML 应该被使用的方式）：

<note day="10" month="01" year="2008"  
to="Tove" from="Jani" heading="Reminder"  
body="Don't forget me this weekend!">  
</note>

#### 5.针对元数据的 XML 属性

有时候会向元素分配 ID 引用。这些 ID 索引可用于标识 XML 元素，它起作用的方式与 HTML 中 id 属性是一样的。这个实例向我们演示了这种情况：

```cobol
<messages>

<note id="501">

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>

<note id="502">

<to>Jani</to>

<from>Tove</from>

<heading>Re: Reminder</heading>

<body>I will not</body>

</note>

</messages>
```

上面的 id 属性仅仅是一个标识符，用于标识不同的便签。它并不是便签数据的组成部分。

在此我们极力向您传递的理念是：**元数据（有关数据的数据）应当存储为属性，而数据本身应当存储为元素。**

___

### 七、查看 XML 文件

**在所有主流的浏览器中，均能够查看原始的 XML 文件。**

> 不要指望 XML 文件会直接显示为 HTML 页面。

#### 1.查看 XML 文件

```cobol
<?xml version="1.0" encoding="ISO-8859-1"?>

- <note>

<to>Tove</to>

<from>Jani</from>

<heading>Reminder</heading>

<body>Don't forget me this weekend!</body>

</note>
```

XML 文档将显示为代码颜色化的根以及子元素。通过点击元素左侧的加号（+）或减号（ - ），可以展开或收起元素的结构。要查看原始的 XML 源（不包括 + 和 - 符号），选择"查看页面源代码"或从浏览器菜单"查看源文件"。

> **注释：**在 Safari 中，只有元素的文本将被显示。要查看原始的 XML，您必须右键单击页面，选择"查看源文件"。

#### 2.查看无效的 XML 文件

餐菜单，存储为 XML 数据。

#### 3.为什么 XML 显示这个样子？

-   XML 文档不会携带有关如何显示数据的信息。
-   由于 XML 标签由 XML 文档的作者"发明"，浏览器无法确定像 <table> 这样一个标签究竟描述一个 HTML 表格还是一个餐桌。
-   **在没有任何有关如何显示数据的信息的情况下，大多数的浏览器都会仅仅把 XML 文档显示为源代码。**
-   后续，我们会了解几个有关这个显示问题的解决方案，其中会使用 **CSS、XSLT** 和 **JavaScript**

___

### 八、使用 CSS 显示 XML

通过使用 CSS（Cascading Style Sheets 层叠[样式表](https://so.csdn.net/so/search?q=%E6%A0%B7%E5%BC%8F%E8%A1%A8&spm=1001.2101.3001.7020)），您可以添加显示信息到 XML 文档中。

#### 1.使用 CSS 显示您的 XML？

使用 CSS 来格式化 XML 文档是有可能的。

下面是 XML 文件的一小部分。

```cobol
<?xml version="1.0" encoding="ISO-8859-1"?>

<?xml-stylesheet type="text/css" href="cd_catalog.css"?>

<CATALOG>

<CD>

<TITLE>Empire Burlesque</TITLE>

<ARTIST>Bob Dylan</ARTIST>

<COUNTRY>USA</COUNTRY>

<COMPANY>Columbia</COMPANY>

<PRICE>10.90</PRICE>

<YEAR>1985</YEAR>

</CD>

<CD>

<TITLE>Hide your heart</TITLE>

<ARTIST>Bonnie Tyler</ARTIST>

<COUNTRY>UK</COUNTRY>

<COMPANY>CBS Records</COMPANY>

<PRICE>9.90</PRICE>

<YEAR>1988</YEAR>

</CD>

.

.

.

</CATALOG>
```

第二行把 XML 文件链接到 CSS 文件：

```xml
<?xml-stylesheet type="text/css" href="cd_catalog.css"?>
```

使用 CSS 格式化 XML 不是常用的方法。

___

### 九、使用 XSLT 显示 XML

通过使用 XSLT，您可以把 XML 文档转换成 HTML 格式。

#### 1.使用 XSLT 显示 XML

-   XSLT 是首选的 XML 样式表语言。
-   XSLT（eXtensible Stylesheet Language Transformations）远比 CSS 更加完善。
-   XSLT 是在浏览器显示 XML 文件之前，先把它转换为 HTML。

#### 2.在服务器上通过 XSLT 转换 XML

在上面的实例中，当浏览器读取 XML 文件时，XSLT 转换是由浏览器完成的。

**在使用 XSLT 来转换 XML 时，不同的浏览器可能会产生不同结果。为了减少这种问题，可以在服务器上进行 XSLT 转换**。

___

### 十、XML JavaScript

#### 1.XMLHttpRequest 对象

**XMLHttpRequest 对象用于在后台与服务器交换数据。**

XMLHttpRequest 对象是**开发者的梦想**，因为您能够：

-   在不重新加载页面的情况下更新网页
-   在页面已加载后从服务器请求数据
-   在页面已加载后从服务器接收数据
-   在后台向服务器发送数据

**创建一个 XMLHttpRequest 对象**

所有现代浏览器（IE、Firefox、Chrome、Safari 和 Opera）都有内建的 XMLHttpRequest 对象。

创建 XMLHttpRequest 对象的语法：

```csharp
xmlhttp=new XMLHttpRequest();
```

#### 2.XML 解析器

所有现代浏览器都有内建的 XML 解析器。

XML 解析器把 XML 文档转换为 XML DOM 对象 - 可通过 JavaScript 操作的对象。

**解析 XML 文档**

下面的代码片段把 XML 文档解析到 XML DOM 对象中：

```cobol
if (window.XMLHttpRequest)

{// code for IE7+, Firefox, Chrome, Opera, Safari

xmlhttp=new XMLHttpRequest();

}

else

{// code for IE6, IE5

xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");

}

xmlhttp.open("GET","books.xml",false);

xmlhttp.send();

xmlDoc=xmlhttp.responseXML;
```

**解析 XML 字符串**

下面的代码片段把 XML 字符串解析到 XML DOM 对象中：

```cobol
txt="<bookstore><book>";

txt=txt+"<title>Everyday Italian</title>";

txt=txt+"<author>Giada De Laurentiis</author>";

txt=txt+"<year>2005</year>";

txt=txt+"</book></bookstore>";

if (window.DOMParser)

{

parser=new DOMParser();

xmlDoc=parser.parseFromString(txt,"text/xml");

}

else // Internet Explorer

{

xmlDoc=new ActiveXObject("Microsoft.XMLDOM");

xmlDoc.async=false;

xmlDoc.loadXML(txt);

}
```

**注释：**Internet Explorer 使用 loadXML() 方法来解析 XML 字符串，而其他浏览器使用 DOMParser 对象。

**跨域访问**

出于安全方面的原因，现代的浏览器不允许跨域的访问。

这意味着，网页以及它试图加载的 XML 文件，都必须位于相同的服务器上。

**3.XML DOM**

DOM（Document Object Model 文档对象模型）定义了访问和操作文档的标准方法。

**XML DOM**

XML DOM（XML Document Object Model）定义了访问和操作 XML 文档的标准方法。

XML DOM 把 XML 文档作为树结构来查看。

> **所有元素可以通过 DOM 树来访问。可以修改或删除它们的内容，并创建新的元素。元素，它们的文本，以及它们的属性，都被认为是节点。**

**HTML DOM**

HTML DOM 定义了访问和操作 HTML 文档的标准方法。

所有 HTML 元素可以通过 HTML DOM 来访问。

**加载一个 XML 文件 - 跨浏览器实例**

下面的实例把 XML 文档（"[note.xml](https://www.runoob.com/try/xml/note.xml "note.xml")"）解析到 XML DOM 对象中，然后通过 JavaScript 提取一些信息：

### 实例

```cobol
<html>

<body>

<h1>W3Schools Internal Note</h1>

<div>

<b>To:</b> <span id="to"></span><br />

<b>From:</b> <span id="from"></span><br />

<b>Message:</b> <span id="message"></span>

</div>

<script>

if (window.XMLHttpRequest)

{// code for IE7+, Firefox, Chrome, Opera, Safari

xmlhttp=new XMLHttpRequest();

}

else

{// code for IE6, IE5

xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");

}

xmlhttp.open("GET","note.xml",false);

xmlhttp.send();

xmlDoc=xmlhttp.responseXML;

document.getElementById("to").innerHTML=

xmlDoc.getElementsByTagName("to")[0].childNodes[0].nodeValue;

document.getElementById("from").innerHTML=

xmlDoc.getElementsByTagName("from")[0].childNodes[0].nodeValue;

document.getElementById("message").innerHTML=

xmlDoc.getElementsByTagName("body")[0].childNodes[0].nodeValue;

</script>

</body>

</html>
```

> 重要注释！
> 
> 如需从上面的 XML 文件（"note.xml"）的 <to> 元素中提取文本 "Tove"，语法是：
> 
> getElementsByTagName("to")\[0\].childNodes\[0\].nodeValue
> 
> 请注意，即使 XML 文件只包含一个 <to> 元素，您仍然必须指定数组索引 \[0\]。这是因为 getElementsByTagName() 方法返回一个数组。

**4.HTML 页面显示 XML 数据**

**在 HTML 页面中显示 XML 数据**

在下面的实例中，我们打开一个 XML 文件（"[cd\_catalog.xml](https://www.runoob.com/try/xml/cd_catalog.xml "cd_catalog.xml")"），然后遍历每个 CD 元素，并显示HTML 表格中的 ARTIST 元素和 TITLE 元素的值：

```cobol
<html>

<body>

<script>

if (window.XMLHttpRequest)

{// code for IE7+, Firefox, Chrome, Opera, Safari

xmlhttp=new XMLHttpRequest();

}

else

{// code for IE6, IE5

xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");

}

xmlhttp.open("GET","cd_catalog.xml",false);

xmlhttp.send();

xmlDoc=xmlhttp.responseXML;

document.write("<table border='1'>");

var x=xmlDoc.getElementsByTagName("CD");

for (i=0;i<x.length;i++)

{

document.write("<tr><td>");

document.write(x[i].getElementsByTagName("ARTIST")[0].childNodes[0].nodeValue);

document.write("</td><td>");

document.write(x[i].getElementsByTagName("TITLE")[0].childNodes[0].nodeValue);

document.write("</td></tr>");

}

document.write("</table>");

</script>

</body>

</html>
```

___

## 总结

本文参考RUNOOB.COM