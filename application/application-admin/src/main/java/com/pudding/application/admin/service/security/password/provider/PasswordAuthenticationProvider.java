package com.pudding.application.admin.service.security.password.provider;


import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.entity.PuddingUserRoleEntity;
import com.pudding.manager.auth.login.PasswordLoginManager;
import com.pudding.manager.user.PuddingUserRoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 密码登录自定义Provider
 */
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(PasswordAuthenticationProvider.class);
    @Resource
    private PasswordLoginManager passwordLoginManager;

    @Resource
    private PuddingUserRoleManager puddingUserRoleManager;

    /**
     * 在此方法进行认证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 账号
        String identifier = (String) authentication.getPrincipal();
        // 密码
        String password = (String) authentication.getCredentials();

        // 获取用户信息
        PuddingUserEntity puddingUserEntity = passwordLoginManager.login(identifier, password);
        AdminLoginUser loginUser = new AdminLoginUser();
        loginUser.setUserId(puddingUserEntity.getId());
        loginUser.setAccount(puddingUserEntity.getAccount());
        loginUser.setUserName(puddingUserEntity.getUserName());
        loginUser.setPassword(puddingUserEntity.getPassword());
        loginUser.setIsAdmin(puddingUserEntity.getIsAdmin());
        if (!puddingUserEntity.getIsAdmin()) {
            PuddingUserRoleEntity puddingUserRoleEntity = puddingUserRoleManager.getUserRoleByUserId(puddingUserEntity.getId());
            loginUser.setRoleId(puddingUserRoleEntity.getRoleId());
        }

        return new UsernamePasswordAuthenticationToken(loginUser,
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
