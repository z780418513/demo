package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author zhaochengshui
 * @description AtomicMarkableReference主要是解决引用是否被修改
 * @date 2023/5/15
 */
@Slf4j
public class AtomicMarkableReferenceDemo {
    private AtomicMarkableReference<Integer> markableReference =
            new AtomicMarkableReference<>(100, false);

    @Test
    public void test() throws InterruptedException {
        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            log.info("默认标记位:{}", marked);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            markableReference.compareAndSet(100, 200, marked, !marked);
        }).start();

        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            log.info("默认标记位:{}", marked);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            boolean flag = markableReference.compareAndSet(100, 2000, marked, !marked);
            log.info("最终结果:{}, 修改标识位: {}, 修改是否成功:{}", markableReference.getReference(), markableReference.isMarked(),flag);
        }).start();


        TimeUnit.SECONDS.sleep(3);
    }
}
