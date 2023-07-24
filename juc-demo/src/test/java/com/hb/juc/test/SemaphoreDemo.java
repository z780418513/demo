package com.hb.juc.test;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        // 设置许可数量为3
        MySemaphore semaphore = new MySemaphore(10);
        for (int i = 1; i <= 6; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    // 抢占车位
                    semaphore.acquire(1);
                    System.out.println(finalI + "抢到了车位---");
                    // 休眠5以内的随机数
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放车位
                    semaphore.release();
                    System.out.println(finalI + "离开了车位--------");
                }

            },String.valueOf(i)).start();
        }

    }
}
