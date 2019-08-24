# 一、Spring Boot入门

> ## 2、微服务

微服务：架构风格（服务微化）

一个应用应该是一组小型服务；可以通过HTTP的方式进行互通；

单体应用：All IN ONE

微服务：每一个功能元素最终都是一个可独立替换和独立升级的软件单元；



@**SpringBootConfiguration**：SpringBoot的配置类

​	标注在某个类上，表示这是一个SpringBoot的配置类；

​	@**Configuration**：配置类上来标注这个注解；

​		配置类----配置文件；配置类也是容器中的一个组件；@Component

@**EnableAutoConfiguration**：开启自动配置功能

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```

​	@**AutoConfigurationPackage**：自动配置包

​		@**Import**(AutoConfigurationPackages.Registrar.class)：

​		Spring的底层注解@Import，给容器中导入一个组件；导入的组件由AutoConfigurationPackages.Registrar.class 指定

​		将主配置类（@SpringBootConfiguration 标注的类）的所在包及下面所有子包里面的所有组件扫描到Spring容器

@**Import**(AutoConfigurationImportSelector.class)

​	给容器中导入组件

​	将所有需要导入的组件以全类名返回，这些组件就会被添加到容器中

​	会给容器中导入非常多的自动配置类（xxxAutoConfiguration）；就是给容器中导入这个场景需要的所有组件，并配置好这些组件；

![](G:\03_Markdown\Images\自动配置.png)

有了自动配置类，免去了我们手动编写配置注入功能组件等的工作；

``` Java
SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class，classLoader)
```

SpringBoot在启动的时候从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置导入到容器中，自动配置类就生效，帮我们进行自动配置工作；

J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-2.1.7.RELEASE.jar下面



# 二、配置文件

## 1、配置文件

SpringBoot使用一个全局的配置文件，配置文件名是固定的；

application.properties

application.yml



配置文件的作用：修改SpringBoot自动配置的默认值；SpringBoot在底层都给我们自动配置好

## 2、YAML语法

### 1、基本语法

k:(空格)v：表示一对键值对（空格必须有）；

以**空格**的缩进来控制层级关系；只要是左对齐的一列数据，都是同一个层级的

```yaml
server:
	port: 8081
	path: /hello
```

属性值也是大小写敏感

### 2、值的写法

字面量：普通的值（数字，字符串，布尔）

​	k: v：字面量直接来写

​		字符串默认不用加上单引号或双引号

​		""： 双引号，不会转义字符串里面的特殊字符，特殊字符会作为本身想表示的意思

​			name："zhangsan \n lisi"：输出；zhangsan 换行 lisi

​		''：单引号，会转义字符，**特殊字符最终只是一个普通的字符串数据**

name："zhangsan \n lisi"：输出；zhangsan \n lisi



对象、Map（属性和值）（键值对）：

​	k: v：在下一行来写对象的属性和值的关系；注意缩进

​		对象还是k: v的方式

```yaml
friends:
	lastName: zhangsan
	age: 20
```

行内写法：

```yaml
friends:{lastName: zhangsan, age: 20}
```



数组（List、Set）：

用 -值表示数组中的一个元素

```yaml
pets:
 - cat
 - dog
 - pig
```

行内写法

```yaml
pets:[cat,dog,pig]
```

## 3、配置文件值注入

配置文件

```yaml
person:
  lastName: zhangsan
  age: 18
  boss: false
  birth: 2017/12/12
  maps: {k1: v1,k2: 12}
  lists:
    - lisi
    - zhaoliu
  dog:
    name: xiaolin
    age: 12
```



JavaBean

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性配置和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *      只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能；
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String, Object> maps;
    private List<Object> lists;
    private Dog dog;
```

我们可以导入配置文件处理器，以后编写配置就有提示了

