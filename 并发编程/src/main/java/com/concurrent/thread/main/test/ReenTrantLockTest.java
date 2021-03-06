package com.concurrent.thread.main.test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dxw
 * @create 2021-02-25 11:47
 * ReenTrantLock和Synchronized主要区别：
 */
public class ReenTrantLockTest {
    private static int i = 0;

    private void add() {
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        ReenTrantLockTest lockTest = new ReenTrantLockTest();
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            for (int j = 0; j < 1000; j++) {
                lockTest.add();
                Thread.yield();
            }
            lock.unlock();
        }).start();
        new Thread(() -> {
            lock.lock();
            for (int j = 0; j < 1000; j++) {
                lockTest.add();
                Thread.yield();
            }
            lock.unlock();
        }).start();

        Thread.sleep(500);
        System.out.println(i);
    }
}
