package com.hb.juc.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/15
 */
@Slf4j
public class AtomicIntegerDemo {
    @Test
    public void total() throws InterruptedException {
        MyCount myCount = new MyCount();
        CountDownLatch countDownLatch = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000; j++) {
                    myCount.addPlus();
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        log.info("50 *1000 的结果是:{}",myCount.getCount());

    }
}


@Data
class MyCount{
    private AtomicInteger count = new AtomicInteger(0);
    // 使用原子类
    public void addPlus(){
        count.getAndIncrement();
    }
}
