package com.concurrent.thread.main.test;

/**
 * @author dxw
 * @create 2021-03-09 10:59
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        ThreadLocal<Integer> threadLocal = new ThreadLocal(){
            @Override
            protected Object initialValue() {
                return 0;
            }
        };
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                Integer init = threadLocal.get();
                for (int j = 0; j < 100; j++) {
                    init+=1;
                }
                threadLocal.set(init);
                System.out.println(Thread.currentThread().getName() + "get " + threadLocal.get());
            }).start();
        }
    }
}
