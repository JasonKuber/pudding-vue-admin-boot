package com.pudding.application.admin.service.security.password.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
import com.pudding.domain.model.convert.PuddingUserEntityConvert;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.vo.LoginUserVO;
import com.pudding.repository.cache.redis.StringOperations;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StringOperations<PuddingUserEntity> stringOperations;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 获取密码登录Token
        PasswordAuthenticationToken passwordAuthenticationToken = (PasswordAuthenticationToken) authentication;

        // 获取到认证成功从PasswordAuthenticationProvider中返回的数据
        PuddingUserEntity entity = (PuddingUserEntity) passwordAuthenticationToken.getPrincipal();

        String snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr();
        String ipAddress = IpUtils.getIpAddress(request);
        Map<String,Object>  claims = new HashMap<>();
        claims.put("clientIP",ipAddress);
        String accessToken = JwtTokenUtil.generateAccessToken(snowflakeNextIdStr,claims);

        String refreshToken = JwtTokenUtil.generateRefreshToken(entity.getId().toString(), claims);

        LoginUserVO loginUserVO = PuddingUserEntityConvert.toVo(entity);
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setRefreshToken(refreshToken);
        loginUserVO.setLoginTime(System.currentTimeMillis());

        String redisKey = "token:key:" + snowflakeNextIdStr;
        // 解析一下AccessToken 获取到过期时间并延迟五秒再过期
        Date acceessTokenExpirationDate = JwtTokenUtil.extractAccessTokenClaim(accessToken, Claims::getExpiration);
        stringOperations.set(redisKey,entity,acceessTokenExpirationDate.getTime() + 5000L, TimeUnit.MILLISECONDS);

        // 返回Token
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ApiResponse.success(loginUserVO)));


    }
}
