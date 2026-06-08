package com.example.testservice.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWT Token - Shiro 认证令牌封装
 * <p>
 * 将 JWT 字符串包装为 Shiro 可识别的 AuthenticationToken，
 * principal 和 credentials 均使用 JWT 字符串本身。
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    /** JWT 令牌字符串 */
    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.substring(0, Math.min(token.length(), 20)) + "...";
    }
}
