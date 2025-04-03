package com.pudding.bootstrap.admin.security.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutHandler自定义登出以后处理逻辑,比如记录在线时长等等
 */
@Configuration
public class AdminLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }
}
