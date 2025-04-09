package com.pudding.manager.auth.jwt;

import javax.servlet.http.HttpServletRequest;

public interface TokenManager {
    String refreshToken(HttpServletRequest request);
}
