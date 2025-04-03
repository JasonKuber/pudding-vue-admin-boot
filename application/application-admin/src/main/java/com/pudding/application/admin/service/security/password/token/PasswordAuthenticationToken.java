package com.pudding.application.admin.service.security.password.token;

import com.pudding.common.utils.AssertUtils;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 密码认证对象
 * 注：这个token没有设置角色信息，根据实际情况进行添加
 */
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // 自定义属性
    /**
     * 用于存储认证前的凭证，认证成功后可以存放用户信息
     */
    private final Object principal;

    /**
     * 认证前可以用来存储密码等信息，认证后可以存放Token
     */
    private Object credentials;

    /**
     * 登录时间
     */
    @Getter
    private Long loginTime;


    /**
     没经过身份验证时，初始化权限为空，setAuthenticated(false)设置为不可信令牌
     */
    public PasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     经过身份验证后，将权限放进去，setAuthenticated(true)设置为可信令牌
     */
    public PasswordAuthenticationToken(Object principal, Object credentials,Long loginTime,
                                       Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.loginTime = loginTime;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        AssertUtils.isTrue(!authenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
