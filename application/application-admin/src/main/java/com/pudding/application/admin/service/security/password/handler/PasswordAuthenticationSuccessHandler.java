package com.pudding.application.admin.service.security.password.handler;

import com.alibaba.fastjson2.JSON;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
import com.pudding.domain.model.convert.PuddingUserEntityConvert;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.vo.LoginUserVO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Environment environment;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 获取密码登录Token
        PasswordAuthenticationToken passwordAuthenticationToken = (PasswordAuthenticationToken) authentication;

        // 获取到认证成功从PasswordAuthenticationProvider中返回的数据
        PuddingUserEntity entity = (PuddingUserEntity) passwordAuthenticationToken.getPrincipal();

        String ipAddress = IpUtils.getIpAddress(request);
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientIP", ipAddress);
        String accessToken = JwtTokenUtil.generateAccessToken(entity.getId().toString(), claims);

        String refreshToken = JwtTokenUtil.generateRefreshToken(entity.getId().toString(), claims);

        LoginUserVO loginUserVO = PuddingUserEntityConvert.toVo(entity);
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setRefreshToken(refreshToken);
        loginUserVO.setLoginTime(System.currentTimeMillis());

        Date refreshTokenExpirationDate = JwtTokenUtil.extractRefreshTokenClaim(refreshToken, Claims::getExpiration);
        long durationSeconds = (refreshTokenExpirationDate.getTime() - System.currentTimeMillis()) / 1000;

        // 返回Token
        boolean enableSecure = environment.acceptsProfiles("prod");
        response.setContentType("application/json;charset=UTF-8");
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // 只允许服务端访问， JavaScript无法读取，防止XSS攻击
                .secure(enableSecure) // 仅在HTTPS连接中传输，防止中间人攻击
                .sameSite("Strict") // 限制跨站请求携带Cookie，防止CSRF攻击 Strict 表示完全禁止跨站点请求携带这个 Cookie
                .path("/") // Cookie 在整个网站范围内有效（根路径）
                .maxAge(Duration.ofSeconds(durationSeconds)) // 设置过期时间
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.getWriter().write(JSON.toJSONString(ApiResponse.success(loginUserVO)));


    }
}
