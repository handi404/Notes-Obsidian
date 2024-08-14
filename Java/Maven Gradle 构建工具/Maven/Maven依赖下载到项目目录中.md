将Maven依赖下载到项目目录中，而不是默认的用户目录（通常是 `~/.m2/repository`），可以通过以下两种方式实现：

### 方式 1：使用Maven依赖拷贝插件（Maven Dependency Plugin）

Maven提供了`maven-dependency-plugin`，可以将依赖下载到项目的特定目录中。你可以将依赖复制到项目的`lib`目录或其他自定义目录中。

#### 1.1 在`pom.xml`中添加`maven-dependency-plugin`

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>3.1.2</version>
            <executions>
                <execution>
                    <id>copy-dependencies</id>
                    <phase>package</phase>
                    <goals>
                        <goal>copy-dependencies</goal>
                    </goals>
                    <configuration>
                        <outputDirectory>${project.build.directory}/lib</outputDirectory>
                        <overWriteReleases>false</overWriteReleases>
                        <overWriteSnapshots>true</overWriteSnapshots>
                        <overWriteIfNewer>true</overWriteIfNewer>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### 1.2 运行Maven命令

运行以下命令，将依赖复制到项目的`target/lib`目录下：

```bash
mvn package
```

### 方式 2：指定本地仓库路径

你可以通过设置Maven的本地仓库路径，将依赖下载到项目目录中的某个自定义位置。

#### 2.1 修改Maven的`settings.xml`

修改Maven的配置文件`settings.xml`，将本地仓库路径改为项目的`lib`目录。`settings.xml`文件通常位于以下路径：

- Windows: `%MAVEN_HOME%\conf\settings.xml`
- Linux/Mac: `/etc/maven/settings.xml` 或 `~/.m2/settings.xml`

在`settings.xml`中添加或修改`localRepository`配置：

```xml
<settings>
    <localRepository>${basedir}/lib/repository</localRepository>
</settings>
```

这样，Maven会将依赖下载到你项目根目录下的`lib/repository`文件夹中。

### 注意事项

- **第一种方式**：依赖仍然会首先下载到默认的`.m2/repository`，然后通过插件复制到指定目录。这个方法适用于你希望在项目中便捷访问依赖文件的情况。
  
- **第二种方式**：完全改变了Maven的本地仓库路径，依赖将直接下载到项目目录中。使用这种方式需要注意可能导致不同项目之间的依赖冲突，因为Maven的依赖将不再被全局共享。

### 总结

- 如果你只是想在项目中使用依赖文件（例如打包发布），推荐使用第一种方法，通过`maven-dependency-plugin`将依赖复制到项目的`lib`目录。
- 如果你希望彻底改变Maven的依赖管理路径，可以修改Maven的本地仓库配置，但请谨慎操作，以免影响其他项目的依赖管理。