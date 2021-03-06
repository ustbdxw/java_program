package com.concurrent.thread.main.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dxw
 * @create 2021-02-25 16:59
 * 场景：生产者消费者
 * 阻塞队列原理：
 *
 * 队列方法总结
 * 方法类型	抛异常	    特殊值	    阻塞	超时
 * 插入方法	add(o)	    offer(o)	put(o)	offer(o, timeout, timeunit)
 * 移除方法	remove(o)	poll()	    take()	poll(timeout, timeunit)
 * 检查方法	element()	peek()	    不可用	不可用
 */
public class QueueTest {
    private int i = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
//        produceConsume1();
        produceConsume2();
    }

    /**
     * 传统的生产消费者模式 A线程生产，B线程消费
     */
    private static void produceConsume1() {
        QueueTest queueTest = new QueueTest();
        Thread t1 = new Thread(() -> {
            while (true) {
                queueTest.produce();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            while (true) {
                queueTest.consume();
            }
        }, "t2");
        t2.start();
        t1.start();
    }

    /**
     * 生产流程：i=0就开始生产，生产完阻塞，毕通知其他线程消费
     */
    private void produce() {
        lock.lock();
        try {
            while (i > 0) {
                condition.await();
            }
            i++;
            System.out.println(Thread.currentThread().getName() + "生产," + "此时库存：" + i);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费流程：i>0就开始消费，消费完毕若i=0，阻塞，通知其他线程生产
     */
    private void consume() {
        lock.lock();
        try {
            while (i == 0) {
                condition.await();
            }
            i--;
            System.out.println(Thread.currentThread().getName() + "消费，" + "此时库存：" + i);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 阻塞队列生产消费者模式
     */
    private static void produceConsume2() throws InterruptedException {
        QueueTest queueTest = new QueueTest();
        Thread t1 = new Thread(() -> {
            try {
                queueTest.queueProduce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                queueTest.queueConsume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(50);
        queueTest.flag = false;
        t1.interrupt();
        t2.interrupt();
        System.out.println("生产消费结束");
    }

    /**
     * 阻塞队列生产
     */
    private void queueProduce() throws InterruptedException {
        while (flag) {
            int data = atomicInteger.getAndIncrement();
            queue.put(data);
            System.out.println("插入队列成功:" + data);
        }
    }

    /**
     * 阻塞队列消费
     */
    private void queueConsume() throws InterruptedException {
        while (flag) {
            Integer take = queue.take();
            System.out.println("消费者取到数据" + take);
            Thread.sleep(10);
        }
    }


}
