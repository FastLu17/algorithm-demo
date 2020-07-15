package com.luxf.leetcode.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 小66
 * @date 2020-07-07 14:53
 **/
public class IntervalPrint {

    private final static Semaphore fooSemaphore = new Semaphore(1);
    /**
     * 初始化为0、确保 barSemaphore 的线程在 fooSemaphore 线程释放了barSemaphore的信号之后才能获取到、 确保了对应的顺序
     */
    private final static Semaphore barSemaphore = new Semaphore(0);
    private static int a = 4;

    private static volatile AtomicInteger INCREMENT_INTEGER = new AtomicInteger(1);

    public static void main(String[] args) {
        method();
//        ExecutorService threadPool = Executors.newCachedThreadPool();
//        for (int i = 0; i < a; i++) {
//            threadPool.execute(() -> {
//                try {
//                    fooSemaphore.acquire();
//                    printHello();
//                    int permits = barSemaphore.availablePermits();
//                    System.out.println("permits = " + permits);
//                    barSemaphore.release();
//                    int availablePermits = barSemaphore.availablePermits();
//                    System.out.println("availablePermits = " + availablePermits);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            });
//        }
//
//        for (int i = 0; i < a; i++) {
//            threadPool.execute(() -> {
//                try {
//                    barSemaphore.acquire();
//                    printWorld();
//                    fooSemaphore.release();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            });
//        }
//        threadPool.shutdown();
    }

    private static void printWorld() {
        System.out.print("world, ");
    }

    private static void printHello() {
        System.out.print("hello");
    }

    public static void method() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.execute(IntervalPrint::printHelloByAtomic);
        threadPool.execute(IntervalPrint::printWorldByAtomic);
        threadPool.shutdown();
    }

    private static void printWorldByAtomic() {
        while (INCREMENT_INTEGER.get() <= 2 * a) {
            while (INCREMENT_INTEGER.get() <= 2 * a && INCREMENT_INTEGER.get() % 2 == 0) {
                System.out.print("world, ");
                INCREMENT_INTEGER.incrementAndGet();
            }
        }
    }

    /**
     * TODO: 如果使用 'while (INCREMENT_INTEGER.get() % 2 == 1) ', 此处使用了2次 INCREMENT_INTEGER.get(), 第二次的时候, 线程不安全！
     */
    private static void printHelloByAtomic() {
        while (INCREMENT_INTEGER.get() <= 2 * a) {
            // 多加一个条件也是线程不安全的、
            while (INCREMENT_INTEGER.get() <= 2 * a && INCREMENT_INTEGER.get() % 2 == 1) {
                System.out.println("INNER = " + INCREMENT_INTEGER.get());
                System.out.print("hello");
                INCREMENT_INTEGER.incrementAndGet();
            }
        }
    }
}
