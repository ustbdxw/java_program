package com.concurrent.thread.main.test;


import java.util.concurrent.*;

/**
 * @author dxw
 * @create 2021-02-25 17:01
 */
public class ThreadPoolTest {

    /**
     * SynchronousQueue put线程执行queue.put(1) 后就被阻塞了，只有take线程进行了消费，put线程才可以返回。可以认为这是一种线程与线程间一对一传递消息的模型。
     * 缓存线程池没有核心线程，最大线程数为无限个  存活时间是60s;队列是同步队列，
     */
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
    /**
     * 调度线程池
     */
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(6);
//    ScheduledExecutorService scheduledThreadPool1 =  new ThreadPoolExecutor(6,Integer.MAX_VALUE,
//                           10L,TimeUnit.SECONDS,
//               new DelayedWorkQueue());
    /**
     * 核心线程和最大线程数都是设置的线程数量，因此这里keepAliveTime可以为0，核心线程数不会被回收，任务可能积压在阻塞队列中
     */
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
    ThreadPoolExecutor fixedExecutor = new ThreadPoolExecutor(6, 6, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {

        // 创建一个可重用固定线程数的线程池
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

        // 创建线程
        Thread t1 = new MyThread();
        Thread t2 = new MyThread();
        Thread t3 = new MyThread();
        Thread t4 = new MyThread();
        Thread t5 = new MyThread();
        // 将线程放入池中进行执行
        fixedThreadPool.execute(t1);
//        fixedThreadPool.execute(t2);
//        fixedThreadPool.execute(t3);
//        fixedThreadPool.execute(t4);
//        fixedThreadPool.execute(t5);
        // 将线程放入池中进行执行
//        cachedThreadPool.execute(t1);
//        cachedThreadPool.execute(t2);
//        cachedThreadPool.execute(t3);
//        cachedThreadPool.execute(t4);
//        cachedThreadPool.execute(t5);
        // 关闭线程池
//        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();

        scheduledThreadPool.scheduleWithFixedDelay(t1, 10, 1, TimeUnit.SECONDS);
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
    }
}