```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

### 1、属性文件中文乱码问题

![1566470244465](G:\03_Markdown\Images\1566470244465.png)



### 2、@Value获取值和@ConfigurationProperties获取值比较

|                      | @ConfigurationProperties | @Value     |
| -------------------- | ------------------------ | ---------- |
| 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法） | 支持                     | 不支持     |
| SpEL                 | 不支持                   | 支持       |
| JSR303数据校验       | 支持                     | 不支持     |
| 复杂类型封装         | 支持                     | 不支持     |

配置文件yml或者properties都能获取值

如果说，我们只是在某个业务逻辑中需要获取一下配置文件中的某项值，使用 @Value；

如果说，我们专门编写了一个JavaBean来和配置文件进行映射，我们就直接使用 @ConfigurationProperties；

### 3、配置文件注入值数据校验

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性配置和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *      只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能；
 *
 *      @ConfigurationProperties 默认从全局获取配置
 */
@Component
@ConfigurationProperties(prefix = "person")
@Validated   // 结合 @Email注解验证
public class Person {
    /**
     * <bean class="Person">
     *     <property name="lastName" value=""字面量/${key}从环境变量、配置文件中获取值/#{SpEL}></property>
     * </bean>
     */

    @Email
    private String lastName;
//    @Value("#{11*2}")
    private Integer age;
    @Value("true")
    private Boolean boss;
    private Date birth;
```



### 4、@PropertySource & @ImportResource

@PropertySource加载指定的配置文件

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性配置和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *      只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能；
 *
 *      @ConfigurationProperties 默认从全局获取配置
 */
@Component
@PropertySource(value = {"classpath:person.properties"})
@ConfigurationProperties(prefix = "person")
//@Validated   // 结合 @Email注解验证
public class Person {
    /**
     * <bean class="Person">
     *     <property name="lastName" value=""字面量/${key}从环境变量、配置文件中获取值/#{SpEL}></property>
     * </bean>
     */
//    @Value("${person.last-name}")  // @Value注解不支持@Validated 验证
//    @Email
    private String lastName;
//    @Value("#{11*2}")
    private Integer age;
    @Value("true")
    private Boolean boss;
    private Date birth;
```





@ImportResource导入Spring配置文件，让配置文件里的内容生效

SpringBoot里面没有Spring的配置文件，我们自己编写的配置文件，也不能自动识别；

想让Spring的配置文件生效，加载进来；**@ImportResource** 标注在一个配置类上

```java
@ImportResource(locations = {"classpath:beans.xml"})
导入Spring的配置文件让其生效
```

SpringBoot不推荐不编写Spring配置文件了；

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
<bean id="helloService" class="com.ireadygo.springbootsxt.service.HelloService"/>
</beans>
```





SpringBoot推荐给容器中添加组件的方式；推荐使用全注解的方式

1、配置类===Spring配置文件

2、上使用@Bean给容器中添加组件

```java
/**
 * @Configuration：指明当前是一个配置类，就是来代替之前的Spring配置文件
 * 在配置文件中使用 <bean></bean> 标签添加组件
 */
@Configuration
public class MyAppConfig {

    // 将方法的返回值添加到容器中；方法名默认就是组件的id
    @Bean
    public HelloService helloService(){
        System.out.println("配置类@Bean给容器中添加组件了");
        return new HelloService();
    }
```



## 4、配置文件占位符

### 1、随机数

```java
${random.value},${random.int}
```

### 2、占位符获取之前配置的值，如果没有值，使用：指定默认值



## 5、Profile

### 1、多Profile文件

我们在主配置文件编写的时候，文件名可以是 application-{profile}.properties/yaml

### 2、yml支持多文档块方式

```yaml
server:
  port: 8082
spring:
  profiles:
    active: prod
---
server:
  port: 8083
spring:
  profiles: dev
---
server:
  port: 8084
spring:
  profiles: prod
```



### 3、激活指定profile

1、在配置文件中指定 spring.profiles.active=dev

2、命令行：

java -jar G:\01_SpringBoot_Project\springbootsxt\target\springbootsxt-0.0.1-SNAPSHOT.jar --spring-profile-active=prod

可以直接在测试的时候，配置传入命令行参数

3、虚拟机参数

-Dspring.profile.active=dev



## 6、配置文件加载位置

SpringBoot启动会扫描以下位置的application.properties或者application.yml文件作为SpringBoot默认配置文件

-file:./config/

-file:./

-classpath:/config/

-classpath:/

优先级由高到低，高优先级的配置会覆盖低优先级的配置；

SpringBoot会从这个四个位置全部加载主配置文件，**互补配置**；



我们还可以通过spring.config.location改变默认配置文件位置 

**项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的心位置；指定配置文件和默认加载的这些配置文件共同起作用形成互补配置；**



## 7、外部配置加载顺序

**SpringBoot也可以从以下位置加载配置；优先级从高到低；高优先级的配置覆盖低优先级的配置，所有的配置会形成互补配置**

1.命令行参数

java -jar G:\01_SpringBoot_Project\springbootsxt\target\springbootsxt-0.0.1-SNAPSHOT.jar 

**--server.port=8088**

多个配置用空格分开； --配置项=值

2.来自java:comp/env的JNDI属性

3.Java系统属性（System.getProperties()）

4.操作系统环境变量

5.RandomValuePropertySource配置的random.*属性值



**由jar包外向jar包内进行寻找：**

**优先加载带profile配置**

**6.jar包外部的application-{profile}.properties或application.yml（带spring.profile）配置文件**

**7.jar包内部的application-{profile}.properties或application.yml（带spring.profile）配置文件**

**8.jar包外部的application.properties或application.yml（不带spring.profile）配置文件**

**9.jar包内部的application.properties或application.yml（不带spring.profile）配置文件**



10.@Configuration注解类上的@PropertySource

11.通过SpringApplication.setDefaultProperties指定的默认属性

所有支持的配置加载来源：

[参考官方文档](<https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/boot-features-external-config.html>)



## 8、自动配置原理

配置文件到底能写什么？怎么写？自动配置原理；

[配置文件能配置的属性参照](<https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/common-application-properties.html>)

自动配置原理：

1）、SpringBoot启动的时候记载主配置类，开启了自动配置功能 **@EnableAutoConfiguration**

2）、@EnableAutoConfiguration作用：

- 利用 **EnableAutoConfigurationImportSelector** 给容器导入一些组件？

- 可以查看selectImports()方法的内容；

- List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);获取候选的配置

  ```java
  SpringFactoriesLoader.loadFactoryNames()
  扫描所有 jar 包类路径下 META-INF/spring.factories
  把扫描到的这些文件的内容包装成properties对象
  从properties中获取到 EnableAutoConfiguration.class 类（类名）对应的值，然后把它们添加到容器中
  ```



