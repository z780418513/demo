package com.hb.juc.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/15
 */
@Slf4j
public class AtomicIntegerFieldUpdaterDemo {
    @Test
    public void total() throws InterruptedException {
        MyCount2 myCount = new MyCount2();

        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000; j++) {
                    myCount.addPlus(myCount);
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
        log.info("50 *1000 的结果是:{}",myCount.getCount());

    }
}

@Data
class MyCount2{
    // 更新的字段必须是volatile修饰
    private volatile int count;
    private String otherFiled1;
    private String otherFiled2;
    private String otherFiled3;

    private AtomicIntegerFieldUpdater<MyCount2> fieldUpdater =
            AtomicIntegerFieldUpdater.newUpdater(MyCount2.class,"count");

    public void addPlus(MyCount2 myCount2){
        fieldUpdater.incrementAndGet(myCount2);
    }
}
