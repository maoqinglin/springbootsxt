package com.ireadygo.springbootsxt.config;

import com.ireadygo.springbootsxt.component.LoginHandlerInterceptor;
import com.ireadygo.springbootsxt.component.MyLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
//@EnableWebMvc
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器发送 /extendMvc 请求来到success页面
        registry.addViewController("/extendMvc")
                .setViewName("success");
    }

    // 所有的 WebMvcConfigurer 組件都会一起起作用
    @Bean // 将组件注册到容器中
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
                registry.addViewController("/main.html").setViewName("dashboard");
            }

            // 注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandlerInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/index.html", "/", "/user/login", "/asserts/**"); // 不要拦截登录页面，及静态资源
                // 因为static下有多种资源，所以添加一个asserts目录
            }
        };
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }
}
