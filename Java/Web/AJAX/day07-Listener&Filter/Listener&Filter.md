# 一、课程目标


```markdown
1.  【掌握】 Filter过滤器的使用
2.  【了解】 Filter过滤器的生命周期
3.  【掌握】 Filter过滤器相应案例的书写
4.  【掌握】 Listener监听器的使用
```



# 二、Filter

## 2.1 概述

在服务器运行期间对指定的请求进行过滤与操作,减少服务器压力,或特殊需求的实现

### 引入

* 现实案例理解分析（水杯滤网，地铁安检口），引出请求到达servlet之前，先经过过滤器，再访问servlet，在过滤器中解决乱码

### 特点

* 过滤器本身不是目标资源(不能直接被访问)
* 过滤器是在访问目标对象前后执行的,过滤器是双向的
* 过滤器可以配置多个
* 过滤器过滤的都是多个目标资源

## 2.2 入门程序

### 2.2.1 Filter创建

```java
//创建java类 实现 javax.servlet.Filter接口 实现doFiLter方法
public class MyFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//request就是当前请求对象  所有的请求数据都包含在其中(注意 这时还是ServletRequest对象 不能使用HttpServletRequest对象方法)
		//response就是对于本次请求的响应对象 (因为响应结束后还是通过过滤器返回 所以会创建)
		//chain 过滤器链对象 代表是否继续执行 执行doFilter方法可以继续向下执行 否则到此中断
		
		System.out.println("过滤器执行前");
		// 放行 只有调用这个方法 对应的servlet才能继续执行
		chain.doFilter(request, response);
		System.out.println("过滤器执行后");
	}
}
```



### 2.2.2 Fiter配置

```xml
 <!-- 配置过滤器 -->
        <!-- 配置过滤器的基本的信息 -->
        <filter>
            <!-- 过滤器的名称，可以是任意的，一般都和类名称是相同 -->
            <filter-name>Demo1Filter</filter-name>
            <!-- 配置类的全路径 -->
            <filter-class>com.yh.Demo1Filter</filter-class>
        </filter>
        <!-- 配置过滤器的映射信息 -->
        <filter-mapping>
            <!-- 配置过滤器的名称，通过该名称找到过滤器，该名称和上面名称必须相同 -->
            <filter-name>Demo1Filter</filter-name>
   
        
            <url-pattern>/*</url-pattern>
        </filter-mapping>
```



## 2.3 生命周期

与srvelt相同 都是通过三个方法进行操作,不同的是Filter接口中init方法与destory方法为默认方法(可以不实现)

* 过滤器实例被创建（服务器启动(与servlt一样通过设置,不同版本默认可能不同)）
  * init():过滤器被创建后,立即调用init方法进行初始化
* 过滤器的实例提供服务（拦截一次执行一次）
  * doFilter():每次拦截都会被执行
* 过滤器实例对象被销毁（服务器关闭，销毁）
  * destory():过滤器实例被销毁之前,调用该方法





如果存在多个过滤器过滤相同的url,根据xml配置顺序决定过滤器执行顺序

执行的顺序与栈类似先进后出(先执行的过滤器 最后执行结束代码)



## 2.4 FilterChain接口

* 代表的是过滤器链(多个过滤器组成在一起)

### 2.4.1 作用

* 用来放行，如果有下一个过滤器,执行下一个过滤器,没有,执行目标资源

### 2.4.2 特点

* 过滤器链是由Tomcat服务器提供的
* 执行顺序是由filter-mapper顺序决定的

```java
@WebFilter(filterName = "MyFilter1",value = "/*")
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("myFilter1过滤器执行");
        //FilterChain过滤器链
        //该对象代码当前服务器所有的过滤器
        //当当前过滤器执行后之后有其他过滤器时doFilter方法代表执行下一个过滤器的doFilter方法
        //当当前过滤器执行之后没有其他过滤器时doFilter方法代表放行 执行请求的服务
        //需要注意的,即使请求的服务器不存在 对应的过滤器也会执行(WEB-INF除外)
        //如果是配置Filter 那么过滤器执行的顺序由配置mapping顺序决定
        //如果使用注解配置,配置书写在类中,所以执行的顺序与文件顺序有关(相同url的过滤器 )
        //注解形式就是在加载对应class之后根据calss中注解生成配置
        //先加载的class会先生成配置,所以符合原本的mapping书写顺序
        chain.doFilter(request, response);

        System.out.println("myFilter1过滤器执行");
    }
}
```





