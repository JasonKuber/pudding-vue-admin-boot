package com.pudding.config.db.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DruidConfig {

    private static final String DB_PREFIX = "spring.datasource";

    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("init Druid Servlet Configuration");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow", "*");
        // IP黑名单（共同存在时,deny优先于allow）
        servletRegistrationBean.addInitParameter("deny", "192.168.1.20");
        // 控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    /**
     * 配置 Druid 监控 之  web 监控的 filter
     * WebStatFilter：用于配置Web和Druid数据源之间的管理关联监控统计
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //"/*" 表示过滤所有请求
        filterRegistrationBean.addUrlPatterns("/*");
        //exclusions：设置哪些请求进行过滤排除掉，从而不进行统计
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


}
