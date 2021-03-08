package com.concurrent.thread.main.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author dxw
 * @create 2021-02-25 16:15
 * 计数器锁 阻塞主线程
 * 例子：一个教室有1个班长和10个学生，班长要等所有学生都走了才能关门，那么要如何实现。
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 10个学生
         */
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int num = i;
            new Thread(() -> {
                System.out.println("学生" + num + "放学了");
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("班长锁门");
    }
}

/**
 * 循环屏障；阻塞子线程
 * 十人排位，等待所有人加载完毕后比赛开始
 * CountDownLatch 和 CyclicBarrier 其实是相反的操作，一个是相减到0开始执行，一个是相加到指定值开始执行
 */
class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10,()->{
            System.out.println("排位开始");
        });
        for (int i = 0; i < 10; i++) {
            int num = i;
            new Thread(()->{
                System.out.println("玩家"+num+"准备就绪");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}

/**
 * 信号量，用于共享资源的相互排斥使用 ，用于并发资源数的控制。
 * 抢车位问题，此时有六部车辆，但是只有三个车位的问题。
 */
class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"停车2秒后离开车位");
                semaphore.release();
            }).start();
        }
    }
}