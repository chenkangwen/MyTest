package com.example.testservice.config.shiro;

import com.alibaba.fastjson.JSON;
import com.example.testcommon.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT Shiro 过滤器
 * <p>
 * 从 HTTP 请求头 {@code Authorization: Bearer <token>} 中提取 JWT，
 * 构造 JwtToken 并执行 Shiro 登录认证。
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Slf4j
public class JwtShiroFilter extends BasicHttpAuthenticationFilter {

    /** JWT 请求头前缀 */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 判断是否需要登录认证
     * <p>
     * 所有非 OPTIONS 请求都需要经过 JWT 认证。
     * OPTIONS（CORS 预检）请求直接放行。
     * </p>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // OPTIONS 预检请求直接放行
        if (isOptionsRequest(request)) {
            return true;
        }
        // 尝试执行登录认证
        return executeLogin(request, response);
    }

    /**
     * 认证失败时的处理
     * <p>
     * 跨域预检请求直接放行；其他请求返回 401 及 JSON 错误信息。
     * </p>
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 判断是否为 OPTIONS 请求
        if (isOptionsRequest(request)) {
            httpResponse.setStatus(HttpStatus.OK.value());
            return true;
        }

        log.warn("JWT 认证失败: URI={}", ((HttpServletRequest) request).getRequestURI());

        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = httpResponse.getWriter()) {
            writer.write(JSON.toJSONString(Result.fail(401, "未登录或 token 已过期")));
            writer.flush();
        }
        return false;
    }

    /**
     * 创建 Shiro AuthenticationToken
     * <p>
     * 从 Authorization 请求头提取 Bearer Token，封装为 JwtToken。
     * 如果没有携带 Token 则返回空，后续会触发 onAccessDenied。
     * </p>
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length()).trim();
            if (!jwt.isEmpty()) {
                return new JwtToken(jwt);
            }
        }
        // 没有 token 时返回 null，由 onAccessDenied 处理
        return null;
    }

    /**
     * 执行登录
     * <p>
     * 重写父类方法，处理 token 为空和认证异常的情况。
     * </p>
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            // 没有携带 token，触发 onAccessDenied
            return false;
        }

        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return true;
        } catch (AuthenticationException e) {
            log.error("Shiro 登录认证异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断是否为 OPTIONS 请求（CORS 预检）
     */
    private boolean isOptionsRequest(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return "OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod());
        }
        return false;
    }

    /**
     * 登录成功后的回调，可用于记录日志
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) {
        log.debug("JWT 认证成功: {}", token);
        return true;
    }

    /**
     * 登录失败后的回调
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        log.warn("JWT 登录失败: token={}, error={}", token, e.getMessage());
        return super.onLoginFailure(token, e, request, response);
    }
}
