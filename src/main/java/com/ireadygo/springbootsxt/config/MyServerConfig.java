package com.ireadygo.springbootsxt.config;

import com.ireadygo.springbootsxt.filter.MyFilter;
import com.ireadygo.springbootsxt.servlet.MyServlet;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;

@Configuration
public class MyServerConfig {

    // 定义嵌入式的Servlet容器
    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.setPort(8888); // 此配置的优先级低于yaml配置文件的优先级
        return tomcatServletWebServerFactory;
    }

    // 注册三大组件
    @Bean
    public ServletRegistrationBean<HttpServlet> servletRegistrationBean() {
        ServletRegistrationBean<HttpServlet> servletRegistrationBean = new ServletRegistrationBean<>(new MyServlet(), "/myServlet");
        return servletRegistrationBean;
    }

    @Bean
    FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>(new MyFilter());
        filter.setUrlPatterns(Arrays.asList("/hello", "/myServlet"));
        return filter;
    }
}
