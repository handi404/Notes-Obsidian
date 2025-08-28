---
title: "PO、VO、BO、DTO、DAO、POJO傻傻分不清楚。。。"
source: "https://mp.weixin.qq.com/s/MkeND4CzdDuGQ2KvcfgyXA"
author:
  - "[[苏三]]"
published:
created: 2025-08-26
description: "最近有小伙伴问我：PO、VO、BO、DTO、DAO、POJO有什么区别？"
tags:
  - "clippings"
---
*2025年08月21日 14:10*

以下文章来源于苏三说技术 ，作者苏三

[

**苏三说技术**.

苏三，曾浪迹几家大厂，掘金优秀创作者，CSDN优质创作者，免费刷题网站：www.susan.net.cn

](https://mp.weixin.qq.com/s/#)

![图片](https://mmbiz.qpic.cn/mmbiz_png/CKvMdchsUwnC6m7jOrSicQNaDib8riaAnaFK0KOd6ZHbncyicVGOznXiaf58n3sehicX4KAibchibmAXEsia90pC3TCa0Nw/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

Boot+Cloud项目学习： macrozheng.com

最近有小伙伴问我：PO、VO、BO、DTO、DAO、POJO有什么区别？

你第一眼看到，可能也会有点懵。

这些对象的概念很多，确实容易搞混。

今天这篇文章跟大家一起聊聊这6种对象的含义、职责、区别和常见的坑，希望对你会有所帮助。

## 一、6种对象的职责边界

**对象设计的本质是关注点分离** ——每个对象只做一件事，且做好它！

### 1.1 PO

它的含义是Persistent Object，即持久化对象。

- **职责** ：与数据库表严格1:1映射， **仅承载数据存储结构**
- **特征** ：
- 属性与表字段完全对应
	- 无业务逻辑方法（仅有getter/setter）
- **代码示例** ：
```
public class UserPO {  
  private Long id;      // 对应表主键  
  private String name;  // 对应name字段
}
```

### 1.2 DAO

它的含义是Data Access Object，即数据访问对象。

- **职责** ： **封装所有数据库操作** （CRUD），隔离业务与存储细节
- **特征** ：
- 接口方法对应SQL操作
	- 返回PO或PO集合
- **代码示例** ：
```
public interface UserDao {  
    // 根据ID查询PO  
    UserPO findById(Long id);  
    
    // 分页查询  
    List<UserPO> findPage(@Param("offset") int offset, @Param("limit") int limit);  
}
```

**底层原理** ：DAO模式 = 接口 + 实现类 + PO

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/ibJZVicC7nz5gIg2BDY56XC8PPKo6PVgk3wgiaPOdNEIicgqQq8qjLuibKvyPnK0WYL2fgicSEeOibp9ofUb8rv49a2iag/640?wx_fmt=png&from=appmsg&wxfrom=5&wx_lazy=1&tp=webp)

### 1.3 BO

它的含义是Business Object，即业务对象。

- **职责** ： **封装核心业务逻辑** ，聚合多个PO完成复杂操作
- **特征** ：
- 包含业务状态机、校验规则
	- 可持有多个PO引用
- **代码示例** ：订单退款BO
```
public class OrderBO {  
   // 主订单数据  
   private OrderPO orderPO;  
   // 子订单项  
   private List<OrderItemPO> items; 
   
   // 业务方法：执行退款  
   public RefundResult refund(String reason) {     
   if (!"PAID".equals(orderPO.getStatus())) {  
      throw new IllegalStateException("未支付订单不可退款"); 
   }  // 计算退款金额、调用支付网关等  }  
}
```

### 1.4 DTO

它的含义是Data Transfer Object，即数据传输对象。

- **职责** ： **跨层/跨服务数据传输** ，屏蔽敏感字段
- **特征** ：
- 属性集是PO的子集（如排除 `password` 字段）
	- 支持序列化（实现 `Serializable` ）
- **代码示例** ：用户信息DTO
```
public class UserDTO implements Serializable {  
    private Long id;  
    private String name;  
}
```

### 1.5 VO

它的含义是View Object，即视图对象。

- **职责** ： **适配前端展示** ，包含渲染逻辑
- **特征** ：
- 属性可包含格式化数据（如日期转 `yyyy-MM-dd` ）
	- 聚合多表数据（如订单VO包含用户名字）
- **代码示例** ：
```
public class OrderVO {  
  private String orderNo;  
  private String createTime; // 格式化后的日期   private String userName;   // 关联用户表字段    
  
  //状态码转文字描述  
  public String getStatusText() {  
     return OrderStatus.of(this.status).getDesc();  
  }  
}
```

### 1.6 POJO

它的含义是Plain Old Java Object，即普通Java对象。

- **职责** ： **基础数据容器** ，可扮演PO/DTO/VO角色
- **特征** ：
- 只有属性+getter/setter
	- 无框架依赖（如不继承Spring类）
- **典型实现** ：Lombok简化代码
```
// 自动生成getter/setter  
@Data 
public class UserPOJO {  
  private Long id;  
  private String name;  
}
```

## 二、主流的对象流转模型

### 场景1

传统三层架构（DAO → DTO → VO）。

**适用系统** ：后台管理系统、工具类应用 **核心流程** ：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/ibJZVicC7nz5gIg2BDY56XC8PPKo6PVgk3MblpCTHeEroCsKXibBG5f4UqeTp1EZ4FFt1OlQNU8mzViaK2ySP7I0Ew/640?wx_fmt=png&from=appmsg&wxfrom=5&wx_lazy=1&tp=webp)

