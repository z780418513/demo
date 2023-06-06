package com.hb.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redisson工具类
 *
 * @author zhaochengshui
 * @description
 * @date 2023/6/1
 */
@Slf4j
@Component
public class RedissonUtil {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param lockKey   加锁的key
     * @param waitTime  等待时间
     * @param leaseTime 持有锁时间
     * @param unit      时间单位
     * @param t         搭配consumer使用，用于参数的传递
     * @param consumer  自定义消费者
     * @param <T>
     * @author zhaochengshui
     */
    public <T> void doLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, T t, Consumer<T> consumer) {
        log.info("Redisson Lock Start, LockKey: {}", lockKey);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = lock.tryLock(waitTime, leaseTime, unit);
            // 加锁失败
            if (!isLock) {
                log.warn("Lock fail LockKey: {}", lockKey);
                return;
            }
            consumer.accept(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 只能解锁自己的
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            log.info("Redisson Lock end, LockKey: {}", lockKey);
        }
    }

    /**
     * 加锁
     *
     * @param lockKey   加锁的key
     * @param waitTime  等待时间
     * @param leaseTime 持有锁时间
     * @param unit      时间单位
     * @param runnable  runnable函数
     * @author zhaochengshui
     */
    public void doLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, Runnable runnable) {
        log.info("Redisson Lock Start, LockKey: {}", lockKey);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = lock.tryLock(waitTime, leaseTime, unit);
            // 加锁失败
            if (!isLock) {
                log.warn("Lock fail LockKey: {}", lockKey);
                return;
            }
            runnable.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 只能解锁自己的
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            log.info("Redisson Lock end, LockKey: {}", lockKey);
        }
    }


}
