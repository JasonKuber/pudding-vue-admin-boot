package com.pudding.application.admin.service.security;

import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态权限元数据源：提供给 Spring Security，用于判断请求需要哪些权限
 */
@Slf4j
@Component
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher,Collection<ConfigAttribute>> permissionMap = new ConcurrentHashMap<>();

    @Resource
    private PuddingApiPermissionManager puddingApiPermissionManager;

    @PostConstruct
    public void init() {
        this.loadPermissions();
    }

    public void refresh() {
        this.permissionMap.clear();
        this.loadPermissions();
    }

    private void loadPermissions() {
        log.info("[ Admin API Permission 初始化开始 ]");
        List<PuddingApiPermissionEntity> permissionEntityList = puddingApiPermissionManager.allPermission();
        for (PuddingApiPermissionEntity permission : permissionEntityList) {
            // 创建一个 URL + Method 的匹配器
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(permission.getPermApi(), permission.getMethod());

            // 将匹配器与该权限需要的角色（ConfigAttribute）绑定
            permissionMap.put(matcher, Arrays.asList(new SecurityConfig(permission.getPermCode())));
        }

        log.info("[ Admin API Permission 初始化完毕 ]");
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 根据当前请求查找所需的权限
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : permissionMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        // 如果找不到匹配，说明该请求不需要权限
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
