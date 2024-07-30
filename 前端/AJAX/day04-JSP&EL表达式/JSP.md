

# 一、课程目标


```markdown
1.  【了解】 jsp概述
2.  【掌握】 jsp基础语法
3.  【掌握】 jsp运行原理
4.  【掌握】 九大内置对象以及四大作用域
5.  【掌握】 EL书写语法
```



# 二、JSP

## 2.1 JSP概述

### 2.1.1 jsp简介

* Java Server Pager：运行在服务器端的网页
* 简单理解：在html中嵌入java语言

本质:使用通过response对象获取打印流将页面信息打印至客户端进行网页的书写

但是由于在java书写页面必须使用打印流字符串的形式,所以很繁琐,所以开发出了可以书写java代码的页面jsp



### 2.1.2 使用servlet打印流的形式打印动态页面

```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String username = request.getParameter("username");
		
		//通过打印流返回动态页面
		PrintWriter writer = response.getWriter();
		writer.print("<html>");
		writer.print("<head><title>登录成功</title></head>");
		writer.print("<body>欢迎用户:"+username+"登录!</body>");
		writer.print("</html>");
	}
}
```



### 2.1.3 使用jsp的形式书写动态页面

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String username = request.getParameter("username");
	%>
	<!-- out就相当去打印流对象 -->
	欢迎用户:<% out.print(username); %>登录!
</body>
</html>
```



## 2.2 JSP基础语法

### 2.2.1 组成

* 静态HTML标签
* 指令:<%@ 指令%>
* 脚本(代码块)
  * <%JAVA代码%>  局部代码
  * <%=java表达式%>
  * <%!    声明   %>  声明方法
* 注释
  * HTML注释：<!-- -->
  * JAVA注释 :<% //       /**/     %>
  * JSP:<%-- --%>



```jsp
	<!-- jsp脚本-->
	<!-- jsp本质还是servlet 只不过是特殊的servlet 可以直接书写html -->
	<!-- 为了将java代码与html静态标签区分 需要使用脚本包裹java代码 -->
	<!-- 这样在解析jsp时 会忽略静态html标签 执行java代码 -->

	<!-- java小脚本 代码块 用于包裹java代码  可以在< % 中书写任意java代码 % > -->
	<%
		//在这里可以使用任意的java代码 
		System.out.println("hello world!");
	%>

	<!-- java打印脚本  用于将其结果打印至页面  < %= 内容%>-->
	<!-- 要书写在页面的内容  注意 虽然是java代码 但是无需书写;  -->
	<!-- 通常与小脚本一起使用 用于输出结果 -->
	<%
		int a = 1;
		int b = 2;
	%>
	<%=a + b%>
	<%="输出汉字也可以"%>等价于write.print("") 一般常用方式为为页面标签动态赋值
	<br>
	<input type="text" value="<%=a%>">

	<!-- java声明脚本 -->
	<!-- 在普通的脚本中书写的代码本质就是在对应的doget  dopost中 -->
	<!-- 因为java中方法中不允许声明方法 所以需要使用特殊的方法声明方法 -->

	<%!//声明脚本可以声明方法  由其他脚本调用
	public void aaa() {
		System.out.println("aaa方法执行");
	}%>

	<%
		aaa();
	%>



	<!-- jsp注释 -->
	<!-- 因为jsp是java提供的书写html的服务 -->
	<!-- 对于注释的使用拥有三种注释 -->
	<!-- 1.html注释 -->
	<!-- 书写在html中 会发送至客户端  客户端可以查看 -->
	<!-- 2.java注释 -->
	<%  //书写在java小脚本中 与java中一样 需要书写注释符号
	// 在读取jsp后 解析java代码 编译时忽略 %>
	<!-- 3.jsp注释 -->
	<%-- 无需添加其他特殊语法 直接书写注释内容即可 
		在读取jsp时会自动忽略
	--%>
