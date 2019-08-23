package com.ireadygo.springbootsxt.config;

import com.ireadygo.springbootsxt.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
