package com.pudding.bootstrap.admin.security.config;

import com.pudding.api.admin.security.password.filter.PasswordAuthenticationLoginFilter;
import com.pudding.application.admin.service.security.handler.LoginAuthenticationFailureHandler;
import com.pudding.application.admin.service.security.password.handler.PasswordAuthenticationSuccessHandler;
import com.pudding.application.admin.service.security.password.provider.PasswordAuthenticationProvider;
import com.pudding.bootstrap.admin.security.filter.TokenAuthenticationFilter;
import com.pudding.bootstrap.admin.security.handler.AdminLogoutSuccessHandler;
import com.pudding.bootstrap.admin.security.handler.EntryPointUnauthorizedHandler;
import com.pudding.bootstrap.admin.security.handler.RequestAccessDeniedHandler;
import com.pudding.config.web.NotAuthenticationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    /**
     * 密码登录成功处理器
     */
    private final PasswordAuthenticationSuccessHandler passwordAuthenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    private final LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用表单登录，前后端分离用不上
        http.formLogin().disable()
                //cxrf为了防止跨站伪造请求攻击，认证时还会认证一个csrf_token。前后端分离项目是天然能防止的，所以必须关闭csrf，否则认证不了
                .csrf().disable()
                // 配置URL的授权
                .authorizeRequests(rep ->
                        rep.antMatchers(
                                // 通过 @NotAuthentication 注解放行接口
                                notAuthenticationConfig.getPermitAllUrls().toArray(new String[0])
                                ).permitAll()
                                // 通过编写路径放行接口
                                .antMatchers(
                                        "/login/password",
                                        "/swagger-resources",
                                        "/v3/api-docs",
                                        "/doc.html/**",
                                        "/webjars/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                // 允许所有人访问 permitAll()表示不再拦截
//                .antMatchers("/login").permitAll()
                // hasRole()表示需要指定的角色才能访问资源
//                .antMatchers("/hello").hasRole("ADMIN")
                // 其它所有请求都要认证
//                .anyRequest().authenticated()
//                .and()

                // 处理登出
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(adminLogoutSuccessHandler)
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

                // 将Token校验过滤器配置到过滤器链中，否则不生效，放到UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(passwordAuthenticationLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
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


    /**
     * 获取AuthenticationManager
     *
     * @param configuration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
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
    public PasswordAuthenticationProvider passwordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }



}
