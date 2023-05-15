package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/14
 */
@Slf4j
public class AtomicStampedDemo {

    // ABA问题
    @Test
    public void test() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(100);

        new Thread(() -> {
            // ABA问题，先修改了值，然后又改回去了，但是导致值一样
            atomicInteger.compareAndSet(100, 101);
            atomicInteger.compareAndSet(101, 100);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t1").start();


        new Thread(() -> {
            // 而其他线程执行的时候，理论上不能进行操作了，但是还是修改成功了，这个就是ABA问题
            boolean andSet = atomicInteger.compareAndSet(100, 200);
            log.info("修改成功:{}, atomicInteger:{}", andSet, atomicInteger);
        }, "t2").start();
    }

    // 使用AtomicStampedReference 解决ABA问题
    @Test
    public void test2() throws InterruptedException {
        AtomicStampedReference<Integer> integerAtomic = new AtomicStampedReference<>(100, 1);

        new Thread(() -> {
            log.info("首次版本号：{}", integerAtomic.getStamp());
            // 保证获取到的是同一个版本号
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int stamp = integerAtomic.getStamp();
            integerAtomic.compareAndSet(100, 101, stamp, stamp + 1);

            int stamp2 = integerAtomic.getStamp();
            integerAtomic.compareAndSet(101, 100, stamp2, stamp2 + 1);


            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t1").start();


        new Thread(() -> {
            log.info("首次版本号：{}", integerAtomic.getStamp());

            // 保证获取到的是同一个版本号
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int stamp = integerAtomic.getStamp();
            boolean flag = integerAtomic.compareAndSet(100, 106, stamp, stamp + 1);

            log.info("修改成功:{}, integerAtomic:{}, stamp:{}", flag, integerAtomic.getReference(), integerAtomic.getStamp());
        }, "t2").start();

        TimeUnit.SECONDS.sleep(5);
    }

}