**代码示例** ：用户查询服务

```
// Service层  
public UserDTO getUserById(Long id) {  
   UserPO userPO = userDao.findById(id); // 从DAO获取PO  
   UserDTO dto = new UserDTO();  
   dto.setId(userPO.getId());  
   dto.setName(userPO.getName()); // 过滤敏感字段  
   return dto; // 返回DTO  
}  

// Controller层  
public UserVO getUser(Long id) {  
   UserDTO dto = userService.getUserById(id);  
   UserVO vo = new UserVO();  
   vo.setUserId(dto.getId());  
   vo.setUserName(dto.getName());  
   vo.setRegisterTime(formatDate(dto.getCreateTime())); // 格式化日期  
   return vo;  
}
```

**优点** ：简单直接，适合CRUD场景 **缺点** ：业务逻辑易泄漏到Service层

### 场景2

DDD架构（PO → DO → DTO → VO）。

**适用系统** ：电商、金融等复杂业务系统 **核心流程** ：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/ibJZVicC7nz5gIg2BDY56XC8PPKo6PVgk3cyYFS5TY72RnEibn8ZxLWr0L2BLsYbEwQUf2Epmq3nn9jaiaibmjc0SFw/640?wx_fmt=png&from=appmsg&wxfrom=5&wx_lazy=1&tp=webp)

**关键角色** ：DO（Domain Object）替代BO **代码示例** ：订单支付域

```
// Domain层：订单领域对象  
publicclass OrderDO {  
   private OrderPO orderPO;  
   private PaymentPO paymentPO;  
   
   // 业务方法：支付校验  
   public void validatePayment() {  
     if (paymentPO.getAmount() < orderPO.getTotalAmount()) {  
        thrownew PaymentException("支付金额不足");  
      } 
   }  
}  

// App层：协调领域对象  
public OrderPaymentDTO pay(OrderPayCmd cmd) {  
    OrderDO order = orderRepo.findById(cmd.getOrderId());  
    order.validatePayment(); // 调用领域方法  return OrderConverter.toDTO(order); // 转DTO  
}
```

**优点** ：业务高内聚，适合复杂规则系统 **缺点** ：转换层级多，开发成本高

## 三、高效转换工具

手动转换对象？效率低且易错！

### 3.1 MapStruct：编译期代码生成

**原理** ：APT注解处理器生成转换代码 **示例** ：PO转DTO

```
@Mapper  
publicinterface UserConverter {  
   UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);  
   
   @Mapping(source = "createTime", target = "registerDate")  
   UserDTO poToDto(UserPO po);  
}  

// 编译后生成UserConverterImpl.java  
publicclass UserConverterImpl {  
   public UserDTO poToDto(UserPO po) {  
      UserDTO dto = new UserDTO();  
      dto.setRegisterDate(po.getCreateTime()); // 自动赋值！  
      return dto;  
  }  
}
```

**优点** ：零反射损耗，性能接近手写代码 **开源地址** ：https://github.com/mapstruct/mapstruct

### 3.2 Dozer + Lombok：注解驱动转换

**组合方案** ：

- **Lombok** ：自动生成getter/setter
- **Dozer** ：XML/注解配置字段映射
```
// Lombok注解
@Data 
public class UserVO {  
   private String userId;  
   private String userName;  
}  

// 转换配置  
<field>  
  <a>userId</a>  
  <b>id</b>  
</field>
```

**适用场景** ：字段名不一致的复杂转换

### 3.3 手动Builder模式：精细控制

**适用场景** ：需要动态构造的VO

