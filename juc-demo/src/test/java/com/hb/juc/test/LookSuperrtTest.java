package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/13
 */
@Slf4j
public class LookSuperrtTest {
    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("拥有许可证,通过");
        }, "t1");
        t1.start();

        new Thread(() -> {
            LockSupport.unpark(t1);
            log.info("发放许可证");
        }, "t2").start();

        TimeUnit.SECONDS.sleep(2);
    }
}
