package com.hb.juc.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author zhaochengshui
 * @description 使用AtomicReferenceFieldUpdater初始化一个自定义线程
 * @date 2023/5/15
 */
public class AtomicReferenceFieldUpdaterDemo {
    @Test
    public void test() throws InterruptedException {
        MyThread myThread = new MyThread();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                myThread.init(myThread);
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
    }
}

@Data
@Slf4j
class MyThread {
    private volatile Boolean isInitEd = false;
    private String name;
    private Integer size;

    private AtomicReferenceFieldUpdater<MyThread, Boolean> fieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(MyThread.class, Boolean.class, "isInitEd");

    public void init(MyThread myThread) {
        if (fieldUpdater.compareAndSet(myThread, false, true)) {
            log.info("该线程初始化成功");
        }else {
            log.info("初始化失败");
        }
    }

}
