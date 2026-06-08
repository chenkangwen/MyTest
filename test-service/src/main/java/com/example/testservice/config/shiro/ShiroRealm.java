package com.example.testservice.config.shiro;

import com.example.testapi.service.UserService;
import com.example.testapi.vo.MpUserVO;
import com.example.testcommon.commom.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Shiro 自定义 Realm
 * <p>
 * 支持双认证模式：
 * <ul>
 *   <li>UsernamePasswordToken —— Session 登录（手机号 + 验证码）</li>
 *   <li>JwtToken —— JWT Bearer Token 登录</li>
 * </ul>
 * <p>
 * 认证通过后，Principal 存储 MpUserVO 用户对象，方便业务层获取。
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 支持两种 Token 类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken
                || token instanceof JwtToken;
    }

    // ==================== 授权 ====================

    /**
     * 授权：从 Principal 中获取用户信息，设置角色和权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object principal = principals.getPrimaryPrincipal();
        log.debug("Shiro 授权: principal={}", principal);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        if (principal instanceof MpUserVO) {
            // Session 模式：Principal 是 MpUserVO 对象
            MpUserVO user = (MpUserVO) principal;
            Set<String> roles = getRolesForUser(user);
            Set<String> permissions = getPermissionsForUser(user);
            info.setRoles(roles);
            info.setStringPermissions(permissions);
            log.debug("Session 模式 — 角色: {}, 权限: {}", roles, permissions);

        } else if (principal instanceof String) {
            // JWT 模式：Principal 是 JWT 字符串
            String token = (String) principal;
            try {
                Claims claims = JwtUtil.parseToken(token);
                String role = (String) claims.get("role");
                Set<String> roles = new HashSet<>();
                if (role != null) {
                    roles.add(role);
                }
                info.setRoles(roles);

                Set<String> permissions = new HashSet<>();
                if ("admin".equals(role)) {
                    permissions.add("*");
                } else {
                    permissions.add("user:view");
                }
                info.setStringPermissions(permissions);
                log.debug("JWT 模式 — 角色: {}, 权限: {}", roles, permissions);
            } catch (Exception e) {
                log.error("JWT 授权解析失败", e);
            }
        }

        return info;
    }

    // ==================== 认证 ====================

    /**
     * 认证：根据不同的 Token 类型执行不同的认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken)
            throws AuthenticationException {

        if (authToken instanceof JwtToken) {
            return doJwtAuthentication((JwtToken) authToken);
        } else if (authToken instanceof UsernamePasswordToken) {
            return doSessionAuthentication((UsernamePasswordToken) authToken);
        }

        throw new AuthenticationException("不支持的 Token 类型: " + authToken.getClass());
    }

    /**
     * JWT 认证
     * <p>
     * 解析 JWT → 获取 userId → 查数据库 → 返回用户信息。
     * Principal 存储 JWT 字符串（兼容无状态场景）。
     * </p>
     */
    private AuthenticationInfo doJwtAuthentication(JwtToken jwtToken) {
        String token = jwtToken.getToken();
        log.debug("JWT 认证: token={}", token.substring(0, Math.min(token.length(), 20)));

        try {
            Claims claims = JwtUtil.parseToken(token);
            String userId = (String) claims.get("userId");
            if (userId == null) {
                throw new AuthenticationException("JWT 中缺少 userId");
            }

            MpUserVO user = userService.getVOById(Long.valueOf(userId));
            if (user == null) {
                throw new UnknownAccountException("用户不存在: " + userId);
            }

            return new SimpleAuthenticationInfo(token, token, getName());

        } catch (UnknownAccountException e) {
            throw e;
        } catch (Exception e) {
            log.error("JWT 认证失败", e);
            throw new ExpiredCredentialsException("JWT 已过期或无效", e);
        }
    }

    /**
     * Session 认证（手机号登录）
     * <p>
     * 使用手机号作为用户名，查找用户 → 返回用户信息。
     * Principal 存储 MpUserVO 对象（方便业务层直接使用）。
     * 认证成功后 Shiro 自动创建 Session 并通过 RedisSessionDAO 存入 Redis。
     * </p>
     */
    private AuthenticationInfo doSessionAuthentication(UsernamePasswordToken token) {
        String phone = token.getUsername();
        log.debug("Session 认证: phone={}", phone);

        // 根据手机号查询用户
        MpUserVO user = userService.getVOByPhone(phone);
        if (user == null) {
            throw new UnknownAccountException("用户不存在: " + phone);
        }

        // 密码校验（当前简化：无密码；实际项目应使用验证码或密码）
        // char[] password = token.getPassword();
        // if (!verifyPassword(user, new String(password))) {
        //     throw new IncorrectCredentialsException("密码错误");
        // }

        // Principal 存储用户对象，Credentials 使用 token 中的密码（保证凭证匹配）
        // 当前简化：不校验密码/验证码，使用 token 自带凭证确保匹配通过
        return new SimpleAuthenticationInfo(user, token.getCredentials(), getName());
    }

    // ==================== 角色/权限获取 ====================

    /**
     * 获取用户角色集合
     */
    private Set<String> getRolesForUser(MpUserVO user) {
        Set<String> roles = new HashSet<>();
        // TODO: 根据实际业务从数据库查询用户角色
        roles.add("user");
        // 示例：id=1 的用户为 admin
        if (user.getId() != null && user.getId() == 1L) {
            roles.add("admin");
        }
        return roles;
    }

    /**
     * 获取用户权限集合
     */
    private Set<String> getPermissionsForUser(MpUserVO user) {
        Set<String> permissions = new HashSet<>();
        permissions.add("user:view");
        // 示例：admin 角色拥有所有权限
        if (user.getId() != null && user.getId() == 1L) {
            permissions.add("*");
            permissions.add("user:write");
        }
        return permissions;
    }

    /**
     * 清除缓存（可选实现）
     */
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
        log.debug("已清除授权缓存: {}", principals.getPrimaryPrincipal());
    }
}
