Gradle 提供了两种构建脚本语言，Groovy DSL（领域特定语言）和 Kotlin DSL。Groovy 是 Gradle 的默认脚本语言，而 Kotlin DSL 则是在 Kotlin 语言的基础上为 Gradle 提供的构建脚本支持。两者之间的语法有所不同，但实现的功能基本相同。

下面，我将对 `build.gradle` 文件进行讲解，并分为 Groovy DSL 和 Kotlin DSL 两种方式进行说明。

### 1. **插件声明** (`plugins` 或 `apply plugin`)

#### Groovy DSL

```groovy
plugins {
    id 'java'  // Java 插件
    id 'application'  // 应用插件
}
```

- `id 'java'`：使用 Java 插件，表示该项目是一个 Java 项目。
- `id 'application'`：使用应用插件，通常用于创建可执行的 Java 应用程序。

#### Kotlin DSL

```kotlin
plugins {
    id("java")  // Java 插件
    id("application")  // 应用插件
}
```

- 在 Kotlin DSL 中，插件 ID 使用双引号而不是单引号。

---

### 2. **仓库声明** (`repositories`)

#### Groovy DSL

```groovy
repositories {
    mavenCentral()  // 使用 Maven Central 仓库
}
```

- `mavenCentral()`：指定 Gradle 从 Maven Central 仓库下载依赖。

#### Kotlin DSL

```kotlin
repositories {
    mavenCentral()  // 使用 Maven Central 仓库
}
```

- Kotlin DSL 中的语法几乎与 Groovy DSL 相同。

---

### 3. **依赖声明** (`dependencies`)

#### Groovy DSL

```groovy
dependencies {
    implementation 'org.springframework:spring-core:5.3.8'  // 主要依赖
    testImplementation 'junit:junit:4.13.2'  // 测试依赖
}
```
#### **依赖配置范围**

- `implementation`：主代码使用的依赖，编译期和运行期都需要。
- `testImplementation`：测试代码所需的依赖，仅在测试时可用。
- `compileOnly`：仅编译时可用，运行时不需要。
- `runtimeOnly`：仅运行时需要，编译时不需要。

#### Kotlin DSL

```kotlin
dependencies {
    implementation("org.springframework:spring-core:5.3.8")  // 主要依赖
    testImplementation("junit:junit:4.13.2")  // 测试依赖
}
```

- Kotlin DSL 语法相似，但字符串使用双引号。

---

### 4. **任务配置** (`task`)

#### Groovy DSL

```groovy
task hello {
    doLast {
        println 'Hello, Gradle!'
    }
}
```

- 定义一个名为 `hello` 的任务，任务执行后输出 "Hello, Gradle!"。

#### Kotlin DSL

```kotlin
tasks.register("hello") {
    doLast {
        println("Hello, Gradle!")
    }
}
```

- Kotlin DSL 使用 `tasks.register` 来定义任务，并且字符串常量使用双引号。

---

### 5. **构建配置** (`buildscript`)

#### Groovy DSL

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.springframework.boot:spring-boot-gradle-plugin:2.4.5'
    }
}
```

- `repositories`：指定构建脚本的依赖仓库。
- `dependencies`：指定构建脚本所需的依赖项。

#### Kotlin DSL

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.4.5")
    }
}
```

- Kotlin DSL 语法与 Groovy 类似，但依赖项使用双引号。

---

### 6. **版本声明** (`version` 和 `group`)

#### Groovy DSL

```groovy
group = 'com.example'
version = '1.0.0'
```

- `group`：定义项目的分组 ID。
- `version`：定义项目的版本号。

#### Kotlin DSL

```kotlin
group = "com.example"
version = "1.0.0"
```

- Kotlin DSL 使用双引号来定义字符串。

---

### 7. **Java 配置** (`java`)

#### Groovy DSL

```groovy
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

- `sourceCompatibility`：设置源码的 Java 版本。
- `targetCompatibility`：设置编译后的字节码版本。

#### Kotlin DSL

```kotlin
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

- Kotlin DSL 中的 `java` 配置和 Groovy 非常相似。

---

### 8. **应用程序配置** (`application`)

#### Groovy DSL

```groovy
application {
    mainClass = 'com.example.Main'  // 指定主类
}
```

- `mainClass`：指定可执行的主类。

#### Kotlin DSL

```kotlin
application {
    mainClass.set("com.example.Main")  // 指定主类
}
```

- 在 Kotlin DSL 中，`mainClass` 使用 `set` 方法来设置值。

---

### 9. **自定义配置** (`ext`)

#### Groovy DSL

```groovy
ext {
    springVersion = '5.3.8'
    junitVersion = '4.13.2'
}
```

- `ext` 用于定义全局变量或自定义属性，可以在其他部分引用。

#### Kotlin DSL

```kotlin
extra["springVersion"] = "5.3.8"
extra["junitVersion"] = "4.13.2"
```

- Kotlin DSL 使用 `extra` 属性来定义自定义变量，采用键值对方式。

---

### 完整示例

#### Groovy DSL

```groovy
plugins {
    id 'java'
    id 'application'
}

group = 'com.example'
version = '1.0.0'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework:spring-core:5.3.8'
    testImplementation 'junit:junit:4.13.2'
}

application {
    mainClass = 'com.example.Main'
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

#### Kotlin DSL

```kotlin
plugins {
    id("java")
    id("application")
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-core:5.3.8")
    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass.set("com.example.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

### 总结

- **Groovy DSL** 和 **Kotlin DSL** 在很多配置上几乎相同，主要区别是语法。Groovy 使用的是动态类型，支持 DSL 语法，而 Kotlin 提供了静态类型支持和更严格的编译时检查。
- Kotlin DSL 的优势在于它能享受 Kotlin 的类型推断和智能提示，可以提高开发效率，尤其是在大型项目中。