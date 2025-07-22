## 添加应用程序审计员意识::Auditing
[[Spring Security 的 JPA Auditing]]
为了知道谁做了什么以及何时完成，我们需要实现 ApplicationAuditorAware。
这个应用程序审计员意识**是 Spring 处理的一种机制，我们需要做的就是提供关于用户的信息或如何获取关于用户的信息**。以及在我们的例子中，是使用用户 ID 来定义或确定谁做了什么。

那么继续实施应用程序审计意识并添加配置，让我们看看如何审计用户的申请并跟踪谁做了什么。

我们之前需要做的非常重要的事情就是 AuditingEntityListener
```Java
@EntityListeners(AuditingEntityListener.class)
```
所以你可能会问，我们都这样做了，还在启动类添加了注解 `@EnableJpaAuditing` 启用了 jpa 审计。
但 JPA 审计仅适用于这两个字段：创建日期、最后修改日期。
```Java
@CreatedDate  
@Column(nullable = false, updatable = false)  
private LocalDateTime createdDate; // 创建时间  
  
@LastModifiedDate  
@Column(insertable = false)  
private LocalDateTime lastModifiedDate; // 最后修改时间
```
但对于创建者和最后修改者，我们需要进一步扩大需求，为其增添一些额外内容。

问题是为什么？因为 spring 默认不知道如何获取用户，但在我们的例子中由于我们自己实现了安全性，我们知道应用程序是如何工作的，我们可以自己实现。

为了追踪谁创建了这条记录，谁更新了这条记录，Spring 需要我们提供一个 `AuditorAware<T>` 类型的 Bean (AuditorAware 是一接口，需创建新类实现此接口)。
```Java
/**  
用于识别应用程序当前审计器的组件的接口。这通常是某种用户。
类型参数：<T> – 审计实例的类型。 
*/
public interface AuditorAware<T> {  
  
    /**  
     * 返回应用程序的当前审核员。
     返回值：当前审核员。
     */    
     Optional<T> getCurrentAuditor();  
}
```

那么在 config 中创建 ApplicationAuditAware 实现 `AuditorAware<T>`。AuditorAware 是一个通用接口，你可以决定要审计的内容。对于我们的情况，我将仅跟踪用户的 ID，那么需要实现 `AuditorAware<Integer>` (若跟踪 Email 则设为 String 类型)。
```Java
// AuditorAware 是一个通用接口，你可以决定要审计的内容
public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication 为 null || authentication 未经过身份验证 || authentication 是匿名身份验证令牌的实例
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }
        User userPrincipal = (User)authentication.getPrincipal();

        // Optional.ofNullable(value): 用一个可能为 null 的值创建一个 Optional 实例
        return Optional.ofNullable(userPrincipal.getId());
    }
}
```
现在实现了，但是这个类还没有被使用，让我们继续使用它。
将我们实现的 `AuditorAware<Integer>` 注册为一个 Bean 交由 Spring IoC 容器管理，转到 BeanConfig 进行添加：
```Java
@Bean  
public AuditorAware<Integer> auditorAware() {  
    return new ApplicationAuditAware();  
}
```

但这不是结束，我们还需添加一个另一件事。
当我们启用 jpa 审计时，我们需要为 Spring 进行标记或者说我们需要告诉 Spring 他需要的 AuditorAware 参考是什么，使用和引用我们所实现的 Bean 或 Bean 名称将是这个审计感知 Bean 的名称：auditorAware（提醒，熟悉 Bean 命名）。
```Java
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
```