```



### 2.2.2 案例

#### 1) 计算两个变量的和并显示在页面

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
//定义局部变量
int a = 2;
int b = 3;

//求和
int sum = a+b;
%>

<h2><%=sum %></h2>
</body>
</html>
```

#### 2) 定义求和方法

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%!
//成员变量
int a=10;
int b =60;

//声明方法
public int sum(int c,int d){
	return c+d;
}
%>
<%=sum(4,6) %>
</body>
</html>
```



## 2.3 JSP运行原理

### 2.3.1 JSP执行流程

> 1. 客户端发送请求请求对应jsp时
> 2. 服务器获取jsp翻译成java文件
> 3. java文件编译生成class文件
> 4. 运行class文件，向客户端响应内容
>
> **执行流程:**
>
> 当客户端请求对应的jsp时,服务器首先判断的是是否编译过,如果编译过继续判断,判断是否修改,如果修改重新编译,如果没有修改 ,则直接将之前编译的servlet对象(jsp在执行时会翻译成servlet)返回并进行服务处理

### 2.3.2  JSP本质

> jsp本质就是一个servlet
>
> 注意：jsp只在第一次访问或修改的时候重新翻译和编译。
>
> 第一次请求jsp导致加载jsp响应过慢的情况称之为第一人惩罚策略









 

## 2.4 九大内置对象以及四大作用域

### 2.4.1 概念

* jsp本质是servlet,在书写servlet对应方法时自己创建的对象,例如 请求对象 响应对象 会话对象 上下文对象等,由srervlet方法参数直接创建 ,我们使用时无需创建可以直接使用的对象,称之为内置对象,我们只需要使用内置对象名.方法即可,无需创建

### 2.4.2 分类

* request:一次请求
  * HttpServletRequest
* session：一次会话
  * HttpSession
* application：整个应用
  * ServletContext
* pageContext：当前页面
  * PageContext
* response:保存请求页面响应信息
  * HttpServletResponse
* config:保存当前servlet对应的配置文件
  * ServletConfig
* page:代码当前的jsp对应的servlet对象 
  * this
* out:可以直接在页面输出内容,内容会直接答应在页面中
  * PrintWriter
* exception:用于存储当前页面异常信息,当页面发送异常时将异常信息存储至当前对象
  * Throwable



九大内置对象分别代码对应数据的存储,其中最常用的四大作用域pageContext.request.session.application



**page.jsp**

```jsp
<body>
page.jsp
<!-- 四大作用域 -->
<!-- 方法通用 都是为指定作用域中添加数据 获取数据 删除数据 -->
<br>pageContext作用域范围为当前页面<br>
<% pageContext.setAttribute("pageContext", "只有当前页面生效"); %>
<%=pageContext.getAttribute("pageContext") %>
<br>request作用域范围为本次请求,如果通过转发其他页面也可以访问<br>
<% request.setAttribute("request", "本次请求生效"); %>
<%=request.getAttribute("request") %>
<br>session作用域范围为当前会话,浏览器不关闭其他分页打开不同页面也可以访问<br>
<% session.setAttribute("session","本次会话生效"); %>
<%=session.getAttribute("session") %>

<br>application作用域为当前服务器,只要服务器不关闭都可以后期<br>
<% application.setAttribute("applicaton", "当前服务器生效"); %>
<%=application.getAttribute("applicaton") %>

<!-- 将请求转发 这样look.jsp与当前jsp使用相同的请求对象 -->
<% request.getRequestDispatcher("look.jsp").forward(request, response); %>
</body>
```



**look.jsp**

```jsp
<body>
look.jsp
<!-- 四大作用域 -->
<!-- 方法通用 都是为指定作用域中添加数据 获取数据 删除数据 -->
<br>pageContext作用域范围为当前页面<br>
<%=pageContext.getAttribute("pageContext") %>
<br>request作用域范围为本次请求,如果通过转发其他页面也可以访问<br>

