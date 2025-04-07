package com.pudding.common.constants.http;

public interface HeaderConstants {

    /**
     * Authorization 认证
     */
    String AUTHORIZATION = "Authorization";

    /**
     * 认证类型 Bearer ：常见于 OAuth 和 JWT 授权
     */
    String AUTHORIZATION_TYPE_BEARER = "Bearer ";

    /**
     * 刷新Token
     */
    String REFRESH_TOKEN = "Refresh-Token";

    /**
     * 登录方式
     */
    String LOGIN_TYPE = "login-type";

}