## 2.5 FilterConfig接口

* 过滤器配置对象：代表的是过滤器的配置信息,通过该接口中的一些方法来获取到当前的过滤器的配置文件信息

### 2.5.1 方法

| 方法名                        | 功能                             | 备注 |
| ----------------------------- | -------------------------------- | ---- |
| getFilterName                 | 获取过滤器名字                   |      |
| getInitParameter(String name) | 获取过滤器中的初始化参数信息     |      |
| getInitParamsterNames()       | 获取过滤器中的所有初始化参数信息 |      |
| getServletContext()           | 获取ServletContext域对象         |      |

### 2.5.2 案例

```java
	/**
	 * 每次拦截该方法都会执行了
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("demo2Filter执行了...");
		// 放行
		chain.doFilter(request, response);
	}
	
	/**
	 * 过滤器实例对象一被创建，立即调用Init方法做初始化
	 */
	public void init(FilterConfig config) throws ServletException {
		
		// 先获取到过滤器配置文件中的<filter-name>的值
		String filterName = config.getFilterName();
		System.out.println("过滤器的名称："+filterName);
		
		// 可以获取到初始化参数
		String username = config.getInitParameter("username");
		String password = config.getInitParameter("password");
		System.out.println(username+" : "+password);
		
		// 获取初始化参数的名称
		Enumeration<String> e = config.getInitParameterNames();
		while(e.hasMoreElements()){
			String key = e.nextElement();

			String value = config.getInitParameter(key);
			System.out.println(key+" : "+value);
		}

		// 获取到ServletContext对象（非常重要）
		ServletContext context = config.getServletContext();
		
		System.out.println("Demo2Filter已经被创建了...");
	}
```

过滤器配置

```xml
<filter>
    <filter-name>Demo2Filter</filter-name>
    <filter-class>com.yh.Demo2Filter</filter-class>
    <init-param>
      <param-name>username</param-name>
      <param-value>root</param-value>
    </init-param>
    <init-param>
      <param-name>password</param-name>
      <param-value>123456</param-value>
    </init-param>
 </filter>
```



## 2.6 过滤器配置总结

```xml
<!-- 过滤器的基本信息 -->
    <filter>
        <!-- 名称可以任意，默认情况下和类名称是相同的 -->
        <filter-name>Demo4Filter</filter-name>
        <!-- 该过滤器的包名+类名 -->
        <filter-class>com.yh.Demo4Filter</filter-class>
    </filter>

    <!-- 过滤器的映射的信息（过滤器的拦截哪些目标资源） -->
    <filter-mapping>
        <!-- 和上面的名称必须相同的 -->
        <filter-name>Demo4Filter</filter-name>
        <!-- 拦截哪些资源 采用的是目录匹配比较多-->
        <url-pattern>/*</url-pattern>
        
            <!-- 配置过滤器过滤的url -->
			<!-- 1.设置过滤的具体url -->
			<!-- <url-pattern>/b</url-pattern> -->
			<!-- 2.设置过滤指定后缀 -->
			<!-- 过滤以指定后缀结尾的url -->
			<!-- <url-pattern>*.do</url-pattern> -->
			<!-- 3.全部过滤 -->
			<!-- 所有请求url都进行过滤 -->
		  	<url-pattern>/*</url-pattern> 
			<!-- 也可以过滤指定的二级目录 -->
			<!-- <url-pattern>/ai/*</url-pattern> -->
    </filter-mapping>
```


## 2.7 案例

### 1) 统计当前网站访问总次数

