package com.pudding.api.admin.security.password.filter;

import com.pudding.application.admin.service.security.password.token.PasswordAuthenticationToken;
import com.pudding.common.utils.AssertUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 密码登录认证的filter
 */
public class PasswordAuthenticationLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/login/password", "POST");

    private static final String REQUEST_PARAM_IDENTIFIER = "identifier";
    private static final String REQUEST_PARAM_PASSWORD = "password";

    public PasswordAuthenticationLoginFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        AssertUtils.equals(request.getMethod(),"POST","不支持的请求方法: " + request.getMethod());


        String identifier = request.getParameter(REQUEST_PARAM_IDENTIFIER);
        identifier = identifier.trim();
        String password = request.getParameter(REQUEST_PARAM_PASSWORD);
        // 封装到token中提交
        PasswordAuthenticationToken authenticationToken = new PasswordAuthenticationToken(identifier, password);
        this.setDetails(request,authenticationToken);
        return getAuthenticationManager().authenticate(authenticationToken);
    }


    protected void setDetails(HttpServletRequest request,PasswordAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }




}
