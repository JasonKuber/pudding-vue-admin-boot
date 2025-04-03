package com.pudding.application.admin.service.security.password.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.common.vo.ApiResponse;
import com.pudding.domain.model.convert.PuddingUserEntityConvert;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.domain.model.vo.LoginUserVO;
import com.pudding.repository.cache.redis.StringOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StringOperations<String> stringOperations;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 获取密码登录Token
        PasswordAuthenticationToken passwordAuthenticationToken = (PasswordAuthenticationToken) authentication;

        // 获取到认证成功从PasswordAuthenticationProvider中返回的数据
        PuddingUserEntity entity = (PuddingUserEntity) passwordAuthenticationToken.getPrincipal();
        Long loginTime = passwordAuthenticationToken.getLoginTime();

        String token = JwtTokenUtil.generateToken(entity.getAccount());
        String uid = IdUtil.getSnowflakeNextIdStr();
        LoginUserVO loginUserVO = PuddingUserEntityConvert.toVo(entity);
        loginUserVO.setToken(token);
        loginUserVO.setUid(uid);
        loginUserVO.setLoginTime(loginTime);

        String redisKey = "token:key:" + uid;
        // 设置30分钟过期时间超过直接过期退出登录
        stringOperations.set(redisKey,token,30, TimeUnit.MINUTES);

        // 返回Token
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ApiResponse.success(loginUserVO)));


    }
}
