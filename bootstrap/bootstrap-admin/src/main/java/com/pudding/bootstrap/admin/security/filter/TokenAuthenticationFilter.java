package com.pudding.bootstrap.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.constants.http.HeaderConstants;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.http.HeaderUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.manager.user.PuddingUserManager;
import com.pudding.repository.cache.token.TokenBlacklistCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@WebFilter
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private PuddingUserManager puddingUserManager;

    @Resource
    private TokenBlacklistCache tokenBlacklistCache;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        // 前端传递的AccessToken
        String accessToken = HeaderUtils.getAuthorizationBearerToken(request);
        if (StrUtil.isNotBlank(accessToken)) {

            // 解析和校验Token
            String loginType = request.getHeader(HeaderConstants.LOGIN_TYPE);
            AssertUtils.isNotEmpty(loginType, ResultCodeEnum.REQUEST_PARAM_REQUIRED_ERROR);

            // 校验AccessToken
            AssertUtils.isTrue(JwtTokenUtil.validateAccessToken(accessToken), ResultCodeEnum.TOKEN_INVALID);


            // 获取accessToken中的数据
            Claims claim = JwtTokenUtil.extractAccessTokenClaim(accessToken);

            // 校验IP一致
            String clientIP =(String) claim.get("clientIP");
            AssertUtils.equals(clientIP, IpUtils.getIpAddress(request),ResultCodeEnum.TOKEN_IP_NO_MATCH);

            // 校验Token是否已经被拉黑
            String jti = claim.getId();
            AssertUtils.isFalse(tokenBlacklistCache.validateAccessTokenBlacklist(accessToken,jti),ResultCodeEnum.TOKEN_INVALID);


            String userId = claim.getSubject();
            PuddingUserEntity puddingUserEntity = puddingUserManager.getCacheById(userId);

            Authentication authentication = null;
            if (loginType.equals("password")) {
                authentication = new PasswordAuthenticationToken(puddingUserEntity, null, Collections.emptyList());
                // 将authentication存入 ThreadLocal,方便后续获取用户信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }

        // 放行
        filterChain.doFilter(request, response);


    }

    /**
     * 因为使用的自定义File过滤器，所以在此放行静态资源
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
