package com.example.testservice.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis Session DAO
 * <p>
 * 将 Shiro Session 持久化到 Redis，实现分布式 Session 共享。
 * <ul>
 *   <li>多应用实例共享同一 Redis，天然支持水平扩展</li>
 *   <li>Session 过期时间与 Redis key TTL 一致，自动清理</li>
 *   <li>每次访问自动续期</li>
 * </ul>
 * <p>
 * Redis Key 格式: {@code shiro:session:{sessionId}}
 * </p>
 *
 * @author chenkangwen
 * @date 2026-06-08
 */
@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO {

    /** Redis 中 Session 的 key 前缀 */
    @Value("${shiro.redis.session.key-prefix:shiro:session:}")
    private String keyPrefix;

    /** Session 超时时间（毫秒），默认 30 分钟 */
    @Value("${shiro.session.globalSessionTimeout:1800000}")
    private long sessionTimeout;

    /**
     * 使用默认的 RedisTemplate（JDK 序列化），
     * Shiro Session 已实现 Serializable，可直接序列化。
     */
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 创建 Session 并持久化到 Redis
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        log.debug("Session 已创建: id={}", sessionId);
        return sessionId;
    }

    /**
     * 从 Redis 读取 Session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        String key = buildKey(sessionId);
        try {
            Session session = (Session) redisTemplate.opsForValue().get(key);
            if (session != null) {
                log.debug("Session 已读取: id={}", sessionId);
                // 续期：更新 TTL
                redisTemplate.expire(key, sessionTimeout, TimeUnit.MILLISECONDS);
            }
            return session;
        } catch (Exception e) {
            log.error("读取 Session 异常: id={}", sessionId, e);
            return null;
        }
    }

    /**
     * 更新 Session 到 Redis
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        try {
            saveSession(session);
            log.debug("Session 已更新: id={}", session.getId());
        } catch (Exception e) {
            log.error("更新 Session 异常: id={}", session.getId(), e);
            throw new UnknownSessionException(e);
        }
    }

    /**
     * 从 Redis 删除 Session
     */
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        String key = buildKey(session.getId());
        try {
            redisTemplate.delete(key);
            log.debug("Session 已删除: id={}", session.getId());
        } catch (Exception e) {
            log.error("删除 Session 异常: id={}", session.getId(), e);
        }
    }

    /**
     * 获取所有活跃的 Session
     * <p>
     * 遍历 Redis 中所有 shiro:session:* 的 key。
     * 注意：大量 Session 时可能有性能影响，生产环境建议限制返回数量。
     * </p>
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        try {
            Set<Object> keys = redisTemplate.keys(keyPrefix + "*");
            if (keys != null && !keys.isEmpty()) {
                for (Object keyObj : keys) {
                    String key = (String) keyObj;
                    Session session = (Session) redisTemplate.opsForValue().get(key);
                    if (session != null) {
                        sessions.add(session);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取活跃 Session 列表异常", e);
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(sessions);
    }

    /**
     * 保存 Session 到 Redis，设置 TTL
     */
    private void saveSession(Session session) {
        String key = buildKey(session.getId());
        redisTemplate.opsForValue().set(key, session, sessionTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 构建 Redis Key
     */
    private String buildKey(Serializable sessionId) {
        return keyPrefix + sessionId.toString();
    }
}
