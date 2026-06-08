package com.example.testservice.config;

import com.example.testcommon.commom.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * JWT 初始化配置
 * <p>
 * 在应用启动时从配置文件读取 JWT 密钥和过期时间，
 * 初始化 JwtUtil 工具类。
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Slf4j
@Configuration
public class JwtInitConfig {

    @Value("${jwt.secret:partner-jwt-default-secret-key-256bit}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @PostConstruct
    public void init() {
        JwtUtil.init(secret, expiration);
        log.info("JWT 已初始化: expiration={}ms", expiration);
    }
}
