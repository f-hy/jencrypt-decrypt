package com.friendy.homework;

import java.util.Arrays;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/10/14 8:49 encrypt-decrypt Lab5<p>
 * <p>
 * <br>
 * https://blog.csdn.net/qq_28205153/article/details/55798628
 * https://www.bilibili.com/video/BV1i341187fK
 * https://blog.csdn.net/qq_45879579/article/details/122421688
 */
public class Lab5 {
    final static int p = 2;
    final static int n = 4;
    final static int primePolynomial = 0b10011;
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
                g[i] ^= primePolynomial;//用到了前面说到的乘法技巧
            }
        }
        // 反表
        for (int i = 0; i < pn; ++i) {
            arc[g[i]] = i;
        }
        // 逆元表
        for (int i = 1; i < pn; ++i) {//0没有逆元，所以从1开始
            inverse_table[i] = g[(pn - 1 - arc[i]) % (pn - 1)];//m_table的取值范围为 [0, pn-2]
        }
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
     *
     * @param a a
     * @param b b
     * @return a+b
     */
    public static int add(int a, int b) {
        return (a ^ b) % pn;
    }

    /**
     * 减法运算
     *
     * @param a a
     * @param b b
     * @return a-b
     */
    public static int subtract(int a, int b) {
        return (a ^ b) % pn;
    }

    /**
     * 乘法运算
     *
     * @param a a
     * @param b b
     * @return axb
     */
    public static int multiply(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return g[(arc[a] + arc[b]) % (pn - 1)];
    }

    /**
     * 除法运算
     *
     * @param a a
     * @param b b
     * @return a/b = axb_inverse
     */
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new RuntimeException("denominator should not be 0!");
        }
        return multiply(a, inverse_table[b]);
    }

    public static final int[] S = {
            0b1001, 0b0100, 0b1010, 0b1011,
            0b1101, 0b0001, 0b1000, 0b0101,
            0b0110, 0b0010, 0b0000, 0b0011,
            0b1100, 0b1110, 0b1111, 0b0111
    };

    public static int[] addRoundKey(int[] data, int[] key) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = data[i] ^ key[i];
        }
        return result;
    }

    public static int[] substituteNibbles(int[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = S[data[i]];
        }
        return result;
    }

    public static int[] shiftRows(int[] data) {//
        int[] result = data.clone();
        int tmp = result[1];
        result[1] = result[3];
        result[3] = tmp;
        return result;
    }

    public static int[] mixColumns(int[] data) {
        int[] result = new int[data.length];
        result[0] = add(data[0], multiply(data[1], 4));
        result[1] = add(data[1], multiply(data[0], 4));
        result[2] = add(data[2], multiply(data[3], 4));
        result[3] = add(data[3], multiply(data[2], 4));
        return result;
    }

    public static int[][] keyExpansion(int[] key) {
        int[][] keys = new int[3][key.length];
        keys[0] = key.clone();

        keys[1][0] = add(S[keys[0][3]], 0b1000) ^ keys[0][0];
        keys[1][1] = S[keys[0][2]] ^ keys[0][1];
        keys[1][2] = keys[0][2] ^ keys[1][0];
        keys[1][3] = keys[0][3] ^ keys[1][1];

        keys[2][0] = add(S[keys[1][3]], 0b0011) ^ keys[1][0];
        keys[2][1] = S[keys[1][2]] ^ keys[1][1];
        keys[2][2] = keys[1][2] ^ keys[2][0];
        keys[2][3] = keys[1][3] ^ keys[2][1];
        return keys;
    }

    public static int[] S_AESen(int[] data, int[] key) {
        int[][] keys = keyExpansion(key);
        printKeys(keys);
        int[] d0 = addRoundKey(data, keys[0]);
        int[] r1 = addRoundKey(mixColumns(shiftRows(substituteNibbles(d0))), keys[1]);
        int[] r2 = addRoundKey(shiftRows(substituteNibbles(r1)), keys[2]);
        return r2;
    }

    public static String toBinaryString(int data) {
        StringBuilder stringBuilder = new StringBuilder();
        int tmp = data;
        for (int i = 0; i < 4; ++i) {
            stringBuilder.append(tmp % 2);
            tmp >>= 1;
        }
        return stringBuilder.reverse().toString();
    }
    public static String toBinaryString(int[] data){
        StringBuilder stringBuilder = new StringBuilder();
        for (int datum : data) {
            stringBuilder.append(toBinaryString(datum) + " ");
        }
        return stringBuilder.toString().substring(0, data.length*5-1);
    }
    public static void printKeys(int[][] data){
        for(int i=0;i<data.length;++i){
            System.out.println("Key" + i + ": " + toBinaryString(data[i]));
        }
    }
    public static void main(String[] args) {
        int[] key = {0b1010, 0b0111, 0b0011, 0b1011};
        int[] data = {0b0110, 0b1111, 0b0110, 0b1011};
        int[] cipher = S_AESen(data, key);
        System.out.println("Cipher: " + toBinaryString(cipher));
    }
}
