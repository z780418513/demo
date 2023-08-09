/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.hb.redisson;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author lgh
 */
@Aspect
@Component
public class RedisLockAspect {

    @Resource
    private RedissonClient redissonClient;

    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        Object result = null;
        Object target = joinPoint.getTarget();
        Method method = getMethod(joinPoint);
        Object[] args = joinPoint.getArgs();
        String lockKey = SpelUtils.parse(target, redisLock.lockName(), method, args );

        RLock lock = redissonClient.getLock(REDISSON_LOCK_PREFIX + lockKey);
        try {
            boolean isLock = lock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), redisLock.timeUnit());
            if (isLock) {
                //执行方法
                result = joinPoint.proceed();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 只能解锁自己的
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return result;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }


}
