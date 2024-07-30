# 一、课程目标


```markdown
1.  【了解】 ServletRequst接口
2.  【了解】 ServletResponse接口
3.  【掌握】 HttpServletRequest获取请求参数
4.  【掌握】 HttpServletRequest域对象存值
5.  【掌握】 HttpServletRequest请求转发
6.  【掌握】 HttpServletResponse页面输出内容
7.  【掌握】 HttpServletResponse定时刷新页面
8.  【掌握】 HttpServletResponse请求重定向
9.  【掌握】 HttpServletResponse转发与重定向的区别
```

# 二、ServletRequest

## 2.1 概述

服务器将服务封装成一个servlet服务对象,那么客户的请求携带的数据也可以封装成一个ServletRequest服务请求对象,本质是一个接口,由不同的客户端发送进行实现,并将发送的数据封装至对应对象发送给服务器端

但是由于现在web传输大多使用的http协议,并且该协议在请求时会传递很多信息,所以根据http协议创建了继承于ServletRequest接口的子接口HttpServletRequest,对应HttpServlet服务的使用.



**本质**:就是书写一个对象存储浏览器端发送的数据(服务器可以获取这个对象并从该对象中获取数据以及其他功能的使用)

由客户端发送的所有数据都存储在该对象中

```java
public interface HttpServletRequest extends ServletRequest {....}
```

## 2.2 作用

### 2.2.1 获取客户端信息

```java
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RequestServlet", value = "/request1")
public class RequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request对象中封装保存着客户端(浏览器发送携带的数据)
        //可以通过对应的方法获取客户端请求携带的对应数据

        //获取客户端请求协议
        String protocol = request.getProtocol();
        System.out.println(protocol);

        //获取客户端请求服务名称(ip或者域名)
        String serverName = request.getServerName();
        System.out.println(serverName);

        //获取客户端请求的服务url
        String servletPath = request.getServletPath();
        System.out.println(servletPath);

        //获取客户端请求服务的端口号
        int serverPort = request.getServerPort();
        System.out.println(serverPort);

        //获取客户端请求的项目地址(项目名,由tomcat配置的请求地址不是实际项目名)
        String contextPath = request.getContextPath();
        System.out.println(contextPath);

        //获取客户端请求的url的相对路径
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        //获取客户端请求url的绝对路径
        String requestURL = request.getRequestURL().toString();
        System.out.println(requestURL);

        //获取请求头中指定字段值
        String userAgent = request.getHeader("User-Agent");
        System.out.println(userAgent);
        String CacheControl = request.getHeader("Cache-Control");
        System.out.println(CacheControl+"11111");
    }
}
```

 



### 2.2.2 获取请求参数

#### 1) 获取请求中的单个或多个数据

**RequestServlet2.java**

```java
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
@WebServlet(name = "RequestServlet2", value = "/request2")
public class RequestServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //一般我们在获取请求数据时 最常用的是获取请求中写的数据(表单提交数据)
        //get请求数据在url地址栏传输
        //获取请求中携带的指定参数对应的值
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "|" + password);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //一般我们在获取请求数据时 最常用的是获取请求中写的数据(表单提交数据)
        //post请求数据在请求体中传输
        //也可以通过方法获取所有的数据
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String s = names.nextElement();
            System.out.println(s + "=>" + request.getParameter(s));
        }
        //getParameter方法只能获取对应key第一个数据 如果多个数据key相同则需要使用其他方法获取
        String[] likes = request.getParameterValues("like");
        System.out.println(Arrays.toString(likes));
    }
}
```

**form.html**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="request2" method="post">
        <input type="text" name="username"><br>
        <input type="password" name="password"><br>
        <input type="checkbox" name="like" value="cy"> 抽烟<br>
        <input type="checkbox" name="like" value="hj"> 喝酒<br>
        <input type="checkbox" name="like" value="tt"> 烫头<br>
        <input type="submit"  value="提交"><br>
    </form>
