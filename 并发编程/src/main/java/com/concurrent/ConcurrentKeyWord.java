package com.concurrent;

/**
 * @author dxw
 * @create 2021-02-20 11:02
 * 并发编程三要素：原子性、可见性、有序性
 */
public class ConcurrentKeyWord {
    private volatile int i = 0;

    /**
     * 非线程安全
     */
    private void add() {
        i++;
    }

    /**
     * 线程安全:对象锁
     */
    private synchronized void sycAdd() {
        i++;
    }

    /**
     * 局部变量不考虑线程安全
     */
    private void nativeAdd() {
        int j = 0;
        j++;
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
        test2();
    }

    /**
     * 测试i++非原子操作
     */
    private static void test1() throws InterruptedException {
        ConcurrentKeyWord concurrentKeyWord = new ConcurrentKeyWord();
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                concurrentKeyWord.add();
//                让出cpu执行权，让其它线程执行，增加不可见几率
                Thread.yield();
            }
        }).start();
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                concurrentKeyWord.add();
                Thread.yield();
            }
        }).start();
//        保证线程全部执行完毕再打印
        Thread.sleep(200);
        System.out.println(concurrentKeyWord.i);
    }

    /**
     * 测试synchronized保证原子性和可见性
     */
    private static void test2() throws InterruptedException {
        ConcurrentKeyWord concurrentKeyWord = new ConcurrentKeyWord();
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                concurrentKeyWord.sycAdd();
//                即使让出cpu执行权，其他线程没拿到锁也执行不了，这里不起作用
                Thread.yield();
            }
        }).start();
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                concurrentKeyWord.sycAdd();
                Thread.yield();
            }
        }).start();
        Thread.sleep(200);
        System.out.println(concurrentKeyWord.i);
    }
}
