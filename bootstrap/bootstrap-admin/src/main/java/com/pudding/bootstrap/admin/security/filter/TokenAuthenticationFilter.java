package com.pudding.bootstrap.admin.security.filter;

import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
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
    private StringOperations<String> stringOperations;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization Bearer");
        String uid = request.getHeader("uid");
        String loginType = request.getHeader("login-type");
        String loginTime = request.getHeader("login-time");
        AssertUtils.isNotEmpty(token, ResultCodeEnum.NOT_LOGIN);
        AssertUtils.isNotEmpty(uid, ResultCodeEnum.NOT_LOGIN);

        AssertUtils.isTrue(JwtTokenUtil.validateToken(token), ResultCodeEnum.TOKEN_INVALID);

        String redisKey = "token:key:" + uid;
        String redisToken = stringOperations.get(redisKey);
        AssertUtils.isNotNull(redisToken,ResultCodeEnum.TOKEN_INVALID);
        AssertUtils.equals(token,redisToken,ResultCodeEnum.TOKEN_INVALID);

        String username = JwtTokenUtil.extractUsername(token);

        Authentication authentication = null;
        if (loginType.equals("password")) {
            authentication = new PasswordAuthenticationToken(loginTime, username, Long.valueOf(loginTime), Collections.emptyList());
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
