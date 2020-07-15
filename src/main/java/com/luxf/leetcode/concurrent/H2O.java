package com.luxf.leetcode.concurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 使用{@link Semaphore}实现是最简洁的方式、单独只使用{@link CyclicBarrier}不太容易实现功能、
 *
 * @author 小66
 * @date 2020-07-08 13:39
 **/
public class H2O {
    private static final CyclicBarrier COMMON_BARRIER = new CyclicBarrier(3);
    private static final Semaphore HYDROGEN_SEMAPHORE = new Semaphore(2);
    /**
     * TODO: 注意具体获取和释放信号数量的灵活运用！
     * 由于此处是 一个O对应两个H, 因此 OXYGEN_SEMAPHORE.acquire(2) 和 HYDROGEN_SEMAPHORE.release(2)、
     */
    private static final Semaphore OXYGEN_SEMAPHORE = new Semaphore(2);

    /**
     * 输入字符串的总长将会是 3n, 1 ≤ n ≤ 50；
     * 输入字符串中的 “H” 总数将会是 2n。
     * 输入字符串中的 “O” 总数将会是 n。
     * <p>
     * 输入: "OOHHHH"  --> 打印的日志：连续三三成组合产生一个水分子、
     * 输出: "HHOHHO"
     * 解释: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" 和 "OHHOHH" 依然都是有效解。
     *
     * @param args
     */
    public static void main(String[] args) {
        String str = "HHHHHHHHOOOO";
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            String valueOf = String.valueOf(charAt);
            if ("H".equals(valueOf)) {
                threadPool.execute(H2O::hydrogenBySemaphore);
            } else {
                threadPool.execute(H2O::oxygenBySemaphore);
            }
        }
        threadPool.shutdown();
    }

    public static void hydrogenBySemaphore() {
        try {
            HYDROGEN_SEMAPHORE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("H");
        // 注意：需要先输出、再释放！否则顺序会混乱
        OXYGEN_SEMAPHORE.release();
    }

    public static void oxygenBySemaphore() {
        try {
            // 注意：此处是获取到2个信号才会执行、
            OXYGEN_SEMAPHORE.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("O");
        // 注意：需要先输出、再释放！否则顺序会混乱
        HYDROGEN_SEMAPHORE.release(2);
    }

    /**
     * 同时使用{@link CyclicBarrier}和{@link Semaphore}--> 不够简洁。 都已经使用了2个Semaphore
     *
     * @throws InterruptedException
     */
    public static void hydrogen() throws InterruptedException {
        HYDROGEN_SEMAPHORE.acquire();
        COMMON_BARRIER.wait();
        System.out.print("H");
        HYDROGEN_SEMAPHORE.release();
    }

    public static void oxygen() throws InterruptedException {
        OXYGEN_SEMAPHORE.acquire(2);
        COMMON_BARRIER.wait();
        System.out.print("O");
        OXYGEN_SEMAPHORE.release(2);
    }
}
