package com.hb.juc.test;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/8
 */
public class MySemaphore {
    private final Sync sync;

    public MySemaphore(int permits) {
        this.sync = new MySync(permits);
    }

    public void acquire(int permits) throws InterruptedException {
        sync.acquireSharedInterruptibly(permits);
    }

    public void release() {
        sync.releaseShared(1);
    }

    abstract static class Sync extends AbstractQueuedSynchronizer {
        Sync(int permits) {
            setState(permits);
        }

        protected int tryAcquireShared(int acquires) {
            int acquireShared = nonfairTryAcquireShared(acquires);
            return acquireShared;
        }

        final int nonfairTryAcquireShared(int acquires) {
            for (; ; ) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                        compareAndSetState(available, remaining))
                    return remaining;
            }
        }



        protected final boolean tryReleaseShared(int releases) {
            for (; ; ) {
                int current = getState();
                int next = current + releases;
                if (next < current) // overflow
                    throw new Error("Maximum permit count exceeded");
                if (compareAndSetState(current, next))
                    return true;
            }
        }


    }

    class MySync extends Sync {
        MySync(int permits) {
            super(permits);
        }


    }


}

