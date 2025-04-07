package com.pudding.repository.cache.token.impl;

import com.pudding.common.utils.security.JwtTokenUtil;
import com.pudding.repository.cache.redis.KeyValueOperations;
import com.pudding.repository.cache.redis.StringOperations;
import com.pudding.repository.cache.token.TokenBlacklistCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenBlacklistCacheImpl implements TokenBlacklistCache {

    private final StringOperations<String> stringOperations;
    private final KeyValueOperations keyValueOperations;


    @Override
    public void addRefreshTokenBlacklist(String token, String jti) {
        long ttl = JwtTokenUtil.extractRefreshTokenClaim(token, Claims::getExpiration).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            String redisKey = "blackTokenList:refresh:key:" + jti;
            stringOperations.set(redisKey,token,ttl, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Boolean validateRefreshTokenBlacklist(String token, String jti) {
        String redisKey = "blackTokenList:refresh:key:" + jti;
        return keyValueOperations.hasKey(redisKey);
    }

    @Override
    public void addAccessTokenBlacklist(String token, String jti) {
        long ttl = JwtTokenUtil.extractAccessTokenClaim(token, Claims::getExpiration).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            String redisKey = "blackTokenList:access:key:" + jti;
            stringOperations.set(redisKey,token,ttl, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Boolean validateAccessTokenBlacklist(String token, String jti) {
        String redisKey = "blackTokenList:access:key:" + jti;
        return keyValueOperations.hasKey(redisKey);
    }
}
