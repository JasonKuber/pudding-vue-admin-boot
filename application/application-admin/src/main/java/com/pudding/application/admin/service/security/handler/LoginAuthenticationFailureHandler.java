package com.pudding.application.admin.service.security.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.vo.ApiResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败操作
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {

        ApiResponse result = null;

        // BadCredentialsException 这个异常一般是用户名或密码错误
        if (exception instanceof BadCredentialsException) {
            result = ApiResponse.error(ResultCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }

        // AccountExpiredException 账号过期
//        if (exception instanceof AccountExpiredException) {
//
//        }

        // CredentialsExpiredException 密码过期
//        if (exception instanceof CredentialsExpiredException){
//
//        }

        // DisabledException 账号不可用
        if (exception instanceof DisabledException) {
            result = ApiResponse.error(ResultCodeEnum.ACCOUNT_DISABLED);
        }

        // LockedException 账号锁定
//        if (exception instanceof LockedException) {
//
//        }

        // 用户不存在
//        if (exception instanceof InternalAuthenticationServiceException) {
//
//        }

        if (ObjectUtil.isNull(result)) {
            result = ApiResponse.error("登录失败");
        }

        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));

    }
}
