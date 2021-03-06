package com.concurrent.thread.main.test;

/**
 * @author dxw
 * @create 2021-02-20 16:36
 * 线程的生命周期及五种基本状态
 * new runnable running dead blocked
 * 阻塞的情况分三种：
 * (一). 等待阻塞：运行状态中的线程执行 wait()方法，JVM会把该线程放入等待队列(waitting queue)中，使本线程进入到等待阻塞状态；
 * (二). 同步阻塞：线程在获取 synchronized 同步锁失败(因为锁被其它线程所占用)，，则JVM会把该线程放入锁池(lock pool)中，线程会进入同步阻塞状态；
 * (三). 其他阻塞: 通过调用线程的 sleep()或 join()或发出了 I/O 请求时，线程会进入到阻塞状态。当 sleep()状态超时、join()等待线程终止或者超时、或者 I/O 处理完毕时，线程重新转入就绪状态。
 */
public class ThreadLifeCycle {
    /**
     * 线程abc顺序执行
     */
    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("a");
        }, "a");
        Thread b = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("b");
        }, "b");
        Thread c = new Thread(() -> {
            System.out.println("c");
        }, "c");
        a.start();
//        尽可能保证join时 线程a已start,处于alive状态
        a.join();

        b.start();
        b.join();

        c.start();
    }

    /**
     * join方法原理:main方法中执行a.join(),synchronized加锁，a为锁对象，只要a是alive状态，main线程就阻塞在这里
     * 线程a作为锁对象，当线程a执行完毕，jvm会notify被a阻塞住的所有线程
     * 因此join之前，尽肯能保证a已start(),否则main方法不会阻塞
     * 用while而不用if,保证main线程被唤醒时，a线程生命周期已结束
     */
    public final synchronized void join() throws InterruptedException {
//        while (isAlive()) {
//            wait(0);
//        }
    }
}

/**
 * wait()，notify()的使用方式
 */
class WaitNotifyDemo {
    /**
     * 改变信号，唤醒指定线程
     */
    private static int signal = 1;
    public static void main(String[] args) {
        WaitNotifyDemo waitNotifyDemo = new WaitNotifyDemo();
        new Thread(() -> {
            synchronized (waitNotifyDemo) {
                for (int i = 0; i < 10; i++) {
//                    用while保证被唤醒时信号也正确
                    while (signal != 1) {
                        try {
                            waitNotifyDemo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("a");
//                    改变信号，唤醒指定线程
                    signal = 2;
//                    唤醒所有；这里用notify()不能精确唤醒
                    waitNotifyDemo.notifyAll();
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (waitNotifyDemo) {
                for (int i = 0; i < 10; i++) {
                    while (signal != 3) {
                        try {
                            waitNotifyDemo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("c ");
                    signal = 1;
                    waitNotifyDemo.notifyAll();
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (waitNotifyDemo) {
                for (int i = 0; i < 10; i++) {
                    while (signal != 2) {
                        try {
                            waitNotifyDemo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("b");
                    signal = 3;
                    waitNotifyDemo.notifyAll();
                }
            }
        }).start();
    }
}
