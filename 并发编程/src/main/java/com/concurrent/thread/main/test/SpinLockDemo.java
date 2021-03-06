package com.concurrent.thread.main.test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dxw
 * @create 2021-02-25 15:39
 * 实现自旋锁：好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU。
 * 建立AtomicReference
 * 加锁，cas将null引用换为当前线程，成功则加锁，失败则自旋
 * 解锁：cas将当前线程换为null
 *
 * 自旋替换为当前线程的好处：只有自己才能解锁，避免线程a加的锁被线程b释放掉
 */
public class SpinLockDemo {
    private static int i = 0;
    private AtomicReference<Thread> atomicReference = new AtomicReference();

    private void lock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    private void unLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
    }

    public static void main(String[] args) throws InterruptedException {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.lock();
            for (int k = 0; k < 1000; k++) {
                i++;
                Thread.yield();
            }
            spinLockDemo.unLock();
        }).start();
        new Thread(() -> {
            spinLockDemo.lock();
            for (int k = 0; k < 1000; k++) {
                i++;
                Thread.yield();
            }
            spinLockDemo.unLock();
        }).start();
        Thread.sleep(500);
        System.out.println(i);
    }
}
