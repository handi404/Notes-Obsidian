使用 Eclipse IDE 创建可执行 jar
-----------------------------------------
1. Right click on Java project (pick a project with a GUI)
   右键单击 Java 项目（选择一个带有 GUI 的项目）
2. Export
   导出
3. Java , Runnable Jar file
   Java、可运行的Jar文件
4. At Launch configuration select your project
   在“启动配置”中，选择您的项目
5. At Export destination, select where you want this jar file exported to
   在“导出目标”中，选择要将此 jar 文件导出到的位置
6. Finish
   完成

使用 IntelliJ 创建可执行 jar
-----------------------------------------
1. File , Project Structure , Artifacts , (+) , JAR ,  jar from module with dependencies
   文件、项目结构、工件、（+）、JAR、带有依赖项的模块中的jar
2. Main Class: select the class containing your main method
   主类：选择包含您的主方法的类
3. OK
3. Change output directory to where you want this JAR saved to
   将输出目录更改为您希望将此 JAR 保存到的位置
6. Apply , OK
7. Build (top of window in IntelliJ) , Build Artifacts , Build
   构建（IntelliJ 中的窗口顶部） ， 构建工件 ， 构建

如果您需要图标，请将图像转换为. ico 文件
----------------------------------------------------
1. https://icoconvert.com/（这是一个不错的网站）
2. 选择您的图像（文件类型各不相同）
3. 上传
4. 转换为 ICO
5. 下载您的图标

使用 Launch 4 j 创建带有自定义图标的. exe
----------------------------------------------
1. 安装 Launch 4 j http://launch4j.sourceforge.net/ 
2. 输出文件：浏览输出目标并命名以 .exe 结尾的文件
3. Jar：浏览保存 jar 或可执行 jar 文件的位置
4. （可选）图标：浏览您的自定义图标（必须. ico）
5. 导航到 JRE 选项卡
6. 捆绑的 JRE 路径：浏览 jdk 的位置（示例 C：\Program Files\Java\jdk-14.0.1）
7. 在窗口顶部，单击 Build Wrapper（齿轮图标）
8. 命名并保存 Lauch 4 j 配置文件
9. 您的 application. exe 应保存到您的输出文件位置（不再需要以前的文件）