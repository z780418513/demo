package com.hb.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author zhaochengshui
 * @description CompletableFuture 常用方法
 * @date 2023/5/11
 */
@Slf4j
public class CompletableFutureTest {
    // 自定义线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    // 获取结果和触发计算
    // 对计算结果进行处理
    // 对计算结果进行消费
    // 对计算速度进行选用
    // 对计算结果进行合并

    private CompletableFuture<String> mockFuture() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "get data";
        }, threadPool);
    }

    /**
     * 获取返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void get() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = mockFuture();
        // get和join一样 会阻塞线程
//        log.info("result:{}", future.get());
        log.info("result:{}", future.join());
    }

    /**
     * 限时返回
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void getTime() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future = mockFuture();
        // 超时直接抛异常
        log.info("result:{}", future.get(1, TimeUnit.SECONDS));
    }

    /**
     * 立刻返回
     */
    @Test
    public void getNow() {
        CompletableFuture<String> future = mockFuture();
        // 立马获取结果，任务没有执行结束的话，返回默认值
        log.info("result:{}", future.getNow("默认值"));
    }

    /**
     * 完成任务
     */
    @Test
    public void complete() {
        CompletableFuture<String> future = mockFuture();
        // complete有两个功能
        // 1. 获取任务是否结束布尔值
        // 2. 如果任务未完成结束任务，设置任务结果为 默认值
        log.info("complete:{}", future.complete("默认值"));
        log.info("result:{}", future.join());
    }


    /**
     * 串行执行任务,中途遇到异常直接结束任务
     */
    @Test
    public void thenApply() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("步骤一");
            return 1;
        }, threadPool).thenApply((value) -> {
            log.info("步骤二");
//            int i = 1 / 0;  // 会直接结束任务
            return value + 2;
        }).thenApply((value) -> {
            log.info("步骤三");
            return value + 3;
        });
        log.info("result:{}", future.join());
    }

    /**
     * handle 串行执行任务,中途遇到异常,也会继续执行
     * handle 的入参是上一步的value 和 exception
     */
    @Test
    public void handle() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("步骤一");
            return 1;
        }, threadPool).handle((value, e) -> {
            log.info("步骤二");
//            int i = 1 / 0;  // 会直接结束任务
            if (value == 1){
                throw new RuntimeException();
            }

            return value + 2;
        }).handle((value, e) -> {
            if (e != null) {
                value = 0;
            }
            log.info("步骤三");
            return value + 3;
        });
        log.info("result:{}", future.join());
    }

    /**
     * thenAccept 消费型，处理上一步骤的结果,没有返回值
     * thenAccept 如果上一任务是也是消费型，那她的value = null
     */
    @Test
    public void thenAccept() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            log.info("步骤一");
            return 1;
        }, threadPool).thenAcceptAsync((value) -> {
            log.info("步骤二");
            log.info("value:{}", value);
        }).thenAcceptAsync((value) -> {
            log.info("步骤三");
            log.info("value:{}", value);
        }, threadPool);
        log.info("result:{}", future.join());
    }


    /**
     * applyToEither 两个任务执行,哪个最新执行结束，就用哪个返回值
     */
    @Test
    public void applyToEither() {
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "A TASK";
        });

        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "B TASK";
        });
        // applyToEither谁快用谁
        CompletableFuture<String> future = futureA.applyToEither(futureB, (value) -> value);
        log.info("{}, is fast", future.join());
    }


    /**
     * thenCombine 对两个结果进行合并
     */
    @Test
    public void thenCombine() {
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> 11);
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> 22);
        // thenCombine 对两个结果进行合并
        CompletableFuture<Integer> future = futureA.thenCombine(futureB, Integer::sum);
        log.info("result:{}", future.join());
    }

}
