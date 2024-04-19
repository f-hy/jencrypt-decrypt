package com.friendy.homework;

import java.util.Arrays;
import java.util.HashMap;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/10/8 11:47 encrypt-decrypt Lab4<p>
 * <p>
 * <br>
 * 伽罗华域（Galois Field）上的四则运算
 * https://blog.csdn.net/luotuo44/article/details/41645597
 */
public class Lab4 {
    final static int p = 2;
    final static int n = 4;
    final static HashMap<Integer, Integer> primePolynomial = new HashMap<Integer, Integer>() {{
        put(3, 0b1011);
        put(4, 0b10011);
        put(8, 0b100011011);
    }};

    static int pn = pow(p, n);
    static int[] g = new int[pn]; // 正表
    static int[] arc = new int[pn]; // 反表
    static int[] inverse_table = new int[pn]; // 逆元表


    static {
        // 正表
        g[0] = 1;//g^0
        for (int i = 1; i < pn - 1; ++i) {//生成元为x + 1
            //下面是m_table[i] = m_table[i-1] * (x + 1)的简写形式
            g[i] = g[i - 1] << 1;
            if ((g[i] & pn) != 0) {//最高指数已经到了n，需要模上m(x)
                g[i] ^= primePolynomial.get(n);//用到了前面说到的乘法技巧
            }
        }
        System.out.print("table:");
        System.out.println(Arrays.toString(g));
        // 反表
        for (int i = 0; i < pn; ++i) {
            arc[g[i]] = i;
        }
        System.out.print("arc_table:");
        System.out.println(Arrays.toString(arc));
        // 逆元表
        for (int i = 1; i < pn; ++i) {//0没有逆元，所以从1开始
            inverse_table[i] = g[(pn - 1 - arc[i])%(pn - 1)];//m_table的取值范围为 [0, pn-2]
        }
        System.out.print("inverse_table:");
        System.out.println(Arrays.toString(inverse_table));
    }

    private static int pow(int a, int b) {
        int res = a;
        for (int i = 1; i < b; ++i) {
            res *= a;
        }
        return res;
    }

    /**
     * 加法运算
     * @param a a
     * @param b b
     * @return a+b
     */
    public static int add(int a, int b) {
        return (a ^ b) % pn;
    }

    /**
     * 减法运算
     * @param a a
     * @param b b
     * @return a-b
     */
    public static int subtract(int a, int b) {
        return (a ^ b) % pn;
    }

    /**
     * 乘法运算
     * @param a a
     * @param b b
     * @return axb
     */
    public static int multiply(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        System.out.println("arc_table[a] = " + arc[a]);
        System.out.println("arc_table[b] = " + arc[b]);
        return g[(arc[a] + arc[b]) % (pn - 1)];
    }

    /**
     * 除法运算
     * @param a a
     * @param b b
     * @return a/b = axb_inverse
     */
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new RuntimeException("denominator should not be 0!");
        }
        System.out.println("inverse_table[b] = " + inverse_table[b]);
        return multiply(a, inverse_table[b]);
    }


    public static void main(String[] args) {
        System.out.println("add(0b1100, 0b1010) = " + add(0b1100, 0b1010));
        System.out.println("subtract(0b1100, 0b1010) = " + subtract(0b1100, 0b1010));
        System.out.println("multiply(0b0111, 0b1001) = " + multiply(0b0111, 0b1001));
        System.out.println("divide(0b1101, 0b1011) = " + divide(0b1101, 0b1011));
    }
}
