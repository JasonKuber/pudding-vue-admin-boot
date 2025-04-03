package com.pudding.bootstrap.admin.security.handler;

import com.alibaba.fastjson2.JSON;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户访问受保护的资源，但是用户没有通过认证则会进入这个处理器
 */
@Slf4j
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(ApiResponse.error(ResultCodeEnum.NOT_LOGIN)));

    }
}
