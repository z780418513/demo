package com.hb.thread;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/20
 */
public class ThreadLocalHolder<T> {

    private final ThreadLocal<T> dataHolder = new ThreadLocal<>();

    public ThreadLocalHolder(T data) {
        this.dataHolder.set(data);
    }
    public ThreadLocalHolder() {

    }

    public T getLocal() {
        return this.dataHolder.get();
    }

    public void setLocal(T data) {
        this.dataHolder.set(data);
    }

    public void remove() {
        this.dataHolder.remove();
    }
}
