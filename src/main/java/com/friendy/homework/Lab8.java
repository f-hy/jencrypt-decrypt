package com.friendy.homework;

import com.friendy.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/12/2 9:13 encrypt-decrypt Lab8<p>
 * <p>
 * <br>
 */
public class Lab8 {
    static int q = 71;
    static int alpha = 7;

    public static void main(String[] args) {
        int YB = 3, k = 2, M = 30;
        int[] encrypt = encrypt(YB, k, M);
        System.out.println("（a）若B的公钥YB=3，A选择的随机整数k=2，则M=30的密文是 " + Arrays.toString(encrypt));
        ArrayList<Integer> dict = CommonUtil.pow_modulus(alpha, q);
        int C1 = 59;
        k = dict.indexOf(C1);
        System.out.println(dict);
        int K = CommonUtil.pow_modulus(YB, k, q);
        int C2 = K * M % q;
        System.out.println("（b）若A选择的k值使得M=30的密文为C=(59,C2)，则整数C2是 " + C2);
    }

    private static int[] encrypt(int Y, int k, int M) {
        int C1 = CommonUtil.pow_modulus(alpha, k, q);
        int K = CommonUtil.pow_modulus(Y, k, q);
        int C2 = (K * M) % q;
        return new int[]{C1, C2};
    }

    private static int decrypt(int[] C, int Y) {
        int K = CommonUtil.pow_modulus(C[0], Y, q);
        return (C[1] * CommonUtil.getInverse(K, q)) % q;
    }
}