```
public class OrderVOBuilder {  
   public OrderVO build(OrderDTO dto) {  
     return OrderVO.builder()  
        .orderNo(dto.getOrderNo())  
        .amount(dto.getAmount() + "元") // 动态拼接  
        .statusText(convertStatus(dto.getStatus()))  
        .build();  
    }  
}
```

## 四、避坑指南

### 坑1：PO直接返回给前端

```
// 致命错误：暴露数据库敏感字段！  
public UserPO getUser(Long id) {  
  // 返回的PO包含password  
  return userDao.findById(id); 
}
```

**解决方案** ：

- 使用DTO过滤字段
- 注解屏蔽： `@JsonIgnore`

### 坑2：DTO中嵌入业务逻辑

```
public class OrderDTO {
    // 错误！DTO不应有业务方法  
    public void validate() { 
       if (amount < 0) 
          throw new Exception();  
    }  
}
```

**本质错误** ：混淆DTO与BO的职责

### 坑3：循环嵌套转换

```
// OrderVO中嵌套List<ProductVO>  
public class OrderVO {
   // 嵌套对象  
   private List<ProductVO> products; 
}  

// 转换时触发N+1查询  
orderVO.setProducts(order.getProducts()
       .stream()  
       .map(p -> convertToVO(p)) // 循环查询数据库  
       .collect(toList()));
```

**优化方案** ：批量查询 + 并行转换

### 五、如何选择对象模型？

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/ibJZVicC7nz5gIg2BDY56XC8PPKo6PVgk3U2HvH9LESZ56nHI2uOtdicsW0uGibdBPzJy9s8Odg9hc6JZRuWsPt9rA/640?wx_fmt=png&from=appmsg&wxfrom=5&wx_lazy=1&tp=webp)

## 总结

关于对象的4个核心原则：

1. **单一职责** ： PO只存数据，BO只管业务，VO只负责展示—— **绝不越界！**
2. **安全隔离** ：
- PO永不出DAO层（防数据库泄露）
	- VO永不出Controller（防前端逻辑污染服务）
4. **性能优先** ：
- 大对象转换用 **MapStruct** （编译期生成代码）
	- 嵌套集合用 **批量查询** （杜绝N+1）
6. **适度设计** ：
- 10张表以内的系统：可用POJO一撸到底
	- 百张表以上核心系统： **必须严格分层**

对象设计没有银弹， **理解业务比套用模式更重要** ！

当你在为对象命名纠结时，不妨回到业务的起点问一句：“它此刻的核心职责是什么？”

---

Github上 `标星11K` 的微服务实战项目mall-swarm，全套 [视频教程（2024最新版）](https://mp.weixin.qq.com/s?__biz=MzU1Nzg4NjgyMw==&mid=2247529674&idx=1&sn=96ce25780c426efc8383698c06c2618b&scene=21#wechat_redirect) 来了！全套教程 `约26小时，共59期` ，如果你想学习 `目前最新的微服务技术栈` ，同时提高自己 `微服务项目的开发能力` 的话，不妨了解下，下面是项目的整体架构图，感兴趣的小伙伴可以点击链接 [mall-swarm视频教程](https://mp.weixin.qq.com/s?__biz=MzU1Nzg4NjgyMw==&mid=2247529674&idx=1&sn=96ce25780c426efc8383698c06c2618b&scene=21#wechat_redirect) 加入学习。

![图片](https://mmbiz.qpic.cn/mmbiz_jpg/CKvMdchsUwnC6m7jOrSicQNaDib8riaAnaFV3cnA7qabOnE1ZW0iauhGpMexepxIDpLo96XOzwZHZ3eicfIiaeIiaBP6A/640?wx_fmt=jpeg&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1)

整套 [视频教程](https://mp.weixin.qq.com/s?__biz=MzU1Nzg4NjgyMw==&mid=2247529674&idx=1&sn=96ce25780c426efc8383698c06c2618b&scene=21#wechat_redirect) 的内容还是非常完善的，涵盖Spring Cloud核心组件、微服务项目实战、Kubernetes容器化部署等内容，你也可以点击链接 [mall-swarm视频教程](https://mp.weixin.qq.com/s?__biz=MzU1Nzg4NjgyMw==&mid=2247529674&idx=1&sn=96ce25780c426efc8383698c06c2618b&scene=21#wechat_redirect) 了解更多内容。

![图片](https://mmbiz.qpic.cn/mmbiz_gif/CKvMdchsUwlkU1ysoMgG69dVYbCQcI6Byneb8ibzZWPfUCr3T8CuBicCSGyFE6SpAtxpxtDCp6VlZ4F1hEL1BNyg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

继续滑动看下一个

macrozheng

向上滑动看下一个