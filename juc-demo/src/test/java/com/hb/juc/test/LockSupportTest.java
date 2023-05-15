package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/13
 */
@Slf4j
public class LockSupportTest {
    @Test
    public void synchronizedNotify() throws InterruptedException {
        Object object = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (object) {
                try {
                    log.info("object waiting ");
                    object.wait();
                    log.info("object go on ");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1");
        t1.start();

        new Thread(() -> {
            synchronized (object) {
                log.info("object notify .....");
                object.notify();
                log.info("object do other");
            }
        }, "t2").start();

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void lockNotify() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                log.info("object waiting ");
                condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            log.info("object go on ");

        }, "t1");
        t1.start();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("object notify .....");
                condition.signal();
            } finally {
                lock.unlock();

                log.info("object do other");
            }
        }, "t2").start();

        TimeUnit.SECONDS.sleep(5);
    }


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
