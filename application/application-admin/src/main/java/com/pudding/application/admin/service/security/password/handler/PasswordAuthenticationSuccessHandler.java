package com.pudding.application.admin.service.security.password.handler;

import com.alibaba.fastjson2.JSON;
import com.pudding.domain.model.constants.auth.JwtConstants;
import com.pudding.domain.model.constants.auth.LoginTypeConstants;
import com.pudding.domain.model.constants.http.CookieConstants;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
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

        // 获取到认证成功从AuthenticationProvider中返回的数据
        AdminLoginUser entity = (AdminLoginUser) authentication.getPrincipal();

        String ipAddress = IpUtils.getIpAddress(request);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.CLIENT_IP, ipAddress);
        claims.put(JwtConstants.LOGIN_TYPE, LoginTypeConstants.PASSWORD);
        claims.put(JwtConstants.ROLE_ID,entity.getRoleId());
        claims.put(JwtConstants.IS_ADMIN,entity.getIsAdmin());
        String accessToken = JwtTokenUtil.generateAccessToken(entity.getUserId().toString(), claims);

        String refreshToken = JwtTokenUtil.generateRefreshToken(entity.getUserId().toString(), claims);

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUserName(entity.getUsername());
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setLoginTime(System.currentTimeMillis());

        Date refreshTokenExpirationDate = JwtTokenUtil.extractRefreshTokenClaim(refreshToken, Claims::getExpiration);
        long durationSeconds = (refreshTokenExpirationDate.getTime() - System.currentTimeMillis()) / 1000;

        // 返回Token
        boolean enableSecure = environment.acceptsProfiles("prod");
        response.setContentType("application/json;charset=UTF-8");
        ResponseCookie cookie = ResponseCookie.from(CookieConstants.REFRESH_TOKEN, refreshToken)
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
