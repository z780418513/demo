package com.hb.juc.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/15
 */
@Slf4j
public class LongAdderDemo {
    @Test
    public void test() throws InterruptedException {
        long _1w = 10000;
        int threadSize = 500;
        MyCount3 myCount3 = new MyCount3();
        CountDownLatch countDownLatch1 = new CountDownLatch(threadSize);
        CountDownLatch countDownLatch2 = new CountDownLatch(threadSize);
        CountDownLatch countDownLatch3 = new CountDownLatch(threadSize);
        CountDownLatch countDownLatch4 = new CountDownLatch(threadSize);
        long startTime, endTime;

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                for (long l = 1; l <= 100 * _1w; l++) {
                    myCount3.addBySynchronized();
                }
                countDownLatch1.countDown();
            }
            ).start();
        }
        countDownLatch1.await();
        endTime = System.currentTimeMillis();
        log.info("addBySynchronized, cost:{}, result:{}", endTime - startTime, myCount3.getCount());

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                for (long l = 1; l <= 100 * _1w; l++) {
                    myCount3.addByAtomicLong();
                }
                countDownLatch2.countDown();
            }
            ).start();
        }
        countDownLatch2.await();
        endTime = System.currentTimeMillis();
        log.info("addByAtomicLong, cost:{}, result:{}", endTime - startTime, myCount3.getCount2());

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                for (long l = 1; l <= 100 * _1w; l++) {
                    myCount3.addByLongAdder();
                }
                countDownLatch3.countDown();
            }
            ).start();
        }
        countDownLatch3.await();
        endTime = System.currentTimeMillis();
        log.info("addByLongAdder, cost:{}, result:{}", endTime - startTime, myCount3.getLongAdder().longValue());

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                for (long l = 1; l <= 100 * _1w; l++) {
                    myCount3.addByLongAccumulator();
                }
                countDownLatch4.countDown();
            }
            ).start();
        }
        countDownLatch4.await();
        endTime = System.currentTimeMillis();
        log.info("addByLongAccumulator, cost:{}, result:{}", endTime - startTime, myCount3.getLongAccumulator().longValue());


    }

}


@Data
class MyCount3 {
    private long count;

    public synchronized void addBySynchronized() {
        count++;
    }

    private AtomicLong count2 = new AtomicLong();

    public void addByAtomicLong() {
        count2.incrementAndGet();
    }

    private LongAdder longAdder = new LongAdder();

    public void addByLongAdder() {
        longAdder.increment();
    }


    private LongAccumulator longAccumulator = new LongAccumulator(Long::sum, 0);

    public void addByLongAccumulator() {
        longAccumulator.accumulate(1);
    }
}
