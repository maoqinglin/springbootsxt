package com.ireadygo.springbootsxt.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidSource {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    // 配置Druid的监控
    // 1、配置一个管理后天的Servlet
    @Bean
    public ServletRegistrationBean StatViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParmas = new HashMap<>();
        initParmas.put("loginUsername", "admin");
        initParmas.put("loginPassword", "123456");
        initParmas.put("allow", "");//默认允许所有访问
        initParmas.put("deny", "192.168.199.128");
        bean.setInitParameters(initParmas);

        return bean;
    }

    // 配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter() {  // 不要写 filterRegistrationBean
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParms = new HashMap<>();
        initParms.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParms);

        bean.setUrlPatterns(Arrays.asList("/*"));

        return bean;
    }
}