</body>
</html>
```





#### 2) 请求参数中文乱码问题

乱码原因:数据在网络上进行传输使用的是流的形式.如果设置了编码格式与当前开发环境匹配则不会出现乱码,如果没有设置,浏览器默认编码为IOS-8859-1,可以使用String类的构造方法将指定编码的字节数组转化为对应编码的字符串,但是通常情况会在服务器端通过方法的形式进行编码的转换而不是这种方式

* 提交的方式是get：中文不会乱码（tomcat8.0以上的版本 UTF-8）

  * ```java
    String username = new String(req.getParameter("username").getBytes("ISO8859-1"),"UTF-8")
    ```

* 提交的方式是post：中文乱码

  * ```java
            //出现中文乱码只需要在获取对应数据之前将请求中数据的编码格式修改为对应编码
            request.setCharacterEncoding("UTF-8");
    ```


注意:一定要书写在获取数据之前,否则没有意义





### 2.2.3 request域对象

浏览器在请求服务器时,会将所有的请求信息封装为一个对应的请求对象,可以从请求对象中获取对应的数据,服务器在获取这个请求对象之后,可以继续进行数据添加,但是请求的作用域只有本次请求,因为http协议是基于请求与响应的协议,每次在浏览器输入地址请求服务器都会重写发送请求,也会创建新的请求对象.

作用

* 在一次请求中，传递数据

方法

| 方法名                                   | 功能                | 备注 |
| ---------------------------------------- | ------------------- | ---- |
| Object getAttribute(String name)         | request域对象获取值 |      |
| void setAttribute(String name, Object o) | request域对象取值   |      |
| void removeAttribute(String name)        | request域对象删除值 |      |

总结

* request主要用来存储提示信息

```java
   //在客户端每次请求时 会将每次的请求封装为一个请求对象保存对应信息
        //再次请求  即使请求数据相同  创建的请求对象也是不同的对象
        //request作为域对象传值 就是在本次请求内进行数据的传递
        //域对象统一API
        //设置指定key对应的object数据
        // request.setAttribute(key,obj);
        //删除指定key对应的数据
        //request.removeAttribute(key);
        // 获取指定key存储的数据
        // Object key = request.getAttribute(key);
        //获取指定域对象 存储的所有key的列表对象
        //Enumeration<String> attributeNames = request.getAttributeNames();

        Random random = new Random();
        request.setAttribute("random", random.nextInt(100));
        request.setAttribute("test", "test");

        Enumeration<String> attributeNames = request.getAttributeNames();
        while(attributeNames.hasMoreElements()){
            String key = attributeNames.nextElement();
            Object o = request.getAttribute(key);
            System.out.println(key+":"+o);
        }

        Object r = request.getAttribute("random");
        System.out.println(r);
        request.removeAttribute("random");
        Object r1 = request.getAttribute("random");
        System.out.println(r1);

        Enumeration<String> attributeNames1 = request.getAttributeNames();
        while(attributeNames1.hasMoreElements()){
            String key = attributeNames1.nextElement();
            Object o = request.getAttribute(key);
            System.out.println(key+":"+o);
        }
```









### 2.2.4 转发

#### 1) 概念

* 转发也是程序的跳转，在服务器的内部
* 跳转的路径：使用服务器端的绝对路径，不包含项目名称！！
* 执行流程:当客户端请求服务a服务a获取客户端请求数据并存储至请求对象中,但是a不能完成对应的服务,通过转发的方法将请求转发到b服务,并将请求的对象一同转发进行执行,当b执行完毕后将结果返回给a服务,a服务将最终结果发生给客户端

#### 2) 原理

![](assert\转发的原理.bmp)

#### 3) 方法

* request.getRequestDispatcher("要转发的路径").forward(request,response);

#### 4) 案例

**AServlet.java**

```java
@WebServlet(name = "AServlet", value = "/a")
public class AServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //与servletcontext拥有下昂用的赋值获取移除方法
        //只不过现在针对的是request对象进行对应操作
        request.setAttribute("username", "lisi");
        System.out.println("a执行");
        //如果想让A中的数据传递给B需要将AServlet中的请求对象转发到B

        //请求转发
        //将当前的请求转发到其他服务  并将当前请求的请求对象与响应对象填入
        request.getRequestDispatcher("/b").forward(request,response);
    }
}
```

**BServlet.java**

```java
@WebServlet(name = "BServlet", value = "/b")
public class BServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //不能获取其他servlet requset对象中的数据
        //因为每次请求都会创建新的请求对象
        Object username = request.getAttribute("username");
        System.out.println(username);
        System.out.println("b执行");
    }
}
```





转发不仅仅局限于服务的转发,页面也可以转发,并且由于转发是服务器内部请求,所以可以访问服务器内部资源(转发可以访问WEB-INF下的页面)













# 三、ServletResponse

## 3.1 概述

客户端发送请求的数据存储在请求对象中,那么服务器处理完相应请求的数据会封装在响应对象中(包含响应格式)

对于响应提供了ServletResponse接口进行数据的存储,但是sun公司基于Http协议提供了继承于ServletResponse接口HttpServletResponse用于处理基于http协议请求的响应



## 3.2 作用

### 3.2.1 页面输出内容

```java
@WebServlet(name = "ResponseServlet", value = "/rs")
public class ResponseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //HttpServletResponse用于保存响应相关数据
        //已经根据http协议定义好了响应的常用相关数据  我们只需要根据需求继续添加即可

        //设置response缓冲区的编码
        response.setCharacterEncoding("UTF-8");
        //设置客户端相应内容的类型
        response.setContentType("text/html;charset=utf-8");


        //getWriter()获取打印流直接将信息打印至客户端页面
        PrintWriter writer = response.getWriter();
        writer.print("response返回的数据<br>");

    }
}
```



### 3.2.2 定时刷新

RefreshServlet.Java

```java
package com.yh;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面的定时的跳转
 */
