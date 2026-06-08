package com.example.testservice.controller.login;

import com.example.testapi.service.UserService;
import com.example.testapi.vo.MpUserVO;
import com.example.testcommon.commom.eventListener.TwoEvent;
import com.example.testcommon.commom.utils.JwtUtil;
import com.example.testcommon.entity.Result;
import com.example.testservice.annotation.LogAnnotation;
import com.example.testservice.config.shiro.JwtToken;
import com.example.testservice.controller.BaseController;
import com.example.testservice.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器（Redis 分布式 Session 版）
 * <p>
 * 认证方式：
 * <ul>
 *   <li><b>Session 登录（推荐）</b>：POST /user/login?phone=xxx → 创建 Session 存入 Redis → 返回 Cookie</li>
 *   <li><b>JWT 登录</b>：GET /user/login/{id} → 返回 JWT Token</li>
 * </ul>
 * <p>
 * 分布式 Session 流程：
 * <pre>
 * 客户端 → POST /user/login?phone=xxx
 *       → Shiro 认证 → RedisSessionDAO 存 Session 到 Redis
 *       → Set-Cookie: sid={sessionId}
 *       → 后续请求携带 Cookie → 从 Redis 读取 Session → 认证通过
 * </pre>
 * </p>
 *
 * @author chenkangwen
 * @date 2024-12-18
 */
