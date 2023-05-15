package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自定义自旋锁
 * @author zhaochengshui
 * @description  t1线程-加锁-解锁 --> t2线程-加锁-解锁
 * @date 2023/5/14
 */
@Slf4j
public class SpinLockDemo {
    AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();

    public void lock() {
        // 如果没有自旋成功，重复执行
        // 期望值为null，当前线程才能加锁成功
        while (!threadAtomicReference.compareAndSet(null, Thread.currentThread())) {

        }
        log.info("自旋锁加锁成功");
    }

    public void unLock() {
        // 期望值是当前线程，才能解锁成功
        boolean flag = threadAtomicReference.compareAndSet(Thread.currentThread(), null);
        if (flag) {
            log.info("自旋锁解锁成功");
        }
    }

    public static void main(String[] args) {
        SpinLockDemo spinLock = new SpinLockDemo();
        new Thread(() -> {
            spinLock.lock();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            spinLock.unLock();
        }, "t1").start();

        new Thread(() -> {
            spinLock.lock();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            spinLock.unLock();
        }, "t2").start();

    }
}
