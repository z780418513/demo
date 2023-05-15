package com.hb.juc.test;

import org.junit.Test;

import java.lang.ref.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/15
 */
public class ReferenceDemo {


    /**
     * 强引用 会被gc回收
     * @throws InterruptedException
     */
    @Test
    public void strongReference() throws InterruptedException {
        MyObject myObject = new MyObject();
        // 将myObject至为空，就可以被gc清理了
        myObject = null;
        // 触发gc
        System.gc();
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 弱引用，当内存不足的时候，会被gc回收，内存充足，不会被回收
     * @throws InterruptedException
     */
    @Test
    public void softReference() throws InterruptedException {
        MyObject myObject = new MyObject();
        SoftReference<MyObject> softRef = new SoftReference<>(myObject);     // 软引用
        myObject = null;
        // 触发gc
        System.gc();
        // 创建20mb内存，用于强制触发gc
        byte[] bytes = new byte[1024 * 1024 * 20];
        TimeUnit.SECONDS.sleep(2);

    }

    /**
     * 软引用，只要gc，就会被回收
     * @throws InterruptedException
     */
    @Test
    public void weakReference() throws InterruptedException {
        MyObject myObject = new MyObject();
        WeakReference<MyObject> weakReference = new WeakReference<>(myObject);
        myObject = null;
        // 触发gc
        System.gc();
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 虚引用”顾名思义，就是形同虚设，与其他几种引用都不同，
     * 虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。
     *
     * 虚引用必须和引用队列 （ReferenceQueue）联合使用，
     * 当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之 关联的引用队列中。
     * @throws InterruptedException
     */
    @Test
    public void weakReference1() throws InterruptedException {
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject,referenceQueue);
        myObject = null;

        Reference<? extends MyObject> reference = referenceQueue.poll();
        new Thread(()->{
            while (true){
                if (reference != null){
                    System.out.println(reference);
                }
            }
        }).start();

        new Thread(()->{
            for (;;){
                byte[] bytes = new byte[1024 * 1024 * 1000];
            }
        }).start();
        TimeUnit.SECONDS.sleep(20);
    }


}


class MyObject {
    // 一般情况下不会去重写它，这边仅仅只是为了实验
    @Override
    protected void finalize() {
        System.out.println("my object finalize ....");
    }
}
