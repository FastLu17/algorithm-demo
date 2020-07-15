package com.luxf.leetcode.algorithm;

/**
 * 欧几里得算法、取最大公约数
 *
 * @author 小66
 * @date 2020-07-10 13:22
 **/
public class GcdAlgorithm {

    public static void main(String[] args) {
        int gcd1 = gcd(2, 8);
        int gcd2 = gcd(1230, 35);
        System.out.println("gcd1 = " + gcd1);
        System.out.println("gcd2 = " + gcd2);

        // 左移 <<：乘以2的n次方, 如：3<<2 == 12
        // 右移 >>：除以2的n次方, 如：16>>2 == 4
        // 00000011：3
        // 00000100：4
        // 00000111：7  异或运算符 ^：相应位的值不同的则取1、相同的则取0。
        // 00000000：0  与运算符 &：相应位的值, 2个都是'1',则取1、否则取0。
        // 00000111：7  或运算符 |：相应位的值, 2个都是'0',则取0、否则取1。
        int i = 3 ^ (3 >>> 16);
        System.out.println("i = " + Integer.toBinaryString(i));
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            // 如果 b > a, 则 mod = a;
            int mod = a % b;
            a = b;
            b = mod;
        }
        return a;
    }
}
