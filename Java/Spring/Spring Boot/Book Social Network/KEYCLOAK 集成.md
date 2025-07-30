将 keycloak 与 spring boot 和 angular 应用程序集成。遍历所有配置：创建 realm（领域）、client (客户端)、users (用户)、roles (角色)、groups (组)、localisation (本地化)、social authentication (社交身份验证)、keycloak events……

---

说实话，不建议使用 JWT 保护您的应用程序，而且它也不是最佳实践之一，原因证据是，例如 Spring 没有针对 JWT 的默认或内置实现，所以我们所做的主要是 100% 由我们自己完成。
所以现在我会推荐，对我来说最好的建议是使用 Auth 提供程序或身份和访问管理提供程序，对于这种情况，我推荐开源和免费工具 KEYCLOAK。

## Keycloak 身份验证流程
首先需要了解集成 Keycloak 时的流程，我们的身份验证和授权流程是什么样的。
![[keycloak.png]]
根据图中的步骤编号
首先，让我们从用户请求一个页面开始（1-Request a page），因此它将在浏览器上请求一个页面，例如 `http://localhost:4200/books`。然后浏览器将执行检查（2-Check），因为前端应用程序也配置 Keycloak，因此接下来要做的是联系 Keycloak 并确保用户是否已注册或用户是否有有效令牌。我们将发送用户或将重定向到登录页面（3-Redirect to login page），我们将使用默认的主题，因此用户需要输入他的用户名或电子邮件以及密码（4-username/password）。这是第一步检查。完成此检查后，我们将验证凭据（5-Check credentials）与Keycloak进行比对，因此，在单击“登录”后，我们将使用此信息（即用户名和密码）向 Keycloak 发送请求，一旦 Keycloak 检查了信息，我们有两个可能的选择（6-Invalid credentials、7-Valid credentials），凭据无效，显示错误消息告知用户名或密码无效，凭据正确且有效，我们将把用户重定向到请求的页面，即 `http://localhost:4200/books`。
这不是故事的结束，当尝试加载图书页面时，我们将要做的就是向后端发送一个请求（8-request），因此后端也需要检索或发回信息。后端还有另一个检查过程，正如你在第九步中看到的，后端需要验证令牌（9-Validate token），因为当发送一个请求时，我们会在标头请求中包含 bear 令牌或 JWT 令牌。自 Keycloak  作为我们的JWT令牌的发行方以来，有两种可能的情况，所以第一个是第 10 步，它是令牌有效或有效令牌，因此我们将向 API 发送回令牌有效，然后我们将前端请求数据发送回前端（11-response），第二种即步骤 12，令牌无效，向前端发送回 HTTP 403（13-403）。

## 使用 docker 设置 keycloak
docker compose 中添加
```yaml
keycloak:
  container_name: keycloak-bsn
  image: quay.io/keycloak/keycloak:24.0.2
  environment:
    KEYCLOAK_ADMIN: admin
    KEYCLOAK_ADMIN_PASSWORD: admin
  ports:
    - 9090:8080
  networks:
    - spring-demo
  command:
    - "start-dev"
```

## 创建一个新的 keycloak realm 和 client
访问 kaycloak 管理页面：http://localhost:9090

**创建 realm**
称为 book-social-network
![[create-realm.png]]

所以，建立领域和几乎所有其他设置都非常简单，只是为了开始。

### client
接下来需要做的就是创建一个新的客户端，客户端将是用来连接到我们的前端，进入该领域，同时连接到我们的后端。
![[client-1.png]]

接下来这里我们到底想要什么，如果您想要客户端身份验证或身份验证流程直接访问，那么所有内容保留为默认值，如果需要更新某些内容，后面可以修改。
![[client-2.png]]