<%=request.getAttribute("request") %>
<br>session作用域范围为当前会话,浏览器不关闭其他分页打开不同页面也可以访问<br>
<%=session.getAttribute("session") %>

<br>application作用域为当前服务器,只要服务器不关闭都可以后期<br>
<%=application.getAttribute("applicaton") %>
</body>
```



### 2.4.3 域对象范围案例演示

* A.jsp

  ```jsp
  <%@ page language="java" contentType="text/html; charset=UTF-8"
      pageEncoding="UTF-8"%>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  </head>
  <body>
  <h2>A页面</h2>
  <!-- 在pageContext域对象存入值-->
  <%
  pageContext.setAttribute("msg", "hello");
  %>
  
  <!-- 获取pageContext域对象中的属性值 -->
  <%=pageContext.getAttribute("msg") %>
  
  
  <!-- 在request域对象中存入值 -->
  <%request.setAttribute("info", "world"); %>
  
  
  <!-- 在session域对象中存入值 -->
  <%session.setAttribute("test", "111"); %>
  
  
  <!-- 在application中存入值 -->
  <%application.setAttribute("music", "简单爱"); %>
  
  
  <a href="/JSPTest/a">跳转到Servlet</a>
  </body>
  </html>
  ```

  

  

* MyServlet

  ```java
  //重定向到B.jsp（分别演示重定向和转发到B页面）
  resp.sendRedirect("/JSPTest/scope/B.jsp");
  ```

* B.jsp

  ```jsp
  <%@ page language="java" contentType="text/html; charset=UTF-8"
      pageEncoding="UTF-8"%>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  </head>
  <body>
  <h2>B页面</h2>
  <!-- 获取pageContext域对象中的属性值 -->
  <%=pageContext.getAttribute("msg") %>
  
  <!-- 获取request域对象中的属性值 -->
  <%=request.getAttribute("info") %>
  
  
  <!-- 获取session 域对象中的属性值 -->
  <%=session.getAttribute("test") %>
  
  <!-- 获取application 域对象中的属性值 -->
  <%=application.getAttribute("music") %>
  
  </body>
  </html>
  ```







# 三、EL表达式

## 3.1 产生背景

* 从JSP2.0开始，就不推荐使用java脚本，而是使用el表达式或者动态标签代替java脚本

##  3.2 概念

- 概念:Expression Language ：jsp内置的一种表达式语言
- 原理:el表达式的出现目的是替换jsp中的java的脚本中的<%=%>
- 并且内部进行了null值判断,如果为null则不会输出 ,不为null输出对应数据

## 3.3 写法规范

* ${表达式语言}

```jsp
<%--        <%= request.getAttribute("msg")%>--%>
<!-- 等价于<%=  %>输出内容 会自动获取域中数据 并进行null值判断 -->
        ${msg}
```

##  3.4 使用场景

- 获取域对象中的值(重要)
- 执行运算符号
- 调用java中方法（了解）
- EL表达式内置常用对象（重要）

### 3.4.1 从作用域访问对象

> 从page,request,session,application域对象中取值

**实现步骤**

```java
<%-- jsp的使用 --%>
<%-- 从域中取值 --%>
<%
    /*分别为不同域设置相应key 不同值的数据*/
    pageContext.setAttribute("key","pageValue");
    request.setAttribute("key","requestValue");
    session.setAttribute("key","sessionValue");
    application.setAttribute("key","applicationValue");
%>
<%-- 1.指定取值的域  从中取值 --%>
当前页面:${pageScope.key}<br>
当前请求:${requestScope.key}<br>
当前会话:${sessionScope.key}<br>
当前应用:${applicationScope.key}<br>

