package com.pudding.bootstrap.admin.security.handler;

import com.alibaba.fastjson2.JSON;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.vo.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当认证后的用户受保护的资源时，权限不够，则会进入这个处理器
 */
@Component
public class RequestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {


        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(ApiResponse.error(ResultCodeEnum.LIMITED_ACCESS)));

    }
}
