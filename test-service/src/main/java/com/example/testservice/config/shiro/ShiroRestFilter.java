package com.example.testservice.config.shiro;

import com.alibaba.fastjson.JSON;
import com.example.testcommon.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Shiro REST 统一认证过滤器
 * <p>
 * 认证策略（按优先级）：
 * <ol>
 *   <li>已有有效 Session（通过 Cookie 传递的 Session ID）→ 直接放行</li>
 *   <li>请求头携带 {@code Authorization: Bearer <jwt>} → JWT 认证</li>
 *   <li>以上均不满足 → 返回 401 JSON</li>
 * </ol>
 * <p>
 * Redis 分布式 Session：
 * Session 存储在 Redis 中，多实例共享，天然支持水平扩展。
 * 客户端只需携带 Cookie（sid），任意实例均可读取 Session。
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Slf4j
public class ShiroRestFilter extends AccessControlFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 判断是否允许访问
     * <p>
     * 1. 检查是否有已认证的 Session（Session 来自 Redis 共享存储）
     * 2. 检查是否有 JWT Bearer Token
     * </p>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) {
        // OPTIONS 预检请求直接放行
        if (isOptionsRequest(request)) {
            return true;
        }

        Subject subject = getSubject(request, response);

        // 策略1: 已有有效 Session（Redis 中）
        if (subject.isAuthenticated()) {
            log.debug("Session 认证通过: principal={}", subject.getPrincipal());
            return true;
        }

        // 策略2: 尝试 JWT Bearer Token 认证
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length()).trim();
            if (!jwt.isEmpty()) {
                try {
                    subject.login(new JwtToken(jwt));
                    log.debug("JWT 认证通过");
                    return true;
                } catch (AuthenticationException e) {
                    log.warn("JWT 认证失败: {}", e.getMessage());
                }
            }
        }

        return false;
    }

    /**
     * 认证失败，返回 401 JSON
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isOptionsRequest(request)) {
            return true;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType("application/json;charset=UTF-8");

        try (PrintWriter writer = httpResponse.getWriter()) {
            writer.write(JSON.toJSONString(Result.fail(401, "未登录或登录已过期，请重新登录")));
            writer.flush();
        }
        return false;
    }

    private boolean isOptionsRequest(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return "OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod());
        }
        return false;
    }
}
