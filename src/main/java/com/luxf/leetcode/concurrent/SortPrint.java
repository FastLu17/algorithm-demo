package com.luxf.leetcode.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 简单的两种方案：方法调用顺序一致, 不会因为多线程混乱
 *
 * @author 小66
 * @date 2020-07-07 13:45
 **/
public class SortPrint {
    private static CountDownLatch second = new CountDownLatch(1);
    private static CountDownLatch third = new CountDownLatch(1);

    /**
     * One获取到 信号后, 在给Tow释放信号, Tow就可以获取到信号、否则由于Tow和Three初始化的permit=0, 导致availablePermits一直为0、
     */
    private static final Semaphore SEMAPHORE_ONE = new Semaphore(1);
    private static final Semaphore SEMAPHORE_TOW = new Semaphore(0);
    private static final Semaphore SEMAPHORE_THREE = new Semaphore(0);

    public SortPrint() {
    }

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.execute(SortPrint::one);
        threadPool.execute(SortPrint::tow);
        threadPool.execute(SortPrint::three);
        threadPool.shutdown();
    }

    private static void one() {
        try {
            SEMAPHORE_ONE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("one");
        second.countDown();
        SEMAPHORE_TOW.release();

    }

    private static void tow() {
        try {
            SEMAPHORE_TOW.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            second.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("tow");
        third.countDown();
        SEMAPHORE_THREE.release();
    }

    private static void three() {
        try {
            SEMAPHORE_THREE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            third.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("three");
    }
}