```java
public class DemoFilter implements Filter{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		servletContext.setAttribute("count", 0);
	}
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		ServletContext servletContext = arg0.getServletContext();
		Integer attribute = (Integer) servletContext.getAttribute("count");		
		servletContext.setAttribute("count", attribute+1);
		arg2.doFilter(arg0, arg1);
	}
}
```



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
网站访问量:${count }
</body>
</html>
```





### 2) 解决参数中文乱码问题

在使用jsp后响应数据的乱码基本不会出现,因为在jsp已经设置了各种的编码,(由jsp请求发送的参数也很少乱码,但是依旧存在),解决中文乱码的方式是在servlet获取数据之前设置编码集合,但是每个servlet都设置编码集造成了代码的冗余,可以使用过滤器来解决这一问题,在所有servlet执行之前,通过过滤器设置请求与响应的编码集,这样通过过滤器的原理就可以只在过滤器书写一次,即可解决中文乱码问题

```java
@WebFilter(filterName = "CodiingFilter", value = "/*", initParams = {@WebInitParam(name = "code", value = "UTF-8")})
public class CodiingFilter implements Filter {
    private String code = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        code = filterConfig.getInitParameter("code");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding(code);
        response.setCharacterEncoding(code);
        response.setContentType("text/html;charset=" + code);
        chain.doFilter(request, response);
    }
}
```



### 3) 自动登录

可以设置过滤器校验请求,必须在登录后再能继续请求对应的服务,如果没有登录跳转至登录页面,,如果在每个服务doget请求进行书写会造成代码的冗余,可以将登录验证操作书写在过滤器中,这样当请求时通过过滤器验证是否登录过,如果登录过继续请求对应的服务,如果没有登录过则跳转登录页面进行登录.

```java
@WebFilter(filterName = "LoginFilter", value = "/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //在登录成功后会将标识放在session中 ,可以通过session获取对应数据 存在则登录成功  不存在则跳转登录页面
        //因为过滤器使用的ServletRequest保存请求对象
        //需要将其转换为HttpServletRequest对象
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        Object username = session.getAttribute("username");
        if(username!=null){
            //继续执行
            chain.doFilter(request, response);
        }else{
            httpServletRequest.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
        }
    }
}
```









# 三、Listener

## 3.1 概述

* 举例生活中监听的案例（商店报警装置，房间失火喷水装置）

## 3.2 java中的监听器

* 监听器是一个类，监听另一个java类的状态的改变
* JavaWeb三大组件(Servlet/Filter/Listener),监听器的创建与其他组件类似,只不过由于功能不同,web中定义了很多的监听器接口,根据实际需求去实现对应的监听器接口实现功能
* 由于是web端代码,所以只能监听web端应用对象(request/session/application)

**组成部分**

* 事件源：被监听的对象(request/session/application)
* 监听器：监听的对象(我们创建实现对应监听接口的类)
* 监听方法 : 实现监听接口重写的对应方法
* 事件对象的作用：可以获取到的事件源对象(在监听器中事件源对象)

## 3.3 web监听器

由于域对象添加方法与修改方法都是使用setAttribute("key","value")形式添加,所以在进行操作代码书写可能出现混淆,但是监听器不会混淆,监听器只会根据对应的事件执行对应的方法,只有当前对应的域对象中没有数据时调用setAttribute监听器才会执行对应的监听方法,如果已存在监听器之后执行的都是监听修改的方法(相同的key 监听器添加数据方法只执行一次),同理在删除对应数据时也一样,当对应域中不存在对应的key时,即使执行的对应的removeAttribute方法,监听器对应方法也不会执行





### 3.3.1 请求域对象监听器

#### **1) 对请求对象的创建与销毁进行监听**

MyRequestListener.java

```java
//监听请求域对象的创建与销毁
public class MyRequestListener implements ServletRequestListener {

	// 创建请求对象的监听方法
	public void requestInitialized(ServletRequestEvent sre) {
		// ServletRequestEvent代表请求事件对象
		// 可以获取请求对象相应的数据
		// 获取当前发生事件的请求对象
		ServletRequest servletRequest = sre.getServletRequest();
		// 之后可以通过请求对象获取请求域中相应的数据
		System.out.println("请求对象创建");
	}

	// 销毁请求对象的监听方法
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("请求对象销毁");
	}
}
```

web.xml

```xml
<listener>
    <listener-class>com.yh.listener.MyRequestListener</listener-class>