@Slf4j
@RequestMapping(value = "/user")
@RestController
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    // ==================== Session 登录（Redis 分布式 Session） ====================

    /**
     * Session 登录接口（推荐）
     * <p>
     * 通过手机号登录，认证成功后 Shiro 自动：
     * <ol>
     *   <li>创建 Session</li>
     *   <li>通过 RedisSessionDAO 将 Session 存入 Redis</li>
     *   <li>返回 Set-Cookie: sid={sessionId}</li>
     * </ol>
     * 客户端后续请求自动携带 Cookie，任意实例均可从 Redis 读取 Session。
     * </p>
     */
    @PostMapping("/login")
    public Result sessionLogin(@RequestParam("phone") String phone) {
        log.info("Session 登录: phone={}", phone);

        // 1. 预检查用户是否存在
        MpUserVO user = userService.getVOByPhone(phone);
        if (user == null) {
            return Result.fail(404, "用户不存在");
        }

        // 2. 执行 Shiro Session 登录
        //    UsernamePasswordToken 的 username=phone, password 可用于验证码
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(phone, "");
            // 开启 RememberMe（可选）
            // token.setRememberMe(true);
            subject.login(token);

            // 3. 获取 Session 信息
            Session session = subject.getSession();
            Serializable sessionId = session.getId();

            // 4. 将用户信息存入 Session（可选，Principal 已包含用户对象）
            session.setAttribute("userId", user.getId());
            session.setAttribute("phone", user.getPhone());

            log.info("Session 登录成功: phone={}, sessionId={}", phone, sessionId);

            // 5. 返回结果（Cookie 由 Shiro 自动设置）
            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", sessionId);
            result.put("userId", user.getId());
            result.put("phone", user.getPhone());
            result.put("authenticated", true);
            return Result.success(result);

        } catch (AuthenticationException e) {
            log.error("Session 登录失败: phone={}, error={}", phone, e.getMessage());
            return Result.fail(401, "登录失败: " + e.getMessage());
        }
    }

    // ==================== JWT 登录（兼容方案） ====================

    /**
     * JWT 登录接口（通过用户 ID 获取 JWT）
     * <p>
     * 适用于移动端或不支持 Cookie 的客户端。
     * 返回的 JWT 通过 Authorization: Bearer <token> 携带。
     * </p>
     */
    @LogAnnotation
    @GetMapping("/login/{id}")
    public Result jwtLogin(@PathVariable("id") Long id) {
        log.info("JWT 登录请求: id={}", id);

        MpUserVO user = userService.getVOById(id);
        if (user == null) {
            return Result.fail(404, "用户不存在");
        }

        // 生成 JWT
        String jwt = JwtUtil.generateToken(String.valueOf(user.getId()), "user");

        // 执行 JWT 认证（同时创建 Session，兼容两种方式）
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new JwtToken(jwt));
        } catch (AuthenticationException e) {
            log.error("JWT 认证失败", e);
            return Result.fail(401, "认证失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("user", user);
        result.put("expiresIn", JwtUtil.getExpirationTime() / 1000);
        return Result.success(result);
    }

    /**
     * 手机号 + JWT 登录（移动端常用）
     */
    @PostMapping("/login/token")
    public Result tokenLogin(@RequestParam("phone") String phone) {
        log.info("Token 登录: phone={}", phone);

        MpUserVO user = userService.getVOByPhone(phone);
        if (user == null) {
            return Result.fail(404, "用户不存在");
        }

        // 生成 JWT
        String role = (user.getId() != null && user.getId() == 1L) ? "admin" : "user";
        String jwt = JwtUtil.generateToken(String.valueOf(user.getId()), role);

        // 同时创建 Shiro Session（可选）
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new JwtToken(jwt));
        } catch (AuthenticationException ignored) {
            // JWT 模式下 Session 创建失败不影响返回 Token
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("userId", user.getId());
        result.put("phone", user.getPhone());
        result.put("expiresIn", JwtUtil.getExpirationTime() / 1000);
        return Result.success(result);
    }

    // ==================== 登出 ====================

    /**
     * 登出
     * <p>
     * Shiro 自动调用 RedisSessionDAO.delete() 从 Redis 删除 Session，
     * 并清除客户端 Cookie。
     * </p>
     */
    @GetMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            Serializable sessionId = subject.getSession().getId();
            log.info("用户登出: sessionId={}", sessionId);
            subject.logout();
        }
        return Result.success("已登出");
    }

    // ==================== 用户信息 ====================

    /**
     * 获取当前登录用户信息
     * <p>
     * 支持 Session 和 JWT 两种认证方式。
     * Session 模式下 Principal 是 MpUserVO 对象，可直接获取用户信息。
     * </p>
     */
    @GetMapping("/info")
    public Result info() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return Result.fail(401, "未登录");
        }

        Object principal = subject.getPrincipal();
        Map<String, Object> info = new HashMap<>();

        if (principal instanceof MpUserVO) {
            // Session 模式：Principal 是用户对象
            MpUserVO user = (MpUserVO) principal;
            info.put("userId", user.getId());
            info.put("phone", user.getPhone());
            info.put("authMode", "session");

            // 获取 Session 元信息
            Session session = subject.getSession();
            info.put("sessionId", session.getId());
            info.put("sessionStart", session.getStartTimestamp());
            info.put("sessionLastAccess", session.getLastAccessTime());

        } else if (principal instanceof String) {
            // JWT 模式：Principal 是 JWT 字符串
            String token = (String) principal;
            try {
                io.jsonwebtoken.Claims claims = JwtUtil.parseToken(token);
                info.put("userId", claims.get("userId"));
                info.put("role", claims.get("role"));
                info.put("issuedAt", claims.getIssuedAt());
                info.put("expiration", claims.getExpiration());
                info.put("authMode", "jwt");
            } catch (Exception e) {
                return Result.fail(401, "Token 无效");
            }
        }

        info.put("authenticated", subject.isAuthenticated());
        return Result.success(info);
    }

    /**
     * 在线用户数（需要 admin 角色）
     */
    @GetMapping("/sessions/count")
    @RequiresRoles("admin")
    public Result activeSessionsCount() {
        Session session = SecurityUtils.getSubject().getSession();
        // RedisSessionDAO 可以获取活跃 Session 数量
        // 此处通过当前 Session 演示分布式能力
        Map<String, Object> result = new HashMap<>();
        result.put("currentSessionId", session.getId());
        result.put("host", session.getHost());
        result.put("startTimestamp", session.getStartTimestamp());
        return Result.success(result);
    }

    // ==================== 权限示例 ====================

    /**
     * 管理员接口示例（需要 admin 角色）
     */
    @GetMapping("/admin")
    @RequiresRoles("admin")
    public Result adminOnly() {
        return Result.success("欢迎，管理员！");
    }

    /**
     * 需要 user:write 权限的接口示例
     */
    @PostMapping("/update")
    @RequiresPermissions("user:write")
    public Result updateUser() {
        return Result.success("更新成功");
    }

    // ==================== 事件测试 ====================

    /**
     * 事件发布测试接口
     */
    @PostMapping("/login/event")
    public void event() {
        TwoEvent event1 = new TwoEvent();
        event1.setName("twoEvent");
        SpringContextUtils.getApplicationContext().publishEvent(event1);
    }
}
