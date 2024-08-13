当然，可以将 MongoDB 连接配置和连接池管理放在一个单独的类中，这样可以更好地组织代码，并在多个地方重用连接配置。下面是如何实现这个功能的示例。

### 目录结构
```
src/
├── config/
│   └── MongoDBConnection.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   └── UserService.java
└── Main.java
```

### 配置类（MongoDBConnection）

`config/MongoDBConnection.java`
```java
package config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;

import java.util.concurrent.TimeUnit;

public class MongoDBConnection {
    private static MongoClient mongoClient;

    static {
        // 配置连接池
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                .maxSize(50)  // 最大连接数
                .minSize(10)  // 最小连接数
                .maxConnectionIdleTime(30, TimeUnit.SECONDS)  // 最大空闲时间
                .maxWaitTime(30, TimeUnit.SECONDS)  // 获取连接的最大等待时间
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                .build();

        mongoClient = MongoClients.create(settings);
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }
}
```

### 数据访问层（UserRepository）

`repository/UserRepository.java`
```java
package repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.MongoDBConnection;
import model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private MongoCollection<Document> collection;

    public UserRepository() {
        MongoClient mongoClient = MongoDBConnection.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("test_db");
        collection = database.getCollection("users");
    }

    public void insertUser(User user) {
        Document document = new Document("name", user.getName()).append("age", user.getAge());
        collection.insertOne(document);
    }

    public User findUserByName(String name) {
        Document query = new Document("name", name);
        Document result = collection.find(query).first();
        if (result != null) {
            User user = new User(result.getString("name"), result.getInteger("age"));
            user.setId(result.getObjectId("_id"));
            return user;
        }
        return null;
    }

    public void updateUserAge(String name, int age) {
        Document query = new Document("name", name);
        Document update = new Document("$set", new Document("age", age));
        collection.updateOne(query, update);
    }

    public void deleteUserByName(String name) {
        Document query = new Document("name", name);
        collection.deleteOne(query);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            User user = new User(doc.getString("name"), doc.getInteger("age"));
            user.setId(doc.getObjectId("_id"));
            users.add(user);
        }
        return users;
    }
}
```

### 模型层（User）

`model/User.java`
```java
package model;

import org.bson.types.ObjectId;

public class User {
    private ObjectId id;
    private String name;
    private int age;

    public User() {}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', age=" + age + "}";
    }
}
```

### 业务逻辑层（UserService）

`service/UserService.java`
```java
package service;

import model.User;
import repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public void addUser(User user) {
        userRepository.insertUser(user);
    }

    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public void updateUserAge(String name, int age) {
        userRepository.updateUserAge(name, age);
    }

    public void deleteUser(String name) {
        userRepository.deleteUserByName(name);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
```

### 表示层（Main）

`Main.java`
```java
import model.User;
import service.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // 添加用户
        User user1 = new User("Alice", 25);
        userService.addUser(user1);

        User user2 = new User("Bob", 30);
        userService.addUser(user2);

        // 查询用户
        User alice = userService.getUserByName("Alice");
        System.out.println("Found user: " + alice);

        // 更新用户年龄
        userService.updateUserAge("Alice", 26);

        // 查询所有用户
        List<User> users = userService.getAllUsers();
        System.out.println("All users:");
        for (User user : users) {
            System.out.println(user);
        }

        // 删除用户
        userService.deleteUser("Bob");

        // 查询所有用户
        users = userService.getAllUsers();
        System.out.println("All users after deletion:");
        for (User user : users) {
            System.out.println(user);
        }
    }
}
```

### 解释

1. **`MongoDBConnection` 类**：这个类负责配置和管理 MongoDB 连接池，并提供一个静态方法 `getMongoClient()` 来获取 MongoClient 实例。
2. **`UserRepository` 类**：通过调用 `MongoDBConnection.getMongoClient()` 获取 MongoDB 客户端实例，从而与数据库进行交互。
3. **`UserService` 和 `Main` 类**：不需要修改，依旧可以通过 `UserRepository` 进行数据库操作。

这种方式将连接池管理和数据库操作解耦，使得代码更具可维护性和重用性。