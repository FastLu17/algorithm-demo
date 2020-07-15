package com.luxf.leetcode.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO: 利用{@link AtomicInteger}的特性, 完美的按序输出!  注意：不要嵌套使用AtomicInteger, 线程不安全！
 *      while (INCREMENT_INTEGER.get() <= 2 * a) {
 *          // 多加一个条件(INCREMENT_INTEGER.get() <= 2)也是线程不安全的、
 *          while (INCREMENT_INTEGER.get() <= 2 * a && INCREMENT_INTEGER.get() % 2 == 1) {
 *              System.out.println("INNER = " + INCREMENT_INTEGER.get());
 *              System.out.print("hello");
 *              INCREMENT_INTEGER.incrementAndGet();
 *          }
 *      }
 *
 * <p>
 * 编写一个可以从 1 到 n 输出代表这个数字的字符串的程序，但是：
 * <p>
 * 如果这个数字可以被 3 整除，输出 "fizz"。
 * 如果这个数字可以被 5 整除，输出 "buzz"。
 * 如果这个数字可以同时被 3 和 5 整除，输出 "fizzbuzz"。
 * 例如，当 n = 15，输出： 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz。
 *
 * @author 小66
 * @date 2020-07-08 15:54
 **/
public class FizzBuzz {
    private static final int LENGTH = 20;
    private volatile static AtomicInteger current = new AtomicInteger(1);

    /**
     * 使用ReentrantLock和Condition不是明智的选择
     */
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();
    private static final Condition FIZZ_COND = REENTRANT_LOCK.newCondition();
    private static final Condition BUZZ_COND = REENTRANT_LOCK.newCondition();
    private static final Condition FIZZ_BUZZ_COND = REENTRANT_LOCK.newCondition();
    private static final Condition NUMBER_COND = REENTRANT_LOCK.newCondition();

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        threadPool.execute(FizzBuzz::fizz);
        threadPool.execute(FizzBuzz::buzz);
        threadPool.execute(FizzBuzz::fizzbuzz);
        threadPool.execute(FizzBuzz::number);
        threadPool.shutdown();
    }

//    public static void fizz() {
//        while (current.get() <= LENGTH) {
//            REENTRANT_LOCK.lock();
//            try {
//                while (!(current.get() % 3 == 0 && current.get() % 5 != 0)) {
//                    FIZZ_COND.await();
//                }
//                print("fizz");
//                signalNext(current.incrementAndGet());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                REENTRANT_LOCK.unlock();
//            }
//        }
//    }
//
//    private static void signalNext(int next) {
//        if (next > LENGTH) {
//            return;
//        }
//        if (next % 3 == 0 && next % 5 != 0) {
//            FIZZ_COND.signal();
//        } else if (next % 3 != 0 && next % 5 == 0) {
//            BUZZ_COND.signal();
//        } else if (next % 3 == 0 && next % 5 == 0) {
//            FIZZ_BUZZ_COND.signal();
//        } else {
//            NUMBER_COND.signal();
//        }
//    }
//
//    public static void buzz() {
//        while (current.get() <= LENGTH) {
//            REENTRANT_LOCK.lock();
//            try {
//                while (!(current.get() % 3 != 0 && current.get() % 5 == 0)) {
//                    BUZZ_COND.await();
//                }
//                print("buzz");
//                signalNext(current.incrementAndGet());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                REENTRANT_LOCK.unlock();
//            }
//        }
//    }
//
//    public static void fizzbuzz() {
//        while (current.get() <= LENGTH) {
//            REENTRANT_LOCK.lock();
//            try {
//                while (!(current.get() % 3 == 0 && current.get() % 5 == 0)) {
//                    FIZZ_BUZZ_COND.await();
//                }
//                print("fizzbuzz");
//                signalNext(current.incrementAndGet());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                REENTRANT_LOCK.unlock();
//            }
//        }
//    }
//
//    public static void number() {
//        while (current.get() <= LENGTH) {
//            REENTRANT_LOCK.lock();
//            try {
//                while (!(current.get() % 3 != 0 && current.get() % 5 != 0)) {
//                    NUMBER_COND.await();
//                }
//                print(current.get());
//                signalNext(current.incrementAndGet());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                REENTRANT_LOCK.unlock();
//            }
//        }
//    }

    public static void fizz() {
        while (current.get() <= LENGTH) {
            int i = current.get();
            if (i % 3 == 0 && i % 5 != 0) {
                print("fizz");
                current.getAndIncrement();
            }
        }
    }

    public static void buzz() {
        while (current.get() <= LENGTH) {
            int i = current.get();
            if (i % 5 == 0 && i % 3 != 0) {
                print("buzz");
                current.getAndIncrement();
            }
        }
    }

    public static void fizzbuzz() {
        while (current.get() <= LENGTH) {
            int i = current.get();
            if (i % 15 == 0) {
                print("buzz");
                current.getAndIncrement();
            }
        }
    }

    public static void number() {
        while (current.get() <= LENGTH) {
            int i = current.get();
            if (i % 3 != 0 && i % 5 != 0) {
                print(i);
                current.getAndIncrement();
            }
        }
    }

    private static void print(Object obj) {
        if (current.get() == LENGTH) {
            System.out.print(obj);
        } else {
            System.out.print(obj + ",");
        }
    }
}