将类路径下 **META-INF/spring.factories** 里面配置的所有 **EnableAutoConfiguration** 的值加入到了容器中

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.cloud.CloudServiceConnectorsAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration,\
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration,\
org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration,\
org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration,\
org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,\
org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,\
org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration,\
org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration,\
org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration,\
org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration,\
org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration,\
org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration,\
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration,\
org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration,\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration,\
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration,\
org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration,\
org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration,\
org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration,\
org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration,\
org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration,\
org.springframework.boot.autoconfigure.reactor.core.ReactorCoreAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityRequestMatcherProviderAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration,\
org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration,\
org.springframework.boot.autoconfigure.session.SessionAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration,\
org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration,\
org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration,\
org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration,\
org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration,\
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration,\
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration,\
org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration,\
org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration,\
org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration
```

每一个这样的 **xxxAutoConfiguration** 类都是容器中的一个组件，都加入到容器中；用它们来做自动配置；

3）、每一个自动配置类进行自动配置功能

4）、以 **HttpEncodingAutoConfiguration** **（Http编码自动配置）**为例解释自动配置原理

```java
@Configuration	// 表示这是一个配置类，以前编写的配置文件一样，也可以给容器中添加组件
@EnableConfigurationProperties(HttpProperties.class) // 启动指定类的ConfigurationProperties功能；将配置文件中对应的值和 HttpProperties绑定起来；并把
HttpProperties加入到容器中

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)//Spring底层@Conditional注解，根据不同的条件，如果满足指定的条件，整个配置类里面的配置就会生效；判断当前应用是否是web应用，如果是，当前配置类生效

@ConditionalOnClass(CharacterEncodingFilter.class) // 判断当前项目有没有这个类
CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；

@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true) // 判断配置文件中是否存在某个配置，spring.http.encoding.enabled；如果不存在，判断也是成立的；即使我们配置文件中不配置此属性，也是默认生效的；
public class HttpEncodingAutoConfiguration {

	// 它已经和SpringBoot的配置文件映射了
	private final HttpProperties.Encoding properties;
	
