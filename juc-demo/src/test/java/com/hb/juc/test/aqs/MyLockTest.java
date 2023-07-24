package com.hb.juc.test.aqs;

public class MyLockTest {

    static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        MyLock lock = new MyLock();

        Runnable r = () -> {
            try {
                lock.lock();
                for (int i = 0; i < 1000000; i++) {
                    count++;
                }
            } finally {
                lock.unlock();
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}

