在 C++ 中实现与 MongoDB 的连接池，并使用多层架构进行操作，可以使用 `mongocxx` 驱动。这个驱动提供了对 MongoDB 的强大支持，包括连接池的管理。以下是如何实现这一功能的详细步骤。

### 目录结构
```
src/
├── config/
│   └── MongoDBConnectionPool.h
├── model/
│   └── User.h
├── repository/
│   └── UserRepository.h
│   └── UserRepository.cpp
├── service/
│   └── UserService.h
│   └── UserService.cpp
└── main.cpp
```

### 安装依赖
首先，需要安装 MongoDB C++ 驱动 `mongocxx` 以及其依赖 `libbson` 和 `libmongoc`。可以参考官方文档进行安装：[mongocxx installation guide](http://mongocxx.org/mongocxx-v3/installation/)

### 配置类（MongoDBConnectionPool）

`config/MongoDBConnectionPool.h`
```cpp
#pragma once

#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/pool.hpp>
#include <memory>

namespace config {
    class MongoDBConnectionPool {
    public:
        static MongoDBConnectionPool& getInstance() {
            static MongoDBConnectionPool instance;
            return instance;
        }

        std::shared_ptr<mongocxx::pool> getPool() {
            return pool;
        }

    private:
        MongoDBConnectionPool() {
            mongocxx::instance instance{};
            mongocxx::uri uri("mongodb://localhost:27017");
            pool = std::make_shared<mongocxx::pool>(uri);
        }

        std::shared_ptr<mongocxx::pool> pool;
    };
}
```

### 模型类（User）

`model/User.h`
```cpp
#pragma once

#include <bsoncxx/json.hpp>
#include <bsoncxx/builder/basic/document.hpp>

namespace model {
    class User {
    public:
        User(std::string name, int age) : name(name), age(age) {}

        std::string getName() const { return name; }
        int getAge() const { return age; }

        bsoncxx::document::value toBson() const {
            using namespace bsoncxx::builder::basic;
            return make_document(kvp("name", name), kvp("age", age));
        }

    private:
        std::string name;
        int age;
    };
}
```

### 数据访问层（UserRepository）

`repository/UserRepository.h`
```cpp
#pragma once

#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/pool.hpp>
#include <mongocxx/uri.hpp>
#include <mongocxx/collection.hpp>
#include "../config/MongoDBConnectionPool.h"
#include "../model/User.h"
#include <vector>
#include <memory>

namespace repository {
    class UserRepository {
    public:
        UserRepository() {
            auto pool = config::MongoDBConnectionPool::getInstance().getPool();
            auto client = pool->acquire();
            database = (*client)["test_db"];
            collection = database["users"];
        }

        void insertUser(const model::User& user) {
            collection.insert_one(user.toBson().view());
        }

        std::vector<model::User> getAllUsers() {
            std::vector<model::User> users;
            auto cursor = collection.find({});
            for (auto&& doc : cursor) {
                std::string name = doc["name"].get_utf8().value.to_string();
                int age = doc["age"].get_int32();
                users.emplace_back(name, age);
            }
            return users;
        }

    private:
        mongocxx::database database;
        mongocxx::collection collection;
    };
}
```

### 业务逻辑层（UserService）

`service/UserService.h`
```cpp
#pragma once

#include "../model/User.h"
#include "../repository/UserRepository.h"
#include <vector>

namespace service {
    class UserService {
    public:
        UserService() : userRepository(std::make_shared<repository::UserRepository>()) {}

        void addUser(const model::User& user) {
            userRepository->insertUser(user);
        }

        std::vector<model::User> getAllUsers() {
            return userRepository->getAllUsers();
        }

    private:
        std::shared_ptr<repository::UserRepository> userRepository;
    };
}
```

### 主程序（main）

`main.cpp`
```cpp
#include <iostream>
#include "service/UserService.h"

int main() {
    service::UserService userService;

    // 添加用户
    model::User user1("Alice", 25);
    userService.addUser(user1);

    model::User user2("Bob", 30);
    userService.addUser(user2);

    // 查询所有用户
    auto users = userService.getAllUsers();
    for (const auto& user : users) {
        std::cout << "User: " << user.getName() << ", Age: " << user.getAge() << std::endl;
    }

    return 0;
}
```

### 解释

1. **MongoDBConnectionPool 类**：使用单例模式管理 MongoDB 连接池。`getPool()` 方法返回一个共享指针，指向连接池实例。
2. **UserRepository 类**：通过获取连接池实例来操作 MongoDB 数据库。实现了 `insertUser` 和 `getAllUsers` 方法。
3. **UserService 类**：业务逻辑层，通过 `UserRepository` 来管理用户数据。
4. **main 函数**：主程序，演示了添加用户和查询用户的功能。

这种多层架构将数据库连接池的管理、数据访问和业务逻辑分离，增强了代码的可维护性和可扩展性。