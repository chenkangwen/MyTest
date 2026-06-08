package com.example.testcommon.commom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * <p>
 * 支持可配置密钥和过期时间，通过静态方法初始化：
 * <pre>{@code
 *   JwtUtil.init("your-secret-key", 86400000L);
 * }</pre>
 * 如未初始化，使用默认值。
 * </p>
 *
 * @author chenkangwen
 */
public class JwtUtil {

    /** 默认密钥（生产环境请通过 init() 覆盖） */
    private static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /** 默认过期时间：24 小时（毫秒） */
    private static long EXPIRATION_TIME = 86400000L;

    /** 是否已初始化 */
    private static boolean initialized = false;

    /**
     * 初始化 JWT 配置
     *
     * @param secret         密钥字符串（至少 256 位 / 32 字符）
     * @param expirationMs   过期时间（毫秒）
     */
    public static void init(String secret, long expirationMs) {
        if (secret != null && !secret.isEmpty()) {
            SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        if (expirationMs > 0) {
            EXPIRATION_TIME = expirationMs;
        }
        initialized = true;
    }

    /**
     * 生成 JWT Token
     *
     * @param userId 用户 ID
     * @param role   用户角色
     * @return JWT 字符串
     */
    public static String generateToken(String userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 生成 JWT Token（带自定义 claims）
     *
     * @param userId      用户 ID
     * @param role        用户角色
     * @param extraClaims 额外 claims
     * @return JWT 字符串
     */
    public static String generateToken(String userId, String role, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        if (extraClaims != null) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 验证并解析 JWT
     *
     * @param token JWT 字符串
     * @return Claims 对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取过期时间（毫秒）
     */
    public static long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    /**
     * 是否已初始化
     */
    public static boolean isInitialized() {
        return initialized;
    }

    public static void main(String[] args) {
        // 先初始化
        JwtUtil.init("my-test-secret-key-for-jwt-256bit!", 86400000L);

        String token = generateToken("12345", "admin");
        System.out.println("生成的Token: " + token);

        Claims claims = parseToken(token);
        System.out.println("用户ID: " + claims.get("userId"));
        System.out.println("角色: " + claims.get("role"));
        System.out.println("过期时间: " + claims.getExpiration());
    }
}