现在我们需要提供的是我们的 **Root URL**，根 URL 是你的前端应用程序的根 URL。
Home URL 不是必需的。
我们需要设置一个 **Valid redirect URIs**（有效的重定向 URL），这一点非常重要，因为这是 Keycloak 登录用户时会发生的情况，所以重定向 URL 是什么，点击?的弹出窗口中查看，所以 `*` 很重要。
设置当用户注销时，**Valid post logout redirect URIs**（有效的注销后重定向 URI），这意味着我们将尝试默认将用户重定向到我们应用程序的主页。
对于 **Web Origins**（网络源），我们可以指定我们的本地主机和后端的服务器端口。或只是做一些开发和实验，可以把它设为 `*`，不建议在这里使用 `*`，因为这些是我们希望允许访问我们客户端的 Origin。
![[client-3.png]]

完成领域和客户端的配置，现在我们做一件小事，继续创建一个用户
![[user-1.png]]
对于 User，我们有详细信息；
Credentials (凭据)，创建了一个用户，但是我们的用户还没有密码，直接从 Keycloak 管理高级用户，而不从注册表单。
![[user-password.png]]
开启 Temporary，意味着当用户第一次登录时，需要更新密码。

你拥有执行任何操作所需的所有权限，因此你需要小心，尤其是在使用 Keycloak 进行生产时。

