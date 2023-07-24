package com.hb.juc.test.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

    public class MyLock {

    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public void unlock() {
        sync.release(0);
    }

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            setState(arg);
            return true;
        }
    }
}

