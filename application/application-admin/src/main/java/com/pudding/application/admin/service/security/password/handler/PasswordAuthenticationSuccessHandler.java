package com.pudding.application.admin.service.security.password.handler;

import com.alibaba.fastjson2.JSON;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.utils.http.IpUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
import com.pudding.domain.model.convert.PuddingUserEntityConvert;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.vo.LoginUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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
        Map<String,Object>  claims = new HashMap<>();
        claims.put("clientIP",ipAddress);
        String accessToken = JwtTokenUtil.generateAccessToken(entity.getId().toString(),claims);

        String refreshToken = JwtTokenUtil.generateRefreshToken(entity.getId().toString(), claims);

        LoginUserVO loginUserVO = PuddingUserEntityConvert.toVo(entity);
        loginUserVO.setAccessToken(accessToken);
        loginUserVO.setRefreshToken(refreshToken);
        loginUserVO.setLoginTime(System.currentTimeMillis());

        // 返回Token
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ApiResponse.success(loginUserVO)));


    }
}