## Keycloak 与 Angular 集成
### 集成
将 Keycloak 集成到 Angular 中，因此首先我们需要一个依赖项来帮助我们与 Keycloak 通信，因此这个依赖项称为 [`keycloak-js`](https://www.keycloak.org/securing-apps/javascript-adapter)。
有一件事非常重要，在使用这些依赖项时，无论是在后端还是在前端，你都需要注意版本，所以尽可能选择与 Keycloak 服务器相同的版本。否则可能会有风险，例如不兼容。
所以： `npm i keycloak-js@24.0.2`

我们需要创建一个服务器，这个服务将初始化 Keycloak，它将我们的应用程序连接到 Keycloak 服务器并执行一些操作。
进入 services 创建 keycloak 文件夹，然后在其中 `ng g s keycloak`。

当我们创建某些东西时，我们总是需要连接它，但在连接服务之前，让我们首先解释这项服务的含义。
所以这是将要运行的 keycloak 服务，它将在我们启动或引导应用程序时执行，所以在引导应用程序时，我们需要告诉我们的 angular 应用程序"我们有一个身份验证提供程序"，在这种情况下是 keycloak，我们想要使用它，并且你需要对 keycloak 服务器执行所有检查。

所以接下来需要做的是转到我们的 app.module.ts，添加一个新的 provider
```ts
export function kcFactory(kcService: KeycloakService) {
  return () => kcService.init();
}

@NgModule({
  //...
  providers: [
    HttpClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      deps: [KeycloakService],
      useFactory: kcFactory,
      multi: true
    }

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

那么，加下来需要进入 KeycloakService 实现最主要的 init 方法，和一些其他方法。
```ts
import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {UserProfile} from './user-profile';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'book-social-network',
        clientId: 'bsn'
      });
    }
    return this._keycloak;
  }

  private _profile: UserProfile | undefined;

  get profile(): UserProfile | undefined {
    return this._profile;
  }
  // 初始化
  async init() {
    const authenticated = await this.keycloak.init({
      // login-required: 检查用户是否已经登录
      // check-sso: 单点登录
      onLoad: 'login-required',
    });
	// 若通过身份验证
    if (authenticated) {
      // 加载用户详细信息
      this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak.token || '';
    }
  }
  // 登录
  login() {
    return this.keycloak.login();
  }
  // 注销
  logout() {
    // this.keycloak.accountManagement();
    // 可以指定重定向uri
    return this.keycloak.logout({redirectUri: 'http://localhost:4200'});
  }
}
```

创建 user-profile.ts
```js
export interface UserProfile {
  username?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  token?: string;
}
```

### guard
在转到后端之前，首先是 guard（守卫），我们的身份验证守卫使用我们的 TokenService，使用 JWT 的情况是正确的，例如自定义或个性化实现，但是当
```ts
import { Injectable } from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    // decode the token
    const jwtHelper = new JwtHelperService();
    // check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  get userRoles(): string[] {
    const token = this.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const decodedToken = jwtHelper.decodeToken(token);
      console.log(decodedToken.authorities);
      return decodedToken.authorities;
    }
    return [];
  }
}
```
这对于使用 JWT 的情况是正确的，例如自定义或个性化实现，但是当谈到 keycloak 时，情况完全不同，就像我想让它更轻量或更柔和一些，但没有什么是 100% 不同的，keycloak 管理和处理令牌的方式也不同。
```ts
export const authGuard: CanActivateFn = () => {
  const tokenService = inject(KeycloakService);
  const router = inject(Router);
  // 若令牌过期
  if (tokenService.keycloak.isTokenExpired()) {
    router.navigate(['login']);
    return false;
  }
  return true;
};
```

### intercept
第二部分是我们有拦截器，所以对于拦截器，我们以同样的方式使用令牌服务来获取令牌并将其作为 bear token 传递
```ts
@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(
    private keycloakService: KeycloakService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.keycloakService.keycloak.token;
    if (token) {
      const authReq = request.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${token}`
        })
      });
      return next.handle(authReq);
    }
    return next.handle(request);
  }
}
```

别忘记修改组件中的注销，以及注释掉所有的登录组件。
```ts
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMsg: Array<string> = [];

  constructor(
    private ss: KeycloakService
  ) {
  }

  async ngOnInit(): Promise<void> {
    await this.ss.init();
    await this.ss.login();
  }

  /*login() {
    this.errorMsg = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['books']);
      },
      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.errorMsg);
        }
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }*/
}
```


## Keycloak 与 Spring Boot 集成
第一件事就是更新依赖项，我们正在使用 `spring-boot-starter-security`，现在不再使用，因为我们使用的是 Keycloak，所以 Keycloak 是 OAuth2 安全的一部分，所以在这里我们不再使用那个，而是使用 `spring-boot-starter-oauth2-resource-server`。
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```
在项目中，你不会遇到任何编译问题，因为这个 oauth 2-resource-server 中也有 Spring Security Starter，所以我们这里有所有的 Spring Security 依赖项。

### **更新属性**
进入 application-dev.yml，删除 application 下的 security 和 mailing，因为不在使用 jwt，并且应用程序将不再处理电子邮件发送。

我们需要配置 `spring.security.oauth 2.resourceserver.jwt.issuer-uri` 属性，
在这里我们需要做的就是提供 issuer-uri（发行者URI）。发行者是指哪个实体或哪个服务器，或者哪个资源服务器负责发行JWT令牌。
如何找到？
![[realm-settings.png]]
![[realm-uri.png]]
那么可看到是 `http://localhost:9090/realms/book-social-network`
```yaml
spring:
  datasource: # 数据源
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/book_social_network
    username: username
    password: password
  jpa:
    hibernate:
      ddl-auto: update # 开发环境 保持更新
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    # 指定数据库有助于 JPA 和 Hibernate 更好地准备或生成
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect # sql 方言
  # 对邮件（JavaMail）功能的属性设置, 用于通过 SMTP 服务器发送邮件
  mail:
    host: localhost # SMTP 服务器地址
    port: 1025
    username: ali # 登录 SMTP 服务器的用户名
    password: ali # 登录 SMTP 服务器的密码
    properties: # mail.properties 子项
      # 把 JavaMail 的原生 mail.* 属性传递给底层的 javax.mail.Session
      mail:
        smtp:
          trust: "*"
        auth: true
        starttls:
          enabled: true
        connectiontimeout: 5000
        timeout: 3000
        writetimeout: 5000
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: "http://localhost:9090/realms/book-social-network"

server:
  port: 8088

application:
#  security:
#    jwt:
#      secret-key: 1fa1f80fd9c12472df8faa8fc41d213774937951f65ef878a72513f5871938ee
#      expiration: 86400000 # 1 day
#      refresh-token:
#        expiration: 604800000 # 7 days
#  mailing:
#    frontend:
#      activation-url: http://localhost:4200/activate # 前端
  file:
    upload:
      photos-output-path: ./uploads # 文件上传到服务器路径
```
对于属性来说，这是需要执行的唯一更改。

### **调整安全配置**
进入 SecurityConfig，删除 sessionManagement、authenticationProvider 和 addFilterBefore 配置，我将添加使用 `oauth2ResourceServer` 配置。

```Java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	http
			.cors(withDefaults())
			// 禁用 csrf
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(request -> request
					// requestMatchers：可以传递一个字符串列表或一个模式列表，这将代表应用程序白名单
					.requestMatchers(WHITE_LIST_URL).permitAll()
					// 其余都需经过身份验证
					.anyRequest().authenticated()
			)
			.oauth2ResourceServer(auth ->
					auth.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

	return http.build();

}
```
需要创建一个能够转换我的令牌的类，不完全是令牌，但我们需要执行一些转换，所以我们想要做的主要是将 Keycloak 角色转换为 Spring 角色。
所以 KeycloakJwtAuthenticationConverter 是将立即创建的类，它不需要任何参数，在同一个包中创建此类。

### **创建 KeycloakJwtAuthenticationConverter**
```Java
public class KeycloakJwtAuthenticationConverter implements org.springframework.core.convert.converter.Converter<org.springframework.security.oauth2.jwt.Jwt, ? extends org.springframework.security.authentication.AbstractAuthenticationToken> {  
}
```
这里我们看到 spring 自动理解这是一个应该实现 Converter 类。
修改即可
```Java
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
}
```

实现 convert 方法
```Java
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream()
                ).collect(Collectors.toSet())
        );
    }

    // JWT 令牌中提取这个 resource_access 对象，然后提取 account 对象，
    // 然后提取 roles 列表，最后将每个 roles 中的每个 role 转换为 spring role
    public Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        var resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var eternal = (Map<String, List<String>>) resourceAccess.get("account");
        var roles = eternal.get("roles");

        return roles.stream()
                //  spring 角色默认是 `ROLE_[角色名称]`，并且若角色名称中有 `-`，需替换为 `_`
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }
}
```

讲解一下 extractResourceRoles 方法，首先进行一次登录
![[access_token.png]]
复制，到 jwt.io，进行解码得到 Payload 部分为
```json
{
  "exp": 1753778570,
  "iat": 1753778270,
  "auth_time": 1753778268,
  "jti": "94b8b380-c632-4d84-aa71-56e85d6dfb4d",
  "iss": "http://localhost:9090/realms/book-social-network",
  "aud": "account",
  "sub": "ff13e645-b014-41e7-93a8-d7392189b691",
  "typ": "Bearer",
  "azp": "bsn",
  "nonce": "45c36948-95ae-4f07-91fa-62375ca2c4c1",
  "session_state": "957764ed-771f-4040-bd32-8118ca320d1c",
  "acr": "1",
  "allowed-origins": [
    "*"
  ],
  "realm_access": {
    "roles": [
      "offline_access",
      "uma_authorization",
      "default-roles-book-social-network"
    ]
  },
  "resource_access": {
    "account": {
      "roles": [
        "manage-account",
        "manage-account-links",
        "view-profile"
      ]
    }
  },
  "scope": "openid email profile",
  "sid": "957764ed-771f-4040-bd32-8118ca320d1c",
  "email_verified": true,
  "name": "han di",
  "preferred_username": "han@mail.com",
  "given_name": "han",
  "family_name": "di",
  "email": "han@mail.com"
}
```
其中有 realm_access 和 resource_access，在资源访问权限中，我们有一个名为 account 的对象，在 account 中我们有一个角色列表 roles，所以我们需要从这里提取角色并将它们转换为 spring roles。
你知道 spring 角色默认是 `ROLE_[角色名称]`，并且若角色名称中有 `-`，需替换为 `_`。
所以 extractResourceRoles 方法只是从我们的 JWT 令牌中提取这个 resource_access 对象，然后提取 account 对象，然后提取 roles 列表，最后将每个 roles 中的每个 role 转换为 spring role。类似于最开始时 User 实现 UserDetails 所实现的 getAuthorities 方法。

### **清理修改**
那么现在我们实现了转换器，回到我们的安全配置并执行一些清理，所以这里我们不再需要 JwtAuthFilter、AuthenticationProvider、JwtService。
将其进行注释掉。

进入 BeansConfig，authenticationProvider、passwordEncoder、authenticationManager 注释掉，留下 auditorAware 和 corsFilter。

UserDetailsServiceImpl 也不再需要。

auth 包下的也都不再需要，将 AuthenticationController、AuthenticationService 注释掉。

用户不再由我们管理，它不再由应用程序管理，所以我们不应该有 User 实体，自然也不需要 Token 和 Role。Book 和 BookTransactionHistory 与 User 之间关系的映射也要注释掉。

---
### **问题修复**
用户不再存在，首先通过连接用户转换为 User 已无法实现，注释掉。这并不意味着无法使用连接用户。

#### **book**
在 BookRepository 中不应再使用 book.owner.id，应该为 book.createdBy。
BaseEntity 中的 createdBy 和 lastModifiedBy 类型修改为 String。

BookSpecification 中 root.get("owner").get("id") 修改为 root.get("createdBy")，以及修改参数类型为 String。

以及 BookService 中很多处都用到了 owner，将 setOwner 注释掉，getOwner 替换为 getCreatedBy。
以及 user.getId() 将替换使用 connectedUser.getName()，以及修改方法参数类型，将 Integer (user.getId()的类型) 改为 String (connectedUser.getName()的类型)。
![[user-1.png]] connectedUser.getName() 将准确返回 Keycloak 中用户详细信息中的 ID。

以及 BookMapper 中的。

#### **history**
由于 BookTransactionHistory 是 book 与 user 之间的中间表，又因注释掉了与 User 的关系映射，那么我们需要添加一个字段 `userId`：
```java
@Column(name = "user_id")  
private String userId;
```
然后将 BookTransactionHistoryRepository 中的 JPQL 语句中的 user.id 改为 userId。

什么时候需要对 BookTransactionHistory 中的 userId 进行赋值，自然是构建实体时；什么时候需要创建 BookTransactionHistory 实体，自然时借书事件发生时。那么修改 BookService 中的 borrowBook 方法中的 BookTransactionHistory 实体的创建，将 user(user) 替换为 userId(connectedUser.getName())。

#### **feedback**
对应需改即可

#### **审计**
keycloak 中的用户 ID 为一种 UUID，是字符串，所以我们将对应的类型从 Integer 改为了 String，例如 BaseEntity 中的 createdBy 与 lastModifiedBy。
这意味着审计员感知类型从 Integer 变为了 String，那么 ApplicationAuditorAware 与 BeansConfig 中的 auditorAware，他们中的 Integer 也应改为 String。
```Java
// AuditorAware 是一个通用接口，你可以决定要审计的内容
public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication 为 null || authentication 未经过身份验证 || authentication 是匿名身份验证令牌的实例
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }
        // User userPrincipal = (User)authentication.getPrincipal();

        // Optional.ofNullable(value): 用一个可能为 null 的值创建一个 Optional 实例
        return Optional.ofNullable(authentication.getName());
    }
}
```

```Java
@Bean  
public AuditorAware<String> auditorAware() {  
    return new ApplicationAuditAware();  
}
```

#### **container**
不要忘记一个重要部分，即首先从应用程序迁移所有用户（换到了 Keycloak），那么前面也提到了用户的 ID 类型不再是 Integer 而是 String。那么就需要对每个使用这个 createdBy 或使用这个用户 ID 的表执行更新查询，修改此外键字段的类型，从 Integer 换为 varchar。
否则会遇到以下报错：
`No operator matches the given name and argument types. You might need to add explicit type casts.` (没有操作符与给定的名称和参数类型匹配。您可能需要添加显式类型转换。)

# Keycloak 高级配置
了解不同的选项、不同的工具以及配置 Keycloak 的不同方法。

进入 Realm settings。
## Realm settings

### Login
首先进入 Login 选项
![[realm-settings-login.png]]

可开启各种功能，比如启用**用户注册**
现在登录页面可看到 register 选项
![[register-1.png]]


当然，也可开启**忘记密码**，用户可提供自己的电子邮件地址来重置自己的密码，然后 Keycloak 会向用户发送一封电子邮件，以便重置密码，但发送电子邮件需要一些配置

开启**电子邮件作为用户名**选项，则注册时无 Username。

以及想要启用的功能之一是 **Verify email**，电子邮件验证，所以我们也可以启用它，但我们会在配置电子邮件属性后启用它。

### 帐户管理界面
Keycloak 也为我们提供了帐户管理界面。例如，如果您想允许用户更新自己的信息，例如名字、姓氏、电子邮件等等，还可以更改密码并执行所有这些操作，也可以通过启用连接到帐户管理来实现。

例如，我们将使用注销的图标，因此不会调用注销，而是调用另一个称为 `accountManagement` (帐户管理)的方法。
在 KeycloakService 中修改 logout 方法：
```ts
logout() {
// 可以直接跳转到账户管理页面
this.keycloak.accountManagement();
// 可以指定重定向uri
// return this.keycloak.logout({ redirectUri: 'http://localhost:4200' });
}
```

![[personal-info.png]]
修改密码
![[persinal-update-password.png]]
并且修改密码后，还可实现从其他设备注销。
想象一下自己实现所有这些，那么这将是一场真正的噩梦。

### Verify Email
![[realm-settings-email.png]]

继续注册过程，配置电子邮件。
首先是 Template (模板)：
第一件事是设置发件人电子邮件，发件人名称，回复电子邮件内容。

设置 SMTP 或者说设置连接和身份验证：
- 在 Host 这里将使用 smtp.gmail.com
- Port 使用 465
- 启用 SSL，启用 StartTLS
- 身份验证设置为启用，输入你的谷歌邮箱和密码，当然你的谷歌邮箱需要开启 SMTP/IMAP服务，并配置专用密码。

但在此之前，为了测试连接，应该为管理员（Admin）配置电子邮件地址。不要与你设置连接和身份验证中的 username 一致，否则测试可能失败。

测试连接成功后，保存。

现在返回 Realm settings 中的 Login 选项，可以启用这个 Verify email  (验证电子邮件)功能。

#### 测试
进行测试最好使用虚拟邮箱，例如https://yopmail.com/zh/。

然后使用在 yopmail 创建的虚拟邮箱进行注册，验证。
![[verfy-email.png]]
点击链接验证，启用账户，自动重定向到我们的应用程序。
![[verify-email-1.png]]

### Themes
可以指定要使用的主题。

### Keys
跳过，因为它太高级了。

### Events
事件是从 Keycloak 捕获事件并对其进行处理。

### Localization
Localization 重要，默认情况下 Keycloak 支持多种语言，但默认情况下它是禁用的，启用，并添加多个语言，例如中文，以及默认语言。
![[localization.png]]
这样在应用程序的登录注册页面等页面的默认语言为中文，并可选择修改，添加什么就可选择什么。
![[localization-1.png]]

那你可能会指出，上面的 BOOK-SOCIAL-NETWORK 如何根据语言改变。
那么首先在 Realm overrides，语言选择中文，点击 Add translation
![[localization-translation.png]]

在进入 general 进行如下配置，保存。
![[realm-settings-general.png]]

刷新登录页面，即可看到修改
![[localization-2.png]]

### Security defenses
安全防御，这也是非常高级的东西，跳过。

### Sessions
会话，配置单点登录会话设置，例如，我希望我的令牌有效多长时间、会话空闲时长，
对于会话空闲意味着如果用户30分钟不使用应用程序，这意味着将注销用户。

### Tokens

### Client policies

### User profiles
用户资料，这里的属性也是用户注册时需要填写的信息，顺序也是一致的。
也可以在这里创建新的属性，还要如何为注册创建属性或特定或额外的属性，如何使用它们，如何获取它们，如何在令牌中显示它们，所有与之相关的事情。
### User registration
对于用户注册，有这个默认的 roles，这意味着任何注册新帐户的用户，他都会继承，或者默认情况下他会获得这些角色。

如果我们想要分配一个自定义的 role，比如当新用户注册时，我想给他分配一个角色一样。
转到 Realm roles，这是我们拥有的默认角色，也是该领域的全局角色，这意味着每个客户端也有 roles，所以领域角色适用于所有客户端，而不是相反。

那么在 Realm roles 中创建 MANAGER、USER、ADMIN，创建后可为其添加属性和查看哪些用户分配了此 role。
![[create-role-1.png]]
![[create-role.png]]

推荐的是，当你创建角色时，继续创建 Group (组)，所以组只不过是角色的容器。
创建 ADMINS、USERS、MANAGERS 三个组。
![[create-group.png]]
并且组中还可创建子组、添加用户、添加属性、分配角色。
点击分配角色，USER 分配给 USERS，ADMIN 分配给 ADMINS，MANGER 分配给 MANAGERS。那么进入此组的将自动继承，所以他将自动拥有此组的角色。
例如，当手动创建一个用户时，我不需要为他分配角色，我需要做的就是将他附加或分配给一个组。

选择一个用户，让其加入 USERS 组，那么他的 roles 中将会多个 USER。

回到 User registration，分配 USER，默认情况下每个新用户都会有角色 USER。

## Authentication
### Flows
在 Authentication 中有流程，例如其中的注册流程，就是我们的用户注册。
默认情况下 Terms and conditions (条款和条件) 是禁用的，将其设为必须，在注册页面就可看到多出的条款，需要接受条款才可注册。
![[authentication-register.png]]

### Required actions
Required actions 意味着用户在创建个人资料后需要执行的操作。若启用则在 user 中可指定使用这些启用的 required actions；若启用并设为默认操作，则创建用户后，登录时需执行的操作。
![[required-actions.png]]

也可更改执行顺序。

## 禁用用户访问
![[user-disable.png]]

## 集成社交登录
在使用Keycloak时，如何在 JWT 中混合使用 authentication 或 OAuth 2
![[identity-providers.png]]

选择需要的，填写 Client ID 和 Client Secret。

# 导出 keycloak realm
你只是在本地或 Dev 环境中尝试或测试一些东西，并且你想将这些更改复制到另一个环境。
例如，本地使用 Keycloak，然后你决定好的，这就是我想要的配置，我现在想要复制或将这个配置从一个环境带到另一个环境。

docker ps
docker exec -it `[keycloak 的 id]` /bin/sh
pwd
ls -l 找到 opt，keycloak 位于 opt 中。
ls -l
cd keycloak
ls -l
cd bin/
ls -l
./kc.sh export --file book-social-network --realm book-social-network
将 book-social-network 领域配置导出到 book-social-network 文件夹中。
ls -l 可看到 book-social-network
接下来复制到本机。
docker cp `[keycloak 的 id]`:opt/keycloak/bin/book-social-network ./keycloak/realm
