package com.pudding.manager.auth.jwt.impl;

import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.manager.auth.jwt.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenManagerImpl implements TokenManager {

    @Override
    public String refreshToken(HttpServletRequest request) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst().map(Cookie::getValue).orElse(null);

        AssertUtils.isNotEmpty(refreshToken, ResultCodeEnum.TOKEN_INVALID);
        boolean b = JwtTokenUtil.validateRefreshToken(refreshToken);
        AssertUtils.isTrue(b,ResultCodeEnum.TOKEN_INVALID);

        String ipAddress = IpUtils.getIpAddress(request);
        Claims claim = JwtTokenUtil.extractRefreshTokenClaim(refreshToken);

        String clientIP = claim.get("clientIP", String.class);
        AssertUtils.equals(clientIP,ipAddress,ResultCodeEnum.TOKEN_IP_NO_MATCH);

        String userId = claim.getSubject();
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientIP", clientIP);
        return JwtTokenUtil.generateAccessToken(userId, claims);
    }
}
