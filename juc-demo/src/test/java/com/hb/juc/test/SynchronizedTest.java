package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/12
 */
@Slf4j
public class SynchronizedTest {

    public static synchronized void doLock() {
        log.info("do static lock");
    }

    public void lock2() {
        synchronized (this) {
            log.info("do lock2");
        }
    }

    public synchronized void lock() {
        log.info("do lock");
    }

    public static void main(String[] args) {

    }
}
