package com.pudding.bootstrap.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.pudding.application.admin.service.security.PermissionGrantedAuthority;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.http.HeaderUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.domain.model.constants.auth.JwtConstants;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.domain.model.entity.PuddingPermissionRoleEntity;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import com.pudding.manager.permission.PuddingPermissionRoleManager;
import com.pudding.manager.user.PuddingUserManager;
import com.pudding.repository.cache.token.TokenBlacklistCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@WebFilter
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private PuddingUserManager puddingUserManager;

    @Resource
    private TokenBlacklistCache tokenBlacklistCache;

    @Resource
    private PuddingPermissionRoleManager puddingPermissionRoleManager;

    @Resource
    private PuddingApiPermissionManager puddingPermissionManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        // 前端传递的AccessToken
        String accessToken = HeaderUtils.getAuthorizationBearerToken(request);
        if (StrUtil.isNotBlank(accessToken)) {

            // 校验AccessToken
            AssertUtils.isTrue(JwtTokenUtil.validateAccessToken(accessToken), ResultCodeEnum.TOKEN_INVALID);

            // 解析accessToken中的数据
            Claims claim = JwtTokenUtil.extractAccessTokenClaim(accessToken);

            // 校验IP一致
            String clientIP = claim.get(JwtConstants.CLIENT_IP, String.class);
            AssertUtils.equals(clientIP, IpUtils.getIpAddress(request), ResultCodeEnum.TOKEN_IP_NO_MATCH);

            // 校验Token是否已经被拉黑
            String jti = claim.getId();
            AssertUtils.isFalse(tokenBlacklistCache.validateAccessTokenBlacklist(accessToken, jti), ResultCodeEnum.TOKEN_INVALID);

            String userId = claim.getSubject();
            PuddingUserEntity puddingUserEntity = puddingUserManager.getCacheById(userId);

            Boolean isAdmin = claim.get(JwtConstants.IS_ADMIN, Boolean.class);

            UsernamePasswordAuthenticationToken authentication = null;
            if (!isAdmin) {
                Long roleId = claim.get(JwtConstants.ROLE_ID, Long.class);
                // 用户角色权限
                List<PuddingPermissionRoleEntity> permissionRoleEntityList = puddingPermissionRoleManager.getPermissionRoleByRoleId(roleId);

                // 查询权限
                List<Long> permIdList = permissionRoleEntityList.stream()
                        .map(PuddingPermissionRoleEntity::getPermId)
                        .collect(Collectors.toList());
                List<PuddingApiPermissionEntity> permissionEntityList = puddingPermissionManager.listPermissionByIdList(permIdList);

                authentication = getUsernamePasswordAuthenticationToken(permissionEntityList, puddingUserEntity, roleId);
            } else {
                authentication = getUsernamePasswordAuthenticationToken(puddingUserEntity);
            }

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 放行
        filterChain.doFilter(request, response);


    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(PuddingUserEntity puddingUserEntity) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new PermissionGrantedAuthority(true));

        AdminLoginUser loginUser = new AdminLoginUser(puddingUserEntity.getId(),
                puddingUserEntity.getUserName(),
                puddingUserEntity.getPassword(),
                puddingUserEntity.getAccount(),
                0L,
                puddingUserEntity.getIsAdmin(),
                grantedAuthorityList);

        return new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(List<PuddingApiPermissionEntity> permissionEntityList, PuddingUserEntity puddingUserEntity, Long roleId) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        for (PuddingApiPermissionEntity permission : permissionEntityList) {
            PermissionGrantedAuthority authority = new PermissionGrantedAuthority(permission.getPermCode());
            grantedAuthorityList.add(authority);
        }

        AdminLoginUser loginUser = new AdminLoginUser(puddingUserEntity.getId(),
                puddingUserEntity.getUserName(),
                puddingUserEntity.getPassword(),
                puddingUserEntity.getAccount(),
                roleId,
                puddingUserEntity.getIsAdmin(),
                grantedAuthorityList);

        return new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
    }

    /**
     * 因为使用的自定义File过滤器，所以在此放行静态资源
     *
     * @param request current HTTP request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger") ||
                path.startsWith("/v2/api-docs/**") ||
                path.startsWith("/doc.html") ||
                path.startsWith("/swagger-ui/**") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/favicon.ico") ||
                path.startsWith("/api-docs-ext/**") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/static/**") ||
                path.startsWith("/public/**") ||
                path.startsWith("/js/**") ||
                path.startsWith("/css/**") ||
                path.startsWith("/webjars") ||
                path.startsWith("/image/**")
                ;
    }
}
