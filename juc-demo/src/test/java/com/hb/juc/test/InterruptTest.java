package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/12
 */
@Slf4j
public class InterruptTest {
    // 通过volatile变量的改变，来实现线程的结束
    private volatile boolean interruptFlag = true;

    @Test
    public void volatileInterrupt() throws InterruptedException {
        new Thread(() -> {
            while (interruptFlag) {
                log.info("thread is running.....");
            }
        }).start();

        TimeUnit.SECONDS.sleep(5);
        new Thread(() -> {
            interruptFlag = false;
            log.info("to interrupt thread ");
        }).start();
    }

    // 通过AtomicBoolean变量的改变，来实现线程的结束
    private AtomicBoolean aBoolean = new AtomicBoolean(true);

    @Test
    public void atomicBooleanInterrupt() throws InterruptedException {
        new Thread(() -> {
            while (aBoolean.get()) {
                log.info("thread is running.....");
            }
        }).start();

        TimeUnit.SECONDS.sleep(5);
        new Thread(() -> {
            aBoolean.set(false);
            log.info("to interrupt thread ");
        }).start();
    }

    // 使用线程的interrupt的中断标志位来实现结束线程
    @Test
    public void ThreadInterrupted() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                log.info("thread is running.....");
                // 如果线程中断标志位为true，就打断
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        });
        t1.start();

        TimeUnit.SECONDS.sleep(5);

        // 将线程中断标志为设置为true
        t1.interrupt();
        log.info("to interrupt thread ");

    }

    @Test
    public void ThreadInterrupted2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            // 如果未被中断，可以继续执行
            while (!Thread.currentThread().isInterrupted()) {
                log.info("thread is running.....");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // 阻塞的线程被中断，会抛异常，被捕获，而这时的中断标志会被改成false
                    // 需要再次调用interrupt,不然在循环情况下，会导致中断失败，造成死循环
                    Thread.currentThread().interrupt();
                }
            }
        });
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        // 将线程中断标志为设置为true
        t1.interrupt();
        log.info("to interrupt thread ");

        TimeUnit.SECONDS.sleep(50);

    }


}
