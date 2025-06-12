---
title: "审计::Spring Data JPA --- Auditing :: Spring Data JPA"
source: "https://docs.spring.io/spring-data/jpa/reference/auditing.html"
author:
published:
created: 2025-06-11
description:
tags:
  - "clippings"
---
## Basics 基础知识

Spring Data 提供了完善的支持，可以透明地跟踪实体的创建者或更改者以及更改发生的时间。要使用此功能，您必须为实体类配备审计元数据，这些元数据可以使用注解或实现接口来定义。此外，还必须通过注解配置或 XML 配置来启用审计功能，以注册所需的基础架构组件。请参阅特定于商店的部分以获取配置示例。

|  | 仅跟踪创建和修改日期的应用程序不需要让其实体实现 [`AuditorAware`](https://docs.spring.io/spring-data/jpa/reference/#auditing.auditor-aware) 。 |
| --- | --- |

### 基于注解的 Auditing Metadata(审计元数据)

我们提供 `@CreatedBy` 和 `@LastModifiedBy` 来捕获创建或修改实体的用户，以及 `@CreatedDate` 和 `@LastModifiedDate` 来捕获更改发生的时间。

受审计实体

```java
class Customer {

  @CreatedBy
  private User user;

  @CreatedDate
  private Instant createdDate;

  // … further properties omitted
}
```

如您所见，可以根据要捕获的信息选择性地应用注解。这些注解指示在发生更改时进行捕获，可用于 JDK8 日期和时间类型（ `long` 、 `Long` 以及旧版 Java `Date` 和 `Calendar` ）的属性。

审计元数据不一定需要存在于根级实体中，但可以添加到嵌入式实体中（取决于实际使用的存储），如下面的代码片段所示。

嵌入式实体中的审计元数据

```java
class Customer {

  private AuditMetadata auditingMetadata;

  // … further properties omitted
}

class AuditMetadata {

  @CreatedBy
  private User user;

  @CreatedDate
  private Instant createdDate;

}
```

### 基于接口的 Auditing Metadata(审计元数据)

如果您不想使用注解来定义审计元数据，您可以让您的域类实现 `Auditable` 接口。该接口公开了所有审计属性的设置方法。

### AuditorAware

如果您使用 `@CreatedBy` 或 `@LastModifiedBy` ，审计基础架构需要以某种方式感知当前主体。为此，我们提供了一个 `AuditorAware<T>` SPI 接口，您必须实现该接口来告知基础架构当前与应用程序交互的用户或系统是谁。泛型 `T` 定义了使用 `@CreatedBy` 或 `@LastModifiedBy` 注解的属性的类型。

下面的示例展示了使用 Spring Security 的 `Authentication` 对象的接口的实现：

基于 Spring Security 的 `AuditorAware` 实现

```java
class SpringSecurityAuditorAware implements AuditorAware<User> {

  @Override
  public Optional<User> getCurrentAuditor() {

    return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getPrincipal)
            .map(User.class::cast);
  }
}
```

该实现访问 Spring Security 提供的 `Authentication` 对象，并查找您在 `UserDetailsService` 实现中创建的自定义 `UserDetails` 实例。我们假设您通过 `UserDetails` 实现公开域用户，但根据找到的 `Authentication` 实例，您也可以从任何地方查找它。

### ReactiveAuditorAware

使用响应式基础架构时，您可能需要利用上下文信息来提供 `@CreatedBy` 或 `@LastModifiedBy` 信息。我们提供了一个 `ReactiveAuditorAware<T>` SPI 接口，您必须实现该接口来告知基础架构当前与应用程序交互的用户或系统是谁。泛型 `T` 定义了使用 `@CreatedBy` 或 `@LastModifiedBy` 注解的属性的类型。

以下示例展示了使用反应式 Spring Security 的 `Authentication` 对象的接口的实现：

基于 Spring Security 的 `ReactiveAuditorAware` 实现

```java
class SpringSecurityAuditorAware implements ReactiveAuditorAware<User> {

  @Override
  public Mono<User> getCurrentAuditor() {

    return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast);
  }
}
```

该实现访问 Spring Security 提供的 `Authentication` 对象，并查找您在 `UserDetailsService` 实现中创建的自定义 `UserDetails` 实例。我们假设您通过 `UserDetails` 实现公开域用户，但根据找到的 `Authentication` 实例，您也可以从任何地方查找它。

还有一个便捷的基类 `AbstractAuditable` ，您可以扩展它以避免手动实现接口方法。这样做会增加域类与 Spring Data 的耦合度，而这可能是您想要避免的。通常，基于注解定义审计元数据的方式是首选，因为它侵入性较小且更灵活。

## General Auditing Configuration常规审计配置

Spring Data JPA 附带一个实体监听器，可用于触发审计信息的捕获。首先，必须在 `orm.xml` 文件中注册 `AuditingEntityListener` ，用于持久化上下文中的所有实体，如下例所示：

示例 1. 审计配置 orm.xml

```xml
<persistence-unit-metadata>
  <persistence-unit-defaults>
    <entity-listeners>
      <entity-listener class="….data.jpa.domain.support.AuditingEntityListener" />
    </entity-listeners>
  </persistence-unit-defaults>
</persistence-unit-metadata>
```

您还可以使用 `@EntityListeners` 注释为每个实体启用 `AuditingEntityListener` ，如下所示：

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MyEntity {

}
```

|  | 审计功能需要 `spring-aspects.jar` 位于类路径上。 |
| --- | --- |

通过适当修改 `orm.xml` 并将 `spring-aspects.jar` 放在类路径上，激活审计功能只需将 Spring Data JPA `auditing` 命名空间元素添加到您的配置中，如下所示：

示例 2. 使用 XML 配置激活审计

```xml
<jpa:auditing auditor-aware-ref="yourAuditorAwareBean" />
```

从 Spring Data JPA 1.5 开始，您可以通过在配置类上使用 `@EnableJpaAuditing` 注解来启用审计。您仍然必须修改 `orm.xml` 文件，并将 `spring-aspects.jar` 添加到类路径中。以下示例显示如何使用 `@EnableJpaAuditing` 注解：

示例 3. 使用 Java 配置激活审计

```java
@Configuration
@EnableJpaAuditing
class Config {

  @Bean
  public AuditorAware<AuditableUser> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
```

如果您将 `AuditorAware` 类型的 bean 暴露给 `ApplicationContext` ，则审计基础结构会自动选择该 bean，并使用它来确定要在域类型上设置的当前用户。如果您在 `ApplicationContext` 中注册了多个实现，则可以通过显式设置 `@EnableJpaAuditing` 的 `auditorAwareRef` 属性来选择要使用的实现。