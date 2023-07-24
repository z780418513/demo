package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/12
 */
@Slf4j
public class CompletableFuture_First {
    ReadWriteLock lock;
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(()->{
            log.info("future task start");
            TimeUnit.SECONDS.sleep(3);
            return 1+1;
        });

        new Thread(futureTask).start();
        // futureTask的缺点，直接使用会阻塞线程
        System.out.println("futureTask.get() = " + futureTask.get());
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(()->{
            log.info("future task start");
            TimeUnit.SECONDS.sleep(3);
            return 1+1;
        });

        new Thread(futureTask).start();
        // 使用while来循环执行，这样虽然能解决阻塞问题，但是会造成cpu性能浪费
        while (true){
            if (futureTask.isDone()){
                System.out.println("futureTask.get() = " + futureTask.get());
                break;
            }

        }
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(()->{
            log.info("CompletableFuture task start");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return 1+1;
        }).whenComplete((value, exception)->{
            log.info("CompletableFuture task value:{}",value);
        });

        log.info("do else thing ....");
        TimeUnit.SECONDS.sleep(5);
    }
}
