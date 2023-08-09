package com.hb.redisson;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行分布式锁
 *
 * @author lanhai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * redis锁 名字
     */
    String lockName() default "";

    /**
     * 等待时间
     *
     * @return 默认5
     */
    long waitTime() default 5L;

    /**
     * 持有锁时间
     *
     * @return 默认5
     */
    long leaseTime() default 5L;

    /**
     * 超时时间单位
     *
     * @return 默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
