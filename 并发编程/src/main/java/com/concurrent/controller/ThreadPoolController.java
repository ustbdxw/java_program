package com.concurrent.controller;

import com.concurrent.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author dxw
 * @create 2021-03-02 11:11
 */
@RestController
public class ThreadPoolController {

    public static Random random = new Random();
    /**
     * 线程池 taskExecutor
     */
    @Resource(name = "taskExecutor")
    TaskExecutor taskExecutor;
    @Autowired
    private TestService testService;
//2）使用线程池

    @GetMapping("/test")
    public void testAsync() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        test();
        test1();
        test2();
        System.out.println("欧了");
    }

    /**
     * @throws InterruptedException
     * @Async 所修饰的函数不要定义为static类型，这样异步调用不会生效
     */
    @Async("taskExecutor")
    public void test() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "开始做任务一");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
    }

    @Async("taskExecutor")
    public void test1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "开始做任务二");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
    }

    @Async("taskExecutor")
    public void test2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "开始做任务三");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(1000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
    }


}
