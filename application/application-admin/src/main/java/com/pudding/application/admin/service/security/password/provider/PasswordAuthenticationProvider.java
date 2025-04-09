package com.pudding.application.admin.service.security.password.provider;


import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.manager.auth.login.PasswordLoginManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 密码登录自定义Provider
 */
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private PasswordLoginManager passwordLoginManager;



    /**
     * 在此方法进行认证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordAuthenticationToken passwordAuthenticationToken = (PasswordAuthenticationToken) authentication;

        // 账号
        String principal = (String) passwordAuthenticationToken.getPrincipal();
        // 密码
        String credentials = (String) passwordAuthenticationToken.getCredentials();

        PuddingUserEntity puddingUserEntity = passwordLoginManager.login(principal, credentials);

        return new PasswordAuthenticationToken(puddingUserEntity,
                null,
                Collections.emptyList());
    }

    /**
     * 此方法决定Provider能够处理哪些Token，此Provider只能处理密码登录方式的Token，这里也是多种登录方式的核心
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //Manager传递token给provider，调用本方法判断该provider是否支持该token。不支持则尝试下一个filter
        //本类支持的token类：PasswordAuthenticationToken
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
