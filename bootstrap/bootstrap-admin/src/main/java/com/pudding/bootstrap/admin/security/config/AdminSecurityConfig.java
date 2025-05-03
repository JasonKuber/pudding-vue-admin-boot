package com.pudding.bootstrap.admin.security.config;

import com.pudding.api.admin.security.password.filter.PasswordAuthenticationLoginFilter;
import com.pudding.application.admin.service.security.handler.AdminLogoutHandler;
import com.pudding.application.admin.service.security.handler.AdminLogoutSuccessHandler;
import com.pudding.application.admin.service.security.handler.LoginAuthenticationFailureHandler;
import com.pudding.application.admin.service.security.password.handler.PasswordAuthenticationSuccessHandler;
import com.pudding.application.admin.service.security.password.provider.PasswordAuthenticationProvider;
import com.pudding.application.admin.service.security.DynamicSecurityMetadataSource;
import com.pudding.bootstrap.admin.security.filter.TokenAuthenticationFilter;
import com.pudding.bootstrap.admin.security.handler.EntryPointUnauthorizedHandler;
import com.pudding.bootstrap.admin.security.handler.RequestAccessDeniedHandler;
import com.pudding.bootstrap.admin.security.manager.DynamicAccessDecisionManager;
import com.pudding.config.web.NotAuthenticationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    private final NotAuthenticationConfig notAuthenticationConfig;

    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    private final RequestAccessDeniedHandler requestAccessDeniedHandler;

    private final AdminLogoutSuccessHandler adminLogoutSuccessHandler;

    private final AdminLogoutHandler adminLogoutHandler;

    /**
     * 密码登录成功处理器
     */
    private final PasswordAuthenticationSuccessHandler passwordAuthenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    private final LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;

    /**
     * 动态权限元数据源
     */
    private final DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(passwordAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用表单登录，前后端分离用不上
        http.formLogin().disable()
                //cxrf为了防止跨站伪造请求攻击，认证时还会认证一个csrf_token。前后端分离项目是天然能防止的，所以必须关闭csrf，否则认证不了
                .cors()
                .and()
                .csrf().disable()
                // 配置URL的授权
                .authorizeRequests(rep ->
                        rep.antMatchers(
                                // 通过 @NotAuthentication 注解放行接口
                                notAuthenticationConfig.getPermitAllUrls().toArray(new String[0])
                                ).permitAll()
                                // 通过编写路径放行接口
                                .antMatchers(
                                        "/swagger-resources",
                                        "/v3/api-docs",
                                        "/doc.html/**",
                                        "/webjars/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )

                // 处理登出
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)       // 替代 SecurityContextLogoutHandler
                .invalidateHttpSession(true)     // 替代 SecurityContextLogoutHandler
                .addLogoutHandler(adminLogoutHandler)                     // 自定义逻辑（优先执行）
                .addLogoutHandler(new HeaderWriterLogoutHandler(
                        new ClearSiteDataHeaderWriter(
                                ClearSiteDataHeaderWriter.Directive.COOKIES,
                                ClearSiteDataHeaderWriter.Directive.STORAGE
                        )))  // 按需清理浏览器数据
                .addLogoutHandler(new CookieClearingLogoutHandler("JSESSIONID")) // 按需清理 Cookie
                .logoutSuccessHandler(adminLogoutSuccessHandler)          // 在LogoutHandler退出处理完毕后
                .and()

                // 处理异常情况：认证失败和权限不足
                .exceptionHandling()
                // 认证未通过，不允许访问异常处理器
                .authenticationEntryPoint(entryPointUnauthorizedHandler)
                // 认证通过，但是没权限处理器
                .accessDeniedHandler(requestAccessDeniedHandler)
                .and()

                // 禁用session，JWT校验不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 注册权限拦截器
                .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
                // 将Token校验过滤器配置到过滤器链中，否则不生效，放到LogoutFilter之前
                .addFilterBefore(passwordAuthenticationLoginFilter(), LogoutFilter.class)
                .addFilterBefore(authenticationTokenFilterBean(), LogoutFilter.class);
    }

    /**
     * Security默认使用的PasswordEncoder要求数据库中的密码格式为：{id}password 。它会根据id去判断密码的加密方式。但是我们一般不会采用这种方式。所以就需要替换PasswordEncoder。
     * 我们一般使用SpringSecurity为我们提供的BCryptPasswordEncoder。
     * 我们只需要使用把BCryptPasswordEncoder对象注入Spring容器中，SpringSecurity就会使用该PasswordEncoder来进行密码校验
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    /**
//     * 获取AuthenticationManager
//     *
//     * @param configuration
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(Arrays.asList(
                passwordAuthenticationProvider()
        ));
    }

    /**
     * 自定义的Jwt Token校验过滤器
     * @return
     */
    @Bean
    public TokenAuthenticationFilter authenticationTokenFilterBean()  {
        return new TokenAuthenticationFilter();
    }

    /**
     *  配置自定义的FilterSecurityInterceptor
     * @return
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        // 加载所有接口权限

        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        // 设置权限数据源
        filterSecurityInterceptor.setSecurityMetadataSource(dynamicSecurityMetadataSource);
        // 设置权限决策器
        filterSecurityInterceptor.setAccessDecisionManager(new DynamicAccessDecisionManager());
        // 设置认证管理器
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        return filterSecurityInterceptor;
    }


    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOriginPattern("*");
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        //放行哪些原始域(请求方式)
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.OPTIONS);
        //放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        config.addExposedHeader("*");

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }


    /*  配置多种登录方式  */


    /**
     * 密码登录过滤器
     * @return
     * @throws Exception
     */
    private PasswordAuthenticationLoginFilter passwordAuthenticationLoginFilter() throws Exception {
        // 配置密码登录过滤器
        PasswordAuthenticationLoginFilter passwordAuthenticationLoginFilter = new PasswordAuthenticationLoginFilter();
        passwordAuthenticationLoginFilter.setAuthenticationManager(authenticationManagerBean());
        // 配置认证成功处理器
        passwordAuthenticationLoginFilter.setAuthenticationSuccessHandler(passwordAuthenticationSuccessHandler);
        // 配置认证失败处理器
        passwordAuthenticationLoginFilter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
        return passwordAuthenticationLoginFilter;
    }

    @Bean
    public AuthenticationProvider passwordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }



}