<%-- 2.自动查找域中的值 从作用域小的向上查找--%>
<%-- 自动查找会由下向上查找第一个指定的key并返回 如果使用自动查找 建议多个域中不要存储相同的key--%>
自动查找:${key}
```



### 3.4.2 获取数据中的值

> 获取数组,list,map,对象的值

```jsp
<body>
<%-- jsp的使用 --%>
<%-- 从域中取值(数组 list集合  map集合)并打印获取 --%>
<%
    //分别创建数组 list集合 map集合 对象 存储至域中
    String [] arr={"张三","李四","王五"};
    ArrayList<String> arrayList=new ArrayList<>();
    Collections.addAll(arrayList,"二狗子","猪蹄子");
    HashMap<String,String> hashMap=new HashMap<>();
    hashMap.put("username","zhangsan");
    hashMap.put("password","123456");

    Student student=new Student("lisi","abcddef");
    //存到域中
    pageContext.setAttribute("arr",arr);
    pageContext.setAttribute("arrayList",arrayList);
    pageContext.setAttribute("hashMap",hashMap);
    pageContext.setAttribute("student",student);
%>
获取数组数据
<%-- 可以直接通过  域key值[索引]  的形式获取数组中对应的数据 --%>
${arr[0]}
${arr[1]}
${arr[2]}
<hr>
获取list集合数据
<%-- 可以直接通过  域key值[索引]  的形式获取数组中对应的数据 --%>
${arrayList[0]}
${arrayList[1]}
<%-- 也可以通过对应对象的方法进行操作(会进行类型转换) --%>
${arrayList.get(0)}
${arrayList.get(1)}
<hr>
获取map集合数据
<%-- 可以直接通过 域key值.map的key 形式获取对应的value--%>
${hashMap.username}
${hashMap.password}
<%-- 也可以通过对应对象的方法进行操作(会进行类型转换) --%>
${hashMap.get("username")}
${hashMap.get("password")}

<hr>
获取对象中的属性值
<%-- 可以直接通过 域key值.属性 形式获取对应的value --%>
${student.username}
${student.password}
<%-- 也可以通过对应对象的方法进行操作(会进行类型转换) --%>
${student.getUsername()}

<%-- 注意 在获取自定义对象属性时 如果使用.属性的形式进行获取 那么必须书写公开的的get方法 --%>
</body>
```



### 3.4.3 运算

| 基础运算    | 加减乘除取模                                                 |      |
| ----------- | ------------------------------------------------------------ | ---- |
| 关系运算    | ==	eq<br/>>	gt<br/><	lt<br/>>=	ge<br/><=	le<br/>!=	ne |      |
| 逻辑运算    | && 与<br/>\|\|或<br/>! 非<br/>                               |      |
| empty运算符 | 判断对象是否存在，或者集合的长度是否为0                      |      |

```jsp
<body>
<%
    pageContext.setAttribute("n1", 20);
    pageContext.setAttribute("n2", 10);
%>
${n1+n2}
${n1 == n2}
${n1 eq n2}
${n1 ne n2}
${empty a}//判断域对象中是否有a
</body>
```



## 3.5 内置对象

| 对象                                                         | 含义                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| pageScope<br/>requestScope<br/>sessionScope<br/>applicationScope<br/> | 代表当前页面<br/>代表一次请求<br/>代表一次会话 session<br/>代表application |
| param                                                        | 返回客户端请求参数字符串值，相当于request.getParameter()     |
| paramValues                                                  | 返回客户端请求参数字符串值，相当于request.getParameters()    |
| pageContext                                                  | 可以获取其他八个内置对象                                     |
| headerValues                                                 | 代表请求头                                                   |
| cookie                                                       | 代表客户端发送请求中的所有cookie                             |

```
<h3>获取请求参数</h3>
<%= request.getParameter("username") %>

<h3>EL的方式</h3>
${ param.username }
${ paramValues.username[0] }


<h3>获取到请求头的信息</h3>
<%= request.getHeader("user-agent") %>

<h3>EL的方式</h3>
${ header["user-agent"] }

```


