package com.concurrent.thread.main.test;

/**
 * @author dxw
 * @create 2021-02-20 11:02
 * 并发编程三要素：原子性、可见性、有序性
 * 出现线程安全问题的原因：
 * 1.线程切换带来的原子性问题
 * 2.缓存导致的可见性问题
 * 3.编译优化带来的有序性问题
 * <p>
 * 解决办法：
 * JDK Atomic开头的原子类、synchronized、LOCK，可以解决原子性问题
 * synchronized、volatile、LOCK，可以解决可见性问题
 * Happens-Before 规则可以解决有序性问题
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
//                让出cpu执行权，让其它线程执行,不一定成功
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

class Volatile {
    private int i = 0;

    /**
     * 这里的synchronized，可以保证读写互斥，也就是读到最新值
     */
    private synchronized void setI() {
        i = 10086;
    }
    private synchronized int getI(){
        return i;
    }

    public static void main(String[] args) {
        Volatile aVolatile = new Volatile();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---" + aVolatile.getI());
            try {
                Thread.sleep(3000);
                aVolatile.setI();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        while (aVolatile.getI() == 0){
            //循环
        }
    }

}
class Volatile1 {
    /**
     * 这里不加volatile会一直循环
     */
    private volatile int i = 0;

    public static void main(String[] args) {
        Volatile1 aVolatile = new Volatile1();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---" + aVolatile.i);
            try {
                Thread.sleep(3000);
                aVolatile.i = 10086;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        while (aVolatile.i == 0){
            //循环
        }
    }
}
