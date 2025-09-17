经常用 `IDEA` 写代码的朋友都知道，用 `@Autowired` 依赖注入总是会有一堆警告。

于是很多朋友改用 `@Resource` 。警告是没了，但实际上Spring官方最推荐的即不是 `@Autowired` 也不是 `@Resource` ，而是 **构造器注入** 。

### 1\. 先看三种写法长啥样

**@Autowired写法（字段注入）：**

```java
@Service
public class UserService {
    @Autowired  // 直接写在字段上
    private UserMapper userMapper;
    
    public void getUser() {
        userMapper.findUser();  // 可能空指针！
    }
}
```

**@Resource写法：**

```java
@Service
public class UserService {
    @Resource  // JSR-250标准注解
    private UserMapper userMapper;
}
```

**构造器注入写法：**

```java
@Service
public class OrderService {

    private final UserService userService;

    public OrderService(UserService userService) {
        this.userService = userService;
    }

    public void createOrder() {
        userService.saveLog("下单了");
    }
}
```

构造器方式可以更简洁点，用 `Lombok` ，加个 `@RequiredArgsConstructor` 注解：

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;

    public void createOrder() {
        userService.saveLog("下单了");
    }
}
```

### 2\. 为什么Spring官方推荐构造器注入？

来看一个例子:

上周我们线上出了事故，原因是因为 `@Autowired` 注入为 `null` ！

```java
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;  // 可能为null！
    
    public void createOrder() {
        paymentService.pay();  // 这里报空指针！
    }
}
```

**为什么为null？**

- • `Spring` 容器启动时， `Bean` 的创建顺序不确定
- • `OrderService` 可能比 `PaymentService` 先创建
- • 这时候 `paymentService` 就是 `null`

**如果用构造器注入：**

```java
@Service
public class OrderService {
    private final PaymentService paymentService;
    private final LogisticsService logisticsService;
    
    // 构造器注入
    public OrderService(PaymentService paymentService, 
                       LogisticsService logisticsService) {
        // 如果paymentService为null，这里直接报错！
        this.paymentService = paymentService;
        this.logisticsService = logisticsService;
    }
    
    public void createOrder() {
        paymentService.pay();  // 这里是绝对安全的！
        logisticsService.ship();
    }
}
```

**构造器注入的四大好处：**

1. 1\. **依赖不为空** \- 如果为null，启动就报错，早发现早治疗
2. 2\. **可以用final** \- 防止被意外修改
3. 3\. **代码更清晰** \- 一看构造器就知道需要哪些依赖
4. 4\. **易于测试** \- 单元测试时直接new对象，不用反射设置字段

### 3\. @Autowired和@Resource到底啥区别？

**简单总结：**

- • `@Autowired` ：Spring早期推荐的方式，按类型（byType）注入
- • `@Resource` ：Java标准注解，按名称（byName）注入

**看个实际案例（代码注释有说明）：**

```java
public interface MessageService {
    void send();
}

@Service("smsService")  // 指定bean名称
public class SmsService implements MessageService {
    public void send() { System.out.println("发短信"); }
}

@Service("emailService")  // 另一个实现
public class EmailService implements MessageService {
    public void send() { System.out.println("发邮件"); }
}

// 案例1：@Autowired会报错
@Service
public class NotificationService {
    @Autowired  // 报错！找到两个MessageService实现
    private MessageService messageService;
}

// 案例2：@Resource可以指定名称
@Service
public class NotificationService {
    @Resource(name = "smsService")  // 明确要哪个
    private MessageService messageService;
}

// 案例3：@Autowired+@Qualifier也可以
@Service
public class NotificationService {
    @Autowired
    @Qualifier("emailService")  // 配合使用
    private MessageService messageService;
}
```

### 4\. 实际开发中怎么选择？

**记住这个选择指南：**

#### 场景1：必需依赖 → 用构造器注入

```java
@Service
public class UserService {
    private final UserMapper userMapper;  // 必需
    private final OrderService orderService;  // 必需
    
    public UserService(UserMapper userMapper, OrderService orderService) {
        this.userMapper = userMapper;
        this.orderService = orderService;
    }
}
```

#### 场景2：可选依赖 → 用@Autowired（不影响主流程的依赖）

```
@Service
public class UserService {
    @Autowired(required = false)  // 可选的，没有也不会报错
    private BonusService bonusService;
}
```

#### 场景3：多个实现选特定 → 用@Resource

```java
@Service
public class UserService {
    @Resource(name = "wechatNotify")  // 明确要微信通知
    private NotifyService notifyService;
}
```

#### 场景4：Setter方法注入 → 用于可选依赖

```java
@Service
public class UserService {
    private ConfigService configService;
    
    @Autowired  // 写在setter方法上
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
}
```

### 5\. 综合案例

来看一个完整的用户服务类就一目了然了：

```java
@Service
public class UserService {
    // 必需的核心依赖：构造器注入
    private final UserMapper userMapper;
    private final OrderService orderService;
    
    // 可选的增强功能：@Autowired
    @Autowired(required = false)
    private VIPService vipService;
    
    // 多个通知实现：用@Resource指定
    @Resource(name = "smsNotify")
    private NotifyService notifyService;
    
    // 配置信息：setter注入
    private ConfigService configService;
    
    // 构造器注入必需依赖
    public UserService(UserMapper userMapper, OrderService orderService) {
        this.userMapper = userMapper;
        this.orderService = orderService;
    }
    
    // setter注入可选配置
    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
    
    public void register(User user) {
        userMapper.save(user);
        orderService.initUserOrder(user);
        
        if (vipService != null) {
            vipService.activateTrial(user);
        }
        
        notifyService.sendWelcome(user);
    }
}
```

### 6\. 单元测试

构造器的注入可以让单元测试变得超级简单：

```java
// 生产代码
@Service
public class UserService {
    private final UserMapper userMapper;
    
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}

// 测试代码
public class UserServiceTest {
    @Test
    public void testUserService() {
        // 直接new，不用Mockito注解
        UserMapper mockMapper = Mockito.mock(UserMapper.class);
        UserService userService = new UserService(mockMapper);
        
        // 写测试用例...
    }
}
```

### 7\. 总结一下选择策略

| 场景 | 推荐方式 | 示例 |
| --- | --- | --- |
| 必需依赖 | 构造器注入 | `public UserService(UserMapper mapper)` |
| 可选依赖 | @Autowired(required=false) | `@Autowired(required=false)` |
| 按名称注入 | @Resource(name="xxx") | `@Resource(name="smsService")` |
| 可选配置 | Setter注入 | `@Autowired public void setXxx()` |

**黄金法则：**

1\. **首选构造器注入** \- 特别是核心业务依赖
2\. **次要选择setter注入** \- 用于可选配置
3\. **按名注入用@Resource** \- 多个实现时
4\. **尽量避免字段注入** \- 容易产生空指针
