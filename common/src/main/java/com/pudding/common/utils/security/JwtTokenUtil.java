package com.pudding.common.utils.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil {


    /*  访问Token配置  */
    /**
     * 访问Token 签发机构
     */
    private static String ACCESS_TOKEN_ISSUER;

    /**
     * 访问Token 过期时间
     */
    private static Long ACCESS_TOKEN_EXPIRATION_TIME;

    /**
     * 访问Token 密钥Key
     */
    private static String ACCESS_TOKEN_SECRET_KEY;


    /*  刷新Token配置  */
    /**
     * 刷新Token 签发机构
     */
    private static String REFRESH_TOKEN_ISSUER;

    /**
     * 刷新Token 过期时间
     */
    private static Long REFRESH_TOKEN_EXPIRATION_TIME;

    /**
     * 刷新Token 密钥Key
     */
    private static String REFRESH_TOKEN_SECRET_KEY;

    /* 刷新Token参数 */

    @Value("${jwt.refreshToken.issuer}")
    public void setRefreshTokenIssuer(String issuer) {
        REFRESH_TOKEN_ISSUER = issuer;
    }


    @Value("${jwt.refreshToken.expirationTime}")
    public void setRefreshTokenExpirationTime(Long expirationTime) {
        REFRESH_TOKEN_EXPIRATION_TIME = expirationTime;
    }

    @Value("${jwt.refreshToken.secretKey}")
    public void setRefreshTokenSecretKey(String secretKey) {
        REFRESH_TOKEN_SECRET_KEY = secretKey;
    }


    /* 访问Token参数 */

    @Value("${jwt.accessToken.issuer}")
    public void setAccessTokenIssuer(String issuer) {
        ACCESS_TOKEN_ISSUER = issuer;
    }


    @Value("${jwt.accessToken.expirationTime}")
    public void setAccessTokenExpirationTime(Long expirationTime) {
        ACCESS_TOKEN_EXPIRATION_TIME = expirationTime;
    }

    @Value("${jwt.accessToken.secretKey}")
    public void setAccessTokenSecretKey(String secretKey) {
        ACCESS_TOKEN_SECRET_KEY = secretKey;
    }

    public static void main(String[] args) throws Exception {
        // 使用 HMAC-SHA256 算法生成密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGen.generateKey();
        byte[] keyBytes = secretKey.getEncoded();

        // 转换为 Base64 字符串
        String secretBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
        System.out.println("HS256 Secret Key (Base64): " + secretBase64);
    }


    /**
     * 生成带有额外Claims的刷新Token
     * @param subject 主题
     * @param extraClaims 额外Claims信息
     * @return
     */
    public static String generateAccessToken(String subject, Map<String, Object> extraClaims) {
        return createToken(ACCESS_TOKEN_ISSUER,extraClaims, subject, ACCESS_TOKEN_EXPIRATION_TIME,ACCESS_TOKEN_SECRET_KEY);
    }


    /**
     * 生成带有额外Claims的刷新Token
     * @param subject 主题
     * @param extraClaims 额外Claims信息
     * @return
     */
    public static String generateRefreshToken(String subject, Map<String, Object> extraClaims) {
        return createToken(REFRESH_TOKEN_ISSUER,extraClaims, subject, REFRESH_TOKEN_EXPIRATION_TIME,REFRESH_TOKEN_SECRET_KEY);
    }

    /**
     * 从访问Token中提取主题
     *
     * @param token JWT Token
     * @return 提取的用户名
     */
    public static String extractAccessTokenSubject(String token) {
        return extractAccessTokenClaim(token, Claims::getSubject);
    }


    /**
     * 提取Token中的特定Claim
     *
     * @param token          JWT Token
     * @param claimsResolver Claim解析器
     * @return 提取的Claim值
     */
    public static <T> T extractAccessTokenClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token,ACCESS_TOKEN_SECRET_KEY);
        return claimsResolver.apply(claims);
    }


    /**
     * 从刷新Token中提取主题
     *
     * @param token JWT Token
     * @return 提取的用户名
     */
    public static String extractRefreshTokenSubject(String token) {
        return extractRefreshTokenClaim(token, Claims::getSubject);
    }


    /**
     * 提取刷新Token中的特定Claim
     *
     * @param token          JWT Token
     * @param claimsResolver Claim解析器
     * @return 提取的Claim值
     */
    public static <T> T extractRefreshTokenClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token,REFRESH_TOKEN_SECRET_KEY);
        return claimsResolver.apply(claims);
    }

    /**
     * 验证访问Token
     *
     * @param freshToken 访问JWT Token
     * @return Token是否有效
     */
    public static boolean validateRefreshToken(String freshToken) {
        try {
            String tokenUsername = extractRefreshTokenSubject(freshToken);
            return (StrUtil.isNotEmpty(tokenUsername) && !isRefreshTokenExpired(freshToken));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证访问Token
     *
     * @param accessToken 访问JWT Token
     * @return Token是否有效
     */
    public static boolean validateAccessToken(String accessToken) {
        try {
            String tokenUsername = extractAccessTokenSubject(accessToken);
            return (StrUtil.isNotEmpty(tokenUsername) && !isAccessTokenExpired(accessToken));
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 创建Token
     *
     * @param claims         Claims信息
     * @param subject        Token主题，一般为用户名
     * @param expirationTime 过期时间
     * @return 生成的JWT Token
     */
    private static String createToken(String issuer,Map<String, Object> claims, String subject ,Long expirationTime,String secretKey) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }




    /**
     * 提取Token中的所有Claims
     * @param token JWT Token
     * @param secretKey JWT 密钥Key
     * @return Claims对象
     */
    private static Claims extractAllClaims(String token,String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }




    /**
     * 判断访问Token是否过期
     *
     * @param accessToken 访问JWT Token
     * @return 是否过期
     */
    private static boolean isAccessTokenExpired(String accessToken) {
        Date expiration = extractAccessTokenClaim(accessToken, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * 判断刷新Token是否过期
     *
     * @param refreshToken 刷新JWT Token
     * @return 是否过期
     */
    private static boolean isRefreshTokenExpired(String refreshToken) {
        Date expiration = extractAccessTokenClaim(refreshToken, Claims::getExpiration);
        return expiration.before(new Date());
    }

}
