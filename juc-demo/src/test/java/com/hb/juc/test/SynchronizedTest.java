package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/12
 */
public class SynchronizedTest {
    // 静态方法加锁 (类锁)
    public static synchronized void doLock() {
        System.out.println("do static lock");
    }

    // 代码块加锁 (对象锁)
    public void lock2() {
        synchronized (this) {
            System.out.println("do lock2");
        }
    }

    // 实例方法加锁 (对象锁)
    public synchronized void lock() {
        System.out.println("do lock");
    }

    public static void main(String[] args) {

    }
}