public class Refresh1Servlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 		response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        // 先输出内容
        System.out.println("Refresh1Servlet执行了...");
        // 设置头信息
        response.setHeader("refresh", "5;url=/ResponseTest/response/demo1/suc.html");
        // 输出一句内容
        response.getWriter().print("5秒之后跳转");
	}
 
}
```







### 3.2.3 重定向

#### 1) 概念

当用户请求服务A进行服务执行,但是服务A不能执行对应的服务,并且服务A与实际能执行的服务没有关联,只能提供对应的地址由客户端再次请求对应服务完成需求的实现.

#### 2) 原理图

![](assert\重定向的原理.png)

#### 3) 方法

response.sendRedirect("/重定向地址");

只能访问公开资源,不能访问内部资源

#### 4) 案例

* 需求：用户在登陆界面输入用户名和密码，后台的servlet进行判断，如果用户名和密码都是admin，跳转到成功页面，否则跳转到登录失败页面

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>登陆页面</h3>
<form action="/ServletTest/login" method="get">
	用户名：<input type="text" name="username"/><br/>
	
	密码：<input type="text" name="password"/>
	<input type="submit" value="登陆"/>
</form>
</body>
</html>
```

```java
package com.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取用户名和密码
		String username = req.getParameter("username");
		
		String password = req.getParameter("password");
		
		//判断
		if("admin".equals(username) && "admin".equals(password)){
			//跳转到成功页面
			resp.sendRedirect("/ServletTest/succ.html");
		}else{
			//跳转到失败页面
			resp.sendRedirect("/ServletTest/fail.html");
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}

```



```java
@WebServlet(name = "AServletResponse", value = "/as")
public class AServletResponse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("A执行");
        //sendRedirect将当前请求重定向指定路径 不会携带参数
        request.setAttribute("username","lisi");
        response.sendRedirect("bs");
    }
}

@WebServlet(name = "BServletResponse", value = "/bs")
public class BServletResponse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("b执行");
        //由于重定向相当于客户再次请求  会创建新的请求对象 所以不会获取上一个服务赋值的数据
        Object username = request.getAttribute("username");
        System.out.println(username);
    }
}

```





# 四、**常见面试题**

>**转发与重定向区别**
>
>* 转发的地址栏不会发生改变，但是重定向会发生改变
>* 转发是一次请求和一次响应，但是重定向是多次请求与响应
>* 转发的状态码是2xx,重定向是3xx
>* 转发可以使用request域对象传递数据，但是重定向不可以(必须将请求与响应对象传递)
>* 转发本质请求转发指定服务执行后由当前服务进行返回，重定向当前服务结束客户端继续请求其他服务
>* 转发发生在服务器内部，转发的资源不需要携带项目名，重定向可以重定到任何公开资源(需要带项目名)。
>* 转发可以请求web-inf下的内部资源
>
>
>
>**get请求与post请求区别**
>
>  * get请求发送数据在url地址栏传输,post请求在请求体中传输
>* get请求相比post不安全
>  * get请求可以被书签保存,但是post不可以(因为书签保存的是url可以保存请求的数据)
>* get请求在浏览器页面后退时无影响,post可能导致表单的重复提交
>* get请求浏览器会主动缓存相应的数据,post不会主动缓存
>* get请求页面编码与后台编码相同不会乱码,post可能会乱码
>* get请求根据不同浏览器设置请求数据上限不定(一般为2k),post请求可以看做无大小限制
>* get请求发送1次,post请求发送2次(请求会将数据同url一同发送,post则是请求地址响应后再次发送)
>* post请求服务器主动接受，会在服务器响应后才将数据发生给服务器
>
>最后一条也可以理解为,get请求服务器被动接收(你服务器接不接受数据我数据都随着请求地址发送过去了)