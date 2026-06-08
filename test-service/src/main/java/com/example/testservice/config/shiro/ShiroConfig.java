package com.example.testservice.config.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache Shiro 配置类（Redis 分布式 Session 版）
 * <p>
 * 架构：
 * <pre>
 * 客户端 → [Cookie: sid] → ShiroRestFilter
 *        → SecurityManager → RedisSessionDAO → Redis
 *        → 多实例共享 Session
 * </pre>
 * <p>
 * 支持双认证策略：
 * <ul>
 *   <li>Session Cookie（主）：登录后携带 sid Cookie，Session 存储于 Redis</li>
 *   <li>JWT Bearer Token（辅）：Authorization: Bearer &lt;token&gt;</li>
 * </ul>
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Configuration
public class ShiroConfig {

    /** Session 超时时间（毫秒），默认 30 分钟 */
    @Value("${shiro.session.globalSessionTimeout:1800000}")
    private long sessionTimeout;

    /** Session 验证间隔（毫秒），默认 5 分钟 */
    @Value("${shiro.session.validationInterval:300000}")
    private long sessionValidationInterval;

    /** Cookie 名称 */
    @Value("${shiro.session.sessionIdCookie.name:sid}")
    private String cookieName;

    /** Cookie 路径 */
    @Value("${shiro.session.sessionIdCookie.path:/}")
    private String cookiePath;

    /** Cookie HttpOnly */
    @Value("${shiro.session.sessionIdCookie.httpOnly:true}")
    private boolean cookieHttpOnly;

    /** Cookie 有效期（秒），-1 为浏览器关闭失效 */
    @Value("${shiro.session.sessionIdCookie.maxAge:-1}")
    private int cookieMaxAge;

    // ==================== Shiro 过滤器链 ====================

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 注册自定义 REST 认证过滤器（支持 Session Cookie + JWT 双认证）
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("restAuthc", new ShiroRestFilter());
        factoryBean.setFilters(filters);

        // 过滤器链定义（有序，从上到下匹配）
        Map<String, String> filterChain = new LinkedHashMap<>();
        // Druid 监控 — 匿名访问
        filterChain.put("/druid/**", "anon");
        // 登录接口 — 匿名访问
        filterChain.put("/user/login/**", "anon");
        filterChain.put("/user/logout", "anon");
        // Swagger / API 文档（如有）— 匿名访问
        filterChain.put("/swagger-ui/**", "anon");
        filterChain.put("/v2/api-docs/**", "anon");
        filterChain.put("/swagger-resources/**", "anon");
        // 其他所有请求 — REST 认证（Session / JWT）
        filterChain.put("/**", "restAuthc");

        factoryBean.setFilterChainDefinitionMap(filterChain);
        return factoryBean;
    }

    // ==================== 安全管理器 ====================

    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm,
                                                     SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);

        // 使用 Redis 支持的 SessionManager（分布式 Session 核心）
        securityManager.setSessionManager(sessionManager);

        // RememberMe 管理器（可选）
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    // ==================== Session 管理（Redis 分布式 Session） ====================

    @Bean
    public SessionDAO sessionDAO() {
        return new RedisSessionDAO();
    }

    @Bean
    public DefaultWebSessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // 设置 Redis Session DAO
        sessionManager.setSessionDAO(sessionDAO);

        // 禁用 Session 定时校验器（多实例时避免每个实例都执行校验）
        sessionManager.setSessionValidationSchedulerEnabled(false);

        // Session 超时时间
        sessionManager.setGlobalSessionTimeout(sessionTimeout);

        // 是否在请求之间删除无效 Session
        sessionManager.setDeleteInvalidSessions(true);

        // Session ID Cookie 配置
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);

        // 将 Session ID 写入 URL（禁用，只用 Cookie）
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }

    /**
     * Session ID Cookie
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie(cookieName);
        cookie.setPath(cookiePath);
        cookie.setHttpOnly(cookieHttpOnly);
        cookie.setMaxAge(cookieMaxAge);
        // 生产环境建议设置为 true
        // cookie.setSecure(true);
        // 防止 CSRF，生产环境建议设置为 Strict 或 Lax
        // cookie.setSameSite(null);
        return cookie;
    }

    // ==================== RememberMe Cookie ====================

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(604800); // 7 天
        return cookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        // 加密密钥（生产环境请更换）
        manager.setCipherKey(org.apache.shiro.codec.Base64.decode(
                "4AvVhmFLUs0KTA3Kprsdag=="));
        return manager;
    }

    // ==================== Realm ====================

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    // ==================== Shiro 注解支持 ====================

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
