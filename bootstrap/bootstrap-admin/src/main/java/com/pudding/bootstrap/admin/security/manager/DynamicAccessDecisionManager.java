package com.pudding.bootstrap.admin.security.manager;

import com.pudding.application.admin.service.security.PermissionGrantedAuthority;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * 权限决策管理器：根据用户拥有的权限和目标请求所需权限做对比，决定是否放行
 */
public class DynamicAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if (configAttributes == null || configAttributes.isEmpty() || authentication instanceof AnonymousAuthenticationToken) return;


        // 获取当前用户拥有的权限
        Collection<PermissionGrantedAuthority> authorities = (Collection<PermissionGrantedAuthority>) authentication.getAuthorities();

        // 遍历请求所需权限，只要用户拥有任意一个，即可放行
        for (ConfigAttribute configAttribute : configAttributes) {
            for (PermissionGrantedAuthority authority : authorities) {
                // ADMIN则直接允许访问
                if (authority.getIsAdmin()) {
                    return;
                }

                if (configAttribute.getAttribute().equals(authority.getAuthority())) {
                    // 匹配成功，允许访问
                    return;
                }
            }
        }

        // 没有匹配权限，抛出拒绝访问异常
        throw new AccessDeniedException("无访问权限");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
