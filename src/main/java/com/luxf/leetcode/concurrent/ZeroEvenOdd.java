package com.luxf.leetcode.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * 能用信号量解决的问题, 都可以使用Lock和Condition模型解决、
 *
 * @author 小66
 * @date 2020-07-07 15:52
 **/
public class ZeroEvenOdd {
    private static final IntConsumer CONSUMER = System.out::print;
    private final static Semaphore zeroSemaphore = new Semaphore(1);
    private final static Semaphore evenSemaphore = new Semaphore(0);
    private final static Semaphore oddSemaphore = new Semaphore(0);
    private static final int SIZE = 5;

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();
    private static final Condition EVEN_COND = REENTRANT_LOCK.newCondition();
    private static final Condition ODD_COND = REENTRANT_LOCK.newCondition();
    private static final Condition ZERO_COND = REENTRANT_LOCK.newCondition();
    private volatile static int flag = 0;


    /**
     * 输入：SIZE = 2
     * 输出："0102"
     * 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"。
     */
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
//        threadPool.execute(ZeroEvenOdd::zero);
//        threadPool.execute(ZeroEvenOdd::even);
//        threadPool.execute(ZeroEvenOdd::odd);

        threadPool.execute(ZeroEvenOdd::zeroByLock);
        threadPool.execute(ZeroEvenOdd::evenByLock);
        threadPool.execute(ZeroEvenOdd::oddByLock);
        threadPool.shutdown();
    }

    public static void zero() {
        for (int i = 1; i <= SIZE; i++) {
            try {
                zeroSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CONSUMER.accept(0);
            if (i % 2 == 0) {
                evenSemaphore.release();
            } else {
                oddSemaphore.release();
            }
        }
    }

    public static void even() {
        for (int i = 2; i <= SIZE; i += 2) {
            try {
                evenSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CONSUMER.accept(i);
            zeroSemaphore.release();
        }
    }

    public static void odd() {
        for (int i = 1; i <= SIZE; i += 2) {
            try {
                oddSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CONSUMER.accept(i);
            zeroSemaphore.release();
        }
    }

    public static void zeroByLock() {
        for (int i = 1; i <= SIZE; i++) {
            REENTRANT_LOCK.lock();
            try {
                while (flag != 0) {
                    try {
                        ZERO_COND.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONSUMER.accept(0);
                if (i % 2 == 0) {
                    flag = 2;
                    EVEN_COND.signal();
                } else {
                    flag = 1;
                    ODD_COND.signal();
                }
            } finally {
                REENTRANT_LOCK.unlock();
            }
        }
    }

    public static void evenByLock() {
        for (int i = 2; i <= SIZE; i += 2) {
            REENTRANT_LOCK.lock();
            try {
                while (flag != 2) {
                    try {
                        EVEN_COND.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONSUMER.accept(i);
                flag = 0;
                ZERO_COND.signal();
            } finally {
                REENTRANT_LOCK.unlock();
            }
        }
    }


    public static void oddByLock() {
        for (int i = 1; i <= SIZE; i += 2) {
            REENTRANT_LOCK.lock();
            try {
                while (flag != 1) {
                    try {
                        ODD_COND.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONSUMER.accept(i);
                flag = 0;
                ZERO_COND.signal();
            } finally {
                REENTRANT_LOCK.unlock();
            }
        }
    }
}