	// 只有一个有参构造器的情况下，参数的值就会从容器中拿
	public HttpEncodingAutoConfiguration(HttpProperties properties) {
		this.properties = properties.getEncoding();
	}
	
	@Bean // 给容器中添加一个组件，这个组件的某些值需要从 properties中获取
	@ConditionalOnMissingBean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.properties.getCharset().name());
		filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
		filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
		return filter;
	}
```

根据当前不同的条件判断，决定这个配置类是否生效？

一旦这个配置类生效；这个配置类就会给容器中添加各种组件；这些组件的属性是从对应的 **properties** 类中获取的，这些类里面的每一个属性又是和配置文件绑定的；



**精髓：**

​	**1）、 SpringBoot启动会加载大量的自动配置类**

​	**2）、看我们需要的功能SpringBoot是否提供了默认配置类**

​	**3）、我们再来看这个配置类中到底配置了哪些组件；（只要我们要用的组件已有，我们就不需要再配置了）**

​	**4）、给容器中自动配置类添加组件的时候，会从properties类中获取某些属性。我们就可以在配置文件中指定这些属性的值；**

xxxAutoConfiguration：自动配置类

给容器中添加组件

xxxProperties：封装配置文件中相关属性；



5）、所有在配置文件中能配置的属性都是在 **xxxProperties** 类中封装着；配置文件能配置什么就可以参照某个功能对应的这个属性类

```java
@ConfigurationProperties(prefix = "spring.http") // 从配置文件中获取指定的值和bean的属性进行绑定
public class HttpProperties {

```

## 3、SpringBoot日志关系

底层依赖关系

![](G:\03_Markdown\Images\SpringBoot_Log依赖.png)

总结：

​	1）、SpringBoot底层也是使用 slf4j + logback 的方式进行日志记录

​	2）、SpringBoot也把其它的日志都替换成了 slf4j；

​	3）、中间替换包？

```java
private static class Slf4jAdapter {

		public static Log createLocationAwareLog(String name) {
			Logger logger = LoggerFactory.getLogger(name);
			return (logger instanceof LocationAwareLogger ?
					new Slf4jLocationAwareLog((LocationAwareLogger) logger) : new Slf4jLog<>(logger));
		}

		public static Log createLog(String name) {
			return new Slf4jLog<>(LoggerFactory.getLogger(name));
		}
	}
```



​	4）、如果我们要引入其它框架？一定要把这个框架默认的日志依赖移除掉？

​	

**SpringBoot能自动适配所有的日志，而且底层使用 slf4j+logback的方式记录日志，引入其它框架的时候，只需要把这个框架依赖的日志框架排除掉；**



## 4、日志使用

### 1、SpringBoot修改日志的默认配置

```properties
#指定日志的完整路径
#logging.file=G:/springboot.log
#在当前磁盘下创建springboot目录和log目录，使用spring.log作为默认文件
logging.path=G:/springboot/log

# 在控制台输出的日志的格式
logging.pattern.console=
# 在文件中输出的日志的格式
logging.pattern.file=
```



SpringBoot日志默认配置

主jar包下，logging包下logback

base.xm

```xml
<included>
	<include resource="org/springframework/boot/logging/logback/defaults.xml" />
	<property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
	<include resource="org/springframework/boot/logging/logback/console-appender.xml" />
	<include resource="org/springframework/boot/logging/logback/file-appender.xml" />
	<root level="INFO">
		<appender-ref ref="CONSOLE" />
		<appender-ref ref="FILE" />
	</root>
</included>
```

root 默认为 INFO级别

include配置了控制台及文件输出

defaults.xml：规定了默认配置



### 2、指定配置

给类路径下放上每个日志框架自己的配置文件即可；SpringBoot就不使用默认配置了

| Logging System          | Customization                                                |
| ----------------------- | ------------------------------------------------------------ |
| Logback                 | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml`, or `logback.groovy` |
| Log4j2                  | `log4j2-spring.xml` or `log4j2.xml`                          |
| JDK (Java Util Logging) | `logging.properties`                                         |

logback.xml：直接就被日志框架识别了；

logback_spring.xml：日志框架就不直接加载日志的配置项，由SpringBoot解析日志配置，可以使用SpringBoot的高级 Profile 功能

```xml
<springProfile name="staging">
	<!-- configuration to be enabled when the "staging" profile is active -->
</springProfile>
```


