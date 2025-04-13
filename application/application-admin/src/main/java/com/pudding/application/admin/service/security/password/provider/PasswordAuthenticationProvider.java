package com.pudding.application.admin.service.security.password.provider;


import com.pudding.application.admin.service.security.PermissionGrantedAuthority;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.domain.model.entity.PuddingPermissionRoleEntity;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.entity.PuddingUserRoleEntity;
import com.pudding.manager.auth.login.PasswordLoginManager;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import com.pudding.manager.permission.PuddingPermissionRoleManager;
import com.pudding.manager.user.PuddingUserRoleManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码登录自定义Provider
 */
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private PasswordLoginManager passwordLoginManager;

    @Resource
    private PuddingUserRoleManager puddingUserRoleManager;

    @Resource
    private PuddingPermissionRoleManager puddingPermissionRoleManager;

    @Resource
    private PuddingApiPermissionManager puddingPermissionManager;




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

        // 获取用户关联信息
        PuddingUserRoleEntity puddingUserRoleEntity = puddingUserRoleManager.getUserRoleByUserId(puddingUserEntity.getId());

        // 用户角色权限
        List<PuddingPermissionRoleEntity> permissionRoleEntityList =  puddingPermissionRoleManager.getPermissionRoleByRoleId(puddingUserRoleEntity.getRoleId());

        // 查询权限
        List<Long> permIdList = permissionRoleEntityList.stream()
                .map(PuddingPermissionRoleEntity::getPermId)
                .collect(Collectors.toList());
        List<PuddingApiPermissionEntity> permissionEntityList =  puddingPermissionManager.listPermissionByIdList(permIdList);

        List<GrantedAuthority> grantedAuthorityList  = new ArrayList<>();
        for (PuddingApiPermissionEntity permission : permissionEntityList) {
            PermissionGrantedAuthority authority = new PermissionGrantedAuthority(permission.getPermCode());
            grantedAuthorityList.add(authority);
        }

        AdminLoginUser loginUser = new AdminLoginUser(puddingUserEntity.getId(),
                puddingUserEntity.getUserName(),
                puddingUserEntity.getPassword(),
                puddingUserEntity.getPhoneNumber(),
                puddingUserEntity.getAccount(),
                puddingUserRoleEntity.getRoleId(),
                grantedAuthorityList);


        return new UsernamePasswordAuthenticationToken(loginUser,
                null,
                grantedAuthorityList);
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
