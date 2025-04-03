package com.pudding.common.utils.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class JwtTokenUtil {

    private final static String secretKey = "your-secret-key-your-secret-key-your-secret-key"; // 256-bit key
    private final static long expirationTime = 1000 * 60 * 60 * 24; // 24 hours
    private final static  Key key = Keys.hmacShaKeyFor(secretKey.getBytes());


    /**
     * 生成Token
     * @param username 用户名
     * @return 生成的JWT Token
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * 生成带有额外Claims的Token
     * @param username 用户名
     * @param extraClaims 额外的Claims信息
     * @return 生成的JWT Token
     */
    public static String generateToken(String username, Map<String, Object> extraClaims) {
        return createToken(extraClaims, username);
    }

    /**
     * 创建Token
     * @param claims Claims信息
     * @param subject Token主题，一般为用户名
     * @return 生成的JWT Token
     */
    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中提取用户名
     * @param token JWT Token
     * @return 提取的用户名
     */
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 提取Token中的特定Claim
     * @param token JWT Token
     * @param claimsResolver Claim解析器
     * @return 提取的Claim值
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 提取Token中的所有Claims
     * @param token JWT Token
     * @return Claims对象
     */
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证Token
     * @param token JWT Token
     * @return Token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            String tokenUsername = extractUsername(token);
            return (StrUtil.isNotEmpty(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证Token
     * @param token JWT Token
     * @param username 用户名
     * @return Token是否有效
     */
    public static boolean validateToken(String token,String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断Token是否过期
     * @param token JWT Token
     * @return 是否过期
     */
    private static boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

}
