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

















