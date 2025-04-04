package com.pudding.bootstrap.admin.security.filter;

import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.repository.cache.redis.StringOperations;
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
    private StringOperations<PuddingUserEntity> stringOperations;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 前端传递的AccessToken
        String accessToken = request.getHeader("Authorization Bearer");
        AssertUtils.isNotEmpty(accessToken, ResultCodeEnum.NOT_LOGIN);
        String loginType = request.getHeader("login-type");
        AssertUtils.isNotEmpty(loginType, ResultCodeEnum.REQUEST_PARAM_REQUIRED_ERROR);

        // 校验AccessToken
        AssertUtils.isTrue(JwtTokenUtil.validateAccessToken(accessToken), ResultCodeEnum.TOKEN_INVALID);

        // 获取accessToken中的数据
        String accessId = JwtTokenUtil.extractAccessTokenSubject(accessToken);
        String clientIP =(String) JwtTokenUtil.extractAccessTokenClaim(accessToken, claims -> claims.get("clientIP"));
        AssertUtils.equals(clientIP, IpUtils.getIpAddress(request),ResultCodeEnum.TOKEN_IP_NO_MATCH);

        String redisKey = "token:key:" + accessId;
        PuddingUserEntity puddingUserEntity = stringOperations.get(redisKey);

        Authentication authentication = null;
        if (loginType.equals("password")) {
            authentication = new PasswordAuthenticationToken(puddingUserEntity, null, Collections.emptyList());
            // 将authentication存入 ThreadLocal,方便后续获取用户信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