</listener>
```



#### 2) 对请求域对象中的数据进行监听

MyRequestAttrListener.java

```java
//监听请求域对象中数据的操作
public class MyRequestAttrListener implements ServletRequestAttributeListener {
	// 当调用setAttribute时添加新的数据时执行的方法
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		// ServletRequestAttributeEvent 代表域对象值改变事件对象
		// 可以从该对象中获取改变时域对象的值
		// 获取对应的请求对象
		ServletRequest servletRequest = srae.getServletRequest();
		// 获取发生事件的key
		String name = srae.getName();
		// 获取发生事件的value
		Object value = srae.getValue();
		System.out.println("域对象添加数据:" + name + "=>" + value);
	}

	// 当调用removeAttribute删除数据时执行的方法
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		// 获取发生事件的key
		String name = srae.getName();
		// 获取发生事件的value
		Object value = srae.getValue();
		System.out.println("域对象删除数据:" + name + "=>" + value);
	}

	// 当调用setAttribute时修改已有数据时执行的方法
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		// 获取发生事件的key
		String name = srae.getName();
		// 获取发生事件的value
		Object value = srae.getValue();
		//修改获取的是修改前的数据
		System.out.println("域对象修改数据:" + name + "=>" + value);
		//修改后的数据可以通过获取请求对象之后获取对应的key
		ServletRequest servletRequest = srae.getServletRequest();
		System.out.println("修改后");
		System.out.println("域对象修改数据:" + name + "=>" + servletRequest.getAttribute(name));
	}
}
```

web.xml

```xml
  <listener>
    <listener-class>com.yunhe.listener.MyRequestAttrListener</listener-class>
  </listener>
```



### 3.3.2 应用域对象监听器



#### **1) 对应用域对象的创建与销毁和数据的修改进行监听**

MyApplicationListener.java

```java
//ServletContextListener 监听应用于对象的创建与销毁
//ServletContextAttributeListener  监听域对象中数据的创建 修改 删除
public class MyApplicationListener implements ServletContextListener, ServletContextAttributeListener {

	// 当服务器创建时执行
	public void contextInitialized(ServletContextEvent sce) {
		// ServletContextEvent 代表发生事件的对象
		// 可以由该获取域对象 继续操作获取域对象中数据
		ServletContext servletContext = sce.getServletContext();
		System.out.println("应用域对象创建");
	}

	// 当服务器正常关闭时时执行
	public void contextDestroyed(ServletContextEvent sce) {
		// 注意:应用域对象随服务器的启动与关闭创建与销毁
		System.out.println("应用域对象销毁");
	}

	// 监听域对象数据的添加
	public void attributeAdded(ServletContextAttributeEvent scae) {
		// ServletContextAttributeEvent 代表发生事件的对象
		// 获取key
		String name = scae.getName();
		// 获取value
		Object value = scae.getValue();
		System.out.println("应用域对象存值:" + name + "=>" + value);
	}

	// 监听域对象数据的修改
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		// 获取key
		String name = scae.getName();
		// 获取value
		Object value = scae.getValue();
		Object newValue = scae.getServletContext().getAttribute(name);
		System.out.println("应用域对象修改数据:将"+name+"  "+value+"=>"+newValue);
	}

	// 监听域对象数据的删除
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		// 获取key
		String name = scae.getName();
		// 获取value
		Object value = scae.getValue();
		System.out.println("应用域对象删除数据:"+name+"=>"+value);
	}
}
```

web.xml

```xml
  <listener>
    <listener-class>com.yunhe.listener.MyApplicationListener</listener-class>
  </listener>
```





### 3.3.3 会话域对象监听器



#### **1) 对会话域的创建与销毁和数据的修改进行监听**

MySessionListener.java

```java
//@WebListener注解形式创建listener对象 在加载类时会自定进行监听器的配置
//HttpSessionListener  session对象创建销毁监听器接口
//HttpSessionAttributeListener  session对象值操作监听器接口
@WebListener
public class MySessionListener implements  HttpSessionListener, HttpSessionAttributeListener {

