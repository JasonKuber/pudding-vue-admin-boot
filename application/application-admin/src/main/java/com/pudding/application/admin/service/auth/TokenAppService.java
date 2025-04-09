package com.pudding.application.admin.service.auth;

import javax.servlet.http.HttpServletRequest;

public interface TokenAppService {
    String refreshToken(HttpServletRequest request);
}
