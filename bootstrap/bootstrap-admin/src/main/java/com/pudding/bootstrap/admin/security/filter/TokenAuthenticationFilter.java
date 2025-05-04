package com.pudding.bootstrap.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.pudding.application.admin.service.security.PermissionGrantedAuthority;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.exception.BusinessException;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.utils.http.HeaderUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
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
import org.springframework.security.core.AuthenticationException;
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

            try {


                // 解析accessToken中的数据
                Claims claim = getAccessTokenClaims(accessToken,request,response);


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
            } catch (BusinessException businessException) {
                handleException(businessException,response);
                return;
            } catch (AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                throw ex;
            }


        }

        // 放行
        filterChain.doFilter(request, response);


    }

    private Claims getAccessTokenClaims(String accessToken,HttpServletRequest request,HttpServletResponse response) {
        // 校验accessToken
        checkAccessToken(accessToken);

        // 解析accessToken中的数据
        Claims claim = JwtTokenUtil.extractAccessTokenClaim(accessToken);

        // 校验claims
        checkJwtClaims(claim,request,response);

        // 校验Token是否已经被拉黑
        String jti = claim.getId();
        if (tokenBlacklistCache.validateAccessTokenBlacklist(accessToken, jti)) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
        return claim;
    }

    private void checkAccessToken(String accessToken) {
        if (!JwtTokenUtil.validateAccessToken(accessToken)) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
    }


    private void checkJwtClaims(Claims claim,HttpServletRequest request,HttpServletResponse response) {
        // 校验IP一致
        String clientIP = claim.get(JwtConstants.CLIENT_IP, String.class);
        if (!clientIP.equals(IpUtils.getIpAddress(request))) {
            throw new BusinessException(ResultCodeEnum.TOKEN_IP_NO_MATCH);
        }


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

    private void handleException(BusinessException businessException, HttpServletResponse response) {
        // 自定义异常处理，例如返回特定的错误信息
        try {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(ApiResponse.error(businessException.getErrorCode(),businessException.getErrorMsg())));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
