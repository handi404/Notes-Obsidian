

**Springboot项目启动后自动打开浏览器访问**

1、在Springboot项目中每次启动完项目，手动在浏览器输入访问地址太麻烦了。在启动类中加入下方代码，就可高效地在控制台中单击URL访问项目了~  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/621181947b71c4863825191678513d3c.png#pic_center)  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d48415c16252b16801520b0419320c04.png#pic_center)

**示例代码：**

```java
@SpringBootApplication
@Slf4j
public class WebApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(WebApplication.class, args);
                System.out.println(
            "  ____  __  __    ___    ___     __    ____    ____  \n" +
            " /',__\\/\\ \\/\\ \\  /'___\\ /'___\\ /'__`\\ /',__\\  /',__\\ \n" +
            "/\\__, `\\ \\ \\_\\ \\/\\ \\__//\\ \\__//\\  __//\\__, `\\/\\__, `\\\n" +
            "\\/\\____/\\ \\____/\\ \\____\\ \\____\\ \\____\\/\\____/\\/\\____/\n" +
            " \\/___/  \\/___/  \\/____/\\/____/\\/____/\\/___/  \\/___/ \n");
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t" +
                "----------------------------------------------------------");
String jvmName = ManagementFactory.getRuntimeMXBean().getName();
log.info("当前项目进程号：" + jvmName.split("@")[0]);
    }

```

**2、此外，还可以设置打开系统默认浏览器，并加载指定的页面。如下添加监听类。**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/fe34916160a97881bf5fb1bb8895573c.png#pic_center)

**示例代码：**

```java
package com.yc.star.web.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * spring boot 容器加载后自动监听
 */
@Component
public class MyCommandRunner implements CommandLineRunner {

    @Value("${spring.web.loginurl}")
    private String loginUrl;

    @Value("${spring.auto.openurl}")
    private boolean isOpen;

    @Override
    public void run(String... args) {
        if (isOpen) {
            System.out.println("自动加载指定的页面");
            try {
                Runtime.getRuntime().exec("cmd /c start " + loginUrl);  // 可以指定自己的路径
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("浏览器打开页面异常");
            }
        }
    }

}

```

**3、在application.yml文件中配置相关的参数：**  
![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/0a6327dad9a9d1a0cd4d60233392fe4f.png#pic_center)  
spring:  
auto:  
openurl: true # 是否自动打开浏览器，false为否  
web:  
loginurl: http://localhost:8090 # 指定加载的页面地址

至此，可愉快地启动项目，等待浏览器自动加载我们指定的页面。