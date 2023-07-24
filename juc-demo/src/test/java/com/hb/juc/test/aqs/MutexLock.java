package com.hb.juc.test.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MutexLock {
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 在加锁的时候,尝试获取许可证,这边使用cas来获取,期望0,更新为1,成功后将拥有的锁指定为该线程
         *
         * @param acquires the acquire argument. This value is always the one
         *                 passed to an acquire method, or is the value saved on entry
         *                 to a condition wait.  The value is otherwise uninterpreted
         *                 and can represent anything you like.
         * @return
         */
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 在释放锁的时候,
         * @param releases the release argument. This value is always the one
         *        passed to a release method, or the current state value upon
         *        entry to a condition wait.  The value is otherwise
         *        uninterpreted and can represent anything you like.
         * @return
         */
        protected boolean tryRelease(int releases) {
            if (getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }

    private final Sync sync = new Sync();

    /**
     * 加锁
     */
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 解锁
     */
    public void unlock() {
        sync.release(1);
    }
}
