package com.pudding.application.admin.service.security.handler;

import com.pudding.common.constants.http.HeaderConstants;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.utils.http.HeaderUtils;
import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.repository.cache.token.TokenBlacklistCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutHandler自定义登出以后处理逻辑,比如记录在线时长,拉黑token等等
 */
@Component
@RequiredArgsConstructor
public class AdminLogoutHandler implements LogoutHandler {

    private final TokenBlacklistCache tokenBlacklistCache;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessToken = HeaderUtils.getAuthorizationBearerToken(request);
        AssertUtils.isNotEmpty(accessToken, ResultCodeEnum.TOKEN_INVALID);

        String refreshToken = request.getHeader(HeaderConstants.REFRESH_TOKEN);
        AssertUtils.isNotEmpty(refreshToken,ResultCodeEnum.TOKEN_INVALID);

        String accessJti = JwtTokenUtil.extractAccessTokenClaim(accessToken, Claims::getId);
        tokenBlacklistCache.addAccessTokenBlacklist(accessToken,accessJti);

        String refreshJti = JwtTokenUtil.extractRefreshTokenClaim(refreshToken, Claims::getId);
        tokenBlacklistCache.addRefreshTokenBlacklist(refreshToken,refreshJti);

        // 清理SecurityContext
        SecurityContextHolder.clearContext();

    }
}