    //HttpSessionListener 提供的监听session创建的方法
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //HttpSessionEvent 代表发生事件时的对象 可以获取相应的信息
        HttpSession session = se.getSession();
        System.out.println("session创建:"+session.getId());
    }

    //HttpSessionListener 提供的监听session销毁的方法
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        System.out.println("session销毁:"+session.getId());
    }

    //HttpSessionAttributeListener 提供监听session对象添加新的数据时执行
    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        //HttpSessionBindingEvent 代表监听对象
        //获取添加数据的key
        String name = sbe.getName();
        //获取添加数据的value
        Object value = sbe.getValue();
        System.out.println("session添加数据:"+name+"=>"+value);
    }
    //HttpSessionAttributeListener 提供监听session对象修改数据时执行
    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        //获取session对象
        HttpSession session = sbe.getSession();

        //获取添加数据的key
        String name = sbe.getName();
        //获取添加数据的value
        Object value = sbe.getValue();
        Object newValue = session.getAttribute(name);
        System.out.println("session修改数据:"+name+" "+value+"=>"+newValue);
    }
    ////HttpSessionAttributeListener 提供监听在session对象删除数据时执行
    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        //获取添加数据的key
        String name = sbe.getName();
        //获取添加数据的value
        Object value = sbe.getValue();
        System.out.println("session删除数据:"+name+"=>"+value);
    }
}
```





#### 2) 监听HttpSession中javaBean状态的改变

* 概述

  监听器是监听HttpSession域对象的，操作的都是javaBean，在web.xml中不需要进行配置（编写javaBean类实现监听器接口）

  因为session域对象存储数据为Object所以是可以存储自定义对象的,有时我们需要知道session在什么时候绑定了对象,就可以使用指定的对象实现HttpSessionBindingListener接口完成需求

* 有关接口

  HttpSessionBindingListener

  ```java
    //创建session域对象要保存对象 并实现HttpSessionBindingListener接口
    public class User implements HttpSessionBindingListener {
        private String username;
        private String password;
    
        public User() {
        }
    
        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
      }
    
        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    
        //当当前类的对象被session绑定时 执行的方法
        @Override
        public void valueBound(HttpSessionBindingEvent event) {
            // HttpSessionBindingEvent 发生事件的事件对象
            //获取绑定时的key
            String name = event.getName();
            //获取绑定时的value
            Object value = event.getValue();
            System.out.println("session绑定了User对象:"+name+"=>"+value);
        }
    
        //当当前类的对象被session解绑时  执行的方法
        @Override
        public void valueUnbound(HttpSessionBindingEvent event) {
    
            String name = event.getName();
            Object value = event.getValue();
            System.out.println("session解绑了User对象:"+name+"=>"+value);
        }
    }
  ```
  


与其他的修改不同,在解除绑定后对应的对象可能就会被回收,所以不能获取对应的值

#### 3) 监听session对象的钝化与活化

* 概述

  session对象存储在服务器中,但是当服务关闭时可能还存在很多没有到达过期时间的session对象,导致session没有到达过期时间就会被删除,tomcat容器提供了钝化与活化方法解决这一问题

  钝化:通过序列化的方式将session对象以及存储的数据转存到硬盘中

  活化:当服务器启动时,通过反序列化将session对象读取到内存中

* 有关接口

  HttpSessionActivationListener
  
  同样需要将session存储的对象实现对应的监听方法,为了完成钝化与活化要求对象实现序列化接口

 ```java
/**
 * 目的：完成钝化和活化
 * 钝化是序列化到磁盘上（实现Serializable接口）
 * @author Administrator
 */
public class User2 implements Serializable,HttpSessionActivationListener{
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 活化
	 */
	public void sessionDidActivate(HttpSessionEvent se) {
		  HttpSession session = se.getSession();
      System.out.println("session活化"+session.getId());
	}
	
	/**
	 * 钝化
	 */
	public void sessionWillPassivate(HttpSessionEvent se) {
	 HttpSession session = se.getSession();
      System.out.println("session钝化"+session.getId());
	}

}
 ```

* 好处：对session机制的优化

可以在META-INF下创建context.xml设置钝化保存位置

* ```xml
    <Context>
        <Manager className="org.apache.catalina.session.PersistentManager" maxIdleSwap="1">
        <Store className="org.apache.catalina.session.FileStore" directory="D://yunhe"/>
        </Manager>
    </Context>
   ```
   



钝化后会使用sessionid作为文件名进行保存,只有在服务器再次启动,客户端再次请求时服务器获取sessionid之后进行活化




