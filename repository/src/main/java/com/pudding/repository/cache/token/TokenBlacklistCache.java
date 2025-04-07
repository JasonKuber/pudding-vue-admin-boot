package com.pudding.repository.cache.token;

/**
 * Token黑名单
 */
public interface TokenBlacklistCache {

    /**
     * 将刷新Token加入黑名单为每个Token单独设置过期时间
     * @param token jwt Token
     * @param jti Token 唯一标识
     */
    void addRefreshTokenBlacklist(String token,String jti);

    /**
     * 校验刷新Token是否在黑名单内
     * @param token jwt Token
     * @param jti Token唯一标识
     * @return true表示在黑名单 false表示不在
     */
    Boolean validateRefreshTokenBlacklist(String token,String jti);

    /**
     * 将访问Token加入黑名单为每个Token单独设置过期时间
     * @param token jwt Token
     * @param jti Token 唯一标识
     */
    void addAccessTokenBlacklist(String token,String jti);

    /**
     * 校验访问Token是否在黑名单内
     * @param token jwt Token
     * @param jti Token唯一标识
     * @return true表示在黑名单 false表示不在
     */
    Boolean validateAccessTokenBlacklist(String token,String jti);




}
