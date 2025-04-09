package com.pudding.application.admin.service.auth.impl;

import com.pudding.application.admin.service.auth.TokenAppService;
import com.pudding.manager.auth.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class TokenAppServiceImpl implements TokenAppService {

    private final TokenManager tokenManager;


    @Override
    public String refreshToken(HttpServletRequest request) {
        return tokenManager.refreshToken(request);
    }
}
